package com.fsll.wmes.fund.action.console;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.distributor.service.MemberDistributorService;
import com.fsll.wmes.entity.FundAnno;
import com.fsll.wmes.entity.FundBonus;
import com.fsll.wmes.entity.FundDoc;
import com.fsll.wmes.entity.FundHouse;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.fund.service.FundHouseService;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundHouseDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
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
	private FundHouseService fundHouseService;
	@Autowired
	private AccessoryFileService accessoryFileService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
//        String fundId = request.getParameter("fundId");
//        String productId ;
//        if(StringUtils.isNotBlank(fundId)){
//        	FundInfo info = fundInfoService.findFundInfoById(fundId);
//        	if(!"".equals(info)&&null!=info){
//        		productId = info.getProduct().getId();
//        		model.put("productId", productId);
//        	}
//        }
        
    	List<MemberDistributor> distributorList = memberDistributorService.findAllDistributors();
    	model.put("distributorList", distributorList);
    	SysAdmin admin = this.getLoginUser(request);
        if(null != admin){
        	return "console/fund/info/list";//页面摆放路径
        }else{
        	return "redirect:"+this.getFullPath(request)+"viewLogin.do";
        }
    }
    
    /**
     * 分页获得基金记录 modify by rqwang 20170601
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String keyword = StrUtils.getString(request.getParameter("keyword"));
    	String distributorId = StrUtils.getString(request.getParameter("distributorId")); 
    	
    	String riskLevelList = StrUtils.getString(request.getParameter("riskLevelList"));
    	Integer level = null;
    	if(!"".equals(riskLevelList)){
    		level = Integer.valueOf(riskLevelList).intValue();
    	}
    	
        jsonPaging = this.getJsonPaging(request);

        jsonPaging = fundInfoService.findAllFundInfo(jsonPaging, keyword, level, distributorId,langCode);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 风险级别关键字列表显示
     * @author rqwang 20170601
     */
    @RequestMapping(value = "/riskLevelList", method = RequestMethod.POST)
    public void riskLevelList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	List<Integer> riskLevelList = fundInfoService.findRiskLevelList();
    	String riskLevelListJson = JsonUtil.toJson(riskLevelList);
    	Map<String, Object> obj = new HashMap<String, Object>();
    	obj.put("riskLevelList", riskLevelListJson);
    	obj.put("result", true);
    	JsonUtil.toWriter(obj, response);
    }
    
    /**
     * 代理商的代理产品页面(基金)
     * @author rqwang 20170608
     */
    @RequestMapping(value = "/disFundList", method = RequestMethod.GET)
    public String disFundList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        
    	List<MemberDistributor> distributorList = memberDistributorService.findAllDistributors();
    	model.put("distributorList", distributorList);
    	String disId = request.getParameter("id");
		model.put("id", disId);
    	SysAdmin admin = this.getLoginUser(request);
        if(null != admin){
        	return "console/distributor/dis_fund_list";//页面摆放路径
        }else{
        	return "redirect:"+this.getFullPath(request)+"viewLogin.do";
        }
    }
    
    
    /**
     * 代理商的代理产品页面(基金)数据
     * @author rqwang 20170608
     */
    @RequestMapping(value = "/disListJson", method = RequestMethod.POST)
    public String disFundListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	
    	String langCode = this.getLoginLangFlag(request);
    	String keyword = StrUtils.getString(request.getParameter("keyword"));
    	String distributorId = StrUtils.getString(request.getParameter("distributorId")); 
    	String riskLevelList = StrUtils.getString(request.getParameter("riskLevelList"));
    	String disId = request.getParameter("id");
    	Integer level = null;
    	if(!"".equals(riskLevelList)){
    		level = Integer.valueOf(riskLevelList).intValue();
    	}
    	
        jsonPaging = this.getJsonPaging(request);

        jsonPaging = fundInfoService.disFindAllFundInfo(jsonPaging, keyword, level,disId, distributorId,langCode);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 代理商的代理产品(基金)详情页面
     * @author rqwang 20170608
     */
    @RequestMapping(value = "/disFundInfo", method = RequestMethod.GET)
    public String disFundInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	this.isMobileDevice(request,model);
    	String id = request.getParameter("id");
    	FundInfo info = fundInfoService.findFundInfoById(id);
    	FundInfoSc infoSc = fundInfoService.findFundInfoScById(id);
    	FundInfoEn infoEn = fundInfoService.findFundInfoEnById(id);
    	FundInfoTc infoTc = fundInfoService.findFundInfoTcById(id);
    	//获取注册地列表
    	List<SysCountry> domicileList = fundInfoService.getCountryList();
    	//获取发行公司列表
    	List<FundHouseDataVO> fundHouseList = fundHouseService.getFundHouseVoList();
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
    	
    	if(StringUtils.isBlank(id)){
    		String uuid = UUID.randomUUID().toString();
    		uuid = uuid.replace("-", "");
    		model.put("id", uuid);
    		model.put("isAdd", "true");
    	}else{
    		model.put("id",info.getId());
    		model.put("isAdd","false");
    	}
    	model.put("info", info);
    	model.put("infoSc", infoSc);
    	model.put("infoEn", infoEn);
    	model.put("infoTc", infoTc);
    	model.put("domicileList", domicileList);
    	model.put("fundHouseList", fundHouseList);
    	
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
    	
        return "console/distributor/dis_fund_info";
    }
    
    
    
    
    
    /**
     * 详细信息页面
     * Yan 2016-11-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String Pagedetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
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
    	List<FundHouseDataVO> fundHouseList = fundHouseService.getFundHouseVoList();
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
    	
    	if(StringUtils.isBlank(id)){
    		String uuid = UUID.randomUUID().toString();
    		uuid = uuid.replace("-", "");
    		model.put("id", uuid);
    		model.put("isAdd", "true");
    	}else{
    		model.put("id",info.getId());
    		model.put("isAdd","false");
    	}
    	model.put("info", info);
    	model.put("infoSc", infoSc);
    	model.put("infoEn", infoEn);
    	model.put("infoTc", infoTc);
    	model.put("domicileList", domicileList);
    	model.put("fundHouseList", fundHouseList);
    	
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
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String add = request.getParameter("isAdd");
		boolean isAdd = false;
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
		//String overhead=request.getParameter("overheadSc");
		
		//SC
		String fundNameSc=request.getParameter("fundNameSc");
		String fundNamePinyinSc=request.getParameter("fundNamePinyinSc");
		String domicileCodeSc = request.getParameter("domicileCodeSc");
		String domicileSc = request.getParameter("domicileSc");
		String fundCurrencyCodeSc = request.getParameter("fundCurrencyCodeSc");
		String fundCurrencySc=request.getParameter("fundCurrencySc");
		//当基金货币为空时为基金货币设置默认值
		if(StringUtils.isBlank(fundCurrencyCodeSc)){
			fundCurrencyCodeSc = "USD";
			fundCurrencySc = "美元";
		}
		
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
		//当基金货币为空时为基金货币设置默认值
		if(StringUtils.isBlank(fundCurrencyCodeTc)){
			fundCurrencyCodeTc = "USD";
			fundCurrencyTc = "美元";
		}
		
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
		//当基金货币为空时为基金货币设置默认值
		if(StringUtils.isBlank(fundCurrencyCodeTc)){
			fundCurrencyCodeTc = "USD";
			fundCurrencyTc = "USD";
		}
		
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
		//String symbolCode = "F" + random;
		String productId = "FUND_" + random;
		
		FundInfo info = new FundInfo();
		FundInfoSc infoSc = new FundInfoSc();
		FundInfoEn infoEn = new FundInfoEn();
		FundInfoTc infoTc = new FundInfoTc();
		
		if("true".equals(add)){
			isAdd = true;
			info.setId(null);
			infoSc.setId(null);
			infoTc.setId(null);
			infoEn.setId(null);
			info.setCreateTime(date);
			ProductInfo product = new ProductInfo();
			product.setId(productId);
			product.setType("fund");
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
		if(StringUtils.isNotBlank(isinCode))
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
		/*if(!"".equals(overhead) && null != overhead){
			info.setOverhead(overhead);
			info.setOverheadTime(new Date());
		}*/
		
		//Sc
		infoSc.setFundName(fundNameSc);
		infoSc.setFundNamePinyin(fundNamePinyinSc);
		infoSc.setDomicileCode(domicileCodeSc);
		infoSc.setDomicile(domicileSc);
		infoSc.setFundCurrencyCode(fundCurrencyCodeSc);
		infoSc.setFundCurrency(fundCurrencySc);
		
		if(StringUtils.isNoneBlank(fundHouseIdSc)){
			FundHouse fundhouse = fundHouseService.findFundHouseById(fundHouseIdSc);
			infoSc.setFundHouseId(fundhouse);
		}
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
		
		if(StringUtils.isNoneBlank(fundHouseIdTc)){
			FundHouse fundhouse = fundHouseService.findFundHouseById(fundHouseIdTc);
			infoTc.setFundHouseId(fundhouse);
		}
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
		
		if(StringUtils.isNoneBlank(fundHouseIdEn)){
			FundHouse fundhouse = fundHouseService.findFundHouseById(fundHouseIdEn);
			infoEn.setFundHouseId(fundhouse);
		}
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
	 * 删除
     * Yan 2016-11-25
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public void del(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String id=request.getParameter("id");
		FundInfo info = fundInfoService.findFundInfoById(id);
		if((null!=info || "".equals(info)) && null!=info.getProduct()){
			fundInfoService.updateIsValid(id);
			String productId = info.getProduct().getId();
			//ProductInfo product = productInfoService.findProductInfoById(productId);
			productInfoService.updateIsValid(productId);
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else{
			obj.put("result",false);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
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
     * @author Yan 2016-11-25
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
     * @author Yan 2016-11-25
     */
    @RequestMapping(value = "/fundDocListJson", method = RequestMethod.POST)
    public String fundDocListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String fundId = request.getParameter("fundId");	
        jsonPaging = this.getJsonPaging(request);
        if(StringUtils.isNoneBlank(fundId)){
        	jsonPaging = fundInfoService.findFundDocList(jsonPaging,fundId);
            model.put("fundId", fundId);
            this.toJsonString(response, jsonPaging);
        }
        return null;
    }
    
    /**
     * 详细信息页面
     * @author Yan 2016-11-25
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
     * @author Yan 2016-11-25
     */
	@RequestMapping(value = "/saveDoc", method = RequestMethod.POST)
	public void saveDoc(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
    	//boolean result = false;
    	
		boolean isAdd =false;		
    	SysAdmin admin = this.getLoginUser(request);
    	String createId = "";
    	if(null!=admin){
    		createId = admin.getId();
    	}
    	
		String fundId=request.getParameter("fundId");
		String id = request.getParameter("id");
		String documentName=request.getParameter("documentName");
		
		String filePath=request.getParameter("filePath");
		String fileType = "doc";
		Date time = new Date();
		if(StringUtils.isNotBlank(filePath)){
			fileType = filePath.substring(filePath.lastIndexOf('.')+1);
		}
		
		try {
			//查找文件
//			String ctxPath = request.getSession().getServletContext().getRealPath("/");
			File docFile = new File(filePath);
			//如果文件存在，保存文件信息
			if (docFile.isFile() && docFile.exists()) {				
				FundDoc doc = new FundDoc();
				if(StringUtils.isNoneBlank(id)){
					//修改
					doc = fundInfoService.findFundDoc(id);
				} else if(StringUtils.isEmpty(id) && StringUtils.isNoneBlank(fundId)){
					//新增
					isAdd = true;
					FundInfo info = fundInfoService.findFundInfoById(fundId);
					if(null!=info){
						doc.setFund(info);
						doc.setCreateTime(time);						
					}else{
						obj.put("result", false);
						obj.put("msg", "没找到相应的基金信息，请先添加该基金信息");
					}
				}
				doc.setIsValid("1");
				doc.setLastUpdate(time);
				doc.setDocumentName(documentName);
				doc = fundInfoService.saveDoc(doc, isAdd);
				String relateId = doc.getId();
				//如果是新增文件，需要保存附件资源表信心
				if(isAdd){
					AccessoryFile accessoryFile = new AccessoryFile();
					accessoryFile.setFileName(documentName);
					accessoryFile.setFilePath(filePath);
					accessoryFile.setFileType(fileType);
					accessoryFile.setModuleType("fund_doc");
					accessoryFile.setRelateId(relateId);
					accessoryFile.setCreateBy(createId);
					accessoryFile.setCreateTime(time);
					accessoryFileService.saveOrUpdate(accessoryFile);
				}else{
					AccessoryFile accessoryFile = accessoryFileService.findByRelateId(relateId);
					accessoryFile.setFileName(documentName);
					accessoryFileService.saveOrUpdate(accessoryFile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
		}		
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 删除
     * @author Yan 2016-11-25
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
	 *发布状态修改，isPublic
	 *@author scshi
	 *@date 20170425
	 *@param id 基金主键
	 *@param pubVal 状态值
	 * */
	@RequestMapping(value = "/isPubEdit", method = RequestMethod.POST)
	public void isPubEdit(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String id = request.getParameter("id");
		String isPubVal = request.getParameter("pubVal");
		fundInfoService.updateFoundIsPublish(id,isPubVal);
		//保存日志
		String remark = "edit fund(id="+id+") is publish to "+isPubVal;
		this.saveSysLog(request, this.getLoginUser(request).getLoginCode(), this.getLoginUser(request).getNickName(), 
				CoreConstants.ADMIN_FUND_INFO, remark);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	
	/**
	 *基金公告信息
	 *@author rqwang
	 *@date 2017-06-20
	 **/
    @RequestMapping(value = "/fundAnnoInfo", method = RequestMethod.GET)
    public String fundAnnoInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String fundId = request.getParameter("fundId");
    	String id = request.getParameter("id");
        FundAnno info = fundInfoService.findFundAnno(id);
        model.put("fundId", fundId);
        model.addAttribute("info", info);        
        return "console/fund/info/anno/info";
    }
	
}
