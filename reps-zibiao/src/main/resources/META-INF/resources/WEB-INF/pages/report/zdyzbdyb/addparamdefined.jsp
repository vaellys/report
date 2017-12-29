<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>参数定义添加</title>
	<reps:theme />
</head>
<body>
<reps:container layout="true">
	<reps:panel id="first" dock="top" action="addparamdefined.mvc?indicatorId=${indicatorId }" formId="form" validForm="true" method="post" style="width:800px;">
			<reps:formcontent style="margin-left:110px;">
				
				<reps:formfield label="参数名" fullRow="true">
					<reps:input name="paramName" maxLength="30" required="true"></reps:input>
				</reps:formfield>
				
				<reps:formfield label="说明" fullRow="true">
					<reps:input name="description" maxLength="30" required="true"></reps:input>
				</reps:formfield>
				
				<reps:formfield label="类型" fullRow="true">
					<reps:input name="type" maxLength="30" required="true"></reps:input>
				</reps:formfield>
				
			</reps:formcontent>
		<reps:formbar >
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
	window.location.href= "listparamdefined.mvc?indicatorId=${indicatorId }";
}

</script>
</html>