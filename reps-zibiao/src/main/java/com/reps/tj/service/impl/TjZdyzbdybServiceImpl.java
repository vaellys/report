package com.reps.tj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.orm.ListResult;
import com.reps.tj.dao.TjZdyzbdybDao;
import com.reps.tj.entity.TjZdyzbdyb;
import com.reps.tj.service.ITjZdyzbdybService;

/**
 * TjZdyzbdybService的实现类，实现自定义指标的管理
 * 
 * @author qianguobing
 * @date 2017年7月27日 下午5:02:30
 */
@Service
public class TjZdyzbdybServiceImpl implements ITjZdyzbdybService {

	@Autowired
	TjZdyzbdybDao dao;

	@Override
	public void save(TjZdyzbdyb tjZdyzbdyb) {
		dao.save(tjZdyzbdyb);
	}

	@Override
	public void delete(TjZdyzbdyb tjZdyzbdyb) {
		dao.delete(tjZdyzbdyb);
	}

	@Override
	public void update(TjZdyzbdyb tjZdyzbdyb) {
		dao.update(tjZdyzbdyb);
	}

	@Override
	public TjZdyzbdyb get(String id) {
		return dao.get(id);
	}

	@Override
	public ListResult<TjZdyzbdyb> query(int start, int pagesize, TjZdyzbdyb tjZdyzbdyb) {
		return dao.query(start, pagesize, tjZdyzbdyb);
	}

	@Override
	public List<TjZdyzbdyb> getIndicatorOfTopic(String tid) {
		return dao.getIndicatorOfTopic(tid);
	}

	@Override
	public TjZdyzbdyb getIndicatorByName(TjZdyzbdyb tjZdyzbdyb) {
		return dao.getIndicatorByName(tjZdyzbdyb);
	}

	@Override
	public List<TjZdyzbdyb> getAllIndicator() {
		return dao.getAllIndicator();
	}

	@Override
	public List<TjZdyzbdyb> getAllRootIndicator() {
		return dao.getAllRootIndicator();
	}

	@Override
	public List<TjZdyzbdyb> getIndicatorByPid(String pId) {
		return dao.getIndicatorByPid(pId);
	}

	@Override
	public boolean hasClild(String pId) {
		List<TjZdyzbdyb> indicators = getIndicatorByPid(pId);
		if (null != indicators && indicators.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ListResult<TjZdyzbdyb> queryByPid(int start, int pagesize, String pId) {
		return dao.queryByPid(start, pagesize, pId);
	}

}
