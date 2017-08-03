package com.fsll.wmes.ifafirm.action.console;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.IfafirmDistributor;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundHouseDataVO;
import com.fsll.wmes.fund.vo.FundScreenerDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifafirm.service.IfaFirmFundService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.product.service.ProductInfoService;
import com.fsll.wmes.product.vo.ProductVO;

/*****
 * 
 * 
 * @author wwlin 2017-06-19
 */
@Controller
@RequestMapping("/front/ifafirm/fund")
public class ConsoleIfaFirmFundAct extends CoreBaseAct {
	@Autowired
    private FundInfoService fundInfoService;
	@Autowired
    private IfaFirmFundService ifaFirmFundService;
	@Autowired
	private ProductInfoService productInfoService;
	/**
     * 基金筛选列表
     * @author wwlin
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model, String select, String showCart) {
        this.isMobileDevice(request, model);
        String lang = this.getLoginLangFlag(request);
        MemberSsoVO loginUser = this.getLoginUserSSO(request);//获取当前登录用户
        if(null!=loginUser&&!"".equals(loginUser.getId())){
        	//获取代理商
        	int subMemberType = loginUser.getSubMemberType();
        	String ifafirmId =  loginUser.getIfafirmId();
        	if(subMemberType == 22){
        		List<MemberDistributor> mdList = new ArrayList<MemberDistributor>();
        		List<IfafirmDistributor> dList =  ifaFirmFundService.getIfaFirmDistributorByifaFirmid(ifafirmId);
        		if(null!=dList&&!dList.isEmpty()){
        			for(IfafirmDistributor each : dList){
        				if(null!=each&&!"".equals(each.getId())){
        					MemberDistributor meach = new MemberDistributor();
        					meach.setCompanyName(each.getDistributor().getCompanyName());
        					meach.setId(each.getDistributor().getId());
        					String logoFile = each.getDistributor().getLogofile();
        					logoFile = PageHelper.getLogoUrl(logoFile, "D");
        					meach.setLogofile(logoFile);
        					mdList.add(meach);
        				}
        			}
        			model.put("IfafirmDistributorList", mdList);
        		}
        	}
        	
        	 List<GeneralDataVO> currencyList = findSysParameters("currency_type",lang);
             model.put("currencyList", currencyList);
             
             List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(lang);
             model.put("fundHouseList", fundHouseList);
             
     		//select参数用于作为基金选择功能
         	select = StrUtils.getString(select);
         	if ("true".equals(select)) model.put("select", select);
     		
         	//showCart参数用于控制是否显示购物车
         	showCart = StrUtils.getString(showCart);
         	if ("true".equals(showCart)) model.put("showCart", "true");
         	else model.put("showCart", "false");
         	
     		String displayColor = "";
     		if(null != loginUser){ 
     			MemberSsoVO ssoVO = getLoginUserSSO(request);
     			String individualId = ssoVO.getIndividualId();
     			if(null != individualId && !"".equals(individualId)){
     				model.put("individualId", individualId);
     			}
     			
     			

     			
     			displayColor = loginUser.getDefDisplayColor();
     		}
     		if(null==displayColor || "".equals(displayColor)){
     		    displayColor=CommonConstants.DEF_DISPLAY_COLOR;
     		}
     		model.put("displayColor", displayColor);
     		model.put("defCurrency", loginUser.getDefCurrency());
        }
        
       
    	
		return "console/ifafirm/fund/fundsscreener";
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
    	String distributorList = "";
    	MemberSsoVO loginUser = this.getLoginUserSSO(request);//获取当前登录用户
        if(null!=loginUser&&!"".equals(loginUser.getId())){
        	
        	int subMemberType = loginUser.getSubMemberType();
        	String ifafirmId =  loginUser.getIfafirmId();
        	if(subMemberType == 22){
        		List<IfafirmDistributor> dList =  ifaFirmFundService.getIfaFirmDistributorByifaFirmid(ifafirmId);
        		if(null!=dList&&!dList.isEmpty()){
        			for(IfafirmDistributor each : dList){
        				MemberDistributor dm = each.getDistributor();
        				if(null!=dm&&!"".equals(dm.getId())){
        					distributorList += dm.getId() + ",";
        				}
        			}
        		}
        	}
        	if(!"".equals(distributorList)){
        		distributorList = distributorList.substring(0, distributorList.length()-1);
        	}
        }
    	filters.setFundID(toUTF8String(filters.getFundID()));
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
        filters.setDistributorId(toUTF8String(distributorList));
        //filters.setIfaFirmId(toUTF8String("8080804056a201d90156b01c3aeb0021"));
       // filters.setFundHouseIds("4A8A08F09D37B73795649038408B5F33,0CC175B9C0F1B6A831C399E269772661");
      //  filters.setFundHouse("贝莱");
        model.put("filters", filters);

        //测试条件
//        filters.setLipperCR_ID("3");
//        filters.setLipperCR("5");
        
        jsonPaging = this.getJsonPaging(request);
        // jsonPaging.setSort("convert_mine(" + jsonPaging.getSort() + ",gbk)");
        // 解决中英文混排问题，modified by michael @20160613
//        jsonPaging.setSort("ifnull(fristPinyin(" + jsonPaging.getSort() + "), pinyin(" + jsonPaging.getSort() + ")) ");
//        
        // 解决orderby字段排序问题，需转换成数字类型，modified by michael @20160613
//        if (jsonPaging.getSort() != null && jsonPaging.getSort().contains("orderBy")) {
//            jsonPaging.setSort("(orderBy+0)");
//        }
        if("riskLevel".equals(jsonPaging.getSort())){
        	jsonPaging.setSort("f.riskLevel");
        }
        jsonPaging = ifaFirmFundService.findFundInfoList(jsonPaging, filters);
        
        return jsonPaging;
    }
    
    /**
     * 分页获得记录for ajax Json 调用
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
    	jsonPaging = queryFundsByFilters(request, model, filters);

        String lang = this.getLoginLangFlag(request);
        List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(lang);
        //this.toJsonString(response, jsonPaging);
        model.put("jsonPaging", jsonPaging);
//        model.put("filters", filters);
        model.put("fundHouseList_in", fundHouseList);
        
        ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
    }
}
