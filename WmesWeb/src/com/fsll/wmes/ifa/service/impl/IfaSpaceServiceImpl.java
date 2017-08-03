package com.fsll.wmes.ifa.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.FundReturn;
import com.fsll.wmes.entity.FundReturnEn;
import com.fsll.wmes.entity.FundReturnSc;
import com.fsll.wmes.entity.FundReturnTc;
import com.fsll.wmes.entity.IfaExtInfo;
import com.fsll.wmes.entity.IfaFeeItem;
import com.fsll.wmes.entity.IfaHighlight;
import com.fsll.wmes.entity.InsightCount;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.LiveInfo;
import com.fsll.wmes.entity.LiveScheduler;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaCumperf;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.entity.WebFriend;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebPushDetail;
import com.fsll.wmes.entity.WebRecommended;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifa.service.IfaSpaceService;
import com.fsll.wmes.ifa.vo.IfaRecommendNewsVO;
import com.fsll.wmes.ifa.vo.IfaSpaceFundVO;
import com.fsll.wmes.ifa.vo.IfaSpaceInsightVO;
import com.fsll.wmes.ifa.vo.IfaSpaceLiveVO;
import com.fsll.wmes.ifa.vo.IfaSpaceNewsVO;
import com.fsll.wmes.ifa.vo.IfaSpacePortfoliosVO;
import com.fsll.wmes.ifa.vo.IfaSpaceStrategyInfoVO;
import com.fsll.wmes.ifa.vo.IfaSpaceVO;
import com.fsll.wmes.ifa.vo.IfaSpaceVisitVO;
import com.fsll.wmes.ifa.vo.InsightVistorVo;
import com.fsll.wmes.ifa.vo.RecommendInfoVO;
import com.fsll.wmes.strategy.vo.StrategyWebPushVO;

/**
 * 个人空间信息查询服务接口
 * @author 林文伟
 * @date 2016-8-19
 */
@Service("ifaSpaceService")
//@Transactional
public class IfaSpaceServiceImpl extends BaseService implements IfaSpaceService{
	@Autowired
	 private SysParamService sysParamService;
	@Autowired
	 private IfaInfoService ifaInfoService;
	@Autowired
	private FundInfoService fundInfoService;
	
	/**
	 * 通过IFA的ID获取IFA信息，用于空间数据展示
	 * @author 林文伟
	 * @param langCode 语言
	 * @return
	 */
	public IfaSpaceVO loadIfaSpaceData(String loginMemberId,String memberId,String langCode,String regionId,String curDefCurrency) {
		
		IfaSpaceVO vo = new IfaSpaceVO();
		//获取个人基础信息
		String hql = "select a.id,m.id,m.iconUrl,b.companyName,m.nickName,m.investStyle,m.investField,m.liveRegion,a.spaceShowAum,a.popularityTotal,a.gender,m.defCurrency,c.firmLogo,a.spaceShowPerformance,m.languageSpoken ";
		hql +=" from MemberIfa a ";
		hql += " inner join MemberBase m on m.id = a.member.id ";
		hql += " inner join "+this.getLangString("MemberIfafirm", langCode)+" b on b.id = a.ifafirm.id ";
		hql += " inner join MemberIfafirm  c on c.id = b.id ";
		hql += " left join AccessoryFile f on f.id = c.firmLogo";
		hql += " where m.id = '"+memberId+"' ";
		List list = this.baseDao.find(hql, null, false);
		String ifaId = "";
		String ifaHeadImg = "";//头像
		String ifaName = "";
		String ifaFirmName = "";
		String spaceShowAum = "0";//默认不显示AUM值
		String spaceShowPerformance = "0";
		String popularityTotal = "0";//人气值
		String defCurrency = "";//默认货币值
		String gender = "";
		String ifaFirmLogoPath = "";
		if(!list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			ifaId = objs[0]==null?"":objs[0].toString();
			memberId = objs[1]==null?"":objs[1].toString();
			ifaHeadImg = objs[2]==null?"":objs[2].toString();
			ifaFirmName = objs[3]==null?"":objs[3].toString();
			ifaName = objs[4]==null?"":objs[4].toString();
			spaceShowAum = objs[8]==null?"0":objs[8].toString();
			spaceShowPerformance = objs[13]==null?"0":objs[13].toString();
			popularityTotal = objs[9]==null?"0":objs[9].toString();
			defCurrency = objs[11]==null?"HKD":objs[11].toString();//默认HKD
			if(!StringUtils.isNotBlank(defCurrency))defCurrency="HKD";
			ifaFirmLogoPath = objs[12]==null?"":objs[12].toString();
			vo.setIfaId(ifaId);
			vo.setMemberId(memberId);
			vo.setIfaName(ifaName);
			vo.setDefCurrency(defCurrency);
			//设置头像
			gender = objs[10]==null?"":objs[10].toString();
			ifaHeadImg = PageHelper.getUserHeadUrl(ifaHeadImg, gender);
			vo.setIfaHeadImg(ifaHeadImg);
			
			////////////////////
			/////////////////////////
			ifaFirmLogoPath = PageHelper.getLogoUrl(ifaFirmLogoPath, "F");
			 vo.setIfaFirmLogoPath(ifaFirmLogoPath);
			
			vo.setIfaFirmName(ifaFirmName);
			vo.setPopular(Integer.parseInt(popularityTotal));
			
			/*//获取好友数
			StringBuffer getFrinendsHql = new StringBuffer("" +
					" from WebFriend w" +
					" where w.checkResult=1" +
					" and (w.relationships like '%Client%' or w.relationships like '%Prospect%')" +
					" and w.toMember.id=?");
			List params = new ArrayList();
			params.add(memberId);
			List<WebFriend> webFriends = this.baseDao.find(getFrinendsHql.toString(), params.toArray(), false);
			if(webFriends != null){
				vo.setFollowCount(webFriends.size());
			}*/
			
			
			
			
			//生成投资风格,擅长领域，服务区域
			List<String> ifaPersonalCharacteristics = new ArrayList<String>();
			 String investStyle = objs[5]==null?"":objs[5].toString();
			 if(""!=investStyle){
				 List<String> temp = getBaseConfigData(investStyle,langCode);
				 for(String each : temp)
					 ifaPersonalCharacteristics.add(each);
			 }
			 String expertiseType = objs[6]==null?"":objs[6].toString();
			 if(""!=expertiseType){
				 List<String> temp = getBaseConfigData(expertiseType,langCode);
				 for(String each : temp)
					 ifaPersonalCharacteristics.add(each);
			 }
			 String liveRegionName = "";
			 String liveRegion = objs[7]==null?"":objs[7].toString();
			 if(""!=liveRegion){
				 List<String> temp = getBaseConfigData(liveRegion,langCode);
				 for(String each : temp){
					 ifaPersonalCharacteristics.add(each);
					 liveRegionName += each + ",";
				 }
			 }
			 if(""!=liveRegionName){
				 liveRegionName = liveRegionName.substring(0, liveRegionName.length()-1);
				 vo.setLiveRegion(liveRegionName);
			 }
			 
			 String languageSpokenName = "";
			 String languageSpoken = objs[14]==null?"":objs[14].toString();
			 if(""!=languageSpoken){
				 List<String> temp = getBaseConfigData(languageSpoken,langCode);
				 for(String each : temp){
					 languageSpokenName += each + ",";
				 }
			 }
			 if(""!=languageSpokenName){
				 languageSpokenName = languageSpokenName.substring(0, languageSpokenName.length()-1);
				 vo.setLanguageSpoken(languageSpokenName);
			 }
			 
			 vo.setIfaPersonalCharacteristics(ifaPersonalCharacteristics);
			 
			//获取个人最新心情，His Performance的AUM也包含在里面 AUM不在这里显示了
			String hql1 = "from IfaExtInfo t where t.ifa.id='"+ifaId+"'";
			List<IfaExtInfo> list1 = this.baseDao.find(hql1, null, false);
			if(!list1.isEmpty()){
				IfaExtInfo extVO = list1.get(0);
					String hightlight = extVO.getHighlight();
					String aumNumber = extVO.getAumNumber();
					//如果IFA设置不显示AUM数据，则设置为空
					if("1".equals(spaceShowAum)){
						if(null==aumNumber)aumNumber="0";
						//vo.setAumNum(aumNumber);
						vo.setIsShowAumNum(true);
					}
					else{
						//vo.setAumNum("0");
						vo.setIsShowAumNum(false);
					}
					vo.setLatestHighlight(hightlight);
			}
			double aumNum = this.getIfaAUM(ifaId, curDefCurrency);
			vo.setAumNum(aumNum);
			double totalReturnValue = this.getIfaTotalReturnValue(ifaId);
			vo.setTotalReturnValue(totalReturnValue);
			if("1".equals(spaceShowPerformance))vo.setIsSpaceShowPerformance(true);
			else vo.setIsSpaceShowPerformance(false);
			//System.out.println(spaceShowPerformance);
			//获取最新一条观点
			String hql2 = "from InsightInfo t where t.creator.id='"+memberId+"' and t.status=1 order by t.createTime desc ";
			List<InsightInfo> list2 = this.baseDao.find(hql2, null, false);
			if(!list2.isEmpty()){
				InsightInfo temp = list2.get(0);
				String title = temp.getTitle();
				vo.setLatestInsight(title);
			}
//			//获取选择一个要在IFA空间显示的，由该IFA负责的投资组合，这里存放的是投资组合id，对应portfolio_hold
//			//2016-12-9号改为显示IFA全部组合的历史年化回报率
//			 MemberIfa objMemberIfa = (MemberIfa)this.baseDao.get(MemberIfa.class, ifaId);
//			 if(null!=objMemberIfa.getSpaceShowPerformance()){
//				 if(""!=objMemberIfa.getSpaceShowPerformance()){
//					 PortfolioHold portfolioHold = (PortfolioHold)this.baseDao.get(PortfolioHold.class, objMemberIfa.getSpaceShowPerformance());
//					 if(null!=portfolioHold){
//						 vo.setTopProtfoliosName(portfolioHold.getPortfolio().getPortfolioName());
//						 vo.setTopProtfoliosPercentage(portfolioHold.getTotalReturnRate().toString());
//					 }
//					 else{
//						 vo.setTopProtfoliosName("");
//						 vo.setTopProtfoliosPercentage("0");
//					 }
//				 } else{
//					 vo.setTopProtfoliosName("");
//					 vo.setTopProtfoliosPercentage("0");
//				 }
//			 }
//			 else{
//				 vo.setTopProtfoliosName("");
//				 vo.setTopProtfoliosPercentage("0");
//			 }
			//获取选择一个要在IFA空间显示的，由该IFA负责的投资组合，这里存放的是投资组合id，对应portfolio_hold
			//2016-12-9号改为显示IFA全部组合的历史年化回报率
			 MemberIfa objMemberIfa = (MemberIfa)this.baseDao.get(MemberIfa.class, ifaId);
			 if(null!=objMemberIfa.getSpaceShowPerformance()){
				 if(""!=objMemberIfa.getSpaceShowPerformance()){
					 PortfolioHold portfolioHold = (PortfolioHold)this.baseDao.get(PortfolioHold.class, objMemberIfa.getSpaceShowPerformance());
					 if(null!=portfolioHold){
						 vo.setTopProtfoliosName(portfolioHold.getPortfolio().getPortfolioName());
						 vo.setTopProtfoliosPercentage(portfolioHold.getTotalReturnRate().toString());
					 }
					 else{
						 vo.setTopProtfoliosName("");
						 vo.setTopProtfoliosPercentage("0");
					 }
				 } else{
					 vo.setTopProtfoliosName("");
					 vo.setTopProtfoliosPercentage("0");
				 }
			 }
			 else{
				 vo.setTopProtfoliosName("");
				 vo.setTopProtfoliosPercentage("0");
			 }
			 
			 //设置板块
			 vo.setPersonSectionList(getBaseConfigDataList("section_type"));
			 //获取推荐模块之策略
			 vo.setRecommendedStrategies(this.getRecommendedStrategies(vo.getMemberId(),"","",loginMemberId));
			 //获取推荐组合
			 vo.setRecommendedPortfolios(this.getRecommendedPortfolios(vo.getMemberId(),"","",loginMemberId));
			 //获取推荐的基金
			 vo.setFundList(this.getSpaceFundList(vo.getMemberId(),langCode,"",""));
			//观点列表推荐
			 vo.setInsightList(this.getSpaceInsightList(loginMemberId,vo.getMemberId(),"",""));
			//推荐新闻列表
			 //vo.setNewsList(this.getSpaceNewsList(vo.getMemberId(),"",""));
			//获取最新访问
			 vo.setVisitList(this.getSpaceVisitorList(vo.getMemberId(),langCode));
			//获取直播左侧数据
			 //vo.setLiveList(this.getSpaceLiveLeftList(vo.getMemberId(),langCode));
			 //
			 vo.setIfaFeeItemList(this.getIfaFeeItem(ifaId));
		}

		return vo;
	}
	
	/**
	 * 获取某用户擅长领域基础数据
	 * @author 林文伟
	 * @param expertiseType 服务区域,来源于基础数据,保存的是基础数据的code值,多个之间用,分隔
	 * @return
	 */
	public List<String> getBaseConfigData1(String expertiseType) {
		//String nameList = "";
		List<String> nameList = new ArrayList<String>();
		if(!"".equals(expertiseType))
		{
			String[] typeList = expertiseType.split(",");
			for(String each :typeList)
			{
				if(!"".equals(each))
				{
					SysParamConfig config = sysParamService.findByCode(each);
					if(null!=config&&!"".equals(config.getId())){
						//nameList += config.getNameTc()+",";
						nameList.add(config.getNameTc());
					}
				}
			}
		}
		
		return nameList;
	}
	
	/**
	 * 获取某用户擅长领域基础数据
	 * @author 林文伟
	 * @param expertiseType 服务区域,来源于基础数据,保存的是基础数据的code值,多个之间用,分隔
	 * @return
	 */
	public List<String> getBaseConfigData(String typeCode,String langCode) {
		List<String> nameList = new ArrayList<String>();
		if(!"".equals(typeCode))
		{
			String[] typeList = typeCode.split(",");
			for(String each :typeList)
			{
				if(!"".equals(each))
				{
					SysParamConfig config = sysParamService.findByCode(each);
					if(null!=config&&!"".equals(config.getId())){
						if("tc".equals(langCode))nameList.add(config.getNameTc());
						else if("sc".equals(langCode))nameList.add(config.getNameSc());
						else if("en".equals(langCode))nameList.add(config.getNameEn());
					}
				}
			}
		}
		
		return nameList;
	}
	
	/**
	 * 获取某用户擅长领域基础数据
	 * @author 林文伟
	 * @param expertiseType 服务区域,来源于基础数据,保存的是基础数据的code值,多个之间用,分隔
	 * @return
	 */
	public List<GeneralDataVO> getBaseConfigDataList(String expertiseType) {
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		if(!"".equals(expertiseType))
		{
			String[] typeList = expertiseType.split(",");
			for(String each :typeList)
			{
				if(!"".equals(each))
				{
					SysParamConfig config = sysParamService.findByCode(each);
					if(null!=config&&!"".equals(config.getId())){
						GeneralDataVO vo = new GeneralDataVO();
						vo.setId(config.getId());
						vo.setName(config.getNameSc());
						vo.setItemCode(config.getConfigCode());
						list.add(vo);
					}
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 获取推荐模块之策略实体
	 * @author 林文伟
	 * @param memberId 
	 * @param regionTypeCode 
	 * @param sectorTypeCode 
	 * @return
	 */
	public List<IfaSpaceStrategyInfoVO> getRecommendedStrategies(String memberId,String regionTypeCode,String sectorTypeCode,String viewMemberId) {
		List<IfaSpaceStrategyInfoVO> voList = new ArrayList<IfaSpaceStrategyInfoVO>();
		String hql = "from StrategyInfo  t ";
		hql += " where t.creator.id='"+memberId+"' and t.isValid='1' and t.status='1' ";
		if(!"".equals(regionTypeCode))
		{
			String conditionSql = " and ( CONCAT(',' ,t.geoAllocation , ',')  LIKE '%,"+regionTypeCode+",%'  ) ";
			hql+= conditionSql;
		}
		if(!"".equals(sectorTypeCode))
		{
			String conditionSql = " and ( CONCAT(',' ,t.sector , ',')  LIKE '%,"+sectorTypeCode+",%'  ) ";
			hql+= conditionSql;
		}
		hql += " ORDER BY t.overhead DESC,t.createTime DESC";
		List<StrategyInfo> list1 = this.baseDao.find(hql, null, false);
		if(!list1.isEmpty()){
			for(int x=0;x<list1.size();x++){
				IfaSpaceStrategyInfoVO vo = new IfaSpaceStrategyInfoVO();
				String strategyId = list1.get(x).getId();
				 String strategyName = list1.get(x).getStrategyName();
				 int productCount = this.getStrategiesProductCount(strategyId);
				 vo.setStrategyName(strategyName);
				 vo.setContainProCount(productCount);
				 vo.setId(strategyId);
				 //如果访问者刚好是策略的作者，则不需要权限，否则需要判断
				 if(viewMemberId.equals(memberId))voList.add(vo);
				 else {
					 if(getMemberCanViewModule(viewMemberId,strategyId,CommonConstantsWeb.WEB_VIEW_MODULE_STRATEGY))voList.add(vo);
				 }
			}
		}
		return voList;
		

	}
	
	/**
	 * 获取策略里面的产品数
	 * @author 林文伟
	 * @param expertiseType 
	 * @return
	 */
	public int getStrategiesProductCount(String id) {
		int count = 0;
		String hql1 = "from StrategyProduct t where t.strategy.id='"+id+"' ";
		List<StrategyInfo> list1 = this.baseDao.find(hql1, null, false);
		if(!list1.isEmpty())count = list1.size();
		return count;
	}
	
	/**
	 * 获取推荐列表模块
	 * @author 林文伟
	 * @param memberI 
	 * @return
	 */
	public List<IfaSpacePortfoliosVO> getRecommendedPortfolios(String memberId,String regionTypeCode,String sectorTypeCode,String viewMemberId) {
		List<IfaSpacePortfoliosVO> voList = new ArrayList<IfaSpacePortfoliosVO>();
		List params=new ArrayList();
		params.add(memberId);
		//String hql = "from WebRecommended  t ";
		//hql += " inner join PortfolioArena a on a.id = t.relateId ";
		//hql += " where t.creator.id=? and t.moduleType='portfolio_arena' and t.overhead='1'  ";
		String hql = "from PortfolioArena  t ";
		hql += " where t.creator.id=? and t.isPublic='1' and t.isValid='1' and t.status='1' ";
		if(!"".equals(regionTypeCode))
		{
			String conditionSql = " and ( CONCAT(',' ,t.geoAllocation , ',')  LIKE '%,"+regionTypeCode+",%'  ) ";
			hql+= conditionSql;
		}
		if(!"".equals(sectorTypeCode))
		{
			String conditionSql = " and ( CONCAT(',' ,t.sector , ',')  LIKE '%,"+sectorTypeCode+",%'  ) ";
			hql+= conditionSql;
		}
		hql += " order by t.overheadTime desc";
		List<PortfolioArena> list = this.baseDao.find(hql, params.toArray(), false);
		if(list!=null&&!list.isEmpty()){
			for(PortfolioArena each : list){
				IfaSpacePortfoliosVO vo = new IfaSpacePortfoliosVO();
				String id = each.getId();
				String portfolioName = each.getPortfolioName();
				vo.setPortfoliosName(portfolioName);
				//获取最新一日的收益回报率
				 PortfolioArenaCumperf cumperf = this.getPortfolioArenaCumperf(id);
				 if(cumperf!=null){
					 vo.setReturnRate(cumperf.getCumprefRate());
				 }else{
					 vo.setReturnRate(0);
				 }
				 vo.setId(id);
				 //权限，需要判断
				 if(getMemberCanViewModule(viewMemberId,id,CommonConstantsWeb.WEB_VIEW_MODULE_PORTFOLIO_ARENA))voList.add(vo);
			}
		}
		
		
//		List list = this.baseDao.find(hql, params.toArray(), false);
//		if(!list.isEmpty()){
//			for(int x=0;x<list.size();x++){
//				IfaSpacePortfoliosVO vo = new IfaSpacePortfoliosVO();
//				 Object[] each = (Object[])list.get(x);
//				 //Object wrObj =(Object)each[0];//WebRecommended实体
//				 Object paObj =(Object)each[1];//PortfolioArena实体
//				 if(paObj instanceof PortfolioArena)
//				 {
//					 String id = ((PortfolioArena) paObj).getId();
//					 String portfolioName = ((PortfolioArena) paObj).getPortfolioName();
//					 vo.setPortfoliosName(portfolioName);
//					 //获取最新一日的收益回报率
//					 PortfolioArenaCumperf cumperf = this.getPortfolioArenaCumperf(id);
//					 if(cumperf!=null){
//						 vo.setReturnRate(cumperf.getCumprefRate());
//					 }else{
//						 vo.setReturnRate(0);
//					 }
//					 vo.setId(id);
//				 }
//				 voList.add(vo);
//			}
//		}
		return voList;
		
	}
	
	/**
	 *  获取某一个portfolio_arena的最新一日收益数据
	 * @author 林文伟
	 * @param expertiseType 
	 * @return
	 */
	public PortfolioArenaCumperf getPortfolioArenaCumperf(String portfolioId) {
		String hql1 = "from PortfolioArenaCumperf t where t.portfolio.id='"+portfolioId+"' order by valuationDate desc";
		List<PortfolioArenaCumperf> list = this.baseDao.find(hql1, null, false);
		if(!list.isEmpty())return  list.get(0);
		else return null;
	}
	
	/**
	 * 获取IFA空间推荐的新闻列表，空间只显示2条
	 * @author 林文伟
	 * @return
	 */
	public List<IfaSpaceNewsVO> getSpaceNewsList(String memberId,String regionTypeCode,String sectorTypeCode) {
		List<IfaSpaceNewsVO> volist = new ArrayList<IfaSpaceNewsVO>();
		String hql = "from WebRecommended t";
		hql += " inner join NewsInfo b on b.id = t.relateId ";
		hql +=  "where t.creator.id='"+memberId+"' and t.moduleType='news' and t.overhead='1'  ";
		if(!"".equals(regionTypeCode))
		{
			String conditionSql = " and ( CONCAT(',' ,b.regionType , ',')  LIKE '%,"+regionTypeCode+",%'  ) ";
			hql+= conditionSql;
		}
		if(!"".equals(sectorTypeCode))
		{
			String conditionSql = " and ( CONCAT(',' ,b.sectionType , ',')  LIKE '%,"+sectorTypeCode+",%'  ) ";
			hql+= conditionSql;
		}
		hql += "order by t.overheadTime desc";
		List list1 = this.baseDao.find(hql, null, false);
		if(!list1.isEmpty()){
			for(int x=0;x<list1.size();x++){
				 IfaSpaceNewsVO vo = new IfaSpaceNewsVO();
				 Object[] each2 = (Object[])list1.get(x);
				 Object questObj2 =(Object)each2[0];//WebRecommended实体
				 Object questObj3 =(Object)each2[1];//NEWS实体
				 if(questObj2 instanceof WebRecommended)
				 {
					 String reason = ((WebRecommended) questObj2).getReason();
					vo.setIfaRecommendedReason(reason);
				 }
				 if(questObj3 instanceof NewsInfo)
				 {
					 String newsId = ((NewsInfo) questObj3).getId();
					 String title = ((NewsInfo) questObj3).getTitle();
					// String thumbnailImagePath = ((NewsInfo) questObj3).getIconUrl();
					 //String newsUrl = ((NewsInfo) questObj3).getgetUrl();
					 //Date createTime = ((NewsInfo) questObj3).getCreateTime();
					// vo.setCreateTime(createTime);
					 vo.setNewsId(newsId);
					 //vo.setThumbnailImagePath(thumbnailImagePath);
					 //vo.setUrl(newsUrl);
					 vo.setTitle(title);
					 if(x<2)volist.add(vo);//空间只显示2条
				 }
			}

		}
		return volist;
	}
	
	/**
	 * 获取IFA空间推荐的观点列表
	 * @author 林文伟
	 * @return
	 */
	public List<IfaSpaceInsightVO> getSpaceInsightList(String loginMemberId,String memberId,String regionTypeCode,String sectorTypeCode) {
		List<IfaSpaceInsightVO> volist = new ArrayList<IfaSpaceInsightVO>();
//		String hql = "from WebRecommended t";
//		hql += " inner join InsightInfo b on b.id = t.relateId ";
//		hql += " LEFT join InsightCount c on c.id = b.id ";
//		hql +=  "where t.creator.id='"+memberId+"' and t.moduleType='insight' and t.overhead='1'  ";
		String hql = "from InsightInfo t";
		hql += " LEFT join InsightCount c on c.id = t.id ";
		hql +=  "where t.creator.id='"+memberId+"' and  t.status='1' ";
		if(!"".equals(regionTypeCode))
		{
			String conditionSql = " and ( CONCAT(',' ,t.geoAllocation , ',')  LIKE '%,"+regionTypeCode+",%'  ) ";
			hql+= conditionSql;
		}
		if(!"".equals(sectorTypeCode))
		{
			String conditionSql = " and ( CONCAT(',' ,t.sector , ',')  LIKE '%,"+sectorTypeCode+",%'  ) ";
			hql+= conditionSql;
		}
		hql += "order by t.createTime desc";
		List list1 = this.baseDao.find(hql, null, false);
		if(!list1.isEmpty()){
			for(int x=0;x<list1.size();x++){
				//if(x>3)break;
				IfaSpaceInsightVO vo = new IfaSpaceInsightVO();
				vo.setInsightCount(list1.size());
				 Object[] each2 = (Object[])list1.get(x);
				 //Object questObj2 =(Object)each2[0];//WebRecommended实体
				 Object questObj3 =(Object)each2[0];//InsightInfo实体
				 Object questObj4 =(Object)each2[1];//统计实体
				 if(questObj3 instanceof InsightInfo)
				 {
					 InsightInfo info = (InsightInfo) questObj3;
					 String insightId = info.getId();
					 String title = info.getTitle();
					 Date createTime = info.getCreateTime();
					 vo.setInsightId(insightId);
					 vo.setTitle(title);
					 vo.setCreateTime(createTime);
					 vo.setViews(info.getClick()==null?0:info.getClick());
					 
					 if(questObj4 instanceof InsightCount)
					 {
						 String comments = ((InsightCount) questObj4).getComments().toString();
						 vo.setComments(comments);//
					 }
					 
					 //判断某个查看者member是否有权限查看某个观点
					 if(getMemberCanViewModule(loginMemberId,insightId,CommonConstantsWeb.WEB_VIEW_MODULE_INSIGHT))volist.add(vo);
					 
				 }
				 
			}
		}
		return volist;
	}
	
	/**
	 * 获取IFA空间的最新10条访客
	 * @author 林文伟
	 * @return
	 */
	public List<IfaSpaceVisitVO> getSpaceVisitorList(String ifaMemberId,String lang) {
		String hql = " select MAX(t.vistiTime),t.member.id ";
		hql +=" from WebInvestorVisit t where t.relateId=? and t.moduleType=? ";
		hql += " group by t.relateId,t.member.id ";
		hql += " order by t.vistiTime desc ";
		List params = new ArrayList();
		params.add(ifaMemberId);
		params.add("ifa");//对应模块,ifa空间访客,insight观点访客,news新闻访客,strategy策略,portfolio_arena组合竞技场访客,portfolio_info组合访客
		List list = this.baseDao.find(hql, params.toArray(), false);
		List<IfaSpaceVisitVO> voList = new ArrayList<IfaSpaceVisitVO>();
		if(list.isEmpty())return voList;
		for(int z=0;z<list.size();z++){
			IfaSpaceVisitVO vo = new IfaSpaceVisitVO();
			Object[] objs = (Object[])list.get(z);
			String visitTime = objs[0]==null?"":objs[0].toString();
			//查看时间差
			if(!"".equals(visitTime)){
				Date visitDateTime = DateUtil.StringToDate(visitTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
				vo.setVisitDateTime(visitDateTime);
				vo.setVisitDateTimeStr(DateUtil.getDateTimeGoneFormatStr(visitDateTime,lang,""));
			}
			//头像与名称
			String memberId = objs[1]==null?"":objs[1].toString();
			if(null!=memberId&&!"".equals(memberId)){
				//if(){
					MemberBase user = (MemberBase)this.baseDao.get(MemberBase.class, memberId);
					if(null!=user){
						//vo.setMemberHeadImg(user.getIconUrl());
						vo.setName(user.getNickName());
						vo.setMemberId(memberId);
						String visitorHeadImg = PageHelper.getUserHeadUrl(user.getIconUrl(), "F");
						vo.setMemberHeadImg(visitorHeadImg);
						
						vo.setMemberType(user.getMemberType());
					}
				//}
			}
			voList.add(vo);
		}
		return voList;

	}
	
	/**
	 * 获取IFA空间推荐的基金列表
	 * @author 林文伟
	 * @return
	 */
	public List<IfaSpaceFundVO> getSpaceFundList(String memberId,String langCode,String regionCodeType,String sectorCodeType) {
		List<IfaSpaceFundVO> volist = new ArrayList<IfaSpaceFundVO>();
		String hql = "from WebRecommended t";
		hql += " inner join "+this.getLangString("FundInfo", langCode) + " a on a.id = t.relateId ";
		hql += " left join FundReturn b on b.fund.id = t.relateId  and b.periodCode='return_period_code_1M'";
		hql += " left join "+this.getLangString("FundReturn", langCode) + " out on b.periodCode=out.periodCode ";
		hql +=  "where t.creator.id='"+memberId+"' and t.moduleType='fund'  ";
		if(!"".equals(regionCodeType))
		{
			String conditionSql = " and ( CONCAT(',' ,a.geoAllocationCode , ',')  LIKE '%,"+regionCodeType+",%'  ) ";
			hql+= conditionSql;
		}
		if(!"".equals(sectorCodeType))
		{
			String conditionSql = " and ( CONCAT(',' ,a.sectorTypeCode , ',')  LIKE '%,"+sectorCodeType+",%'  ) ";
			hql+= conditionSql;
		}
		hql += "order by  t.overhead desc , b.increase desc ";
		List list1 = this.baseDao.find(hql, null, false);
		if(!list1.isEmpty()){
			for(int x=0;x<list1.size();x++){
				IfaSpaceFundVO vo = new IfaSpaceFundVO();
				 Object[] each2 = (Object[])list1.get(x);
				 //Object questObj2 =(Object)each2[0];//WebRecommended实体
				 Object questObj3 =(Object)each2[1];//FundInfo实体
				 Object questObj4 =(Object)each2[2];//FundReturn实体
				 Object questObj5 =(Object)each2[3];//FundReturn多语言实体
				 if(questObj3 instanceof FundInfoEn)
				 {
					 String name = ((FundInfoEn) questObj3).getFundName();
					 vo.setFundName(name);
				 }  else if(questObj3 instanceof FundInfoSc){
					 String name = ((FundInfoSc) questObj3).getFundName();
					 vo.setFundName(name);
					 
				 } else if(questObj3 instanceof FundInfoTc){
					 String name = ((FundInfoTc) questObj3).getFundName();
					 vo.setFundName(name);
				 }
				 
				 if(questObj4 instanceof FundReturn)
				 {
					 String fundId =((FundReturn) questObj4).getFund().getId();// ((FundReturn) questObj4).getId();
					 double increase = ((FundReturn) questObj4).getIncrease();
					 vo.setFundId(fundId);
					 vo.setIncreaseRate(increase);
				 }
				 
				 if(questObj5 instanceof FundReturnEn)
				 {
					 String periodName = ((FundReturnEn) questObj5).getPeriodName();
					 vo.setPeriodName(periodName);
				 }  else if(questObj5 instanceof FundReturnSc){
					 String periodName = ((FundReturnSc) questObj5).getPeriodName();
					 vo.setPeriodName(periodName);
					 
				 } else if(questObj5 instanceof FundReturnTc){
					 String periodName = ((FundReturnTc) questObj5).getPeriodName();
					 vo.setPeriodName(periodName);
				 }

				 volist.add(vo);
			}
		}
		return volist;
	}
	
	/**
	 * 获取IFA空间直播左侧列表
	 * @author 林文伟
	 * @return
	 */
	@Override
	public List<IfaSpaceLiveVO> getSpaceLiveLeftList(String memberId,String langCode) {
		List<IfaSpaceLiveVO> volist = new ArrayList<IfaSpaceLiveVO>();
		String hql = "from LiveInfo t";
		hql += " left join LiveScheduler out on out.live.id = t.id ";
		hql +=  "where 1=1 order by t.overheadBy desc ";
		List list1 = this.baseDao.find(hql, null, false);
		if(!list1.isEmpty()){
			for(int x=0;x<list1.size();x++){
				IfaSpaceLiveVO vo = new IfaSpaceLiveVO();
				 Object[] each2 = (Object[])list1.get(x);
				 Object questObj2 =(Object)each2[0];//LiveInfo实体
				 Object questObj3 =(Object)each2[1];//LiveScheduler实体
				 if(questObj2 instanceof LiveInfo)
				 {
					 String id = ((LiveInfo) questObj2).getId();
					 String title = ((LiveInfo) questObj2).getTitle();
					 String content = ((LiveInfo) questObj2).getContent();
					 String issuedTime = ((LiveInfo) questObj2).getIssuedTime().toString();
					 vo.setLideId(id);
					 vo.setTitle(title);
					 vo.setContent(content);
					 vo.setIssuedTime(issuedTime);
				 }
				 if(null!=questObj3){
					 vo.setLiveScheduler((LiveScheduler)questObj3);
				 }
				 
				 volist.add(vo);
			}
		}
		return volist;
	}
	
	/**
	 * 获取IFA空间将要直播左侧列表，已播的不显示
	 * @author 林文伟
	 * @return
	 */
	@Override
	public List<IfaSpaceLiveVO> getSpaceWillLiveLeftList(String memberId,String langCode) {
		List<IfaSpaceLiveVO> volist = new ArrayList<IfaSpaceLiveVO>();
		String hql = "from LiveInfo t";
		hql += " left join LiveScheduler out on out.live.id = t.id ";
		hql +=  "where out.beginTime>=NOW() ORDER BY out.beginTime ASC ";
		List list1 = this.baseDao.find(hql, null, false);
		if(!list1.isEmpty()){
			for(int x=0;x<list1.size();x++){
				IfaSpaceLiveVO vo = new IfaSpaceLiveVO();
				 Object[] each2 = (Object[])list1.get(x);
				 Object questObj2 =(Object)each2[0];//LiveInfo实体
				 Object questObj3 =(Object)each2[1];//LiveScheduler实体
				 if(questObj2 instanceof LiveInfo)
				 {
					 String id = ((LiveInfo) questObj2).getId();
					 String title = ((LiveInfo) questObj2).getTitle();
					 String content = ((LiveInfo) questObj2).getContent();
					 String issuedTime = ((LiveInfo) questObj2).getIssuedTime().toString();
					 vo.setLideId(id);
					 vo.setTitle(title);
					 vo.setContent(content);
					 vo.setIssuedTime(issuedTime);
				 }
				 if(null!=questObj3){
					 vo.setLiveScheduler((LiveScheduler)questObj3);
				 }
				 
				 volist.add(vo);
			}
		}
		return volist;
	}
	
	/**
	 * 检测用户是否查看了ifa的观点
	 * @author mqzou	
	 * @date 2016-09-27
	 * @param ifaId
	 * @param memberId
	 * @return
	 */
	@Override
	public boolean checkInsightRead(String ifaMemberId, String memberId) {
		String sql="SELECT COUNT(0) visitCount FROM `web_investor_visit` v LEFT JOIN `insight_info` i ON v.`relate_id`=i.`id` WHERE v.`member_id`=? AND i.`creator_id`=?";
		List params=new ArrayList();
		params.add(memberId);
		params.add(ifaMemberId);
		List list=this.springJdbcQueryManager.springJdbcQueryForList(sql,params.toArray());
		if(null!=list && !list.isEmpty()){
			HashMap map=(HashMap)list.get(0);
		    Object object=map.get("visitCount");
		    if(null!=object && !"".equals(object.toString())){
		    	int count=Integer.parseInt(object.toString());
		    	if(count>0)
		    		return true;
		    }
		}
		return false;
	}
	@Override
	public int checkIfaInvSameStyle(String ifaMemberId, String memberId) {
		String sql="SELECT invest_style FROM `member_base` WHERE id=? OR id=?";
		List params=new ArrayList();
		params.add(ifaMemberId);
		params.add(memberId);
		List list=this.springJdbcQueryManager.springJdbcQueryForList(sql,params.toArray());
		if(list == null || list.size()<2)
			return 0;
		HashMap map=(HashMap)list.get(0);
		String style1=map.get("invest_style")!=null?map.get("invest_style").toString():"";
		map=(HashMap)list.get(1);
		String style2=map.get("invest_style")!=null?map.get("invest_style").toString():"";
		List<String> styleList1=Arrays.asList(style1.split(","));
		List<String> styleList2=Arrays.asList(style2.split(","));
		Iterator<String> it=styleList1.iterator();
		int count=0;
		while (it.hasNext()) {
			String string = (String) it.next();
			if(!"".equals(string) && styleList2.contains(string))
				count++;
		}
		return count;
	}
	@Override
	public boolean checkInvestorWithIfa(String memberId) {
		String sql="SELECT COUNT(0) ifaCount FROM investor_account r WHERE r.member_id=? AND ifa_id IS NOT NULL AND  ifa_id!='';";
		List params=new ArrayList(0);
		params.add(memberId);
		List list=this.springJdbcQueryManager.springJdbcQueryForList(sql,params.toArray());
		if(null!=list && !list.isEmpty()){
			HashMap map=(HashMap)list.get(0);
			Object obj=map.get("ifaCount");
			if(null!=obj && !"".equals(obj.toString())){
				int count=Integer.parseInt(obj.toString());
				if(count >0)
					return true;
			}
			}
		return false;
	}

	/**
	 * 获取ifa空间访客列表
	 * @author mqzou	
	 * @date 2016-09-27
	 * @param ifaId
	 * @return
	 */
	@Override
	public List findSpaceVisit(String ifaId) {
		
		String hql=" from WebInvestorVisit r where 1=1 and r.moduleType='ifa'";
		List params=new ArrayList();
		hql+=" and r.member.id=?";
		params.add(ifaId);
		hql+=" order by vistiTime desc";
		List reList=this.baseDao.find(hql, params.toArray(), false);
		Iterator it=reList.iterator();
		List list=new ArrayList();
		while (it.hasNext()) {
			WebInvestorVisit object = (WebInvestorVisit) it.next();
			list.add(object.getRelateId());
		}
		return list;
	}

	/**
	 * 获取ifa推荐的新闻列表
	 * @author mqzou	
	 * @date 2016-09-30
	 * @param jsonPaging
	 * @param newsInfo
	 * @param webRecommended
	 * @return
	 */
	@Override
	public JsonPaging findIfaRecommendNews(JsonPaging jsonPaging,NewsInfo newsInfo, WebRecommended webRecommended,String beginDate,String endDate) {
	    StringBuilder sql=new StringBuilder();
	    StringBuilder hql=new StringBuilder();
	    hql.append(" select n.id as newsId,r.id as recommendId,n.title,n.iconUrl,n.createTime,r.recommendTime,r.overhead,r.reason, ");
	    hql.append(" c.views,c.ups,c.downs");
	    hql.append(" from WebRecommended r left join NewsInfo n on r.relateId=n.id left join NewsCount c on n.id=c.id");
	    hql.append(" left join MemberIfa i on r.creator.id=i.member.id where r.moduleType = 'news' ");
	    
	    List params=new ArrayList();
/*	    sql.append(" SELECT n.`id` newsId,r.`id` recommendId,n.`title`,n.`icon_url`, n.`create_time`,r.`recommend_time`,");
	    sql.append(" r.`overhead`,r.`reason`,c.`views`,c.`ups`,c.`downs`,");
	    sql.append(" (SELECT COUNT(0)  FROM`web_investor_visit` v WHERE v.relate_id = n.`id` AND v.module_type = 'news' ");
	    sql.append(" AND EXISTS(SELECT 1  FROM investor_account a  WHERE a.member_id = v.member_id AND a.ifa_id = i.id)) visitCount ");
	    sql.append("  FROM `web_recommended` r  LEFT JOIN `news_info` n  ON r.`relate_id` = n.`id` ");
	    sql.append("  LEFT JOIN `news_count` c  ON n.`id` = c.`info_id`  LEFT JOIN member_ifa i  ON r.creator_id = i.member_id  where r.`module_type` = 'news' ");
*/	    if(null!=webRecommended){
	        hql.append(" and r.creator.id = ?");
	    	params.add(webRecommended.getCreator().getId());
	    }
	    if(null != newsInfo){
	    	if(null!=newsInfo.getRegionType() && !"".equals(newsInfo.getRegionType())){
	    		String[] strings=newsInfo.getRegionType().split(",");
	    		hql.append(" and ( ");
	    		for (String string : strings) {
					if(!"".equals(string)){
						sql.append(" or n.regionType like ? ");
						params.add("%"+string+"%");
					}
				}
	    		hql.append(")");
	    		/*sql.append(" and n.region_type=? ");
	    		params.add(newsInfo.getRegionType());*/
	    	}
	    	if(null!=newsInfo.getSectionType() && !"".equals(newsInfo.getSectionType())){
	    		hql.append(" and n.sectionType=?");
	    		params.add(newsInfo.getSectionType());
	    	}
	    }
	    if(null!=beginDate && !"".equals(beginDate)){
	    	hql.append(" and r.recommendTime between ? and ?");
	    	params.add(DateUtil.getDate(beginDate));
	    	params.add(DateUtil.getDate(endDate));
	    }
	    hql.append(" order by r.overhead desc");
	   // jsonPaging=this.springJdbcQueryManager.springJdbcQueryForPaging(sql.toString(), params.toArray(), jsonPaging);
	    jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
	  /*  long total=this.springJdbcQueryManager.springJdbcQueryForTotal(sql.toString(), params.toArray());
	    jsonPaging.setTotal(Integer.parseInt(toString()));*/
	    List list=new ArrayList();
	    Iterator it=jsonPaging.getList().iterator();
	    while (it.hasNext()) {
	    	int index=0;
	    	Object[] obj=(Object[])it.next();
	    	Object newsId=obj[index++];
	    	Object recommendId=obj[index++];
	    	Object title=obj[index++];
	    	Object iconUrl=obj[index++];
	    	Object createTime=obj[index++];
	    	Object recommendTime=obj[index++];
	    	Object overhead=obj[index++];
	    	Object reason=obj[index++];
	    	Object views=obj[index++];
	    	Object ups=obj[index++];
	    	Object downs=obj[index++];
	    	
	    	IfaRecommendNewsVO vo=new IfaRecommendNewsVO();
	    	int visitCount=getRecommendVisitCount(newsId.toString(), "");
	    	vo.setCreateTime(null!=createTime?createTime.toString():"");
	    	vo.setDowns(null!=downs?downs.toString():"0");
	    	vo.setIconUrl(null!=iconUrl?iconUrl.toString():"");
	    	vo.setId(null!=recommendId?recommendId.toString():"");
	    	vo.setOverhead(null!=overhead?overhead.toString():"0");
	    	vo.setReason(null!=reason?reason.toString():"");
	    	vo.setRecommendTime(null!=recommendTime?recommendTime.toString():"");
	    	vo.setTitle(null!=title?title.toString():"");
	    	vo.setUps(null!=ups?ups.toString():"0");
	    	vo.setViews(null!=views?views.toString():"0");
	    	vo.setVisitCount(String.valueOf(visitCount));
	    	vo.setNewsId(null!=newsId?newsId.toString():"");
			/*HashMap map = (HashMap) it.next();
			IfaRecommendNewsVO vo=new IfaRecommendNewsVO();
			vo.setId(getMapValue(map, "recommendId"));
			vo.setCreateTime(getMapValue(map, "create_time"));
			vo.setDowns("".equals(getMapValue(map, "downs"))?"0":getMapValue(map, "downs"));
			vo.setIconUrl(getMapValue(map, "icon_url"));
			vo.setNewsId(getMapValue(map, "newsId"));
			vo.setOverhead(getMapValue(map, "overhead"));
			vo.setReason(getMapValue(map, "reason"));
			vo.setRecommendTime(getMapValue(map, "recommend_time"));
			vo.setTitle(getMapValue(map, "title"));
			vo.setUps("".equals(getMapValue(map, "ups"))?"0":getMapValue(map, "ups"));
			vo.setViews("".equals(getMapValue(map, "views"))?"0":getMapValue(map, "views"));
			vo.setVisitCount(getMapValue(map, "visitCount"));*/
			list.add(vo);
		}
	    jsonPaging.setList(list);
		return jsonPaging;
	}
	/**
	 * 获取推荐的访问数量
	 * @author mqzou 2016-10-31
	 * @param recommendId
	 * @param moduleType
	 * @return
	 */
	private int getRecommendVisitCount(String recommendId,String moduleType){
		String hql="SELECT COUNT(0)  FROM WebInvestorVisit v WHERE v.relateId = ? ";
		List params=new ArrayList();
		params.add(recommendId);
		if(null!=moduleType && !"".equals(moduleType)){
			hql+=" AND v.moduleType = ?";
			params.add(recommendId);
		}
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			//Object[] objects=(Object[])list.get(0);
			Object obj=list.get(0);
			if(null!=obj && !"".equals(obj.toString())){
				return Integer.parseInt(obj.toString());
			}
		}
		return 0;
		
	}
	
		
	/**
	 * 获取新闻的访客列表
	 * @author mqzou	
	 * @date 2016-09-30
	 * @param memberId
	 * @return
	 */
	@Override
	public List findNewsVisitorList(String memberId, String newsId) {
		String hql=" from WebInvestorVisit  i left join WebRecommended r on i.relateId=r.relateId  where i.relateId=? and r.creator.id=? ";
		List params=new ArrayList();
		params.add(newsId);
		params.add(memberId);
		List resultList=this.baseDao.find(hql, params.toArray(), false);
		List list=new ArrayList();
		Iterator it=resultList.iterator();
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();
			WebInvestorVisit visit=(WebInvestorVisit)object[0];
			InsightVistorVo vo=new InsightVistorVo();
			vo.setLoginCode(visit.getMember().getLoginCode());
			vo.setIconUrl(visit.getMember().getIconUrl());
			vo.setVisitTime(DateUtil.getDateStr(visit.getVistiTime()));
			list.add(vo);
		}
		return list;
	}
	
	/**
	 * 获取新闻关联的推送权限设置
	 * @author mqzou 
	 * @date 2016-10-08
	 * @param newsId
	 * @return
	 */
	@Override
	public StrategyWebPushVO findWebPushByNews(String newsId,String moduleType) {
		List<String> params = new ArrayList<String>();
		String hql = " from WebPush t where t.relateId =? and t.moduleType='"+moduleType+"' ";
		params.add(newsId);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (!list.isEmpty()){
			WebPush push = (WebPush)list.get(0);
			List<WebPushDetail> details = findWebPushDetailList(push.getId(), null);
			StrategyWebPushVO vo = new StrategyWebPushVO();
			BeanUtils.copyProperties(push, vo);//拷贝信息
			vo.setDetails(details);
			
			if (null==vo.getBuddyFlag()) vo.setBuddyFlag("0");//无
			vo.setBuddies("1".equals(vo.getBuddyFlag())?"ALL":"");
			
			if (null==vo.getClientFlag()) vo.setClientFlag("0");//无
			vo.setClients("1".equals(vo.getClientFlag())?"ALL":"");
			
			if (null==vo.getProspectFlag()) vo.setProspectFlag("0");//无
			vo.setProspects("1".equals(vo.getProspectFlag())?"ALL":"");
			
			if (null==vo.getColleagueFlag()) vo.setColleagueFlag("0");//无
			vo.setColleagues("1".equals(vo.getColleagueFlag())?"ALL":"");
			
			if (!details.isEmpty()){
				for (int i=0;i<details.size();i++){
					WebPushDetail x = details.get(i);
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_CLIENT.equals(x.getType())){
						vo.setClients(vo.getClients()+","+x.getToMember().getId());
					}
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_BUDDY.equals(x.getType())){
						vo.setBuddies(vo.getBuddies()+","+x.getToMember().getId());
					}
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_PROSPECT.equals(x.getType())){
						vo.setProspects(vo.getProspects()+","+x.getToMember().getId());
					}
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_TEAM.equals(x.getType())){
						vo.setColleagues(vo.getColleagues()+","+x.getToMember().getId());
					}
				}
			}
			return vo;
		}
		return null;
	}
	
	/**
	 * 获取推送权限明细
	 * @author mqzou
	 * @param pushId
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebPushDetail> findWebPushDetailList(String pushId, String type) {
		List<String> params = new ArrayList<String>();
		String hql = " from WebPushDetail t where t.push.id=?  ";
		params.add(pushId);
		
		if (null!=type && !"".equals(type)){
			hql += " and t.type=? ";
			params.add(type);
		}
		return (List<WebPushDetail>)this.baseDao.find(hql, params.toArray(), false);
	}
	
	/**
	 * 获取新闻推荐的实体信息
	 * @author mqzou 
	 * @date 2016-10-08
	 * @param id
	 * @return
	 */
	@Override
	public WebRecommended getIfaRecommendNews(String id) {
		WebRecommended vo=(WebRecommended)this.baseDao.get(WebRecommended.class, id);
		return vo;
	}
	
	/**
     * 修改新闻推荐的实体信息
	 * @author mqzou 
	 * @date 2016-10-08
     * @param webRecommended
     * @return
     */
	@Override
	public WebRecommended updateWebRecommended(WebRecommended webRecommended) {
		WebRecommended vo=(WebRecommended)this.baseDao.saveOrUpdate(webRecommended);
		return vo;
	}

	/**
	 * 获取推荐的访客信息
	 * @author mqzou 
	 * @date 2016-10-19
	 * @param memberId
	 * @param relateId
	 * @param moduleType
	 * @return
	 */
	@Override
	public RecommendInfoVO getRecommendInfo(String memberId, String relateId, String moduleType) {
		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT a.id, a.`recommend_time`,a.overhead, COUNT(r.`relate_id`=a.`relate_id`) allCount,");
		sql.append(" COUNT((SELECT 1 FROM `web_friend` f WHERE f.`from_member_id`=? AND f.`to_member_id`=r.`member_id` )) visitCount");
		sql.append("  FROM `web_recommended` a LEFT JOIN  `web_investor_visit`  r  ON a.`relate_id`=r.`relate_id` WHERE a.`module_type`=? AND a.relate_id=?  AND a.`creator_id`=? ");
		List params=new ArrayList();
		params.add(memberId);
		params.add(moduleType);
		params.add(relateId);
		params.add(memberId);
		List list=this.springJdbcQueryManager.springJdbcQueryForList(sql.toString(),params.toArray());
		RecommendInfoVO vo=new RecommendInfoVO();
		if(null!=list && !list.isEmpty()){
			HashMap map=(HashMap)list.get(0);
			Object id=map.get("id");
			Object date=map.get("recommend_time");
			Object overhead=map.get("overhead");
			Object allCount=map.get("allCount");
			Object visiCount=map.get("visitCount");
			if(null!=date && !"".equals(date)){
				vo.setId(id.toString());
				vo.setRecommendDate(DateUtil.getDateStr(date.toString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
				vo.setViews(null!=allCount && !"".equals(allCount.toString())?allCount.toString():"0");
				vo.setVisitCount(null!=visiCount && !"".equals(visiCount.toString())?visiCount.toString():"0");
				vo.setOverhead(null!=overhead?overhead.toString():"0");
			}else {
				vo.setViews("0");
				vo.setVisitCount("0");
			}
			
		}
		return vo;
	}
	
	/**
	 * 推荐置顶操作
	 * @author mqzou 
	 * @date 2016-10-19
	 * @param memberId
	 * @param id
	 * @param overhead
	 * @return
	 */
	@Override
	public boolean checkRecommendOverhead(String memberId, String id, String overhead,String moduleType) {
		String hql="update WebRecommended r set overhead='0' where r.creator.id=? and r.moduleType=?";
		List params=new ArrayList();
		params.add(memberId);
		params.add(moduleType);
		int rs=this.baseDao.updateHql(hql, params.toArray());
		hql="update WebRecommended r set overhead=? where id=?";
		params=new ArrayList();
		params.add(overhead);
		params.add(id);
		rs=this.baseDao.updateHql(hql, params.toArray());
		if(rs>0)
			return true;
		return false;
	}

	/**
	 * 删除推荐
	 * @author mqzou 
	 * @date 2016-11-03
	 * @param webRecommended
	 * @return
	 */
	@Override
	public void delRecommended(WebRecommended webRecommended) {
		this.baseDao.delete(webRecommended);
	}

	/**
	 * 更新ifa人气,同一用户每刷新一次页面，其人气值则+1，实际同一用户24小时内只计算一次
	 * @author wwluo
	 * @date 2016-11-03
	 * @param memberId
	 */
	@Override
	public Boolean updateIfaPopularityTotal(String memberId,String visitMemberId) {
		MemberIfa ifa = new MemberIfa();
		if (StringUtils.isNotBlank(memberId)) {
			String sql = "from WebInvestorVisit t where t.relateId='"+memberId+"' and  t.member.id='"+visitMemberId+"' and HOUR(TIMEDIFF(NOW(),t.vistiTime)) <24 and t.moduleType='ifa'";
			List<WebInvestorVisit> vList = this.baseDao.find(sql, null, false);
			if(vList!=null&&vList.isEmpty()||vList==null){
				StringBuffer hql = new StringBuffer(" from MemberIfa where member_id=?");
				List params = new ArrayList();
				params.add(memberId);
				List<MemberIfa> list = this.baseDao.find(hql.toString(), params.toArray(), false);
				if(list != null && !list.isEmpty()){
					ifa = list.get(0);
					Integer popularityTotal = ifa.getPopularityTotal();
					if(null==popularityTotal)popularityTotal=1;
					else popularityTotal++;
					ifa.setPopularityTotal(popularityTotal);
					ifa = (MemberIfa) this.baseDao.saveOrUpdate(ifa);
					return true;
				}
			}		
		}
		return false;
	}
	
	/**
	 * IFA个人空间推荐区域，根据搜索条件【区域与投资领域板块】获取策略、组合、基金、观点、新闻
	 * @author 林文伟
	 * @param langCode 语言
	 * @return
	 */
	@Override
	public IfaSpaceVO loadIfaSpaceFilterRecommendData(String loginMemberId,String memberId,String langCode,String regionCodeType,String sectorCodeType) {
		
		IfaSpaceVO vo = new IfaSpaceVO();

		if(""!=memberId){
		 //获取推荐模块之策略
		 vo.setRecommendedStrategies(this.getRecommendedStrategies(memberId,regionCodeType,sectorCodeType,loginMemberId));
		 //获取推荐组合
		 vo.setRecommendedPortfolios(this.getRecommendedPortfolios(memberId,regionCodeType,sectorCodeType,loginMemberId));
		 //获取推荐的基金
		 vo.setFundList(this.getSpaceFundList(memberId,langCode,regionCodeType,sectorCodeType));
		//观点列表推荐
		 vo.setInsightList(this.getSpaceInsightList(loginMemberId,memberId,regionCodeType,sectorCodeType));
		//推荐新闻列表
		 vo.setNewsList(this.getSpaceNewsList(memberId,regionCodeType,sectorCodeType));
		}
		
		return vo;
	}
	
	/**
	 * IFA空间更新hightlight
	 * @author 林文伟 
	 * @date 2016-10-19
	 * @param ifaId
	 * @param hightLight
	 * @return
	 */
	@Override
	public boolean updateHightLight(String ifaId, String hightLight) {
		MemberIfa ifa = new MemberIfa();
		ifa.setId(ifaId);
		
		if(!this.checkIfaExtInfoIsExist(ifaId)){ //如果不存在，则插入一条记录
			IfaExtInfo newVo = new IfaExtInfo();
			newVo.setIfa(ifa);
			newVo.setLastUpdate(new Date());
			this.baseDao.saveOrUpdate(newVo, true);
		}
		String hql="update IfaExtInfo r set highlight=? where r.ifa.id=?  ";
		List params=new ArrayList();
		params.add(hightLight);
		params.add(ifaId);
		int rs=this.baseDao.updateHql(hql, params.toArray());
		if(rs>0){
			IfaHighlight vo = new IfaHighlight();
			vo.setCreateTime(new Date());
			vo.setIfa(ifa);
			vo.setTitle(hightLight);
			vo.setIsDef("0");
			this.baseDao.saveOrUpdate(vo, true);
			
			return true;
		}
			
		return false;
	}
	
	public boolean checkIfaExtInfoIsExist(String ifaId) {
		String hql="from IfaExtInfo r where r.ifa.id=? ";
		List params=new ArrayList();
		params.add(ifaId);
		List<IfaExtInfo> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list&&!list.isEmpty()){
			return true;
		} else return false;
	}
	
	/**
	 * 获取ifa个人所管理的所有资产的总值 总资产=总市值+总现金
		portfolio_hold里面只有总市值
		portfolio_hold_account这张表有总现金
	 * @author 林文伟 
	 * @date 2016-10-19
	 * @param ifaId
	 * @param hightLight
	 * @return
	 */
	public double getIfaAUM(String ifaId,String toCurrency) {
		//double aum = 0;
		double totalMarketValue = 0;
		String hql="from PortfolioHold r where r.ifa.id=? ";
		List params=new ArrayList();
		params.add(ifaId);
		List<PortfolioHold> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list&&!list.isEmpty()){
			//if(list.size()>0){
				for(PortfolioHold each : list){
					if(null!=each.getTotalMarketValue()){
						String eachfromCurrency = each.getBaseCurrency();
						double marketValue = each.getTotalMarketValue();
						double rate = 1;
						if(marketValue>0){
							WebExchangeRate exchangeRate = fundInfoService.findExchangeRate(eachfromCurrency, toCurrency);
							if (null != exchangeRate) {
								rate = exchangeRate.getRate();
								marketValue = marketValue*rate;
							}
						}
						totalMarketValue += marketValue;
					}
						
				}
			//}
		}
		//总现金
		double totalCashAvailable = 0;
		hql="from PortfolioHoldAccount r where r.ifa.id=? ";
		List params1=new ArrayList();
		params1.add(ifaId);
		List<PortfolioHoldAccount> list1 = this.baseDao.find(hql.toString(), params1.toArray(), false);
		if(null!=list1&&!list1.isEmpty()){
			//if(list1.size()>0){
				for(PortfolioHoldAccount each : list1){
					if(null!=each.getCashAvailable()){
						String eachfromCurrency = each.getBaseCurrency();
						double cashAvailable = each.getCashAvailable();
						double rate = 1;
						if(cashAvailable>0){
							WebExchangeRate exchangeRate = fundInfoService.findExchangeRate(eachfromCurrency, toCurrency);
							if (null != exchangeRate) {
								rate = exchangeRate.getRate();
								cashAvailable = cashAvailable*rate;
							}
						}
						totalCashAvailable += cashAvailable;
					}
						
				}
			//}
		}
		
		//执行转换
//		double rate = 1;
//		if(aum>0){
//			if(fromCurrency.equals(toCurrency))return aum;
//			WebExchangeRate exchangeRate = fundInfoService.findExchangeRate(fromCurrency, toCurrency);
//			if (null != exchangeRate) {
//				rate = exchangeRate.getRate();
//				aum = aum*rate;
//			}
//		}
		
		return totalMarketValue+totalCashAvailable;
	}
	
	/**
	 * 获取ifa个人total_return_value 
	 * @author 林文伟 
	 * @date 2016-10-19
	 * @param ifaId
	 * @param hightLight
	 * @return
	 */
	public double getIfaTotalReturnValue(String ifaId) {
		String hql="from PortfolioHold r where r.ifa.id=? order by r.totalReturnRate desc";
		List params=new ArrayList();
		params.add(ifaId);
		List<PortfolioHold> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		//double totalReturnValue = 0; //
		double totalReturnRate = 0;
		//double result = 0;
		if(null!=list&&!list.isEmpty()){
			totalReturnRate = list.get(0).getTotalReturnRate();
//			for(PortfolioHold each : list){
//				if(null!=each.getTotalReturnValue()){
//					totalReturnValue += each.getTotalReturnValue();
//				}
//			}
		}
//		if(totalReturnValue>0){
//			if(null!=list){
//				for(PortfolioHold each : list){
//					if(null!= each.getTotalReturnRate()&&null!=each.getTotalReturnValue()){
//						double returnRate = each.getTotalReturnRate();
//						double returnValue = each.getTotalReturnValue();
//						double eachJQ = (returnValue/totalReturnValue)*returnRate;
//						result += eachJQ;
//					}
//					
//				}
//			}
//		}
		
		
		return totalReturnRate;//result
	}
	
	/**
	 * 通过不同的货币转换
	 * @author 林文伟 
	 * @date 2016-10-19
	 * @return
	 */
	public String exchangeCurrency(double aum,String fromCurrency,String toCurrency) {
		//执行转换
		String newNumber = "";
		double rate = 1;
		if(aum>0){
			//if(fromCurrency.equals(toCurrency))return aum;
			WebExchangeRate exchangeRate = fundInfoService.findExchangeRate(fromCurrency, toCurrency);
			if (null != exchangeRate) {
				rate = exchangeRate.getRate();
				aum = aum*rate;
				newNumber = PageHelper.getNumberString(aum);
			}
		}
		return newNumber;
	}
	
	/**
	 * GET IfaFeeItem
	 * @author 林文伟
	 * @param ifaId 
	 * @return
	 */
	public  List<IfaFeeItem> getIfaFeeItem(String ifaId) {
		//int count = 0;
		String hql1 = "from IfaFeeItem t where t.ifa.id='"+ifaId+"' ORDER BY feeNumber ASC";
		List<IfaFeeItem> list1 = this.baseDao.find(hql1, null, false);
		return list1;
	}
	
	/**
	 * 判断member是否可访问的某个观点
	 * @author 林文伟
	 * @param ifaId 
	 * @return
	 */
	public  Boolean getMemberCanViewModule(String memberId,String relateId,String moduleType) {
		Boolean isCanView = false;
		//获取某个观点的web_view信息
		String hql = "from WebView t where t.moduleType= ? and t.relateId = ? ";
		List params=new ArrayList();
		params.add(moduleType);//
		params.add(relateId);
		List<WebView> webViewList = this.baseDao.find(hql, params.toArray(), false);
		if(webViewList!=null&&!webViewList.isEmpty()){
			WebView wv = webViewList.get(0);
			String wvId = wv.getId();
			String createId = wv.getFromMember().getId();//可以认为观点的创建者就是这个web_view的创建者
			String scopeFlag = wv.getScopeFlag();
			if(createId.equals(memberId)){//如果是自己访问自己
				isCanView = true;
				return isCanView;
			}
			if("2".equals(scopeFlag)){ //如果是public类型
				isCanView = true;
				return isCanView;
			}
			//通过wvid获取web_view_detail列表信息
			hql = "from WebViewDetail t where t.view.id= ? ";
			List params1=new ArrayList();
			params1.add(wvId);
			List<WebViewDetail> webViewDetailList = this.baseDao.find(hql, params1.toArray(), false);
			if(webViewDetailList!=null&&!webViewDetailList.isEmpty()){//如果有数据，则判断正在看看的member是否存在在该WebViewDetail表中
				for(WebViewDetail each : webViewDetailList){
					String wvdMemeberId = each.getToMember().getId();
					if(wvdMemeberId.equals(memberId)){
						isCanView = true;
						break;
					}
				}
			}
		}

		return isCanView;
	}
	
	/**
	 * 设置是否显示AUM给投资者看
	 * @author wwlin 
	 * @date 2016-10-19
	 * @return
	 */
	@Override
	public boolean updateIfaIsShowAum(String memberId, String isShowAum) {
		String hql="update MemberIfa r set spaceShowAum=? where r.member.id=? ";
		List params=new ArrayList();
		params.add(isShowAum);
		params.add(memberId);
		int rs=this.baseDao.updateHql(hql, params.toArray());
		if(rs>0)
			return true;
		return false;
	}
	
	/**
	 * 设置是否在IFA空间显示个人业绩
	 * @author wwlin 
	 * @date 2016-10-19
	 * @return
	 */
	@Override
	public boolean updateIfaIsShowFerformance(String memberId, String isShowFerformance) {
		String hql="update MemberIfa r set spaceShowPerformance=? where r.member.id=? ";
		List params=new ArrayList();
		params.add(isShowFerformance);
		params.add(memberId);
		int rs=this.baseDao.updateHql(hql, params.toArray());
		if(rs>0)
			return true;
		return false;
	}
}
