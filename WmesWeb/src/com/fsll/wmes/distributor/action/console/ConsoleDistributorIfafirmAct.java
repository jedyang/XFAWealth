package com.fsll.wmes.distributor.action.console;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fsll.common.util.ChineseToEnglish;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.service.SysCountryService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.IfafirmDistributor;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.ProductIfafirmDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.ifafirm.service.IfaFirmForDistributorService;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.IfafirmDistributorVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.member.service.MemberIfafirmService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.product.service.ProductIfafirmDistributorService;

/**
 * 运营公司信息管理-公司IFA Firm设置
 * @author Yan
 * @version 1.0.0 Created On: 2016-12-06
 */

@Controller
@RequestMapping("/console/distributorifafirm")
public class ConsoleDistributorIfafirmAct extends WmesBaseAct {
	
	@Autowired
	private IfaFirmForDistributorService ifaFirmForDistributorService;

	@Autowired
	private MemberIfafirmService memberIfafirmService;
	
	@Autowired
	private SysCountryService SysCountryService;
	@Autowired
	private FundInfoService fundInfoService;
	
	@Autowired
	private DistributorService distributorService;
	@Autowired
	private ProductIfafirmDistributorService productIfafirmDistributorService;
	@Autowired
	private IfafirmManageService ifafirmService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
       return "console/distributor/distributorifafirm/ifafirm_list";//页面摆放路径
    }
    
    /**
     * 分页获得记录  modify by rqwang 2017-06-14
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String ifafirmName = request.getParameter("ifafirmName");
    	MemberAdmin member = this.getLoginMemberAdmin(request);
    	
    	//条件筛选
    	IfafirmDistributorVO infoVo = new IfafirmDistributorVO();
    	if(null!=member){
    		MemberDistributor distributor = member.getDistributor();
    		if(null!=distributor){
    			infoVo.setDistributor(distributor);
    		}
    	}
    	if(StringUtils.isNotBlank(ifafirmName)){
    		infoVo.setIfafirmName(ifafirmName);
    	}
    	
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = ifaFirmForDistributorService.findAll(jsonPaging, infoVo, langCode);
        List<IfafirmDistributorVO> list = new ArrayList<IfafirmDistributorVO>();
        List<Object> objList = jsonPaging.getList();
        for (int i = 0; i < objList.size(); i++) {
        	IfafirmDistributorVO vo = new IfafirmDistributorVO();
			Object[] objRead = (Object[])objList.get(i);
			IfafirmDistributor info = (IfafirmDistributor)objRead[0];
			String PlaceId = info.getIfafirm().getIncorporationPlace();
			if(null != PlaceId && StringUtils.isNotBlank(PlaceId)){
				SysCountry incorporationPlace = SysCountryService.findBycountryId(PlaceId);
				if("sc".equals(langCode)){
	    			MemberIfafirmSc ifafirmSc = (MemberIfafirmSc)objRead[1];
	    			if(!"".equals(ifafirmSc) && null!=ifafirmSc){
	    				vo.setIfafirmName(ifafirmSc.getCompanyName());
	    				info.getIfafirm().setIncorporationPlace(incorporationPlace.getNameSc());
	    			}
				}
				if("tc".equals(langCode)){
					MemberIfafirmTc ifafirmTc = (MemberIfafirmTc)objRead[1];
					if(!"".equals(ifafirmTc) && null!=ifafirmTc){
						vo.setIfafirmName(ifafirmTc.getCompanyName());
						info.getIfafirm().setIncorporationPlace(incorporationPlace.getNameTc());
					}
				}
				if("en".equals(langCode)){
					MemberIfafirmEn ifafirmEn = (MemberIfafirmEn)objRead[1];
					if(!"".equals(ifafirmEn) && null!=ifafirmEn){
						vo.setIfafirmName(ifafirmEn.getCompanyName());
						info.getIfafirm().setIncorporationPlace(incorporationPlace.getNameEn());
	    			}
				}
			}
			info.getIfafirm().setFirmLogo(PageHelper.getLogoUrl(info.getIfafirm().getFirmLogo(), "F"));
			BeanUtils.copyProperties(info,vo);
			list.add(vo);
		}
		jsonPaging.setList(list);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String id = request.getParameter("id");
    	MemberAdmin member = this.getLoginMemberAdmin(request);
    	if(null!=member){
    		MemberDistributor distributor = member.getDistributor();
    		model.put("distributor", distributor);
    	}
//    	List<CompanyInfo> companyVoList = companyInfoService.findAllCompany(langCode);
    	List<MemberIfafirm> memberIfafirmList = (List<MemberIfafirm>)memberIfafirmService.findAllMemberIfafirm(langCode);
//    	model.put("companyVoList", companyVoList);
    	model.put("memberIfafirmList", memberIfafirmList);
    	
    	if(!"".equals(id) && id!=null){
	        model.put("id", id);
	        IfafirmDistributorVO infoVo = (IfafirmDistributorVO)ifaFirmForDistributorService.findVoById(id, langCode);
	        model.put("infoVo", infoVo);
    	}
        return "console/distributor/distributorifafirm/input";
    }
    
   
	/**
	 * 新增/保存编辑
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");
		MemberAdmin member = this.getLoginMemberAdmin(request);
		MemberDistributor distributor = member.getDistributor();
//		String companyId = request.getParameter("companyId");
		String ifafirmId = request.getParameter("ifafirmId");
		
		//创建实体
		IfafirmDistributor info;
		if(null==id||"".equals(id)){
			info = new IfafirmDistributor();
			info.setId(null);
		} else {
			info = ifaFirmForDistributorService.findById(id);
		}
		if(null!=distributor){
			info.setDistributor(distributor);
		}else{
			info.setDistributor(null);
		}
		if(StringUtils.isNotBlank(ifafirmId)){
			MemberIfafirm ifafirm = memberIfafirmService.findById(ifafirmId);
			info.setIfafirm(ifafirm);
		}else{
			info.setIfafirm(null);
		}
		info = ifaFirmForDistributorService.saveOrUpdate(info);
			
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 删除  modify by rqwang 20170621 逻辑删除代理商的IFAFirm
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public void del(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");		
		ifaFirmForDistributorService.deleteById(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}

	//代理商-业务管理-投资顾问公司管理

	/**
     * 代理商的投资顾问公司的代理产品(基金)页面
     * @author rqawng
     * @date 2017-06-14
     */
    @RequestMapping(value = "/ifafirmFundList", method = RequestMethod.GET)
    public String ifafirmFund(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        //获取当前登录者信息代理商
        MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
        String disId = curMember.getDistributorId();//代理商id
        String ifaFirmId = request.getParameter("ifaFirmId");//投资顾问公司id
        if(null != disId && StringUtils.isNotBlank(disId) && null != ifaFirmId && StringUtils.isNotBlank(ifaFirmId)){
        	model.put("disId", disId);
        	model.put("ifaFirmId", ifaFirmId);
        }
        	
        return "console/distributor/distributorifafirm/ifafirm_fund";
    }
	
	/**
     * 代理商的投资顾问公司的代理产品(基金)数据
     * @author rqawng
     * @date 2017-06-14
     */
    @RequestMapping(value = "/ifafirmFundlistJson", method = RequestMethod.POST)
    public String ifafirmFundlistJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String keyword = StrUtils.getString(request.getParameter("keyword"));//基金编码，基金名称
    	String disId = request.getParameter("disId");//代理商id
        String ifaFirmId = request.getParameter("ifaFirmId");//投资顾问公司id
        
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = fundInfoService.findDisIfafirmFundInfo(jsonPaging, keyword,disId,ifaFirmId,langCode);
        this.toJsonString(response, jsonPaging);
        return null;
    }
	
    /**
     * 代理商的投资顾问公司的代理产品(基金)添加列表
     * @author rqawng 暂未使用
     * @date 2017-06-15
     */
    /*@RequestMapping(value = "/ifafirmFundListInput", method = RequestMethod.GET)
    public String ifafirmFundlistAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String disId = request.getParameter("disId");//代理商id
        String ifaFirmId = request.getParameter("ifaFirmId");//投资顾问公司id
        if(null != disId && StringUtils.isNotBlank(disId) && null != ifaFirmId && StringUtils.isNotBlank(ifaFirmId)){
        	model.put("disId", disId);
        	model.put("ifaFirmId", ifaFirmId);
        }
        return "console/distributor/distributorifafirm/ifafirm_fund_input";
    }*/
    
	/**
     * 代理商的投资顾问公司的代理产品(基金)添加列表数据
     * @author rqawng 暂未使用
     * @date 2017-06-15
     */
   /* @RequestMapping(value = "/ifafirmFundListInputJson", method = RequestMethod.POST)
    public String ifafirmFundlistAddJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String keyword = StrUtils.getString(request.getParameter("keyword"));//基金编码，基金名称
    	String disId = request.getParameter("disId");//代理商id
        String ifaFirmId = request.getParameter("ifaFirmId");//投资顾问公司id
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = fundInfoService.findDisIfafirmFundInfoInput(jsonPaging, keyword,disId,ifaFirmId,langCode);
        this.toJsonString(response, jsonPaging);
        return null;
    } */
   
    /**
     * 基金公司下拉列表框显示
     * @author rqawng 暂未使用
     * @date 2017-06-15
     */
    /*@RequestMapping(value = "/fundHouseNameList", method = RequestMethod.POST)
    public void riskLevelList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	//获取当前登录者信息代理商
        MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
        String disId = curMember.getDistributorId();//代理商id
        String langCode = this.getLoginLangFlag(request);
    	List<String> fundHouseName = fundInfoService.findRiskLevelList(disId,langCode);
    	String fundHouseNameJson = JsonUtil.toJson(fundHouseName);
    	Map<String, Object> obj = new HashMap<String, Object>();
    	obj.put("fundHouseName", fundHouseNameJson);
    	obj.put("result", true);
    	JsonUtil.toWriter(obj, response);
    }*/
    
    /**
     * 代理商所属ifaFirm添加基金
     * @author rqawng
     * @date 2017-06-16
     */
	@RequestMapping(value = "/saveFirmFund", method = RequestMethod.POST)
	public void saveFirmFund(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String disId = request.getParameter("disId");
		String ifaFirmId = request.getParameter("ifaFirmId");
		String ids = request.getParameter("ids");//基金的字符串的ids
		
		String[] fundIds = ids.split(",");
		for (String fundId : fundIds) {
			//创建实体
			ProductIfafirmDistributor info = new ProductIfafirmDistributor();
			if(null != disId && StringUtils.isNotBlank(disId) 
			&& null != ifaFirmId && StringUtils.isNotBlank(ifaFirmId) 
			&& null != fundId && StringUtils.isNotBlank(fundId)){
				MemberDistributor dis = distributorService.findDistributorById(disId);
				MemberIfafirm firm = memberIfafirmService.findById(ifaFirmId);
				FundInfo fund = fundInfoService.findFundInfoById(fundId);
				
				ProductInfo product = fund.getProduct();
				if(null != product ){
					info.setDistributor(dis);
					info.setIfafirm(firm);
					info.setProduct(product);
					info.setCreateTime(new Date());
					info = productIfafirmDistributorService.save(info);
				}
				
			}
		}

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	 /**
     * 代理商所属ifaFirm删除基金
     * @author rqawng
     * @date 2017-06-16
     */
	@RequestMapping(value = "/deleteFirmFund", method = RequestMethod.POST)
	public void deleteFirmFund(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String disId = request.getParameter("disId");
		String ifaFirmId = request.getParameter("ifaFirmId");
		String fundId = request.getParameter("fundId");
		FundInfo fund = fundInfoService.findFundInfoById(fundId);
		ProductInfo product = fund.getProduct();
		String productId = product.getId();
		//查出删除数据
		ProductIfafirmDistributor info = productIfafirmDistributorService.findProductIfafirmDistributorById(disId,ifaFirmId,productId);
		
		if(null != info.getId()){
			//根据id删除
			productIfafirmDistributorService.deleteById(info.getId());
		}
			
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**代理商添加投资顾问公司
	 * @author rqawng
     * @date 2017-06-20
	 */
	@RequestMapping(value = "disInputIfafirm")
	public String disInputIfafirm(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		return "console/distributor/distributorifafirm/ifafirm_input";
	}
	
	/**代理商添加投资顾问公司-保存
	 * @author rqawng
     * @date 2017-06-20
	 */
	@RequestMapping(value = "/saveFirminfo", method = RequestMethod.POST)
	public void saveFirmInfo(HttpServletRequest request,HttpServletResponse response,MemberIfafirmVO firmVO) {
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
		String disId = curMember.getDistributorId();//获取登录的代理商的id
		Map<String, Object> obj = new HashMap<String, Object>();

		
		boolean result = false;
		try {
			//保存MemberIfafirm
			MemberIfafirm firm = new MemberIfafirm();
			firm.setIsValid("1");
			firm.setEntityType(firmVO.getEntityType());
			firm.setRegisterNo(firmVO.getRegisterNo());
			firm.setIsFinancial(firmVO.getIsFinancial());
			firm.setGiin(firmVO.getGiin());
			firm.setNaturePurpose(firmVO.getNaturePurpose());
			
			String incorporationDateStr = firmVO.getIncorporationDateStr();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if(null != incorporationDateStr && StringUtils.isNotBlank(incorporationDateStr)){
				Date incorporationDate = df.parse(incorporationDateStr);
				firm.setIncorporationDate(incorporationDate);
			}
			
			firm.setIncorporationPlace(firmVO.getIncorporationPlace());
			firm.setRegisteredAddress(firmVO.getRegisteredAddress());
			firm.setMailingAddress(firmVO.getMailingAddress());
			firm.setCountry(firmVO.getCountry());
			firm.setWebsite(firmVO.getWebsite());
			firm.setFirmLogo(firmVO.getFirmLogo());
			firm = ifafirmService.saveOrUpdate(firm, true);
			
			//保存对应多语言
			if(firm != null && StringUtils.isNotBlank(firm.getId())){
				MemberIfafirmEn firmEn = ifafirmService.findIfafirmEnById(firm.getId());
				MemberIfafirmSc firmSc = ifafirmService.findIfafirmScById(firm.getId());
				MemberIfafirmTc firmTc = ifafirmService.findIfafirmTcById(firm.getId());
				if(firmEn==null){
					firmEn = new MemberIfafirmEn();
				}
				if(firmSc==null){
					firmSc = new MemberIfafirmSc();
				}
				if(firmTc==null){
					firmTc = new MemberIfafirmTc();
				}
				String pinYin = ChineseToEnglish.getPingYin(firmVO.getCompanyNameSc());
				firmEn.setId(firm.getId());
				firmEn.setPinYin(pinYin);
				firmEn.setCompanyName(firmVO.getCompanyNameEn());
				
				firmSc.setId(firm.getId());
				firmSc.setPinYin(pinYin);
				firmSc.setCompanyName(firmVO.getCompanyNameSc());
				
				firmTc.setId(firm.getId());
				firmTc.setPinYin(pinYin);
				firmTc.setCompanyName(firmVO.getCompanyNameTc());
				firmSc = ifafirmService.saveOrUpdateFirmSc(firmSc);
				firmTc = ifafirmService.saveOrUpdateFirmTc(firmTc);
				firmEn = ifafirmService.saveOrUpdateFirmEn(firmEn);
				result= true;
			}
			//保存代理商与ifaFirm的关系
			IfafirmDistributor firmDis = new IfafirmDistributor();
			MemberDistributor dis = distributorService.findDistributorById(disId);
			firmDis.setIfafirm(firm);
			firmDis.setDistributor(dis);
			firmDis = ifaFirmForDistributorService.saveOrUpdate(firmDis);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
}
