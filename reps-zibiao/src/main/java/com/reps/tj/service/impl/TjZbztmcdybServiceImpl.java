package com.reps.tj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.orm.ListResult;
import com.reps.tj.dao.TjZbztmcdybDao;
import com.reps.tj.entity.TjZbztmcdyb;
import com.reps.tj.service.ITjZbztmcdybService;

/**
 * ITjZbztmcdybService的实现类，实现指标主题的管理
 * 
 * @author qianguobing
 * @date 2017年7月27日 下午5:02:30
 * 
 */
@Service
public class TjZbztmcdybServiceImpl implements ITjZbztmcdybService {

	@Autowired
	TjZbztmcdybDao dao;

	@Override
	public void save(TjZbztmcdyb tjZbztmcdyb) {
		dao.save(tjZbztmcdyb);
	}

	@Override
	public void delete(TjZbztmcdyb tjZbztmcdyb) {
		dao.delete(tjZbztmcdyb);
	}

	@Override
	public void update(TjZbztmcdyb tjZbztmcdyb) {
		dao.update(tjZbztmcdyb);
	}

	@Override
	public TjZbztmcdyb get(String id) {
		return dao.get(id);
	}

	@Override
	public ListResult<TjZbztmcdyb> query(int start, int pagesize, TjZbztmcdyb tjZbztmcdyb) {
		return dao.query(start, pagesize, tjZbztmcdyb);
	}

	@Override
	public List<TjZbztmcdyb> queryList(String namespace) {
		return dao.queryList(namespace);
	}

	@Override
	public List<TjZbztmcdyb> getAllTopic() {
		return dao.getAllTopic();
	}

}
