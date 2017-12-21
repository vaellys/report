package com.reps.tj.service;

import java.util.List;

import com.reps.tj.entity.TjTableItem;

/**
 * 统计表指标项管理
 * @author zql
 *
 */
public interface ITjTableItemService {
	
	/**
	 * 通过指定id获得对象
	 * @param id
	 * @return
	 */
	TjTableItem get(String id);
	
	/**
	 * 获取统计表指标项的附加属性，比如，字典项引用的字典名称
	 */
	TjTableItem get(TjTableItem tableItem);
	
	/**
	 * 查找所有指标项。
	 * @param info
	 * @return
	 */
	List<TjTableItem> query(TjTableItem info);
	
	
	/**
	 * 添加统计表指标项
	 * @param tableItem
	 */
	void save(TjTableItem tableItem);
	
	/**
	 * 删除统计表指标项
	 * @param tabItem
	 */
	void delete(TjTableItem tableItem);
	
	/**
	 * 修改统计表指标项
	 * @param tabItem
	 */
	void update(TjTableItem tabItem);
	
}
