package com.reps.tj.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.ListResult;
import com.reps.core.orm.wrapper.GenericDao;
import com.reps.core.util.StringUtil;
import com.reps.tj.entity.TjItem;

@Repository
public class TjItemDao {
	
	@Autowired
	GenericDao<TjItem> dao;
	
	public void save(TjItem tjItem){
		dao.save(tjItem);
	}
	
	public void delete(TjItem tjItem)
	{
		dao.delete(tjItem);
	}
	
	public void update(TjItem tjItem)
	{
		dao.update(tjItem);
	}
	
	public TjItem get(String id){
		return dao.get(TjItem.class, id);
	}
	
	public ListResult<TjItem> query(int start, int pagesize, TjItem info)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(TjItem.class);
		dc.createAlias("tjItemCategory", "t");
		if (info != null)
		{
			if (StringUtil.isNotBlank(info.getChineseName()))
			{
				dc.add(Restrictions.like("chineseName", info.getChineseName(),
						MatchMode.ANYWHERE));
			}
			if (StringUtil.isNotBlank(info.getCategoryId()))
			{
				dc.add(Restrictions.eq("t.id", info.getCategoryId()));
			}
		}
		return dao.query(dc, start, pagesize, Order.asc("name"));
	}
	
	public List<TjItem> getItemsOfCategory(String cid){
		DetachedCriteria dc = DetachedCriteria.forClass(TjItem.class);
		if(StringUtil.isNotBlank(cid)){
			dc.add(Restrictions.eq("categoryId", cid));
		}
		dc.addOrder(Order.asc("showOrder"));
		
		return dao.findByCriteria(dc);
	}
	
	public List<TjItem> getAllItem(){
		DetachedCriteria dc = DetachedCriteria.forClass(TjItem.class);
		dc.createAlias("tjItemCategory", "t");
		dc.addOrder(Order.asc("showOrder"));
		
		return dao.findByCriteria(dc);
	}
	
	public TjItem getItemByName(String name){
		DetachedCriteria dc = DetachedCriteria.forClass(TjItem.class);
		if(StringUtil.isNotBlank(name)){
			dc.add(Restrictions.eq("name", name));
		}
		List<TjItem> itemList = dao.findByCriteria(dc);
		if(itemList!=null && itemList.size()>0){
			return itemList.get(0);
		}
		return null;
	}

}
