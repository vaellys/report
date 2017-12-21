package com.reps.tj.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.reps.core.dictionary.DictionaryItem;
import com.reps.core.dictionary.DictionaryPool;
import com.reps.core.exception.RepsException;
import com.reps.core.util.StringUtil;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.tj.entity.TjTableDefine;
import com.reps.tj.entity.TjTableItem;
import com.reps.tj.service.ITjItemCategoryService;
import com.reps.tj.service.ITjItemService;
import com.reps.tj.service.ITjTableDataService;
import com.reps.tj.service.ITjTableDefineService;
import com.reps.tj.service.ITjTableItemService;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/report/tabledata")
public class TjTableDataAction extends BaseAction {
	
	@Autowired
	ITjTableDataService service;
	
	@Autowired
	ITjTableDefineService defineService;
	
	@Autowired
	ITjTableItemService tabItemService;
	
	@Autowired
	ITjItemService itemService;
	
	@Autowired
	ITjItemCategoryService itemCateService;
	
	@Autowired
	ITjTableItemService tjTableItemService;
	
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/index")
	public ModelAndView index(){
		ModelAndView mav = getModelAndView("/report/tabledata/index");
		List<TjTableDefine> list = defineService.query(null);
		mav.addObject("treelist", list);
		return mav;
	}
	
	@RequestMapping(value = "/right")
	public ModelAndView right(){
		ModelAndView mav = getModelAndView("/report/tabledata/right");
		return mav;
	}
	
	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager,String did){
		ModelAndView mav = getModelAndView("/report/tabledata/list");
		TjTableDefine define = defineService.get(did, true);
		List<Map<String,Object>> list = service.queryForList(pager.getStartRow(), pager.getPageSize(),did);
		List<Map<String,Object>> list1 = new ArrayList<Map<String, Object>>();
		if (list!=null) {
			for (Map<String, Object> map : list) {
				Map<String,Object> map1 = new LinkedHashMap<String, Object>();
				List<TjTableItem> items = define.getItems();
				for (TjTableItem tjTableItem : items) {
					if(map.containsKey(tjTableItem.getItemName())){
						map1.put(tjTableItem.getItemChineseName(), map.get(tjTableItem.getItemName()));
					}
				}
				if (map.containsKey("id")) {
					map1.put("id", map.get("id"));
				}
				list1.add(map1);
			}
		}
		mav.addObject("define", define);
		pager.setTotalRecord(list1!=null?list1.size():0);
		mav.addObject("list", list1);
		mav.addObject("pager", pager);
		return mav;
	}
	
	@RequestMapping(value = "/toadd")
	public ModelAndView toAdd(String did) throws RepsException{
		ModelAndView mav = getModelAndView("/report/tabledata/add");
		if(StringUtil.isBlank(did)){
			throw new RepsException("数据不完整");
		}
		TjTableDefine define  = defineService.get(did, true);
		Map<String, TjTableItem> map = null;
		if (define!= null) {
			List<TjTableItem> itemList = define.getItems();
			if (itemList!=null) {
				map = new LinkedHashMap<String, TjTableItem>(itemList.size());
				TjTableItem tableItem = null;
				Map<String,List<DictionaryItem>> dictMap = new HashMap<String,List<DictionaryItem>>();
				for(TjTableItem item : itemList){
					tableItem = tjTableItemService.get(item);
					if(tableItem.getIsDictionary()!=null&&tableItem.getReferDictionary()!=null&&tableItem.getIsDictionary()==1&&StringUtil.isNotBlank(tableItem.getReferDictionary())){
						String dictName = tableItem.getReferDictionary();//reps_dict_xxxxxxxx
						//String newName = dictName.replaceFirst("reps_dict_", "");//去掉reps_dict_
						String newName = dictName;
						List<DictionaryItem> dictionaryList = DictionaryPool.getItems(newName);
						tableItem.setReferDictionary(newName);
						dictMap.put(tableItem.getItemName(), dictionaryList);
					}
					map.put(tableItem.getItemChineseName(),tableItem);
				}
				if(dictMap.size()>0){
					mav.addObject("dictMap", dictMap);
				}
			}
		}
		mav.addObject("map", map);
		mav.addObject("define", define);
		return mav;
	}
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(HttpServletRequest request) throws RepsException {
		try {
			String did = request.getParameter("did");
			String p = request.getParameter("columndata");
			if(StringUtil.isBlank(did) || StringUtil.isBlank(p)){
				throw new RepsException("数据不完整");
			}
			String columndata = URLDecoder.decode(p, "UTF-8");
			service.add(did, columndata);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ajax(AjaxStatus.ERROR, "添加失败");
		}
	}
	
	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String did,String id) throws RepsException{
		ModelAndView mav = getModelAndView("/report/tabledata/edit");
		if(StringUtil.isBlank(did) || StringUtil.isBlank(id)){
			throw new RepsException("数据不完整");
		}
		TjTableDefine define  = defineService.get(did, true);
		Map<String, TjTableItem> map = null;
		if (define!= null) {
			List<TjTableItem> itemList = define.getItems();
			if (itemList!=null) {
				//List<Object> list = service.get(id,define.getName());
				Map<String,Object> maps = service.queryMap(id,define.getName());
				map = new LinkedHashMap<String, TjTableItem>(itemList.size());
				TjTableItem tableItem = null;
				Map<String,List<DictionaryItem>> dictMap = new HashMap<String,List<DictionaryItem>>();
				for (TjTableItem item : itemList) {
					tableItem = tjTableItemService.get(item);
					if(tableItem.getIsDictionary()!=null&&tableItem.getReferDictionary()!=null&&tableItem.getIsDictionary()==1&&StringUtil.isNotBlank(tableItem.getReferDictionary())){
						String dictName = tableItem.getReferDictionary();//reps_dict_xxxxxxxx
						//String newName = dictName.substring(10, dictName.length());//去掉reps_dict_
						String newName = dictName;
						List<DictionaryItem> dictionaryList = DictionaryPool.getItems(newName);
						tableItem.setReferDictionary(newName);
						dictMap.put(tableItem.getItemName(), dictionaryList);
					}
					if (maps.containsKey(item.getItemName())) {
						item.setItemValue(maps.get(item.getItemName()).toString());
						map.put(item.getItemChineseName(),item);
					}
				}
				if(dictMap.size()>0){
					mav.addObject("dictMap", dictMap);
				}
			}
		}
		mav.addObject("define", define);
		mav.addObject("id", id);
		mav.addObject("map", map);
		return mav;
	}
	
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(HttpServletRequest request) throws RepsException{
		try {
			String did = request.getParameter("did");
			String id = request.getParameter("id");
			String p = request.getParameter("columndata");
			if(StringUtil.isBlank(did) || StringUtil.isBlank(id) || StringUtil.isBlank(p)){
				throw new RepsException("数据不完整");
			}
			String columndata = URLDecoder.decode(p, "UTF-8");
			service.update(did, id, columndata);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ajax(AjaxStatus.ERROR, "修改失败");
		}
	}
	
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(String did,String id) throws RepsException{
		if(StringUtil.isBlank(did) || StringUtil.isBlank(id)){
			throw new RepsException("数据不完整");
		}
		try{
			TjTableDefine define = defineService.get(did, true);
			if(define != null){
				service.delete(id, define.getName());
			}
			return ajax(AjaxStatus.OK, "删除成功");
		}catch(Exception e){
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}
	
}
