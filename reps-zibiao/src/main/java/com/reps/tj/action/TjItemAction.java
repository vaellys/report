package com.reps.tj.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.reps.system.dict.entity.DictionaryDefine;
import com.reps.system.dict.service.IDictionaryService;
import com.reps.tj.entity.TjItem;
import com.reps.tj.entity.TjItemCategory;
import com.reps.tj.service.ITjItemCategoryService;
import com.reps.tj.service.ITjItemService;

/**
 * 指标相关操作
 */
@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/report/item")
public class TjItemAction extends BaseAction {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	ITjItemService itemService;
	
	@Autowired
	ITjItemCategoryService categoryService;
	
	@Autowired
	//IDataDictionaryService dataDictionaryService;
	
	IDictionaryService dataDictionaryService;
	
	/**
	 * 分页查询
	 * @param pager
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, TjItem info){
		ModelAndView mav = getModelAndView("/report/item/list");
		ListResult<TjItem> listResult = itemService.query(pager.getStartRow(), pager.getPageSize(), info);
		//查询所有分类
		List<TjItemCategory> categoryList = categoryService.queryList(null);
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "");
		for(TjItemCategory t : categoryList){
			map.put(t.getId(), t.getName());
		}
		
		mav.addObject("categoryList", map);
		//设置总数
		pager.setTotalRecord(listResult.getCount());
		//分页数据
		mav.addObject("itemList", listResult.getList());
		//分页参数
		mav.addObject("pager", pager);
		mav.addObject("info", info);
		
		return mav;
	}
	
	/**
	 * 根据指标分类id添加指标
	 * @param categoryId
	 * @return
	 * @throws RepsException 
	 */
	@RequestMapping(value = "/toadd")
	public ModelAndView toAdd(String categoryId) throws RepsException{
		ModelAndView mav = getModelAndView("/report/item/add");
		Map<String,String> dictionaryDefineMap = new HashMap<String,String>();
		
		TjItemCategory category = categoryService.get(categoryId);
		mav.addObject("category", category);
		
		List<TjItem> itemCateList = itemService.getItemsOfCategory(categoryId);
		mav.addObject("showOrder", itemCateList.size()+1);
		
		List<DictionaryDefine> dictionaryDefineList = dataDictionaryService.queryDefineTable();
		for(DictionaryDefine d : dictionaryDefineList){
			dictionaryDefineMap.put(d.getDictName(), d.getChineseName());
		}
		mav.addObject("dictionaryDefineMap", dictionaryDefineMap);
		return mav;
	}
	
	/**
	 * 添加对象
	 * @param item
	 * @return
	 * @throws RepsException
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(TjItem item) throws RepsException{
		if(item == null){
			throw new RepsException("数据不完整");
		}
		
		if(item.getTjItemCategory() == null || StringUtil.isBlank(item.getTjItemCategory().getId())){
			return ajax(AjaxStatus.FAIL, "添加失败，请选择指标分类！");
		}
		
		if(StringUtil.isNotBlank(item.getName())){
			TjItem tjItem = itemService.getItemByName(item.getName());
			if(tjItem!=null&&tjItem.getName().equals(item.getName())){
				return ajax(AjaxStatus.FAIL, "指标名称已存在，请重新输入！");
			}
		}
		if(item.getIsDictionary() == null){//默认设置为否
			item.setIsDictionary((short) 0);
		}
		
		
		//item.setShowOrder(items.size()+1);//设置显示顺序
		itemService.save(item);
		
		return ajax(AjaxStatus.OK, "添加成功");
	}
	
	/**
	 * 编辑页面的跳转
	 * @param id
	 * @return
	 * @throws RepsException 
	 */
	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String id) throws RepsException{
		ModelAndView mav = getModelAndView("/report/item/edit");
		Map<String,String> dictionaryDefineMap = new HashMap<String,String>();
		TjItem tjItem = itemService.get(id);
		List<TjItemCategory> cateList = categoryService.queryList(null);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "");
		for(TjItemCategory t : cateList){
			map.put(t.getId(), t.getName());
		}
		
		List<DictionaryDefine> dictionaryDefineList = dataDictionaryService.queryDefineTable();
		for(DictionaryDefine d : dictionaryDefineList){
			dictionaryDefineMap.put(d.getDictName(), d.getChineseName());
		}
		mav.addObject("dictionaryDefineMap", dictionaryDefineMap);
		
		mav.addObject("tjItem", tjItem);
		mav.addObject("cateList", map);
		
		return mav;
	}
	
	/**
	 * 修改对象
	 * @param info
	 * @return
	 * @throws RepsException
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(TjItem info) throws RepsException{
		if(info == null){
			throw new RepsException("数据不完整");
		}
		
		if(StringUtil.isNotBlank(info.getName())){
			TjItem tjItem = itemService.getItemByName(info.getName());
			if(tjItem!=null&&!info.getId().equals(tjItem.getId())&&tjItem.getName().equals(info.getName())){
				return ajax(AjaxStatus.FAIL, "指标名称已存在，请重新输入！");
			}
		}
		
		if(info.getIsDictionary() == null){//默认设置为否
			info.setIsDictionary((short) 0);
		}
		
		itemService.update(info);
		
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
			TjItem tjItem = itemService.get(id);
			if(tjItem != null){
				itemService.delete(tjItem);
			}
			return ajax(AjaxStatus.OK, "删除成功");
		}catch(Exception e){
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
		
	}
	
	/**
	 * 点击树节点显示分类下的详细信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/show")
	public ModelAndView show(String id){
		ModelAndView mav = getModelAndView("/report/item/show");
//		TjItemCategory itemCate = itemCateService.get(id);
//		List<TjItem> itemList = service.getItemsOfCategory(id);
		
		StringBuffer tempPath = new StringBuffer();//目录
		String path = "";
		
		TjItem item = itemService.get(id);
		tempPath.insert(0, "/"+item.getChineseName());
		
		List<TjItemCategory> itemCateList = categoryService.queryList("");
		TjItemCategory parentItemCate = categoryService.get(item.getCategoryId());
		tempPath.insert(0, "/"+parentItemCate.getName());
		
		boolean flax = true;
		
		while(flax){
			if(parentItemCate != null && !"-1".equals(parentItemCate.getParentId())){
				parentItemCate = categoryService.get(parentItemCate.getParentId());
				tempPath.insert(0, "/"+parentItemCate.getName());
			}else{
				flax = false;
			}
		}
		
		path = tempPath.substring(1, tempPath.length());
		
		mav.addObject("path", path);
		mav.addObject("info", item);
		mav.addObject("itemCateList", itemCateList);
		
		return mav;
	}
	
}
