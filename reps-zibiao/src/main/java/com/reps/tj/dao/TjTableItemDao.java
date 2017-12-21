package com.reps.tj.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.wrapper.GenericDao;
import com.reps.core.util.StringUtil;
import com.reps.tj.entity.TjTableItem;

@Repository
public class TjTableItemDao {
	@Autowired
	GenericDao<TjTableItem> dao;

	
	public TjTableItem get(String id) {
		return dao.get(TjTableItem.class, id);
	}
	
	public void save(TjTableItem info) {
		dao.save(info);
	}
	
	public void delete(TjTableItem info){
		dao.delete(info);
	}
	
	public void update(TjTableItem info){
		dao.update(info);
	}
	
	public List<TjTableItem> query(TjTableItem info){
		DetachedCriteria dc = DetachedCriteria.forClass(TjTableItem.class);
		
		dc.createAlias("tjTableDefine", "t", JoinType.INNER_JOIN);
		if (info != null){
			if (StringUtil.isNotBlank(info.getId())){
				dc.add((Restrictions.eq("id", info.getId())));
			}
			if (info.getTjTableDefine() != null){
				if(StringUtil.isNotBlank(info.getTjTableDefine().getId())){
					dc.add((Restrictions.eq("t.id", info.getTjTableDefine().getId())));
				}
			}
		}
		dc.addOrder(Order.asc("statOrder"));
		
		return dao.findByCriteria(dc);
	}
	
}
