package com.fsll.wmes.fund.action.console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.member.service.MemberDistributorService;
import com.fsll.wmes.product.service.ProductDistributorService;
import com.fsll.wmes.product.service.ProductInfoService;
import com.fsll.wmes.product.vo.ProductVO;

/**
 * 控制器:基金信息管理（工作台）
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-7-6
 */
@Controller
@RequestMapping("/console/fund/distributor")
public class ConsoleFundDistributorAct extends WmesBaseAct {

	@Autowired
	private ProductInfoService productInfoService;
	@Autowired
	private ProductDistributorService productDistributorService;
	@Autowired
	private MemberDistributorService memberDistributorService;
	
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String fundsdetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {       
        String langCode = this.getLoginLangFlag(request);
    	List<ProductVO> productList = (List<ProductVO>)productInfoService.getProductFundList(null, langCode);   
        //List<MemberDistributor> list = memberDistributorService.findAllDistributors();
        MemberAdmin admin = this.getLoginMemberAdmin(request);
        if(null!=admin && null!=admin.getDistributor()){
        	MemberDistributor distributor = admin.getDistributor();
            model.put("distributor", distributor);
        }
        model.put("productList", productList);
        
//        model.addAttribute("typeListVO", list);
        
        return "console/fund/distributor/input";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveEnObj(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
    	String actionMsg="global.success";
    	String productId = request.getParameter("productId");
    	String distributorId = request.getParameter("distributorId");
    	String code = request.getParameter("txtCode");
    	String isPublish = request.getParameter("isPublish");
    	String remark = request.getParameter("txtRemark");
    	
    	ProductDistributor info = new ProductDistributor();
    	if(StringUtils.isNotBlank(productId)){
    		ProductInfo product = productInfoService.findProductInfoById(productId);
    		info.setProduct(product);
    	}
    	if(StringUtils.isNotBlank(distributorId)){
    		MemberDistributor distributor = memberDistributorService.findById(distributorId);
    		info.setDistributor(distributor);
    	}
    	
    	info.setId(null);
    	info.setSymbolCode(code);
    	info.setIsPublish(isPublish);
    	info.setRemark(remark);
    	
    	productDistributorService.saveOrUpdate(info);
    	
    	Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		String msg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null);
		obj.put("msg",msg);
		
		JsonUtil.toWriter(obj, response);
		//ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
    }
    
}
