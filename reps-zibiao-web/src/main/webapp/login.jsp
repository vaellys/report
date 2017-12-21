<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<head>
	<title>资源云平台</title>
	<script type="text/javascript" src="library/base/jquery-1.8.0.min.js"></script>
	<script type="text/javascript">
	  if(top.location!=self.location){
	   	 window.open(self.location.href,'_parent');  
	  }
	</script>
</head>
<body>
<div style="width:500px;border:1px solid #cccccc; padding:10px;margin: 0 auto;">
<div style="text-align: center;height:30px;font-size:16px;font-weight: bold;">数据中心控制台登录</div>
<form action="login.mvc" method="post" name="LoginForm">
<input type="hidden" name="url" value="${param.url}"/>
<p><input id="username" name="name" type="text" placeholder="用户名" value="${username}"/></p>
<p><input name="password" type="password" type="text" placeholder="密码" value="${password}"/></p>
<p><input type="submit" value="登录"/> </p>
</form>
</div>
</body>
</html>
