package com.fsll.wmes.ifafirm.action.console;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.service.SysCountryService;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.service.MemberCompanyService;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MemberInviteCode;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberInviteCodeService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.IndividualVO;

/**
 * IFA firm我的客户管理
 * @author Yan 2017-2-22
 */
@Controller
@RequestMapping(value = "/console/ifafirm/investor")
public class ConsoleIfafirmInvestor extends CoreBaseAct {
	
	@Autowired
	private SysCountryService sysCountryService;
	@Autowired
	private MemberManageServiceForConsole memberManageService;
	@Autowired
	private MemberInviteCodeService memberInviteCodeService;
	@Autowired
	private IfafirmManageService ifafirmManageService;
	@Autowired
	private IfaManageService ifaManageService;
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private MemberCompanyService memberCompanyService;
	@Autowired
	private CompanyInfoService companyInfoService;
	/** 
	 * 编辑
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String input(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id)){
			MemberIndividual obj = memberBaseService.findIndividualMember(id);
			if (null==obj) obj = new MemberIndividual();
			IndividualVO vo = new IndividualVO(obj);
			
			List<CrmCustomer> list = crmCustomerService.findCustomerByMember(id);
			if (null!=list && !list.isEmpty()){
				CrmCustomer crm = list.get(0);
				if (null!=crm && null!=crm.getIfa() && null!=crm.getIfa().getId())
					vo.setIfaId(StrUtils.getString(crm.getIfa().getId()));
			}
			model.put("individualvo", vo);
		}
		return "console/ifafirm/investor/input";
	}
	
	/**
	 * 生成随机数字和字母
	 * @param length
	 * @return
	 */
    public String getStringRandom(int length) {
        String val = "";  
        Random random = new Random();  
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }
	
    /**
     * 发送邀请码
     * @return
     */
    public Map<String, Object> sendInviteCode(HttpServletRequest request, HttpServletResponse response
    		, MemberBase fromMember, MemberBase toMember, String inviteCode, String password){
		Map<String, Object> obj = new HashMap<String, Object>();
	    boolean result = false;
		if(null != toMember.getEmail() && null != inviteCode){
			Date create = new Date();
			Calendar calendar = new GregorianCalendar(); 
		    calendar.setTime(create); 
		    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
		    Date expire = calendar.getTime();   //这个时间就是日期往后推一天的结果 
		    MemberInviteCode invite = null;
		    if(StringUtils.isNotBlank(toMember.getEmail())){
				//保存邀请记录
				try {
					MemberInviteCode code = new MemberInviteCode();
					code.setMember(fromMember);//发送人
					code.setToEmail(toMember.getEmail());
					code.setInviteCode(inviteCode);
					code.setCreateTime(create);
					code.setExpireTime(expire);
					code.setLastUpdate(create);
					code.setIsValid("1");
					code.setInviteType("2");//邀请码类型.0 投资者自己注册，1 ifa协助注册，2 ifa firm协助注册

					invite = memberInviteCodeService.saveOrUpdate(code);
					if(null != invite){
						result = true;
						obj.put("saveInvite",true);
						obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success.save",null));
					}
				} catch (Exception e) {
					// TODO: handle exception
					obj.put("saveInvite",false);
					obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed.save",null));
					e.printStackTrace();
				}
				
				//发送邮件
				if (result)
				try {
					result = false;
					StringBuilder message = new StringBuilder();
					message.append("Login Code："+toMember.getLoginCode()+";");
					message.append("Password："+password+";");
					message.append("Email："+toMember.getEmail()+".");
					String title = "Send invite code";
					//boolean sendFlag = new MailUtil().send(email, title, message);
					//发送邮件和写邮件日志
					WebEmailLog emailLog =sendEmail("inviteCode", fromMember, toMember, toMember.getEmail(), title, message.toString(), invite.getId());
					if(null!=emailLog){
						result = true;
						obj.put("sendInvite",true);
						obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
						ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
					}
				} catch (Exception e) {
					// TODO: handle exception
					obj.put("sendInvite",false);
					obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
					e.printStackTrace();
				}
			}
		}else{
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.required",new String[]{"Email and invite code"}));
		}
		obj.put("result",result);
//		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
		return obj;
    }
    
	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(IndividualVO individualvo,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		//获取当前登录管理员的Ifafirm
		if(null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(individualvo.getLoginCode())){
			try {
				//MemberBase
				MemberBase memberbase = memberBaseService.findById(individualvo.getMemberId());
				if (null==memberbase){
					memberbase = new MemberBase();
					memberbase.setCreateTime(new Date());
					memberbase.setStatus("1");	//改变激活状态
					memberbase.setIsValid("1");
				}
				memberbase.setPassword(pwdEncoder.encodePassword(individualvo.getPassword()));
				memberbase.setLoginCode(individualvo.getLoginCode());
				memberbase.setNickName(individualvo.getNickName());
				memberbase.setEmail(individualvo.getEmail());
				memberbase.setMobileCode(individualvo.getMobileCode());
				memberbase.setMobileNumber(individualvo.getMobileNumber());
				memberbase.setMemberType(1);	// 投资人
				memberbase.setSubMemberType(11);	// 独立投资账户
				memberbase.setLastUpdate(new Date());
				String inviteCode = this.getStringRandom(8);
				memberbase.setInviteCode(inviteCode);	//设置邀请码	
				memberbase = (MemberBase) memberBaseService.saveOrUpdate(memberbase);
				
				//MemberIndividual
				MemberIndividual individual = memberManageService.findIndividualById(individualvo.getId());
				if (null==individual){
					individual = new MemberIndividual();
					individual.setCreateTime(new Date());
				}
				individual.setFirstName(individualvo.getFirstName());
				individual.setLastName(individualvo.getLastName());
				individual.setBorn(individualvo.getBorn());
				individual.setCertNo(individualvo.getCertNo());
				individual.setCertType(individualvo.getCertType());
				individual.setEducation(individualvo.getEducation());
				individual.setEmployment(individualvo.getEmployment());
				individual.setGender(individualvo.getGender());
				individual.setNameChn(individualvo.getNameChn());
				individual.setOccupation(individualvo.getOccupation());
				individual.setTelephone(individualvo.getTelephone());
				individual.setAddress(individualvo.getAddress());
				individual.setLastUpdate(new Date());
				
				if (StringUtils.isNoneBlank(individualvo.getCountry())) {
					SysCountry country = sysCountryService.findBycountryId(individualvo.getCountry());
					individual.setCountry(country);
				}
				if (StringUtils.isNoneBlank(individualvo.getNationality())) {
					SysCountry country = sysCountryService.findBycountryId(individualvo.getNationality());
					individual.setNationality(country);
				}
				individual.setMember(memberbase);
				
				//保存账号及详细信息
				individual = memberManageService.saveIndividual(individual);
				if(individual != null){
					result = true;
					MemberBase base = individual.getMember();
					//获取当前登录ifafirm关联的运营公司
					MemberCompany firmMemberCompany = new MemberCompany();
					firmMemberCompany.setMember(curAdmin.getMember());
					List<MemberCompany> firmMemberCompanyList = memberCompanyService.findByMemberCompany(firmMemberCompany);
					CompanyInfo company = firmMemberCompanyList.get(0).getCompany();
					
					//独立投资人关联运营公司
					MemberCompany info = new MemberCompany();
					info.setCompany(company);
					info.setMember(base);
					List<MemberCompany> list = memberCompanyService.findByMemberCompany(info);
					if(list.isEmpty()){
						MemberCompany memberCompany = new MemberCompany();
						memberCompany.setId(null);
						memberCompany.setMember(base);
						memberCompany.setCompany(company);
						memberCompanyService.saveOrUpdate(memberCompany);
					}
					
					//CrmCustomer
					CrmCustomer customer = new CrmCustomer();
					//如有选定IFA，则保存crm记录，初始化为 准客户client_type=0，sales_stage_id=sales_new
					String ifaId = individualvo.getIfaId();
					if(!StrUtils.isEmpty(ifaId)){
						MemberIfa ifa = ifaManageService.findIfaById(ifaId);
						if (null!=ifa && !StrUtils.isEmpty(ifa.getId())
								&& null!=base && !StrUtils.isEmpty(base.getId())){
							customer.setIfa(ifa);
							customer.setMember(base);
							customer.setCreateTime(new Date());
							customer.setLastUpdate(new Date());
							customer.setIsValid("1");
							customer.setClientType("0");//准客户
							customer.setSalesStageId("sales_new");//新客户
							customer.setNickName(StrUtils.getString(base.getNickName(),base.getLoginCode()));
							crmCustomerService.saveCustomer(customer);
						}
					}
					
					
					
					//Send Invite Code
					obj = this.sendInviteCode(request, response, curAdmin.getMember(), base, inviteCode, individualvo.getPassword());
				}
			} catch (Exception e) {
				result = false;
				obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.exceptionThrew",null));
				e.printStackTrace();
			}
		}else{
			result = false;
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
		}
		obj.put("result", result);
		obj.put("data", "{}");
//		JsonUtil.toWriter(obj, response);
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * ifa分页列表
	 */
	@RequestMapping(value = "/getIfaInIfaFirm", method = RequestMethod.POST)
	public void getIfaInIfaFirm(HttpServletRequest request, HttpServletResponse response) {
	    Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		//获取当前登录管理员的Ifafirm
		if(null!=curAdmin.getIfafirm()){
			String firmId = curAdmin.getIfafirm().getId();
			//获取属于该Ifafirm的IFA
			if(StringUtils.isNotBlank(firmId)){
			    List<MemberIfa> ifaList =ifafirmManageService.getIFAKeyList(firmId, "");
				obj.put("ifaList", ifaList);
			}
			obj.put("result", true);
		}else{
			obj.put("ifaList", null);
			obj.put("result", false);
		}
		JsonUtil.toWriter(obj, response);
	}
}
