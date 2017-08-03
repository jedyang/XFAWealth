/**
 * 
 */
package com.fsll.app.fund.ws;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.fund.service.AppFundAnnoService;
import com.fsll.app.fund.vo.AppFundAnncInfoDataVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.FundAnno;

/**
 *@author zpzhou
 *获取基金公告
 *@date 20160615
 */
@RestController
@RequestMapping("/service/fundAnno")
public class AppFundAnnoRest extends WmesBaseRest{
	@Autowired
	private AppFundAnnoService fundAnnoService;
	
	/**
	 * 获取基金公告列表， 通过body传递参数
	 * 调用示例:[地址]/service/fundAnno/getFundAnncList.r
		{"langCode":"sc","page":1,"rows":15,"fundId":"1679091C5A880FAF6FB5E6087EB1B2DC" }
	 */
	@RequestMapping(value = "/getFundAnncList")
	@ResponseBody
	public ResultWS getFundAnncList(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String fundId=jsonObject.optString("fundId","");
		String page = jsonObject.optString("page", "");// 获得当前页数
		String rows = jsonObject.optString("rows", "");// 获得每页记录数
		if ("".equals(page) || "".equals(rows) || "".equals(fundId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		FundAnno queryAnno = new FundAnno();
		queryAnno.setFundId(fundId);
		//设置分页信息
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(StrUtils.getInt(page));
		jsonPaging.setRows(StrUtils.getInt(rows));
		jsonPaging.setSort("annoDate");
		jsonPaging.setOrder("desc");
		
		//获取数据
		jsonPaging = fundAnnoService.fundAnncList(jsonPaging,queryAnno,langCode);
		//设置返回对象
		if(jsonPaging.getList().isEmpty()){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE2001);
			result.setErrorMsg(WSConstants.MSG2001);
			return result;
		}else{
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData(jsonPaging.getList());
			result.setCurPage(jsonPaging.getPage());
			result.setPageSize(jsonPaging.getRows());
			result.setTotal(jsonPaging.getTotal());
		}
		return result;
	}

	/**
	 * 获取基金公告详细信息， 通过body传递参数
	 * 调用示例:[地址]/service/fundAnno/getFundAnncInfo.r
	 * {"anncId":"107"}
	 */
	@RequestMapping(value = "/getFundAnncInfo")
	@ResponseBody
	public ResultWS getFundAnncInfo(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String anncId = jsonObject.getString("anncId");
//		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		if ("".equals(anncId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		AppFundAnncInfoDataVO vo = fundAnnoService.fundAnncInfo(anncId);
		if(null==vo){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE2002);
			result.setErrorMsg(WSConstants.MSG2002);
		}else{
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData(vo);
		}
		return result;
	}
}
