package com.reps.tj.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.commons.OrderCondition;
import com.reps.core.orm.ListResult;
import com.reps.core.orm.wrapper.JdbcDao;
import com.reps.core.util.DBTableUtil;
import com.reps.tj.entity.TjTableDefine;
import com.reps.tj.entity.TjTableItem;
import com.reps.tj.service.ITjDataQueryService;

@Service("TjDataQueryServiceImpl")
public class TjDataQueryServiceImpl implements ITjDataQueryService {
	
	@Autowired
	JdbcDao jdbcDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> query(String sql, List<Object> values) throws Exception {
//		Object[] objects = values.toArray(new Object[values.size()]);
		
		return jdbcDao.queryForList(sql, values.toArray(new Object[values.size()]));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> query(String tableName, String condition, List<Object> values) throws Exception {
		String sql = "select * from " + tableName;
		sql = StringUtils.isBlank(condition) ? sql : sql + " " + condition;
		return jdbcDao.queryForList(sql, values.toArray(new Object[values.size()]));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ListResult<Map<String, Object>> query(int start, int max, TjTableDefine tableDefine, String condition, List<Object> values) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		//sql.append("select school,district,org_type,school_bb,town_type,classes_count,teacher_man_count,teacher_woman_count,student_boy_count,student_girl_count ");
		StringBuffer fields = new StringBuffer();
		List<TjTableItem> items = tableDefine.getItems();
		for(TjTableItem im : items)
		{
			fields.append(im.getItemName()).append(",");
		}
		sql.append(fields.substring(0, fields.length() - 1));
		sql.append(" from ").append(tableDefine.getName());
		sql.append(" where ");
		if(StringUtils.isBlank(condition))
		{
			sql.append("1=1");
		}
		else
		{
			sql.append(condition);
		}
		int[] a = {start, start + max};
		return jdbcDao.queryToMapPageList(sql.toString(), values.toArray(new Object[values.size()]), a);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> queryByZhyw(TjTableDefine tableDefine, String condition, List<Object> values) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		if("reps_tj_data_gqxzsbyrstj".equals(tableDefine.getName())){
			sql.append("year,school_phase,sum(student_zs_boy_count) as student_zs_boy_count,sum(student_zs_girl_count) as student_zs_girl_count,"
					+"sum(student_bys_boy_count) as student_bys_boy_count,"
					+"sum(student_bys_girl_count) as student_bys_girl_count");
		} else if("reps_tj_data_gqxjygmtj".equals(tableDefine.getName())){
			sql.append(" sum(master_man) as master_man,sum(master_wom) as master_wom,sum(bachelor_man) as bachelor_man,sum(bachelor_wom) as bachelor_wom,"
					+"sum(junior_college_man) as junior_college_man,sum(junior_college_wom) as junior_college_wom,"
					+"sum(edu_degree_others_man) as edu_degree_others_man,sum(edu_degree_others_wom) as edu_degree_others_wom,year");
		} else if("reps_tj_data_report1".equals(tableDefine.getName())){
			sql.append(" * ");
		} else if("reps_tj_data_base_yey".equals(tableDefine.getName())){
			sql.append("sum(teacher_man_count) as teacher_man_count, sum(teacher_woman_count) as teacher_woman_count," 
					+"sum(student_boy_count) as student_boy_count, sum(student_girl_count) as student_girl_count,"
					+"town_type");
		} else{
			StringBuffer fields = new StringBuffer();
			List<TjTableItem> items = tableDefine.getItems();
			for(TjTableItem im : items)
			{
				fields.append(im.getItemName()).append(",");
			}
			sql.append(fields.substring(0, fields.length() - 1));
		}
		
		sql.append(" from ").append(tableDefine.getName());
		if("reps_tj_data_gqxzsbyrstj".equals(tableDefine.getName())&&StringUtils.isBlank(condition))
		{
			sql.append(" where school_phase = '2' GROUP BY YEAR,school_phase");//2 小学
		}
		if("reps_tj_data_gqxzsbyrstj".equals(tableDefine.getName())&&StringUtils.isNotBlank(condition)){
			sql.append(" where school_phase = ? GROUP BY YEAR,school_phase");//
		}
		if(!"reps_tj_data_gqxzsbyrstj".equals(tableDefine.getName())&&StringUtils.isBlank(condition)){
			sql.append(" where ");
			sql.append(" 1=1 ");
		}
		if(!"reps_tj_data_gqxzsbyrstj".equals(tableDefine.getName())&&!"reps_tj_data_report1".equals(tableDefine.getName())&&StringUtils.isNotBlank(condition)){
			sql.append(" where ");
			sql.append(condition);
		}
		if("reps_tj_data_gqxjygmtj".equals(tableDefine.getName())){
			sql.append(" GROUP BY year ");
		}
		if("reps_tj_data_report1".equals(tableDefine.getName())&&StringUtils.isNotBlank(condition)){
			sql.append(" where ");
			sql.append(condition);
		}
		if("reps_tj_data_base_yey".equals(tableDefine.getName())){
			sql.append(" where ");
			sql.append(condition);
		}
		//int[] a = {start, start + max};
		//return jdbcDao.queryToMapPageList(sql.toString(), values.toArray(new Object[values.size()]));
		return jdbcDao.queryForList(sql.toString(), values.toArray(new Object[values.size()]));
	}

	@Override
	public List<Map<String, Object>> getSumByTableName(String tableName, String[] includeFields, String[] excludeFields, 
			String countField, String[] groupBy, List<OrderCondition> orderBy, String sqlWhere) throws Exception {
		if (StringUtils.isBlank(tableName)){
			return null;
		}
		
		//取出所有字段
		if (includeFields == null || includeFields.length < 1){
			DBTableUtil dbTable = new DBTableUtil();
			List<String> fieldList = dbTable.getTableFields(tableName);
			//默认剔除id
			fieldList.remove("id");
			//剔除不需要求和的字段
			if (excludeFields != null && excludeFields.length > 0){
				for (String f : excludeFields){
					fieldList.remove(f);
				}
			}
			
			includeFields = fieldList.toArray(new String[0]);
		}
		

		String selectSql = "";
		for (String f : includeFields){
			selectSql += "".equals(selectSql) ? "sum("+f+") as "+f : ",sum("+f+") as "+f;
		}
		
		String groupBySql = "";
		if (groupBy != null && groupBy.length > 0){
			for (String g : groupBy){
				groupBySql += "".equals(groupBySql) ? g : ","+g;
				selectSql += "," + g;
			}
			if (StringUtils.isNotBlank(groupBySql)){
				groupBySql = " group by " + groupBySql;
			}
		}
		
		String orderBySql = "";
		if (orderBy != null && !orderBy.isEmpty()){
			for (OrderCondition o : orderBy){
				orderBySql += "".equals(orderBySql) ? o.getName()+" "+o.getOrder() : ","+o.getName()+" "+o.getOrder();
			}
			if (StringUtils.isNotBlank(orderBySql)){
				orderBySql = " order by " + orderBySql;
			}
		}
		
		selectSql = StringUtils.isBlank(countField) ? selectSql : "count(*) as "+countField+","+selectSql;
		selectSql = "select " + selectSql + " from " + tableName;
		selectSql = StringUtils.isBlank(sqlWhere) ? selectSql : selectSql+" where "+sqlWhere;
		selectSql += groupBySql + orderBySql;
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = jdbcDao.queryToMapList(selectSql);

		return list;
	}

}
