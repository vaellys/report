package com.reps.dc.sso;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reps.sso.client.authentication.AuthenticationRedirectStrategy;

public class ClientAuthenticationRedirectStrategy implements AuthenticationRedirectStrategy {
	
	@Override
	public void redirect(HttpServletRequest request, HttpServletResponse response, String potentialRedirectUrl)
			throws IOException {
		if(isAjax(request)) {
			String uri = request.getServletPath();
			 uri = request.getServletPath();
			RequestDispatcher dispatcher = request.getRequestDispatcher(uri);
			try {
				dispatcher.forward(request, response);//forward不会拦截。
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}else{
			//跳转到第三方登陆页面。
			response.sendRedirect(potentialRedirectUrl);
		}
		
	}
	
	/**
	 * 判断ajax请求
	 * @param request
	 * @return
	 */
	boolean isAjax(HttpServletRequest request){
		boolean b = (request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
	    return  b;
	}

}
