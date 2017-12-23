<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>指标详细信息</title>
<reps:theme />
</head>
<body>
	<reps:container>
		<reps:panel id="myform" title="所在路径:${path}" dock="top" action=""
			formId="xform" validForm="false">
			<div class="block_container">
				<div class="block_title" id="divIndicator">
					<h3>指标基础信息</h3>
					<reps:formcontent>
						<reps:formfield label="指标ID" labelStyle="width:20%"
							textStyle="width:30%">
							<reps:input name="id" readonly="true">${indicator.id }</reps:input>
						</reps:formfield>
						<reps:formfield label="指标名称" labelStyle="width:18%"
							textStyle="width:32%">
							<reps:input name="zbmc" readonly="true">${indicator.zbmc }</reps:input>
						</reps:formfield>
						<reps:formfield label="所属主题" labelStyle="width:20%"
							textStyle="width:30%">
							<reps:input name="zbmc" readonly="true">${indicator.tjZbztmcdyb.zbztmc }</reps:input>
						</reps:formfield>
						<reps:formfield label="上级指标" labelStyle="width:18%"
							textStyle="width:32%">
							<reps:input name="fId" readonly="true">
								<c:if test="${not empty parentIndicator}">${parentIndicator.zbmc }</c:if>
							</reps:input>
						</reps:formfield>
						<reps:formfield label="显示顺序" fullRow="true" labelStyle="width:18%"
							textStyle="width:32%">
							<reps:input name="xsxh" dataType="integernum" readonly="true">${indicator.xsxh }</reps:input>
						</reps:formfield>
						<reps:formfield label="指标算法" fullRow="true">
							<reps:input name="zbsf" multiLine="true"
								style="width:600px;height:100px" readonly="true">${indicator.zbsf }</reps:input>
						</reps:formfield>
						<reps:formfield label="指标元数据" fullRow="true">
							<reps:input name="zbmeta" multiLine="true"
								style="width:600px;height:100px" readonly="true">${indicator.zbmeta }</reps:input>
						</reps:formfield>
						<reps:formfield label="指标说明" fullRow="true">
							<reps:input name="zbsm" multiLine="true"
								style="width:600px;height:100px" readonly="true">${indicator.zbsm }</reps:input>
						</reps:formfield>
						<reps:formfield label="指标算法MSSQL" fullRow="true">
							<reps:input name="zbsfMssql" multiLine="true"
								style="width:600px;height:100px" readonly="true">${indicator.zbsfMssql }</reps:input>
						</reps:formfield>
						<reps:formfield label="指标算法MYSQL" fullRow="true">
							<reps:input name="zbsfMysql" multiLine="true"
								style="width:600px;height:100px" readonly="true">${indicator.zbsfMysql }</reps:input>
						</reps:formfield>
						<reps:formfield label="指标算法ORACLE" fullRow="true">
							<reps:input name="zbsfOracle" multiLine="true"
								style="width:600px;height:100px" readonly="true">${indicator.zbsfOracle }</reps:input>
						</reps:formfield>
						<reps:formfield label="指标算法MONGODB" fullRow="true">
							<reps:input name="zbsfMongodbb" multiLine="true"
								style="width:600px;height:100px" readonly="true">${indicator.zbsfMongodb }</reps:input>
						</reps:formfield>
					</reps:formcontent>
				</div>
				<reps:formbar>
					<c:if test="${indicator.fId == '-1' || empty indicator.fId}">
						<reps:button cssClass="btn_back_a"
							messageCode="manage.action.return"
							action="${ctx}/reps/report/zbztmcdyb/list.mvc?zbztId=${indicator.tjZbztmcdyb.zbztId}"></reps:button>
					</c:if>
					<c:if test="${indicator.fId != '-1' && not empty indicator.fId}">
						<reps:button cssClass="btn_back_a"
							messageCode="manage.action.return"
							action="${ctx}/reps/report/zdyzbdyb/show.mvc?id=${indicator.fId}"></reps:button>
					</c:if>
				</reps:formbar>
				<div class="block_title" id="divMeta">
					<h3>指标元数据信息</h3>
					<reps:tabs style="height:300px;">
						<reps:tabitem value="数据库类型" url="listdatabasetype.mvc?indicatorId=${indicator.id }&flag=1"></reps:tabitem>
						<reps:tabitem value="参数定义" url="listparamdefined.mvc?indicatorId=${indicator.id }&flag=1"></reps:tabitem>
						<reps:tabitem value="输出字段定义" url="listoutputfieldefined.mvc?indicatorId=${indicator.id }&flag=1"></reps:tabitem>
						<reps:tabitem value="引用统计项目分类" url="liststatisticsitemcategory.mvc?indicatorId=${indicator.id }&flag=1"></reps:tabitem>
						<reps:tabitem value="关联明细指标"></reps:tabitem>
					</reps:tabs>
				</div>
				<br>
			</div>
		</reps:panel>
	</reps:container>
</body>
</html>