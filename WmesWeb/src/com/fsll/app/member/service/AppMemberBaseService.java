/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.app.member.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.fsll.app.member.vo.AppInfoVO;
import com.fsll.app.member.vo.AppMemberBaseVO;
import com.fsll.app.member.vo.AppMemberSsoVO;
import com.fsll.core.ResultWS;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberValidateInfo;
/***
 * 业务接口类：统一帐号管理
 * @author mjhuang
 * @date 2016-6-20
 */
public interface AppMemberBaseService {
	/**
	 *	判断数据项重复接口
	 *@author scshi
	 *@param tableName
	 *@param items
	 *@return InfoVO fieldName对应传入json参数的key值;duplicate 0：不重复，1：重复
	 * */
	//@Transactional(readOnly=true)
	public AppInfoVO checkDuplicate(String tableName, String items);
	
	/**
	 * 创建会员接口
	 * @author scshi
	 * @param accountDataJSON
	 * @param memberDataJSON
	 * @return String 会员id
	 * */
	public String createMember(JSONObject accountDataJSON, JSONObject memberDataJSON,String companyInfoCode);
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
	 * 增加或者修改一条数据
	 * @param memberBase 帐号信息
	 * @return 
	 */
	public  MemberBase saveOrUpdate(MemberBase memberBase);

	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public MemberBase findById(String id);
	/**
	 * 通过用户邮箱查找用户
	 * @param email 用户帐号
	 * @return
	 */
	public MemberBase findByEmail(String email);
	
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
	 * @author michael
	 * @date 20160712
	 * 发送重置密码邮件接口
	 * @param email	邮件接受地址
	 * @param activeCode 随机6位字符串（字母数字）
	 * @return 结果代码
	 * */
	public ResultWS sendResetPasswordEmail(String email, String validateCode);
	
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
	public AppMemberSsoVO saveLoginAuth(HttpServletRequest request,String loginCode,String password,String fromType);
	
	/**
	 *校验token
	 *@author mjhuang
	 *@param tokenId 要校验的token
	 *@param fromType 来源：web,ios,android
	 *@return 0 参数错误  1成功 2令牌失效 3令牌不存在
	 */
	public String saveCheckSSOToken(String tokenId,String fromType);
	
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
	
	/**
	 * 检查指定用户loginCode是否存在
	 * @param loginCode
	 * @return
	 */
	public MemberBase checkIfExistsUserByLoginCode(String loginCode);
	
	/**
	 * 检查指定用户loginCode是否存在运营公司
	 * @param loginCode
	 * @return
	 */
	public MemberCompany checkIfValidUserByLoginCode(String loginCode);
	
	/***************  登录sso相关方法　end  **********************/
	
	/**
	 * app启动时用的方法     获得系统中是否存在指定的token
	 * @param appType 平台类型 1:android   2:ios 
	 * @param deviceId 设备ID
	 * @param token token 设备token值
	 * @param aliasType 帐号
	 * @param alias 帐号所在组
	 */
	public void saveDeviceToken(String appType,String deviceId,String token);
	
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
	public void saveTokenAccount(String memberId,String type,String appType,String deviceId,String token,String aliasType,String alias);
	
	/**
	 * 设置——个人信息
	 * @author zxtan
	 * @date 2017-02-20
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public AppMemberBaseVO findMemberBase(String memberId,String langCode);
}
