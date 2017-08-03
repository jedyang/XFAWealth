package com.fsll.app.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.fund.service.AppFundInfoService;
import com.fsll.app.fund.vo.AppFundBasicDataVO;
import com.fsll.app.fund.vo.AppFundListItemVo;
import com.fsll.app.fund.vo.AppFundSearchDetailVO;
import com.fsll.app.fund.vo.AppFundSearchItemVO;
import com.fsll.app.investor.market.service.AppPortfolioArenaMessService;
import com.fsll.app.investor.market.vo.AppIndexHotVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.vo.SysParamConfigVO;
import com.fsll.wmes.entity.FundHouse;
import com.fsll.wmes.entity.FundHouseEn;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebWatchlist;

/**
 * 基金信息查询服务接口实现
 * @author zpzhou
 * @date 2016-6-20
 */
@Service("appFundInfoService")
//@Transactional
public class AppFundInfoServiceImpl extends BaseService implements AppFundInfoService {

	@Autowired
	private AppPortfolioArenaMessService portfolioArenaMessService;
	/**
	 * 得到首页最热基金更多列表信息
	 * @param jsonPaging 分页信息
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 显示语言
	 * @param num 返回的组合列表条数
	 * @return	JsonPaging	分页基金数据
	 */
	public JsonPaging findHotFundList(JsonPaging jsonPaging,String langCode,String periodCode,int num){
		List<AppIndexHotVO> fundList=new ArrayList<AppIndexHotVO>();
		String hql = "select f.id,s.fundName,f.riskLevel,r.increase,count(*) as num,p.id ";
		hql += " from ProductInfo p";
		hql += " right join PortfolioArenaProduct t on t.product.id=p.id ";
		hql += " inner join FundInfo f on f.product.id=p.id ";
		hql += " inner join "+this.getLangString("FundInfo", langCode)+" s on s.id=f.id ";
		hql += " left join FundReturn r on r.fund.id=f.id and r.periodCode=? ";
		hql += " where p.type=? and p.isValid=? GROUP BY t.product.id order by num desc,r.increase desc ";
		List params = new ArrayList();
		params.add(periodCode);
		params.add("fund");
		params.add("1");
		jsonPaging= baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppIndexHotVO  vo = new AppIndexHotVO();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setName(objs[1]==null?"":objs[1].toString());
			vo.setRiskLevel(objs[2]==null?"":objs[2].toString());
			vo.setIncrease(objs[3]==null?"":objs[3].toString());
			vo.setNum(objs[4]==null?"":objs[4].toString());
			if(null==vo.getIncrease() || "".equals(vo.getIncrease())){
				vo.setIncrease(getFormatNumByPer(null));
			}else{
				vo.setIncrease(getFormatNumByPer(Double.parseDouble(vo.getIncrease())));
			}
			String productId=objs[5]==null?"":objs[5].toString();
			if(null!=productId){
				List portfolioList=portfolioArenaMessService.findProductPortfolioList(productId, langCode, periodCode,num);	
				vo.setPortfolioList(portfolioList);
			}
			fundList.add(vo);
		}
		jsonPaging.setList(fundList);
		return jsonPaging;
	}
	/**
	 * 分页查询基金列表信息
	 * @param jsonPaging 分页参数
	 * @param keyWord 搜索关键词
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @param fundType 基金类型
	 * @param periodCode 基金回报类型
	 * @param toCurrency 转换货币
	 * @return
	 */
	public JsonPaging findAllFundList(JsonPaging jsonPaging,String keyWord,String langCode,String userId,String fundType,String periodCode, String toCurrency){
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		List params = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append("select f.id,f.product.id,x.fundName,x.fundCurrency,f.riskLevel, ");
		hql.append(" x.fundType,get_exchange_rate(x.fundCurrencyCode,'"+toCurrency+"',f.lastNav) as lastNav,");
		hql.append(" date_format(f.lastNavUpdate,'%Y-%m-%d'),x.fundCurrencyCode,r.increase,");
		if(null!=userId && !"".equals(userId)){
			hql.append(" case when w.id is null then '0' else '1' end as followFlag ");
		}else{
			hql.append(" '0' as followFlag ");
		}
		hql.append(",x.issueCurrency,x.issueCurrencyCode");
		hql.append(" from FundInfo f ");
		hql.append(" inner join "+this.getLangString("FundInfo", langCode)+" x on f.id=x.id  ");
		
		if(null!=userId && !"".equals(userId)){
			hql.append(" left join WebWatchlist w on w.product.id=f.product.id and w.member.id=? and w.isValid='1' ");
			params.add(userId);
		}
		//股票时间类型:return_period_code_1W,return_period_code_1M,return_period_code_1Y...
		hql.append(" left join FundReturn r on r.fund.id=f.id and r.periodCode=? ");
		params.add(periodCode);
		hql.append(" where f.isValid='1' ");
		
		//基金类型编码,对应基础参数表中fund_type_??
		if(null!=fundType && !"".equals(fundType)){
			hql.append(" and x.fundTypeCode=? ");
			params.add(fundType);
		}
		
		if(null!=keyWord && !"".equals(keyWord)){
			hql.append(" and x.fundName like ? ");
			params.add("%"+keyWord+"%");
		}
		
		if(null!=userId && !"".equals(userId)){
			hql.append(" and exists( SELECT pc.id FROM ProductCompany pc ");
			hql.append(" INNER JOIN MemberCompany mc ON mc.company.id=pc.company.id ");
			hql.append(" WHERE pc.product.id=f.product.id AND mc.member.id=? )");
			params.add(userId);
		}
		
		if ("last_nav".equals(jsonPaging.getSort()) && !"".equals(toCurrency)){
			jsonPaging.setSort("");
			hql.append(" order by f.lastNav "+jsonPaging.getOrder());
		}
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql.toString(), params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		List listTemp=new ArrayList();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size(); i++){
				Object[] objs = (Object[])list.get(i);
				AppFundListItemVo  vo = new AppFundListItemVo();
				vo.setId(objs[0]==null?"":objs[0].toString());
				vo.setProductId(objs[1]==null?"":objs[1].toString());
				vo.setFundName(objs[2]==null?"":objs[2].toString());
				if(StringUtils.isBlank(toCurrency)){
					vo.setFundCurrency(objs[3]==null?"":objs[3].toString());
					vo.setFundCurrencyCode(objs[8]==null?"":objs[8].toString());
				}else {
					vo.setFundCurrency(toCurrencyName);
					vo.setFundCurrencyCode(toCurrency);
				}
				
				vo.setRiskLevel(objs[4]==null?"":objs[4].toString());
				vo.setFundType(objs[5]==null?"":objs[5].toString());
				vo.setLastNav(objs[6]==null?"":objs[6].toString());
				vo.setLastNavUpdate(objs[7]==null?"":objs[7].toString());
				vo.setIncrease(objs[9]==null?"":objs[9].toString());
				vo.setFollowFlag(objs[10]==null?"":objs[10].toString());
				vo.setIssueCurrency(objs[11]==null?"":objs[11].toString());
				vo.setIssueCurrencyCode(objs[12]==null?"":objs[12].toString());
				//数据格式处理
				if(null==vo.getLastNav() || "".equals(vo.getLastNav())){
					vo.setLastNav(getFormatNum(null));
				}else{
					vo.setLastNav(getFormatNum(Double.parseDouble(vo.getLastNav()),toCurrency));
				}
				if(null==vo.getIncrease() || "".equals(vo.getIncrease())){
					vo.setIncrease(getFormatNumByPer(null));
				}else{
					vo.setIncrease(getFormatNumByPer(Double.parseDouble(vo.getIncrease())));
				}
				listTemp.add(vo);
	    	}			
		}
		jsonPaging.setList(listTemp);
		return jsonPaging;
	}
	
	/**
	 * 获取我关注的基金名单列表
	 * @param jsonPaging
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @param dateType 基金回报类型
	 * @param toCurrency 转换货币
	 * @return
	 */
	public JsonPaging getFundFollowList(JsonPaging jsonPaging,String langCode,String userId,String periodCode, String toCurrency) {
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		String hql = "select f.id,f.product.id,s.fundName," +
				" case when w.id is null then '0' else '1' end as followFlag," +
				" s.fundCurrency,s.fundType,f.riskLevel," +
				" get_exchange_rate(s.fundCurrencyCode,'"+toCurrency+"',f.lastNav) as lastNav,date_format(f.lastNavUpdate,'%c-%d') as lastNavUpdate,r.increase,s.fundCurrencyCode ";
		hql +=",s.issueCurrency,s.issueCurrencyCode";
		hql += " from WebWatchlist w ";
		hql += " inner join FundInfo f on f.product.id=w.product.id ";
		hql += " left join "+this.getLangString("FundInfo", langCode);
		hql += " s on s.id=f.id ";
		hql += " left join FundReturn r on r.fund.id=f.id and r.periodCode=? ";
		hql += " where w.isValid='1' and w.member.id=? ";
		List params = new ArrayList();
		params.add(periodCode);
		params.add(userId);
		
		if ("last_nav".equals(jsonPaging.getSort())){
			jsonPaging.setSort("");
			hql += "order by f.lastNav "+jsonPaging.getOrder();
		}
		
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		List listTemp=new ArrayList();
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			AppFundListItemVo  vo = new AppFundListItemVo();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setProductId(objs[1]==null?"":objs[1].toString());
			vo.setFundName(objs[2]==null?"":objs[2].toString());
			vo.setFollowFlag(objs[3]==null?"":objs[3].toString());
			if(StringUtils.isBlank(toCurrency)){
				vo.setFundCurrency(objs[4]==null?"":objs[4].toString());
				vo.setFundCurrencyCode(objs[10]==null?"":objs[10].toString());
			}else {
				vo.setFundCurrency(toCurrencyName);
				vo.setFundCurrencyCode(toCurrency);
			}
			vo.setFundType(objs[5]==null?"":objs[5].toString());
			vo.setRiskLevel(objs[6]==null?"":objs[6].toString());
			vo.setLastNav(objs[7]==null?"":objs[7].toString());
			vo.setLastNavUpdate(objs[8]==null?"":objs[8].toString());
			vo.setIncrease(objs[9]==null?"":objs[9].toString());
			vo.setIssueCurrency(objs[11]==null?"":objs[11].toString());
			vo.setIssueCurrencyCode(objs[12]==null?"":objs[12].toString());
			//数据格式处理
			if(null==vo.getLastNav() || "".equals(vo.getLastNav())){
				vo.setLastNav(getFormatNum(null));
			}else{
				vo.setLastNav(getFormatNum(Double.parseDouble(vo.getLastNav()),toCurrency));
			}
			if(null==vo.getIncrease() || "".equals(vo.getIncrease())){
				vo.setIncrease(getFormatNumByPer(null));
			}else{
				vo.setIncrease(getFormatNumByPer(Double.parseDouble(vo.getIncrease())));
			}
			listTemp.add(vo);
    	}
		jsonPaging.setList(listTemp);
		return jsonPaging;
	}
	
	/**
	 * 搜索基金信息
	 * @param jsonPaging 分页参数
	 * @param keyWord 搜索关键词
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @return
	 */
	public JsonPaging queryFundList(JsonPaging jsonPaging,String keyWord,String langCode,String userId){
		List params = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append("select f.id ,f.product.id,x.fundName,f.isinCode, ");
		if(null!=userId && !"".equals(userId)){
			hql.append(" case when w.id is null then '0' else '1' end as followFlag ");
		}else{
			hql.append(" '0' as followFlag ");
		}
		hql.append(" from FundInfo f ");
		hql.append(" inner join "+this.getLangString("FundInfo", langCode)+" x on f.id=x.id  ");

		if(null!=userId && !"".equals(userId)){
			hql.append(" left join WebWatchlist w on w.product.id=f.product.id and w.member.id=? and w.isValid='1' ");
			params.add(userId);
		}
		hql.append(" where f.isValid='1' ");
		if(null!=keyWord && !"".equals(keyWord)){
			hql.append(" and (x.fundName like ? or f.isinCode like ?) ");
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		
		if(null!=userId && !"".equals(userId)){
			hql.append(" and exists( SELECT pc.id FROM ProductCompany pc ");
			hql.append(" INNER JOIN MemberCompany mc ON mc.company.id=pc.company.id ");
			hql.append(" WHERE pc.product.id=f.product.id AND mc.member.id=? )");
			params.add(userId);
		}
		
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql.toString(), params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			List listTemp=new ArrayList();
			for(int i=0;i<list.size(); i++){
				Object[] objs = (Object[])list.get(i);
				AppFundListItemVo  vo = new AppFundListItemVo();
				vo.setId(objs[0]==null?"":objs[0].toString());
				vo.setProductId(objs[1]==null?"":objs[1].toString());
				vo.setFundName(objs[2]==null?"":objs[2].toString());
				vo.setIsinCode(objs[3]==null?"":objs[3].toString());
				vo.setFollowFlag(objs[4]==null?"":objs[4].toString());
				listTemp.add(vo);
	    	}
			jsonPaging.setList(listTemp);			
		}
		return jsonPaging;
	}
	
	/**
	 * 搜索基金信息
	 * @param jsonPaging 分页参数
	 * @param jsonObject 搜索内容
	 * @return
	 */
	public JsonPaging queryFundListByAdvance(JsonPaging jsonPaging,JSONObject jsonObject){

		String memberId = jsonObject.optString("memberId", "");
		String langCode = jsonObject.optString("langCode", "");
//		String keyword = jsonObject.optString("keyword", "");
		String fundType = jsonObject.optString("fundType", "");
		String riskLevel = jsonObject.optString("riskLevel", "");
		String fundGeo = jsonObject.optString("fundGeo", "");
		String fundSector = jsonObject.optString("fundSector", "");

		String fundSize = jsonObject.optString("fundSize", "");
		String mgtFee = jsonObject.optString("mgtFee", "");
		String minInitialInv = jsonObject.optString("minInitialInv", "");
				
		String return1MonFrom = jsonObject.optString("return1MonFrom", "");
		String return1MonTo = jsonObject.optString("return1MonTo", "");
		String return3MonFrom = jsonObject.optString("return3MonFrom", "");
		String return3MonTo = jsonObject.optString("return3MonTo", "");
		String return6MonFrom = jsonObject.optString("return6MonFrom", "");
		String return6MonTo = jsonObject.optString("return6MonTo", "");
		
		String return1YearFrom = jsonObject.optString("return1YearFrom", "");
		String return1YearTo = jsonObject.optString("return1YearTo", "");
		String return3YearFrom = jsonObject.optString("return3YearFrom", "");
		String return3YearTo = jsonObject.optString("return3YearTo", "");
		String return5YearFrom = jsonObject.optString("return5YearFrom", "");
		String return5YearTo = jsonObject.optString("return5YearTo", "");
		
		String returnYTDFrom = jsonObject.optString("returnYTDFrom", "");
		String returnYTDTo = jsonObject.optString("returnYTDTo", "");
		String returnLaunchFrom = jsonObject.optString("returnLaunchFrom", "");
		String returnLaunchTo = jsonObject.optString("returnLaunchTo", "");
		
		String fundHouse = jsonObject.optString("fundHouse", "");
		String distributor = jsonObject.optString("distributor", "");
		
		
		List params = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append(" from FundInfo f ");
		hql.append(" inner join "+this.getLangString("FundInfo", langCode)+" l on l.id=f.id ");
		hql.append(" left join WebWatchlist w on w.product.id=f.product.id and w.member.id=? and w.isValid='1' ");
		hql.append(" where f.isValid='1' ");
		
		params.add(memberId);
		
//		if(StringUtils.isNotBlank(keyword)){
//			hql.append(" and (x.fundName like ? or f.isinCode like ?) ");
//			params.add("%"+keyword+"%");
//			params.add("%"+keyword+"%");
//		}
		
		if(StringUtils.isNotBlank(memberId)){
			hql.append(" and exists( SELECT pc.id FROM ProductCompany pc ");
			hql.append(" INNER JOIN MemberCompany mc ON mc.company.id=pc.company.id ");
			hql.append(" WHERE pc.product.id=f.product.id AND mc.member.id=? )");
			params.add(memberId);
		}
		
		if(StringUtils.isNotBlank(riskLevel)){
			hql.append(" and f.riskLevel = ? ");
			params.add(jsonObject.optInt("riskLevel", 1));
		}
		
		if(StringUtils.isNotBlank(fundType)){
			hql.append(" and FIND_IN_SET(l.fundTypeCode,?)>0 ");
			params.add(fundType);
		}
		
		if(StringUtils.isNotBlank(fundGeo)){
			hql.append(" and FIND_IN_SET(l.geoAllocationCode,?)>0 ");
			params.add(fundGeo);
		}
		
		if(StringUtils.isNotBlank(fundSector)){
			hql.append(" and FIND_IN_SET(l.sectorTypeCode,?)>0 ");
			params.add(fundSector);
		}
		
		if(StringUtils.isNotBlank(fundHouse)){
			hql.append(" and FIND_IN_SET(l.fundHouseId.id,?)>0 ");
			params.add(fundHouse);
		}
		
		if(StringUtils.isNotBlank(distributor)){
			hql.append(" AND EXISTS ( SELECT d.id FROM ProductDistributor d WHERE d.product.id = f.product.id AND FIND_IN_SET(d.distributor.id,?)>0 ) ");
			params.add(distributor);
		}
		
        if( StringUtils.isNotBlank(mgtFee) ){
        	String mgtFeeFrom = "";
        	String mgtFeeTo = "";
        	if("criteria_fund_mgt_fee_02".equalsIgnoreCase(mgtFee)){
            	mgtFeeTo = "1.0";
        	}else if ("criteria_fund_mgt_fee_03".equalsIgnoreCase(mgtFee)) {
            	mgtFeeTo = "1.25";
			}else if ("criteria_fund_mgt_fee_04".equalsIgnoreCase(mgtFee)) {
            	mgtFeeTo = "1.50";
			}else if ("criteria_fund_mgt_fee_05".equalsIgnoreCase(mgtFee)) {
            	mgtFeeTo = "1.75";
			}else if ("criteria_fund_mgt_fee_06".equalsIgnoreCase(mgtFee)) {
            	mgtFeeTo = "2.0";
			}else if ("criteria_fund_mgt_fee_07".equalsIgnoreCase(mgtFee)) {
        		mgtFeeFrom = "2.0";
			}

            if(StringUtils.isNotBlank(mgtFeeFrom)){
        		hql.append(" and f.mgtFee >= ? ");
        		params.add(Double.parseDouble(mgtFeeFrom));
            }
            
            if(StringUtils.isNotBlank(mgtFeeTo)){
        		hql.append(" and f.mgtFee <= ? ");
        		params.add(Double.parseDouble(mgtFeeTo));
            }
        }
		
        if( null != fundSize && !"".equals(fundSize) ){
        	String fundSizeFrom = "";
        	String fundSizeTo = "";
        	if("criteria_fund_size_01".equalsIgnoreCase(fundSize)){
        		fundSizeTo = "10";
        	}else if ("criteria_fund_size_02".equalsIgnoreCase(fundSize)) {
        		fundSizeFrom = "10";
        		fundSizeTo = "30";
			}else if ("criteria_fund_size_03".equalsIgnoreCase(fundSize)) {
				fundSizeFrom = "30";
        		fundSizeTo = "50";
			}else if ("criteria_fund_size_04".equalsIgnoreCase(fundSize)) {
				fundSizeFrom = "50";
        		fundSizeTo = "100";
			}else if ("criteria_fund_size_05".equalsIgnoreCase(fundSize)) {
				fundSizeFrom = "100";
			}

            if(StringUtils.isNotBlank(fundSizeFrom)){
        		hql.append(" and l.fundSize >= ? ");
        		params.add(Double.parseDouble(fundSizeFrom));
            }
            
            if(StringUtils.isNotBlank(fundSizeTo)){
        		hql.append(" and l.fundSize <= ? ");
        		params.add(Double.parseDouble(fundSizeTo));
            }
        }
		
		if( StringUtils.isNotBlank(minInitialInv) ){
        	String minInitialInvFrom = "";
        	String minInitialInvTo = "";
        	if("criteria_fund_min_initial_amount_01".equalsIgnoreCase(minInitialInv)){
        		minInitialInvFrom = "0";
        		minInitialInvTo = "0";
        	}else if ("criteria_fund_min_initial_amount_02".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "0";
        		minInitialInvTo = "2000";
			}else if ("criteria_fund_min_initial_amount_03".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "2000";
        		minInitialInvTo = "4000";
			}else if ("criteria_fund_min_initial_amount_04".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "4000";
        		minInitialInvTo = "6000";
			}else if ("criteria_fund_min_initial_amount_05".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "6000";
        		minInitialInvTo = "8000";
			}else if ("criteria_fund_min_initial_amount_06".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "8000";
        		minInitialInvTo = "10000";
			}else if ("criteria_fund_min_initial_amount_07".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "10000";
        		minInitialInvTo = "";
			}
        	if( StringUtils.isNotBlank(minInitialInvFrom) ){
        		hql.append(" and f.minInitialAmount >= ? ");
        		params.add(Double.parseDouble(minInitialInvFrom));
        	}
			
			if( StringUtils.isNotBlank(minInitialInvTo) ){
				hql.append(" and f.minInitialAmount <= ? ");
				params.add(Double.parseDouble(minInitialInvTo));
			}
        }
		
		//基金回报筛选条件
		StringBuilder returnHql = new StringBuilder();
		if(StringUtils.isNotBlank(return1MonFrom)){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_1M");
			params.add(StrUtils.getDouble(return1MonFrom));
		}
		if(StringUtils.isNotBlank(return1MonTo)){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_1M");
			params.add(StrUtils.getDouble(return1MonTo));
		}
		if(StringUtils.isNotBlank(return3MonFrom)){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_3M");
			params.add(StrUtils.getDouble(return3MonFrom));
		}
		if(StringUtils.isNotBlank(return3MonTo)){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_3M");
			params.add(StrUtils.getDouble(return3MonTo));
		}
		if(StringUtils.isNotBlank(return6MonFrom)){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_6M");
			params.add(StrUtils.getDouble(return6MonFrom));
		}
		if(StringUtils.isNotBlank(return6MonTo)){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_6M");
			params.add(StrUtils.getDouble(return6MonTo));
		}
		if(StringUtils.isNotBlank(return1YearFrom)){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_1Y");
			params.add(StrUtils.getDouble(return1YearFrom));
		}
		if(StringUtils.isNotBlank(return1YearTo)){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_1Y");
			params.add(StrUtils.getDouble(return1YearTo));
		}
		if(StringUtils.isNotBlank(return3YearFrom)){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_3Y");
			params.add(StrUtils.getDouble(return3YearFrom));
		}
		if(StringUtils.isNotBlank(return3YearTo)){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_3Y");
			params.add(StrUtils.getDouble(return3YearTo));
		}
		if(StringUtils.isNotBlank(return5YearFrom)){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_5Y");
			params.add(StrUtils.getDouble(return5YearFrom));
		}
		if(StringUtils.isNotBlank(return5YearTo)){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_5Y");
			params.add(StrUtils.getDouble(return5YearTo));
		}
		if(StringUtils.isNotBlank(returnYTDFrom)){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_YTD");
			params.add(StrUtils.getDouble(returnYTDFrom));
		}
		if(StringUtils.isNotBlank(returnYTDTo)){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_YTD");
			params.add(StrUtils.getDouble(returnYTDTo));
		}
		if(StringUtils.isNotBlank(returnLaunchFrom)){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_LAUNCH");
			params.add(StrUtils.getDouble(returnLaunchFrom));
		}
		if(StringUtils.isNotBlank(returnLaunchTo)){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_LAUNCH");
			params.add(StrUtils.getDouble(returnLaunchTo));
		}
		if (returnHql.length()>0){
			returnHql = new StringBuilder("select r.fund.id from FundReturn r where r.isValid='1' and r.fund.id is not null ").append(returnHql.toString());
			hql.append(" and f.id in ( "+returnHql.toString()+" ) ");
		}
		
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql.toString(), params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			List<AppFundListItemVo> listTemp=new ArrayList<AppFundListItemVo>();
			for(int i=0;i<list.size(); i++){
				AppFundListItemVo  vo = new AppFundListItemVo();
				Object[] objs = (Object[])list.get(i);
				FundInfo info = (FundInfo) objs[0];
				FundInfoEn infoEn = new FundInfoEn();
				BeanUtils.copyProperties(objs[1], infoEn);
						
				
				vo.setId(info.getId());
				vo.setProductId(info.getProduct().getId());
				vo.setFundName(infoEn.getFundName());
				vo.setIsinCode(info.getIsinCode());
				String followFlag = "0";
				if(null != objs[2]){
					WebWatchlist watchlist = (WebWatchlist) objs[2];
					if(watchlist.getId() != null){
						followFlag = "1";
					}
				}
				vo.setFollowFlag(followFlag);
				listTemp.add(vo);
	    	}
			jsonPaging.setList(listTemp);			
		}
		return jsonPaging;
	}
	
	/**
	 * 得到基金基本信息
	 * @param fundId 基金ID
	 * @param langCode  语言
	 * @param userId 用户ID
	 * @param toCurrency 要转换的货币类型
	 * @return
	 */
	public AppFundBasicDataVO getFundBasicDataMess(String fundId, String langCode, String userId, String toCurrency){
		AppFundBasicDataVO dataVO = new AppFundBasicDataVO();
		List params = new ArrayList();
		String hql = "from "+this.getLangString("FundInfo", langCode)+" t where t.id=?";
		params.add(fundId);
		List list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object object=list.get(0);
			BeanUtils.copyProperties(object,dataVO);//拷贝信息
		}
		FundInfo info = (FundInfo) baseDao.get(FundInfo.class, fundId);
		
		dataVO.setFundId(info.getId());
		dataVO.setProductId(info.getProduct().getId());
		dataVO.setIsinCode(info.getIsinCode());
		dataVO.setRiskLevel(StrUtils.getString(info.getRiskLevel()));
		dataVO.setLastNAVUpdate(DateUtil.dateToDateString(info.getLastNavUpdate(),CoreConstants.DATE_TIME_FORMAT));
		dataVO.setIssueDate(DateUtil.dateToDateString(info.getIssueDate(),CoreConstants.DATE_FORMAT));
		dataVO.setLangCode(langCode);	
		if("".equals(toCurrency)){
			//原始货币
			dataVO.setToCurrency(dataVO.getFundCurrency());
			dataVO.setIssuePrice(getFormatNum(info.getIssuePrice(),dataVO.getFundCurrencyCode()));
			dataVO.setLastNAV(getFormatNum(info.getLastNav(),dataVO.getFundCurrencyCode()));
			dataVO.setMgtFee(getFormatNum(info.getMgtFee(),dataVO.getFundCurrencyCode()));
			dataVO.setMinInitAmount(getFormatNum(info.getMinInitialAmount(),dataVO.getFundCurrencyCode()));
			dataVO.setMinSubscribeAmount(getFormatNum(info.getMinSubscribeAmount(),dataVO.getFundCurrencyCode()));
			dataVO.setMinHoldingAmount(getFormatNum(info.getMinHoldingAmount(),dataVO.getFundCurrencyCode()));
			dataVO.setMinRedemptionAmount(getFormatNum(info.getMinRedemptionAmount(),dataVO.getFundCurrencyCode()));
			dataVO.setMinRspAmount(getFormatNum(info.getMinRspAmount(),dataVO.getFundCurrencyCode()));
		}else {
			//货币转换--兑换率
			
			dataVO.setToCurrency(getParamConfigName(langCode, toCurrency, "currency_type"));
			String fromCurrency = dataVO.getFundCurrencyCode();
			fromCurrency = StringUtils.isBlank(fromCurrency)?CommonConstants.DEF_CURRENCY_HKD:fromCurrency;
			
			Double rate = getExchangeRate(fromCurrency, toCurrency);
//			String issueCurrency = dataVO.getIssueCurrencyCode();
//			Double iRate = getExchangeRate(issueCurrency, toCurrency);
			
			dataVO.setIssuePrice(getFormatNumByRate(info.getIssuePrice(),rate,toCurrency));
			dataVO.setLastNAV(getFormatNumByRate(info.getLastNav(),rate,toCurrency));
			dataVO.setMgtFee(getFormatNumByRate(info.getMgtFee(),rate,toCurrency));
			dataVO.setMinInitAmount(getFormatNumByRate(info.getMinInitialAmount(),rate,toCurrency));
			dataVO.setMinSubscribeAmount(getFormatNumByRate(info.getMinSubscribeAmount(),rate,toCurrency));
			dataVO.setMinHoldingAmount(getFormatNumByRate(info.getMinHoldingAmount(),rate,toCurrency));
			dataVO.setMinRedemptionAmount(getFormatNumByRate(info.getMinRedemptionAmount(),rate,toCurrency));
			dataVO.setMinRspAmount(getFormatNumByRate(info.getMinRspAmount(),rate,toCurrency));
			
		}

		dataVO.setFundSize(dataVO.getFundSize());
		dataVO.setLastFundReturn(getFormatNumByPer(info.getDayReturn()));//百分比
		
		//基金自选状态
		String followStatus = getWebFollowStatus(dataVO.getProductId(),userId);
		dataVO.setFollowStatus(followStatus);
		return dataVO;
	}
	
	/**
	 * 获取基金自选状态
	 * @param productId 产品id
	 * @param memberId 用户ID
	 */
	public String getWebFollowStatus(String productId, String memberId) {
		String hql = "from WebWatchlist t where t.product.id=? and t.member.id=?  and t.isValid='1' ";
		List params = new ArrayList();
		params.add(productId);
		params.add(memberId);
		List<WebFollow> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			return "1";
		}
		return "0";
	}
	
	/**
	 * 根据基金类型统计
	 * @param fundId 同类基金ID
	 * @param typeCode 基金类型编码
	 * @param typeName 基金类型名称
	 * @return
	 */
	public int findFundTotalByType(String fundId, String typeCode, String typeName){
		List<Object> params = new ArrayList<Object>();
	    String hql = "select t.id from FundInfoEn t where 1=1 ";

		if(null!=fundId && !"".equals(fundId)){
			hql += " and t.fundTypeCode = (select max(i.fundTypeCode) from FundInfoEn i where i.fundTypeCode is not null and id=?) ";
			params.add(fundId);
		}
		if(null!=typeCode && !"".equals(typeCode)){
			hql += " and t.fundTypeCode = ?";
			params.add(typeCode);
		}
		if(null!=typeName && !"".equals(typeName)){
			hql += " and t.fundType like ?";
			params.add("%"+typeName+"%");
		}
		List list = null;
		try {
		    list = this.baseDao.find(hql, params.toArray(), false);
		} catch (Exception e) {
			return 0;
		}
	    return (list==null?0:list.size());  
	}
	
	/**
	 * 获取基金搜索的查询条件
	 * @author zxtan
	 * @date 2017-06-01
	 * @param langCode
	 * @return
	 */
	public AppFundSearchDetailVO findFundSearchDetail(String langCode){
		AppFundSearchDetailVO vo = new AppFundSearchDetailVO();
		
		List<AppFundSearchItemVO> fundTypeList = findParamConfigList("fund_type", langCode);
		List<AppFundSearchItemVO> fundGeoList = findParamConfigList("region", langCode);
		List<AppFundSearchItemVO> fundSectorList = findParamConfigList("fund_sector", langCode);
		List<AppFundSearchItemVO> fundMgtFeeList = findParamConfigList("criteria_fund_mgt_fee", langCode);
		List<AppFundSearchItemVO> fundSizeList = findParamConfigList("criteria_fund_size", langCode);
		List<AppFundSearchItemVO> fundMinInitialInvList = findParamConfigList("criteria_fund_min_initial_amount", langCode);
		List<AppFundSearchItemVO> fundHouseList = findFundHouseList(langCode);
		List<AppFundSearchItemVO> distributorList = findDistributorList();
		
		vo.setFundTypeList(fundTypeList);
		vo.setFundGeoList(fundGeoList);
		vo.setFundSectorList(fundSectorList);
		vo.setFundSizeList(fundSizeList);
		vo.setFundMgtFeeList(fundMgtFeeList);
		vo.setFundMinInitialInvList(fundMinInitialInvList);
		vo.setFundHouseList(fundHouseList);
		vo.setDistributorList(distributorList);
		return vo;
	}
	
	private List<AppFundSearchItemVO> findParamConfigList(String typeCode,String langCode){
		List<AppFundSearchItemVO> voList = new ArrayList<AppFundSearchItemVO>();
		List<SysParamConfig> list = getParamConfigListByTypeCode(typeCode);
		if(null != list){
			for (int i = 0; i < list.size(); i++) {
				AppFundSearchItemVO vo = new AppFundSearchItemVO();
				SysParamConfig info = list.get(i);
				vo.setCode(info.getConfigCode());
				if(CommonConstants.LANG_CODE_EN.equalsIgnoreCase(langCode)){
					vo.setName(info.getNameEn());
				}else if(CommonConstants.LANG_CODE_SC.equalsIgnoreCase(langCode)){
					vo.setName(info.getNameSc());
				}else if(CommonConstants.LANG_CODE_TC.equalsIgnoreCase(langCode)){
					vo.setName(info.getNameTc());
				}
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	
	private List<AppFundSearchItemVO> findFundHouseList(String langCode){
		List<AppFundSearchItemVO> voList = new ArrayList<AppFundSearchItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from FundHouse t ");
		hql.append(" inner join "+getLangString("FundHouse", langCode)+ " s on s.id=t.id ");
		hql.append(" where t.isValid='1' ");
		hql.append(" Order by s.houseName ");
		List list = baseDao.find(hql.toString(), null, false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppFundSearchItemVO vo = new AppFundSearchItemVO();
				Object[] objs = (Object[]) list.get(i);
				FundHouse info = (FundHouse) objs[0];
				FundHouseEn infoEn = new FundHouseEn();
				BeanUtils.copyProperties(objs[1], infoEn);
				vo.setCode(info.getId());
				vo.setName(infoEn.getHouseName());
				voList.add(vo);
			}
		}
		return voList;
	}
	
	private List<AppFundSearchItemVO> findDistributorList(){
		List<AppFundSearchItemVO> voList = new ArrayList<AppFundSearchItemVO>();
		String hql = "from MemberDistributor t Order by t.companyName ";
		List<MemberDistributor> list = baseDao.find(hql, null, false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppFundSearchItemVO vo = new AppFundSearchItemVO();
				MemberDistributor info = list.get(i);
				vo.setCode(info.getId());
				vo.setName(info.getCompanyName());
				voList.add(vo);
			}
		}
		return voList;
	}
}
