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
import com.reps.tj.entity.TjNamespace;
import com.reps.tj.entity.TjZbztmcdyb;

@Repository
public class TjZbztmcdybDao {

	@Autowired
	GenericDao<TjZbztmcdyb> dao;

	public void save(TjZbztmcdyb tjZbztmcdyb) {
		dao.save(tjZbztmcdyb);
	}

	public void delete(TjZbztmcdyb tjZbztmcdyb) {
		dao.delete(tjZbztmcdyb);
	}

	public void update(TjZbztmcdyb tjZbztmcdyb) {
		dao.update(tjZbztmcdyb);
	}

	public TjZbztmcdyb get(String id) {
		return dao.get(TjZbztmcdyb.class, id);
	}

	public ListResult<TjZbztmcdyb> query(int start, int pagesize, TjZbztmcdyb info) {
		DetachedCriteria dc = DetachedCriteria.forClass(TjZbztmcdyb.class);
		dc.createAlias("tjNamespace", "t");
		if (info != null) {
			if (StringUtils.isNotBlank(info.getZbztId())) {
				dc.add(Restrictions.eq("zbztId", info.getZbztId()));
			}
			if (StringUtils.isNotBlank(info.getZbztmc())) {
				dc.add(Restrictions.like("zbztmc", info.getZbztmc(), MatchMode.ANYWHERE));
			}
			TjNamespace tjNamespace = info.getTjNamespace();
			String namespace = null != tjNamespace ? tjNamespace.getNamespace() : "";
			if (StringUtils.isNotBlank(namespace)) {
				dc.add(Restrictions.eq("t.namespace", namespace));
			}
		}
		return dao.query(dc, start, pagesize, Order.asc("zbztmc"));
	}

	public List<TjZbztmcdyb> queryList(String namespace) {
		DetachedCriteria dc = DetachedCriteria.forClass(TjZbztmcdyb.class);
		dc.createAlias("tjNamespace", "t");
		if (StringUtils.isNotBlank(namespace)) {
			dc.add(Restrictions.eq("t.namespace", namespace));
		}
		dc.addOrder(Order.asc("zbztmc"));

		return dao.findByCriteria(dc);
	}

	public List<TjZbztmcdyb> getAllTopic() {
		DetachedCriteria dc = DetachedCriteria.forClass(TjZbztmcdyb.class);
		return dao.findByCriteria(dc);
	}

}
