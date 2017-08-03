package com.fsll.app.investor.market.ws;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.market.service.AppIndexService;
import com.fsll.app.investor.market.vo.AppIndexBbsTopicVO;
import com.fsll.app.investor.market.vo.AppIndexHotVO;
import com.fsll.app.investor.market.vo.AppIndexNoMissVo;
import com.fsll.app.investor.market.vo.AppIndexTopCategoryVO;
import com.fsll.app.investor.market.vo.AppIndexVo;
import com.fsll.app.investor.market.vo.AppNoticeItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 首页接口服务
 * @author zpzhou
 * @date 2016-10-31
 */
@RestController
@RequestMapping("/service/index")
public class AppIndexRest extends WmesBaseRest{
	@Autowired
	private AppIndexService indexService;
	
	/**
	 * 得到首页所有数据接口， 通过body传递参数 
	 * 调用示例:[地址]/service/index/getIndexAllMess.r
	 *  {"langCode":"sc","periodCode":"return_period_code_1M","num":3,"typeId":"0","moduleId":""}
	 */
	@RequestMapping(value = "/getIndexAllMess")
	@ResponseBody
	public ResultWS getIndexAllMess(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId", "");// 用户Id
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		String periodCode = jsonObject.optString("periodCode",CommonConstants.RETURN_PERIOD_CODE_1M);
		int num = jsonObject.optInt("num", 5);//返回条数
		String typeId = jsonObject.optString("typeId", "");//类型ID  0:最热门  1：推荐  2:模块
		String moduleId = jsonObject.optString("moduleId", "");//模块ID  当typeId=2时不能为空
		if ("".equals(memberId) ||"".equals(typeId) || ("2".equals(typeId) && "".equals(moduleId))) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		AppIndexVo vo=new AppIndexVo();
		List<AppIndexHotVO> fundList= indexService.findIndexHotFund(langCode,periodCode,num);
		List<AppIndexHotVO> portfolioList= indexService.findIndexHotPortfolio(memberId,langCode,periodCode,num);
		List<AppIndexNoMissVo> noMissList= indexService.findIndexNoMiss(memberId,langCode,periodCode);
		List<AppIndexTopCategoryVO> topList= indexService.findIndexTopCategory(langCode);
		List<AppIndexBbsTopicVO> topicList= indexService.findIndexTopicList(typeId,moduleId,num);
		List<AppNoticeItemVO> noticeList = null;
		String errorMsg = "";
		try {
			noticeList = indexService.findNoticeList(memberId,num);
		} catch (Exception e) {
			// TODO: handle exception
			noticeList = new ArrayList<AppNoticeItemVO>();
			errorMsg = e.getMessage();
		}
		
		vo.setFundList(fundList);
		vo.setPortfolioList(portfolioList);
		vo.setNoMissList(noMissList);
		vo.setTopList(topList);
		vo.setTopicList(topicList);
		vo.setNoticeList(noticeList);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg(errorMsg);
		result.setData(vo);
		return result;
	}
}
