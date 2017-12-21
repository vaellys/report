<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>添加指标项</title>
	<reps:theme />
</head>
<body>
	<reps:container layout="true">
		<reps:panel id="mytop" dock="top" action="toitemlist.mvc" method="post" formId="xform">
			<reps:formcontent parentLayout="true" style="width:70%;">
				<reps:formfield label="中文名称">
					<reps:input name="chineseName">${info.chineseName}</reps:input>
				</reps:formfield>
				<reps:querybuttons style="margin-right:20px;">
		            <reps:ajaxgrid messageCode="manage.button.query" formId="xform" gridId="fieldgrid" cssClass="small-search-form-a"></reps:ajaxgrid>
		         </reps:querybuttons>
			</reps:formcontent>
		</reps:panel>
		<reps:panel id="mygrid" dock="center" border="true">
			<reps:grid id="fieldgrid" items="${list}" var="item" form="xform" flagShowPages="false" pagination="${pager}">
				<reps:gridrow>
					<reps:gridfield title="指标名称" width="25">${item.name}</reps:gridfield>
                 <reps:gridfield title="指标中文名称" width="25">${item.chineseName}</reps:gridfield>
					<reps:gridfield title="操作" width="15" align="center">
						<a class="submit-input" href="javascript:void(0)" onclick="javascript:chooseFieldBack('${item.id}');" title="点击确认">确定
						</a>
					</reps:gridfield>
				</reps:gridrow>
			</reps:grid>
		</reps:panel>
	</reps:container>
</body>
<script type="text/javascript">
var chooseFieldBack  = function(id){
	window.parent.chooseAddItem(id);
};
</script>
</html>