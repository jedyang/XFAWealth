package com.fsll.app.discover.ws;

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

import com.fsll.app.discover.service.AppCommunityService;
import com.fsll.app.discover.vo.AppCommunitySearchDataVO;
import com.fsll.app.discover.vo.AppCommunitySectionVO;
import com.fsll.app.discover.vo.AppCommunityTopicVO;
import com.fsll.app.investor.discover.service.AppNewsInfoService;
import com.fsll.app.investor.discover.vo.AppNewsCategoryVO;
import com.fsll.app.investor.discover.vo.AppNewsInfoItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 社区接口实现
 * @author zxtan
 * @date 2017-05-18
 */
@RestController
@RequestMapping("/service/discover/community")
public class AppCommunityRest extends WmesBaseRest {
	@Autowired
	private AppCommunityService communityService;
	

	
	/**
	 * 栏目列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/discover/community/findSectionList.r
	 * {"langCode":"sc"}
	 */
	@RequestMapping(value = "/findSectionList")
	@ResponseBody
	public ResultWS findSectionList(HttpServletRequest request,@RequestBody String body) {
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


		List<AppCommunitySectionVO> list = communityService.findSectionList(langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 * 栏目帖子接口， 通过body传递参数 
	 * 调用示例:[地址]/service/discover/community/findDynamicTopicList.r
	 * {"page":1, "rows":15,"memberId":"40280ad455e327690155e3291c582000","langCode":"sc"}
	 */
	@RequestMapping(value = "/findDynamicTopicList")
	@ResponseBody
	public ResultWS findDynamicTopicList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		int page = jsonObject.optInt("page", 1);// 获得当前页数
		int rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String memberId = jsonObject.optString("memberId", "");//当前登录人
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);//语言
		
		if ("".equals(memberId) || "".equals(langCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = communityService.findDynamicTopicList(jsonPaging, memberId, langCode);

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
	 * 栏目帖子接口， 通过body传递参数 
	 * 调用示例:[地址]/service/discover/community/findTopicListBySection.r
	 * {"page":1, "rows":15,"memberId":"40280ad455e327690155e3291c582000","sectionId":"0023957f085e11e7a3ba00155d8a2755","langCode":"sc"}
	 */
	@RequestMapping(value = "/findTopicListBySection")
	@ResponseBody
	public ResultWS findTopicListBySection(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		int page = jsonObject.optInt("page", 1);// 获得当前页数
		int rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String memberId = jsonObject.optString("memberId", "");//当前登录人
		String sectionId = jsonObject.optString("sectionId", "");// 获得当前栏目ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);//语言
		
		if ("".equals(memberId) || "".equals(sectionId) || "".equals(langCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = communityService.findTopicListBySection(jsonPaging, memberId, sectionId, langCode);

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
	 * 调用示例:[地址]/service/discover/community/findTopicInfo.r
	 * {"memberId":"40280ad455e327690155e3291c582000","topicId":"40280a295b1855a7015b18de49c7000f","langCode":"sc"}
	 */
	@RequestMapping(value = "/findTopicInfo")
	@ResponseBody
	public ResultWS findTopicInfo(HttpServletRequest request,@RequestBody String body) {
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
		String topicId = jsonObject.optString("topicId","");
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(memberId) || "".equals(topicId) || "".equals(langCode) ) {//"".equals(memberId)||
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}


		AppCommunityTopicVO info = communityService.findTopicInfo(memberId, topicId, langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	
	/**
	 * 新闻点赞/踩接口， 通过body传递参数 
	 * 调用示例:[地址]/service/discover/community/saveTopic.r
	 * {"memberId":"40280ad455e327690155e3291c582000", "title":"社区帖子001", "sectionId":"0023957f085e11e7a3ba00155d8a2755","content":"帖子内容001"}
	 */
	@RequestMapping(value = "/saveTopic")
	@ResponseBody
	public ResultWS saveTopic(HttpServletRequest request,@RequestBody String body) {
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
		String sectionId = jsonObject.optString("sectionId", "");
		String content = jsonObject.optString("content", "");
		String title = jsonObject.optString("title", "");
//		int isInsight = jsonObject.optInt("isInsight", 0);
//		String visitor = jsonObject.optString("visitor", "all");
//		String sourceType = jsonObject.optString("sourceType", "");
//		String sourceId = jsonObject.optString("sourceId", "");
		
		if ("".equals(memberId) || "".equals(sectionId) || "".equals(title) || "".equals(content) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		boolean flag = communityService.saveTopic(jsonObject);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
	
	/**
	 * 评论列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/discover/community/findCommentList.r
	 * {"page":1, "rows":15,"memberId":"40280ad455e327690155e3291c582000","target":"topic","targetId":"40280a295b1855a7015b18de49c7000f","langCode":"sc"}
	 */
	@RequestMapping(value = "/findCommentList")
	@ResponseBody
	public ResultWS findCommentList(HttpServletRequest request,@RequestBody String body) {
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
		int page = jsonObject.optInt("page", 1);// 获得当前页数
		int rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String target = jsonObject.optString("target", "");// topic 帖子 question 问题
		String targetId = jsonObject.optString("targetId", "");// topic 帖子id question 问题id
		String commentType = jsonObject.optString("commentType", "comment");// comment评论/回复；answer 回答（是只有question时才有）
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(memberId) || "".equals(langCode)  || "".equals(targetId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = communityService.findCommentList(jsonPaging, memberId, target, targetId, commentType, langCode);

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
	 * 调用示例:[地址]/service/discover/community/saveBehavior.r
	 * {"memberId":"40280ad455e327690155e3291c582000", "target":"topic", "targetId":"40280a295b1855a7015b18de49c7000f","behaviorType":"like"}
	 */
	@RequestMapping(value = "/saveBehavior")
	@ResponseBody
	public ResultWS saveBehavior(HttpServletRequest request,@RequestBody String body) {
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
		String target = jsonObject.optString("target", "");// topic 帖子 question 问题 comment 评论
		String targetId = jsonObject.optString("targetId", "");// 帖子ID、问题ID、评论ID
		String behaviorType = jsonObject.optString("behaviorType", "");// like 点赞/unlike 踩
		
		if ("".equals(memberId) || "".equals(target) || "".equals(targetId) || "".equals(behaviorType) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		boolean flag = communityService.saveBehavior(memberId, target, targetId, behaviorType);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
	
	/**
	 * 添加评论接口， 通过body传递参数 
	 * 调用示例:[地址]/service/discover/community/addComment.r
	 * {"memberId":"40280ad455e327690155e3291c582000","target":"topic", "targetId":"40280a295b1855a7015b18de49c7000f","comment":"评论内容","parentId":"","commentType":"comment"}
	 */
	@RequestMapping(value = "/addComment")
	@ResponseBody
	public ResultWS addComment(HttpServletRequest request,@RequestBody String body) {
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
		String target = jsonObject.optString("target", "");// 获得当前新闻ID
		String targetId = jsonObject.optString("targetId", "");// 获得当前新闻ID
		String content = jsonObject.optString("content", "");//评论内容
		String parentId = jsonObject.optString("parentId", "");//被回复评论的id
		String commentType = jsonObject.optString("commentType", "");//被回复评论的id
		
		if ("".equals(memberId)|| "".equals(target) || "".equals(targetId) || "".equals(content) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		boolean flag = communityService.addComment(memberId, target, targetId, parentId, commentType, content);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
	
	/**
	 * 社区搜索列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/discover/community/findSearchDataList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"abc","langCode":"sc"}
	 */
	@RequestMapping(value = "/findSearchDataList")
	@ResponseBody
	public ResultWS findSearchDataList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}

		String memberId = jsonObject.optString("memberId","");//当前登录人
		String keyword = jsonObject.optString("keyword","");//查询关键字
//		int page = jsonObject.optInt("page", 1);// 获得当前页数
//		int rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(memberId) || "".equals(langCode)  || "".equals(keyword) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPagingForTopic = new JsonPaging();
		jsonPagingForTopic.setPage(1);
		jsonPagingForTopic.setRows(3);
		jsonPagingForTopic = communityService.findSearchTopicList(jsonPagingForTopic, memberId, keyword, langCode);
						
		JsonPaging jsonPagingForUser = new JsonPaging();
		jsonPagingForUser.setPage(1);
		jsonPagingForUser.setRows(4);
		
		jsonPagingForUser = communityService.findSearchUserList(jsonPagingForUser, memberId,keyword,langCode);
		
		AppCommunitySearchDataVO vo = new AppCommunitySearchDataVO();
		if(null != jsonPagingForTopic){
			vo.setTopicList(jsonPagingForTopic.getList());
		}
		
		if(null != jsonPagingForUser){
			vo.setUserList(jsonPagingForUser.getList());
		}
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	/**
	 * 帖子搜索列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/discover/community/findSearchTopicList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"abc","langCode":"sc"}
	 */
	@RequestMapping(value = "/findSearchTopicList")
	@ResponseBody
	public ResultWS findSearchTopicList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}

		String memberId = jsonObject.optString("memberId","");//当前登录人
		String keyword = jsonObject.optString("keyword","");//查询关键字
		int page = jsonObject.optInt("page", 1);// 获得当前页数
		int rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(memberId) || "".equals(langCode)  || "".equals(keyword) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		jsonPaging = communityService.findSearchTopicList(jsonPaging, memberId, keyword, langCode);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonPaging.getList());
		return result;
	}
	
	/**
	 * 会员列表搜索列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/discover/community/findSearchUserList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"abc","langCode":"sc"}
	 */
	@RequestMapping(value = "/findSearchUserList")
	@ResponseBody
	public ResultWS findSearchUserList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}

		String memberId = jsonObject.optString("memberId","");//当前登录人
		String keyword = jsonObject.optString("keyword","");//查询关键字
		int page = jsonObject.optInt("page", 1);// 获得当前页数
		int rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(memberId) || "".equals(langCode)  || "".equals(keyword) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
					
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = communityService.findSearchUserList(jsonPaging, memberId,keyword,langCode);
		
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonPaging.getList());
		return result;
	}
	
	/**
	 * 新闻内容接口， 通过body传递参数 
	 * 调用示例:[地址]/service/discover/community/findQuestionInfo.r
	 * {"memberId":"40280ad455e327690155e3291c582000","questionId":"40280a295b1855a7015b18de49c7000f","langCode":"sc"}
	 */
	@RequestMapping(value = "/findQuestionInfo")
	@ResponseBody
	public ResultWS findQuestionInfo(HttpServletRequest request,@RequestBody String body) {
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
		String questionId = jsonObject.optString("questionId","");
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(memberId) || "".equals(questionId) || "".equals(langCode) ) {//"".equals(memberId)||
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}


		AppCommunityTopicVO info = communityService.findTopicInfo(memberId, questionId, langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
}
