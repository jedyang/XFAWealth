/**
 * 
 */
package com.fsll.app.watch.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.market.vo.AppPortfolioArenaMessVo;
import com.fsll.app.investor.market.vo.AppStrategyInfoVO;
import com.fsll.app.investor.market.vo.AppStrategyMessVo;
import com.fsll.app.watch.service.AppWatchMessService;
import com.fsll.app.watch.vo.AppFundInfoVO;
import com.fsll.app.watch.vo.AppPortfolioInfoVO;
import com.fsll.app.watch.vo.AppWatchFundVO;
import com.fsll.app.watch.vo.AppWatchProductVo;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaReturn;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.PortfolioHoldReturn;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.entity.WebWatchlistAlert;
import com.fsll.wmes.entity.WebWatchlistType;

/**
 * 自选接口服务接口实现类
 * @author zpzhou
 * @date 2016-9-30
 */
@Service("appWatchMessService")
//@Transactional
public class AppWatchMessServiceImpl extends BaseService implements AppWatchMessService {
	
	/**
	 * 产品的关注/取消
	 * @param relateID 对应类型id
	 * @param memberID 网站会员ID
	 * @param OpType	Follow-设置关注;Delete-取消关注
	 * @param moduleType 对应模块,组合，策略关注
	 */
	public WebFollow saveWebFollowMess(String relateId, String memberId,String opType, String moduleType) {
		String hql = "from WebFollow t where t.relateId=? and t.member.id=? and t.moduleType=? and t.isValid='1' ";
		List params = new ArrayList();
		params.add(relateId);
		params.add(memberId);
		params.add(moduleType);
		List<WebFollow> list = this.baseDao.find(hql, params.toArray(), false);
		if("Follow".equalsIgnoreCase(opType) && list.isEmpty()){
			WebFollow follow = new WebFollow();
			MemberBase member = (MemberBase)this.baseDao.get(MemberBase.class, memberId);
			follow.setCreateTime(new Date());
			follow.setIsValid("1");
			follow.setMember(member);
			follow.setModuleType(moduleType);
			follow.setRelateId(relateId);
			this.baseDao.create(follow);
			return follow;
		}else if("Delete".equalsIgnoreCase(opType) && !list.isEmpty()){
			WebFollow follow = list.get(0);
            this.baseDao.delete(follow);
		}
		return null;
		
	}
	
	
	/**
	 * 产品的关注
	 * @param relateID 对应类型id
	 * @param memberID 网站会员ID
	 * @param moduleType 对应模块,组合，策略关注
	 */
	public String findWebFollowCount(String relateId, String memberId, String moduleType) {
		String hql = "from WebFollow t where t.relateId=? and t.member.id=? and t.moduleType=? and t.isValid='1' ";
		List params = new ArrayList();
		params.add(relateId);
		params.add(memberId);
		params.add(moduleType);
		List<WebFollow> list = this.baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){			
			return String.valueOf(list.size());
		}
		return "0";
		
	}
	
	
	/**
	 * 产品的自选/取消
	 * @author zxtan
	 * @date 2017-02-14
	 * @param productId 产品id
	 * @param typeId 自选类型Id
	 * @param memberId 网站会员ID
	 * @param OpType	add-设置自选;Delete-取消自选
	 */
	public WebWatchlist saveWebWatchlistMess(String productId, String typeId, String memberId,String opType) {
		StringBuilder hql = new StringBuilder();
		hql.append("from WebWatchlist t where t.isValid='1' and t.product.id=? and t.member.id=? ");
		List params = new ArrayList();
		params.add(productId);
		params.add(memberId);
		if(null != typeId && !"".equals(typeId)){
			hql.append(" and t.webWatchlistType.id=? ");
			params.add(typeId);
		}
		
		List<WebWatchlist> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if("add".equalsIgnoreCase(opType)){
			WebWatchlist watchlist = new WebWatchlist();
			if(list.isEmpty()){				
				ProductInfo product = (ProductInfo) this.baseDao.get(ProductInfo.class, productId);
				MemberBase member = (MemberBase)this.baseDao.get(MemberBase.class, memberId);
				WebWatchlistType type = (WebWatchlistType) this.baseDao.get(WebWatchlistType.class, typeId);
				watchlist.setProduct(product);
				watchlist.setWebWatchlistType(type);
				watchlist.setMember(member);
				watchlist.setCreateTime(new Date());
				watchlist.setLastUpdate(new Date());
				watchlist.setOrderBy(1);
				watchlist.setIsValid("1");
				watchlist = (WebWatchlist) this.baseDao.create(watchlist);
			}else {
				watchlist = list.get(0);
			}			
			return watchlist;
		}else if("Delete".equalsIgnoreCase(opType) && !list.isEmpty()){
			WebWatchlist watchlist= list.get(0);
            this.baseDao.delete(watchlist);
		}
		return null;
		
	}

	/**
	 * 得到自选分类信息
	 * @param memberID 网站会员ID
	 */
	public  List<WebWatchlistType> getWatchTypeMess(String memberId){
		String hql = "from WebWatchlistType t where t.member.id=? and t.isValid='1' order by t.createTime desc";
		List params = new ArrayList();
		params.add(memberId);
		List<WebWatchlistType> list = this.baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (WebWatchlistType webWatchlistType : list) {
				int count = findWatchProductCount(webWatchlistType.getId(),memberId);
				webWatchlistType.setProductCount(String.valueOf(count));
			}
		}
		return list;
	}
	
	/**
	 * 根据自选类别得到我自选的产品列表
	 * @param memberId 用户ID
	 * @param typeId 分类ID
	 * @return int 列表数据
	 */
	private int findWatchProductCount(String typeId,String memberId){
		
		String hql = " from WebWatchlist t " +
				" where t.webWatchlistType.id=? and t.member.id=? and t.isValid=? ";
		List params = new ArrayList();
		params.add(typeId);
		params.add(memberId);
		params.add("1");
		List list= baseDao.find(hql, params.toArray(), false);
		
		return list.size();
	}
	
	/**
	 * 获取自选预警设置信息
	 * @param productId 产品ID
	 * @param memberID 网站会员ID
	 */
	public List<WebWatchlistAlert> getWatchAlertMess(String productId,String memberId){
		String hql = "from WebWatchlistAlert t where t.product.id=? and t.member.id=?  order by t.type asc";
		List params = new ArrayList();
		params.add(productId);
		params.add(memberId);
		List<WebWatchlistAlert> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	/**
	 * 保存自选预警设置信息
	 * @param productId 产品ID
	 * @param memberID 网站会员ID
	 * @param array 预警设置信息
	 * @return 0:成功  -1:失败
	 */
	public int saveWatchAlertMess(String productId,String memberId,JSONArray array){
		try {
			//先删除原来的记录，再新增
			String hql = "delete from WebWatchlistAlert t where t.product.id=? and t.member.id=? ";
			List params = new ArrayList();
			params.add(productId);
			params.add(memberId);
			baseDao.updateHql(hql, params.toArray());
			MemberBase member=new MemberBase();
			member.setId(memberId);
			ProductInfo product=new ProductInfo();
			product.setId(productId);
			for (int i = 0; i < array.size(); i++) {
		        JSONObject object = (JSONObject) array.get(i);
		        WebWatchlistAlert alert=new WebWatchlistAlert();
		        alert.setDataValue(object.optString("dataValue",""));
		        alert.setIfOpen(object.optString("ifOpen",""));
		        alert.setType(object.optString("type",""));
		        alert.setUpDown(object.optString("upDown", ""));
		        alert.setProduct(product);
		        alert.setMember(member);
		        baseDao.create(alert);
		    }
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 新增/修改自选分类信息
	 * @param typeId 分类ID
	 * @param memberID 用户ID
	 * @param name 分类名称
	 * @return 0:成功  -1:失败
	 */
	public int saveWatchTypeMess(String typeId,String memberId,String name){
		try {
			WebWatchlistType type =new WebWatchlistType();
			if("".equals(typeId)){//新增
				type.setCreateTime(new Date());
				type.setLastUpdate(new Date());
				type.setIsValid("1");
			}else{//修改
				type=(WebWatchlistType)baseDao.get(WebWatchlistType.class, typeId);
				type.setLastUpdate(new Date());
			}
			MemberBase member=new MemberBase();
			member.setId(memberId);
			type.setMember(member);
			type.setName(name);
			baseDao.saveOrUpdate(type);
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 删除自选分类信息
	 * @param typeId 分类ID
	 * @return 0:成功  -1:失败
	 */
	public int deleteWatchTypeMess(String typeId){
		try {
			String hql = "from WebWatchlist t where t.webWatchlistType.id=?";
			List params = new ArrayList();
			params.add(typeId);
			List<WebWatchlist> list = this.baseDao.find(hql, params.toArray(), false);
			for(WebWatchlist watch :list){
				//先删除自选预警设置信息
				if(null != watch.getProduct() && null != watch.getMember()){
					String hqlExt = "delete WebWatchlistAlert t  where t.product.id=? and t.member.id=?  ";
					List paramsExt = new ArrayList();
					paramsExt.add(watch.getProduct().getId());
					paramsExt.add(watch.getMember().getId());
					baseDao.updateHql(hqlExt, paramsExt.toArray());
				}
			}
			//再删除自选信息
			String hqlWatch = "delete WebWatchlist t where t.webWatchlistType.id=? ";
			List paramsWatch= new ArrayList();
			paramsWatch.add(typeId);
			baseDao.updateHql(hqlWatch, paramsWatch.toArray());
			//最后再删除自选分类信息
			String hqlType = "delete WebWatchlistType t where t.id=? ";
			List paramsType= new ArrayList();
			paramsType.add(typeId);
			baseDao.updateHql(hqlType, paramsType.toArray());
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 根据自选类别得到我自选的产品列表
	 * @param memberId 用户ID
	 * @param typeId 分类ID
	 * @return List 列表数据
	 */
	public List<AppWatchProductVo> findWatchProductList(String typeId,String memberId,String langCode){
		List<AppWatchProductVo>  messList = new ArrayList<AppWatchProductVo>();
		String hql = "select p.type,f.id,fl.fundName,s.id,sl.stockName,b.id,bl.bondName,p.createTime,p.lastUpdate" +
				" from WebWatchlist t " +
				" inner join t.product p on p.isValid=? " +
				" left join FundInfo f on f.product.id=p.id "+
				" left join "+this.getLangString("FundInfo", langCode)+" fl on fl.id=f.id "+
				" left join StockInfo s on s.product.id=p.id "+
				" left join "+this.getLangString("StockInfo", langCode)+" sl on sl.id=s.id "+
				" left join BondInfo b on b.product.id=p.id "+
				" left join "+this.getLangString("BondInfo", langCode)+" bl on bl.id=b.id "+
				" where t.webWatchlistType.id=? and t.member.id=? and t.isValid=? order by t.orderBy asc";
		List params = new ArrayList();
		params.add("1");
		params.add(typeId);
		params.add(memberId);
		params.add("1");
		List list= baseDao.find(hql, params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppWatchProductVo vo =new AppWatchProductVo();
			String type=objs[0]==null?"":objs[0].toString();//产品类型,fund基金,stock股票,bond债券,futures期货
			vo.setProductType(type);
			if("fund".equals(type)){
				vo.setId(objs[1]==null?"":objs[1].toString());	
				vo.setProductName(objs[2]==null?"":objs[2].toString());
			}else if("stock".equals(type)){
				vo.setId(objs[3]==null?"":objs[3].toString());	
				vo.setProductName(objs[4]==null?"":objs[4].toString());
			}else if("bond".equals(type)){
				vo.setId(objs[5]==null?"":objs[5].toString());	
				vo.setProductName(objs[6]==null?"":objs[6].toString());
			}
			vo.setCreateTime(objs[7]==null?"":objs[7].toString());
			vo.setLastUpdate(objs[8]==null?"":objs[8].toString());
			messList.add(vo);
		}
		return messList;
	}
	
	
	
	/**
	 * 得到我自选的基金产品列表
	 * @author zxtan
	 * @date 2017-02-20
	 * @param memberId 用户ID
	 * @param typeId 类型ID
	 * @param langCode 语言
	 * @param toCurrency 货币转换
	 * @return List 列表数据
	 */
	public List<AppWatchFundVO> findWatchFundList(String memberId,String typeId,String langCode,String toCurrency){
		List<AppWatchFundVO>  messList = new ArrayList<AppWatchFundVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from WebWatchlist t ");
		hql.append(" inner join FundInfo f on f.product.id=t.product.id ");
		hql.append(" inner join "+this.getLangString("FundInfo", langCode)+" fl on fl.id=f.id ");
		hql.append(" where t.member.id=? and t.isValid='1' ");
		List params = new ArrayList();
		params.add(memberId);
		if(!"".equals(typeId)){
			hql.append(" and t.webWatchlistType.id=? ");
			params.add(typeId);
		}
		hql.append(" order by t.createTime asc");
		
		List list= baseDao.find(hql.toString(), params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			FundInfo fundInfo = (FundInfo) objs[1];
			FundInfoSc fundInfoSc = new FundInfoSc();
			BeanUtils.copyProperties(objs[2], fundInfoSc);
			AppWatchFundVO vo =new AppWatchFundVO();
			
			vo.setId(fundInfo.getId());
			vo.setProductId(fundInfo.getProduct().getId());
			vo.setFundName(fundInfoSc.getFundName());
			String fromCurrency = fundInfoSc.getFundCurrencyCode();
			vo.setFundCurrency(fundInfoSc.getFundCurrency());
			vo.setFundCurrencyCode(fromCurrency);
			vo.setIssueCurrency(fundInfoSc.getIssueCurrency());
			vo.setIssueCurrencyCode(fundInfoSc.getIssueCurrencyCode());
			vo.setRiskLevel(String.valueOf(fundInfo.getRiskLevel()));
			vo.setFundType(fundInfoSc.getFundType());
			vo.setLastNavUpdate(DateUtil.dateToDateString(fundInfo.getLastNavUpdate(), "yyyy-MM-dd"));
			if(null != toCurrency && !"".equals(toCurrency)){
				vo.setToCurrency(toCurrencyName);
				String lastNav = getFormatNumByCurrency(fundInfo.getLastNav(), fromCurrency, toCurrency);
				vo.setLastNav(lastNav);
			}else {
				vo.setToCurrency(getParamConfigName(langCode, fromCurrency, "currency_type"));
				vo.setLastNav(getFormatNum(fundInfo.getLastNav(),fromCurrency));
			}
			
			vo.setDayReturn(getFormatNumByPer(fundInfo.getDayReturn()));		
			
			messList.add(vo);
		}
		return messList;
	}
	
	
	/**
	 * 得到我自选的投资策略列表
	 * @param memberId 用户ID
	 * @return List 数据
	 */
	public List<AppStrategyMessVo> findWatchStrategyList(String memberId,String langCode){
		List<AppStrategyMessVo>  messList = new ArrayList<AppStrategyMessVo>();
		String hql = " from WebFollow w " +
		             " inner join StrategyInfo s on s.id=w.relateId "+
                     " where w.member.id=? and w.moduleType=? and w.isValid=? ";
		List params = new ArrayList();
		params.add(memberId);
		params.add("strategy");
		params.add("1");
		List list= baseDao.find(hql, params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			StrategyInfo info=(StrategyInfo)objs[1];
			AppStrategyMessVo vo =new AppStrategyMessVo();
			vo.setId(info.getId());
			vo.setStrategyName(info.getStrategyName());
			vo.setRiskLevel(info.getRiskLevel());
			vo.setDescription(info.getDescription());
			vo.setInvestmentGoal(info.getInvestmentGoal());
			vo.setOverhead(info.getOverhead());
			vo.setReason(info.getReason());
			vo.setSuitability(info.getSuitability());
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			MemberBase memberBase=info.getCreator();
			vo.setCreator(getCommonMemberName(memberBase.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(memberBase.getIconUrl(), null));
			messList.add(vo);
		}
		return messList;
	}
	/**
	 * 得到我自选的投资组合列表
	 * @param memberId 用户ID
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @return List 列表数据
	 */
	public List<AppPortfolioArenaMessVo> findWatchPortfolioList(String memberId,String periodCode,String langCode){
		List<AppPortfolioArenaMessVo>  messList = new ArrayList<AppPortfolioArenaMessVo>();
		List params = new ArrayList();
		String hql = " from WebFollow w " +
			         " inner join PortfolioArena s on s.id=w.relateId "+
			         " left join PortfolioArenaReturn r on r.portfolio.id=s.id and r.periodCode=? "+
			         " where w.member.id=? and w.moduleType=? and w.isValid=? ";
		params.add(periodCode);
		params.add(memberId);
		params.add("portfolio_arena");
		params.add("1");
		List list = baseDao.find(hql, params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioArena info=(PortfolioArena)objs[1];
			AppPortfolioArenaMessVo vo =new AppPortfolioArenaMessVo();
			vo.setId(info.getId());
			vo.setPortfolioName(info.getPortfolioName());
			vo.setRiskLevel(String.valueOf(info.getRiskLevel()));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setOverhead(info.getOverhead());
			if(null!=objs[2]){
				PortfolioArenaReturn arenaReturn=(PortfolioArenaReturn)objs[2];
				vo.setIncrease(getFormatNumByPer(arenaReturn.getIncrease()));
			}
			MemberBase memberBase=info.getCreator();
			vo.setCreator(getCommonMemberName(memberBase.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(memberBase.getIconUrl(), null));
			messList.add(vo);
		}
		return messList;
	}
	
	/**
	 * 得到感兴趣的基金信息
	 * @param memberId
	 * @param langCode
	 * @param periodCode
	 * @param rows
	 * @return
	 */
	public List<AppFundInfoVO> findVisitedFundList(String memberId,String langCode,String periodCode,int rows){
		List<AppFundInfoVO> voList = new ArrayList<AppFundInfoVO>();
		StringBuilder hql = new StringBuilder();
		List params = new ArrayList();
		hql.append(" select f.id,f.product.id,x.fundName,x.fundCurrency,f.riskLevel,x.fundType,r.increase,x.issueCurrency,x.issueCurrencyCode");
		hql.append(" from FundInfo f left join "+this.getLangString("FundInfo", langCode));
		hql.append(" x on f.id=x.id ");
		//时间类型:return_period_code_1W,return_period_code_1M,return_period_code_1Y...
		hql.append(" left join FundReturn r on r.fund.id=f.id ");
		hql.append(" left join WebInvestorVisit v on f.id = v.relateId ");
		hql.append(" where v.moduleType='fund' and r.periodCode=? and v.member.id=? ");
		hql.append(" order by v.vistiTime desc, r.increase desc ");
		
		params.add(periodCode);
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);
		if( null == list || list.isEmpty()){
			
			hql = new StringBuilder();
			params = new ArrayList();
			hql.append(" select f.id,f.product.id,x.fundName,x.fundCurrency,f.riskLevel,x.fundType,r.increase,x.issueCurrency,x.issueCurrencyCode");
			hql.append(" from FundInfo f left join "+this.getLangString("FundInfo", langCode));
			hql.append(" x on f.id=x.id ");
			hql.append(" left join FundReturn r on r.fund.id=f.id ");			
			hql.append(" where r.periodCode=?  ");
			hql.append(" order by r.increase desc ");

			params.add(periodCode);
			
			list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);
			
		}
		
		if( null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				AppFundInfoVO  vo = new AppFundInfoVO();
				vo.setId(objs[0]==null?"":objs[0].toString());
				vo.setProductId(objs[1]==null?"":objs[1].toString());
				vo.setFundName(objs[2]==null?"":objs[2].toString());
				vo.setFundCurrency(objs[3]==null?"":objs[3].toString());
				vo.setRiskLevel(objs[4]==null?"":objs[4].toString());
				vo.setFundType(objs[5]==null?"":objs[5].toString());				
				vo.setIncrease(objs[6]==null?"":objs[6].toString());
				vo.setIssueCurrency(objs[7]==null?"":objs[7].toString());				
				vo.setIssueCurrencyCode(objs[8]==null?"":objs[8].toString());
				
				if(null==vo.getIncrease() || "".equals(vo.getIncrease())){
					vo.setIncrease(getFormatNumByPer(null));
				}else{
					vo.setIncrease(getFormatNumByPer(Double.parseDouble(vo.getIncrease())));
				}
				voList.add(vo);
			}
			
		}
		
		return voList;
	}
	
	
	/**
	 * 得到感兴趣的组合信息
	 * @param memberId
	 * @param langCode
	 * @param periodCode
	 * @param rows
	 * @return
	 */
	public List<AppPortfolioInfoVO> findVisitedPortfolioList(String memberId,String langCode,String periodCode,int rows){
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
				AppPortfolioInfoVO vo =new AppPortfolioInfoVO();
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
				vo.setCreator(getCommonMemberName(memberBase.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				vo.setIconUrl(PageHelper.getUserHeadUrlForWS(memberBase.getIconUrl(),null));
				voList.add(vo);
			}				
		}
		
		return voList;
	}

	
	/**
	 * 得到感兴趣的策略信息
	 * @param memberId
	 * @param langCode
	 * @param periodCode
	 * @param rows
	 * @return
	 */
	public List<AppStrategyInfoVO> findVisitedStrategyList(String memberId,String langCode,int rows){
		List<AppStrategyInfoVO>  voList = new ArrayList<AppStrategyInfoVO>();
		List params = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append(" select i.id,fn_getconfigname(i.geoAllocation,'"+langCode+"') as geoAllocation,fn_getconfigname(i.sector,'"+langCode+"') as sector,");
		hql.append(" i.strategyName,i.riskLevel,i.investmentGoal,i.reason,m.nickName,i.createTime,i.lastUpdate,m.iconUrl,m.id  from StrategyInfo i ");
		hql.append(" left join MemberBase m on m.id=i.creator.id");
		hql.append(" left join WebInvestorVisit v on i.id = v.relateId ");
		hql.append(" where v.moduleType='strategy' and v.member.id=? ");
		hql.append(" order by v.vistiTime desc, i.createTime desc ");
		
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);
		
		if(null == list || list.isEmpty()){			
			params = new ArrayList();
			hql = new StringBuilder();
			hql.append(" select i.id,fn_getconfigname(i.geoAllocation,'"+langCode+"') as geoAllocation,fn_getconfigname(i.sector,'"+langCode+"') as sector,");
			hql.append(" i.strategyName,i.riskLevel,i.investmentGoal,i.reason,m.nickName,i.createTime,i.lastUpdate,m.iconUrl,m.id  from StrategyInfo i ");
			hql.append(" left join MemberBase m on m.id=i.creator.id");
			hql.append(" inner join WebView v on v.relateId=i.id ");
			hql.append(" where i.status='1' and v.scopeFlag='2' ");
			hql.append(" order by i.createTime desc ");
						
			list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);			
		}
		
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] obj = (Object[]) list.get(i);
				AppStrategyInfoVO vo =new AppStrategyInfoVO();
				vo.setId(obj[0]==null?"":obj[0].toString());
				vo.setGeoAllocation(obj[1]==null?"":obj[1].toString());
				vo.setSector(obj[2]==null?"":obj[2].toString());
				vo.setStrategyName(obj[3]==null?"":obj[3].toString());
				vo.setRiskLevel(obj[4]==null?"":obj[4].toString());
				vo.setInvestmentGoal(obj[5]==null?"":obj[5].toString());
				vo.setReason(obj[6]==null?"":obj[6].toString());
				String creatorId = obj[11]==null?"":obj[11].toString();
				String iconUrl = obj[10]==null?"":obj[10].toString();
				vo.setCreator(getCommonMemberName(creatorId, langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				vo.setCreateTime(DateUtil.dateToDateString( DateUtil.getDate(obj[8]==null?"":obj[8].toString()),"yyyy-MM-dd HH:mm:ss"));
				vo.setLastUpdate(DateUtil.dateToDateString( DateUtil.getDate(obj[9]==null?"":obj[9].toString()),"yyyy-MM-dd HH:mm:ss"));
				vo.setIconUrl(PageHelper.getUserHeadUrlForWS(iconUrl, null));
				voList.add(vo);
			}
		}
		
		
		return voList;
	}
	
}
