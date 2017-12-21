package com.reps.tj.service;

import com.reps.core.exception.RepsException;


/**
 * 统计接口
 * @author Karlova
 */
public interface ITjService {

	void saveStat(String tableId) throws RepsException;
	
}
