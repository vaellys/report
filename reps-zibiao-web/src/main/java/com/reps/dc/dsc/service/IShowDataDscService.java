package com.reps.dc.dsc.service;

import java.util.Date;
import java.util.List;

import com.reps.dc.server.StatData;


public interface IShowDataDscService {
	
	/**
	 * 数据交换流量记录数
	* @author zhangqilong
	* @date  2016-1-13 下午3:06:04
	* @param paramDate1
	* @param paramDate2
	* @param paramString
	* @return
	* @return Long
	 */
	public abstract Long countByReceiver(Date paramDate1, Date paramDate2, String paramString);
	
	/**
	 * 近30日处理数据统计
	* @author zhangqilong
	* @date  2016-1-13 下午3:06:34
	* @param paramDate
	* @param paramString
	* @return
	* @return List<StatData>
	 */
	public abstract List<StatData> statByAgentDays(Date paramDate, String paramString);

}
