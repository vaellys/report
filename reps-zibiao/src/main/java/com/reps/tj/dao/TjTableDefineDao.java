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
import com.reps.tj.entity.TjTableDefine;


@Repository
public class TjTableDefineDao {
	@Autowired
	GenericDao<TjTableDefine> dao;

	
	public TjTableDefine get(String id) {
		return dao.get(TjTableDefine.class, id);
	}
	
	public List<TjTableDefine> query(TjTableDefine info){
		DetachedCriteria dc = DetachedCriteria.forClass(TjTableDefine.class);
		
		if (info != null){
			if (info.getIsBasic() != null){
				dc.add((Restrictions.eq("isBasic", info.getIsBasic())));
			}
			if (info.getEnabled() != null){
				dc.add((Restrictions.eq("enabled", info.getEnabled())));
			}
			if (StringUtil.isNotBlank(info.getId())){
				dc.add((Restrictions.eq("id", info.getId())));
			}
		}
		dc.addOrder(Order.desc("isBasic")).addOrder(Order.asc("priority"));
		
		return dao.findByCriteria(dc);
	}
	
	public ListResult<TjTableDefine> query(int start, int pageSize, TjTableDefine info){
		DetachedCriteria dc = DetachedCriteria.forClass(TjTableDefine.class);
		if(info != null){
			if(StringUtil.isNotBlank(info.getChineseName())){
				dc.add(Restrictions.like("chineseName", info.getChineseName(), MatchMode.ANYWHERE));
			}
			if(StringUtil.isNotBlank(info.getName())){
				dc.add(Restrictions.like("name", info.getName(), MatchMode.ANYWHERE));
			}
			if(info.getIsBasic()!=null){
				dc.add(Restrictions.eq("isBasic", info.getIsBasic()));
			}
			if (info.getEnabled() != null){
				dc.add((Restrictions.eq("enabled", info.getEnabled())));
			}
		}
		return dao.query(dc, start, pageSize, Order.desc("isBasic"), Order.asc("priority"));
		
	}
	
	public void save(TjTableDefine info) {
		dao.save(info);
	}
	
	public void update(TjTableDefine info) {
		dao.update(info);
	}
	
	public void delete(TjTableDefine info) {
		dao.delete(info);
	}
	
	public TjTableDefine getTjTableDefineByName(String tableName){
		DetachedCriteria dc = DetachedCriteria.forClass(TjTableDefine.class);
			if(StringUtil.isNotBlank(tableName)){
				dc.add(Restrictions.eq("name", tableName));
			}
		
			List<TjTableDefine> tjTableDefineList = dao.findByCriteria(dc);
			if(tjTableDefineList!=null&&tjTableDefineList.size()>0){
				return tjTableDefineList.get(0);
			}
			
			return null;
	}
	

}
