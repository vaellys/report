package com.reps.tj.service;

import java.util.List;

import com.reps.core.orm.ListResult;
import com.reps.tj.entity.TjZbztmcdyb;

/**
 * 指标主题管理
 * 
 * @author qianguobing
 * @date 2017年8月8日 下午3:51:03
 */
public interface ITjZbztmcdybService {

	/**
	 * 添加主题
	 * 
	 * @param tjZbztmcdyb
	 */
	public void save(TjZbztmcdyb tjZbztmcdyb);

	/**
	 * 删除指定的主题
	 * 
	 * @param tjZbztmcdyb
	 */
	public void delete(TjZbztmcdyb tjZbztmcdyb);

	/**
	 * 修改主题的相关消息
	 * 
	 * @param tjZbztmcdyb
	 */
	public void update(TjZbztmcdyb tjZbztmcdyb);

	/**
	 * 通过id得到主题
	 * 
	 * @param id
	 * @return TjZbztmcdyb
	 */
	public TjZbztmcdyb get(String id);

	/**
	 * 分页查询
	 * 
	 * @param start
	 * @param pagesize
	 * @param tjZbztmcdyb
	 * @return ListResult<TjItemCategory>
	 */
	ListResult<TjZbztmcdyb> query(int start, int pagesize, TjZbztmcdyb tjZbztmcdyb);

	/**
	 * 查询所有在namespace下面的主题
	 * 
	 * @param namespace
	 * @return List<TjZbztmcdyb>
	 */
	public List<TjZbztmcdyb> queryList(String namespace);

	/**
	 * 查询所有上级指标主题
	 * 
	 * @return List<TjZbztmcdyb>
	 */
	public List<TjZbztmcdyb> getAllTopic();
}
