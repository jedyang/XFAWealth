package com.fsll.wmes.ifafirm.action.console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.ChineseToEnglish;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.service.SysCountryService;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.web.vo.IfafirmTeamVO;

/*****
 * 邮件管理
 * @author 林文伟
 * 2016-06-23
 */
@Controller
public class IfafirmManageAct extends CoreBaseAct {

	@Autowired
	 private IfafirmManageService ifafirmService;
	@Autowired
	 private MemberBaseService memberBaseService;
	@Autowired
	 private DistributorService distributorConsoleService;
	@Autowired
	private SysCountryService sysCountryService;
	
	/**
	 * 分页列表
	 * @author 林文伟
	 * @date 2016-07-01
	 */
	@RequestMapping(value = "/console/ifafirm/list")
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/list";
	}

	/**
	 * 弹出选择公司窗体
	 * @author 林文伟
	 * @date 2016-07-01
	 */
	@RequestMapping(value = "/console/ifafirm/dialogFirmShow")
	public String dialogFirmShow(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/dialog_firm";
	}
	
	/**
	 * 在弹出的公司查询窗体中数据查询的方法
	 * @author 林文伟
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/console/ifafirm/listfirmJson", method = RequestMethod.POST)
	public void listFirmJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String companyName=request.getParameter("companyname");
		//String entityType=request.getParameter("entitytype");
		//String registerNo=request.getParameter("registerno");
		MemberIfafirm info=new MemberIfafirm();
		if(null!=companyName&&!"".equals(companyName)){
			try {
				companyName=URLDecoder.decode(companyName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		info.setCompanyName(companyName);
		
		//获取当前登录者信息
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		MemberAdmin admin = distributorConsoleService.findDistributorMemberAdmin(curMember.getId());
//		String typeSql = "";
//		if(null!=admin)
//		{
//			if("2".equals(admin.getType()))
//			{
//				typeSql+= " and r.id = '" + admin.getDistributor().getId() + "' ";
//			}
//			else if("0".equals(admin.getType())){typeSql="";}
//			else {typeSql=" and 1=0 ";};
//		}
		
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=ifafirmService.findAll(jsonPaging, info,admin);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 获取公司的团队数据，自己组装数据
	 * @author 林文伟
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/console/ifafirm/listfirmteamJson", method = RequestMethod.POST)
	public void listFirmTeamJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
//			String ifafirmid=request.getParameter("ifafirmid");
			IfafirmTeam info=new IfafirmTeam();
			MemberIfafirm firm = new MemberIfafirm();
			firm.setId(curAdmin.getIfafirm().getId());
			info.setIfafirm(firm);
			
			List<IfafirmTeam> countryList = ifafirmService.findTeamAll(info);
			String countryJson = JsonUtil.toJson(countryList);
			obj.put("result",true);
			obj.put("teamJson",countryJson);
		}else{
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 弹出公司信息窗体
	 * @author 林文伟
	 * @date 2016-07-01
	 */
	@RequestMapping(value = "/console/ifafirm/dialogFirmInfo")
	public String dialogFirmInfo(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/dialog_firm_form";
	}
	
	/**
	 * 获取国家列表
	 * */
	@RequestMapping(value="/console/ifafirm/listCountryJson",method = RequestMethod.POST)
	public void countryList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		List<SysCountry> countryList = memberBaseService.loadSysCountryList();
		String countryJson = JsonUtil.toJson(countryList);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("countryJson",countryJson);
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
	@RequestMapping(value = "/console/ifafirm/savefirminfo", method = RequestMethod.POST)
	public void saveFirmInfo(MemberIfafirm info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String id=request.getParameter("id");
		String companyName=request.getParameter("CompanyName");
		String entitytype=request.getParameter("Entitytype");
		String registerNo=request.getParameter("RegisterNo");
		String isFinancial=request.getParameter("IsFinancial");
		String giin=request.getParameter("Giin");
		String incorporationPlace=request.getParameter("IncorporationPlace");
		String registeredAddress=request.getParameter("RegisteredAddress");
		String mailingAddress=request.getParameter("MailingAddress");
		String country=request.getParameter("Country");
		
		//add wwluo 160823
		String naturePurpose=request.getParameter("NaturePurpose");
		String incorporationDate=request.getParameter("IncorporationDate");
		String website=request.getParameter("Website");
		String companyNameTc=request.getParameter("CompanyNameTc");
		String companyNameEn=request.getParameter("CompanyNameEn");
		//end
		
		info.setId(id);
		info.setCompanyName(companyName);
		info.setEntityType(entitytype);
		info.setRegisterNo(registerNo);
		info.setIsFinancial(isFinancial);
		info.setGiin(giin);
		info.setIncorporationPlace(incorporationPlace);
		info.setRegisteredAddress(registeredAddress);
		info.setMailingAddress(mailingAddress);
		info.setCountry(country);
		
		//add wwluo 160823
		info.setNaturePurpose(naturePurpose);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date=sdf.parse(incorporationDate);
			info.setIncorporationDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		info.setWebsite(website);
		info.setIsValid("1");
		//end
		
		if(null!=info.getId()&&!"".endsWith(info.getId())){

			isAdd=false;  
		}else{
			isAdd=true;
			info.setId(null);

		}

		info=ifafirmService.saveOrUpdate(info,isAdd);
		
		
		//add wwluo 160826
		if(null != info){
			String pinyin = ChineseToEnglish.getPingYin(companyName);
			MemberIfafirmSc memberIfafirmSc = new MemberIfafirmSc();
				memberIfafirmSc.setId(info.getId());
				memberIfafirmSc.setCompanyName(companyName);
				memberIfafirmSc.setPinYin(pinyin);
			memberIfafirmSc = ifafirmService.saveOrUpdateFirmSc(memberIfafirmSc);
			
			MemberIfafirmTc memberIfafirmTc = new MemberIfafirmTc();
				memberIfafirmTc.setId(info.getId());
				memberIfafirmTc.setCompanyName(companyNameTc);
				memberIfafirmTc.setPinYin(pinyin);
			memberIfafirmTc = ifafirmService.saveOrUpdateFirmTc(memberIfafirmTc);
			
			MemberIfafirmEn memberIfafirmEn = new MemberIfafirmEn();
				memberIfafirmEn.setId(info.getId());
				memberIfafirmEn.setCompanyName(companyNameEn);
				memberIfafirmEn.setPinYin(pinyin);
			memberIfafirmEn = ifafirmService.saveOrUpdateFirmEn(memberIfafirmEn);
		}
		//end
		
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 弹出团队成员管理窗口
	 * @author 林文伟
	 * @date 2016-07-01
	 */
	@RequestMapping(value = "/console/ifafirm/dialogTeamUser")
	public String dialogTeamUserShow(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String teamid=request.getParameter("teamid");
//			String ifafirmid=request.getParameter("ifafirmid");
			model.put("teamid", teamid);
			model.put("ifafirmid", curAdmin.getIfafirm().getId());
			
			return "console/ifafirm/dialog_team_user";
		}else{
    		return "redirect:"+this.getFullPath(request)+"index.do";
    	}
	}
	
	/**
	 * 弹出团队信息窗口
	 * @author michael
	 * @date 2017-03-01
	 */
	@RequestMapping(value = "/console/ifafirm/dialogTeamShow")
	public String dialogTeamInfoShow(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String teamid=request.getParameter("id");
	//		String ifafirmid=request.getParameter("ifafirmid");
			String parentid=request.getParameter("parentid");
			model.put("parentid", parentid);
			
//			String name=request.getParameter("name");
//			String code=request.getParameter("code");
//			//String orderby=request.getParameter("orderby");
//			String remark=request.getParameter("remark");
//			String classlayer=request.getParameter("classlayer");
//			boolean isAdd =false;
			
			if (!StrUtils.isEmpty(parentid)){
				IfafirmTeam parent = ifafirmService.getIfafirmTeam(parentid);
				model.put("parent", parent);
			}
			
			IfafirmTeam info = null;
			if (!StrUtils.isEmpty(teamid)){
				info = ifafirmService.getIfafirmTeam(teamid);
			}
			if (null==info) info = new IfafirmTeam();
			model.put("info", info);
			
			if (null!=info.getParent() &&  !StrUtils.isEmpty(info.getParent().getId())){
				model.put("parentid", info.getParent().getId());
			}
			
			return "console/ifafirm/dialog_team_form";
		}else{
    		return "redirect:"+this.getFullPath(request)+"index.do";
    	}
	}
	
	/**
	 * 通过输入一个KEY获取IFA数据，用于团队成员选择窗口
	 * */
	@RequestMapping(value="/console/ifafirm/listKeyToIFAJson")
	public void getIFAKeyList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
//			String ifafirmid=request.getParameter("ifafirmid");
			String keyword=request.getParameter("keyword");
			List<MemberIfa> list = ifafirmService.getIFAKeyList(curAdmin.getIfafirm().getId(),keyword);
			String ifaJson = JsonUtil.toJson(list);
			obj.put("result",true);
			obj.put("ifaJson",ifaJson);
		}else{
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
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
	@RequestMapping(value = "/console/ifafirm/saveallteaminfo", method = RequestMethod.POST)
	public void saveAllTeamInfo(IfafirmTeam info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String json=request.getParameter("json");
		
		List<IfafirmTeamVO>	 list	= this.toList(json,IfafirmTeamVO.class);
		if(!list.isEmpty())
		{
			for(IfafirmTeamVO vo : list)
			{
				String name = vo.getName();
				String code = vo.getcode();
				int orderby = vo.getOrderby();
				int classlayer = vo.getClasslayer();
				String ifafirmid = vo.getIfafirmid();
				String id = vo.getId();
				String parentid = vo.getParentid();
				//判断该团队是否存在，存在则更新，否则新增
				IfafirmTeam team = ifafirmService.getIfafirmTeam(id);
				if(null!=team)
				{
					if(null!=team.getId()&&!"".endsWith(team.getId()))
					{
						team.setId(id);
						team.setCode(code);
						team.setName(name);
						team.setOrderBy(orderby);
						team.setClassLayer(classlayer);
						
						
						MemberIfafirm memberIfafirm = new MemberIfafirm();
						memberIfafirm.setId(ifafirmid);
						info.setIfafirm(memberIfafirm);

						
						IfafirmTeam ifafirmTeam1 = new IfafirmTeam();
						ifafirmTeam1.setId(parentid);
						info.setParent(ifafirmTeam1);
						
						//team.setParentId(parentid);
						//team.setIfafirmId(ifafirmid);
						
						isAdd = false;
						team=ifafirmService.saveOrUpdateTeam(team,isAdd);
					}
				}
				else 
				{
					IfafirmTeam team1 = new IfafirmTeam();
					isAdd = true;
					team1.setId(null);
					team1.setCode(code);
					team1.setName(name);
					team1.setOrderBy(orderby);
					team1.setClassLayer(classlayer);
					
					MemberIfafirm memberIfafirm = new MemberIfafirm();
					memberIfafirm.setId(ifafirmid);
					info.setIfafirm(memberIfafirm);

					
					IfafirmTeam ifafirmTeam1 = new IfafirmTeam();
					ifafirmTeam1.setId(parentid);
					info.setParent(ifafirmTeam1);
					
					team1=ifafirmService.saveOrUpdateTeam(team1,isAdd);
				}
					
				//team=ifafirmService.saveOrUpdateTeam(team,isAdd);
			}
		}
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("ifaJson",list);
		JsonUtil.toWriter(obj, response);
	}
	
	public  Object getObjFromJsonArrStr(String source,Class beanClass)  
	{  
	    JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(source);  
	    return  JSONObject.toBean(jsonObject,beanClass);          
	}  
	
	public  List toList(String jsonString, Class beanClass) {
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        List list = new ArrayList();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Object object = JSONObject.toBean(jsonObject, beanClass);
            list.add(object);
        }
        return list;
    }
	
	/**
	 * 通过公司与团队ID获取其所有成员数据
	 * */
	@RequestMapping(value="/console/ifafirm/listTeamMemberIfaJson")
	public void getTeamMemberIfaByTeamId(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){

			String keyword=request.getParameter("keyword");
			String teamid=request.getParameter("teamid");
			String ifafirmid=request.getParameter("ifafirmid");
			String ifaid=request.getParameter("ifaid");
	
			
			jsonPaging=this.getJsonPaging(request);
			jsonPaging=ifafirmService.findAllTeamIfa(jsonPaging, ifafirmid,teamid,keyword,ifaid);
			this.toJsonString(response, jsonPaging);
		}else{
			this.toJsonString(response, null);
		}
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
	@RequestMapping(value = "/console/ifafirm/saveteammemberifainfo", method = RequestMethod.POST)
	public void saveTeamMemberifaInfo(IfafirmTeam info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			//boolean isAdd =false;
			String list=request.getParameter("list");
			String teamid=request.getParameter("teamid");
//			String ifafirmid=request.getParameter("ifafirmid");
			
			ifafirmService.delTeamMemberIfa(teamid, curAdmin.getIfafirm().getId());
			
			String[] array = list.split(",");//
			for (int i = 0 ; i <array.length ; i++ ) 
			{
				String ifaId = array[i]; //
				IfafirmTeamIfa ifa = new IfafirmTeamIfa();
				//isAdd = true;
				ifa.setId(null);
				
				MemberIfafirm memberIfafirm = new MemberIfafirm();
				memberIfafirm.setId(curAdmin.getIfafirm().getId());
				ifa.setIfafirm(memberIfafirm);
	
				IfafirmTeam ifafirmTeam1 = new IfafirmTeam();
				ifafirmTeam1.setId(teamid);
				ifa.setTeam(ifafirmTeam1);
				
				MemberIfa memberIfa = new MemberIfa();
				memberIfa.setId(ifaId);
				//ifa.setIfafirmId(ifafirmid);
				ifa.setIfa(memberIfa);
				//ifa.setTeamId(teamid);
	
				ifafirmService.saveOrUpdateTeamMemberifa(ifa,true);
			}
			
			obj.put("result",true);
			obj.put("ifaJson",list);
		}else{
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 给团队添加成员
	 * */
	@RequestMapping(value="/console/ifafirm/addTeamMemberIfa")
	public void addTeamMemberIfa(IfafirmTeamIfa info,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String ifaid=request.getParameter("ifaid");
			String teamid=request.getParameter("teamid");
			String ifafirmid=request.getParameter("ifafirmid");
			boolean isAdd =false;
			//判断该成员是否存在在该团队中
			IfafirmTeamIfa ifafirmTeamIfa = ifafirmService.checkIfaIsExistInTeam(ifafirmid, teamid, ifaid);
			if(null!=ifafirmTeamIfa)
			{
				isAdd=true;
			} 
			else
			{
				info.setId(null);
				
				MemberIfafirm memberIfafirm = new MemberIfafirm();
				memberIfafirm.setId(ifafirmid);
				info.setIfafirm(memberIfafirm);
	
				IfafirmTeam ifafirmTeam1 = new IfafirmTeam();
				ifafirmTeam1.setId(teamid);
				info.setTeam(ifafirmTeam1);
				
				MemberIfa memberIfa = new MemberIfa();
				memberIfa.setId(ifaid);
				info.setIfa(memberIfa);
	//			info.setIfaId(ifaid);
	//			info.setIfafirmId(ifafirmid);
	//			info.setTeamId(teamid);
				info.setIsSupervisor("0");
				isAdd=true;
				info=ifafirmService.saveOrUpdateTeamIfa(info, isAdd);
			}
			
	
			//ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
			
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else{
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 添加团队节点
	 * */
	@RequestMapping(value="/console/ifafirm/addIfafirmTeamInfo")
	public void addIfafirmTeamInfo(IfafirmTeam info,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String teamid=request.getParameter("id");
//			String ifafirmid=request.getParameter("ifafirmid");
			String parentid=request.getParameter("parentid");
			String name=request.getParameter("name");
			String code=request.getParameter("code");
			//String orderby=request.getParameter("orderby");
			String remark=request.getParameter("remark");
			String classlayer=request.getParameter("classlayer");
			boolean isAdd =false;
			
			
			info.setCode(code);
			info.setName(name);
			info.setName(remark);
			
			//判断该团队是否存在   modify wwluo 160831
			//IfafirmTeam ifafirmTeam = ifafirmService.getIfafirmTeam(teamid);
			IfafirmTeam ifafirmTeam = null;
			if(StringUtils.isNotBlank(teamid)){
				ifafirmTeam = ifafirmService.getIfafirmTeam(teamid);
			}
			//end
			if(null!=ifafirmTeam)
			{
				info.setId(teamid);
	
//				MemberIfafirm memberIfafirm = new MemberIfafirm();
//				memberIfafirm.setId(ifafirmid);
				info.setIfafirm(curAdmin.getIfafirm());
				//info.setIfafirmId("06829AF2C1C691B408A789D84D31B6FA");
	
				info.setOrderBy(99);
				info.setIsValid("1");
				
				IfafirmTeam ifafirmTeam1 = new IfafirmTeam();
				ifafirmTeam1.setId(parentid);
				info.setParent(ifafirmTeam1);
				//info.setParentId("1");
				info.setClassLayer(2);
				isAdd=false;
			}
			else
			{
				MemberIfafirm memberIfafirm = new MemberIfafirm();
				memberIfafirm.setId(curAdmin.getIfafirm().getId());
				info.setIfafirm(memberIfafirm);
				//info.setIfafirmId(ifafirmid);
				info.setId(null);
				info.setOrderBy(99);
				info.setIsValid("1");
				
				IfafirmTeam ifafirmTeam1 = new IfafirmTeam();
				ifafirmTeam1.setId(parentid);
				info.setParent(ifafirmTeam1);
				//info.setParentId(parentid);
				
				info.setClassLayer(Integer.parseInt(classlayer) );
				isAdd=true;
			}
			
			info=ifafirmService.saveOrUpdateTeam(info, isAdd);
			//ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
			
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else{
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/****
	 * 删除节点
	 * @author 
	 * @date 2016-3-18
	 */
	@RequestMapping(value = "/console/ifafirm/delIfafirmTeamInfo", method = RequestMethod.POST)
	public void delIfafirmTeamInfo(MemberIfafirm info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		//boolean isAdd =false;
		String id=request.getParameter("id");

		Boolean rs =ifafirmService.delIfafirmTeamInfo(id);
		
		if(rs)
		{
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
			ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
		}
		else
		{
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("result",false);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
			ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
		}
		
	}
	
	/****
	 * 启动与取消团队成员的sv操作
	 * @author 
	 * @date 2016-3-18
	 */
	@RequestMapping(value = "/console/ifafirm/dealSupervisor", method = RequestMethod.POST)
	public void dealSupervisor(MemberIfafirm info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		//boolean isAdd =false;
		String id=request.getParameter("id");

		ifafirmService.dealSupervisor(id);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 弹出窗口，显示代理商列表页面
	 * @author 林文伟
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/console/ifafirm/dialogList")
	public String dialogList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/dialog_distributor_list";
	}
	
	/**
	 * ifafirm的代理商列表查询的方法
	 * @author 林文伟
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/console/ifafirm/findIfafirmDistributorListJson", method = RequestMethod.POST)
	public void findIfafirmDistributorListJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
//			String ifafirmId=request.getParameter("ifafirmid");
			jsonPaging=this.getJsonPaging(request);
			jsonPaging=ifafirmService.findIfafirmDistributorList(jsonPaging, curAdmin.getIfafirm().getId());
			this.toJsonString(response, jsonPaging);
		}else{
			this.toJsonString(response, null);
		}
	}
	
	/****
	 * 删除公司与代理商的关联关系
	 * @author 
	 * @date 2016-3-18
	 */
	@RequestMapping(value = "/console/ifafirm/delIfafirmDistributorid", method = RequestMethod.POST)
	public void delIfafirmDistributorid(MemberIfafirm info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");

		ifafirmService.delIfafirmDistributorid(id);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/****
	 * 删除公司与代理商的关联关系
	 * @author 
	 * @date 2016-3-18
	 */
	@RequestMapping(value = "/console/ifafirm/addIfafirmDistributorid", method = RequestMethod.POST)
	public void addIfafirmDistributorid(MemberIfafirm info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String distributorid=request.getParameter("distributorid");
//			String ifafirmid=request.getParameter("ifafirmid");
			
			ifafirmService.addIfafirmDistributorid(distributorid, curAdmin.getIfafirm().getId());
			
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else{
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	
	/**
	 * ifafirm 分页列表
	 * @author wwluo
	 * @date 2016-08-22
	 */
	@RequestMapping(value = "/console/ifafirm/ifaFirmList")
	public String ifaFirmList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/firm_List";
	}
	
	/**
	 * 获取ifafirm 列表数据
	 * @author wwluo
	 * @date 2016-08-22
	 */
	@RequestMapping(value = "/console/ifafirm/ifaFirmJson")
	public void ifaFirmJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String lang = this.getLoginLangFlag(request);
		String companyName = request.getParameter("companyName");
		String registerNo = request.getParameter("registerNo");
		String entityType = request.getParameter("entityType");
		jsonPaging = this.getJsonPaging(request);
		String  langFlag = this.getLoginLangFlag(request);
		jsonPaging=ifafirmService.getIfaFirmJson(jsonPaging ,companyName ,registerNo ,entityType,lang);
		for (Object object : jsonPaging.getList()) {
			MemberIfafirm ifafirm = (MemberIfafirm) object; 
			SysCountry  country = sysCountryService.findById(ifafirm.getCountry());
			if(null != country){
				if("en".equals(langFlag)){
					ifafirm.setCountry(country.getNameEn());
				}else if("tc".equals(langFlag)){
					ifafirm.setCountry(country.getNameTc());
				}else{
					ifafirm.setCountry(country.getNameSc());
				}
			}
			/*String name = ifafirm.getCompanyName();
			if("en".equals(langFlag)){
				MemberIfafirmEn memberIfafirmEn = 
					(MemberIfafirmEn) ifafirmService.findCompanyNameById(langFlag,
						ifafirm.getId());
				if(null != memberIfafirmEn)
					ifafirm.setCompanyName(memberIfafirmEn.getCompanyName());
			}else if("tc".equals(langFlag)){
				MemberIfafirmTc memberIfafirmTc = 
					(MemberIfafirmTc) ifafirmService.findCompanyNameById(langFlag,
						ifafirm.getId());
				if(null != memberIfafirmTc)
					ifafirm.setCompanyName(memberIfafirmTc.getCompanyName());
			}else{
				MemberIfafirmSc memberIfafirmSc = 
					(MemberIfafirmSc) ifafirmService.findCompanyNameById(langFlag,
						ifafirm.getId());
				if(null != memberIfafirmSc)
					ifafirm.setCompanyName(memberIfafirmSc.getCompanyName());
			}*/
			if(StringUtils.isBlank(ifafirm.getCompanyName())){
				ifafirm.setCompanyName(ifafirm.getCompanyName());
			}
		}
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 获取ifafirm 详情
	 * @author wwluo
	 * @date 2016-08-22
	 */
	@RequestMapping(value = "/console/ifafirm/ifaFirmDetail")
	public String ifaFirmDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String ifafirmId = curAdmin.getIfafirm().getId();//request.getParameter("ifafirmId");
	    	MemberIfafirmVO memberIfafirmVO = new MemberIfafirmVO();
	        if(StringUtils.isNoneEmpty(ifafirmId)){
	        	MemberIfafirm ifafirm = ifafirmService.findById(ifafirmId);
	        	MemberIfafirmSc ifafirmSc = ifafirmService.findIfafirmScById(ifafirmId);
	        	MemberIfafirmTc ifafirmTc = ifafirmService.findIfafirmTcById(ifafirmId);
	        	MemberIfafirmEn ifafirmEn = ifafirmService.findIfafirmEnById(ifafirmId);
	        	BeanUtils.copyProperties(ifafirm, memberIfafirmVO);
	        	if(null != ifafirmSc){
	        		memberIfafirmVO.setCompanyNameSc(ifafirmSc.getCompanyName());
	        	}
	        	if(null != ifafirmTc){
	        		memberIfafirmVO.setCompanyNameTc(ifafirmTc.getCompanyName());
	        	}
	        	if(null != ifafirmEn){
	        		memberIfafirmVO.setCompanyNameEn(ifafirmEn.getCompanyName());
	        	}
	        }
	        model.put("ifafirm", memberIfafirmVO);
			return "console/ifafirm/firm_input";
		}else{
    		return "redirect:"+this.getFullPath(request)+"index.do";
    	}
	}
	
	/**
	 * 删除ifafirm
	 * @author wwluo
	 * @date 2016-08-22
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/console/ifafirm/delifafirm")
	public void deleteIfafirm(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map map =new HashMap();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
//			String ifafirmId = request.getParameter("ifafirmId");
//			if(StringUtils.isNoneEmpty(ifafirmId)){
				boolean flag = false;
	        	flag = ifafirmService.deleteById(curAdmin.getIfafirm().getId());
	        	map.put("flag", flag);
//	        }
		}else{
			map.put("flag",false);
			map.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(map, response);
	}
	
	/**
	 * 跳转关联的firm页面(ifafirm-ifafirm)
	 * @author wwluo
	 * @date 2016-08-22
	 */
	@RequestMapping(value = "/console/ifafirm/ifafirmIfafirm")
	public String firmFirmList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
//			String ifafirmId = request.getParameter("ifafirmId");
			MemberIfafirm memberIfafirm = curAdmin.getIfafirm();//ifafirmService.findById(ifafirmId);
			if(null != memberIfafirm)
				model.put("companyName",memberIfafirm.getCompanyName());
			model.put("ifafirmId", curAdmin.getIfafirm().getId());
			return "console/ifafirm/firm_firm";
		}else{
    		return "redirect:"+this.getFullPath(request)+"index.do";
    	}
	}
	
	/**
	 * 获取关联的firm数据(ifafirm-ifafirm)
	 * @author wwluo
	 * @date 2016-08-22
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/console/ifafirm/ifafirmIfafirmJson")
	public void relevanceFirmJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
        Map map = new HashMap();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
//			String ifafirmId = request.getParameter("ifafirmId");
			List<MemberIfafirmVO> list = ifafirmService.findIfafirmIfafirmByid(curAdmin.getIfafirm().getId());
			String  langFlag = this.getLoginLangFlag(request);
			for (MemberIfafirmVO ifafirm : list) {
				String name = ifafirm.getCompanyName();
				if("en".equals(langFlag)){
					MemberIfafirmEn memberIfafirmEn = 
						(MemberIfafirmEn) ifafirmService.findCompanyNameById(langFlag,
							ifafirm.getId());
					if(null != memberIfafirmEn)
						ifafirm.setCompanyName(memberIfafirmEn.getCompanyName());
				}else if("tc".equals(langFlag)){
					MemberIfafirmTc memberIfafirmTc = 
						(MemberIfafirmTc) ifafirmService.findCompanyNameById(langFlag,
							ifafirm.getId());
					if(null != memberIfafirmTc)
						ifafirm.setCompanyName(memberIfafirmTc.getCompanyName());
				}else{
					MemberIfafirmSc memberIfafirmSc = 
						(MemberIfafirmSc) ifafirmService.findCompanyNameById(langFlag,
							ifafirm.getId());
					if(null != memberIfafirmSc)
						ifafirm.setCompanyName(memberIfafirmSc.getCompanyName());
				}
				if(StringUtils.isBlank(ifafirm.getCompanyName())){
					ifafirm.setCompanyName(name);
				}
			}
			map.put("ifafirmId", curAdmin.getIfafirm().getId());
			map.put("ifafirmJson", list);
		}else{
			map.put("result",false);
			map.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(map, response);
	}
	
	/**
	 * 增加公司关系（add childFirm）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/console/ifafirm/addChildFirm")
	public void addChildFirm(HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		String parentId = request.getParameter("parentId");	
		String childId = request.getParameter("childId");	
		boolean flag = false;
		flag = ifafirmService.saveChildFirm(parentId,childId);
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("parentId", parentId);
		map.put("childId", childId);
		JsonUtil.toWriter(map, response);
	}
	
	/**
	 * 移除公司关系（remove childFirm）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/console/ifafirm/removeChildFirm")
	public void removeChildFirm(HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		String parentId = request.getParameter("parentId");	
		String childId = request.getParameter("childId");	
		boolean flag = false;
		flag = ifafirmService.removeChildFirm(parentId,childId);
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("parentId", parentId);
		map.put("childId", childId);
		JsonUtil.toWriter(map, response);
	}
	
	/**
	 * 跳转组织架构
	 * @author wwluo
	 * @date 2016-08-25
	 */
	@RequestMapping(value = "/console/ifafirm/orgList")
	public String orgList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
//			String ifafirmId = request.getParameter("ifafirmId");	
			MemberIfafirm memberIfafirm = curAdmin.getIfafirm();//ifafirmService.findById(ifafirmId);
			if(null != memberIfafirm)
				model.put("companyName",memberIfafirm.getCompanyName());
			model.put("ifafirmId",curAdmin.getIfafirm().getId());
			return "console/ifafirm/firm_org";
		}else{
			return "redirect:"+this.getFullPath(request)+"index.do";
		}
	}
	
	/**
	 * 跳转代理商
	 * @author wwluo
	 * @date 2016-08-25
	 */
	@RequestMapping(value = "/console/ifafirm/distributorList")
	public String distributorList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
//			String ifafirmId = request.getParameter("ifafirmId");
			MemberIfafirm memberIfafirm = curAdmin.getIfafirm();//ifafirmService.findById(ifafirmId);
			if(null != memberIfafirm)
				model.put("companyName",memberIfafirm.getCompanyName());
			model.put("ifafirmId",curAdmin.getIfafirm().getId());
			return "console/ifafirm/firm_distributor";
		}else{
			return "redirect:"+this.getFullPath(request)+"index.do";
		}
	}
	
}


