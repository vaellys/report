package com.reps.tj.util;

import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import com.reps.core.SpringContext;
import com.reps.core.exception.RepsException;

import freemarker.template.Template;

/**
 * 数据库表创建器
 * @author Karlova
 */
public class TableGenerator {

	private FormTable tableVo;
	private String scriptFileName = "";
	
	/**
	 * 构造函数
	 * @param tableVo
	 */
	public TableGenerator(FormTable tableVo) {
		this.tableVo = tableVo;
	}
	
	/**
	 * 构造函数
	 * @param tableVo
	 * @param scriptFileName
	 */
	public TableGenerator(FormTable tableVo, String scriptFileName) {
		this.tableVo = tableVo;
		this.scriptFileName = scriptFileName;
	}
	
	public void generatorTable() throws RepsException{
		if (tableVo.getColumnAttrList().isEmpty()) {
			throw new RepsException("自动创建表时，没有属性列");
		}
		
		Template tpl;
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("entity", tableVo);
			
			//tpl = getTemplateConfig("/com/ftl").getTemplate("template.hb.ftl");
			tpl = getTemplateConfig("/").getTemplate("template.hb.ftl");
			
			Writer out = new StringWriter();
			tpl.process(paramMap, out);
			String hbxml = out.toString();
	
			System.out.println(hbxml);
			
			Configuration hbcfg = this.getHibernateCfg(hbxml);
			
			createDbTableByCfg(hbcfg);
			
		} catch (Exception e) {
			throw new RepsException(e.getMessage());
		}
	}
	
	/**
	 * 获取freemarker的cfg
	 * @param resource
	 * @return Configuration
	 */
	protected freemarker.template.Configuration getTemplateConfig(String resource) {
		freemarker.template.Configuration cfg = new freemarker.template.Configuration();
		cfg.setDefaultEncoding("UTF-8");
		cfg.setClassForTemplateLoading(this.getClass(), resource);
	
		return cfg;
	}
	
	/**
	 * 处理hibernate的配置文件
	 * @param resource
	 */
	protected Configuration getHibernateCfg(String hbxml) {
		
		/**
		 * 1. 通过Hiberante配置
		 */
		//org.hibernate.cfg.Configuration hbcfg = new org.hibernate.cfg.Configuration();
		//hbcfg.configure("/hibernate.cfg.xml");

		/**
		 * 2. 通过Spring+Hibernate配置
		 *    &sessionFactory: 一定要包含 & ，不加Spring返回的是Hibernate下的SessionFactoryImpl类
		 */
		// 调试时，使用
//		org.springframework.core.io.ClassPathResource
//			ac = new org.springframework.core.io.ClassPathResource("applicationContext.xml");
//		org.springframework.beans.factory.xml.XmlBeanFactory
//			xbf = new org.springframework.beans.factory.xml.XmlBeanFactory(ac);
//		LocalSessionFactoryBean lsfb=(LocalSessionFactoryBean) xbf.getBean("&sessionFactory");
		
		// 正式环境
		LocalSessionFactoryBean lsfb = (LocalSessionFactoryBean) SpringContext.getBean("&sessionFactory");
		
		
		
		Configuration hbcfg = lsfb.getConfiguration();
		
		// 必须使用新建的Hibernate配置，hbcfg里面含有数据库所有实体表的mapping
		Configuration cfg = new Configuration();
		
		Properties extraProp = new Properties();
		extraProp.put("hibernate.hbm2ddl.auto", "none");
		
		cfg.addProperties(extraProp);
		cfg.setProperties(hbcfg.getProperties());
		cfg.addXML(hbxml);

//		System.out.println(hbcfg.getProperty("hibernate.dialect"));
		
//		Iterator classes = hbcfg.getClassMappings();
//		while ( classes.hasNext() ) {
//			PersistentClass model = (PersistentClass) classes.next();  
//			
//			System.out.println("-----------entity name: "+model.getEntityName());
//		        if ( !model.isInherited() ) {  
//		            IdentifierGenerator generator = model.getIdentifier().createIdentifierGenerator(  
//		                    settings.getDialect(),  
//		                    settings.getDefaultCatalogName(),  
//		                    settings.getDefaultSchemaName(),  
//		                    (RootClass) model  
//		                );  
//		            identifierGenerators.put( model.getEntityName(), generator );  
//		        }  
//		}
		
		
		return cfg;
	}
	
	/**
	 * 根据hibernate cfg配置文件动态建表
	 * @param hbcfg
	 */
	public void createDbTableByCfg(Configuration hbcfg) {
		SchemaExport schemaExport;
		
		schemaExport = new SchemaExport(hbcfg);
		
		//通过生成sql文件，用jdbc来执行
//		schemaExport.setOutputFile(scriptFileName);
		schemaExport.drop(false, true);
		schemaExport.create(false, true);
//		schemaExport.execute(false, true, false, true);
		
		
//		FileReadWrite fReader = new FileReadWrite();
//		String sql = fReader.readTxt(scriptFileName);
		
//		if (StringUtils.isNotBlank(sql)){
//			jdbcDao.execute(sql);
//		}

	}
	
	/**
	 * 根据配置文件Connection 来动态建表
	 * @param conf
	 * @param ds
	 */
	public void createDbTableByConn(Configuration conf, DataSource ds) throws RepsException {
		SchemaExport schemaExport;
		
		try {
			schemaExport = new SchemaExport(conf, ds.getConnection());
//			schemaExport.setOutputFile(scriptFileName);
			schemaExport.execute(false, true, false, true);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new RepsException(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepsException(e.getMessage());
		}
		
	}

	public String getScriptFileName() {
		return scriptFileName;
	}

	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
	}
	
	
}

