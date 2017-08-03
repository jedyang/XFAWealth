/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.oms.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.PropertyUtils;
import com.fsll.dao.entity.WebInterfaceLog;
import com.fsll.oms.OmsConstants;
import com.fsll.oms.WebServiceProxy;
import com.fsll.oms.service.OmsAimaService;
import com.fsll.oms.vo.OmsAimaLogonVO;

/**
 * oms aima的接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-12-26
 */
@Service("omsAimaService")
public class OmsAimaServiceImpl extends BaseService implements OmsAimaService{
    @Autowired
    private WebServiceProxy webServiceProxy;
	/**
	 * 登录Aima方法
	 * @param code
	 * @param pwd
	 * @return LogonVO
	 */
	public OmsAimaLogonVO saveLogin(HttpServletRequest request,String code,String pwd){
        try {
        	
    		//保存调用日志
    		WebInterfaceLog log = new WebInterfaceLog();
    		log.setInvokerType("F");
    		log.setBusKey(code);
    		log.setResultFlag("1");
    		log.setModuleType(OmsConstants.AIMA_LOG);
    		log.setMethod("saveLogin");
        	
        	OmsAimaLogonVO vo = new OmsAimaLogonVO();
        	com.fsll.oms.aimaws.OMSAPIWebServiceSoapStub omsApiWebService = webServiceProxy.getOmsApiWebService();
    		if(code == null || pwd == null ){//都为空时,统一从配置文件中拿相关数据
    			code = PropertyUtils.getConfPropertyValue("ws.aima.user");
    			pwd = PropertyUtils.getConfPropertyValue("ws.aima.pwd");
    		}
        	com.fsll.oms.aimaws.WSLogonResult logonResult = omsApiWebService.logon(code,pwd);
        	vo.setUserCode(code);
    		vo.setErrorCode(logonResult.getErrorCode());
    		vo.setErrorMessage(logonResult.getErrorMessage());
    		vo.setSessionID(logonResult.getSessionID());
    		
    		String content = logonResult.getErrorCode()+"  "+logonResult.getErrorMessage()+"  "+logonResult.getSessionID();
    		log.setContent(content);
    		webInterfaceLogService.saveOrUpdate(log);
    		
    		return vo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 修改密码
	 * @param 修改人登录oms系统的 code
	 * @param 修改人登录oms系统的 密码
	 * @param 修改目标的用户类型 userType 0=ifa  1=investor
	 * @param 修改目标的code  targetCode
	 * @return 
	 */
	public boolean saveModPwd(HttpServletRequest request,String code,String pwd,String userType,String targetCode){
		boolean flag = false;
		String memo = "";
		try {
			OmsAimaLogonVO logonVO = this.saveLogin(request, code, pwd);
			memo = logonVO.getErrorMessage();
			if(logonVO.getErrorCode() == 0){
				com.fsll.oms.aimaws.OMSAPIWebServiceSoapStub omsApiWebService = webServiceProxy.getOmsApiWebService();
				com.fsll.oms.aimaws.WSResult dsResult = omsApiWebService.resetPassword(code, logonVO.getSessionID(), userType, targetCode);
			    if(dsResult.getErrorCode() == 0){
			    	flag = true;
			    }else{
			    	memo = dsResult.getErrorMessage();
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//保存调用日志
		WebInterfaceLog log = new WebInterfaceLog();
		log.setInvokerType("F");
		log.setBusKey(code);
		if(flag){
			log.setResultFlag("1");
		}else{
			log.setResultFlag("0");
		}
		log.setMemo(memo);
		log.setModuleType(OmsConstants.AIMA_LOG);
		log.setMethod("saveModPwd");
		webInterfaceLogService.saveOrUpdate(log);
		return flag;
	}
	
	/**
	 * 修改pin码
	 * @param 修改人登录oms系统的 code
	 * @param 修改人登录oms系统的 密码
	 * @param 修改目标的用户类型 userType 0=ifa  1=investor
	 * @param 修改目标的code  targetCode
	 * @return 
	 */
	public boolean saveModPin(HttpServletRequest request,String code,String pwd,String userType,String targetCode){
		boolean flag = false;
		String memo = "";
		try {
			OmsAimaLogonVO logonVO = this.saveLogin(request, code, pwd);
			memo = logonVO.getErrorMessage();
			if(logonVO.getErrorCode() == 0){
				com.fsll.oms.aimaws.OMSAPIWebServiceSoapStub omsApiWebService = webServiceProxy.getOmsApiWebService();
				com.fsll.oms.aimaws.WSResult dsResult = omsApiWebService.resetPIN(code, logonVO.getSessionID(), userType, targetCode);
			    if(dsResult.getErrorCode() == 0){
			    	flag = true;
			    }else{
			    	memo = dsResult.getErrorMessage();
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//保存调用日志
		WebInterfaceLog log = new WebInterfaceLog();
		log.setInvokerType("F");
		log.setBusKey(code);
		if(flag){
			log.setResultFlag("1");
		}else{
			log.setResultFlag("0");
		}
		log.setMemo(memo);
		log.setModuleType(OmsConstants.AIMA_LOG);
		log.setMethod("saveModPin");
		webInterfaceLogService.saveOrUpdate(log);
		return flag;
	}
	
	/**
	 * 查询帐户资产
	 * @param code ae_code或者account_code
	 */
/*	public List<InvestorAccountCurrency> saveCurrencyAccount(HttpServletRequest request,String code){
		try {
			List<InvestorAccountCurrency> rList = new ArrayList<InvestorAccountCurrency>();
			OmsAimaLogonVO logonVO = this.saveLogin(request, null, null);
			String memo = "";
			if(logonVO.getErrorCode() == 0){
				com.fsll.oms.aimaws.OMSAPIWebServiceSoapStub omsApiWebService = webServiceProxy.getOmsApiWebService();
				com.fsll.oms.aimaws.WSDSResult dsResult = omsApiWebService.getBalance(logonVO.getUserCode(), logonVO.getSessionID(),code);
			    if(dsResult.getErrorCode() == 0){
			    	com.fsll.oms.aimaws.WSDSResultDataSet dsResultDataSet = dsResult.getDataSet();
			    	org.apache.axis.message.MessageElement [] anyArr = dsResultDataSet.get_any();
			    	
					String hql="from InvestorAccount r where r.accountNo='"+code+"' ";
					List<InvestorAccount> listIA = baseDao.find(hql,null, false);
					InvestorAccount account = null;
					if (null!=listIA&&!listIA.isEmpty()) {
						account = listIA.get(0);
						//1.删除旧记录
						String hqlDel = "delete from InvestorAccountCurrency r where r.account.id='"+account.getId()+"' ";
						baseDao.updateHql(hqlDel,null);
				    	if(anyArr.length  > 0){
				    		for(int k = 0; k < anyArr.length ; k++){
				    			org.apache.axis.message.MessageElement el = anyArr[k]; 
				    			memo = el.getAsString();
				    			if(memo.contains("diffgr:diffgram")){
				    				Document doc = DocumentHelper.parseText(memo);
				    				Element rootEl = doc.getRootElement();
				    				Iterator<Element> itFirst = rootEl.elementIterator();
				    				while(itFirst.hasNext()){
				    					Element eFirst = itFirst.next();  
					    				Iterator<Element> itSec = eFirst.elementIterator();
					    		        while(itSec.hasNext()){
						    				String cur = "";
						    				String cashBalance = "";
						    				String ledgerBalance = "";
						    				//String mTDInterest = "";
						    				String holdFund = "";
					    		            Element eSec = itSec.next();  
					    		            Iterator<Element> itThr = eSec.elementIterator();
						    		        while(itThr.hasNext()){  
						    		            Element eThr = itThr.next();
						    		            String tagName =  eThr.getName();
						    		            String tagValue =  eThr.getText();
						    		            if("cur".equals(tagName)){
						    		            	cur = tagValue;
						    		            }else if("cashBalance".equals(tagName)){
						    		            	cashBalance = tagValue;
						    		            }else if("ledgerBalance".equals(tagName)){
						    		            	ledgerBalance = tagValue;
						    		            //}else if("mTDInterest".equals(tagName)){
						    		            //	mTDInterest = tagValue;
						    		            }else if("holdFund".equals(tagName)){
						    		            	holdFund = tagValue;
						    		            }
						    		        }
						    		        InvestorAccountCurrency voTmp = new InvestorAccountCurrency();
						    		        voTmp.setAccount(account);
						                    voTmp.setCurrency(cur);
						                    SysParamConfig config = this.getParamConfigByCode(cur);
						                    if(config != null){
						                    	voTmp.setCurrencyCode(config.getConfigCode());
						                    }
						    		        voTmp.setCashAvailable(Double.parseDouble(cashBalance));
						    		        voTmp.setCashWithdrawal(Double.parseDouble(ledgerBalance));
						    		        voTmp.setCashHold(Double.parseDouble(holdFund));
						    		        this.baseDao.create(voTmp);
					    		        }
				    				}
				    			}
				    		}
				    	}
						
					}
			    }
			}
			
			//保存调用日志
			WebInterfaceLog log = new WebInterfaceLog();
			log.setInvokerType("F");
			log.setBusKey(code);
			log.setResultFlag("1");
			log.setModuleType(OmsConstants.AIMA_LOG);
			log.setMethod("saveCurrencyAccount");
			log.setContent(memo);
			webInterfaceLogService.saveOrUpdate(log);
			
			String hql="from InvestorAccountCurrency r where r.accountNo='"+code+"' ";
			List<InvestorAccountCurrency> list = baseDao.find(hql,null, false);
			if (null!=list && !list.isEmpty()) {
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
}
