/*
 * Copyright (c) 2009-2012 by fsll
 * All rights reserved.
 */

package com.fsll.common.util;

import java.net.URL;

import net.sf.json.JSONObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fsll.core.CoreConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;

/**
 * @author michael
 * @version 1.0
 * Created On: Dec 26, 2016
 */
public class WebServiceUtil {
	final static String WORKFLOW_OPEN_ACCOUNT = "Investor_open_account";// 开户流程的invoke
	
	//消息服务配置信息config.properties
	final static String SMS_SERVICE_URL = "sms_server_url";//消息服务url
	final static String SMS_SERVICE_CODE = "sms_msg_code";//账号编码
	final static String SMS_SERVICE_PWD = "sms_msg_pwd";//密码
	final static String SMS_SERVICE_IP = "sms_msg_server_ip";//使用的短信服务器ip
	final static String SMS_SERVICE_STS = "sms_msg_service";//消息服务状态：start | stop

	/**
	 * 调用ws接口，获取返回结果(适用与RestFul的url参数方式的调用)
	 * @author michael
	 * @param urlString  http://www.xx.com/service/servicename?a=a&b=b...
	 * @return
	 */
	public static String sendWebServiceByUrl(String urlString){
		String res = ""; 
		try { 
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
//			conn.addRequestProperty("", value);
			java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line;
			}
			in.close();
		} catch (Exception e) {
			//logger.error("error in wapaction,and e is " + e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 调用ws接口，获取返回结果(适用与RestFul的body参数方式的调用)
	 * @author michael
	 * @param urlString http://www.xx.com/service/servicename
	 * @param bodyString 参数json字符串
	 */
	public static String sendWebServiceByBody(String urlString, String bodyString) {
		RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        
        JSONObject jsonObj = JSONObject.fromObject(bodyString);
//        JSONObject jsonObj = new JSONObject();
        
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);

        String result = restTemplate.postForObject(urlString, formEntity, String.class);
//        System.out.println(result);
        return result;
	}
	
	/**
	 * 工作流接口：创建开户审批流程
	 * 
	 * @param accountId
	 * @param userId
	 * @return
	 */
	public static  String createFlow(String accountId, String userId) {
		// 创建流程
		String wsServer = getCompWsServer();
		String urlString = wsServer + CoreConstants.COMP_WS_WORKFLOW_CREATE;

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("invokCode", WORKFLOW_OPEN_ACCOUNT);
		jsonObj.put("businessId", accountId);
		jsonObj.put("flowName", "Open account");// 测试
		jsonObj.put("userId", userId);// 流程创建人
		String jsonString = jsonObj.toString();
		String result = sendWebServiceByBody(urlString, jsonString);
		// JSONObject jsonObject = JSONObject.fromObject(result);
		return result;
	}

	/**
	 * 工作流接口：查询开户流程的状态
	 * @param accountId 业务id
	 * @param userId  环节处理人id
	 * @return
	 */
	public static String queryFlow(String accountId, String userId) {
		// 查询流程
		String wsServer = getCompWsServer();
		String urlString = wsServer + CoreConstants.COMP_WS_WORKFLOW_QUERY;

		//检查必填项
		if (StrUtils.isEmpty(wsServer) || StrUtils.isEmpty(urlString) || StrUtils.isEmpty(accountId) 
				|| StrUtils.isEmpty(userId)){
			ResultWS result = new ResultWS();
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return JsonUtil.toJson(result);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("invokCode", WORKFLOW_OPEN_ACCOUNT);
		jsonObj.put("businessId", accountId);
		jsonObj.put("flowUserId", userId);
		String jsonString = jsonObj.toString();
		String result = sendWebServiceByBody(urlString, jsonString);
		return result;
	}

	/**
	 * 工作流接口：插入开户流程待办
	 * 
	 * @param accountId
	 * @param instanseId
	 * @param flowCode
	 * @param userId
	 * @param nextUsers
	 * @param todoUrl
	 * @return
	 */
	public static String insertTodo(String accountId, String instanseId, String flowCode, String userId, String nextUsers, String todoUrl) {
		// 更新流程
		String wsServer = getCompWsServer();
		String urlString = wsServer + CoreConstants.COMP_WS_WORKFLOW_INSERT;

		//检查必填项
		if (StrUtils.isEmpty(wsServer) || StrUtils.isEmpty(urlString) || StrUtils.isEmpty(accountId) || StrUtils.isEmpty(flowCode)
				|| StrUtils.isEmpty(instanseId) || StrUtils.isEmpty(userId) || StrUtils.isEmpty(nextUsers) || StrUtils.isEmpty(todoUrl)){
			ResultWS result = new ResultWS();
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			String resultString = JsonUtil.objectToJsonStr(result);
			return resultString;
		}
//		if (null == instanseId || "".equals(instanseId)) {
//			InvestorAccount account = investorService.findInvestorAccountById(accountId);
//			instanseId = account.getFlowId();
//		}

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("invokCode", WORKFLOW_OPEN_ACCOUNT);
		jsonObj.put("businessId", accountId);
		jsonObj.put("flowCode", flowCode);
		jsonObj.put("userId", userId);
		jsonObj.put("flowUserIds", nextUsers);
		jsonObj.put("todoUrl", todoUrl);
		jsonObj.put("instanseId", instanseId);
		String jsonString = jsonObj.toString();
		String result = sendWebServiceByBody(urlString, jsonString);
		return result;
	}

	/**
	 * 工作流接口：更新开户流程状态
	 * 
	 * @param accountId
	 * @param flowCode
	 * @param userId
	 * @param nextUsers
	 * @param checkStatus
	 * @param comment
	 * @return
	 */
	public static String updateFlow(String accountId, String instanseId, String flowCode, String userId, String checkStatus, String comment) {
		// 更新流程
		String wsServer = getCompWsServer();
		String urlString = wsServer + CoreConstants.COMP_WS_WORKFLOW_UPDATE;
		
		//检查必填项
		if (StrUtils.isEmpty(wsServer) || StrUtils.isEmpty(urlString) || StrUtils.isEmpty(accountId) || StrUtils.isEmpty(flowCode)
				|| StrUtils.isEmpty(instanseId) || StrUtils.isEmpty(userId) || StrUtils.isEmpty(checkStatus)){
			ResultWS result = new ResultWS();
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return JsonUtil.toJson(result);
		}
		
//		if (null == instanseId || "".equals(instanseId)) {
//			InvestorAccount account = investorService.findInvestorAccountById(accountId);
//			instanseId = account.getFlowId();
//		}

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("invokCode", WORKFLOW_OPEN_ACCOUNT);
		jsonObj.put("businessId", accountId);
		jsonObj.put("flowCode", flowCode);
		// jsonObj.put("processor", userId);
		jsonObj.put("flowUserId", userId);
		jsonObj.put("checkStatus", checkStatus);
		jsonObj.put("comment", comment);
		jsonObj.put("instanseId", instanseId);
		String jsonString = jsonObj.toString();
		String result = sendWebServiceByBody(urlString, jsonString);
		return result;
	}
	
	/**
	 * 获取组件接口的服务器
	 * @return
	 */
	public static String getCompWsServer(){
		String wsServer = PropertyUtils.getConfPropertyValue("comp_ws_server");//获取配置文件的配置
		if (null==wsServer || "".equals(wsServer)) wsServer = CoreConstants.COMP_WS_SERVER;
		wsServer = "http://localhost/comp";//@@测试，临时写死本地
		return wsServer;
	}
}
