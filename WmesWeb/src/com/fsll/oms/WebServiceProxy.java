/*
 * Copyright (c) 2009-2012 by fsll
 * All rights reserved.
 */

package com.fsll.oms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.fsll.common.util.PropertyUtils;

/**
 * 描述：服务任务代理类
 * @author mjhuang
 * @version 1.0
 */
@Component("webServiceProxy")
public class WebServiceProxy {
	
	//private final Log log = LogFactory.getLog(WebServiceProxy.class);
	
	//开户相关接口
	private com.fsll.oms.accountws.OMSAPIWebServiceLocator omsApiAccountWebServiceLocator = new com.fsll.oms.accountws.OMSAPIWebServiceLocator();
	private com.fsll.oms.accountws.OMSAPIWebServiceSoapStub omsApiAccountWebService;
	public com.fsll.oms.accountws.OMSAPIWebServiceSoapStub getOmsAccountApiWebService(){
		try{
			omsApiAccountWebServiceLocator.setOMSAPIWebServiceSoapEndpointAddress(this.getPropertieValue("ws.account.url"));
			omsApiAccountWebService = (com.fsll.oms.accountws.OMSAPIWebServiceSoapStub)omsApiAccountWebServiceLocator.getOMSAPIWebServiceSoap();
			return omsApiAccountWebService;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	//Aima管理相关接口
	private com.fsll.oms.aimaws.OMSAPIWebServiceLocator omsApiWebServiceLocator = new com.fsll.oms.aimaws.OMSAPIWebServiceLocator();
	private com.fsll.oms.aimaws.OMSAPIWebServiceSoapStub omsApiWebService;
	public com.fsll.oms.aimaws.OMSAPIWebServiceSoapStub getOmsApiWebService(){
		try{
			omsApiWebServiceLocator.setOMSAPIWebServiceSoapEndpointAddress(this.getPropertieValue("ws.aima.url"));
			omsApiWebService = (com.fsll.oms.aimaws.OMSAPIWebServiceSoapStub)omsApiWebServiceLocator.getOMSAPIWebServiceSoap();
			return omsApiWebService;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
    /**
     * 读取配置文件
     * @param proName
     * @return
     */
	private String getPropertieValue(String proName) {
		return PropertyUtils.getConfPropertyValue(proName);
	}
	
}
