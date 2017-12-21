<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>指标分类详细信息</title>
	<reps:theme />
</head>
<body>
<reps:container layout="true">
	<reps:panel id="myform" title="分类:${path}" dock="none" formId="xform" validForm="false">
		<reps:detail id="iteminfo" borderFlag="true" textBackgroundFlag="false" style="width:800px">
			<reps:detailfield label="指标名称" fullRow="true" labelStyle="width:20%;">${info.name}</reps:detailfield>
			<reps:detailfield label="指标中文名称" fullRow="true">${info.chineseName}</reps:detailfield>
			<reps:detailfield label="所属分类" fullRow="true">
				<c:forEach items="${itemCateList}" var="ic">
					<c:if test="${info.categoryId == ic.id}">${ic.name}</c:if>
				</c:forEach>
			</reps:detailfield>
			<reps:detailfield label="字段类型" fullRow="true">${info.fieldType}</reps:detailfield>
			<reps:detailfield label="字段长度" fullRow="true">${info.fieldLength}</reps:detailfield>
			<reps:detailfield label="表达式类型" fullRow="true">${info.expressionType}</reps:detailfield>
			<reps:detailfield label="表达式" fullRow="true">${info.defaultExpression}</reps:detailfield>
		</reps:detail>
		<br/><br/>
		<reps:formbar>
			<reps:button cssClass="btn_back_a" messageCode="manage.action.return"
				action="${ctx}/reps/report/itemcategory/list.mvc?parentId=${info.categoryId}"></reps:button>
		</reps:formbar>
	</reps:panel>
</reps:container>
</body>
</html>