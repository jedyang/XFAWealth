package com.fsll.app.ifa.market.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.app.ifa.market.service.AppPortfolioArenaService;
import com.fsll.app.ifa.market.vo.AppArenaAllocationDetailVO;
import com.fsll.app.ifa.market.vo.AppArenaCumperfVO;
import com.fsll.app.ifa.market.vo.AppArenaItemVO;
import com.fsll.app.ifa.market.vo.AppArenaProductVO;
import com.fsll.app.ifa.market.vo.AppArenaReturnVO;
import com.fsll.app.ifa.market.vo.AppArenaVO;
import com.fsll.app.watch.service.AppWatchMessService;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundReturnSc;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaCumperf;
import com.fsll.wmes.entity.PortfolioArenaProduct;
import com.fsll.wmes.entity.PortfolioArenaReturn;

@Service("appIfaMarketPortfolioArenaService")
public class AppPortfolioArenaServiceImpl extends BaseService implements
		AppPortfolioArenaService {
	@Autowired
	private AppWatchMessService watchMessService;

	/**
	 * 获取我的组合库
	 * @author zxtan
	 * @date 2017-04-15
	 */
	public JsonPaging findMyPortfolioList(JsonPaging jsonPaging,
			String memberId, String keyword, String status, String isValid,String periodCode,
			String langCode) {
		// TODO Auto-generated method stub
		StringBuilder hql = new StringBuilder();
		hql.append("from PortfolioArena m ");
		hql.append(" left join PortfolioArenaReturn r on r.portfolio.id=m.id and r.periodCode=? ");
		hql.append(" left join "+getLangString("FundReturn", langCode)+" rl on rl.periodCode=r.periodCode ");
		hql.append(" where m.creator.id=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(periodCode);
		params.add(memberId);
		
		if(!"".equals(keyword)){
			hql.append(" and m.portfolioName like ? ");
			params.add("%"+keyword +"%");
		}
		if(!"".equals(status)){
			hql.append(" and m.status = ? ");
			params.add(status);
		}
		if(!"".equals(isValid)){
			hql.append(" and m.isValid = ? ");
			params.add(isValid);
		}
		
		hql.append(" order by m.lastUpdate desc ");
		
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		List<AppArenaItemVO> voList = new ArrayList<AppArenaItemVO>();
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppArenaItemVO vo = new AppArenaItemVO();
				Object[] objs = (Object[]) list.get(i);
				PortfolioArena info = (PortfolioArena) objs[0];
				
				vo.setArenaId(info.getId());
				vo.setPortfolioName(info.getPortfolioName());
				vo.setRiskLevel(String.valueOf(info.getRiskLevel()));
				int investDays = 0;
				try {
					investDays = daysBetween(info.getCreateTime(), new Date());
				} catch (Exception e) {
					// TODO: handle exception
				}
				vo.setInvestDays(String.valueOf(investDays));
				vo.setStatus(info.getStatus());
				vo.setIsValid(info.getIsValid());
				
				if(null != objs[1]){
					PortfolioArenaReturn arenaReturn = (PortfolioArenaReturn) objs[1];
					vo.setIncrease(getFormatNumByPer(arenaReturn.getIncrease()));
				}
				if(null != objs[2]){
					FundReturnSc fundReturnSc = new FundReturnSc();
					BeanUtils.copyProperties(objs[2], fundReturnSc);
					vo.setPeriodName(fundReturnSc.getPeriodName());					
				}				
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		
		return jsonPaging;
	}
	
	/**
	 * 获取我有权限查看的组合列表
	 * @author zxtan
	 * @date 2017-04-25
	 */
	public JsonPaging findAllPortfolioList(JsonPaging jsonPaging,JSONObject jsonObject) {
		// TODO Auto-generated method stub
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String keyword = jsonObject.optString("keyword", "");// 搜索关键字
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);//回报周期
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前语言
		String sort = jsonObject.optString("sort","0");//排序字段,0创建时间 1回报 2风险		
		String order = jsonObject.optString("order","DESC");//方向	
		
		StringBuilder hql = new StringBuilder();
		hql.append("from PortfolioArena m ");
		hql.append(" left join PortfolioArenaReturn r on r.portfolio.id=m.id and r.periodCode=? ");
		hql.append(" left join "+getLangString("FundReturn", langCode)+" rl on rl.periodCode=r.periodCode ");
		hql.append(" where m.status='1' and m.isValid='1' ");
		hql.append(" and ( m.creator.id=? or m.id in (select v.relateId from WebView v where  v.moduleType=? ");
		hql.append(" 	and (v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?)))) ) ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(periodCode);
		params.add(memberId);
		params.add("portfolio_arena");
		params.add(memberId);
		
		if(!"".equals(keyword)){
			hql.append(" and m.portfolioName like ? ");
			params.add("%"+keyword +"%");
		}
		
		if(",0,1,2,".indexOf(sort)>-1){
			if("0".equals(sort)){
				sort = " m.createTime ";//创建时间
			}else if("1".equals(sort)){
				sort = " r.increase ";//回报率
			}else if("2".equals(sort)){
				sort = " m.riskLevel ";//风险
			}
			jsonPaging.setOrder(order);
			hql.append(" order by "+ sort+" " + order);
		}
//		hql.append(" order by m.overhead desc ,m.lastUpdate desc ");
		
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		List<AppArenaItemVO> voList = new ArrayList<AppArenaItemVO>();
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppArenaItemVO vo = new AppArenaItemVO();
				Object[] objs = (Object[]) list.get(i);
				PortfolioArena info = (PortfolioArena) objs[0];
				
				vo.setArenaId(info.getId());
				vo.setPortfolioName(info.getPortfolioName());
				vo.setRiskLevel(String.valueOf(info.getRiskLevel()));
				vo.setInvestmentGoal(info.getInvestmentGoal());
				vo.setSuitability(info.getSuitability());
				vo.setReason(info.getReason());
				int investDays = 0;
				try {
					investDays = daysBetween(info.getCreateTime(), new Date());
				} catch (Exception e) {
					// TODO: handle exception
				}
				vo.setInvestDays(String.valueOf(investDays));
				vo.setStatus(info.getStatus());
				vo.setIsValid(info.getIsValid());
				
				if(null != objs[1]){
					PortfolioArenaReturn arenaReturn = (PortfolioArenaReturn) objs[1];
					vo.setIncrease(getFormatNumByPer(arenaReturn.getIncrease()));
				}
				if(null != objs[2]){
					FundReturnSc fundReturnSc = new FundReturnSc();
					BeanUtils.copyProperties(objs[2], fundReturnSc);
					vo.setPeriodName(fundReturnSc.getPeriodName());					
				}				
				vo.setCreatorId(info.getCreator().getId());
				vo.setCreatorIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(), null));
				vo.setCreatorName(getCommonMemberName(info.getCreator().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME));
				String ifFollow = watchMessService.findWebFollowCount(info.getId(), memberId, "portfolio_arena");
				vo.setIfFollow(ifFollow);
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		
		return jsonPaging;
	}
	
	
	/**
	 * 得到投资组合基本信息
	 * @author zxtan
	 * @date 2016-11-16
	 * @param memberId member Id
	 * @param portfolioId 组合Id
	 * @return
	 */
	public AppArenaVO findPortfolioInfoMess(String memberId,String portfolioId,String langCode){
		AppArenaVO vo =new AppArenaVO();
		String hql = " from PortfolioArena p where p.id=? ";
		List params = new ArrayList();
		params.add(portfolioId);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			PortfolioArena info=(PortfolioArena)list.get(0);
			vo.setId(info.getId());
			vo.setPortfolioName(info.getPortfolioName());
			vo.setInvestmentGoal(info.getInvestmentGoal());	
			vo.setReason(info.getReason());
			vo.setSuitability(info.getSuitability());
			vo.setDescription(info.getDescription());
			//数据格式处理
			if(null==info.getTotalReturn() || "".equals(info.getTotalReturn().toString())){
				vo.setTotalReturn(getFormatNum(null));
			}else{
				vo.setTotalReturn(getFormatNum(info.getTotalReturn()));
			}						
			vo.setRiskLevel(String.valueOf(info.getRiskLevel()));			
			vo.setCreator(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(), null));
			String ifFollow = watchMessService.findWebFollowCount(info.getId(), memberId, "portfolio_arena");
			vo.setIfFollow(ifFollow);
		}
		return vo;
	}
	
	/**
	 * 得到投资组合回报
	 * @author zxtan
	 * @date 2016-11-18
	 * @param portfolioId 组合ID
	 * @param periodCode 回报周期
	 * @return
	 */
	public AppArenaReturnVO findPortfolioReturn(String portfolioId,String periodCode){
		AppArenaReturnVO vo =new AppArenaReturnVO();
		String hql = " from PortfolioArenaReturn p where p.portfolio.id=? and p.periodCode=? ";
		List params = new ArrayList();
		params.add(portfolioId);
		params.add(periodCode);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			PortfolioArenaReturn info=(PortfolioArenaReturn)list.get(0);
			vo.setPeriodCode(info.getPeriodCode());
			//数据格式处理
			if(null==info.getIncrease() || "".equals(info.getIncrease().toString())){
				vo.setIncrease(getFormatNum(null));
			}else{
				vo.setIncrease(getFormatNumByPer(info.getIncrease()));
			}	
		}		
		
		return vo;
	}
	
	
	/**
	 * 得到某个投资组合行情数据
	 * @author zxtan
	 * @date 2016-11-16
	 * @param portfolioId 组合ID
	 * @param startDate 起始时间
	 * @return
	 */
	public List<AppArenaCumperfVO> findPortfolioCumperfList(String portfolioId,String startDate){
		List<AppArenaCumperfVO> cumperfList = new ArrayList<AppArenaCumperfVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioArenaCumperf t where 1=1 ");
		List params = new ArrayList();
		if(null!=portfolioId && !"".equals(portfolioId)){
			hql.append(" and t.portfolio.id=? ");
			params.add(portfolioId);
		}
		if(null!=startDate && !"".equals(startDate)){
			hql.append( "and t.valuationDate>=? ");
			params.add(DateUtil.getDate(startDate));
		}
		
		hql.append(" order by t.valuationDate ");

		
		List<PortfolioArenaCumperf> list = baseDao.find(hql.toString(), params.toArray(), false);
		for(int i=0;i<list.size();i++){
			PortfolioArenaCumperf cumperf = list.get(i);
			AppArenaCumperfVO vo = new AppArenaCumperfVO();
			
			vo.setId(cumperf.getId());
			vo.setPortfolioId(cumperf.getPortfolio().getId());
			vo.setValuationDate(DateUtil.dateToDateString(cumperf.getValuationDate(),"yyyy-MM-dd"));
			if(null==cumperf.getCumprefRate() || "".equals(cumperf.getCumprefRate().toString())){
				vo.setCumprefRate(getFormatNum(null));
			}else{
				vo.setCumprefRate(getFormatNum(cumperf.getCumprefRate()));
			}
			if(null==cumperf.getTotalPl() || "".equals(cumperf.getTotalPl().toString())){
				vo.setTotalPl(getFormatNum(null));
			}else{
				vo.setTotalPl(getFormatNum(cumperf.getTotalPl()));
			}
			if(null==cumperf.getDayPl() || "".equals(cumperf.getDayPl().toString())){
				vo.setDayPl(getFormatNum(null));
			}else{
				vo.setDayPl(getFormatNum(cumperf.getDayPl()));
			}
			vo.setCreateTime(DateUtil.dateToDateString(cumperf.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(cumperf.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			cumperfList.add(vo);
		}
		return cumperfList;
	}
	
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-11-16
	 * @param portfolioId 组合ID
	 * @param langCode 语言
	 * @return
	 */
	public List<AppArenaProductVO> findPortfolioProductList(String portfolioId,String langCode){
		List<AppArenaProductVO>  productList = new ArrayList<AppArenaProductVO>();
		StringBuilder hql = new StringBuilder();
//		hql.append("SELECT p.id,p.portfolio.id,p.product.id,p.allocationRate,f.id as fundId,l.fundName,l.fundType ,f.riskLevel");
		hql.append(" FROM PortfolioArenaProduct p");
		hql.append(" inner JOIN FundInfo f ON p.product.id = f.product.id ");
		hql.append(" inner JOIN "+this.getLangString("FundInfo", langCode)+" l ON f.id = l.id ");
		hql.append(" where p.portfolio.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioArenaProduct product = (PortfolioArenaProduct) objs[0];
			FundInfo fundInfo = (FundInfo) objs[1];
			FundInfoSc fundInfoSc = new FundInfoSc();
			BeanUtils.copyProperties(objs[2], fundInfoSc);
			AppArenaProductVO  vo = new AppArenaProductVO();
			
			vo.setId(product.getId());
			vo.setPortfolioId(product.getPortfolio().getId());
			vo.setProductId(product.getProduct().getId());
			vo.setAllocationRate(String.valueOf(product.getAllocationRate()));
			vo.setFundId(fundInfo.getId());
			vo.setFundName(fundInfoSc.getFundName());
			vo.setFundType(fundInfoSc.getFundType());
			vo.setRiskLevel(String.valueOf(fundInfo.getRiskLevel()));
			vo.setFundCurrency(fundInfoSc.getFundCurrency());
			vo.setFundCurrencyCode(fundInfoSc.getFundCurrencyCode());
			vo.setIssueCurrency(fundInfoSc.getIssueCurrency());
			vo.setIssueCurrencyCode(fundInfoSc.getIssueCurrencyCode());
			
			productList.add(vo);
		}
		return productList;
	}
	
	
	/**
	 * 得到投资组合的产品分类配置情况
	 * @author zxtan
	 * @date 2016-11-16
	 * @param portfolioId 组合ID
	 * @param langCode 语言
	 * @param groupType 统计分类
	 * @return
	 */
	public List<AppArenaAllocationDetailVO> findPortfolioAllocationList(String portfolioId,String langCode,String groupType){
		List<AppArenaAllocationDetailVO> allocationList = new ArrayList<AppArenaAllocationDetailVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT l."+groupType+",sum(p.allocationRate)");
		hql.append(" FROM PortfolioArenaProduct p");
		hql.append(" LEFT JOIN FundInfo f ON p.product.id = f.product.id ");
		hql.append(" LEFT JOIN "+this.getLangString("FundInfo", langCode)+" l ON f.id = l.id ");
		hql.append(" where p.portfolio.id=? ");
		hql.append(" group by l."+groupType );
		
		List params = new ArrayList();
		params.add(portfolioId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppArenaAllocationDetailVO  vo = new AppArenaAllocationDetailVO();
			
			vo.setItemName(objs[0]==null?"":objs[0].toString());
			vo.setItemRate(objs[1]==null?"":objs[1].toString());
			if(null==vo.getItemRate() || "".equals(vo.getItemRate())){
				vo.setItemRate(getFormatNum(null));
			}else{
				vo.setItemRate(getFormatNum(Double.parseDouble(vo.getItemRate())));
			}
			allocationList.add(vo);
		}
		return allocationList;
	}


	public boolean updatePortfolioArena(String updateType, String id,	String isValid) {
		// TODO Auto-generated method stub
		PortfolioArena info = (PortfolioArena) baseDao.get(PortfolioArena.class, id);
		if("delete".equalsIgnoreCase(updateType)){
			baseDao.delete(info);
			return true;
		}else if("update".equalsIgnoreCase(updateType)) {
			info.setIsValid(isValid);
			baseDao.update(info);
			return true;
		}
		return false;
	}

}
