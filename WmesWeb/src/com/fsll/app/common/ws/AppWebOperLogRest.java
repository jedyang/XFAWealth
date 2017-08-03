package com.fsll.app.common.ws;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.common.service.AppWebOperLogService;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebOperLog;

/**
 * 控制器:基础数据
 * 
 * @author mjhuang
 * @date 2016年2月15日
 */
@RestController
@RequestMapping("/service/webOperLog")
public class AppWebOperLogRest extends WmesBaseRest{	
	@Autowired
	private AppWebOperLogService webOperLogService;
	
	/**
	 * 新增修改一条日志信息
	 * 调用示例:[地址]/service/webOperLog/create.r
	 */
	@RequestMapping(value = "/create")
	@ResponseBody
	public ResultWS create(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		result = parseParam(request,jsonObject,result,"add");
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		
		WebOperLog log = parseObject(request,jsonObject,"add");
		log = webOperLogService.saveOrUpdate(log);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(log.getId());
		return result;
	}
	
	/**
	 * 修改一条日志信息
	 * 调用示例:[地址]/service/webOperLog/update.r
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public ResultWS update(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		result = parseParam(request,jsonObject,result,"mod");
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		WebOperLog log = parseObject(request,jsonObject,"mod");
		log = webOperLogService.saveOrUpdate(log);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(log.getId());
		return result;
	}
	
	
	/**
	 * 删除日志信息
	 * 调用示例:[地址]/service/webOperLog/delete.r
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public ResultWS delete(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		result = parseParam(request,jsonObject,result,"del");
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		String ids = jsonObject.get("ids") == null ? "" : jsonObject.getString("ids");
		boolean rFlag = webOperLogService.delete(ids);
		if(rFlag){//成功
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData("");
			return result;
		}else{
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1000);
			result.setErrorMsg(WSConstants.MSG1000);
			result.setData("");
			return result;
		}
	}
	
	/**
	 * 分析参数,必需参数是否为空
	 * @param request
	 * @param jsonObject
	 * @param result
	 * @param oper add mod
	 * @return
	 */
	private ResultWS parseParam(HttpServletRequest request,JSONObject jsonObject,ResultWS result,String oper) {
		String memberId = jsonObject.get("memberId") == null ? "" : jsonObject.getString("memberId");
		String loginCode = jsonObject.get("loginCode") == null ? "" : jsonObject.getString("loginCode");
		if("mod".equals(oper)){
			String id = jsonObject.get("id") == null ? "" : jsonObject.getString("id");
			if("".equals(id)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“id”"+WSConstants.MSG1002);
				return result;
			}
		}else if("del".equals(oper)){
			String ids = jsonObject.get("ids") == null ? "" : jsonObject.getString("ids");
			if("".equals(ids)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“ids”"+WSConstants.MSG1002);
			}
			return result;
		}
		if("".equals(memberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“memberId”"+WSConstants.MSG1002);
			return result;
		}
		if("".equals(loginCode)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“loginCode”"+WSConstants.MSG1002);
			return result;
		}
		return result;
	}
	
	/**
	 * 分析对象
	 * @param request
	 * @param jsonObject
	 * @param oper add mod
	 * @return
	 */
	private WebOperLog parseObject(HttpServletRequest request,JSONObject jsonObject,String oper) {
		WebOperLog log = new WebOperLog();
		if("add".equals(oper)){
			log.setId(null);
			log.setCreateTime(new Date());
		}else{
			String id = jsonObject.get("id") == null ? "" : jsonObject.getString("id");
			log.setId(id);
		}
		String memberId = jsonObject.get("memberId") == null ? "" : jsonObject.getString("memberId");
		String loginCode = jsonObject.get("loginCode") == null ? "" : jsonObject.getString("loginCode");
		String nickName = jsonObject.get("nickName") == null ? "" : jsonObject.getString("nickName");
		String moduleType = jsonObject.get("moduleType") == null ? "" : jsonObject.getString("moduleType");
		String remark = jsonObject.get("remark") == null ? "" : jsonObject.getString("remark");
		
		MemberBase member = new MemberBase();
		member.setId(memberId);
		log.setMember(member);
		log.setLoginCode(loginCode);
		log.setNickName(nickName);
		log.setModuleType(moduleType);
		log.setRemark(remark);
		log.setIp(this.getRemoteHost(request));
		return log;
	}
	
}
