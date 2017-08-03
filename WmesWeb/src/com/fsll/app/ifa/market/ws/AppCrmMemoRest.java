package com.fsll.app.ifa.market.ws;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fsll.app.ifa.market.service.AppCrmMemoService;
import com.fsll.app.ifa.market.vo.AppCrmMemoVO;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 日程接口
 * @author zxtan
 * @date 2017-03-23
 */
@RestController
@RequestMapping("/service/ifa/market/memo")
public class AppCrmMemoRest extends WmesBaseRest {

	@Autowired
	private AppCrmMemoService memoService;
		
	/**
	 * 获取日程安排
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/memo/findCrmMemoList.r
	 * {"page":1, "rows":15,"ifaMemberId":"40280ad455e327690155e3291c582000","clientMemberId":"40280ad655bfe9820155bfef8921000c"}
	 */
	@RequestMapping(value = "/findCrmMemoList")
	@ResponseBody
	public ResultWS findCrmMemoList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		Integer page = jsonObject.optInt("page", 1);// 获得当前页数
		Integer rows = jsonObject.optInt("rows", 10);// 获得每页记录数isRead
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String clientMemberId = "";// jsonObject.optString("clientMemberId", "");// 获得当前登录用户ID
		
		if ( "".equals(memberId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = memoService.findCrmMemoList(jsonPaging, memberId, clientMemberId);

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
	 * 获取日程安排详情
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/memo/findCrmMemo.r
	 * {"memoId":"40280a295a45cef6015a4622ef250001"}
	 */
	@RequestMapping(value = "/findCrmMemo")
	@ResponseBody
	public ResultWS findCrmMemo(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memoId = jsonObject.optString("memoId", "");// 销售记录ID
		
		if ( "".equals(memoId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		AppCrmMemoVO info = memoService.findCrmMemo(memoId);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	/**
	 *更新/新增记录
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/memo/saveCrmMemo.r
	 * {"id":"40280a295a45cef6015a4622ef250003","ifaMemberId":"40280ad455e327690155e3291c582000","memberId":"40280ad655bfe9820155bfef8921000c","memoText":"abc"}
	 */
	@RequestMapping("/saveCrmMemo")
	@ResponseBody
	public ResultWS saveCrmMemo(HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();// JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
				
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
	                request.getSession().getServletContext());
	        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
           //获取multiRequest 中所有的文件名
    		String ts = multiRequest.getParameter("_ts_");
    		String tokenId = multiRequest.getParameter("_tokenId_");
    		String signature = multiRequest.getParameter("_signature_");
            
    		jsonObject = new JSONObject();
    		jsonObject.put("_ts_", ts);
    		jsonObject.put("_tokenId_", tokenId);
    		jsonObject.put("_signature_", signature);
        }
		
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
			
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean flag = false;
		flag = memoService.saveCrmMemo(request);
		map.put("flag", flag);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
	
	
	/**
	 *删除策略
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/memo/deleteCrmMemo.r
	 * {"id":"40280a295a45cef6015a4622ef250003"}
	 */
	@RequestMapping("/deleteCrmMemo")
	@ResponseBody
	public ResultWS deleteCrmMemo(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String id = jsonObject.optString("id","");
		
		if("".equals(id)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean flag = false;
		flag = memoService.deleteCrmMemo(id);
		map.put("flag", flag);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
}
