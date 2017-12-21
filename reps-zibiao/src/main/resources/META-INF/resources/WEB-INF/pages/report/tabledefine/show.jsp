<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>统计表详细信息</title>
<reps:theme />
</head>
<body>
	<reps:container layout="true">
		<reps:panel id="first" dock="top" action="" formId="objectForm" validForm="true" title="对象信息" 
			border="true" style="margin-top:0px;">
			<reps:detail id="info" borderFlag="false" textBackgroundFlag="false">
				<reps:detailfield label="统计表名称" labelStyle="width:20%" textStyle="width:30%">${tableDefine.chineseName}</reps:detailfield>
				<reps:detailfield label="数据表名" labelStyle="width:18%" textStyle="width:32%">${tableDefine.name}</reps:detailfield>
				<reps:detailfield label="报表类型">${tableDefine.category}报表</reps:detailfield>
				<reps:detailfield label="是否基础表">
					<c:if test="${tableDefine.isBasic == 1}">是</c:if>
				</reps:detailfield>
			</reps:detail>
		</reps:panel>
		<reps:panel id="mygrid" dock="center" title="包含的指标项" border="true">
			<reps:grid id="fieldgrid" items="${tableDefine.items}" var="item" form="Kform" flagSeq="true">
				<reps:gridrow>
					<reps:gridfield title="中文名称" width="11">${item.itemChineseName}</reps:gridfield>
					<reps:gridfield title="指标项名" width="16">${item.itemName}</reps:gridfield>
					<reps:gridfield title="表达式类型" width="8" align="center">${item.expressionType}</reps:gridfield>
					<reps:gridfield title="表达式" width="45">${item.expression}</reps:gridfield>
					<reps:gridfield title="依赖项" width="12">${item.dependency}</reps:gridfield>
					<reps:gridfield title="批次约束项" width="8" align="center">
						<c:if test="${item.batchKey == 1}">是</c:if>
					</reps:gridfield>
				</reps:gridrow>
			</reps:grid>
		</reps:panel>
	</reps:container>
</body>
</html>