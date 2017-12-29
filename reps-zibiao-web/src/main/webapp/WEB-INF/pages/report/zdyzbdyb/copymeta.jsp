<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
  <head>
     <title>复制元数据信息</title>
	 <reps:theme/>
  </head>
  <body>
    <reps:container layout="true">
  	<reps:panel id="myform" dock="none" border="true" action="copymeta.mvc" formId="xform" validForm="true" method="post" title="指标元数据选择">
		<reps:formcontent style="">
			<input type="hidden" name="indicatorId" value="${indicatorId }">
			<c:forEach items="${metaMaps}" var="entry">
	          	<reps:formfield label="${entry.value }"   >
					<reps:checkbox name="metas" >${entry.key }</reps:checkbox>
				</reps:formfield>
			</c:forEach>
	   </reps:formcontent>
	    <reps:formbar style="margin-top:15px;">
  		   <reps:ajax confirm="你确定要复制吗？" formId="xform"  type="link" cssClass="btn_save_a" callBack="my"  messageCode="edit.button.save"/>
      	 </reps:formbar>
  	</reps:panel>
  </reps:container>
  <script type="text/javascript">
  
	var my = function(data){
		messager.message(data, function() {
			window.parent.location.href = "list.mvc";
		});
	}
  </script>
  </body>
</html>