package com.reps.tj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.orm.ListResult;
import com.reps.tj.dao.TjItemDao;
import com.reps.tj.entity.TjItem;
import com.reps.tj.service.ITjItemService;

/**
 * ITjItemService的实现类，实现对象的管理
 * @author zql
 *
 */
@Service
public class TjItemServiceImpl implements ITjItemService {
	
	@Autowired
	TjItemDao dao;

	@Override
	public void save(TjItem tjItem) {
		dao.save(tjItem);

	}

	@Override
	public void delete(TjItem tjItem) {
		dao.delete(tjItem);

	}

	@Override
	public void update(TjItem tjItem) {
		dao.update(tjItem);

	}

	@Override
	public ListResult<TjItem> query(int start, int pagesize, TjItem info) {
		return dao.query(start, pagesize, info);
	}

	@Override
	public TjItem get(String id) {
		return dao.get(id);
	}

	@Override
	public List<TjItem> getItemsOfCategory(String cid) {
		return dao.getItemsOfCategory(cid);
	}

	@Override
	public List<TjItem> getAllItem() {
		return dao.getAllItem();
	}

	@Override
	public TjItem getItemByName(String name) {
		return dao.getItemByName(name);
	}

}
