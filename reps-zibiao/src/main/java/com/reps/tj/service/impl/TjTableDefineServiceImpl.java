package com.reps.tj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.orm.wrapper.JdbcDao;
import com.reps.tj.dao.TjTableDefineDao;
import com.reps.tj.dao.TjTableItemDao;
import com.reps.tj.entity.TjTableDefine;
import com.reps.tj.entity.TjTableItem;
import com.reps.tj.service.ITjTableDefineService;
import com.reps.tj.util.ColumnAttribute;
import com.reps.tj.util.FormTable;
import com.reps.tj.util.TableGenerator;

@Service
public class TjTableDefineServiceImpl implements ITjTableDefineService {
	@Autowired
	TjTableDefineDao tableDao;
	@Autowired
	TjTableItemDao tableItemDao;
	@Autowired
	JdbcDao jdbcDao;

	@Override
	public TjTableDefine get(String id, boolean eager) {
		TjTableDefine t = tableDao.get(id);
		if (eager){
			Hibernate.initialize(t.getItems());
		}
		
		return t;
	}

	@Override
	public List<TjTableDefine> query(TjTableDefine info) {
		return tableDao.query(info);
	}

	@Override
	public void save(TjTableDefine info) {
		tableDao.save(info);
	}

	@Override
	public void update(TjTableDefine info) {
		tableDao.update(info);
	}

	@Override
	public void delete(TjTableDefine info) {
		if(info.getItems() != null){
			//删除该统计表下所有的指标项
			List<TjTableItem> items = info.getItems();
			for(TjTableItem item : items){
				tableItemDao.delete(item);
			}
		}
		
		tableDao.delete(info);
	}

	@Override
	public void createTable(String id) {
		TjTableDefine t = get(id, true);
		
		if (t!=null && t.getItems()!=null){
			
			//删除原来的表，不再使用下面代码，通过hibernate删除
//			try{
//				jdbc.execute("drop table " + t.getName());
//			} catch(Exception e){
//				System.out.println(e.getMessage());
//			}
			
			List<ColumnAttribute> list = new ArrayList<ColumnAttribute>();
			for(TjTableItem item : t.getItems()){
				if (item.getIsTemporary() == 0){
					ColumnAttribute attr = new ColumnAttribute();
					attr.setColumnName(item.getItemName());
					attr.setColumnType(item.getFieldType());
					if (StringUtils.isNotBlank(item.getFieldLength())){
						attr.setLength(Integer.valueOf(item.getFieldLength()));
					}
					list.add(attr);
				}
			}
			
			FormTable fromTable = new FormTable();
			
			fromTable.setTableName(t.getName());
			
			fromTable.addColunms(list);
			TableGenerator tg = new TableGenerator(fromTable);
			try {
				tg.generatorTable();
			} catch (RepsException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ListResult<TjTableDefine> query(int start, int pageSize, TjTableDefine info) {
		return tableDao.query(start, pageSize, info);
	}

	@Override
	public TjTableDefine getTjTableDefineByName(String tableName) {
		TjTableDefine tjTableDefine = tableDao.getTjTableDefineByName(tableName);
		if(tjTableDefine!=null){
			Hibernate.initialize(tjTableDefine.getItems());
		}
		return tjTableDefine;
	}


}
