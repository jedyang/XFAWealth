/**
 * 
 */
package com.fsll.wmes.rpq.action.console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.RpqPage;
import com.fsll.wmes.entity.RpqPageLevel;
import com.fsll.wmes.entity.RpqQuest;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.rpq.service.RpqForDistributorService;
import com.fsll.wmes.rpq.service.RpqManageService;
import com.fsll.wmes.rpq.vo.RpqListVO;

/**
 * @author scshi
 *代理商对自己的问卷管理
 *@author scshi
 *@date 20170612
 */
@Controller
@RequestMapping("/console/rpqDistributor")
public class RqpForDistributorAct extends CoreBaseAct {
	@Autowired
	private DistributorService distributorConsoleService;
	@Autowired
	private RpqForDistributorService rpqForDistributorService;
	@Autowired
	private RpqManageService rpqManageService;
	/**
	 * 代理RPQ问卷页面
	 * @author scshi
	 * @date 20170612
	 * */
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/rpqDistributor/list";
	}
	
	/**
	 * 代理商RPQ问卷列表
	 * @author scshi
	 * @date 30170612
	 * */
	@RequestMapping(value = "/listPageJson", method = RequestMethod.POST)
	public void listPageJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String language = this.getLoginLangFlag(request);//获取语言
		String title=request.getParameter("title");
		String clientType=request.getParameter("clientType");
		String pageType=request.getParameter("pageType");
		RpqPage info=new RpqPage();
		if(null!=title&&!"".equals(title)){
			try {
				title=URLDecoder.decode(title, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		info.setTitle(title);
		info.setClientType(clientType);
		info.setPageType(pageType);
		
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
		MemberAdmin admin = distributorConsoleService.findDistributorMemberAdmin(curMember.getId());
		String typeSql = "";
		if(null!=admin){
			String memberType = admin.getType();
			if("2".equals(memberType)){//代理商
				typeSql= " and t.distributor.id = '" + admin.getDistributor().getId() + "' ";
			}
			else if("1".equals(memberType)){//ifa公司
				typeSql=" and t.ifafirm.id='"+admin.getIfafirm().getId()+"'";
			}
			else{//平台
				typeSql=" and 1=0 ";
			}
		}
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = rpqForDistributorService.queryPageList(jsonPaging,language,info,typeSql);
		this.toJsonString(response, jsonPaging);
	}
	
	
	/**
	 * 题库管理
	 * @author scshi
	 * @date 20170612
	 * */
	@RequestMapping(value = "/questionsList")
	public String questionsPage(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		this.isMobileDevice(request,model);
		return "console/rpqDistributor/questionsBankList";
	}
	
	/**
	 *题库列表
	 *@author scshi
	 *@date 20170612
	 * */
	@RequestMapping(value = "/questionsListJson", method = RequestMethod.POST)
	public void questionsListJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String title=request.getParameter("title");
		String status=request.getParameter("status");
		String questType=request.getParameter("questType");
		String adaptType=request.getParameter("adaptType");
		String language = this.getLoginLangFlag(request);//获取语言
		RpqQuest info=new RpqQuest();
		if(null!=title&&!"".equals(title)){
			try {
				title=URLDecoder.decode(title, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		info.setTitle(title);
		info.setStatus(status);//草稿，使用中，未使用，删除
		info.setQuestType(questType);
		info.setAdaptType(adaptType);//开户，kyc，投资方案rpq
		
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
		MemberAdmin admin = distributorConsoleService.findDistributorMemberAdmin(curMember.getId());
		if(null!=admin){
			info.setDistributor(admin.getDistributor());
		}
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=rpqForDistributorService.queryQuestionsList(jsonPaging,language,info);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 從系統模板創建問卷
	 * @author scshi
	 * @date 20170612
	 * */
	@RequestMapping(value = "/pageFromTemp")
	public String pageFromTemp(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		this.isMobileDevice(request,model);
		return "console/rpqDistributor/paperFromTemp";
	}
	
	/**
	 * 系統模板列表
	 * @author scshi
	 * @date 20170613
	 * */
	@RequestMapping(value = "/pageFromTempListJson", method = RequestMethod.POST)
	public void pageFromTempListJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String language = this.getLoginLangFlag(request);//获取语言
		String title=request.getParameter("title");
		String clientType=request.getParameter("clientType");
		String pageType=request.getParameter("pageType");
		RpqPage info=new RpqPage();
		if(null!=title&&!"".equals(title)){
			try {
				title=URLDecoder.decode(title, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		info.setTitle(title);
		info.setClientType(clientType);
		info.setPageType(pageType);
		
		String typeSql = " and t.distributor.id is null and t.ifafirm.id is null ";
		
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = rpqForDistributorService.queryPageList(jsonPaging,language,info,typeSql);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 模板關聯當前登錄用戶
	 * @author scshi
	 * @date 20170613
	 * */

	
	/**
	 * 问卷等级查看看
	 * @author scshi
	 * @date 20170614
	 * */
	@RequestMapping(value = "/pageLevelView")
	public String pageLevelView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		this.isMobileDevice(request,model);
		String language = this.getLoginLangFlag(request);//获取语言
		String pageId=request.getParameter("pageid");
		RpqListVO page = rpqManageService.getPageVoById(pageId,language);
		model.put("page",page);
		return "console/rpqDistributor/pageLevelList";
	}
	
	@RequestMapping(value="/pageLevelJson", method = RequestMethod.POST)
	public void pageLevelJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String pageId=request.getParameter("pageid");
		String language = this.getLoginLangFlag(request);//获取语言
		List<RpqPageLevel> list = rpqManageService.getLevelByPageId(pageId,language);
		String levelJson = JsonUtil.toJson(list);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("levelJson",levelJson);
		JsonUtil.toWriter(obj, response);
	}
	
	
	/**
	 * 保存来自模板的问卷
	 * @author scshi
	 * @date 20170613
	 * */
	@RequestMapping(value="/templateUnionDistributor", method = RequestMethod.POST)
	public void templateUnionDistributor(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String tempId = request.getParameter("tempId");
		//当前登录代理商
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
		MemberAdmin admin = distributorConsoleService.findDistributorMemberAdmin(curMember.getId());
		rpqForDistributorService.saveTemplateUnionDistributor(admin.getDistributor(),tempId);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		JsonUtil.toWriter(obj, response);
	}
}
