package com.fsll.app.common.ws;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.common.service.AppOperLogService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.OperLog;

/**
 * 
 * 业务操作日志接口服务
 * 
 * @author mjhuang
 * @date 2016年2月15日
 */
@RestController
@RequestMapping("/service/operLog")
public class AppOperLogRest extends WmesBaseRest{	
	@Autowired
	private AppOperLogService operLogService;
	
	/**
	 * 新增一条日志信息
	 * 调用示例:[地址]/service/operLog/create.r
	 */
	@RequestMapping(value = "/create")
	@ResponseBody
	public ResultWS create(HttpServletRequest request,@RequestBody String body){
		ResultWS result = new ResultWS();
		
		OperLog log = new OperLog();
		log.setMemberId("1");
		log.setLoginCode("test1");
		log.setNickName("测试");
		log.setClientType("web");
		log.setModuleType("test");
		log.setRelateData("123");
		log.setRemark("测试备注");
		log.setIp(this.getRemoteHost(request));
		operLogService.create(log);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(log.getId());
		return result;
	}
	
	/**
	 * 修改一条日志信息
	 * 调用示例:[地址]/service/operLog/update.r
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public ResultWS update(HttpServletRequest request,@RequestBody String body){
		ResultWS result = new ResultWS();
		
		Update update = new Update();
		update.set("nickName","测试更新");
		operLogService.update("57c7d930566598373c11c415",update);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData("");
		return result;
	}
	
	/**
	 * 查询一条日志信息
	 * 调用示例:[地址]/service/operLog/findById.r
	 */
	@RequestMapping(value = "/findById")
	@ResponseBody
	public ResultWS findById(HttpServletRequest request,@RequestBody String body){
		ResultWS result = new ResultWS();
		result.setData(null);
		OperLog log = operLogService.findById("57c7d930566598373c11c415");
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(log);
		return result;
	}
	
	/**
	 * 分页查询日志信息
	 * 调用示例:[地址]/service/operLog/findByPage.r
	 */
	@RequestMapping(value = "/findByPage")
	@ResponseBody
	public ResultWS findByPage(HttpServletRequest request,@RequestBody String body){
		ResultWS result = new ResultWS();
		JsonPaging jsonpaging = new JsonPaging();
		jsonpaging.setPage(1);
		jsonpaging.setRows(2);
		OperLog log = new OperLog();
		//log.setLoginCode("测试");
		//log.setModuleType("test");
		//log.setStartTime("2016-09-01 15:35:30");
		//log.setEndTime("2016-09-01 15:45:30");
		jsonpaging = operLogService.findAll(jsonpaging,log);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonpaging.getList());
		return result;
	}
	
	
	/**
	 * 删除日志信息
	 * 调用示例:[地址]/service/operLog/delete.r
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public ResultWS delete(HttpServletRequest request,@RequestBody String body){
		ResultWS result = new ResultWS();
		result.setData(null);
		operLogService.delete("57c7d930566598373c11c415");
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData("");
		return result;
	}
	
}
