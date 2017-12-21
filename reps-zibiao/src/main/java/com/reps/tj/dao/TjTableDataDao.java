package com.reps.tj.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.wrapper.GenericDao;
import com.reps.core.orm.wrapper.JdbcDao;
import com.reps.tj.entity.TjTableDefine;
import com.reps.tj.entity.TjTableItem;


@Repository
public class TjTableDataDao {
	@Autowired
	JdbcDao jdbcDao;
	@Autowired
	GenericDao<TjTableDefine> defineDao;

	@SuppressWarnings("unchecked")
	public List<Object> get(String id,String name) {
		List<Object> object = null;
		try {
			String sql = "select * from "+name+" where id = '"+id+"'";
			object = jdbcDao.queryForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> queryMap(String id,String name) {
		Map<String,Object> object = null;
		try {
			String sql = "select * from "+name+" where id = '"+id+"'";
			object = jdbcDao.queryForMap(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	} 
	
	public void add(String id, TjTableDefine define, Map<String ,Object> row) {
		List<TjTableItem> items = define.getItems();
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ").append(define.getName()).append(" (");
		for (TjTableItem item : items) {
			sb.append(item.getItemName()).append(",");
		}
		sb.append("id)").append(" values (");
		for (TjTableItem item : items) {
			if (row.containsKey(item.getItemName())) {
				String isshow = row.get("" + item.getItemName() + "") != null ? (String) row.get("" + item.getItemName() + "") : "";
				if ("nvarchar".equals(item.getFieldType()) || "varchar".equals(item.getFieldType()) || "char".equals(item.getFieldType())) {
					sb.append("'"+isshow+"'").append(",");
				}else {
					sb.append(isshow).append(",");
				}
			}
		}
		sb.append("'"+id+"'").append(")");
		jdbcDao.execute(sb.toString());
	}
	
	public void update(String id, TjTableDefine define, Map<String ,Object> row) {
		List<TjTableItem> items = define.getItems();
		StringBuffer sb = new StringBuffer();
		sb.append("update ").append(define.getName()).append(" set ");
		for (TjTableItem item : items) {
			if (row.containsKey(item.getItemName())) {
				String isshow = row.get("" + item.getItemName() + "") != null ? (String) row.get("" + item.getItemName() + "") : "";
				if ("nvarchar".equals(item.getFieldType()) || "varchar".equals(item.getFieldType()) || "char".equals(item.getFieldType())) {
					sb.append(item.getItemName()).append(" = '").append(isshow).append("',");
				}else {
					sb.append(item.getItemName()).append(" = ").append(isshow).append(",");
				}
			}
		}
		sb.append("id = '" + id + "' where id = '" + id + "'");
		jdbcDao.execute(sb.toString());
	}
	
	public void delete(String id,String name){
		jdbcDao.execute("delete from "+name+" where id = '"+id+"'");
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryToMapList(int start, int pageSize, String name) throws Exception{
		String sql = "select * from " + name + "";
		return jdbcDao.queryToMapListByPage(start, pageSize, sql, new Object[]{});
	}
}
