package com.fsll.app.investor.me.ws;

import it.sauronsoftware.base64.Base64;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.elasticsearch.indices.breaker.AllCircuitBreakerStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.me.service.AppInvestService;
import com.fsll.app.investor.me.vo.AppInvestPortfolioAllocationForRebalanceVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioForBuyVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioForCheckVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioForRebalanceVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioReturnForRebalanceVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioReturnVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioVO;
import com.fsll.app.investor.me.vo.AppInvestProductVO;
import com.fsll.app.investor.me.vo.AppPieChartItemVO;
import com.fsll.app.member.service.AppMemberBaseService;
import com.fsll.buscore.fund.service.CoreFundService;
import com.fsll.buscore.fund.vo.CoreFundNavVO;
import com.fsll.common.CommonConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.oms.service.ITraderSendService;
import com.fsll.oms.vo.OmsAddOrderVO;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberAccountSso;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanProduct;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.trade.service.TradeCheckService;
import com.fsll.wmes.trade.service.TradeOrderService;

/**
 * 基金信息查询服务接口实现
 * 
 * @author zxtan
 * @date 2017-01-20
 */
@RestController
@RequestMapping("/service/me/invest")
public class AppInvestRest extends WmesBaseRest {
	@Autowired
	private AppInvestService investService;
	@Autowired
	private ITraderSendService iTraderSendService;
	
	@Autowired
	private AppMemberBaseService memberBaseService;
	@Autowired
	private TradeOrderService tradeOrderService;
	@Autowired
	private TradeCheckService tradeCheckService;
	@Autowired
	private CoreFundService coreFundService;
	
	
	/**
	 * 
	 * 组合分析产品列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/findWatchProductList.r
	 * {"memberId":"40280ad8888327690155e3291c580000","langCode":"sc" }
	 */
	@RequestMapping(value = "/findWatchProductList")
	@ResponseBody
	public ResultWS findWatchProductList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String memberId = jsonObject.optString("memberId", "");// 投资者Member ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE );// 获得当前Ifa ID		
				
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
								
		List<AppInvestProductVO> voList = investService.findWatchProductList(memberId,langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(voList);
		return result;
	}
	
	/**
	 * 
	 * 组合分析产品列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/findInvestPortfolio.r
	 * {"productIds":"FUND_000000000000001,FUND_000000000000051","productRates":"","langCode":"sc","periodCode":"return_period_code_YTD","toCurrency":"" }
	 */
	@RequestMapping(value = "/findInvestPortfolio")
	@ResponseBody
	public ResultWS findInvestPortfolio(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String productIds = jsonObject.optString("productIds", "");// 选中产品ID串，以逗号隔开
		String productRates = jsonObject.optString("productRates", "");// 选中产品占比（小数），以逗号隔开
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE );// 获得当前Ifa ID		
		String toCurrency = jsonObject.optString("toCurrency", "");// 获得转换货币
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);// 获得回报周期
				
		if ("".equals(productIds)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
						
		AppInvestPortfolioVO portfolioVO = investService.findInvestPortfolio(productIds,productRates, langCode, toCurrency, periodCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(portfolioVO);
		return result;
	}
	
	/**
	 * 
	 * 组合分析产品回报接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/findInvestPortfolioReturn.r
	 * {"productIds":"FUND_000000000000001,FUND_000000000000051","productRates":"0.6,0.4","periodCode":"return_period_code_YTD" }
	 */
	@RequestMapping(value = "/findInvestPortfolioReturn")
	@ResponseBody
	public ResultWS findInvestPortfolioReturn(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		
		String productIds = jsonObject.optString("productIds", "");// 选中产品ID串，以逗号隔开
		String productRates = jsonObject.optString("productRates", "");// 选中产品占比（小数），以逗号隔开
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);// 获得回报周期
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String frequencyType = periodCode.replace("return_period_code_", "");
		String[] arrProduct = productIds.split(",");
		String[] arrRate = productRates.split(",");
		
		if ("".equals(productIds) || "".equals(productRates) || arrProduct.length != arrRate.length ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
						
//		List<AppInvestPortfolioReturnVO> list = investService.findProductReturn(productIds,productRates, periodCode);

		//改用统一方法
		List<CoreFundNavVO> list = coreFundService.getMoreFundReturnRateForAPP(productIds, productRates, frequencyType, langCode);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 * 
	 * 组合购买产品列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/findInvestPortfolioForBuy.r
	 * {"memberId":"40280a25559b852e01559b89c80f0004","productIds":"FUND_000000000000001,FUND_000000000000051","productRates":"0.6,0.4","langCode":"sc","toCurrency":"HKD" }
	 */
	@RequestMapping(value = "/findInvestPortfolioForBuy")
	@ResponseBody
	public ResultWS findInvestPortfolioForBuy(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}

		String memberId = jsonObject.optString("memberId", "");//投资者MemberID
		String holdId = jsonObject.optString("holdId", "");//持仓组合ID
		String productIds = jsonObject.optString("productIds", "");// 选中产品ID串，以逗号隔开
		String productRates = jsonObject.optString("productRates", "");// 选中产品占比（小数），以逗号隔开
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE );// 获得当前语言
		String toCurrency = jsonObject.optString("toCurrency", "");// 获得转换货币
		String[] arrProduct = productIds.split(",");
		String[] arrRate = productRates.split(",");
		
		if ("".equals(memberId)||"".equals(productIds)||"".equals(productRates)|| arrProduct.length != arrRate.length ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
						
		AppInvestPortfolioForBuyVO portfolioVO = investService.findInvestPortfolioForBuy(memberId,holdId, productIds, productRates, langCode, toCurrency);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(portfolioVO);
		return result;
	}
	
	
	/**
	 * 
	 * 组合购买，保存草稿接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/saveInvestPortfolioOrderDraft.r
	 * {"memberId":"40280a25559b852e01559b89c80f0004","portfolioHold":{"portfolioName":"portfolioHold_test001","baseCurrency":"HKD"},"orderPlan":{"totalBuy":1000.00,"aipFlag":"0","baseCurrency":"HKD"},"orderPlanProduct":[	{"productId":"FUND_000000000000001","amount":600.00,"weight":"0.6","dividend":"R","accountId":"40280ad456020d5e015602108b930003","accountNo":"mic1232"},	{"productId":"FUND_000000000000051","amount":400.00,"weight":"0.4","dividend":"D","accountId":"40280ad456020per01a002108b93886","accountNo":"aa011010102"}]}
	 */
	@RequestMapping(value = "/saveInvestPortfolioOrderDraft",method=RequestMethod.POST)
	@ResponseBody
	public ResultWS saveInvestPortfolioOrderDraft(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
			
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE );// 获得当前语言
		String memberId = jsonObject.optString("memberId", "");//投资者MemberID
		String ifaCheck = jsonObject.optString("ifaCheck", "0");//Ifa Check,1:Yes,0:No
		String ifaMemberId = jsonObject.optString("ifaMemberId", "");//Ifa MemberID
		JSONObject holdObject = jsonObject.getJSONObject("portfolioHold");
		JSONObject planObject = jsonObject.getJSONObject("orderPlan");
		JSONArray productArray = jsonObject.getJSONArray("orderPlanProduct");

		
		if ("".equals(memberId) || ("1".equals(ifaCheck) && "".equals(ifaMemberId)) || holdObject == null || planObject==null || productArray==null ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
						
		OrderPlan orderPlan = investService.saveInvestPortfolioOrderDraft(memberId, holdObject, planObject, productArray,ifaCheck,ifaMemberId,langCode);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(orderPlan);
		return result;
	}
	
	/**
	 * 
	 * 投资账号登录验证接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/saveInvestorAccountLogin.r
	 * {"accountNo":"accountNo-001","accountPwd":"pwd123456"}
	 */
	@RequestMapping(value = "/saveInvestorAccountLogin",method=RequestMethod.POST)
	@ResponseBody
	public ResultWS saveInvestorAccountLogin(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
			

		String accountNo = jsonObject.optString("accountNo", "");//投资账号
		String accountPwd = jsonObject.optString("accountPwd", "");//投资账号交易密码		
				
		if ("".equals(accountNo) || "".equals(accountPwd) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		String accountType = "1";
		Map<String, Object> resultData = new HashMap<String, Object>();
		accountPwd = Base64.decode(accountPwd, "UTF-8");
		MemberAccountSso sso = iTraderSendService.saveLogin(request, accountType, accountNo, accountPwd);
		boolean flag = false;
		if(sso != null){
			flag = true;
			String pinPos = sso.getPinPos();
			resultData.put("pinPos", pinPos);
		}else {
			resultData.put("pinPos", "");
		}
		resultData.put("flag", flag);
//		resultData.put("flag", true);
//		resultData.put("pinPos", "2,4,5");
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(resultData);
		return result;
	}
	
	/**
	 * 
	 * 投资账号登录Pin码验证接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/saveInvestorAccountLogin.r
	 * {"accountNo":"accountNo-001","pinCode":"2,4,5"}
	 */
	@RequestMapping(value = "/saveInvestorAccountPin",method=RequestMethod.POST)
	@ResponseBody
	public ResultWS saveInvestorAccountPin(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
			
		String accountNo = jsonObject.optString("accountNo", "");//投资账号
		String pinCode = jsonObject.optString("pinCode", "");//投资账号交易密码		
				
		if ("".equals(accountNo) || "".equals(pinCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		String accountType = "1";
		Map<String, Object> map = new HashMap<String, Object>();
		pinCode = Base64.decode(pinCode, "UTF-8");
		
		boolean flag = false;
		flag = iTraderSendService.savePin(request, accountType,accountNo, pinCode);
		
		map.put("flag", flag);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
	
	
	
	/**
	 * 
	 * 组合购买产品列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/addInvestPortfolioOrder.r
	 * {"memberId":"40280ad655bfe9820155bfef8921000c","langCode":"sc","planId":"40280ad55a364f67015a36610186000b","accountList":[{"accountNo":"Account01","password":"***"},{"accountNo":"Account02","password":"***"}]}
	 */
	@RequestMapping(value = "/addInvestPortfolioOrder",method=RequestMethod.POST)
	@ResponseBody
	public ResultWS addInvestPortfolioOrder(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId", "");
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String planId = jsonObject.optString("planId", "");
		String accountData = jsonObject.optString("accountList", "");
//		JSONArray accountArray = jsonObject.getJSONArray("accountList");
		MemberBase member = memberBaseService.findById(memberId);
				
		if (null == member ||"".equals(planId) ||"".equals(accountData)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		boolean flag = false;
		flag = tradeOrderService.addOrderPlan(request, member, langCode, planId, accountData);
		
		/*OrderPlan orderPlan = investService.findOrderPlanById(planId);
		PortfolioHold hold = orderPlan.getPortfolioHold();
		for (int i = 0; i < accountArray.size(); i++) {
			JSONObject accountObject = accountArray.getJSONObject(i);
			String accountNo = accountObject.optString("accountNo", "");//投资账号
			String accountPwd = accountObject.optString("accountPwd", "");//投资账号交易密码		
			
			List<OrderPlanProduct> orderPlanProducts = investService.findOrderPlanProduct(planId, accountNo);
			
			if(orderPlanProducts != null && !orderPlanProducts.isEmpty()){
				for (OrderPlanProduct orderPlanProduct : orderPlanProducts) {
					ProductInfo productInfo = orderPlanProduct.getProduct();
					FundInfo fundInfo = investService.findFundInfoByProduct(productInfo.getId());
	//				FundInfoEn fundInfoEn = fundInfoService.findFundInfoEnById(fundInfo.getId());
					InvestorAccount account = orderPlanProduct.getAccount();
					String aeCode = investService.findAECode(account.getIfa().getId(),account.getDistributor().getId());
					String symbolCode = investService.findSymbolCode(orderPlanProduct.getProduct().getId(),account.getDistributor().getId());
					OmsAddOrderVO omsOrder = new OmsAddOrderVO();
					omsOrder.setAccountNo(account.getAccountNo());
					omsOrder.setCreatorNo(account.getAccountNo()); //下单人的 accountNo
					omsOrder.setAeCode(aeCode);
					// history
					OrderHistory history = new OrderHistory();  
					history.setAccount(account);
					history.setAccountNo(account.getAccountNo());
					history.setCommissionAmount(orderPlanProduct.getAmount()); // 参考交易金额
					history.setCommissionPrice(fundInfo.getLastNav());  // 委托单价 
					history.setCommissionUnit(orderPlanProduct.getUnit()); // 委托份额
					history.setCurrencyCode(orderPlanProduct.getTranFeeCur()); // 基金产品货币
					history.setFee(null); // 当次交易费用
					history.setIfa(account.getIfa());
					history.setIfAip(orderPlan.getAipFlag());
					history.setMember(orderPlan.getCreator());
					history.setOrderDate(new Date());
					
					String tranType = "0";		
					if(CommonConstants.TRAN_TYPE_BUY.equals(orderPlanProduct.getTranType())){
						tranType = "0";
						history.setOrderType("Buy");
					}else {
						tranType = "1";
						history.setOrderType("Sell");
					}					
					omsOrder.setLimitedPrice(orderPlanProduct.getAmount()); // 买入金额（增持时为买入总金额，减持为产品净值）

					history.setPlan(orderPlan);
					history.setPortfolioHold(hold);
					history.setProduct(productInfo);
					//history.setProductName();
					history.setStatus(null);
					history.setTransactionAmount(null);
					history.setTransactionUnit(null);
					history.setUpdateTime(new Date());
					history = investService.saveOrderHistory(history);
					
					omsOrder.setOrderNO(history.getId()); 
					omsOrder.setSymbolCode(symbolCode);
					omsOrder.setTradingPassword(accountPwd);
					omsOrder.setTranType(tranType);
					// 调用交易接口
					boolean orderFlag = iTraderSendService.addOrder(request, omsOrder);
					if(orderFlag){
						// 更新组合持仓帐户绑定表
						investService.updatePortfolioHoldAccount(hold.getId(),hold.getMember().getId(),account.getId(),account.getAccountNo());
						flag = true;
					}
				}
			}
			
		}
		
		investService.updateOrderPlan(planId);*/
		
		Map<String, Object> resultData = new HashMap<String, Object>();
		resultData.put("flag", flag);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(resultData);
		return result;
	}
	
	/**
	 * 
	 * 组合购买，保存草稿接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/saveInvestPortfolioOrderDraftForRebalance.r
	 * {"memberId":"40280a25559b852e01559b89c80f0004","portfolioHold":{"portfolioName":"portfolioHold_test001","baseCurrency":"HKD"},"orderPlan":{"totalBuy":1000.00,"aipFlag":"0","baseCurrency":"HKD"},"orderPlanProduct":[	{"productId":"FUND_000000000000001","amount":600.00,"weight":"0.6","dividend":"R","accountId":"40280ad456020d5e015602108b930003","accountNo":"mic1232"},	{"productId":"FUND_000000000000051","amount":400.00,"weight":"0.4","dividend":"D","accountId":"40280ad456020per01a002108b93886","accountNo":"aa011010102"}]}
	 */
	@RequestMapping(value = "/saveInvestPortfolioOrderDraftForRebalance",method=RequestMethod.POST)
	@ResponseBody
	public ResultWS saveInvestPortfolioOrderDraftForRebalance(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
			
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE );// 获得当前语言
		String memberId = jsonObject.optString("memberId", "");//投资者MemberID
		String ifaCheck = jsonObject.optString("ifaCheck", "0");//Ifa Check,1:Yes,0:No
		String ifaMemberId = jsonObject.optString("ifaMemberId", "");//Ifa MemberID
		String portfolioHoldId = jsonObject.optString("portfolioHoldId", "");//组合ID
		String orderPlanId = jsonObject.optString("orderPlanId", "");//交易计划Id
		JSONArray productArray = jsonObject.getJSONArray("orderPlanProduct");

		
		if ("".equals(memberId) || ("1".equals(ifaCheck) && "".equals(ifaMemberId))|| "".equals(portfolioHoldId) || productArray==null ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
						
		OrderPlan orderPlan = investService.saveInvestPortfolioOrderDraftForRebalance(memberId, portfolioHoldId, orderPlanId, productArray,ifaCheck,ifaMemberId,langCode);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(orderPlan);
		return result;
	}
	
	/**
	 * 
	 * 组合调整组合分析，产品配置接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/findProductAllocationForRebalance.r
	 * {"memberId":"40280a25559b852e01559b89c80f0004","portfolioHold":{"portfolioName":"portfolioHold_test001","baseCurrency":"HKD"},"orderPlan":{"totalBuy":1000.00,"aipFlag":"0","baseCurrency":"HKD"},"orderPlanProduct":[	{"productId":"FUND_000000000000001","amount":600.00,"weight":"0.6","dividend":"R","accountId":"40280ad456020d5e015602108b930003","accountNo":"mic1232"},	{"productId":"FUND_000000000000051","amount":400.00,"weight":"0.4","dividend":"D","accountId":"40280ad456020per01a002108b93886","accountNo":"aa011010102"}]}
	 */
	@RequestMapping(value = "/findProductAllocationForRebalance",method=RequestMethod.POST)
	@ResponseBody
	public ResultWS findProductAllocationForRebalance(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
			

		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);//投资者MemberID
		String portfolioHoldId = jsonObject.optString("portfolioHoldId", "");//组合ID
		String orderPlanId = jsonObject.optString("orderPlanId", "");//交易计划Id

		
		if ("".equals(langCode) || "".equals(portfolioHoldId)  || "".equals(orderPlanId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppInvestPortfolioAllocationForRebalanceVO allocationVO = new AppInvestPortfolioAllocationForRebalanceVO();
		
		AppInvestPortfolioForRebalanceVO holdDetail = investService.findProductAllocationForRebalance(portfolioHoldId, "", langCode);		
		AppInvestPortfolioForRebalanceVO adjustDetail = investService.findProductAllocationForRebalance(portfolioHoldId, orderPlanId, langCode);		
		allocationVO.setHoldDetail(holdDetail);
		allocationVO.setAdjustDetail(adjustDetail);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(allocationVO);
		return result;
	}
	
	
	/**
	 * 
	 * 组合调整里的组合分析产品回报接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/findInvestPortfolioReturnForRebalance.r
	 * {"holdInfo":{"productIds":"FUND_000000000000001,FUND_000000000000051","productRates":"0.6,0.4" },
		"adjustInfo":{"productIds":"FUND_000000000000002,FUND_000000000000031","productRates":"0.7,0.3" },
		"periodCode":"return_period_code_YTD"
		}
	 */
	@RequestMapping(value = "/findInvestPortfolioReturnForRebalance")
	@ResponseBody
	public ResultWS findInvestPortfolioReturnForRebalance(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		JSONObject holdObject = jsonObject.optJSONObject("holdInfo");
		JSONObject adjustObject = jsonObject.optJSONObject("adjustInfo");
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);// 获得回报周期
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String frequencyType = periodCode.replace("return_period_code_", "");
		
		if ( "".equals(periodCode) || holdObject.isNullObject() || adjustObject.isNullObject() ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		AppInvestPortfolioReturnForRebalanceVO returnDetail = new AppInvestPortfolioReturnForRebalanceVO();
		
		String productIds = holdObject.optString("productIds", "");// 选中产品ID串，以逗号隔开
		String productRates = holdObject.optString("productRates", "");// 选中产品占比（小数），以逗号隔开
		
		String[] arrProduct = productIds.split(",");
		String[] arrRate = productRates.split(",");
		
		if ("".equals(productIds) || "".equals(productRates) || arrProduct.length != arrRate.length ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}		
						
//		List<AppInvestPortfolioReturnVO> holdReturnList = investService.findProductReturn(productIds,productRates, periodCode);
		List<CoreFundNavVO> holdReturnList = coreFundService.getMoreFundReturnRateForAPP(productIds, productRates, frequencyType, langCode);
		returnDetail.setHoldReturnList(holdReturnList);
		
		productIds = adjustObject.optString("productIds", "");// 选中产品ID串，以逗号隔开
		productRates = adjustObject.optString("productRates", "");// 选中产品占比（小数），以逗号隔开
		
		arrProduct = productIds.split(",");
		arrRate = productRates.split(",");
		
		if ("".equals(productIds) || "".equals(productRates) || arrProduct.length != arrRate.length ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}		
						
//		List<AppInvestPortfolioReturnVO> adjustReturnList = investService.findProductReturn(productIds,productRates, periodCode);
		List<CoreFundNavVO> adjustReturnList = coreFundService.getMoreFundReturnRateForAPP(productIds, productRates, frequencyType, langCode);
		returnDetail.setAdjustReturnList(adjustReturnList);		
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(returnDetail);
		return result;
	}
	
	/**
	 * 
	 * 组合确认购买产品列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/findInvestPortfolioForCheck.r
	 * {"memberId":"40280ad65601004c0156010188390021","orderPlanId":"40280ad55a3fdb67015a40032eae0015","langCode":"sc"}
	 */
	@RequestMapping(value = "/findInvestPortfolioForCheck")
	@ResponseBody
	public ResultWS findInvestPortfolioForCheck(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}

		String memberId = jsonObject.optString("memberId", "");//member ID
		String orderPlanId = jsonObject.optString("orderPlanId", "");//交易计划ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE );// 获得当前语言	
				
		if ("".equals(memberId)||"".equals(orderPlanId)||"".equals(langCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
								
		AppInvestPortfolioForCheckVO portfolioVO = investService.findInvestPortfolioForCheck(memberId,orderPlanId, langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(portfolioVO);
		return result;
	}
	
	/**
	 * 
	 * 投资账号登录Pin码验证接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/updateOrderPlanCheck.r
	 * {"id":"40280ad55ab7bde8015ab7f8e5d10014","planId":"40280ad55ab7bde8015ab7f8e5d10013","memberId":"40280ad655bfe9820155bfef8921000c","checkStatus":"1","checkResult":"审核意见内容"}
	 */
	@RequestMapping(value = "/updateOrderPlanCheck",method=RequestMethod.POST)
	@ResponseBody
	public ResultWS updateOrderPlanCheck(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
			
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String id = jsonObject.optString("id", "");//待审核的记录Id
		String planId = jsonObject.optString("planId", "");//投资计划Id		
		String memberId = jsonObject.optString("memberId", "");//审核人 memberId
		String checkStatus = jsonObject.optString("checkStatus", "");//审核意见，1通过，2不通过	
		String checkResult = jsonObject.optString("checkResult", "");//审核结果
		String checkIp = getRemoteHost(request);//Ip Addr	
		MemberBase member = memberBaseService.findById(memberId);
		
		if ("".equals(id) || "".equals(planId)|| "".equals(memberId) || "".equals(checkStatus)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
//		boolean flag = investService.updateOrderPlanCheck(id, planId, memberId, checkStatus, checkResult, checkIp);
		boolean flag = tradeCheckService.saveApprovalPlan(member, langCode, planId, checkStatus, checkResult, checkIp);
		map.put("flag", flag);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
	
	/**
	 * 
	 * 删除草稿状态的交易计划接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/invest/deleteOrderPlanDraft.r
	 * {"planId":"40280ad55adaecc2015adbc0df86001d","memberId":"40280ad655bfe9820155bfef8921000c"}
	 */
	@RequestMapping(value = "/deleteOrderPlanDraft",method=RequestMethod.POST)
	@ResponseBody
	public ResultWS deleteOrderPlanDraft(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
			
		String planId = jsonObject.optString("planId", "");//投资计划Id		
		String memberId = jsonObject.optString("memberId", "");//计划创建人 memberId
				
		if ( "".equals(planId)|| "".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean flag = false;
		flag = investService.deleteOrderPlanDraft(planId, memberId);
		
		map.put("flag", flag);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
}
