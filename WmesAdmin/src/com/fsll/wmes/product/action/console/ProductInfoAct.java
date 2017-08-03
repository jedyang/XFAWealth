package com.fsll.wmes.product.action.console;

import java.util.ArrayList;
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
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.distributor.service.MemberDistributorService;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondInfoSc;
import com.fsll.wmes.entity.BondInfoTc;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockInfoSc;
import com.fsll.wmes.entity.StockInfoTc;
import com.fsll.wmes.product.service.ProductInfoService;
import com.fsll.wmes.product.vo.ProductVO;
import com.fsll.wmes.rpq.vo.RpqPageLangVO;

/**
 * 控制器:产品信息管理（后台）
 * @author Yan
 * @version 1.0.0 Created On: 2016-11-28
 */

@Controller
@RequestMapping("/console/product/info")
public class ProductInfoAct extends WmesBaseAct {
	
	@Autowired
	private ProductInfoService productInfoService;
	@Autowired
	private MemberDistributorService memberDistributorService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        SysAdmin admin = this.getLoginUser(request);
        List<MemberDistributor> distributorList = memberDistributorService.findAllDistributors();
        model.put("distributorList", distributorList);
        if(null != admin){
            return "console/product/list";//页面摆放路径
        }else{
        	return "redirect:"+this.getFullPath(request)+"viewLogin.do";
        }
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String proName = request.getParameter("proName");
    	String distributor = request.getParameter("distributor");
    	String type = request.getParameter("type");
    	
    	ProductVO searchVo = new ProductVO();
    	if(StringUtils.isNotBlank(proName)){
    		searchVo.setName(proName);
    	}
    	if(StringUtils.isNotBlank(distributor)){
    		searchVo.setDistributorId(distributor);
    	}
    	if(StringUtils.isNotBlank(type)){
    		searchVo.setType(type);
    	}
    	
    	String langCode = this.getLoginLangFlag(request);
    	jsonPaging = this.getJsonPaging(request);
    	jsonPaging.setOrder("desc");
    	jsonPaging.setSort("p.createTime");
        jsonPaging = productInfoService.getProductVOList(jsonPaging, searchVo, langCode);
        List<Object> jsonList = jsonPaging.getList();
        List<Object> list = new ArrayList<Object>();
        if(!jsonList.isEmpty()){
        	for (int i = 0; i < jsonList.size(); i++) {
        		ProductVO vo = new ProductVO();
            	Object[] objRead = (Object[])jsonList.get(i);
            	//商品信息
            	ProductInfo info = (ProductInfo)objRead[0];
            	vo.setProductId(info.getId());
            	vo.setType(info.getType());
            	vo.setCreateTime(info.getCreateTime());
            	vo.setLastUpdate(info.getLastUpdate());
            	vo.setIsValid(info.getIsValid());
            	//代理商信息
            	MemberDistributor dis = (MemberDistributor)objRead[9];
            	if(null!=dis){
	            	vo.setDistributorId(dis.getId());
	            	vo.setDistributorName(dis.getCompanyName());
            	}
	            //商品类别信息
            	if("fund".equals(info.getType())){	//product为基金类型
            		FundInfo fInfo = (FundInfo)objRead[1];
            		vo.setIsinCode(fInfo.getIsinCode());
            		vo.setId(fInfo.getId());
            		if("sc".equals(langCode)){
	        			FundInfoSc fLang = (FundInfoSc)objRead[2];
	        			vo.setName(fLang.getFundName());
	            		SysParamConfig config = (SysParamConfig)objRead[7];
	            		vo.setCurrency(config.getNameSc());
            		}else if("tc".equals(langCode)){
            			FundInfoTc fLang = (FundInfoTc)objRead[2];
            			vo.setName(fLang.getFundName());
            			SysParamConfig config = (SysParamConfig)objRead[7];
	            		vo.setCurrency(config.getNameTc());
                	} else if("en".equals(langCode)){
            			FundInfoEn fLang = (FundInfoEn)objRead[2];
            			vo.setName(fLang.getFundName());
            			SysParamConfig config = (SysParamConfig)objRead[7];
	            		vo.setCurrency(config.getNameEn());
                	}
            	} else if("bond".equals(info.getType())){	//product为债券类型
            		BondInfo bInfo = (BondInfo)objRead[3];
            		vo.setIsinCode(bInfo.getSymbolCode());
            		vo.setId(bInfo.getId());
            		if("sc".equals(langCode)){
	            		BondInfoSc bLang = (BondInfoSc)objRead[4];
	            		vo.setName(bLang.getBondName());
	            		SysParamConfig config = (SysParamConfig)objRead[7];
	            		vo.setCurrency(config.getNameSc());
            		} else if("tc".equals(langCode)){
                		BondInfoTc bLang = (BondInfoTc)objRead[4];
                		vo.setName(bLang.getBondName());
                		SysParamConfig config = (SysParamConfig)objRead[7];
	            		vo.setCurrency(config.getNameTc());
                	} else if("en".equals(langCode)){
                		BondInfoEn bLang = (BondInfoEn)objRead[4];
                		vo.setName(bLang.getBondName());
                		SysParamConfig config = (SysParamConfig)objRead[7];
	            		vo.setCurrency(config.getNameEn());
                	}
            	} else if("stock".equals(info.getType())){	//product为股票类型
            		StockInfo sInfo = (StockInfo)objRead[5];
            		vo.setIsinCode(sInfo.getSymbolCode());
            		vo.setId(sInfo.getId());
            		if("sc".equals(langCode)){
	            		StockInfoSc sLang = (StockInfoSc)objRead[6];
	            		vo.setName(sLang.getStockName());
//	            		SysParamConfig config = (SysParamConfig)objRead[7];
//	            		vo.setCurrency(config.getNameSc());
            		} else if("tc".equals(langCode)){
                		StockInfoTc sLang = (StockInfoTc)objRead[6];
                		vo.setName(sLang.getStockName());
//                		SysParamConfig config = (SysParamConfig)objRead[7];
//	            		vo.setCurrency(config.getNameTc());
                	} else if("en".equals(langCode)){
                		StockInfoEn sLang = (StockInfoEn)objRead[6];
                		vo.setName(sLang.getStockName());
//                		SysParamConfig config = (SysParamConfig)objRead[7];
//	            		vo.setCurrency(config.getNameEn());
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
    public String Pagedetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	this.isMobileDevice(request,model);
    	String id = request.getParameter("id");
    	ProductInfo info = productInfoService.findProductInfoById(id);
    	model.put("info", info);
        return "console/product/info/input";
    }
    
    /**
     * 暂不需要单独新增product_info信息
     * @param request
     * @param response
     * @param model
     
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveEnObj(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
//		boolean isAdd =false;

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}*/
	
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
