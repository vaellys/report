<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>数据库类型列表</title>
<reps:theme />
</head>
<body>
	<reps:container layout="true">

		<c:if test="${flag != 1 }">
			<reps:panel id="mytop" dock="top">
				<reps:footbar>
					<reps:button cssClass="add-a"
						action="toaddatabasetype.mvc?indicatorId=${indicatorId }"
						messageCode="manage.action.add" value="新增"></reps:button>
				</reps:footbar>
			</reps:panel>
		</c:if>

		<reps:panel id="mybody" dock="center">
			<reps:grid id="itemlist" items="${databaseTypeList}" form="queryForm"
				var="i" flagSeq="false">
				<reps:gridrow>
					<reps:gridfield title="数据库类型" width="30">${i.dbType}</reps:gridfield>
					<reps:gridfield title="字段" width="20">${i.field}</reps:gridfield>
					<c:if test="${flag != 1 }">
						<reps:gridfield title="操作" width="20">
							<reps:button cssClass="modify-table"
								messageCode="manage.action.update"
								action="${ctx}/reps/report/zdyzbdyb/toeditdatabasetype.mvc?indicatorId=${indicatorId }&dbTypeId=${i.id }"></reps:button>
							<reps:ajax cssClass="delete-table"
								messageCode="manage.action.delete" confirm="您确定要删除所选行吗?"
								callBack="my"
								url="${ctx}/reps/report/zdyzbdyb/deletedatabasetype.mvc?dbTypeId=${i.id }&indicatorId=${indicatorId }"></reps:ajax>
						</reps:gridfield>
					</c:if>
				</reps:gridrow>
			</reps:grid>
		</reps:panel>

	</reps:container>
	<script type="text/javascript">
		var my = function(data) {
			messager.message(data, function() {
				window.location.href = "listdatabasetype.mvc?indicatorId=${indicatorId }";
			});
		};
	</script>
</body>
</html>
