<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>参数定义列表</title>
<reps:theme />
</head>
<body>
	<reps:container layout="true">

		<c:if test="${flag != 1 }">
			<reps:panel id="mytop" dock="top">
				<reps:footbar>
					<reps:button cssClass="add-a"
						action="toaddparamdefined.mvc?indicatorId=${indicatorId }"
						messageCode="manage.action.add" value="新增"></reps:button>
				</reps:footbar>
			</reps:panel>
		</c:if>

		<reps:panel id="mybody" dock="center">
			<reps:grid id="itemlist" items="${paramDefinedList}" form="queryForm"
				var="i" flagSeq="false">
				<reps:gridrow>
					<reps:gridfield title="参数名" width="30">${i.paramName}</reps:gridfield>
					<reps:gridfield title="说明" width="20">${i.description}</reps:gridfield>
					<reps:gridfield title="类型" width="20">${i.type}</reps:gridfield>
					<c:if test="${flag != 1 }">
						<reps:gridfield title="操作" width="20">
							<reps:button cssClass="modify-table"
								messageCode="manage.action.update"
								action="${ctx}/reps/report/zdyzbdyb/toeditparamdefined.mvc?indicatorId=${indicatorId }&paramDefId=${i.id }"></reps:button>
							<reps:ajax cssClass="delete-table"
								messageCode="manage.action.delete" confirm="您确定要删除所选行吗?"
								callBack="my"
								url="${ctx}/reps/report/zdyzbdyb/deleteparamdefined.mvc?paramDefId=${i.id }&indicatorId=${indicatorId }"></reps:ajax>
						</reps:gridfield>
					</c:if>
				</reps:gridrow>
			</reps:grid>
		</reps:panel>

	</reps:container>
	<script type="text/javascript">
		var my = function(data) {
			messager.message(data, function() {
				window.location.href = "listparamdefined.mvc?indicatorId=${indicatorId }";
			});
		};
	</script>
</body>
</html>
