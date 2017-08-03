package com.fsll.wmes.fund.action.console;

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

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysCountry;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.FundAnno;
import com.fsll.wmes.entity.FundBonus;
import com.fsll.wmes.entity.FundDoc;
import com.fsll.wmes.entity.FundHouseEn;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.fund.service.FundDistributorService;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundHouseDataVO;
import com.fsll.wmes.fund.vo.FundScreenerDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.member.service.MemberDistributorService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.product.service.ProductInfoService;

/**
 * 控制器:基金信息管理（工作台）
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-6-28
 */

@Controller
@RequestMapping("/console/fund/info")
public class ConsoleFundInfoAct extends WmesBaseAct {
	
	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private ProductInfoService productInfoService;
	@Autowired
	private MemberDistributorService memberDistributorService;
	@Autowired
	private FundDistributorService fundDistributorService;
	
    /**
     * 基金列表-查询（后台版）
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/fund/info/list";//页面摆放路径
    }
    
    /**
     * 基金列表-查询（后台版） - 异步获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if (null!=admin && null!=admin.getDistributor()){
        	String langCode = this.getLoginLangFlag(request);
        	String keyword = request.getParameter("keyword");

            MemberDistributor distributor = admin.getDistributor();
            String distributorId = distributor.getId();

            jsonPaging = this.getJsonPaging(request);

            jsonPaging = fundInfoService.findAllFundInfo(jsonPaging, keyword,distributorId,langCode);
    	}else{
    		jsonPaging.setList(null);
		}
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面
     * Yan 2016-11-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	this.isMobileDevice(request,model);
    	//String langCode = this.getLoginLangFlag(request);
    	
    	String id = request.getParameter("id");
    	FundInfo info = fundInfoService.findFundInfoById(id);
    	FundInfoSc infoSc = fundInfoService.findFundInfoScById(id);
    	FundInfoEn infoEn = fundInfoService.findFundInfoEnById(id);
    	FundInfoTc infoTc = fundInfoService.findFundInfoTcById(id);
    	//获取注册地列表
    	List<SysCountry> domicileList = fundInfoService.getCountryList();
    	//获取发行公司列表
    	//List<FundHouseDataVO> fundHouseList = fundHouseService.getFundHouseVoList();
    	//获取货币列表
    	List<GeneralDataVO> currencyListSc = this.findSysParameters("currency_type","sc");
    	List<GeneralDataVO> currencyListTc = this.findSysParameters("currency_type","tc");
    	List<GeneralDataVO> currencyListEn = this.findSysParameters("currency_type","en");
    	//获取基金类型列表
    	List<GeneralDataVO> fundTypeListSc = this.findSysParameters("fund_type","sc");
    	List<GeneralDataVO> fundTypeListTc = this.findSysParameters("fund_type","tc");
    	List<GeneralDataVO> fundTypeListEn = this.findSysParameters("fund_type","en");
    	//获取基金主题分类列表
    	List<GeneralDataVO> fundSectorListSc = this.findSysParameters("fund_sector","sc");
    	List<GeneralDataVO> fundSectorListTc = this.findSysParameters("fund_sector","tc");
    	List<GeneralDataVO> fundSectorListEn = this.findSysParameters("fund_sector","en");
    	//获取交易市场列表
    	List<GeneralDataVO> regionListSc = this.findSysParameters("region","sc");
    	List<GeneralDataVO> regionListTc = this.findSysParameters("region","tc");
    	List<GeneralDataVO> regionListEn = this.findSysParameters("region","en");
    	
    	//获取基金分红派息数据
//    	List<GeneralDataVO> fundBonus = fundInfoService.findFundBonus(id);
//    	//获取基金附件文档
//    	List<GeneralDataVO> fundDoc = fundInfoService.findFundDocs(id, langCode);
    	
    	model.put("info", info);
    	model.put("infoSc", infoSc);
    	model.put("infoEn", infoEn);
    	model.put("infoTc", infoTc);
    	model.put("domicileList", domicileList);
    	//model.put("fundHouseList", fundHouseList);
    	
    	model.put("currencyListSc", currencyListSc);
    	model.put("currencyListTc", currencyListTc);
    	model.put("currencyListEn", currencyListEn);
    	
    	model.put("fundTypeListSc", fundTypeListSc);
    	model.put("fundTypeListTc", fundTypeListTc);
    	model.put("fundTypeListEn", fundTypeListEn);
    	
    	model.put("fundSectorListSc", fundSectorListSc);
    	model.put("fundSectorListTc", fundSectorListTc);
    	model.put("fundSectorListEn", fundSectorListEn);

    	model.put("regionListSc", regionListSc);
    	model.put("regionListTc", regionListTc);
    	model.put("regionListEn", regionListEn);
    	
//    	model.put("fundBonus", fundBonus);
//    	model.put("fundDoc", fundDoc);
    	
        return "console/fund/info/input";
    }
    
    /**
     * 保存
     * Yan 2016-11-25
     * @param request
     * @param response
     * @param model
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveEnObj(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		
		String fundId=request.getParameter("fundId");
		String isinCode=request.getParameter("isinCodeSc");
		String riskLevel=request.getParameter("riskLevelSc");
		String lastNav=request.getParameter("lastNavSc");
		String lastNavUpdate = request.getParameter("lastNavUpdateSc");
		String issuePrice=request.getParameter("issuePriceSc");
		String issueDate=request.getParameter("issueDateSc");
		String mgtFee=request.getParameter("mgtFeeSc");
		String dayReturn=request.getParameter("dayReturnSc");
		String subscribeFee=request.getParameter("subscribeFeeSc");
		String minInitialAmount=request.getParameter("minInitialAmountSc");
		String minSubscribeAmount=request.getParameter("minSubscribeAmountSc");
		String minHoldingAmount=request.getParameter("minHoldingAmountSc");
		String minRedemptionAmount=request.getParameter("minRedemptionAmountSc");
		String minRspAmount=request.getParameter("minRspAmountSc");
		String overhead=request.getParameter("overheadSc");
		
		//SC
		String fundNameSc=request.getParameter("fundNameSc");
		String fundNamePinyinSc=request.getParameter("fundNamePinyinSc");
		String domicileCodeSc = request.getParameter("domicileCodeSc");
		String domicileSc = request.getParameter("domicileSc");
		String fundCurrencyCodeSc = request.getParameter("fundCurrencyCodeSc");
		String fundCurrencySc=request.getParameter("fundCurrencySc");
		
		String fundHouseIdSc = request.getParameter("fundHouseIdSc");
		String fundHouseSc = request.getParameter("fundHouseSc");
		String fundHousePinyinSc = request.getParameter("fundHousePinyinSc");
		String fundManCoSc = request.getParameter("fundManCoSc");
		String fundManagerSc = request.getParameter("fundManagerSc");
		String fundSizeSc = request.getParameter("fundSizeSc");
		String fundTypeSc = request.getParameter("fundTypeSc");
		String fundTypeCodeSc = request.getParameter("fundTypeCodeSc");
		String sectorTypeSc = request.getParameter("sectorTypeSc");
		String sectorTypeCodeSc = request.getParameter("sectorTypeCodeSc");
		String geoAllocationSc = request.getParameter("geoAllocationSc");
		String geoAllocationCodeSc = request.getParameter("geoAllocationCodeSc");
		String invTargetSc = request.getParameter("invTargetSc");
		String keyRisksSc = request.getParameter("keyRisksSc");
		
		//TC
		String fundNameTc=request.getParameter("fundNameTc");
		String fundNamePinyinTc=request.getParameter("fundNamePinyinTc");
		String domicileCodeTc = request.getParameter("domicileCodeTc");
		String domicileTc = request.getParameter("domicileTc");
		String fundCurrencyCodeTc = request.getParameter("fundCurrencyCodeTc");
		String fundCurrencyTc=request.getParameter("fundCurrencyTc");
		
		String fundHouseIdTc = request.getParameter("fundHouseIdTc");
		String fundHouseTc = request.getParameter("fundHouseTc");
		String fundHousePinyinTc = request.getParameter("fundHousePinyinTc");
		String fundManCoTc = request.getParameter("fundManCoTc");
		String fundManagerTc = request.getParameter("fundManagerTc");
		String fundSizeTc = request.getParameter("fundSizeTc");
		String fundTypeTc = request.getParameter("fundTypeTc");
		String fundTypeCodeTc = request.getParameter("fundTypeCodeTc");
		String sectorTypeTc = request.getParameter("sectorTypeTc");
		String sectorTypeCodeTc = request.getParameter("sectorTypeCodeTc");
		String geoAllocationTc = request.getParameter("geoAllocationTc");
		String geoAllocationCodeTc = request.getParameter("geoAllocationCodeTc");
		String invTargetTc = request.getParameter("invTargetTc");
		String keyRisksTc = request.getParameter("keyRisksTc");
		
		//EN
		String fundNameEn=request.getParameter("fundNameEn");
		String fundNamePinyinEn=request.getParameter("fundNamePinyinEn");
		String domicileCodeEn = request.getParameter("domicileCodeEn");
		String domicileEn = request.getParameter("domicileEn");
		String fundCurrencyCodeEn = request.getParameter("fundCurrencyCodeEn");
		String fundCurrencyEn=request.getParameter("fundCurrencyEn");
		
		String fundHouseIdEn = request.getParameter("fundHouseIdEn");
		String fundHouseEn = request.getParameter("fundHouseEn");
		String fundHousePinyinEn = request.getParameter("fundHousePinyinEn");
		String fundManCoEn = request.getParameter("fundManCoEn");
		String fundManagerEn = request.getParameter("fundManagerEn");
		String fundSizeEn = request.getParameter("fundSizeEn");
		String fundTypeEn = request.getParameter("fundTypeEn");
		String fundTypeCodeEn = request.getParameter("fundTypeCodeEn");
		String sectorTypeEn = request.getParameter("sectorTypeEn");
		String sectorTypeCodeEn = request.getParameter("sectorTypeCodeEn");
		String geoAllocationEn = request.getParameter("geoAllocationEn");
		String geoAllocationCodeEn = request.getParameter("geoAllocationCodeEn");
		String invTargetEn = request.getParameter("invTargetEn");
		String keyRisksEn = request.getParameter("keyRisksEn");
		
		//按格式要求新增ID
		Date date = new Date();
		String dateFormat = DateUtil.getYYYYMMDDHHMMSSDate(date);
		String random = dateFormat + Math.round(Math.random()*10);
		String symbolCode = "F" + random;
		String productId = "FUND_" + random;
		
		FundInfo info = new FundInfo();
		FundInfoSc infoSc = new FundInfoSc();
		FundInfoEn infoEn = new FundInfoEn();
		FundInfoTc infoTc = new FundInfoTc();
		
		if(null==fundId||"".equals(fundId))
		{
			isAdd = true;
			info.setId(null);
			infoSc.setId(null);
			infoTc.setId(null);
			infoEn.setId(null);
			info.setCreateTime(date);
			ProductInfo product = new ProductInfo();
			product.setId(productId);
			product.setType("fund");
			//product.setSymbolCode(symbolCode);
			product.setCreateTime(date);
			product.setLastUpdate(date);
			product.setIsValid("1");
			product = productInfoService.saveOrUpdate(product,isAdd);
			info.setProduct(product);
		} else {
			isAdd = false;
			infoSc.setId(fundId);
			infoTc.setId(fundId);
			infoEn.setId(fundId);
			info = fundInfoService.findFundInfoById(fundId);
			infoSc = fundInfoService.findFundInfoScById(fundId);
			infoTc = fundInfoService.findFundInfoTcById(fundId);
			infoEn = fundInfoService.findFundInfoEnById(fundId);
		}
		
		//FundInfo
		info.setIsValid("1");
		info.setLastUpdate(new Date());
		info.setIsinCode(isinCode);
		
		if(!"".equals(riskLevel) && null != riskLevel){
			info.setRiskLevel(Integer.parseInt(riskLevel));
		}
		if(!"".equals(lastNav) && null != lastNav){
			info.setLastNav(Double.parseDouble(lastNav));
			if(!"".equals(lastNavUpdate)){
				info.setLastNavUpdate(DateUtil.getDate(lastNavUpdate));
			}
		}
		if(!"".equals(issuePrice) && null != issuePrice){
			info.setIssuePrice(Double.parseDouble(issuePrice));
		}
		if(!"".equals(issueDate) && null != issueDate){
			info.setIssueDate(DateUtil.getDate(issueDate));
		}
		if(!"".equals(mgtFee) && null != mgtFee){
			info.setMgtFee(Double.parseDouble(mgtFee));
		}
		
		if(!"".equals(dayReturn) && null != dayReturn){
			info.setDayReturn(Double.parseDouble(dayReturn));
		}
		if(!"".equals(subscribeFee) && null != subscribeFee){
			info.setSubscribeFee(Double.parseDouble(subscribeFee));
		}
		if(!"".equals(minInitialAmount) && null != minInitialAmount){
			info.setMinInitialAmount(Double.parseDouble(minInitialAmount));
		}
		if(!"".equals(minSubscribeAmount) && null != minSubscribeAmount){
			info.setMinSubscribeAmount(Double.parseDouble(minSubscribeAmount));
		}
		if(!"".equals(minHoldingAmount) && null != minHoldingAmount){
			info.setMinHoldingAmount(Double.parseDouble(minHoldingAmount));
		}
		if(!"".equals(minRedemptionAmount) && null != minRedemptionAmount){
			info.setMinRedemptionAmount(Double.parseDouble(minRedemptionAmount));
		}
		if(!"".equals(minRspAmount) && null != minRspAmount){
			info.setMinRspAmount(Double.parseDouble(minRspAmount));
		}
		if(!"".equals(overhead) && null != overhead){
			info.setOverhead(overhead);
			info.setOverheadTime(new Date());
		}
		
		//Sc
		infoSc.setFundName(fundNameSc);
		infoSc.setFundNamePinyin(fundNamePinyinSc);
		infoSc.setDomicileCode(domicileCodeSc);
		infoSc.setDomicile(domicileSc);
		infoSc.setFundCurrencyCode(fundCurrencyCodeSc);
		infoSc.setFundCurrency(fundCurrencySc);
		
//		if(StringUtils.isNoneBlank(fundHouseIdSc)){
//			FundHouse fundhouse = fundHouseService.findFundHouseById(fundHouseIdSc);
//			infoSc.setFundHouseId(fundhouse);
//		}
		infoSc.setFundHouse(fundHouseSc);
		infoSc.setFundHousePinyin(fundHousePinyinSc);
		infoSc.setFundManCo(fundManCoSc);
		infoSc.setFundManager(fundManagerSc);
		infoSc.setFundSize(fundSizeSc);
		infoSc.setFundTypeCode(fundTypeCodeSc);
		infoSc.setFundType(fundTypeSc);
		infoSc.setSectorTypeCode(sectorTypeCodeSc);
		infoSc.setSectorType(sectorTypeSc);
		infoSc.setGeoAllocationCode(geoAllocationCodeSc);
		infoSc.setGeoAllocation(geoAllocationSc);
		infoSc.setInvTarget(invTargetSc);
		infoSc.setKeyRisks(keyRisksSc);
				
		//Tc
		infoTc.setFundName(fundNameTc);
		infoTc.setFundNamePinyin(fundNamePinyinTc);
		infoTc.setDomicileCode(domicileCodeTc);
		infoTc.setDomicile(domicileTc);
		infoTc.setFundCurrencyCode(fundCurrencyCodeTc);
		infoTc.setFundCurrency(fundCurrencyTc);
		
//		if(StringUtils.isNoneBlank(fundHouseIdTc)){
//			FundHouse fundhouse = fundHouseService.findFundHouseById(fundHouseIdTc);
//			infoTc.setFundHouseId(fundhouse);
//		}
		infoTc.setFundHouse(fundHouseTc);
		infoTc.setFundHousePinyin(fundHousePinyinTc);
		infoTc.setFundManCo(fundManCoTc);
		infoTc.setFundManager(fundManagerTc);
		infoTc.setFundSize(fundSizeTc);
		infoTc.setFundTypeCode(fundTypeCodeTc);
		infoTc.setFundType(fundTypeTc);
		infoTc.setSectorTypeCode(sectorTypeCodeTc);
		infoTc.setSectorType(sectorTypeTc);
		infoTc.setGeoAllocationCode(geoAllocationCodeTc);
		infoTc.setGeoAllocation(geoAllocationTc);
		infoTc.setInvTarget(invTargetTc);
		infoTc.setKeyRisks(keyRisksTc);
		
		//En
		infoEn.setFundName(fundNameEn);
		infoEn.setFundNamePinyin(fundNamePinyinEn);
		infoEn.setDomicileCode(domicileCodeEn);
		infoEn.setDomicile(domicileEn);
		infoEn.setFundCurrencyCode(fundCurrencyCodeEn);
		infoEn.setFundCurrency(fundCurrencyEn);
		
//		if(StringUtils.isNoneBlank(fundHouseIdEn)){
//			FundHouse fundhouse = fundHouseService.findFundHouseById(fundHouseIdEn);
//			infoEn.setFundHouseId(fundhouse);
//		}
		infoEn.setFundHouse(fundHouseEn);
		infoEn.setFundHousePinyin(fundHousePinyinEn);
		infoEn.setFundManCo(fundManCoEn);
		infoEn.setFundManager(fundManagerEn);
		infoEn.setFundSize(fundSizeEn);
		infoEn.setFundTypeCode(fundTypeCodeEn);
		infoEn.setFundType(fundTypeEn);
		infoEn.setSectorTypeCode(sectorTypeCodeEn);
		infoEn.setSectorType(sectorTypeEn);
		infoEn.setGeoAllocationCode(geoAllocationCodeEn);
		infoEn.setGeoAllocation(geoAllocationEn);
		infoEn.setInvTarget(invTargetEn);
		infoEn.setKeyRisks(keyRisksEn);
		
		info = fundInfoService.save(info, infoSc, infoTc, infoEn, isAdd);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 删除产品和代理商关系
     * Yan 2016-11-25
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public void del(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String id=request.getParameter("id");
		MemberAdmin admin = this.getLoginMemberAdmin(request);
		if (null!=admin && null!=admin.getDistributor()){
	        MemberDistributor distributor = admin.getDistributor();
	        String distributorId = distributor.getId();
			
	        fundDistributorService.delFundDistributor(id, distributorId);//删除代理商与产品的关系记录
	        
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else{
			obj.put("result",false);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/********************** Fund Anno ********************************/
	
	/**
     * 详细信息页面
     * Yan 2016-11-25
     */
    @RequestMapping(value = "/fundAnnoList", method = RequestMethod.GET)
    public String fundAnnoList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	this.isMobileDevice(request,model);
    	String fundId = request.getParameter("fundId");
    	model.put("fundId", fundId);
    	return "console/fund/info/anno/list";
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/fundAnnoListJson", method = RequestMethod.POST)
    public String fundAnnoListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String fundId = request.getParameter("fundId");
    	//String langCode = this.getLoginLangFlag(request);  	
        jsonPaging = this.getJsonPaging(request);
        if(StringUtils.isNoneBlank(fundId)){
        	jsonPaging = fundInfoService.findFundAnnoList(jsonPaging,fundId, "");
        }
        model.put("fundId", fundId);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/fundAnnoDetail", method = RequestMethod.GET)
    public String fundAnnoDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String fundId = request.getParameter("fundId");
    	String id = request.getParameter("id");
        FundAnno info = fundInfoService.findFundAnno(id);
        model.put("fundId", fundId);
        model.addAttribute("info", info);        
        return "console/fund/info/anno/input";
    }
    
    /**
     * 保存
     * Yan 2016-11-25
     * @param request
     * @param response
     * @param model
     */
	@RequestMapping(value = "/saveAnno", method = RequestMethod.POST)
	public void saveAnno(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;		
		String fundId=request.getParameter("fundId");
		String id = request.getParameter("id");
		String langCode=request.getParameter("langCode");
		String annoName = request.getParameter("annoName");
		String annoContent=request.getParameter("annoContent");
		String annoDate = request.getParameter("annoDate");
		
		FundAnno anno = new FundAnno();
		if(StringUtils.isNoneBlank(id)){
			anno = fundInfoService.findFundAnno(id);
			anno.setLastUpdate(new Date());
		} else if(StringUtils.isEmpty(id) && StringUtils.isNoneBlank(fundId)){
			isAdd = true;
			FundInfo info = fundInfoService.findFundInfoById(fundId);
			anno.setFundId(fundId);
			anno.setFund(info);
			anno.setCreateTime(new Date());
			anno.setLastUpdate(new Date());
		}
		anno.setIsValid("1");
		anno.setLangCode(langCode);
		anno.setAnnoName(annoName);
		anno.setAnnoContent(annoContent);
		anno.setAnnoDate(DateUtil.getDate(annoDate));
		
		anno = fundInfoService.saveAnno(anno, isAdd);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/********************** Fund Bonus ********************************/
	
	/**
     * 详细信息页面
     * Yan 2016-11-25
     */
    @RequestMapping(value = "/fundBonusList", method = RequestMethod.GET)
    public String fundBonusList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	this.isMobileDevice(request,model);
    	String fundId = request.getParameter("fundId");
    	model.put("fundId", fundId);
    	return "console/fund/info/bonus/list";
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/fundBonusListJson", method = RequestMethod.POST)
    public String fundBonusListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String fundId = request.getParameter("fundId");
    	//String langCode = this.getLoginLangFlag(request);  	
        jsonPaging = this.getJsonPaging(request);
        if(StringUtils.isNoneBlank(fundId)){
        	jsonPaging = fundInfoService.findFundBonusList(jsonPaging,fundId);
        }
        model.put("fundId", fundId);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/fundBonusDetail", method = RequestMethod.GET)
    public String fundBonusDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String fundId = request.getParameter("fundId");
    	String id = request.getParameter("id");
        FundBonus info = fundInfoService.findFundBonusById(id);
        model.put("fundId", fundId);
        model.addAttribute("info", info);        
        return "console/fund/info/bonus/input";
    }
    
    /**
     * 保存
     * Yan 2016-11-25
     * @param request
     * @param response
     * @param model
     */
	@RequestMapping(value = "/saveBonus", method = RequestMethod.POST)
	public void saveBonus(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;		
		String fundId=request.getParameter("fundId");
		String id = request.getParameter("id");
		String year=request.getParameter("year");
		String exDividendDate = request.getParameter("exDividendDate");
		String dividendPerUnit=request.getParameter("dividendPerUnit");
		String annualDividendYield = request.getParameter("annualDividendYield");
		
		FundBonus bonus = new FundBonus();
		if(StringUtils.isNoneBlank(id)){
			bonus = fundInfoService.findFundBonusById(id);
			bonus.setLastUpdate(new Date());
		} else if(StringUtils.isEmpty(id) && StringUtils.isNoneBlank(fundId)){
			isAdd = true;
			FundInfo info = fundInfoService.findFundInfoById(fundId);
			bonus.setFund(info);
			bonus.setCreateTime(new Date());
			bonus.setLastUpdate(new Date());
		}
		bonus.setIsValid("1");
		bonus.setYear(Integer.parseInt(year));
		bonus.setExDividendDate(DateUtil.getDate(exDividendDate));
		bonus.setDividendPerUnit(Double.parseDouble(dividendPerUnit));
		bonus.setAnnualDividendYield(Double.parseDouble(annualDividendYield));
		
		bonus = fundInfoService.saveBonus(bonus, isAdd);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/********************** Fund Doc ********************************/
	
	/**
     * 详细信息页面
     * Yan 2016-11-25
     */
    @RequestMapping(value = "/fundDocList", method = RequestMethod.GET)
    public String fundDocList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	this.isMobileDevice(request,model);
    	String fundId = request.getParameter("fundId");
    	model.put("fundId", fundId);
    	return "console/fund/info/doc/list";
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/fundDocListJson", method = RequestMethod.POST)
    public String fundDocListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String fundId = request.getParameter("fundId");	
        jsonPaging = this.getJsonPaging(request);
        if(StringUtils.isNoneBlank(fundId)){
        	jsonPaging = fundInfoService.findFundDocList(jsonPaging,fundId);
        }
        model.put("fundId", fundId);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/fundDocDetail", method = RequestMethod.GET)
    public String fundDocDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String fundId = request.getParameter("fundId");
    	String id = request.getParameter("id");
        FundDoc info = fundInfoService.findFundDoc(id);
        List<AccessoryFile> accessoryList = fundInfoService.findAccessoryList(id);
        if(!accessoryList.isEmpty()){
        	AccessoryFile accessory = accessoryList.get(0);
            model.put("accessory", accessory);
        }
        model.put("fundId", fundId);
        model.addAttribute("info", info);
        return "console/fund/info/doc/input";
    }
    
    /**
     * 保存
     * Yan 2016-11-25
     * @param request
     * @param response
     * @param model
     */
	@RequestMapping(value = "/saveDoc", method = RequestMethod.POST)
	public void saveDoc(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;		
		String fundId=request.getParameter("fundId");
		String id = request.getParameter("id");
		String documentName=request.getParameter("documentName");
		
		FundDoc doc = new FundDoc();
		if(StringUtils.isNoneBlank(id)){
			doc = fundInfoService.findFundDoc(id);
			doc.setLastUpdate(new Date());
		} else if(StringUtils.isEmpty(id) && StringUtils.isNoneBlank(fundId)){
			isAdd = true;
			FundInfo info = fundInfoService.findFundInfoById(fundId);
			doc.setFund(info);
			doc.setCreateTime(new Date());
			doc.setLastUpdate(new Date());
		}
		doc.setIsValid("1");
		doc.setDocumentName(documentName);
		
		doc = fundInfoService.saveDoc(doc, isAdd);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 删除
     * Yan 2016-11-25
	 */
	@RequestMapping(value = "/delFile", method = RequestMethod.POST)
	public void delFile(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		if("anno".equals(type)){
			fundInfoService.updateFileValidById(id,type);
		}
		if("bonus".equals(type)){
			fundInfoService.updateFileValidById(id,type);
		}
		if("doc".equals(type)){
			fundInfoService.updateFileValidById(id,type);
		}
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
    /**
     * 基金筛选列表（前台版）
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/fundScreener", method = RequestMethod.GET)
    public String fundScreener(HttpServletRequest request, HttpServletResponse response, ModelMap model, String select, String showCart) {
        this.isMobileDevice(request, model);
        String lang = this.getLoginLangFlag(request);
        try {
            //List<SysParamConfig> currencyList = sysParamService.findParamConfigByType("currency_type");
        	List<GeneralDataVO> currencyList = findSysParameters("currency_type",lang);
            model.put("currencyList", currencyList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
	        List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(lang);
	        model.put("fundHouseList", fundHouseList);
		} catch (Exception e) {
			// TODO: handle exception
		}


		//select参数用于作为基金选择功能
    	select = StrUtils.getString(select);
    	if ("true".equals(select)) model.put("select", select);
		
    	//showCart参数用于控制是否显示购物车
    	showCart = StrUtils.getString(showCart);
    	if ("true".equals(showCart)) model.put("showCart", "true");
    	else model.put("showCart", "false");
    	
		String displayColor = "";
		MemberBase loginUser = getLoginUser(request);
		if(null != loginUser){ 
			MemberSsoVO ssoVO = getLoginUserSSO(request);
			String individualId = ssoVO.getIndividualId();
			if(null != individualId && !"".equals(individualId)){
				model.put("individualId", individualId);
			}
			
//			String memberId = loginUser.getId();
//			try {
//		        List<PortfolioHold> portfolioHoldList = portfolioHoldService.findMyPortfolioHoldList(memberId);
//		        model.put("portfolioHoldList", portfolioHoldList);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
			
			displayColor = loginUser.getDefDisplayColor();
		}
		if(null==displayColor || "".equals(displayColor)){
		    displayColor=CommonConstants.DEF_DISPLAY_COLOR;
		}
		model.put("displayColor", displayColor);
		model.put("defCurrency", loginUser.getDefCurrency());
    	
		return "console/fund/info/fundScreener";
    }
    
    /**
     * 基金筛选列表（前台版） -- 分页获得记录for ajax Json 调用
     * 	  与fundsscreener()一样
     * @author michael
     * @param request
     * @param response
     * @param model
     * @param filters 筛选条件
     * @return
     */
    @RequestMapping(value = "/getFundListJson", method = RequestMethod.POST)
    public void getFundListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model, FundScreenerDataVO filters) {
        MemberAdmin admin = this.getLoginMemberAdmin(request);
        if (null!=admin){
	        String lang = this.getLoginLangFlag(request);
	        List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(lang);
	        model.put("jsonPaging", jsonPaging);
	        model.put("fundHouseList_in", fundHouseList);
	
	        //工作台
	        MemberIfafirm ifafirm = admin.getIfafirm();
	        MemberDistributor distributor = admin.getDistributor();
	        
	        if (null!=ifafirm && !StrUtils.isEmpty(ifafirm.getId()))
	        	filters.setIfaFirmId(ifafirm.getId());
	        
	        if (null!=distributor && !StrUtils.isEmpty(distributor.getId()))
	        	filters.setDistributorId(distributor.getId());	 
	        
	        jsonPaging = queryFundsByFilters(request, model, filters);
        }else{
			jsonPaging.setList(null);
		}
        ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
    }
    
    /**
     * 根据多条件筛选基金
     * @author michael
     * @param request
     * @param model
     * @param filters 筛选条件
     * @return
     */
    private JsonPaging queryFundsByFilters(HttpServletRequest request, ModelMap model, FundScreenerDataVO filters)
    {
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if (null!=admin){
	        MemberIfafirm ifafirm = admin.getIfafirm();
	        MemberDistributor distributor = admin.getDistributor();
	        
	        //如未设置则按会话设置
	        if (null!=ifafirm && !StrUtils.isEmpty(ifafirm.getId()) && !StrUtils.isEmpty(filters.getIfaFirmId()))
	        	filters.setIfaFirmId(ifafirm.getId());
	        
	        //如未设置则按会话设置
	        if (null!=distributor && !StrUtils.isEmpty(distributor.getId()) && !StrUtils.isEmpty(filters.getDistributorId()))
	        	filters.setDistributorId(distributor.getId());
	        
	        filters.setLoginUser(this.getLoginUser(request));
	        filters.setKeyword(toUTF8String(filters.getKeyword()));
	        filters.setFundName(toUTF8String(filters.getFundName()));
	        filters.setDomicile(toUTF8String(filters.getDomicile()));
	        filters.setCurrency(toUTF8String(filters.getCurrency()));
	//        filters.setType(toUTF8String(fundType));
	        filters.setFundType(toUTF8String(filters.getFundType()));
	        filters.setSectorType(toUTF8String(filters.getSectorType()));
	        filters.setGeoAllocation(toUTF8String(filters.getGeoAllocation()));
	        filters.setFundHouseIds(StrUtils.getString(filters.getFundHouseIds()));
	        model.put("filters", filters);
	
	        
	        jsonPaging = this.getJsonPaging(request);
	        if("riskLevel".equals(jsonPaging.getSort())){
	        	jsonPaging.setSort("f.riskLevel");
	        }
	        jsonPaging = fundInfoService.findFundInfoList(jsonPaging, filters);
    	}else{
    		jsonPaging.setList(null);
		}
        return jsonPaging;
    }
    
}
