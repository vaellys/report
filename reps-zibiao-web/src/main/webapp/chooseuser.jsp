<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<c:if test="${empty sGlobal.token and not empty sessionScope.users}">
<%@ include file="/WEB-INF/commons/common.jsp"%>
<script type="text/javascript" src="${jsPath}/base/jquery-1.11.2.min.js"></script>
<style type="text/css">
.login-tk{
	position: absolute;
	top:0;
	left: 0;
	width:100%;
	height: 100%;
	background:rgba(0, 0, 0, 0.5) none repeat scroll 0 0 !important;
	overflow: hidden;
	clear: both;
	z-index: 999;
}
.login-con{ 
	width:390px; 
	height:346px; 
	border-radius:10px; 
	background:#fff; 
	margin:0 auto;
	margin-top:120px;
}
.login-top{
	height: 45px;
	line-height: 45px;
	background: #3d95d5;
	position:relative;		
}
.login-top h2{
	font-size: 21px;
	color:#fff;
	text-align:center;
	font-weight:normal;
}
.login-info{
	width: 300px;
	margin:35px auto 0;
	font-size: 14px;
}
.login-user{
	margin-bottom: 18px;
}
.login-user input{	
	border: 1px solid #ccc;
	width: 270px;
	height: 35px;
	line-height: 35px;	
	padding-left: 28px;
	font-size: 14px;
}
.login-in{
	width: 300px;
	border-radius:3px;
	background: #3d95d5;
	margin-top:15px;
}
.login-in input{
	background:0; 
	border:0; 
	color:#fff;
	height: 40px;
	line-height: 40px;
	font-size:16px; 
	text-align:center; 
	font-weight: bold;
	cursor: pointer;
}
</style>
<div class="login-tk">
	<div class="login-con">
	    <div class="login-top">
	    	<h2>用户切换</h2>
	    </div>
	    <div class="login-info">
	    	<c:forEach items="${sessionScope.users}" var="user">
	    	<div class="login-user login-in">
	    		<c:choose>
	    			<c:when test="${sGlobal.token.userId eq user.userId}">
	    				<input title="当前登录" type="button" value="&gt; - ${user.organizeName} - <sys:dictionary src="user_identity">${user.userIdentity}</sys:dictionary>" />
	    			</c:when>
	    			<c:otherwise>
	    				<input onclick="cuser('${user.userId}')" type="button" value="${user.organizeName} - <sys:dictionary src="user_identity">${user.userIdentity}</sys:dictionary>" />
	    			</c:otherwise>
	    		</c:choose>
	        </div>
	    	</c:forEach>
	    </div>  
	</div>
</div>
<script type="text/javascript">
var cuser = function(userId) {
	$.ajax({
	  type: "POST",
	  url: basePath + "/index.jsp",
	  data : {"uid" : userId},
	  success: function(){
	     location.href = "login.do";
	  }
	}); 
};
</script>
</c:if>
<c:if test="${not empty sGlobal.token}">
		<c:redirect url="index.jsp" />
</c:if>