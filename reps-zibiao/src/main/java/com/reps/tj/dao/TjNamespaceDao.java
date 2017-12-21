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

@Repository
public class TjNamespaceDao {

	@Autowired
	GenericDao<TjNamespace> dao;

	public void save(TjNamespace tjNamespace) {
		dao.save(tjNamespace);
	}

	public void delete(TjNamespace tjNamespace) {
		dao.delete(tjNamespace);
	}

	public void update(TjNamespace tjNamespace) {
		dao.update(tjNamespace);
	}

	public TjNamespace get(String id) {
		return dao.get(TjNamespace.class, id);
	}

	public ListResult<TjNamespace> query(int start, int pagesize, TjNamespace info) {
		DetachedCriteria dc = DetachedCriteria.forClass(TjNamespace.class);
		if (info != null) {
			if (StringUtils.isNotBlank(info.getDisplayTitle())) {
				dc.add(Restrictions.like("displayTitle", info.getDisplayTitle(), MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotBlank(info.getNamespace())) {
				dc.add(Restrictions.eq("namespace", info.getNamespace()));
			}
		}
		return dao.query(dc, start, pagesize, Order.asc("displayTitle"));
	}

	public List<TjNamespace> queryList(String namespace) {
		DetachedCriteria dc = DetachedCriteria.forClass(TjNamespace.class);
		if (StringUtils.isNotBlank(namespace)) {
			dc.add(Restrictions.eq("namespace", namespace));
		}
		dc.addOrder(Order.asc("displayOrder"));

		return dao.findByCriteria(dc);
	}

	public List<TjNamespace> getAllNamespace() {
		DetachedCriteria dc = DetachedCriteria.forClass(TjNamespace.class);
		return dao.findByCriteria(dc);
	}

}
