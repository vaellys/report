package com.reps.portal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.reps.dc.server.ServerInfoUtil;
import com.reps.dc.server.ServerStatus;
import com.reps.dc.server.ServerStatus.DiskInfoVo;
import com.reps.system.standard.entity.ObjectField;
import com.reps.system.standard.service.IObjectFieldService;
import com.reps.tj.service.ITjDataQueryService;

/**
 * 首页
 * @author Karlova
 * @version 2016-04-26
 */
@Controller("com.reps.portal.controller.IndexAction")
public class IndexAction {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ITjDataQueryService dataQueryService;
	
	@Autowired
	private IObjectFieldService fieldService;
	

	@RequestMapping(value = "/index")
	public ModelAndView index() throws Exception {
		ModelAndView mav = new ModelAndView("/index");
		
		//统计基础数据
		String tableName = "reps_tj_data_base_yxjsszs";
		String[] fields = new String[]{};
		String[] excludeFields = new String[]{"year", "district"};
		
		List<Map<String, Object>> list = dataQueryService.getSumByTableName(tableName, fields, excludeFields, null, new String[]{}, null, null);
		
		if (list!=null && !list.isEmpty()){
			Map<String, Object> map = list.get(0);
			
			for(Map.Entry<String, Object> entry : map.entrySet()){
				mav.addObject(entry.getKey(), entry.getValue());
			}
		}
		
		return mav;
	}
	
	@RequestMapping(value = "/getfieldsbyobjname",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getFieldsByObjName(String name) throws Exception{
		List<ObjectField> list = fieldService.getFieldsByObjName(name);
		Gson gson = new Gson();
		return gson.toJson(list);
	}

	@RequestMapping(value = "/server")
	@ResponseBody
	public String serverinfo() {
		try {

			ServerStatus status = ServerInfoUtil.getServerStatus();
			String cpuUsage = status.getCpuUsage();// cpu
			long freeMem = status.getFreeMem();// 未使用的内存
			long useMem = status.getUsedMem();
			List<DiskInfoVo> diskInfoList = status.getDiskInfos();//磁盘信息列表
			List<Map<String,String>> diskListMap = new ArrayList<Map<String,String>>();
			//long totalMem = status.getTotalMem();
			for(DiskInfoVo disk : diskInfoList){
				Map<String,String> map = new HashMap<String,String>();
				map.put("panFuName", disk.getDevName());
				map.put("useDisk", String.valueOf(disk.getUsedSize()));
				map.put("totalDisk", String.valueOf(disk.getTotalSize()));
				diskListMap.add(map);
			}

			long jvmFree = status.getJvmFreeMem();
			long jvmTotal = status.getJvmTotalMem();

			StringBuffer json = new StringBuffer("{");
			// cpu
			double useCPU = Double.valueOf(cpuUsage);
			double nouseCPU = 100 - useCPU;
			json.append("\"cpu\":{\"useCPU\":"+useCPU+",\"nouseCPU\":"+nouseCPU+"},");
			json.append("\"mem\":{\"useMem\":"+useMem+",\"freeMem\":"+freeMem+"},");
			json.append("\"jvm\":{\"useJvm\":"+(jvmTotal - jvmFree)+",\"freeJvm\":"+jvmFree+"}");
			String jsonDiskInfo = JSONArray.fromObject(diskListMap).toString();
			json.append(",\"disk\":"+jsonDiskInfo);

//			PieChart<Double> cpuPie = new PieChart<Double>("CPU监控");
//			cpuPie.addData("使用", useCPU);
//			cpuPie.addData("空闭", nouseCPU);
//			cpuPie.setToolbox(false);
//			cpuPie.option().legend().x(X.right).orient(Orient.vertical);
//			
//			String piejson=cpuPie.toJson();
//			//System.out.println(piejson);
//			json.append("\"cpu\":" + piejson);
//
//			// 内存
//			PieChart<Long> memPie = new PieChart<Long>("内存监控");
//			memPie.option().legend().x(X.right).orient(Orient.vertical);
//			memPie.addData("使用", useMem);
//			memPie.addData("空闭", freeMem);
//			memPie.setToolbox(false);
//
//			json.append(",\"mem\":" + memPie.toJson());
//			// jvm
//			PieChart<Long> jvmPie = new PieChart<Long>("JVM监控");
//			jvmPie.option().legend().x(X.right).orient(Orient.vertical);
//			jvmPie.addData("使用", jvmTotal - jvmFree);
//			jvmPie.addData("空闭", jvmFree);
//			jvmPie.setToolbox(false);
//			json.append(",\"jvm\":" + jvmPie.toJson());

			json.append("}");
			return json.toString();

		} catch (Exception exp) {
			exp.printStackTrace();
			return "{}";
		}

	}
}
