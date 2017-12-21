package com.reps.tj.service;

import java.util.List;

import com.reps.core.orm.ListResult;
import com.reps.tj.entity.TjNamespace;
import com.reps.tj.util.ZtreeAttribute;

/**
 * 命名空间管理操作
 * 
 * @author qianguobing
 * @date 2017年8月8日 下午3:51:33
 */
public interface ITjNamespaceService {

	/**
	 * 添加命名空间
	 * 
	 * @param tjNamespace
	 */
	public void save(TjNamespace tjNamespace);

	/**
	 * 删除指定的命名空间
	 * 
	 * @param tjNamespace
	 */
	public void delete(TjNamespace tjNamespace);

	/**
	 * 修改命名空间的相关消息
	 * 
	 * @param tjNamespace
	 */
	public void update(TjNamespace tjNamespace);

	/**
	 * 通过namespace得到命名空间
	 * 
	 * @param namespace
	 * @return TjNamespace
	 */
	public TjNamespace get(String namespace);

	/**
	 * 分页查询
	 * 
	 * @param start
	 * @param pagesize
	 * @param tjNamespace
	 * @return ListResult<TjNamespace>
	 */
	ListResult<TjNamespace> query(int start, int pagesize, TjNamespace tjNamespace);

	/**
	 * 查询所有命名空间
	 * 
	 * @return List<TjZbztmcdyb>
	 */
	public List<TjNamespace> getAllNamespace();

	/**
	 * 初始华左侧树形菜单
	 * 
	 * @return List<ZtreeAttribute>
	 */
	public List<ZtreeAttribute> buildNamespaceTreeNode();
}
