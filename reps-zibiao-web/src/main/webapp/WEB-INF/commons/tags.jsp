<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/system-tags" prefix="sys"%>
<%@ taglib uri="/reps-tags" prefix="reps"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@page import="com.reps.core.RepsConstant"%>
<% 
String path = request.getContextPath().equals("/") ? "" : request.getContextPath();
String port = request.getServerPort() == 80 ? "" : ":" + request.getServerPort();
String basePath = request.getScheme() + "://" + request.getServerName() + port + path;
String title = RepsConstant.getContextProperty("global.title");
%>
<c:set var="ctx" value="<%=basePath%>" />
<c:set var="title" value="<%=title%>" />