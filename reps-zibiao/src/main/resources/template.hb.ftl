<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping
  PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
  "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
	<class
		name="${entity.name}"
		table="${entity.tableName}"
		dynamic-update="false"
		dynamic-insert="false"
		select-before-update="false"
		optimistic-lock="version">
        
		<id
			name="id"
			type="java.lang.String"
			unsaved-value="null">
			<column name="id" sql-type="char(32)" />
<#--
 * 也可以设置成自动增长
 * type="long"
 * <generator class="native" />
 -->
			<generator class="assigned" />
		</id>
		
		<#if entity.columnAttrList?exists>
			<#list entity.columnAttrList as attr>
				<#if attr.name == "id">
				<#elseif attr.columnType == "char">
					<property
						name="${attr.name}"
						type="java.lang.String"
						update="true"
						insert="true"
						access="property"
					>
						<column
							name="${attr.columnName}"
							sql-type="char(${attr.length})"
							not-null="${attr.notNull}"
							unique="${attr.unique}"
						/>
					</property>
				<#elseif attr.columnType == "varchar">
					<property
						name="${attr.name}"
						type="java.lang.String"
						update="true"
						insert="true"
						access="property"
						column="${attr.columnName}"
						length="${attr.length}"
						not-null="${attr.notNull}"
						unique="${attr.unique}"
					/>
				<#elseif attr.columnType == "byte">
					<property
						name="${attr.name}"
						type="java.lang.Byte"
						update="true"
						insert="true"
						access="property"
						column="${attr.columnName}"
						not-null="${attr.notNull}"
						unique="${attr.unique}"
					/>
				<#elseif attr.columnType == "short">
					<property
						name="${attr.name}"
						type="java.lang.Short"
						update="true"
						insert="true"
						access="property"
						column="${attr.columnName}"
						not-null="${attr.notNull}"
						unique="${attr.unique}"
					/>
				<#elseif attr.columnType == "int">
					<property
						name="${attr.name}"
						type="java.lang.Integer"
						update="true"
						insert="true"
						access="property"
						column="${attr.columnName}"
						length="${attr.length}"
						not-null="${attr.notNull}"
						unique="${attr.unique}"
					/>
				<#elseif attr.columnType == "long">
					<property
						name="${attr.name}"
						type="java.lang.Long"
						update="true"
						insert="true"
						access="property"
						column="${attr.columnName}"
						not-null="${attr.notNull}"
						unique="${attr.unique}"
					/>
				<#elseif attr.columnType == "float">
					<property
						name="${attr.name}"
						type="java.lang.Float"
						update="true"
						insert="true"
						access="property"
						column="${attr.columnName}"
						length="${attr.length+10}"
						not-null="${attr.notNull}"
						unique="${attr.unique}"
					/>
				<#elseif attr.columnType == "nvarchar">
					<property
						name="${attr.name}"
						type="java.lang.String"
						update="true"
						insert="true"
						access="property"
						column="${attr.columnName}"
						length="${attr.length*3}"
						not-null="${attr.notNull}"
						unique="${attr.unique}"
					/>
				<#elseif attr.columnType == "datetime">
					<property
						name="${attr.name}"
						type="calendar"
						update="true"
						insert="true"
						access="property"
						column="${attr.columnName}"
						length="${attr.length}"
						not-null="${attr.notNull}"
						unique="${attr.unique}"
					/>
				<#else>
					<property
						name="${attr.name}"
						type="${attr.columnType}"
						update="true"
						insert="true"
						access="property"
						column="${attr.columnName}"
						not-null="${attr.notNull}"
						unique="${attr.unique}"
					/>
				</#if>
			</#list>
		</#if>
	</class>
</hibernate-mapping>
