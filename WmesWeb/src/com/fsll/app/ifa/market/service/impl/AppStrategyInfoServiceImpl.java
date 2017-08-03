package com.fsll.app.ifa.market.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.app.ifa.market.service.AppStrategyInfoService;
import com.fsll.app.ifa.market.vo.AppStrategyAllocationVO;
import com.fsll.app.ifa.market.vo.AppStrategyInfoItemVO;
import com.fsll.app.ifa.market.vo.AppStrategyInfoVO;
import com.fsll.app.ifa.market.vo.AppStrategyProductVo;
import com.fsll.app.watch.service.AppWatchMessService;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.StrategyInfo;

/**
 * IFA策略管理服务实现
 * @author zxtan
 * @date 2017-03-29
 */
@Service("appIfaMarketStrategyInfoService")
public class AppStrategyInfoServiceImpl extends BaseService implements
		AppStrategyInfoService {
	@Autowired
	private AppWatchMessService watchMessService;
	
	/**
	 * IFA策略管理列表
	 * @author zxtan
	 * @date 2017-03-29
	 */
	public JsonPaging findMyStrategyInfoList(JsonPaging jsonPaging,
			String memberId, String keyword, String status, String isValid,
			String langCode) {
		// TODO Auto-generated method stub
		StringBuilder hql = new StringBuilder();
		hql.append("from StrategyInfo m where m.creator.id=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		
		if(!"".equals(keyword)){
			hql.append(" and m.strategyName like ? ");
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
		
		hql.append(" order by m.overhead desc ,m.lastUpdate desc ");
		
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<StrategyInfo> list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			List<AppStrategyInfoItemVO> voList = new ArrayList<AppStrategyInfoItemVO>();
			for (int i = 0; i < list.size(); i++) {
				AppStrategyInfoItemVO vo = new AppStrategyInfoItemVO();
				StrategyInfo info = list.get(i);
				vo.setStrategyId(info.getId());
				vo.setStrategyName(info.getStrategyName());
				String sector = getParamConfigNameList(langCode, info.getSector());
				String geo = getParamConfigNameList(langCode, info.getGeoAllocation());
				vo.setGeoAllocation(geo);
				vo.setSector(sector);
				vo.setRiskLevel(info.getRiskLevel());
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
				vo.setCreatorId(info.getCreator().getId());
				vo.setCreatorIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(), null));
				vo.setCreatorName(getCommonMemberName(info.getCreator().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME));
				vo.setStatus(info.getStatus());
				vo.setIsValid(info.getIsValid());
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		
		return jsonPaging;
	}
	
	/**
	 * IFA策略列表
	 * @author zxtan
	 * @date 2017-04-25
	 */
	public JsonPaging findAllStrategyInfoList(JsonPaging jsonPaging,
			String memberId, String keyword, String langCode) {
		// TODO Auto-generated method stub
		StringBuilder hql = new StringBuilder();
		hql.append("from StrategyInfo m ");
		hql.append(" where m.status='1' and m.isValid='1' ");
		hql.append(" and ( m.creator.id=? or m.id in (select v.relateId from WebView v where  v.moduleType=? ");
		hql.append(" 	and (v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?)))) ) ");
		
		
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add("strategy");
		params.add(memberId);
		
		if(!"".equals(keyword)){
			hql.append(" and m.strategyName like ? ");
			params.add("%"+keyword +"%");
		}
				
		hql.append(" order by m.overhead desc ,m.lastUpdate desc ");
		
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<StrategyInfo> list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			List<AppStrategyInfoItemVO> voList = new ArrayList<AppStrategyInfoItemVO>();
			for (int i = 0; i < list.size(); i++) {
				AppStrategyInfoItemVO vo = new AppStrategyInfoItemVO();
				StrategyInfo info = list.get(i);
				vo.setStrategyId(info.getId());
				vo.setStrategyName(info.getStrategyName());
				String sector = getParamConfigNameList(langCode, info.getSector());
				String geo = getParamConfigNameList(langCode, info.getGeoAllocation());
				vo.setGeoAllocation(geo);
				vo.setSector(sector);
				vo.setRiskLevel(info.getRiskLevel());
				vo.setInvestmentGoal(info.getInvestmentGoal());
				vo.setSuitability(info.getSuitability());
				vo.setReason(info.getReason());
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
				vo.setCreatorId(info.getCreator().getId());
				vo.setCreatorIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(), null));
				vo.setCreatorName(getCommonMemberName(info.getCreator().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME));
				vo.setStatus(info.getStatus());
				vo.setIsValid(info.getIsValid());
				
				String ifFollow = watchMessService.findWebFollowCount(info.getId(), memberId, "strategy");
				vo.setIfFollow(ifFollow);
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		
		return jsonPaging;
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
//		String hql = "select i.id,fn_getconfigname(i.geoAllocation,'"+langCode+"') as geoAllocation,fn_getconfigname(i.sector,'"+langCode+"') as sector,i.strategyName,i.riskLevel,i.investmentGoal,i.reason,i.creator.nickName,i.createTime,i.lastUpdate,i.creator.iconUrl  from StrategyInfo i  where i.id=? ";
		String hql = "from StrategyInfo i  where i.id=?";
		List params = new ArrayList();
		params.add(strategyId);
		List<StrategyInfo> list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			StrategyInfo info = (StrategyInfo) list.get(0);
			vo.setId(info.getId());
			vo.setStrategyName(info.getStrategyName());
			String sector = getParamConfigNameList(langCode, info.getSector());
			String geo = getParamConfigNameList(langCode, info.getGeoAllocation());
			vo.setGeoAllocation(geo);
			vo.setSector(sector);
			vo.setRiskLevel(info.getRiskLevel());
			vo.setInvestmentGoal(info.getInvestmentGoal());
			vo.setSuitability(info.getSuitability());
			vo.setReason(info.getReason());
			vo.setDescription(info.getDescription());
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
			vo.setOverhead(info.getOverhead());
			vo.setOverheadTime(DateUtil.dateToDateString(info.getOverheadTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setCreatorId(info.getCreator().getId());
			vo.setCreatorIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(), null));
			vo.setCreatorName(getCommonMemberName(info.getCreator().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setStatus(info.getStatus());
			vo.setIsValid(info.getIsValid());
			
			String ifFollow = watchMessService.findWebFollowCount(info.getId(), memberId, "strategy");
			vo.setIfFollow(ifFollow);
		}
		
		List<AppStrategyAllocationVO> allocationList = findStrategyAllocationList(strategyId,null,langCode);
		vo.setAllocationList(allocationList);
		return vo;
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
		hql.append(" ORDER BY a.methodType,a.layerLevel");		
		Map<Integer, AppStrategyAllocationVO> map = new TreeMap<Integer, AppStrategyAllocationVO>();
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
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
			
			if(null==vo.getItemWeight() || "".equals(vo.getItemWeight())){
				vo.setItemWeight(getFormatNum(null));
			}else{
				vo.setItemWeight(getFormatNum(Double.parseDouble(vo.getItemWeight())));
			}
			
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
		return voList;
	}
	
	/**
	 * 得到投资策略的产品信息列表
	 * @param strategyId 策略ID
	 * @param langCode 语言
	 * @param periodCode 回报时间类型
	 * @return
	 */
	public List<AppStrategyProductVo> findStrategyProductList(String strategyId,String langCode,String periodCode,String productType,int rows,String toCurrency){
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
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list && !list.isEmpty()){
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
					vo.setLastNav(getFormatNum(Double.parseDouble(vo.getLastNav())));
				}
				if(null==vo.getIncrease() || "".equals(vo.getIncrease())){
					vo.setIncrease(getFormatNumByPer(null));
				}else{
					vo.setIncrease(getFormatNumByPer(Double.parseDouble(vo.getIncrease())));
				}
				messList.add(vo);
			}
		}
		return messList;
	}

	/**
	 * 更新策略信息（设置是否有效）
	 */
	public boolean updateStrategyInfo( String id,	String isValid) {
		// TODO Auto-generated method stub
		StrategyInfo info = (StrategyInfo) baseDao.get(StrategyInfo.class, id);
		if(null != info) {
			info.setIsValid(isValid);
			baseDao.update(info);
			return true;
		}
		return false;
	}
	
	/**
	 * 删除策略信息
	 */
	public boolean deleteStrategyInfo( String id) {
		// TODO Auto-generated method stub
		StrategyInfo info = (StrategyInfo) baseDao.get(StrategyInfo.class, id);
		if(null != info){
			info.setIsValid("0");
			baseDao.update(info);
			return true;
		}
		return false;
	}

}
