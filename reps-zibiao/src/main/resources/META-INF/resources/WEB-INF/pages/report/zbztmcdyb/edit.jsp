<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>指标主题修改</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="first" dock="top" action="edit.mvc" formId="xform" validForm="true" style="width:750px">
		<reps:formcontent>
			<input type="hidden" value="${topic.zbztId}" name="zbztId">
			<reps:formfield label="指标主题名称" labelStyle="width:20%" textStyle="width:30%">
				<reps:input name="zbztmc" maxLength="40" required="true">${topic.zbztmc}</reps:input>
			</reps:formfield>
			<reps:formfield label="命名空间" labelStyle="width:20%" textStyle="width:30%">
				<reps:select dataSource="${namespaceList}" name="tjNamespace.namespace" required="true">${topic.tjNamespace.namespace }</reps:select>
			</reps:formfield>
			<reps:formfield label="权限类型" labelStyle="width:20%" textStyle="width:30%">
				<reps:input name="qxlx" dataType="integernum">${topic.qxlx }</reps:input>
			</reps:formfield>
			<reps:formfield label="所属单位" labelStyle="width:20%" textStyle="width:30%">
				<reps:input name="ssdw" maxLength="80" >${topic.ssdw }</reps:input>
			</reps:formfield>
			<reps:formfield label="指标主题说明" fullRow="true">
				<reps:input name="zbztsm" maxLength="200" multiLine="true" style="width:546px;height:70px">${topic.zbztsm }</reps:input>
			</reps:formfield>
		</reps:formcontent>
		<reps:formbar>
			<reps:ajax messageCode="edit.button.save" formId="xform" callBack="skip" type="link"
				confirm="确定要提交修改？" cssClass="btn_save_a">
			</reps:ajax>
			<reps:button cssClass="btn_cancel_a" messageCode="add.button.cancel" action="list.mvc"></reps:button>
		</reps:formbar>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var skip = function(data) {
		messager.message(data, function() {
			var gid = $("#gid").val();
			parent.location.href = "${ctx}/reps/report/namespace/index.mvc";
		});
	};
</script>
</body>
</html>