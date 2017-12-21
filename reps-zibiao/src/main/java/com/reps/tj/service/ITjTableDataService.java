package com.reps.tj.service;

import java.util.List;
import java.util.Map;

import com.reps.tj.entity.TjTableDefine;

public interface ITjTableDataService {
	
	List<Map<String, Object>> queryForList(int start, int pageSize, String id);

	List<Object> get(String id, String name);

	void delete(String id, String name);

	TjTableDefine get(String id, boolean bLoadLazy);

	void add(String did, String data);

	Map<String, Object> queryMap(String id, String name);

	void update(String did, String id, String data);

}
