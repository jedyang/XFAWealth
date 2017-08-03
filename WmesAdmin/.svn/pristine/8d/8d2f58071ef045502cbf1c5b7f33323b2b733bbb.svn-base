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
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.security.PwdEncoder;
import com.fsll.common.util.DESUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.entity.SysCountry;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.IfaExtInfo;
import com.fsll.wmes.entity.IfaHighlight;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MemberRegTmp;
import com.fsll.wmes.entity.MemberSso;
import com.fsll.wmes.entity.MemberValidateInfo;
import com.fsll.wmes.entity.WebAppToken;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.InfoVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.member.vo.PersonalInfoVO;
import com.fsll.wmes.member.vo.WebToDoToReadVO;
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
		if(StringUtils.isBlank(id)){
			return null;
		}
		MemberBase memberBase = (MemberBase) baseDao.get(MemberBase.class, id);
		return findDetail(memberBase);
	}
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param memberBase 查询参数
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging,MemberBase memberBase) {
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
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
		Integer index = 0;
    	while(it.hasNext()){
			index++;
			MemberBase obj = (MemberBase)it.next();
			obj = findDetail(obj);
			obj.setXh(Integer.toString(indexNumber+index));
			list.add(obj);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
	}
	
	/**
	 * 通过memberType查询所有，如需查询全部，memberType=0
	 * @return list
	 */
	//@Transactional(readOnly=true)
	@Override
	public List<MemberBase> findAllMember(Integer memberType){
		String hql=" FROM MemberBase r WHERE r.isValid='1' ";
		List<Object> params = new ArrayList<Object>();
		if(memberType>0){
			hql += " AND r.memberType=? ";
			params.add(memberType);
		}
		List<MemberBase> list=this.baseDao.find(hql, params.toArray(), false);
		return list;
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
	 * sso登录验证
	 * 
	 * @param request 请求
	 * @param loginCode 登录帐号
	 * @param password 密码
	 * @param fromType 来源
	 * @param isConsole 针对web有效,来源于前台0还是控制台1
	 * @return
	 */
	public MemberSsoVO saveLoginAuth(HttpServletRequest request,String loginCode,String password,String fromType,String isConsole) {
		List<Object> params = new ArrayList<Object>();
		String hql = "from MemberBase r where r.isValid='1' and (r.loginCode = ? or r.loginCode = ?) and r.password = ? ";
		params.add(loginCode.toUpperCase());
		params.add(loginCode.toLowerCase());
		params.add(password);
		MemberBase memberBase = null;
		List<MemberBase> mList = this.baseDao.find(hql, params.toArray(), false);
		if (!mList.isEmpty()) {
			memberBase = mList.get(0);	
			
			Date curDate = new Date();//当前时间
			
			//向数据库写sso的信息
			String dataSecrefKey = UUID.randomUUID().toString().replace("-", "");
			String tokenId = DESUtils.encrypt(dataSecrefKey,CommonConstants.SECRET_KEY);					
			MemberSso memberSso = new MemberSso();
			memberSso.setMember(memberBase);
			memberSso.setFromType(fromType);
			memberSso.setSecureKey(dataSecrefKey);
			memberSso.setTokenId(tokenId);
			memberSso.setIp(this.getRemoteHost(request));
			memberSso.setRemark("sso login");
			memberSso.setCreateTime(curDate);
			memberSso.setExpireTime(DateUtil.getInternalDateByMin(curDate,CommonConstants.TOKEN_TIME_OUT));

			//返回登录信息
			MemberSsoVO ssoVO = new MemberSsoVO();
			try{
				BeanUtils.copyProperties(ssoVO,memberBase);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ssoVO.setTokenId(tokenId);
			ssoVO.setTokenExpireTime(DateUtil.dateToDateString(memberSso.getExpireTime(), "yyyy-MM-dd HH:mm:ss"));
			ssoVO.setLastLoginTime(memberBase.getLoginTime());//last login time
			
			//向帐号表更新登录信息
			memberBase.setLastLoginIp(this.getRemoteHost(request));
			memberBase.setLoginTime(curDate);
			if(memberBase.getLoginCount() != null){
				memberBase.setLoginCount(memberBase.getLoginCount()+1);
			}else{
				memberBase.setLoginCount(1);
			}
			
			//保存sso之前先删除原来的sso信息
			String hqlDel = "delete from MemberSso r where r.member.id='"+memberSso.getMember().getId()+"'";
			if("guest".equals(memberBase.getId())){//如果是游客,则需要再加ip这个删除条件
				hqlDel += " and r.ip='"+memberSso.getIp()+"'";
			}
			
			if("ios".equals(fromType) || "android".equals(fromType)){//来源于手机端 
				if("guest".equals(memberBase.getId()) || (memberBase.getMemberType() != null && memberBase.getMemberType() == 1)){//投资人或者游客
					//保存sso之前先删除原来的sso信息
					this.baseDao.updateHql(hqlDel,null);
					this.baseDao.create(memberSso);//保存sso
					this.baseDao.update(memberBase);//保存登录信息
					return ssoVO;
				}
			}else if("web".equals(fromType)){//来源于web
				if("0".equals(isConsole)){//来源前台,游客/Investor/ifa能用
					if("guest".equals(memberBase.getId()) || 
					   (null !=memberBase.getMemberType() 
							   && (1==memberBase.getMemberType() || 21==memberBase.getSubMemberType())	
						)
					){
						//保存sso之前先删除原来的sso信息
						this.baseDao.updateHql(hqlDel,null);
						this.baseDao.create(memberSso);//保存sso
						this.baseDao.update(memberBase);//保存登录信息
						return ssoVO;
					}
				}else if("1".equals(isConsole) && null != memberBase.getMemberType() &&
						(0==memberBase.getMemberType()
					  || 22==memberBase.getSubMemberType()
					  || 31==memberBase.getSubMemberType())){//来源来控制台,只有平台管理员/ifafirm/distributor能进控制台

			        //保存sso之前先删除原来的sso信息
			        this.baseDao.updateHql(hqlDel,null);
			        this.baseDao.create(memberSso);//保存sso
					this.baseDao.update(memberBase);//保存登录信息
					return ssoVO;

				}
			}
		}
		return null;
	}
	
	/**
	 * 校验token有没有过期
	 * @param memberId 
	 * @param tokenId 
	 * @return
	 */
	//@Transactional(readOnly = true)
	public boolean validTokenId(String tokenId) {
		List<Object> params = new ArrayList<Object>();
		String hql = "from MemberSso r where r.tokenId=?";
		params.add(tokenId);
		List<MemberSso> mssoList = this.baseDao.find(hql, params.toArray(), false);
		if (!mssoList.isEmpty()) {
			MemberSso msso = mssoList.get(0);
			if("guest".equals(msso.getMember().getId())){//游客的token永远有效
				return true;
			}else{
				if (msso.getExpireTime().getTime() >= new Date().getTime()) {
					return true;
				}
			}
		}
		return false;
	}
	
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
	public String createMember(JSONObject accountDataJSON, JSONObject memberDataJSON) {
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
			String primaryEsidence = memberDataJSON.getString("primaryEsidence");
			if(null==primaryEsidence || "".equals(primaryEsidence)){
				memIfa.setPrimaryResidence(null);
			}else{
				SysCountry pe = (SysCountry)this.baseDao.get(SysCountry.class, primaryEsidence);
				memIfa.setPrimaryResidence(pe);
			}
			memIfa.setTelephone(memberDataJSON.optString("mobile",""));
			memIfa.setIntroduction(memberDataJSON.getString("introduction")==null?"":memberDataJSON.getString("introduction"));
			memIfa.setCreateTime(new Date());
			memIfa.setLastUpdate(memIfa.getCreateTime());

			this.baseDao.create(memIfa);
		}
		
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
	public ResultWS sendValidateEmail(String loginCode, String email, int validateType, String validateCode) {
		ResultWS result = new ResultWS();
		result.setRet(WSConstants.SUCCESS);
		result.setData(null);
		
		MemberBase user = findByCode(loginCode);//通过登录账号查找有效的用户 isValid=1
		if (null==user && null!=email && !"".equals(email))
			user = findByEmail(email);//通过邮箱查找有效的用户 isValid=1
		
		if (null!=user){
			//检测用户状态
			if (null!=user.getStatus() && 
					(("1".equals(user.getStatus()) &&  2==validateType)||("0".equals(user.getStatus()) &&  1==validateType))
					){
				//@@发送邮件，提供临时密码
//				String activeCode = StrUtils.getRandomString(6);//获取6位随机码
				try {
					//临时密码不用加密
					//1:激活,2:取回密码,3:修改密码
					saveMemberValidateInfo(user.getId(), email, validateType, validateCode, DateUtil.getCurDate(Calendar.DATE, 1));
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
		MemberIndividual individual = findIndividualMember(id);
		MemberIfa ifa = findIfaMember(id);
		try {
			BeanUtils.copyProperties(infoVO.getBaseInfo(), memberBase);
			if (individual!=null){
				BeanUtils.copyProperties(infoVO.getIndividualPerson(), individual);
			}
			if (ifa!=null){
				BeanUtils.copyProperties(infoVO.getIfaPerson(), ifa);
				infoVO.setIntroduction(ifa.getIntroduction());
				IfaExtInfo extInfo = findIfaExtInfo(ifa.getId());
				BeanUtils.copyProperties(infoVO.getIfaExtInfo(), extInfo);
				infoVO.setHighlight(extInfo.getHighlight());
			}
			infoVO.setInfos(lang);
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
			return list.get(0);
		}
		return null;
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
	 * @return
	 */
	public MemberValidateInfo saveMemberValidateInfo(String memberId, String email, int validType, String validCode, Date expireTime){
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
		info.setCreateTime(new Date());
		
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
	
	public boolean certNoUniqueBasic(String tableName, String idCard,String certType){
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
	public boolean memActive(String memberId, String valiCode) {
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
		hql += " and t.id in ( select r.member.id from SysRoleMember r where r.role.id=? )";
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
	 * @param token 设备token值
	 * @param deviceId 设备ID
	 * @param appType 平台类型 1:android   2:ios 
	 */
	public void setDeviceToken(String token,String deviceId,String appType){
		String hql = "from WebAppToken t where t.token=? ";
		List params = new ArrayList();
		params.add(token);
		List<WebAppToken> list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			WebAppToken obj = list.get(0);
			obj.setUpdateTime(new Date());
			baseDao.saveOrUpdate(obj, false);
		}else{
			WebAppToken webAppToken = new WebAppToken();
			webAppToken.setAppType(appType);
			webAppToken.setToken(token);
			webAppToken.setDeviceId(deviceId);
			webAppToken.setCreateTime(new Date());
			baseDao.create(webAppToken);
		}
	}
	/**
	 * 保存设备的Token与帐号的关联
	 * @param token 设备token值
	 * @param memberId 会员ID
	 * @param type 操作类型 0：保存帐号关联，1：去掉帐号关联
	 */
	public void setTokenAccount(String token,String memberId,String type,String appType){
		String hql = "from WebAppToken t where t.token=? ";
		List params = new ArrayList();
		params.add(token);
		List<WebAppToken> list = this.baseDao.find(hql, params.toArray(), false);
		MemberBase member=new MemberBase();
		member.setId(memberId);
		if(null!=list && !list.isEmpty()){
			WebAppToken obj = list.get(0);
			if("0".equals(type)){
				obj.setMember(member);
			}else{
				obj.setMember(null);
			}
			obj.setUpdateTime(new Date());
			baseDao.saveOrUpdate(obj, false);
		}else{
			if("0".equals(type)){
				WebAppToken webAppToken = new WebAppToken();
				webAppToken.setAppType(appType);
				webAppToken.setToken(token);
				webAppToken.setMember(member);
				webAppToken.setCreateTime(new Date());
				webAppToken.setUpdateTime(new Date());
				baseDao.create(webAppToken);
			}
		}
	}

	@Override
	public List<MemberBase> findByNickName(String nickName) {
		List<Object> params = new ArrayList<Object>();
		String hql = " FROM MemberBase r ";
		 hql += " WHERE r.isValid='1' ";
		if(!"".equals(nickName) && null!=nickName){
			hql += " AND r.nickName LIKE ? ";
			params.add("%"+nickName+"%");
		}
		List<MemberBase> members = this.baseDao.find(hql, params.toArray(), false);
		return members;
	}
}
