package com.reps.tj.service;

import java.util.List;

import com.reps.core.orm.ListResult;
import com.reps.tj.entity.TjItem;

/**
 * 指标管理
 * @author zql
 *
 */
public interface ITjItemService {

	/**
	 * 添加指标
	 * @param dataObj 
	 */
	public void save(TjItem tjItem);
	
	/**
	 * 删除指标
	 * @param tjItem
	 */
	public void delete(TjItem tjItem);
	
	/**
	 * 修改指标
	 * @param tjItemCate
	 */
	public void update(TjItem tjItem);
	
	/**
	 * 通过指定id获得对象
	 * @param id
	 * @return
	 */
	public TjItem get(String id);
	
	/**
	 * 分页查询
	 * @param start
	 * @param pagesize
	 * @param tjItem
	 * @return
	 */
	ListResult<TjItem> query(int start, int pagesize, TjItem tjItem);
	
	/**
	 * 根据指标分类id，得到该分类下的所有指标
	 * @param cid
	 * @return
	 */
	
	public List<TjItem> getItemsOfCategory(String cid);
	
	/**
	 * 根据指标名称，得到指标
	 * @param name
	 * @return
	 */
	public TjItem getItemByName(String name);
	
	
	public List<TjItem> getAllItem();
}
