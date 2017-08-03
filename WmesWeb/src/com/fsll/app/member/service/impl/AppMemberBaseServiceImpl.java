/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.app.member.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.app.member.service.AppMemberBaseService;
import com.fsll.app.member.vo.AppInfoVO;
import com.fsll.app.member.vo.AppMemberBaseVO;
import com.fsll.app.member.vo.AppMemberSsoVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.security.PwdEncoder;
import com.fsll.common.util.DESUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.MailUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysSite;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.vo.CompanyInfoVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.MemberAppToken;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberFi;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MemberRegTmp;
import com.fsll.wmes.entity.MemberSso;
import com.fsll.wmes.entity.MemberValidateInfo;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.web.service.EmailLogService;
/***
 * 业务接口实现类：统一帐号管理
 * @author mjhuang
 * @date 2016-6-20
 */
@Service("appMemberBaseService")
//@Transactional
public class AppMemberBaseServiceImpl extends BaseService implements AppMemberBaseService {
	@Autowired
	protected PwdEncoder pwdEncoder;//密码加密接口类
	
	@Autowired
	protected EmailLogService emailService;

	@Autowired
    private CompanyInfoService companyInfoService;
	
	/**
	 * 增加或者修改一条数据
	 * @param memberBase 帐号信息
	 * @return 
	 */
	public  MemberBase saveOrUpdate(MemberBase memberBase){
		if(null == memberBase.getId() || "".equals(memberBase.getId())){
			memberBase.setId(null);
			memberBase.setCreateTime(new Date());
			memberBase = (MemberBase)baseDao.create(memberBase);
		}else{
			memberBase = (MemberBase)baseDao.update(memberBase);
		}
		return memberBase;
	}
	
	/***************  登录sso相关方法　begin  **********************/
	/**
	 * sso登录验证
	 * 
	 * @param request 请求
	 * @param loginCode 登录帐号
	 * @param password 密码
	 * @param fromType 来源：web,ios,android
	 * @return
	 */
	public AppMemberSsoVO saveLoginAuth(HttpServletRequest request,String loginCode,String password,String fromType) {
		SysSite sysSite = (SysSite)this.baseDao.get(SysSite.class, CommonConstants.MAIN_SITE_ID);
		
		AppMemberSsoVO ssoVO = new AppMemberSsoVO();
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
	//@Transactional(readOnly=true)
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
	//@Transactional(readOnly=true)
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
	
	/***************  登录sso相关方法　end  **********************/
	
	/**
	 * 发送邮件(调用邮件服务器发送邮件，并写邮件发送日志）
	 * @author michael
	 * @param moduleType 调用模块
	 * @param receiver 接收人邮箱
	 * @param subject 邮件标题
	 * @param message 邮件内容
	 * @return emailLog 邮件发送日志，返回null，发送失败
	 */
	public WebEmailLog sendEmail(String moduleType, MemberBase sender, MemberBase receiver,String receiverEmail, String subject, String message){
		boolean sendFlag = false;
		try {
			
			//发送邮件
			sendFlag = new MailUtil().send(receiverEmail, subject, message,null);
			
			//写发送邮件日志
			WebEmailLog webEmailLog = new WebEmailLog();
			webEmailLog.setModuleType(moduleType);
			webEmailLog.setSendCount(1);
			webEmailLog.setSendedTime(new Date());
			//发送状态：0 - fail, 1- succ
			if (sendFlag){
				webEmailLog.setSendFlag("1");
			}else {
				webEmailLog.setSendFlag("0");
			}
			webEmailLog.setToEmailAddr(receiverEmail);
			webEmailLog.setToEmailContent(message);
			webEmailLog.setToEmailTitle(subject);
			webEmailLog.setToMember(receiver);
			webEmailLog = emailService.saveOrUpdate(webEmailLog);
			
			return webEmailLog;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *	判断数据项重复接口
	 *@author scshi
	 *@param items
	 *@return InfoVO 	fieldName对应传入jason参数的key值;duplicate 0：不重复，1：重复
	 * */
	//@Transactional(readOnly=true)
	public AppInfoVO checkDuplicate(String items) {
		return checkDuplicate("MemberBase", items);
	}
	/**
	 *	判断数据项重复接口
	 *@author scshi
	 *@param tableName
	 *@param items
	 *@return InfoVO fieldName对应传入json参数的key值;duplicate 0：不重复，1：重复
	 * */
	//@Transactional(readOnly=true)
	public AppInfoVO checkDuplicate(String tableName, String items) {
		AppInfoVO vo = new AppInfoVO();
		try {
			List params = new ArrayList();
			
			StringBuffer hql = new StringBuffer("from "+tableName+" t where 1=1 and (");
			
			JSONObject jsonItems = JSONObject.fromObject(items); 
			Iterator<String> iterator = jsonItems.keys();
			jsonItems.size();
			String itemName = "";
			int index = 0;
			while(iterator.hasNext()){
				if(index>0){
					hql.append(" or ");
					itemName+=",";
				}
				String key = iterator.next();
				String value = jsonItems.getString(key);
				if("regPhoneKey".equals(key)||"disPhoneKey".equals(key)){
					key = "mobileNumber";
				}
				hql.append(key +"='"+value+"'" );
				itemName += key;
				index++;
			}
			hql.append(")");
			List list = this.baseDao.find(hql.toString(), params.toArray(), false);
			vo.setFieldName(itemName);
			if(list.isEmpty()){
				vo.setDuplicate("0");
			}else{
				vo.setDuplicate("1");
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return vo;
	}
	
	/**
	 * 创建会员接口
	 * @author scshi
	 * @param accountDataJSON
	 * @param memberDataJSON
	 * @return String 会员id
	 * */
	public String createMember(JSONObject accountDataJSON, JSONObject memberDataJSON,String companyInfoCode) {
		String subdivided = accountDataJSON.getString("subMemberType");//子用户类型
		
		//-------------- 公司账号 -----------------
		if("22".equals(subdivided)|| "31".equals(subdivided)){//ifa 公司與distributor不需要寫入member_base表，写在member_reg_tmp表
			//String remark = memberDataJSON.getString("remark");
			MemberRegTmp memTemp = new MemberRegTmp();
			memTemp.setId(null);
			memTemp.setCompanyName(accountDataJSON.getString("companyName")==null?"":accountDataJSON.getString("companyName"));
			memTemp.setLinkName(accountDataJSON.getString("contact")==null?"":accountDataJSON.getString("contact"));
			memTemp.setLinkEmail(accountDataJSON.getString("email")==null?"":accountDataJSON.getString("email"));
			memTemp.setLinkPhone(accountDataJSON.optString("mobilePhone",""));
			memTemp.setRegType("22".equals(subdivided)?1:2);
			memTemp.setMobileCode(accountDataJSON.getString("mobileCode")==null?"":accountDataJSON.getString("mobileCode"));
			memTemp.setMobileNumber(accountDataJSON.getString("mobileNumber")==null?"":accountDataJSON.getString("mobileNumber"));
			memTemp.setId(null);
			memTemp.setCreateTime(new Date());
			memTemp.setIsValid("1");
			this.baseDao.create(memTemp);
			return memTemp.getId();
		}
		
		//-------------- 个人账号 ----------------
		String loginCode = accountDataJSON.getString("loginCode");
		String nickName = accountDataJSON.optString("nickName","");
		String email = accountDataJSON.getString("email");
		String password = this.pwdEncoder.encodePassword(accountDataJSON.getString("password"));
		String memberType = accountDataJSON.getString("memberType");
		String mobileCode = accountDataJSON.optString("mobileCode","");
		String mobileNumber = accountDataJSON.optString("mobileNumber","");
		
		//检测唯一性
		JSONObject jsonItems = new JSONObject(); 
		jsonItems.put("loginCode", loginCode);
		jsonItems.put("email", email);
		jsonItems.put("mobileNumber", mobileNumber);
		AppInfoVO uniVo = checkDuplicate(JsonUtil.toJson(jsonItems));
		if (null!=uniVo && "1".equals(uniVo.getDuplicate())) return WSConstants.CODE1010;//已存在
		
		//保存账号信息
		MemberBase memberBase = new MemberBase();
		memberBase.setId(null);
		memberBase.setLoginCode(loginCode);
		memberBase.setEmail(email);
		memberBase.setPassword(password);
		memberBase.setMemberType(Integer.parseInt(memberType));
		memberBase.setSubMemberType(Integer.parseInt(subdivided));
		memberBase.setCreateTime(new Date());
		memberBase.setLastUpdate(memberBase.getCreateTime());
		memberBase.setIsValid("1");//有效状态
		memberBase.setStatus("0");//待激活
		memberBase.setMobileCode(mobileCode);
		memberBase.setMobileNumber(mobileNumber);
		memberBase.setNickName("".equals(nickName)?loginCode:nickName);
		
		String gender = memberDataJSON.optString("gender","");//获取性别
		String iconUrl = accountDataJSON.optString("iconUrl","");
//		iconUrl = "/res/images/head_m.png";
//		if ("F".equalsIgnoreCase(gender)) iconUrl = "/res/images/head_f.png";
		if(null==iconUrl || "".equals(iconUrl)){
			iconUrl = "/res/images/head_"+gender.toLowerCase()+".png";
		}
		memberBase.setIconUrl(iconUrl);

		this.baseDao.create(memberBase);
		
		//详细信息
		nickName = memberDataJSON.optString("nickName","");
		String firstName = memberDataJSON.optString("firstName","");
		String lastName = memberDataJSON.optString("lastName","");
		String address = memberDataJSON.optString("address","");
		String chnName = memberDataJSON.optString("nameChn","");
		String certType = memberDataJSON.optString("certType","");
		String certNo = memberDataJSON.optString("certNo","");
		String occupation = memberDataJSON.optString("occupation","");//编码
		String birthPlace = memberDataJSON.optString("country","");//ID
		String birthDate = memberDataJSON.optString("born","");
		String nationality = memberDataJSON.optString("nationality","");//编码
		String education = memberDataJSON.optString("education","");//编码
		String employment = memberDataJSON.optString("employment","");//编码
		String telephone = memberDataJSON.optString("telephone","");
		
		if("11".equals(subdivided) ){//独立投资人
			//String loginIDMem = memberDataJSON.getString("loginID");//对应账号表id
			//String residence = memberDataJSON.getString("residence");//主要居住国
			MemberIndividual memberIdual = new MemberIndividual();
			memberIdual.setId(null);
			memberIdual.setCreateTime(new Date());
			memberIdual.setMember(memberBase);//登陆用户
//			memberIdual.setNickName(nickName);
			memberIdual.setFirstName(firstName);
			memberIdual.setLastName(lastName);
			memberIdual.setAddress(address);
			memberIdual.setNameChn(chnName);
			memberIdual.setCertType(certType);
			memberIdual.setCertNo(certNo);
			memberIdual.setGender(gender);
			memberIdual.setOccupation(occupation);
			memberIdual.setEmployment(employment);
			memberIdual.setEducation(education);
			memberIdual.setTelephone(telephone);
			memberIdual.setEmployment(employment);
			
			if(null==birthPlace || "".equals(birthPlace)){
				memberIdual.setCountry(null);
			}else{
				try {
					SysCountry sc = (SysCountry)this.baseDao.get(SysCountry.class, birthPlace);
					memberIdual.setCountry(sc);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			try{
				memberIdual.setBorn(DateUtil.getDate(birthDate));
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(null==nationality||"".equals(nationality)){
				memberIdual.setNationality(null);
			}else{
				try {
					SysCountry nation = (SysCountry)this.baseDao.get(SysCountry.class, nationality);
					memberIdual.setNationality(nation);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			memberIdual.setCreateTime(new Date());
			memberIdual.setLastUpdate(memberIdual.getCreateTime());
			this.baseDao.create(memberIdual);
			
		}else if("21".equals(subdivided)){//IFA個人
			MemberIfa memIfa = new MemberIfa();
			memIfa.setId(null);
			memIfa.setMember(memberBase);
			memIfa.setFirstName(firstName);
			memIfa.setLastName(lastName);
//			memIfa.setNickName(nickName);
			memIfa.setNameChn(chnName);
			
			if(null==birthPlace || "".equals(birthPlace)){
				memIfa.setCountry(null);
			}else{
				SysCountry sc = (SysCountry)this.baseDao.get(SysCountry.class, birthPlace);
				memIfa.setCountry(sc);
			}
			
			memIfa.setGender(gender);
			memIfa.setCertType(certType);
			memIfa.setCertNo(certNo);
			
			try{
				memIfa.setBorn(DateUtil.getDate(birthDate));
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(null==nationality||"".equals(nationality)){
				memIfa.setNationality(null);
			}else{
				SysCountry nation = (SysCountry)this.baseDao.get(SysCountry.class, nationality);
				memIfa.setNationality(nation);
			}
			memIfa.setEducation(memberDataJSON.getString("education")==null?"":memberDataJSON.getString("education"));
			memIfa.setOccupation(occupation);
			memIfa.setCeNumber(memberDataJSON.getString("CE")==null?"":memberDataJSON.getString("CE"));
			memIfa.setCfaNumber(memberDataJSON.getString("cfa_no")==null?"":memberDataJSON.getString("cfa_no"));
			memIfa.setCfpNumber(memberDataJSON.getString("cfp")==null?"":memberDataJSON.getString("cfp"));
			String primaryResidence = memberDataJSON.getString("primaryResidence");
			if(null==primaryResidence || "".equals(primaryResidence)){
				memIfa.setPrimaryResidence(null);
			}else{
				SysCountry pe = (SysCountry)this.baseDao.get(SysCountry.class, primaryResidence);
				memIfa.setPrimaryResidence(pe);
			}
			memIfa.setTelephone(memberDataJSON.optString("mobile",""));
			memIfa.setIntroduction(memberDataJSON.getString("introduction")==null?"":memberDataJSON.getString("introduction"));
			memIfa.setCreateTime(new Date());
			memIfa.setLastUpdate(memIfa.getCreateTime());

			this.baseDao.create(memIfa);
		}
		
		//插入记录到MemberCompany
		String hql = "from CompanyInfo t where t.isValid='1' and t.code=? ";
		List<CompanyInfo> compList = baseDao.find(hql, new Object[]{companyInfoCode}, false);
		if(null != compList && !compList.isEmpty()){
			MemberCompany mcInfo = new MemberCompany();
			mcInfo.setId(null);
			mcInfo.setCompany(compList.get(0));
			mcInfo.setMember(memberBase);
			this.baseDao.create(mcInfo);
		}		

		
		String activeCode = StrUtils.getRandomString(6);//获取6位随机码
		sendRegEmail(memberBase.getLoginCode(), memberBase.getEmail(), activeCode);
		
		return memberBase.getId();
	}

	/**
	 * @author scshi
	 * @date 20160627
	 * 3.4.3 发送激活邮件接口
	 * @param loginID 登录账号
	 * @param email	邮件接受地址
	 * @param activeCode 激活码，=Encrypt.md5(email+pwd+time)
	 * @return 结果代码
	 * */
	public ResultWS sendRegEmail(String loginID, String email, String activeCode) {
		return sendValidateEmail(loginID, email, 1, activeCode);
	}
	
	/**
	 * @author michael
	 * @date 20160712
	 * 发送重置密码邮件接口
	 * @param email	邮件接受地址
	 * @param activeCode 随机6位字符串（字母数字）
	 * @return 结果代码
	 * */
	public ResultWS sendResetPasswordEmail(String email, String validateCode) {
		return sendValidateEmail("", email, 2, validateCode);
	}
	
	/**
	 * @author Michael
	 * @date 20160712
	 * 发送用户验证邮件接口（写记录到用户验证表）
	 * @param loginCode	登录账号
	 * @param email	邮件接受地址
	 * @param validateType 1:激活,2:取回密码,3:修改密码
	 * @param validateCode 验证码
	 * @return 结果代码
	 * */
	private ResultWS sendValidateEmail(String loginCode, String email, int validateType, String validateCode) {
		ResultWS result = new ResultWS();
		result.setRet(WSConstants.SUCCESS);
		result.setData(null);
		
		MemberBase user = null;
		if(StringUtils.isNotBlank(loginCode)){
			user = findByCode(loginCode);//通过登录账号查找有效的用户 isValid=1
		}
		if (null==user && StringUtils.isNotBlank(email)){
			user = findByEmail(email);//通过邮箱查找有效的用户 isValid=1
		}
		
		if (null!=user){
			//检测用户状态
			if (null!=user.getStatus() && 
					(("1".equals(user.getStatus()) &&  2==validateType)||("0".equals(user.getStatus()) &&  1==validateType))
					){
				//@@发送邮件				
				String moduleType = "";
				String subject = "";
				String message = "";
				String langCode = null==user.getLangCode()?CommonConstants.DEF_LANG_CODE:user.getLangCode();
				String href = "";
				if (1==validateType){
					moduleType = "register";
					subject = "member.register.email.subject";
					message = "member.register.email.msg";
					
					String siteUrl = "";
					String siteName = "";
					try{
//						String companyCode = PropertyUtils.getConfPropertyValue(CoreConstants.DEFAULT_COMPANY);
						CompanyInfoVO companyInfo = companyInfoService.findByMember(user.getId());
						if(null != companyInfo){
							companyInfo.setInfos(langCode);
							siteUrl = companyInfo.getWebUrl();
							siteName = companyInfo.getName();
						}
					}catch(Exception e){
						e.printStackTrace();
					}

					//新内容模板
					href = PropertyUtils.getConfPropertyValue("wmes_web_site")+"/front/regist/memActive.do?memId="+user.getId()+"&valiCode="+validateCode;
					String[] vals = new String[]{user.getLoginCode(),siteName,href,siteName,siteUrl};
					message = PropertyUtils.getPropertyValue(langCode,message,vals);
				}else if (2==validateType){
					moduleType = "resetPass";
					subject = "member.resetPassword.email.subject";
					message = "member.resetPassword.email.msg";
					
					href = "<a href='"+validateCode+"'>"+validateCode+"</a>";
					String[] vals = new String[]{href};
					message = PropertyUtils.getPropertyValue(langCode,message,vals);
				}else if (3==validateType){
					moduleType = "changePass";
				}
				
				subject = PropertyUtils.getPropertyValue(langCode,subject,null);
				
				//发送邮件和写邮件日志
				WebEmailLog emailLog = sendEmail(moduleType, null, user, email, subject, message);
				
				
				try {
					//临时密码不用加密
					//1:激活,2:取回密码,3:修改密码
					saveMemberValidateInfo(user.getId(), email, validateType, validateCode, DateUtil.getCurDate(Calendar.DATE, 1),emailLog);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					result.setErrorCode(WSConstants.CODE1000);
					result.setErrorMsg(WSConstants.MSG1000);
				}
			}
			else{
				//用户未激活
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1004);
				result.setErrorMsg(WSConstants.MSG1004);
			}
		}else{
			//用户不存在
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1008);
			result.setErrorMsg(WSConstants.MSG1008);
		}
		return result;
	}
	
	/**
	 * 通过用户用户帐号查看用户
	 * @param loginCode 用户帐号
	 * @return
	 */
	//@Transactional(readOnly = true)
	private MemberBase findByCode(String loginCode) {
		List<Object> params = new ArrayList<Object>();
		String hql = "from MemberBase r where r.isValid='1' and upper(r.loginCode)=?";
		params.add(loginCode.toUpperCase());
		List<MemberBase> accounts = this.baseDao.find(hql, params.toArray(), false);
		if (!accounts.isEmpty()) {
			return accounts.get(0);
		}
		return null;
	}
	
	/**
	 * 保存用户验证信息
	 * @author michael
	 * @param memberId 用户id
	 * @param email 用户email
	 * @param validType 验证信息类型 1:激活,2:取回密码,3:修改密码
	 * @param validCode 验证码
	 * @param expireTime 过期时间
	 * @return
	 */
	public MemberValidateInfo saveMemberValidateInfo(String memberId, String email, int validType, String validCode, Date expireTime,WebEmailLog emailLog){
		deleteMemberValidateInfo(memberId, email, validType);
		
		MemberValidateInfo info = new MemberValidateInfo();
		MemberBase member = null;
		try {
			if (null!=memberId && !"".equals(memberId))
				member = findById(memberId);
			if (null==member && null!=email && !"".equals(email))
				member = findByEmail(email);
		} catch (Exception e) {
			// TODO: handle exception
		}
		info.setMember(member);
		info.setExpireTime(expireTime);
		info.setValidateCode(validCode);
		info.setValidateType(validType);
		info.setEmailLog(emailLog);
		info.setCreateTime(new Date());
		
		return (MemberValidateInfo) this.baseDao.create(info);
	}
	
	/**
	 * 获取个人验证信息
	 * @author michael
	 * @param memberId	用户id
	 * @param email 用户email
	 * @param validType 验证信息类型 1:激活,2:取回密码,3:修改密码
	 * @return int
	 * */
	//@Transactional(readOnly = true)
	public int deleteMemberValidateInfo(String memberId, String email, int validType) {
		List<Object> params = new ArrayList<Object>();
		String hql = "from MemberValidateInfo t where validateType=? ";
		params.add(validType);
		if (null!=memberId && !"".equals(memberId) && null!=email && !"".equals(email)){
			hql += " and (";
			hql += " t.member.id=? ";
			hql += " or t.member.email=? "; 
			hql += " )";
			params.add(memberId);
			params.add(email);
		}else if (null!=memberId && !"".equals(memberId)){
			hql += " and t.member.id=? ";
			params.add(memberId);
		}else if (null!=email && !"".equals(email)){
			hql += " and t.member.email=? "; 
			params.add(email);
		}
//		int cnt = this.baseDao.updateHql(hql, params.toArray());
		int cnt = 0;
		List<MemberValidateInfo> list = this.baseDao.find(hql, params.toArray(), false);
		for (MemberValidateInfo i: list){
			this.baseDao.delete(i);
			cnt++;
		}
		return cnt;
	}
	
	/**
	 * 获取个人验证信息
	 * @author michael
	 * @param memberId	用户id
	 * @param email 用户email
	 * @param validType 验证信息类型 1:激活,2:取回密码,3:修改密码
	 * @return list
	 * */
	//@Transactional(readOnly = true)
	public List<MemberValidateInfo> findMemberValidateInfo(String memberId, String email, int validType) {
		List<Object> params = new ArrayList<Object>();
		String hql = "from MemberValidateInfo t where validateType=? ";
		params.add(validType);
		if (null!=memberId && !"".equals(memberId) && null!=email && !"".equals(email)){
			hql += " and (";
			hql += " t.member.id=? ";
			hql += " or t.member.email=? "; 
			hql += " )";
			params.add(memberId);
			params.add(email);
		}else if (null!=memberId && !"".equals(memberId)){
			hql += " and t.member.id=? ";
			params.add(memberId);
		}else if (null!=email && !"".equals(email)){
			hql += " and t.member.email=? "; 
			params.add(email);
		}
		List<MemberValidateInfo> list = this.baseDao.find(hql, params.toArray(), false);

		return list;
	}
	
	/**
	 * 通过用户邮箱查找用户
	 * @author michael
	 * @param email 用户帐号
	 * @return
	 */
	public MemberBase findByEmail(String email){
		List<Object> params = new ArrayList<Object>();
		String hql = "from MemberBase r where r.isValid='1' and r.email=? ";
		params.add(email);
		List<MemberBase> accounts = this.baseDao.find(hql, params.toArray(), false);
		if (!accounts.isEmpty()) {
			return accounts.get(0);
		}
		return null;
	}
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public MemberBase findById(String id){
		MemberBase memberBase = (MemberBase) baseDao.get(MemberBase.class, id);
		return findDetail(memberBase);
	}
	
	/**
	 * 获得一条记录的详细信息
	 * @param CompanyInfo
	 * @return
	 */
	private MemberBase findDetail(MemberBase memberBase){
		
		return memberBase;
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
	
	
	/**
	 * 设置——个人信息
	 * @author zxtan
	 * @date 2017-02-20
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public AppMemberBaseVO findMemberBase(String memberId,String langCode){
		AppMemberBaseVO vo = new AppMemberBaseVO();
		StringBuilder hql = new StringBuilder();
		hql.append(" from MemberBase m");
		hql.append(" left join MemberIndividual d on d.member.id=m.id ");
		hql.append(" left join MemberIfa i on i.member.id=m.id ");
//		hql.append(" left join SysParamConfig c on c.configCode=m.defCurrency ");
		hql.append(" where m.id=? ");
		List<String> params = new ArrayList<String>();
//		params.add(langCode);
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			Object[] obj = (Object[]) list.get(0);
			MemberBase member = (MemberBase) obj[0];			
//			SysParamConfig config = (SysParamConfig) obj[3];
			
			vo.setId(member.getId());
			vo.setLoginCode(member.getLoginCode());
			vo.setNickName(member.getNickName());
			vo.setEmail(member.getEmail());
			vo.setMobileCode(member.getMobileCode());
			vo.setMobileNumber(member.getMobileNumber());
			String hobby = getParamConfigNameList(langCode, member.getHobbyType());
			
			String iconUrl = member.getIconUrl();
			String gender = "";
			if(1 == member.getMemberType()){
				//investor
				MemberIndividual individual = (MemberIndividual) obj[1];
				gender = individual.getGender();
			}else {
				//ifa
				MemberIfa ifa = (MemberIfa) obj[2];
				gender = ifa.getGender();
			}
			iconUrl = PageHelper.getUserHeadUrlForWS(iconUrl, gender);
			vo.setIconUrl(iconUrl);
			vo.setGender(gender);
			vo.setDefDisplayColor(member.getDefDisplayColor());
			vo.setDefCurrencyCode(member.getDefCurrency());
			vo.setDateformat(member.getDateFormat());
			vo.setDefCurrency(getParamConfigNameList(langCode, member.getDefCurrency()));
			String hobbyType = member.getHobbyType();
			if(StringUtils.isNotBlank(hobbyType)){
				hobby = (null == hobby)?"":hobby;
				String[] arr = hobbyType.split(",");
				for (String item : arr) {
					if(item.indexOf("{")>-1){
						hobby += ","+item.replace("{", "").replace("}", "");
					}
				}
			}
			vo.setHobby(hobby);
		}
		
		return vo;
	}
	
}
