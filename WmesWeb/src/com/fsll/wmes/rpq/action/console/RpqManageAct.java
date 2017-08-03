package com.fsll.wmes.rpq.action.console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
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
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.RpqPage;
import com.fsll.wmes.entity.RpqPageEn;
import com.fsll.wmes.entity.RpqPageLevel;
import com.fsll.wmes.entity.RpqPageLevelEn;
import com.fsll.wmes.entity.RpqPageLevelSc;
import com.fsll.wmes.entity.RpqPageLevelTc;
import com.fsll.wmes.entity.RpqPageQuest;
import com.fsll.wmes.entity.RpqPageSc;
import com.fsll.wmes.entity.RpqPageTc;
import com.fsll.wmes.entity.RpqQuest;
import com.fsll.wmes.entity.RpqQuestEn;
import com.fsll.wmes.entity.RpqQuestItem;
import com.fsll.wmes.entity.RpqQuestSc;
import com.fsll.wmes.entity.RpqQuestTc;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.rpq.service.RpqManageService;
import com.fsll.wmes.rpq.vo.RpqListVO;
import com.fsll.wmes.rpq.vo.RpqPageLangVO;
import com.fsll.wmes.rpq.vo.RpqPageLevelLangVO;
import com.fsll.wmes.rpq.vo.RpqPageView;
import com.fsll.wmes.rpq.vo.RpqQuestItemVO;

/*****
 * 问卷管理
 * @author 林文伟
 * @editor scshi
 * @date 20161213
 * 2016-07-19
 */
@Controller
public class RpqManageAct extends CoreBaseAct {

	@Autowired
	 private RpqManageService rpqManageService;
	@Autowired
	 private DistributorService distributorConsoleService;
	
	/**
	 * 问卷分页列表
	 * @author 林文伟
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/rpq/list")
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/rpq/list";
	}
	
	/**
	 * 添加问卷表单
	 * @author 林文伟
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/rpq/form")
	public String form(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String id=request.getParameter("id");
		if(null!=id && !"".equals(id))
		{
			String language = this.getLoginLangFlag(request);//获取语言
//			RpqPage page = rpqManageService.getPage(id);
			RpqListVO pageVo = rpqManageService.getPageVoById(id,language);
			RpqPageSc sc = rpqManageService.getPageSc(id);
			RpqPageTc tc = rpqManageService.getPageTc(id);
			RpqPageEn en = rpqManageService.getPageEn(id);
			
			model.put("page", pageVo);
			model.put("sc", sc);
			model.put("tc", tc);
			model.put("en", en);
			model.put("language", language);
		}
		model.put("rpqPageId", id);
		return "console/rpq/form";
	}
	
	/**
	 * 问卷详细页
	 * @author 林文伟
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/rpq/detail")
	public String Pagedetail(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/rpq/detail";
	}

	/**
	 * 问卷分页数据查询的方法
	 * @author 林文伟
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/console/rpq/listpageJson", method = RequestMethod.POST)
	public void listPageJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String title=request.getParameter("title");
		String language = this.getLoginLangFlag(request);//获取语言
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
		//获取当前登录者信息
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
		MemberAdmin admin = distributorConsoleService.findDistributorMemberAdmin(curMember.getId());
		
		String typeSql = "";
		if(null!=admin){
			String memberType = admin.getType();
			if("2".equals(memberType)){//代理商
				typeSql+= " and t.distributor.id = '" + admin.getDistributor().getId() + "' ";
			}
			else if("1".equals(memberType)){//ifa公司
				typeSql=" and t.ifafirm.id='"+admin.getIfafirm().getId()+"'";
			}
			else{//平台
				typeSql=" and 1=0 ";
			}
		}
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=rpqManageService.findPageAll(jsonPaging, info, language,typeSql,pageType,clientType);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 题库列表页面
	 * @author 林文伟
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/rpq/set_page_quest")
	public String setPageRuest(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String langCode = this.getLoginLangFlag(request);
		String pageId=request.getParameter("pageid");
		model.put("pageId", pageId);
		String pageName = "";
		if("sc".equals(langCode)){
			RpqPageSc sc = rpqManageService.getPageSc(pageId);
			pageName = sc.getTitle();
		} else if("tc".equals(langCode)){
			RpqPageTc tc = rpqManageService.getPageTc(pageId);
			pageName = tc.getTitle();
		} else if("en".equals(langCode)){
			RpqPageEn en = rpqManageService.getPageEn(pageId);
			pageName = en.getTitle();
		}
		model.put("pageName", pageName);
		return "console/rpq/set_page_quest";
	}
	
	/****
	 * 保存题库的方法
	 * @author 
	 * @date 2016-07-18
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/console/rpq/setPageQuest", method = RequestMethod.POST)
	public void setPageQuest(RpqQuest info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String pageId=request.getParameter("pageid");
		String questList=request.getParameter("questlist");
	
		//选项实体 List<RpqPageQuest> pageQuestList
		List<RpqPageQuest> pageQuestList =new ArrayList<RpqPageQuest>();
		if(""!=questList)
		{
			String[] array = questList.split(",");
			if(array.length>0)
			{
				for(int i=0;i<array.length;i++)
				{
					if(""!=array[i])
					{
						String questId = array[i];
						
						RpqPageQuest each = new RpqPageQuest();
						each.setId(null);
						each.setOrderBy(i+1);
						
						RpqPage page = new RpqPage();
						page.setId(pageId);
						
						RpqQuest quest = new RpqQuest();
						quest.setId(questId);
						
						each.setPage(page);
						each.setQuest(quest);

						
						pageQuestList.add(each);
					}
				}
				
			}
		}

		rpqManageService.setPageQuest(pageId, pageQuestList);
		
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/***
     * 获取问卷下的所有题目
     * @author 林文伟
     * @date 2016-06-20
     */
	@RequestMapping(value="/console/rpq/listPageQuestList")
	public void getQuestByPageId(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String pageId=request.getParameter("pageid");
		String language = this.getLoginLangFlag(request);//获取语言
		List<RpqQuest> list = rpqManageService.getQuestByPageId(pageId,language);
		String ifaJson = JsonUtil.toJson(list);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("itemJson",ifaJson);
		JsonUtil.toWriter(obj, response);
		//ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/****
	 * 保存用户表单的方法
	 * @author 
	 * @date 2016-3-18
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/console/ifafirm/savePageInfo", method = RequestMethod.POST)
	public void savePageInfo(RpqPage info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String id=request.getParameter("id");
		String titleSc=request.getParameter("titleSc");
		String remarkSc=request.getParameter("remarkSc");
		String clientType=request.getParameter("clientType");
		//String langCode=request.getParameter("langCode");
		String isDefault=request.getParameter("isDefault");
		String pageType=request.getParameter("pageType");
		String status=request.getParameter("status");
		//TC
		String titleTc=request.getParameter("titleTc");
		String remarkTc=request.getParameter("remarkTc");
		//EN
		String titleEn=request.getParameter("titleEn");
		String remarkEn=request.getParameter("remarkEn");
		
		
		//add 增加“是否计算分值”字段    wwluo 160826 
		String isCalScore = request.getParameter("isCalScore");
		info.setIsCalScore(isCalScore);
		//end
		
		
		
		//info.setTitle(titleSc);
		info.setCreateTime(new Date());
		info.setIsValid("1");
		info.setLastUpdate(new Date());
		info.setStatus(status);
		info.setId(null);
		
		RpqPageLangVO vo = new RpqPageLangVO();
		vo.setTitleSc(titleSc);
		vo.setRemarkSc(remarkSc);
		vo.setTitleTc(titleTc);
		vo.setRemarkTc(remarkTc);
		vo.setTitleEn(titleEn);
		vo.setRemarkEn(remarkEn);
		//System.out.println(id);
		if(!"".equals(id.trim())){
			info.setId(id);
			isAdd=false;  
		}else{
			isAdd=true;
			info.setId(null);

		}
		
		//获取当前登录者信息
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
		MemberAdmin admin = distributorConsoleService.findDistributorMemberAdmin(curMember.getId());
		
		if(null!=admin)
		{
			//modify 设置问卷来源   wwluo 160826
			/*info.setMember(admin.getMember());
			if("2".equals(admin.getType()))//如果是代理商
			{
				info.setType("1");
				MemberDistributor distributor = admin.getDistributor();
				info.setDistributor(distributor);
			}*/
			info.setMember(admin.getMember());
			if(!"".equals(admin.getMember())){
				info.setCreatorId(admin.getMember().getId());
			}
			if("2".equals(admin.getType()))//代理商
			{
				info.setType("2");
				MemberDistributor distributor = admin.getDistributor();
				info.setDistributor(distributor);
			}else if("1".equals(admin.getType())){//ifa
				info.setType("1");
				MemberIfafirm ifaFirm = admin.getIfafirm();
				info.setIfafirm(ifaFirm);
			}else if("0".equals(admin.getType())){//平台
				info.setType("0");
			}
			//end
		}
		info.setCreatorId(admin.getMember().getId());
		info=rpqManageService.saveOrUpdate(info,vo,isAdd);
		//保存应该表信息
		rpqManageService.consoleSaveOrUpdateRpqPagetType(info,clientType,isDefault,pageType,admin);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/****
	 * 删除问卷，同时删除与题目的关联,跟等级数据
	 * @author 
	 * @date 2016-07-18
	 */
	@RequestMapping(value = "/console/rpq/delPage", method = RequestMethod.POST)
	public void delPage(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");
		
		rpqManageService.delRpqPage(id);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	
	/**************************等级管理******************************/
	/**
	 * 某问卷的等级列表页面
	 * @author 林文伟
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/rpq/page_level_list")
	public String pageLevelList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String language = this.getLoginLangFlag(request);//获取语言
		String pageId=request.getParameter("pageid");
//		if ("en".equals(language)) 
		RpqListVO page = rpqManageService.getPageVoById(pageId,language);
		model.put("page",page);
		return "console/rpq/page_level_list";
	}
	
	/**
	 * 获取问卷的所有等级数据列表
	 * */
	@RequestMapping(value="/console/rpq/listPageLevel")
	public void getLevelByPageId(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String pageId=request.getParameter("pageid");
		String language = this.getLoginLangFlag(request);//获取语言
		List<RpqPageLevel> list = rpqManageService.getLevelByPageId(pageId,language);
		String levelJson = JsonUtil.toJson(list);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("levelJson",levelJson);
		JsonUtil.toWriter(obj, response);
		//ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 弹出等级表单页面
	 * @author 林文伟
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/rpq/level_form")
	public String levelForm(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String id=request.getParameter("id");
		if(""!=id&&null!=id)
		{
			RpqPageLevel level = rpqManageService.getPageLevel(id);
			RpqPageLevelSc sc = rpqManageService.getPageLevelSc(id);
			RpqPageLevelTc tc = rpqManageService.getPageLevelTc(id);
			RpqPageLevelEn en = rpqManageService.getPageLevelEn(id);
			String language = this.getLoginLangFlag(request);//获取语言
			
			model.put("level", level);
			model.put("sc", sc);
			model.put("tc", tc);
			model.put("en", en);
			model.put("language", language);
		}
		return "console/rpq/level_form";
	}
	
	/****
	 * 删除Level
	 * @author 
	 * @date 2016-07-18
	 */
	@RequestMapping(value = "/console/rpq/delPageLevel", method = RequestMethod.POST)
	public void delPageLevel(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String levelId=request.getParameter("id");
		
		rpqManageService.delRpqPageLevel(levelId);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	
	/**************************题库 管理******************************/
	/**
	 * 题库列表页面
	 * @author 林文伟
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/rpq/quest_list")
	public String questlist(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/rpq/quest_list";
	}
	
	/**
	 * 题库分页数据查询的方法
	 * @author 林文伟
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/console/rpq/listquestJson", method = RequestMethod.POST)
	public void listQuestJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String title = request.getParameter("title");
		String questType = request.getParameter("questType");
		String status = request.getParameter("status");
		String language = this.getLoginLangFlag(request);//获取语言
		RpqQuest info=new RpqQuest();
		if(null!=title&&!"".equals(title)){
			try {
				title=URLDecoder.decode(title, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		info.setStatus(status);
		info.setTitle(title);
		info.setQuestType(questType);
		
		//获取当前登录者信息
		//MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		//MemberAdmin admin = distributorConsoleService.findDistributorMemberAdmin(curMember.getId());
		MemberAdmin admin = this.getLoginMemberAdmin(request);
		
		String typeSql = "";
		if(null!=admin)
		{
			if("2".equals(admin.getType()))//代理商
			{
				typeSql+= " and r.distributor.id = '" + admin.getDistributor().getId() + "' ";
			}
			else if("0".equals(admin.getType())){typeSql="";}
			else {typeSql=" and 1=0 ";};
		}
		
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=rpqManageService.findQuestAll(jsonPaging, info,language,typeSql);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 题库表单页面
	 * @author 林文伟
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/rpq/quest_form")
	public String questform(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String id=request.getParameter("id");
		RpqQuest info = rpqManageService.getQuest(id);
		RpqQuestSc rpqQuestSc = rpqManageService.getQuestSc(id);
		RpqQuestTc rpqQuestTc = rpqManageService.getQuestTc(id);
		RpqQuestEn rpqQuestEn = rpqManageService.getQuestEn(id);
		String language = this.getLoginLangFlag(request);//获取语言
		
		model.put("rpqQuestVO", info);
		model.put("rpqQuestScVO", rpqQuestSc);
		model.put("rpqQuestTcVO", rpqQuestTc);
		model.put("rpqQuestEnVO", rpqQuestEn);
		model.put("language", language);
		
		return "console/rpq/quest_form";
	}
	
	/**
	 * 弹出题目的选项表单页面
	 * @author 林文伟
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/rpq/quest_item_form")
	public String questitemform(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/rpq/dialog_quest_item";
	}
	
	/****
	 * 保存题库的方法
	 * @author 
	 * @date 2016-07-18
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/console/rpq/saveQuestInfo", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String id=request.getParameter("id");
		String titleSc=request.getParameter("titleSC");
		String remarkSc=request.getParameter("remarkSC");
		String ispublic=request.getParameter("ispublic");
		String status=request.getParameter("status");
		String questType=request.getParameter("questType");
		String titleTc=request.getParameter("titleTC");
		String remarkTc=request.getParameter("remarkTC");
		String titleEn=request.getParameter("titleEN");
		String remarkEn=request.getParameter("remarkEN");
		String itemlist=request.getParameter("itemlist");
		//System.out.print(itemlist);
		//题目实体
		RpqQuest info = new RpqQuest();
		RpqQuestSc infoSc = new RpqQuestSc();
		RpqQuestTc infoTc = new RpqQuestTc();
		RpqQuestEn infoEn = new RpqQuestEn();
		if("".equals(id)||null==id){
			info.setId(null);
			infoSc.setId(null);
			infoTc.setId(null);
			infoEn.setId(null);
			isAdd = true;
		} else {
			isAdd = false;
			info.setId(id);
			infoSc.setId(id);
			infoTc.setId(id);
			infoEn.setId(id);
		}

		//info.setTitle(titleSc);
		info.setCreateTime(new Date());
		info.setLastUpdate(new Date());
		info.setIsPublic(ispublic);
		info.setIsValid("1");
		//info.setRemark(remarkSc);
		info.setStatus(status);
		info.setQuestType(questType);
		infoSc.setTitle(titleSc);
		infoSc.setRemark(remarkSc);
		infoTc.setTitle(titleTc);
		infoTc.setRemark(remarkTc);
		infoEn.setTitle(titleEn);
		infoEn.setRemark(remarkEn);
		
		//获取当前登录者信息
		//MemberSsoVO vo=(MemberSsoVO)request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		//MemberBase mb = new MemberBase();
		//info.setType(vo.getMemberType().toString());
		//mb.setId(vo.getId());
		//info.setMember(mb);
		
		//MemberAdmin admin = distributorConsoleService.findDistributorMemberAdmin(vo.getId());
		MemberAdmin admin = this.getLoginMemberAdmin(request);
		
		if(null!=admin)
		{
			info.setMember(admin.getMember());
			if("2".equals(admin.getType()))//如果是代理商
			{
				info.setType("1");
				MemberDistributor distributor = admin.getDistributor();
				info.setDistributor(distributor);
			}
		}
		
		//选项实体
		List<RpqQuestItemVO> itemList =new ArrayList<RpqQuestItemVO>();

		if(""!=itemlist)
		{
			String[] array = itemlist.split("###");
			if(array.length>0)
			{
				for(int i=0;i<array.length;i++)
				{
					if(""!=array[i])
					{
						String[] eachArray = array[i].split("#~");// itemtitle+'||'+type+'||'+score+'||'+orderno+
						RpqQuestItemVO each = new RpqQuestItemVO();
						
						each.setOrderBy(Integer.parseInt(eachArray[3]));
						each.setRemark("__EMPTYSTR__".equals(eachArray[4])==true?"":eachArray[4]);
						each.setScoreValue(Integer.parseInt(eachArray[2]));
						each.setTitle("__EMPTYSTR__".equals(eachArray[0])==true?"":eachArray[1]);
						each.setType(eachArray[1]);
						each.setTitleSc(eachArray[0]);
						each.setRemarkSc("__EMPTYSTR__".equals(eachArray[4])==true?"":eachArray[4]);
						each.setTitleTc("__EMPTYSTR__".equals(eachArray[5])==true?"":eachArray[5]);
						each.setRemarkTc("__EMPTYSTR__".equals(eachArray[6])==true?"":eachArray[6]);
						each.setTitleEn("__EMPTYSTR__".equals(eachArray[7])==true?"":eachArray[7]);
						each.setRemarkEn("__EMPTYSTR__".equals(eachArray[8])==true?"":eachArray[8]);

						itemList.add(each);
					}
					
				}
				
			}
		}

		info=rpqManageService.save(info,infoSc,infoTc,infoEn, itemList,isAdd);
		
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 获取题目下的所有选项
	 * */
	@RequestMapping(value="/console/rpq/listQuestItem")
	public void getIFAKeyList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String questId=request.getParameter("id");
		List<RpqQuestItem> list = rpqManageService.getRpqQuestItemByQuestId(questId);
		String ifaJson = JsonUtil.toJson(list);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("itemJson",ifaJson);
		JsonUtil.toWriter(obj, response);
		//ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/****
	 * 删除题目
	 * @author 
	 * @date 2016-07-18
	 */
	@RequestMapping(value = "/console/rpq/delQuest", method = RequestMethod.POST)
	public void delQuest(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");
		
		rpqManageService.delRpqQuest(id);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 题库表单页面
	 * @author 林文伟
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/rpq/quest_view")
	public String questview(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String id=request.getParameter("id");
		RpqQuest info = rpqManageService.getQuest(id);
		RpqQuestSc rpqQuestSc = rpqManageService.getQuestSc(id);
		RpqQuestTc rpqQuestTc = rpqManageService.getQuestTc(id);
		RpqQuestEn rpqQuestEn = rpqManageService.getQuestEn(id);
		String language = this.getLoginLangFlag(request);//获取语言
		
		model.put("rpqQuestVO", info);
		model.put("rpqQuestScVO", rpqQuestSc);
		model.put("rpqQuestTcVO", rpqQuestTc);
		model.put("rpqQuestEnVO", rpqQuestEn);
		model.put("language", language);
		
		return "console/rpq/quest_view";
	}
	
	/****
	 * 保存等级数据
	 * @author 
	 * @date 2016-07-18
	 */
	@RequestMapping(value = "/console/rpq/savePageLevel", method = RequestMethod.POST)
	public void savePageLevel(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String id=request.getParameter("id");
		String pageId=request.getParameter("pageid");
		String beginScore=request.getParameter("beginScore");
		String endScore=request.getParameter("endScore");
		String riskLevel=request.getParameter("riskLevel");
		//SC
		String resultSc=request.getParameter("resultSc");
		String remarkSc=request.getParameter("remarkSc");
		//TC
		String resultTc=request.getParameter("resultTc");
		String remarkTc=request.getParameter("remarkTc");
		//EN
		String resultEn=request.getParameter("resultEn");
		String remarkEn=request.getParameter("remarkEn");
		//等级多语言实体
		RpqPageLevelLangVO vo = new RpqPageLevelLangVO();
		vo.setTitleSc(resultSc);
		vo.setRemarkSc(remarkSc);
		vo.setTitleTc(resultTc);
		vo.setRemarkTc(remarkTc);
		vo.setTitleEn(resultEn);
		vo.setRemarkEn(remarkEn);
		//等级实体
		RpqPageLevel info = new RpqPageLevel();
		RpqPage page = new RpqPage();
		page.setId(pageId);
		
		if(""==id)
		{
			info.setId(null);
			isAdd = true;
		}
		else
		{
			isAdd = false;
			info.setId(id);
		}
		
		info.setBeginScore( Integer.parseInt(beginScore) );
		info.setEndScore( Integer.parseInt(endScore) );
		info.setCreateTime(new Date());
		info.setLastUpdate(new Date());
		info.setIsValid("1");
		info.setPage(page);

		info.setRiskLevel(Integer.parseInt(riskLevel));

		
		info=rpqManageService.savePageLevel(info,vo,isAdd);
		
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 显示问卷界面
	 * @author 林文伟
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/rpq/view_page")
	public String view_page(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);

		return "console/rpq/view_page";
	}
	
	/**
	 * 预览问卷
	 * */
	@RequestMapping(value="/console/rpq/viewPageData")
	public void viewPage(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String pageId=request.getParameter("id");
		String language = this.getLoginLangFlag(request);//获取语言
		RpqPageView view = rpqManageService.viewPage(pageId, language);
		//List view = rpqManageService.viewPageTest(pageId, language);
		String ifaJson = JsonUtil.toJson(view);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("view",ifaJson);
		JsonUtil.toWriter(obj, response);

	}
}



