package com.fsll.wmes.ifa.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.InsightCount;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebRecommended;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.ifa.service.InsightInfoService;
import com.fsll.wmes.ifa.vo.InsightInfoVo;
import com.fsll.wmes.ifa.vo.InsightItemVo;
import com.fsll.wmes.ifa.vo.InsightSearchParamVo;
import com.fsll.wmes.ifa.vo.InsightVistorVo;

/**
 * 观点接口
 * @author zpzhou
 * @date 2016-8-11
 */
@Service("insightInfoService")
//@Transactional
public class InsightInfoServiceImpl extends BaseService implements InsightInfoService {
	
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
	 * @author scshi
	 * @param jsonPaging 分页实体
	 * @param loginUserId 登陆用户
	 * @param searchParam 搜索条件vo
	 * 
	 * */
	public JsonPaging findIfaInsightListForWeb(JsonPaging jsonPaging,String loginUserId, InsightSearchParamVo searchParam){
		String hql = "from InsightInfo t ";
//		hql += "";
		hql += " where t.creator.id=? " ;
		List params = new ArrayList();
		params.add(loginUserId);
		if(null!=searchParam.getIssuedDate() && !"".equals(searchParam.getIssuedDate())){
			String issudeDate = searchParam.getIssuedDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			if("1Week".equals(issudeDate)){//过去7天发布的观点
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 7);  
			}
			
			if("1Month".equals(issudeDate)){
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
			}
			
			if("3Month".equals(issudeDate)){
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 3);
			}
			
			if("6Month".equals(issudeDate)){//过去6个月
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 6);
			}
			
			if("1Year".equals(issudeDate)){//过去一年
				cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
			}
			//System.out.println("issudeDateissudeDateissudeDateissudeDate===="+cal.getTime());
			hql += " and t.pubDate>=? ";
			params.add(cal.getTime());
		}
		
		if(null!=searchParam.getSector() && !"".equals(searchParam.getSector())){
			hql += " and t.sector like ? ";
			params.add("%"+searchParam.getSector()+"%");
		}
		
		if(null!=searchParam.getKeyWord() && !"".equals(searchParam.getKeyWord())){
			String keyWord = searchParam.getKeyWord();
			hql += " and (t.title like ? or t.keyword like ? or t.content like ? ) ";
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		
		if(null!=searchParam.getIssuedDateSort()&& !"".equals(searchParam.getIssuedDateSort())){//发布日期排序
			hql += " order by t.pubDate "+searchParam.getIssuedDateSort();
			
		}else if(null!=searchParam.getViewSort() && !"".equals(searchParam.getViewSort())){//点击次数排序
			hql += " order by t.click "+ searchParam.getViewSort();
		
		}else{//默认排序
			hql += " order by t.overhead desc,t.createTime desc ";
		}
		
		List<InsightInfo> list = this.baseDao.find(hql, params.toArray(), false);
//		for(int x=0;x<list.size();x++){
//			InsightInfo info = list.get(x);
//		}
		jsonPaging.setList(list);
		jsonPaging.setTotal(list.size());
		return jsonPaging;
	}
	
	/**
	 * 观点访客列表
	 * @author scshi
	 * @date 20160822
	 * @param insightId 观点唯一标识
	 * @param vistType	对应模块,ifa空间访客,insight观点访客,news新闻访客,strategy策略,portfolio_arena组合竞技场访客,portfolio_info组合访客
	 * */
	public List<InsightVistorVo> loadVistorList(String insightId,String vistType) {
		String hql = " select t.vistiTime,t.member.id,count(distinct t.member.id) ";
		hql +=" from WebInvestorVisit t where t.relateId=? and t.moduleType=? ";
		hql += " group by t.member.id ";
		hql += " order by t.vistiTime desc ";
		List params = new ArrayList();
		params.add(insightId);
		params.add(vistType);
		List list = this.baseDao.find(hql, params.toArray(), false);
		List<InsightVistorVo> voList = new ArrayList<InsightVistorVo>();
		if(list.isEmpty())return voList;
		for(int z=0;z<list.size();z++){
			InsightVistorVo vo = new InsightVistorVo();
			Object[] objs = (Object[])list.get(z);
			String visitTime = objs[0]==null?"":objs[0].toString();
			//查看时间差
			if(!"".equals(visitTime)){
				vo.setVisitTime(visitTime);
			}
			//头像与名称
			String memberId = objs[1]==null?"":objs[1].toString();
			if(!"".equals(memberId)){
				MemberBase user = (MemberBase)this.baseDao.get(MemberBase.class, memberId);
				vo.setIconUrl(user.getIconUrl());
				vo.setLoginCode(user.getLoginCode());
			}
			voList.add(vo);
		}
		return voList;
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
			insight.setCreateTime(new Date());
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
			String firstImgSrc = this.getFirstImgSrc(content);
			insight.setThumbnail(firstImgSrc);
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
//            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String strImg = mImg.group(2);
                //开始匹配<img />标签中的src
                Pattern pSrc = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher mSrc = pSrc.matcher(strImg);
                if (mSrc.find()) {
                    String strSrc = mSrc.group(3);
                    return strSrc;
                }
//            }
        }
		return "";
	}
	
}
