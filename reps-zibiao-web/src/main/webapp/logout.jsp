<%@page import="com.reps.client.ConfigConstants"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String port = request.getServerPort() == 80 ? "": ":" + request.getServerPort(); 
	String path = request.getContextPath().equals("/") ? "" : request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + port + path + "/";
	session.invalidate();
	String serverUrlPrefix = ConfigConstants.getString(ConfigConstants.SERVER_URL_PREFIX_KEY);
	response.sendRedirect(serverUrlPrefix + "/logout?service=" + basePath);
%>
