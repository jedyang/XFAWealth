/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.entity.SysCountry;
import com.fsll.wmes.entity.IfaExtInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MemberSso;
import com.fsll.wmes.entity.MemberValidateInfo;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.member.vo.InfoVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.member.vo.PersonalInfoVO;
import com.fsll.wmes.member.vo.WebToDoToReadVO;
/***
 * 业务接口类：统一帐号管理
 * @author mjhuang
 * @date 2016-6-20
 */
public interface MemberBaseService {
	/**
	 * 增加或者修改一条数据
	 * @param memberBase 帐号信息
	 * @return 
	 */
	public  MemberBase saveOrUpdate(MemberBase memberBase);

	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids);
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public MemberBase findById(String id);
	
    /***
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param memberBase 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonPaging,MemberBase memberBase);
	
	/**
	 * 通过用户用户帐号查看用户
	 * @param loginCode 用户帐号
	 * @param password 用户密码
	 * @return
	 */
	public MemberBase findByCode(String loginCode,String password);
	
	/**
	 * 通过账户类型获取列表
	 * @param memberType
	 * @return
	 */
	public List<MemberBase> findListByMemberType(int memberType);
	
	/**
	 * 通过区号和手机号码查找用户（暂用作手机区号+号码的唯一性检查）
	 * @param mobileCode
	 * @param mobileNumber
	 * @return
	 */
	public List<MemberBase> findByMobileCodeAndNumber(String mobileCode, String mobileNumber);
	
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
	public MemberSsoVO saveLoginAuth(HttpServletRequest request,String loginCode,String password,String fromType);
	
	/**
	 *校验token
	 *@author mjhuang
	 *@param tokenId 要校验的token
	 *@param fromType 来源：web,ios,android
	 *@return true有效 false无效
	 */
	public boolean saveCheckSSOToken(String tokenId,String fromType);
	
	/**
	 *校验当前ip是否存在token
	 *@author mjhuang
	 *@param fromIp 来源 机器ip
	 *@param fromType 来源：web,ios,android
	 *@return true存在 false不存在
	 */
	public boolean checkSSOExist(String fromIp,String fromType);
	
	/**
	 *校验帐号是否在其他机器有登录
	 *@author mjhuang
	 *@param loginCode 要检查来源的ip
	 *@param fromIp 来源 机器ip
	 *@param fromType 来源：web,ios,android
	 *@return true 在其他机器登录 false没有
	 */
	public boolean checkOtherSSOExist(String loginCode,String fromIp,String fromType);
	
	/***************  登录sso相关方法　end  **********************/
	
	
	
	
	/**
	 * 检查帐号的唯一性
	 * @param loginCode 用户帐号
	 * @param accountId 用户id
	 * @return
	 */
	public MemberBase checkCodeUnique(String loginCode, String rId);

	
	/**
	 *	判断数据项重复接口
	 *@author scshi
	 *@param items	判断字段json
	 *@return InfoVO 	fieldName对应传入jason参数的key值;duplicate 0：不重复，1：重复
	 * */
	public InfoVO checkDuplicate(String items);

	/**
	 *	判断数据项重复接口
	 *@author scshi
	 *@param tableName
	 *@param items
	 *@return InfoVO fieldName对应传入json参数的key值;duplicate 0：不重复，1：重复
	 * */
	//@Transactional(readOnly=true)
	public InfoVO checkDuplicate(String tableName, String items);
	
	/**
	 * 创建会员接口
	 * @author scshi
	 * @param accountDataJSON
	 * @param memberDataJSON
	 * @return String 会员id
	 * */
	public String createMember(JSONObject accountDataJSON, JSONObject memberDataJSON, String langCode);
	
//	/**
//	 * @author scshi
//	 * @date 20160627
//	 * 3.4.3 发送激活邮件接口
//	 * @param loginID 登录账号
//	 * @param email	邮件接受地址
//	 * @param activeCode 激活码，=Encrypt.md5(email+pwd+time)
//	 * @return 结果代码
//	 * */
//	public ResultWS sendRegEmail(String loginID, String email, String activeCode, String path);

//	/**
//	 * @author michael
//	 * @date 20160712
//	 * 发送重置密码邮件接口
//	 * @param email	邮件接受地址
//	 * @param activeCode 随机6位字符串（字母数字）
//	 * @return 结果代码
//	 * */
//	public ResultWS sendResetPasswordEmail(String email, String validateCode);
	
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
	public ResultWS saveAndSendValidateEmail(String loginCode, String email, int validateType, String validateCode, String path);
	
	/***
	 * 公司注册唯一验证
	 * */
	public InfoVO checkCompanyUnique(String itemsJSON);

	public InfoVO checkMobileUnique(String mobileCode, String mobileNumber);

	public List<SysCountry> loadSysCountryList();

	/**
	 * 通过ID查找个人信息的完整数据
	 * @param id
	 * @param lang 语言编码 
	 * @return
	 */
	public PersonalInfoVO findPersonalInfoById(String id, String lang);
	
	/**
	 * 查找国家列表
	 * @param keyWord 关键字
	 * @param langCode 语言
	 * @return
	 */
	public List<GeneralDataVO> findCountryList(String keyWord, String langCode);
	
	/**
	 * 通过id查找国家
	 * @param id
	 * @return
	 */
	public SysCountry findCountryById(String id);
	
	/**
	 * 通过用户邮箱查找用户
	 * @param email 用户帐号
	 * @return
	 */
	public MemberBase findByEmail(String email);

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
	public MemberValidateInfo saveMemberValidateInfo(String memberId, String email, int validType, String validCode, Date expireTime, WebEmailLog emailLog);
	
	/**
	 * 保存用户验证信息
	 * @author michael
	 * @param info
	 * @return
	 */
	public MemberValidateInfo saveMemberValidateInfo(MemberValidateInfo info);
	
	/**
	 * 获取个人验证信息
	 * @author michael
	 * @param memberId	用户id
	 * @param email 用户email
	 * @param validType 验证信息类型 1:激活,2:取回密码,3:修改密码
	 * @return list
	 * */
	public List<MemberValidateInfo> findMemberValidateInfo(String memberId, String email, int validType);
	
	/**
	 * 获取个人验证信息
	 * @author michael
	 * @param memberId	用户id
	 * @param email 用户email
	 * @param validType 验证信息类型 1:激活,2:取回密码,3:修改密码
	 * @return int
	 * */
	public int deleteMemberValidateInfo(String memberId, String email, int validType);

	/**
	 * 获取独立投资人信息
	 * @author michael
	 * @param memberId	主信息id
	 * */
	public MemberIndividual findIndividualMember(String memberId);
	
	/**
	 * 基本信息护照/身份证唯一验证
	 * @param tableName  
	 * @param idCard 
	 * @param certType 
	 * */
	public boolean checkCertNoUnique(String tableName, String idCard,String certType);
	
	/**
	 * 会员激活
	 * */
	public boolean updateMemberActive(String memberId, String valiCode);
	
/*	public List<WebToDo> findMyWebToDoList( MemberBase loginUser );
	*//**
	 * 读取我的待办列表
	 * @author tejay zhu
	 * @param loginUser
	 * @param code
	 * @param type
	 * @param status
	 * @return
	 *//*
	public List<WebToDo> findMyWebToDoList(MemberBase loginUser, String code, String type, String status);
	
	public List<WebToRead> findMyWebToReadList( MemberBase loginUser );*/
	/**
	 * 读取我的待阅列表
	 * @author tejay zhu
	 * @param loginUser
	 * @param code
	 * @param type
	 * @param status
	 * @return
	 */
	//public List<WebToRead> findMyWebToReadList( MemberBase loginUser, String code, String type, String status);
	
	public WebToDoToReadVO findWebToDoToRead( String id, String langCode, String toDoToRead );
	//public WebToDoToReadVO parseWebToDoToWebToDoToReadVO( WebToDo toDo, WebToDoToReadVO vo );
	//public WebToDoToReadVO parseWebToReadToWebToDoToReadVO( WebToRead toRead, WebToDoToReadVO vo );
	//public WebToRead setToReadDone( String id );
	
	/**
	 * 通过角色查找用户列表
	 * @author michael
	 * @param roldId 角色id
	 * @return
	 */
	public List<MemberBase> findMemberListByRoleId(String roldId);
	
	/**
	 * 获取IFA个人信息
	 * @author michael
	 * @param memberId	主信息id
	 * */
	public MemberIfa findIfaMember(String memberId);
	
	/**
	 * 获取IFA扩展信息
	 * @author michael
	 * @param ifaId	主信息id
	 * */
	public IfaExtInfo findIfaExtInfo(String ifaId);
	
	/**
	 * 增加或者修改一条数据
	 * @param IfaExtInfo ifa扩展信息
	 * @return 
	 */
	public  IfaExtInfo saveOrUpdate(IfaExtInfo extInfo);
	
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
	public WebEmailLog saveAndSendEmail(String moduleType, MemberBase sender, MemberBase receiver,String receiverEmail, String subject, String message, String relateId);

	/**
	 * 获取注册成功信息
	 * @param loginId
	 * @param email
	 * @param langCode
	 * @return
	 */
	public String getCompletedMessage(String loginId, String email, String langCode);

	/**
	 * @author scshi_u330p
	 * @date 20161208
	 * 登录失败次数累加
	 * param loginCode 登录账号
	 * return MemberBase 當前登錄人
	 * */
	public MemberBase saveLoginFailPlus(String loginCode);

	/**
	 * @author scshi_u330p
	 * @date 20161208
	 * @param loginCode;
	 * @return String 1--已锁定，0--未锁定
	 * */
	public String loadAccountLockStatus(String loginCode);
	
	/**
	 * 通过用户用户帐号查看用户
	 * @param loginCode 用户帐号
	 * @return
	 */
	//@Transactional(readOnly = true)
	public MemberBase findByCode(String loginCode);
	
	/**
	 * 判断登陆账号是否存在token
	 * @author wwluo
	 * @param fromIp 来源 机器ip
	 * @param fromType 来源：web,ios,android
	 */
	public boolean checkSSOyLoginCode(String fromIp,String fromType,String loginCode);
	
	/**
	 * 删除sso信息
	 * @author wwluo
	 * @param loginCode 登陆账号
	 * @param fromIp IP
	 * @param fromType 来源：web,ios,android
	 */
	public void delMemberSso(String loginCode,String fromIp,String fromType);
	
	/**
	 * 保存用户和所在运营公司的关系
	 * @author Michael
	 * @param memberCompany 关系信息
	 * @return 
	 */
	public MemberCompany saveMemberCompany(MemberCompany memberCompany);
	
	/**
	 * 添加用户的im信息
	 * @author mqzou 2017-02-07
	 * @return
	 */
	public int addMemberImInfo();
	
	/**
	 * 添加用户信息到Im
	 * @author mqzou 2017-02-07
	 * @param member
	 * @param isAdd 是否新增
	 * @return
	 */
	public boolean saveMemberToIm(MemberBase member,boolean isAdd);
	 
	/**
	 * 根据IM userId 获取用户实体
	 * @author mqzou 2017-02-13
	 * @param userId
	 * @return
	 */
	public MemberBase findByImUserId(String userId);

	/**
	 * 获取登录sso信息表
	 * @author wwluo 2017-03-13
	 * @param loginCode
	 * @return
	 */
	public MemberSso getMemberSsoByLoginCode(String loginCode);
}
