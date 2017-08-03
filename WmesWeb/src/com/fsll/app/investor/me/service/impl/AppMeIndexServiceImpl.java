package com.fsll.app.investor.me.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.lexer.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.market.vo.AppPortfolioArenaMessVo;
import com.fsll.app.investor.me.service.AppMeIndexService;
import com.fsll.app.investor.me.vo.AppInsightItemVo;
import com.fsll.app.investor.me.vo.AppPortfolioInfoVO;
import com.fsll.app.investor.me.vo.AppPortfolioOrderHistoryVO;
import com.fsll.app.investor.me.vo.AppVisitedInfoVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.InsightCount;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaReturn;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebRecommended;

/**
 * 基金信息查询服务接口
 * @author zpzhou
 * @date 2016-7-25
 */
@Service("appMeIndexService")
//@Transactional
public class AppMeIndexServiceImpl extends BaseService implements AppMeIndexService {

	public List<AppPortfolioInfoVO> findVisitedPortfolioList(String memberId,String langCode,String periodCode,int rows) {
		List<AppPortfolioInfoVO> voList = new ArrayList<AppPortfolioInfoVO>();
		StringBuilder hql = new StringBuilder();
		List params = new ArrayList();
		hql.append(" from PortfolioArena s ");
		hql.append(" left join PortfolioArenaReturn r on r.portfolio.id=s.id ");
		hql.append(" left join WebInvestorVisit v on s.id = v.relateId ");
		hql.append(" where v.moduleType='portfolio_arena' and r.periodCode=? and v.member.id=? ");
		hql.append(" order by v.vistiTime desc, r.increase desc ");
		
		params.add(periodCode);
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);
		if( null == list || list.isEmpty()){			
			hql = new StringBuilder();
			params = new ArrayList();
			hql.append(" from PortfolioArena s ");
			hql.append(" left join PortfolioArenaReturn r on r.portfolio.id=s.id ");
			hql.append(" where r.periodCode=? ");
			hql.append(" order by r.increase desc ");

			params.add(periodCode);
			
			list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);
			
		}
		
		if( null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				PortfolioArena info=(PortfolioArena)objs[0];
				AppPortfolioInfoVO vo = new AppPortfolioInfoVO();
				vo.setId(info.getId());
				vo.setPortfolioName(info.getPortfolioName());
				vo.setRiskLevel(String.valueOf(info.getRiskLevel()));
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
				
				if(null!=objs[1]){
					PortfolioArenaReturn arenaReturn=(PortfolioArenaReturn)objs[1];
					vo.setIncrease(getFormatNumByPer(arenaReturn.getIncrease()));
				}else{
					vo.setIncrease(getFormatNumByPer(null));
				}
				MemberBase memberBase=info.getCreator();
				vo.setCreator(memberBase.getNickName());
				vo.setIconUrl(PageHelper.getUserHeadUrlForWS(memberBase.getIconUrl(),""));
				voList.add(vo);
			}				
		}
		
		return voList;
	}
	
	public AppInsightItemVo findVisitedInsightInfo(String memberId){
		AppInsightItemVo vo = new AppInsightItemVo();
		StringBuilder hql = new StringBuilder();
		hql.append(" from InsightInfo i ");
		hql.append(" left join InsightCount c on c.id=i.id ");
		hql.append(" left join WebInvestorVisit v on i.id = v.relateId ");
		hql.append(" where i.status='1' and v.moduleType='insight' and v.member.id=? ");
		hql.append(" order by v.vistiTime desc, i.pubDate desc ");
		List<String> params = new ArrayList<String>();
		params.add(memberId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null == list || list.isEmpty()){
			hql = new StringBuilder();
			hql.append(" from InsightInfo i ");
			hql.append(" left join InsightCount c on c.id=i.id ");
			hql.append(" where i.status='1' ");
			hql.append(" order by i.pubDate desc ");
		}
		
		if(null != list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			InsightInfo info=(InsightInfo)objs[0];
			InsightCount count=(InsightCount)objs[1];
			
			vo.setId(info.getId());
			vo.setRegionType(info.getGeoAllocation());
			vo.setSectionType(info.getSector());
			vo.setTitle(info.getTitle());
			vo.setUpCounter(info.getUpCounter());
			vo.setDownCounter(info.getDownCounter());
			if(null!=info.getCreator())vo.setCreatorName(info.getCreator().getNickName());
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
			if(null!=count){
				vo.setViews(count.getViews());
				vo.setComments(count.getComments());
				vo.setUps(count.getUps());
				vo.setDowns(count.getDowns());
			}	
			MemberBase memberBase=info.getCreator();
			vo.setCreator(memberBase.getNickName());
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(memberBase.getIconUrl(),""));
		}
		return vo;
	}
	
	/**
	 * 得到投资客户的交易记录列表
	 * @author zxtan
	 * @date 2017-01-22
	 * @param memberId
	 * @return
	 */
	public JsonPaging findOrderHistoryList(JsonPaging jsonPaging,String memberId, String toCurrency,String langCode,String orderType,String keyword){
		List<AppPortfolioOrderHistoryVO>  messList = new ArrayList<AppPortfolioOrderHistoryVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from OrderHistory m ");
		hql.append(" left join InvestorAccount a on a.id=m.account.id ");
		hql.append(" left join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" left join "+this.getLangString("FundInfo", langCode));
		hql.append(" s on s.id=f.id ");
		hql.append(" left join PortfolioHold p on p.id=m.portfolioHold.id ");
		hql.append(" where m.member.id=? and m.status in ('2','3') ");

		List params = new ArrayList();
		params.add(memberId);
		
		
		if(null != orderType && !"".equals(orderType)){
			if("other".equalsIgnoreCase(orderType)){
				hql.append(" and FIND_IN_SET(m.orderType,'buy,sell')=0 ");
			}else {
				hql.append(" and m.orderType = ? ");
				params.add(orderType);
			}			
		}
		
		
		if(null != keyword && !"".equals(keyword)){
			hql.append(" and p.portfolioName like ? ");
			params.add("%"+keyword+"%");
		}

		hql.append(" order by m.updateTime desc ");	
		
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql.toString(), params.toArray(), jsonPaging, false);
		
		List list = jsonPaging.getList();// baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				OrderHistory history = (OrderHistory) objs[0];
				InvestorAccount account = (InvestorAccount) objs[1];
				AppPortfolioOrderHistoryVO vo = new AppPortfolioOrderHistoryVO();
				FundInfoSc fundInfo = new FundInfoSc();
				
				if(null != objs[3]){
					BeanUtils.copyProperties(objs[3], fundInfo);
					vo.setFundName(fundInfo.getFundName());
				}
				if(null != objs[4]){
					PortfolioHold portfolioHold = (PortfolioHold) objs[4];
					vo.setPortfolioName(portfolioHold.getPortfolioName());
				}
				
				vo.setId(history.getId());
				//vo.setOrderNo(history.getOrderNo());
				vo.setAccountNo(account.getAccountNo());
				String currencyCode = history.getCurrencyCode();
				
				if(null != history.getCommissionUnit()){
					vo.setCommissionUnit(history.getCommissionUnit().toString());
				}
				
				//汇率转换
				if(null != toCurrency && !"".equals(toCurrency)){
					vo.setCurrencyCode(toCurrencyName);
					if(null != history.getCommissionPrice()){
						vo.setCommissionPrice(getFormatNumByCurrency(history.getCommissionPrice(), currencyCode, toCurrency));
					}
					if(null != history.getCommissionAmount()){
						vo.setCommissionAmount(getFormatNumByCurrency(history.getCommissionAmount(),currencyCode,toCurrency));
					}		
					if(null != history.getTransactionAmount()){
						vo.setTransactionAmount(getFormatNumByCurrency(history.getTransactionAmount(),currencyCode,toCurrency));
					}
					if(null != history.getFee()){
						vo.setFee(getFormatNumByCurrency(history.getFee(),currencyCode,toCurrency));
					}
				}else {//原始数据
					vo.setCurrencyCode(getParamConfigName(langCode, currencyCode, "currency_type"));
					if(null != history.getCommissionPrice()){
						vo.setCommissionPrice(getFormatNum(history.getCommissionPrice(),currencyCode));
					}
					if(null != history.getCommissionAmount()){
						vo.setCommissionAmount(getFormatNum(history.getCommissionAmount(),currencyCode));
					}		
					if(null != history.getTransactionAmount()){
						vo.setTransactionAmount(getFormatNum(history.getTransactionAmount(),currencyCode));
					}
					if(null != history.getFee()){
						vo.setFee(getFormatNum(history.getFee(),currencyCode));
					}
				}
				
				
				if(null != history.getTransactionUnit()){
					vo.setTransactionUnit(history.getTransactionUnit().toString());
				}	
				
				vo.setOrderType(history.getOrderType());
				vo.setOrderDate(DateUtil.dateToDateString(history.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
				vo.setCloseTime(DateUtil.dateToDateString(history.getCloseTime(), "yyyy-MM-dd HH:mm:ss"));
				vo.setUpdateTime(DateUtil.dateToDateString(history.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				vo.setStatus(history.getStatus());
				vo.setIfAip(history.getIfAip());
				vo.setSwitchFlag(history.getSwitchFlag());
				
				messList.add(vo);
			}
		}
		jsonPaging.setList(messList);
		return jsonPaging;		
//		return messList;
	}


}
