package com.reps.tj.action;

import java.util.ArrayList;
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
import com.reps.tj.entity.TjItem;
import com.reps.tj.entity.TjItemCategory;
import com.reps.tj.service.ITjItemCategoryService;
import com.reps.tj.service.ITjItemService;

/**
 * 报表指标分类相关操作
 * @author zql
 *
 */
@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/report/itemcategory")
public class TjItemCategoryAction extends BaseAction {
	
	@Autowired
	ITjItemCategoryService service;
	
	@Autowired
	ITjItemService itemService;

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 主页面
	 * @param pager
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index(String pId){
		ModelAndView mav = getModelAndView("/report/itemcategory/index");
		List<TjItemCategory> itemCateList = service.queryList("");//查询所有的指标分类.
		List<TjItemCategory> list = new ArrayList<TjItemCategory>();
		
		for(int i = 0; i < itemCateList.size(); i++){
			List<TjItem> itemList = itemService.getItemsOfCategory(itemCateList.get(i).getId());//该分类下的所有指标
			TjItemCategory cate = null;
			for(int j = 0; j < itemList.size(); j++){
				cate = new TjItemCategory();
				cate.setParentId(itemCateList.get(i).getId());
				cate.setId(itemList.get(j).getId());
				cate.setName(itemList.get(j).getChineseName());
				cate.setIsItem("1");//是指标
				list.add(cate);
			}
		}
		
		itemCateList.addAll(list);
		
		mav.addObject("treelist", itemCateList);
		
		return mav;
	}
	
	/**
	 * 分页查询
	 * @param pager
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, TjItemCategory info, HttpServletRequest request){
		ModelAndView mav = getModelAndView("/report/itemcategory/list");
		
		ListResult<TjItemCategory> listResult = service.query(pager.getStartRow(), pager.getPageSize(), info);
		List<TjItemCategory> categoryList = service.getAllCategory();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "");
		for(TjItemCategory t : categoryList){
			map.put(t.getId(), t.getName());
		}
		
		StringBuffer tempPath = new StringBuffer();//目录
		String path = "";
		
		if(info != null && StringUtil.isNotBlank(info.getParentId())){
			List<TjItem> itemList = itemService.getItemsOfCategory(info.getParentId());//获得该分类下的所有指标。
			//String requestType = request.getHeader("X-Requested-With");//判断是否是ajax请求。
			if(itemList.size() > 0){// && !"XMLHttpRequest".equals(requestType)){
				//进入指标列表页面
				//mav.setViewName(RepsConstant.JSP_BASE_PATH + "/report/item/list");
				mav.setViewName("/report/item/list");
				//设置总数
				pager.setTotalRecord(itemList.size());
				mav.addObject("itemList", itemList);
				mav.addObject("categoryId", info.getParentId());
			}else{
				//设置总数
				pager.setTotalRecord(listResult.getCount());
			}
			
			TjItemCategory cate = service.get(info.getParentId());//当前指标分类
			if(cate != null){
				tempPath.insert(0, "/"+cate.getName());
			}
			boolean flax = true;
			
			while(flax){
				if(cate != null && !"-1".equals(cate.getParentId())){
					cate = service.get(cate.getParentId());
					tempPath.insert(0, "/"+cate.getName());
				}else{
					flax = false;
				}
			}
			
			//去掉最前面的"/"
			path = tempPath.substring(1, tempPath.length());
		}else{
			//设置总数
			pager.setTotalRecord(listResult.getCount());
		}
		
		mav.addObject("path", path);
		mav.addObject("categoryList", map);
		mav.addObject("info", info);
		
		//分页数据
		mav.addObject("list", listResult.getList());
		//分页参数
		mav.addObject("pager", pager);
				
		return mav;
	}
	
	/**
	 * 跳转到添加页面
	 * @param pId
	 * @return
	 */
	@RequestMapping(value = "/toadd")
	public ModelAndView toAdd(String pId) {
		ModelAndView mav = getModelAndView("/report/itemcategory/add");
		TjItemCategory itemCate = null;
		List<TjItemCategory> itemCateList = null;
		
		Map<String, String> map = new HashMap<String, String>();
		
		if(pId != null && !pId.trim().equals("")){
			itemCate = service.get(pId);
		}else{
			itemCateList = service.getAllCategory();
			map.put("", "");
			for(TjItemCategory t : itemCateList){
				map.put(t.getId(), t.getName());
			}
		}
		itemCateList = service.queryList("-1");
		mav.addObject("itemCateList", map);
		mav.addObject("itemCate", itemCate);
		mav.addObject("showOrder", itemCateList.size()+1);
		
		return mav;
	}
	
	/**
	 * 添加对象
	 * @param info
	 * @return
	 * @throws RepsException
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(TjItemCategory info) throws RepsException{
		
		if(info == null){
			throw new RepsException("数据不完整");
		}
		
		/*
		if(StringUtil.isBlank(info.getParentId())){
			info.setParentId("-1");
			itemCateList = service.queryList("-1");
			info.setShowOrder(itemCateList.size()+1);//设置显示顺序
		}else{
			itemCateList = service.queryList(info.getParentId());
			info.setShowOrder(itemCateList.size()+1);//设置显示顺序
		}
		*/
		
		service.save(info);
		
		
		return ajax(AjaxStatus.OK, "添加成功");
	}
	
	/**
	 * 编辑页面的跳转
	 * @return
	 */
	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String id){
		ModelAndView mav = getModelAndView("/report/itemcategory/edit");

		TjItemCategory itemCate = service.get(id);
		TjItemCategory parentitemCate = service.get(itemCate.getParentId());
		List<TjItemCategory> itemCateList = service.getAllCategory();
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "");
		for(TjItemCategory t : itemCateList){
			map.put(t.getId(), t.getName());
		}
		mav.addObject("itemCateList", map);
		mav.addObject("itemCate", itemCate);
		mav.addObject("parentitemCate", parentitemCate);

		return mav;
	}
	
	/**
	 * 修改指定对象
	 * @param info
	 * @return
	 * @throws RepsException
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(TjItemCategory info) throws RepsException {
		if(info == null){
			throw new RepsException("数据不完整");
		}
		
		TjItemCategory itemCate = service.get(info.getId());
		if(itemCate != null){
			itemCate.setName(info.getName());
			if(StringUtil.isBlank(info.getParentId())){
				itemCate.setParentId("-1");
			}else{
				itemCate.setParentId(info.getParentId());
			}
			
			itemCate.setRemark(info.getRemark());
			itemCate.setShowOrder(info.getShowOrder());
			service.update(itemCate);
		}
		
		return ajax(AjaxStatus.OK, "修改成功");
		
	}
	
	/**
	 * 删除指定对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(String id){
		try{
			TjItemCategory itemCate = service.get(id);
			if(itemCate != null){
				service.delete(itemCate);
			}
			return ajax(AjaxStatus.OK, "删除成功");
		}
		catch(Exception e){
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}
	
	@RequestMapping(value = "/item")
	public ModelAndView item(Pagination pager, TjItem info){
		ModelAndView mav = getModelAndView("/report/item/list");
		ListResult<TjItem> listResult = itemService.query(pager.getStartRow(), pager.getPageSize(), info);
		
		//设置总数
		pager.setTotalRecord(listResult.getCount());
		//分页数据
		mav.addObject("list", listResult.getList());
		//分页参数
		mav.addObject("pager", pager);
		mav.addObject("info", info);
		
		return mav;
	}
	
	/**
	 * 分页查询
	 * @param pager
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/itemlist")
	public ModelAndView itemList(Pagination pager, TjItem info){
		ModelAndView mav = getModelAndView("/report/item/list");
		ListResult<TjItem> listResult = itemService.query(pager.getStartRow(), pager.getTotalPage(), info);
		//查询所有分类
		List<TjItemCategory> itemCateList = service.queryList(null);
		mav.addObject("itemCateList", itemCateList);
		//设置总数
		pager.setTotalRecord(listResult.getCount());
		//分页数据
		mav.addObject("list", listResult.getList());
		//分页参数
		mav.addObject("pager", pager);
		mav.addObject("info", info);
		
		return mav;
	}
	
	
	
}
