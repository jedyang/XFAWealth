/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service.impl;

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

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.security.PwdEncoder;
import com.fsll.common.util.DESUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.MailUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysSite;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.vo.CompanyInfoVO;
import com.fsll.wmes.entity.IfaExtInfo;
import com.fsll.wmes.entity.IfaHighlight;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberFi;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MemberRegTmp;
import com.fsll.wmes.entity.MemberSso;
import com.fsll.wmes.entity.MemberValidateInfo;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.im.ImTbUtil;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.InfoVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.member.vo.PersonalInfoVO;
import com.fsll.wmes.member.vo.WebToDoToReadVO;
import com.fsll.wmes.web.service.EmailLogService;
/***
 * 业务接口实现类：统一帐号管理
 * @author mjhuang
 * @date 2016-6-20
 */
@Service("memberBaseService")
//@Transactional
public class MemberBaseServiceImpl extends BaseService implements MemberBaseService {
	@Autowired
	protected PwdEncoder pwdEncoder;//密码加密接口类
	
	@Autowired
	protected EmailLogService emailService;
	@Autowired
	private IfaManageService ifaManageService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
    private CompanyInfoService companyInfoService;
	/**
	 * 增加或者修改一条数据
	 * @param memberBase 帐号信息
	 * @return 
	 */
	public  MemberBase saveOrUpdate(MemberBase memberBase){
		String nickName=memberBase.getNickName();
		if(null==nickName || "".equals(nickName))
			nickName=memberBase.getLoginCode();
		memberBase.setImNickName(nickName);
		memberBase.setImNickUrl("");
		if(null==memberBase.getImUserId() || "".equals(memberBase.getImUserId())){
			memberBase.setImUserId(UUID.randomUUID().toString());
			memberBase.setImUserPwd(memberBase.getImUserId());
		}
		if(null == memberBase.getId() || "".equals(memberBase.getId())){
			memberBase.setId(null);
			memberBase.setCreateTime(new Date());
			memberBase = (MemberBase)baseDao.create(memberBase);
			saveMemberToIm(memberBase, true);
		}else{
			memberBase = (MemberBase)baseDao.update(memberBase);
			saveMemberToIm(memberBase, false);
		}
		return memberBase;
	}
	
	
	/**
	 * 删除其他关联记录
	 * @param id
	 */
	private  void deleteRelate(String id){

	}
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	private boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			MemberBase memberBase = findById(id);
			if(memberBase != null){
				deleteRelate(id);
				baseDao.delete(memberBase);
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids){
		if (!"".equals(ids)) {
			String tmpStr = ids;
			if(ids.endsWith(","))tmpStr = ids.substring(0,ids.length()-1);
			String[] arr = tmpStr.split(",");
			for (String id : arr) {
				boolean result = deleteById(id);
				if(!result){
					return false;
				}
			}
		}
		return true;
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
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public MemberBase findById(String id){
		MemberBase memberBase = (MemberBase) baseDao.get(MemberBase.class, id);
		return findDetail(memberBase);
	}
	
    /***
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param memberBase 查询参数
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonPaging,MemberBase memberBase) {
		String hql=" from MemberBase r where r.isValid='1' ";
		List<Object> params=new ArrayList<Object>();
		if(null!=memberBase.getLoginCode()&&!"".equals(memberBase.getLoginCode())){
			hql+=" and r.loginCode like ? ";
			params.add("%"+memberBase.getLoginCode()+"%");
		}
		if(null!=memberBase.getNickName()&&!"".equals(memberBase.getNickName())){
			hql+=" and r.nickName like ? ";
			params.add("%"+memberBase.getNickName()+"%");
		}
		if(null!=memberBase.getMemberType()){
			hql+=" and r.memberType = ? ";
			params.add(memberBase.getMemberType());
		}
		if(null!=memberBase.getSubMemberType()){
			hql+=" and r.subMemberType = ? ";
			params.add(memberBase.getSubMemberType());
		}
		if(null!=memberBase.getStatus()&&!"".equals(memberBase.getStatus())){
			hql+=" and r.status = ? ";
			params.add(memberBase.getStatus());
		}
		if(null!=memberBase.getLangCode()&&!"".equals(memberBase.getLangCode())){
			hql+=" and r.langCode = ? ";
			params.add(memberBase.getLangCode());
		}
		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List list = new ArrayList();
		Iterator it = jsonPaging.getList().iterator();
		Integer indexNumber = (jsonPaging.getPage()-1)*jsonPaging.getRows();
		Integer index = 0;
    	while(it.hasNext()){
			index++;
			MemberBase obj = (MemberBase)it.next();
			obj = findDetail(obj);
			obj.setXh(Integer.toString(indexNumber+index));
			list.add(obj);
		}
    	jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 通过用户用户帐号查看用户
	 * @param loginCode 用户帐号
	 * @param password 用户密码
	 * @return
	 */
	//@Transactional(readOnly = true)
	public MemberBase findByCode(String loginCode,String password) {
		List<Object> params = new ArrayList<Object>();
		String hql = "from MemberBase r where r.isValid='1' and upper(r.loginCode)=? and r.password = ? ";
		params.add(loginCode.toUpperCase());
		params.add(password);
		List<MemberBase> accounts = this.baseDao.find(hql, params.toArray(), false);
		if (!accounts.isEmpty()) {
			return accounts.get(0);
		}
		return null;
	}
	
	/**
	 * 通过用户用户帐号查看用户
	 * @param loginCode 用户帐号
	 * @return
	 */
	//@Transactional(readOnly = true)
	public MemberBase findByCode(String loginCode) {
		if(StringUtils.isNotBlank(loginCode)){
			List<Object> params = new ArrayList<Object>();
			String hql = "from MemberBase r where r.isValid='1' and upper(r.loginCode)=?";
			params.add(loginCode.toUpperCase());
			List<MemberBase> accounts = this.baseDao.find(hql, params.toArray(), false);
			if (!accounts.isEmpty()) {
				return accounts.get(0);
			} else {
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 通过账户类型获取列表
	 * @param memberType
	 * @return
	 */
	@Override
	public List<MemberBase> findListByMemberType(int memberType){
		String hql=" from MemberBase r where r.isValid='1' ";
		List<Object> params=new ArrayList<Object>();
		if(memberType>0){
			hql+=" and r.memberType = ? ";
			params.add(memberType);
		}
		return (List<MemberBase>)this.baseDao.find(hql, params.toArray(), false);
	}
	
	/**
	 * 通过区号和手机号码查找用户（暂用作手机区号+号码的唯一性检查）
	 * @param mobileCode
	 * @param mobileNumber
	 * @return
	 */
	public List<MemberBase> findByMobileCodeAndNumber(String mobileCode, String mobileNumber){
		if(StringUtils.isNotBlank(mobileCode)&&StringUtils.isNotBlank(mobileNumber)){
			String hql = " FROM MemberBase r WHERE r.isValid='1' "
				+ " AND r.mobileCode=? AND r.mobileNumber=? ";
			return (List<MemberBase>)this.baseDao.find(hql, new String[]{mobileCode,mobileNumber}, false);
		} else {
			return null;
		}
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
	public MemberSsoVO saveLoginAuth(HttpServletRequest request,String loginCode,String password,String fromType) {
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
				memberSso.setExpireTime(DateUtil.getInternalDateByMin(curDate,sysSite.getTokenTimeOut()));
				memberSso.setSessionId(request.getSession().getId());
				
				//返回登录信息
				try{
					BeanUtils.copyProperties(ssoVO,memberBase);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
								if(ssoVO.getMemberName() == null || "".equals(ssoVO.getMemberName())){
									if(ssoVO.getNickName() != null && !"".equals(ssoVO.getNickName())){
										ssoVO.setMemberName(ssoVO.getNickName());
									}else{
										ssoVO.setMemberName(ssoVO.getLoginCode());
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
					ssoVO.setErrorMsg(WSConstants.CODE1012);
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
	 *@return true有效 false无效
	 */
	public boolean saveCheckSSOToken(String tokenId,String fromType) {
		SysSite sysSite = (SysSite)this.baseDao.get(SysSite.class, CommonConstants.MAIN_SITE_ID);
		String hql = "from MemberSso r where r.tokenId = ? and expireTime >= "+CommonConstants.SYS_DATE;
		List params = new ArrayList();
		params.add(tokenId);
		if("ios".equals(fromType) || "android".equals(fromType)){
			hql += " and r.fromType in ('ios','android') ";
		}else if("web".equals(fromType)){
			hql += " and r.fromType = 'web' ";
		}else{
			return false;
		}
		List<MemberSso> list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			//校验成功后,要更新token
			MemberSso memberSso = list.get(0);
			memberSso.setExpireTime(DateUtil.getInternalDateByMin(new Date(),sysSite.getTokenTimeOut()));
			this.baseDao.update(memberSso);
			return true;
		}else{
			return false;
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
	 * 判断登陆账号是否存在token
	 * @author wwluo
	 * @param fromIp 来源 机器ip
	 * @param fromType 来源：web,ios,android
	 */
	//@Transactional(readOnly=true)
	public boolean checkSSOyLoginCode(String fromIp,String fromType,String loginCode) {
		//判断是否存在
		if(StringUtils.isNotBlank(loginCode) && checkSSOExist(fromIp,fromType)){
			String hql = "from MemberSso r where expireTime >= "+CommonConstants.SYS_DATE;
			List params = new ArrayList();
			hql += " and r.ip = ? ";
			params.add(fromIp);
			if("ios".equals(fromType) || "android".equals(fromType)){
				hql += " and r.fromType in ('ios','android') ";
			}else if("web".equals(fromType)){
				hql += " and r.fromType = 'web' ";
			}
			hql += " and r.loginCode = ? ";
			params.add(loginCode);
			List<MemberSso> list = this.baseDao.find(hql, params.toArray(), false);
			if(null!=list && !list.isEmpty()){
				return true;
			}else{
				return false;
			}
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
		if (StringUtils.isNotBlank(loginCode)) {
			hql += " and r.loginCode=?";
			params.add(loginCode);
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
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 删除sso信息
	 * @author wwluo
	 * @param loginCode 登陆账号
	 * @param fromIp IP
	 * @param fromType 来源：web,ios,android
	 */
	@Override
	public void delMemberSso(String loginCode,String fromIp,String fromType){
		if (StringUtils.isNotBlank(loginCode) 
				&& StringUtils.isNotBlank(fromIp) 
					&& StringUtils.isNotBlank(fromType)) {
			String hql = "" +
					" FROM" +
					" MemberSso r" +
					" WHERE" +
					" r.ip=?" +
					" AND" +
					" r.loginCode=?" +
					"";
			List params = new ArrayList();
			params.add(fromIp);
			params.add(loginCode);
			if("ios".equals(fromType) || "android".equals(fromType)){
				hql += " AND r.fromType IN ('ios','android') ";
			}else if("web".equals(fromType)){
				hql += " AND r.fromType = 'web' ";
			}
			List<MemberSso> list = this.baseDao.find(hql, params.toArray(), false);
			if(list != null && !list.isEmpty()){
				this.baseDao.delete(list.get(0));
			}
		}
	}
	/***************  登录sso相关方法　end  **********************/
	
	
	/**
	 * 检查帐号的唯一性
	 * @param loginCode 用户帐号
	 * @param accountId 用户id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public MemberBase checkCodeUnique(String loginCode, String rId) {
		List<Object> params = new ArrayList<Object>();
		String hql = "from MemberBase r where upper(r.loginCode = ? ) ";
		params.add(loginCode.toUpperCase());
		if(null!=rId && !"".equals(rId)){
			hql += "and r.id<>? ";
			params.add(rId);
		}
		List<MemberBase> accounts = this.baseDao.find(hql, params.toArray(), false);
		if (!accounts.isEmpty()) {
			return accounts.get(0);
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
	public InfoVO checkDuplicate(String items) {
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
	public InfoVO checkDuplicate(String tableName, String items) {
		InfoVO vo = new InfoVO();
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
	public String createMember(JSONObject accountDataJSON, JSONObject memberDataJSON, String langCode) {
		String subdivided = accountDataJSON.getString("subMemberType");//子用户类型
		
		//-------------- 公司账号 -----------------
		if("22".equals(subdivided)|| "31".equals(subdivided)){//ifa 公司與distributor不需要寫入member_base表，写在member_reg_tmp表
			//String remark = memberDataJSON.getString("remark");
			MemberRegTmp memTemp = new MemberRegTmp();
			memTemp.setId(null);
//			memTemp.setCompanyName(accountDataJSON.getString("companyName")==null?"":accountDataJSON.getString("companyName"));
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
		String nickName = memberDataJSON.optString("nickName","");
		String email = accountDataJSON.getString("email");
		String password = this.pwdEncoder.encodePassword(accountDataJSON.getString("password"));
		String memberType = accountDataJSON.getString("memberType");
		String mobileCode = accountDataJSON.optString("mobileCode","");
		String mobileNumber = accountDataJSON.optString("mobileNumber","");
		String inviteCode = accountDataJSON.optString("inviteCode","");
		
		//检测唯一性
		JSONObject jsonItems = new JSONObject(); 
		jsonItems.put("loginCode", loginCode);
		jsonItems.put("email", email);
		jsonItems.put("mobileNumber", mobileNumber);
		InfoVO uniVo = checkDuplicate(JsonUtil.toJson(jsonItems));
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
		memberBase.setDefCurrency(CommonConstants.DEF_CURRENCY);
		memberBase.setDateFormat(CoreConstants.DATE_FORMAT);
		memberBase.setDefDisplayColor(CommonConstants.DEF_DISPLAY_COLOR);
//		memberBase.setLangCode(CommonConstants.LANG_CODE_SC);
		memberBase.setLangCode(accountDataJSON.optString("langCode",langCode));
		memberBase.setIconUrl(CommonConstantsWeb.DEFAULT_HEADER);//因为注册时没有设置头像，保存是设置为默认头像
		if(StringUtils.isNotBlank(inviteCode)){
			memberBase.setInviteCode(inviteCode);
		}
		
		String gender = memberDataJSON.optString("gender","");//获取性别
		String iconUrl = accountDataJSON.optString("iconUrl","");
//		iconUrl = "/res/images/head_m.png";
//		if ("F".equalsIgnoreCase(gender)) iconUrl = "/res/images/head_f.png";
		if(null==iconUrl || "".equals(iconUrl)){
			iconUrl = "/res/images/common/portrait_"+gender.toLowerCase()+".png";
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
		String ifaFirmId = memberDataJSON.optString("company","");//id
		
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

			memIfa = (MemberIfa)this.baseDao.create(memIfa);
			
			//创建ifa 与 firm的关系
			if (!ifaFirmId.isEmpty()){
				MemberIfafirm ifafirm = new MemberIfafirm();
				ifafirm.setId(ifaFirmId);
				
				MemberIfaIfafirm ifaIfafirm = new MemberIfaIfafirm();
				ifaIfafirm.setIfa(memIfa);
				ifaIfafirm.setIfafirm(ifafirm);
				ifaIfafirm.setCheckStatus("0");
				ifaIfafirm.setIsValid("1");
				ifaIfafirm.setCreateTime(new Date());
				ifaIfafirm.setLastUpdate(memIfa.getCreateTime());
				this.baseDao.create(ifaIfafirm);
			}
		}
		
		return memberBase.getId();
	}

//	/**
//	 * @author scshi
//	 * @date 20160627
//	 * 3.4.3 发送激活邮件接口
//	 * @param loginID 登录账号
//	 * @param email	邮件接受地址
//	 * @param activeCode 激活码，=Encrypt.md5(email+pwd+time)
//	 * @return 结果代码
//	 * */
//	public ResultWS sendRegEmail(String loginID, String email, String activeCode, String path) {
//		return sendValidateEmail(loginID, email, 1, activeCode, path);
//	}
	
//	/**
//	 * @author michael
//	 * @date 20160712
//	 * 发送重置密码邮件接口
//	 * @param email	邮件接受地址
//	 * @param activeCode 随机6位字符串（字母数字）
//	 * @return 结果代码
//	 * */
//	public ResultWS sendResetPasswordEmail(String email, String validateCode) {
//		return sendValidateEmail("", email, 2, validateCode, validateCode);
//	}
	
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
	public ResultWS saveAndSendValidateEmail(String loginCode, String email, int validateType, String validateCode, String path) {
		String msg = "";
		String lang = CommonConstants.DEF_LANG_CODE;
		ResultWS result = new ResultWS();
		result.setRet(WSConstants.SUCCESS);
		result.setData(null);
		
		MemberBase user = findByCode(loginCode);//通过登录账号查找有效的用户 isValid=1
		
		//如果没loginCode则通过邮箱查找有效的用户 isValid=1 -- 例如重置密码的场景
		if (null==user && null!=email && !"".equals(email))
			user = findByEmail(email);
		
		if (null!=user){
			//检测用户状态
			if (null!=user.getStatus() && 
					(("1".equals(user.getStatus()) &&  2==validateType)||("0".equals(user.getStatus()) &&  1==validateType))
					){
				//@@发送邮件，提供临时密码
				String moduleType = "";
				String subject = "";
				String message = "";
				String langCode = null==user.getLangCode()?CommonConstants.DEF_LANG_CODE:user.getLangCode();
				String href="<a href='"+path+"'>"+path+"</a>";

				if (1==validateType){
					moduleType = "register";
					subject = "member.register.email.subject";
					message = "member.register.email.msg";
					
					String siteUrl = "";
					String siteName = "";
					try{
//						String companyCode = PropertyUtils.getConfPropertyValue(CoreConstants.DEFAULT_COMPANY);
						CompanyInfoVO companyInfo = companyInfoService.findByMember(user.getId());
						companyInfo.setInfos(langCode);
						siteUrl = companyInfo.getWebUrl();
						siteName = companyInfo.getName();
					}catch(Exception e){
						
					}
//					//旧内容模板
//					String[] vals = new String[]{href};
					//新内容模板，modified by michael @20170213
					String[] vals = new String[]{user.getLoginCode(),siteName,href,siteName,siteUrl};
					message = PropertyUtils.getPropertyValue(langCode,message,vals);
				}else if (2==validateType){
					moduleType = "resetPass";
					subject = "member.resetPassword.email.subject";
					message = "member.resetPassword.email.msg";
					
					String[] vals = new String[]{href};
					message = PropertyUtils.getPropertyValue(langCode,message,vals);
				}else if (3==validateType){
					moduleType = "changePass";
				}
				
				subject = PropertyUtils.getPropertyValue(langCode,subject,null);
				
				//发送邮件和写邮件日志
				WebEmailLog emailLog = saveAndSendEmail(moduleType, null, user, email, subject, message, null);
				
				try {
					//临时密码不用加密
					//1:激活,2:取回密码,3:修改密码
					saveMemberValidateInfo(user.getId(), email, validateType, validateCode, DateUtil.getCurDate(Calendar.DATE, 1), emailLog);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					result.setErrorCode(WSConstants.CODE1000);
					msg = "wsconstants.msg1000";
					msg = PropertyUtils.getPropertyValue(langCode,msg,null);
					result.setErrorMsg(msg);
				}
			}
			else{
				//用户未激活
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1004);
				msg = "wsconstants.msg1004";
				msg = PropertyUtils.getPropertyValue(lang,msg,null);
				result.setErrorMsg(msg);
			}
		}else{
			//用户不存在
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1008);
			msg = "wsconstants.msg1008";
			msg = PropertyUtils.getPropertyValue(lang,msg,null);
			result.setErrorMsg(msg);
		}
		return result;
	}

	
	/**
	 * 
	 */
	public String getCompletedMessage(String loginId, String email, String langCode) {
		String message = "";
		//String langCode = CommonConstants.DEF_LANG_CODE;
		MemberBase user = findById(loginId);//通过登录账号查找有效的用户 isValid=1
		if(user != null){
			langCode = user.getLangCode();
		}
		String key = "member.register.emailcontent";
		String[] vals = new String[]{email};
		message = PropertyUtils.getPropertyValue(langCode, key, vals);
		return message;
	}
	/***/
	
	
	
	/**
	 * 
	 * */
	public InfoVO checkCompanyUnique(String itemsJSON) {
		
		InfoVO vo = new InfoVO();
		List params = new ArrayList();
		
		StringBuffer hql = new StringBuffer("from MemberRegTmp t where 1=1 and (");
		
		JSONObject jsonItems = JSONObject.fromObject(itemsJSON); 
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
		return vo;
	}

	/**
	 * 公司注册--手机号码唯一验证
	 * */
	public InfoVO checkMobileUnique(String mobileCode, String mobileNumber) {
		InfoVO vo = new InfoVO();
	//	List params = new ArrayList();
		StringBuffer hql = new StringBuffer("from MemberRegTmp t where t.mobileCode=? and t.mobileNumber=? ");
		List list = this.baseDao.find(hql.toString(), new String[]{mobileCode,mobileNumber}, false);
		if(list.isEmpty()){
			vo.setDuplicate("0");
		}else{
			vo.setDuplicate("1");
		}
		vo.setFieldName("mobileCode,mobileNumber");
		return vo;
	}
	
	/***
	 * 获取国家列表
	 * */

	public List<SysCountry> loadSysCountryList() {
		String hql = "from SysCountry t order by t.orderBy ";
		List<SysCountry> list = this.baseDao.find(hql, null, false);
		return list;
	}
	
	/**
	 * 通过ID查找个人信息的完整数据
	 * @author michael
	 * @param id
	 * @param lang 语言编码 
	 * @return
	 */
	//@Transactional(readOnly = true)
	public PersonalInfoVO findPersonalInfoById(String id, String lang)
	{
		PersonalInfoVO infoVO = new PersonalInfoVO();
		MemberBase memberBase = findById(id);
		//memberBase.setLoginTime(DateUtil.getDate(DateUtil.dateToDateString(memberBase.getLoginTime(), memberBase.getDateFormat()+" HH:mm:ss")));
		MemberIndividual individual = findIndividualMember(id);
		MemberIfa ifa = findIfaMember(id);
		try {
			BeanUtils.copyProperties(infoVO.getBaseInfo(), memberBase);
			if (individual!=null){
				BeanUtils.copyProperties(infoVO.getIndividualPerson(), individual);
				infoVO.setNationId(null!=individual.getNationality()?individual.getNationality().getId():"");
				infoVO.setNationName(getCountryString(individual.getNationality(), lang));
			}
			if (ifa!=null){
				BeanUtils.copyProperties(infoVO.getIfaPerson(), ifa);
				infoVO.setIntroduction(ifa.getIntroduction());
				infoVO.setInvestLifeBegin(DateUtil.dateToDateString(ifa.getInvestLifeBegin(), DateUtil.DEFAULT_DATE_FORMAT));
				IfaExtInfo extInfo = findIfaExtInfo(ifa.getId());
				if(null!=extInfo){
					BeanUtils.copyProperties(infoVO.getIfaExtInfo(), extInfo);
					infoVO.setHighlight(extInfo.getHighlight());
				}
				infoVO.setNationId(null!=ifa.getNationality()?ifa.getNationality().getId():"");
				infoVO.setNationName(getCountryString(ifa.getNationality(), lang));
			}
			infoVO.setInfos(lang);
			
			infoVO.setCertType(sysParamService.findNameByCode(infoVO.getCertTypeId(), lang));
			infoVO.setCountryName(sysParamService.findNameByCode(infoVO.getCountryId(), lang));
			infoVO.setEducation(sysParamService.findNameByCode(infoVO.getEducationId(), lang));
			infoVO.setEmployment(sysParamService.findNameByCode(infoVO.getEmploymentId(), lang));
			infoVO.setOccupation(sysParamService.findNameByCode(infoVO.getOccupationId(), lang));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return infoVO;
	}
	
	/**
	 * 获取独立投资人信息
	 * @author michael
	 * @param memberId	主信息id
	 * */
	public MemberIndividual findIndividualMember(String memberId) {
		String hql = "from MemberIndividual t where t.member.id=? ";
		List<MemberIndividual> list = this.baseDao.find(hql, new String[]{memberId}, false);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获取IFA个人信息
	 * @author michael
	 * @param memberId	主信息id
	 * */
	//@Transactional(readOnly = true)
	public MemberIfa findIfaMember(String memberId) {
		String hql = "from MemberIfa t where t.member.id=? ";
		List<MemberIfa> list = this.baseDao.find(hql, new String[]{memberId}, false);
		if(!list.isEmpty()){
			MemberIfa ifa=(MemberIfa)list.get(0);
			MemberIfaIfafirm ifaIfafirm=ifaManageService.getIfaAndIfafirm(ifa.getId());
			if(null!=ifaIfafirm)
				ifa.setIfafirm(ifaIfafirm.getIfafirm());
			return list.get(0);
		}
		return null;
	}
	
	
	
	/**
	 * 查找国家列表
	 * @author michael
	 * @param keyWord 关键字
	 * @param langCode 语言
	 * @return
	 */
	//@Transactional(readOnly = true)
	public List<GeneralDataVO> findCountryList(String keyWord, String langCode){
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		List<String> params = new ArrayList<String>();

		String hql = "select t.id,"+this.getLangString("t.name", langCode)+" as name, t.isoCode3, t.pinYin from SysCountry t where 1=1 ";
		if (null!=keyWord && !"".equals(keyWord)){
			hql += " and ( t.nameEn like ? or t.nameSc like ? or t.nameTc like ? )";
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		hql += " order by t.orderBy ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setId(StrUtils.getString(objs[0]));
				vo.setName(StrUtils.getString(objs[1]));
				vo.setItemCode(StrUtils.getString(objs[2]));
				vo.setKey(StrUtils.getString(objs[3]));
				list.add(vo);
			}
		}
		
		return list;
	}
	
	/**
	 * 通过id查找国家
	 * @author michael
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public SysCountry findCountryById(String id){
		SysCountry country = (SysCountry) baseDao.get(SysCountry.class, id);
		return country;
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
	 * 保存用户验证信息
	 * @author michael
	 * @param memberId 用户id
	 * @param email 用户email
	 * @param validType 验证信息类型 1:激活,2:取回密码,3:修改密码
	 * @param validCode 验证码
	 * @param expireTime 过期时间
	 * @param emailLog 日志
	 * @return
	 */
	public MemberValidateInfo saveMemberValidateInfo(String memberId, String email, int validType, String validCode, Date expireTime, WebEmailLog emailLog){
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
	 * 保存用户验证信息
	 * @author michael
	 * @param info
	 * @return
	 */
	public MemberValidateInfo saveMemberValidateInfo(MemberValidateInfo info){
		return (MemberValidateInfo) this.baseDao.create(info);
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
		hql += " ORDER BY t.createTime DESC";
		List<MemberValidateInfo> list = this.baseDao.find(hql, params.toArray(), false);

		return list;
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
	
	public boolean checkCertNoUnique(String tableName, String idCard,String certType){
		String hql = "from "+tableName+ " t where t.certType=? and t.certNo=? ";
		List param = new ArrayList();
		param.add(certType);
		param.add(idCard);
		if("".equals(idCard)){
			return true;
		}
		List list = this.baseDao.find(hql, param.toArray(), false);
		if(list.isEmpty()){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 会员激活
	 * */
	public boolean updateMemberActive(String memberId, String valiCode) {
		String hql = "from MemberValidateInfo t where t.member.id=? and t.validateCode=? and t.expireTime>=? and t.validateType=1";
		List params = new ArrayList();
		params.add(memberId);
		params.add(valiCode);
		params.add(new Date());
		List list = this.baseDao.find(hql, params.toArray(), false);
		if(list.isEmpty())return false;
		
		//激活
		String activeHql = "update MemberBase t set t.status='1' where t.id=? ";
		this.baseDao.updateHql(activeHql, new String[]{memberId});
		return true;
	}

	/**
	 * 读取我的待办列表
	 * @author tejay zhu
	 */
	/*public List<WebToDo> findMyWebToDoList(MemberBase loginUser){
		return findMyWebToDoList(loginUser, null, null, "1");
	}*/
	
	/**
	 * 读取我的待办列表
	 * @author tejay zhu
	 * @param loginUser
	 * @param code
	 * @param type
	 * @param status
	 * @return
	 */
	/*public List<WebToDo> findMyWebToDoList(MemberBase loginUser, String code, String type, String status){
		String hql = " from WebToDo t where t.owner.id = ? and t.isValid='1' and t.status = ? ";
		List<String> params = new ArrayList();
		params.add( loginUser.getId() );
		params.add(status);
		if (null!=code && !"".equals(code)){
			hql += " and t.toDoCode=? ";
			params.add(code);
		}
		if (null!=type && !"".equals(type)){
			hql += " and t.toDoType=? ";
			params.add(type);
		}
		hql += " order by t.receiveTime desc";
		List<WebToDo> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}*/
	
	/**
	 * 读取我的待阅列表
	 * @author tejay zhu
	 */
	/*public List<WebToRead> findMyWebToReadList( MemberBase loginUser){
		return findMyWebToReadList(loginUser, null, null, "1");
	}*/
	
	/**
	 * 读取我的待阅列表
	 * @author tejay zhu
	 * @param loginUser
	 * @param code
	 * @param type
	 * @param status
	 * @return
	 */
	/*public List<WebToRead> findMyWebToReadList( MemberBase loginUser, String code, String type, String status){
		String hql = " from WebToRead t where t.owner.id = ? and t.isValid='1' and t.status = ? ";
		List<String> params = new ArrayList();
		params.add( loginUser.getId() );
		params.add(status);
		if (null!=code && !"".equals(code)){
			hql += " and t.toReadCode=? ";
			params.add(code);
		}
		if (null!=type && !"".equals(type)){
			hql += " and t.toReadType=? ";
			params.add(type);
		}
		hql += " order by t.receiveTime desc";
		List<WebToRead> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}*/
	
	/**
	 * 在多语言表读取对应的 待办待阅title
	 * @author tejay zhu
	 */
	public WebToDoToReadVO findWebToDoToRead( String id, String langCode, String toDoToRead ){
		String hql = "select t.id, t.title from "+toDoToRead+this.getLangFirstCharUpper(langCode)+" t where t.id = ? ";
		List<String> params = new ArrayList();
		params.add( id );
		WebToDoToReadVO vo = new WebToDoToReadVO();
		List dataList = this.baseDao.find(hql, params.toArray(), false);
		if ( null != dataList && !dataList.isEmpty() ) {
			Object[] objs = (Object[])dataList.get(0);
			vo.setId(null==objs[0]?"":objs[0].toString());
			vo.setTitle(null==objs[1]?"":objs[1].toString());
			
		} 
		return vo;
		
	}

	/*public WebToDoToReadVO parseWebToDoToWebToDoToReadVO( WebToDo toDo, WebToDoToReadVO vo ){
		vo.setToDoToReadCode( toDo.getToDoCode() );
		vo.setToDoToReadId( toDo.getToDoId() );
		vo.setFromMember( toDo.getFromMember() );
		vo.setOwner( toDo.getOwner() );
		vo.setUrl( toDo.getUrl() );
		vo.setCreateTime( toDo.getCreateTime() );
		vo.setReceiveTime( toDo.getReceiveTime() );
		vo.setStatus( toDo.getStatus() );
		vo.setIsValid( toDo.getIsValid() );
		return vo;
	}*/
	
/*	public WebToDoToReadVO parseWebToReadToWebToDoToReadVO( WebToRead toRead, WebToDoToReadVO vo ){
		vo.setToDoToReadCode( toRead.getToReadCode() );
		vo.setToDoToReadId( toRead.getToReadId() );
		vo.setFromMember( toRead.getFromMember() );
		vo.setOwner( toRead.getOwner() );
		vo.setUrl( toRead.getUrl() );
		vo.setCreateTime( toRead.getCreateTime() );
		vo.setReceiveTime( toRead.getReceiveTime() );
		vo.setStatus( toRead.getStatus() );
		vo.setIsValid( toRead.getIsValid() );
		return vo;
	}*/
	
	/*public WebToRead setToReadDone( String id ){
		WebToRead toRead = (WebToRead)this.baseDao.get(WebToRead.class, id);
		toRead.setStatus("2");
		toRead = (WebToRead)this.baseDao.update(toRead);
		return toRead;
	}*/
	
	/**
	 * 通过角色查找用户列表
	 * @author michael
	 * @param roldId 角色id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public List<MemberBase> findMemberListByRoleId(String roldId){
		List<MemberBase> list = new ArrayList<MemberBase>();
		List<String> params = new ArrayList<String>();

		String hql = "from MemberBase t where 1=1 ";
		hql += " and t.id in ( select r.member.id from MemberConsoleRoleMember r where r.role.id=? )";
		params.add(roldId);

//		hql += " order by t.orderBy ";
		
		list = this.baseDao.find(hql, params.toArray(), false);

		
		return list;
	}
	
	/**
	 * 获取IFA扩展信息
	 * @author michael
	 * @param ifaId	主信息id
	 * */
	public IfaExtInfo findIfaExtInfo(String ifaId){
		String hql = "from IfaExtInfo t where t.ifa.id=? ";
		List<IfaExtInfo> list = this.baseDao.find(hql, new String[]{ifaId}, false);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 增加或者修改一条数据
	 * @param IfaExtInfo ifa扩展信息
	 * @return 
	 */
	public  IfaExtInfo saveOrUpdate(IfaExtInfo extInfo){
		extInfo = (IfaExtInfo)baseDao.saveOrUpdate(extInfo);
		
		//更新历史记录
		IfaHighlight highlight = new IfaHighlight();
		highlight.setIfa(extInfo.getIfa());
		highlight.setTitle(extInfo.getHighlight());
		highlight.setCreateTime(new Date());
		saveOrUpdate(highlight);
		
		return extInfo;
	}
	
	/**
	 * 增加或者修改一条数据
	 * @param IfaHighlight highlight信息
	 * @return 
	 */
	private IfaHighlight saveOrUpdate(IfaHighlight highlight){
		return (IfaHighlight)baseDao.saveOrUpdate(highlight);
	}
	

	/**
	 * 发送邮件(调用邮件服务器发送邮件，并写邮件发送日志）
	 * @author michael
	 * @param moduleType 调用模块
	 * @param sender 发送人
	 * @param receiver 接收人
	 * @param receiverEmail 接收人邮箱
	 * @param subject 邮件标题
	 * @param message 邮件内容
	 * @return emailLog 邮件发送日志，返回null，发送失败
	 */
	public WebEmailLog saveAndSendEmail(String moduleType, MemberBase sender, MemberBase receiver,String receiverEmail, String subject, String message, String relateId){
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
			webEmailLog.setCreatorId(null!=sender?sender.getId():null);
			webEmailLog.setRelateId(relateId);
			webEmailLog = emailService.saveOrUpdate(webEmailLog);
			
			return webEmailLog;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @author scshi_u330p
	 * 登录失败次数累加
	 * param loginCode 登录账号
	 * return Integer 登录失败次数；
	 * */
	@Override
	public MemberBase saveLoginFailPlus(String loginCode) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("from MemberBase t where t.loginCode=? and t.isValid=?");
		params.add(loginCode);
		params.add("1");
		List<MemberBase> list = this.baseDao.find(sb.toString(), params.toArray(), false);
		if(!list.isEmpty()){
			MemberBase currentUser = list.get(0);
			
			Integer failTimes = currentUser.getFailCount()==null?1:currentUser.getFailCount()+1;
			currentUser.setFailCount(failTimes);
			
			if(5==failTimes){//失败5次，锁定账号
				currentUser.setLockStatus("1");
				currentUser.setLockDate(new Date());
			}
			this.baseDao.saveOrUpdate(currentUser);
			return currentUser;
		}
		return null;
	}
	
	/**
	 * @author scshi_u330p
	 * @date 20161208
	 * @param loginCode;
	 * @return String 1--已锁定，0--未锁定
	 * */
	public String loadAccountLockStatus(String loginCode){
		StringBuffer sb = new StringBuffer("from MemberBase t where t.loginCode='"+loginCode+"' and t.isValid='1' ");
		List<MemberBase> list = this.baseDao.find(sb.toString(), null, false);
		if(list.isEmpty())return null;
		MemberBase base = list.get(0);
		if("1".equals(base.getLockStatus())){
			long lockTime = base.getLockDate().getTime();
			long nowTime = new Date().getTime();
			int hours = (int) ((nowTime - lockTime)/(1000 * 60 * 60));  
			if(hours>=3){
				base.setLockDate(null);
				base.setLockStatus("0");
				base.setFailCount(0);
			}
		}
		return base.getLockStatus();
	}
	
	/**
	 * @author scshi_u330p
	 * @date 20161208
	 * @param loginCode;
	 * @return String 1--已锁定，0--未锁定
	 * */
	/*public String loadAccountLockStatus(String loginCode){
		StringBuffer sb = new StringBuffer("from MemberBase t where t.loginCode='"+loginCode+"' and t.isValid='1' ");
		List<MemberBase> list = this.baseDao.find(sb.toString(), null, false);
		if(list.isEmpty())return null;
		MemberBase base = list.get(0);
		if("1".equals(base.getLockStatus())){
			long lockTime = base.getLockDate().getTime();
			long nowTime = new Date().getTime();
			int hours = (int) ((nowTime - lockTime)/(1000 * 60 * 60));  
			if(hours>=3){
				base.setLockDate(null);
				base.setLockStatus("0");
				base.setFailCount(0);
			}
		}
		return base.getLockStatus();
	}*/
	

	/**
	 * 保存用户和所在运营公司的关系
	 * @author Michael
	 * @param memberCompany 关系信息
	 * @return 
	 */
	public MemberCompany saveMemberCompany(MemberCompany memberCompany){
		StringBuffer sb = new StringBuffer("from MemberCompany t where t.member.id='"+memberCompany.getMember().getId()+"' and t.company.id='"+memberCompany.getCompany().getId()+"' ");
		List<MemberCompany> list = this.baseDao.find(sb.toString(), null, false);
		if (null==list || list.isEmpty()){
			memberCompany = (MemberCompany)baseDao.saveOrUpdate(memberCompany);
		}else{
			memberCompany =  list.get(0);
		}
		return memberCompany;
	}

	/**
	 * 添加用户的im信息
	 * @author mqzou 2017-02-07
	 * @return
	 */
	@Override
	public int addMemberImInfo() {
		int count=0;
		StringBuilder hql=new StringBuilder();
		hql.append(" from MemberBase r where r.isValid='1' and (r.imUserId=null or r.imUserId='')");
		List list=this.baseDao.find(hql.toString(), null, false);
		if(null!=list && !list.isEmpty()){
			Iterator it=list.iterator();
			while (it.hasNext()) {
				MemberBase memberBase = (MemberBase) it.next();
				memberBase.setImNickName(memberBase.getNickName());
				memberBase.setImNickUrl("");
				memberBase.setImUserId(UUID.randomUUID().toString());
				memberBase.setImUserPwd(memberBase.getImUserId());
				this.baseDao.saveOrUpdate(memberBase);
				count++;
			}
		}
		return count;
	}


	/**
	 * 添加用户信息到Im
	 * @author mqzou 2017-02-07
	 * @param member
	 * @param isAdd 是否新增
	 * @return
	 */
	@Override
	public boolean saveMemberToIm(MemberBase member,boolean isAdd) {
		if(null!=member){
			try {
				String gender="";
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==member.getSubMemberType()){
					MemberIfa ifa=findIfaMember(member.getId());
					if(null!=ifa)
					  gender=ifa.getGender();
				}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==member.getSubMemberType()) {
					MemberIndividual individual=findIndividualMember(member.getId());
					if(null!=individual)
						gender=individual.getGender();
				}
				if(isAdd)
					ImTbUtil.addOpenImUser(member.getImUserId(), member.getImUserPwd(), gender, member.getImNickName(), member.getImNickUrl());
				else {
					ImTbUtil.updateOpenImUser(member.getImUserId(), member.getImUserPwd(), gender, member.getImNickName(), member.getImNickUrl());
				}
				return true;
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		return false;
	}


	/**
	 * 根据IM userId 获取用户实体
	 * @author mqzou 2017-02-13
	 * @param userId
	 * @return
	 */
	@Override
	public MemberBase findByImUserId(String userId) {
		String hql=" from MemberBase r where r.imUserId=? and r.isValid='1'";
		List<Object> params=new ArrayList<Object>();
		params.add(userId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			MemberBase member=(MemberBase)list.get(0);
			return member;
		}
		return null;
	}

	/**
	 * 获取登录sso信息表
	 * @author wwluo 2017-03-13
	 * @param loginCode
	 * @return
	 */
	@Override
	public MemberSso getMemberSsoByLoginCode(String loginCode) {
		MemberSso memberSso = null;
		if (StringUtils.isNotBlank(loginCode)) {
			StringBuffer hql = new StringBuffer(" FROM MemberSso s WHERE s.loginCode=?");
			List<Object> params = new ArrayList<Object>();
			params.add(loginCode);
			List<MemberSso> memberSsos = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(memberSsos != null && !memberSsos.isEmpty()){
				memberSso = memberSsos.get(0);
			}
		}
		return memberSso;
	}
	
	
}
