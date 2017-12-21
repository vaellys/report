<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>指标主题添加</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="first" dock="top" action="add.mvc" formId="form" validForm="true" style="width:750px">
		<reps:formcontent>
			<reps:formfield label="指标主题名称" labelStyle="width:20%" textStyle="width:30%">
				<reps:input name="zbztmc" maxLength="40" required="true"></reps:input>
			</reps:formfield>
			<reps:formfield label="命名空间" labelStyle="width:20%" textStyle="width:30%">
				<reps:select dataSource="${namespaceList}" name="tjNamespace.namespace" required="true">${tjNamespace.namespace }</reps:select>
				<input type="hidden" name="tjNamespace.namespace" value="${tjNamespace.namespace}">
			</reps:formfield>
			<reps:formfield label="权限类型" labelStyle="width:20%" textStyle="width:30%">
				<reps:input name="qxlx" dataType="integernum"></reps:input>
			</reps:formfield>
			<reps:formfield label="所属单位" labelStyle="width:20%" textStyle="width:30%">
				<reps:input name="ssdw" maxLength="80" ></reps:input>
			</reps:formfield>
			<reps:formfield label="指标主题说明" fullRow="true">
				<reps:input name="zbztsm" maxLength="200" multiLine="true" style="width:546px;height:70px"></reps:input>
			</reps:formfield>
		</reps:formcontent>
		<br/>
		<reps:formbar>
			<reps:ajax  messageCode="add.button.save" formId="form" callBack="skip" type="button" cssClass="btn_save"></reps:ajax>
			<reps:button cssClass="btn_cancel_a" messageCode="add.button.cancel" action="list.mvc"></reps:button>
		</reps:formbar>
	</reps:panel>
</reps:container>
</body>
<script type="text/javascript">
	var skip = function(data) {
		messager.message(data, function() {
			parent.location.href = "${ctx}/reps/report/namespace/index.mvc";
		});
	};
</script>
</html>