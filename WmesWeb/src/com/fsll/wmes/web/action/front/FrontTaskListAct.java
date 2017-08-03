package com.fsll.wmes.web.action.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.common.util.JsonUtil;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.WebTaskList;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.web.service.WebTaskListService;
import com.fsll.wmes.web.vo.WebTaskListVO;

@Controller
@RequestMapping("/front/web/taskList")
public class FrontTaskListAct extends WmesBaseAct{
	
	@Autowired
	private WebTaskListService webTaskListService;

	/**
	 * 获取所属的 Task List
	 * @author wwluo
	 * @data 2016-10-21
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getTaskList")
	public void getTaskList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		boolean flag = false;
		try {
			if(loginUser!=null){
				String recurentFlag = request.getParameter("recurentFlag");
				String ifImportant = request.getParameter("ifImportant");
				List<WebTaskListVO> webTaskListVOs = webTaskListService.getTaskList(loginUser.getId(),langCode,recurentFlag,ifImportant);
				flag = true;
				result.put("webTaskLists", webTaskListVOs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
}
