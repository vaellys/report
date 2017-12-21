<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>数据中心统计</title>
	<reps:theme />
	<style type="text/css">
		.tj{height:50px; font-size:13pt;margin: 20px 0px 0px 40px}
		.code{display:none}
		.div{float:right;}
	</style>
</head>
<body>
<reps:container layout="true">
	<reps:panel id="mytop" dock="top">
		<div class="block_container">
			<div class="block_title">
				<h3>基础表</h3>
				<table>
				<c:forEach items="${fixedList}" var="table" varStatus="status">
					<c:if test="${(status.index)%3 == 0}">
					<tr>
					</c:if>
					<td>
						<input type="button" id="start" value="  ${table.chineseName}  " class="tj" />
						<div class="code">${table.id}</div>
					</td>
					<c:if test="${fn:length(list)==(status.index+1)}">
					</tr>
					</c:if>
				</c:forEach>
				</table>
			</div>
			<br/>
			<div class="block_title">
				<h3>业务表</h3>
				<table>
				<c:forEach items="${unfixedList}" var="table" varStatus="status">
					<c:if test="${(status.index)%3 == 0}">
					<tr>
					</c:if>
					<td>
						<input type="button" id="start" value="  ${table.chineseName}  " class="tj" />
						<div class="code">${table.id}</div>
					</td>
					<c:if test="${fn:length(list)==(status.index+1)}">
					</tr>
					</c:if>
				</div>
				</c:forEach>
				</table>
			</div>
		</div>
	</reps:panel>
</reps:container>
</body>
<script> 
	$(function() {
		$("input.tj").each(function(){
			$(this).click(function(){

				$(this).attr({
					"disabled" : "disabled"
				});
				
				var parentDiv=$(this).parent();
				var postStr=parentDiv.find("div.code").text();
				
				$.ajax({
					type : "post",
					url : "tj.mvc",
					dataType : "json",
					data: {"tableId" : postStr},
					success : function(data) {
						messager.message(data);
						$(this).removeAttr("disabled");
					},
					error : function(data) {
						messager.message("统计失败！");
					}
				});

//				urlStr=".."+urlStr;
//				var postStr=parentDiv.find("div.code").text();	
//				$.ajax({
//						type: "GET",
//						url: "security/encrypt",
//						dataType:"text",	
//						data:{"nouserkey":nouserkey,"json":postStr},
//						success: function(str, textStatus){
//							if(encryptDiv.length>0){
//							   encryptDiv.text(str);
//							}
//							var jsonData={"senddata":str,"client":clientVal};
//							postData(resultDiv,urlStr,jsonData,"",nouserkey);
//						},
//						complete: function(XMLHttpRequest, textStatus){
//							//HideLoading();
//						},
//						error: function(err){
//							resultDiv.html(err);
//						}
//				 });
//				
			});
		});
		
//		$("#start").on("click", function() {
//			$("#start").attr({
//				"disabled" : "disabled"
//			});
//			
//			$.ajax({
//				type : "post",
//				url : "tj.mvc",
//				dataType : "json",
//				success : function(data) {
//					messager.message(data);
//					$("#start").removeAttr("disabled");
//				},
//				error : function(data) {
//					messager.message("统计失败！");
//				}
//			});
//		});

	});
</script>

</html>
