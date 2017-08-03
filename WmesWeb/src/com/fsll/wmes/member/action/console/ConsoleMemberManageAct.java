package com.fsll.wmes.member.action.console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysCountryService;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberFi;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.CoporateVO;
import com.fsll.wmes.member.vo.FiVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.member.vo.IndividualVO;
import com.fsll.wmes.member.vo.MemberAdminVO;
import com.fsll.wmes.member.vo.MemberSsoVO;

/*****
 * 成员管理
 * @author mqzou 2016-06-30
 */
@Controller
public class ConsoleMemberManageAct extends CoreBaseAct {
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private MemberManageServiceForConsole memberManageService;
	@Autowired
	private SysParamService paramService;
	@Autowired
	private DistributorService distributorService;
	@Autowired
	private IfafirmManageService ifafirmService;
	@Autowired
	private SysCountryService sysCountryService;
	@Autowired
	private IfaManageService ifaManageService;

	/**
	 * Individual分页列表
	 * @author mqzou
	 * @date 2016-06-28
	 */
	@RequestMapping(value = "/console/member/list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberSsoVO curMember = getLoginUserSSO(request);
		boolean editable=false;
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM==curMember.getMemberType()){
			editable=true;
		}
		this.isMobileDevice(request, model);
		model.put("editable", editable);
		return "console/member/list";
	}
	
	/**
	 * Individual 数据查询的方法
	 * @author mqzou
	 * @date 2016-06-29
	 */
	@RequestMapping(value = "/console/member/individual/listJson", method = RequestMethod.POST)
	public void listJsonIndividual(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String name = request.getParameter("keyword");
		String langCode = this.getLoginLangFlag(request);
		MemberSsoVO curMember = getLoginUserSSO(request);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR==curMember.getMemberType()){
			jsonPaging=new JsonPaging();
		}else {
			MemberIndividual voIndividual = new MemberIndividual();
			if (null != name && !"".equals(name)) {
				try {
					name = URLDecoder.decode(name, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			MemberBase memberBase = new MemberBase();
			memberBase.setLoginCode(name);
			memberBase.setMemberType(curMember.getMemberType());
			memberBase.setId(curMember.getId());
			voIndividual.setMember(memberBase);
			jsonPaging = this.getJsonPaging(request);
			jsonPaging = memberManageService.findAllIndividual(jsonPaging, voIndividual, langCode);
		}
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * admin分页列表
	 * 
	 * @author mqzou
	 * @date 2016-07-21
	 */
	@RequestMapping(value = "/console/member/admin/list")
	public String adminList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		model.put("curMember", curMember);
		return "console/member/admin/list";
	}
	

	/**
	 * Individual新增
	 * 
	 * @author mqzou
	 * @date 2016-06-28
	 */
	@RequestMapping(value = "/console/member/individual/add")
	public String individualAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// this.isMobileDevice(request,model);
		IndividualVO vo = new IndividualVO();
		model.put("title", "新增Individual");
		model.put("individualvo", vo);
		return "console/member/individual_input";
	}

	/**
	 * Individual明细(修改)
	 * 
	 * @author mqzou
	 * @date 2016-06-28
	 */
	@RequestMapping(value = "/console/member/individual/detail")
	public String individualDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		MemberIndividual obj = memberManageService.findIndividualById(id);
		IndividualVO vo = new IndividualVO();
		vo.setId(obj.getId());
		vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
		vo.setAddress(obj.getAddress());
		vo.setBorn(obj.getBorn());
		vo.setCertNo(obj.getCertNo());
		vo.setCertType(obj.getCertType());
		vo.setCountry(obj.getCountry() == null ? "" : obj.getCountry().getId());
		vo.setEducation(obj.getEducation());
		vo.setEmail(obj.getMember().getEmail());
		vo.setEmployment(obj.getEmployment());
		vo.setFirstName(obj.getFirstName());
		vo.setGender(obj.getGender());
		vo.setLastName(obj.getLastName());
		vo.setLoginCode(obj.getMember().getLoginCode());
		vo.setMobileNumber(obj.getMember().getMobileNumber());
		vo.setNameChn(obj.getNameChn());
		vo.setNationality(obj.getNationality() == null ? "" : obj.getNationality().getId());
		vo.setNickName(obj.getMember().getNickName());
		vo.setOccupation(obj.getOccupation());
		vo.setPassword(obj.getMember().getPassword());
		vo.setTelephone(obj.getTelephone());
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		boolean editable=false;
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM==curMember.getMemberType()){
			editable=true;
		}
		model.put("individualvo", vo);
		model.put("title", "Individual详细信息");
		model.put("editable", editable);
		return "console/member/individual_input";
	}

	/**
	 * 保存Individual
	 * 
	 * @author mqzou
	 * @date 2016-06-29
	 */
	@RequestMapping(value = "/console/member/individual/save", method = RequestMethod.POST)
	public void saveIndividual(HttpServletRequest request, HttpServletResponse response, ModelMap model, IndividualVO individualVo) {
		// individualVo.setMember(memberBase);
		MemberIndividual individualUpdate = null;
		MemberBase memberUpdate = null;
		String postData=request.getParameter("postData");
		String[] inputDataList = postData.split("&");
		Map<String,Object> memberMap = new HashMap<String,Object>();
		for(String memberStr:inputDataList){
			String[] memberStrArray = memberStr.split("=");
			memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
		}
		JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
		individualVo.setId(memberDataJSON.getString("id"));
		individualVo.setAddress(memberDataJSON.getString("address"));
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		try {
			individualVo.setBorn(sdf.parse(memberDataJSON.getString("born")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		individualVo.setCertNo(memberDataJSON.getString("certNo"));
		individualVo.setCertType(memberDataJSON.containsKey("certType")? memberDataJSON.getString("certType"):"");
		individualVo.setCountry(memberDataJSON.getString("country"));
		individualVo.setEducation(memberDataJSON.getString("education"));
		individualVo.setEmail(memberDataJSON.getString("email"));
		individualVo.setEmployment(memberDataJSON.getString("employment"));
		individualVo.setFirstName(memberDataJSON.getString("firstName"));
		individualVo.setGender(memberDataJSON.getString("gender"));
		individualVo.setLastName(memberDataJSON.getString("lastName"));
		individualVo.setLoginCode(memberDataJSON.getString("loginCode"));
		individualVo.setMobileNumber(memberDataJSON.getString("mobileNumber"));
		individualVo.setNameChn(memberDataJSON.getString("nameChn"));
		individualVo.setNationality(memberDataJSON.getString("nationality"));
		individualVo.setNickName(memberDataJSON.getString("nickName"));
		individualVo.setOccupation(memberDataJSON.getString("occupation"));
		individualVo.setTelephone(memberDataJSON.getString("telephone"));
		
		if ("" != individualVo.getId() && null != individualVo.getId() && !"".equals(individualVo.getId())) {
			individualUpdate = memberManageService.findIndividualById(individualVo.getId());
			memberUpdate = individualUpdate.getMember();
		}
		// if(null!=individualUpdate){
		individualUpdate.setFirstName(individualVo.getFirstName());
		individualUpdate.setLastName(individualVo.getLastName());
		individualUpdate.setBorn(individualVo.getBorn());
		individualUpdate.setCertNo(individualVo.getCertNo());
		individualUpdate.setCertType(individualVo.getCertType());

		if ("" != individualVo.getCountry() && null != individualVo.getCountry() && !"".equals(individualVo.getCountry())) {
			SysCountry country = memberManageService.findCountryById(individualVo.getCountry());
			individualUpdate.setCountry(country);
		}

		individualUpdate.setEducation(individualVo.getEducation());
		individualUpdate.setEmployment(individualVo.getEmployment());
		individualUpdate.setGender(individualVo.getGender());
		individualUpdate.setNameChn(individualVo.getNameChn());
		individualUpdate.setLastUpdate(new Date());
		if ("" != individualVo.getNationality() && null != individualVo.getNationality() && !"".equals(individualVo.getNationality())) {
			SysCountry country = memberManageService.findCountryById(individualVo.getNationality());
			individualUpdate.setNationality(country);
		}
		//individualUpdate.setNickName(individualVo.getNickName());
		individualUpdate.setOccupation(individualVo.getOccupation());
		individualUpdate.setTelephone(individualVo.getTelephone());
		individualUpdate.setAddress(individualVo.getAddress());

		memberUpdate.setLoginCode(individualVo.getLoginCode());
		memberUpdate.setNickName(individualVo.getNickName());
		memberUpdate.setEmail(individualVo.getEmail());
		memberUpdate.setPassword(individualVo.getPassword());
		memberUpdate.setMobileNumber(individualVo.getMobileNumber());
		memberUpdate.setMemberType(1);// 投资人
		memberUpdate.setSubMemberType(11);// 独立投资账户
		memberUpdate.setLastUpdate(new Date());
		/*
		 * }else { memberUpdate=memberBase; individualUpdate=individualVo; }
		 */
		MemberIndividual voIndividual = memberManageService.saveIndividual(individualUpdate, memberUpdate);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != voIndividual) {
			obj.put("result", true);
			obj.put("message", "保存成功");
		} else {
			obj.put("result", false);
			obj.put("message", "保存失败");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 异步获取国家列表
	 * */
	@RequestMapping(value = "/console/countryList", method = RequestMethod.POST)
	public void countryList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		List<GeneralDataVO> countryList = memberBaseService.findCountryList("", langCode);
		String countryJson = JsonUtil.toJson(countryList);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("countryJson", countryJson);
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 异步获取职业列表
	 * */
	@RequestMapping(value = "/console/occupationList", method = RequestMethod.POST)
	public void occupationList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(1);
		jsonPaging.setRows(1000);
		jsonPaging.setSort("orderBy");
		jsonPaging.setOrder("asc");
		SysParamConfig paramConfig = new SysParamConfig();
		paramConfig.setTypeCode("occupation");
		jsonPaging = paramService.findAll(jsonPaging, paramConfig);
		String occupationJSON = JsonUtil.toJson(jsonPaging);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("occupationJSON", occupationJSON);
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 异步获取教育程度列表
	 * */
	@RequestMapping(value = "/console/educationList", method = RequestMethod.POST)
	public void educationList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(1);
		jsonPaging.setRows(1000);
		jsonPaging.setSort("orderBy");
		jsonPaging.setOrder("asc");
		SysParamConfig paramConfig = new SysParamConfig();
		paramConfig.setTypeCode("education");
		jsonPaging = paramService.findAll(jsonPaging, paramConfig);
		String occupationJSON = JsonUtil.toJson(jsonPaging);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("educationJSON", occupationJSON);
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 异步获取就业情况列表
	 * */
	@RequestMapping(value = "/console/employmentList", method = RequestMethod.POST)
	public void employmentList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(1);
		jsonPaging.setRows(1000);
		jsonPaging.setSort("orderBy");
		jsonPaging.setOrder("asc");
		SysParamConfig paramConfig = new SysParamConfig();
		paramConfig.setTypeCode("employment");
		jsonPaging = paramService.findAll(jsonPaging, paramConfig);
		String occupationJSON = JsonUtil.toJson(jsonPaging);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("employmentJSON", occupationJSON);
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * ifa数据查询的方法
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 */
	@RequestMapping(value = "/console/member/ifa/listJson", method = RequestMethod.POST)
	public void listJsonIfa(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String name = request.getParameter("keyword");
		String langCode = this.getLoginLangFlag(request);
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR==curMember.getMemberType()){
			jsonPaging=new JsonPaging();
		}else {
			MemberIfa voIfa = new MemberIfa();
			MemberAdmin admin=memberManageService.findAdminByMemberId(curMember.getId());
			if (null != name && !"".equals(name)) {
				try {
					name = URLDecoder.decode(name, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			MemberIfafirm ifaFirm = admin==null?null:admin.getIfafirm();
			MemberBase memberBase = new MemberBase();
			memberBase.setLoginCode(name);
			memberBase.setMemberType(curMember.getMemberType());
			memberBase.setId(curMember.getId());
			voIfa.setIfafirm(ifaFirm);
			voIfa.setMember(memberBase);
			jsonPaging = this.getJsonPaging(request);
			jsonPaging = memberManageService.findAllIfa(jsonPaging, voIfa, langCode);
		}
		

		
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * ifa_ifafirm数据查询的方法
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 */
	@RequestMapping(value = "/console/member/ifa_ifafirm/listJson", method = RequestMethod.POST)
	public void listJsonIfaInfirm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		String name = request.getParameter("keyword");
		MemberIfa voIfa = new MemberIfa();
		if (null != name && !"".equals(name)) {
			try {
				name = URLDecoder.decode(name, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		MemberBase memberBase = new MemberBase();
		memberBase.setLoginCode(name);
		voIfa.setMember(memberBase);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = memberManageService.findAllIfainIfaFirm(jsonPaging, voIfa, langCode);

		
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * ifa新增
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 */
	@RequestMapping(value = "/console/member/ifa/add")
	public String ifaAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// this.isMobileDevice(request,model);
		IfaVO vo = new IfaVO();
		model.put("title", "新增IFA");
		model.put("ifavo", vo);
		return "console/member/ifa_input";
	}

	/**
	 * ifa明细(修改)
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 */
	@RequestMapping(value = "/console/member/ifa/detail")
	public String ifaDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		MemberIfa obj = memberManageService.finIfaById(id);
		IfaVO vo = new IfaVO();
		vo.setId(obj.getId());
		vo.setAddress(obj.getAddress());
		vo.setBorn(DateUtil.getDateStr(obj.getBorn()));
		vo.setCertNo(obj.getCertNo());
		vo.setCertType(obj.getCertType());
		vo.setCountry(obj.getCountry() == null ? "" : obj.getCountry().getId());
		vo.setEducation(obj.getEducation());
		vo.setEmail(obj.getMember().getEmail());
		vo.setEmployment(obj.getEmployment());
		vo.setFirstName(obj.getFirstName());
		vo.setGender(obj.getGender());
		vo.setLastName(obj.getLastName());
		vo.setLoginCode(obj.getMember().getLoginCode());
		vo.setMobileNumber(obj.getMember().getMobileNumber());
		vo.setNameChn(obj.getNameChn());
		vo.setNationality(obj.getNationality() == null ? "" : obj.getNationality().getId());
		vo.setNickName(obj.getMember().getNickName());
		vo.setOccupation(obj.getOccupation());
		vo.setPassword(obj.getMember().getPassword());
		vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
		vo.setTelephone(obj.getTelephone());
		vo.setAppellation(obj.getAppellation());
		vo.setCeNumber(obj.getCeNumber());
		vo.setCfaNumber(obj.getCfaNumber());
		vo.setCfpNumber(obj.getCfpNumber());
		vo.setCompanyIfafirmId(obj.getIfafirm() == null ? "" : obj.getIfafirm().getId());
		vo.setCompanyType(obj.getCompanyType());
		vo.setIntroduction(obj.getIntroduction());
		vo.setPosition(obj.getPosition());
		vo.setPrimaryResidence(obj.getPrimaryResidence() == null ? "" : obj.getPrimaryResidence().getId());
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		boolean editable=false;
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM==curMember.getMemberType()){
			editable=true;
		}
		model.put("editable", editable);
		model.put("ifavo", vo);
		model.put("title", "IFA详细信息");
		return "console/member/ifa_input";
	}

	/**
	 * 保存Ifa
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/ifa/save", method = RequestMethod.POST)
	public String saveIfa(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String jsonData=request.getParameter("jsonData");
			if(StringUtils.isBlank(jsonData)){
				return null;
			}
			IfaVO ifaVO = (IfaVO) JSONObject.toBean(JSONObject.fromObject(jsonData), IfaVO.class);
			MemberAdmin curAdmin = getLoginMemberAdmin(request);
			MemberIfa ifaUpdate = null;
			MemberBase memberUpdate = null;
			if (StringUtils.isNotBlank(ifaVO.getId())) {
				ifaUpdate = memberManageService.finIfaById(ifaVO.getId());
				memberUpdate = ifaUpdate.getMember();
			} else {
				ifaUpdate = new MemberIfa();
				memberUpdate = new MemberBase();
				//IFA Firm管理员新增ifa，自动通过审批
				ifaUpdate.setIfafirm(curAdmin.getIfafirm());
				ifaUpdate.setCompanyType("2");
				ifaUpdate.setIfafirmStatus("1");
				memberUpdate.setMemberType(2);
				memberUpdate.setSubMemberType(22);
			}
			ifaUpdate.setFirstName(ifaVO.getFirstName());
			ifaUpdate.setLastName(ifaVO.getLastName());
			ifaUpdate.setBorn(DateUtil.getDate(ifaVO.getBorn()));
			ifaUpdate.setCertNo(ifaVO.getCertNo());
			ifaUpdate.setCertType(ifaVO.getCertType());
			if (StringUtils.isNoneBlank(ifaVO.getCountry())) {
				SysCountry country = sysCountryService.findBycountryId(ifaVO.getCountry());
				ifaUpdate.setCountry(country);
			}
			if (StringUtils.isNoneBlank(ifaVO.getNationality())) {
				SysCountry country = sysCountryService.findBycountryId(ifaVO.getNationality());
				ifaUpdate.setNationality(country);
			} 
			if (StringUtils.isNoneBlank(ifaVO.getPrimaryResidence())) {
				SysCountry country = sysCountryService.findBycountryId(ifaVO.getPrimaryResidence());
				ifaUpdate.setPrimaryResidence(country);
			} 
			ifaUpdate.setEducation(ifaVO.getEducation());
			ifaUpdate.setEmployment(ifaVO.getEmployment());
			ifaUpdate.setGender(ifaVO.getGender());
			ifaUpdate.setNameChn(ifaVO.getNameChn());
			ifaUpdate.setLastUpdate(new Date());
			
			ifaUpdate.setOccupation(ifaVO.getOccupation());
			ifaUpdate.setTelephone(ifaVO.getTelephone());
			ifaUpdate.setAddress(ifaVO.getAddress());
			ifaUpdate.setCeNumber(ifaVO.getCeNumber());
			ifaUpdate.setAppellation(ifaVO.getAppellation());
			ifaUpdate.setCfaNumber(ifaVO.getCfaNumber());
			ifaUpdate.setCfpNumber(ifaVO.getCfpNumber());
			ifaUpdate.setCompanyType(ifaVO.getCompanyType());
			
			ifaUpdate.setIntroduction(ifaVO.getIntroduction());
			ifaUpdate.setPosition(ifaVO.getPosition());		

			memberUpdate.setLoginCode(ifaVO.getLoginCode());
			memberUpdate.setNickName(ifaVO.getNickName());
			memberUpdate.setEmail(ifaVO.getEmail());
			memberUpdate.setPassword(ifaVO.getPassword());
			memberUpdate.setMobileNumber(ifaVO.getMobileNumber());
			memberUpdate.setLastUpdate(new Date());
			memberUpdate.setIsValid("1");
			memberUpdate.setPassword(pwdEncoder.encodePassword(ifaVO.getPassword()));
			
			
			ifaUpdate.setMember(memberUpdate);
			MemberIfa memberIfa = memberManageService.saveIfa(ifaUpdate);
			if(memberIfa != null && curAdmin.getIfafirm() != null){
				//保存ifa与ifaFrim关联关系
				MemberIfaIfafirm releInfo = ifaManageService.getIfaIfaFirmByIFrimId(ifaVO.getId(), ifaVO.getIfafirmId());
				if(releInfo == null){
					releInfo = new MemberIfaIfafirm();
					releInfo.setCreateTime(new Date());
					releInfo.setLastUpdate(new Date());
					releInfo.setCheckStatus("1");
					releInfo.setIsValid("1");
				}
				releInfo.setIfa(memberIfa);
				releInfo.setIfafirm(curAdmin.getIfafirm());
				ifaManageService.saveOrUpdateIfaIfafirm(releInfo);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			if(result){
				obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
			}else{
				obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
			}
			JsonUtil.toWriter(obj, response);
		}
		return null;
	}

	/**
	 * ifa_ifafirm数据查询的方法
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 */
	@RequestMapping(value = "/console/member/ifafirm/listJson", method = RequestMethod.POST)
	public void listJsonIfafirm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		List list = memberManageService.getIfafirmlist();
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("ifafirmJson", list);
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 检查登录帐号的唯一性
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 */
	@RequestMapping(value = "/console/member/checkCodeUnique", method = RequestMethod.POST)
	public void checkCodeUnique(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String loginCode = request.getParameter("loginCode");
		String userId = request.getParameter("userId");
		/*MemberBase memberBase = memberBaseService.checkCodeUnique(loginCode, userId);
		
		if (memberBase == null) {
			JsonUtil.toWriter(true, response);
		} else {
			JsonUtil.toWriter(false, response);
		}*/
		
		boolean result=memberManageService.checkLoginCode(loginCode, userId);
		JsonUtil.toWriter(result, response);
	}

	/**
	 * fi数据查询的方法
	 * 
	 * @author mqzou
	 * @date 2016-07-06
	 */
	@RequestMapping(value = "/console/member/fi/listJson", method = RequestMethod.POST)
	public void listJsonFi(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String name = request.getParameter("keyword");
		String langCode = this.getLoginLangFlag(request);
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR==curMember.getMemberType()){
			jsonPaging=new JsonPaging();
		}else {
			MemberFi voIfa = new MemberFi();
			if (null != name && !"".equals(name)) {
				try {
					name = URLDecoder.decode(name, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			MemberBase memberBase = new MemberBase();
			memberBase.setLoginCode(name);
			memberBase.setMemberType(curMember.getMemberType());
			memberBase.setId(curMember.getId());
			voIfa.setMember(memberBase);
			jsonPaging = this.getJsonPaging(request);
			jsonPaging = memberManageService.findallFi(jsonPaging, voIfa, langCode);
		}
		

		/*
		 * List list = new ArrayList(); Iterator it =
		 * jsonPaging.getList().iterator(); while (it.hasNext()) { MemberFi ifa
		 * = (MemberFi) it.next(); FiVO vo = new FiVO(); vo.setId(ifa.getId());
		 * vo.setEmail(ifa.getMember().getEmail());
		 * 
		 * vo.setLoginCode(ifa.getMember().getLoginCode());
		 * vo.setNickName(ifa.getMember().getNickName());
		 * 
		 * list.add(vo); } jsonPaging.setList(list);
		 */
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * FI新增
	 * 
	 * @author mqzou
	 * @date 2016-07-06
	 */
	@RequestMapping(value = "/console/member/fi/add")
	public String fiAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// this.isMobileDevice(request,model);
		FiVO vo = new FiVO();
		model.put("title", "新增FI");
		model.put("fivo", vo);
		return "console/member/fi_input";
	}

	/**
	 * FI明细(修改)
	 * 
	 * @author mqzou
	 * @date 2016-07-06
	 */
	@RequestMapping(value = "/console/member/fi/detail")
	public String fiDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		MemberFi obj = memberManageService.findFiById(id);
		FiVO vo = new FiVO();
		vo.setId(obj.getId());
		vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
		vo.setCountry(null == obj.getCountry() ? "" : obj.getCountry().getId());
		vo.setIncorporationPlace(null == obj.getIncorporationPlace() ? "" : obj.getIncorporationPlace().getId());
		vo.setCompanyName(obj.getCompanyName());
		vo.setEntityOther(obj.getEntityOther());
		vo.setEntityType(obj.getEntityType());
		vo.setGiin(obj.getGiin());
		vo.setIncorporationDate(obj.getIncorporationDate());
		vo.setIsFinancial(obj.getIsFinancial());
		vo.setMailingAddress(obj.getMailingAddress());
		vo.setNaturePurpose(obj.getNaturePurpose());
		vo.setRegisteredAddress(obj.getRegisteredAddress());
		vo.setRegisterNo(obj.getRegisterNo());
		vo.setWebsite(obj.getWebsite());

		vo.setEmail(obj.getMember().getEmail());

		vo.setLoginCode(obj.getMember().getLoginCode());
		vo.setMobileNumber(obj.getMember().getMobileNumber());
		vo.setNickName(obj.getMember().getNickName());
		vo.setPassword(obj.getMember().getPassword());
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		boolean editable=false;
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM==curMember.getMemberType()){
			editable=true;
		}
		model.put("editable", editable);
		model.put("fivo", vo);
		model.put("title", "FI详细信息");
		return "console/member/fi_input";
	}

	/**
	 * 保存FI
	 * 
	 * @author mqzou
	 * @date 2016-07-06
	 */
	@RequestMapping(value = "/console/member/fi/save", method = RequestMethod.POST)
	public void saveFI(HttpServletRequest request, HttpServletResponse response, ModelMap model, FiVO fiVo) {
		// individualVo.setMember(memberBase);
		MemberFi fiUpdate = null;
		MemberBase memberUpdate = null;
		String postData=request.getParameter("postData");
		String[] inputDataList = postData.split("&");
		Map<String,Object> memberMap = new HashMap<String,Object>();
		for(String memberStr:inputDataList){
			String[] memberStrArray = memberStr.split("=");
			memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
		}
		JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
		fiVo.setId(memberDataJSON.getString("id"));
		fiVo.setCompanyName(memberDataJSON.getString("companyName"));
		fiVo.setCountry(memberDataJSON.getString("country"));
		fiVo.setEmail(memberDataJSON.getString("email"));
		fiVo.setEntityOther(memberDataJSON.getString("entityOther"));
		fiVo.setEntityType(memberDataJSON.getString("entityType"));
		fiVo.setGiin(memberDataJSON.getString("giin"));
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		try {;
			fiVo.setIncorporationDate(sdf.parse(memberDataJSON.getString("incorporationDate")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		fiVo.setIncorporationPlace(memberDataJSON.getString("incorporationPlace"));
		fiVo.setIsFinancial(memberDataJSON.getString("isFinancial"));
		fiVo.setLoginCode(memberDataJSON.getString("loginCode"));
		fiVo.setMailingAddress(memberDataJSON.getString("mailingAddress"));
		fiVo.setMobileNumber(memberDataJSON.getString("mobileNumber"));
		fiVo.setNaturePurpose(memberDataJSON.getString("naturePurpose"));
		fiVo.setNickName(memberDataJSON.getString("nickName"));
		fiVo.setRegisteredAddress(memberDataJSON.getString("registeredAddress"));
		fiVo.setRegisterNo(memberDataJSON.getString("registerNo"));
		fiVo.setWebsite(memberDataJSON.getString("website"));
		
		if ("" != fiVo.getId() && null != fiVo.getId() && !"".equals(fiVo.getId())) {
			fiUpdate = memberManageService.findFiById(fiVo.getId());
			memberUpdate = fiUpdate.getMember();
		} else {
			fiUpdate = new MemberFi();
			memberUpdate = new MemberBase();
		}

		if ("" != fiVo.getCountry() && null != fiVo.getCountry() && !"".equals(fiVo.getCountry())) {
			SysCountry country = memberManageService.findCountryById(fiVo.getCountry());
			fiUpdate.setCountry(country);
		}

		if ("" != fiVo.getIncorporationPlace() && null != fiVo.getIncorporationPlace() && !"".equals(fiVo.getIncorporationPlace())) {
			SysCountry country = memberManageService.findCountryById(fiVo.getIncorporationPlace());
			fiUpdate.setIncorporationPlace(country);
		}
		fiUpdate.setCompanyName(fiVo.getCompanyName());
		fiUpdate.setEntityOther(fiVo.getEntityOther());
		fiUpdate.setEntityType(fiVo.getEntityType());
		fiUpdate.setGiin(fiVo.getGiin());
		fiUpdate.setIncorporationDate(fiVo.getIncorporationDate());
		fiUpdate.setIsFinancial(fiVo.getIsFinancial());
		fiUpdate.setMailingAddress(fiVo.getMailingAddress());
		fiUpdate.setNaturePurpose(fiVo.getNaturePurpose());
		fiUpdate.setRegisteredAddress(fiVo.getRegisteredAddress());
		fiUpdate.setRegisterNo(fiVo.getRegisterNo());
		fiUpdate.setWebsite(fiVo.getWebsite());

		memberUpdate.setLoginCode(fiVo.getLoginCode());
		memberUpdate.setNickName(fiVo.getNickName());
		memberUpdate.setEmail(fiVo.getEmail());
		memberUpdate.setPassword(fiVo.getPassword());
		memberUpdate.setMobileNumber(fiVo.getMobileNumber());
		memberUpdate.setMemberType(1);// 投资人
		memberUpdate.setSubMemberType(13);// FI账户
		memberUpdate.setLastUpdate(new Date());

		fiUpdate.setMember(memberUpdate);

		MemberFi voFi = memberManageService.saveFi(fiUpdate);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != voFi) {
			obj.put("result", true);
			obj.put("message", "保存成功");
		} else {
			obj.put("result", false);
			obj.put("message", "保存失败");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * coporate数据查询的方法
	 * 
	 * @author mqzou
	 * @date 2016-07-06
	 */
	@RequestMapping(value = "/console/member/coporate/listJson", method = RequestMethod.POST)
	public void listJsonCoporator(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		String name = request.getParameter("keyword");
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR==curMember.getMemberType()){
			jsonPaging=new JsonPaging();
		}else {
			MemberCorporate voIfa = new MemberCorporate();
			if (null != name && !"".equals(name)) {
				try {
					name = URLDecoder.decode(name, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			MemberBase memberBase = new MemberBase();
			memberBase.setLoginCode(name);
			memberBase.setId(curMember.getId());
			memberBase.setMemberType(curMember.getMemberType());
			voIfa.setMember(memberBase);
			jsonPaging = this.getJsonPaging(request);
			jsonPaging = memberManageService.findallCoporate(jsonPaging, voIfa, langCode);

		}

		this.toJsonString(response, jsonPaging);
	}

	/**
	 * Coporate新增
	 * 
	 * @author mqzou
	 * @date 2016-07-06
	 */
	@RequestMapping(value = "/console/member/coporate/add")
	public String coporateAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// this.isMobileDevice(request,model);
		FiVO vo = new FiVO();
		model.put("title", "新增Coporate");
		model.put("fivo", vo);
		return "console/member/coporator_input";
	}

	/**
	 * Coporate明细(修改)
	 * 
	 * @author mqzou
	 * @date 2016-07-06
	 */
	@RequestMapping(value = "/console/member/coporate/detail")
	public String coporateDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		MemberCorporate obj = memberManageService.findCorporateById(id);
		CoporateVO vo = new CoporateVO();
		vo.setId(obj.getId());
		vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
		vo.setCountry(null == obj.getCountry() ? "" : obj.getCountry().getId());
		vo.setIncorporationPlace(null == obj.getIncorporationPlace() ? "" : obj.getIncorporationPlace().getId());
		vo.setCompanyName(obj.getCompanyName());
		vo.setEntityOther(obj.getEntityOther());
		vo.setEntityType(obj.getEntityType());
		vo.setGiin(obj.getGiin());
		vo.setIncorporationDate(obj.getIncorporationDate());
		vo.setIsFinancial(obj.getIsFinancial());
		vo.setMailingAddress(obj.getMailingAddress());
		vo.setNaturePurpose(obj.getNaturePurpose());
		vo.setRegisteredAddress(obj.getRegisteredAddress());
		vo.setRegisterNo(obj.getRegisterNo());
		vo.setWebsite(obj.getWebsite());

		vo.setEmail(obj.getMember().getEmail());

		vo.setLoginCode(obj.getMember().getLoginCode());
		vo.setMobileNumber(obj.getMember().getMobileNumber());
		vo.setNickName(obj.getMember().getNickName());
		vo.setPassword(obj.getMember().getPassword());
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		boolean editable=false;
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM==curMember.getMemberType()){
			editable=true;
		}
		model.put("editable", editable);
		model.put("coporatevo", vo);
		model.put("title", "Coporate详细信息");
		return "console/member/coporator_input";
	}

	/**
	 * 保存Coporate
	 * 
	 * @author mqzou
	 * @date 2016-07-06
	 */
	@RequestMapping(value = "/console/member/coporate/save", method = RequestMethod.POST)
	public void saveCoporate(HttpServletRequest request, HttpServletResponse response, ModelMap model, CoporateVO coporateVo) {
		// individualVo.setMember(memberBase);
		MemberCorporate coporateUpdate = null;
		MemberBase memberUpdate = null;
		String postData=request.getParameter("postData");
		String[] inputDataList = postData.split("&");
		Map<String,Object> memberMap = new HashMap<String,Object>();
		for(String memberStr:inputDataList){
			String[] memberStrArray = memberStr.split("=");
			memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
		}
		JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
		coporateVo.setId(memberDataJSON.getString("id"));
		coporateVo.setCompanyName(memberDataJSON.getString("companyName"));
		coporateVo.setCountry(memberDataJSON.getString("country"));
		coporateVo.setEmail(memberDataJSON.getString("email"));
		coporateVo.setEntityOther(memberDataJSON.getString("entityOther"));
		coporateVo.setEntityType(memberDataJSON.getString("entityType"));
		coporateVo.setGiin(memberDataJSON.getString("giin"));
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		try {
			coporateVo.setIncorporationDate(sdf.parse(memberDataJSON.getString("incorporationDate")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		coporateVo.setIncorporationPlace(memberDataJSON.getString("incorporationPlace"));
		coporateVo.setIsFinancial(memberDataJSON.getString("isFinancial"));
		coporateVo.setMailingAddress(memberDataJSON.getString("mailingAddress"));
		coporateVo.setLoginCode(memberDataJSON.getString("loginCode"));
		coporateVo.setMobileNumber(memberDataJSON.getString("mobileNumber"));
		coporateVo.setNaturePurpose(memberDataJSON.getString("naturePurpose"));
		coporateVo.setNickName(memberDataJSON.getString("nickName"));
		coporateVo.setRegisteredAddress(memberDataJSON.getString("registeredAddress"));
		coporateVo.setRegisterNo(memberDataJSON.getString("registerNo"));
		coporateVo.setWebsite(memberDataJSON.getString("website"));
		
		
		if ("" != coporateVo.getId() && null != coporateVo.getId() && !"".equals(coporateVo.getId())) {
			coporateUpdate = memberManageService.findCorporateById(coporateVo.getId());
			memberUpdate = coporateUpdate.getMember();
		} else {
			coporateUpdate = new MemberCorporate();
			memberUpdate = new MemberBase();
		}

		if ("" != coporateVo.getCountry() && null != coporateVo.getCountry() && !"".equals(coporateVo.getCountry())) {
			SysCountry country = memberManageService.findCountryById(coporateVo.getCountry());
			coporateUpdate.setCountry(country);
		}

		if ("" != coporateVo.getIncorporationPlace() && null != coporateVo.getIncorporationPlace() && !"".equals(coporateVo.getIncorporationPlace())) {
			SysCountry country = memberManageService.findCountryById(coporateVo.getIncorporationPlace());
			coporateUpdate.setIncorporationPlace(country);
		}
		coporateUpdate.setCompanyName(coporateVo.getCompanyName());
		coporateUpdate.setEntityOther(coporateVo.getEntityOther());
		coporateUpdate.setEntityType(coporateVo.getEntityType());
		coporateUpdate.setGiin(coporateVo.getGiin());
		coporateUpdate.setIncorporationDate(coporateVo.getIncorporationDate());
		coporateUpdate.setIsFinancial(coporateVo.getIsFinancial());
		coporateUpdate.setMailingAddress(coporateVo.getMailingAddress());
		coporateUpdate.setNaturePurpose(coporateVo.getNaturePurpose());
		coporateUpdate.setRegisteredAddress(coporateVo.getRegisteredAddress());
		coporateUpdate.setRegisterNo(coporateVo.getRegisterNo());
		coporateUpdate.setWebsite(coporateVo.getWebsite());

		memberUpdate.setLoginCode(coporateVo.getLoginCode());
		memberUpdate.setNickName(coporateVo.getNickName());
		memberUpdate.setEmail(coporateVo.getEmail());
		memberUpdate.setPassword(coporateVo.getPassword());
		memberUpdate.setMobileNumber(coporateVo.getMobileNumber());
		memberUpdate.setMemberType(1);// 投资人
		memberUpdate.setSubMemberType(12);// 公司投资账户
		memberUpdate.setLastUpdate(new Date());

		coporateUpdate.setMember(memberUpdate);

		MemberCorporate voCoporate = memberManageService.savecCorporate(coporateUpdate);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != voCoporate) {
			obj.put("result", true);
			obj.put("message", "保存成功");
		} else {
			obj.put("result", false);
			obj.put("message", "保存失败");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 删除individual
	 * 
	 * @author mqzou
	 * @date 2016-07-08
	 */
	@RequestMapping(value = "/console/member/individual/delete", method = RequestMethod.POST)
	public void deleteIndividual(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		MemberIndividual vo = memberManageService.delteIndividual(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != vo) {
			obj.put("result", true);
			obj.put("message", "");
		} else {
			obj.put("result", false);
			obj.put("message", "");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 删除FI
	 * 
	 * @author mqzou
	 * @date 2016-07-08
	 */
	@RequestMapping(value = "/console/member/fi/delete", method = RequestMethod.POST)
	public void deleteFi(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		MemberFi vo = memberManageService.deleteMemberFi(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != vo) {
			obj.put("result", true);
			obj.put("message", "");
		} else {
			obj.put("result", false);
			obj.put("message", "");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 删除coporate
	 * 
	 * @author mqzou
	 * @date 2016-07-08
	 */
	@RequestMapping(value = "/console/member/coporate/delete", method = RequestMethod.POST)
	public void deleteCoporate(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		MemberCorporate vo = memberManageService.delelteCorporate(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != vo) {
			obj.put("result", true);
			obj.put("message", "");
		} else {
			obj.put("result", false);
			obj.put("message", "");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 删除IFA
	 * 
	 * @author mqzou
	 * @date 2016-07-08
	 */
	@RequestMapping(value = "/console/member/ifa/delete", method = RequestMethod.POST)
	public void deleteifa(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		MemberIfa vo = memberManageService.deleteIfa(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != vo) {
			obj.put("result", true);
			obj.put("message", "");
		} else {
			obj.put("result", false);
			obj.put("message", "");
		}
		JsonUtil.toWriter(obj, response);
	}
	/**
	 * 删除用户
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/member/delete", method = RequestMethod.POST)
	public void deleteMember(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("memberId");
		Map<String, Object> obj = new HashMap<String, Object>();
		if (memberManageService.deleteMember(id)) {
			obj.put("result", true);
			obj.put("message", "");
		} else {
			obj.put("result", false);
			obj.put("message", "");
		}
		JsonUtil.toWriter(obj, response);
	}
	
	
	/**
	 * admin数据查询的方法
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/member/admin/listJson", method = RequestMethod.POST)
	public void listJsonAdmin(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//String langCode = this.getLoginLangFlag(request);
		String keyWord = request.getParameter("keyword");
		String type=request.getParameter("type");
		MemberAdmin memberAdmin=new MemberAdmin();
		MemberBase memberBase=new MemberBase();
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if (null != keyWord && !"".equals(keyWord)) {
			try {
				keyWord = URLDecoder.decode(keyWord, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(null!=type && !"".equals(type) ){
			memberAdmin.setType(type);
		}
		MemberAdmin admin=memberManageService.findAdminByMemberId(curMember.getId());
		memberBase.setLoginCode(keyWord);
		memberAdmin.setMember(memberBase);
		memberAdmin.setId(admin.getId());
		jsonPaging=this.getJsonPaging(request);
		if(CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(admin.getType())){
			jsonPaging=memberManageService.findAllAdminMember(jsonPaging, memberAdmin);	
		}else {
			jsonPaging=memberManageService.findAllAdminMemberPro(jsonPaging, memberAdmin,getLoginLangFlag(request));
		}
		
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * admin新增
	 * 
	 * @author mqzou
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/member/admin/add")
	public String adminAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// this.isMobileDevice(request,model);
		MemberAdminVO vo = new MemberAdminVO();
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		List list = memberManageService.getIfafirmlist();
		List distributorList = distributorService.findAllDistributor();
		model.put("curMember", curMember);
		model.put("adminVo", vo);
		model.put("ifafirmlist", list);
		model.put("distributorList", distributorList);
		return "console/member/admin/admin_input";
	}
	/**
	 * admin详细信息
	 * 
	 * @author mqzou
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/member/admin/detail")
	public String adminDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		MemberAdminVO vo = new MemberAdminVO();
		MemberAdmin memberAdmin=memberManageService.findAdminById(id);
		if(null!=memberAdmin){
			vo.setId(memberAdmin.getId());
			vo.setMemberId(memberAdmin.getMember().getId());
			vo.setDistributorId(null!=memberAdmin.getDistributor()?memberAdmin.getDistributor().getId():"");
			vo.setEmail(memberAdmin.getMember().getEmail());
			vo.setIfafirmId(null!=memberAdmin.getIfafirm()?memberAdmin.getIfafirm().getId():"");
			vo.setLoginCode(memberAdmin.getMember().getLoginCode());
			vo.setMobileNumber(memberAdmin.getMember().getMobileNumber());
			vo.setNickName(memberAdmin.getMember().getNickName());
			vo.setParentId(null!=memberAdmin.getParent()?memberAdmin.getParent().getId():"");
			vo.setParentName(null!=memberAdmin.getParent()?memberAdmin.getParent().getMember().getNickName():"");
			vo.setType(memberAdmin.getType());
			//vo.setCreateBy(null!=memberAdmin.getCreateBy()?memberAdmin.getCreateBy().getNickName():"");
			
			
		}
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		List list = memberManageService.getIfafirmlist();
		List distributorList = distributorService.findAllDistributor();
		model.put("curMember", curMember);
		model.put("adminVo", vo);
		model.put("ifafirmlist", list);
		model.put("distributorList", distributorList);
		return "console/member/admin/admin_input";
	}
	

	/**
	 * 保存admin 信息
	 * 
	 * @author mqzou
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/member/admin/save")
	public void adminSave(HttpServletRequest request, HttpServletResponse response, ModelMap model, MemberAdminVO memberAdminVO) {
		MemberBase memberBase = null;
		MemberAdmin memberAdmin = null;
		
		String postData=request.getParameter("postData");
		
		String[] inputDataList = postData.split("&");
		Map<String,Object> memberMap = new HashMap<String,Object>();
		for(String memberStr:inputDataList){
			String[] memberStrArray = memberStr.split("=");
			memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
		}
		JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
		memberAdminVO.setId(memberDataJSON.containsKey("id")?memberDataJSON.getString("id"):"");
		memberAdminVO.setDistributorId(memberDataJSON.containsKey("distributorId")?memberDataJSON.getString("distributorId"):"");
		memberAdminVO.setEmail(memberDataJSON.getString("email"));
		memberAdminVO.setIfafirmId(memberDataJSON.containsKey("ifafirmId")?memberDataJSON.getString("ifafirmId"):"");
		memberAdminVO.setLoginCode(memberDataJSON.getString("loginCode"));
		memberAdminVO.setMemberId(memberDataJSON.getString("memberId"));
		memberAdminVO.setMobileNumber(memberDataJSON.getString("mobileNumber"));
		memberAdminVO.setNickName(memberDataJSON.getString("nickName"));
		memberAdminVO.setType(memberDataJSON.containsKey("type")?memberDataJSON.getString("type"):"");
		
		// 当前用户信息
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if (null != memberAdminVO.getId() && !"".equals(memberAdminVO.getId())) {// 修改
			memberAdmin = memberManageService.findAdminById(memberAdminVO.getId());
			memberBase = memberAdmin.getMember();
			
			if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM == curMember.getMemberType() && null != memberAdminVO.getType()) {// 如果当前用户是系统用户

					if (CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(memberAdminVO.getType())) {// 系统用户添加系统用户为下级
						memberAdmin.setType(CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM);
						memberAdmin.setDistributor(null);
						memberAdmin.setIfafirm(null);

						memberBase.setMemberType(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM);
					} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(memberAdminVO.getType())) {// 系统用户添加ifa管理员为初级
						memberAdmin.setType(memberAdminVO.getType());
						memberAdmin.setDistributor(null);

						memberBase.setMemberType(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA);
						memberBase.setSubMemberType(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFAFIRM);
						if (null != memberAdminVO.getIfafirmId()) {
							MemberIfafirm ifafirm = ifafirmService.findById(memberAdminVO.getIfafirmId());
							memberAdmin.setIfafirm(ifafirm);
						}

					} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(memberAdminVO.getType())) {// 系统用户添加distributor管理员为初级
						memberAdmin.setType(memberAdminVO.getType());
						memberAdmin.setIfafirm(null);
						memberBase.setMemberType(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR);
						memberBase.setSubMemberType(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_DISTRIBUTOR);
						if (null != memberAdminVO.getDistributorId()) {

							MemberDistributor distributor = distributorService.findDistributorById(memberAdminVO.getDistributorId());
							memberAdmin.setDistributor(distributor);
						}
					}
			

			}
			
		} else {// 新增
			memberAdmin = new MemberAdmin();
			memberBase = new MemberBase();

			
			MemberAdmin curAdmin = memberManageService.findAdminByMemberId(curMember.getId());
		//	MemberBase curBase=curAdmin.getMember();

		//	memberAdmin.setCreateBy(curBase);
			if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM == curMember.getMemberType()) {// 如果当前用户是系统用户
				if (null != memberAdminVO.getType()) {
					if (CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(memberAdminVO.getType())) {// 系统用户添加系统用户为下级
						memberAdmin.setType(CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM);
						memberAdmin.setParent(curAdmin);
						memberAdmin.setDistributor(null);
						memberAdmin.setIfafirm(null);

						memberBase.setMemberType(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM);
					} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(memberAdminVO.getType())) {// 系统用户添加ifa管理员为初级
						memberAdmin.setType(memberAdminVO.getType());
						memberAdmin.setParent(null);
						memberAdmin.setDistributor(null);

						memberBase.setMemberType(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA);
						memberBase.setSubMemberType(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFAFIRM);
						if (null != memberAdminVO.getIfafirmId()) {
							MemberIfafirm ifafirm = ifafirmService.findById(memberAdminVO.getIfafirmId());
							memberAdmin.setIfafirm(ifafirm);
						}

					} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(memberAdminVO.getType())) {// 系统用户添加distributor管理员为初级
						memberAdmin.setType(memberAdminVO.getType());
						memberAdmin.setParent(null);
						memberAdmin.setIfafirm(null);
						memberBase.setMemberType(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR);
						memberBase.setSubMemberType(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_DISTRIBUTOR);
						if (null != memberAdminVO.getDistributorId()) {

							MemberDistributor distributor = distributorService.findDistributorById(memberAdminVO.getDistributorId());
							memberAdmin.setDistributor(distributor);
						}
					}
				}

			} else if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == curMember.getMemberType()) {// ifa管理员添加ifa管理员为下级
				memberAdmin.setType(CommonConstantsWeb.MEMBERADMIN_TYPE_IFA);
				memberAdmin.setParent(curAdmin);
				memberAdmin.setIfafirm(curAdmin.getIfafirm());
				memberBase.setMemberType(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA);
				memberBase.setSubMemberType(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFAFIRM);
				

			} else if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR == curMember.getMemberType()) {// distributor管理员添加distributor管理员为下级
				memberAdmin.setType(CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR);
				memberAdmin.setParent(curAdmin);
				memberAdmin.setDistributor(curAdmin.getDistributor());
				memberBase.setMemberType(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR);
				memberBase.setSubMemberType(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_DISTRIBUTOR);
			}
		}

		memberBase.setLoginCode(memberAdminVO.getLoginCode());
		memberBase.setEmail(memberAdminVO.getEmail());
		memberBase.setIsValid("1");
		memberBase.setMobileNumber(memberAdminVO.getMobileNumber());
		memberBase.setNickName(memberAdminVO.getNickName());
		memberBase.setStatus("0");

		memberAdmin.setMember(memberBase);

		memberAdmin = memberManageService.saveAdmin(memberAdmin);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != memberAdmin) {
			obj.put("result", true);
			obj.put("message", "");
		} else {
			obj.put("result", false);
			obj.put("message", "");
		}
		JsonUtil.toWriter(obj, response);

	}
}
