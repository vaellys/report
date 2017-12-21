package com.reps.tj.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.reps.core.exception.RepsException;
import com.reps.core.util.DateUtil;
import com.reps.core.util.IDGenerator;
import com.reps.tj.entity.TjTableDefine;
import com.reps.tj.entity.TjTableItem;
import com.reps.tj.util.ColumnAttribute;
import com.reps.tj.util.FormTable;
//import com.reps.tj.util.MathEval;
import com.reps.tj.util.TableGenerator;

public class TestMain {
	private static TestMain test;
	
	public static void main(String[] args)
	{
//		test = new TestMain();
//		test.runTj();
//		double a = MathEval.eval("0 / 1");
//		if (String.valueOf(a).equals("Infinity")){
//			System.out.println("is inf");
//		}
		//String a =DateUtil.unformatStrCsDateTime("1287年10月2日");
		String a =DateUtil.unformatStrDateTime("1287年10月2日");
		System.out.println(a);
	}
	
	public void runTj(){
		List<TjTableDefine> tables = Jdbc.getTable();
		for(TjTableDefine t : tables){
			//创建表
//			createTable(t);
			
			//统计数据
			stat(t.getId(), t.getName());
		}
	}
	
	@SuppressWarnings("unused")
	private void createTable(TjTableDefine t){
		List<ColumnAttribute> list = test.initData(t.getId());
		
		FormTable fromTable = new FormTable();
		
		fromTable.setTableName(t.getName());
		
		fromTable.addColunms(list);
		TableGenerator tg = new TableGenerator(fromTable);
		try {
			tg.generatorTable();
		} catch (RepsException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void stat(String tableId, String tableName){
		
		//每条SQL查询出来的结果集
		Map<String, Object> rsMap = new HashMap<String, Object>();
		//需要保存入库的每个指标对应的值集
		Map<String, List> valueMap = new HashMap<String, List>();
		
		List<TjTableItem> items = Jdbc.getTableItems(tableId);
		
//		Map<String, TjTableItem> map = Jdbc.listToMap(items);

		for(TjTableItem item : items){
			//解析依赖项，支持多项依赖
			String[] depItem = null, depValue = null;
			
			if (StringUtils.isNotBlank(item.getDependency())){
				String[] temp = item.getDependency().split(",");
				depItem = new String[temp.length];
				depValue = new String[temp.length];
				
				for(int i=0; i<temp.length; i++){
					int pos = temp[i].indexOf("['");
					if (pos >= 0){
						depItem[i] = temp[i].substring(0, pos);
						depValue[i] = temp[i].substring(pos+2, temp[i].length()-2);
					}
					else{
						depItem[i] = temp[i];
						depValue[i] = depItem[i];
					}
				}
			}
			
			//运算表达式
			if ("SQL".equals(item.getExpressionType())){
				String sql = item.getExpression();
				
				if (StringUtils.isNotBlank(item.getDependency())){
					//以第一个依赖项的结果集作为引导
					List<Map> masterList = (List<Map>)rsMap.get(depItem[0]);
					
					String newSql = "", restSql = "";
					for(int i=0; i<masterList.size(); i++){
						Map masterMap = masterList.get(i);
						
						//依赖的SQL，需要重新运算
						int pos = sql.indexOf("?");
						int length = sql.length();
						newSql = sql.substring(0, pos) + "'" + masterMap.get(depValue[0]) + "'";
						if (pos < length-1){
							restSql = sql.substring(pos+1, length);
						}
						
/*						不能采用以下算法，如果被替代的值里面带有问号，转换出来的SQL将出现异常
						newSql = ++pos == length ? sql.substring(0, pos-1) + "'" + masterMap.get(depValue[0]) + "'" :
							sql.substring(0, pos-1) + "'" + masterMap.get(depValue[0]) + "'" + sql.substring(pos, length);
*/						
						for(int j=1; j<depItem.length; i++){
							List<Map> slaveList = (List<Map>)rsMap.get(depItem[j]);
							Map slaveMap = slaveList.get(i);
							
							pos = restSql.indexOf("?");
							length = restSql.length();
							newSql += restSql.substring(0, pos) + "'" + slaveMap.get(depValue[0]) + "'";
							if (pos < length-1){
								restSql = restSql.substring(pos+1, length);
							}

//							sql = ++pos == length ? sql.substring(0, pos-1) + "'" + slaveMap.get(depValue[0]) + "'" :
//								sql.substring(0, pos-1) + "'" + slaveMap.get(depValue[0]) + "'" + sql.substring(pos, length);
						}
						newSql += restSql;
						
//						System.out.println(item.getItemName() + "=" + newSql);
						
						List<Map<String, String>> list = Jdbc.executeQuery(newSql);
						//SQL结果集缓存
						rsMap.put(item.getItemName(), list);

						//保存值缓存
						//合并结果集，如果已经存在的话
						List<String> values = valueMap.get(item.getItemName());
						if (values == null){
							values = new ArrayList<String>();
						}
						
						for(Map map : list){
							String s = map.get(item.getItemName()).toString();
							values.add(s);
//							System.out.println(item.getItemName() + "=" + s);
						}
						valueMap.put(item.getItemName(), values);
					}

				}
				else{
					List<Map<String, String>> list = Jdbc.executeQuery(sql);
					//SQL结果集缓存
					rsMap.put(item.getItemName(), list);
					
					//保存值缓存
					List<String> values = new ArrayList<String>();
					for(Map map : list){
						String s = map.get(item.getItemName()).toString();
						values.add(s);
//						System.out.println(item.getItemName() + "=" + s);
					}
					valueMap.put(item.getItemName(), values);
				}
			}
			else if ("变量".equals(item.getExpressionType())){
				if (StringUtils.isNotBlank(item.getDependency())){
					List<Map> list = (List<Map>)rsMap.get(depItem[0]);
					
					//保存值缓存
					List<String> values = new ArrayList<String>();
					for(Map map : list){
						String s = map.get(item.getItemName()).toString();
						values.add(s);
					}
					valueMap.put(item.getItemName(), values);
				}
			}
		}
		
		//入库保存统计结果
		List indexList = valueMap.get(((TjTableItem)items.get(0)).getItemName());
		
		for(int i=0; i<indexList.size(); i++){
			String sqlInsert = "", sqlValue = "", value = "";
			
			for(TjTableItem item : items){

				List tempList = valueMap.get(item.getItemName());
				
				value = tempList.get(i).toString();
				
				if (StringUtils.isNotBlank(sqlInsert)){
					sqlInsert += "," + item.getItemName();
					sqlValue += ",'" + value + "'";
				}
				else{
					sqlInsert += item.getItemName();
					sqlValue += "'" + value + "'";

				}
			}

			//提交
			value = IDGenerator.generate();
			sqlInsert = "insert into " + tableName + "(id," + sqlInsert + ") ";
			sqlValue = "values('" + value + "'," + sqlValue + ")";
			System.out.println("sqlInsert="+sqlInsert+sqlValue);
			
			Jdbc.execute(sqlInsert + sqlValue);
		}
		

	}
	
	/**
	 * 初始化数据
	 * @return
	 */
	private List<ColumnAttribute> initData(String tableId) {
		
		List<TjTableItem> items = Jdbc.getTableItems(tableId);
		
		List<ColumnAttribute> list = new ArrayList<ColumnAttribute>();
		for(TjTableItem item : items){
			ColumnAttribute attr = new ColumnAttribute();
			attr.setColumnName(item.getItemName());
			attr.setColumnType(item.getFieldType());
			if (StringUtils.isNotBlank(item.getFieldLength())){
				attr.setLength(Integer.valueOf(item.getFieldLength()));
			}
			list.add(attr);
		}

		return list;
	}

}
