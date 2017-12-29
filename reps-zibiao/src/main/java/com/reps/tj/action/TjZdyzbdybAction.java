package com.reps.tj.action;

import static com.reps.tj.enums.Meta.DATABASE_TYPE_LIST;
import static com.reps.tj.enums.Meta.DETAILS_INDICATOR;
import static com.reps.tj.enums.Meta.META_CODES;
import static com.reps.tj.enums.Meta.META_MAPS;
import static com.reps.tj.enums.Meta.META_JOINER;
import static com.reps.tj.enums.Meta.OUTPUT_FIELD_DEFINED;
import static com.reps.tj.enums.Meta.PARAM_DEFINED_LIST;
import static com.reps.tj.enums.Meta.STATISTICS_ITEM_CATEGORY;
import static com.reps.tj.util.MetaJsonParse.addSpecialValueFromJson;
import static com.reps.tj.util.MetaJsonParse.getSpecialValueFromList;
import static com.reps.tj.util.MetaJsonParse.getSpecialValueFromObject;
import static com.reps.tj.util.MetaJsonParse.getSpecialValuesFromJson;
import static com.reps.tj.util.MetaJsonParse.removeSpecialValueFromJson;
import static com.reps.tj.util.MetaJsonParse.replaceSpecialValueFromJson;
import static com.reps.tj.util.MetaJsonParse.setSpecialValueFromObject;
import static com.reps.tj.util.MetaManager.getMetaDatasFromSession;
import static com.reps.tj.util.MetaManager.getValuesFromSession;
import static com.reps.tj.util.MetaManager.removeCopyMetaDatasFromSession;
import static com.reps.tj.util.MetaManager.removeMetaDatasFromSession;
import static com.reps.tj.util.MetaManager.setValuesToSession;
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
import com.reps.tj.entity.DetailsIndicator;
import com.reps.tj.entity.OutputFieldDefined;
import com.reps.tj.entity.ParamDefined;
import com.reps.tj.entity.StatisticsItemCategory;
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
	private static final String ID = "id";

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
		String zbmeta = indicator.getZbmeta();
		if (StringUtil.isBlank(zbmeta)) {
			indicator.setZbmeta(tjZdyzbdyb.getZbmeta());
		}
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
		removeMetaDatasFromSession();
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
		List<DatabaseType> databaseList = getIndicatorMetaList(indicatorId, DATABASE_TYPE_LIST.getCode(), DatabaseType.class, request);
		// 分页数据
		mav.addObject("databaseTypeList", databaseList);
		mav.addObject("indicatorId", indicatorId);
		mav.addObject("flag", flag);
		return mav;
	}

	private <T> List<T> getIndicatorMetaList(String indicatorId, String key, Class<T> clazz, HttpServletRequest request) {
		List<T> list = getValuesFromSession(request, key);
		return getIndicatorMetaList(indicatorId, key, clazz, list);
	}

	private <T> List<T> getIndicatorMetaList(String indicatorId, String key, Class<T> clazz, List<T> list) {
		if (null == list && StringUtil.isNotBlank(indicatorId)) {
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			list = getSpecialValuesFromJson(jsonData, key, clazz);
		}
		return list;
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
			addIndicatorMeta(databaseType, indicatorId, DATABASE_TYPE_LIST.getCode(), request);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (RepsException e) {
			e.printStackTrace();
			log.error("添加数据库类型失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	/**
	 * 指标元数据增加
	 * 
	 * @param t
	 * @param indicatorId
	 * @param key
	 * @param request
	 * @throws RepsException
	 */
	private <T> void addIndicatorMeta(T t, String indicatorId, String key, HttpServletRequest request) throws RepsException {
		List<T> resultList = getValuesFromSession(request, key);
		setSpecialValueFromObject(t, IDGenerator.generate(), ID);
		// 若指标ID不为空，指标修改页面添加指标元数据信息
		if (StringUtil.isNotBlank(indicatorId)) {
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			String metaData = addSpecialValueFromJson(jsonData, key, t);
			updateMeta(tjZdyzbdyb, metaData);
		} else {
			if (null == resultList) {
				List<T> list = new ArrayList<>();
				list.add(t);
				setValuesToSession(request, key, list);
			} else {
				resultList.add(t);
			}
		}
	}

	@RequestMapping(value = "/toeditdatabasetype")
	public Object toEditDatabaseType(String indicatorId, String dbTypeId, HttpServletRequest request) {
		try {
			ModelAndView mav = getModelAndView("/report/zdyzbdyb/editdatabasetype");
			DatabaseType databaseType = getIndicatorMeta(indicatorId, dbTypeId, DATABASE_TYPE_LIST.getCode(), DatabaseType.class, request);
			mav.addObject("databaseType", databaseType);
			mav.addObject("indicatorId", indicatorId);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改数据库类型失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	/**
	 * 指标元数据查询
	 * 
	 * @param indicatorId
	 * @param id
	 * @param key
	 * @param clazz
	 * @param request
	 * @return
	 */
	private <T> T getIndicatorMeta(String indicatorId, String id, String key, Class<T> clazz, HttpServletRequest request) throws RepsException {
		T bean = null;
		// 若指标ID不为空，指标修改页面添加指标元数据信息
		if (StringUtil.isNotBlank(indicatorId)) {
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			bean = getSpecialValueFromList(getSpecialValuesFromJson(jsonData, key, clazz), id, ID);
		} else {
			List<T> list = getValuesFromSession(request, key);
			bean = getSpecialValueFromList(list, id, ID);
		}
		return bean;
	}

	@RequestMapping(value = "/editdatabasetype")
	@ResponseBody
	public Object editDatabaseType(DatabaseType databaseType, String indicatorId, HttpServletRequest request) {
		try {
			if (databaseType == null) {
				throw new RepsException("数据不完整");
			}
			editIndicatorMeta(databaseType, indicatorId, DATABASE_TYPE_LIST.getCode(), request);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改数据库类型失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	/**
	 * 修改指标元数据
	 * 
	 * @param t
	 * @param indicatorId
	 * @param key
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	private <T> void editIndicatorMeta(T t, String indicatorId, String key, HttpServletRequest request) throws RepsException {
		String idValue = getSpecialValueFromObject(t, ID);
		// 若指标ID不为空，指标修改页面添加指标元数据信息
		if (StringUtil.isNotBlank(indicatorId)) {
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			List<T> resultList = (List<T>) getSpecialValuesFromJson(jsonData, key, t.getClass());
			T bean = getSpecialValueFromList(resultList, idValue, ID);
			Collections.replaceAll(resultList, bean, t);
			// 替换元数据中特定的值
			String metaJson = replaceSpecialValueFromJson(jsonData, key, resultList);
			updateMeta(tjZdyzbdyb, metaJson);
		} else {
			List<T> list = getValuesFromSession(request, key);
			T bean = getSpecialValueFromList(list, idValue, ID);
			Collections.replaceAll(list, bean, t);
		}
	}

	@RequestMapping(value = "/deletedatabasetype")
	@ResponseBody
	public Object deleteDatabaseType(String dbTypeId, String indicatorId, HttpServletRequest request) {
		try {
			deleteIndicatorMeta(dbTypeId, indicatorId, DATABASE_TYPE_LIST.getCode(), DatabaseType.class, request);
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除数据库类型失败", e);
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}

	/**
	 * 指标元数据删除
	 * 
	 * @param id
	 * @param indicatorId
	 * @param key
	 * @param clazz
	 * @param request
	 */
	private <T> void deleteIndicatorMeta(String id, String indicatorId, String key, Class<T> clazz, HttpServletRequest request) throws RepsException {
		if (StringUtil.isNotBlank(indicatorId)) {
			TjZdyzbdyb tjZdyzbdyb = zdyzbdybService.get(indicatorId);
			// 获取指标元数据json对象
			JSONObject jsonData = getIndicatorMeta(tjZdyzbdyb);
			T bean = getSpecialValueFromList(getSpecialValuesFromJson(jsonData, key, clazz), id, ID);
			// 从元数据JSON中移除
			String metaJson = removeSpecialValueFromJson(jsonData, key, bean);
			updateMeta(tjZdyzbdyb, metaJson);
		} else {
			List<DatabaseType> resultList = getValuesFromSession(request, key);
			resultList.remove(getSpecialValueFromList(resultList, id, ID));
		}
	}

	private void updateMeta(TjZdyzbdyb tjZdyzbdyb, String metaJson) throws RepsException {
		// 修改数据库
		tjZdyzbdyb.setZbmeta(metaJson);
		zdyzbdybService.update(tjZdyzbdyb);
	}

	private JSONObject getIndicatorMeta(TjZdyzbdyb tjZdyzbdyb) throws RepsException {
		String zbmeta = tjZdyzbdyb.getZbmeta();
		if (StringUtil.isBlank(zbmeta)) {
			zbmeta = "{\"indicatorMetaInfo\":{}}";
		}
		return JSONObject.fromObject(zbmeta);
	}

	/**
	 * 指标元数据参数定义列表
	 * 
	 * @param pager
	 * @param indicatorId
	 *            指标ID
	 * @param flag
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/listparamdefined")
	public ModelAndView listParamDefined(Pagination pager, String indicatorId, Integer flag, HttpServletRequest request) {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/listparamdefined");
		List<ParamDefined> paramDefinedList = getIndicatorMetaList(indicatorId, PARAM_DEFINED_LIST.getCode(), ParamDefined.class, request);
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
			addIndicatorMeta(paramDefined, indicatorId, PARAM_DEFINED_LIST.getCode(), request);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (RepsException e) {
			e.printStackTrace();
			log.error("添加参数定义失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toeditparamdefined")
	public Object toEditParamDefined(String indicatorId, String paramDefId, HttpServletRequest request) {
		try {
			ModelAndView mav = getModelAndView("/report/zdyzbdyb/editparamdefined");
			ParamDefined paramDefined = getIndicatorMeta(indicatorId, paramDefId, PARAM_DEFINED_LIST.getCode(), ParamDefined.class, request);
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
	public Object editParamDefined(ParamDefined paramDefined, String indicatorId, HttpServletRequest request) {
		try {
			if (paramDefined == null) {
				throw new RepsException("数据不完整");
			}
			editIndicatorMeta(paramDefined, indicatorId, PARAM_DEFINED_LIST.getCode(), request);
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
			deleteIndicatorMeta(paramDefId, indicatorId, PARAM_DEFINED_LIST.getCode(), ParamDefined.class, request);
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
		List<OutputFieldDefined> outputFieldDefinedList = getIndicatorMetaList(indicatorId, OUTPUT_FIELD_DEFINED.getCode(), OutputFieldDefined.class, request);
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
			addIndicatorMeta(outputFieldDefined, indicatorId, OUTPUT_FIELD_DEFINED.getCode(), request);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (RepsException e) {
			e.printStackTrace();
			log.error("添加输出字段定义失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toeditoutputfieldefined")
	public Object toEditOutputFieldDefined(String indicatorId, String outputFieldDefId, HttpServletRequest request) {
		try {
			ModelAndView mav = getModelAndView("/report/zdyzbdyb/editoutputfieldefined");
			OutputFieldDefined outputFieldDefined = getIndicatorMeta(indicatorId, outputFieldDefId, OUTPUT_FIELD_DEFINED.getCode(), OutputFieldDefined.class, request);
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
	public Object editOutputFieldDefined(OutputFieldDefined outputFieldDefined, String indicatorId, HttpServletRequest request) {
		try {
			if (outputFieldDefined == null) {
				throw new RepsException("数据不完整");
			}
			editIndicatorMeta(outputFieldDefined, indicatorId, OUTPUT_FIELD_DEFINED.getCode(), request);
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
			deleteIndicatorMeta(outputFieldDefId, indicatorId, OUTPUT_FIELD_DEFINED.getCode(), OutputFieldDefined.class, request);
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除输出字段定义失败", e);
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}

	@RequestMapping(value = "/liststatisticsitemcategory")
	public ModelAndView listStatisticsItemCategory(Pagination pager, String indicatorId, Integer flag, HttpServletRequest request) {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/liststatisticsitemcategory");
		List<StatisticsItemCategory> statisticsItemCategoryList = getIndicatorMetaList(indicatorId, STATISTICS_ITEM_CATEGORY.getCode(), StatisticsItemCategory.class, request);
		// 分页数据
		mav.addObject("statisticsItemCategoryList", statisticsItemCategoryList);
		mav.addObject("indicatorId", indicatorId);
		mav.addObject("flag", flag);
		return mav;
	}

	@RequestMapping(value = "/toaddstatisticsitemcategory")
	public ModelAndView toAddStatisticsItemCategory(String indicatorId) throws RepsException {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/addstatisticsitemcategory");
		mav.addObject("indicatorId", indicatorId);
		return mav;
	}

	@RequestMapping(value = "/addstatisticsitemcategory")
	@ResponseBody
	public Object addStatisticsItemCategory(StatisticsItemCategory statisticsItemCategory, String indicatorId, HttpServletRequest request) {
		try {
			addIndicatorMeta(statisticsItemCategory, indicatorId, STATISTICS_ITEM_CATEGORY.getCode(), request);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (RepsException e) {
			e.printStackTrace();
			log.error("添加统计项目分类失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toeditstatisticsitemcategory")
	public Object toEditStatisticsItemCategory(String indicatorId, String itemId, HttpServletRequest request) {
		try {
			ModelAndView mav = getModelAndView("/report/zdyzbdyb/editstatisticsitemcategory");
			StatisticsItemCategory statisticsItemCategory = getIndicatorMeta(indicatorId, itemId, STATISTICS_ITEM_CATEGORY.getCode(), StatisticsItemCategory.class, request);
			mav.addObject("statisticsItemCategory", statisticsItemCategory);
			mav.addObject("indicatorId", indicatorId);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改统计项目分类失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/editstatisticsitemcategory")
	@ResponseBody
	public Object editStatisticsItemCategory(StatisticsItemCategory statisticsItemCategory, String indicatorId, HttpServletRequest request) {
		try {
			if (statisticsItemCategory == null) {
				throw new RepsException("数据不完整");
			}
			editIndicatorMeta(statisticsItemCategory, indicatorId, STATISTICS_ITEM_CATEGORY.getCode(), request);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改统计项目分类失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/deletestatisticsitemcategory")
	@ResponseBody
	public Object deleteStatisticsItemCategory(String itemId, String indicatorId, HttpServletRequest request) {
		try {
			deleteIndicatorMeta(itemId, indicatorId, STATISTICS_ITEM_CATEGORY.getCode(), StatisticsItemCategory.class, request);
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除统计项目分类失败", e);
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}

	@RequestMapping(value = "/listdetailsindicator")
	public ModelAndView listDetailsIndicator(Pagination pager, String indicatorId, Integer flag, HttpServletRequest request) {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/listdetailsindicator");
		List<DetailsIndicator> detailsIndicatorList = getIndicatorMetaList(indicatorId, DETAILS_INDICATOR.getCode(), DetailsIndicator.class, request);
		// 分页数据
		mav.addObject("detailsIndicatorList", detailsIndicatorList);
		mav.addObject("indicatorId", indicatorId);
		mav.addObject("flag", flag);
		return mav;
	}

	@RequestMapping(value = "/toaddetailsindicator")
	public ModelAndView toAddDetailsIndicator(String indicatorId) throws RepsException {
		ModelAndView mav = getModelAndView("/report/zdyzbdyb/addetailsindicator");
		mav.addObject("indicatorId", indicatorId);
		return mav;
	}

	@RequestMapping(value = "/addetailsindicator")
	@ResponseBody
	public Object addDetailsIndicator(DetailsIndicator detailsindicator, String indicatorId, HttpServletRequest request) {
		try {
			addIndicatorMeta(detailsindicator, indicatorId, DETAILS_INDICATOR.getCode(), request);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (RepsException e) {
			e.printStackTrace();
			log.error("添加关联明细指标失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toeditdetailsindicator")
	public Object toEditDetailsIndicator(String indicatorId, String detailId, HttpServletRequest request) {
		try {
			ModelAndView mav = getModelAndView("/report/zdyzbdyb/editdetailsindicator");
			DetailsIndicator detailsIndicator = getIndicatorMeta(indicatorId, detailId, DETAILS_INDICATOR.getCode(), DetailsIndicator.class, request);
			mav.addObject("detailsIndicator", detailsIndicator);
			mav.addObject("indicatorId", indicatorId);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改关联明细指标失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/editdetailsindicator")
	@ResponseBody
	public Object editDetailsIndicator(DetailsIndicator detailsIndicator, String indicatorId, HttpServletRequest request) {
		try {
			if (detailsIndicator == null) {
				throw new RepsException("数据不完整");
			}
			editIndicatorMeta(detailsIndicator, indicatorId, DETAILS_INDICATOR.getCode(), request);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改关联明细指标失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/deletedetailsindicator")
	@ResponseBody
	public Object deleteDetailsIndicator(String detailId, String indicatorId, HttpServletRequest request) {
		try {
			deleteIndicatorMeta(detailId, indicatorId, DETAILS_INDICATOR.getCode(), DetailsIndicator.class, request);
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除关联明细指标失败", e);
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}

	@RequestMapping(value = "/tocopymeta")
	public Object toCopyMeta(String indicatorId) {
		try {
			ModelAndView mav = getModelAndView("/report/zdyzbdyb/copymeta");
			mav.addObject("indicatorId", indicatorId);
			mav.addObject("metaMaps", META_MAPS);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("复制指标元数据失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/copymeta")
	@ResponseBody
	public Object copyMeta(String indicatorId, String metas, HttpServletRequest request) {
		try {
			if (StringUtil.isNotBlank(metas)) {
				String[] metaKeys = metas.split(",");
				// 复制之前清除原先的数据
				removeCopyMetaDatasFromSession();
				// 复制指标原数据
				for (String key : metaKeys) {
					if (DATABASE_TYPE_LIST.getCode().equals(key)) {
						setValuesToSession(request, META_JOINER + key, getIndicatorMetaList(indicatorId, key, DatabaseType.class, request));
					} else if (PARAM_DEFINED_LIST.getCode().equals(key)) {
						setValuesToSession(request, META_JOINER + key, getIndicatorMetaList(indicatorId, key, ParamDefined.class, request));
					} else if (OUTPUT_FIELD_DEFINED.getCode().equals(key)) {
						setValuesToSession(request, META_JOINER + key, getIndicatorMetaList(indicatorId, key, OutputFieldDefined.class, request));
					} else if (STATISTICS_ITEM_CATEGORY.getCode().equals(key)) {
						setValuesToSession(request, META_JOINER + key, getIndicatorMetaList(indicatorId, key, StatisticsItemCategory.class, request));
					} else if (DETAILS_INDICATOR.getCode().equals(key)) {
						setValuesToSession(request, META_JOINER + key, getIndicatorMetaList(indicatorId, key, DetailsIndicator.class, request));
					}
				}
			}
			return ajax(AjaxStatus.OK, "复制成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("复制指标元数据失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/pastemeta")
	@ResponseBody
	public Object pasteMeta(HttpServletRequest request) {
		try {
			for (String code : META_CODES) {
				List pasteData = getValuesFromSession(request, META_JOINER + code);
				if (null == pasteData) {
					continue;
				}
				List resultList = getValuesFromSession(request, code);
				if (null == resultList) {
					List list = new ArrayList<>();
					list.addAll(pasteData);
					setValuesToSession(request, code, list);
				} else {
					resultList.addAll(pasteData);
				}
			}
			return ajax(AjaxStatus.OK, "粘贴成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("复制指标元数据失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

}
