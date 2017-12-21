package com.reps.dc.dsc.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.reps.core.SpringContext;
import com.reps.core.orm.wrapper.JdbcDao;
import com.reps.dc.server.StatData;

public class DataDscDao {
	
	private JdbcDao dscMessageLogJdbcDao;
	
	public JdbcDao getJdbcDao() {
		if (this.dscMessageLogJdbcDao == null) {
			this.dscMessageLogJdbcDao = ((JdbcDao) SpringContext.getBean("dscMessageLogJdbcDao"));
		}
		return this.dscMessageLogJdbcDao;
	}
	
	public Long countByReceiver(Date start, Date end, String receiver) {
		StringBuffer sql = new StringBuffer("Select count(*) from reps_dsc_log ");
		Long count = (long) 0;
		if (start != null) {
			sql.append(" Where send_time between ? and ? ");
			sql.append(" and receiver =? ");
			try {
				count = this.getJdbcDao().queryForLong(sql.toString(), new Object[] { start, end, receiver });
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			sql.append(" where receiver =? ");
			count = this.getJdbcDao().queryForLong(sql.toString(), new Object[] { receiver });
		}
		return count;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<StatData> countByAgentDays(Date start, String server) {
		StringBuffer sql = new StringBuffer("select sender,month(send_time) as month, day(send_time) as day, count(*) as total from reps_dsc_log ");
		sql.append(" where send_time >= ?");
		sql.append(" and sender !=? ");
		sql.append(" group by sender,month(send_time),day(send_time)");

		List list = this.getJdbcDao().queryForList(sql.toString(), new Object[] { start, server });
		List result = new ArrayList();
		
		
		if ((list != null) && (list.size() > 0)) {
			for (int i = 0; i < list.size(); ++i) {
//				Object[] objs = (Object[]) (Object[]) list.get(i);
//				StatData sd = new StatData();
//				sd.setAgent(objs[0].toString());
//				sd.setTimeName(objs[1] + "月" + objs[2]);
//				sd.setValue(Long.valueOf(objs[3].toString()));
//				result.add(sd);
				
				Map<String, String> map = (Map<String, String>) list.get(i);
				StatData sd = new StatData();
				sd.setAgent(map.get("sender"));
				Object obj = map.get("month");
				String month = String.valueOf(obj);
				obj = map.get("day");
				String day = String.valueOf(obj);
				sd.setTimeName(month+ "月" +day);
				String total = String.valueOf(map.get("total"));
				sd.setValue(Long.parseLong(total));
				result.add(sd);
				
			}
		}
		return result;
	}

	public void setDscMessageLogJdbcDao(JdbcDao dscMessageLogJdbcDao) {
		this.dscMessageLogJdbcDao = dscMessageLogJdbcDao;
	}
	
}
