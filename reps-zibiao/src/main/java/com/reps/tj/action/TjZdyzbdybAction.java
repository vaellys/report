package com.reps.tj.action;

import static com.reps.tj.enums.Meta.*;
import static com.reps.tj.util.MetaManager.getMetaDatasFromSession;
import static com.reps.tj.util.MetaManager.getValuesFromSession;
import static com.reps.tj.util.MetaManager.removeMetaDatasFromSession;
import static com.reps.tj.util.MetaManager.setValuesToSession;
import static com.reps.tj.util.MetaJsonParse.*;
import static com.reps.tj.util.SelectTransformUtil.transSelectOptionStrFromMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.reps.core.RepsConstant;
import com.reps.core.commons.Pagination;
import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.IDGenerator;
import com.reps.core.util.StringUtil;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.tj.entity.DatabaseType;
import com.reps.tj.entity.OutputFieldDefined;
import com.reps.tj.entity.ParamDefined;
import com.reps.tj.entity.TjZbztmcdyb;
import com.reps.tj.entity.TjZdyzbdyb;
import com.reps.tj.service.ITjZbztmcdybService;
import com.reps.tj.service.ITjZdyzbdybService;

import net.sf.json.JSONObject;

/**
 * 指标相关操作
 * 
 * @author qianguobing
 * @date 2017年8月8日 下午3:46:48
 */
@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/report/zdyzbdyb")
public class TjZdyzbdybAction extends BaseAction {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	ITjZdyzbdybService zdyzbdybService;

	@Autowired
	ITjZbztmcdybService zbztmcdybService;

	/**
	 * 跳转到增加指标页面，若topicId不为空，则说明该指标是在该主题下 新增页面也不会显示该指标的上级指标
	 * 
	 * @param topicId
	 *            主题Id
	 * @param indicatorId
	 *            指标Id
	 * @return ModelAndView
	 * @throws RepsException
	 */
	@RequestMapping(value = "/toadd")
	public ModelAndView toAdd(String topicId, String indicatorId) throws RepsException {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/add");
		TjZbztmcdyb topic = null;
		if (StringUtil.isNotBlank(topicId)) {
			topic = zbztmcdybService.get(topicId);
		} else {
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			topic = tjZdyzbdyb.getTjZbztmcdyb();
			mav.addObject("indicator", tjZdyzbdyb);
		}
		mav.addObject("topic", topic);
		return mav;
	}

	/**
	 * 添加指标
	 * 
	 * @param indicator
	 * @return Object
	 * @throws RepsException
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(TjZdyzbdyb indicator, HttpServletRequest request) throws RepsException {
		if (indicator == null) {
			throw new RepsException("数据不完整");
		}

		String name = indicator.getZbmc();
		if (StringUtil.isNotBlank(name)) {
			// 检查相同命名空间底下的指标名称是否已经存在
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.getIndicatorByName(indicator);
			if (null != tjZdyzbdyb && tjZdyzbdyb.getZbmc().equals(name)) {
				return ajax(AjaxStatus.FAIL, "指标名称已存在，请重新输入！");
			}
		}

		// 如果上级指标为空则设置为-1
		if (StringUtil.isBlank(indicator.getfId())) {
			// 设置没有上级指标
			indicator.setfId("-1");
		}

		String metaDatas = getMetaDatasFromSession(request);
		indicator.setZbmeta(metaDatas);
		zdyzbdybService.save(indicator);
		removeMetaDatasFromSession();
		return ajax(AjaxStatus.OK, "添加成功");
	}

	/**
	 * 跳转到指标修改页面 通过topicId来记录用户点击的是主题还是指标，用于页面返回
	 * 
	 * @param id
	 *            指标id
	 * @param topicId
	 *            主题id
	 * @return ModelAndView
	 * @throws RepsException
	 */
	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String id, String topicId) throws RepsException {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/edit");
		// 记录点击的是主题
		if (StringUtil.isNotBlank(topicId)) {
			mav.addObject("topicId", topicId);
		}
		TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(id);
		// 查询命名空间
		String namespace = tjZdyzbdyb.getTjZbztmcdyb().getTjNamespace().getNamespace();
		Map<String, String> map = buildTopicSelectMap(namespace);
		List<TjZdyzbdyb> indicatorList = zdyzbdybService.getIndicatorOfTopic(tjZdyzbdyb.getTjZbztmcdyb().getZbztId());
		Map<String, String> indicatorMap = new HashMap<>();
		indicatorMap.put("", "");
		for (TjZdyzbdyb indicator : indicatorList) {
			indicatorMap.put(indicator.getId(), indicator.getZbmc());
		}
		mav.addObject("topicList", map);
		mav.addObject("indicatorList", indicatorMap);
		mav.addObject("indicator", tjZdyzbdyb);

		return mav;
	}

	private Map<String, String> buildTopicSelectMap(String namespace) {
		List<TjZbztmcdyb> topicList = zbztmcdybService.queryList(namespace);
		Map<String, String> map = new HashMap<>();
		map.put("", "");
		for (TjZbztmcdyb t : topicList) {
			map.put(t.getZbztId(), t.getZbztmc());
		}
		return map;
	}

	/**
	 * 修改指标信息
	 * 
	 * @param indicator
	 * @return Object
	 * @throws RepsException
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(TjZdyzbdyb indicator) throws RepsException {
		if (indicator == null) {
			throw new RepsException("数据不完整");
		}
		if (StringUtil.isBlank(indicator.getfId())) {// 默认设置为-1
			indicator.setfId("-1");
		}
		TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicator.getId());
		zdyzbdybService.update(indicator);
		// 如果修改了指标所属主题或者上级指标则刷新整个框架页面
		if (!(tjZdyzbdyb.getTjZbztmcdyb().getZbztId().equals(indicator.getTjZbztmcdyb().getZbztId())) || !(indicator.getfId().equals(tjZdyzbdyb.getfId()))) {
			return ajax(AjaxStatus.OK, "修改成功", "changed", true);
		} else {
			return ajax(AjaxStatus.OK, "修改成功", "changed", false);
		}
	}

	/**
	 * 删除指定对象
	 * 
	 * @param id
	 * @return Object
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(String id) {
		try {
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(id);
			if (tjZdyzbdyb != null) {
				zdyzbdybService.delete(tjZdyzbdyb);
			}
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			return ajax(AjaxStatus.ERROR, "删除失败");
		}

	}

	/**
	 * 根据主题Id查询相应的指标 用于指标下拉框展示
	 * 
	 * @param id
	 *            主题id
	 * @return Object
	 * @throws RepsException
	 */
	@RequestMapping(value = "/indicSelect")
	@ResponseBody
	public Object indicSelect(String id) throws RepsException {
		List<TjZdyzbdyb> indicatorList = zdyzbdybService.getIndicatorOfTopic(id);
		Map<String, String> indicatorMap = new HashMap<>();
		for (TjZdyzbdyb indicator : indicatorList) {
			indicatorMap.put(indicator.getId(), indicator.getZbmc());
		}
		return ajax(AjaxStatus.OK, transSelectOptionStrFromMap(indicatorMap, true));
	}

	/**
	 * 指标列表展示 用于指标查询
	 * 
	 * @param pager
	 *            分页对象
	 * @param info
	 *            指标信息
	 * @param namespace
	 *            命名空间
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, TjZdyzbdyb info, String namespace) {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/list");
		ListResult<TjZdyzbdyb> listResult = zdyzbdybService.query(pager.getStartRow(), pager.getPageSize(), info);
		setModelAndView(pager, info, mav, listResult, namespace);
		return mav;
	}

	private void setModelAndView(Pagination pager, TjZdyzbdyb info, ModelAndView mav, ListResult<TjZdyzbdyb> listResult, String namespace) {
		// 查询所有主题
		Map<String, String> map = buildTopicSelectMap(namespace);
		mav.addObject("topicList", map);
		// 设置总数
		pager.setTotalRecord(listResult.getCount());
		// 分页数据
		mav.addObject("indicatorList", listResult.getList());
		// 分页参数
		mav.addObject("pager", pager);
		mav.addObject("indicator", info);
	}

	/**
	 * 点击树节点显示分类下的详细信息
	 * 
	 * @param pager
	 * @param info
	 * @param isDetail
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/show")
	public ModelAndView show(Pagination pager, TjZdyzbdyb info, boolean isDetail) {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/list");
		// 当前指标信息
		TjZdyzbdyb indicator = zdyzbdybService.get(info.getId());
		TjZdyzbdyb parent = zdyzbdybService.get(indicator.getfId());
		// 获取指标所对应的命名空间
		String namespace = indicator.getTjZbztmcdyb().getTjNamespace().getNamespace();
		// 判断是否直接查看当前指标详情
		if (!isDetail) {
			// 当前指标Id
			String id = indicator.getId();
			// 如果指标下面有子级显示子节点列表，否则显示指标详情页面
			if (zdyzbdybService.hasClild(id)) {
				ListResult<TjZdyzbdyb> listResult = zdyzbdybService.queryByPid(pager.getStartRow(), pager.getPageSize(), id);
				setModelAndView(pager, info, mav, listResult, namespace);
			} else {
				mav.setViewName("/report/zdyzbdyb/show");
				mav.addObject("indicator", indicator);
			}
		} else {
			mav.setViewName("/report/zdyzbdyb/show");
			mav.addObject("indicator", indicator);
		}
		// 详情显示上级指标
		if (null != parent) {
			mav.addObject("parentIndicator", parent);
		}
		// 导航路径
		StringBuilder path = new StringBuilder();
		// 增加当前指标路径
		path.insert(0, "/" + indicator.getZbmc());
		// 获取上级指标路径
		path = getIndicatorPath(indicator, path);
		// 获取固定导航路径 命名空间-》主题-》指标
		getFixedIndicatorPath(indicator, namespace, path);
		log.debug("指标导航路径 {}", path);
		mav.addObject("path", path);
		return mav;
	}

	/**
	 * 获取固定导航路径
	 * 
	 * @param indicator
	 * @param namespace
	 * @param path
	 */
	private void getFixedIndicatorPath(TjZdyzbdyb indicator, String namespace, StringBuilder path) {
		path.insert(0, "/" + indicator.getTjZbztmcdyb().getZbztmc());
		path.insert(0, namespace);
	}

	/**
	 * 递归获取指标所在路径，不包括指标pid为-1的指标
	 * 
	 * @param indicator
	 * @param path
	 * @return StringBuilder
	 */
	public StringBuilder getIndicatorPath(TjZdyzbdyb indicator, StringBuilder path) {
		String pId = indicator.getfId();
		if (null != pId && !"-1".equals(pId)) {
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(pId);
			path.insert(0, "/" + tjZdyzbdyb.getZbmc());
			getIndicatorPath(tjZdyzbdyb, path);
		}
		return path;
	}

	@RequestMapping(value = "/listdatabasetype")
	public ModelAndView listDatabaseType(Pagination pager, String indicatorId, Integer flag, HttpServletRequest request) {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/listdatabasetype");
		List<DatabaseType> databaseList = getValuesFromSession(request, DATABASE_TYPE_LIST.getCode());
		if (null == databaseList && StringUtil.isNotBlank(indicatorId)) {
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			databaseList = getSpecialValuesFromJson(jsonData, DATABASE_TYPE_LIST.getCode(), DatabaseType.class);
		}
		// 分页数据
		mav.addObject("databaseTypeList", databaseList);
		mav.addObject("indicatorId", indicatorId);
		mav.addObject("flag", flag);
		return mav;
	}

	@RequestMapping(value = "/toaddatabasetype")
	public ModelAndView toAddDatabaseType(String indicatorId) throws RepsException {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/addatabasetype");
		mav.addObject("indicatorId", indicatorId);
		return mav;
	}

	/**
	 * 添加数据库类型
	 * 
	 * @param databaseType
	 * @param request
	 * @return
	 * @throws RepsException
	 */
	@RequestMapping(value = "/addatabasetype")
	@ResponseBody
	public Object addDatabaseType(DatabaseType databaseType, String indicatorId, HttpServletRequest request) {
		try {
			List<DatabaseType> databaseTypeList = getValuesFromSession(request, DATABASE_TYPE_LIST.getCode());
			databaseType.setId(IDGenerator.generate());
			// 若指标ID不为空，指标修改页面添加指标元数据信息
			if (StringUtil.isNotBlank(indicatorId)) {
				TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
				JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
				String metaData = addSpecialValueFromJson(jsonData, DATABASE_TYPE_LIST.getCode(), databaseType);
				updateMeta(tjZdyzbdyb, metaData);
			} else {
				if (null == databaseTypeList) {
					List<DatabaseType> databaseTypes = new ArrayList<>();
					databaseTypes.add(databaseType);
					setValuesToSession(request, DATABASE_TYPE_LIST.getCode(), databaseTypes);
				} else {
					databaseTypeList.add(databaseType);
				}
			}
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (RepsException e) {
			e.printStackTrace();
			log.error("添加数据库类型失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toeditdatabasetype")
	public Object toEditDatabaseType(String indicatorId, String dbTypeId) {
		try {
			ModelAndView mav = getModelAndView("/report/zdyzbdyb/editdatabasetype");
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			DatabaseType databaseType = getSpecialValueFromList(getSpecialValuesFromJson(jsonData, DATABASE_TYPE_LIST.getCode(), DatabaseType.class), dbTypeId, "id");
			mav.addObject("databaseType", databaseType);
			mav.addObject("indicatorId", indicatorId);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改数据库类型失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/editdatabasetype")
	@ResponseBody
	public Object editDatabaseType(DatabaseType databaseType, String indicatorId) {
		try {
			if (databaseType == null) {
				throw new RepsException("数据不完整");
			}
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			List<DatabaseType> databaseTypeList = getSpecialValuesFromJson(jsonData, DATABASE_TYPE_LIST.getCode(), DatabaseType.class);
			DatabaseType bean = getSpecialValueFromList(databaseTypeList, databaseType.getId(), "id");
			Collections.replaceAll(databaseTypeList, bean, databaseType);
			//替换元数据中特定的值
			String metaJson = replaceSpecialValueFromJson(jsonData, DATABASE_TYPE_LIST.getCode(), databaseTypeList);
			updateMeta(tjZdyzbdyb, metaJson);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改数据库类型失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/deletedatabasetype")
	@ResponseBody
	public Object deleteDatabaseType(String dbTypeId, String indicatorId, HttpServletRequest request) {
		try {
			if (StringUtil.isNotBlank(indicatorId)) {
				TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
				//获取指标元数据json对象
				JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
				DatabaseType bean = getSpecialValueFromList(getSpecialValuesFromJson(jsonData, DATABASE_TYPE_LIST.getCode(), DatabaseType.class), dbTypeId, "id");
				//从元数据JSON中移除
				String metaJson = removeSpecialValueFromJson(jsonData, DATABASE_TYPE_LIST.getCode(), bean);
				updateMeta(tjZdyzbdyb, metaJson);
			} else {
				List<DatabaseType> databaseTypeList = getValuesFromSession(request, DATABASE_TYPE_LIST.getCode());
				databaseTypeList.remove(getSpecialValueFromList(databaseTypeList, dbTypeId, "id"));
			}
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除数据库类型失败", e);
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}

	private void updateMeta(TjZdyzbdyb tjZdyzbdyb, String metaJson) {
		//修改数据库
		tjZdyzbdyb.setZbmeta(metaJson);
		zdyzbdybService.update(tjZdyzbdyb);
	}
	
	private JSONObject getIndicatorMeta(TjZdyzbdyb tjZdyzbdyb) {
		String zbmeta = tjZdyzbdyb.getZbmeta();
		return JSONObject.fromObject(zbmeta);
	}
	
	/**
	 * 指标元数据参数定义列表
	 * @param pager
	 * @param indicatorId 指标ID
	 * @param flag
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/listparamdefined")
	public ModelAndView listParamDefined(Pagination pager, String indicatorId, Integer flag, HttpServletRequest request) {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/listparamdefined");
		List<ParamDefined> paramDefinedList = getValuesFromSession(request, PARAM_DEFINED_LIST.getCode());
		if (null == paramDefinedList && StringUtil.isNotBlank(indicatorId)) {
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			paramDefinedList = getSpecialValuesFromJson(jsonData, PARAM_DEFINED_LIST.getCode(), ParamDefined.class);
		}
		// 分页数据
		mav.addObject("paramDefinedList", paramDefinedList);
		mav.addObject("indicatorId", indicatorId);
		mav.addObject("flag", flag);
		return mav;
	}
	
	@RequestMapping(value = "/toaddparamdefined")
	public ModelAndView toAddParamDefined(String indicatorId) throws RepsException {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/addparamdefined");
		mav.addObject("indicatorId", indicatorId);
		return mav;
	}
	
	@RequestMapping(value = "/addparamdefined")
	@ResponseBody
	public Object addParamDefined(ParamDefined paramDefined, String indicatorId, HttpServletRequest request) {
		try {
			List<ParamDefined> paramDefinedList = getValuesFromSession(request, PARAM_DEFINED_LIST.getCode());
			paramDefined.setId(IDGenerator.generate());
			// 若指标ID不为空，指标修改页面添加指标元数据信息
			if (StringUtil.isNotBlank(indicatorId)) {
				TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
				JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
				String metaData = addSpecialValueFromJson(jsonData, PARAM_DEFINED_LIST.getCode(), paramDefined);
				updateMeta(tjZdyzbdyb, metaData);
			} else {
				if (null == paramDefinedList) {
					List<ParamDefined> paramDefineds = new ArrayList<>();
					paramDefineds.add(paramDefined);
					setValuesToSession(request, PARAM_DEFINED_LIST.getCode(), paramDefineds);
				} else {
					paramDefinedList.add(paramDefined);
				}
			}
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (RepsException e) {
			e.printStackTrace();
			log.error("添加参数定义失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/toeditparamdefined")
	public Object toEditParamDefined(String indicatorId, String paramDefId) {
		try {
			ModelAndView mav = getModelAndView("/report/zdyzbdyb/editparamdefined");
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			ParamDefined paramDefined = getSpecialValueFromList(getSpecialValuesFromJson(jsonData, PARAM_DEFINED_LIST.getCode(), ParamDefined.class), paramDefId, "id");
			mav.addObject("paramDefined", paramDefined);
			mav.addObject("indicatorId", indicatorId);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改参数定义失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/editparamdefined")
	@ResponseBody
	public Object editParamDefined(ParamDefined paramDefined, String indicatorId) {
		try {
			if (paramDefined == null) {
				throw new RepsException("数据不完整");
			}
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			List<ParamDefined> paramDefinedList = getSpecialValuesFromJson(jsonData, PARAM_DEFINED_LIST.getCode(), ParamDefined.class);
			ParamDefined bean = getSpecialValueFromList(paramDefinedList, paramDefined.getId(), "id");
			Collections.replaceAll(paramDefinedList, bean, paramDefined);
			//替换元数据中特定的值
			String metaJson = replaceSpecialValueFromJson(jsonData, PARAM_DEFINED_LIST.getCode(), paramDefinedList);
			updateMeta(tjZdyzbdyb, metaJson);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改参数定义失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/deleteparamdefined")
	@ResponseBody
	public Object deleteParamDefined(String paramDefId, String indicatorId, HttpServletRequest request) {
		try {
			if (StringUtil.isNotBlank(indicatorId)) {
				TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
				//获取指标元数据json对象
				JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
				ParamDefined bean = getSpecialValueFromList(getSpecialValuesFromJson(jsonData, PARAM_DEFINED_LIST.getCode(), ParamDefined.class), paramDefId, "id");
				//从元数据JSON中移除
				String metaJson = removeSpecialValueFromJson(jsonData, PARAM_DEFINED_LIST.getCode(), bean);
				updateMeta(tjZdyzbdyb, metaJson);
			} else {
				List<ParamDefined> paramDefinedList = getValuesFromSession(request, PARAM_DEFINED_LIST.getCode());
				paramDefinedList.remove(getSpecialValueFromList(paramDefinedList, paramDefId, "id"));
			}
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除参数定义失败", e);
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}
	
	@RequestMapping(value = "/listoutputfieldefined")
	public ModelAndView listOutputFieldDefined(Pagination pager, String indicatorId, Integer flag, HttpServletRequest request) {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/listoutputfieldefined");
		List<OutputFieldDefined> outputFieldDefinedList = getValuesFromSession(request, OUTPUT_FIELD_DEFINED.getCode());
		if (null == outputFieldDefinedList && StringUtil.isNotBlank(indicatorId)) {
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			outputFieldDefinedList = getSpecialValuesFromJson(jsonData, OUTPUT_FIELD_DEFINED.getCode(), OutputFieldDefined.class);
		}
		// 分页数据
		mav.addObject("outputFieldDefinedList", outputFieldDefinedList);
		mav.addObject("indicatorId", indicatorId);
		mav.addObject("flag", flag);
		return mav;
	}

	@RequestMapping(value = "/toaddoutputfieldefined")
	public ModelAndView toAddOutputFieldDefined(String indicatorId) throws RepsException {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/addoutputfieldefined");
		mav.addObject("indicatorId", indicatorId);
		return mav;
	}

	@RequestMapping(value = "/addoutputfieldefined")
	@ResponseBody
	public Object addOutputFieldDefined(OutputFieldDefined outputFieldDefined, String indicatorId, HttpServletRequest request) {
		try {
			List<OutputFieldDefined> outputFieldDefinedList = getValuesFromSession(request, OUTPUT_FIELD_DEFINED.getCode());
			outputFieldDefined.setId(IDGenerator.generate());
			// 若指标ID不为空，指标修改页面添加指标元数据信息
			if (StringUtil.isNotBlank(indicatorId)) {
				TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
				JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
				String metaData = addSpecialValueFromJson(jsonData, OUTPUT_FIELD_DEFINED.getCode(), outputFieldDefined);
				updateMeta(tjZdyzbdyb, metaData);
			} else {
				if (null == outputFieldDefinedList) {
					List<OutputFieldDefined> outputFieldDefineds = new ArrayList<>();
					outputFieldDefineds.add(outputFieldDefined);
					setValuesToSession(request, OUTPUT_FIELD_DEFINED.getCode(), outputFieldDefineds);
				} else {
					outputFieldDefinedList.add(outputFieldDefined);
				}
			}
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (RepsException e) {
			e.printStackTrace();
			log.error("添加输出字段定义失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toeditoutputfieldefined")
	public Object toEditOutputFieldDefined(String indicatorId, String outputFieldDefId) {
		try {
			ModelAndView mav = getModelAndView("/report/zdyzbdyb/editoutputfieldefined");
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			OutputFieldDefined outputFieldDefined = getSpecialValueFromList(getSpecialValuesFromJson(jsonData, OUTPUT_FIELD_DEFINED.getCode(), OutputFieldDefined.class), outputFieldDefId, "id");
			mav.addObject("outputFieldDefined", outputFieldDefined);
			mav.addObject("indicatorId", indicatorId);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改输出字段定义失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/editoutputfieldefined")
	@ResponseBody
	public Object editOutputFieldDefined(OutputFieldDefined outputFieldDefined, String indicatorId) {
		try {
			if (outputFieldDefined == null) {
				throw new RepsException("数据不完整");
			}
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			List<OutputFieldDefined> outputFieldDefinedList = getSpecialValuesFromJson(jsonData, OUTPUT_FIELD_DEFINED.getCode(), OutputFieldDefined.class);
			OutputFieldDefined bean = getSpecialValueFromList(outputFieldDefinedList, outputFieldDefined.getId(), "id");
			Collections.replaceAll(outputFieldDefinedList, bean, outputFieldDefined);
			//替换元数据中特定的值
			String metaJson = replaceSpecialValueFromJson(jsonData, OUTPUT_FIELD_DEFINED.getCode(), outputFieldDefinedList);
			updateMeta(tjZdyzbdyb, metaJson);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改输出字段定义失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/deleteoutputfieldefined")
	@ResponseBody
	public Object deleteOutputFieldDefined(String outputFieldDefId, String indicatorId, HttpServletRequest request) {
		try {
			if (StringUtil.isNotBlank(indicatorId)) {
				TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
				//获取指标元数据json对象
				JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
				OutputFieldDefined bean = getSpecialValueFromList(getSpecialValuesFromJson(jsonData, OUTPUT_FIELD_DEFINED.getCode(), OutputFieldDefined.class), outputFieldDefId, "id");
				//从元数据JSON中移除
				String metaJson = removeSpecialValueFromJson(jsonData, OUTPUT_FIELD_DEFINED.getCode(), bean);
				updateMeta(tjZdyzbdyb, metaJson);
			} else {
				List<OutputFieldDefined> outputFieldDefinedList = getValuesFromSession(request, OUTPUT_FIELD_DEFINED.getCode());
				outputFieldDefinedList.remove(getSpecialValueFromList(outputFieldDefinedList, outputFieldDefId, "id"));
			}
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除输出字段定义失败", e);
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}

}
