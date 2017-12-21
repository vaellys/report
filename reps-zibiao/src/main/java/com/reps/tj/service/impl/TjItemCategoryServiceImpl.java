package com.reps.tj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.orm.ListResult;
import com.reps.tj.dao.TjItemCategoryDao;
import com.reps.tj.entity.TjItemCategory;
import com.reps.tj.service.ITjItemCategoryService;

/**
 * ITjItemCategoryService的实现类，实现对象的管理
 * @author zql
 *
 */
@Service
public class TjItemCategoryServiceImpl implements ITjItemCategoryService {
	
	@Autowired
	TjItemCategoryDao dao;

	@Override
	public void save(TjItemCategory tjItemCate) {
		dao.save(tjItemCate);

	}

	@Override
	public void delete(TjItemCategory tjItemCate) {
		dao.delete(tjItemCate);

	}

	@Override
	public void update(TjItemCategory tjItemCate) {
		dao.update(tjItemCate);

	}

	@Override
	public ListResult<TjItemCategory> query(int start, int pagesize,
			TjItemCategory info) {
		return dao.query(start, pagesize, info);
	}

	@Override
	public List<TjItemCategory> queryList(String pId) {
		return dao.queryList(pId);
	}

	@Override
	public TjItemCategory get(String pId) {
		return dao.get(pId);
	}

	@Override
	public List<TjItemCategory> getAllCategory() {
		return dao.getAllCategory();
	}

	@Override
	public List<TjItemCategory> getAllNotCategory() {
		return dao.getAllNotCategory();
	}

}
