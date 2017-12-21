package com.reps.dc.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.reps.core.LoginManager;
import com.reps.core.util.StringUtil;

public class ChooseUserFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if (request.getSession().getAttribute("users") != null) {
			if (LoginManager.getUserId() == null && request.getParameter("uid") != null) {
				LoginManager.setUserId(request.getParameter("uid"));
				LoginManager.setVariable("userid", request.getParameter("uid"));
				List<Map<String, String>> users = Collections.emptyList();
				users = (List<Map<String, String>>) request.getSession().getAttribute("users");
				for(Map<String, String> m : users){
					String userId = m.get("userId");
					if(userId.equals(request.getParameter("uid"))){
						LoginManager.setVariable("organizeName", m.get("organizeName")+"-");
					}
				}
			} else if (LoginManager.getUserId() == null && request.getParameter("uid") == null) {
				String path = request.getContextPath().equals("/") ? "" : request.getContextPath();
				StringBuffer buf = new StringBuffer(request.getRequestURL());
				if (StringUtils.isNotBlank(request.getQueryString())) {
					buf.append("?").append(request.getQueryString());
				}
				response.sendRedirect(path + "/chooseuser.jsp?refer=" + StringUtil.urlEncode(buf.toString()));
				return;
			}
		}else{
			Boolean youke = (Boolean)request.getSession().getAttribute("noUser");
			if(null!=youke&&youke){
				//游客身份。退出第三方登陆系统,并跳转到数据中心首页。
				String path = request.getContextPath().equals("/") ? "" : request.getContextPath();
				String port = request.getServerPort() == 80 ? "" : ":" + request.getServerPort();
				String basePath = request.getScheme() + "://" + request.getServerName() + port + path;
				response.sendRedirect(path + "/logout?refer=" + basePath);
				return;
			}
		}
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		
	}

}
