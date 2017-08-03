package com.fsll.wmes.company.action.console;

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

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.core.entity.SysAdmin;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.bond.service.BondInfoService;
import com.fsll.wmes.bond.vo.BondProductVO;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.service.ProductCompanyService;
import com.fsll.wmes.company.vo.MemberCompanyIfafirmVO;
import com.fsll.wmes.company.vo.ProductCompanyVO;
import com.fsll.wmes.distributor.service.MemberDistributorService;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondInfoSc;
import com.fsll.wmes.entity.BondInfoTc;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.MemberCompanyIfafirm;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.ProductCompany;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockInfoSc;
import com.fsll.wmes.entity.StockInfoTc;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundBasicDataVO;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.fund.vo.FundProductVO;
import com.fsll.wmes.product.service.ProductInfoService;
import com.fsll.wmes.stock.service.StockInfoService;
import com.fsll.wmes.stock.vo.StockProductVO;

/**
 * 产品运营企业关系管理
 * @author Yan
 * @date 2016-12-07
 */
@Controller
@RequestMapping("/console/productcompany")
public class ConsoleProductCompanyAct extends WmesBaseAct {

	@Autowired
	private ProductCompanyService productCompanyService;
	@Autowired
	private CompanyInfoService companyInfoService;
	@Autowired
	private ProductInfoService productInfoService;
	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private StockInfoService stockInfoService;
	@Autowired
	private BondInfoService bondInfoService;
//	@Autowired
//	private MemberDistributorService memberDistributorService;
	
	/**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model); 
        String langCode = this.getLoginLangFlag(request);
        SysAdmin admin = this.getLoginUser(request);
        if(null != admin){
        	List<CompanyInfo> companyList = companyInfoService.findAllCompany(langCode);
        	model.put("companyList", companyList);
            return "console/company/productcompany/list";//页面摆放路径
        }else{
        	return "redirect:"+this.getFullPath(request)+"viewLogin.do";
        }
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String companyName = request.getParameter("companyName");
//    	String distributorName = request.getParameter("distributorName");
    	String productName = request.getParameter("productName");
    	
    	//productInfo的产品类型
    	String type = request.getParameter("type");
    	if(StringUtils.isEmpty(type)){
    		type = "fund";
    	}
    	
    	//条件筛选
    	ProductCompanyVO infoVo = new ProductCompanyVO();
    	if(StringUtils.isNotBlank(companyName)){
    		infoVo.setCompanyName(companyName);
    	}
    	if(StringUtils.isNotBlank(productName)){
    		infoVo.setProductName(productName);
    	}
    	
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = productCompanyService.findAll(jsonPaging, infoVo, type, langCode);
        List<ProductCompanyVO> list = new ArrayList<ProductCompanyVO>();
        List<Object> objList = jsonPaging.getList();
        for (int i = 0; i < objList.size(); i++) {
        	ProductCompanyVO vo = new ProductCompanyVO();
			Object[] objRead = (Object[])objList.get(i);
			ProductCompany info = (ProductCompany)objRead[0];
			//多语言获取信息
			if("sc".equals(langCode)){
				CompanyInfoSc infoSc = (CompanyInfoSc)objRead[1];
				if(!"".equals(infoSc) && null!=infoSc){
					vo.setCompanyName(infoSc.getName());
    			}
				//分产品类别获取对应产品名称
				if(StringUtils.isNotBlank(type) && "fund".equals(type)){
					FundInfoSc fundSc = (FundInfoSc)objRead[3];
					if(!"".equals(fundSc) && null!=fundSc){
	    				vo.setProductName(fundSc.getFundName());
	    			}
				} else if(StringUtils.isNotBlank(type) && "bond".equals(type)){
					BondInfoSc bondSc = (BondInfoSc)objRead[3];
					if(!"".equals(bondSc) && null!=bondSc){
	    				vo.setProductName(bondSc.getBondName());
	    			}
				} else if(StringUtils.isNotBlank(type) && "stock".equals(type)){
					StockInfoSc stockSc = (StockInfoSc)objRead[3];
					if(!"".equals(stockSc) && null!=stockSc){
	    				vo.setProductName(stockSc.getStockName());
	    			}
				}
			}
			if("tc".equals(langCode)){
				CompanyInfoTc infoTc = (CompanyInfoTc)objRead[1];
				if(!"".equals(infoTc) && null!=infoTc){
					vo.setCompanyName(infoTc.getName());
				}
				//分产品类别获取对应产品名称
				if(StringUtils.isNotBlank(type) && "fund".equals(type)){
					FundInfoTc fundTc = (FundInfoTc)objRead[3];
					if(!"".equals(fundTc) && null!=fundTc){
	    				vo.setProductName(fundTc.getFundName());
	    			}
				} else if(StringUtils.isNotBlank(type) && "bond".equals(type)){
					BondInfoTc bondTc = (BondInfoTc)objRead[3];
					if(!"".equals(bondTc) && null!=bondTc){
	    				vo.setProductName(bondTc.getBondName());
	    			}
				} else if(StringUtils.isNotBlank(type) && "stock".equals(type)){
					StockInfoTc stockTc = (StockInfoTc)objRead[3];
					if(!"".equals(stockTc) && null!=stockTc){
	    				vo.setProductName(stockTc.getStockName());
	    			}
				}
			}
			if("en".equals(langCode)){
				CompanyInfoEn infoEn = (CompanyInfoEn)objRead[1];
				if(!"".equals(infoEn) && null!=infoEn){
					vo.setCompanyName(infoEn.getName());
				}
				//分产品类别获取对应产品名称
				if(StringUtils.isNotBlank(type) && "fund".equals(type)){
					FundInfoEn fundEn = (FundInfoEn)objRead[3];
					if(!"".equals(fundEn) && null!=fundEn){
	    				vo.setProductName(fundEn.getFundName());
	    			}
				} else if(StringUtils.isNotBlank(type) && "bond".equals(type)){
					BondInfoEn bondEn = (BondInfoEn)objRead[3];
					if(!"".equals(bondEn) && null!=bondEn){
	    				vo.setProductName(bondEn.getBondName());
	    			}
				} else if(StringUtils.isNotBlank(type) && "stock".equals(type)){
					StockInfoEn stockEn = (StockInfoEn)objRead[3];
					if(!"".equals(stockEn) && null!=stockEn){
	    				vo.setProductName(stockEn.getStockName());
	    			}
				}
			}
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
    	
    	String type = request.getParameter("type");
    	//赋默认值为fund
    	if(StringUtils.isEmpty(type)){
    		type = "fund";
    	}
    	//通过类型获取相应productInfo
    	if(StringUtils.isNotBlank(type) && "fund".equals(type)){
    		List<FundProductVO> fundVoList = fundInfoService.getFundProductVoList(langCode);
        	model.put("productList", fundVoList);
        	model.put("type", "fund");
    	}
    	if(StringUtils.isNotBlank(type) && "stock".equals(type)){
    		List<StockProductVO> stockVoList = stockInfoService.getStockProductVoList(langCode);
        	model.put("productList", stockVoList);
        	model.put("type", "stock");
    	}
    	if(StringUtils.isNotBlank(type) && "bond".equals(type)){
    		List<BondProductVO> bondVoList = bondInfoService.getBondProductVoList(langCode);
        	model.put("productList", bondVoList);
        	model.put("type", "bond");
    	}
    	//获取运营企业CompanyInfo
    	List<CompanyInfo> companyVoList = companyInfoService.findAllCompany(langCode);    	
    	model.put("companyVoList", companyVoList);
    	//获取代理商MemberDistributor
//    	List<MemberDistributor> distributorList = memberDistributorService.findAllDistributors();
//    	model.put("distributorList", distributorList);
    	
    	if(!"".equals(id) && id!=null){
	        model.put("id", id);
	        ProductCompanyVO infoVo = (ProductCompanyVO)productCompanyService.findVoById(id, langCode);
	        model.put("infoVo", infoVo);
    	}
        return "console/company/productcompany/input";
    }
    
    /**
     * 改变产品类型重新获取产品列表
     */
    @RequestMapping(value = "/changeType", method = RequestMethod.POST)
    public String changeType(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String langCode = this.getLoginLangFlag(request);
    	String type = request.getParameter("type");
    	if(StringUtils.isNotBlank(type) && "fund".equals(type)){
    		List<FundProductVO> fundVoList = fundInfoService.getFundProductVoList(langCode);
    		jsonPaging.setList(fundVoList);
    	} else if(StringUtils.isNotBlank(type) && "stock".equals(type)){
    		List<StockProductVO> stockVoList = stockInfoService.getStockProductVoList(langCode);
    		jsonPaging.setList(stockVoList);
    	} else if(StringUtils.isNotBlank(type) && "bond".equals(type)){
    		List<BondProductVO> bondVoList = bondInfoService.getBondProductVoList(langCode);
    		jsonPaging.setList(bondVoList);
    	}
    		
        this.toJsonString(response, jsonPaging);
        return null;
    }
   
	/**
	 * 新增/保存编辑
	*/
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String id=request.getParameter("id");
		
		String companyId=request.getParameter("companyId");
//		String distributorId=request.getParameter("distributorId");
		String productId=request.getParameter("productId");
//		String type=request.getParameter("type");
//		String code=request.getParameter("code");
//		String isPublish=request.getParameter("isPublish");
		
		//创建实体
		ProductCompany info;
		if(null==id||"".equals(id)){
			isAdd = true;
			info = new ProductCompany();
			info.setId(null);
			info.setCreateTime(new Date());
		} else {
			isAdd = false;
			info = productCompanyService.findById(id);
		}
		//运营公司
		if(StringUtils.isNotBlank(companyId)){
			CompanyInfo company = companyInfoService.findById(companyId);
			info.setCompany(company);
		}else{
			info.setCompany(null);
		}
		//代理公司
//		if(StringUtils.isNotBlank(distributorId)){
//			MemberDistributor distributor = memberDistributorService.findById(distributorId);
//			info.setDistributor(distributor);
//		}else{
//			info.setDistributor(null);
//		}
		//产品
		if(StringUtils.isNotBlank(productId)){
			ProductInfo product = productInfoService.findProductInfoById(productId);
			info.setProduct(product);
		}else{
			info.setProduct(null);
		}
		
//		info.setSymbolCode(code);
//		info.setIsPublish(isPublish);
		info = productCompanyService.save(info, isAdd);
			
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	} 
	
	/**
	 * 删除
	*/
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public void del(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String id=request.getParameter("id");
		if(StringUtils.isNotBlank(id)){
			productCompanyService.deleteById(id);
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		} else {
			obj.put("result", false);
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	} 
}
