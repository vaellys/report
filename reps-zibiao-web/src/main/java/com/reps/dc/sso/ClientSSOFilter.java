package com.reps.dc.sso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.reps.core.LoginManager;
import com.reps.sso.client.filter.AbstractClientSSOFilter;

public class ClientSSOFilter extends AbstractClientSSOFilter {
	
	public ClientSSOFilter() {
		super();
		//未登录跳转实现
		super.setAuthenticationRedirectStrategy(new ClientAuthenticationRedirectStrategy());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loginUserInfo(HttpServletRequest request, HttpServletResponse response, Map<String, Object> attributes) {
		if(attributes != null && !attributes.isEmpty()) {
			List<Map<String, String>> users = Collections.emptyList();
			if(attributes.get("users") != null) {
				String strUsers = (String) attributes.get("users");
				if(StringUtils.isNotBlank(strUsers)) {
					JSONArray jsonArray = JSONArray.fromObject(strUsers);
					if(jsonArray != null && jsonArray.size() > 0) {
						users = new ArrayList<Map<String,String>>(jsonArray.size());
						for(int i = 0; i < jsonArray.size(); i++){
							Map<String, String> map = new LinkedHashMap<String, String>(4);
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							for (Iterator<String> iter = jsonObject.keys(); iter.hasNext();) { //先遍历整个 people 对象  
							    String key = (String) iter.next();  
							    map.put(key, jsonObject.getString(key));
							}
							users.add(map);
						}
					}
				}
			}
			if(users != null && users.size() > 1) {
				//多身份
				request.getSession().setAttribute("users", users);
				request.getSession().setAttribute("hasMoreUser", true);
				//Map<String, String> user = users.get(0);
				//LoginManager.setUserId(user.get("userId"));
				//LoginManager.setVariable("userid",user.get("userId").toString());
				LoginManager.setVariable("username", attributes.get("username").toString());
				LoginManager.setVariable("name", attributes.get("name").toString());
			} else if(users != null && users.size() == 1) {
				Map<String, String> user = users.get(0);
				//LoginManager.setUserId(user.get("userId"));
				LoginManager.setVariable("userid",user.get("userId").toString());
				LoginManager.setVariable("organizeName", user.get("organizeName")+"-");
				LoginManager.setVariable("username", attributes.get("username").toString());
				LoginManager.setVariable("name", attributes.get("name").toString());
				
			} else {
				//游客
				request.getSession().setAttribute("noUser", true);
			}
		}
	}
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		//退出
		LoginManager.logout();
		request.getSession().invalidate();
	}
}

