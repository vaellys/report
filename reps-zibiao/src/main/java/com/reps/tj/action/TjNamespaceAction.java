package com.reps.tj.action;

import static com.reps.tj.util.MetaManager.removeMetaDatasFromSession;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reps.core.RepsConstant;
import com.reps.core.web.BaseAction;
import com.reps.tj.service.ITjNamespaceService;
import com.reps.tj.util.ZtreeAttribute;


/**
 * 指标命名空间操作
 * @author qianguobing
 * @date 2017年8月8日 下午3:10:42
 */
@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/report/namespace")
public class TjNamespaceAction extends BaseAction {
	
	@Autowired
	private ITjNamespaceService namespaceService;
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 主页面，初始化左侧树形菜单
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index(){
		ModelAndView mav = getModelAndView("/report/namespace/index");
		List<ZtreeAttribute> treeNodeList = namespaceService.buildNamespaceTreeNode();
		mav.addObject("treelist", treeNodeList);
		removeMetaDatasFromSession();
		return mav;
	}
	
}
