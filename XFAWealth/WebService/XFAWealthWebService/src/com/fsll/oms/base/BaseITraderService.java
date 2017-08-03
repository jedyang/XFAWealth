/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.oms.base;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.dao.entity.DistributorServer;
import com.fsll.dao.entity.MemberAccountSso;
import com.fsll.dao.entity.WebInterfaceLog;
import com.fsll.oms.OmsConstants;

/**
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-1-16
 */
public class BaseITraderService extends BaseService{
	/**
	 * 获得Socket
	 * @param distributorId 关联的distributor
	 * @param type ssm或者its
	 */
	protected Socket getSock(String distributorId,String type){
		try {
			String hql="from DistributorServer r where r.distributor.id='"+distributorId+"'";
			List<DistributorServer> listTmp = baseDao.find(hql,null, false);
			DistributorServer distributorServer = null;
			if (null!=listTmp&&!listTmp.isEmpty()) {
				distributorServer =  listTmp.get(0);
			}
			if(distributorServer != null){
				if("ssm".equals(type)){
					String serverIp = distributorServer.getOmsSsmIp();
					int serverPort = distributorServer.getOmsSsmPort();
					InetAddress addr = InetAddress.getByName(serverIp);
					return new Socket(addr,serverPort);//SSM
				}else if("its".equals(type)){
					String serverIp = distributorServer.getOmsItsIp();
					int serverPort = distributorServer.getOmsItsPort();
					InetAddress addr = InetAddress.getByName(serverIp);
					return new Socket(addr,serverPort);//its
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 出错信息的处理
	 * @param moduleType 处理的模块
	 * @param waitHandleMsg 待处理消息
	 */
	protected void saveHandleError(String moduleType,String waitHandleMsg){
		if("session".equals(moduleType)){
			String tmpMsg = waitHandleMsg;
			tmpMsg = tmpMsg.substring(tmpMsg.indexOf("|")+1);
			Map map = StrUtils.stringToMap(tmpMsg);
			Iterator iter = map.entrySet().iterator();
			String errorCode = "";
			String errorMsg = "";
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String)entry.getKey();
				String val = (String)entry.getValue();
				if("25".equals(key)){
					errorMsg = val;
				}else if("39".equals(key)){
					errorCode = val;
				}
			}
			
			//保存日志
			WebInterfaceLog log = new WebInterfaceLog();
			log.setInvokerType("F");
			log.setBusKey(errorCode);
			log.setResultFlag("0");
			log.setModuleType(OmsConstants.ITS_LOG);
			log.setMethod("saveHandleError");
			log.setContent(waitHandleMsg);
			log.setCreateTime(new Date());
			log.setMemo(errorMsg);
			
			if("10".equals(errorCode) || "13".equals(errorCode)){//10登录失败 13用户禁用
				String code = "";
				iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String)entry.getKey();
					String val = (String)entry.getValue();
					if("13".equals(key)){
						code = val;
					}
				}
				if(!"".equals(code)){
					String hql="from MemberAccountSso r where r.accountCode='"+code+"' ";
					List<MemberAccountSso> listSSO = baseDao.find(hql,null, false);
					if (null!=listSSO&&!listSSO.isEmpty()) {
						MemberAccountSso ssoTmp =  listSSO.get(0);
						ssoTmp.setLastUpdate(new Date());
						ssoTmp.setErrorCode(errorCode);
						ssoTmp.setErrorMsg(errorMsg);
						baseDao.update(ssoTmp);
					}
				}			
				baseDao.create(log);
			}else if("19".equals(errorCode)){//19 Pin校验失败
				String sessionId = "";
				iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String)entry.getKey();
					String val = (String)entry.getValue();
					if("0".equals(key)){
						sessionId = val;
					}
				}
				if(!"".equals(sessionId)){
					String hql="from MemberAccountSso r where r.sessionId='"+sessionId+"' ";
					List<MemberAccountSso> listSSO = baseDao.find(hql,null, false);
					if (null!=listSSO&&!listSSO.isEmpty()) {
						MemberAccountSso ssoTmp =  listSSO.get(0);
						ssoTmp.setLastUpdate(new Date());
						ssoTmp.setErrorCode(errorCode);
						ssoTmp.setErrorMsg(errorMsg);
						baseDao.update(ssoTmp);
					}
				}
				baseDao.create(log);
			}else if("20".equals(errorCode)){// 20 verify校验失败
				String sessionId = "";
				iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String)entry.getKey();
					String val = (String)entry.getValue();
					if("0".equals(key)){
						sessionId = val;
					}
				}
				//会话校验失败,删除sso记录
				if(!"".equals(sessionId)){
					String hql="delete from MemberAccountSso r where r.sessionId='"+sessionId+"' ";
					baseDao.updateHql(hql, null);
				}
				baseDao.create(log);
			}
		}
	}
	
	/**
	 * 处理消息 因为在不同的线程中,所以要使用自定义事务
	 * @param type 类型 login登录   pin码校验   verify会话校验
	 * @param waitHandleMsg 待处理消息
	 */
	protected MemberAccountSso saveSessionMsg(String type,String waitHandleMsg){
		MemberAccountSso sso = new MemberAccountSso();
		String tmpMsg = waitHandleMsg;
		tmpMsg = tmpMsg.substring(tmpMsg.indexOf("|")+1);
		Map map = StrUtils.stringToMap(tmpMsg);
		Iterator iter = map.entrySet().iterator();
		String clientCode = "";
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String)entry.getKey();
			String val = (String)entry.getValue();
			if("0".equals(key)){
				sso.setSessionId(val);
			}else if("13".equals(key)){
				String[] tmpArr = val.split("@");
				clientCode = tmpArr[0];
				sso.setAccountCode(clientCode);
			}else if("22".equals(key)){
				sso.setType(val);
			}else if("191".equals(key)){
				sso.setTradeId(val);
			}else if("29".equals(key)){
				val = val.replace("/", "-");
				sso.setPwdExpireTime(DateUtil.getDate(val,"yyyy-MM-dd HH:mm:ss"));
			}else if("134".equals(key)){
				val = val.replace("/", "-");
				sso.setTradeSystemTime(DateUtil.getDate(val,"yyyy-MM-dd HH:mm:ss"));
			}else if("116".equals(key)){
				sso.setPinPos(val);
			}else if("1115".equals(key)){
				val = val.replace("/", "-");
				sso.setLastLoginTime(DateUtil.getDate(val,"yyyy-MM-dd HH:mm:ss"));
			}
		}
		if("login".equals(type) && !"".equals(clientCode)){//登录
			sso.setPinCheck("0");
			sso.setCreateTime(new Date());
			sso.setVerifyFlag("0");
			sso.setVerifyTime(null);
			sso = (MemberAccountSso)baseDao.create(sso);
			return sso;
		}else if("pin".equals(type) && !"".equals(clientCode)){//pin校验
			String hql="from MemberAccountSso r where r.accountCode='"+clientCode+"' ";
			List<MemberAccountSso> listSSO = baseDao.find(hql,null, false);
			if (null!=listSSO&&!listSSO.isEmpty()) {
				MemberAccountSso ssoTmp =  listSSO.get(0);
				ssoTmp.setLastUpdate(new Date());
				ssoTmp.setPinCheck("1");
				ssoTmp.setErrorCode("");
				ssoTmp.setErrorMsg("");
				return (MemberAccountSso)baseDao.update(ssoTmp);
			}
		}else if("verify".equals(type) && !"".equals(clientCode)){//会话校验
			String hql="from MemberAccountSso r where r.accountCode='"+clientCode+"' ";
			List<MemberAccountSso> listSSO = baseDao.find(hql,null, false);
			if (null!=listSSO&&!listSSO.isEmpty()) {
				MemberAccountSso ssoTmp =  listSSO.get(0);
				ssoTmp.setTradeSystemTime(sso.getTradeSystemTime());
				ssoTmp.setExpireTime(DateUtil.getInternalDateByMin(new Date(),OmsConstants.TRADE_TIME_OUT));//过期时间
				ssoTmp.setLastUpdate(new Date());
				ssoTmp.setVerifyFlag("1");
				ssoTmp.setVerifyTime(new Date());
				ssoTmp.setErrorCode("");
				ssoTmp.setErrorMsg("");
				return (MemberAccountSso)baseDao.update(ssoTmp);
			}
		}
		return null;
	}
	
	
	//==================================== 旧代码备用  ===========================================================
	/*	*//**
	 * 初始化SocketClient
	 * @param type ssm或者its
	 *//*
	protected SocketClientUtil initSocketClient(String type){
		if("ssm".equals(type) || "its".equals(type)){
			WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();  
	        ServletContext servletContext = webApplicationContext.getServletContext();
			if("ssm".equals(type)){
				Object obj = servletContext.getAttribute(CoreConstants.ITRADER_SSM_SERVER);
				if(obj != null){
					SocketClientUtil client = (SocketClientUtil)obj;
					if(!client.isRunning()){
						client.start();
					}
					servletContext.setAttribute(CoreConstants.ITRADER_SSM_SERVER,client);
					return client;
				}else{
					SocketClientUtil client = new SocketClientUtil("ssm");
					client.start();
					servletContext.setAttribute(CoreConstants.ITRADER_SSM_SERVER,client);
					return client;
				}
			}else if("its".equals(type)){
				Object obj = servletContext.getAttribute(CoreConstants.ITRADER_ITS_SERVER);
				if(obj != null){
					SocketClientUtil client = (SocketClientUtil)obj;
					if(!client.isRunning()){
						client.start();
					}
					servletContext.setAttribute(CoreConstants.ITRADER_ITS_SERVER,client);
					return client;
				}else{
					SocketClientUtil client = new SocketClientUtil("its");
					client.start();
					servletContext.setAttribute(CoreConstants.ITRADER_ITS_SERVER,client);
					return client;
				}
			}
		}
		return null;
	}
	
	*//**
	 *停止SocketClient
	 * @param type ssm或者its
	 *//*
	protected void closeSocketClient(String type){
		if("ssm".equals(type) || "its".equals(type)){
			WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();  
	        ServletContext servletContext = webApplicationContext.getServletContext();
			if("ssm".equals(type)){
				Object obj = servletContext.getAttribute(CoreConstants.ITRADER_SSM_SERVER);
				if(obj != null){
					SocketClientUtil client = (SocketClientUtil)obj;
					client.stop();
				}
			}else if("its".equals(type)){
				Object obj = servletContext.getAttribute(CoreConstants.ITRADER_ITS_SERVER);
				if(obj != null){
					SocketClientUtil client = (SocketClientUtil)obj;
					client.stop();
				}
			}
		}
	}*/
	
	/**
	 * 检验会话的方法
	 * @param request
	 * @param socket
	 * @param code
	 */
/*	protected boolean saveVerify(HttpServletRequest request,Socket socket,String code,String sessionId){
		try {
			String sendMsg = "ITS|verify|0|"+sessionId+"|13|"+code+"@"+this.getRemoteHost(request)+"|";
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			out.println(sendMsg);//将字符串输出到Server
			out.flush();//刷新输出流,使Server马上收到该字符串
			String resultMsg = in.readLine();
			if(resultMsg.startsWith("error")){//会话校验失败,删除sso记录
				String hql="delete from MemberAccountSso r where r.sessionId='"+sessionId+"' ";
				baseDao.updateHql(hql, null);
			}else{
				MemberAccountSso sso = saveSessionMsg("verify",resultMsg);
				if(sso != null){
					return true;
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}*/
	
}
