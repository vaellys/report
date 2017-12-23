<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>输出字段定义修改</title>
	<reps:theme />
</head>
<body>
<reps:container layout="true">
	<reps:panel id="first" dock="top" action="editoutputfieldefined.mvc?indicatorId=${indicatorId }" formId="form" validForm="true" method="post" style="width:800px">
			<reps:formcontent>
			
				<reps:formfield label="结果字段名称" labelStyle="width:20%;" textStyle="width:20%;">
					<input type="hidden" name="id" value="${outputFieldDefined.id }"/>
					<reps:input name="resultFieldName" maxLength="30" required="true">${outputFieldDefined.resultFieldName }</reps:input>
				</reps:formfield>
				
				<reps:formfield label="显示名称" labelStyle="width:15%" textStyle="width:30%;">
					<reps:input name="displayName" maxLength="30" required="true">${outputFieldDefined.displayName}</reps:input>
				</reps:formfield>
				
				<reps:formfield label="字段类型" labelStyle="width:20%;" textStyle="width:20%;">
					<reps:input name="fieldType" maxLength="30" required="true">${outputFieldDefined.fieldType}</reps:input>
				</reps:formfield>
				
				<reps:formfield label="显示顺序" labelStyle="width:15%" textStyle="width:30%;">
					<reps:input name="showOrder" dataType="integernum" required="true">${outputFieldDefined.showOrder}</reps:input>
				</reps:formfield>
				
				<reps:formfield label="显示宽度" labelStyle="width:20%;" textStyle="width:20%;">
					<reps:input name="showWidth" dataType="integernum">${outputFieldDefined.showWidth}</reps:input>
				</reps:formfield>
				
				<reps:formfield label="对齐方式" labelStyle="width:15%" textStyle="width:30%;">
					<reps:input name="alignment" maxLength="30">${outputFieldDefined.alignment}</reps:input>
				</reps:formfield>
				
				<reps:formfield label="可否隐藏" labelStyle="width:20%;" textStyle="width:20%;">
					<reps:input name="isHide" maxLength="30" >${outputFieldDefined.isHide}</reps:input>
				</reps:formfield>
				
				<reps:formfield label="显示格式" labelStyle="width:15%" textStyle="width:30%;">
					<reps:input name="showFormat" maxLength="30" >${outputFieldDefined.showFormat}</reps:input>
				</reps:formfield>
				
			</reps:formcontent>
		<reps:formbar>
			<reps:ajax  messageCode="add.button.save" formId="form" callBack="my" type="button" cssClass="btn_save"></reps:ajax>
			<reps:button cssClass="btn_cancel_a" messageCode="add.button.cancel" onClick="back()"></reps:button>
		</reps:formbar>
		<br/>
		
	</reps:panel>
</reps:container>
</body>
<script type="text/javascript">
var my = function(data){
	messager.message(data, function(){ back(); });
};

function back() {
	window.location.href= "listoutputfieldefined.mvc?indicatorId=${indicatorId }";
}

</script>
</html>