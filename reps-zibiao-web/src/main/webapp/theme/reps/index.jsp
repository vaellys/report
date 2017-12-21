<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/system-tags" prefix="sys"%><%@ taglib uri="/reps-tags" prefix="reps"%>
<%@ page trimDirectiveWhitespaces="true" %>
<% 
	String path = request.getContextPath().equals("/") ? "" : request.getContextPath();
	String path2 = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path2+"/"; 
%>
<c:set var="path" value="<%=path%>" />
<c:set var="basePath" value="<%=basePath%>" />
<html>
<head>
<meta charset="utf-8" />
<title>数据中心管理</title>
<link rel="shortcut icon" href="/favicon.ico">
<reps:theme/>
<link rel="stylesheet" href="${ctx}/theme/reps/index.css" type="text/css"/>
<script type="text/javascript" src="${ctx}/library/jlayout/jquery.sizes.js"></script>
<script type="text/javascript" src="${ctx}/library/jlayout/jlayout.border.js"></script>
<script type="text/javascript" src="${ctx}/library/jlayout/jquery.jlayout.js"></script>

<script type="text/javascript">

$(function(){
	var container = $('.layout');
	function relayout() {
		container.layout({resize: false});
	}
	relayout();
	$(window).resize(relayout);
	
	$(".reps-fit").each(function(){var $this = $(this);$this.height($this.parent().height()); $this.width($this.parent().width());});
	
	$("#myaccordion").accordion({fillSpace:true,alwaysOpen:true,active:0});
	
});
</script>
</head>
<body style="height: 100%;overflow:auto;">
<div data-layout='{"type": "border", "hgap": 4, "vgap":0}' class="layout">
  <div class="north">
  <div id="header">
     <h1 style="float:left;width:200px;padding-top:10px;padding-left:10px;font-size:20px">数据中心管理系统</h1>
     <ul>
        <li class="exit"><a href="${ctx}/logout?refer=${basePath}" title="退出登录">退出登录</a></li>
	 </ul>
  </div>
  </div>
  <div class="west">
      <reps:accordion id="myaccordion">
         <c:forEach items="${modules}" var="m">
            <c:forEach items="${m.menu.nodes}" var="n">
	          <reps:accordionitem title="${n.name}">
	             <ul class="leftMenu">
	                  <c:forEach items="${n.nodes}" var="menu">
	                  	<c:set value="${fn:indexOf(menu.action, ';') == -1 ? menu.action : fn:substring(menu.action, 0, fn:indexOf(menu.action, ';'))}" var="action" />
	                    <li><a target="mainFrame" href="${ctx}${action}">${menu.name}</a></li>
	                 </c:forEach>
	             </ul>
	          </reps:accordionitem>
	          </c:forEach>
          </c:forEach>
      </reps:accordion>
  </div>
  <div class="center">
      <iframe id="mainFrame" name="mainFrame" src=""  class="reps-fit"  frameborder="no" border="0" marginwidth="0" marginheight="0" ></iframe>
  </div>
</div>

</body>
</html>