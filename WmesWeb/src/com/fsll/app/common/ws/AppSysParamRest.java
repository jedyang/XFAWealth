package com.fsll.app.common.ws;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.common.service.AppSysParamService;
import com.fsll.app.common.vo.AppSysParamVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 基础数据接口
 * @author zpzhou
 * @date 2016年2月15日
 */
@RestController
@RequestMapping("/service/sysParam")
public class AppSysParamRest extends WmesBaseRest{	
	@Autowired
	private AppSysParamService paramService;
	
	/**
	 * 获得基础数据列表 通过body传递参数
	 * 调用示例:[地址]/service/sysParam/getListByType.r
	   {"page":1, "rows":10, "typeCode":"education","langCode":"sc"}
	 */
	@RequestMapping(value = "/getListByType")
	@ResponseBody
	public ResultWS getListByType(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		String typeCode = jsonObject.getString("typeCode");//获得查询参数
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String page = jsonObject.optString("page", "");// 获得当前页数
		String rows = jsonObject.optString("rows", "");// 获得每页记录数
		
		if ("".equals(page) || "".equals(rows) ||"".equals(typeCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		//分页和排序的一些参数
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));
		
		jsonPaging = paramService.getParamList(jsonPaging,langCode,typeCode);
					
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonPaging.getList());
		result.setCurPage(jsonPaging.getPage());
		result.setPageSize(jsonPaging.getRows());
		result.setTotal(jsonPaging.getTotal());
		return result;
	}
	
	/**
	 * 获得一条基础数据
	 * 调用示例:[地址]/service/sysParam/getOneByCode.r
		{"configCode":"fund_type_02"}
	 */
	@RequestMapping(value = "/getOneByCode")
	@ResponseBody
	public ResultWS getOneByCode(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		String configCode = jsonObject.getString("configCode");//获得查询参数
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		if ("".equals(configCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppSysParamVO obj = this.paramService.findParamByCode(langCode,configCode);
		if(obj == null ){//无数据
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE2002);
			result.setErrorMsg(WSConstants.MSG2002);
		}else{
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData(obj);
		}
		return result;
	}
	
}
