package com.reps.tj.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.ListResult;
import com.reps.core.orm.wrapper.GenericDao;
import com.reps.tj.entity.TjItemCategory;

@Repository
public class TjItemCategoryDao {
	
	@Autowired
	GenericDao<TjItemCategory> dao;
	
	public void save(TjItemCategory tjItemCate){
		dao.save(tjItemCate);
	}
	
	public void delete(TjItemCategory tjItemCate)
	{
		dao.delete(tjItemCate);
	}
	
	public void update(TjItemCategory tjItemCate)
	{
		dao.update(tjItemCate);
	}
	
	public TjItemCategory get(String pId){
		return dao.get(TjItemCategory.class, pId);
	}
	
	public ListResult<TjItemCategory> query(int start, int pagesize, TjItemCategory info)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(TjItemCategory.class);
		if (info != null)
		{
			if (StringUtils.isNotBlank(info.getName()))
			{
				dc.add(Restrictions.like("name", info.getName(),
						MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotBlank(info.getParentId()))
			{
				dc.add(Restrictions.eq("parentId", info.getParentId()));
			}
			
		}
		return dao.query(dc, start, pagesize, Order.asc("name"));
	}
	
	public List<TjItemCategory> queryList(String pId)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(TjItemCategory.class);
		if (StringUtils.isNotBlank(pId))
		{
			dc.add(Restrictions.eq("parentId", pId));
		}
		dc.addOrder(Order.asc("showOrder"));
		
		return dao.findByCriteria(dc);
	}
	
	public List<TjItemCategory> getAllCategory(){
		DetachedCriteria dc = DetachedCriteria.forClass(TjItemCategory.class);
		
		return dao.findByCriteria(dc);
	}
	
	public List<TjItemCategory> getAllNotCategory(){
		DetachedCriteria dc = DetachedCriteria.forClass(TjItemCategory.class);
		dc.add(Restrictions.ne("parentId", "-1"));
		
		return dao.findByCriteria(dc);
	}

}
