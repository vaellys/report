package com.reps.tj.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.reps.tj.entity.TjTableDefine;
import com.reps.tj.entity.TjTableItem;
import com.reps.tj.service.ITjItemCategoryService;
import com.reps.tj.service.ITjItemService;
import com.reps.tj.service.ITjTableDefineService;
import com.reps.tj.service.ITjTableItemService;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/report/tabledefine")
public class TjTableDefineAction extends BaseAction {
	
	@Autowired
	ITjTableDefineService service;
	
	@Autowired
	ITjTableItemService tabItemService;
	
	@Autowired
	ITjItemService itemService;
	
	@Autowired
	ITjItemCategoryService itemCateService;
	
	protected final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 主页面
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index(TjTableDefine info){
		ModelAndView mav = getModelAndView("/report/tabledefine/index");
		List<TjTableDefine> list = service.query(info);
		
		//将统计表分为基础表和业务表两种类型。
		TjTableDefine tjTableRootBase = new TjTableDefine();//基础表
		tjTableRootBase.setId("1");
		tjTableRootBase.setChineseName("基础表");
		tjTableRootBase.setTjTableDefineTypeId("-1");
		TjTableDefine tjTableRootYeWu = new TjTableDefine();//业务表
		tjTableRootYeWu.setId("0");
		tjTableRootYeWu.setChineseName("业务表");
		tjTableRootYeWu.setTjTableDefineTypeId("-1");
		for(TjTableDefine t : list){
			if(t.getIsBasic()==1){//是基础表类型
				t.setTjTableDefineTypeId("1");
			}else{//是业务表类型
				t.setTjTableDefineTypeId("0");
			}
		}
		list.add(tjTableRootBase);
		list.add(tjTableRootYeWu);
		
		
		/*TjTableDefine rootTabDefine = new TjTableDefine();
		rootTabDefine.setId("-1");
		rootTabDefine.setChineseName("统计表列表");
		list.add(rootTabDefine);*/
		
		mav.addObject("treelist", list);
		
		return mav;
	}
	
	/**
	 * 分页查询
	 * @param pager
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, TjTableDefine info){
		ModelAndView mav = getModelAndView("/report/tabledefine/list");
		ListResult<TjTableDefine> listResult = service.query(pager.getStartRow(), pager.getPageSize(), info);
		
		//设置总数
		pager.setTotalRecord(listResult.getCount());
		//分页数据
		mav.addObject("list", listResult.getList());
		//分页参据
		mav.addObject("pager", pager);
		mav.addObject("info", info);
		
		return mav;
		
	}
	
	/**
	 * 跳转到添加对象界面
	 * @return
	 */
	@RequestMapping(value = "/toadd")
	public ModelAndView toAdd(){
		ModelAndView mav = getModelAndView("/report/tabledefine/add");
		
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
	public Object add(TjTableDefine info) throws RepsException{
		if(info == null){
			throw new RepsException("数据不完整");
		}
		
		//要判断表名重复问题。
		if(StringUtil.isNotBlank(info.getName())){
			TjTableDefine tableDefine = service.getTjTableDefineByName(info.getName());
			if(tableDefine!=null&&tableDefine.getName().equals(info.getName())){
				return ajax(AjaxStatus.FAIL,"数据表名已存在！");
			}
		}
		
		service.save(info);
		
		//return ajax(AjaxStatus.OK, "添加成功", info.getId());
		return ajax(AjaxStatus.OK, "添加成功");
	}
	
	/**
	 * 编辑页面的跳转
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String id){
		ModelAndView mav = getModelAndView("/report/tabledefine/edit");
		TjTableDefine tableDefine = service.get(id, false);
		mav.addObject("tableDefine", tableDefine);
		TjTableItem tableItem = new TjTableItem();
		tableItem.setTjTableDefine(tableDefine);
		List<TjTableItem> tableItemList = tabItemService.query(tableItem);
		mav.addObject("tableItemList", tableItemList);
		
		return mav;
	}
	
	/**
	 * 编辑页面的跳转
	 * @return
	 */
	@RequestMapping(value = "/toedititem")
	public ModelAndView toEditItem(String id, String tid){
		ModelAndView mav = getModelAndView("/report/tabledefine/edititem");
		TjTableItem tableItem = tabItemService.get(id);
		mav.addObject("tableItem", tableItem);
		mav.addObject("tid", tid);
		return mav;
	}
	
	@RequestMapping(value = "/edititem")
	@ResponseBody
	public Object editItem(TjTableItem info) throws RepsException{
		if(info == null){
			throw new RepsException("数据不完整");
		}
		
		TjTableItem tableItem = tabItemService.get(info.getId());
		if(tableItem != null){
			info.setTjTableDefine(tableItem.getTjTableDefine());
			tabItemService.update(info);
		}
		
		return ajax(AjaxStatus.OK, "修改成功");
	}
	
	/**
	 * 删除指定指标项
	 */
	@RequestMapping(value = "/deleteitem")
	@ResponseBody
	public Object deleteItem(String id){
		try{
			TjTableItem tableItem = tabItemService.get(id);
			if(tableItem != null){
				tabItemService.delete(tableItem);
			}
			return ajax(AjaxStatus.OK, "删除成功");
		}catch(Exception e){
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}
	
	/**
	 * 修改指定对象
	 * @param info
	 * @return
	 * @throws RepsException
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(TjTableDefine info) throws RepsException{
		if(info == null){
			throw new RepsException("数据不完整");
		}
		
		//要判断表名重复问题。
		if(StringUtil.isNotBlank(info.getName())){
			TjTableDefine tableDefine = service.getTjTableDefineByName(info.getName());
			if(tableDefine!=null&&!tableDefine.getId().equals(info.getId())&&tableDefine.getName().equals(info.getName())){
				return ajax(AjaxStatus.FAIL,"数据表名已存在！");
			}
			
		}
		
		service.update(info);
		
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
			TjTableDefine tableDefine = service.get(id, true);
			if(tableDefine != null){
				service.delete(tableDefine);
			}
			return ajax(AjaxStatus.OK, "删除成功");
		}catch(Exception e){
			return ajax(AjaxStatus.ERROR, "删除失败");
		}
	}
	
	/**
	 * 创建数据表
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/createtable")
	@ResponseBody
	public Object createTable(String id){
		try{
			TjTableDefine tableDefine = service.get(id, true);
			if(tableDefine != null){
				service.createTable(id);
			}
			return ajax(AjaxStatus.OK, "报表创建成功");
		}catch(Exception e){
			return ajax(AjaxStatus.ERROR, "报表创建失败");
		}
	}
	

	/**
	 * 点击树节点显示对象的详细信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/show")
	public ModelAndView show(String id){
		ModelAndView mav = getModelAndView("/report/tabledefine/show");

		//显示统计表的详细信息(包括所含的指标项)
		TjTableDefine tableDefine = service.get(id, true);
		
		mav.addObject("tableDefine", tableDefine);
		
		return mav;
	}
	
	/**
	 * 跳转到添加指标项页面
	 * @return
	 */
	@RequestMapping(value = "/toitemlist")
	public ModelAndView toItemList(Pagination pager, String tId, TjItem info){
		ModelAndView mav = getModelAndView("/report/tabledefine/itemlist");
		ListResult<TjItem> listResult = itemService.query(pager.getStartRow(), pager.getPageSize(), info);
		
		//设置总数
		pager.setTotalRecord(listResult.getCount());
		//分页数据
		mav.addObject("list", listResult.getList());
		//分页参据
		mav.addObject("pager", pager);
		mav.addObject("info", info);
		mav.addObject("tId", tId);
		return mav;
	}
	
	/**
	 * 返回所有的指标项
	 * @return
	 */
	@RequestMapping(value = "/getitemlist", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public Object getItemList(){
		List<TjItem> list = itemService.getAllItem();
		Set<String> set = new HashSet<String>();
		TjItemCategory itemCategory = null;
		StringBuffer treeStr = new StringBuffer();
		treeStr.append("[");
		
		for(TjItem t : list){
			treeStr.append("{\"id\":").append("\""+t.getId()+"\",pId:").append("\""+t.getCategoryId()+"\",tableName:").append("\""+t.getName()+"\",name:").append("\""+t.getChineseName()+"\",\"open\":true},");
			set.add(t.getCategoryId());
		}
		
		for(String s : set){
			itemCategory = itemCateService.get(s);
			treeStr.append("{\"id\":").append("\""+itemCategory.getId()+"\",pId:0,name:").append("\""+itemCategory.getName()+"\",\"open\":true},");
		}
		treeStr = treeStr.delete(treeStr.length()-1, treeStr.length());
		treeStr.append("]");

		return treeStr.toString();
	}
	
	
	/**
	 * 跳转到添加指标项页面
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/additem2")
	public ModelAndView addItem2(String id){
		ModelAndView mav = getModelAndView("/report/tabledefine/additem");
		TjTableDefine tableDefine = service.get(id, true);
		mav.addObject("tableDefine", tableDefine);
		mav.addObject("itemCount", tableDefine.getItems().size());
		
		return mav;
	}
	
	/**
	 * 添加指标项
	 * @param tableItem
	 * @return
	 * @throws RepsException
	 */
	@RequestMapping(value = "/saveitem")
	@ResponseBody
	public Object saveTableItem(String data,String tableId) throws RepsException{
		if (StringUtil.isBlank(data) && StringUtil.isBlank(tableId)) {
	      throw new RepsException("非法请求");
	    }
		
		try
		{
			//保存之前，要把已经添加的指标项都删除。
			TjTableItem tjTableItem = new TjTableItem();
			tjTableItem.setTjTableDefine(new TjTableDefine());
			tjTableItem.getTjTableDefine().setId(tableId);
			
			List<TjTableItem> tjTableItemList = tabItemService.query(tjTableItem);
			for(TjTableItem t : tjTableItemList){
				tabItemService.delete(t);
			}
			
			JSONArray jsons = JSONArray.fromObject(data);
	//		String[] arr=new String[jsons.size()];
			for(int i=0;i<jsons.size();i++){
				JSONArray jsonArr = jsons.getJSONArray(i);
				for(int j=0;j<jsonArr.size();j++){
					JSONObject jsonObj = jsonArr.getJSONObject(j);
					TjTableItem tableItem = new TjTableItem();
					tableItem.setTjTableDefine(new TjTableDefine());
					tableItem.getTjTableDefine().setId(tableId);
					tableItem.setItemName((String)jsonObj.get("itemName"));
					String batchKey = (String)jsonObj.get("batchKey");
					if(StringUtil.isBlank(batchKey)){
						batchKey = "0";//设为默认值
					}
					String isTemporary = (String)jsonObj.get("isTemporary");
					if(StringUtil.isBlank(isTemporary)){
						isTemporary = "0";//当没有添加临时指标项时，isTemporary设为默认值。
					}
					tableItem.setIsTemporary(Short.parseShort(isTemporary));
					tableItem.setBatchKey(Short.parseShort(batchKey));
					tableItem.setItemChineseName((String)jsonObj.get("itemChineseName"));
					tableItem.setFieldType((String)jsonObj.getString("fieldType"));
					tableItem.setFieldLength((String)jsonObj.getString("fieldLength"));
					tableItem.setExpressionType((String)jsonObj.getString("expressionType"));
					tableItem.setDependency((String)jsonObj.getString("dependency"));
					tableItem.setValuedColumn((String)jsonObj.getString("valuedColumn"));
					tableItem.setExpression((String)jsonObj.getString("expression"));
					tableItem.setStatOrder(Short.parseShort(jsonObj.getString("orders")));
					
					tabItemService.save(tableItem);
				}
				
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "success";
	}
	
	/**
	 * 通过指标id得到该指标的所有信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/chooseitem")
	@ResponseBody
	public TjItem chooseItem(String id){
		TjItem item = itemService.get(id);
		
		return item;
	}
	
	/**
	 * 通过统计表id得到已添加的指标
	 */
	@RequestMapping(value = "/getallitembytableid")
	@ResponseBody
	public List<TjTableItem> getAllItemByTableId(String tableId){
		List<TjTableItem> tableItemList = null;
		if(StringUtil.isNotBlank(tableId)){
			TjTableItem tableItem = new TjTableItem();
			tableItem.setTjTableDefine(new TjTableDefine());
			tableItem.getTjTableDefine().setId(tableId);
			tableItemList = tabItemService.query(tableItem);
		}
		return tableItemList;
	}

//	public Object getAllItemByTableId(String tableId){
//		List<TjTableItem> tableItemList = null;
//		if(StringUtil.isNotBlank(tableId)){
//			TjTableItem tableItem = new TjTableItem();
//			tableItem.setTjTableDefine(new TjTableDefine());
//			tableItem.getTjTableDefine().setId(tableId);
//			tableItemList = tabItemService.query(tableItem);
//		}
//		JSONObject jsonObject = new JSONObject();
//		JSONArray jsonArray = new JSONArray();
//		if (tableItemList != null){
//			jsonArray = JSONArray.fromObject(tableItemList);
//		}
//		jsonObject.put("data", jsonArray.toString());
//		
//		return jsonObject;
//	}
	
	
	/**
	 * 通过统计表id获得该统计表中所有的指标名称集合。
	 * @param tableId
	 * @return
	 */
	@RequestMapping(value="/getallitemname")
	@ResponseBody
	public List<String> getAllItemNameByTableId(String tableId){
		TjTableItem tableItem = new TjTableItem();
		tableItem.setTjTableDefine(new TjTableDefine());
		tableItem.getTjTableDefine().setId(tableId);
		List<TjTableItem> TjTableItemList = tabItemService.query(tableItem);
		List<String> itemNameList = new ArrayList<String>();
		
		for(TjTableItem t : TjTableItemList){
			itemNameList.add(t.getItemName());
		}
		
		return itemNameList;
	}
}
