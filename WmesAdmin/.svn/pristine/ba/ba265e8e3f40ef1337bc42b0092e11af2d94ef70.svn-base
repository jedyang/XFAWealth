package com.fsll.wmes.strategy.action.console;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.core.service.SysParamService;
import com.fsll.core.vo.SysParamVO;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmProposalService;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.strategy.vo.StrategyAllocationTypeVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationVO;
import com.fsll.wmes.strategy.vo.StrategyDetailVO;
import com.fsll.wmes.strategy.vo.StrategyWebViewVO;

/**
 * 控制器:基金信息管理（工作台）
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-8-17
 */

@Controller
@RequestMapping("/console/strategy/info")
public class ConsoleStrategyInfoAct extends WmesBaseAct {
	
	@Autowired
	private StrategyInfoService strategyInfoService;
	
	@Autowired
	private  SysParamService  sysParamService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/strategy/info/list";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String keyword = request.getParameter("keyword");
	    String langCode=getLoginLangFlag(request);
    	
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = strategyInfoService.findAll(jsonPaging, keyword,langCode);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    @RequestMapping(value = "/input", method = RequestMethod.GET)
    public String getProposalDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String id = request.getParameter("id");
    	StrategyInfo obj = strategyInfoService.findById(id);
    	
    	String langCode = this.getLoginLangFlag(request);
    	String sector = obj.getSector();
    	String geoAllocation = obj.getGeoAllocation();
    	
    	if(sector == null || sector.equals("")){
    		obj.setSector("无");
    	}else {
    		String[] sctArray = sector.split(",");
        	String sectForPage="";
        	
        	for(int i = 0; i < sctArray.length; i++){
        		
        		SysParamVO findParamByCode = sysParamService.findParamByCode(langCode, sctArray[i]);
        		String name = findParamByCode.getName();
        		if(name == null)continue;
        		
        		if (i != sctArray.length-1) {
        			sectForPage += name + ",";
    			}else {
    				sectForPage += name ;
    			}
        	}
        	
        	obj.setSector(sectForPage);
    	}
    	
    	if(geoAllocation == null || geoAllocation.equals("")){
    		obj.setGeoAllocation("无");
    	}else {
    		String[] geoArray = geoAllocation.split(",");
        	String geoForPage="";
        	
        	for(int i = 0; i < geoArray.length; i++){
        		
        		SysParamVO findParamByCode = sysParamService.findParamByCode(langCode, geoArray[i]);
        		String name = findParamByCode.getName();
        		if(name == null)continue;
        		
        		if (i != geoArray.length-1) {
        			geoForPage += name + ",";
    			}else {
    				geoForPage += name ;
    			}
        	}
        	
        	obj.setGeoAllocation(geoForPage);
    	}
    	
    	sysParamService.findParamByCode(langCode, sector);
    	sysParamService.findParamByCode(langCode, geoAllocation);
    	
    	model.put("strategyVO", obj);
    	return "console/strategy/info/input";
    }
    
    /**
     * 策略列表
     * @author mqzou  2017-05-27
     */
    @RequestMapping(value="/stratetyList")
    public String stratetyList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	//风险等级
    	List<GeneralDataVO> itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_STRATEGY_RISK_RATING, this.getLoginLangFlag(request));
        model.put("riskList", itemList);
    	return "console/strategy/info/strategylist";
    }
    
    /**
     * 策略列表数据
     * @author mqzou  2017-05-27
     */
    @RequestMapping(value="/stratetyListJson")
    public void strategyListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String keyword=request.getParameter("keyword");
    	String riskLevel=request.getParameter("riskLevel");
    	String langCode=getLoginLangFlag(request);
    	jsonPaging=getJsonPaging(request);
    	jsonPaging=strategyInfoService.findAllList(jsonPaging, keyword, langCode,riskLevel);
    	 this.toJsonString(response, jsonPaging);
    }
    
    /**
     * 删除策略(逻辑删除)
     * @author mqzou  2017-05-27
     */
    @RequestMapping(value="/deleteStrategy")
    public void deleteStrategy(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	HashMap<String, Object> result=new HashMap<String, Object>();
    	String id=request.getParameter("id");
    	StrategyInfo info=strategyInfoService.findById(id);
    	if(null!=info){
    		info.setIsValid("0");
    		strategyInfoService.saveOrUpdate(info);
    		result.put("result", true);
    	}else{
    		result.put("result", false);
    	}
    	JsonUtil.toWriter(result, response);
    }
    
    /**
     * 策略详情
     * @author mqzou  2017-06-02
     */
    @RequestMapping(value="/detail")
    public String strategyDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String id=request.getParameter("id");
    	String langCode=getLoginLangFlag(request);
    	StrategyDetailVO strategyDetailVO=strategyInfoService.findStrategyDetail(id, langCode);
    	
    	model.put("strategyInfo", strategyDetailVO);
		
		List<StrategyAllocationTypeVO> typeList=strategyInfoService.findStrategyAllocationType(id);
		model.put("typeList", typeList);
		
		List<StrategyAllocationVO> allocationList=strategyInfoService.findStrategyAllocationData(id, langCode,CommonConstantsWeb.WEB_ALLOCATION_METHOD_GEOGRAPHICAL);
		double total=0;
		if(null!=allocationList){
			Iterator<StrategyAllocationVO> it=allocationList.iterator();
			while (it.hasNext()) {
				StrategyAllocationVO vo = (StrategyAllocationVO) it.next();
				double weight=null!=vo.getItemWeight()?Double.valueOf(vo.getItemWeight()):0;
				total+=weight;
			}
		}
		model.put("allocationTotal", total);
		model.put("allocationList", allocationList);
		model.put("allocationJson", JsonUtil.toJson(allocationList));
		

		List<StrategyAllocationVO> sectorList=strategyInfoService.findStrategyAllocationData(id, langCode,CommonConstantsWeb.WEB_ALLOCATION_METHOD_SECTOR);
		 total=0;
			if(null!=allocationList){
				Iterator<StrategyAllocationVO> it=sectorList.iterator();
				while (it.hasNext()) {
					StrategyAllocationVO vo = (StrategyAllocationVO) it.next();
					double weight=null!=vo.getItemWeight()?Double.valueOf(vo.getItemWeight()):0;
					total+=weight;
				}
			}
	    model.put("sectorTotal", total);
		model.put("sectorList", sectorList);
		model.put("sectorListJson", JsonUtil.toJson(sectorList));
		
		//基金列表
    	List<String> productInfoList = strategyInfoService.findFundIdByStrategy(id);
    	String fundIds = "";
    	if (!productInfoList.isEmpty())
    		for (String x : productInfoList){
    			if (null!=x && !"".equals(x))
    				fundIds+=x+",";
    		}
    	model.put("fundIds", fundIds);
		model.put("productInfoList", productInfoList);
    	
		return "console/strategy/info/detail";
    
    }
}
