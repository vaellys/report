<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>报表数据维护列表</title>
	<reps:theme />
</head>
<body>
<reps:container layout="true">
	<reps:panel id="top" dock="top"  formId="queryForm">
		<reps:footbar>
			<reps:button cssClass="add-a" messageCode="manage.action.add" action="toadd.mvc?did=${define.id}" value="添加"></reps:button>
		</reps:footbar>
	</reps:panel>
	<reps:panel id="mygrid" dock="center">
		<reps:grid id="fieldgrid" items="${list}" var="items" form="Kform" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<c:if test="${not empty items}">
					<c:forEach items="${items}" var="c">
						<c:if test="${c.getKey() eq 'id'}">
							<c:set value="${c.getValue()}" var="id"/>
						</c:if>
						<c:if test="${c.getKey() ne 'id'}">
							<reps:gridfield title="${c.getKey()}" width="20">${c.getValue()}</reps:gridfield>
						</c:if>
					</c:forEach>
					<c:if test="${not empty list}">
						<reps:gridfield title="操作" width="45">
							<reps:button cssClass="modify-table" messageCode="manage.action.update" action="toedit.mvc?did=${define.id}&id=${id}"></reps:button>
							<reps:ajax cssClass="delete-table" messageCode="manage.action.delete" confirm="您确定要删除所选行吗？"
								callBack="my" url="delete.mvc?did=${define.id}&id=${id}">
							</reps:ajax>
						</reps:gridfield>
					</c:if>
				</c:if>
				<c:if test="${empty items}">
					<c:forEach items="${define.items}" var="t">
						<reps:gridfield title="${t.itemChineseName}" width="25">0</reps:gridfield>
					</c:forEach>
				</c:if>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>
</body>
<script type="text/javascript">
	var my = function(data){
		messager.message(data, function(){ window.location.reload(); });
	};
</script>
</html>