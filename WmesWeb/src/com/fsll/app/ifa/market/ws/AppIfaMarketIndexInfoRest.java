package com.fsll.app.ifa.market.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.ifa.market.service.AppIndexInfoService;
import com.fsll.app.ifa.market.vo.AppFollowArenaItemVO;
import com.fsll.app.ifa.market.vo.AppHotTopicItemVO;
import com.fsll.app.ifa.market.vo.AppMarketIndexVO;
import com.fsll.app.ifa.market.vo.AppNoticeItemVO;
import com.fsll.app.ifa.market.vo.AppStrategyInfoItemVO;
import com.fsll.app.ifa.market.vo.AppWatchFundItemVO;
import com.fsll.app.ifa.market.vo.AppWatchTypeVO;
import com.fsll.common.CommonConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * IFA Market 首页接口服务接口
 * @author zxtan
 * @date 2017-03-14
 */
@RestController
@RequestMapping("/service/ifa/market")
public class AppIfaMarketIndexInfoRest extends WmesBaseRest {

	@Autowired
	private AppIndexInfoService indexInfoService;
	/**
	 *获取自选分类信息
	 * 调用示例:[地址]/service/ifa/market/findIndexInfo.r
	 * {"memberId":"40280ad65601004c0156010188390011","periodCode":"return_period_code_1M","langCode":"sc"}
	 */
	@RequestMapping(value = "/findIndexInfo")
	@ResponseBody
	public ResultWS findIndexInfo(HttpServletRequest request,@RequestBody String body){
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
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		if("".equals(memberId)||"".equals(periodCode)||"".equals(langCode)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppMarketIndexVO indexVO = new AppMarketIndexVO();
		List<AppWatchTypeVO> watchTypeList = indexInfoService.findMyWatchTypeList(memberId, periodCode, langCode);
		List<AppFollowArenaItemVO> portfolioList = indexInfoService.findMyFollowPortfolioList(memberId, periodCode, langCode);
		List<AppStrategyInfoItemVO> strategyList = indexInfoService.findMyFollowStrategyList(memberId, langCode);
		List<AppHotTopicItemVO> hotTopicList = indexInfoService.findHotTopicList(memberId, langCode);
		List<AppNoticeItemVO> noticeList = indexInfoService.findNoticeList(memberId);
		boolean ifWatchFund = true;
		boolean ifFollowPortfolio = true;
		boolean ifFollowStrategy = true;
		if(null == watchTypeList || watchTypeList.isEmpty()){
			List<AppWatchFundItemVO> visitedFundList = indexInfoService.findVisitedFundList(memberId, periodCode, langCode);
			indexVO.setVisitedFundList(visitedFundList);
			ifWatchFund = false;
		}
		
		if(null == portfolioList || portfolioList.isEmpty()){
			portfolioList = indexInfoService.findVisitedPortfolioList(memberId, periodCode, langCode);
			ifFollowPortfolio = false;
		}
		
		if(null == strategyList || strategyList.isEmpty()){
			strategyList = indexInfoService.findVisitedStrategyList(memberId, langCode);
			ifFollowStrategy = false;
		}
		

		indexVO.setIfWatchFund(ifWatchFund);
		indexVO.setIfFollowPortfolio(ifFollowPortfolio);
		indexVO.setIfFollowStategy(ifFollowStrategy);		
		indexVO.setWatchTypeList(watchTypeList);
		indexVO.setPortfolioList(portfolioList);
		indexVO.setStrategyList(strategyList);
		indexVO.setHotTopicList(hotTopicList);
		indexVO.setNoticeList(noticeList);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(indexVO);
		return result;
	}
}
