<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>统计项目分类修改</title>
	<reps:theme />
</head>
<body>
<reps:container layout="true">
	<reps:panel id="first" dock="top" action="editstatisticsitemcategory.mvc?indicatorId=${indicatorId }" formId="form" validForm="true" method="post" style="width:800px">
			<reps:formcontent>
			
				<reps:formfield label="统计项目分类ID" fullRow="true">
					<input type="hidden" name="id" value="${statisticsItemCategory.id }"/>
					<reps:input name="itemId" maxLength="30" required="true">${statisticsItemCategory.itemId }</reps:input>
				</reps:formfield>
				
				<reps:formfield label="统计项目分类名称" fullRow="true">
					<reps:input name="itemName" maxLength="30" required="true">${statisticsItemCategory.itemName }</reps:input>
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
	window.location.href= "liststatisticsitemcategory.mvc?indicatorId=${indicatorId }";
}

</script>
</html>