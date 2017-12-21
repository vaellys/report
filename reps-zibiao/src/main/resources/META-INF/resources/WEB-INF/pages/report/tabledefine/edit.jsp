<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>统计表修改</title>
	<reps:theme />
</head>
<body>
<reps:container layout="true">
	<reps:panel id="first" dock="top" formId="xform" validForm="true" title="修改《${tableDefine.chineseName}》" 
		style="height:200px;margin-top:0px;" action="edit.mvc" border="true">
		<reps:formcontent>
			<input type="hidden" value="${tableDefine.id}" name="id">
			<reps:formfield label="统计报表名称" labelStyle="width:18%" textStyle="width:24%">
				<reps:input name="chineseName" maxLength="30" required="true">${tableDefine.chineseName}</reps:input>
			</reps:formfield>
			<reps:formfield label="映射数据表名" labelStyle="width:15%" textStyle="width:33%">
				<reps:input name="name" maxLength="30" required="true">${tableDefine.name}</reps:input>
			</reps:formfield>
			<reps:formfield label="报表类型">
				<reps:select name="category" jsonData="{'':'', '清单':'清单报表', '固定':'固定/套打报表'}" required="true">${tableDefine.category}</reps:select>
			</reps:formfield>
			<reps:formfield label="是否基础表">
				<reps:select name="isBasic" jsonData="{'':'', '1':'是', '0':'否'}" required="true">${tableDefine.isBasic}</reps:select>
			</reps:formfield>
			<reps:formfield label="统计方法">
				<reps:select name="method" jsonData="{'V':'简单', 'H':'复杂'}" required="true"></reps:select>
			</reps:formfield>
			<reps:formfield label="统计优先级">
				<reps:input name="priority" maxLength="11" dataType="number">${tableDefine.priority}</reps:input>
			</reps:formfield>
			<reps:formfield label="是否启用">
				<reps:select name="enabled" jsonData="{'1':'启用', '0':'不启用'}" required="true">${tableDefine.enabled}</reps:select>
			</reps:formfield>
		</reps:formcontent>
		<br/>
		<reps:formbar>
			<%--
			<reps:ajax messageCode="edit.button.save" formId="xform" callBack="skip" type="link" confirm="确定要提交修改？" cssClass="btn_save_a"></reps:ajax>
			--%>
			<reps:ajax messageCode="edit.button.save" formId="xform" callBack="skip" type="button" confirm="确定要提交修改？" cssClass="btn_save"></reps:ajax>
			<reps:button action="list.mvc?id=${tableDefine.id}" messageCode="edit.button.cancel" cssClass="btn_cancel_a"></reps:button>
		</reps:formbar>
	</reps:panel>
	<reps:panel id="grid" dock="center">
		<reps:grid id="itemgrid" items="${tableItemList}" var="items" form="Kform" flagSeq="true" >
			<reps:gridrow>
				<reps:gridfield title="指标项名" width="15">${items.itemName}</reps:gridfield>
				<reps:gridfield title="指标中文名称" width="15">${items.itemChineseName}</reps:gridfield>
				<reps:gridfield title="字段类型" width="8" align="center">${items.fieldType}</reps:gridfield>
				<reps:gridfield title="字段长度" width="7" align="center">${items.fieldLength}</reps:gridfield>
				<reps:gridfield title="表达式" width="25">${items.expression}</reps:gridfield>
				<reps:gridfield title="依赖" width="15">${items.dependency}</reps:gridfield>
				<reps:gridfield title="操作" width="15">
					<reps:dialog id="additem" title="修改指标项" rel="additem" url="toedititem.mvc?id=${items.id}&tid=${tableDefine.id}"
						iframe="true" value="修改" mask="true" height="400" width="750" cssClass="dialog-a">
					</reps:dialog>
					<reps:ajax value="删除" url="deleteitem.mvc?id=${items.id}" confirm="你确定要删除此字段吗？" redirect="toedit.mvc?id=${tableDefine.id}" cssClass="delete-table"></reps:ajax>
				</reps:gridfield>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>	
<script type="text/javascript">
	var skip = function(data) {
		messager.message(data, function() {
			parent.location.href = "index.mvc";
		});
	};
	
	function closeDialog(){
		$.pdialog.closeDialog();
	}
	
	var my=function(data)
	{
		messager.message(data, function(){ window.location.reload(); });
	}
</script>
</body>
</html>