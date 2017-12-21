package com.reps.tj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.util.IDGenerator;
import com.reps.tj.dao.TjTableDataDao;
import com.reps.tj.dao.TjTableDefineDao;
import com.reps.tj.entity.TjTableDefine;
import com.reps.tj.service.ITjTableDataService;

@Service
public class TjTableDataServiceImpl implements ITjTableDataService {
	@Autowired
	TjTableDefineDao defineDao;
	@Autowired
	TjTableDataDao dataDao;

	@Override
	public TjTableDefine get(String id, boolean bLoadLazy) {
		TjTableDefine t = defineDao.get(id);
		if (bLoadLazy){
			Hibernate.initialize(t.getItems());
		}
		
		return t;
	}
	
	@Override
	public List<Object> get(String id,String name) {
		return dataDao.get(id, name);
	} 
	
	@Override
	public Map<String, Object> queryMap(String id,String name){
		return dataDao.queryMap(id, name);
	}
	
	@Override
	public void add(String did, String data) {
		TjTableDefine define = get(did, true);
		String id = IDGenerator.UUID();
		Map<String, Object> row = new HashMap<String, Object>();
		String[] arr = data.split("&");
		for (int i = 0; i < arr.length; i++) {
			String[] kv = arr[i].split("=");
			if (kv.length>1) {
				row.put(kv[0], kv[1]);
			}
		}
		dataDao.add(id, define, row);
	}
	
	@Override
	public void update(String did,String id,String data){
		TjTableDefine define = get(did,true);
		Map<String, Object> row = new HashMap<String, Object>();
		String[] arr = data.split("&");
		for (int i = 0; i < arr.length; i++) {
			String[] kv = arr[i].split("=");
			if (kv.length>1) {
				row.put(kv[0], kv[1]);
			}
		}
		dataDao.update(id, define,row);
	}
	
	@Override
	public void delete(String id,String name){
		dataDao.delete(id, name);
	}
	
	@Override
	public List<Map<String, Object>> queryForList(int start, int pageSize, String did) {
		List<Map<String, Object>> list = null;
		try {
			TjTableDefine define = defineDao.get(did);
			list = dataDao.queryToMapList(start, pageSize, define.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	} 



}
