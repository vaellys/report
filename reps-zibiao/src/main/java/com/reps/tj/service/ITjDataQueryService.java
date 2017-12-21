package com.reps.tj.service;

import java.util.List;
import java.util.Map;

import com.reps.core.commons.OrderCondition;
import com.reps.core.orm.ListResult;
import com.reps.tj.entity.TjTableDefine;

/*
 * 查询统计表数据的接口
 * @author Karlova
 */
public interface ITjDataQueryService {
	
	/**
	 * 查询数据表，不分页
	 * @param sql（可以包含where，group by，order by）
	 * @param values 条件参数值
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	List<Map<String, Object>> query(String sql, List<Object> values) throws Exception;
	
	/**
	 * 查询数据表，不分页
	 * @param tableName 数据表
	 * @param condition 条件（可以包含where，order by）
	 * @param values 条件参数值
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	List<Map<String, Object>> query(String tableName, String condition, List<Object> values) throws Exception;
	
	/**
	 * 
	 * @param start
	 * @param max
	 * @param tableDefine
	 * @param condition
	 * @param values
	 * @return
	 * @throws Exception
	 */
	ListResult<Map<String, Object>> query(int start, int max, TjTableDefine tableDefine, String condition, List<Object> values) throws Exception;
	
	/**
	 * 综合业务分拆查询
	* @param tableDefine
	* @param condition
	* @param values
	* @throws Exception
	* @return List<Map<String,Object>>
	 */
	List<Map<String,Object>> queryByZhyw(TjTableDefine tableDefine, String condition, List<Object> values) throws Exception;
	
	/**
	 * 计算每个指标的总数
	 * @param tableName 数据表
	 * @param includeFields 要统计的指标字段，可选。如果是null，则所有字段
	 * @param excludeFields 不需要统计的字段，可选。如果includeFields非空，则此项失效
	 * @param countField 记录数，通常与groupBy结合使用
	 * @param groupBy - 分组字段
	 * @param orderBy - 排序字段
	 * @param sqlWhere - 条件
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	List<Map<String, Object>> getSumByTableName(String tableName, String[] includeFields, String[] excludeFields, 
			String countField, String[] groupBy, List<OrderCondition> orderBy, String sqlWhere) throws Exception;

}
