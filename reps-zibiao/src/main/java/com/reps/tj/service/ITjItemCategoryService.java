package com.reps.tj.service;

import java.util.List;

import com.reps.core.orm.ListResult;
import com.reps.tj.entity.TjItemCategory;

/**
 * 报表指标分类管理
 * @author zql
 *
 */
public interface ITjItemCategoryService {
	
	/**
	 * 添加对象
	 * @param dataObj
	 */
	public void save(TjItemCategory tjItemCate);
	
	/**
	 * 删除指定的对象
	 * @param tjItemCate
	 */
	public void delete(TjItemCategory tjItemCate);
	
	/**
	 * 修改对象的相关消息
	 * @param tjItemCate
	 */
	public void update(TjItemCategory tjItemCate);
	
	/**
	 * 通过id得到对象
	 * @param pId
	 * @return
	 */
	public TjItemCategory get(String pId);
	
	/**
	 * 分页查询
	 * @param start
	 * @param pagesize
	 * @param tjItemCate
	 * @return
	 */
	ListResult<TjItemCategory> query(int start, int pagesize, TjItemCategory tjItemCate);
	
	/**
	 * 查询所有在pId下面的数据对象
	 * @param pId
	 * @return
	 */
	public List<TjItemCategory> queryList(String pId);
	
	/**
	 * 查询所有上级指标类型
	 * @return
	 */
	public List<TjItemCategory> getAllCategory();
	
	/**
	 * 查询所有非上级指标类型
	 * @return
	 */
	public List<TjItemCategory> getAllNotCategory();
	
}
