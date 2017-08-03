package com.fsll.app.investor.discover.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.discover.service.AppNewsInfoService;
import com.fsll.app.investor.discover.vo.AppNewsCategoryVO;
import com.fsll.app.investor.discover.vo.AppNewsInfoItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 新闻接口实现
 * @author zpzhou
 * @date 2016-8-11
 */
@RestController
@RequestMapping("/service/newsInfo")
public class AppNewsInfoRest extends WmesBaseRest {
	@Autowired
	private AppNewsInfoService newsInfoService;
	
/*	*//**
	 * 新闻列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/newsInfo/findNewsList.r
	 * {"page":1, "rows":15,"_user_id_":"40280a25559b852e01559b8615da0002","type":"1"}
	 *//*
	@RequestMapping(value = "/findNewsList")
	@ResponseBody
	public ResultWS findNewsList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String page = jsonObject.optString("page", "");// 获得当前页数
		String rows = jsonObject.optString("rows", "");// 获得每页记录数
		String userId = jsonObject.optString("_user_id_", "");// 获得当前登录用户ID
		String type = jsonObject.optString("type", "");//0或空为全部，1：关注，2：推荐
		if ("".equals(page) && "".equals(rows)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = newsInfoService.findNewsList(jsonPaging, userId,type);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonPaging.getList());
		result.setCurPage(jsonPaging.getPage());
		result.setPageSize(jsonPaging.getRows());
		result.setTotal(jsonPaging.getTotal());
		return result;
	}*/
	
	/**
	 * 新闻列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/newsInfo/findNewsCategoryList.r
	 * {"langCode":"sc"}
	 */
	@RequestMapping(value = "/findNewsCategoryList")
	@ResponseBody
	public ResultWS findNewsCategoryList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);// 获得当前语言
		if ("".equals(langCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}


		List<AppNewsCategoryVO> list = newsInfoService.findNewsCategoryList(langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 * 栏目新闻接口， 通过body传递参数 
	 * 调用示例:[地址]/service/newsInfo/findNewsListByCategory.r
	 * {"page":1, "rows":15,"categoryId":"40280a25559b852e01559b8615da0009","langCode":"sc","keyword":""}
	 */
	@RequestMapping(value = "/findNewsListByCategory")
	@ResponseBody
	public ResultWS findNewsListByCategory(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String page = jsonObject.optString("page", "");// 获得当前页数
		String rows = jsonObject.optString("rows", "");// 获得每页记录数
		String categoryId = jsonObject.optString("categoryId", "");// 获得当前栏目ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);//语言
		String keyword = jsonObject.optString("keyword", "");//搜索关键字
		if ("".equals(page) || "".equals(rows) || "".equals(categoryId) || "".equals(langCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = newsInfoService.findNewsListByCategory(jsonPaging,categoryId,langCode,keyword);

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
	 * 新闻内容接口， 通过body传递参数 
	 * 调用示例:[地址]/service/newsInfo/findNewsInfo.r
	 * {"id":"9b466f3d-ebe2-4346-9724-8948b263e9f4"}
	 */
	@RequestMapping(value = "/findNewsInfo")
	@ResponseBody
	public ResultWS findNewsInfo(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId","");
		String id = jsonObject.optString("id","");
		if ("".equals(id) ) {//"".equals(memberId)||
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}


		AppNewsInfoItemVO info = newsInfoService.findNewsInfo(memberId,id);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	/**
	 * 新闻评论列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/newsInfo/findNewsCommentList.r
	 * {"page":1, "rows":15,"newsInfoId":"9b466f3d-ebe2-4346-9724-8948b263e9f4"}
	 */
	@RequestMapping(value = "/findNewsCommentList")
	@ResponseBody
	public ResultWS findNewsCommentList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String memberId = jsonObject.optString("memberId","");//||"".equals(memberId)
		String page = jsonObject.optString("page", "");// 获得当前页数
		String rows = jsonObject.optString("rows", "");// 获得每页记录数
		String newsInfoId = jsonObject.optString("newsInfoId", "");// 获得当前新闻ID
		if ("".equals(page) || "".equals(rows)  || "".equals(newsInfoId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = newsInfoService.findNewsCommentList(jsonPaging, memberId, newsInfoId);

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
	 * 新闻点赞/踩接口， 通过body传递参数 
	 * 调用示例:[地址]/service/newsInfo/addNewsBehavior.r
	 * {"memberId":"40280ad455e327690155e3291c582000", "targetId":"6e8c196484b74e1cb87cea96eff9f433","behaviorType":"like"}
	 */
	@RequestMapping(value = "/addNewsBehavior")
	@ResponseBody
	public ResultWS addNewsBehavior(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录人
		String targetId = jsonObject.optString("targetId", "");// 获得当前新闻ID
		String behaviorType = jsonObject.optString("behaviorType", "");// like 点赞/unlike 踩
		if ("".equals(memberId) || "".equals(targetId) || "".equals(behaviorType) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		boolean flag = newsInfoService.addNewsBehavior(memberId, targetId, behaviorType);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
	
	/**
	 * 新闻点赞/踩接口， 通过body传递参数 
	 * 调用示例:[地址]/service/newsInfo/addNewsComment.r
	 * {"memberId":"40280ad455e327690155e3291c582000", "infoId":"6e8c196484b74e1cb87cea96eff9f433","comment":"评论内容","replyCommentId":""}
	 */
	@RequestMapping(value = "/addNewsComment")
	@ResponseBody
	public ResultWS addNewsComment(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录人
		String infoId = jsonObject.optString("infoId", "");// 获得当前新闻ID
		String comment = jsonObject.optString("comment", "");//评论内容
		String replyCommentId = jsonObject.optString("replyCommentId", "");//被回复评论的id
		String ip = getRemoteHost(request);//ip地址
		if ("".equals(memberId) || "".equals(infoId) || "".equals(comment) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		boolean flag = newsInfoService.addNewsComment(infoId, memberId, comment, replyCommentId, ip);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
	
	/**
	 * 新闻点赞/踩接口， 通过body传递参数 
	 * 调用示例:[地址]/service/newsInfo/addNewsCommentBehavior.r
	 * {"memberId":"40280ad455e327690155e3291c582000", "targetId":"40280ad55bcd3c21015bcd415e1f0003","behaviorType":"like"}
	 */
	@RequestMapping(value = "/addNewsCommentBehavior")
	@ResponseBody
	public ResultWS addNewsCommentBehavior(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录人
		String targetId = jsonObject.optString("targetId", "");// 获得当前新闻ID
		String behaviorType = jsonObject.optString("behaviorType", "");// like 点赞/unlike 踩
		if ("".equals(memberId) || "".equals(targetId) || "".equals(behaviorType) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		boolean flag = newsInfoService.addNewsCommentBehavior(memberId, targetId, behaviorType);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
}
