package com.reps.tj.service;

import java.util.List;

import com.reps.core.orm.ListResult;
import com.reps.tj.entity.TjTableDefine;

public interface ITjTableDefineService {
	
	TjTableDefine get(String id, boolean eager);
	
	TjTableDefine getTjTableDefineByName(String tableName);
	
	List<TjTableDefine> query(TjTableDefine info);

	void save(TjTableDefine info);
	
	void update(TjTableDefine info);

	void delete(TjTableDefine info);
	
	void createTable(String id);
	
	/**
	 * 分页查询
	 * @param start
	 * @param pageSize
	 * @param info
	 * @return
	 */
	ListResult<TjTableDefine> query(int start, int pageSize, TjTableDefine info);

}
