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
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MemberValidateInfo;
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
     * @param jsonpaging 分页参数
     * @param memberBase 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonpaging,MemberBase memberBase);
	
	/**
	 * 通过memberType查询所有，如需查询全部，memberType=0
	 * @return list
	 */
	public List<MemberBase> findAllMember(Integer memberType);
	
	/**
	 * 通过用户用户帐号查看用户
	 * @param loginCode 用户帐号
	 * @param password 用户密码
	 * @return
	 */
	public MemberBase findByCode(String loginCode,String password);
	
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
	public MemberSsoVO saveLoginAuth(HttpServletRequest request,String loginCode,String password,String fromType,String isConsole);
	
	/**
	 * 校验token有没有过期
	 * @param tokenId 
	 * @return
	 */
	public boolean validTokenId(String tokenId);
	
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
	public String createMember(JSONObject accountDataJSON, JSONObject memberDataJSON);
	
	/**
	 * @author scshi
	 * @date 20160627
	 * 3.4.3 发送激活邮件接口
	 * @param loginID 登录账号
	 * @param email	邮件接受地址
	 * @param activeCode 激活码，=Encrypt.md5(email+pwd+time)
	 * @return 结果代码
	 * */
	public ResultWS sendRegEmail(String loginID, String email, String activeCode);

	/**
	 * @author michael
	 * @date 20160712
	 * 发送重置密码邮件接口
	 * @param email	邮件接受地址
	 * @param activeCode 随机6位字符串（字母数字）
	 * @return 结果代码
	 * */
	public ResultWS sendResetPasswordEmail(String email, String validateCode);
	
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
	public ResultWS sendValidateEmail(String loginCode, String email, int validateType, String validateCode);
	
	/***
	 * 公司注册唯一验证
	 * */
	public InfoVO checkCompanyUnique(String itemsJSON);

	public InfoVO checkMobileUnique(String mobileCode, String mobileNumber);

	/**
	 * 通过ID查找个人信息的完整数据
	 * @param id
	 * @param lang 语言编码 
	 * @return
	 */
	public PersonalInfoVO findPersonalInfoById(String id, String lang);
	
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
	 * @return
	 */
	public MemberValidateInfo saveMemberValidateInfo(String memberId, String email, int validType, String validCode, Date expireTime);
	
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
	public boolean certNoUniqueBasic(String tableName, String idCard,String certType);
	
	/**
	 * 会员激活
	 * */
	public boolean memActive(String memberId, String valiCode);
	
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
	 * 设备注册,保存设备的Token
	 * @param token 设备token值
	 * @param deviceId 设备ID
	 * @param appType 平台类型 1:android   2:ios 
	 */
	public void setDeviceToken(String token,String deviceId,String appType);
	/**
	 * 保存设备的Token与帐号的关联
	 * @param token 设备token值
	 * @param memberId 会员ID
	 */
	public void setTokenAccount(String token,String memberId,String type,String appType);
	
	/**
	 * 通过NickName获取MemberBase
	 * @param nickName
	 */
	public List<MemberBase> findByNickName(String nickName);
}
