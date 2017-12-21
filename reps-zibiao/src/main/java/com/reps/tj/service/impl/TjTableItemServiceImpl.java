package com.reps.tj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.tj.dao.TjItemDao;
import com.reps.tj.dao.TjTableItemDao;
import com.reps.tj.entity.TjItem;
import com.reps.tj.entity.TjTableItem;
import com.reps.tj.service.ITjTableItemService;

/**
 * ITjTableItemService的实现类，实现对象的管理
 * @author zql
 *
 */
@Service
public class TjTableItemServiceImpl implements ITjTableItemService {
	
	@Autowired
	TjTableItemDao tableItemDao;
	@Autowired
	TjItemDao itemDao;

	@Override
	public TjTableItem get(String id) {
		return tableItemDao.get(id);
	}

	@Override
	public TjTableItem get(TjTableItem tableItem) {
		TjItem item = itemDao.getItemByName(tableItem.getItemName());
		tableItem.setIsDictionary(item.getIsDictionary());
		tableItem.setReferDictionary(item.getReferDictionary());
		return tableItem;
	}
	
	@Override
	public List<TjTableItem> query(TjTableItem info) {
		return tableItemDao.query(info);
	}

	@Override
	public void save(TjTableItem tableItem) {
		tableItemDao.save(tableItem);
	}

	@Override
	public void update(TjTableItem tabItem) {
		tableItemDao.update(tabItem);
		
	}

	@Override
	public void delete(TjTableItem tabItem) {
		tableItemDao.delete(tabItem);
	}

}
