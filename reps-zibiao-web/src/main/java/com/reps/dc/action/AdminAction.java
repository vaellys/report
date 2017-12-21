package com.reps.dc.action;

import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reps.core.modules.Module;
import com.reps.core.modules.SystemModulePool;

@Controller("com.reps.dc.action.AdminAction")
public class AdminAction {
	
	@RequestMapping(value = "/admin")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/theme/reps/index");
		//提取权限信息,提取所有的功能
		Collection<Module> list= SystemModulePool.getModules();
		//去掉多个的情况
		mav.addObject("modules", list);
		
		return mav;
	}
}
