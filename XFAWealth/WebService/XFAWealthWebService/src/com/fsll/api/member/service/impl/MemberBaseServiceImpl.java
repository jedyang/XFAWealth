/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.api.member.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.api.member.service.MemberBaseService;
import com.fsll.api.member.vo.MemberSsoVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.security.PwdEncoder;
import com.fsll.common.util.DESUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.WSConstants;
import com.fsll.core.entity.SysSite;
import com.fsll.dao.entity.MemberAppToken;
import com.fsll.dao.entity.MemberBase;
import com.fsll.dao.entity.MemberCompany;
import com.fsll.dao.entity.MemberCorporate;
import com.fsll.dao.entity.MemberDistributor;
import com.fsll.dao.entity.MemberFi;
import com.fsll.dao.entity.MemberIfa;
import com.fsll.dao.entity.MemberIfafirm;
import com.fsll.dao.entity.MemberIndividual;
import com.fsll.dao.entity.MemberSso;
/***
 * 业务接口实现类：统一帐号管理
 * @author mjhuang
 * @date 2016-6-20
 */
@Service("appMemberBaseService")
public class MemberBaseServiceImpl extends BaseService implements MemberBaseService {
	@Autowired
	protected PwdEncoder pwdEncoder;//密码加密接口类
	
	/***************  登录sso相关方法　begin  **********************/
	/**
	 * sso登录验证
	 * 
	 * @param request 请求
	 * @param loginCode 登录帐号
	 * @param password 密码
	 * @param fromType 来源：web,ios,android
	 * @param appCode app编码
	 * @return
	 */
	public MemberSsoVO saveLoginAuth(HttpServletRequest request,String loginCode,String password,String fromType,String appCode) {
		SysSite sysSite = (SysSite)this.baseDao.get(SysSite.class, CommonConstants.MAIN_SITE_ID);
		
		MemberSsoVO ssoVO = new MemberSsoVO();
		Date curDate = new Date();//当前时间
		
		//1.获得用户信息
		List params = new ArrayList<Object>();
		String hql = "from MemberBase r where r.isValid='1' and (r.loginCode = ? or r.loginCode = ?) ";
		params.add(loginCode.toUpperCase());
		params.add(loginCode.toLowerCase());
		List<MemberBase> mList = this.baseDao.find(hql, params.toArray(), false);
		MemberBase memberBase = null;
		if(!mList.isEmpty()){
			memberBase = mList.get(0);
			//2.帐号已锁,但还没过有效期
			Long lockTime = null;
			if(memberBase.getLockDate() != null){
				lockTime = memberBase.getLockDate().getTime();
			}
			if("1".equals(memberBase.getLockStatus()) && (lockTime > curDate.getTime())){
				ssoVO.setRet(WSConstants.FAIL);
				ssoVO.setErrorCode(WSConstants.CODE1013);
				ssoVO.setErrorMsg(WSConstants.MSG1013);
				return ssoVO;
			}
			//3.是否已激活
			if(!"1".equals(memberBase.getStatus())){
				ssoVO.setRet(WSConstants.FAIL);
				ssoVO.setErrorCode(WSConstants.CODE1004);
				ssoVO.setErrorMsg(WSConstants.MSG1004);
				return ssoVO;
			}
			
			if(password.equals(memberBase.getPassword())){//密码正确
				//向数据库写sso的信息
				String dataSecrefKey = UUID.randomUUID().toString().replace("-", "");
				String tokenId = DESUtils.encrypt(dataSecrefKey,CommonConstants.SECRET_KEY);					
				MemberSso memberSso = new MemberSso();
				memberSso.setFromType(fromType);
				memberSso.setLoginCode(loginCode);
				memberSso.setSecureKey(dataSecrefKey);
				memberSso.setTokenId(tokenId);
				memberSso.setIp(this.getRemoteHost(request));
				
				String agent = request.getHeader("user-agent");  
				memberSso.setBrowser(agent);
				
				memberSso.setRemark("sso login");
				memberSso.setCreateTime(curDate);
				memberSso.setAppCode(appCode);
				memberSso.setExpireTime(DateUtil.getInternalDateByMin(curDate,sysSite.getTokenTimeOut()));
				memberSso.setSessionId(request.getSession().getId());
				
				//返回登录信息
				try{
					BeanUtils.copyProperties(ssoVO,memberBase);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ssoVO.setIconUrl(PageHelper.getUserHeadUrlForWS(ssoVO.getIconUrl(), ssoVO.getGender()));
				ssoVO.setSessionId(memberSso.getSessionId());
				ssoVO.setTokenId(tokenId);
				ssoVO.setTokenExpireTime(DateUtil.dateToDateString(memberSso.getExpireTime(), "yyyy-MM-dd HH:mm:ss"));
				ssoVO.setLastLoginTime(memberBase.getLoginTime());//last login time
				ssoVO.setRet(WSConstants.SUCCESS);
				ssoVO.setImAppKey(PropertyUtils.getConfPropertyValue("im_app_key"));
				if(ssoVO.getLangCode() == null || "".equals(ssoVO.getLangCode())){
					ssoVO.setLangCode(CommonConstants.DEF_LANG_CODE);
				}
				if(ssoVO.getDateFormat() == null || "".equals(ssoVO.getDateFormat())){
					ssoVO.setDateFormat(CommonConstants.FORMAT_DATE);
				}
				if(ssoVO.getDefCurrency() == null || "".equals(ssoVO.getDefCurrency())){
					ssoVO.setDefCurrency(CommonConstants.DEF_CURRENCY);
				}
				ssoVO.setDefCurrencyName(getParamConfigName(ssoVO.getLangCode(), ssoVO.getDefCurrency(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
				if(ssoVO.getDefDisplayColor() == null || "".equals(ssoVO.getDefDisplayColor())){
					if(ssoVO.getLangCode().equals(CommonConstants.LANG_CODE_EN)){
						ssoVO.setDefDisplayColor("1");
					}else{
						ssoVO.setDefDisplayColor("2");
					}
				}
				//分组信息获取和设置?????
				String groupCode = "";//也等于app中的aliasType
				ssoVO.setGroupCode(groupCode);
				
				//向帐号表更新登录信息
				memberBase.setLastLoginIp(this.getRemoteHost(request));
				memberBase.setLoginTime(curDate);
				if(memberBase.getLoginCount() != null){
					memberBase.setLoginCount(memberBase.getLoginCount()+1);
				}else{
					memberBase.setLoginCount(1);
				}
				
				//运营公司
				String hqlFirst = "from MemberCompany r where r.member.id = '"+memberBase.getId()+"' ";
				List<MemberCompany> listFirst = this.baseDao.find(hqlFirst,null, false);
				if(null!=listFirst && !listFirst.isEmpty()){
					MemberCompany objTmp = listFirst.get(0);
					ssoVO.setCompanyId(objTmp.getCompany().getId());
				}
				
				//清空登录次数与锁定状态/锁定日期
				memberBase.setFailCount(0);
				memberBase.setLockStatus("0");
				memberBase.setLockDate(null);
				
				//保存sso之前先删除原来的sso信息
				//String hqlDel = "delete from MemberSso r where r.loginCode='"+loginCode+"'";
				String hqlDel = "delete from MemberSso r where (r.ip='"+memberSso.getIp()+"' or r.loginCode='"+loginCode+"') ";
				if("ios".equals(fromType) || "android".equals(fromType)){
					hqlDel += " and r.fromType in ('ios','android') ";
				}else if("web".equals(fromType)){
					hqlDel += " and r.fromType = 'web' ";
				}
				//if("guest".equals(memberBase.getId())){//如果是游客,则需要再加ip这个删除条件
				//	hqlDel += " and r.ip='"+memberSso.getIp()+"'";
				//}
				ssoVO.setDevice(fromType);//返回登录设备
				if("ios".equals(fromType) || "android".equals(fromType)){//来源于手机端 
					if("guest".equals(memberBase.getId()) || null !=memberBase.getMemberType() ){
						//保存sso之前先删除原来的sso信息
						this.baseDao.updateHql(hqlDel,null);
						this.baseDao.create(memberSso);//保存sso
						this.baseDao.update(memberBase);//保存登录信息
						if(memberBase.getMemberType() == 1){//投资人或者游客
							if(11==memberBase.getSubMemberType()){//当为investor 个人时,加入member_individual.id
								String hqlTmp = "from MemberIndividual r where r.member.id = ? ";
								List paramsTmp = new ArrayList();
								paramsTmp.add(memberBase.getId());
								List<MemberIndividual> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
								if(null!=listTmp && !listTmp.isEmpty()){
									MemberIndividual objTmp = listTmp.get(0);
									ssoVO.setIndividualId(objTmp.getId());
									ssoVO.setGender(objTmp.getGender());
									String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
									if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
										enName +=" "+objTmp.getLastName();
									}
									String chiName = objTmp.getNameChn();
									if(chiName == null || "".equals(chiName)){
										chiName = enName;
									}
									if(ssoVO.getLangCode().equals(CommonConstants.LANG_CODE_EN)){
										if(!"".equals(enName) && !"".equals(chiName)){//同时有中文和英文
											ssoVO.setMemberName(enName+"("+chiName+")");
										}else if(!"".equals(enName) && "".equals(chiName)){//只有英文
											ssoVO.setMemberName(enName);
										}else if("".equals(enName) && !"".equals(chiName)){//只有中文
											ssoVO.setMemberName(chiName);
										}
									}else{// 中文版：显示中文名，如果没，则显示英文名
										if(!"".equals(chiName)){
											ssoVO.setMemberName(chiName);
										}else{
											ssoVO.setMemberName(enName);
										}
									}
									if(ssoVO.getMemberName() == null || "".equals(ssoVO.getMemberName())){
										if(ssoVO.getNickName() != null && !"".equals(ssoVO.getNickName())){
											ssoVO.setMemberName(ssoVO.getNickName());
										}else{
											ssoVO.setMemberName(ssoVO.getLoginCode());
										}
									}
								}
							}else if(12==memberBase.getSubMemberType()){//当为investor 企业时,加入member_corporate.id
								String hqlTmp = "from MemberCorporate r where r.member.id = ? ";
								List paramsTmp = new ArrayList();
								paramsTmp.add(memberBase.getId());
								List<MemberCorporate> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
								if(null!=listTmp && !listTmp.isEmpty()){
									MemberCorporate objTmp = listTmp.get(0);
									ssoVO.setCorporateId(objTmp.getId());
								}
							}else if(13==memberBase.getSubMemberType()){//当为investor fi时,加入member_fi.id
								String hqlTmp = "from MemberFi r where r.member.id = ? ";
								List paramsTmp = new ArrayList();
								paramsTmp.add(memberBase.getId());
								List<MemberFi> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
								if(null!=listTmp && !listTmp.isEmpty()){
									MemberFi objTmp = listTmp.get(0);
									ssoVO.setFiId(objTmp.getId());
								}
							}
						}else if(memberBase.getMemberType() == 2 && memberBase.getSubMemberType() != null && 21==memberBase.getSubMemberType()){//ifa
							String hqlTmp = "from MemberIfa r where r.member.id = ? ";
							List paramsTmp = new ArrayList();
							paramsTmp.add(memberBase.getId());
							List<MemberIfa> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
							if(null!=listTmp && !listTmp.isEmpty()){
								MemberIfa objTmp = listTmp.get(0);
								ssoVO.setIfaId(objTmp.getId());
								ssoVO.setGender(objTmp.getGender());
								String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
								if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
									enName +=" "+objTmp.getLastName();
								}
								String chiName = objTmp.getNameChn();
								if(chiName == null || "".equals(chiName)){
									chiName = enName;
								}
								if(ssoVO.getLangCode().equals(CommonConstants.LANG_CODE_EN)){
									if(!"".equals(enName) && !"".equals(chiName)){//同时有中文和英文
										ssoVO.setMemberName(enName+"("+chiName+")");
									}else if(!"".equals(enName) && "".equals(chiName)){//只有英文
										ssoVO.setMemberName(enName);
									}else if("".equals(enName) && !"".equals(chiName)){//只有中文
										ssoVO.setMemberName(chiName);
									}
								}else{// 中文版：显示中文名，如果没，则显示英文名
									if(!"".equals(chiName)){
										ssoVO.setMemberName(chiName);
									}else{
										ssoVO.setMemberName(enName);
									}
								}
								if(ssoVO.getMemberName() == null || "".equals(ssoVO.getMemberName())){
									if(ssoVO.getNickName() != null && !"".equals(ssoVO.getNickName())){
										ssoVO.setMemberName(ssoVO.getNickName());
									}else{
										ssoVO.setMemberName(ssoVO.getLoginCode());
									}
								}
							}
						}
						return ssoVO;
					}
				}else if("web".equals(fromType)){//来源于web
					//mjhuang 2016-11-11 前台与工作台为同一登录入口
					if("guest".equals(memberBase.getId()) || null !=memberBase.getMemberType() ){
						//保存sso之前先删除原来的sso信息
						this.baseDao.updateHql(hqlDel,null);
						this.baseDao.create(memberSso);//保存sso
						this.baseDao.update(memberBase);//保存登录信息
						if(memberBase.getSubMemberType() != null && 11==memberBase.getSubMemberType()){//当为investor 个人时,加入member_individual.id
							String hqlTmp = "from MemberIndividual r where r.member.id = ? ";
							List paramsTmp = new ArrayList();
							paramsTmp.add(memberBase.getId());
							List<MemberIndividual> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
							if(null!=listTmp && !listTmp.isEmpty()){
								MemberIndividual objTmp = listTmp.get(0);
								ssoVO.setIndividualId(objTmp.getId());
								ssoVO.setGender(objTmp.getGender());
								String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
								if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
									enName +=" "+objTmp.getLastName();
								}
								String chiName = objTmp.getNameChn();
								if(chiName == null || "".equals(chiName)){
									chiName = enName;
								}
								if(ssoVO.getLangCode().equals(CommonConstants.LANG_CODE_EN)){
									if(!"".equals(enName) && !"".equals(chiName)){//同时有中文和英文
										ssoVO.setMemberName(enName+"("+chiName+")");
									}else if(!"".equals(enName) && "".equals(chiName)){//只有英文
										ssoVO.setMemberName(enName);
									}else if("".equals(enName) && !"".equals(chiName)){//只有中文
										ssoVO.setMemberName(chiName);
									}
								}else{// 中文版：显示中文名，如果没，则显示英文名
									if(!"".equals(chiName)){
										ssoVO.setMemberName(chiName);
									}else{
										ssoVO.setMemberName(enName);
									}
								}
								if(ssoVO.getMemberName() == null || "".equals(ssoVO.getMemberName())){
									if(ssoVO.getNickName() != null && !"".equals(ssoVO.getNickName())){
										ssoVO.setMemberName(ssoVO.getNickName());
									}else{
										ssoVO.setMemberName(ssoVO.getLoginCode());
									}
								}
							}
						}else if(memberBase.getSubMemberType() != null && 12==memberBase.getSubMemberType()){//当为investor 企业时,加入member_corporate.id
							String hqlTmp = "from MemberCorporate r where r.member.id = ? ";
							List paramsTmp = new ArrayList();
							paramsTmp.add(memberBase.getId());
							List<MemberCorporate> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
							if(null!=listTmp && !listTmp.isEmpty()){
								MemberCorporate objTmp = listTmp.get(0);
								ssoVO.setCorporateId(objTmp.getId());
							}
						}else if(memberBase.getSubMemberType() != null && 13==memberBase.getSubMemberType()){//当为investor fi时,加入member_fi.id
							String hqlTmp = "from MemberFi r where r.member.id = ? ";
							List paramsTmp = new ArrayList();
							paramsTmp.add(memberBase.getId());
							List<MemberFi> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
							if(null!=listTmp && !listTmp.isEmpty()){
								MemberFi objTmp = listTmp.get(0);
								ssoVO.setFiId(objTmp.getId());
							}
						}else if(memberBase.getSubMemberType() != null && 21==memberBase.getSubMemberType()){//当为ifa时,加入member_ifa.id
							String hqlTmp = "from MemberIfa r where r.member.id = ? ";
							List paramsTmp = new ArrayList();
							paramsTmp.add(memberBase.getId());
							List<MemberIfa> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
							if(null!=listTmp && !listTmp.isEmpty()){
								MemberIfa objTmp = listTmp.get(0);
								ssoVO.setIfaId(objTmp.getId());
								ssoVO.setGender(objTmp.getGender());
								String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
								if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
									enName +=" "+objTmp.getLastName();
								}
								String chiName = objTmp.getNameChn();
								if(chiName == null || "".equals(chiName)){
									chiName = enName;
								}
								if(ssoVO.getLangCode().equals(CommonConstants.LANG_CODE_EN)){
									if(!"".equals(enName) && !"".equals(chiName)){//同时有中文和英文
										ssoVO.setMemberName(enName+"("+chiName+")");
									}else if(!"".equals(enName) && "".equals(chiName)){//只有英文
										ssoVO.setMemberName(enName);
									}else if("".equals(enName) && !"".equals(chiName)){//只有中文
										ssoVO.setMemberName(chiName);
									}
								}else{// 中文版：显示中文名，如果没，则显示英文名
									if(!"".equals(chiName)){
										ssoVO.setMemberName(chiName);
									}else{
										ssoVO.setMemberName(enName);
									}
								}
								//都为空时
								if(ssoVO.getMemberName() == null || "".equals(ssoVO.getMemberName())){
									if(ssoVO.getNickName() != null && !"".equals(ssoVO.getNickName())){
										ssoVO.setMemberName(ssoVO.getNickName());//呢称不为空
									}else{
										ssoVO.setMemberName(ssoVO.getLoginCode());//呢称也为空
									}
								}
							}
						}
						else if(22==memberBase.getSubMemberType()){//当为ifafirm时,加入member_ifafirm.id
							String hqlTmp = "select r from MemberIfafirm r left join MemberAdmin a on r.id=a.ifafirm.id where a.member.id = ? ";
							List paramsTmp = new ArrayList();
							paramsTmp.add(memberBase.getId());
							List<MemberIfafirm> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
							if(null!=listTmp && !listTmp.isEmpty()){
								MemberIfafirm objTmp = listTmp.get(0);
								ssoVO.setIfafirmId(objTmp.getId());
							}
						}else if(31==memberBase.getSubMemberType()){//当为distributor时,加入member_distributor.id
							String hqlTmp = "select r from MemberDistributor r left join MemberAdmin a on r.id=a.distributor.id where a.member.id = ? ";
							List paramsTmp = new ArrayList();
							paramsTmp.add(memberBase.getId());
							List<MemberDistributor> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
							if(null!=listTmp && !listTmp.isEmpty()){
								MemberDistributor objTmp = listTmp.get(0);
								ssoVO.setDistributorId(objTmp.getId());
							}
						}else if(4==memberBase.getMemberType()&&41==memberBase.getSubMemberType()){//当为sys/admin时
							//附加信息？
						}
						return ssoVO;
					}
					return ssoVO;
				}
				return ssoVO;
			}else{//密码错误
				ssoVO.setRet(WSConstants.FAIL);
				Integer failTime = memberBase.getFailCount();
				if(failTime == null){
					failTime = 1;
				}else{
					failTime++;
				}
				memberBase.setFailCount(failTime);
				Integer lessCount = sysSite.getLoginFailCount()-failTime;
				if(0==lessCount){
					memberBase.setLockStatus("1");
					memberBase.setLockDate(DateUtil.addDate(new Date(),Calendar.HOUR_OF_DAY,sysSite.getLoginRetryHour()));
					this.baseDao.update(memberBase);
					ssoVO.setFailCount(failTime);
					ssoVO.setErrorCode(WSConstants.CODE1013);
					ssoVO.setErrorMsg(WSConstants.MSG1013);
					return ssoVO;
				}else{
					this.baseDao.update(memberBase);
					ssoVO.setFailCount(failTime);
					ssoVO.setErrorCode(WSConstants.CODE1012);
					ssoVO.setErrorMsg(WSConstants.MSG1012);
					return ssoVO;
				}
			}
		}else{//用户不存在
			ssoVO.setRet(WSConstants.FAIL);
			ssoVO.setErrorCode(WSConstants.CODE1008);
			ssoVO.setErrorMsg(WSConstants.MSG1008);
			return ssoVO;
		}
	}
	
	/**
	 *校验token
	 *@author mjhuang
	 *@param tokenId 要校验的token
	 *@param fromType 来源：web,ios,android
	 *@return 0 参数错误  1成功 2令牌失效 3令牌不存在
	 */
	public String saveCheckSSOToken(String tokenId,String fromType) {
		SysSite sysSite = (SysSite)this.baseDao.get(SysSite.class, CommonConstants.MAIN_SITE_ID);
		String hql = "from MemberSso r where r.tokenId = ? and expireTime >= "+CommonConstants.SYS_DATE;
		List params = new ArrayList();
		params.add(tokenId);
		if("ios".equals(fromType) || "android".equals(fromType)){
			hql += " and r.fromType in ('ios','android') ";
		}else if("web".equals(fromType)){
			hql += " and r.fromType = 'web' ";
		}else{
			return "0";
		}
		List<MemberSso> list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			//校验成功后,要更新token
			MemberSso memberSso = list.get(0);
			memberSso.setExpireTime(DateUtil.getInternalDateByMin(new Date(),sysSite.getTokenTimeOut()));
			this.baseDao.update(memberSso);
			return "1";
		}else{
			hql = "from MemberSso r where r.tokenId = ? ";
			params = new ArrayList();
			params.add(tokenId);
			if("ios".equals(fromType) || "android".equals(fromType)){
				hql += " and r.fromType in ('ios','android') ";
			}else if("web".equals(fromType)){
				hql += " and r.fromType = 'web' ";
			}
			list = this.baseDao.find(hql, params.toArray(), false);
			if(null!=list && !list.isEmpty()){
				return "2";
			}else{
				return "3";
			}
		}
	}
	
	/**
	 *校验当前ip是否存在token
	 *@author mjhuang
	 *@param fromIp 来源 机器ip
	 *@param fromType 来源：web,ios,android
	 *@return true存在 false不存在
	 */
	public boolean checkSSOExist(String fromIp,String fromType) {
		String hql = "from MemberSso r where expireTime >= "+CommonConstants.SYS_DATE;
		List params = new ArrayList();
		if(fromIp != null && !"".equals(fromIp)){
			hql += " and r.ip = ? ";
			params.add(fromIp);
		}else{
			return false;
		}
		if("ios".equals(fromType) || "android".equals(fromType)){
			hql += " and r.fromType in ('ios','android') ";
		}else if("web".equals(fromType)){
			hql += " and r.fromType = 'web' ";
		}else{
			return false;
		}
		List<MemberSso> list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 *校验帐号是否在其他机器有登录
	 *@author mjhuang
	 *@param loginCode 要检查来源的ip
	 *@param fromIp 来源 机器ip
	 *@param fromType 来源：web,ios,android
	 *@return true 在其他机器登录 false没有
	 */
	public boolean checkOtherSSOExist(String loginCode,String fromIp,String fromType){
		String hql = "from MemberSso r where expireTime >= "+CommonConstants.SYS_DATE;
		List params = new ArrayList();
		if(fromIp != null && !"".equals(fromIp)){
			hql += " and r.ip <> ? ";
			params.add(fromIp);
		}else{
			return true;
		}
		if("ios".equals(fromType) || "android".equals(fromType)){
			hql += " and r.fromType in ('ios','android') ";
		}else if("web".equals(fromType)){
			hql += " and r.fromType = 'web' ";
		}else{
			return true;
		}
		List<MemberSso> list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 检查指定用户loginCode是否存在
	 * @param loginCode
	 * @return
	 */
	public MemberBase checkIfExistsUserByLoginCode(String loginCode){
		List params = new ArrayList();
		String hql = "from MemberBase r where (r.loginCode = ? or r.loginCode = ?) ";
		params.add(loginCode.toUpperCase());
		params.add(loginCode.toLowerCase());
		List<MemberBase> list = this.baseDao.find(hql, params.toArray(), false);
		if (null!=list && !list.isEmpty()) return list.get(0);
		return null;
	}
	
	/**
	 * 检查指定用户loginCode是否存在运营公司
	 * @param loginCode
	 * @return
	 */
	public MemberCompany checkIfValidUserByLoginCode(String loginCode){
		List params = new ArrayList();
		String hql = "from MemberCompany r where (r.member.loginCode = ? or r.member.loginCode = ?) ";
		params.add(loginCode.toUpperCase());
		params.add(loginCode.toLowerCase());
		List<MemberCompany> list = this.baseDao.find(hql, params.toArray(), false);
		if (null!=list && !list.isEmpty()) return list.get(0);
		return null;
	}
	
	/**
	 * app启动时用的方法
	 * @param appType 平台类型 1:android   2:ios 
	 * @param deviceId 设备ID
	 * @param token token 设备token值
	 * @param aliasType 帐号
	 * @param alias 帐号所在组
	 */
	public void saveDeviceToken(String appType,String deviceId,String token){
		String hql = "from MemberAppToken t where t.deviceToken=? ";
		List params = new ArrayList();
		params.add(token);
		List<MemberAppToken> list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			MemberAppToken obj = list.get(0);
			obj.setLastUpdate(new Date());
			obj.setIsValid("1");
			obj.setAppType(appType);
			obj.setDeviceId(deviceId);
			obj.setDeviceToken(token);
			baseDao.saveOrUpdate(obj, false);
		}else{
			MemberAppToken appToken = new MemberAppToken();
			appToken.setAppType(appType);
			appToken.setDeviceId(deviceId);
			appToken.setDeviceToken(token);
			appToken.setAliasType("");
			appToken.setAlias("");
			appToken.setCreateTime(new Date());
			appToken.setIsValid("1");
			baseDao.create(appToken);
		}
	}
	
	/**
	 *  保存设备的Token与帐号的关联
	 * @param memberId 会员ID
	 * @param type 操作类型 0：保存帐号关联，1：去掉帐号关联
	 * @param appType 平台类型 1:android   2:ios 
	 * @param deviceId 设备编号
	 * @param token 设备token值
	 * @param aliasType 帐号
	 * @param alias 帐号所在组
	 */
	public void saveTokenAccount(String memberId,String type,String appType,String deviceId,String token,String aliasType,String alias){
		String hql = "from MemberAppToken t where t.deviceToken=? ";
		List params = new ArrayList();
		params.add(token);
		List<MemberAppToken> list = this.baseDao.find(hql, params.toArray(), false);
		MemberBase member=new MemberBase();
		member.setId(memberId);
		if(null!=list && !list.isEmpty()){
			MemberAppToken obj = list.get(0);
			if("0".equals(type)){
				obj.setMemberId(memberId);
			}else{
				obj.setMemberId(null);
			}
			obj.setAppType(appType);
			obj.setDeviceId(deviceId);
			obj.setDeviceToken(token);
			obj.setAliasType(aliasType);
			obj.setAlias(alias);
			obj.setLastUpdate(new Date());
			obj.setIsValid("1");
			baseDao.saveOrUpdate(obj, false);
		}else{
			if("0".equals(type)){
				MemberAppToken appToken = new MemberAppToken();
				appToken.setAppType(appType);
				appToken.setDeviceId(deviceId);
				appToken.setDeviceToken(token);
				appToken.setAliasType(aliasType);
				appToken.setAlias(alias);
				appToken.setMemberId(memberId);
				appToken.setCreateTime(new Date());
				appToken.setLastUpdate(new Date());
				appToken.setIsValid("1");
				baseDao.create(appToken);
			}
		}
	}
	
	/***************  登录sso相关方法　end  **********************/
	
}
