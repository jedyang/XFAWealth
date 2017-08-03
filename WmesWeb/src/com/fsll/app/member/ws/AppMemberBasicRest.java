package com.fsll.app.member.ws;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.member.service.AppMemberBaseService;
import com.fsll.app.member.vo.AppInfoVO;
import com.fsll.app.member.vo.AppMemberBaseVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.StrUtils;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberValidateInfo;

/**
 * @author scshi
 * @date 20160623
 * 用户注册接口
 */
@RestController
@RequestMapping("/service/member")
public class AppMemberBasicRest extends WmesBaseRest{
	
	@Autowired
	private AppMemberBaseService memberBaseService;
	
	/**
	 * 判断数据项重复接口
	 * @author scshi
	 * @date 20160627
	 * @params items Jason格式字符串{fieldname：value,…}
	 * POST /service/member/checkDuplicate.r
	  {"tableName":"MemberBase","items":{"loginCode":"mic123","nickName":"test1name"}}
	 * */
	@RequestMapping(value = "/checkDuplicate")
	@ResponseBody
	public ResultWS checkDuplicate(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		result = parseParam(request,jsonObject,result,"duplicate");//参数验证
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		
		String tableName = jsonObject.optString("tableName","MemberBase");
		String items = jsonObject.getString("items");
		AppInfoVO info  = memberBaseService.checkDuplicate(tableName, items);
		
		if(null==info){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1000);
			result.setErrorMsg(WSConstants.MSG1000);
		}else{
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData(info);
		}
		return result;
	}
	
	/**
	 * 创建会员接口
	 * @author scshi
	 * @date 20160627
	 * POST /service/member/createMember.r
	 * {"accountData":{"loginCode":"ws-test136","mobileCode":"86","mobileNumber":"12345678","email":"xx@qq.com","password":"123456","memberType":"1","subMemberType":"11","nickName":"ws-test1"},
		"memberData":{"firstName":"test1","lastName":"ws","nickName":"ws-test1","nameChn":"chi name","country":"44","gender":"1","certType":"cert_type_01","certNo":"10101010110","born":"2010-10-10","nationality":"44","education":"education_01","employment":"employment_05","occupation":"occupation_16","address":"","telephone":"xxxxxx"}}
	 * */
	@RequestMapping(value = "/createMember")
	@ResponseBody
	public ResultWS createMember(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		result = parseParam(request,jsonObject,result,"createMember");//参数验证
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		String accountData = jsonObject.optString("accountData", "");
		String memberData = jsonObject.optString("memberData", "");
		String companyInfoCode = jsonObject.optString("companyCode", "");
		JSONObject accountDataJSON = JSONObject.fromObject(accountData); 
		JSONObject memberDataJSON = JSONObject.fromObject(memberData); 
		String memberId = memberBaseService.createMember(accountDataJSON,memberDataJSON,companyInfoCode);
		
		if (memberId.equals(WSConstants.CODE1010)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1010);
			result.setErrorMsg(WSConstants.MSG1010);
			return result;
		}
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(memberId);
		return result;
	}
	
	/**
	 * @author scshi
	 * @date 20160627
	 * 发送激活邮件接口
	 * POST /service/member/sendRegEmail.r
	 * {"loginCode":"test136","email":"test1@test.com","activeCode":"321456"}
	 * */
	@RequestMapping(value = "/sendRegEmail")
	@ResponseBody
	public ResultWS sendRegEmail(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		result = parseParam(request,jsonObject,result,"sendRegEmail");//参数验证
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		String loginCode = jsonObject.getString("loginCode")==null?"":jsonObject.getString("loginCode");
		String email = jsonObject.getString("email")==null?"":jsonObject.getString("email");
		String activeCode = jsonObject.getString("activeCode")==null?"":jsonObject.getString("activeCode");
		
		//发送激活邮件
		result = memberBaseService.sendRegEmail(loginCode, email, activeCode);
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		result.setRet(WSConstants.SUCCESS);
		return result;
	}
	
	/**
	 * 修改用户密码接口
	 * @author michael
	 * @date 20160712
	 * POST /service/member/changePassword.r
	 * {"memberId":"40280ad455e327690155e3291c580000","oldPassword":"mic","newPassword":"mic123"}
	 * */
	@RequestMapping(value = "/changePassword")
	@ResponseBody
	public ResultWS changePassword(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setRet(WSConstants.SUCCESS);
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		result = parseParam(request,jsonObject,result,"changePassword");//参数验证
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		String memberId = jsonObject.getString("memberId")==null?"":jsonObject.getString("memberId");
		String oldPassword = jsonObject.getString("oldPassword")==null?"":jsonObject.getString("oldPassword");
		String newPassword = jsonObject.getString("newPassword")==null?"":jsonObject.getString("newPassword");
		
		//获取用户信息
		MemberBase userBase = memberBaseService.findById(memberId);
		if (null!=userBase){
			//检测用户状态
			if (null!=userBase.getStatus() && "1".equals(userBase.getStatus())){
				String md5password = this.pwdEncoder.encodePassword(oldPassword);
		        //检测旧密码是否正确
		        if (!md5password.equalsIgnoreCase(userBase.getPassword())){
		        	result.setRet(WSConstants.FAIL);
					result.setErrorCode(WSConstants.CODE1006);
					result.setErrorMsg(WSConstants.MSG1006);
					return result;
		        }else{
		        
			        //保存新密码
			        md5password = this.pwdEncoder.encodePassword(newPassword);
			        userBase.setPassword(md5password);
			        userBase = memberBaseService.saveOrUpdate(userBase);
		        }
	        }else{
	        	result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1004);
				result.setErrorMsg(WSConstants.MSG1004);
				return result;
			}
		}else{
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1008);
			result.setErrorMsg(WSConstants.MSG1008);
			return result;
		}
		return result;
	}
	
	
	/**
	 * 重置用户密码接口
	 * @author michael
	 * @date 20160712
	 * POST /wmes/service/member/resetPassword.r
	 * {"email":"xx@qq.com","oldPassword":"mic","newPassword":"mic123"}
	 * */
	@RequestMapping(value = "/resetPassword")
	@ResponseBody
	public ResultWS resetPassword(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setRet(WSConstants.SUCCESS);
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		result = parseParam(request,jsonObject,result,"resetPassword");//参数验证
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		String email = jsonObject.getString("email")==null?"":jsonObject.getString("email");
		String tempPassword = jsonObject.getString("tempPassword")==null?"":jsonObject.getString("tempPassword");
		String newPassword = jsonObject.getString("newPassword")==null?"":jsonObject.getString("newPassword");
		
		String md5password = "";
		//获取用户信息
		MemberBase userBase = memberBaseService.findByEmail(email);
		if (null!=userBase){
			//检测用户状态
			if (null!=userBase.getStatus() && "1".equals(userBase.getStatus())){
				
				//检测临时密码是否正确
				try {
					List<MemberValidateInfo> infolist = memberBaseService.findMemberValidateInfo("", email, 2);
					if (!infolist.isEmpty()){
						MemberValidateInfo validateInfo = infolist.get(0);
//						md5password = this.pwdEncoder.encodePassword(tempPassword);
						//校验码错误，或已过期
						if (!validateInfo.getValidateCode().equalsIgnoreCase(tempPassword)||(null!=validateInfo.getExpireTime() && validateInfo.getExpireTime().compareTo(new Date())<=0)){
							result.setRet(WSConstants.FAIL);
							result.setErrorCode(WSConstants.CODE1007);
							result.setErrorMsg(WSConstants.MSG1007);
							return result;
						}
						//用户状态正常，校验通过
						//保存新密码
						md5password = this.pwdEncoder.encodePassword(newPassword);
						userBase.setPassword(md5password);
						memberBaseService.saveOrUpdate(userBase);
					}else{
						result.setRet(WSConstants.FAIL);
						result.setErrorCode(WSConstants.CODE1009);
						result.setErrorMsg(WSConstants.MSG1009);
						return result;
					}
				} catch (Exception e) {
					// TODO: handle exception
					result.setRet(WSConstants.FAIL);
					result.setErrorCode(WSConstants.CODE1000);
					result.setErrorMsg(WSConstants.MSG1000);
					return result;
				}
			}else{
	        	result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1004);
				result.setErrorMsg(WSConstants.MSG1004);
				return result;
			}
		}else{
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1008);
			result.setErrorMsg(WSConstants.MSG1008);
			return result;
		}
		return result;
	}

	/**
	 * 发送重置密码邮件接口
	 * @author michael
	 * @date 20160712
	 * POST /service/member/sendResetPasswordEmail.r
	 * {"email":"test@test.com"}
	 * */
	@RequestMapping(value = "/sendResetPasswordEmail")
	@ResponseBody
	public ResultWS sendResetPasswordEmail(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		String email = jsonObject.getString("email")==null?"":jsonObject.getString("email");
		String activeCode = StrUtils.getRandomString(6);//获取6位随机码
		//发送重置密码邮件
		result = memberBaseService.sendResetPasswordEmail(email, activeCode);
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		result.setRet(WSConstants.SUCCESS);
		return result;
	}
	
	/**
	 * 分析参数,必需参数是否为空
	 * @param request
	 * @param jsonObject
	 * @param result
	 * @param oper add mod
	 * @return
	 */
	private ResultWS parseParam(HttpServletRequest request,JSONObject jsonObject,ResultWS result,String oper){
		if("duplicate".equals(oper)){
			String items = jsonObject.getString("items")==null?"":jsonObject.getString("items");
			if("".equals(items)){//参数不能为空
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“items”"+WSConstants.MSG1002);
			}
		}else if("createMember".equals(oper)){
			String accountData = jsonObject.optString("accountData","");
			String memberData = jsonObject.optString("memberData","");
			String companyInfoCode = jsonObject.optString("companyCode","");
			if("".equals(accountData)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“accountData”"+WSConstants.MSG1002);
			}
			if("".equals(memberData)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“memberData”"+WSConstants.MSG1002);
			}
			if("".equals(companyInfoCode)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“companyCode”"+WSConstants.MSG1002);
			}
		}else if("sendRegEmail".equals(oper)){
			String loginCode = jsonObject.getString("loginCode")==null?"":jsonObject.getString("loginCode");
			String email = jsonObject.getString("email")==null?"":jsonObject.getString("email");
			String activeCode = jsonObject.getString("activeCode")==null?"":jsonObject.getString("activeCode");
			if("".equals(loginCode)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“loginCode”"+WSConstants.MSG1002);
			}
			if("".equals(email)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“email”"+WSConstants.MSG1002);
			}
			if("".equals(activeCode)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“activeCode”"+WSConstants.MSG1002);
			}
		}else if("changePassword".equals(oper)){
			if("".equals(jsonObject.optString("memberId",""))){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“memberId”"+WSConstants.MSG1002);
			}
			if("".equals(jsonObject.optString("oldPassword",""))){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“oldPassword”"+WSConstants.MSG1002);
			}
			if("".equals(jsonObject.optString("newPassword",""))){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“newPassword”"+WSConstants.MSG1002);
			}
		}else if("resetPassword".equals(oper)){
			if("".equals(jsonObject.optString("email",""))){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“memberId”"+WSConstants.MSG1002);
			}
			if("".equals(jsonObject.optString("tempPassword",""))){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“tempPassword”"+WSConstants.MSG1002);
			}
			if("".equals(jsonObject.optString("newPassword",""))){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“newPassword”"+WSConstants.MSG1002);
			}
		}else if("updateInfo".equals(oper) && "".equals(jsonObject.optString("memberId",""))){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“memberId”"+WSConstants.MSG1002);						
		}
		return result;
	}
	
	
	/**
	 * 获取投资者个人信息接口
	 * @author zxtan
	 * @date 2017-02-20
	 * POST /service/member/findMemberBase.r
	 * {"memberId":"40280ad65601004c0156010188390025","langCode":"sc"}
	 * */
	@RequestMapping(value = "/findMemberBase")
	@ResponseBody
	public ResultWS findMemberBase(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}

		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		
		if ("".equals(memberId)|| "".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		AppMemberBaseVO memberBase = memberBaseService.findMemberBase(memberId, langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(memberBase);
		return result;
	}
	
	/**
	 * 修改用户个人信息
	 * @author zxtan
	 * @date 2017-03-08
	 * POST /service/member/updateMemberInfo.r
	 * {"memberId":"40280ad65601004c0156010188390025","defCurrency":"CNY","defDisplayColor":"1","dateformat":"yyyy-MM-dd"}
	 * */
	@RequestMapping(value = "/updateMemberInfo")
	@ResponseBody
	public ResultWS updateMemberInfo(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setRet(WSConstants.SUCCESS);
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		result = parseParam(request,jsonObject,result,"updateInfo");//参数验证
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		String memberId = jsonObject.optString("memberId", "");
		String defDisplayColor = jsonObject.optString("defDisplayColor", "");
		String defCurrency = jsonObject.optString("defCurrency", "");
		String dateformat = jsonObject.optString("dateformat", "");
		
		//获取用户信息
		MemberBase userBase = memberBaseService.findById(memberId);
		if (null!=userBase){
			//检测用户状态
			if (null!=userBase.getStatus() && "1".equals(userBase.getStatus())){
				
		        //默认涨跌颜色
		        if ( !"".equals(defDisplayColor) && "1,2,".indexOf(defDisplayColor)>-1){
			        userBase.setDefDisplayColor(defDisplayColor);
		        }
		        //默认货币
		        if (!"".equals(defCurrency.trim())){
			        userBase.setDefCurrency(defCurrency);
		        }
		        //默认日期格式
		        if (!"".equals(dateformat.trim())){
			        userBase.setDateFormat(dateformat);
		        }
		        
		        userBase = memberBaseService.saveOrUpdate(userBase);
	        }else{
	        	result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1004);
				result.setErrorMsg(WSConstants.MSG1004);
				return result;
			}
		}else{
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1008);
			result.setErrorMsg(WSConstants.MSG1008);
			return result;
		}
		return result;
	}
	
}
