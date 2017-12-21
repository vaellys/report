package com.reps.tj.action;

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
import com.reps.core.util.StringUtil;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.tj.entity.TjNamespace;
import com.reps.tj.entity.TjZbztmcdyb;
import com.reps.tj.entity.TjZdyzbdyb;
import com.reps.tj.service.ITjNamespaceService;
import com.reps.tj.service.ITjZbztmcdybService;
import com.reps.tj.service.ITjZdyzbdybService;


/**
 * 指标主题操作
 * @author qianguobing
 * @date 2017年8月1日 上午10:40:23
 */
@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/report/zbztmcdyb")
public class TjZbztmcdybAction extends BaseAction {
	
	@Autowired
	private ITjZdyzbdybService zdyzbdybService;
	
	@Autowired
	private ITjZbztmcdybService zbztmcdybService;
	
	@Autowired
	private ITjNamespaceService namespaceService;
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 主题列表列显示以及点击主题时显示主题下面的指标
	 * @param pager
	 * @param info
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, TjZbztmcdyb info, HttpServletRequest request) throws RepsException {
		ModelAndView mav = getModelAndView("/report/zbztmcdyb/list");
		TjNamespace tjNamespace = info.getTjNamespace();
		//第一次进入页面时namespace是空的，查询所有命名空间的主题
		String namespace = null != tjNamespace ? tjNamespace.getNamespace() : "";
		ListResult<TjZbztmcdyb> listResult = zbztmcdybService.query(pager.getStartRow(), pager.getPageSize(), info);
		//查询所命名空间
		Map<String, String> namespaceMap = buildNamespaceSelectMap();
		//点击主题进入页面显示该主题下面的指标
		String zbztId = info.getZbztId();
		if(StringUtil.isNotBlank(zbztId)){
			List<TjZdyzbdyb> indicatorList = zdyzbdybService.getIndicatorOfTopic(zbztId);//获得该主题下的所有指标
			TjZbztmcdyb topic = zbztmcdybService.get(zbztId);
			//进入指标列表页面
			mav.setViewName("/report/zdyzbdyb/list");
			//设置总数
			pager.setTotalRecord(indicatorList.size());
			mav.addObject("indicatorList", indicatorList);
			mav.addObject("topic", topic);
			namespace = topic.getTjNamespace().getNamespace();
		}else{
			//设置总数
			pager.setTotalRecord(listResult.getCount());
		}
		StringBuilder path = null;//导航路径
		//获取导航路径信息
		if(!"".equals(namespace)) {
			path = new StringBuilder();
			path.append(namespace);
			if(StringUtil.isNotBlank(zbztId)) {
				TjZbztmcdyb tjZbztmcdyb = zbztmcdybService.get(zbztId);
				path.append("/");
				path.append(tjZbztmcdyb.getZbztmc());
			}
		}
		//搜索页面上显示所有主题
		List<TjZbztmcdyb> allTopic = zbztmcdybService.queryList(namespace);
		Map<String, String> map = new HashMap<>();
		map.put("", "");
		for(TjZbztmcdyb t : allTopic){
			map.put(t.getZbztId(), t.getZbztmc());
		}
		mav.addObject("path", path);
		mav.addObject("topicList", map);
		mav.addObject("info", info);
		mav.addObject("namespace", namespace);
		mav.addObject("namespaceList", namespaceMap);
		//分页数据
		mav.addObject("list", listResult.getList());
		//分页参数
		mav.addObject("pager", pager);
				
		return mav;
	}
	
	/**
	 * 跳转到添加页面
	 * @param namespace
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/toadd")
	public ModelAndView toAdd(String namespace) {
		ModelAndView mav = getModelAndView("/report/zbztmcdyb/add");
		TjNamespace tjNamespace = namespaceService.get(namespace);
		Map<String, String> map = buildNamespaceSelectMap();
		
		mav.addObject("namespaceList", map);
		mav.addObject("tjNamespace", tjNamespace);
		
		return mav;
	}
	
	/**
	 * 添加主题对象
	 * @param info
	 * @return Object
	 * @throws RepsException
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(TjZbztmcdyb info) throws RepsException {
		if (info == null) {
			throw new RepsException("数据不完整");
		}
		TjNamespace tjNamespace = info.getTjNamespace();
		String namespace = tjNamespace.getNamespace();
		if (tjNamespace == null || StringUtil.isBlank(namespace)) {
			return ajax(AjaxStatus.FAIL, "添加失败，请选择指标命名空间！");
		} else {
			String[] ns = namespace.split(",");
			tjNamespace.setNamespace(ns[0]);
		}
		if (info.getQxlx() == null) {// 默认设置为否
			info.setQxlx((short) 0);
		}
		zbztmcdybService.save(info);
		return ajax(AjaxStatus.OK, "添加成功");
	}
	
	/**
	 * 编辑页面的跳转
	 * @param id
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String id){
		ModelAndView mav = getModelAndView("/report/zbztmcdyb/edit");

		TjZbztmcdyb topic = zbztmcdybService.get(id);
		
		Map<String, String> map = buildNamespaceSelectMap();
		mav.addObject("topic", topic);
		mav.addObject("namespaceList", map);
		return mav;
	}

	/**
	 * 构造命名空间map
	 * @return Map<String, String>
	 */
	private Map<String, String> buildNamespaceSelectMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<TjNamespace> allNamespace = namespaceService.getAllNamespace();
		map.put("", "");
		for(TjNamespace t : allNamespace){
			map.put(t.getNamespace(), t.getNamespace());
		}
		return map;
	}
	
	/**
	 * 修改主题对象
	 * @param info
	 * @return Object
	 * @throws RepsException
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(TjZbztmcdyb info) throws RepsException {
		if(info == null){
			throw new RepsException("数据不完整");
		}
		
		TjZbztmcdyb topic = zbztmcdybService.get(info.getZbztId());
		if(topic != null){
			topic.setZbztmc(info.getZbztmc());
			if(StringUtil.isBlank(info.getTjNamespace().getNamespace())){
				return ajax(AjaxStatus.FAIL, "添加失败，请选择指标命名空间！");
			}
			if (info.getQxlx() != null) {
				topic.setQxlx(info.getQxlx());
			}
			topic.setSsdw(info.getSsdw());
			topic.setZbztsm(info.getZbztsm());
			topic.setTjNamespace(info.getTjNamespace());
			zbztmcdybService.update(topic);
		}
		return ajax(AjaxStatus.OK, "修改成功");
	}
	
	/**
	 * 删除指定主题对象
	 * @param id
	 * @return Object
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(String id) {
		try{
			TjZbztmcdyb topic = zbztmcdybService.get(id);
			if(topic != null){
				zbztmcdybService.delete(topic);
			}
			return ajax(AjaxStatus.OK, "删除成功");
		}
		catch(Exception e){
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}
	
}
