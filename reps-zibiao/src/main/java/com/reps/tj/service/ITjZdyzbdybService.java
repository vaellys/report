package com.reps.tj.service;

import java.util.List;

import com.reps.core.orm.ListResult;
import com.reps.tj.entity.TjZdyzbdyb;

/**
 * 自定义指标定义表管理
 * 
 * @author qianguobing
 * @date 2017年7月27日 下午4:32:43
 */
public interface ITjZdyzbdybService {

	/**
	 * 添加指标
	 * 
	 * @param tjZdyzbdyb
	 */
	public void save(TjZdyzbdyb tjZdyzbdyb);

	/**
	 * 删除指标
	 * 
	 * @param tjZdyzbdyb
	 */
	public void delete(TjZdyzbdyb tjZdyzbdyb);

	/**
	 * 修改指标
	 * 
	 * @param tjZdyzbdyb
	 */
	public void update(TjZdyzbdyb tjZdyzbdyb);

	/**
	 * 通过指定id获得对象
	 * 
	 * @param id
	 * @return TjZdyzbdyb
	 */
	public TjZdyzbdyb get(String id);

	/**
	 * 分页查询
	 * 
	 * @param start
	 * @param pagesize
	 * @param tjZdyzbdyb
	 * @return ListResult<TjZdyzbdyb>
	 */
	ListResult<TjZdyzbdyb> query(int start, int pagesize, TjZdyzbdyb tjZdyzbdyb);

	/**
	 * 根据指标主题id，得到该主题下的所有指标
	 * 
	 * @param tid
	 * @return List<TjZdyzbdyb>
	 */

	public List<TjZdyzbdyb> getIndicatorOfTopic(String tid);

	/**
	 * 根据指标名称，得到指标
	 * 
	 * @param name
	 * @return TjZdyzbdyb
	 */
	public TjZdyzbdyb getIndicatorByName(TjZdyzbdyb tjZdyzbdyb);

	/**
	 * 查询所有指标
	 * 
	 * @param name
	 * @return List<TjZdyzbdyb>
	 */
	public List<TjZdyzbdyb> getAllIndicator();

	/**
	 * 查询所有顶层指标
	 * 
	 * @return List<TjZdyzbdyb>
	 */
	public List<TjZdyzbdyb> getAllRootIndicator();

	/**
	 * 根据指定pid查询所有指标
	 * 
	 * @param pId
	 * @return List<TjZdyzbdyb>
	 */
	public List<TjZdyzbdyb> getIndicatorByPid(String pId);

	/**
	 * 判断当前指标是否有子级指标
	 * 
	 * @param pId
	 * @return boolean
	 */
	public boolean hasClild(String pId);

	/**
	 * 根据PID查询所有子节点
	 * 
	 * @param start
	 * @param pagesize
	 * @param pId
	 * @return ListResult<TjZdyzbdyb>
	 */
	ListResult<TjZdyzbdyb> queryByPid(int start, int pagesize, String pId);

}
