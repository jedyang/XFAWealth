package com.fsll.wmes.ifa.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.InsightCount;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebRecommended;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.ifa.service.IfaSpaceService;
import com.fsll.wmes.ifa.service.InsightInfoService;
import com.fsll.wmes.ifa.vo.IfaMessageVO;
import com.fsll.wmes.ifa.vo.InsightInfoVo;
import com.fsll.wmes.ifa.vo.InsightItemVo;
import com.fsll.wmes.ifa.vo.InsightSearchParamVo;
import com.fsll.wmes.ifa.vo.InsightVistorVo;
import com.fsll.wmes.member.service.MemberBaseService;

/**
 * 观点接口
 * @author zpzhou
 * @date 2016-8-11
 */
@Service("insightInfoService")
//@Transactional
public class InsightInfoServiceImpl extends BaseService implements InsightInfoService {
	@Autowired
	 private IfaSpaceService ifaSpaceService;
	
	@Autowired
	 private CrmCustomerService crmCustomerService;
	
	@Autowired
	 private MemberBaseService memberBaseService;
	/**
	 * 得到观点列表信息
	 * @param jsonPaging 分页参数
	 * @param memberId 用户ID
	 * @return
	 */
	public JsonPaging findInsightList(JsonPaging jsonPaging,String memberId,String type){
		String hql = " from InsightInfo i ";
		hql += " left join InsightCount c on c.infoId=i.id ";
		hql += " left join WebInvestorVisit v on v.relateId=i.id and v.moduleType='insight' and v.member.id=? ";
		if("1".equals(type)){//我的关注时不需要再关联关注表
			hql += " left join WebRecommended r on r.relateId=i.id and r.moduleType='insight' and r.creator.id=? ";
			hql += " where i.id in (select k.relateId from WebFollow k where k.moduleType='insight' and k.isValid='1' and k.followFlag='1' and k.member.id=?) ";
		}else if("2".equals(type)){//我的推荐时不需要再关联关注表
			hql += " left join WebFollow w on w.relateId=i.id and w.moduleType='insight' and w.isValid='1' and w.member.id=? ";
			hql += " where i.id in (select y.relateId from WebRecommended y where y.moduleType='insight' and y.creator.id=?) ";
		}else{
			hql += " left join WebFollow w on w.relateId=i.id and w.moduleType='insight' and w.isValid='1' and w.followFlag='1' and w.member.id=? ";
			hql += " left join WebRecommended r on r.relateId=i.id and r.moduleType='insight' and r.creator.id=? ";
		}
		hql += " order by i.overheadTime,i.pubDate asc ";
		List params = new ArrayList();
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		List<InsightItemVo> iList=new ArrayList<InsightItemVo>();
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			InsightInfo info=(InsightInfo)objs[0];
			InsightCount count=(InsightCount)objs[1];
			WebInvestorVisit visit=(WebInvestorVisit)objs[2];
			WebFollow follow=null;
			WebRecommended recommended=null;
			InsightItemVo vo=new InsightItemVo();
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
			if(null!=visit){
				vo.setInvestorFlag("1");
			}else{
				vo.setInvestorFlag("0");
			}
			if("1".equals(type)){//我的关注
				vo.setFollowFlag("1");
				recommended=(WebRecommended)objs[3];
				if(null!=recommended){
					vo.setRecommendedFlag("1");
				}else{
					vo.setRecommendedFlag("0");
				}
			}else if("2".equals(type)){//我的推荐
				vo.setRecommendedFlag("1");
				follow=(WebFollow)objs[3];
				if(null!=follow){
					vo.setFollowFlag("1");
				}else{
					vo.setFollowFlag("0");
				}
			}else{
				follow=(WebFollow)objs[3];
				recommended=(WebRecommended)objs[4];
				if(null!=follow){
					vo.setFollowFlag("1");
				}else{
					vo.setFollowFlag("0");
				}
				if(null!=recommended){
					vo.setRecommendedFlag("1");
				}else{
					vo.setRecommendedFlag("0");
				}
			}
			iList.add(vo);
    	}
		jsonPaging.setList(iList);
		return jsonPaging;
	}
	/**
	 * 得到观点详细信息
	 * @param insightId
	 * @param memberId
	 * @return
	 */
	public InsightInfoVo findInsightInfo(String insightId,String memberId){
		InsightInfoVo vo=new InsightInfoVo();
		String hql = " from InsightInfo i ";
		hql += " left join InsightCount c on c.infoId=i.id ";
		hql += " left join WebFollow w on w.relateId=i.id and w.moduleType='insight' and w.isValid='1' and w.member.id=?";
		hql += " left join WebInvestorVisit v on v.relateId=i.id and v.moduleType='insight' and v.member.id=?";
		hql += " left join WebRecommended r on r.relateId=i.id and r.moduleType='insight' and r.creator.id=?";
		hql += " where i.id=? ";
		List params = new ArrayList();
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		params.add(insightId);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			InsightInfo info=(InsightInfo)objs[0];
			InsightCount count=(InsightCount)objs[1];
			WebFollow follow=(WebFollow)objs[2];
			WebInvestorVisit visit=(WebInvestorVisit)objs[3];
			WebRecommended recommended=(WebRecommended)objs[4];
			vo.setId(info.getId());
			vo.setRegionType(info.getGeoAllocation());
			vo.setSectionType(info.getSector());
			vo.setTitle(info.getTitle());
			vo.setContent(info.getContent());
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
				vo.setCommentsDay(count.getCommentsDay());
				vo.setCommentsMonth(count.getCommentsMonth());
				vo.setCommentsWeek(count.getCommentsWeek());
				vo.setViewsDay(count.getViewsDay());
				vo.setViewsMonth(count.getViewsMonth());
				vo.setViewsWeek(count.getViewsWeek());
				vo.setUpsDay(count.getUpsDay());
				vo.setUpsMonth(count.getUpsMonth());
				vo.setUpsWeek(count.getUpsWeek());
			}
			if(null!=follow){
				vo.setFollowFlag("1");
			}else{
				vo.setFollowFlag("0");
			}
			if(null!=visit){
				vo.setInvestorFlag("1");
			}else{
				vo.setInvestorFlag("0");
			}
			if(null!=recommended){
				vo.setRecommendedFlag("1");
			}else{
				vo.setRecommendedFlag("0");
			}
    	}
		return vo;
	}

	@Override
	public JsonPaging findAllInsight(JsonPaging jsonPaging, String keyword,
			String lang) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from InsightInfo l where 1=1 ");
		if(null != keyword && !"".equals(keyword)){
			hql.append(" and ( l.title like ? or l.content like ?)");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		return jsonPaging;
	}
	
	@Override
	public InsightInfo findInsightById(String id, String lang) {
		// TODO Auto-generated method stub
		InsightInfo obj = (InsightInfo) this.baseDao.get(InsightInfo.class, id);
		return obj;
	}

	/**
	 * web前台获取IFA观点列表
	 * @author scshi  modify by wwluo 2017/1/19
	 * @param jsonPaging 分页实体
	 * @param loginUserId 登陆用户
	 * @param searchParam 搜索条件vo
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public JsonPaging findIfaInsightListForWeb(JsonPaging jsonPaging,String loginUserId, InsightSearchParamVo searchParam,String viewMemberId){
		StringBuffer hql = new StringBuffer("" +
				" FROM" +
				" InsightInfo t" +
				" WHERE" +
				" t.creator.id=?" +
				"");
		List<Object> params = new ArrayList<Object>();
		params.add(loginUserId);
		if(null!=searchParam.getIssuedDate() && !"".equals(searchParam.getIssuedDate())){
			String issudeDate = searchParam.getIssuedDate();
			SysParamConfig config = this.getParamConfigByCode(issudeDate);
			Calendar cal = Calendar.getInstance();
			if (config != null && StringUtils.isNotBlank(config.getConfValueEn())) {
				String confValue = config.getConfValueEn();
				char type = confValue.charAt(confValue.length()-1);
				Integer day = Integer.parseInt(confValue.substring(0, confValue.length()-1));
				if('D' == type && day > 1){
					cal.set(Calendar.DATE, - day);
				}else if('W' == type){
					cal.set(Calendar.DATE, - (day*7));
				}else if('M' == type){
					cal.set(Calendar.MONTH, - day);
				}else if('Y' == type){
					cal.set(Calendar.YEAR, - day);
				}
			}
			hql.append(" AND t.pubDate>=?");
			params.add(cal.getTime());
		}
		//过滤指定时间      modify by wwluo 2016/11/22  
		if (StringUtils.isNotBlank(searchParam.getFromDate()) && StringUtils.isNotBlank(searchParam.getToDate())) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				hql.append(" AND t.pubDate BETWEEN ? AND ?");
				params.add(dateFormat.parse(searchParam.getFromDate()));
				params.add(dateFormat.parse(searchParam.getToDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		String allocation = searchParam.getAllocation();
		if (StringUtils.isNotBlank(allocation)) {
			List allocations = JsonUtil.toListMap(allocation);
			String allocationStr = null;
			if(allocations != null && !allocations.isEmpty()){
				StringBuffer para = new StringBuffer();
				for (int i = 0; i < allocations.size(); i++) {
					if(i == allocations.size() - 1){
						para.append(" INSTR(t.geoAllocation,'" + allocations.get(i) + "')>0");
					}else{
						para.append(" INSTR(t.geoAllocation,'" + allocations.get(i) + "')>0 OR");
					}
				}
				allocationStr = para.toString();
			}
			if (StringUtils.isNotBlank(allocationStr)) {
				hql.append(" AND ("+allocationStr+")");
			}
		}
		//过滤状态
		if (StringUtils.isNotBlank(searchParam.getStatus())) {
			hql.append(" AND t.status=?");
			params.add(Integer.parseInt(searchParam.getStatus()));
		}
		String sector = searchParam.getSector();
		if (StringUtils.isNotBlank(sector)) {
			List sectors = JsonUtil.toListMap(sector);
			String sectorStr = null;
			if(sectors != null && !sectors.isEmpty()){
				StringBuffer para = new StringBuffer();
				for (int i = 0; i < sectors.size(); i++) {
					if(i == sectors.size() - 1){
						para.append(" INSTR(t.sector,'" + sectors.get(i) + "')>0");
					}else{
						para.append(" INSTR(t.sector,'" + sectors.get(i) + "')>0 OR");
					}
				}
				sectorStr = para.toString();
			}
			if (StringUtils.isNotBlank(sectorStr)) {
				hql.append(" AND ("+sectorStr+")");
			}
		}
		if(null!=searchParam.getKeyWord() && !"".equals(searchParam.getKeyWord())){
			String keyWord = searchParam.getKeyWord();
			hql.append(" AND t.title like ?");
			params.add("%"+keyWord+"%");
		}
		if(null!=searchParam.getIssuedDateSort()&& !"".equals(searchParam.getIssuedDateSort())){//发布日期排序
			hql.append(" ORDER BY t.pubDate "+searchParam.getIssuedDateSort());
		}else if(null!=searchParam.getViewSort() && !"".equals(searchParam.getViewSort())){//点击次数排序
			hql.append(" ORDER BY t.click "+ searchParam.getViewSort());
		}else{//默认排序
			hql.append(" ORDER BY t.overhead DESC,t.overheadTime DESC");
		}
		//List<InsightInfo> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(),params.toArray(),jsonPaging, false);
		List<InsightInfo> list = jsonPaging.getList();
		List<InsightInfo> voList = new ArrayList<InsightInfo>();
		for(int x=0;x<list.size();x++){
			InsightInfo info = list.get(x);
			//列表数据处理
			String insightId = info.getId();
			// modify by wwluo 2016/11/22 
			JsonPaging paging = new JsonPaging();
			//观点访客列表;
			paging = this.loadVistorList(paging,insightId,"insight",searchParam.getLangCode());
			if(paging.getList().isEmpty()){
				info.setFrendsCount(0);
			}else{
				info.setFrendsCount(paging.getTotal());
			}
			SimpleDateFormat format = new SimpleDateFormat(CommonConstants.FORMAT_DATE_TIME);
			if(info.getCreateTime() != null){
				info.setCreateTimeStr(format.format(info.getCreateTime()));	
			}
			if(info.getPubDate() != null){
				info.setPubDateStr(format.format(info.getPubDate()));
			}
			// modify end
			String content = StringEscapeUtils.unescapeHtml(info.getContent());
			//content = StringUtils.replace(content, "&nbsp;", "\r\n");
			info.setContent(content);//StrUtils.txt2htm
			if(loginUserId.equals(viewMemberId))//如果当前访问者正好是观点的创建者，则可访问 ，否则要判断权限
				voList.add(info);
			else{
				if(ifaSpaceService.getMemberCanViewModule(viewMemberId,insightId,CommonConstantsWeb.WEB_VIEW_MODULE_INSIGHT))voList.add(info);
			}
				
		}
		jsonPaging.setList(voList);
		if(voList!=null&&!voList.isEmpty())jsonPaging.setTotal(voList.size());
		else jsonPaging.setTotal(0);
		//jsonPaging.setTotal(voList.size());
		return jsonPaging;
	}
	
	/**
	 * 观点访客列表
	 * @author scshi
	 * @date 20160822
	 * @param insightId 观点唯一标识
	 * @param vistType	对应模块,ifa空间访客,insight观点访客,news新闻访客,strategy策略,portfolio_arena组合竞技场访客,portfolio_info组合访客
	 * */
	public JsonPaging loadVistorList(JsonPaging jsonPaging,String insightId,String vistType,String lang) {
		String hql = " select max(t.vistiTime),t.member.id,count(distinct t.member.id) ";
		hql +=" from WebInvestorVisit t where t.relateId=? and t.moduleType=? ";
		hql += " group by t.member.id ";
		hql += " order by t.vistiTime desc ";
		List params = new ArrayList();
		params.add(insightId);
		params.add(vistType);
		
		
		// modify by wwluo 2016/11/22  修改为分页数据
		//List list = this.baseDao.find(hql, params.toArray(), false);
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<InsightVistorVo> voList = new ArrayList<InsightVistorVo>();
		if(jsonPaging.getList().isEmpty())return jsonPaging;
		for(int z=0;z<jsonPaging.getList().size();z++){
			InsightVistorVo vo = new InsightVistorVo();
			Object[] objs = (Object[])jsonPaging.getList().get(z);
			String visitTime = objs[0]==null?"":objs[0].toString();
			//查看时间差
			if(!"".equals(visitTime)){
				vo.setVisitTime(visitTime);
			}
			//查看时间差
			if(!"".equals(visitTime)){
				Date visitDateTime = DateUtil.StringToDate(visitTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
				vo.setVisitTime(visitTime);
				vo.setVisitDateTimeStr(DateUtil.getDateTimeGoneFormatStr(visitDateTime,lang,""));
			}
			//头像与名称
			String memberId = objs[1]==null?"":objs[1].toString();
			if(!"".equals(memberId)){
				MemberBase user = (MemberBase)this.baseDao.get(MemberBase.class, memberId);

				String visitorHeadImg = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
				vo.setIconUrl(visitorHeadImg);
				//vo.setIconUrl(user.getIconUrl());
				vo.setLoginCode(user.getLoginCode());
				vo.setNickName(user.getNickName());
				//如果是查看IFA观点的访问人，则筛选出该IFA的客户
				InsightInfo insight = (InsightInfo)this.baseDao.get(InsightInfo.class, insightId);
				MemberIfa ifa = memberBaseService.findIfaMember(insight.getCreator().getId());
				if("insight".equals(vistType)){
					List<CrmCustomer> cList = crmCustomerService.findCustomerByIfaId(ifa.getId());
					if(cList!=null&&!cList.isEmpty()){
						for(CrmCustomer each : cList){
							String cId = each.getMember().getId();
							if(cId.equals(memberId))voList.add(vo);
						}
					}
				}
				
			}
			
		}
		jsonPaging.getList().clear();
		jsonPaging.getList().addAll(voList);
		jsonPaging.setTotal(voList.size());
		// modify end
		
		return jsonPaging;
	}
	/**
	 * 保存/创建观点实例
	 * @author scshi
	 * @date 20160823
	 * @param insight 观点实例
	 * */
	public InsightInfo createOrUpdateInsight(InsightInfo insight) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String pubDateStr = insight.getPubDateStr();
		String overHeadTimeStr = insight.getOverheadTimeStr();
		String repostedTimeStr = insight.getRepostedTimeStr();
		
		if(null==insight.getId() || "".equals(insight.getId())){
			insight.setId(null);
			insight.setClick(0);
			insight.setUpCounter(0);
			insight.setDownCounter(0);
			insight.setCreateTime(new Date());
			//insight.setStatus(0);
		}else{
			String createTimeStr = insight.getCreateTimeStr();
			insight.setCreateTime("".equals(createTimeStr)?new Date():sdf.parse(createTimeStr));
		}
		insight.setPubDate(pubDateStr==""?null:sdf.parse(pubDateStr+" 00:00:00"));
		insight.setLastUpdate(new Date());
		insight.setOverheadTime("".equals(overHeadTimeStr)?null:sdf.parse(overHeadTimeStr));
		insight.setRepostedTime("".equals(repostedTimeStr)?null:sdf.parse(repostedTimeStr));
		
		//列表缩略图控制
		String thumbType = insight.getThumbType();
		if("1".equals(thumbType)){
			String content = insight.getContent();
			String firstImgSrc = getFirstImgSrc(content);
			if (StringUtils.isNotBlank(firstImgSrc)) {
				int startIndex = firstImgSrc.indexOf("/u/");//来自Um_editor的图片带有域名，按需截取
				if(startIndex>-1){//避免来自网络的图片不在/u/文件夹下
					firstImgSrc = firstImgSrc.substring(startIndex);
				}
				insight.setThumbnail(firstImgSrc);
			}
		}
		
		this.baseDao.saveOrUpdate(insight);
		
		return insight;
	}
	
	/**
	 * 删除观点
	 * @author scshi
	 * @date 20160823
	 * @param insightId 观点唯一标识
	 * */
	public void delInsightById(String insightId){
		//删除附件
		//InsightInfo info = (InsightInfo)this.baseDao.get(InsightInfo.class, insightId);
		
		String hql = "delete from InsightInfo t where t.id=? ";
		this.baseDao.updateHql(hql, new String[]{insightId});
	}
	
	/**
	 * 获取正文内容中的第一张图片的SRC
	 * @author scshi_u330p
	 * @date 20161025
	 * @param content 正文内容
	 * */
	private String getFirstImgSrc(String content){
		
		Pattern pImg = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher mImg = pImg.matcher(content);
        boolean resultImg = mImg.find();
        if (resultImg) {
           // while (resultImg) {
                //获取到匹配的<img />标签中的内容
                String strImg = mImg.group(2);
                //开始匹配<img />标签中的src
                Pattern pSrc = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher mSrc = pSrc.matcher(strImg);
                if (mSrc.find()) {
                    String strSrc = mSrc.group(3);
                    return strSrc;
                }
           // }
        }
		return null;
	}
	
	/**
	 * 置顶状态修改
	 * @author scshi
	 * @date insightId 观点主键
	 * @date head_status 观点置顶状态 1--置顶，0--不置顶
	 * */
	public void overHeadStatusChange(String insightId, String headStatus) {
		InsightInfo insight = (InsightInfo)this.baseDao.get(InsightInfo.class, insightId);
		if(null!=insight){
			insight.setOverhead(Integer.parseInt(headStatus));
			insight.setOverheadTime(new Date());
			this.baseDao.saveOrUpdate(insight);
		}
	}
	
	/**
	 * 保存观点
	 * @author wwluo
	 * @date 2016/11/22
	 * */
	public InsightInfo updateInsight(InsightInfo info) {
		return (InsightInfo) this.baseDao.saveOrUpdate(info);
	}
	
	/**
	 * 获取推送信息
	 * @author wwluo
	 * @date 2016/11/22
	 * @param id relateId
	 * @param module moduleType
	 * 
	 */
	@Override
	public WebPush getPushByRelateAndModule(String id, String module) {
		WebPush push = null;
		if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(module)){
			StringBuffer hql = new StringBuffer(" from WebPush where relateId=? and moduleType=?");
			List<Object> params = new ArrayList<Object>();
			params.add(id);
			params.add(module);
			List<WebPush> pushs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(pushs !=null && !pushs.isEmpty()){
				push = pushs.get(0);
			}
		}
		return push;
	}
	
	/**
	 * 获取查看权限
	 * @author wwluo
	 * @date 2016/11/22
	 * @param id relateId
	 * @param module moduleType
	 * 
	 */
	@Override
	public WebView getViewByRelateAndModule(String id, String module) {
		WebView view = null;
		if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(module)){
			StringBuffer hql = new StringBuffer(" from WebView where relateId=? and moduleType=?");
			List<Object> params = new ArrayList<Object>();
			params.add(id);
			params.add(module);
			List<WebView> views = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(views !=null && !views.isEmpty()){
				view = views.get(0);
			}
		}
		return view;
	}
	
	/**
	 * 获取观点的栏目
	 * @author mqzou
	 * @date 2016-12-27
	 * @param memberId
	 * @param keyWord
	 * @return
	 */
	@Override
	public List<String> findInsightChanel(String memberId) {
		List<String> list=new ArrayList<String>();
		StringBuilder hql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		hql.append(" from InsightInfo r where r.status='1'");
		if(null!=memberId && !"".equals(memberId)){
			hql.append(" and r.creator.id=?");
			params.add(memberId);
		}
		
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				InsightInfo info = (InsightInfo) it.next();
				if(null!=info.getChannel()&& !"".equals(info.getChannel())){
					String chanel=info.getChannel();
					String[] str=chanel.split(",");
					for (String string : str) {
						if(!list.contains(string.trim()))
							list.add(string.trim());
					}
				}
			}
		}
		return list;
	}
	/**
	 * 获取ifa的观点列表
	 * @author mqzou
	 * @date 2016-12-27
	 * @param memberId
	 * @param chanel
	 * @param keyWord
	 * @return
	 */
	@Override
	public JsonPaging findIfaInsightList(JsonPaging jsonPaging,String memberId, String chanel, String keyWord) {
		StringBuilder hql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		hql.append(" from InsightInfo r where r.status='1'");
		if(null!=memberId && !"".equals(memberId)){
			hql.append(" and r.creator.id=?");
			params.add(memberId);
		}
		if(null!=keyWord && !"".equals(keyWord)){
			hql.append(" and r.title like ? ");
			params.add("%"+keyWord+"%");
		}
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		
		return jsonPaging;
	}
	
	/**
	 * 通过memberId获取展示信息
	 * @param memberId
	 * @return
	 */
	@Override
	public IfaMessageVO getByMemberId(String memberId, String langCode) {
		IfaMessageVO message = new IfaMessageVO();
		String hql = " FROM MemberBase b "
			+ " LEFT JOIN MemberIfa a ON b.id=a.member.id "
			+ " LEFT JOIN MemberIfaIfafirm ff ON a.id=ff.ifa.id "
			+ " LEFT JOIN MemberIfafirm f ON ff.ifafirm.id=f.id "
			+ " LEFT JOIN " + this.getLangString("MemberIfafirm", langCode) + " l ON f.id=l.id "
			+ " WHERE 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(memberId)){
			hql += " AND b.id=? ";
			params.add(memberId);
			List<Object> list = this.baseDao.find(hql, params.toArray(), false);
			if(!list.isEmpty()){
				Object[] objRead = (Object[])list.get(0);
				
				if(null!=objRead[0]){
					MemberBase base = (MemberBase)objRead[0];
					message.setMemberId(base.getId());
					message.setNickName(base.getNickName());
					message.setEmail(base.getEmail());
					message.setMobileCode(base.getMobileCode());
					message.setMobileNumber(base.getMobileNumber());
					message.setIconUrl(base.getIconUrl());
				}
				if(null!=objRead[1]){
					MemberIfa ifa = (MemberIfa)objRead[1];
					message.setIfaId(ifa.getId());
					message.setGender(ifa.getGender());
					message.setPopularityTotal(ifa.getPopularityTotal());
					message.setInvestLifeBegin(ifa.getInvestLifeBegin());
					if(null!=ifa.getInvestLifeBegin()){
						Date fromDate = ifa.getInvestLifeBegin();
						Date nowDate = new Date();
						long fromTime = fromDate.getTime();
						long nowTime = nowDate.getTime();
						long time = (nowTime-fromTime)/1000;
						double year = time/(3600*24*365.0);
						Integer experience = (int)Math.round(year);
						message.setExperience(experience);
					}
				}
				/*if(null!=objRead[2]){
					MemberIfaIfafirm ff = (MemberIfaIfafirm)objRead[2];
				}*/
				if(null!=objRead[3]){
					MemberIfafirm firm = (MemberIfafirm)objRead[3];
					message.setFirmLogo(firm.getFirmLogo());
				}
				if("sc".equals(langCode)&&null!=objRead[4]){
					MemberIfafirmSc sc = (MemberIfafirmSc)objRead[4];
					message.setCompanyName(sc.getCompanyName());
				}else if("tc".equals(langCode)&&null!=objRead[4]){
					MemberIfafirmTc tc = (MemberIfafirmTc)objRead[4];
					message.setCompanyName(tc.getCompanyName());
				}else if("en".equals(langCode)&&null!=objRead[4]){
					MemberIfafirmEn en = (MemberIfafirmEn)objRead[4];
					message.setCompanyName(en.getCompanyName());
				}
			}
		}
		return message;
	}
	
	/**
	 * 查看权限详情
	 * @author wwluo
	 * @date 2016-12-27
	 * @return
	 */
	@Override
	public List<WebViewDetail> getViewDetailByView(String viewId) {
		List<WebViewDetail> webViewDetails = null;
		if(StringUtils.isNotBlank(viewId)){
			StringBuffer hql = new StringBuffer(" FROM WebViewDetail w WHERE w.view.id=? ");
			List<Object> params = new ArrayList<Object>();
			params.add(viewId);
			webViewDetails = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return webViewDetails;
	}
}
