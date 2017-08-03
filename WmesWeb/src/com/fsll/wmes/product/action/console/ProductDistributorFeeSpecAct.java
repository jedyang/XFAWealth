package com.fsll.wmes.product.action.console;

import java.util.ArrayList;
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

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysParamTypeService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.crm.vo.CrmClientForIfaVO;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.ProductDistributorFeeSpec;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifafirm.service.IfaClientService;
import com.fsll.wmes.ifafirm.service.IfaFirmForDistributorService;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.product.service.ProductDistributorFeeSpecService;
import com.fsll.wmes.product.service.ProductInfoService;
import com.fsll.wmes.product.vo.ProductDistributorFeeSpecVO;
import com.fsll.wmes.product.vo.ProductVO;

/**
 * 控制器:产品代理费用（工作台）
 * @author Yan
 * @version 1.0.0 Created On: 2016-12-19
 */

@Controller
@RequestMapping("/console/product/distributor/fee/spec")
public class ProductDistributorFeeSpecAct extends WmesBaseAct {
	
	@Autowired
	private ProductDistributorFeeSpecService productDistributorFeeSpecService;
	@Autowired
	private ProductInfoService productInfoService;
	@Autowired
	private SysParamTypeService sysParamTypeService;
	@Autowired
	private IfaFirmForDistributorService ifaFirmForDistributorService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private IfaClientService ifaClientService;
	@Autowired
	private IfafirmManageService ifafirmManageService;
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        //获取登录代理商ID
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if(!"".equals(admin)&&!"".equals(admin.getDistributor())){
    		String distributorId = admin.getDistributor().getId();
    		model.put("distributorId", distributorId);
            return "console/product/fee_spec_list";//页面摆放路径
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
        jsonPaging = productDistributorFeeSpecService.getList(jsonPaging,keyword,langCode);
        List<Object> jsonList = jsonPaging.getList();
        List<Object> list = new ArrayList<Object>();
        if(null!=jsonList && !jsonList.isEmpty()){
        	for (int i = 0; i < jsonList.size(); i++) {
        		ProductDistributorFeeSpecVO vo = new ProductDistributorFeeSpecVO();
				Object[] objRead = (Object[])jsonList.get(i);
				ProductDistributorFeeSpec info = (ProductDistributorFeeSpec)objRead[0];
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
				if(!"".equals(info.getModuleType()) && !"".equals(info.getRelateId())){
					vo.setModuleType(info.getModuleType());
					vo.setRelateId(info.getRelateId());
					//暂时只对invester角色进行处理
					if("investor".equalsIgnoreCase(info.getModuleType())){
						MemberBase member = memberBaseService.findById(info.getRelateId());
						if(null!=member && !StrUtils.isEmpty(member.getNickName())){
							vo.setRelateName(member.getNickName());
						}
					}
					else if("ifafirm".equalsIgnoreCase(info.getModuleType())){
						MemberIfafirm member = ifafirmManageService.findById(info.getRelateId(),langCode);
						if(null!=member && !StrUtils.isEmpty(member.getCompanyName())){
							vo.setRelateName(member.getCompanyName());
						}
					}
					else if("ifa".equalsIgnoreCase(info.getModuleType())){
						MemberBase member = memberBaseService.findById(info.getRelateId());
						if(null!=member && !StrUtils.isEmpty(member.getNickName())){
							vo.setRelateName(member.getNickName());
						}
					}
				}
				
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
//        	String type = request.getParameter("type");
        	
    		ProductDistributorFeeSpec info = productDistributorFeeSpecService.findById(id);
    		if (null==info){
        		info = new ProductDistributorFeeSpec();
        		info.setIsValid("0");
        	}
        	model.put("info", info);
    		
    		//费用类型
    		List<SysParamConfig> configList = sysParamTypeService.getChildren("distributor_fee_type");
        	model.put("configList", configList);

        	//关联的产品
			List<ProductVO> productList = productInfoService.getProductFundList(distributor.getId(), langCode);//??
			model.put("productList", productList); 
    		
			List<GeneralDataVO> list = findObjList(request, info.getModuleType());
			model.put("objList", list);
			
            return "console/product/fee_spec_input";
    	}else{
    		return "redirect:"+this.getFullPath(request)+"front/index.do";
    	}
    }
    
    /**
     * 获取对象列表（根据对象类型获取modelType）
     * @param request
     * @param response
     * @param model
     */
	@RequestMapping(value = "/getObjList", method = RequestMethod.POST)
	public void getObjList(HttpServletRequest request,HttpServletResponse response,ModelMap model, String moduleType) {
		Map<String, Object> obj = new HashMap<String, Object>();
		this.isMobileDevice(request,model);
    	String langCode = this.getLoginLangFlag(request);
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if(!"".equals(admin)&&!"".equals(admin.getDistributor())){
    		List<GeneralDataVO> list = findObjList(request, moduleType);
    		
    		obj.put("result",true);
    		obj.put("data",JsonUtil.toJson(list));
//    		ResponseUtils.renderHtml(response,JsonUtil.toJson(list));
    	}else{
    		obj.put("result",false);
    	}
    	JsonUtil.toWriter(obj, response);
	}
	
	private List findObjList(HttpServletRequest request,String moduleType) {
		Map<String, Object> obj = new HashMap<String, Object>();
    	String langCode = this.getLoginLangFlag(request);
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if(!"".equals(admin)&&!"".equals(admin.getDistributor())){
    		MemberDistributor distributor = admin.getDistributor();
    		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
    		
    		if (StrUtils.getString(moduleType).equalsIgnoreCase("investor")){
	    		//已开户的关联投资者
	        	List clientList = ifaClientService.findClientForDistributor(distributor.getId(), "", "1");
	        	if (null!=clientList && !clientList.isEmpty()){
	        		for (int i=0;i<clientList.size();i++){
	        			CrmClientForIfaVO vo = (CrmClientForIfaVO)clientList.get(i);
	        			GeneralDataVO dataVO = new GeneralDataVO();
	        			dataVO.setId(vo.getMemberId());
	        			dataVO.setName(vo.getNickName());
	        			list.add(dataVO);
	        		}
	        	}
    		}
    		else if (StrUtils.getString(moduleType).equalsIgnoreCase("ifafirm")){
	            //关联的投资顾问公司
	        	List ifafirmList = ifaFirmForDistributorService.findIfafirmByDistributor(distributor.getId(), langCode);
	        	if (null!=ifafirmList && !ifafirmList.isEmpty()){
	        		for (int i=0;i<ifafirmList.size();i++){
	        			Object[] objs = (Object[])ifafirmList.get(i);
	        			MemberIfafirm vo = (MemberIfafirm)objs[0];
	        			GeneralDataVO dataVO = new GeneralDataVO();
	        			dataVO.setId(vo.getId());//ifafirm id
	        			dataVO.setName(StrUtils.getString(objs[1]));//company name
	        			list.add(dataVO);
	        		}
	        	}
    		}
    		else if (StrUtils.getString(moduleType).equalsIgnoreCase("ifa")){
	            //关联的投资顾问
	        	List ifaList = ifaFirmForDistributorService.findIfaByDistributor(distributor.getId());
	        	if (null!=ifaList && !ifaList.isEmpty()){
	        		for (int i=0;i<ifaList.size();i++){
	        			MemberIfa vo = (MemberIfa)ifaList.get(i);
	        			GeneralDataVO dataVO = new GeneralDataVO();
	        			dataVO.setId(vo.getMember().getId());
	        			dataVO.setName(vo.getMember().getNickName());
	        			list.add(dataVO);
	        		}
	        	}
    		}
    		return list;
    	}
    	return null;
	}
	
    /**
     * 保存
     * @param request
     * @param response
     * @param model
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model, ProductDistributorFeeSpec fee) {
		boolean isAdd =false;
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
		
		productDistributorFeeSpecService.saveOrUpdate(fee, isAdd);
		
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
		productDistributorFeeSpecService.deleteById(id);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
}
