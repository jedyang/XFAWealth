/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.api.product.ws;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.api.product.service.PruductService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.core.base.CoreBaseRest;

/**
 * 产品信息管理
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-6-19
 */
@RestController
@RequestMapping("/api/product")
public class ProductRest extends CoreBaseRest {
	@Autowired
	private PruductService pruductService;
	
	/**
	 * 搜索:调用示例=[地址]/api/product/list.r
	 * {"page":1,"rows":2,"productType":"fund","langCode":"sc","periodCode":"return_period_code_1M"}
	 */
	@RequestMapping(value = "/list",method = {RequestMethod.POST})
	@ResponseBody
	public ResultWS list(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		
		//1.校验权限和参数
		result = this.validParam(request,"list", jsonObject, result);
		if(!(result.getRet()).equals(WSConstants.SUCCESS)){
			return result;
		}
		
		//2.分页信息
		String page = jsonObject.optString("page", "1");// 获得当前页数
		String rows = jsonObject.optString("rows", "10");// 获得每页记录数
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));
		
		//3.获取数据并对数据进行加密传输
		jsonPaging = pruductService.findAll(jsonPaging,jsonObject);
		
		//3.1.数据不需要加密
		result.setData(jsonPaging.getList());
		
		//3.2.数据需要加密传输
		//String jsonData = this.toJSONString(jsonPaging.getList(), null, null);
		//result.setData(this.dataEncrypt(jsonObject,jsonData));
        
        //4.返回结果
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setCurPage(jsonPaging.getPage());
		result.setPageSize(jsonPaging.getRows());
		result.setTotal(jsonPaging.getTotal());
		return result;
	}
	
	/**
	 * 获得明细:调用示例=[地址]/api/product/detail.r
	 * {"productType":"fund", "productId":"1"}
	 */
	@RequestMapping(value = "/detail",method = {RequestMethod.POST})
	@ResponseBody
	public ResultWS detail(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		
		//1.校验权限和参数
		result = this.validParam(request,"detail", jsonObject, result);
		if(!(result.getRet()).equals(WSConstants.SUCCESS)){
			return result;
		}
		
		//2.获取数据
		Object obj = pruductService.findById(jsonObject);
        
        //3.返回结果
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(obj);
		return result;
	}
	
	/**
	 * 保存或者修改:调用示例=[地址]/api/product/save.r
	 * {"_VER_":201,"productType":"fund", "code":"I320827",.....}
	 */
	@RequestMapping(value = "/save",method = {RequestMethod.POST})
	@ResponseBody
	public ResultWS save(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		
		//1.校验权限和参数
		result = this.validParam(request,"save", jsonObject, result);
		if(!(result.getRet()).equals(WSConstants.SUCCESS)){
			return result;
		}
		
		//2.获取数据
		Object obj = pruductService.saveOrUpdate(jsonObject);
        
        //3.返回结果
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(obj);
		return result;
	}
	
	/**
	 * 修改状态:调用示例=[地址]/api/product/status.r
	 * {"productType":"fund", "ids":"1,2,3","status":"1"}
	 */
	@RequestMapping(value = "/status",method = {RequestMethod.POST})
	@ResponseBody
	public ResultWS status(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		
		//1.校验权限和参数
		result = this.validParam(request,"status", jsonObject, result);
		if(!(result.getRet()).equals(WSConstants.SUCCESS)){
			return result;
		}
		
		//2.获取数据
		boolean obj = pruductService.saveStatus(jsonObject);
        
        //3.返回结果
		if(obj){
			result.setRet(WSConstants.SUCCESS);
		}else{
			result.setRet(WSConstants.FAIL);
		}
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(null);
		return result;
	}
	
	/**
	 * 删除:调用示例=[地址]/api/product/delete.r
	 * {"productType":"fund", "ids":"1,2,3"}
	 */
	@RequestMapping(value = "/delete",method = {RequestMethod.POST})
	@ResponseBody
	public ResultWS delete(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		
		//1.校验权限和参数
		result = this.validParam(request,"delete", jsonObject, result);
		if(!(result.getRet()).equals(WSConstants.SUCCESS)){
			return result;
		}
		
		//2.获取数据
		boolean obj = pruductService.delete(jsonObject);
        
        //3.返回结果
		if(obj){
			result.setRet(WSConstants.SUCCESS);
		}else{
			result.setRet(WSConstants.FAIL);
		}
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(null);
		return result;
	}
	
	/**
	 * 参数的权限的统一校验
	 * @param request
	 * @param methodType
	 * @param jsonObject
	 * @param result
	 * @return
	 */
	private ResultWS validParam(HttpServletRequest request,String methodType,JSONObject jsonObject,ResultWS result) {
		//0.初始状态
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		
		//1.检查权限
		String checkPowerResult = checkPower(request,jsonObject,result);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		//2.检查特有参数
		if("list".equals(methodType)){//搜索方法
			//检查必填的参数是否为空或者是否存在
			String productType = jsonObject.optString("productType","");
			if("".equals(productType)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“productType”"+WSConstants.MSG1002);
				return result;
			}
			//.......
		}else if("save".equals(methodType)){//新增或者修改
			//检查必填的参数是否为空或者是否存在
			//.......
		}else if("status".equals(methodType)){//修改状态
			//检查必填的参数是否为空或者是否存在
			//.......
		}else if("delete".equals(methodType)){//删除
			//检查必填的参数是否为空或者是否存在
			//.......
		}
		return result;
	}
	
}
