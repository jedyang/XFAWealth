package com.fsll.wmes.fund.action.console;

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
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.vo.MemberCompanyVO;
import com.fsll.wmes.distributor.service.MemberDistributorService;
import com.fsll.wmes.entity.CompanyDistributor;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductCompany;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.fund.service.FundDistributorService;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundDistributorVO;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.product.service.ProductInfoService;

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
	private FundDistributorService fundDistributorService;
	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private ProductInfoService productInfoService;
	@Autowired
	private CompanyInfoService companyInfoService;
	@Autowired
	private MemberDistributorService distributorService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        String fundId = request.getParameter("fundId");
        model.put("fundId", fundId);
        
        return "console/fund/distributor/list";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String distributorName = StrUtils.getString(request.getParameter("distributorName"));
    	String fundId = StrUtils.getString(request.getParameter("fundId"));
    	if(fundId!=null && !StrUtils.isEmpty(fundId)){
    		FundInfo fund = fundInfoService.findFundInfoById(fundId);
    		String proId = fund.getProduct().getId();
    		
    		jsonPaging = this.getJsonPaging(request);
            jsonPaging = fundDistributorService.findAllIncludeDistributorName(jsonPaging,proId,distributorName,langCode);
            
            List<FundDistributorVO> list = new ArrayList<FundDistributorVO>();
            List<ProductDistributor> objList = jsonPaging.getList();
            if (null!=objList && !objList.isEmpty())
            for (int i = 0; i < objList.size(); i++) {
    			FundDistributorVO vo = new FundDistributorVO();
				ProductDistributor info = objList.get(i);
					
				BeanUtils.copyProperties(info,vo);
				vo.setSymbolCode(info.getSymbolCode());
    			vo.setProductId(info.getProduct().getId());
    			/*
    			List<MemberCompanyVO> companyList = companyInfoService.findCompanyList(info.getProduct().getId(), info.getDistributor().getId(), langCode);
				if (null!=companyList && !companyList.isEmpty()){
					String names = "";
					for (MemberCompanyVO r: companyList){
						names += r.getCompanyName()+",";
					}
					if (names.endsWith(",")) names = names.substring(0, names.length()-1);
					vo.setCompanyName(names);
				}
				*/
    			if (null!=info.getDistributor()){
	    			vo.setDistributor(info.getDistributor());
	    			vo.setDistributorName(info.getDistributor().getCompanyName());
	    			vo.setDistributorId(info.getDistributor().getId());
    			}
    			list.add(vo);
			}
    		jsonPaging.setList(list);
    	}else{
    		jsonPaging.setList(null);
    	}
        
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String productId = StrUtils.getString(request.getParameter("producId"));
        //编辑是fundId为空，id不为空，通过id获取productDistributor的信息
        String id = StrUtils.getString(request.getParameter("id"));
        ProductDistributor productDistributor = fundDistributorService.findById(id);
        if(null!=productDistributor && null!=productDistributor.getProduct() && !StrUtils.isEmpty(productDistributor.getProduct().getId())){
        	productId = productDistributor.getProduct().getId();
        }
        FundDistributorVO fundDistributorVO = new FundDistributorVO();
        if (null!=productDistributor){
        	BeanUtils.copyProperties(productDistributor, fundDistributorVO);
            
    		if (null!=productDistributor.getDistributor()){
    			fundDistributorVO.setDistributor(productDistributor.getDistributor());
    			fundDistributorVO.setDistributorName(productDistributor.getDistributor().getCompanyName());
    			fundDistributorVO.setDistributorId(productDistributor.getDistributor().getId());
    		}        }
        
        //新增时id、productId为空，通过fundId获取fundInfo对productDistributor表进行新增
        String fundId = StrUtils.getString(request.getParameter("fundId"));
        if(StringUtils.isNotBlank(fundId)){
			FundInfo info = fundInfoService.findFundInfoById(fundId);
			if(null!=info && null!=info.getProduct() && !StrUtils.isEmpty(info.getProduct().getId())){
				productId = info.getProduct().getId();
			}
		}

		/*
        if (!StrUtils.isEmpty(fundDistributorVO.getDistributorId())){
	        List<MemberCompanyVO> companys = companyInfoService.findCompanyList(productId, fundDistributorVO.getDistributorId(), this.getLoginLangFlag(request));
			if (null!=companys && !companys.isEmpty()){
				String names = "";
				for (MemberCompanyVO r: companys){
					names += r.getCompanyName()+",";
				}
				if (names.endsWith(",")) names = names.substring(0,names.length());
				fundDistributorVO.setCompanyName(names);
			}
        }*/
		
        //获取基金的代理商
        List<CompanyInfo> companyList = companyInfoService.findCompanyByFund(fundId, this.getLoginLangFlag(request));

        //获取所有代理商
        List<MemberDistributor> list = fundDistributorService.findAllDistributors(); 
        model.put("entityVO", fundDistributorVO);
        model.put("listVO", list);     
        model.put("companyList", companyList);
        model.put("productId", productId);
//        model.addAttribute("typeListVO", list);    
        return "console/fund/distributor/input";
    }
    
    

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String actionMsg="global.success";

		//ProductDistributorId
		String id = request.getParameter("id");
		String productId = request.getParameter("productId");
		String distributorId = request.getParameter("distributorId");
//		String companyId = request.getParameter("companyId");
		String isPublish = request.getParameter("isPublish");
		String code = request.getParameter("symbolCode");
				
		ProductInfo product = productInfoService.findProductInfoById(productId);
		MemberDistributor objDistributor = distributorService.findById(distributorId);
//		CompanyInfo company = companyInfoService.findById(companyId);

		if( null != id && !"".equals(id)){
			//update
			ProductDistributor obj = fundDistributorService.findById(id);
			if (null==obj){
				obj = new ProductDistributor();
				obj.setCreateTime(new Date());
			}
			obj.setProduct(product);
			obj.setDistributor(objDistributor);
			obj.setSymbolCode(code);
			obj.setIsPublish(isPublish);
			obj = fundDistributorService.saveOrUpdate(obj);	
			
		} else {
			//insert
			ProductDistributor obj = new ProductDistributor();
			obj.setId(null);
			obj.setProduct(product);
			obj.setDistributor(objDistributor);
			obj.setSymbolCode(code);
			obj.setIsPublish(isPublish);
			obj.setCreateTime(new Date());
			obj = fundDistributorService.saveOrUpdate(obj);					
		}
		
		/*
		//保存CompanyDistributor(不存在则添加)
		if (null!=company && null!=objDistributor){
			CompanyDistributor cd = fundDistributorService.findCompanyDistributor(company.getId(), objDistributor.getId());
			if (null==cd || StrUtils.isEmpty(cd.getId())){
				cd = new CompanyDistributor();
				cd.setCompany(company);
				cd.setDistributor(objDistributor);
				cd.setCreateTime(new Date());
				cd = fundDistributorService.saveOrUpdate(cd);	
			}
		}
		
		//保存ProductCompany(不存在则添加)
		if (null!=company && null!=product){
			ProductCompany pc = fundDistributorService.findProductCompany(product.getId(), company.getId());
			if (null==pc || StrUtils.isEmpty(pc.getId())){
				pc = new ProductCompany();
				pc.setCompany(company);
				pc.setProduct(product);
				pc.setCreateTime(new Date());
				pc = fundDistributorService.saveOrUpdate(pc);	
			}
		}
		*/
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		String msg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null);
		obj.put("msg",msg);
		
		JsonUtil.toWriter(obj, response);
		//ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
     
	/**
	 * 修改状态的方法（删除）
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public void updatestatus(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id = request.getParameter("id");
		String actionMsg="global.success";
		fundDistributorService.deleteProductDistributor(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 检查产品编码是否已存在(true: 不存在，可用； false :已存在，不可用
	 * @param request
	 * @param response
	 * @param model
	 * @param code
	 */
	@RequestMapping(value = "/checkSymbolCode", method = RequestMethod.POST)
	public void checkSymbolCode(HttpServletRequest request,HttpServletResponse response,ModelMap model, String code) {
		boolean result= true;
		String id = StrUtils.getString(request.getParameter("id"));
		ProductDistributor pd = fundDistributorService.findBySymbolCode(code);
		if (null!=pd && !StrUtils.isEmpty(pd.getSymbolCode()) && !id.equals(pd.getId()))
			result = false;
		JsonUtil.toWriter(result, response);
	}
}
