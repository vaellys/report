<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>输出字段定义列表</title>
<reps:theme />
</head>
<body>
	<reps:container layout="true">

		<c:if test="${flag != 1 }">
			<reps:panel id="mytop" dock="top">
				<reps:footbar>
					<reps:button cssClass="add-a"
						action="toaddoutputfieldefined.mvc?indicatorId=${indicatorId }"
						messageCode="manage.action.add" value="新增"></reps:button>
				</reps:footbar>
			</reps:panel>
		</c:if>

		<reps:panel id="mybody" dock="center">
			<reps:grid id="itemlist" items="${outputFieldDefinedList}" form="queryForm"
				var="i" flagSeq="false">
				<reps:gridrow>
					<reps:gridfield title="结果字段名称" width="20">${i.resultFieldName}</reps:gridfield>
					<reps:gridfield title="显示名称" width="20">${i.displayName}</reps:gridfield>
					<reps:gridfield title="字段类型" width="20">${i.fieldType}</reps:gridfield>
					<reps:gridfield title="显示顺序" width="20">${i.showOrder}</reps:gridfield>
					<reps:gridfield title="显示宽度" width="20">${i.showWidth}</reps:gridfield>
					<reps:gridfield title="对齐方式" width="20">${i.alignment}</reps:gridfield>
					<reps:gridfield title="可否隐藏" width="20">${i.isHide}</reps:gridfield>
					<reps:gridfield title="显示格式" width="20">${i.showFormat}</reps:gridfield>
					<c:if test="${flag != 1 }">
						<reps:gridfield title="操作" width="30">
							<reps:button cssClass="modify-table"
								messageCode="manage.action.update"
								action="${ctx}/reps/report/zdyzbdyb/toeditoutputfieldefined.mvc?indicatorId=${indicatorId }&outputFieldDefId=${i.id }"></reps:button>
							<reps:ajax cssClass="delete-table"
								messageCode="manage.action.delete" confirm="您确定要删除所选行吗?"
								callBack="my"
								url="${ctx}/reps/report/zdyzbdyb/deleteoutputfieldefined.mvc?outputFieldDefId=${i.id }&indicatorId=${indicatorId }"></reps:ajax>
						</reps:gridfield>
					</c:if>
				</reps:gridrow>
			</reps:grid>
		</reps:panel>

	</reps:container>
	<script type="text/javascript">
		var my = function(data) {
			messager.message(data, function() {
				window.location.href = "listoutputfieldefined.mvc?indicatorId=${indicatorId }";
			});
		};
	</script>
</body>
</html>
