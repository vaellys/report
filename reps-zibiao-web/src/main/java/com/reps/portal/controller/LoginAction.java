package com.reps.portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.reps.core.LoginManager;
import com.reps.core.util.StringUtil;
import com.reps.core.web.BaseAction;

@Controller("com.reps.portal.controller.LoginAction")
public class LoginAction extends BaseAction{
	
	@RequestMapping(value="/login")
    public String login(HttpServletRequest request, HttpServletResponse response){
		//UserToken token = getCurrentToken();
		//String userId = LoginManager.getUserId();
		Boolean youke = (Boolean) request.getSession().getAttribute("noUser");
		if(null!=youke && youke){
			//游客身份。退出第三方登陆系统,并跳转到数据中心首页。
			String path = request.getContextPath().equals("/") ? "" : request.getContextPath();
			String port = request.getServerPort() == 80 ? "" : ":" + request.getServerPort();
			String basePath = request.getScheme() + "://" + request.getServerName() + port + path;
			return "redirect:logout?refer=" + basePath;
		}else{
			Object userId = LoginManager.getValue("userid");
			if(null!=userId && StringUtil.isNotBlank(userId.toString())){
				return "redirect:admin.mvc";
			}else {
				return "登录出现异常，请重新登录！";
			}
		}
    }
	
}
