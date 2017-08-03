package com.fsll.wmes.member.action.console;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ReadWriteExcelUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysCountryService;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.service.MemberCompanyIfafirmService;
import com.fsll.wmes.company.service.MemberCompanyService;
import com.fsll.wmes.company.vo.MemberCompanyVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberCompanyIfafirm;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberFi;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.CoporateVO;
import com.fsll.wmes.member.vo.FiVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.member.vo.IndividualVO;
import com.fsll.wmes.member.vo.MemberAdminVO;
import com.fsll.wmes.member.vo.MemberBaseVO;
import com.fsll.wmes.web.service.DistributorService;

/**
 * 前台、工作台用户成员管理
 * @author qgfeng
 * @date 2016-11-3
 */
@Controller
public class ConsoleMemberManageAct extends CoreBaseAct {
	
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
	private MemberBaseService memberBaseService;
	@Autowired
	private IfaManageService ifaManageService;
	@Autowired
	private MemberCompanyIfafirmService memberCompanyIfafirmService;
	@Autowired
	private MemberCompanyService memberCompanyService;
	@Autowired
	private CompanyInfoService companyInfoService;

	/**
	 * Individual分页列表
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "console/member/list";
	}
	
	/**
	 * 投资人（Individual）分页查询
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/individual/listJson", method = RequestMethod.POST)
	public void listJsonIndividual(HttpServletRequest request, HttpServletResponse response,MemberBaseVO memberVo) {
		String langCode = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = memberManageService.findAllIndividual(jsonPaging, memberVo, langCode);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * Individual新增
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/individual/add")
	public String individualAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		IndividualVO vo = new IndividualVO();
		model.put("individualvo", vo);
		return "console/member/individual_input";
	}
	
	/**
	 * Individual明细(修改)
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/individual/detail")
	public String individualDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//String langCode = this.getLoginLangFlag(request);
		String id = request.getParameter("id");
		model.put("id", id);
		MemberIndividual individual = memberManageService.findIndividualById(id);
		IndividualVO vo = new IndividualVO(individual);
		String memberId = individual.getMember().getId();
		//通过memberId查找关联的company
		if(StringUtils.isNotBlank(memberId)){
			MemberBase member = memberBaseService.findById(memberId);
			MemberCompany mc = new MemberCompany();
			mc.setMember(member);
			List<MemberCompany> list = memberCompanyService.findByMemberCompany(mc);
			if(!list.isEmpty()){
				mc = list.get(0);
				vo.setCompanyId(mc.getCompany().getId());
			}
		}
		model.put("individualvo", vo);
		return "console/member/individual_input";
	}

	/**
	 * 保存Individual
	 * @author qgfeng
	 * @date 2016-11-4
	 */
	@RequestMapping(value = "/console/member/individual/save", method = RequestMethod.POST)
	public void saveIndividual(IndividualVO individualVo,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			MemberIndividual individual = memberManageService.findIndividualById(individualVo.getId());;
			MemberBase memberbase = null;
			if (individual == null) {
				individual = new MemberIndividual();
				individual.setCreateTime(new Date());
				memberbase = new MemberBase();
				memberbase.setCreateTime(new Date());
				memberbase.setIsValid("1");
				memberbase.setPassword(pwdEncoder.encodePassword(individualVo.getPassword()));
			}else{
				memberbase = individual.getMember();
				if(StringUtils.isNotBlank(individualVo.getRepassword())){
					memberbase.setPassword(pwdEncoder.encodePassword(individualVo.getRepassword()));
				}
			}
			memberbase.setLastUpdate(new Date());
			individual.setLastUpdate(new Date());
			memberbase.setLoginCode(individualVo.getLoginCode());
			memberbase.setNickName(individualVo.getNickName());
			memberbase.setEmail(individualVo.getEmail());
			memberbase.setMobileCode(individualVo.getMobileCode());
			memberbase.setMobileNumber(individualVo.getMobileNumber());
			memberbase.setMemberType(1);// 投资人
			memberbase.setSubMemberType(11);// 独立投资账户
			memberbase.setLastUpdate(new Date());
			//改变激活状态
			if(StringUtils.isNotBlank(individualVo.getStatus())){
				memberbase.setStatus(individualVo.getStatus());
			}else{
				memberbase.setStatus("1");
			}
			
			individual.setFirstName(individualVo.getFirstName());
			individual.setLastName(individualVo.getLastName());
			individual.setBorn(individualVo.getBorn());
			individual.setCertNo(individualVo.getCertNo());
			individual.setCertType(individualVo.getCertType());
			individual.setEducation(individualVo.getEducation());
			individual.setEmployment(individualVo.getEmployment());
			individual.setGender(individualVo.getGender());
			individual.setNameChn(individualVo.getNameChn());
			individual.setOccupation(individualVo.getOccupation());
			individual.setTelephone(individualVo.getTelephone());
			individual.setAddress(individualVo.getAddress());

			if (StringUtils.isNoneBlank(individualVo.getCountry())) {
				SysCountry country = sysCountryService.findBycountryId(individualVo.getCountry());
				individual.setCountry(country);
			}
			if (StringUtils.isNoneBlank(individualVo.getNationality())) {
				SysCountry country = sysCountryService.findBycountryId(individualVo.getNationality());
				individual.setNationality(country);
			}
			individual.setMember(memberbase);
			MemberIndividual voIndividual = memberManageService.saveIndividual(individual);
			if(voIndividual != null){
				result = true;
				//如果memberCompany没有相关联的数据则插入
				String companyId = individualVo.getCompanyId();
				CompanyInfo company = companyInfoService.findById(companyId);
				MemberBase member = voIndividual.getMember();
				MemberCompany info = new MemberCompany();
				info.setMember(member);
				List<MemberCompany> list = memberCompanyService.findByMemberCompany(info);
				if(list!=null && list.size()>0){
					info=list.get(0);
				}	
					info.setCompany(company);
					memberCompanyService.saveOrUpdate(info);
					MemberCompany memberCompany = new MemberCompany();
					memberCompany.setId(null);
					memberCompany.setMember(member);
					memberCompany.setCompany(company);
					memberCompanyService.saveOrUpdate(memberCompany);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * 删除individual
	 * @author qgfeng
	 * @date 2016-11-4
	 */
	@RequestMapping(value = "/console/member/individual/delete",method = RequestMethod.POST)
	public void deleteIndividual(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String ids = request.getParameter("ids");
			String[] idArry = ids.split(",");
			if(idArry != null && idArry.length>0){
				for (String id : idArry) {
					if(StringUtils.isNotBlank(id)){
						memberManageService.delteIndividual(id);
					}
				}
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}

	/**
	 * memberAdmin excel导入
	 * @author qgfeng
	 * @date 2016-12-19
	 */
    @RequestMapping("/console/member/individual/excelInput")
    public String excelInput(HttpServletRequest request,HttpServletResponse response){
    	Map<String, Object> obj = new HashMap<String, Object>();
    	boolean result = false;
    	Integer errorRow = null;//错误excel行数
    	String errorType = "";//错误类型：0登入账号为空；1登入账号已存在；2手机号为空；3email为空
		try {
			String ctxPath = request.getSession().getServletContext().getRealPath("/");
			String filePath = request.getParameter("filePath");
			File excelFile = new File(ctxPath+filePath);
			List<MemberIndividual> individualList = new ArrayList<MemberIndividual>();
			if (excelFile.isFile() && excelFile.exists()) {
				List<Map<String, String>> list = ReadWriteExcelUtils.read(excelFile);
				if(list != null && !list.isEmpty()){
					for(int i=0;i<list.size();i++){
						Map<String, String> map = list.get(i);
						//memberBase info
						String loginCode = map.get("login_code");
						String password = map.get("password");
						String nickName = map.get("nick_name");
						String email = map.get("email");
						String mobileCode = map.get("mobile_code");
						String mobileNumber = map.get("mobile_number");
						String defCurrency = map.get("def_currency");
						String defDisplayColor = map.get("def_display_color");
						String languageSpoken =  map.get("language_spoken");
						String liveRegion = map.get("live_region");
						String langCode = map.get("lang_code");
						String webchatCode = map.get("webchat_code");
						String linkedInCode =  map.get("linkedIn_code");
						String facebookCode = map.get("facebook_code");
						String qqCode = map.get("qq_code");
						String weiboCode = map.get("weibo_code");
						String twitterCode = map.get("twitter_code");
						
						//individual info
						String firstName = map.get("first_name");
						String lastName = map.get("last_name");
						String nameChn = map.get("name_chn");
						String gender = map.get("gender");
						String certType = map.get("cert_type");
						String certNo = map.get("cert_no");
						
						MemberBase base = new MemberBase();
						base.setLoginCode(loginCode);
						if(StringUtils.isNotBlank(password)){
							base.setPassword(pwdEncoder.encodePassword(password));
						}
						base.setNickName(nickName);
						base.setEmail(email);
						base.setMobileNumber(mobileNumber);
						base.setMobileCode(mobileCode);
						base.setDefCurrency(defCurrency);
						base.setDefDisplayColor(defDisplayColor);
						base.setLanguageSpoken(languageSpoken);
						base.setLiveRegion(liveRegion);
						base.setLangCode(langCode);
						base.setWebchatCode(webchatCode);
						base.setLinkedInCode(linkedInCode);
						base.setFacebookCode(facebookCode);
						base.setQqCode(qqCode);
						base.setWeiboCode(weiboCode);
						base.setTwitterCode(twitterCode);
						
						MemberIndividual individual = new MemberIndividual();
						individual.setFirstName(firstName);
						individual.setLastName(lastName);
						individual.setNameChn(nameChn);
						individual.setGender(gender);
						individual.setCertNo(certNo);
						individual.setCertType(certType);
						individual.setMember(base);
						individualList.add(individual);
						errorRow = i;
						boolean checkLoginCode = memberManageService.checkLoginCode(loginCode, null);
						if(StringUtils.isBlank(loginCode)){
							errorType = "0";
							return null;
						}else if(!checkLoginCode){
							errorType = "1";
							return null;
						}else if(StringUtils.isBlank(mobileNumber)){
							errorType = "2";
							return null;
						}else if(StringUtils.isBlank(email)){
							errorType = "3";
							return null;
						}
					}
				}
				//删除excel文件
				excelFile.delete();
				//数据库保存操作
				for(MemberIndividual info : individualList){
					memberManageService.saveIndividual(info);
				}
				result = true;
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			obj.put("errorRow", errorRow);
			obj.put("errorType", errorType);
			JsonUtil.toWriter(obj, response);
		}
		return null;
    }
    
	//-----  end Individual -----------
	/**
	 * fi数据查询
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/fi/listJson", method = RequestMethod.POST)
	public void listJsonFi(HttpServletRequest request, HttpServletResponse response,MemberBaseVO memberVo) {
		String langCode = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = memberManageService.findallFi(jsonPaging, memberVo, langCode);
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * FI新增
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/fi/add")
	public String fiAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		FiVO vo = new FiVO();
		model.put("fivo", vo);
		return "console/member/fi_input";
	}

	/**
	 * FI明细(修改)
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/fi/detail")
	public String fiDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		MemberFi memeberFi = memberManageService.findFiById(id);
		FiVO vo = new FiVO(memeberFi);
		model.put("fivo", vo);
		return "console/member/fi_input";
	}

	/**
	 * 保存FI
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/fi/save", method = RequestMethod.POST)
	public void saveFI(FiVO fiVo,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			MemberFi fi = memberManageService.findFiById(fiVo.getId());;
			MemberBase base = null;
			if (fi == null) {
				fi = new MemberFi();
				base = new MemberBase();
				base.setCreateTime(new Date());
				base.setIsValid("1");
			} else {
				base = fi.getMember();
				base.setLastUpdate(new Date());
			}

			if (StringUtils.isNoneBlank(fiVo.getCountry())) {
				SysCountry country = sysCountryService.findBycountryId(fiVo.getCountry());
				fi.setCountry(country);
			}

			if (StringUtils.isNoneBlank(fiVo.getIncorporationPlace())) {
				SysCountry country = sysCountryService.findBycountryId(fiVo.getIncorporationPlace());
				fi.setIncorporationPlace(country);
			}
			fi.setCompanyName(fiVo.getCompanyName());
			fi.setEntityOther(fiVo.getEntityOther());
			fi.setEntityType(fiVo.getEntityType());
			fi.setGiin(fiVo.getGiin());
			fi.setIncorporationDate(fiVo.getIncorporationDate());
			fi.setIsFinancial(fiVo.getIsFinancial());
			fi.setMailingAddress(fiVo.getMailingAddress());
			fi.setNaturePurpose(fiVo.getNaturePurpose());
			fi.setRegisteredAddress(fiVo.getRegisteredAddress());
			fi.setRegisterNo(fiVo.getRegisterNo());
			fi.setWebsite(fiVo.getWebsite());

			base.setLoginCode(fiVo.getLoginCode());
			base.setNickName(fiVo.getNickName());
			base.setEmail(fiVo.getEmail());
			base.setMobileNumber(fiVo.getMobileNumber());
			base.setMemberType(1);// 投资人
			base.setSubMemberType(13);// FI账户
			

			fi.setMember(base);

			MemberFi voFi = memberManageService.saveFi(fi);
			if(voFi != null){
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			JsonUtil.toWriter(obj, response);
		}
		
	}
	
	/**
	 * 删除FI
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/fi/delete", method = RequestMethod.POST)
	public void deleteFi(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String ids = request.getParameter("ids");
			String[] idArry = ids.split(",");
			if(idArry != null && idArry.length>0){
				for (String id : idArry) {
					if(StringUtils.isNotBlank(id)){
						memberManageService.deleteMemberFi(id);
					}
				}
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	//-----  end FI -----------
	
	/**
	 * coporate数据查询的方法
	 * @author qgfeng
	 * @date 2016-11-3
	 * @param request
	 */
	@RequestMapping(value = "/console/member/coporate/listJson", method = RequestMethod.POST)
	public void listJsonCoporator(HttpServletRequest request, HttpServletResponse response,MemberBaseVO memberVo) {
		String langCode = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = memberManageService.findallCoporate(jsonPaging, memberVo, langCode);
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * Coporate新增
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/coporate/add")
	public String coporateAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		CoporateVO vo = new CoporateVO();
		model.put("coporatevo", vo);
		return "console/member/coporator_input";
	}

	/**
	 * Coporate明细(修改)
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/coporate/detail")
	public String coporateDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		MemberCorporate corporate = memberManageService.findCorporateById(id);
		CoporateVO vo = new CoporateVO(corporate);
		model.put("coporatevo", vo);
		return "console/member/coporator_input";
	}

	/**
	 * 保存Coporate
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/coporate/save", method = RequestMethod.POST)
	public void saveCoporate(CoporateVO coporateVo,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			MemberCorporate coporate = memberManageService.findCorporateById(coporateVo.getId());
			MemberBase member = null;
			if (coporate == null) {
				coporate = new MemberCorporate();
				member = new MemberBase();
				member.setCreateTime(new Date());
				member.setIsValid("1");
			} else {
				member = coporate.getMember();
				member.setLastUpdate(new Date());
			}
			if (StringUtils.isNoneBlank(coporateVo.getCountry())) {
				SysCountry country = sysCountryService.findBycountryId(coporateVo.getCountry());
				coporate.setCountry(country);
			}

			if (StringUtils.isNoneBlank(coporateVo.getIncorporationPlace())) {
				SysCountry country = sysCountryService.findBycountryId(coporateVo.getIncorporationPlace());
				coporate.setIncorporationPlace(country);
			}
			coporate.setCompanyName(coporateVo.getCompanyName());
			coporate.setEntityOther(coporateVo.getEntityOther());
			coporate.setEntityType(coporateVo.getEntityType());
			coporate.setGiin(coporateVo.getGiin());
			coporate.setIncorporationDate(coporateVo.getIncorporationDate());
			coporate.setIsFinancial(coporateVo.getIsFinancial());
			coporate.setMailingAddress(coporateVo.getMailingAddress());
			coporate.setNaturePurpose(coporateVo.getNaturePurpose());
			coporate.setRegisteredAddress(coporateVo.getRegisteredAddress());
			coporate.setRegisterNo(coporateVo.getRegisterNo());
			coporate.setWebsite(coporateVo.getWebsite());

			member.setLoginCode(coporateVo.getLoginCode());
			member.setNickName(coporateVo.getNickName());
			member.setEmail(coporateVo.getEmail());
			member.setPassword(coporateVo.getPassword());
			member.setMobileNumber(coporateVo.getMobileNumber());
			member.setMemberType(1);// 投资人
			member.setSubMemberType(12);// 公司投资账户
			
			coporate.setMember(member);
			
			MemberCorporate voCoporate = memberManageService.savecCorporate(coporate);
			if(voCoporate != null){
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			JsonUtil.toWriter(obj, response);
		}
		
	}
	
	/**
	 * 删除coporate
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/coporate/delete", method = RequestMethod.POST)
	public void deleteCoporate(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String ids = request.getParameter("ids");
			String[] idArry = ids.split(",");
			if(idArry != null && idArry.length>0){
				for (String id : idArry) {
					if(StringUtils.isNotBlank(id)){
						memberManageService.delelteCorporate(id);
					}
				}
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	//-----  end coporate -----------
	
	/**
	 * ifa数据查询的方法
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/ifa/listJson", method = RequestMethod.POST)
	public void listJsonIfa(HttpServletRequest request, HttpServletResponse response,MemberBaseVO memberVo) {
		String langCode = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = memberManageService.findAllIfa(jsonPaging, memberVo, langCode);
		this.toJsonString(response, jsonPaging);
	}

	
	/**
	 * ifa新增
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/ifa/add")
	public String ifaAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// this.isMobileDevice(request,model);
		IfaVO vo = new IfaVO();
		model.put("ifavo", vo);
		return "console/member/ifa_input";
	}

	/**
	 * ifa明细(修改)
	 * @author qgfeng modify by rqwang 20170525
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/ifa/detail")
	public String ifaDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		MemberIfa ifa = memberManageService.finIfaById(id);
		IfaVO vo = new IfaVO();
		if (null!=ifa){
			vo = new IfaVO(ifa,null);
			String memberId = ifa.getMember().getId();
			//通过memberId查找关联的company
			if(StringUtils.isNotBlank(memberId)){
				MemberBase member = memberBaseService.findById(memberId);
				MemberCompany mc = new MemberCompany();
				mc.setMember(member);
				List<MemberCompany> list = memberCompanyService.findByMemberCompany(mc);
				if(null!=list && !list.isEmpty()){
					mc = list.get(0);
					if (null!=mc.getCompany())
						vo.setCompanyId(mc.getCompany().getId());
				}
			}
			
			//查询关联的ifafirm 返回ifafirm主键
			List<MemberIfaIfafirm> firmRelateArray = ifaManageService.findIfaIfaFirm(ifa.getId(), "1", "1", null, this.getLoginLangFlag(request));
			vo.setCompanyIfafirmId(firmRelateArray.isEmpty()?"":firmRelateArray.get(0).getIfafirm().getId());
			
		}
		model.put("ifavo", vo);
		return "console/member/ifa_input";
	}

	/**
	 * 保存Ifa
	 * @author qgfeng modify by rqwang 20170525
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/ifa/save", method = RequestMethod.POST)
	public void saveIfa(IfaVO ifaVO,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		String errorMsg = "";
		try {
			MemberIfa memberIfa = null;
			MemberBase memberBase = null;
			if (StringUtils.isNoneBlank(ifaVO.getId())) {
				memberIfa = memberManageService.finIfaById(ifaVO.getId());
				memberBase = memberIfa.getMember();
				//修改了密码
				if(StringUtils.isNotBlank(ifaVO.getRepassword())){
					memberBase.setPassword(pwdEncoder.encodePassword(ifaVO.getRepassword()));
				}
			} else {
				Date date = new Date();
				memberIfa = new MemberIfa();
				memberBase = new MemberBase();
				memberIfa.setIfafirmStatus("1");
				memberIfa.setCreateTime(date);
				memberBase.setIsValid("1");
				memberBase.setPassword(pwdEncoder.encodePassword(ifaVO.getPassword()));
				memberBase.setCreateTime(date);
			}
			MemberIfafirm ifaFirm = ifafirmService.findById(ifaVO.getCompanyIfafirmId(),null);
			if (ifaFirm != null) {
				memberIfa.setIfafirm(ifaFirm);
			} 
			memberIfa.setFirstName(ifaVO.getFirstName());
			memberIfa.setLastName(ifaVO.getLastName());
			memberIfa.setBorn(ifaVO.getBorn());
			memberIfa.setCertNo(ifaVO.getCertNo());
			memberIfa.setCertType(ifaVO.getCertType());

			if (StringUtils.isNoneBlank(ifaVO.getCountry())) {//检查是否没有任何一字符串为空白字符串、""或null
				SysCountry country = sysCountryService.findBycountryId(ifaVO.getCountry());
				memberIfa.setCountry(country);
			}
			if (StringUtils.isNoneBlank(ifaVO.getNationality())) {
				SysCountry country = sysCountryService.findBycountryId(ifaVO.getNationality());
				memberIfa.setNationality(country);
			} 
			
			if (StringUtils.isNoneBlank(ifaVO.getPrimaryResidence())) {
				SysCountry country = sysCountryService.findBycountryId(ifaVO.getPrimaryResidence());
				memberIfa.setPrimaryResidence(country);
			} 
			
			memberBase.setSubMemberType(21);// IFA
			memberIfa.setEducation(ifaVO.getEducation());
			memberIfa.setEmployment(ifaVO.getEmployment());
			memberIfa.setGender(ifaVO.getGender());
			memberIfa.setNameChn(ifaVO.getNameChn());
			memberIfa.setLastUpdate(new Date());
			
			memberIfa.setOccupation(ifaVO.getOccupation());
			memberIfa.setTelephone(ifaVO.getTelephone());
			memberIfa.setAddress(ifaVO.getAddress());
			memberIfa.setCeNumber(ifaVO.getCeNumber());
			memberIfa.setAppellation(ifaVO.getAppellation());
			memberIfa.setCfaNumber(ifaVO.getCfaNumber());
			memberIfa.setCfpNumber(ifaVO.getCfpNumber());
			memberIfa.setCompanyType(ifaVO.getCompanyType());
			memberIfa.setIntroduction(ifaVO.getIntroduction());
			memberIfa.setPosition(ifaVO.getPosition());

			memberBase.setLoginCode(ifaVO.getLoginCode());
			memberBase.setNickName(ifaVO.getNickName());
			memberBase.setEmail(ifaVO.getEmail());
			memberBase.setMobileNumber(ifaVO.getMobileNumber());
			memberBase.setMobileCode(ifaVO.getMobileCode());
			memberBase.setMemberType(2);// IFA
			memberBase.setLastUpdate(new Date());
			if(StringUtils.isNotBlank(ifaVO.getStatus())){
				memberBase.setStatus(ifaVO.getStatus());
			}else{
				memberBase.setStatus("1");
			}
			
			memberIfa.setMember(memberBase);
			memberIfa = memberManageService.saveIfa(memberIfa);
			
			
			//保存ifa与ifaFrim关联关系
			if(memberIfa != null && StringUtils.isNoneBlank(memberIfa.getId()) && ifaFirm != null && StringUtils.isNoneBlank(ifaFirm.getId())){
				//先将旧的记录设为无效
				ifaManageService.updateIfaIfaFirmToInvalid(memberIfa.getId());
				//新增/更新新的关系记录
				MemberIfaIfafirm releInfo = ifaManageService.getIfaIfaFirmByIFrimId(memberIfa.getId(), ifaFirm.getId());
				if(releInfo == null){
					releInfo = new MemberIfaIfafirm();
					releInfo.setCreateTime(new Date());
					releInfo.setCheckStatus("1");
				}
				releInfo.setIfa(memberIfa);
				releInfo.setIfafirm(ifaFirm);
				releInfo.setIsValid("1");
				releInfo.setLastUpdate(new Date());
				ifaManageService.saveOrUpdateIfaIfafirm(releInfo);
			}
			
			//如果memberCompany没有相关联的数据则插入,有就先删除再添加
			if (!StrUtils.isEmpty(ifaVO.getCompanyIfafirmId())){
				String companyId  = ifaVO.getCompanyId();
				CompanyInfo company = companyInfoService.findById(companyId);
				MemberBase member = memberBase;
				//先删除再添加
				memberCompanyService.deleteByMember(member.getId());
				company = new CompanyInfo();
				company.setId(companyId);
				MemberCompany info = new MemberCompany();
				info.setCompany(company);
				info.setMember(memberBase);
				MemberCompany memberCompany = new MemberCompany();
				memberCompany.setId(null);
				memberCompany.setMember(memberBase);
				memberCompany.setCompany(company);
				memberCompanyService.saveOrUpdate(memberCompany);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			obj.put("errorMsg", errorMsg);
			JsonUtil.toWriter(obj, response);
		}
	}
	

	/**
	 * 删除IFA
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/console/member/ifa/delete", method = RequestMethod.POST)
	public void deleteifa(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String ids = request.getParameter("ids");
			String[] idArry = ids.split(",");
			if(idArry != null && idArry.length>0){
				for (String id : idArry) {
					if(StringUtils.isNotBlank(id)){
						memberManageService.deleteIfa(id);
					}
				}
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			
			JsonUtil.toWriter(obj, response);
		}
	}
	
	//-----  end IFA -----------

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
	 * 控制台管理员分页列表（ifaFirm/distubiture）
	 * @author qgfeng
	 * @date 2016-11-9
	 * @return
	 */
	@RequestMapping(value = "/console/member/admin/list")
	public String adminList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "console/member/admin/list";
	}
	
	/**
	 * admin数据查询的方法
	 * @author qgfeng
	 * @date 2016-12-2
	 */
	@RequestMapping(value = "/console/member/admin/listJson", method = RequestMethod.POST)
	public void listJsonAdmin(HttpServletRequest request, HttpServletResponse response,MemberAdminVO memberAdmin) {
		jsonPaging=this.getJsonPaging(request);
		jsonPaging = memberManageService.findMemberAdmin(jsonPaging, memberAdmin,getLoginLangFlag(request));
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * admin 新增、编辑、 详细信息
	 * @author qgfeng
	 * @date 2016-12-6
	 */
	@RequestMapping(value = "/console/member/admin/input")
	public String adminDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		MemberAdmin memberAdmin=memberManageService.findAdminById(id);
		if (null==memberAdmin) memberAdmin = new MemberAdmin();
		
		MemberBase memberBase = memberAdmin.getMember();
		//通过memberId查找关联的company
		if(null!=memberBase){
			MemberCompany mc = new MemberCompany();
			mc.setMember(memberBase);
			List<MemberCompany> list = memberCompanyService.findByMemberCompany(mc);
			if(!list.isEmpty()){
				mc = list.get(0);
				model.put("company", mc.getCompany());
			}
		}else{
			memberBase = new MemberBase();
		}
		model.put("memberAdmin", memberAdmin);
		model.put("memberBase", memberAdmin.getMember());
		return "console/member/admin/admin_input";
	}
	

	/**
	 * 保存admin 信息
	 * @author qgfeng
	 * @date 2016-12-7
	 */
	@RequestMapping(value = "/console/member/admin/save")
	public void adminSave(HttpServletRequest request, HttpServletResponse response, ModelMap model, MemberAdminVO memberAdminVO) {
		MemberAdmin memberAdmin = memberManageService.findAdminById(memberAdminVO.getId());
		MemberBase memberBase = null;
		if(memberAdmin == null){
			memberBase = new MemberBase();
			memberAdmin = new MemberAdmin();
			memberAdmin.setMember(memberBase);
			memberAdmin.setType(memberAdminVO.getType());
			memberBase.setPassword(pwdEncoder.encodePassword(memberAdminVO.getPassword()));
			memberBase.setCreateTime(new Date());
		}else{
			memberBase = memberAdmin.getMember();
			if(StringUtils.isNotBlank(memberAdminVO.getRepassword())){
				memberBase.setPassword(pwdEncoder.encodePassword(memberAdminVO.getRepassword()));
			}
		}
		memberBase.setLoginCode(memberAdminVO.getLoginCode());
		memberBase.setNickName(memberAdminVO.getNickName());
		memberBase.setMobileCode(memberAdminVO.getMobileCode());
		memberBase.setMobileNumber(memberAdminVO.getMobileNumber());
		memberBase.setEmail(memberAdminVO.getEmail());
		memberBase.setIsValid(memberAdminVO.getIsValid());
		memberBase.setLastUpdate(new Date());
		if(StringUtils.isNotBlank(memberAdminVO.getStatus())){
			memberBase.setStatus(memberAdminVO.getStatus());
		} else {
			memberBase.setStatus("1");
		}
		
		String adminType = memberAdminVO.getType();
		if (!StringUtils.isNoneBlank(adminType))
			adminType = request.getParameter("adminType");
		if (null==adminType) adminType="";
		if("1".equals(adminType)){
			MemberIfafirm ifaFirm = ifafirmService.findById(memberAdminVO.getIfafirmId(),null);
			memberAdmin.setIfafirm(ifaFirm);
			memberAdmin.setDistributor(null);
			memberBase.setMemberType(2);
			memberBase.setSubMemberType(22);
			
		}else if("2".equals(adminType)){
			MemberDistributor distributor = distributorService.findDistributorById(memberAdminVO.getDistributorId());
			memberAdmin.setDistributor(distributor);
			memberAdmin.setIfafirm(null);
			memberBase.setMemberType(3);
			memberBase.setSubMemberType(31);
		}
		memberAdmin = memberManageService.saveOrUpdateAdmin(memberAdmin);
		if(null!=memberAdmin && null!=memberAdmin.getMember() && StringUtils.isNoneBlank(memberAdmin.getMember().getId())
				&& StringUtils.isNoneBlank(memberAdminVO.getCompanyId())){
			//如果memberCompany没有相关联的数据则插入
			String companyId = memberAdminVO.getCompanyId();
			CompanyInfo company = companyInfoService.findById(companyId);
			MemberBase member = memberAdmin.getMember();
			//如果存在，则删除旧记录
			memberCompanyService.deleteByMember(member.getId());
			//保存新关系
			MemberCompany memberCompany = new MemberCompany();
			memberCompany.setId(null);
			memberCompany.setMember(member);
			memberCompany.setCompany(company);
			memberCompanyService.saveOrUpdate(memberCompany);
		}
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
	
	/**
	 * 删除Memberadmin操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/console/member/admin/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String ids = request.getParameter("ids");
			String[] idArry = ids.split(",");
			for (String id : idArry) {
				if(StringUtils.isNotBlank(id)){
					memberManageService.deleteMemberAdmin(id);
				}
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}

	/**
	 * MemberAdmin启用/禁用操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/console/member/admin/validOperate", method = RequestMethod.POST)
	public void adminValidOperate(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String ids = request.getParameter("ids");
			String isValid = request.getParameter("isValid");
			String[] idArry = ids.split(",");
			for (String id : idArry) {
				if(StringUtils.isNotBlank(id)){
					MemberAdmin admin = memberManageService.findAdminById(id);
					if(admin!=null && admin.getMember()!=null){
						MemberBase base = admin.getMember();
						base.setIsValid(isValid);
						memberBaseService.saveOrUpdate(base);
					}
				}
			}
			obj.put("result",true);
		} catch (Exception e) {
			obj.put("result",false);
			e.printStackTrace();
		}finally{
			JsonUtil.toWriter(obj, response);
		}
	}
	
	//-------公共--------
	/**
	 * memberBase用户启用/禁用操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/console/member/validOperate", method = RequestMethod.POST)
	public void memberValidOperate(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String memberIds = request.getParameter("memberIds");
			String isValid = request.getParameter("isValid");
			String[] idArry = memberIds.split(",");
			if(idArry != null && idArry.length>0){
				for (String id : idArry) {
					if(StringUtils.isBlank(id)) continue;
					MemberBase base = memberManageService.findMemberBaseById(id);
					if(base != null){
						base.setIsValid(isValid);
						memberBaseService.saveOrUpdate(base);
					}
				}
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	
	/**
	 * 删除MemberBase用户（公共）
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/member/delete", method = RequestMethod.POST)
	public void deleteMember(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("memberId");
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean flag = memberManageService.deleteMember(id);
		if (flag) {
			obj.put("result", true);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		} else {
			obj.put("result", false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
	
	@RequestMapping(value = "/console/member/dialogExcel")
	public String excelInput(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String memberType = request.getParameter("memberType");
		model.put("memberType",memberType);
		return "console/member/dialog_member_excel";
	}
	
	/**
	 * 检查登录帐号的唯一性(公共)
	 * @author mqzou
	 * @date 2016-06-30
	 */
	@RequestMapping(value = "/console/member/checkCodeUnique", method = RequestMethod.POST)
	public void checkCodeUnique(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String loginCode = request.getParameter("loginCode");
		String userId = request.getParameter("userId");
		boolean result=memberManageService.checkLoginCode(loginCode, userId);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 工作台管理员分页列表（dirtributor）
	 * @author rqwang
	 * @date 2017-06-08
	 * @return
	 */
	@RequestMapping(value = "/console/member/admin/disAdminList")
	public String disAdminList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String disId = request.getParameter("id");
		model.put("id", disId);
		return "console/distributor/dis_admin_list";
	}

	/**
	 * 工作台管理员分页列表数据(dirtributor)
	 * @author rqwang
	 * @date 2017-06-08
	 */
	@RequestMapping(value = "/console/member/admin/disAdminListJson", method = RequestMethod.POST)
	public void disAdminListJson(HttpServletRequest request, HttpServletResponse response,MemberAdminVO memberAdmin) {
		String disId = request.getParameter("id");
		jsonPaging=this.getJsonPaging(request);
		jsonPaging = memberManageService.disFindMemberAdmin(jsonPaging,disId, memberAdmin,getLoginLangFlag(request));
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 工作台管理员分页列表详情(dirtributor)
	 * @author rqwang
	 * @date 2017-06-08
	 */
	@RequestMapping(value = "/console/member/admin/disAdminDetail")
	public String disAdminDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		MemberAdmin memberAdmin=memberManageService.findAdminById(id);
		if (null==memberAdmin) memberAdmin = new MemberAdmin();
		
		MemberBase memberBase = memberAdmin.getMember();
		//通过memberId查找关联的company
		if(null!=memberBase){
			MemberCompany mc = new MemberCompany();
			mc.setMember(memberBase);
			List<MemberCompany> list = memberCompanyService.findByMemberCompany(mc);
			if(!list.isEmpty()){
				mc = list.get(0);
				model.put("company", mc.getCompany());
			}
		}else{
			memberBase = new MemberBase();
		}
		model.put("memberAdmin", memberAdmin);
		model.put("memberBase", memberAdmin.getMember());
		return "console/distributor/dis_admin_detail";
	}
	
	
	/**
	 * 工作台管理员分页列表（IFAFirm）
	 * @author rqwang
	 * @date 2017-06-08
	 * @return
	 */
	@RequestMapping(value = "/console/member/admin/firmAdminList")
	public String firmAdminList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String firmId = request.getParameter("ifafirmId");
		model.put("ifafirmId", firmId);
		return "console/ifafirm/firm_admin_list";
	}

	/**
	 * 工作台管理员分页列表数据(IFAFirm)
	 * @author rqwang
	 * @date 2017-06-08
	 */
	@RequestMapping(value = "/console/member/admin/firmAdminListJson", method = RequestMethod.POST)
	public void firmAdminListJson(HttpServletRequest request, HttpServletResponse response,MemberAdminVO memberAdmin) {
		String firmId = request.getParameter("id");
		jsonPaging=this.getJsonPaging(request);
		jsonPaging = memberManageService.disFindMemberAdmin(jsonPaging,firmId, memberAdmin,getLoginLangFlag(request));
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 工作台管理员分页列表详情(IFAFirm)
	 * @author rqwang
	 * @date 2017-06-08
	 */
	@RequestMapping(value = "/console/member/admin/firmAdminDetail")
	public String firmAdminDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String firmId = request.getParameter("id");
		model.put("id", firmId);
		MemberAdmin memberAdmin=memberManageService.findAdminById(firmId);
		if (null==memberAdmin) memberAdmin = new MemberAdmin();
		
		MemberBase memberBase = memberAdmin.getMember();
		//通过memberId查找关联的company
		if(null!=memberBase){
			MemberCompany mc = new MemberCompany();
			mc.setMember(memberBase);
			List<MemberCompany> list = memberCompanyService.findByMemberCompany(mc);
			if(!list.isEmpty()){
				mc = list.get(0);
				model.put("company", mc.getCompany());
			}
		}else{
			memberBase = new MemberBase();
		}
		model.put("memberAdmin", memberAdmin);
		model.put("memberBase", memberAdmin.getMember());
		return "console/ifafirm/firm_admin_detail";
	}
	
	
	/**
	 * ifaFirm下的ifa详情
	 * @author rqwang
	 * @date 2017-06-09
	 */
	@RequestMapping(value = "/console/member/ifa/firmDetail")
	public String firmDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		MemberIfa ifa = memberManageService.finIfaById(id);
		IfaVO vo = new IfaVO();
		if (null!=ifa){
			vo = new IfaVO(ifa,null);
			String memberId = ifa.getMember().getId();
			//通过memberId查找关联的company
			if(StringUtils.isNotBlank(memberId)){
				MemberBase member = memberBaseService.findById(memberId);
				MemberCompany mc = new MemberCompany();
				mc.setMember(member);
				List<MemberCompany> list = memberCompanyService.findByMemberCompany(mc);
				if(null!=list && !list.isEmpty()){
					mc = list.get(0);
					if (null!=mc.getCompany())
						vo.setCompanyId(mc.getCompany().getId());
				}
			}
			
			//查询关联的ifafirm 返回ifafirm主键
			List<MemberIfaIfafirm> firmRelateArray = ifaManageService.findIfaIfaFirm(ifa.getId(), "1", "1", null, this.getLoginLangFlag(request));
			vo.setCompanyIfafirmId(firmRelateArray.isEmpty()?"":firmRelateArray.get(0).getIfafirm().getId());
			
		}
		model.put("ifavo", vo);
		return "console/ifafirm/firm_ifa_detail";
	}
	
	
	/**
	 * 设置客户分组
	 * @author wwluo
	 * @date 2017-06-09
	 */
	@RequestMapping(value = "/console/member/groupAdd")
	public String groupAdd(String memberIds, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = getLoginLangFlag(request);
		Map<String, Object> result = memberManageService.findMemberGroupInfo(memberIds, langCode);
		model.putAll(result);
		return "console/member/dialog_group_add";
	}
}
