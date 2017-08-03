/**
 * 
 */
package com.fsll.wmes.distributor.action.console;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmBaseVO;
import com.fsll.wmes.member.service.MemberDistributorService;
import com.fsll.wmes.member.vo.MemberSsoVO;

/**
 * Distrbutor的客户管理
 * @author michael
 * 2016-12-16
 */
@Controller
@RequestMapping("/console/distributor/client")
public class ConsoleDistributorClientsAct extends WmesBaseAct {
	@Autowired
	private DistributorService distributorService;
	@Autowired
	private SysParamService sysParamService;

	/**
	 * 客户管理
	 */
	@RequestMapping(value = "/clients", method = RequestMethod.GET)
	public String clients(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//		return "distributor/client/clients";
		return "distributor/client/clientsList";
	}
	
	/**
	 * 查看客户资料
	 */
	@RequestMapping(value = "/clientDetail", method = RequestMethod.GET)
	public String clientDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "distributor/client/clientDetail";
	}
	
	/**
	 * 投资账户
	 */
	@RequestMapping(value = "/investAccounts", method = RequestMethod.GET)
	public String investAccounts(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//		return "distributor/client/investAccounts";
		MemberSsoVO ssVo=getLoginUserSSO(request);
		String distributorId=ssVo.getDistributorId();
		String langCode=getLoginLangFlag(request);
		List<MemberIfafirmBaseVO> list=distributorService.findAllIfafirm(distributorId, langCode);
		model.put("ifafirmList", list);
		
		List<GeneralDataVO>  currencList=findSysParameters("currency_type", langCode);
		model.put("currencList", currencList);
		String currency=ssVo.getDefCurrency();
		if(null==currency || "".equals(currency)){
			currency=CommonConstants.DEF_CURRENCY;
		}
		String currencyName=sysParamService.findNameByCode(currency, langCode);
		model.put("currency", currency);
		model.put("curName", currencyName);
		
		int decimal=2;
		if("JPY".equals(currency)){
			decimal=0;
		}
		model.put("decimal", decimal);
		
		return "console/distributor/clientManagement";
	}
	
	/**
	 * 查看客户投资账户
	 */
	@RequestMapping(value = "/investAccountInfo", method = RequestMethod.GET)
	public String investAccountInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "distributor/client/investAccountInfo";
	}
	
	/**
	 * 交易记录-demo
	 */
	@RequestMapping(value = "/transRecords", method = RequestMethod.GET)
	public String transRecords(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "distributor/client/transactionRecord";
	}
	
	/**
	 * 查看客户交易记录
	 */
	@RequestMapping(value = "/transRecordDetail", method = RequestMethod.GET)
	public String transRecordDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "distributor/client/transRecordDetail";
	}
	
	/**
	 * KYC记录
	 */
	@RequestMapping(value = "/kyc", method = RequestMethod.GET)
	public String kyc(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "console/distributor/client/kyc";
	}
	
	/**
	 * 查看客户KYC记录
	 */
	@RequestMapping(value = "/kycInfo", method = RequestMethod.GET)
	public String kycInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "distributor/client/clientDetailKyc";
	}
}
