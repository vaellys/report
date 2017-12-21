<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>指标修改</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="first" dock="top" action="edit.mvc" formId="xform" validForm="true" style="width:800px">
	<div class="block_container">
		<div class="block_title" id="divIndicator">
		<h3>指标基础信息</h3>
		<reps:formcontent>
			<reps:formfield label="指标名称" labelStyle="width:20%" textStyle="width:30%">
				<input type="hidden" name="id" value="${indicator.id }">
				<reps:input name="zbmc" maxLength="30" required="true">${indicator.zbmc }</reps:input>
			</reps:formfield>
			<reps:formfield label="所属主题" labelStyle="width:18%" textStyle="width:32%">
				<reps:select dataSource="${topicList}" id="topicId" name="tjZbztmcdyb.zbztId" required="true" onChange="topicChange()">${indicator.tjZbztmcdyb.zbztId}</reps:select>
			</reps:formfield>
			<reps:formfield label="上级指标" labelStyle="width:20%" textStyle="width:30%">
				<reps:select dataSource="${indicatorList}" name="fId" id="fId" >${indicator.fId}</reps:select>
			</reps:formfield>
			<reps:formfield label="显示顺序" fullRow="true" labelStyle="width:18%" textStyle="width:32%">
				<reps:input name="xsxh" required="true" dataType="integernum">${indicator.xsxh }</reps:input>
			</reps:formfield>
			<reps:formfield label="指标算法" fullRow="true">
				<reps:input name="zbsf" multiLine="true" style="width:555px;height:250px">${indicator.zbsf }</reps:input>
			</reps:formfield>
			<reps:formfield label="指标元数据" fullRow="true">
				<reps:input name="zbmeta" multiLine="true" style="width:555px;height:250px">${indicator.zbmeta }</reps:input>
			</reps:formfield>
			<reps:formfield label="指标说明" fullRow="true">
				<reps:input name="zbsm" multiLine="true" style="width:555px;height:70px">${indicator.zbsm }</reps:input>
			</reps:formfield>
			<reps:formfield label="指标算法MSSQL" fullRow="true">
				<reps:input name="zbsfMssql" multiLine="true" style="width:555px;height:250px">${indicator.zbsfMssql }</reps:input>
			</reps:formfield>
			<reps:formfield label="指标算法MYSQL" fullRow="true">
				<reps:input name="zbsfMysql" multiLine="true" style="width:555px;height:250px">${indicator.zbsfMysql }</reps:input>
			</reps:formfield>
			<reps:formfield label="指标算法ORACLE" fullRow="true">
				<reps:input name="zbsfOracle" multiLine="true" style="width:555px;height:250px">${indicator.zbsfOracle }</reps:input>
			</reps:formfield>
			<reps:formfield label="指标算法MONGODB" fullRow="true">
				<reps:input name="zbsfMongodbb" multiLine="true" style="width:555px;height:250px">${indicator.zbsfMongodb }</reps:input>
			</reps:formfield>
		</reps:formcontent>
		</div>
		<reps:formbar>
		<c:choose>
		  <c:when test="${not empty topicId}">  
		  	<reps:ajax messageCode="edit.button.save" formId="xform" callBack="skip2" type="link" confirm="确定要提交修改？" cssClass="btn_save_a"></reps:ajax>
			<reps:button cssClass="btn_cancel_a" messageCode="edit.button.cancel"
				action="${ctx}/reps/report/zbztmcdyb/list.mvc?zbztId=${topicId}"></reps:button>
		  </c:when>
		   <c:otherwise> 
		   		<reps:ajax messageCode="edit.button.save" formId="xform" callBack="skip" type="link" confirm="确定要提交修改？" cssClass="btn_save_a"></reps:ajax>
		   		<reps:button cssClass="btn_cancel_a" messageCode="edit.button.cancel"
				action="${ctx}/reps/report/zdyzbdyb/show.mvc?id=${indicator.fId}"></reps:button>
		   </c:otherwise>
		</c:choose>
		</reps:formbar>
		<div class="block_title" id="divMeta">
		<h3>指标元数据信息</h3>
		<reps:tabs style="height:300px;">
			<reps:tabitem value="数据库类型" url="listdatabasetype.mvc?indicatorId=${indicator.id }">
			</reps:tabitem>
			<reps:tabitem value="参数定义"></reps:tabitem>
			<reps:tabitem value="输出字段定义"></reps:tabitem>
			<reps:tabitem value="引用统计项目分类"></reps:tabitem>
			<reps:tabitem value="关联明细指标"></reps:tabitem>
		</reps:tabs>
		</div>
		<br>
		</div>
		
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var skip = function(data) {
		messager.message(data, function() {
			console.log(data);
			if(data.data.changed){
				parent.location.href = "${ctx}/reps/report/namespace/index.mvc";
			}else{
				window.location.href = "${ctx}/reps/report/zdyzbdyb/show.mvc?id=${indicator.fId}";
			}
		});
	};
	
	var skip2 = function(data) {
		messager.message(data, function() {
			if(data.data.changed){
				parent.location.href = "${ctx}/reps/report/namespace/index.mvc";
			}else{
				window.location.href = "${ctx}/reps/report/zbztmcdyb/list.mvc?zbztId=${topicId}";
			}
		});
	};
	
	function topicChange() {
		var topicId = $("#topicId").val();
		var select = $("#fId");
		$.ajax({
			type : "GET",
			url : "indicSelect.mvc",
			data : {
				"id" : topicId
			},
			success : function(data) {
				if(data.statusCode == "200"){
					select.empty();
					select.append(data.message);
				}
			}
		});
	}
</script>
</body>
</html>