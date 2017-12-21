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
	<reps:panel id="top" dock="top" title="所在路径：${path}" method="post" action="${ctx}/reps/report/zdyzbdyb/list.mvc" formId="queryForm">
		<reps:formcontent parentLayout="true" style="width:80%;">
			<reps:formfield label="指标名称" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="zbmc"></reps:input>
				<input type="hidden" name="namespace" value=${namespace }>
			</reps:formfield>
			<reps:formfield label="所在主题" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:select dataSource="${topicList}" name="zbztId">${topic.zbztId }</reps:select>
			</reps:formfield>
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="itemlist" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
		<reps:footbar>
			<c:if test="${not empty topic.zbztId }">
			 	<reps:button cssClass="add-a" messageCode="manage.action.add"
				action="${ctx}/reps/report/zdyzbdyb/toadd.mvc?topicId=${topic.zbztId}" value="添加指标"></reps:button>
			</c:if>
			<c:if test="${not empty indicator.id }">
			 	<reps:button cssClass="add-a" messageCode="manage.action.add"
				action="${ctx}/reps/report/zdyzbdyb/toadd.mvc?indicatorId=${indicator.id}" value="添加指标"></reps:button>
				<reps:button cssClass="open-a" messageCode="manage.action.open" 
							action="${ctx}/reps/report/zdyzbdyb/show.mvc?id=${indicator.id}&isDetail=true" value="指标详细"></reps:button>
			</c:if>
		</reps:footbar>
	</reps:panel>
	<reps:panel id="mybody" dock="center">
		<reps:grid id="itemlist" items="${indicatorList}" form="queryForm" var="i" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="指标ID" width="10">${i.id}</reps:gridfield>
				<reps:gridfield title="指标名称" width="10">${i.zbmc}</reps:gridfield>
				<reps:gridfield title="所在主题" width="10" align="center">
					<c:forEach items="${topicList}" var="topic">
						<c:if test="${i.zbztId == topic.key}">${topic.value}</c:if>
					</c:forEach>
				</reps:gridfield>
				<reps:gridfield title="指标算法" width="13">${i.zbsf}</reps:gridfield>
				<reps:gridfield title="指标元数据" width="14" >${i.zbmeta}</reps:gridfield>
				<reps:gridfield title="指标算法MSSQL" width="15" >${i.zbsfMssql}</reps:gridfield>
				<reps:gridfield title="指标算法MYSQL" width="13" >${i.zbsfMysql}</reps:gridfield>
				<reps:gridfield title="指标算法ORACLE" width="15" >${i.zbsfOracle}</reps:gridfield>
				<reps:gridfield title="指标算法MONGODB" width="15" >${i.zbsfMongodb}</reps:gridfield>
				<reps:gridfield title="操作" width="28">
					<reps:button cssClass="add-table" value="添加子级指标" action="${ctx}/reps/report/zdyzbdyb/toadd.mvc?indicatorId=${i.id}"></reps:button>
					<reps:button cssClass="modify-table" messageCode="manage.action.update" 
						action="${ctx}/reps/report/zdyzbdyb/toedit.mvc?id=${i.id}&topicId=${topic.zbztId}"></reps:button>
					<reps:ajax cssClass="delete-table" messageCode="manage.action.delete" confirm="您确定要删除所选行吗?"
					callBack="my" url="${ctx}/reps/report/zdyzbdyb/delete.mvc?id=${i.id}"></reps:ajax>
				</reps:gridfield>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var my = function(data){
		messager.message(data, function(){ window.parent.location.reload(); });
	};
</script>
</body>
</html>
