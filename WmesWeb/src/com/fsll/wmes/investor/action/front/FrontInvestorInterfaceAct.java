package com.fsll.wmes.investor.action.front;

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
import com.fsll.common.util.PageHelper;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.oms.service.ITraderSendService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorBackground;
import com.fsll.wmes.entity.InvestorDocCheck;
import com.fsll.wmes.entity.MemberAccountSso;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.InterfaceCheckPwdAccountVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;

/***
 * 
 * 接口有关
 * @author wwlin
 * @date 2016-7-7
 */
@Controller
@RequestMapping("/front/investorinterface")
public class FrontInvestorInterfaceAct  extends CoreBaseAct {
	@Autowired
	private InvestorService investorService;
	
	@Autowired
	private ITraderSendService iTraderSendService;
	
	@Autowired
	private MemberBaseService memberBaseService;
	
	@Autowired
	private IfaInfoService ifaInfoService;
	
	/**
	 * @author 
	 */
	@RequestMapping(value = "/inputTransactionPassword", method = RequestMethod.GET)
	public String inputTransactionPassword(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		//accountno|type,accountno|type,accountno|type,
		//MemberBase loginUser = getLoginUser(request);
//		if (loginUser != null) {
//			int memberType = loginUser.getMemberType();
//			model.put("memberType",memberType);
//			if(memberType==2){
//				// get ifa ac code
//				String memberId = loginUser.getId();
//				MemberIfa ifa = memberBaseService.findIfaMember(memberId);
//				if(null!=ifa){
//					//get ae code
//					String ifaId = ifa.getId();
//					IfaDistributor ifaDistributor = investorService.findIfaDistributorByIfaId(ifaId);
//					if(ifaDistributor!=null&&""!=ifaDistributor.getId()){
//						model.put("aeCode",ifaDistributor.getAeCode());
//					}
//					else{
//						model.put("aeCode","");
//					}
//				} else{
//					model.put("aeCode","");
//				}
//			}
//		}
		String accountList = request.getParameter("accountList");
		String accounttype = request.getParameter("accounttype");
		String[] array = accountList.split(",");
		List<InterfaceCheckPwdAccountVO> list = new ArrayList<InterfaceCheckPwdAccountVO>();
		if(array.length>0){
			for(String account : array){
				if(StringUtils.isNotBlank(account)&&account.length()>0){
					//if(account.length()>0){
						InterfaceCheckPwdAccountVO vo = new InterfaceCheckPwdAccountVO();
						vo.setAccountNo(account);
						vo.setAccountType(accounttype);
						if("1".equals(accounttype)){ //investor
							String accountNo = account;
							List<InvestorAccount> investList = investorService.findInvestorAccountListByAccountNo(accountNo);
							if(null!=investList&&!investList.isEmpty()){
								InvestorAccount investor = investList.get(0);
								MemberDistributor distributor = investor.getDistributor();
								String logo = distributor.getLogofile();
								if(""!=logo){
									logo =PageHelper.getLogoUrl(logo, "D");
									vo.setDistributorLogo(logo);
								} else{
									logo =PageHelper.getLogoUrl("", "D");
									vo.setDistributorLogo(logo);
								}
								//vo.setDistributor(investor.getDistributor());
							}
						}else if("0".equals(accounttype)){ //ifa
							String aeCode = account;
							IfaDistributor ifaDistributor = ifaInfoService.findIfaDistributorInfo(aeCode);
							if(null != ifaDistributor){
								MemberDistributor distributor = ifaDistributor.getDistributor();
								String logo = distributor.getLogofile();
								if(""!=logo){
									logo =PageHelper.getLogoUrl(logo, "D");
									vo.setDistributorLogo(logo);
								} else{
									logo =PageHelper.getLogoUrl("", "D");
									vo.setDistributorLogo(logo);
								}
								//vo.setDistributor(investor.getDistributor());
							}
						}
						list.add(vo);
					//}
					
				}
			}
		}
		model.put("interfaceCheckPwdAccountVO",list);
		
		return "investor/interface/inputTransactionPassword";
	}
	
	
	
	/**
	 * @date 2016-11-01
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveLogin", method = RequestMethod.POST)
	public void saveLogin(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String accountNo = request.getParameter("accountNo");
		String accountPwd = request.getParameter("accountPwd");
		String accountType = request.getParameter("accountType");
		Map<String, Object> map = new HashMap<String, Object>();
		MemberAccountSso sso = iTraderSendService.saveLogin(request, accountType, accountNo, accountPwd);
		boolean flag = false;
		if(sso != null){
			flag = true;
			String pingPos = sso.getPinPos();
			map.put("pingPos", pingPos);
		}
		map.put("flag", flag);
		JsonUtil.toWriter(map, response);
	}
	
	 /**
	  * 检验Pin码的方法
	  * @param sessionId 会话id
	  * @param code ae_code或者account_code
	  * @param pinValue 要校验的pin码值,多个之间用,分隔
	  * @return true 成功 false失败
	  */
	@RequestMapping(value = "/savePin", method = RequestMethod.POST)
	public void savePin(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String accountNo = request.getParameter("accountNo");
		String pinCode = request.getParameter("inputPinCode");
		//String isForever = request.getParameter("isForever");
		String accountType = request.getParameter("accountType");
		Map<String, Object> map = new HashMap<String, Object>();
		if (pinCode.startsWith(","))pinCode = pinCode.substring(1,pinCode.length());
		if (pinCode.endsWith(","))pinCode = pinCode.substring(0,pinCode.length()-1);
		boolean flag = false;
		try {
			flag = iTraderSendService.savePin(request, accountType,accountNo, pinCode);
//			if(flag){
//				if("1".equals(isForever)){
//					iTraderSendService.saveAccountBind(request, accountNo, "1",accountType);
//				}else{
//					iTraderSendService.saveAccountBind(request, accountNo, "2");
//				}
//			flag = true;
//			if("1".equals(isForever)){
//				iTraderSendService.saveAccountBind(request, accountNo, "1");
//			}
//			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		map.put("flag", flag);
		JsonUtil.toWriter(map, response);
	}
	
	 /**
	  * 查询帐户持仓
	  * @param fromCode 发起人 ae_code或者account_code
	  * @param toCode   目标人ae_code或者account_code
	  * @return 
	  */
	@RequestMapping(value = "/savePosition", method = RequestMethod.POST)
	public void savePosition(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		
	}


}
