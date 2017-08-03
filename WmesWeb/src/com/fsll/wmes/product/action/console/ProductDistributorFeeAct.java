package com.fsll.wmes.product.action.console;

import java.util.ArrayList;
import java.util.Date;
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
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysParamTypeService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.ProductDistributorFee;
import com.fsll.wmes.entity.ProductDistributorFeeSpec;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.product.service.ProductDistributorFeeService;
import com.fsll.wmes.product.service.ProductInfoService;
import com.fsll.wmes.product.vo.ProductDistributorFeeVO;
import com.fsll.wmes.product.vo.ProductVO;

/**
 * 控制器:产品代理费用（工作台）
 * @author Yan
 * @version 1.0.0 Created On: 2016-12-19
 */

@Controller
@RequestMapping("/console/product/distributor/fee")
public class ProductDistributorFeeAct extends WmesBaseAct {
	
	@Autowired
	private ProductDistributorFeeService productDistributorFeeService;
	@Autowired
	private ProductInfoService productInfoService;
	@Autowired
	private SysParamTypeService sysParamTypeService;
	@Autowired
	private DistributorService distributorService;

	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        //获取登录代理商ID
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if(null!=admin && !"".equals(admin.getDistributor())){
    		String distributorId = admin.getDistributor().getId();
    		model.put("distributorId", distributorId);
            return "console/product/fee_list";//页面摆放路径
    	}else{
        	return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
    	}
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String keyword = request.getParameter("keyword");
    	jsonPaging = this.getJsonPaging(request);
        jsonPaging = productDistributorFeeService.getList(jsonPaging,keyword,langCode);
        List<Object> jsonList = jsonPaging.getList();
        List<Object> list = new ArrayList<Object>();
        if(null!=jsonList && !jsonList.isEmpty()){
        	for (int i = 0; i < jsonList.size(); i++) {
        		ProductDistributorFeeVO vo = new ProductDistributorFeeVO();
				Object[] objRead = (Object[])jsonList.get(i);
				ProductDistributorFee info = (ProductDistributorFee)objRead[0];
				SysParamConfig config = (SysParamConfig)objRead[1];
				ProductInfo product = (ProductInfo)objRead[2];
				
				vo.setId(info.getId());
				vo.setDistributorId(info.getDistributor().getId());
				vo.setDistributorName(info.getDistributor().getCompanyName());
				vo.setProductId(info.getProduct().getId());
				vo.setFeeType(info.getFeeType());				
				vo.setFeeDefValue(info.getFeeDefValue());
				vo.setFeeMin(info.getFeeMin());
				vo.setFeeMax(info.getFeeMax());
				vo.setCreateTime(info.getCreateTime());
				vo.setIsValid(info.getIsValid());
				vo.setProductType(product.getType());
				
				if("sc".equals(langCode)){
					vo.setFeeTypeName(config.getNameSc());
					FundInfoSc sc = (FundInfoSc)objRead[4];
					if(!"".equals(sc)){
						vo.setProductName(sc.getFundName());
					}
				} else if("tc".equals(langCode)){
					vo.setFeeTypeName(config.getNameTc());
					FundInfoTc tc = (FundInfoTc)objRead[4];
					if(!"".equals(tc)){
						vo.setProductName(tc.getFundName());
					}
				} else if("en".equals(langCode)){
					vo.setFeeTypeName(config.getNameEn());
					FundInfoEn en = (FundInfoEn)objRead[4];
					if(!"".equals(en)){
						vo.setProductName(en.getFundName());
					}
				}
				list.add(vo);
			}
        	jsonPaging.setList(list);
        }
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	this.isMobileDevice(request,model);
    	String langCode = this.getLoginLangFlag(request);
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if(!"".equals(admin)&&!"".equals(admin.getDistributor())){
    		MemberDistributor distributor = admin.getDistributor();
    		model.put("distributor", distributor);
    		
    		String id = request.getParameter("id");
        	String type = request.getParameter("type");
        	
    		ProductDistributorFee info = productDistributorFeeService.findById(id);
    		if (null==info){
        		info = new ProductDistributorFee();
        		info.setIsValid("0");
        	}
    		
    		List<SysParamConfig> configList = sysParamTypeService.getChildren("distributor_fee_type");
//    		if("fund".equalsIgnoreCase(type)){
    			List<ProductVO> productList = productInfoService.getProductFundList(distributor.getId(), langCode);
    			model.put("productList", productList);
//    		}
        	model.put("info", info);
        	model.put("configList", configList);
            return "console/product/fee_input";
    	}else{
    		return "redirect:"+this.getFullPath(request)+"front/index.do";
    	}
    	
    }
    
    /**
     * 
     * @param request
     * @param response
     * @param model
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model,ProductDistributorFee fee) {
		boolean isAdd =false;
//		String id = request.getParameter("id");
//		String feeType = request.getParameter("feeType");
//		String feeDefValue = request.getParameter("feeDefValue");
//		String feeMin = request.getParameter("feeMin");
//		String feeMax = request.getParameter("feeMax");
//		String productId = request.getParameter("productId");
//		String distributorId = request.getParameter("distributorId");
//    	MemberDistributor distributor = new MemberDistributor();
//    	ProductDistributorFee fee = new ProductDistributorFee();
    	if(StrUtils.isEmpty(fee.getId())){
			isAdd=true;
			fee.setId(null);
			fee.setIsValid("1");
		}
    	if (null==fee.getCreateTime()) fee.setCreateTime(new Date());
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
		if(null!=admin && !"".equals(admin.getIfafirm())){
			MemberDistributor distributor = admin.getDistributor();
			fee.setDistributor(distributor);
    	}
		fee.setLastUpdate(new Date());
		
//    	if(StringUtils.isNotBlank(id)){
//			fee = productDistributorFeeService.findById(id);
//		}else{
//			isAdd=true;
//			fee.setId(null);
//			fee.setCreateTime(new Date());
//		}
//    	if(StringUtils.isNotBlank(distributorId)){
//    		distributor = distributorService.findDistributorById(distributorId);
//    		fee.setDistributor(distributor);
//    	}
//    	ProductInfo product = new ProductInfo();
//    	if(StringUtils.isNotBlank(productId)){
//    		product = productInfoService.findProductInfoById(productId);
//    		fee.setProduct(product);
//    	}
//		
//		if(StringUtils.isNotBlank(feeType))
//			fee.setFeeType(feeType);
//		if(StringUtils.isNotBlank(feeDefValue))
//			fee.setFeeDefValue(Double.valueOf(feeDefValue));
//		if(StringUtils.isNotBlank(feeMax))
//			fee.setFeeMax(Double.valueOf(feeMax));
//		if(StringUtils.isNotBlank(feeMin))
//			fee.setFeeMin(Double.valueOf(feeMin));
//		
//		fee.setLastUpdate(new Date());
//		fee.setIsValid("1");
		
		productDistributorFeeService.saveOrUpdate(fee, isAdd);
		
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
		productDistributorFeeService.deleteById(id);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
}
