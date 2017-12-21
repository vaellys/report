<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>指标项修改</title>
	<reps:theme />
</head>
<body>
<reps:container layout="true">
	<reps:panel id="first" dock="top" formId="xform" validForm="true" action="edititem.mvc">
		<reps:formcontent>
			<input type="hidden" value="${tableItem.id}" name="id">
			<reps:formfield label="指标名称" labelStyle="width:18%" textStyle="width:30%">
				<reps:input name="itemName" maxLength="50" required="true" readonly="true">${tableItem.itemName}</reps:input>
			</reps:formfield>
			<reps:formfield label="指标中文名称" labelStyle="width:22%" textStyle="width:30%" >
				<reps:input name="itemChineseName" maxLength="30" required="true">${tableItem.itemChineseName}</reps:input>
			</reps:formfield>
			<reps:formfield label="字段类型">
				<reps:select name="fieldType" jsonData="{'':'', 'varchar':'字符型（单字节）', 'nvarchar':'字符型（双字节）',
				'byte':'超短整型', 'short':'短整型', 'int':'整型', 'long':'长整型', 'numeric':'数字型', 'datetime':'时间型'}">${tableItem.fieldType}</reps:select>
			</reps:formfield>
			<reps:formfield label="字段长度">
				<reps:input name="fieldLength" maxLength="5">${tableItem.fieldLength}</reps:input>
			</reps:formfield>
			<reps:formfield label="表达式类型">
				<reps:select name="expressionType" jsonData="{'':'', 'SQL':'SQL', '变量':'变量', '运算':'运算', '输入':'输入'}" required="true">
					${tableItem.expressionType}
				</reps:select>
			</reps:formfield>
			<reps:formfield label="是否唯一批次约束项">
				<reps:select name="batchKey" jsonData="{'':'','1':'是','0':'否'}">${tableItem.batchKey}</reps:select>
			</reps:formfield>
			<reps:formfield label="是否临时项" fullRow="true">
				<reps:select name="isTemporary" jsonData="{'':'','1':'是','0':'否'}">${tableItem.isTemporary}</reps:select>
			</reps:formfield>
			<reps:formfield label="表达式" fullRow="true">
				<reps:input name="expression" maxLength="500" multiLine="true" style="width:555px;height:80px" >${tableItem.expression}</reps:input>
			</reps:formfield>
			<reps:formfield label="依赖于指标" fullRow="true">
				<reps:input name="dependency" maxLength="200" multiLine="true" style="width:555px;height:40px">${tableItem.dependency}</reps:input>
			</reps:formfield>
			<reps:formfield label="统计顺序" fullRow="true">
				<reps:input name="statOrder" dataType="number">${tableItem.statOrder}</reps:input>
			</reps:formfield>
		</reps:formcontent>
		<reps:formbar>
			<reps:ajax messageCode="edit.button.save" formId="xform" callBack="skip" type="link" confirm="确定要提交修改？" cssClass="btn_save_a"></reps:ajax>
			<reps:button cssClass="btn_cancel" type="button" onClick="back()" messageCode="edit.button.cancel" />
		</reps:formbar>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var skip = function(data) {
		messager.message(data, function() {
			parent.location.href = "toedit.mvc?id=${tid}";
		});
	};
	function back(){
		window.parent.closeDialog();
	}
	
</script>
</body>
</html>