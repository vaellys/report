package com.reps.dc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.reps.core.SpringContext;
import com.reps.core.orm.ListResult;
import com.reps.core.orm.wrapper.JdbcDao;
import com.reps.core.standard.IDataStandard;
import com.reps.core.standard.bean.DataObjectField;
import com.reps.core.standard.bean.DataObjectInfo;
import com.reps.core.util.StringUtil;
import com.reps.extend.echarts.ForceChart;
import com.reps.system.standard.entity.DataObject;
import com.reps.system.standard.entity.FieldDefine;
import com.reps.system.standard.entity.ObjectField;

public class DataStandardImpl implements IDataStandard{
	
	//private JdbcTemplate jdbc;
	
	private JdbcDao standardJdbcDao;
	
	public JdbcDao getJdbcDao() {
		if (this.standardJdbcDao == null) {
			this.standardJdbcDao = ((JdbcDao) SpringContext.getBean("jdbcDataStandard"));
		}
		return this.standardJdbcDao;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, DataObjectInfo> queryDataObjects(String paramString) {
		Map result = new HashMap();
		ListResult<DataObject> listResult = null;
		StringBuffer sql = new StringBuffer();
		
		JSONObject jsonObject = JSONObject.fromObject(paramString);
        Map<String, Object> mapJson = JSONObject.fromObject(jsonObject);
        Object style = mapJson.get("style");
        
        if("standard".equals(style)||"objectdata".equals(style)){
        	Object name = mapJson.get("objName");
            String start = mapJson.get("start").toString();
            String pageSize = mapJson.get("pageSize").toString();
            Integer sta = Integer.parseInt(start);
            Integer size = Integer.parseInt(pageSize);
            int[] limit = { sta, size };
            Object[] arg = null;
            if(null != name&&StringUtil.isNotBlank(name.toString())&&!"\"null\"".equals(name)){
            	sql.append("select id,name as name,ref_table as groupId from reps_data_object where name like ? order by name asc");
            	arg = new Object[]{"%"+name+"%"};
            }else{
            	sql.append("select id,name as name,ref_table as groupId from reps_data_object order by name asc ");
            }
            
            try {
    			listResult = getJdbcDao().queryToBeanPageList(DataObject.class, sql.toString(), arg, limit);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
            if(listResult.getList()!=null){
    			for(int i=0;i<listResult.getList().size();i++){
    				DataObject dobj=listResult.getList().get(i);
    				
    				DataObjectInfo info = new DataObjectInfo();
    				info.setName(dobj.getName());
    				info.setTable(dobj.getGroupId());
    				
    				List<ObjectField> list = null;
    				StringBuffer sqlField = new StringBuffer();
    				sqlField.append("select id,field_id as refFieldId from reps_data_object_field");
    				sqlField.append(" where object_id = ?");
    				try {
    					list = getJdbcDao().queryToBeanList(ObjectField.class, sqlField.toString(), new Object[] {dobj.getId()});
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    				
    				for (ObjectField fd : list) {
    					DataObjectField dof = convert(fd);

    					info.getFields().add(dof);
    				}
    				result.put(info.getName(), info);
    			}
    		}
            Object objCount = listResult.getCount();
            result.put("count", objCount);
        	
        }
        if("relation".equals(style)){
        	try {
        		sql.append("select * from reps_data_object ");
				List<DataObject> objects = getJdbcDao().queryToBeanList(DataObject.class, sql.toString());
				Map<String,String> objMap=new HashMap<String,String>();
				
				ForceChart chart=new ForceChart();
				chart.addGroup("基础对象");
				chart.addGroup("关联对象");
				for(DataObject obj: objects){
					objMap.put(obj.getId(),obj.getName());
					//chart.addNode("基础对象", obj.getName(),index);
				}
				
				//添加关联关系
				//List<ObjectField> linkFields=fieldService.getFieldIsObject();
				StringBuffer sqlField = new StringBuffer();
				sqlField.append("select * from reps_data_object_field where ref_object_id is not null and ref_object_id != '' ");
				List<Map<String, String>> listMap = getJdbcDao().queryToMapList(sqlField.toString());
				
				if(null!=listMap&&listMap.size()>0){
					for(Map<String,String> map : listMap){
						String target=objMap.get(map.get("ref_object_id"));
						chart.addNode("基础对象", target,1);//把目标对象加进去,防止关联不到
						String objId = map.get("object_id");
						String name = objMap.get(objId);
						
						String fieldId = map.get("field_id");
						StringBuffer sqlDefine = new StringBuffer();
						sqlDefine.append("select id,name as name,chinese_name as chineseName,type as type,length as length,description as description,dictname as dictname from reps_data_field_define");
						sqlDefine.append(" where id = ?");
						FieldDefine fieldDefine = (FieldDefine) getJdbcDao().queryToBean(FieldDefine.class, sqlDefine.toString(), new Object[] {fieldId});
						String fieldName = fieldDefine.getName();
						chart.addLinkData("基础对象",name,1,target,fieldName+"->");
					}
				}
				
				Object objJson = chart.toJson();
				result.put("json", objJson);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        
		
		return result;
	}

	@Override
	public DataObjectInfo getDataObject(String paramString) {
		return null;
	}

	public void setStandardJdbcDao(JdbcDao standardJdbcDao) {
		this.standardJdbcDao = standardJdbcDao;
	}
	
	private DataObjectField convert(ObjectField fd) {
		DataObjectField dof = new DataObjectField();
		
		String fieldId = fd.getRefFieldId();
		FieldDefine fieldDefine = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select id,name as name,chinese_name as chineseName,type as type,length as length,description as description,dictname as dictname from reps_data_field_define");
		sql.append(" where id = ?");
		try {
			fieldDefine = (FieldDefine) getJdbcDao().queryToBean(FieldDefine.class, sql.toString(), new Object[] {fieldId});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null!=fieldDefine){
			dof.setFieldName(fieldDefine.getName());
			dof.setName(fieldDefine.getChineseName());
			
			if (fieldDefine.getLength() != null)
				dof.setLength(Integer.valueOf(fieldDefine.getLength()).intValue());
			else {
				dof.setLength(0);
			}
		}
		
		
		
		
//		dof.setFieldName(fd.getField().getName());
//		dof.setName(fd.getField().getChineseName());
//		dof.setMust(fd.getMandatory().intValue() == 1);
//
//		if (fd.getField().getLength() != null)
//			dof.setLength(Integer.valueOf(fd.getField().getLength()).intValue());
//		else {
//			dof.setLength(0);
//		}
//
//		dof.setFieldType(getFieldType(fd.getField().getType()));
//		dof.setRefObject(fd.getRefObjectId());
//		dof.setRefField(fd.getRefFieldId());
		return dof;
	}
	

}
