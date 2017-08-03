/**
 * 
 */
package com.fsll.app.investor.market.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.market.service.AppStrategyMessService;
import com.fsll.app.investor.market.vo.AppStrategyAllocationVO;
import com.fsll.app.investor.market.vo.AppStrategyInfoVO;
import com.fsll.app.investor.market.vo.AppStrategyMessVo;
import com.fsll.app.investor.market.vo.AppStrategyProductVo;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.StrategyInfo;

/**
 * 投资策略接口服务接口类现实
 * @author zpzhou
 * @date 2016-9-18
 */
@Service("appStrategyMessService")
//@Transactional
public class AppStrategyMessServiceImpl extends BaseService implements AppStrategyMessService {

	/**
	 * 得到我有权限查看的投资策略列表
	 * @param memberId 用户ID
	 * @param keyWord 搜索关键词
	 * @return JsonPaging 分页数据
	 */
	public JsonPaging findStrategyList(JsonPaging jsonPaging,String memberId,String keyWord,String langCode){
		List<AppStrategyMessVo>  messList = new ArrayList<AppStrategyMessVo>();
		String hql = "select i.id,fn_getconfigname(i.geoAllocation,'"+langCode+"') as geoAllocation,"+
					 "fn_getconfigname(i.sector,'"+langCode+"') as sector,i.strategyName,i.riskLevel,"+
					 "i.investmentGoal,i.reason,i.creator.id,i.createTime,i.lastUpdate,i.creator.iconUrl "+
		             ",(select count(0) from WebFollow f where f.relateId=i.id and f.member.id=?)"+
					 " from StrategyInfo i " +
                     " where i.status='1' and i.isValid=? and i.id in (select v.relateId from WebView v where  v.moduleType=? " +
                     " and (v.scopeFlag=? or (v.scopeFlag=? and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?))))  ";
		List params = new ArrayList();
		params.add(memberId);
		params.add("1");
		params.add("strategy");
		params.add("2");
		params.add("3");
		params.add(memberId);
		if(null!=keyWord && !"".equals(keyWord)){
			hql += " and i.strategyName like ? ";
			params.add("%"+keyWord+"%");
		}
		hql += " order by i.lastUpdate desc ";
		
		jsonPaging= baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		if(null!=list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] obj = (Object[]) list.get(i);
				AppStrategyMessVo vo =new AppStrategyMessVo();
				vo.setId(obj[0]==null?"":obj[0].toString());
				vo.setGeoAllocation(obj[1]==null?"":obj[1].toString());
				vo.setSector(obj[2]==null?"":obj[2].toString());
				vo.setStrategyName(obj[3]==null?"":obj[3].toString());
				vo.setRiskLevel(obj[4]==null?"":obj[4].toString());
				vo.setInvestmentGoal(obj[5]==null?"":obj[5].toString());
				vo.setReason(obj[6]==null?"":obj[6].toString());
				
				String creatorId = obj[7]==null?"":obj[7].toString();
				String iconUrl = obj[10]==null?"":obj[10].toString();
				vo.setCreator(getCommonMemberName(creatorId, langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				vo.setIconUrl(PageHelper.getUserHeadUrlForWS(iconUrl,null));
				
				vo.setCreateTime(DateUtil.dateToDateString( DateUtil.getDate(obj[8]==null?"":obj[8].toString(),"yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss"));
				vo.setLastUpdate(DateUtil.dateToDateString( DateUtil.getDate(obj[9]==null?"":obj[9].toString(),"yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss"));
										
				vo.setIfFollow(obj[11]==null?"":obj[11].toString());
				messList.add(vo);
			}
		}
		jsonPaging.setList(messList);
		return jsonPaging;
	}
	/**
	 * 得到投资策略基本信息
	 * @param strategyId 策略ID
	 * @return
	 */
	public AppStrategyMessVo findStrategyInfoMess(String strategyId){
		AppStrategyMessVo vo =new AppStrategyMessVo();
		String hql = " from StrategyInfo p  where p.id=? ";
		List params = new ArrayList();
		params.add(strategyId);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			StrategyInfo info=(StrategyInfo)list.get(0);
			vo.setId(info.getId());
			vo.setDescription(info.getDescription());
			vo.setInvestmentGoal(info.getInvestmentGoal());
			vo.setOverhead(info.getOverhead());
			vo.setReason(info.getReason());
			vo.setRiskLevel(info.getRiskLevel());
			vo.setStrategyName(info.getStrategyName());
			vo.setSuitability(info.getSuitability());
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
		}
		return vo;
	}
	/**
	 * 得到投资策略的产品信息列表
	 * @param strategyId 策略ID
	 * @param langCode 语言
	 * @param periodCode 回报时间类型
	 * @return
	 */
	public List<AppStrategyProductVo> findStrategyProductList(String strategyId,String langCode,String periodCode,String productType,String toCurrency){
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		List<AppStrategyProductVo>  messList = new ArrayList<AppStrategyProductVo>();
		String hql = "select f.id,s.fundName,s.fundCurrency,s.fundType,f.riskLevel," +
				" get_exchange_rate(s.fundCurrencyCode,'"+toCurrency+"',f.lastNav) as lastNav,r.increase,s.fundCurrencyCode,s.issueCurrency,s.issueCurrencyCode ";
		hql += " from StrategyProduct m ";
		hql += " left join m.product p";
		hql += " left join FundInfo f on f.product.id=p.id ";
		hql += " left join "+this.getLangString("FundInfo", langCode);
		hql += " s on s.id=f.id ";
		hql += " left join FundReturn r on r.fund.id=f.id and r.periodCode=? ";
		hql += " where m.strategy.id=? and m.product.type=? ";
		
		List params = new ArrayList();
		params.add(periodCode);
		params.add(strategyId);
		params.add(productType);
		
		List list = baseDao.find(hql, params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppStrategyProductVo  vo = new AppStrategyProductVo();
			vo.setFundId(objs[0]==null?"":objs[0].toString());
			vo.setFundName(objs[1]==null?"":objs[1].toString());
			
			vo.setFundType(objs[3]==null?"":objs[3].toString());
			vo.setRiskLevel(objs[4]==null?"":objs[4].toString());
			vo.setLastNav(objs[5]==null?"":objs[5].toString());
			vo.setIncrease(objs[6]==null?"":objs[6].toString());
			
			if(StringUtils.isBlank(toCurrency)){
				vo.setFundCurrency(objs[2]==null?"":objs[2].toString());
				vo.setFundCurrencyCode(objs[7]==null?"":objs[7].toString());
			}else {
				vo.setFundCurrency(toCurrencyName);
				vo.setFundCurrencyCode(toCurrency);
			}
			vo.setIssueCurrency(objs[8]==null?"":objs[8].toString());
			vo.setIssueCurrencyCode(objs[9]==null?"":objs[9].toString());
			
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
			messList.add(vo);
		}
		return messList;
	}
	
	/**
	 * 得到投资策略的产品信息列表
	 * @param strategyId 策略ID
	 * @param langCode 语言
	 * @param periodCode 回报时间类型
	 * @return
	 */
	public List<AppStrategyProductVo> findStrategyProductList(String strategyId,String langCode,String periodCode,String productType,int rows,String toCurrency){
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		List<AppStrategyProductVo>  messList = new ArrayList<AppStrategyProductVo>();
		String hql = "select f.id,s.fundName,s.fundCurrency,s.fundType,f.riskLevel," +
				" get_exchange_rate(s.fundCurrencyCode,'"+toCurrency+"',f.lastNav) as lastNav,r.increase ";
		hql +=",s.fundCurrencyCode,s.issueCurrency,s.issueCurrencyCode";
		hql += " from StrategyProduct m ";
		hql += " left join m.product p";
		hql += " left join FundInfo f on f.product.id=p.id ";
		hql += " left join "+this.getLangString("FundInfo", langCode);
		hql += " s on s.id=f.id ";
		hql += " left join FundReturn r on r.fund.id=f.id and r.periodCode=? ";
		hql += " where m.strategy.id=? and m.product.type=? ";
		
		List params = new ArrayList();
		params.add(periodCode);
		params.add(strategyId);
		params.add(productType);
		
		List list = baseDao.find(hql, params.toArray(),0,rows, false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppStrategyProductVo  vo = new AppStrategyProductVo();
			vo.setFundId(objs[0]==null?"":objs[0].toString());
			vo.setFundName(objs[1]==null?"":objs[1].toString());
			vo.setFundType(objs[3]==null?"":objs[3].toString());
			vo.setRiskLevel(objs[4]==null?"":objs[4].toString());
			vo.setLastNav(objs[5]==null?"":objs[5].toString());
			vo.setIncrease(objs[6]==null?"":objs[6].toString());
			if(StringUtils.isBlank(toCurrency)){
				vo.setFundCurrency(objs[2]==null?"":objs[2].toString());
				vo.setFundCurrencyCode(objs[7]==null?"":objs[7].toString());
			}else {
				vo.setFundCurrency(toCurrencyName);
				vo.setFundCurrencyCode(toCurrency);
			}
			
			vo.setIssueCurrency(objs[8]==null?"":objs[8].toString());
			vo.setIssueCurrencyCode(objs[9]==null?"":objs[9].toString());
			
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
			messList.add(vo);
		}
		return messList;
	}
	
	/**
	 * 按分类得到某个策略配置情况
	 * @author zxtan
	 * @date 2016-11-14
	 * @param strategyId
	 * @param type
	 * @param langCode
	 * @return
	 */
	public List<AppStrategyAllocationVO> findStrategyAllocationList(String strategyId,String type,String langCode){
		List<AppStrategyAllocationVO> voList = new ArrayList<AppStrategyAllocationVO>();
		List params = new ArrayList();
		
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT a.id,a.strategy.id,a.type,a.methodType,a.itemCode,a.itemWeight,a.layerLevel,"+this.getLangString("c.name", langCode));
		hql.append(" FROM StrategyAllocation a ");
		hql.append(" LEFT JOIN SysParamConfig c ON a.itemCode=c.configCode ");
		hql.append(" WHERE a.strategy.id = ? ");
		params.add(strategyId);
		
		if(null != type && !"".equals(type)){
			hql.append(" and a.layerLevel > 1 and a.type = ? ");		
			params.add(type);
		}else {
			hql.append(" and a.layerLevel = 1 ");	
		}
		hql.append(" ORDER BY a.layerLevel,a.methodType");		
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		Map<Integer, AppStrategyAllocationVO> map = new TreeMap<Integer, AppStrategyAllocationVO>();
		
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				AppStrategyAllocationVO vo = new AppStrategyAllocationVO();
				vo.setId(obj[0]==null?"":obj[0].toString());
				vo.setStrategyId(obj[1]==null?"":obj[1].toString());
				vo.setType(obj[2]==null?"":obj[2].toString());
				vo.setMethodType(obj[3]==null?"":obj[3].toString());
				vo.setItemCode(obj[4]==null?"":obj[4].toString());
				vo.setItemWeight(obj[5]==null?"":obj[5].toString());
				vo.setLayerLevel(obj[6]==null?"":obj[6].toString());
				vo.setItemName(obj[7]==null?"":obj[7].toString());
				
//				if(null==vo.getItemWeight() || "".equals(vo.getItemWeight())){
//					vo.setItemWeight(getFormatNum(null));
//				}else{
//					vo.setItemWeight(getFormatNum(Double.parseDouble(vo.getItemWeight())));
//				}
				
				//排序
				if(null == type || "".equals(type)){
					if("fund".equalsIgnoreCase(vo.getType())){
						map.put(1, vo);
					}else if("stock".equalsIgnoreCase(vo.getType())){
						map.put(2, vo);
					}else if("bond".equalsIgnoreCase(vo.getType())){
						map.put(3, vo);
					}else{
						map.put(4, vo);
					}
				}else {
					voList.add(vo);
				}				
			}			
			//排序
			if(null == type || "".equals(type)){
				//遍历集合
				for (Iterator<Integer> it = map.keySet().iterator(); it.hasNext();) {
					AppStrategyAllocationVO vo = map.get(it.next());
					voList.add(vo);
				}
			}
		}
				
		return voList;
	}
	
	/**
	 * 得到投资策略基本信息
	 * @author zxtan
	 * @date 2016-11-15
	 * @param strategyId 策略ID
	 * @param langCode 语言Code
	 * @return
	 */
	public AppStrategyInfoVO findStrategyInfo(String memberId,String strategyId,String langCode){
		AppStrategyInfoVO vo =new AppStrategyInfoVO();
		//fn_getconfigname(i.geoAllocation,'"+langCode+"') as geoAllocation,fn_getconfigname(i.sector,'"+langCode+"') as sector
		StringBuilder hql = new StringBuilder();
		hql.append("select i.id,fn_getconfigname(i.geoAllocation,'"+langCode+"') as geoAllocation,");
		hql.append("fn_getconfigname(i.sector,'"+langCode+"') as sector,i.strategyName,i.riskLevel,");
		hql.append("i.investmentGoal,i.reason,i.creator.id,i.createTime,i.lastUpdate,i.creator.iconUrl,i.suitability,");
		hql.append("(select count(0) from WebFollow f where f.relateId=i.id and f.member.id=?)");
		hql.append(" from StrategyInfo i  where i.id=? ");
		List params = new ArrayList();
		params.add(memberId);
		params.add(strategyId);
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object[] obj = (Object[]) list.get(0);
			
			vo.setId(obj[0]==null?"":obj[0].toString());
			vo.setGeoAllocation(obj[1]==null?"":obj[1].toString());
			vo.setSector(obj[2]==null?"":obj[2].toString());
			vo.setStrategyName(obj[3]==null?"":obj[3].toString());
			vo.setRiskLevel(obj[4]==null?"":obj[4].toString());
			vo.setInvestmentGoal(obj[5]==null?"":obj[5].toString());
			vo.setReason(obj[6]==null?"":obj[6].toString());
			
			String creatorId = obj[7]==null?"":obj[7].toString();
			String iconUrl = obj[10]==null?"":obj[10].toString();
			vo.setCreator(getCommonMemberName(creatorId, langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(iconUrl,null));
			
			vo.setCreateTime(DateUtil.dateToDateString( DateUtil.getDate(obj[8]==null?"":obj[8].toString()),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString( DateUtil.getDate(obj[9]==null?"":obj[9].toString()),"yyyy-MM-dd HH:mm:ss"));
			
			vo.setSuitability(obj[11]==null?"":obj[11].toString());
			vo.setIfFollow(obj[12]==null?"":obj[12].toString());
		}
		
		List<AppStrategyAllocationVO> allocationList = findStrategyAllocationList(strategyId,null,langCode);
		vo.setAllocationList(allocationList);
		return vo;
	}
}
