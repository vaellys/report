<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>统计表添加</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="first" dock="top" action="add.mvc" formId="xform" validForm="true" method="post" style="width:750px">
		<reps:formcontent>
			<reps:formfield label="统计报表名称" labelStyle="width:20%" textStyle="width:30%">
				<reps:input name="chineseName" maxLength="30" required="true"></reps:input>
			</reps:formfield>
			<reps:formfield label="映射数据表名" labelStyle="width:18%" textStyle="width:32%">
				<reps:input name="name" maxLength="30" required="true"></reps:input>
			</reps:formfield>
			<reps:formfield label="报表类型">
				<reps:select name="category" jsonData="{'':'', '清单':'清单报表', '固定':'固定/套打报表'}" required="true"></reps:select>
			</reps:formfield>
			<reps:formfield label="是否基础表">
				<reps:select name="isBasic" jsonData="{'':'', '1':'是', '0':'否'}" required="true"></reps:select>
			</reps:formfield>
			<reps:formfield label="统计方法">
				<reps:select name="method" jsonData="{'V':'简单', 'H':'复杂'}" required="true"></reps:select>
			</reps:formfield>
			<reps:formfield label="统计优先级">
				<reps:input name="priority" maxLength="11"></reps:input>
			</reps:formfield>
		</reps:formcontent>
		<br/><br/><br/>
		<reps:formbar>
			<reps:ajax  messageCode="add.button.save" formId="xform" callBack="skip" type="button" cssClass="btn_save"></reps:ajax>
			<%--<reps:ajax value="保存并添加指标项" formId="xform"  type="button" callBack="my" cssClass="btn_common"></reps:ajax>
			--%><reps:button cssClass="btn_cancel_a" messageCode="add.button.cancel" action="list.mvc?groupId=${groupId}" ></reps:button>
		</reps:formbar>
	</reps:panel>
</reps:container>
</body>
<script type="text/javascript">
	var skip = function(data) {
		messager.message(data, function() {
			parent.location.href = "index.mvc?groupId=${groupId}";
		});
	};
	var my = function(data) {
		messager.message(data, function() {
			window.location.href = "additem2.mvc?id=" + data.data;
		});
	};
</script>
</html>