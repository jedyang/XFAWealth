/**
 * 
 */
package com.fsll.app.investor.market.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.market.service.AppIndexNoMissService;
import com.fsll.app.investor.market.vo.AppIndexNoMissVo;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.StrategyInfo;

/**
 * 首页不容错过接口现实类
 * @author zpzhou
 * @date 2016-9-26
 */
@Service("appIndexNoMissService")
//@Transactional
public class AppIndexNoMissServiceImpl extends BaseService implements
		AppIndexNoMissService {

	/**
	 * 得到首页不容错过信息
	 * @param langCode 语言
	 * @return
	 */
	public List<AppIndexNoMissVo> findIndexNoMiss(String memberId,String langCode,String period) {
		List<AppIndexNoMissVo> topList = new ArrayList<AppIndexNoMissVo>();
		StringBuilder hqlStrategy = new StringBuilder();
		hqlStrategy.append(" from StrategyInfo s where s.overhead='1' and s.isValid='1' ");
		hqlStrategy.append(" and s.id in (select v.relateId from WebView v where  v.moduleType=? ");
		hqlStrategy.append(" and (v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?)))) ");
		hqlStrategy.append(" order by s.overheadTime desc");
		List paramsStrategy = new ArrayList();
		paramsStrategy.add("strategy");
		paramsStrategy.add(memberId);
		List listStrategy = baseDao.find(hqlStrategy.toString(), paramsStrategy.toArray(), false);
		if(null!=listStrategy && !listStrategy.isEmpty()){
			StrategyInfo info=(StrategyInfo)listStrategy.get(0);
			AppIndexNoMissVo vo=new AppIndexNoMissVo();
			vo.setId(info.getId());
			vo.setName(info.getStrategyName());
			vo.setReason(info.getReason());
			vo.setRiskLevel(info.getRiskLevel());
			vo.setIfaName(info.getCreator().getNickName());
			vo.setType("1");
			topList.add(vo);
		}
		String hqlPortfolio = " from PortfolioArena s where s.overhead=? and s.is_valid=? order by s.overheadTime desc";
		List paramsPortfolio = new ArrayList();
		paramsPortfolio.add("1");
		paramsPortfolio.add("1");
		List listPortfolio = baseDao.find(hqlPortfolio, paramsPortfolio.toArray(), false);
		if(null!=listPortfolio && !listPortfolio.isEmpty()){
			PortfolioArena info=(PortfolioArena)listPortfolio.get(0);
			AppIndexNoMissVo vo=new AppIndexNoMissVo();
			vo.setId(info.getId());
			vo.setName(info.getPortfolioName());
			vo.setReason(info.getReason());
			vo.setRiskLevel(info.getRiskLevel().toString());
			vo.setIfaName(info.getCreator().getNickName());
			vo.setType("2");
			//vo.setReturnRate(String.valueOf(info.getTotalReturn()));
			topList.add(vo);
		}
		String hqlFund = "select s.id,s.fundName,f.riskLevel,r.increase from FundInfo s " +
				" left join " +this.getLangString("FundInfo", langCode)+" a on s.id=a.id "+
				" left join FundReturn r on r.fund.id=s.id and r.periodCode=? "+
				"where s.overhead=? and s.is_valid=? order by s.overheadTime desc";
		List paramsFund = new ArrayList();
		paramsFund.add(period);
		paramsFund.add("1");
		paramsFund.add("1");
		List listFund = baseDao.find(hqlFund, paramsFund.toArray(), false);
		if(null!=listFund && !listFund.isEmpty()){
			Object[] objs = (Object[])listFund.get(0);
			AppIndexNoMissVo vo=new AppIndexNoMissVo();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setName(objs[1]==null?"":objs[1].toString());
			vo.setReason("");
			vo.setRiskLevel(objs[2]==null?"":objs[2].toString());
			vo.setIfaName("");
			vo.setType("3");
			vo.setReturnRate(objs[3]==null?"":objs[3].toString());
			topList.add(vo);
		}
		return topList;
	}
	/**
	 * 得到首页-不容错过-投资策略列表信息
	 * @return
	 */
	public JsonPaging findNoMissStrategyMore(JsonPaging jsonPaging){
		List<AppIndexNoMissVo> list = new ArrayList<AppIndexNoMissVo>();
		String hqlStrategy = " from StrategyInfo s where s.overhead=? and s.is_valid=? order by s.overheadTime desc";
		List paramsStrategy = new ArrayList();
		paramsStrategy.add("1");
		paramsStrategy.add("1");
		this.baseDao.selectJsonPaging(hqlStrategy, paramsStrategy.toArray(), jsonPaging , false);
		List listStrategy = jsonPaging.getList();
		if(null!=listStrategy && !listStrategy.isEmpty()){
			for(int i=0;i<listStrategy.size();i++){
				StrategyInfo info=(StrategyInfo)listStrategy.get(0);
				AppIndexNoMissVo vo=new AppIndexNoMissVo();
				vo.setId(info.getId());
				vo.setName(info.getStrategyName());
				vo.setReason(info.getReason());
				vo.setRiskLevel(info.getRiskLevel());
				vo.setIfaName(info.getCreator().getNickName());
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	/**
	 * 得到首页-不容错过-投资组合列表信息
	 * @return
	 */
	public JsonPaging findNoMissPortfolioMore(JsonPaging jsonPaging){
		List<AppIndexNoMissVo> list = new ArrayList<AppIndexNoMissVo>();
		String hqlPortfolio = " from PortfolioArena s where s.overhead=? and s.is_valid=? order by s.overheadTime desc";
		List paramsPortfolio = new ArrayList();
		paramsPortfolio.add("1");
		paramsPortfolio.add("1");
		this.baseDao.selectJsonPaging(hqlPortfolio, paramsPortfolio.toArray(), jsonPaging , false);
		List listPortfolio = jsonPaging.getList();
		if(null!=listPortfolio && !listPortfolio.isEmpty()){
			for(int i=0;i<listPortfolio.size();i++){
				PortfolioArena info=(PortfolioArena)listPortfolio.get(0);
				AppIndexNoMissVo vo=new AppIndexNoMissVo();
				vo.setId(info.getId());
				vo.setName(info.getPortfolioName());
				vo.setReason(info.getReason());
				vo.setRiskLevel(info.getRiskLevel().toString());
				vo.setIfaName(info.getCreator().getNickName());
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	/**
	 * 得到首页-不容错过-更多基金列表信息
	 * @return
	 */
	public JsonPaging findNoMissFundMore(JsonPaging jsonPaging,String langCode,String period){
		List<AppIndexNoMissVo> list = new ArrayList<AppIndexNoMissVo>();
		String hqlFund = "select s.id,s.fundName,f.riskLevel,r.increase from FundInfo s " +
					" left join " +this.getLangString("FundInfo", langCode)+" a on s.id=a.id "+
					" left join FundReturn r on r.fund.id=s.id and r.periodCode=? "+
					"where s.overhead=? and s.is_valid=? order by s.overheadTime desc";
		List paramsFund = new ArrayList();
		paramsFund.add(period);
		paramsFund.add("1");
		paramsFund.add("1");
		this.baseDao.selectJsonPaging(hqlFund, paramsFund.toArray(), jsonPaging , false);
		List listFund = jsonPaging.getList();
		if(null!=listFund && !listFund.isEmpty()){
			for(int i=0;i<listFund.size();i++){
				Object[] objs = (Object[])listFund.get(0);
				AppIndexNoMissVo vo=new AppIndexNoMissVo();
				vo.setId(objs[0]==null?"":objs[0].toString());
				vo.setName(objs[1]==null?"":objs[1].toString());
				vo.setReason("");
				vo.setRiskLevel(objs[2]==null?"":objs[2].toString());
				vo.setIfaName("");
				vo.setReturnRate(objs[3]==null?"":objs[3].toString());
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
}
