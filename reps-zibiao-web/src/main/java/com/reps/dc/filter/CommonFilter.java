package com.reps.dc.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reps.core.LoginManager;
import com.reps.core.RepsConstant;

public class CommonFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		request.setCharacterEncoding(RepsConstant.GLOBAL_CHARSET);
        response.setCharacterEncoding(RepsConstant.GLOBAL_CHARSET);
		
		Map<String, Object> sGlobal = new HashMap<String, Object>();
		String userId = LoginManager.getUserId();
		if(userId != null) {
		    sGlobal.put("uid", userId);
		    //sGlobal.put("token", RepsContext.getByUserId(userId));
		}
		request.setAttribute("sGlobal", sGlobal);
		
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		
	}

}
