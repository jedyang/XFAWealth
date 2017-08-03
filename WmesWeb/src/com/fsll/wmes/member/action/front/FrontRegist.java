/**
 * 
 */
package com.fsll.wmes.member.action.front;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.vo.CompanyInfoVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MemberInviteCode;
import com.fsll.wmes.entity.MemberValidateInfo;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberInviteCodeService;
import com.fsll.wmes.member.vo.InfoVO;

/**
 * @author scshi
 * 前台注册
 */
@Controller
@RequestMapping("/front/regist")
public class FrontRegist extends CoreBaseAct{
	
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private SysParamService paramService;	
	@Autowired
	private IfafirmManageService ifafirmService;
	@Autowired
    private CompanyInfoService companyInfoService;
	@Autowired
	private MemberInviteCodeService memberInviteCodeService;
	
	/**
	 * 注册页面
	 * 第一步 选择类型
	 * */
	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public String frontRegist(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		return "member/regist/register";
	}
	
	/**
	 * 注册页面
	 * 第二部 填写注册信息
	 * */
	@RequestMapping(value = "/information", method = RequestMethod.GET)
	public String frontInformation(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		//类型名称：Individual,Corporate,FI,IFA User,IFA Firm,Distributor
		String registerType = StrUtils.getString(request.getParameter("registerType"));
		if (registerType.isEmpty()) registerType = StrUtils.getString(request.getSession().getAttribute("registerType"));
		model.put("registerType", registerType);
		request.getSession().setAttribute("registerType", registerType);
		setUserType(request, model,registerType);
		String memberType = StrUtils.getString(model.get("memberType"));
		String subMemberType = StrUtils.getString(model.get("subMemberType"));

		Map<String ,Object> baseMap = new HashMap<String ,Object>();
		MemberBase memberBase = new MemberBase();
		memberBase.setMemberType(StrUtils.getInt(memberType));
		memberBase.setSubMemberType(StrUtils.getInt(subMemberType));

		//如果已填写账号信息，当从基础信息页面回到账号信息页面时，自动从会话中获取，并显示到页面。
		String userParam = StrUtils.getString(request.getSession().getAttribute("userparams"));
		model.put("userparams", userParam);
		if (!userParam.isEmpty()) {
			String []  userParamList = userParam.split("&");
			for(String baseData:userParamList){
				String[]  baseDataList  = baseData.split("=");
				baseMap.put(baseDataList[0], baseDataList.length<2?"":baseDataList[1]);
			}
			JSONObject accountDataJSON = (JSONObject)JSONSerializer.toJSON(baseMap);
			//-------------- 个人账号 ----------------
			String loginCode = accountDataJSON.getString("loginCode");
//			String nickName = memberDataJSON.optString("nickName","");
			String email = accountDataJSON.getString("email");
			String password = accountDataJSON.getString("password");
			String mobileCode = accountDataJSON.optString("mobileCode","");
			String mobileNumber = accountDataJSON.optString("mobileNumber","");
			String inviteCode = accountDataJSON.optString("inviteCode","");
			
			//账号信息
			memberBase.setId(null);
			memberBase.setLoginCode(loginCode);
			memberBase.setEmail(email);
			memberBase.setPassword(password);
			memberBase.setMobileCode(mobileCode);
			memberBase.setMobileNumber(mobileNumber);
			memberBase.setDefCurrency(CommonConstants.DEF_CURRENCY);
			memberBase.setDateFormat(CoreConstants.DATE_FORMAT);
			memberBase.setDefDisplayColor(CommonConstants.DEF_DISPLAY_COLOR);
			memberBase.setLangCode(accountDataJSON.optString("langCode",this.getLoginLangFlag(request)));
			memberBase.setIconUrl(CommonConstantsWeb.DEFAULT_HEADER);//因为注册时没有设置头像，保存是设置为默认头像
			if(StringUtils.isNotBlank(inviteCode)){
				memberBase.setInviteCode(inviteCode);
			}
		}
		model.put("memberBase", memberBase);
		return "member/regist/information";
	}
	
	/**
	 * 在会话中记录用户类型信息
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	private void setUserType(HttpServletRequest request, ModelMap model,String registerType){
		//类型名称：Individual,Corporate,FI,IFA User,IFA Firm,Distributor
//		String registerType = StrUtils.getString(request.getParameter("registerType"));
		if (registerType.isEmpty()) registerType = StrUtils.getString(request.getSession().getAttribute("registerType"));
		model.put("registerType", registerType);

		String memberType = "";
		String subMemberType = "";
		if ("Individual".equals(registerType)){
			memberType = "1";
			subMemberType = "11";
		}else if ("IFA User".equals(registerType)){
			memberType = "2";
			subMemberType = "21";
		}else if ("Corporate".equals(registerType)){
			memberType = "1";
			subMemberType = "12";
		}else if ("FI".equals(registerType)){
			memberType = "1";
			subMemberType = "13";
		}else if ("IFA Firm".equals(registerType)){
			memberType = "2";
			subMemberType = "22";
		}else if ("Distributor".equals(registerType)){
			memberType = "3";
			subMemberType = "31";
		}
		model.put("memberType", memberType);
		model.put("subMemberType", subMemberType);
	}
	/**
	 * 注册信息提交
	 * */
	@RequestMapping(value = "/informationSave", method = RequestMethod.POST)
	public void informationSave(HttpServletRequest request,HttpServletResponse response,ModelMap model){
//		setUserType(request, model, "");
		//String userparams = request.getParameter("Userparams"); 
		String userparams = StringEscapeUtils.unescapeHtml(request.getParameter("Userparams"));
		request.getSession().setAttribute("userparams", userparams);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
	}
	
	
	
	/**
	 * 注册页面
	 * 第三步 填写个人能信息
	 * */
	@RequestMapping(value = "/baseInfo", method = RequestMethod.GET)
	public String frontBaseInfo(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		//类型名称：Individual,Corporate,FI,IFA User,IFA Firm,Distributor
		setUserType(request, model, "");
		
		String userParam = StrUtils.getString(request.getSession().getAttribute("userparams"));
		model.put("userparams", userParam);
		return "member/regist/baseinfo";
	}
	
	/**
	 * 第三部个人信息保存
	 * 
	 * */
	@RequestMapping(value="/baseInfoSave",method=RequestMethod.POST)
	public void baseInfoSave(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		setUserType(request, model, "");
		String memberType = StrUtils.getString(model.get("memberType"));
		String subMemberType = StrUtils.getString(model.get("subMemberType"));
//		String memberType = request.getParameter("memberType");
//		String subMemberType = request.getParameter("subdivided");
		
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		String langCode = this.getLoginLangFlag(request);
		String userParam = StrUtils.getString(request.getSession().getAttribute("userparams"));
		model.put("userparams", userParam);
//		String inputData = request.getParameter("inputData");
		String inputData = StringEscapeUtils.unescapeHtml(request.getParameter("inputData"));
		Map<String ,Object> baseMap = new HashMap<String ,Object>();
		baseMap.put("memberType", memberType);//0平台级用户,1-投资人，2-IFA,3-代理商
		baseMap.put("subMemberType", subMemberType);//11:独立投资账户;12:公司投资账户;13:FI账户;21:IFA个人,22:ifafirm;31:distributor',
		String memberId="";
		
		//loginID=admin234&password=123123&email=12311@qq.com
		
		if("22".equals(subMemberType)|| "31".equals(subMemberType)){
			String []  userParamList = inputData.split("&");
			for(String baseData:userParamList){
				String[]  baseDataList  = baseData.split("=");
				baseMap.put(baseDataList[0], baseDataList.length<2?"":baseDataList[1]);
			}
			JSONObject accountDataJSON = (JSONObject)JSONSerializer.toJSON(baseMap);
			memberId = memberBaseService.createMember(accountDataJSON, null, langCode);
		}else{
			if (null!=userParam) {
				String []  userParamList = userParam.split("&");
				for(String baseData:userParamList){
					String[]  baseDataList  = baseData.split("=");
					baseMap.put(baseDataList[0], baseDataList.length<2?"":baseDataList[1]);
	//				if("password".equals(baseDataList[0])){
	//					baseMap.put("password", this.pwdEncoder.encodePassword(baseDataList[1]));
	//				}
					
				}
				JSONObject accountDataJSON = (JSONObject)JSONSerializer.toJSON(baseMap);
				
				String[] inputDataList = inputData.split("&");
				Map<String,Object> memberMap = new HashMap<String,Object>();
				for(String memberStr:inputDataList){
					String[] memberStrArray = memberStr.split("=");
					memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
				}
				JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
				//存储数据
				memberId = memberBaseService.createMember(accountDataJSON, memberDataJSON, langCode);
			}else{
				obj.put("result",false);
				obj.put("message",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed.save",null));
				model.put("result",false);
				model.put("message",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed.save",null));
			}
		}
		
		if (!memberId.isEmpty()){
			String companyId = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_COMPANY_ID));
			//如果运营公司会话为空，则取默认公司id
			if (null==companyId || companyId.isEmpty()){
				String companyCode = PropertyUtils.getConfPropertyValue(CoreConstants.DEFAULT_COMPANY);
				CompanyInfoVO companyInfo = companyInfoService.findVoByCode(companyCode);
				companyId = companyInfo.getId();
			}
			CompanyInfo company = new CompanyInfo();
			company.setId(companyId);
			MemberBase member = new MemberBase();
			member.setId(memberId);
			MemberCompany memberCompany = new MemberCompany();
			memberCompany.setCompany(company);
			memberCompany.setMember(member);
			memberBaseService.saveMemberCompany(memberCompany);
			result = true;

			//清空会话
			request.getSession().setAttribute("Userparams", "");
			request.getSession().setAttribute("userparams", "");
			
			//发送激活邮件
			String activeCode = StrUtils.getRandomString(6);//获取6位随机码
			MemberBase memBase = memberBaseService.findById(memberId);
			if (null!=memBase && null!=memBase.getEmail()){
				String email = memBase.getEmail();
				String regPath = request.getRequestURL().toString();//获取地址
				String changePath = regPath.replace("/baseInfoSave", "/memActive");//拼接跳转链接
				String backPath = changePath+"?memId="+memberId+"&valiCode="+activeCode;
				ResultWS sendResult = sendRegEmail(member.getLoginCode(),email,activeCode,backPath);
				if(WSConstants.FAIL.equals(sendResult.getRet())){
					model.put("result",false);
					this.saveOperLog(request,email,"",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "发送临时密码失败");
				}else{
					model.put("result",true);
					this.saveOperLog(request,email,"",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "发送临时密码成功");
				}
			}
			model.put("activeCode", activeCode);
			model.put("memBase", memBase);	
//			String message = memberBaseService.getCompletedMessage(memberId,email,langCode);
//			model.put("message", message);
		}
		
		obj.put("result",result);
		obj.put("memberId",memberId);
		obj.put("userParam",userParam);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 第四步，注冊完成
	 * */
	@RequestMapping(value = "/complete", method = RequestMethod.GET)
	public String regComplete(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String langCode = this.getLoginLangFlag(request);
		String memberId = request.getParameter("memberId");
		MemberBase memBase = memberBaseService.findById(memberId);
		if(null!=memBase && !"".equals(memBase)){
			if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==memBase.getSubMemberType()){
				MemberIfa ifa=memberBaseService.findIfaMember(memberId);
				model.put("ifa", ifa);
			}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==memBase.getSubMemberType()) {
				MemberIndividual individual=memberBaseService.findIndividualMember(memberId);
				model.put("individual", individual);
			}
			String email = memBase.getEmail();
			//发送激活邮件
			/*String activeCode = StrUtils.getRandomString(6);//获取6位随机码
			String regPath = request.getRequestURL().toString();//获取地址
			String changePath = regPath.replace("/complete", "/memActive");//拼接跳转链接
			String backPath = changePath+"?memId="+memberId+"&valiCode="+activeCode;
			ResultWS sendResult = sendRegEmail(memBase.getLoginCode(),email,activeCode,backPath);
			if(WSConstants.FAIL.equals(sendResult.getRet())){
				model.put("result",false);
				this.saveOperLog(request,email,"",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "发送临时密码失败");
			}else{
				model.put("result",true);
				this.saveOperLog(request,email,"",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "发送临时密码成功");
			}*/
			//查询校验码
			List<MemberValidateInfo> validateList = memberBaseService.findMemberValidateInfo(memberId, email, 1);
			if(!validateList.isEmpty()){
				MemberValidateInfo validate = validateList.get(0);
				String activeCode = validate.getValidateCode();
				model.put("activeCode", activeCode);
			}
			model.put("memBase", memBase);	
			
			String message = memberBaseService.getCompletedMessage(memberId,email,langCode);
			model.put("message", message);
		}
		return "member/regist/completed";
	}
	
	/**
	 * @author scshi
	 * @date 20160627
	 * 3.4.3 发送激活邮件接口
	 * @param loginCode 登录账号
	 * @param email	邮件接受地址
	 * @param activeCode 激活码，=Encrypt.md5(email+pwd+time)
	 * @return 结果代码
	 * */
	private ResultWS sendRegEmail(String loginCode, String email, String activeCode, String path) {
		return memberBaseService.saveAndSendValidateEmail(loginCode, email, 1, activeCode, path);
	}
	
	/***
	 * 用户激活
	 * */
	@RequestMapping(value = "/memActive", method = RequestMethod.GET)
	public String memActive(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String memberId = request.getParameter("memId");
		String valiCode = request.getParameter("valiCode");
		boolean flag = memberBaseService.updateMemberActive(memberId,valiCode);
		
		//if(flag)return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		if(flag)return "member/regist/registrationSuccessful";
		return null;
	}
	
	/**
	 * 异步获取国家列表
	 * @return List<SysCountry>
	 * */
	@RequestMapping(value="/countryList",method = RequestMethod.POST)
	public void countryList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		List<SysCountry> countryList = memberBaseService.loadSysCountryList();
		String countryJson = JsonUtil.toJson(countryList);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("countryJson",countryJson);
		JsonUtil.toWriter(obj, response);
		//ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 异步获取国家列表
	 * @param keyword
	 * @return List<GeneralDataVO>
	 * */
	@RequestMapping(value="/countryListSearch",method = RequestMethod.POST)
	public void countryListSearch(HttpServletRequest request,HttpServletResponse response,ModelMap model, String keyword){
		List<GeneralDataVO> countryList = memberBaseService.findCountryList(keyword, this.getLoginLangFlag(request));
		String countryJson = JsonUtil.toJson(countryList);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("countryJson",countryJson);
		JsonUtil.toWriter(obj, response);
		//ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 异步获取职业列表
	 * */
	@RequestMapping(value="/occupationList",method=RequestMethod.POST)
	public void occupationList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String typeCode = request.getParameter("category");
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(1);
		jsonPaging.setRows(1000);
		jsonPaging.setSort("orderBy");
		jsonPaging.setOrder("asc");
		SysParamConfig paramConfig=new SysParamConfig();
		paramConfig.setTypeCode(typeCode);
		jsonPaging = paramService.findAll(jsonPaging,paramConfig);
		String occupationJSON = JsonUtil.toJson(jsonPaging);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("occupationJSON",occupationJSON);
		JsonUtil.toWriter(obj, response);
	}
	/**
	 *验证唯一性
	 * */
	@RequestMapping(value = "/checkUnique", method = RequestMethod.POST)
	public void checkUnique(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String keyValue = request.getParameter("keyValue");
		String keyName = request.getParameter("keyName");
		String items = "{'"+keyName+"':'"+keyValue+"'}";
		InfoVO result = memberBaseService.checkDuplicate(items);
		boolean flag = false;
		if("0".equals(result.getDuplicate())){
			flag = true;
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",flag);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
	}
	
	
	/**
	 *验证唯一性
	 * */
	@RequestMapping(value = "/checkUniqueAjax", method = RequestMethod.GET)
	public void checkUniqueAjax(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String keyValue = request.getParameter("fieldValue");
		String keyName = request.getParameter("fieldId");
		String items = "{'"+keyName+"':'"+keyValue+"'}";
		InfoVO result = memberBaseService.checkDuplicate(items);
		boolean flag = false;
		if("0".equals(result.getDuplicate())){
			flag = true;
		}
		Object[] obj = new Object[]{keyName,flag};
		JsonUtil.toWriter(obj, response);
	}

	/**
	 *验证手机（+区号）唯一性
	 * */
	@RequestMapping(value = "/checkCodeMobileUnique", method = RequestMethod.POST)
	public void checkCodeMobileUnique(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		String mobileCode = request.getParameter("mobileCode");
		String mobileNumber = request.getParameter("mobileNumber");
		List<MemberBase> memberList = memberBaseService.findByMobileCodeAndNumber(mobileCode, mobileNumber);
		boolean flag = false;
		if(memberList.isEmpty()){
			flag = true;
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		} else {
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
		}
		obj.put("result",flag);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 公司注册信息唯一验证
	 * */
	@RequestMapping(value = "/checkCompanyUnique", method = RequestMethod.POST)
	public void checkCompanyUnique(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String keyName = request.getParameter("keyName");
		String keyValue = request.getParameter("keyValue");
//		String itemsJSON = request.getParameter("inputData");
		String itemsJSON  = "{'"+keyName+"':'"+keyValue+"'}";
		InfoVO result = memberBaseService.checkCompanyUnique(itemsJSON);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean flag = false;
		if("0".equals(result.getDuplicate())){
			flag = true;
		}
		obj.put("result",flag);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 公司手机唯一验证
	 * */
	@RequestMapping(value = "/checkMobileUnique", method = RequestMethod.POST)
	public void checkMobileUnique(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String mobileCode = request.getParameter("mobileCode");
		String mobileNumber = request.getParameter("mobileNumber");
		InfoVO result = memberBaseService.checkMobileUnique(mobileCode,mobileNumber);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean flag = false;
		if("0".equals(result.getDuplicate())){
			flag = true;
		}
		obj.put("result",flag);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 基本信息护照/身份证唯一验证
	 * @param tableName  
	 * @param idCard 
	 * @param certType 
	 * */
	@RequestMapping(value = "/certNoUnique", method = RequestMethod.GET)
	public void certNoUnique(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String tableName = request.getParameter("tableName");
		String idCard = request.getParameter("idCard");
		String certType = request.getParameter("certType");
		boolean flag = false;
		boolean unique = memberBaseService.checkCertNoUnique(tableName,idCard,certType);
		flag = unique;
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",flag);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 邀请码验证
	 * @author Yan
	 * */
	@RequestMapping(value = "/checkinviteCode", method = RequestMethod.POST)
	public void checkinviteCode(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		boolean flag = false;
		String msg = "global.failed";
		String email = request.getParameter("email");
		String inviteCode = request.getParameter("inviteCode");
		MemberInviteCode code = memberInviteCodeService.checkInviteCode(inviteCode, email);
		if(null!=code){
			Date nowDate = new Date();
			Date expireDate = code.getExpireTime();
			if(expireDate.getTime()>nowDate.getTime()){
				flag = true;
				msg = "global.success";
			}
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",flag);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),msg,null));
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 验证码验证
	 * 
	 * */
	@RequestMapping(value = "/checkValicode", method = RequestMethod.GET)
	public void checkValicode(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String randomCode = (String) request.getSession().getAttribute("randStr");
		String valiCode = request.getParameter("valiCode");
		boolean flag = checkCode(randomCode,valiCode);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",flag);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
	}
		
	
	 /*
     * 检测验证码输入是否成功 parement String,String 接收参数：生成验证码，输入验证码
     * @Return boolean false--验证失败 true---验证成功   
     */
    private boolean checkCode(String randomCode, String validate) {
        boolean blnFlag = false;
        if (validate != null && !"".equals(validate) && randomCode != null && !"".equals(randomCode) && validate.equals(randomCode)) {
            blnFlag = true;
        }
        return blnFlag;
    }
    
    /**
	 * 协议页面跳转
	 * */
	@RequestMapping(value = "/registrationProtocol", method = RequestMethod.GET)
	public String registrationProtocol(HttpServletRequest request,HttpServletResponse response,ModelMap model){	
		return "member/regist/registrationProtocol";
	}
	
	/**
	 * 异步获取ifa firm列表
	 * */
	@RequestMapping(value="/firmList",method=RequestMethod.POST)
	public void firmList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		jsonPaging.setRows(999);
		jsonPaging=ifafirmService.getIfaFirmJson(jsonPaging ,null ,null ,null, this.getLoginLangFlag(request));
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("firmList", jsonPaging.getList());
		JsonUtil.toWriter(obj, response);
	}
}
