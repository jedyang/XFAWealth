package com.fsll.wmes.ifa.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.CornerInfo;
import com.fsll.wmes.entity.CornerLiked;
import com.fsll.wmes.entity.CornerReply;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmMemo;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.IfaExtInfo;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaCumperf;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldReturn;
import com.fsll.wmes.entity.StrategyAllocation;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.StrategyProduct;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebFriend;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebReadToDoEn;
import com.fsll.wmes.entity.WebReadToDoSc;
import com.fsll.wmes.entity.WebReadToDoTc;
import com.fsll.wmes.entity.WebRecommended;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.entity.WebWatchlistType;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifa.vo.IfaClientVO;
import com.fsll.wmes.ifa.vo.IfaCommunicationRecordVO;
import com.fsll.wmes.ifa.vo.IfaCorner;
import com.fsll.wmes.ifa.vo.IfaCornerInfoDetailVO;
import com.fsll.wmes.ifa.vo.IfaCornerInfoLikedVO;
import com.fsll.wmes.ifa.vo.IfaCornerInfoReplyVO;
import com.fsll.wmes.ifa.vo.IfaCountryVO;
import com.fsll.wmes.ifa.vo.IfaHomePortfolioVO;
import com.fsll.wmes.ifa.vo.IfaInfoVO;
import com.fsll.wmes.ifa.vo.IfaListResultVO;
import com.fsll.wmes.ifa.vo.IfaListfiltersVO;
import com.fsll.wmes.ifa.vo.IfaSearchItemVO;
import com.fsll.wmes.ifa.vo.IfafirmVO;
import com.fsll.wmes.ifa.vo.MyFavoritesPortfolios;
import com.fsll.wmes.ifa.vo.MyFavoritesStrategy;
import com.fsll.wmes.ifa.vo.MyFavoritesWatchingListVO;
import com.fsll.wmes.ifa.vo.MyFavoritesWatchingTypeVOList;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.strategy.vo.CharDataVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationTypeVO;
import com.fsll.wmes.web.service.WebFriendService;

/**
 * 基金信息查询服务接口
 * @author zpzhou
 * @date 2016-7-25
 */
@Service("ifaInfoService")
//@Transactional
public class IfaInfoServiceImpl extends BaseService implements IfaInfoService {
	 @Autowired
	 private SysParamService sysParamService;
	 @Autowired
	 private WebFriendService webFriendService;
	 @Autowired
	 private FundInfoService fundInfoService;
	 @Autowired
	 private PortfolioHoldService portfolioHoldService;
	 @Autowired
	 private StrategyInfoService strategyInfoService;
	 @Autowired
	 private MemberBaseService memberBaseService;
	 @Autowired
	 private IfafirmManageService ifafirmManageService;
	/**
	 * 得到distributor信息
	 * @return
	 */
	public List<IfaSearchItemVO> getDistributorMess(){
		List<IfaSearchItemVO> list = new ArrayList();
		String hql = "select c.id,c.companyName from MemberDistributor c ";
		hql+=" order by c.companyName asc ";
		List dataList = this.baseDao.find(hql,null, false);
		if(!dataList.isEmpty()){
			for(int x=0;x<dataList.size();x++){
				IfaSearchItemVO vo=new IfaSearchItemVO();
				Object[] objs = (Object[])dataList.get(x);
				vo.setCode(null==objs[0]?"":objs[0].toString());
				vo.setName(null==objs[1]?"":objs[1].toString());
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * @param jsonPaging 分页参数
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @param serviceRegion 服务区域
	 * @param expertiseType 擅长领域 
	 * @param country 居住地
	 * @param distributor 分销商
	 * @return
	 */
	public JsonPaging findIfaList(JsonPaging jsonPaging,String langCode,String userId,String serviceRegion,String expertiseType, String country,String distributor){
		List<Object> params = new ArrayList<Object>();
		String sql = "select m.id,m.member_id as memberId, m.nick_name as nickName,i.company_name as companyName," +
				"m.invest_style as investStyle,m.introduction,c.name_"+langCode+" as country ,w.check_result as checkResult,m.gender " +
				" from member_ifa m ";
		sql += " left join member_ifafirm  i on i.id=m.company_ifafirm_id ";
		sql += " left join sys_country c on c.id=m.country ";
		sql += " left join web_friend w on w.to_member_id=m.member_id and w.from_member_id=? ";
		sql +=" where 1=1 ";
		params.add(userId);
		//服务领域
		if(null!=serviceRegion && !"".equals(serviceRegion)){
			String [] serviceRegionArray=serviceRegion.split(",");
			sql += " and ( ";
			for (int i = 0; i < serviceRegionArray.length; i++) {
				if(0==i){
					sql += " m.service_region like ? ";	
				}else{
					sql += " or m.service_region like ? ";
				}
				params.add("%"+serviceRegionArray[i]+"%");
			}
			sql += " ) ";
		}
		//擅长领域
		if(null!=expertiseType && !"".equals(expertiseType)){
			String [] expertiseTypeArray=expertiseType.split(",");
			sql += " and ( ";
			for (int i = 0; i < expertiseTypeArray.length; i++) {
				if(0==i){
					sql += " m.expertise_type like ? ";	
				}else{
					sql += " or m.expertise_type like ? ";
				}
				params.add("%"+expertiseTypeArray[i]+"%");
			}
			sql += " ) ";
		}
		//居住地
		if(null!=country && !"".equals(country)){
			String [] countryArray=country.split(",");
			sql += " and ( ";
			for (int i = 0; i < countryArray.length; i++) {
				if(0==i){
					sql += " m.country=? ";	
				}else{
					sql += " or m.country=? ";
				}
				params.add(countryArray[i]);
			}
			sql += " ) ";
		}
		//分销商
		if(null!=distributor && !"".equals(distributor)){
			String [] distributorArray=distributor.split(",");
			String sqlTemp="";
			for (int i = 0; i < distributorArray.length; i++) {
				sqlTemp+="?,";
				params.add(distributorArray[i]);
			}
			sqlTemp=sqlTemp.substring(0,sqlTemp.length()-1);
			sql += " and m.company_ifafirm_id in (select d.ifafirm_id from ifafirm_distributor d where d.is_valid=1 and d.distributor_id in ("+sqlTemp+")) ";	
		}
		jsonPaging = springJdbcQueryManager.springJdbcQueryForPaging(sql, params.toArray(), jsonPaging);
		//long total=springJdbcQueryManager.springJdbcQueryForTotal(sql, params.toArray());
		//jsonPaging.setTotal((int)total);
		return jsonPaging;
	}
	

	
	/**
	 * 查找国家列表
	 * @author 林文伟
	 * @param langCode 语言
	 * @return
	 */
	//@Transactional(readOnly = true)
	public List<IfaCountryVO> findCountryList(String langCode){
		List<IfaCountryVO> list = new ArrayList<IfaCountryVO>();
		List<String> params = new ArrayList<String>();

		String hql = "select t.id,t.pinYin,"+this.getLangString("t.name", langCode)+" as name from SysCountry t ";
		hql += " order by t.orderBy ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				IfaCountryVO vo = new IfaCountryVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setId(StrUtils.getString(objs[0]));
				vo.setPinyin(StrUtils.getString(objs[1]));
				vo.setName(StrUtils.getString(objs[2]));
				list.add(vo);
			}
		}
		
		return list;
	}
	
	/**
	 * 获取IFA公司列表
	 * @author 林文伟
	 * @param langCode 语言
	 * @return
	 */
	public List<IfafirmVO> loadIFAFirmList(String langCode) {
		
		String hql = "select out.id,out.companyName,out.pinYin,t.firmLogo from MemberIfafirm t ";
		hql += " inner join "+this.getLangString("MemberIfafirm", langCode) + " out on t.id=out.id  ";
		//hql += " left join AccessoryFile f on f.id = t.firmLogo ";
		hql += " order by t.incorporationDate desc";
		List list = this.baseDao.find(hql, null, false);
		//List<MemberIfafirmEn> voList = new ArrayList();
		 List<IfafirmVO> voList = new ArrayList();
		if(!list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				//MemberIfafirmEn vo = new MemberIfafirmEn();
				IfafirmVO vo = new IfafirmVO();
				String id = objs[0]==null?"":objs[0].toString();
				String companyName = objs[1]==null?"":objs[1].toString();
				//String pinYin = objs[2]==null?"":objs[2].toString();
				String logoFilePath = objs[3]==null?"":objs[3].toString();
				logoFilePath = PageHelper.getLogoUrl(logoFilePath, "F");
				 vo.setLogoFilePath(logoFilePath);
				vo.setId(id);
				vo.setCompanyName(companyName);
				vo.setLogoFilePath(logoFilePath);
				voList.add(vo);
			}
		}
		return voList;
	}
	
	/**
	 * 获取IFA列表
	 * @author 林文伟
	  * @param jsonPaging 分页参数
	 * @param filters 过滤条件
	 * @return
	 */
	public JsonPaging loadIFAList(JsonPaging jsonPaging,IfaListfiltersVO filter,MemberIndividual investor) {
		
 		String hql = "  from MemberIfa a ";
 		hql += " inner join MemberIfafirm c on c.id = a.ifafirm.id ";
		hql += " inner join "+this.getLangString("MemberIfafirm", filter.getLang())+" b on b.id = c.id ";
		hql += " inner join MemberBase d on d.id = a.member.id ";
		//hql += " left join AccessoryFile f on f.id = c.firmLogo";
		hql += " where 1=1 ";
		//IFA 公司
		if(!"".equals(filter.getIfaFirmIds()))
		{
			String conditionSql = " and a.ifafirm.id='"+filter.getIfaFirmIds()+"'   ";
			hql+= conditionSql;
		}
		//投资风格，字段同已移到base表中
		if(!"".equals(filter.getInvestmentType()))
		{
			String conditionSql = " and ( CONCAT(',' ,d.investStyle , ',')  LIKE '%,"+filter.getInvestmentType()+",%'  ) ";
			hql+= conditionSql;
		}
		//服务区域，字段同已移到base表中
		if(!"".equals(filter.getServiceRegion()))
		{
			String conditionSql = " and ( CONCAT(',' ,d.liveRegion , ',')  LIKE '%,"+filter.getServiceRegion()+",%'  ) ";
			hql+= conditionSql;
		}
		//服务语言，字段同已移到base表中
		if(!"".equals(filter.getServiceLanguage()))
		{
			String conditionSql = " and ( CONCAT(',' ,d.languageSpoken , ',')  LIKE '%,"+filter.getServiceLanguage()+",%'  ) ";
			hql+= conditionSql;
		}
		//擅长领域，字段同已移到base表中
		if(!"".equals(filter.getExpertise()))
		{
			String conditionSql = " and ( CONCAT(',' ,d.investField , ',')  LIKE '%,"+filter.getExpertise()+",%'  ) ";
			hql+= conditionSql;
		}
		//工作年限
		if(!"".equals(filter.getWorkingYearsFrom())&&!"".equals(filter.getWorkingYearsTo()))
		{
			String conditionSql = " and ROUND(DATEDIFF(CURDATE(),a.investLifeBegin)/365)>="+filter.getWorkingYearsFrom()+" and ROUND(DATEDIFF(CURDATE(),a.investLifeBegin)/365)<="+filter.getWorkingYearsTo()+" ";
			hql+= conditionSql;
		}
		//所属国籍
		if(!"".equals(filter.getBelongCountry()))
		{
			String conditionSql = " and a.nationality.id ='"+filter.getBelongCountry()+"' ";
			hql+= conditionSql;
		}
		//关键字
		if(!"".equals(filter.getKeyword()))
		{
			String conditionSql = " and ( a.firstName like '%"+filter.getKeyword()+"%' or a.lastName like '%"+filter.getKeyword()+"%' or a.nameChn like '%"+filter.getKeyword()+"%' or d.nickName like '%"+filter.getKeyword()+"%' )";
			hql+= conditionSql;
		}
		//sort
		if(!"".equals(filter.getSort()))
		{
			String conditionSql = " order by  " + filter.getSort();
			hql+= conditionSql;
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), null, jsonPaging , true);
		List voList = jsonPaging.getList();////每个列表都是一个2维数组，每个数组有4个实体
		List list = new ArrayList();
		if(voList!=null && !voList.isEmpty()){
			String lang = filter.getLang();
			for(int x=0;x<voList.size();x++){
				list = getIfaListResults(lang, voList, list, x,investor);
			}
		}

		jsonPaging.setList(list);
		return jsonPaging;
	}

	//获取ifa展示信息
	public List getIfaListResults(String lang, List voList,List list, int x,MemberIndividual investor) {
		IfaListResultVO vo = new IfaListResultVO();
		 Object[] each2 = (Object[])voList.get(x);
		 Object questObj1 =(Object)each2[1];//MemberIfafirm
		 Object questObj2 =(Object)each2[0];//MemberIfa
		 Object questObj3 =(Object)each2[2];//MemberIfafirm为公司多语言实体
		 
		 if(questObj1 instanceof MemberIfafirm)
		 {
			 MemberIfafirm firm = (MemberIfafirm)questObj1;
			 String ifaFirmLogoPath = firm.getFirmLogo();
			 ifaFirmLogoPath = PageHelper.getLogoUrl(ifaFirmLogoPath, "F");
			 vo.setIfaFirmLogoPath(ifaFirmLogoPath);
		 }

		 if(questObj2 instanceof MemberIfa)
		 {
			 MemberIfa objMemberIfa = (MemberIfa)questObj2;
			 MemberBase member = objMemberIfa.getMember();
			 //生成头像
			 String iconUrl = member.getIconUrl();
			 String gender = objMemberIfa.getGender();
			 String ifaHeadImg = PageHelper.getUserHeadUrl(iconUrl, gender);
			 vo.setIfaHeadImg(ifaHeadImg);
			 vo.setMemberId(member.getId());
			 String ifaId = objMemberIfa.getId();
			 Date ifaWorkingYears =  null;
			 if(null!=objMemberIfa.getInvestLifeBegin())ifaWorkingYears=objMemberIfa.getInvestLifeBegin();
			 int ifaPopularity = 0;
			 if(null!=objMemberIfa.getPopularityTotal()) ifaPopularity = objMemberIfa.getPopularityTotal();
			 //生成投资风格,擅长领域，服务区域
			 String investStyle = "";
			 if(null!=member.getInvestStyle())investStyle = getBaseConfigData(member.getInvestStyle(),lang);
			 String expertiseType = "";
			 if(null!=member.getInvestField())expertiseType = getBaseConfigData(member.getInvestField(),lang);
			 String serviceRegion = "";
			 if(null!=member.getLiveRegion())serviceRegion = getBaseConfigData(member.getLiveRegion(),lang);
			 String ifaPersonalCharacteristics = investStyle + "," +expertiseType+","+serviceRegion;
			 vo.setIfaName(member.getNickName());
			 vo.setIfaId(ifaId);
			 vo.setIfaPersonalCharacteristics(ifaPersonalCharacteristics);
			 vo.setIfaPopularity(ifaPopularity);
			 //一年投资回报率 获取选择一个要在IFA空间显示的，由该IFA负责的投资组合，这里存放的是投资组合id，对应portfolio_hold
			 if(null!=objMemberIfa.getSpaceShowPerformance()){
				 if(""!=objMemberIfa.getSpaceShowPerformance()){
					 PortfolioHold portfolioHold = this.findPortfolioHoldById(objMemberIfa.getSpaceShowPerformance());
					 if(null!=portfolioHold)
						 vo.setSpaceShowPortfolio(portfolioHold.getTotalReturnRate().toString());
					 else
						 vo.setSpaceShowPortfolio("0");
				 } else{
					 vo.setSpaceShowPortfolio("0");
				 }
			 }
			 else{
				 vo.setSpaceShowPortfolio("0");
			 }
			 if(null!=ifaWorkingYears)
			 {
				 Calendar rightNow = Calendar.getInstance();
				 int nowYear = rightNow.get(Calendar.YEAR);
				 
				 int year = ifaWorkingYears.getYear()+1900;
				 int length = nowYear-year;
				 vo.setIfaWorkingYears(Integer.toString(length));
			 }
			 SysCountry nationality = objMemberIfa.getNationality();
			 if(null!=nationality)
			 {
				 String countryName = nationality.getNameEn();
				 if("en".equals(lang))
					 countryName = nationality.getNameEn();
				 if("tc".equals(lang))
					 countryName = nationality.getNameTc();
				 if("sc".equals(lang))
					 countryName = nationality.getNameSc();
				vo.setIfaBelongCountry(countryName);
			 }
			 //做匹配操作
			 if(null!=investor){
				 //获取投资人的几个属性
				 MemberBase memberBase = investor.getMember();
				 String investorLiveRegion = memberBase.getLiveRegion();//活动区域
				 String investorLanguageSpoken = memberBase.getLanguageSpoken();//常用语言
				 String investorInvestField = memberBase.getInvestField();//喜欢的投资领域,来源于基础数据,保存的是基础数据的code值,多个之间用,分隔
				 String investorInvestStyle = memberBase.getInvestStyle();//投资风格
				 String investorHobbyType = memberBase.getHobbyType();//爱好
				 //IFA的几个匹配属性
				 String ifaServiceRegion = member.getLiveRegion();//服务区域
				 String ifaLanguageDesc = member.getLanguageSpoken();//提供服务语言 ifa懂得语言,来源于基础数据,保存的是基础数据的code值,多个之间用,分隔
				 String ifaInvestField = member.getInvestField();//objMemberIfa.getExpertiseType();//擅长的投资领域（
				 String ifaInvestStyle = member.getInvestStyle();//投资风格
				 String ifaHobbyType = objMemberIfa.getMember().getHobbyType();//爱好
				 //进行对比匹配
				 Integer hitNumber = 0;
				 //进行所在地区匹配分析
				 if(null!=investorLiveRegion&&""!=investorLiveRegion){
					 String[] investorLiveRegionArray = investorLiveRegion.split(",");
					 if(investorLiveRegionArray.length>0){
						 for(String each : investorLiveRegionArray){ 
							 if(""!=each&&null!=ifaServiceRegion&&ifaServiceRegion.contains(each)){
								 //if(null!=ifaServiceRegion&&ifaServiceRegion.contains(each)){
									 hitNumber+=1;
									 break;
								 //}
							 }
						 }
					 }
				 }
				 //进行语言匹配分析
				 if(null!=investorLanguageSpoken&&""!=investorLanguageSpoken){
					 String[] investorLanguageSpokenArray = investorLanguageSpoken.split(",");
					 if(investorLanguageSpokenArray.length>0){
						 for(String each : investorLanguageSpokenArray){
							 if(""!=each&&null!=ifaLanguageDesc&&ifaLanguageDesc.contains(each)){
								 //if(null!=ifaLanguageDesc&&ifaLanguageDesc.contains(each)){
									 hitNumber+=1;
									 break;
								 //}
							 }
						 }
					 }
				 }
				 //进行投资领域匹配分析
				 if(null!=investorInvestField&&""!=investorInvestField){
					 String[] investorInvestFieldArray = investorInvestField.split(",");
					 if(investorInvestFieldArray.length>0){
						 for(String each : investorInvestFieldArray){ //对每个领域，判断是否存在于该IFA的投资领导列中
							 if(""!=each&&null!=ifaInvestField&&ifaInvestField.contains(each)){
								 //if(null!=ifaInvestField&&ifaInvestField.contains(each)){//如果包含，则匹配度加1，跳出循环
									 hitNumber+=1;
									 break;
								// }
							 }
						 }
					 }
				 }
				//进行投资风格匹配分析
				 if(null!=investorInvestStyle&&""!=investorInvestStyle){
					 String[] investorInvestStyleArray = investorInvestStyle.split(",");
					 if(investorInvestStyleArray.length>0){
						 for(String each : investorInvestStyleArray){ //对每个领域，判断是否存在于该IFA的投资风格列中
							 if(""!=each&&null!=ifaInvestStyle&& ifaInvestStyle.contains(each)){
								 //if(null!=ifaInvestStyle&& ifaInvestStyle.contains(each)){//如果包含，则匹配度加1，跳出循环
									 hitNumber+=1;
									 break;
								 //}
							 }
						 }
					 }
				 }
				//进行爱好匹配分析
				 if(null!=investorHobbyType&&""!=investorHobbyType){
					 String[] investorHobbyTypeArray = investorHobbyType.split(",");
					 if(investorHobbyTypeArray.length>0){
						 for(String each : investorHobbyTypeArray){ //对每个领域，判断是否存在于该IFA的爱好列中
							 if(""!=each&&null!=ifaHobbyType&&ifaHobbyType.contains(each)){
								 //if(null!=ifaHobbyType&&ifaHobbyType.contains(each)){
									 hitNumber+=1;
									 break;
								 //}
							 }
						 }
					 }
				 }
				 //设置是否匹配布尔值
				 if(hitNumber>0){
					 vo.setIsMatch(true);
				 }
				 else{
					 vo.setIsMatch(false);
				 }
				 //设置最后匹配度
				 vo.setMatchingDegree(hitNumber.toString());
			 }
			 else{
				 vo.setMatchingDegree("0");
				 vo.setIsMatch(false);
			 }
			 
		}
		 if(questObj3 instanceof MemberIfafirmEn)
			{
				 String companyName = (((MemberIfafirmEn)questObj3).getCompanyName());
				 vo.setIfaFirm(companyName);
			}
		 if(questObj3 instanceof MemberIfafirmSc)
			{
				 String companyName = (((MemberIfafirmSc)questObj3).getCompanyName());
				 vo.setIfaFirm(companyName);
			}
		 if(questObj3 instanceof MemberIfafirmTc)
			{
				 String companyName = (((MemberIfafirmTc)questObj3).getCompanyName());
				 vo.setIfaFirm(companyName);
			}

		list.add(vo);
		return list;
	}
	
	/**
	 * 获取投资者与IFA之间的匹配度
	 * @author linwenwei
	 */
	public int getInvestorIfaMatchDegree(MemberBase investor,MemberBase ifa) {
		//获取投资人的几个属性
		 MemberBase memberBase = investor;
		 String investorLiveRegion = memberBase.getLiveRegion();//活动区域
		 String investorLanguageSpoken = memberBase.getLanguageSpoken();//常用语言
		 String investorInvestField = memberBase.getInvestField();//喜欢的投资领域,来源于基础数据,保存的是基础数据的code值,多个之间用,分隔
		 String investorInvestStyle = memberBase.getInvestStyle();//投资风格
		 String investorHobbyType = memberBase.getHobbyType();//爱好
		 //IFA的几个匹配属性
		 String ifaServiceRegion = ifa.getLiveRegion();//服务区域
		 String ifaLanguageDesc = ifa.getLanguageSpoken();//提供服务语言 ifa懂得语言,来源于基础数据,保存的是基础数据的code值,多个之间用,分隔
		 String ifaInvestField = ifa.getInvestField();//objMemberIfa.getExpertiseType();//擅长的投资领域（
		 String ifaInvestStyle = ifa.getInvestStyle();//投资风格
		 String ifaHobbyType = ifa.getHobbyType();//爱好
		 //进行对比匹配
		 Integer hitNumber = 0;
		 //进行所在地区匹配分析
		 if(null!=investorLiveRegion&&""!=investorLiveRegion){
			 String[] investorLiveRegionArray = investorLiveRegion.split(",");
			 if(investorLiveRegionArray.length>0){
				 for(String each : investorLiveRegionArray){ 
					 if(""!=each&&null!=ifaServiceRegion&&ifaServiceRegion.contains(each)){
						 //if(null!=ifaServiceRegion&&ifaServiceRegion.contains(each)){
							 hitNumber+=1;
							 break;
						 //}
					 }
				 }
			 }
		 }
		 //进行语言匹配分析
		 if(null!=investorLanguageSpoken&&""!=investorLanguageSpoken){
			 String[] investorLanguageSpokenArray = investorLanguageSpoken.split(",");
			 if(investorLanguageSpokenArray.length>0){
				 for(String each : investorLanguageSpokenArray){
					 if(""!=each&&null!=ifaLanguageDesc&&ifaLanguageDesc.contains(each)){
						 //if(null!=ifaLanguageDesc&&ifaLanguageDesc.contains(each)){
							 hitNumber+=1;
							 break;
						 //}
					 }
				 }
			 }
		 }
		 //进行投资领域匹配分析
		 if(null!=investorInvestField&&""!=investorInvestField){
			 String[] investorInvestFieldArray = investorInvestField.split(",");
			 if(investorInvestFieldArray.length>0){
				 for(String each : investorInvestFieldArray){ //对每个领域，判断是否存在于该IFA的投资领导列中
					 if(""!=each&&null!=ifaInvestField&&ifaInvestField.contains(each)){
						 //if(null!=ifaInvestField&&ifaInvestField.contains(each)){//如果包含，则匹配度加1，跳出循环
							 hitNumber+=1;
							 break;
						// }
					 }
				 }
			 }
		 }
		//进行投资风格匹配分析
		 if(null!=investorInvestStyle&&""!=investorInvestStyle){
			 String[] investorInvestStyleArray = investorInvestStyle.split(",");
			 if(investorInvestStyleArray.length>0){
				 for(String each : investorInvestStyleArray){ //对每个领域，判断是否存在于该IFA的投资风格列中
					 if(""!=each&&null!=ifaInvestStyle&& ifaInvestStyle.contains(each)){
						 //if(null!=ifaInvestStyle&& ifaInvestStyle.contains(each)){//如果包含，则匹配度加1，跳出循环
							 hitNumber+=1;
							 break;
						 //}
					 }
				 }
			 }
		 }
		//进行爱好匹配分析
		 if(null!=investorHobbyType&&""!=investorHobbyType){
			 String[] investorHobbyTypeArray = investorHobbyType.split(",");
			 if(investorHobbyTypeArray.length>0){
				 for(String each : investorHobbyTypeArray){ //对每个领域，判断是否存在于该IFA的爱好列中
					 if(""!=each&&null!=ifaHobbyType&&ifaHobbyType.contains(each)){
						 //if(null!=ifaHobbyType&&ifaHobbyType.contains(each)){
							 hitNumber+=1;
							 break;
						 //}
					 }
				 }
			 }
		 }
		 
		 return hitNumber;
	}
	
	/**
	 * 通过ID获取我的客户的投资组合持仓实体信息
	 * @author linwenwei
	 */
	public PortfolioHold findPortfolioHoldById(String id) {
		PortfolioHold portfolioHold=(PortfolioHold)this.baseDao.get(PortfolioHold.class, id);
		return portfolioHold;
	}
	
	/**
	 * 获取某用户擅长领域基础数据
	 * @author 林文伟
	 * @param expertiseType 服务区域,来源于基础数据,保存的是基础数据的code值,多个之间用,分隔
	 * @return
	 */
	public String getBaseConfigData(String typeCode,String langCode) {
		String nameList = "";
		if(!"".equals(typeCode))
		{
			String[] typeList = typeCode.split(",");
			for(String each :typeList)
			{
				if(!"".equals(each))
				{
					SysParamConfig config = sysParamService.findByCode(each);
					if(null!=config&&!"".equals(config.getId())){
						if("tc".equals(langCode))
							nameList += config.getNameTc()+",";
						else if("sc".equals(langCode))
							nameList += config.getNameSc()+",";
						else if("en".equals(langCode))
							nameList += config.getNameEn()+",";
					}
				}
			}
		}
		
		return nameList;
	}
	
	public List<WebRecommended> getIfaRecommendFundList( MemberBase memberBase ){
		String hql = "from WebRecommended t where t.moduleType = 'fund' and t.creator.id = ? order by t.recommendTime desc ";
		List<String> params = new ArrayList<String>();
		params.add( memberBase.getId() );
		List<WebRecommended> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 通过memberId获取投资者信息
	 * @author 林文伟
	 * @param memberId
	 * @return
	 */
	public MemberIndividual getMemberIndividual( String memberId ){
		String hql = "from MemberIndividual t where t.member.id = ? ";
		List<String> params = new ArrayList<String>();
		params.add(memberId);
		List<MemberIndividual> list = this.baseDao.find(hql, params.toArray(), false);
		if(list!=null&& !list.isEmpty()){
			return list.get(0);
		} else return null;
	}
	
	/**
	 * 获取ifa推荐的基金的Id集合
	 * @author mqzou
	 * @date 2016-10-18
	 * @param memberBase
	 * @param begin
	 * @param end
	 * @param sectorCode
	 * @param geoCode
	 * @return
	 */
	@Override
	public List<WebRecommended> getIfaRecommendFundIds(MemberBase memberBase, String begin, String end, String sectorCode, String geoCode,String langCode) {
		StringBuilder hql=new StringBuilder();
		List params=new ArrayList();
		hql.append("from WebRecommended t left join "+getLangString("FundInfo", langCode)+" f on t.relateId=f.id  where t.moduleType = 'fund' and t.creator.id = ?");
		params.add(memberBase.getId());
		if(null!=begin && !"".equals(begin) && null!=end && !"".equals(end)){
			hql.append(" and t.recommendTime between ? and ?");
			params.add(DateUtil.getDate(begin));
			params.add(DateUtil.getDate(end));
		}
		if(null!=sectorCode && !"".equals(sectorCode)){
			hql.append(" and f.sectorTypeCode=?");
			params.add(sectorCode);
		}
		if(null!=geoCode && !"".equals(geoCode)){
			hql.append(" and f.geoAllocationCode=?");
			params.add(geoCode);
		}
		hql.append(" order by t.recommendTime desc ");
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		List<WebRecommended> list=new ArrayList<WebRecommended>();
		if(null!=resultList && !resultList.isEmpty()){
		 Iterator it=resultList.iterator();
		 while (it.hasNext()) {
			Object[] object = (Object[]) it.next();
			WebRecommended vo=(WebRecommended)object[0];
			list.add(vo);
		}
		}
		return list;
	}

    
	public WebRecommended getIfaRecommendFund( MemberBase memberBase, String id ){
		String hql = "from WebRecommended t where t.moduleType = 'fund' and t.creator.id = ? and t.relateId = ? ";
		List<String> params = new ArrayList<String>();
		params.add( memberBase.getId() );
		params.add( id );
		List<WebRecommended> list = this.baseDao.find(hql, params.toArray(), false);
		if( !list.isEmpty() ){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public WebRecommended updateWebRecommended( WebRecommended webRecommended ){
		return (WebRecommended) baseDao.saveOrUpdate(webRecommended);
	}

	@Override
	public void deleteWebRecommend(WebRecommended webRecommended) {
		this.baseDao.delete(webRecommended);
		
	}
	
	@Override
	public WebRecommended findRecommendedById(String id) {
		WebRecommended vo=(WebRecommended)baseDao.get(WebRecommended.class, id);
		return vo;
	}
	
	/*****************************************我收藏的策略、组合、watchlist相关功能*********************************************/
	/**
	 * 获取我收藏的策略
	 * @author 林文伟
	 * @param memberId 会员ID
	 * @return
	 */
	@Override
	public List<MyFavoritesStrategy> loadIFAMyFavoritesStrategyList(String memberId,String lang,String defFormatDate, Integer maxResults,String langCode) {
		List<MyFavoritesStrategy> myFavoritesStrategyList= new ArrayList<MyFavoritesStrategy>();
		List params=new ArrayList();
		params.add(memberId);
		String hql = " from WebFollow t ";
		hql += " inner join StrategyInfo out";
		hql += "  on t.relateId=out.id  where t.moduleType='strategy' and t.member.id=? and t.isValid=1  order by t.createTime desc";
		List list1 = this.baseDao.find(hql, params.toArray(), false, maxResults);
		if(!list1.isEmpty()){
			for(int x=0;x<list1.size();x++){
				MyFavoritesStrategy model = new MyFavoritesStrategy();
				 Object[] each2 = (Object[])list1.get(x);
				 Object questObj2 =(Object)each2[0];//WebFollow实体
				 Object questObj3 =(Object)each2[1];//StrategyInfo实体
				 if(questObj2 instanceof WebFollow)
				 {
					 WebFollow vo = (WebFollow)questObj2;
					 String id = vo.getId();
					 model.setFavoritesId(id);
					 //转换时间格式
					 Date favoritesTime = vo.getCreateTime();
					 model.setFavoritesTime(favoritesTime);
					 model.setFavoritesTimeStr(DateUtil.getDateTimeGoneFormatStr(favoritesTime, lang, defFormatDate));
				 }
				 if(questObj3 instanceof StrategyInfo)
				 {
					 StrategyInfo vo = (StrategyInfo)questObj3;
					 String strategyId = vo.getId();
					 String title = vo.getStrategyName();
					 Date createTime = vo.getCreateTime();
					 String createMemberId = vo.getCreator().getId();
					 String createUserName = vo.getCreator().getNickName();
					 if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==vo.getCreator().getSubMemberType()){
						 createUserName=getCommonMemberName(vo.getCreator().getId(),langCode, "2");
					 }
					 
					 String createrHeadUrl = vo.getCreator().getIconUrl();
					 String ifaHeadImg = PageHelper.getUserHeadUrl(createrHeadUrl, "");
					 model.setCreaterHeadUrl(ifaHeadImg);
					 model.setStrategyCreateMemberId(createMemberId);
					 //获取所包含的基金产品数
					 int productCount = this.getStrategiesProductCount(strategyId);
					 model.setCreateTime(createTime);
					 model.setFundsCount(productCount);
					 model.setStrategyCreateName(createUserName);
					 model.setStrategyId(strategyId);
					 model.setStrategyName(title);
					 
					 String region = vo.getGeoAllocation();
					 if (StringUtils.isNotBlank(region)) {
						 model.setGeoAllocationName(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, region, lang));
					 }
					 String sector = vo.getSector();
					 if (StringUtils.isNotBlank(sector)) {
						 model.setSectorName(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, sector, lang));
					 }
					 /*List<StrategyAllocation> strategyAllocations = getStrategyAllocation(vo.getId());
					 if(strategyAllocations != null && !strategyAllocations.isEmpty()){
						List<Map<String, Object>> allocationObj = new ArrayList<Map<String, Object>>();
						Integer itemWeightFund = 0;
						Integer itemWeightStock = 0;
						Integer itemWeightBond = 0;
						for (StrategyAllocation strategyAllocation : strategyAllocations) {
							Integer weight = strategyAllocation.getItemWeight();
							String type = strategyAllocation.getType();
							if(CommonConstantsWeb.WEB_ALLOCATION_TYPE_FUND.equals(type) && weight != null){
								itemWeightFund += weight;
							}else if(CommonConstantsWeb.WEB_ALLOCATION_TYPE_BOND.equals(type) && weight != null){
								itemWeightBond += weight;
							}else if(CommonConstantsWeb.WEB_ALLOCATION_TYPE_STOCK.equals(type) && weight != null){
								itemWeightStock += weight;
							}
						}
						Map<String, Object> fundMap = new HashMap<String, Object>();
						fundMap.put("name", CommonConstantsWeb.WEB_ALLOCATION_TYPE_FUND);
						fundMap.put("value", itemWeightFund);
						if(itemWeightFund > 0){
							allocationObj.add(fundMap);
						}
						Map<String, Object> bondMap = new HashMap<String, Object>();
						bondMap.put("name", CommonConstantsWeb.WEB_ALLOCATION_TYPE_BOND);
						bondMap.put("value", itemWeightBond);
						if(itemWeightBond > 0){
							allocationObj.add(bondMap);
						}
						Map<String, Object> stockMap = new HashMap<String, Object>();
						stockMap.put("name", CommonConstantsWeb.WEB_ALLOCATION_TYPE_STOCK);
						stockMap.put("value", itemWeightStock);
						if(itemWeightStock > 0){
							allocationObj.add(stockMap);
						}
						
					}*/
					 List<CharDataVO> allocation=new ArrayList<CharDataVO>();
						List<StrategyAllocationTypeVO> typeList=strategyInfoService.findStrategyAllocationType(vo.getId());
						if(null!=typeList){
							Iterator iterator=typeList.iterator();
							while (iterator.hasNext()) {
								StrategyAllocationTypeVO object = (StrategyAllocationTypeVO) iterator.next();
								CharDataVO charDataVO=new CharDataVO();
								charDataVO.setName(object.getTypeName());
								charDataVO.setValue(Double.valueOf(object.getWeight()) );
								charDataVO.setDisplayColor(object.getDisplayColor());
								allocation.add(charDataVO);
							}
						}
						
						String allocationJson=JsonUtil.toJson(allocation);
						
						allocationJson.replace("\"", "'");
						
					model.setStrategyAllocationsObj(allocationJson);
					model.setRiskLevel(vo.getRiskLevel());
				 }
				 
				
				 myFavoritesStrategyList.add(model);
			}
		}
		return myFavoritesStrategyList;
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
		List<StrategyProduct> list = this.baseDao.find(hql1, null, false);
		if(null!=list)count = list.size();
		return count;
	}
	
	/**
	 * 获取我收藏的组合数据
	 * @author 林文伟
	 * @param memberId 会员ID
	 * @return
	 */
	@Override
	public List<MyFavoritesPortfolios> loadIFAMyFavoritesPortfoliosList(String memberId, Integer maxResults,String langCode) {
		List<MyFavoritesPortfolios> myFavoritesPortfoliosList= new ArrayList<MyFavoritesPortfolios>();
		List params=new ArrayList();
		params.add(memberId);
		String hql = " from WebFollow t ";
		hql += " inner join PortfolioArena out";
		hql += "  on t.relateId=out.id  where t.moduleType='portfolio_arena' and t.member.id=? and t.isValid=1 order by t.createTime desc";
		List list1 = this.baseDao.find(hql, params.toArray(), false, maxResults);
		if(!list1.isEmpty()){
			for(int x=0;x<list1.size();x++){
				MyFavoritesPortfolios model = new MyFavoritesPortfolios();
				 Object[] each2 = (Object[])list1.get(x);
				 Object questObj2 =(Object)each2[0];//WebFollow实体
				 Object questObj3 =(Object)each2[1];//PortfolioArena实体
				 if(questObj2 instanceof WebFollow)
				 {
					 WebFollow vo = (WebFollow)questObj2;
					 String id = vo.getId();
					 Date favoritesTime = vo.getCreateTime();
					 model.setFavoritesId(id);
					 model.setFavoritesTime(favoritesTime);
				 }
				 if(questObj3 instanceof PortfolioArena)
				 {
					 PortfolioArena vo = (PortfolioArena)questObj3;
					 String portfolioInfoId = vo.getId();
					 String title = vo.getPortfolioName();
					 Date createTime = vo.getCreateTime();
					 String createUserName = vo.getCreator().getNickName();
					 if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==vo.getCreator().getSubMemberType()){
						 createUserName=getCommonMemberName(vo.getCreator().getId(),langCode, "2");
					 }
					 String createMemberId = vo.getCreator().getId();
					 model.setCreateMemberId(createMemberId);
					 String createrHeadUrl = vo.getCreator().getIconUrl();
					 String ifaHeadImg = PageHelper.getUserHeadUrl(createrHeadUrl, "");
					 model.setCreaterHeadUrl(ifaHeadImg);
					 model.setTotalReturn(vo.getTotalReturn());
					 model.setRiskLevel(null!=vo.getRiskLevel()?vo.getRiskLevel():0);
					 
//					 PortfolioArenaCumperf cumperf = this.getPortfolioArenaCumperf(vo.getId());
//					 if(cumperf!=null){
//						 //vo.setReturnRate(cumperf.getCumprefRate());
//						 model.setTotalReturn(cumperf.getCumprefRate().toString();
//					 }else{
//						 model.setTotalReturn("");
//					 }	
					 
					 model.setCreateTime(createTime);
					 //model.setTotalReturn("10.22%");
					 model.setPortfoliosCreateName(createUserName);
					 model.setPortfoliosId(portfolioInfoId);
					 model.setPortfoliosName(title);
				 }
				
				 myFavoritesPortfoliosList.add(model);
			}
		}
		return myFavoritesPortfoliosList;
	}
	
	/**
	 * 获取我自选的基金列表
	 * @author 林文伟
	 * @param memberId 会员ID
	 * @return
	 */
	@Override
	public List<MyFavoritesWatchingTypeVOList> loadIFAMyFavoritesWatchingList(String langCode,String memberId,String toCurrency,String watchingTypeId ) {
		List<MyFavoritesWatchingTypeVOList> watchingTypeVOList = new ArrayList<MyFavoritesWatchingTypeVOList>();
/*		String hql = " from WebWatchlist t ";
		hql += " left join WebWatchlistType l";
		hql += "  on t.webWatchlistType.id=l.id  where t.member.id='"+memberId+"' and l.isValid='1'";
		if(StringUtils.isNotBlank(watchingTypeId)){
			hql += " and t.webWatchlistType = '"+watchingTypeId+"'" ;
		}
		hql += " order by t.orderBy desc";*/
		
		StringBuilder hql=new StringBuilder();
		hql.append(" from WebWatchlistType l where l.member.id='"+memberId+"' and l.isValid='1'");
		if(StringUtils.isNotBlank(watchingTypeId)){
			hql.append(" and l.id='"+watchingTypeId+"'");
		}
		hql.append(" order by l.orderBy desc ");
		List list1 = this.baseDao.find(hql.toString(), null, false);
		if(!list1.isEmpty()){
			for(int x=0;x<list1.size();x++){
				MyFavoritesWatchingTypeVOList typeList = new MyFavoritesWatchingTypeVOList();
				 //WebWatchlist model = new WebWatchlist();
				WebWatchlistType typeVo=(WebWatchlistType)list1.get(x);
				if(null!=typeVo&&""!=typeVo.getId()){
					 String typeId = typeVo.getId();
					 String typeName = typeVo.getName();
					 
					 Boolean isExist = false;
					 for(MyFavoritesWatchingTypeVOList each :watchingTypeVOList)
					 {
						 if(typeId.equals(each.getTypeId())){
							 isExist = true;
							 break;
						 }
						 
					 }
					 if(!isExist)
					 {
						 typeList.setTypeName(typeName);
						 typeList.setTypeId(typeId);
						 watchingTypeVOList.add(typeList);
					 }
				 }
				 /*Object[] each2 = (Object[])list1.get(x);
				 Object questObj2 =(Object)each2[0];//WebWatchlist实体
				 //Object questObj3 =(Object)each2[1];//WebWatchlistType实体
				 if(questObj2 instanceof WebWatchlist)
				 {
					 WebWatchlist vo = (WebWatchlist)questObj2;
					 
					 WebWatchlistType typeVo = vo.getWebWatchlistType();
					 if(null!=typeVo&&""!=typeVo.getId()){
						 String typeId = typeVo.getId();
						 String typeName = typeVo.getName();
						 
						 Boolean isExist = false;
						 for(MyFavoritesWatchingTypeVOList each :watchingTypeVOList)
						 {
							 if(typeId.equals(each.getTypeId())){
								 isExist = true;
								 break;
							 }
							 
						 }
						 if(!isExist)
						 {
							 typeList.setTypeName(typeName);
							 typeList.setTypeId(typeId);
							 watchingTypeVOList.add(typeList);
						 }
					 }
					 
				 }*/	 
			}
		}
		//循环类型
		for(MyFavoritesWatchingTypeVOList each :watchingTypeVOList)
		{
			List<MyFavoritesWatchingListVO> wlvolist =  new ArrayList<MyFavoritesWatchingListVO>();
			String typeId = each.getTypeId();
			if(null==typeId||"10001"==typeId)typeId="";
			String hql1 = " from WebWatchlist t ";
			hql1 += " left join WebWatchlistType l";
			if(""==typeId)
				hql1 += "  on t.webWatchlistType.id=l.id  where t.member.id='"+memberId+"' and l.isValid='1' and l.id is NULL order by t.createTime desc";
			else
				hql1 += "  on t.webWatchlistType.id=l.id  where t.member.id='"+memberId+"' and l.isValid='1' and l.id='"+typeId+"' order by t.createTime desc";
			
			
			List list2 = this.baseDao.find(hql1, null, false);
			if(!list2.isEmpty()){
				for(int x=0;x<list2.size();x++){
					Object[] each2 = (Object[])list2.get(x);
					 Object questObj2 =(Object)each2[0];//WebWatchlist实体
					 if(questObj2 instanceof WebWatchlist)
					 {
						 WebWatchlist vo = (WebWatchlist)questObj2;
						 if (null!=vo && null!=vo.getProduct()){
							 String productId = vo.getProduct().getId();
							 String remark = vo.getRemark();
							 MyFavoritesWatchingListVO wlvo = new MyFavoritesWatchingListVO();
							 wlvo.setRemark(remark);
							 wlvo.setWatchingId(vo.getId());
							 //通过productId获取其对应的fundinfo的ID
							 String fundId = getFundIdByProductId(productId);
							 if(null==vo.getWebWatchlistType()&&""!=vo.getId()){
								 //List<FundInfoDataVO> fundInfoList = 
								 FundInfoDataVO fundInfoVO = fundInfoService.findFundFullInfoById(fundId, langCode, memberId, toCurrency);
								 double fundReturnYearOne = fundInfoVO.getFundReturnYear1();
								 fundReturnYearOne = fundReturnYearOne*100;
								 fundReturnYearOne = new BigDecimal(fundReturnYearOne).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								 fundInfoVO.setFundReturnYear1(fundReturnYearOne);
								 
								 double fundReturnYTD = fundInfoVO.getFundReturnYTD();
								 fundReturnYTD = fundReturnYTD*100;
								 fundReturnYTD = new BigDecimal(fundReturnYTD).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								 fundInfoVO.setFundReturnYTD(fundReturnYTD);
								 
								 wlvo.setFundInfo(fundInfoVO);
	//				        	if(null!=fundInfoVO&&""!=fundInfoVO.getFundId()){System.out.println("vo2");
	//				        		FundInfoDataVO fundInfo = each.getMyFavoritesWatchingListVO().getFundInfoList();
	//				        		if(null==fundInfoList){
	//				        			fundInfoList = new ArrayList<FundInfoDataVO>();
	//				        		}
	//				        		fundInfoList.add(fundInfoVO);
	//				        		each.setFundInfoList(fundInfoList);
	//				        	}
							 } else if(null!=vo.getWebWatchlistType()&&""!=vo.getId()){
								 FundInfoDataVO fundInfoVO = fundInfoService.findFundFullInfoById(fundId, langCode, memberId, toCurrency);
								 double fundReturnYearOne = fundInfoVO.getFundReturnYear1();
								 fundReturnYearOne = fundReturnYearOne*100;
								 fundReturnYearOne = new BigDecimal(fundReturnYearOne).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								 fundInfoVO.setFundReturnYear1(fundReturnYearOne);
								 
								 double fundReturnYTD = fundInfoVO.getFundReturnYTD();
								 fundReturnYTD = fundReturnYTD*100;
								 fundReturnYTD = new BigDecimal(fundReturnYTD).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								 fundInfoVO.setFundReturnYTD(fundReturnYTD);
								 
								 wlvo.setFundInfo(fundInfoVO);
							 }
							 
							 wlvolist.add(wlvo);
						 }
					 }
				}
				each.setMyFavoritesWatchingListVO(wlvolist);
			}
			//watchingTypeVOList.add(each);
		}
		return watchingTypeVOList;
	}
	
	/**
	 *  通过产品ID获取其对应基金基表的ID
	 * @author 林文伟
	 * @param expertiseType 
	 * @return
	 */
	public String getFundIdByProductId(String productId) {
		String hql1 = "from FundInfo t where t.product.id like '%"+productId+"%' ";
		List<FundInfo> list = this.baseDao.find(hql1, null, false);
		if(null!=list && !list.isEmpty())
			return  list.get(0).getId();
		else return "";
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
	
	/***
     * 通过类型删除所有自选基金
     * @author 林文伟
     * @date 2016-09-18
     */
	@Override
	public Boolean delMyWatchlist(String memberid,String typeId)
	{
		String hql1 = " delete from WebWatchlist r where  r.member.id = ?  and r.webWatchlistType.id =?  ";
		List<Object> params = new ArrayList<Object>();
		params.add(memberid);
		params.add(typeId);
		int rs = this.baseDao.updateHql(hql1, params.toArray());
		
		return rs>0?true:false;
	}
	
	/***
     * 更新自选基金的备注信息
     * @author 林文伟
     * @date 2016-09-18
     */
	@Override
	public Boolean saveMyWatchlistRemark(String id,String remark)
	{
		String hql1 = " update  WebWatchlist r set r.remark=? where  r.id = ?   ";
		List<Object> params = new ArrayList<Object>();
		params.add(remark);
		params.add(id);
		
		int rs = this.baseDao.updateHql(hql1, params.toArray());
		
		return rs>0?true:false;
	}
	
	/***
     * 更新自选基金类型的名称
     * @author 林文伟
     * @date 2016-09-18
     */
	@Override
	public Boolean modifyMyWatchTypeName(String id,String name)
	{
		String hql1 = " update  WebWatchlistType r set r.name=? where r.id = ?   ";
		List<Object> params = new ArrayList<Object>();
		params.add(name);
		params.add(id);
		
		int rs = this.baseDao.updateHql(hql1, params.toArray());
		
		return rs>0?true:false;
	}
	
	/***
     * 删除我关注的web_follow信息
     * @author 林文伟
     * @date 2016-12-18
     */
	@Override
	public Boolean deleteMyWebFollow(String followId)
	{
		String hql1 = " delete from WebFollow r where  r.id = ?  ";
		List<Object> params = new ArrayList<Object>();
		params.add(followId);
		int rs = this.baseDao.updateHql(hql1, params.toArray());
		
		return rs>0?true:false;
	}
	
	/*****************************************发现功能********************************************/
	/**
	 * 通过会员ID,待阅类型获取所有待阅信息
	 * @author 林文伟
	 * @param portfolioId
	 * @return
	 */
	@Override
	public List<WebReadToDo> findWebReadTodo(String memberId,String typeCode,String status,String langCode) {
		
		List<WebReadToDo> voList = new ArrayList<WebReadToDo>();
		List<String> params = new ArrayList<String>();
		String hql = " from WebReadToDo t  ";
		hql += " left join "+this.getLangString("WebReadToDo", langCode) + " i on i.id = t.id ";
		hql += " where t.owner.id = ? and t.type = ? ";
		if(StringUtils.isNotBlank(status)){
			hql += " and t.isRead = '"+status+"'" ;
		}
		hql += " order by t.createTime desc";
		params.add(memberId);
		params.add(typeCode);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			for(int x=0;x<list.size();x++){
				Object[] each2 = (Object[])list.get(x);
				Object questObj0 =(Object)each2[0];//WebReadToDo实体
				Object questObj1 =(Object)each2[1];//WebReadToDoSc,En,Tc实体
				 WebReadToDo vo = new WebReadToDo();
				 if(questObj0 instanceof WebReadToDo)
				 {
					 WebReadToDo temp = (WebReadToDo)questObj0;
					 vo.setMsgUrl(temp.getMsgUrl());
					 vo.setType(temp.getType());
					 vo.setCreateTime(temp.getCreateTime());
					 vo.setMsgParam(temp.getMsgParam());
					 vo.setModuleType(temp.getModuleType());
					 vo.setFromMember(temp.getFromMember());
					 vo.setRelateId(temp.getRelateId());
					 vo.setId(temp.getId());
					 vo.setIsRead(temp.getIsRead());
				 }
				 if(questObj1 instanceof WebReadToDoEn)
				 {
					 WebReadToDoEn temp = (WebReadToDoEn)questObj1;
					 vo.setTitle(temp.getTitle());
				 }
				 if(questObj1 instanceof WebReadToDoSc)
				 {
					 WebReadToDoSc temp = (WebReadToDoSc)questObj1;
					 vo.setTitle(temp.getTitle());
				 }
				 if(questObj1 instanceof WebReadToDoTc)
				 {
					 WebReadToDoTc temp = (WebReadToDoTc)questObj1;
					 vo.setTitle(temp.getTitle());
				 }
				 voList.add(vo);
			}
		}

		 return voList;
	}
	
	/**
	 * 通过会员ID,待阅类型获取所有待阅信息
	 * @author 林文伟
	 * @param portfolioId
	 * @return
	 */
	@Override
	public List<WebReadToDo> findWaitWebReadTodo(String memberId,String typeCode,String langCode) {
		
		List<WebReadToDo> voList = new ArrayList<WebReadToDo>();
		List<String> params = new ArrayList<String>();
		String hql = " from WebReadToDo t  ";
		hql += " left join "+this.getLangString("WebReadToDo", langCode) + " i on i.id = t.id ";
		hql += " where t.owner.id = ? and t.type = ? and (t.isRead = '0' or t.isRead is null) order by t.createTime desc";
		params.add(memberId);
		params.add(typeCode);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			for(int x=0;x<list.size();x++){
				Object[] each2 = (Object[])list.get(x);
				Object questObj0 =(Object)each2[0];//WebReadToDo实体
				Object questObj1 =(Object)each2[1];//WebReadToDoSc,En,Tc实体
				 WebReadToDo vo = new WebReadToDo();
				 if(questObj0 instanceof WebReadToDo)
				 {
					 WebReadToDo temp = (WebReadToDo)questObj0;
					 vo.setMsgUrl(temp.getMsgUrl());
					 vo.setType(temp.getType());
					 vo.setCreateTime(temp.getCreateTime());
				 }
				 if(questObj1 instanceof WebReadToDoEn)
				 {
					 WebReadToDoEn temp = (WebReadToDoEn)questObj1;
					 vo.setTitle(temp.getTitle());
				 }
				 if(questObj1 instanceof WebReadToDoSc)
				 {
					 WebReadToDoSc temp = (WebReadToDoSc)questObj1;
					 vo.setTitle(temp.getTitle());
				 }
				 if(questObj1 instanceof WebReadToDoTc)
				 {
					 WebReadToDoTc temp = (WebReadToDoTc)questObj1;
					 vo.setTitle(temp.getTitle());
				 }
				 voList.add(vo);
				 
				 //if(x>0)break;
			}
		}

		 return voList;
	}
	
	/*****************************************圈子分享功能********************************************/
	/**
	 * 获取所有圈子分享主题列表
	 * @author 林文伟
	 * @param portfolioId
	 * @return
	 */
	@Override
	public List<CornerInfo> findCornerInfoList(MemberBase loginUser,String langCode) {
		int memberType = loginUser.getMemberType();//账户类型：1-投资人，2-IFA,3-代理商
		int subType = loginUser.getSubMemberType();
		List<String> params = new ArrayList<String>();
		String hql = "";
		if(memberType==1) { //投资人investor 从好友中拿跟它的投资账号里拿ifa数据
			List<String> idList = new ArrayList<String>();
			//获取好友数据
			List<String> friendList = webFriendService.findInvestorFriendByInvestorId(loginUser.getId());
			if(null!=friendList&&!friendList.isEmpty())idList.addAll(friendList);
			//获取投资账号的IFA数据
			List<String> tempList = this.findInvestorAccountIfaByInvestorId(loginUser.getId());
			if(!tempList.isEmpty())idList.addAll(tempList);
			
			String sqlCondition = "";
			if(!idList.isEmpty()){
				for(String ifaId : idList){
					sqlCondition += "'" + ifaId + "',";
				}
			}
			if(sqlCondition.endsWith(","))sqlCondition = sqlCondition.substring(0, sqlCondition.length()-1);
			else sqlCondition = "''";
			
			hql = " from CornerInfo t where t.author.id in("+sqlCondition+") order by t.createTime desc";
		} else if(subType==22) {//IFA Firm
			hql = " from CornerInfo t where t.author.id in(select b.ifa.member.id FROM MemberIfaIfafirm b,MemberAdmin a WHERE a.ifafirm.id=b.ifafirm.id and a.member.id='"+loginUser.getId()+"' and a.type='1' AND b.checkStatus='1') order by t.createTime desc";
			
		} else if(subType==21) {//IFA
			//获取IFA公司的所有IFA
			hql = " from CornerInfo t where t.author.id in(select b.ifa.member.id FROM MemberIfaIfafirm b,MemberIfaIfafirm a WHERE b.ifafirm.id =a.ifafirm.id and a.ifa.member.id='"+loginUser.getId()+"' AND a.checkStatus='1' AND b.checkStatus='1') order by t.createTime desc";

		}else { //获取IFA所在的公司
			return null;
		}
			
		
		List<CornerInfo> list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list&&!list.isEmpty()){
			//if(list.size()>0){
				for(int x=0;x<list.size();x++){
					list.get(x).setIfaFollows(webFriendService.getIfaWebFriend(loginUser.getId()));
					list.get(x).setCreateTimeFmt(DateUtil.getDateTimeGoneFormatStr(list.get(x).getCreateTime(),langCode,""));
				}
			//}
		}

		 return list;
	}

	
	/**
	 * 根据投资人账号获取它的IFA数据
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public List<String> findInvestorAccountIfaByInvestorId(String investorId) {
		List<String> ifaList = new ArrayList<String>();
		String hql = "FROM InvestorAccount a WHERE a.member.id=?";
		List<InvestorAccount> list = this.baseDao.find(hql, new String[] { investorId }, false);
		if (null!=list&&!list.isEmpty()){
			for(InvestorAccount each : list){
				MemberIfa ifa = each.getIfa();
				if(null!=ifa){
					String id = ifa.getMember().getId();
					if(!ifaList.contains(id)){
						ifaList.add(id);
					}
				}
			}
		}
		return ifaList;
	}
	
	/**
	 * 获取某个IFA会员的圈子分享主题列表，每个主题有点赞评论等各方 面信息
	 * @author 林文伟
	 * @param portfolioId
	 * @return
	 */
	@Override
	public IfaCorner findIfaCornerInfo(String memberId,String langCode) {
		//个人的整体分享圈子
		IfaCorner ifaCorner = new IfaCorner();
		//获取个人相关信息
		MemberBase member=(MemberBase)this.baseDao.get(MemberBase.class, memberId);
		if(null!=member&&""!=member.getId()){
			ifaCorner.setMemberId(member.getId());
			ifaCorner.setIfaHeadImg(member.getIconUrl());
			ifaCorner.setIfaNickName(member.getNickName());
			ifaCorner.setFollows(webFriendService.getIfaWebFriend(memberId)); //获取好友数
			//ifaCorner.setTopicsCount(100);//获取发表主题数
			IfaExtInfo extVo = this.getIfaExtInfo(memberId);
			if(null!=extVo&&""!=extVo.getId()){
				ifaCorner.setLatestHighlight(extVo.getHighlight());
			}
		}
		//圈子主题
		Integer topicsCount = 0;
		List<IfaCornerInfoDetailVO> ifaCornerInfoDetailVOList = new ArrayList<IfaCornerInfoDetailVO>();
		List<String> params = new ArrayList<String>();
		String hql = " from CornerInfo t  ";
		hql += " where t.author.id = ? order by t.createTime desc";
		params.add(memberId);
		List<CornerInfo> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			topicsCount = list.size();//获取子题数量
			ifaCorner.setTopicsCount(topicsCount);
			for(CornerInfo each : list){ //循环每个主题，实体化一个IfaCornerInfoList
				IfaCornerInfoDetailVO vo = new IfaCornerInfoDetailVO();
				//获取每个主题的详细信息，包括点赞，评论列表等
				vo.setContent(each.getContent());
				vo.setCreateTime(each.getCreateTime());
				vo.setEmotionIcon(each.getEmotionIcon());
				vo.setIconUrl(each.getIconUrl());
				vo.setId(each.getId());
				vo.setLastReplayTime(each.getLastReplayTime());
				vo.setCreateTimeFormat(DateUtil.getDateTimeGoneFormatStr(each.getCreateTime(), langCode, ""));
				vo.setReplycount(0);//评论数默认初始化为0
				//分析点赞数据
				List<CornerLiked> cornerLikedList = this.findIfaCornerLikedList(each.getId());
				Integer cornerLikedCount = cornerLikedList.isEmpty()?0:cornerLikedList.size();
				vo.setLikedCount(cornerLikedCount);//点赞数量
				List<IfaCornerInfoLikedVO> ifaCornerInfoLikedList = new ArrayList<IfaCornerInfoLikedVO>();
				if(!cornerLikedList.isEmpty()){
					//int likeCount = cornerLikedList.size();
					//int index = 0;
					for(CornerLiked c :cornerLikedList){
						//index++;
						IfaCornerInfoLikedVO likedVo = new IfaCornerInfoLikedVO();
						likedVo.setMemberId(c.getMember().getId());
						likedVo.setMemberNickName(c.getMember().getNickName()+",");
						ifaCornerInfoLikedList.add(likedVo);
					}
				}
				vo.setIfaCornerInfoLikedList(ifaCornerInfoLikedList);//点 赞会员列表
				
				vo.setTopic(each.getTopic());
				vo.setUrl(each.getUrl());
				//获取主题的评论列表
				List<IfaCornerInfoReplyVO> replyList = findIfaCornerInfoReplyList(each.getId());
				vo.setIfaCornerInfoReplyList(replyList);
				if(!replyList.isEmpty())vo.setReplycount(replyList.size());
				ifaCornerInfoDetailVOList.add(vo);
			}
		}
		ifaCorner.setIfaCornerInfoDetailVOList(ifaCornerInfoDetailVOList);

		 return ifaCorner;
	}
	
	/**
	 * 获取每个主题的详细信息，包括点赞，评论列表等
	 * @author 林文伟
	 * @param portfolioId
	 * @return
	 */
	public List<IfaCornerInfoReplyVO> findIfaCornerInfoReplyList(String cornerId) {
		List<IfaCornerInfoReplyVO> ifaCornerInfoReplyList = new ArrayList<IfaCornerInfoReplyVO>();
		List<String> params = new ArrayList<String>();
		String hql = " from CornerReply t  ";
		hql += " where t.corner.id = ? order by t.replayTime asc";
		params.add(cornerId);
		List<CornerReply> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			for(CornerReply each : list){ //循环每个主题，实体化一个IfaCornerInfoList
				IfaCornerInfoReplyVO vo = new IfaCornerInfoReplyVO();//实例化一个前端评论VO
				vo.setContent(each.getContent());
				vo.setEmotionIcon(each.getEmotionIcon());
				vo.setId(each.getId());
				vo.setReplayTime(each.getReplayTime());
				vo.setReplyMemberId(each.getReplyUser().getId());
				vo.setReplyNickName(each.getReplyUser().getNickName());
				ifaCornerInfoReplyList.add(vo);
			}
		}

		 return ifaCornerInfoReplyList;
	}
	
	/**
	 * 获取每个主题的点赞列表数据
	 * @author 林文伟
	 * @param portfolioId
	 * @return
	 */
	public List<CornerLiked> findIfaCornerLikedList(String cornerId) {
		//List<CornerLiked> ifaCornerInfoReplyList = new ArrayList<CornerLiked>();
		List<String> params = new ArrayList<String>();
		String hql = " from CornerLiked t  ";
		hql += " where t.corner.id = ? order by t.createDate asc";
		params.add(cornerId);
		List<CornerLiked> list = this.baseDao.find(hql, params.toArray(), false);
		
		return list;
	}
	
	/***
     * 评论圈子主题
     * @author 林文伟
     * @date 2016-09-18
     */
	@Override
	public Boolean replyCornerInfo(String cornerId,String memberId,String content)
	{
		CornerReply vo = new CornerReply();
		CornerInfo corner = new CornerInfo();
		corner.setId(cornerId);
		MemberBase member = new MemberBase();
		member.setId(memberId);
		vo.setContent(content);
		vo.setCorner(corner);
		vo.setIsValid("1");
		vo.setReplayTime(new Date());
		vo.setReplyUser(member);
		vo = (CornerReply)this.baseDao.saveOrUpdate(vo, true);
		
		String hqlUpdateCount = "update CornerInfo t set t.replyCount=replyCount+1 where t.id=? ";
		List paramDel = new ArrayList();
		paramDel.add(cornerId);
		baseDao.updateHql(hqlUpdateCount, paramDel.toArray());
		
		return (""!=vo.getId())?true:false;
	}
	
	/***
     * 对主题进行点赞
     * @author 林文伟
     * @date 2016-09-18
     */
	@Override
	public Boolean likedCornerInfo(String cornerId,String memberId)
	{
		CornerLiked vo = new CornerLiked();
		CornerInfo corner = new CornerInfo();
		corner.setId(cornerId);
		MemberBase member = new MemberBase();
		member.setId(memberId);
		vo.setCreateDate(new Date());
		vo.setCorner(corner);
		vo.setMember(member);
		Boolean isLiked = checkMemberIslikedCorner(cornerId,memberId);
		if(!isLiked){
			vo = (CornerLiked)this.baseDao.saveOrUpdate(vo, true);
			return (""!=vo.getId())?true:false;
		} else{
			return false;
		}
	}
	
	/***
     * 判断用户是否已对主题进行了点赞
     * @author 林文伟
     * @date 2016-09-18
     */
	public Boolean checkMemberIslikedCorner(String cornerId,String memberId){
		String hql = " from CornerLiked t where t.corner.id = ? and t.member.id = ? ";
		List<String> params = new ArrayList<String>();
		params.add(cornerId);
		params.add(memberId);
		List list = this.baseDao.find(hql, params.toArray(), false);
		if(list.isEmpty())return false;
		else return true;
	}
	
	/***
     * 分享主题
     * @author 林文伟
     * @date 2016-09-18
     */
	@Override
	public Boolean shareInfo(String memberId,String title,String content,String url)
	{
		CornerInfo corner = new CornerInfo();
		MemberBase member = new MemberBase();
		member.setId(memberId);
		corner.setContent(content);
		corner.setTopic(title);
		corner.setIsValid("1");
		corner.setIsTransfer("1");
		corner.setCreateTime(new Date());
		corner.setLastUpdate(new Date());
		corner.setAuthor(member);
		corner.setUrl(url);
		corner.setReplyCount(0);
		
		corner = (CornerInfo)this.baseDao.saveOrUpdate(corner, true);
		
		return (""!=corner.getId())?true:false;
	}
	
	/***
     * 转载圈子主题
     * @author 林文伟
     * @date 2016-09-18
     */
	@Override
	public Boolean reprintCornerInfo(String cornerId,String memberId,String title,String content,String url)
	{
		CornerInfo corner = new CornerInfo();
		MemberBase member = new MemberBase();
		member.setId(memberId);
		corner.setContent(content);
		corner.setTopic(title);
		corner.setIsValid("1");
		corner.setIsTransfer("1");
		corner.setCreateTime(new Date());
		corner.setLastUpdate(new Date());
		corner.setAuthor(member);
		corner.setUrl(url);
		
		corner = (CornerInfo)this.baseDao.saveOrUpdate(corner, true);
		
		return (""!=corner.getId())?true:false;
	}
	
	//获取个人最新心情
	public IfaExtInfo getIfaExtInfo(String memberId)
	{ 
		IfaExtInfo vo = new IfaExtInfo();
		String hql1 = "from IfaExtInfo t where t.ifa.member.id='"+memberId+"'";
		List<IfaExtInfo> list = this.baseDao.find(hql1, null, false);
		if(!list.isEmpty()){
			vo = list.get(0);
		}
		
		return vo;
	}
	
	/***
     * 发表圈子主题
     * @author 林文伟
     * @date 2016-10-18
     */
	@Override
	public Boolean saveCornerInfo(String memberId,String title,String content,String imgpath )
	{
		CornerInfo corner = new CornerInfo();
		MemberBase member = new MemberBase();
		member.setId(memberId);
		corner.setContent(content);
		corner.setTopic(title);
		corner.setIsValid("1");
		corner.setIsTransfer("0");
		corner.setCreateTime(new Date());
		corner.setLastUpdate(new Date());
		corner.setAuthor(member);
		corner.setIconUrl(imgpath);

		corner = (CornerInfo)this.baseDao.saveOrUpdate(corner, true);
		
		return (""!=corner.getId())?true:false;
	}

	/**
	 * 获取ifa的客户沟通记录
	 * @author mqzou 2016-12-01
	 * @param jsonPaging
	 * @param ifaId
	 * @param beginDate
	 * @param endDate
	 * @param clientName
	 * @return
	 */
	@Override
	public JsonPaging findCommunicationRecord(JsonPaging jsonPaging, String ifaId, String beginDate, String endDate, String clientName,String keyWord) {
		StringBuilder hql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		hql.append(" from CrmMemo r left join CrmCustomer c on r.ifa.id=c.ifa.id and r.member.id=c.member.id where r.ifa.id=? and c !=null ");
		params.add(ifaId);
		if(null!=beginDate && !"".equals(beginDate)){
			hql.append(" and r.lastModify between ? and ?");
			params.add(DateUtil.getDate(beginDate));
			params.add(DateUtil.StringToDate(endDate, DateUtil.DEFAULT_DATE_TIME_FORMAT));
		}
		if(null!=clientName && !"".equals(clientName)){
			hql.append(" and c.nickName like ? ");
			params.add("%"+clientName+"%");
		}
		if(null!=keyWord && !"".equals(keyWord)){
			hql.append(" and r.memoText like ?");
			params.add("%"+keyWord+"%");
		}
		jsonPaging.setSort(" r.lastModify");
		jsonPaging.setOrder(" desc");
		List list=new ArrayList();
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		 Iterator it=jsonPaging.getList().iterator();
		 while (it.hasNext()) {
			Object[] object = (Object[]) it.next();
			CrmMemo memo=(CrmMemo)object[0];
			CrmCustomer customer=(CrmCustomer)object[1];
			if(null==customer)
				continue;
			IfaCommunicationRecordVO vo=new IfaCommunicationRecordVO();
			vo.setId(memo.getId());
			vo.setClientName(customer.getNickName());
			vo.setDate(DateUtil.dateToDateString(memo.getLastModify(), DateUtil.DEFAULT_DATE_FORMAT));
			vo.setTime(DateUtil.getTimeString(DateUtil.getDateStr(memo.getLastModify(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
			vo.setIconUrl(memo.getMember().getIconUrl());
			vo.setIfaId(ifaId);
			vo.setMemberId(memo.getMember().getId());
			String memoText=memo.getMemoText();
			if(null!=memoText && !"".equals(memoText)){
//				int index=memoText.indexOf("&lt;");
				String deliChar = "&rarr;";
				int index=memoText.indexOf(deliChar);//新间隔符号 → &rarr;
				if (index<0){
					deliChar="&lt;";
					index=memoText.indexOf(deliChar);
				}

				if(index>=0){
//					vo.setMemoText(memoText.substring(0,index));
					vo.setMemoText(StringEscapeUtils.unescapeHtml(memoText.substring(0,index)));
					if ("&lt;".equals(deliChar))
						vo.setImgText( HtmlUtils.htmlUnescape(memoText.substring(index,memoText.length())));
					else
						vo.setImgText( HtmlUtils.htmlUnescape(memoText.substring(index+deliChar.length(),memoText.length())));
				}else {
					vo.setMemoText(StringEscapeUtils.unescapeHtml(memoText));
					vo.setImgText("");
				}
			}
			
			String hql1=" from AccessoryFile r where r.relateId='"+vo.getId()+"' and r.moduleType='crm_memo'";
			List<AccessoryFile> fileList=baseDao.find(hql1, null, false);
			
			vo.setFileList(fileList);
			
			list.add(vo);
		}
		 jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * IFA查看表现最好或最差的5个投资组合
	 * @author mqzou 2016-12-21
	 * @param jsonPaging
	 * @param ifaId
	 * @param statistic
	 * @param currency
	 * @return
	 */
	@Override
	public JsonPaging findCustomerTopPortfolio(JsonPaging jsonPaging, String ifaId, String statistic, String currency,String langCode) {
		List<IfaHomePortfolioVO> list=new ArrayList<IfaHomePortfolioVO>();
		List<Object> params=new ArrayList<Object>();
		StringBuilder hql=new StringBuilder();
		
		hql.append("  from PortfolioHold h  ");
		hql.append(" left join CrmCustomer c on c.member.id=h.member.id");
		if(null!=statistic && !"".equals(statistic)){
			hql.append(" left join PortfolioHoldReturn r on r.portfolioHold.id=h.id");
		}
		hql.append(" where c.ifa.id=? and h.ifFirst='0' and h.ifa.id=? ");
		params.add(ifaId);
		params.add(ifaId);
		if(null!=statistic && !"".equals(statistic)){
			hql.append(" and (r.periodCode=? or r.periodCode is null)");
			params.add(statistic);
		}
		
		jsonPaging =this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		Iterator it=jsonPaging.getList().iterator();
		while (it.hasNext()) {
			IfaHomePortfolioVO vo=new IfaHomePortfolioVO();
			Object[] object = (Object[]) it.next();
			PortfolioHold hold=(PortfolioHold)object[0];
			CrmCustomer customer=(CrmCustomer)object[1];
			PortfolioHoldReturn return1=null;
			if(object.length==3)
			  return1=(PortfolioHoldReturn)object[2];
			vo.setMemberId(customer.getMember().getId());
			
			if(null!=statistic && !"".equals(statistic))
				vo.setReturnRate(null!=return1 && null!=return1.getIncrease()? return1.getIncrease()*100:0);
			else {
				vo.setReturnRate(null!=hold.getTotalReturnRate()?hold.getTotalReturnRate()*100:0);
			}
			
			vo.setReturnRateStr(StrUtils.getNumberString(vo.getReturnRate(),"#,##0.00"));
			if(null!=hold){
				double aum=portfolioHoldService.findPortfolioAuM(hold.getId(), currency);
				vo.setAum(aum);
				vo.setAumStr(StrUtils.getNumberString(vo.getAum(),"#,##0.00"));
				vo.setId(null!=hold? hold.getId():"");
				if(null!=hold.getCreateTime()){
					int days=0;
					try {
						days = daysBetween(hold.getCreateTime(), DateUtil.getCurDate());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					vo.setInvDays(String.valueOf(days));
				}
			}

			vo.setUserId(customer.getMember().getId());
			
			/*String nickName=customer.getNickName();
			if(null==nickName || "".equals(nickName))
				nickName=customer.getMember().getLoginCode();*/
			String nickName=getCommonMemberName(customer.getMember().getId(), langCode, "2");
			vo.setNickName(nickName);
			vo.setRiskLevel(String.valueOf(null!=hold && null!=hold.getPortfolio()?hold.getPortfolio().getRiskLevel():""));
			vo.setStatistic(statistic);
			vo.setPortfolioName(null!=hold? hold.getPortfolioName():"");
			vo.setCustomerId(customer.getId());
			MemberBase member=null!=hold && null!=hold.getIfa()?hold.getIfa().getMember():null;
			if(null!=member){
				String displayColor=member.getDefDisplayColor();
				if(null==displayColor || "".equals(displayColor))
					displayColor=CommonConstants.DEF_DISPLAY_COLOR;
				vo.setChartUrl(getPerformanceChartImage(vo.getId(), CommonConstantsWeb.CHART_IMAGE_SIZE_MIDDL, displayColor));
			}
			
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 通过会员ID查询IFA的客户列表
	 * 用户类型：prospect,existingc client
	 * @author zxtan
	 * @date 2017-01-04
	 * @param memberId  IFA会员ID
	 * @param clientType  all：包含正式和潜在，1：正式，0：潜在 
	 * @return list IFA客户列表
	 */
	public List<IfaClientVO> findMyClientList(String memberId, String clientType,String langCode) {
		List<IfaClientVO> voList = new ArrayList<IfaClientVO>();
		List<String> params = new ArrayList<String>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from MemberBase m ");
		hql.append(" inner join MemberIndividual mi on m.id=mi.member.id ");
		hql.append(" inner join CrmCustomer c on m.id=c.member.id ");
		hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		hql.append(" where i.member.id=? ");

		params.add(memberId);
		
		if("0,1,".indexOf(clientType)>-1 ){
			hql.append(" and c.clientType =? ");
			params.add(clientType);
		}else{
			hql.append(" and c.clientType in ('0','1') ");
		}
		
		hql.append(" order by c.clientType desc, c.nickName ");
				
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				IfaClientVO vo = new IfaClientVO();
				Object[] objs = (Object[]) list.get(i);
				MemberBase member = (MemberBase) objs[0];
				MemberIndividual detail = (MemberIndividual) objs[1];
				CrmCustomer customer = (CrmCustomer) objs[2];
				vo.setId(member.getId());
				if("1".equals(customer.getClientType())){
					//existing
					vo.setFullName(getCommonMemberName(member.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				}else {
					//prospect
					String fullName = customer.getNickName();
					if(StringUtils.isBlank(fullName)){
						fullName = getCommonMemberName(member.getId(), langCode, CommonConstants.MEMBER_NAME_NICKNAME);
					}
					vo.setFullName(fullName);
				}
				
				
				vo.setClientType(customer.getClientType());
				
//				String iconUrl = customer.getIconUrl();
//				if(null == iconUrl || "".equals(iconUrl)){
//					iconUrl = member.getIconUrl();
//				}
				//暂时先用base表中
				String iconUrl = member.getIconUrl();
				iconUrl = PageHelper.getUserHeadUrl(iconUrl,detail.getGender());				
				vo.setIconUrl(iconUrl);
				
				voList.add(vo);
			}
		}
		
		return voList;
	}

	
	/**
	 * 通过会员ID查询IFA的同事列表
	 * 用户类型：Colleague
	 * @author zxtan
	 * @date 2017-01-04
	 * @param memberId  IFA会员ID
	 * @param langCode   
	 * @return list IFA同事列表
	 */
	public List<IfaClientVO> findMyColleagueList(String memberId, String langCode) {
		List<IfaClientVO> voList = new ArrayList<IfaClientVO>();
		List<String> params = new ArrayList<String>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from MemberBase m ");
		hql.append(" inner join MemberIfa mi on m.id=mi.member.id ");
		hql.append(" inner join IfafirmTeamIfa f on mi.id = f.ifa.id ");
		hql.append(" where m.id !=? and f.team.id in ( ");
		hql.append(" select ti.team.id from IfafirmTeamIfa ti where ti.ifa.member.id=? ");
		hql.append(" ) ");
		
		if("sc".equalsIgnoreCase(langCode) || "tc".equalsIgnoreCase(langCode)){
			hql.append(" order by mi.nameChn ");
		}else {
			hql.append(" order by mi.firstName ");
		}
				
		params.add(memberId);
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				IfaClientVO vo = new IfaClientVO();
				Object[] objs = (Object[]) list.get(i);
				MemberIfa ifa = (MemberIfa) objs[1];
								
				vo.setId(ifa.getMember().getId());
				vo.setFirstName(ifa.getFirstName());
				vo.setLastName(ifa.getLastName());
				vo.setFullName(getCommonMemberName(ifa.getMember().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
//				if("sc".equalsIgnoreCase(langCode) || "tc".equalsIgnoreCase(langCode)){
//					vo.setFullName(ifa.getNameChn());
//					if(null == vo.getFullName() || "".equals(vo.getFullName())){
//						vo.setFullName(ifa.getFirstName() +" "+ ifa.getLastName());
//					}
//				}else {
//					vo.setFullName(ifa.getFirstName() +" "+ ifa.getLastName());
//					if(null == vo.getFullName() || "".equals(vo.getFullName())){
//						vo.setFullName(ifa.getNameChn());
//					}
//				}

				String iconUrl = PageHelper.getUserHeadUrl(ifa.getMember().getIconUrl(),ifa.getGender());
				vo.setIconUrl(iconUrl);
				voList.add(vo);
			}
		}
		
		return voList;
	}

	/**
	 * 获取ifa的AE帐号
	 * @author mqzou 2017-01-04
	 * @param ifaId
	 * @param distributorId
	 * @return
	 */
	@Override
	public String findIfaAE(String ifaId, String distributorId) {
		String hql=" from IfaDistributor r where r.ifa.id=? and r.distributor.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaId);
		params.add(distributorId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			IfaDistributor vo=(IfaDistributor)list.get(0);
			if(null!=vo){
				return vo.getAeCode();
			}
		}
		return null;
	}
	
	/**
	 * 获取IfaDistributor帐号
	 * @author zxtan 2017-01-11
	 * @param aeCode
	 * @return
	 */
	@Override
	public IfaDistributor findIfaDistributorInfo(String aeCode) {
		String hql=" from IfaDistributor r where r.isValid='1' and r.aeCode=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(aeCode);
		List<IfaDistributor> list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			return (IfaDistributor)list.get(0);
			
		}
		return null;
	}

	/**
	 * 获取ifa持仓客户对应的AE帐号
	 * @author zxtan 2017-01-06
	 * @param ifaId
	 * @return
	 */
	public List<String> findIfaAEList(String ifaId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from IfaDistributor m ");
		hql.append(" where m.ifa.id=? and exists ( ");
		hql.append(" 	select ia.id from InvestorAccount ia ");
		hql.append(" 	inner join PortfolioHoldProduct hp on ia.id=hp.account.id ");
		hql.append(" 	inner join PortfolioHold h on h.id=hp.portfolioHold.id ");
		hql.append("	where h.totalMarketValue>0 and ia.ifa.id=m.ifa.id and ia.distributor.id=m.distributor.id ");
		hql.append(" ) ");
		List<Object> params=new ArrayList<Object>();
		params.add(ifaId);
		List<String> voList = new ArrayList<String>();
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){			 
			for (int i = 0; i < list.size(); i++) {  
				IfaDistributor vo = (IfaDistributor)list.get(i);
				voList.add(vo.getAeCode()); 
			}  
		}
		
		return voList;
	}
	
	/***
     * 删除待办信息
     * @author 林文伟
     * @date 2017-02-18
     */
	@Override
	public Boolean delWebReadToDo(String id)
	{
		String hql = " delete from WebReadToDo r where  r.id = ?   ";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		int rs = this.baseDao.updateHql(hql, params.toArray());
		hql = "delete from WebReadToDoEn r where  r.id = ? ";
		this.baseDao.updateHql(hql, params.toArray());
		hql = "delete from WebReadToDoTc r where  r.id = ? ";
		this.baseDao.updateHql(hql, params.toArray());
		hql = "delete from WebReadToDoSc r where  r.id = ? ";
		this.baseDao.updateHql(hql, params.toArray());
		
		return rs>0?true:false;
	}
	
	/**
	 * 更新系统消息读状态
	 * @author 林文伟 
	 * @date 2016-10-19
	 * @return
	 */
	@Override
	public boolean updateMsgReadStatus(String id, String status) {
		String hql="update WebReadToDo r set isRead=?,readTime=? where r.id=?  ";
		List params=new ArrayList();
		params.add(status);
		params.add(new Date());
		params.add(id);
		int rs=this.baseDao.updateHql(hql, params.toArray());
		if(rs>0)return true;
		else return false;
	}
	
	/**
	 * web前台获取系统消息JSON数据
	 * @author 林文伟  
	 * @param jsonPaging 分页实体
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public JsonPaging findDiscoverMsgForWeb(JsonPaging jsonPaging,MemberBase baseMem, String typeCode,String readStatus,String langCode,String keyWords){
		
//		List<String> params = new ArrayList<String>();
//		String hql = " from WebReadToDo t  ";
//		hql += " left join "+this.getLangString("WebReadToDo", langCode) + " i on i.id = t.id ";
//		hql += " where t.owner.id = ? and t.type = ? ";
//		if(StringUtils.isNotBlank(status)){
//			hql += " and t.isRead = '"+status+"'" ;
//		}
//		hql += " order by t.createTime desc";
//		params.add(memberId);
//		params.add(typeCode);
		
		String hql = " FROM WebReadToDo t " ;
		hql += " left join "+this.getLangString("WebReadToDo", langCode) + " i on i.id = t.id ";
		hql += " WHERE  t.owner.id=? and t.type = ? ";
		if(StringUtils.isNotBlank(readStatus)){
			if("0".equals(readStatus))
				hql += " and ( t.isRead = ? or t.isRead is null ) " ;
			else
				hql += " and t.isRead = ? " ;
		}
		if(StringUtils.isNotBlank(keyWords)){
			hql += " and i.title like ? " ;
		}
		hql += " order by t.createTime desc";
		List<Object> params = new ArrayList<Object>();
		params.add(baseMem.getId());
		params.add(typeCode);
		if(StringUtils.isNotBlank(readStatus)){
			params.add(readStatus);
		}
		if(StringUtils.isNotBlank(keyWords)){
			params.add("%" + keyWords + "%");
		}
		

		//List<InsightInfo> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		jsonPaging = this.baseDao.selectJsonPaging(hql,params.toArray(),jsonPaging, false);
		List list = jsonPaging.getList();
		List<WebReadToDo> voList = new ArrayList<WebReadToDo>();
		for(int x=0;x<list.size();x++){

			Object[] each2 = (Object[])list.get(x);
			Object questObj0 =(Object)each2[0];//WebReadToDo实体
			Object questObj1 =(Object)each2[1];//WebReadToDoSc,En,Tc实体
			 WebReadToDo vo = new WebReadToDo();
			 if(questObj0 instanceof WebReadToDo)
			 {
				 WebReadToDo temp = (WebReadToDo)questObj0;
				 vo.setMsgUrl(temp.getMsgUrl());
				 vo.setType(temp.getType());
				 //转换时间格式
				 String createTimeFmt = DateUtil.getDateTimeGoneFormatStr2(temp.getCreateTime(),langCode,CoreConstants.DATE_FORMAT);
				 if(StringUtils.isNotBlank(baseMem.getDateFormat())){
					 createTimeFmt = DateUtil.getDateTimeGoneFormatStr2(temp.getCreateTime(),langCode,baseMem.getDateFormat());
				 } 
				 vo.setCreateTimeFmt(createTimeFmt);
				 vo.setCreateTime(temp.getCreateTime());
				 
				 vo.setMsgParam(temp.getMsgParam());
				 vo.setModuleType(temp.getModuleType());
				 vo.setFromMember(temp.getFromMember());
				 vo.setRelateId(temp.getRelateId());
				 vo.setId(temp.getId());
				 vo.setIsRead(temp.getIsRead());

			 }
			 if(questObj1 instanceof WebReadToDoEn)
			 {
				 WebReadToDoEn temp = (WebReadToDoEn)questObj1;
				 vo.setTitle(temp.getTitle());
			 }
			 if(questObj1 instanceof WebReadToDoSc)
			 {
				 WebReadToDoSc temp = (WebReadToDoSc)questObj1;
				 vo.setTitle(temp.getTitle());
			 }
			 if(questObj1 instanceof WebReadToDoTc)
			 {
				 WebReadToDoTc temp = (WebReadToDoTc)questObj1;
				 vo.setTitle(temp.getTitle());
			 }
			 voList.add(vo);
			 
			 
				
		}
		jsonPaging.setList(voList);
		//jsonPaging.setTotal(voList.size());
		return jsonPaging;
	}
	
	/**
	 * 获取基础数据
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	private String getParamConfig(String type, String configCode, String langCode) {
		String name = null;
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(configCode)) {
			if (StringUtils.isBlank(langCode)) {
				langCode = CommonConstants.LANG_CODE_EN;
			}
			langCode = langCode.substring(0, 1).toUpperCase()+langCode.substring(1, 2);
			String[] objs = null;
			if(configCode.indexOf(",") > -1){
				objs = configCode.split(",");
			}else{
				objs = new String[]{configCode};
			}
			for (int i = 0; i < objs.length; i++) {
				StringBuffer hql = new StringBuffer("" +
						" SELECT" +
						" c.name" + langCode +
						" FROM" +
						" SysParamConfig c" +
						" WHERE" +
						" c.type.typeCode=?" +
						" AND" +
						" c.configCode=?");
				List<Object> params = new ArrayList<Object>();
				params.add(type);
				params.add(objs[i]);
				List<String> sysParamConfigs = this.baseDao.find(hql.toString(), params.toArray(), false);
				if(sysParamConfigs != null && !sysParamConfigs.isEmpty()){
					name = sysParamConfigs.get(0) + ",";
				}
			}
		}
		return StrUtils.reHeavy(name);
	}
	
	/**
	 * 获取策略分配比例
	 * @author wwluo
	 * @data 2016-10-25
	 * @param strategyId 策略id
	 * @return 
	 */
	private List<StrategyAllocation> getStrategyAllocation(String strategyId) {
		List<StrategyAllocation> strategyAllocations = new ArrayList<StrategyAllocation>();
		if(StringUtils.isNotBlank(strategyId)){
			StringBuffer hql = new StringBuffer(" from StrategyAllocation a where a.layerLevel=1 and a.strategy.id=?");
			List params = new ArrayList();
			params.add(strategyId);
			strategyAllocations = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return strategyAllocations;
	}
	
	/**
	 * 获取ifa的基本信息
	 * @author mqzou  2017-06-16
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	@Override
	public IfaInfoVO findIfaInfo(String memberId, String langCode) {
		MemberIfa ifa=memberBaseService.findIfaMember(memberId);
		if(null!=ifa){
			IfaInfoVO vo=new IfaInfoVO();
			vo.setMemberId(ifa.getMember().getId());
			vo.setIfaId(ifa.getId());
			vo.setMemberName(getCommonMemberName(memberId, langCode, "2"));
			vo.setEmail(ifa.getMember().getEmail());
			vo.setPhoneNumber(ifa.getMember().getMobileNumber());
			vo.setIconUrl(PageHelper.getUserHeadUrl(ifa.getMember().getIconUrl(), ifa.getGender()));
			vo.setGender(ifa.getGender());
			vo.setPopularityTotal(null!=ifa.getPopularityTotal()?ifa.getPopularityTotal().toString():"0");
			MemberIfafirm firm=findIfafirmByIfa(ifa.getId());
			if(null!=firm && null!=firm.getFirmLogo() && !"".equals(firm.getFirmLogo())){
				vo.setIfafirmIconUrl(PageHelper.getLogoUrl(firm.getFirmLogo(), "F"));
				Object ifafirm=ifafirmManageService.findCompanyNameById(langCode, firm.getId());
				if(langCode.equals(CommonConstants.LANG_CODE_EN)){
					MemberIfafirmEn object=(MemberIfafirmEn)ifafirm;
					vo.setIfafirmName(object.getCompanyName());
				}else if (langCode.equals(CommonConstants.LANG_CODE_SC)) {
					MemberIfafirmSc object=(MemberIfafirmSc)ifafirm;
					vo.setIfafirmName(object.getCompanyName());
				}else if (langCode.equals(CommonConstants.LANG_CODE_TC)) {
					MemberIfafirmTc object=(MemberIfafirmTc)ifafirm;
					vo.setIfafirmName(object.getCompanyName());
				}
			}else {
				vo.setIfafirmIconUrl("");
			}
			Date investBegin=ifa.getInvestLifeBegin();
        	if(null!=investBegin){
        		long days=DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(investBegin, "yyyy-MM-dd"), DateUtil.getCurDateStr());
        		 int year=Math.round(days/365);
        		 vo.setInvestLife(String.valueOf(year+1));
        	}else {
				vo.setInvestLife("0");
			}
        	return vo;
        	
		}
		return null;
	}
	/**
	 * 获取ifa的ifafirm
	 * @author mqzou 2017-06-15
	 * @param ifaId
	 * @return
	 */
	private MemberIfafirm findIfafirmByIfa(String ifaId){
		String hql=" from MemberIfaIfafirm r where r.ifa.id='"+ifaId+"' and r.isValid='1' and r.checkStatus='1'";
		List list=baseDao.find(hql, null, false);
		if(null!=list && !list.isEmpty()){
			MemberIfaIfafirm memberIfafirm=(MemberIfaIfafirm)list.get(0);
			if(null!=memberIfafirm){
				return memberIfafirm.getIfafirm();
				
			}
		}
		return null;
	}
	
	
	
}
