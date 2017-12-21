package com.reps.tj.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.reps.core.RepsConstant;
import com.reps.core.exception.RepsException;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;

import com.reps.tj.entity.TjTableDefine;
import com.reps.tj.service.ITjService;
import com.reps.tj.service.ITjTableDefineService;

/**
 * 统计触发模块
 * @author Karlova
 */
@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/report/statistics")
public class TjAction extends BaseAction {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	ITjTableDefineService tableService;
	@Autowired
	ITjService tjService;
	
	
	/**
	 * 统计首页
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView mav = getModelAndView("report/statistics/index");
		
		TjTableDefine info = new TjTableDefine();
		info.setEnabled((short)1);
		info.setIsBasic((short)1);
		List<TjTableDefine> fixedList = tableService.query(info);
		mav.addObject("fixedList", fixedList);
		
		info.setIsBasic((short)0);
		List<TjTableDefine> unfixedList = tableService.query(info);
		mav.addObject("unfixedList", unfixedList);
		return mav;
	}

	/**
	 * 报表统计
	 * @param tableId
	 * @return String
	 */
	@RequestMapping(value = "/tj")
	@ResponseBody
	public Object tj(String tableId) {
		try{
			tjService.saveStat(tableId);
			return ajax(AjaxStatus.OK, "统计完成！");
		} catch (RepsException e) {
			return ajax(AjaxStatus.FAIL, "统计失败！" + e.getMessage());
		}
		
	}

}
