/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.oms.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.fsll.common.util.StrUtils;
import com.fsll.oms.OmsConstants;
import com.fsll.oms.base.BaseITraderService;
import com.fsll.oms.service.ITraderSendService;
import com.fsll.oms.vo.OmsAddOrderVO;
import com.fsll.oms.vo.OmsDelOrderVO;
import com.fsll.wmes.entity.MemberAccountSso;
import com.fsll.wmes.entity.WebInterfaceLog;

/**
 * itrader消息发送方法
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-12-26
 */
@Service("iTraderSendService")
//@Transactional
public class ITraderSendServiceImpl extends BaseITraderService implements ITraderSendService{
	/**
	 * 登录指令方法
	 * @param type 0是AE或者ifa登录 1是investor登录
	 * @param code
	 * @param pwd
	 * return MemberAccountSso 
	 */
	public MemberAccountSso saveLogin(HttpServletRequest request,String type,String code,String pwd){
		try {
			if(!"0".equals(type) && !"1".equals(type))return null;
			
			//1.保存调用日志
			WebInterfaceLog log = new WebInterfaceLog();
			log.setInvokerType("F");
			log.setBusKey(code);
			log.setResultFlag("1");
			log.setModuleType(OmsConstants.ITS_LOG);
			log.setMethod("saveLogin");
			String tmpSendMsg = "login|13|"+code+"|21|****|22|"+type+"|157|Internet|99|"+OmsConstants.PLAT_TYPE+"|115|"+this.getRemoteHost(request)+"|";
			log.setContent(tmpSendMsg);
			webInterfaceLogService.saveOrUpdate(log);
			
			//2.登录时会清空sso的中记录,同时新增一条记录
			String hqlDel = "delete from MemberAccountSso r where r.accountCode='"+code+"' ";
			baseDao.updateHql(hqlDel,null);
			
			//2.2获得code关联的distributor
			String distributorId = "";
			if("0".equals(type)){//AE或者ifa
				String sql = "select distributor_id from ifa_distributor where is_valid='1' and ae_code='"+code+"'";
				List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
				//String sessionId = "";
				if (null!=list&&!list.isEmpty()){
					Map map = (HashMap)list.get(0);
					distributorId = map.get("distributor_id").toString();
				}
			}else if("1".equals(type)){//investor
				String sql = "select distributor_id from investor_account where account_no='"+code+"'";
				List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
				//String sessionId = "";
				if (null!=list&&!list.isEmpty()){
					Map map = (HashMap)list.get(0);
					distributorId = map.get("distributor_id").toString();
				}
			}
			
			String resultMsg = "";
			//3.调用接口
			Socket socket = this.getSock(distributorId,"ssm");
			if (null!=socket){
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				String sendMsg = "login|13|"+code+"|21|"+pwd+"|22|"+type+"|157|Internet|99|"+OmsConstants.PLAT_TYPE+"|115|"+this.getRemoteHost(request)+"|";//已认证
				out.println(sendMsg);//将字符串输出到Server
				out.flush();//刷新输出流,使Server马上收到该字符串
				resultMsg = in.readLine();
				socket.close();
			}
			
			//4.保存登录处理结果
			MemberAccountSso accountSso = new MemberAccountSso();
			String resultFlag = "";
			if(resultMsg.startsWith("error")){
				this.saveHandleError("session",resultMsg);
				resultFlag = "0";
			}else{
				accountSso = this.saveSessionMsg("login",resultMsg);
				if(accountSso != null){
					resultFlag = "1";
				}else{
					resultFlag = "0";
				}
			}
			
			//5.保存结果日志
			log = new WebInterfaceLog();
			log.setInvokerType("F");
			log.setBusKey(code);
			log.setResultFlag(resultFlag);
			log.setModuleType(OmsConstants.ITS_LOG);
			log.setMethod("saveLogin");
			log.setContent(resultMsg);
			webInterfaceLogService.saveOrUpdate(log);
			
			return accountSso;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 登出指令方法
	 * @param type
	 * @param code
	 * @return true 成功 false失败
	 */
	public boolean saveLogout(HttpServletRequest request,String type,String code){
		try {
			if(!"0".equals(type) && !"1".equals(type))return false;
			String hql="from MemberAccountSso r where r.accountCode='"+code+"'";
			List<MemberAccountSso> listTmp = baseDao.find(hql,null, false);
			if (null!=listTmp&&!listTmp.isEmpty()) {
				MemberAccountSso ssoTmp =  listTmp.get(0);
				
				//2.2获得code关联的distributor
				String distributorId = "";
				if("0".equals(type)){//AE或者ifa
					String sql = "select distributor_id from ifa_distributor where is_valid='1' and ae_code='"+code+"'";
					List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
					//String sessionId = "";
					if (null!=list&&!list.isEmpty()){
						Map map = (HashMap)list.get(0);
						distributorId = map.get("distributor_id").toString();
					}
				}else if("1".equals(type)){//investor
					String sql = "select distributor_id from investor_account where account_no='"+code+"'";
					List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
					//String sessionId = "";
					if (null!=list&&!list.isEmpty()){
						Map map = (HashMap)list.get(0);
						distributorId = map.get("distributor_id").toString();
					}
				}
				
				Socket socket = getSock(distributorId,"ssm");
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				String sendMsg = "logout|0|"+ssoTmp.getSessionId()+"|13|"+code+"@"+this.getRemoteHost(request)+"|";
				out.println(sendMsg);//将字符串输出到Server
				out.flush();//刷新输出流,使Server马上收到该字符串
				String resultMsg = in.readLine();
				socket.close();
				
				//返回的数据格式为logout|0|SSMSyjkn6gLfb=0ZjFfq+dZtPbtq=cIul+NNbRRj5KueyqjR+elb4z2WdcigJgIrz0zJSkYmoUr7PXe|13|833890001@127.0.0.1|
				String tmpMsg = resultMsg;
				tmpMsg = tmpMsg.substring(tmpMsg.indexOf("|")+1);
				Map map = StrUtils.stringToMap(tmpMsg);
				Iterator iter = map.entrySet().iterator();
				
				//String sessionId = "";
				String accountCode = "";
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String)entry.getKey();
					String val = (String)entry.getValue();
					if("13".equals(key)){
						String[] tmpArr = val.split("@");
						accountCode = tmpArr[0];
					}
					//else if("0".equals(key)){
					//	sessionId = val;
					//}
				}
				
				if(!"".equals(accountCode)){
					String hqlDel = "delete from MemberAccountSso r where r.accountCode='"+accountCode+"' ";
					baseDao.updateHql(hqlDel,null);
				}
				
				//1.保存调用日志
				WebInterfaceLog log = new WebInterfaceLog();
				log.setInvokerType("F");
				log.setBusKey(code);
				log.setResultFlag("1");
				log.setModuleType(OmsConstants.ITS_LOG);
				log.setMethod("saveLogout");
				log.setContent(sendMsg);
				webInterfaceLogService.saveOrUpdate(log);
				return true;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 检验Pin码的方法
	 * @param type 0是AE或者ifa登录 1是investor登录
	 * @param code ae_code或者account_code
	 * @param pinValue 要校验的pin码值,多个之间用,分隔
	 */
	public boolean savePin(HttpServletRequest request,String type,String code,String pinValue){
		try {
			String hql="from MemberAccountSso r where r.accountCode='"+code+"'";
			List<MemberAccountSso> listTmp = baseDao.find(hql,null, false);
			if (null!=listTmp&&!listTmp.isEmpty()) {
				MemberAccountSso ssoTmp =  listTmp.get(0);				
				String sendMsg = "PIN|0|"+ssoTmp.getSessionId()+"|13|"+code+"@"+this.getRemoteHost(request)+"|117|"+pinValue+"|157|Internet|";
				
				//1.保存调用日志
				WebInterfaceLog log = new WebInterfaceLog();
				log.setInvokerType("F");
				log.setBusKey(code);
				log.setResultFlag("1");
				log.setModuleType(OmsConstants.ITS_LOG);
				log.setMethod("savePin");
				log.setContent(sendMsg);
				webInterfaceLogService.saveOrUpdate(log);
				
				//2.2获得code关联的distributor
				String distributorId = "";
				if("0".equals(type)){//AE或者ifa
					String sql = "select distributor_id from ifa_distributor where is_valid='1' and ae_code='"+code+"'";
					List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
					//String sessionId = "";
					if (null!=list&&!list.isEmpty()){
						Map map = (HashMap)list.get(0);
						distributorId = map.get("distributor_id").toString();
					}
				}else if("1".equals(type)){//investor
					String sql = "select distributor_id from investor_account where account_no='"+code+"'";
					List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
					//String sessionId = "";
					if (null!=list&&!list.isEmpty()){
						Map map = (HashMap)list.get(0);
						distributorId = map.get("distributor_id").toString();
					}
				}
				
				//2.调用接口
				Socket socket = getSock(distributorId,"ssm");
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				out.println(sendMsg);//将字符串输出到Server
				out.flush();//刷新输出流,使Server马上收到该字符串
				String resultMsg = in.readLine();
				socket.close();
				
				MemberAccountSso accountSso = new MemberAccountSso();
				String resultFlag = "";
				if(resultMsg.startsWith("error")){
					this.saveHandleError("session",resultMsg);
					resultFlag = "0";
				}else{
					accountSso = this.saveSessionMsg("pin",resultMsg);
					if(accountSso != null){
						resultFlag = "1";
					}else{
						resultFlag = "0";
					}
				}
				
				//5.保存结果日志
				log = new WebInterfaceLog();
				log.setInvokerType("F");
				log.setBusKey(code);
				log.setResultFlag(resultFlag);
				log.setModuleType(OmsConstants.ITS_LOG);
				log.setMethod("savePin");
				log.setContent(resultMsg);
				webInterfaceLogService.saveOrUpdate(log);
				
				if(accountSso != null){
					return true;
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 下单时重新登录获得seesionId指令方法
	 * @param code
	 * @param pwd
	 * return MemberAccountSso 
	 */
	private MemberAccountSso saveReLogin(HttpServletRequest request,String code,String pwd){
		try {
			//1.登录时会清空sso的中记录,同时新增一条记录
			String hqlDel = "delete from MemberAccountSso r where r.accountCode='"+code+"' ";
			baseDao.updateHql(hqlDel,null);
			
			//2.获得code关联的distributor
			String type = "0";//0=AE或者ifa
			String distributorId = "";
			String sql = "select distributor_id from investor_account where account_no='"+code+"'";
			List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
			if (null!=list&&!list.isEmpty()){
				Map map = (HashMap)list.get(0);
				distributorId = map.get("distributor_id").toString();
				type = "1";
			}
			
			if("0".equals(type)){//AE或者ifa
				sql = "select distributor_id from ifa_distributor where is_valid='1' and ae_code='"+code+"'";
				list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
				if (null!=list&&!list.isEmpty()){
					Map map = (HashMap)list.get(0);
					distributorId = map.get("distributor_id").toString();
				}
			}
			
			String resultMsg = "";
			//3.调用接口
			Socket socket = this.getSock(distributorId,"ssm");
			if (null!=socket){
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				String sendMsg = "login|13|"+code+"|21|"+pwd+"|22|"+type+"|157|Internet|99|"+OmsConstants.PLAT_TYPE+"|115|"+this.getRemoteHost(request)+"|";//已认证
				out.println(sendMsg);//将字符串输出到Server
				out.flush();//刷新输出流,使Server马上收到该字符串
				resultMsg = in.readLine();
				socket.close();
			}
			
			//4.保存登录处理结果
			MemberAccountSso accountSso = new MemberAccountSso();
			accountSso = this.saveSessionMsg("login",resultMsg);
			return accountSso;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 新增订单
	 * @param orderVO 订单
	 * @return true 成功 false失败
	 */
	public boolean addOrder(HttpServletRequest request,OmsAddOrderVO orderVO){
		try {
			if(orderVO.getCreatorNo() != null && !"".equals(orderVO.getCreatorNo())){
				//重新登录的原因主要是为了防止提交订单后,原会话已过期
				MemberAccountSso ssoVO = this.saveReLogin(request,orderVO.getCreatorNo(),orderVO.getTradingPassword());
				if(ssoVO != null){
					//1获得 sessionId tradeId  type
					//String sqlSSO = "select id,session_id,trade_id,type from member_account_sso  where account_code='"+orderVO.getCreatorNo()+"'";
					//List listSSO = this.springJdbcQueryManager.springJdbcQueryForList(sqlSSO);
					//if (null!=listSSO&&!listSSO.isEmpty()){
						//Map map = (HashMap)listSSO.get(0);
						//String sessionId = map.get("session_id").toString();
						//String tradeId = map.get("trade_id").toString();
						//String type = map.get("type").toString();
						String sessionId = ssoVO.getSessionId();
						String tradeId = ssoVO.getTradeId();
						String type = ssoVO.getType();
					
						//2获得code关联的distributor
						String distributorId = "";
						String authorized = "";//是否授权客户
						if("0".equals(type)){//AE或者ifa
							String sql = "select distributor_id from ifa_distributor where is_valid='1' and ae_code='"+orderVO.getCreatorNo()+"'";
							List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
							if (null!=list&&!list.isEmpty()){
								Map map = (HashMap)list.get(0);
								distributorId = map.get("distributor_id").toString();
							}
						}else if("1".equals(type)){//investor
							String sql = "select distributor_id,authorized from investor_account where account_no='"+orderVO.getCreatorNo()+"'";
							List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
							if (null!=list&&!list.isEmpty()){
								Map map = (HashMap)list.get(0);
								distributorId = map.get("distributor_id").toString();
								authorized = map.get("authorized").toString();
							}
						}
						
						//3.调用接口校验会话
						String sendMsg = "ITS|verify|0|"+sessionId+"|13|"+orderVO.getCreatorNo()+"@"+this.getRemoteHost(request)+"|";
						Socket socket = getSock(distributorId,"its");
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintWriter out = new PrintWriter(socket.getOutputStream());
						out.println(sendMsg);//将字符串输出到Server
						out.flush();//刷新输出流,使Server马上收到该字符串
						String resultMsg = in.readLine();
						
						//4.保存登录处理结果
						MemberAccountSso accountSso = new MemberAccountSso();
						String resultFlag = "";
						if(resultMsg.startsWith("error")){
							this.saveHandleError("session",resultMsg);
							resultFlag = "0";
						}else{
							accountSso = this.saveSessionMsg("login",resultMsg);
							if(accountSso != null){
								resultFlag = "1";
							}else{
								resultFlag = "0";
							}
						}
						
						if("1".startsWith(resultFlag)){//会话校验成功
							//4.1.下单
							String msg = "";
							if("2".equals(orderVO.getOrderType())){//股票
								msg = "ORD|add|0|"+orderVO.getSymbolCode()+"|10|"+orderVO.getAccountNo()+"|11|"+orderVO.getTranType()+"|13|"+orderVO.getAeCode()+"|4|"+orderVO.getQuantity()+"|3|"+orderVO.getLimitedPrice()+"|74|"+orderVO.getOrderNO()+"|191|"+tradeId+"|";
								//相关费用计算
								msg += "487|TRANSFEE="+orderVO.getTranFeeRate()+",MINTRANSFEE="+orderVO.getTranFeeMini()+"|";
							}else{//默认是基金
								if("0".equals(orderVO.getTranType()) && orderVO.getLimitedPrice() > 0){//基金买入 数量固定为Quantity=1 LimitedPrice为申购金额
									msg = "ORD|add|0|"+orderVO.getSymbolCode()+"|10|"+orderVO.getAccountNo()+"|11|"+orderVO.getTranType()+"|13|"+orderVO.getAeCode()+"|4|1|3|"+orderVO.getLimitedPrice()+"|74|"+orderVO.getOrderNO()+"|191|"+tradeId+"|";
								    if(orderVO.getSwitchFlag() != null && "1".equals(orderVO.getSwitchFlag())){//如果是基金转换
								    	msg += "487|TRANSFEE="+orderVO.getTranFeeRate()+",SwitchP="+orderVO.getSwitchAllocRate()+"|455|"+orderVO.getSwitchGroup()+"|";
								    	msg += "490|67108864|";//表明转换单
								    }else{//正常基金下单
								    	msg += "487|TRANSFEE="+orderVO.getTranFeeRate()+",MINTRANSFEE="+orderVO.getTranFeeMini()+"|";
								    }
								}else if("1".equals(orderVO.getTranType())){//基金卖出  Quantity=赎回份额  LimitedPrice为1
									msg = "ORD|add|0|"+orderVO.getSymbolCode()+"|10|"+orderVO.getAccountNo()+"|11|"+orderVO.getTranType()+"|13|"+orderVO.getAeCode()+"|4|"+orderVO.getQuantity()+"|3|1|74|"+orderVO.getOrderNO()+"|191|"+tradeId+"|";
								    if(orderVO.getSwitchFlag() != null && "1".equals(orderVO.getSwitchFlag())){//如果是基金转换
								    	msg += "487|TRANSFEE="+orderVO.getTranFeeRate()+"|455|"+orderVO.getSwitchGroup()+"|";
								    	msg += "490|67108864|";//表明转换单
								    }else{//正常基金下单
								    	msg += "487|TRANSFEE="+orderVO.getTranFeeRate()+",MINTRANSFEE="+orderVO.getTranFeeMini()+"|";
								    }
								}
							}
							
							if(authorized != null && "0".equals(authorized)){//非授权审批
								msg += "491|8192|";
							}else{//授权与非授权都加上取消审批的标记
								msg += "491|8192|";
							}
							
							boolean orderResult = false;//下单结果
							if(!"".equals(msg)){							
								//保存下单指令
								WebInterfaceLog log = new WebInterfaceLog();
								log.setInvokerType("F");
								log.setBusKey(orderVO.getAccountNo());
								log.setResultFlag("1");
								log.setModuleType(OmsConstants.ITS_LOG);
								log.setMethod("addOrder");
								log.setContent(msg);
								webInterfaceLogService.saveOrUpdate(log);
								
								sendMsg = msg+"510|"+orderVO.getTradingPassword()+"|400|I|";
								out.println(sendMsg);
								out.flush();
								
								//因会返回所有效的image,所以放在统一的消息监听中进行消息
								/*
								resultMsg = in.readLine();
								String orderHistoryId = "";
								String waitHandleMsg = resultMsg;
								//4.2.下单指令出错
								if(waitHandleMsg.startsWith("image|ERRORD_")){//下单失败
									String tmpMsg = waitHandleMsg;
									tmpMsg = tmpMsg.substring(tmpMsg.indexOf("|")+1);//去掉image|
									tmpMsg = tmpMsg.substring(tmpMsg.indexOf("|")+1);//ERRORD_00700001|
									map = StrUtils.stringToMap(tmpMsg);
									Iterator iter = map.entrySet().iterator();
									String errorMsg = "";
									String orderStatus = "";
									while (iter.hasNext()) {
										Map.Entry entry = (Map.Entry) iter.next();
										String key = (String)entry.getKey();
										String val = (String)entry.getValue();
										if("25".equals(key)){
											errorMsg = val;
										}else if("74".equals(key)){
											orderHistoryId = val;
										}else if("5".equals(key)){
											orderStatus = val;
										}
									}				
									if(!"".equals(orderHistoryId)){
										String hql="update OrderHistory set errorMsg='"+errorMsg+"',status='"+orderStatus+"'  where id='"+orderHistoryId+"' ";
										baseDao.updateHql(hql, null);
									}
									orderResult = false;
								}else if(waitHandleMsg.startsWith("image|ORDA_") || waitHandleMsg.startsWith("image|ORD_")){//下单成功
									String tmpMsg = waitHandleMsg;
									tmpMsg = tmpMsg.substring(tmpMsg.indexOf("|")+1);//去掉image|
									tmpMsg = tmpMsg.substring(tmpMsg.indexOf("|")+1);//ORDA_833890001|
									map = StrUtils.stringToMap(tmpMsg);
									Iterator iter = map.entrySet().iterator();
									String orderNumber = "";
									String orderStatus = "";
									String tranUnit = "";//成效数量
									String tranAmount = "";//成交金额
									String currencyCode = "";//返回的货币类型
									String tranTime = "";//成交时间
									while (iter.hasNext()) {
										Map.Entry entry = (Map.Entry) iter.next();
										String key = (String)entry.getKey();
										String val = (String)entry.getValue();
										if("6".equals(key)){
											orderNumber = val;
										}else if("74".equals(key)){
											orderHistoryId = val;
										}else if("5".equals(key)){
											orderStatus = val;
										}else if("34".equals(key)){
											tranUnit = val;
										}else if("42".equals(key)){
											tranAmount = val;
										}else if("23".equals(key)){
											currencyCode = val;
										}else if("33".equals(key)){
											tranTime = val;
										}
									}
									if(!"".equals(orderHistoryId)){
										String hql="update OrderHistory set itsOrderNumber='"+orderNumber+"',status='"+orderStatus+"'";
										if(!"".equals(tranUnit)){
											hql+=",transactionUnit="+tranUnit+" ";
										}
										if(!"".equals(tranAmount)){
											hql+=",transactionAmount="+tranAmount+" ";
										}
										hql+="  where id='"+orderHistoryId+"' ";
										baseDao.updateHql(hql, null);
										
										//向OrderReturn表写记录
										OrderHistory order = new OrderHistory();
										order.setId(orderHistoryId);
										
										OrderReturn orderReturn = new OrderReturn();
										orderReturn.setOrder(order);
										orderReturn.setTransactionNo(orderNumber);
										orderReturn.setCurrencyCode(currencyCode);
										orderReturn.setRemark(waitHandleMsg);
										orderReturn.setStatus(orderStatus);
										if(!"".equals(tranUnit)){
											orderReturn.setTransactionUnit(Double.parseDouble(tranUnit));
										}
										if(!"".equals(tranAmount)){
											orderReturn.setTransactionAmount(Double.parseDouble(tranAmount));
										}
										if(!"".equals(tranTime)){
											tranTime = DateUtil.dateToDateString(new Date(),"yyyy-MM-dd")+" "+tranTime;
											orderReturn.setTransactionTime(DateUtil.getDate(tranTime,"yyyy-MM-dd HH:mm:ss"));
										}
										baseDao.create(orderReturn);
									}
									orderResult = true;
								}
								*/
								orderResult = true;
							}
							//断开连接
							socket.close();
							
							//保存调用日志
							WebInterfaceLog log = new WebInterfaceLog();
							log.setInvokerType("F");
							log.setBusKey(orderVO.getAccountNo());
							if(orderResult){
								log.setResultFlag("1");
								log.setContent("结束:下单成功");
							}else{
								log.setContent("结束:下单失败");
								log.setResultFlag("0");
							}
							log.setModuleType(OmsConstants.ITS_LOG);
							log.setMethod("addOrder");
							webInterfaceLogService.saveOrUpdate(log);
							return orderResult;
						}else{
							//断开连接
							socket.close();
							
							//保存调用日志
							WebInterfaceLog log = new WebInterfaceLog();
							log.setInvokerType("F");
							log.setBusKey(orderVO.getAccountNo());
							log.setResultFlag(resultFlag);
							log.setModuleType(OmsConstants.ITS_LOG);
							log.setMethod("addOrder");
							log.setContent("结束:下单失败,会话过期");
							webInterfaceLogService.saveOrUpdate(log);
							return false;
						}
					//}	
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 取消订单
	 * @param orderVO
	 * @return true 成功 false失败
	 */
	public boolean deleteOrder(HttpServletRequest request,OmsDelOrderVO orderVO){
		try {
			if(orderVO.getCreatorNo() != null && !"".equals(orderVO.getCreatorNo())){
				//重新登录的原因主要是为了防止提交订单后,原会话已过期
				MemberAccountSso ssoVO = this.saveReLogin(request,orderVO.getCreatorNo(),orderVO.getTradingPassword());
				if(ssoVO != null){
					//1获得 sessionId tradeId  type
					//String sqlSSO = "select id,session_id,trade_id,type from member_account_sso  where account_code='"+orderVO.getCreatorNo()+"'";
					//List listSSO = this.springJdbcQueryManager.springJdbcQueryForList(sqlSSO);
					//if (null!=listSSO&&!listSSO.isEmpty()){
						//Map map = (HashMap)listSSO.get(0);
						//String sessionId = map.get("session_id").toString();
						//String tradeId = map.get("trade_id").toString();
						//String type = map.get("type").toString();
						String sessionId = ssoVO.getSessionId();
						String tradeId = ssoVO.getTradeId();
						String type = ssoVO.getType();
					
					
						//2获得code关联的distributor
						String distributorId = "";
						//String authorized = "";//是否授权客户
						if("0".equals(type)){//AE或者ifa
							String sql = "select distributor_id from ifa_distributor where is_valid='1' and ae_code='"+orderVO.getCreatorNo()+"'";
							List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
							if (null!=list&&!list.isEmpty()){
								Map map = (HashMap)list.get(0);
								distributorId = map.get("distributor_id").toString();
							}
						}else if("1".equals(type)){//investor
							String sql = "select distributor_id,authorized from investor_account where account_no='"+orderVO.getCreatorNo()+"'";
							List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
							if (null!=list&&!list.isEmpty()){
								Map map = (HashMap)list.get(0);
								distributorId = map.get("distributor_id").toString();
								//authorized = map.get("authorized").toString();
							}
						}
						
						//3.调用接口校验会话
						String sendMsg = "ITS|verify|0|"+sessionId+"|13|"+orderVO.getCreatorNo()+"@"+this.getRemoteHost(request)+"|";
						Socket socket = getSock(distributorId,"its");
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintWriter out = new PrintWriter(socket.getOutputStream());
						out.println(sendMsg);//将字符串输出到Server
						out.flush();//刷新输出流,使Server马上收到该字符串
						String resultMsg = in.readLine();
						
						//4.保存登录处理结果
						MemberAccountSso accountSso = new MemberAccountSso();
						String resultFlag = "";
						if("error".startsWith(resultMsg)){
							this.saveHandleError("session",resultMsg);
							resultFlag = "0";
						}else{
							accountSso = this.saveSessionMsg("login",resultMsg);
							if(accountSso != null){
								resultFlag = "1";
							}else{
								resultFlag = "0";
							}
						}
						
						if("1".startsWith(resultFlag)){//会话校验成功
							//2.撤单
							sendMsg = "ORD|cancel|6|"+orderVO.getOmsOrderNO()+"|13|"+orderVO.getAeCode()+"|74|"+orderVO.getOrderNO()+"|191|"+tradeId+"|510|"+orderVO.getTradingPassword()+"|400|I|";
							out.println(sendMsg);
							out.flush();
							//resultMsg = in.readLine();
							socket.close();//断开连接
							
							//因会返回所有效的image,所以放在统一的消息监听中进行消息
							/*
							String orderHistoryId = "";
							String waitHandleMsg = resultMsg;
							if(waitHandleMsg.startsWith("ORD|cancel")){//撤单成功
								String tmpMsg = waitHandleMsg;
								tmpMsg = tmpMsg.substring(tmpMsg.indexOf("|")+1);//去掉ORD|
								tmpMsg = tmpMsg.substring(tmpMsg.indexOf("|")+1);//cancel|
								map = StrUtils.stringToMap(tmpMsg);
								Iterator iter = map.entrySet().iterator();
								String orderNumber = "";
								String orderStatus = "";
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									String key = (String)entry.getKey();
									String val = (String)entry.getValue();
									if("6".equals(key)){
										orderNumber = val;
									}else if("74".equals(key)){
										orderHistoryId = val;
									}else if("5".equals(key)){
										orderStatus = val;
									}
								}				
								if(!"".equals(orderHistoryId)){
									String hql="update OrderHistory set itsOrderNumber='"+orderNumber+"',status='"+orderStatus+"'  where id='"+orderHistoryId+"' ";
									baseDao.updateHql(hql, null);
								}
							}
							*/
							
							//保存调用日志
							WebInterfaceLog log = new WebInterfaceLog();
							log.setInvokerType("F");
							log.setBusKey(orderVO.getAccountNo());
							log.setResultFlag(resultFlag);
							log.setModuleType(OmsConstants.ITS_LOG);
							log.setMethod("deleteOrder");
							log.setContent("结束:撤单成功");
							webInterfaceLogService.saveOrUpdate(log);
							return true;
						}else{
							//断开连接
							socket.close();
							
							//保存调用日志
							WebInterfaceLog log = new WebInterfaceLog();
							log.setInvokerType("F");
							log.setBusKey(orderVO.getAccountNo());
							log.setResultFlag(resultFlag);
							log.setModuleType(OmsConstants.ITS_LOG);
							log.setMethod("deleteOrder");
							log.setContent("结束:撤单失败,会话过期");
							webInterfaceLogService.saveOrUpdate(log);
							return false;
						}
					//}
				}	
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
//==================================== 旧代码备用  ===========================================================	

	/**
	 * 获得member的所有失效帐户列表
	 * @param memberId
	 * @return 失效帐户列表  空表示所有帐户都有效
	 */
/*	public AccountValidVO saveCheckExistValid(HttpServletRequest request,String memberId){
		MemberBase memberBase = (MemberBase)this.baseDao.get(MemberBase.class, memberId);
		if(memberBase != null){
			Integer memberType = memberBase.getMemberType();
			String alllAccountNo = "";//所有帐户  多个之间用,分隔
			String effectiveAccountNo = "";//有效帐户列表 多个之间用,分隔
			String validAccountNo = "";//失败帐户 多个之间用,分隔
			if(memberType == 1){//investor
				String hql="from InvestorAccount r where r.isValid='1' and r.openStatus='3' and r.member.id='"+memberBase.getId()+"'";
				List<InvestorAccount> list = baseDao.find(hql,null, false);
				if (null!=list&&!list.isEmpty()) {
					for(InvestorAccount ia:list){
						if("".equals(alllAccountNo)){
							alllAccountNo = ia.getAccountNo();
						}else{
							alllAccountNo += ","+ia.getAccountNo();
						}
					}
				}
			}else if(memberType == 2){//ifa
				String hql="from IfaDistributor r where r.isValid = '1' and r.ifa.member.id='"+memberBase.getId()+"'";
				List<IfaDistributor> list = baseDao.find(hql,null, false);
				if (null!=list&&!list.isEmpty()) {
					for(IfaDistributor ifaDis:list){
						if("".equals(alllAccountNo)){
							alllAccountNo = ifaDis.getAeCode();
						}else{
							alllAccountNo += ","+ifaDis.getAeCode();
						}
					}
				}
			}
			if(!"".equals(alllAccountNo)){
				String hql="from MemberAccountSso r where r.pinCheck='1' and r.verifyFlag='1' and r.accountCode in("+this.getNewStrId(alllAccountNo)+")";
				List<MemberAccountSso> list = baseDao.find(hql,null, false);
				for(MemberAccountSso sso:list){
					if("".equals(alllAccountNo)){
						effectiveAccountNo = sso.getAccountCode();
					}else{
						effectiveAccountNo += ","+sso.getAccountCode();
					}
				}
				if(!"".equals(effectiveAccountNo)){
					String tmpAllAccountNo = ","+alllAccountNo+",";
					String[] arr = effectiveAccountNo.split(",");
					for (String id : arr) {
						if(tmpAllAccountNo.indexOf(","+id+",") == -1){
							if("".equals(validAccountNo)){
								validAccountNo = id;
							}else{
								validAccountNo += ","+id;
							}
						}
					}
				}else{
					validAccountNo = alllAccountNo;
				}
				AccountValidVO vo = new AccountValidVO();
				vo.setAlllAccountNo(alllAccountNo);
				vo.setEffectiveAccountNo(effectiveAccountNo);
				vo.setValidAccountNo(validAccountNo);
				return vo;
			}
		}
		return null;
	}*/
	
/*	
	*//**
	 * 查询指定会员的帐户资产和持仓
	 * @param fromMemberId 发起人memberId 
	 * @param toMemberId   目标人memberId 当为ifa时为要查询的客户  当为investor时fromMemberId=toMemberId
	 * @return 
	 *//*
	public boolean saveAccountPosition(HttpServletRequest request,String fromMemberId,String toMemberId){
		if(fromMemberId.equals(toMemberId)){//investor
			//客户查自己的持仓,只要其对应的ifa的sessdion id存在,就可以查
			AccountValidVO validVO  = this.saveCheckExistValid(request,fromMemberId);
			if(validVO != null && !"".equals(validVO.getAlllAccountNo())){
				String[] acArr = (validVO.getAlllAccountNo()).split(",");
				for (String acCode : acArr) {
					this.savePosition(request,"1",acCode);
				}
			}
			
			AccountValidVO validVO  = this.saveCheckExistValid(request,fromMemberId);
			if(validVO != null && !"".equals(validVO.getEffectiveAccountNo())){
				String[] acArr = (validVO.getEffectiveAccountNo()).split(",");
				for (String acCode : acArr) {
					this.savePosition(request,"1",acCode);
				}
			}
			
		}else{//ifa
			AccountValidVO validVO  = this.saveCheckExistValid(request,fromMemberId);
			if(validVO != null && !"".equals(validVO.getEffectiveAccountNo())){
				String[] aeArr = (validVO.getEffectiveAccountNo()).split(",");
				for (String aeCode : aeArr) {
					this.savePosition(request,"0",aeCode);
				}
			}
		}
		//保存调用日志
		WebInterfaceLog log = new WebInterfaceLog();
		log.setInvokerType("F");
		log.setBusKey(fromMemberId);
		log.setResultFlag("1");
		log.setModuleType(OmsConstants.ITS_LOG);
		log.setMethod("saveAccountPosition");
		log.setContent("fromMemberId:"+fromMemberId+" toMemberId:"+toMemberId);
		webInterfaceLogService.saveOrUpdate(log);
		return true;
	}
	
	@Autowired
	private ITraderReceiveSession iTraderReceiveSession;
	@Autowired
	private ITraderReceiveAccount iTraderReceiveAccount;
	@Autowired
	private ITraderReceivePosition iTraderReceivePosition;
	*//**
	 * 查询持仓
	 * @param type 0是AE或者ifa登录 1是investor登录
	 * @return 
	 *//*
	private void savePosition(HttpServletRequest request,String type,String acCode){
		if("1".equals(type)){//investor
			String sql = "select s.session_id from investor_account a,ifa_distributor d,member_account_sso s where a.ifa_id=d.ifa_id and d.ae_code=s.account_code and a.account_no='"+acCode+"'";
			List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
			String sessionId = "";
			if (null!=list&&!list.isEmpty()){
				Map map = (HashMap)list.get(0);
				sessionId = map.get("session_id").toString();
			}
			SocketClientUtil ssmClient = this.initSocketClient("its");
			if(!"".equals(sessionId)){//因为ifa的seesion不会过期,所以可以拿investor对应的ifa的session去同步持仓
				//1会话校验
				String sendMsg = "ITS|verify|0|"+sessionId+"|13|"+acCode+"@"+this.getRemoteHost(request)+"|";
				iTraderReceiveSession.setSendMsg(sendMsg);
				ssmClient.addServiceMap(OmsConstants.ITS_SESSION,iTraderReceiveSession);
				ssmClient.sendMsg(OmsConstants.ITS_SESSION,null);				
				
				//2直接同步持仓
				//查询持仓前先清空原来记录
				sendMsg = "ITS|position|10|"+acCode+"|";
				iTraderReceivePosition.setSendMsg(sendMsg);
				ssmClient.addServiceMap(OmsConstants.ITS_POSITION,iTraderReceivePosition);
				ssmClient.sendMsg(OmsConstants.ITS_POSITION,null);
				
				//3延迟1秒 不然只会执行一条指令,不知为何?
				try {
				    Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//4会话校验完成直接同步资产
				sendMsg = "ITS|queryaccount|10|"+acCode+"|";
				iTraderReceiveAccount.setSendMsg(sendMsg);
				ssmClient.addServiceMap(OmsConstants.ITS_ACCOUNT,iTraderReceiveAccount);
				ssmClient.sendMsg(OmsConstants.ITS_ACCOUNT,null);
			}else{
				//1 先对目标acCode做verify
				String hql="from MemberAccountSso r where r.accountCode='"+acCode+"'";
				List<MemberAccountSso> listTmp = baseDao.find(hql,null, false);
				if (null!=listTmp&&!listTmp.isEmpty()) {
					MemberAccountSso ssoTmp =  listTmp.get(0);
					
					String sendMsg = "ITS|verify|0|"+ssoTmp.getSessionId()+"|13|"+acCode+"@"+this.getRemoteHost(request)+"|";
					iTraderReceiveSession.setSendMsg(sendMsg);
					ssmClient.addServiceMap(OmsConstants.ITS_SESSION,iTraderReceiveSession);
					ssmClient.sendMsg(OmsConstants.ITS_SESSION,null);
					
					//2会话校验完成直接同步持仓
					sendMsg = "ITS|position|10|"+acCode+"|";
					iTraderReceivePosition.setSendMsg(sendMsg);
					ssmClient.addServiceMap(OmsConstants.ITS_POSITION,iTraderReceivePosition);
					ssmClient.sendMsg(OmsConstants.ITS_POSITION,null);
					
					//3延迟1秒 不然只会执行一条指令,不知为何?
					try {
					    Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//4会话校验完成直接同步资产
					sendMsg = "ITS|queryaccount|10|"+acCode+"|";
					iTraderReceiveAccount.setSendMsg(sendMsg);
					ssmClient.addServiceMap(OmsConstants.ITS_ACCOUNT,iTraderReceiveAccount);
					ssmClient.sendMsg(OmsConstants.ITS_ACCOUNT,null);
				}
			}
		}else if("0".equals(type)){//ifa
			//1 先对目标aeCode做verify
			SocketClientUtil ssmClient = this.initSocketClient("its");
			String hql="from MemberAccountSso r where r.accountCode='"+acCode+"'";
			List<MemberAccountSso> listTmp = baseDao.find(hql,null, false);
			if (null!=listTmp&&!listTmp.isEmpty()) {
				MemberAccountSso ssoTmp =  listTmp.get(0);
				//2 找到这个ifa关联的distributor下的investor做持仓同步
				hql="from IfaDistributor r where r.isValid = '1' and r.aeCode='"+acCode+"'";
				List<IfaDistributor> listIfa = baseDao.find(hql,null, false);
				IfaDistributor ifaDis = listIfa.get(0);
				hql="from InvestorAccount r where r.isValid='1' and r.openStatus='3' and r.distributor.id='"+ifaDis.getDistributor().getId()+"' and r.ifa.id='"+ifaDis.getIfa().getId()+"'";
				List<InvestorAccount> list = baseDao.find(hql,null, false);
				if (null!=list&&!list.isEmpty()) {
					
					//先做会话校验
					String sendMsg = "ITS|verify|0|"+ssoTmp.getSessionId()+"|13|"+acCode+"@"+this.getRemoteHost(request)+"|";
					iTraderReceiveSession.setSendMsg(sendMsg);
					ssmClient.addServiceMap(OmsConstants.ITS_SESSION,iTraderReceiveSession);
					ssmClient.sendMsg(OmsConstants.ITS_SESSION,null);
					
					//延迟1秒 不然只会执行一条指令,不知为何?
					try {
					    Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					for(InvestorAccount ia:list){
						//2.1会话校验完成直接同步持仓
						
						ssmClient = this.initSocketClient("its");
						sendMsg = "ITS|position|10|"+ia.getAccountNo()+"|";
						iTraderReceivePosition.setSendMsg(sendMsg);
						ssmClient.addServiceMap(OmsConstants.ITS_POSITION,iTraderReceivePosition);
						ssmClient.sendMsg(OmsConstants.ITS_POSITION,null);
						
						//延迟1秒 不然只会执行一条指令,不知为何?
						try {
						    Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						//2.2会话校验完成直接同步资产
						ssmClient = this.initSocketClient("its");
						sendMsg = "ITS|queryaccount|10|"+ia.getAccountNo()+"|";
						iTraderReceiveAccount.setSendMsg(sendMsg);
						ssmClient.addServiceMap(OmsConstants.ITS_ACCOUNT,iTraderReceiveAccount);
						ssmClient.sendMsg(OmsConstants.ITS_ACCOUNT,null);
					}
				}
			}
		}
		
		//延迟1秒
		try {
		    Thread.sleep(OmsConstants.TRADE_SLEEP_SEC);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}*/
	
	//@Autowired
	//private ITraderReceiveOrder iTraderReceiveOrder;
	
	/**
	 * 订算订单的相关费用.适合用股票,不适用于基金
	 * @param orderVO 订单
	 * @return true 成功 false失败
	 */
	/*
	public boolean saveCalOrder(HttpServletRequest request,OmsAddOrderVO orderVO){
		try {
			if(orderVO.getCreatorNo() != null && !"".equals(orderVO.getCreatorNo())){
				String sql = "select id,session_id,trade_id from member_account_sso  where account_code='"+orderVO.getCreatorNo()+"'";
				List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
				if (null!=list&&!list.isEmpty()){
					Map map = (HashMap)list.get(0);
					String sessionId = map.get("session_id").toString();
					
					//1.做会话校验,以便占用此socket
					Socket socket = getSock(orderVO.getDistributorId(),"its");
					boolean verifyFlag = saveVerify(request,socket,orderVO.getCreatorNo(),sessionId);
					if(verifyFlag == false){
						return false;
					}
					
					//订单计算
					String msg = "";
					if(orderVO.getCurrencyCode() == null || "".equals(orderVO.getCurrencyCode())){//默认是港币
						orderVO.setCurrencyCode("HKD");
					}
					if("2".equals(orderVO.getOrderType())){//股票
						msg = "ORD|calculate|0|"+orderVO.getSymbolCode()+"|11|"+orderVO.getTranType()+"|3|"+orderVO.getLimitedPrice()+"|4|"+orderVO.getQuantity()+"|10|"+orderVO.getAccountNo()+"|13|"+orderVO.getAeCode()+"|400|I|23|"+orderVO.getCurrencyCode()+"|";
					}else{//默认是基金
						if("0".equals(orderVO.getTranType()) && orderVO.getLimitedPrice() > 0){//基金买入 数量固定为Quantity=1 LimitedPrice为申购金额
							msg = "ORD|calculate|0|"+orderVO.getSymbolCode()+"|11|"+orderVO.getTranType()+"|3|"+orderVO.getLimitedPrice()+"|4|1|10|"+orderVO.getAccountNo()+"|13|"+orderVO.getAeCode()+"|400|I|23|"+orderVO.getCurrencyCode()+"|";
						}else if("1".equals(orderVO.getTranType())){//基金卖出  Quantity=赎回份额  LimitedPrice为0
							msg = "ORD|calculate|0|"+orderVO.getSymbolCode()+"|11|"+orderVO.getTranType()+"|3|"+orderVO.getLimitedPrice()+"|4|"+orderVO.getQuantity()+"|10|"+orderVO.getAccountNo()+"|13|"+orderVO.getAeCode()+"|400|I|23|"+orderVO.getCurrencyCode()+"|";
						}
					}
					String resultMsg = "";
					if(!"".equals(msg)){
						msg = msg+"510|"+orderVO.getTradingPassword()+"|400|I|";
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintWriter out = new PrintWriter(socket.getOutputStream());
						out.println(msg);//将字符串输出到Server
						out.flush();//刷新输出流,使Server马上收到该字符串
						resultMsg = in.readLine();
						socket.close();
					}
					/*
					image|CALCD_00700001|0|TEM0043|10|833890001|
					100|0.000000000|
					11|0|1109|1|1118|working=0.00, allSent=0,addPending=0.00,cancelled=0,posChange=0.00,posApplied=0.00,adjustQuantity=0,needClose=0.00,posShorted=0.00,needApproval=0,quantityChange=0.00,priceChange=0.00,price1Change=0.00,
					netProceed=-2000.00|122|0|13|00700001|20|FUND|22|10|23|HKD|
					25|Sufficient trading balance/limit|
					3|2000.000000000|34|0|35|0|39|2|4|1|400|I|
					401|0.000000000|402|0.000000000|403|0.000000000|404|0.000000000|405|0.000000000|406|0.000000000|421|0.000000000|422|0.000000000|423|0.000000000|458|0.000000000|459|0.000000000
					|461|0|464|0|487|netFee=0,402=0|492|2097152|502|TRADE|503|0.000000000|95|-2000.000000000|
					
					//保存调用日志
					WebInterfaceLog log = new WebInterfaceLog();
					log.setInvokerType("F");
					log.setBusKey(orderVO.getAccountNo());
					log.setResultFlag("1");
					log.setModuleType(OmsConstants.ITS_LOG);
					log.setMethod("saveCalOrder");
					log.setContent(resultMsg);
					webInterfaceLogService.saveOrUpdate(log);
					return true;
				}	
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}*/
	

/*	*//**
	 * 查询订单的状态
	 * @param orderVO
	 * @return true 成功 false失败
	 *//*
	public boolean saveQueryOrderBook(HttpServletRequest request,OmsQueryOrderVO orderVO){
		if(orderVO.getCreatorNo() != null && !"".equals(orderVO.getCreatorNo())){
			String sql = "select id,session_id,trade_id from member_account_sso  where account_code='"+orderVO.getCreatorNo()+"'";
			List list = this.springJdbcQueryManager.springJdbcQueryForList(sql);
			if (null!=list&&!list.isEmpty()){
				Map map = (HashMap)list.get(0);
				String sessionId = map.get("session_id").toString();
				String tradeId = map.get("trade_id").toString();
				
				//1.做会话校验,以便占用此socket
				SocketClientUtil ssmClient = this.initSocketClient("its");
				String sendMsg = "ITS|verify|0|"+sessionId+"|13|"+orderVO.getCreatorNo()+"@"+this.getRemoteHost(request)+"|";
				iTraderReceiveSession.setSendMsg(sendMsg);
				ssmClient.addServiceMap(OmsConstants.ITS_SESSION,iTraderReceiveSession);
				ssmClient.sendMsg(OmsConstants.ITS_SESSION,null);
				
				//延迟1秒 不然只会执行一条指令,不知为何?
				try {
				    Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//2.查单
				sendMsg = "image|ORD|";
				if(orderVO.getOmsOrderNO() != null && !"".equals(orderVO.getOmsOrderNO())){
					sendMsg += "6|"+orderVO.getOmsOrderNO()+"|";
				}
				if(orderVO.getAccountNo() != null && !"".equals(orderVO.getAccountNo())){
					sendMsg += "10|"+orderVO.getAccountNo()+"|";
				}
				if(orderVO.getXfaOrderNO() != null && !"".equals(orderVO.getXfaOrderNO())){
					sendMsg += "25|"+orderVO.getXfaOrderNO()+"|";
				}
				iTraderReceiveOrder.setSendMsg(sendMsg);
				ssmClient.addServiceMap(OmsConstants.ITS_ORDER,iTraderReceiveOrder);
				ssmClient.sendMsg(OmsConstants.ITS_ORDER,null);
				
				//保存调用日志
				WebInterfaceLog log = new WebInterfaceLog();
				log.setInvokerType("F");
				log.setBusKey(orderVO.getAccountNo());
				log.setResultFlag("1");
				log.setModuleType(OmsConstants.ITS_LOG);
				log.setMethod("saveQueryOrderBook");
				log.setContent(sendMsg);
				webInterfaceLogService.saveOrUpdate(log);
				return true;
			}	
		}
		return false;
	}*/
	
/*	@Autowired
	private ITraderReceiveOrderHistory iTraderReceiveOrderHistory;
	*//**
	 * 获得历史订单,按天查
	 * @param fromMemberId 发起人memberId 
	 * @param toMemberId   目标人memberId 当为ifa时为要查询的客户  当为investor时fromMemberId=toMemberId
	 * @param queryDayNum 查询天数
	 * @return 
	 *//*
	public boolean saveOrderHistoryByDay(HttpServletRequest request,String fromMemberId,String toMemberId,int queryDayNum){
		if(fromMemberId.equals(toMemberId)){//investor
			AccountValidVO validVO  = this.saveCheckExistValid(request,fromMemberId);
			if(validVO != null && !"".equals(validVO.getEffectiveAccountNo())){
				String[] acArr = (validVO.getEffectiveAccountNo()).split(",");
				SocketClientUtil ssmClient = this.initSocketClient("its");
				for (String acCode : acArr) {
					String sql = "select id,session_id,trade_id from member_account_sso  where account_code='"+acCode+"'";
					List listSSO = this.springJdbcQueryManager.springJdbcQueryForList(sql);
					if (null!=listSSO&&!listSSO.isEmpty()){
						Map map = (HashMap)listSSO.get(0);
						String sessionId = map.get("session_id").toString();
						//1.做会话校验,以便占用此socket
						String sendMsg = "ITS|verify|0|"+sessionId+"|13|"+acCode+"@"+this.getRemoteHost(request)+"|";
						iTraderReceiveSession.setSendMsg(sendMsg);
						ssmClient.addServiceMap(OmsConstants.ITS_SESSION,iTraderReceiveSession);
						ssmClient.sendMsg(OmsConstants.ITS_SESSION,null);
						
						//延迟1秒 不然只会执行一条指令,不知为何?
						try {
						    Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						sendMsg = "ITS|history|4|"+queryDayNum+"|10|"+acCode+"|";
						iTraderReceiveOrderHistory.setSendMsg(sendMsg);
						ssmClient.addServiceMap(OmsConstants.ITS_ORDER_HIS,iTraderReceiveOrderHistory);
						ssmClient.sendMsg(OmsConstants.ITS_ORDER_HIS,null);
					}
				}
				//延期2秒
				try {
				    Thread.sleep(OmsConstants.TRADE_SLEEP_SEC);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{//ifa
			AccountValidVO validVO  = this.saveCheckExistValid(request,fromMemberId);
			if(validVO != null && !"".equals(validVO.getEffectiveAccountNo())){
				String[] aeArr = (validVO.getEffectiveAccountNo()).split(",");
				SocketClientUtil ssmClient = this.initSocketClient("its");
				for (String aeCode : aeArr) {
					String sql = "select id,session_id,trade_id from member_account_sso  where account_code='"+aeCode+"'";
					List listSSO = this.springJdbcQueryManager.springJdbcQueryForList(sql);
					if (null!=listSSO&&!listSSO.isEmpty()){
						Map map = (HashMap)listSSO.get(0);
						String sessionId = map.get("session_id").toString();
						
						//1.做会话校验,以便占用此socket
						String sendMsg = "ITS|verify|0|"+sessionId+"|13|"+aeCode+"@"+this.getRemoteHost(request)+"|";
						iTraderReceiveSession.setSendMsg(sendMsg);
						ssmClient.addServiceMap(OmsConstants.ITS_SESSION,iTraderReceiveSession);
						ssmClient.sendMsg(OmsConstants.ITS_SESSION,null);
						
						//2 找到这个ifa关联的distributor下的investor做持仓同步
						String hql="from IfaDistributor r where r.isValid = '1' and r.aeCode='"+aeCode+"'";
						List<IfaDistributor> listIfa = baseDao.find(hql,null, false);
						IfaDistributor ifaDis = listIfa.get(0);
						hql="from InvestorAccount r where r.isValid='1' and r.openStatus='3' and r.distributor.id='"+ifaDis.getDistributor().getId()+"' and r.ifa.id='"+ifaDis.getIfa().getId()+"'";
						List<InvestorAccount> list = baseDao.find(hql,null, false);
						if (null!=list&&!list.isEmpty()) {
							for(InvestorAccount ia:list){
								
								//延迟1秒 不然只会执行一条指令,不知为何?
								try {
								    Thread.sleep(1000);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								sendMsg = "ITS|history|4|"+queryDayNum+"|10|"+ia.getAccountNo()+"|";
								iTraderReceiveOrderHistory.setSendMsg(sendMsg);
								ssmClient.addServiceMap(OmsConstants.ITS_ORDER_HIS,iTraderReceiveOrderHistory);
								ssmClient.sendMsg(OmsConstants.ITS_ORDER_HIS,null);
								
							}
						}
					}
				}
				//延期2秒
				try {
				    Thread.sleep(OmsConstants.TRADE_SLEEP_SEC);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		//保存调用日志
		WebInterfaceLog log = new WebInterfaceLog();
		log.setInvokerType("F");
		log.setBusKey(fromMemberId);
		log.setResultFlag("1");
		log.setModuleType(OmsConstants.ITS_LOG);
		log.setMethod("saveOrderHistoryByDay");
		log.setContent("fromMemberId:"+fromMemberId+" toMemberId:"+toMemberId+" queryDayNum:"+queryDayNum);
		webInterfaceLogService.saveOrUpdate(log);
		
		return true;
	}
	
	*//**
	 * 获得历史订单,按日期查
	 * @param fromMemberId 发起人memberId 
	 * @param toMemberId   目标人memberId 当为ifa时为要查询的客户  当为investor时fromMemberId=toMemberId
	 * @param startDate 开始日期 格式yyyy/MM/dd
	 * @param endDate 结束日期 格式yyyy/MM/dd
	 * @return 
	 *//*
	public boolean saveOrderHistoryByDate(HttpServletRequest request,String fromMemberId,String toMemberId,String startDate,String endDate){
		if(fromMemberId.equals(toMemberId)){//investor
			AccountValidVO validVO  = this.saveCheckExistValid(request,fromMemberId);
			if(validVO != null && !"".equals(validVO.getEffectiveAccountNo())){
				String[] acArr = (validVO.getEffectiveAccountNo()).split(",");
				SocketClientUtil ssmClient = this.initSocketClient("its");
				for (String acCode : acArr) {
					String sql = "select id,session_id,trade_id from member_account_sso  where account_code='"+acCode+"'";
					List listSSO = this.springJdbcQueryManager.springJdbcQueryForList(sql);
					if (null!=listSSO&&!listSSO.isEmpty()){
						Map map = (HashMap)listSSO.get(0);
						String sessionId = map.get("session_id").toString();
						//1.做会话校验,以便占用此socket
						String sendMsg = "ITS|verify|0|"+sessionId+"|13|"+acCode+"@"+this.getRemoteHost(request)+"|";
						iTraderReceiveSession.setSendMsg(sendMsg);
						ssmClient.addServiceMap(OmsConstants.ITS_SESSION,iTraderReceiveSession);
						ssmClient.sendMsg(OmsConstants.ITS_SESSION,null);
						
						//延迟1秒 不然只会执行一条指令,不知为何?
						try {
						    Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						startDate = startDate.replace("-", "/");
						endDate = endDate.replace("-", "/");
						sendMsg = "ITS|history|10|"+acCode+"|1|"+startDate+"|2|"+endDate+"|";
						ssmClient.addServiceMap(OmsConstants.ITS_ORDER_HIS,iTraderReceiveOrderHistory);
						ssmClient.sendMsg(OmsConstants.ITS_ORDER_HIS,null);
					}
				}
				//延期2秒
				try {
				    Thread.sleep(OmsConstants.TRADE_SLEEP_SEC);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{//ifa
			AccountValidVO validVO  = this.saveCheckExistValid(request,fromMemberId);
			if(validVO != null && !"".equals(validVO.getEffectiveAccountNo())){
				String[] aeArr = (validVO.getEffectiveAccountNo()).split(",");
				SocketClientUtil ssmClient = this.initSocketClient("its");
				for (String aeCode : aeArr) {
					String sql = "select id,session_id,trade_id from member_account_sso  where account_code='"+aeCode+"'";
					List listSSO = this.springJdbcQueryManager.springJdbcQueryForList(sql);
					if (null!=listSSO&&!listSSO.isEmpty()){
						Map map = (HashMap)listSSO.get(0);
						String sessionId = map.get("session_id").toString();
						
						//1.做会话校验,以便占用此socket
						String sendMsg = "ITS|verify|0|"+sessionId+"|13|"+aeCode+"@"+this.getRemoteHost(request)+"|";
						iTraderReceiveSession.setSendMsg(sendMsg);
						ssmClient.addServiceMap(OmsConstants.ITS_SESSION,iTraderReceiveSession);
						ssmClient.sendMsg(OmsConstants.ITS_SESSION,null);
						
						//2 找到这个ifa关联的distributor下的investor做持仓同步
						String hql="from IfaDistributor r where r.isValid = '1' and r.aeCode='"+aeCode+"'";
						List<IfaDistributor> listIfa = baseDao.find(hql,null, false);
						IfaDistributor ifaDis = listIfa.get(0);
						hql="from InvestorAccount r where r.isValid='1' and r.openStatus='3' and r.distributor.id='"+ifaDis.getDistributor().getId()+"' and r.ifa.id='"+ifaDis.getIfa().getId()+"'";
						List<InvestorAccount> list = baseDao.find(hql,null, false);
						if (null!=list&&!list.isEmpty()) {
							for(InvestorAccount ia:list){
								
								//延迟1秒 不然只会执行一条指令,不知为何?
								try {
								    Thread.sleep(1000);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								startDate = startDate.replace("-", "/");
								endDate = endDate.replace("-", "/");
								sendMsg = "ITS|history|10|"+aeCode+"|1|"+startDate+"|2|"+endDate+"|";
								iTraderReceiveOrderHistory.setSendMsg(sendMsg);
								ssmClient.addServiceMap(OmsConstants.ITS_ORDER_HIS,iTraderReceiveOrderHistory);
								ssmClient.sendMsg(OmsConstants.ITS_ORDER_HIS,null);
								
							}
						}
					}
				}
				//延期2秒
				try {
				    Thread.sleep(OmsConstants.TRADE_SLEEP_SEC);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		//保存调用日志
		WebInterfaceLog log = new WebInterfaceLog();
		log.setInvokerType("F");
		log.setBusKey(fromMemberId);
		log.setResultFlag("1");
		log.setModuleType(OmsConstants.ITS_LOG);
		log.setMethod("saveOrderHistoryByDate");
		log.setContent("fromMemberId:"+fromMemberId+" toMemberId:"+toMemberId+" startDate:"+startDate+" endDate:"+endDate);
		webInterfaceLogService.saveOrUpdate(log);
		
		return true;
	}
	
	*//**
	 * 获得订单成交情况,按日期查
	 * @param fromMemberId 发起人memberId 
	 * @param toMemberId   目标人memberId 当为ifa时为要查询的客户  当为investor时fromMemberId=toMemberId
	 * @return 
	 *//*
	public boolean saveOrderTrade(HttpServletRequest request,String fromMemberId,String toMemberId){
		if(fromMemberId.equals(toMemberId)){//investor
			AccountValidVO validVO  = this.saveCheckExistValid(request,fromMemberId);
			if(validVO != null && !"".equals(validVO.getEffectiveAccountNo())){
				String[] acArr = (validVO.getEffectiveAccountNo()).split(",");
				SocketClientUtil ssmClient = this.initSocketClient("its");
				for (String acCode : acArr) {
					String sql = "select id,session_id,trade_id from member_account_sso  where account_code='"+acCode+"'";
					List listSSO = this.springJdbcQueryManager.springJdbcQueryForList(sql);
					if (null!=listSSO&&!listSSO.isEmpty()){
						Map map = (HashMap)listSSO.get(0);
						String sessionId = map.get("session_id").toString();
						//1.做会话校验,以便占用此socket
						String sendMsg = "ITS|verify|0|"+sessionId+"|13|"+acCode+"@"+this.getRemoteHost(request)+"|";
						iTraderReceiveSession.setSendMsg(sendMsg);
						ssmClient.addServiceMap(OmsConstants.ITS_SESSION,iTraderReceiveSession);
						ssmClient.sendMsg(OmsConstants.ITS_SESSION,null);
						
						//延迟1秒 不然只会执行一条指令,不知为何?
						try {
						    Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						sendMsg = "ITS|querytrade|10|"+acCode+"|";
						ssmClient.addServiceMap(OmsConstants.ITS_ORDER_HIS,iTraderReceiveOrderHistory);
						ssmClient.sendMsg(OmsConstants.ITS_ORDER_HIS,null);
					}
				}
				//延期2秒
				try {
				    Thread.sleep(OmsConstants.TRADE_SLEEP_SEC);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{//ifa
			AccountValidVO validVO  = this.saveCheckExistValid(request,fromMemberId);
			if(validVO != null && !"".equals(validVO.getEffectiveAccountNo())){
				String[] aeArr = (validVO.getEffectiveAccountNo()).split(",");
				SocketClientUtil ssmClient = this.initSocketClient("its");
				for (String aeCode : aeArr) {
					String sql = "select id,session_id,trade_id from member_account_sso  where account_code='"+aeCode+"'";
					List listSSO = this.springJdbcQueryManager.springJdbcQueryForList(sql);
					if (null!=listSSO&&!listSSO.isEmpty()){
						Map map = (HashMap)listSSO.get(0);
						String sessionId = map.get("session_id").toString();
						
						//1.做会话校验,以便占用此socket
						String sendMsg = "ITS|verify|0|"+sessionId+"|13|"+aeCode+"@"+this.getRemoteHost(request)+"|";
						iTraderReceiveSession.setSendMsg(sendMsg);
						ssmClient.addServiceMap(OmsConstants.ITS_SESSION,iTraderReceiveSession);
						ssmClient.sendMsg(OmsConstants.ITS_SESSION,null);
						
						//2 找到这个ifa关联的distributor下的investor做持仓同步
						String hql="from IfaDistributor r where r.isValid = '1' and r.aeCode='"+aeCode+"'";
						List<IfaDistributor> listIfa = baseDao.find(hql,null, false);
						IfaDistributor ifaDis = listIfa.get(0);
						hql="from InvestorAccount r where r.isValid='1' and r.openStatus='3' and r.distributor.id='"+ifaDis.getDistributor().getId()+"' and r.ifa.id='"+ifaDis.getIfa().getId()+"'";
						List<InvestorAccount> list = baseDao.find(hql,null, false);
						if (null!=list&&!list.isEmpty()) {
							for(InvestorAccount ia:list){
								
								//延迟1秒 不然只会执行一条指令,不知为何?
								try {
								    Thread.sleep(1000);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								sendMsg = "ITS|querytrade|10|"+aeCode+"|";
								iTraderReceiveOrderHistory.setSendMsg(sendMsg);
								ssmClient.addServiceMap(OmsConstants.ITS_ORDER_HIS,iTraderReceiveOrderHistory);
								ssmClient.sendMsg(OmsConstants.ITS_ORDER_HIS,null);
								
							}
						}
					}
				}
				//延期2秒
				try {
				    Thread.sleep(OmsConstants.TRADE_SLEEP_SEC);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		//保存调用日志
		WebInterfaceLog log = new WebInterfaceLog();
		log.setInvokerType("F");
		log.setBusKey(fromMemberId);
		log.setResultFlag("1");
		log.setModuleType(OmsConstants.ITS_LOG);
		log.setMethod("saveOrderTrade");
		log.setContent("fromMemberId:"+fromMemberId+" toMemberId:"+toMemberId);
		webInterfaceLogService.saveOrUpdate(log);
		
		return true;
	}*/
}
