package com.fsll.app.member.ws;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.member.service.AppMemberBaseService;
import com.fsll.app.member.vo.AppMemberSsoVO;
import com.fsll.common.CommonConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;

/***
 * sso认证接口
 * @author mjhuang
 * @date 2016-6-20
 */
@RestController
@RequestMapping("/service/sso")
public class AppMemberSSORest extends WmesBaseRest{
	
	@Autowired
	private AppMemberBaseService memberBaseService;
	
	/**
	 * 登录认证接口
	 * 地址：service/sso/loginAuth.r
	 * {"loginCode":"tst001","loginPwd":"123456","fromType":"android"}
	 */
	@RequestMapping(value = "/loginAuth")
	@ResponseBody
	public ResultWS loginAuth(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		if(body == null || "".equals(body) || "{}".equals(body)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		String loginCode = jsonObject.optString("loginCode","");
		String loginPwd = jsonObject.optString("loginPwd","");
		String loginType = jsonObject.optString("loginType", "");//1 investor ,2 ifa
		//来源web,ios,android
		String fromType = jsonObject.optString("fromType","");
		String companyCode = jsonObject.optString("companyCode","");
		
		//String fromIp = this.getRemoteHost(request);
		if("".equals(loginCode)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“loginCode”"+WSConstants.MSG1002);
			return result;
		}
		if("".equals(loginPwd)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“loginPwd”"+WSConstants.MSG1002);
			return result;
		}
		if("".equals(loginType)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“loginType”"+WSConstants.MSG1002);
			return result;
		}
		if("".equals(fromType)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“fromType”"+WSConstants.MSG1002);
			return result;
		}
		if("".equals(companyCode)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“companyCode”"+WSConstants.MSG1002);
			return result;
		}
		
		MemberBase memberBase = memberBaseService.checkIfExistsUserByLoginCode(loginCode);
		if (memberBase == null){
//			result = new ResultWS();
			result.setRet(WSConstants.FAIL);
//			result.setData(null);
			result.setErrorCode(WSConstants.CODE1008);
			result.setErrorMsg("“loginCode”"+WSConstants.MSG1008);
			return result;
		}else{
			Integer memberType = memberBase.getMemberType();
			if(!loginType.equals(memberType.toString())){
//				result = new ResultWS();
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1016);
				result.setErrorMsg("“loginType”"+WSConstants.MSG1016);
				return result;
			}
		}
		
		MemberCompany memberCompany = memberBaseService.checkIfValidUserByLoginCode(loginCode);
		if (memberCompany == null || (memberCompany != null && !companyCode.equals(memberCompany.getCompany().getCode()))){
//			result = new ResultWS();
			result.setRet(WSConstants.FAIL);
//			result.setData(null);
			result.setErrorCode(WSConstants.CODE1015);
			result.setErrorMsg("“loginCode”"+WSConstants.MSG1015);
			return result;
		}
		
//		if(WSConstants.FAIL.equals(result.getRet())){
//			return result;
//		}
		
		if("ios".equals(fromType) || "android".equals(fromType)){//来源于手机端 
			String md5password = this.pwdEncoder.encodePassword(loginPwd);
			AppMemberSsoVO ssoVO = memberBaseService.saveLoginAuth(request,loginCode,md5password,fromType);
			result.setRet(ssoVO.getRet());
			result.setErrorCode(ssoVO.getErrorCode());
			result.setErrorMsg(ssoVO.getErrorMsg());
			result.setData(ssoVO);
			return result;
		}else{
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“fromType”"+WSConstants.MSG1002);
		}
		return result;
	}
	
	/**
	 * 设备注册,保存设备的Token
	 * 调用示例:[地址]/service/sso/setDeviceToken.r
	 * {"token":"1679091C5A880FAF6FB5E6087EB1B2DC","deviceId":"JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh","appType":"1"}
	 */
	@RequestMapping(value = "/setDeviceToken")
	@ResponseBody
	public ResultWS setDeviceToken(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		String appType = jsonObject.optString("appType","");
		String deviceId = jsonObject.optString("deviceId","");
		String token = jsonObject.optString("token","");
		if("".equals(token) || "".equals(appType) ){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		memberBaseService.saveDeviceToken(appType, deviceId, token);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		return result;
	}
	
	/**
	 * 保存设备的Token与帐号的关联
	 * 调用示例:[地址]/service/sso/setTokenAccount.r
	 * {"token":"1679091C5A880FAF6FB5E6087EB1B2DC","memberId":"40280ad65601004c0156010188390012","type":"0","appType":"2"}
	 */
	@RequestMapping(value = "/setTokenAccount")
	@ResponseBody
	public ResultWS setTokenAccount(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		String memberId = jsonObject.optString("memberId","");
		String type = jsonObject.optString("type","");//操作类型 0：保存帐号关联，1：去掉帐号关联
		String appType = jsonObject.optString("appType", "");
		String deviceId = jsonObject.optString("deviceId", "");
		String token = jsonObject.optString("token","");
		String aliasType = jsonObject.optString("aliasType","");
		String alias = jsonObject.optString("alias","");
		
		if("".equals(token) || "".equals(type) || "".equals(appType) || ("0".equals(type) && "".equals(memberId))){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		memberBaseService.saveTokenAccount(memberId, type, appType, deviceId, token, aliasType, alias);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		return result;
	}
	
}