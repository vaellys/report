<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>关联明细指标修改</title>
	<reps:theme />
</head>
<body>
<reps:container layout="true">
	<reps:panel id="first" dock="top" action="editdetailsindicator.mvc?indicatorId=${indicatorId }" formId="form" validForm="true" method="post" style="width:800px">
			<reps:formcontent>
			
				<reps:formfield label="明细指标ID" fullRow="true">
					<input type="hidden" name="id" value="${detailsIndicator.id }"/>
					<reps:input name="detailIndicId" maxLength="30" required="true">${detailsIndicator.detailIndicId }</reps:input>
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
	window.location.href= "listdetailsindicator.mvc?indicatorId=${indicatorId }";
}

</script>
</html>