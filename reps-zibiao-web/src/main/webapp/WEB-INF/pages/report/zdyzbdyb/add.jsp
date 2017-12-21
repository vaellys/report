<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>指标添加</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="first" dock="top" action="add.mvc" formId="form" validForm="true" method="post" style="width:800px">
	<div class="block_container">
		<div class="block_title" id="divIndicator">
			<h3>指标基础信息</h3>
			<reps:formcontent>
				<reps:formfield label="指标名称" labelStyle="width:20%" textStyle="width:30%">
					<reps:input name="zbmc" maxLength="30" required="true"></reps:input>
				</reps:formfield>
				<reps:formfield label="所属主题" labelStyle="width:18%" textStyle="width:32%">
					<reps:input name="topicName" readonly="true" required="true">${topic.zbztmc}</reps:input>
					<input type="hidden" name="tjZbztmcdyb.zbztId" value="${topic.zbztId}">
					<input type="hidden" name="tjZbztmcdyb.tjNamespace.namespace" value="${topic.tjNamespace.namespace }">
				</reps:formfield>
				<c:if test="${not empty indicator }">
					<reps:formfield label="上级指标" labelStyle="width:20%" textStyle="width:30%">
						<reps:input name="parentId" readonly="true" >${indicator.zbmc}</reps:input>
						<input type="hidden" name="fId" value="${indicator.id }">
					</reps:formfield>
				</c:if>
				<reps:formfield label="显示顺序" fullRow="true" labelStyle="width:18%" textStyle="width:32%">
					<reps:input name="xsxh" required="true" dataType="integernum"></reps:input>
				</reps:formfield>
				<reps:formfield label="指标算法" fullRow="true">
					<reps:input name="zbsf" multiLine="true" style="width:555px;height:250px" ></reps:input>
				</reps:formfield>
				<reps:formfield label="指标元数据" fullRow="true">
					<reps:input name="zbmeta" multiLine="true" style="width:555px;height:250px"></reps:input>
				</reps:formfield>
				<reps:formfield label="指标说明" fullRow="true">
					<reps:input name="zbsm" multiLine="true" style="width:555px;height:70px"></reps:input>
				</reps:formfield>
				<reps:formfield label="指标算法MSSQL" fullRow="true">
					<reps:input name="zbsfMssql" multiLine="true" style="width:555px;height:250px"></reps:input>
				</reps:formfield>
				<reps:formfield label="指标算法MYSQL" fullRow="true">
					<reps:input name="zbsfMysql" multiLine="true" style="width:555px;height:250px"></reps:input>
				</reps:formfield>
				<reps:formfield label="指标算法ORACLE" fullRow="true">
					<reps:input name="zbsfOracle" multiLine="true" style="width:555px;height:250px"></reps:input>
				</reps:formfield>
				<reps:formfield label="指标算法MONGODB" fullRow="true">
					<reps:input name="zbsfMongodb" multiLine="true" style="width:555px;height:250px"></reps:input>
				</reps:formfield>
			</reps:formcontent>
		</div>
		<reps:formbar>
			<reps:ajax  messageCode="add.button.save" formId="form" callBack="skip" type="button" cssClass="btn_save"></reps:ajax>
			<c:if test="${indicator.fId == '-1' || empty indicator.fId}">
				<reps:button cssClass="btn_cancel_a" messageCode="add.button.cancel"
					action="${ctx}/reps/report/zbztmcdyb/list.mvc?zbztId=${topic.zbztId}" ></reps:button>
			</c:if>
			<c:if test="${indicator.fId != '-1' && not empty indicator.fId}">
				<reps:button cssClass="btn_back_a" messageCode="manage.action.return"
					action="${ctx}/reps/report/zdyzbdyb/show.mvc?id=${indicator.fId}"></reps:button>
			</c:if>
		</reps:formbar>
		<div class="block_title" id="divMeta">
		<h3>指标元数据信息</h3>
		<reps:tabs style="height:300px;">
			<reps:tabitem value="数据库类型" url="listdatabasetype.mvc"></reps:tabitem>
			<reps:tabitem value="参数定义" url="listparamdefined.mvc"></reps:tabitem>
			<reps:tabitem value="输出字段定义" url="listoutputfieldefined.mvc"></reps:tabitem>
			<reps:tabitem value="引用统计项目分类"></reps:tabitem>
			<reps:tabitem value="关联明细指标"></reps:tabitem>
		</reps:tabs>
		</div>
		</div>
		<br/>
		
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