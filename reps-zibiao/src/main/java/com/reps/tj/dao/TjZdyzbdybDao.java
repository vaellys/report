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
import com.reps.tj.entity.TjZdyzbdyb;

@Repository
public class TjZdyzbdybDao {

	@Autowired
	GenericDao<TjZdyzbdyb> dao;

	public void save(TjZdyzbdyb tjZdyzbdyb) {
		dao.save(tjZdyzbdyb);
	}

	public void delete(TjZdyzbdyb tjZdyzbdyb) {
		dao.delete(tjZdyzbdyb);
	}

	public void update(TjZdyzbdyb tjZdyzbdyb) {
		dao.update(tjZdyzbdyb);
	}

	public TjZdyzbdyb get(String id) {
		return dao.get(TjZdyzbdyb.class, id);
	}

	public ListResult<TjZdyzbdyb> query(int start, int pagesize, TjZdyzbdyb info) {
		DetachedCriteria dc = DetachedCriteria.forClass(TjZdyzbdyb.class);
		dc.createAlias("tjZbztmcdyb", "t");
		if (info != null) {
			if (StringUtil.isNotBlank(info.getZbmc())) {
				dc.add(Restrictions.like("zbmc", info.getZbmc(), MatchMode.ANYWHERE));
			}
			if (StringUtil.isNotBlank(info.getZbztId())) {
				dc.add(Restrictions.eq("t.zbztId", info.getZbztId()));
			}
		}
		return dao.query(dc, start, pagesize, Order.asc("zbmc"));
	}

	public ListResult<TjZdyzbdyb> queryByPid(int start, int pagesize, String pId) {
		DetachedCriteria dc = DetachedCriteria.forClass(TjZdyzbdyb.class);
		dc.add(Restrictions.eq("fId", pId));
		return dao.query(dc, start, pagesize, Order.asc("zbmc"));
	}

	public List<TjZdyzbdyb> getIndicatorByPid(String pId) {
		DetachedCriteria dc = DetachedCriteria.forClass(TjZdyzbdyb.class);
		dc.add(Restrictions.eq("fId", pId));
		dc.addOrder(Order.asc("xsxh"));

		return dao.findByCriteria(dc);
	}

	public List<TjZdyzbdyb> getIndicatorOfTopic(String tid) {
		DetachedCriteria dc = DetachedCriteria.forClass(TjZdyzbdyb.class);
		if (StringUtil.isNotBlank(tid)) {
			dc.add(Restrictions.eq("zbztId", tid));
		}
		dc.addOrder(Order.asc("xsxh"));

		return dao.findByCriteria(dc);
	}

	public List<TjZdyzbdyb> getAllIndicator() {
		DetachedCriteria dc = DetachedCriteria.forClass(TjZdyzbdyb.class);
		dc.createAlias("tjZbztmcdyb", "t");
		dc.addOrder(Order.asc("zbztmc"));

		return dao.findByCriteria(dc);
	}

	public TjZdyzbdyb getIndicatorByName(TjZdyzbdyb info) {
		DetachedCriteria dc = DetachedCriteria.forClass(TjZdyzbdyb.class);
		dc.createAlias("tjZbztmcdyb.tjNamespace", "t");
		String name = info.getZbmc();
		if (StringUtil.isNotBlank(name)) {
			dc.add(Restrictions.eq("zbmc", name));
		}
		dc.add(Restrictions.eq("t.namespace", info.getTjZbztmcdyb().getTjNamespace().getNamespace()));
		List<TjZdyzbdyb> itemList = dao.findByCriteria(dc);
		if (itemList != null && itemList.size() > 0) {
			return itemList.get(0);
		}
		return null;
	}
	
	public List<TjZdyzbdyb> getAllRootIndicator() {
		DetachedCriteria dc = DetachedCriteria.forClass(TjZdyzbdyb.class);
		dc.add(Restrictions.ne("fId", "-1"));

		return dao.findByCriteria(dc);
	}

}
