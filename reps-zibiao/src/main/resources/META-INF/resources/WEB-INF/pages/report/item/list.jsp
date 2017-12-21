<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>指标管理</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel id="top" dock="top" title="分类：${path}" method="post" action="${ctx}/reps/report/item/list.mvc" formId="queryForm">
		<reps:formcontent parentLayout="true" style="width:80%;">
			<reps:formfield label="指标中文名称" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="chineseName"></reps:input>
			</reps:formfield>
			<reps:formfield label="所在类别" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:select dataSource="${categoryList}" name="categoryId">${categoryId}</reps:select>
			</reps:formfield>
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="itemlist" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
		<reps:footbar>
			<reps:button cssClass="add-a" messageCode="manage.action.add"
				action="${ctx}/reps/report/item/toadd.mvc?categoryId=${categoryId}" value="添加指标"></reps:button>
		</reps:footbar>
	</reps:panel>
	<reps:panel id="mybody" dock="center">
		<reps:grid id="itemlist" items="${itemList}" form="queryForm" var="item" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="指标名称" width="15">${item.name}</reps:gridfield>
				<reps:gridfield title="指标中文名称" width="12">${item.chineseName}</reps:gridfield>
				<reps:gridfield title="所在类别" width="12">
					<c:forEach items="${categoryList}" var="category">
						<c:if test="${item.categoryId == category.key}">${category.value}</c:if>
					</c:forEach>
				</reps:gridfield>
				<reps:gridfield title="表达式" width="40">${item.defaultExpression}</reps:gridfield>
				<reps:gridfield title="表达式类型" width="8" align="center">${item.expressionType}</reps:gridfield>
				<reps:gridfield title="操作" width="13">
					<reps:button cssClass="modify-table" messageCode="manage.action.update" 
						action="${ctx}/reps/report/item/toedit.mvc?id=${item.id}"></reps:button>
					<reps:ajax cssClass="delete-table" messageCode="manage.action.delete" confirm="您确定要删除所选行吗?"
					callBack="my" url="${ctx}/reps/report/item/delete.mvc?id=${item.id}"></reps:ajax>
				</reps:gridfield>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var my = function(data){
		messager.message(data, function(){ window.location.reload(); });
	};
</script>
</body>
</html>
