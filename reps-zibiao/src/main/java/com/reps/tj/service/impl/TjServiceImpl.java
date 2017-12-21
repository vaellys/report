package com.reps.tj.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.wrapper.JdbcDao;
import com.reps.core.util.IDGenerator;
import com.reps.tj.dao.TjTableDefineDao;
import com.reps.tj.entity.TjTableDefine;
import com.reps.tj.entity.TjTableItem;
import com.reps.tj.service.ITjService;
import com.reps.tj.service.ITjTableItemService;
import com.reps.tj.util.MathEval;

/**
 * 自定义报表统计接口
 * @author Karlova
 */
@Service
public class TjServiceImpl implements ITjService {
	//索引项
	private TjTableItem indexItem;
	
	//统计表内的所有指标项
	List<TjTableItem> items;
	
	//每条SQL查询出来的结果集
	private Map<String, Object> rsMap;
	
	//需要保存入库的每个指标对应的值集
	private Map<String, List<String>> valueMap;
	
	//所有被依赖项计算出来的值记录，矩阵存储
	private List<List<Map<String, Object>>> valueMatrix;
	
	//被依赖项记录，在矩阵存储横向位置<被依赖项, 横向位置>
	private Map<String, Integer> indexMatrix;
	
	//常量值
	private Map<String, String> constMap = new HashMap<String, String>();
	
	@Autowired
	JdbcDao jdbcDao;
	@Autowired
	TjTableDefineDao tableDao;
	@Autowired
	ITjTableItemService tableItemService;

	/**
	 * 统计入口
	 */
	@Override
	public void saveStat(String tableId) throws RepsException{
		TjTableDefine t = tableDao.get(tableId);
		
		//初始化
		init(t);
		
		if ("V".equals(t.getMethod())){
			saveStatV(t);
		}
		else{
			saveStatH(t);
		}
	}
	
	private void init(TjTableDefine t) throws RepsException{
		//清除现有的统计数据
		deleteStat(t);
		
		indexItem = null;
		rsMap = new HashMap<String, Object>();
		valueMap = new HashMap<String, List<String>>();
		valueMatrix = new ArrayList<List<Map<String, Object>>>();

		//初始化每个指标
		items = t.getItems();
		for(TjTableItem item : items){
			//设置字典项
			item = tableItemService.get(item);
			//从哪个结果集哪个字段取值
			if (StringUtils.isBlank(item.getValuedColumn())){
				String column = item.getItemName();
				if ("SQL".equals(item.getExpressionType())){
					if (item.getExpression().toLowerCase().equals("count")){
						
					}
					else{
						int pos = item.getExpression().toLowerCase().indexOf("from");
						column = item.getExpression().substring(0, pos).trim();
						
						pos = column.lastIndexOf(" ");
						if (pos > -1){
							column = column.substring(pos, column.length()).trim();
						}
					}
				}
				item.setValuedColumn(item.getItemName()+"['"+column+"']");
			}
		}
	}
	
	/**
	 * 纵向等阶统计模型，根据统计表的指标项，统计出数据
	 * @param tableId 统计表ID
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void saveStatV(TjTableDefine t) throws RepsException {
		try{
			List<String> depItem = new ArrayList<String>();
			List<String> depColumn = new ArrayList<String>();

			for(TjTableItem item : items){
				//解析依赖项，支持多项依赖
				depItem = new ArrayList<String>();
				depColumn = new ArrayList<String>();
				getDepItem(item.getDependency(), depItem, depColumn);
				String expression = parseExpression(item);
				
				//运算表达式
				if ("SQL".equals(item.getExpressionType())){
					//以第一项的结果集作为循环
					if (indexItem == null){
						indexItem = item;
					}
					
//					String sql = item.getExpression().toLowerCase();
//					
//					//对于count函数，把本字段作为列名
//					String atFrom = sql.substring(0, sql.indexOf("from"));
//					int pos = sql.indexOf("count");
//					if (pos > -1){
//						String count = atFrom.substring(pos, atFrom.indexOf(")")+1);
//						sql = sql.replace(count, count.replace(" ", "") + " as " + item.getItemName());
//					}
					
					//依赖的SQL，需要重新运算
					if (StringUtils.isNotBlank(item.getDependency()) && !item.getItemName().equals(indexItem.getItemName())){
						//以第一个依赖项的结果集作为索引
						List<Map> masterList = (List<Map>)rsMap.get(depItem.get(0));
						
						Object[] objects = new Object[depItem.size()];
						for(int i=0; i<masterList.size(); i++){
							Map masterMap = masterList.get(i);
							
							objects[0] = masterMap.get(depColumn.get(0));
							for(int j=1; j<depItem.size(); j++){
								List<Map> slaveList = (List<Map>)rsMap.get(depItem.get(j));
								Map slaveMap = slaveList.get(i);
								objects[j] = slaveMap.get(depColumn.get(j));
							}
							setResultCache(item, expression, "", "", objects);
						}
					}
					else{
						if (depColumn!=null && depColumn.size()>0){
							setResultCache(item, expression, "", depColumn.get(0), new Object[]{});
						}
						else{
							setResultCache(item, expression, "", "", new Object[]{});
						}
					}
				}
				else if ("常量".equals(item.getExpressionType())){
					constMap.put(item.getItemName(), MathEval.parseConst(item.getExpression()));
				}
				else if ("变量".equals(item.getExpressionType())){
					if (StringUtils.isNotBlank(item.getDependency())){
						setResultCache(item, "", depItem.get(0), depColumn.get(0), new Object[]{});
					}
				}
				else if ("运算".equals(item.getExpressionType())){
					setResultCache(item, "", "", "", new Object[]{});
				}
			}
			
			//入库保存统计结果
			saveValue(t);
		} catch (DataAccessException e) {
			throw new RepsException(e.getMessage());
		} catch (RepsException e) {
			throw new RepsException(e.getMessage());
		}
	}

	/**
	 * 横向非等阶统计模型，根据统计表的指标项，统计出数据
	 * @param tableId 统计表ID
	 */
	@SuppressWarnings("unchecked")
	public void saveStatH(TjTableDefine t) throws RepsException {
		try{
			List<String> valuedItem = new ArrayList<String>();
			List<String> valuedColumn = new ArrayList<String>();
			List<String> depItem = new ArrayList<String>();
			List<String> depColumn = new ArrayList<String>();
			Map<String, Integer> indexMatrix = new HashMap<String, Integer>();
			
			//每个被依赖项，通过计算形成关联矩阵结构，
			//拆分存储最新的每个被依赖项单维度结果集，为后续项的依赖统计服务
			//Map<String, List<Map<String, Object>>> depValueMap = new HashMap<String, List<String>>();
			
			for(TjTableItem item : items){
				item = tableItemService.get(item);
				
				//计算常量
				if ("常量".equals(item.getExpressionType())){
					constMap.put(item.getItemName(), MathEval.parseConst(item.getExpression()));
				}
				
				//统计无依赖项的指标
				if (StringUtils.isBlank(item.getDependency())){
					if ("SQL".equals(item.getExpressionType()) && StringUtils.isNotBlank(item.getExpression())){
						//以第一项的结果集作为循环
						if (indexItem == null){
							indexItem = item;
						}
						
						List<Map<String, Object>> list = jdbcDao.queryForList(item.getExpression(), new Object[]{});
						
						//SQL结果集缓存
						rsMap.put(item.getItemName(), list);
						setResultCache(item, "", "", "", new Object[]{});
					}
					else{//留着扩展
						
					}
				}
			}
	
			for(TjTableItem item : items){
				List<List<Map<String, Object>>> myMatrix = new ArrayList<List<Map<String, Object>>>();
				
				if ("运算".equals(item.getExpressionType())){
					setResultCache(item, "", "", "", new Object[]{});
				}
				//统计依赖项的指标
				else if (StringUtils.isNotBlank(item.getDependency())){
					//解析输出项
					valuedItem = new ArrayList<String>();
					valuedColumn = new ArrayList<String>();
					getDepItem(item.getValuedColumn(), valuedItem, valuedColumn);

					//解析依赖项，支持多项依赖
					depItem = new ArrayList<String>();
					depColumn = new ArrayList<String>();
					getDepItem(item.getDependency(), depItem, depColumn);
					String expression = parseExpression(item);
					
					if ("SQL".equals(item.getExpressionType()) && StringUtils.isNotBlank(item.getExpression())){
						//以第一项的结果集作为循环
						if (indexItem == null){
							indexItem = item;
						}
						
						//如果是依赖自己，只统计
						if (item.getItemName().equals(depItem.get(0))){
							List<Map<String, Object>> list = jdbcDao.queryForList(item.getExpression(), new Object[]{});
							
							//SQL结果集缓存
							rsMap.put(item.getItemName(), list);
						}
						else{
							List<List<Map<String, Object>>> depRsList = new ArrayList<List<Map<String, Object>>>();
							List<List<Map<String, Object>>> recursiveResult = new ArrayList<List<Map<String, Object>>>();

							
							for(int i=0; i<depItem.size(); i++){
								int index = indexMatrix.containsKey(depItem.get(i)) ? indexMatrix.get(depItem.get(i)) : -1;
								
								if (index > -1){
									recursiveResult.add(getDimFromMatrix(index));
									depRsList.add(getDimFromMatrix(index));
								}
								else{
									depRsList.add((List<Map<String, Object>>)rsMap.get(depItem.get(i)));
									indexMatrix.put(depItem.get(i), indexMatrix.size());
								}
							}
							
							//以下采用递归算法
							if (depRsList.size() != recursiveResult.size()){
						        recursive(depRsList, recursiveResult, 0, new ArrayList<Map<String, Object>>());
							}
	
					        //获取依赖值，并统计
							Object[] objects = new Object[depItem.size()];
							
							//本指标项的所有统计结果集
							List<Map<String, Object>> myItemResult = new ArrayList<Map<String, Object>>();
							
							for(List<Map<String, Object>> result : recursiveResult){
								for(int i=0; i<depItem.size(); i++){
									Map<String, Object> map = result.get(i);
									objects[i] = map.get(depColumn.get(i));
								}
								System.out.println(Arrays.toString(objects));
								
								List<Map<String, Object>> newList = jdbcDao.queryForList(expression, objects);
								myItemResult.addAll(newList);
								
								//构建结果集矩阵
								build(result, newList, myMatrix);
							}
							
							//SQL结果集缓存
							rsMap.put(item.getItemName(), myItemResult);
							
							//清除无关联值的记录
							
						}
					}
					rebuild(myMatrix);
					setResultCache(item, "", valuedItem.get(0), valuedColumn.get(0), new Object[]{});
				}
				
			}
			
			
			//入库保存统计结果
			if (valueMatrix.size() > 0){
				valueMap = new HashMap<String, List<String>>();
				for(TjTableItem item : items){
					if ("常量".equals(item.getExpressionType())){
						continue;
					}
					
					//解析输出项
					valuedItem = new ArrayList<String>();
					valuedColumn = new ArrayList<String>();
					getDepItem(item.getValuedColumn(), valuedItem, valuedColumn);

					int index = -1;//定位指标项所属的记录list，在矩阵中的位置
					List<String> values = new ArrayList<String>();
					for(List<Map<String, Object>> list : valueMatrix){
						if (index == -1){
							for(int i=0; i<list.size(); i++){
								if (list.get(i).get(valuedColumn.get(0)) != null){
									index = i;
									break;
								}
							}
						}
						if (index > -1){
							values.add(list.get(index).get(valuedColumn.get(0)).toString());
						}
					}
					valueMap.put(item.getItemName(), values);
				}
			}
			saveValue(t);
			
		} catch (DataAccessException e) {
			throw new RepsException(e.getMessage());
		}
	}
	
	/**
	 * 保存统计结果
	 */
	private void saveValue(TjTableDefine t){
		if (valueMap.isEmpty()){
			return;
		}
		
		List<String> indexList = (List<String>)valueMap.get(indexItem.getItemName());
		
		for(int i=0; i<indexList.size(); i++){
			StringBuffer sqlInsert = new StringBuffer();
			StringBuffer sqlValue = new StringBuffer();
			
			String value = "";
			
			for(TjTableItem item : items){
				if ("常量".equals(item.getExpressionType())){
					value = constMap.get(item.getItemName());
				}
				else{
					List<String> tempList = (List<String>)valueMap.get(item.getItemName());
					value = tempList.get(i)==null ? "" : tempList.get(i).toString();
				}
				sqlInsert.append(item.getItemName() + ",");
				sqlValue.append("'" + value + "',");
			}

			//提交
			sqlInsert = sqlInsert.deleteCharAt(sqlInsert.lastIndexOf(","));
			sqlValue = sqlValue.deleteCharAt(sqlValue.lastIndexOf(","));
			
			value = IDGenerator.generate();
			String sql = "insert into " + t.getName() + "(id," + sqlInsert.toString() + ")"
							+ " values('" + value + "'," + sqlValue.toString() + ")";
			
			System.out.println("sqlInsert="+sql);
			
			jdbcDao.execute(sql);
		}

	}
	
	//删除现有的统计数据
	@SuppressWarnings("rawtypes")
	private void deleteStat(TjTableDefine t){
		
		//如果是基础表，清除全部数据
		if (t.getIsBasic() == 1){
			jdbcDao.execute("delete from " + t.getName());
		}
		//如果是业务表，清除本批次数据
		else{
			//常量或SQL才能作为数据批次唯一性
			String[] values = null;
			Map<String, String[]> map = new HashMap<String, String[]>();
			for(TjTableItem item : t.getItems()){
				if (item.getBatchKey() != null && item.getBatchKey() == 1){
					if ("常量".equals(item.getExpressionType())){
						values = MathEval.parseConst(item.getExpression()).split(",");
						if (values != null){
							map.put(item.getItemName(), values);
						}
					}
				}
			}
			
			if (map != null){
				//计算删除条件
				StringBuffer deleteSql = new StringBuffer();
				Iterator it = map.keySet().iterator();
				while(it.hasNext()){
					StringBuffer temp = new StringBuffer();
					String itemName = it.next().toString();
					values = map.get(itemName);
					for(String s : values){
						temp.append("'" + s + "',");
					}
					
					if (temp.length() > 0){
						if (deleteSql.length() > 0){
							deleteSql.append(" and ");
						}
						deleteSql.append(itemName + " in (")
									.append(temp.deleteCharAt(temp.lastIndexOf(",")) + ")");
					}
				}
				
				//删除表记录
				if (deleteSql.length() > 0){
					jdbcDao.execute("delete from " + t.getName() + " where " + deleteSql);
				}
			}
			
		}
		
	}
	
	

	private void getDepItem(String dependency, List<String> depItem, List<String> depColumn){
		if (StringUtils.isNotBlank(dependency)){
			String[] temp = dependency.split(",");
			
			for(int i=0; i<temp.length; i++){
				int pos = temp[i].indexOf("['");
				if (pos >= 0){
					depItem.add(temp[i].substring(0, pos));
					depColumn.add(temp[i].substring(pos+2, temp[i].length()-2));
				}
				else{
					depItem.add(temp[i]);
					depColumn.add(temp[i]);
				}
			}
		}
	}

	private String parseExpression(TjTableItem item){
		String expression = StringUtils.isBlank(item.getExpression()) ? "" : item.getExpression();
		if ("SQL".equals(item.getExpressionType())){
			String temp = expression.toLowerCase();
			
			//对于count函数，把本字段作为列名
			String atFrom = temp.substring(0, temp.indexOf("from"));
			int pos = temp.indexOf("count");
			if (pos > -1){
				String count = atFrom.substring(pos, atFrom.indexOf(")")+1);
				expression = expression.replace(count, count.replace(" ", "") + " as " + item.getItemName());
			}
		}
		return expression;
	}
	
	
	/** 
	 * 递归实现dimValue中的笛卡尔积，结果放在result中
	 * @param dimValue 原始数据
	 * @param result 结果数据
	 * @param layer dimValue的层数
	 * @param curList 每次笛卡尔积的结果
	 */
	private void recursive (List<List<Map<String, Object>>> dimValue, 
			List<List<Map<String, Object>>> result, int layer, List<Map<String, Object>> curList) {
		if (layer < dimValue.size() - 1) {
			if (dimValue.get(layer).size() == 0) {
				recursive(dimValue, result, layer + 1, curList);
			} else {
				for (int i = 0; i < dimValue.get(layer).size(); i++) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(curList);
					list.add(dimValue.get(layer).get(i));
					recursive(dimValue, result, layer + 1, list);
				}
			}
		} else if (layer == dimValue.size() - 1) {
			if (dimValue.get(layer).size() == 0) {
				result.add(curList);
			} else {
				for (int i = 0; i < dimValue.get(layer).size(); i++) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(curList);
					list.add(dimValue.get(layer).get(i));
					result.add(list);
				}
			}
		}
	}
	
	private void build(List<Map<String, Object>> result, List<Map<String, Object>> newList,
			List<List<Map<String, Object>>> myMatrix){
		if (newList != null){
			if (newList.size() > 0){
				result.add(newList.get(0));
				//valueMatrix.add(result);
				myMatrix.add(result);
			}
//			else{
//				result.add(new HashMap<String, Object>());
//			}
//			
		}
	}
	
	private void rebuild(List<List<Map<String, Object>>> myMatrix){
		if (!myMatrix.isEmpty()){
			if (myMatrix.size() == valueMatrix.size()){
				for(int i=0; i<myMatrix.size(); i++){
					List<Map<String, Object>> m = myMatrix.get(i);
					List<Map<String, Object>> v = valueMatrix.get(i);
					v.add(m.get(m.size()-1));
				}
			}
			else{
				valueMatrix = new ArrayList<List<Map<String, Object>>>();
				for(List<Map<String, Object>> l : myMatrix){
					valueMatrix.add(l);
				}
			}
		}
	}
	
	private List<Map<String, Object>> getDimFromMatrix(int index){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (valueMatrix != null){
			for(List<Map<String, Object>> l : valueMatrix){
				list.add(l.get(index));
			}
		}
		return list;
	}
	/**
	 * 统计数据缓存
	 * @param item			指标项
	 * @param sql			统计语句
	 * @param depDataSet	依赖数据集名称
	 * @param depColumn		依赖列名
	 */
	@SuppressWarnings("unchecked")
	private void setResultCache(TjTableItem item, String sql, String depDataSet, String depColumn, Object[] objects) throws RepsException{
		try{
			List<Map<String, Object>> list;
			
			if (StringUtils.isNotBlank(sql)){
				//list = jdbcDao.queryForList(sql, new Object[]{});
				list = jdbcDao.queryForList(sql, objects);

				//SQL结果集缓存
				rsMap.put(item.getItemName(), list);
			}
			else{
				list = (List<Map<String, Object>>)rsMap.get(StringUtils.isBlank(depDataSet) ? item.getItemName() : depDataSet);
			}

			//保存值缓存

			//累加结果集，如果已经存在的话
			List<String> values = valueMap.get(item.getItemName());
//			List<String> dictValues = valueMap.get(item.getItemName()+"_name");
			
			if (values == null){
				values = new ArrayList<String>();
			}
//			if (dictValues == null){
//				dictValues = new ArrayList<String>();
//			}

			if (list == null || list.isEmpty()){
				if ("运算".equals(item.getExpressionType())){
					//准备参与运算的数据集
					List<String> elements = new ArrayList<String>();
					
					for(TjTableItem myItem : items){
						if (item.getExpression().contains(myItem.getItemName())){
							elements.add(myItem.getItemName());
						}
					}
					
					if (elements.size() > 0){
						//取出第一个作为索引
						String expression;
						double d;
						List<String> firstItemValue = valueMap.get(elements.get(0));
						for(int i=0; i<firstItemValue.size(); i++){
							expression = item.getExpression();
							expression = expression.replaceAll(elements.get(0), firstItemValue.get(i));
							
							for(int j=1; j<elements.size(); j++){
								List<String> otherItemValue = valueMap.get(elements.get(j));
								expression = expression.replaceAll(elements.get(j), otherItemValue.get(i));
							}
							d = MathEval.eval(expression);
							if (String.valueOf(d).equals("Infinity") || String.valueOf(d).equals("NaN")){
								d = 0;
							}
							values.add(String.valueOf(d));
						}
					}
				}
				else if (!item.getItemName().equals(indexItem.getItemName())){
					values.add("");
//					if (item.getIsDictionary() == 1){
//						dictValues.add("");
//					}
				}
			}
			else{
				if (StringUtils.isBlank(depColumn)){
					Map<String, Object> map = list.get(0);
					List<String> fields = new ArrayList<String>();
					Set keys = map.keySet();
					if (keys != null){
						Iterator iterator = keys.iterator();
						while (iterator.hasNext()){
							fields.add(iterator.next().toString());
						}
					}
					depColumn = fields.get(0);
				}
				
				for(Map<String, Object> map : list){
					Object obj = map.get(depColumn);
					String s = obj==null ? "" : obj.toString();
					values.add(s);
					
//					if (item.getIsDictionary() == 1){
//						String tempSql = "select name from " + item.getReferDictionary() + " where code='" + s + "'";
//						List<Map<String, Object>> tempList = jdbc.queryForList(tempSql, new Object[]{});
//						if (tempList == null || tempList.isEmpty()){
//							dictValues.add(s);
//						}
//						else{
//							dictValues.add(tempList.get(0).get("name").toString());
//						}
//					}
				}
			}
			valueMap.put(item.getItemName(), values);
		} catch (DataAccessException e) {
			throw new RepsException(e.getMessage());
		}
	}
	
}
