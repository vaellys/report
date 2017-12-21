<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>指标修改</title>
	<reps:theme />
	<script type="text/javascript">
		var fieldTypes = {
			'varchar'  : '字符型（单字节）',
			'nvarchar' : '字符型（双字节）',
			'byte'     : '超短整型（-128到127）',
			'short'    : '短整型（-32768到32767）',
			'int'      : '整型（-2147483648到2147483647）',
			'long'     : '长整型（-2的63次方到2的63次方减一）',
			'float'    : '小数点',
			'datetime' : '时间型'
		};
		
		var expressionTypes = {
			''     : '',
			'SQL'  : 'SQL',
			'常量' : '常量',
			'变量' : '变量',
			'运算' : '运算'
		};
		
		$(document).ready(function(){
			$.each(fieldTypes, function(value, text){
				var option = new Option(text, value);
				if ("${tjItem.fieldType}" == value){
					option.selected = true;
				}
				$('#fieldType').append($(option));
			});
			
			$.each(expressionTypes, function(value, text){
				var option = new Option(text, value);
				if ("${tjItem.expressionType}" == value){
					option.selected = true;
				}
				$('#expressionType').append($(option));
			});
		});
	</script>
</head>
<body>
<reps:container>
	<reps:panel id="first" dock="top" action="edit.mvc" formId="xform" validForm="true" style="width:800px">
		<reps:formcontent>
			<input type="hidden" value="${tjItem.id}" name="id">
			<%--<input type="hidden" value="${tjItem.showOrder}" name="showOrder">
			--%><reps:formfield label="指标名称" labelStyle="width:20%" textStyle="width:30%">
				<reps:input name="name" maxLength="50" required="true">${tjItem.name}</reps:input>
			</reps:formfield>
			<reps:formfield label="指标中文名称" labelStyle="width:18%" textStyle="width:32%">
				<reps:input name="chineseName" maxLength="30" required="true">${tjItem.chineseName}</reps:input>
			</reps:formfield>
			<reps:formfield label="字段类型">
				<reps:select name="fieldType" id="fieldType" onChange="fieldTypeChange()" required="true"></reps:select>
			</reps:formfield>
			<reps:formfield label="字段长度">
				<reps:input name="fieldLength" id="fieldLength" maxLength="5" dataType="number" required="true">${tjItem.fieldLength}</reps:input>
			</reps:formfield>
			<reps:formfield label="所属分类">
				<reps:select dataSource="${cateList}" name="tjItemCategory.id">${tjItem.categoryId}</reps:select>
			</reps:formfield>
			<reps:formfield label="表达式类型">
				<reps:select name="expressionType" id="expressionType" required="true"></reps:select>
			</reps:formfield>
			<reps:formfield label="是否字典项">
				<reps:select name="isDictionary" id="isDictionary" jsonData="{'':'', '1':'是', '0':'否'}">${tjItem.isDictionary}</reps:select>
			</reps:formfield>
			<reps:formfield label="引用字典">
				<reps:select name="referDictionary" id="referDictionary" dataSource="${dictionaryDefineMap}" headerText="" headerValue="">${tjItem.referDictionary}</reps:select>
			</reps:formfield>
			<reps:formfield label="显示顺序" fullRow="true">
				<reps:input name="showOrder" required="true" dataType="integernum">${tjItem.showOrder}</reps:input>
			</reps:formfield>
			<reps:formfield label="表达式" fullRow="true">
				<reps:input name="defaultExpression" multiLine="true" style="width:555px;height:250px">${tjItem.defaultExpression}</reps:input>
			</reps:formfield>
		</reps:formcontent>
		<reps:formbar>
			<reps:ajax messageCode="edit.button.save" formId="xform" callBack="skip" type="link" confirm="确定要提交修改？" cssClass="btn_save_a"></reps:ajax>
			<reps:button cssClass="btn_cancel_a" messageCode="edit.button.cancel"
				action="${ctx}/reps/report/itemcategory/list.mvc?parentId=${tjItem.categoryId}"></reps:button>
		</reps:formbar>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	$("#referDictionary").change(function(){
		if($("#isDictionary option").is(":selected")){
			if($("#isDictionary option:selected").val()==''){
				messager.info("请先选择是否字典项为是，才能选择该项！");
				$("#referDictionary option:selected").prop("selected", false);
				return false;
			}
		}
	});
	
	var referDictStatus = 0;//0表示引用状态是可以编辑的
	$("#isDictionary").change(function(){
		var isDictionary = $(this);
		var value = isDictionary.val();
		
		if(value == 0){//否
			if($("#referDictionary option").is(":selected")){
				$("#referDictionary option:selected").prop("selected", false);
			}
			$("#referDictionary").attr("disabled","disabled");
			$("#referDictionary").attr("style","width: 180px; background-color: rgb(233, 233, 233);");
			referDictStatus = 1;
		}
		if(value==1&&referDictStatus==1){
			$("#referDictionary").removeAttr("disabled");
			$("#referDictionary").attr("style","width: 180px;");
		}
	});
	
	$(function(){
		if($("#isDictionary option:selected").val()=='0'){
			$("#referDictionary option:selected").prop("selected", false);
			$("#referDictionary").attr("disabled","disabled");
			$("#referDictionary").attr("style","width: 180px; background-color: rgb(233, 233, 233);");
			referDictStatus = 1;
		}
	});

	var fieldTypeChange = function(){
		var type = $('#fieldType').val();
		if (type == "varchar" || type == "nvarchar"){
			$("#fieldLength").attr("value", '20');
		}
		else if (type == "byte"){
			$("#fieldLength").attr("value", '2');
		}
		else if (type == "short"){
			$("#fieldLength").attr("value", '4');
		}
		else if (type == "int"){
			$("#fieldLength").attr("value", '7');
		}
		else{
			$("#fieldLength").attr("value", '10');
		}
	};
	
	var skip = function(data) {
		messager.message(data, function() {
			window.location.href = "${ctx}/reps/report/itemcategory/list.mvc?parentId=${tjItem.categoryId}";
		});
	};
</script>
</body>
</html>