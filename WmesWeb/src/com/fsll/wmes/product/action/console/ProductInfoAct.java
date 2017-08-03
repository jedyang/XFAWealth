package com.fsll.wmes.product.action.console;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.bond.service.BondInfoService;
import com.fsll.wmes.bond.vo.BondInfoVO;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.product.service.ProductDistributorService;
import com.fsll.wmes.product.service.ProductInfoService;
import com.fsll.wmes.product.vo.ProductDistributorVO;
import com.fsll.wmes.stock.service.StockInfoService;
import com.fsll.wmes.stock.vo.StockInfoVO;

/**
 * 控制器:产品信息管理（工作台）
 * @author Yan
 * @version 1.0.0 Created On: 2016-11-28
 */

@Controller
@RequestMapping("/console/product/info")
public class ProductInfoAct extends WmesBaseAct {
	
	@Autowired
	private ProductInfoService productInfoService;
	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private ProductDistributorService productDistributorService;
	@Autowired
	private BondInfoService bondInfoService;
	@Autowired
	private StockInfoService stockInfoService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        String loginType = "";
        MemberAdmin admin = this.getLoginMemberAdmin(request);
        if(null!=admin && null!=admin.getIfafirm()){
        	loginType = "ifafirm";
        } else if(null!=admin && null!=admin.getDistributor()){
        	loginType = "distributor";
        } else if(null!=admin && null!=admin.getMember()){
        	loginType = "member";
        }
        model.put("loginType", loginType);
        if(null!=admin){
        	return "console/product/list";//页面摆放路径
        } else {
        	return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
        }
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String keyword = request.getParameter("keyword");
    	String type = request.getParameter("type");
    	model.put("type", type);
    	String langCode = this.getLoginLangFlag(request);
    	String loginId = "";
    	//获取登录信息
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if(null!=admin && null!=admin.getDistributor()){
    		MemberDistributor distributor = admin.getDistributor();
    		loginId = distributor.getId();
    	}
    	jsonPaging=this.getJsonPaging(request);
        jsonPaging = productInfoService.getProductVOList(jsonPaging,loginId,keyword,type,langCode);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面，设置product_distributor的备注
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model, String id, String type) {
    	this.isMobileDevice(request,model);
    	id = StrUtils.getString(id);
    	type = StrUtils.getString(type);
    	model.put("type", type.toLowerCase());
    	ProductDistributor info = new ProductDistributor();
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if(null!=admin && null!=admin.getDistributor()){
    		MemberDistributor distributor = admin.getDistributor();
    		String distributorId = distributor.getId();
    		if("fund".equalsIgnoreCase(type)){
//        		FundInfo fund = fundInfoService.findFundInfoById(id);
    			FundInfoDataVO fund = fundInfoService.findFundFullInfoById(id,this.getLoginLangFlag(request),null,null);
        		if(null!=fund && null!=fund.getFundInfo() && null!=fund.getFundInfo().getProduct()){
        			String productId = fund.getFundInfo().getProduct().getId();
        			List<ProductDistributor> list = productDistributorService.findProductDistributor(productId, distributorId);
        			if(!list.isEmpty()){
        				info = list.get(0);
        				ProductDistributorVO vo = new ProductDistributorVO();
        				BeanUtils.copyProperties(info, vo);
        				vo.setIsinCode(info.getSymbolCode());
        				vo.setProductName(fund.getFundName());
        				model.put("info", vo);
        			}
        		}
        	}
    		if("bond".equalsIgnoreCase(type)){
    			BondInfoVO bond = bondInfoService.findBondFullInfoById(id,this.getLoginLangFlag(request));
        		if(null!=bond && null!=bond.getBondInfo() && null!=bond.getBondInfo().getProduct()){
        			String productId = bond.getBondInfo().getProduct().getId();
        			List<ProductDistributor> list = productDistributorService.findProductDistributor(productId, distributorId);
        			if(!list.isEmpty()){
        				info = list.get(0);
        				ProductDistributorVO vo = new ProductDistributorVO();
        				BeanUtils.copyProperties(info, vo);
        				vo.setProductName(bond.getBondName());
        				model.put("info", vo);
        			}
        		}
    		}
    		if("stock".equalsIgnoreCase(type)){
    			StockInfoVO stock = stockInfoService.findStockFullInfoById(id,this.getLoginLangFlag(request));
        		if(null!=stock && null!=stock.getStockInfo() && null!=stock.getStockInfo().getProduct()){
        			String productId = stock.getStockInfo().getProduct().getId();
        			List<ProductDistributor> list = productDistributorService.findProductDistributor(productId, distributorId);
        			if(!list.isEmpty()){
        				info = list.get(0);
        				ProductDistributorVO vo = new ProductDistributorVO();
        				BeanUtils.copyProperties(info, vo);
        				vo.setProductName(stock.getStockName());
        				model.put("info", vo);
        			}
        		}
    		}
    	}
        return "console/product/input";
    }
    
    /**
     * 
     * @param request
     * @param response
     * @param model
     */
	@RequestMapping(value = "/saveProductDistributor", method = RequestMethod.POST)
	public void saveProductDistributor(HttpServletRequest request,HttpServletResponse response,ModelMap model, ProductDistributor info) {
		if (null==info.getCreateTime()) info.setCreateTime(new Date());
		if(null!=info && !StrUtils.isEmpty(info.getId())){
			productDistributorService.saveOrUpdate(info);
		}else{
			ProductDistributor pd = new ProductDistributor();
			BeanUtils.copyProperties(info, pd);
			info = productDistributorService.saveOrUpdate(pd);
		}
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/****
	 * 删除
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public void del(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");		
		productInfoService.deleteById(id);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
}
