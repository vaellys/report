package com.reps.portal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reps.core.RepsConstant;
import com.reps.core.RepsContext;
import com.reps.core.dictionary.DictionaryItem;
import com.reps.core.dictionary.DictionaryPool;
import com.reps.core.util.DateUtil;
import com.reps.tj.entity.TjTableDefine;
import com.reps.tj.service.ITjDataQueryService;
import com.reps.tj.service.ITjTableDefineService;

/**
 * 基础数据
 */
@Controller
@RequestMapping(value = "/basedata")
public class BaseDataAction {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ITjTableDefineService tjTableDefineService;
	
	@Autowired
	ITjDataQueryService dataQueryService;

	
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/list")
	public ModelAndView list(String type, String year) throws Exception {
		ModelAndView mav = new ModelAndView("/basedata/list");
		List<Map<String, Object>> list;
		
		String area = RepsConstant.getContextProperty("global.area");
		mav.addObject("area", area);
		
		//基础数据概览
		if(StringUtils.isBlank(type)){
			String tableName = "reps_tj_data_base_yxjsszs";
			String[] fields = new String[]{};
			String[] excludeFields = new String[]{"year", "district"};
			
			list = dataQueryService.getSumByTableName(tableName, fields, excludeFields, null, new String[]{}, null, null);
			
			if (list!=null && !list.isEmpty()){
				Map<String, Object> map = list.get(0);
				
				for(Map.Entry<String, Object> entry : map.entrySet()){
					mav.addObject(entry.getKey(), entry.getValue());
				}
			}
			return mav;
		}
		
		mav.setViewName("/basedata/"+type);
		
		//中小学统计
        String dataTable = "reps_tj_data_zxxtj";
		
		//当年数据
		String currentYear = StringUtils.isBlank(year) ? DateUtil.getCurDateTime("yyyy") : year;
		
		mav.addObject("year", currentYear);

		List<Map<String, Object>> qxList = null;
		//区县一级的平台，遍历所有乡镇
		if ("county".equals(area)){
			qxList = dataQueryService.query("select distinct town from "+dataTable
					+" where year='"+currentYear+"' order by town", new ArrayList<Object>());
		}
		else{//地市或以上一级的平台，遍历所有区县
			qxList = dataQueryService.query("select distinct district from "+dataTable
					+" where year='"+currentYear+"' order by distinct", new ArrayList<Object>());
		}
		
		List<String> xueQu = new ArrayList<String>();
		List<Object> xiaoXueData = new ArrayList<Object>();
		List<Object> chuZhongData = new ArrayList<Object>();
		List<Object> gaoZhongData = new ArrayList<Object>();
		List<String> legendData = new ArrayList<String>();
		
		List<Map<String,Object>> allDistrictNameStudentCount = new ArrayList<Map<String, Object>>();
		
		if ("qxsjtj".equals(type)){	//区县数据统计
			List<Map<String, Object>> listTjgl = dataQueryService.query(dataTable, "where year="+currentYear, new ArrayList<Object>());
			
			for (int i=0; i<qxList.size(); i++){
				Map<String, Object> quMap = qxList.get(i);
				
				for (String key : quMap.keySet()) {
					String quXianName = (String) quMap.get(key);
					long studentCount = 0;
					long teacherCount = 0;
					long schoolCount = 0;
					long classCount = 0;
					Map<String, Object> countMap = new HashMap<String, Object>();
					
					for(Map<String, Object> m : listTjgl){
						String scope = "county".equals(area) ? m.get("town").toString() : m.get("district").toString();
						if(quXianName.equals(scope)){
							//学生数量
							Object objCount = m.get("student_count");
							if(null!=objCount){
								studentCount += Long.parseLong(objCount.toString());
							}
							//教师数量
							objCount = m.get("teacher_count");
							if(null!=objCount){
								teacherCount += Long.parseLong(objCount.toString());
							}
							//学校数量
							objCount = m.get("school_count");
							if(null!=objCount){
								schoolCount += Long.parseLong(objCount.toString());
							}
							//班级数量
							objCount = m.get("classes_count");
							if(null!=objCount){
								classCount += Long.parseLong(objCount.toString());
							}
						}
					}
					if ("county".equals(area)){
						countMap.put("town", RepsContext.getDictionaryItemName("town", quXianName));
					}
					else{
						countMap.put("district", RepsContext.getDictionaryItemName("district", quXianName));
					}
					countMap.put("student_count", studentCount);
					countMap.put("teacher_count", teacherCount);
					countMap.put("school_count", schoolCount);
					countMap.put("classes_count", classCount);
					allDistrictNameStudentCount.add(countMap);
				}
			}

			JSONArray jsonArray = new JSONArray();
			String listStr = jsonArray.fromObject(allDistrictNameStudentCount).toString();
			mav.addObject("list", listStr);
		}else{
			for (int i=0; i<qxList.size(); i++){
				Map<String, Object> quMap = qxList.get(i);
				for (String key : quMap.keySet()) {
					String quXianName = (String) quMap.get(key);
					List<Map<String, Object>> listXueDuan = null;
					if ("county".equals(area)){
						listXueDuan = dataQueryService.query(dataTable, "where year="+currentYear+" and town='"+quXianName+"'", new ArrayList<Object>());
					}
					else{
						listXueDuan = dataQueryService.query(dataTable, "where year="+currentYear+" and district='"+quXianName+"'", new ArrayList<Object>());
					}
					if ("county".equals(area)){
						xueQu.add(RepsContext.getDictionaryItemName("town", quXianName));
					}
					else{
						xueQu.add(RepsContext.getDictionaryItemName("district", quXianName));
					}
					int xiaoXue=0, chuZhong=0, gaoZhong=0;

					for(Map<String,Object> map : listXueDuan){
						String lb = map.get("school_lb").toString().substring(0,2);
						String querykey = "teacher_count";
						
						//学生数量
						if("xssltj".equals(type)){
							querykey = "student_count";
						}
						//班级数量
						if("bjsltj".equals(type)){
							querykey = "classes_count";
						}
						//学校数量统计
						if("xxsltj".equals(type)){
							querykey = "school_count";
						}
						
						//小学
						if("2".equals(lb.substring(0,1))){
							xiaoXue += Integer.parseInt(map.get(querykey).toString());
						}
						//初中
						if("30".equals(lb) || "31".equals(lb) || "32".equals(lb) || "33".equals(lb)){
							chuZhong += Integer.parseInt(map.get(querykey).toString());
						}
						//高中
						if("34".equals(lb) || "35".equals(lb)){
							gaoZhong += Integer.parseInt(map.get(querykey).toString());
						}
					}
					xiaoXueData.add(xiaoXue);
					chuZhongData.add(chuZhong);
					gaoZhongData.add(gaoZhong);
			    }
			}
		}

		if(StringUtils.isNotBlank(type)){
			List<Map<String, Object>> series = new ArrayList<Map<String, Object>>();
			
			if(null!=xiaoXueData && xiaoXueData.size()>0){
				legendData.add("小学");
				Map<String, Object> serieMap = new HashMap<String, Object>();
				serieMap.put("name", "小学");
				serieMap.put("type", "bar");
				serieMap.put("stack", "总量");
				//{ normal: {label : {show: true, position: 'insideRight'}}},
				//String normal = "{ normal:{label : {show: true, position: 'insideRight'}}}";
				//serieMap.put("itemStyle", normal);
				serieMap.put("data", xiaoXueData);
				series.add(serieMap);
			}
			
			if(null!=chuZhongData && chuZhongData.size()>0){
				legendData.add("初中");
				Map<String, Object> serieMap = new HashMap<String, Object>();
				serieMap.put("name", "初中");
				serieMap.put("type", "bar");
				serieMap.put("stack", "总量");
				//{ normal: {label : {show: true, position: 'insideRight'}}},
				//String normal = "{ normal:{label : {show: true, position: 'insideRight'}}}";
				//serieMap.put("itemStyle", normal);
				serieMap.put("data", chuZhongData);
				series.add(serieMap);
			}
			
			if(null!=gaoZhongData && gaoZhongData.size()>0){
				legendData.add("高中");
				Map<String, Object> serieMap = new HashMap<String, Object>();
				serieMap.put("name", "高中");
				serieMap.put("type", "bar");
				serieMap.put("stack", "总量");
				//{ normal: {label : {show: true, position: 'insideRight'}}},
				//String normal = "{ normal:{label : {show: true, position: 'insideRight'}}}";
				//serieMap.put("itemStyle", normal);
				serieMap.put("data", gaoZhongData);
				series.add(serieMap);
				
			}
			
			list = dataQueryService.query(dataTable, "where year = "+currentYear, new ArrayList<Object>());
			
//			JSONArray jsonArray = new JSONArray();
//			json = jsonArray.fromObject(list).toString();
			
			JSONArray jsonArray = new JSONArray();
			String xueQujson = jsonArray.fromObject(xueQu).toString();
			mav.addObject("xueQujson", xueQujson);
			String legendjson = jsonArray.fromObject(legendData).toString();
			mav.addObject("legendjson", legendjson);
			String seriesjson = jsonArray.fromObject(series).toString();
			mav.addObject("seriesjson", seriesjson);
			
			mav.addObject("xiaoXueData", xiaoXueData);
			mav.addObject("chuZhongData", chuZhongData);
			mav.addObject("gaoZhongData", gaoZhongData);
		}else{
			/*mav.addObject("studentCount", studentCount);
			mav.addObject("teacherCount", teacherCount);
			mav.addObject("schoolCount", schoolCount);
			mav.addObject("classCount", classCount);*/
		}
		
		return mav;
	}
	
	/**
	 * 历年各学段师生数量
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/lngxdsssl")
	public ModelAndView lngxdsssl(String dataTable) throws Exception{
		ModelAndView mav = new ModelAndView("/basedata/lngxdsssl");
		
		dataTable = "reps_tj_data_" + dataTable;
		JSONArray jsonArray = new JSONArray();
		
		//统计各学段最近学年的学校数量和学生数量
		String[] excludeFields = new String[]{"year", "school_lb"};
		String[] groupBy = new String[]{"year", "school_lb"};
		String whereSql = "year in (select max(year) from " + dataTable + ")";
		List<Map<String, Object>> list = dataQueryService.getSumByTableName(dataTable, new String[]{}, excludeFields, null, groupBy, null, whereSql);
		
		String json = jsonArray.fromObject(list).toString();
		mav.addObject("json", json);

		//
		TjTableDefine tjTableDefine = tjTableDefineService.getTjTableDefineByName(dataTable);
		
		List<Map<String, String>> itemNameList = new ArrayList<Map<String, String>>();
		itemNameList = tjTableDefine.getItemChineseNameList(new String[]{}, new String[]{"year"});
		mav.addObject("itemNameList", jsonArray.fromObject(itemNameList).toString());
		
		//学校类别字典
		Map<String, String> mapCode = new HashMap<String, String>();
		List<DictionaryItem> dictionaryItems = DictionaryPool.getItems("school_type");

		for(DictionaryItem d : dictionaryItems){
			mapCode.put(d.getCode(), d.getName());
		}
		mav.addObject("mapCode", jsonArray.fromObject(mapCode).toString());
		
		return mav;
	}
	
	/**
	 * 近5年中小学学生数量变化情况
	 * @param dataTable
	 * @param topage 转向页面
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/fiveyearstudentcount")
	public ModelAndView fiveYearStudentCount(String dataTable, String topage) throws Exception{
		//ModelAndView mav = new ModelAndView("/report/fiveyearstudentcount");
		ModelAndView mav = new ModelAndView(topage);
		
		dataTable = "reps_tj_data_" + dataTable;
		JSONArray jsonArray = new JSONArray();
		
		//统计最近六年的学生数量变化
		List<Map<String, Object>> listTemp = dataQueryService.query(dataTable, " order by year desc", new ArrayList<Object>());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		if (listTemp != null){
			for (Map<String, Object> m : listTemp){
				list.add(m);
				if (list.size() > 5){
					break;
				}
			}
		}
		String json = jsonArray.fromObject(list).toString();
		mav.addObject("json", json);
		
		//最近年份
		if (list != null && !list.isEmpty()){
			Map<String, Object> map = list.get(0);
			mav.addObject("currentYear", map.get("year").toString());
		}

		return mav;
	}
	
	/**
	 * 幼儿园办别，城乡类型
	 * @param dataTable
	 * @param topage 转向页面
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/yey")
	public ModelAndView yey(String dataTable, String topage) throws Exception{
		ModelAndView mav = new ModelAndView(topage);
		
		dataTable = "reps_tj_data_" + dataTable;
		List<Map<String, Object>> list = dataQueryService.query(dataTable, null, new ArrayList<Object>());
		
		JSONArray jsonArray = new JSONArray();
		String json = jsonArray.fromObject(list).toString();
		mav.addObject("json", json);
		
		//读取指标名称
		TjTableDefine tjTableDefine = tjTableDefineService.getTjTableDefineByName(dataTable);
		List<Map<String, String>> itemNameList = tjTableDefine.getItemChineseNameList(new String[]{}, new String[]{});
		mav.addObject("itemNameList", jsonArray.fromObject(itemNameList).toString());

		return mav;
	}


}
