package com.fsll.wmes.crm.action.front;

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
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.KycDocCheckServic;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.InvestorDocCheck;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.ifa.vo.DocHistoryVo;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.KycDocEditVo;

@Controller
@RequestMapping("/front/kycDoc")
public class FrontKycDocCheckAct  extends CoreBaseAct{
	private static HashMap<Long,String> moduleRelate = new HashMap<Long,String>();
	static{
		moduleRelate.put(5L, "investor_doc");//kyc与开户文档审查
	}
	
	@Autowired
	private KycDocCheckServic kycDocCheckServic;
	@Autowired
	private InvestorService investorService;
	
	/**
	 *KYC正常/过期/审批中/退回/审批页面
	 *@author scshi
	 *@20160928
	 * */
	@RequestMapping(value = "/docDetail", method = RequestMethod.GET)
	public String docDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model){
//		Map<String, Object> map = new HashMap<String, Object>();
		String langCode = this.getLoginLangFlag(request);
		String docId = request.getParameter("docId");
		
		InvestorDoc doc = kycDocCheckServic.findDocInfoById(docId,langCode);
		List<AccessoryFile> fileList = investorService.findSubmitDocList("investor_doc", docId);
		
		boolean compareDateFlag = false;//是否过期 true 过期，false 未过期
		
		if(null!=doc.getExpireDate()){
			compareDateFlag = new Date().after(doc.getExpireDate());
		}
		//审批中或者退回
		String checkStatus = doc.getCheckStatus();
		
		List<InvestorDocCheck> checkList = kycDocCheckServic.findDocCheckList(docId);
		model.put("checkList", checkList);
		
		//审批历史
		String moduleType = moduleRelate.get(5L);
		List<DocHistoryVo> list = kycDocCheckServic.findDocHistoryKyc(docId,moduleType,langCode);
		
//		map.put("compareDateFlag", compareDateFlag);
//		map.put("investorDocInfo", doc);
//		map.put("docImages", fileList);
//		JsonUtil.toWriter(map, response);
		
		model.put("compareDateFlag", compareDateFlag);
		model.put("investorDocInfo", doc);
		model.put("docImages", fileList);
		model.put("historys", list);
//		return "ifa/crm/docCheck/docDetail";
		return "ifa/crm/docCheck/documentUpdateHistory";
	}
	
	/**
	 *KYC文档修改页面
	 *@author scshi_u330p
	 *@date 20161108 
	 * */
	@RequestMapping(value="/docDetailEdit" ,method=RequestMethod.GET)
	public String docDetailEdit(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String docId = request.getParameter("docId");
		String langCode = this.getLoginLangFlag(request);
		
		InvestorDoc doc = kycDocCheckServic.findDocInfoById(docId,langCode);
		List<AccessoryFile> fileList = investorService.findSubmitDocList("investor_doc", docId);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("investorDocInfo", doc);
//		map.put("docImages", fileList);
//		JsonUtil.toWriter(map, response);
		model.put("investorDocInfo", doc);
		model.put("docImages", fileList);
		return "ifa/crm/docCheck/docDetailEdit";
	}
	
	/**
	 * KYC文档修改提交
	 * @author scshi_u330p
	 * @date 20161111 
	 * */
	@RequestMapping(value="/docDetailEditSave" ,method=RequestMethod.POST)
	public void docDetailEditSave(HttpServletRequest request,HttpServletResponse response,ModelMap model,KycDocEditVo vo){
		String langCode = this.getLoginLangFlag(request);
		MemberBase loginUser = this.getLoginUser(request);
		String moduleType = moduleRelate.get(Long.parseLong(request.getParameter("moduleId")));
		kycDocCheckServic.docDetailEditSave(vo,langCode,loginUser,moduleType);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg",getProperty(request,"global.success.save"));
		map.put("result", true);
		JsonUtil.toWriter(map, response);
		
		// 调用公共方法，发送待办待阅消息
		/**
		 * 发送人 investor
		 * 接收人 文档关联的distributor
		 * 消息类型  2--一般性通知
		 * 消息模块  kyc_summit
		 * 消息关联的页面id --docid
		 * */
		InvestorDoc iDoc = this.investorService.getInvestorDoc(vo.getDocId());
		List<MemberBase> disList = kycDocCheckServic.findComfirmDistributorAccount(iDoc.getDistributor());
		if(null!=disList && !disList.isEmpty()){
			for(MemberBase disAccount:disList){
				sendWebReadToDo(loginUser, disAccount, CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL,
						CommonConstantsWeb.WEB_READ_MODULE_KYC_SUMMIT, vo.getDocId(), "Invite kyc document check summit",
						"/front/strategy/info/clientDetailKyc.do");
			}
		}
		
	}
	
	/**
	 *KYC文档审批	
	 *@author scshi_u330p
	 *@date 20161010
	 * */
	@RequestMapping(value = "/kycDocApprove", method = RequestMethod.POST)
	public void docApproveKyc(HttpServletRequest request,HttpServletResponse response,ModelMap model,InvestorDocCheck docCheck ){
		MemberBase loginUser = this.getLoginUser(request);
		String moduleType = moduleRelate.get(Long.parseLong(request.getParameter("moduleId")));
		kycDocCheckServic.kycDocCheckResultSave(loginUser,docCheck,moduleType);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg",getProperty(request,"global.success.save"));
		map.put("result", true);
		JsonUtil.toWriter(map, response);
		//审批完成以后设置待办状态为已读
		kycDocCheckServic.webReadedSave(docCheck.getDocId(),CommonConstantsWeb.WEB_READ_MODULE_KYC_SUMMIT,CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL);
	}
	
	/**
	 * KYC历史文档页面
	 * */
	@RequestMapping(value="/docHistoryKyc",method=RequestMethod.GET)
	public String docHistoryKyc(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String moduleType = moduleRelate.get(Long.parseLong(request.getParameter("moduleId")));
		String docId = request.getParameter("docId");
		String langCode = this.getLoginLangFlag(request);
		
		List<DocHistoryVo> list = kycDocCheckServic.findDocHistoryKyc(docId,moduleType,langCode);
		InvestorDoc doc = kycDocCheckServic.findDocInfoById(docId,langCode);
		
		model.put("historyList", list);
		model.put("doc", doc);
		return  "ifa/crm/docCheck/docHistory";
	}
	
	/**
	 * distrubutor/ifaFirm 修改文档审批日期
	 * @author scshi_u330p
	 * @date 20161212
	 * */
	@RequestMapping(value="expireDateChange",method=RequestMethod.POST)
	public void expireDateChange(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		
		String newExpireDate = request.getParameter("expireDate");
		String id = request.getParameter("id");
		kycDocCheckServic.expireDateChange(id,newExpireDate);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg",getProperty(request,"global.success.save"));
		map.put("result", true);
		JsonUtil.toWriter(map, response);
	}
	
}
