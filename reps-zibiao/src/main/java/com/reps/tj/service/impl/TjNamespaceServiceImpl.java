package com.reps.tj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.tj.dao.TjNamespaceDao;
import com.reps.tj.entity.TjNamespace;
import com.reps.tj.entity.TjZbztmcdyb;
import com.reps.tj.entity.TjZdyzbdyb;
import com.reps.tj.service.ITjNamespaceService;
import com.reps.tj.service.ITjZbztmcdybService;
import com.reps.tj.service.ITjZdyzbdybService;
import com.reps.tj.util.ZtreeAttribute;
import static com.reps.tj.util.Constants.*;

/**
 * ITjNamespaceService的实现类，实现命令空间的管理
 * 
 * @author qianguobing
 * @date 2017年7月27日 下午5:02:30
 */
@Service
public class TjNamespaceServiceImpl implements ITjNamespaceService {

	@Autowired
	TjNamespaceDao dao;

	@Autowired
	ITjZdyzbdybService zdyzbdybService;

	@Autowired
	ITjZbztmcdybService zbztmcdybService;

	@Override
	public void save(TjNamespace tjNamespace) {
		dao.save(tjNamespace);
	}

	@Override
	public void delete(TjNamespace tjNamespace) {
		dao.delete(tjNamespace);
	}

	@Override
	public void update(TjNamespace tjNamespace) {
		dao.update(tjNamespace);
	}

	@Override
	public TjNamespace get(String namespace) {
		return dao.get(namespace);
	}

	@Override
	public ListResult<TjNamespace> query(int start, int pagesize, TjNamespace tjNamespace) {
		return dao.query(start, pagesize, tjNamespace);
	}

	@Override
	public List<TjNamespace> getAllNamespace() {
		return dao.getAllNamespace();
	}

	public List<ZtreeAttribute> buildNamespaceTreeNode() {
		List<TjNamespace> allNamespace = this.getAllNamespace();// 查询所有的命名空间
		List<ZtreeAttribute> treeNodeList = new ArrayList<>();
		for (TjNamespace tjNamespace : allNamespace) {
			// 构造命名空间树节点
			String namespace = tjNamespace.getNamespace();
			setTreeNode(treeNodeList, "-1", namespace, namespace, NAMESPACE);
			// 查询该命名空间下面的所有主题
			List<TjZbztmcdyb> allTopics = zbztmcdybService.queryList(namespace);
			for (TjZbztmcdyb topic : allTopics) {
				String id = topic.getZbztId();
				setTreeNode(treeNodeList, namespace, topic.getZbztmc(), id, TOPIC);
				// 查询该主题下面的所有指标
				List<TjZdyzbdyb> indicatorOfTopic = zdyzbdybService.getIndicatorOfTopic(id);
				for (TjZdyzbdyb indicator : indicatorOfTopic) {
					String fId = indicator.getfId();
					if ("-1".equals(fId) || StringUtil.isBlank(fId)) {
						String indicatorId = indicator.getId();
						setTreeNode(treeNodeList, id, indicator.getZbmc(), indicatorId, INDICATOR);
						bindChildByParent(indicatorId, treeNodeList);
					}
				}
			}
		}
		return treeNodeList;
	}

	/**
	 * 设置树节点属性
	 * 
	 * @param treeNodeList
	 * @param pId
	 * @param name
	 * @param id
	 * @return void
	 */
	private void setTreeNode(List<ZtreeAttribute> treeNodeList, String pId, String name, String id, String type) {
		ZtreeAttribute treeNode;
		// 构造主题树节点
		treeNode = new ZtreeAttribute();
		treeNode.setId(id);
		treeNode.setName(name);
		treeNode.setParentId(pId);
		treeNode.setType(type);
		treeNodeList.add(treeNode);
	}

	/**
	 * 递归构造指标树节点
	 * 
	 * @param pId
	 * @param treeNodeList
	 * @return void
	 */
	private void bindChildByParent(String pId, List<ZtreeAttribute> treeNodeList) {
		if (zdyzbdybService.hasClild(pId)) {
			List<TjZdyzbdyb> indicators = zdyzbdybService.getIndicatorByPid(pId);
			for (TjZdyzbdyb indicator : indicators) {
				String id = indicator.getId();
				setTreeNode(treeNodeList, pId, indicator.getZbmc(), id, INDICATOR);
				this.bindChildByParent(id, treeNodeList);
			}
		}
	}

}
