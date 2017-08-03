package com.fsll.app.investor.me.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.app.investor.me.service.AppNewsInfoService;
import com.fsll.app.investor.me.vo.AppInsightItemVo;
import com.fsll.app.investor.me.vo.AppNewsInfoItemVO;
import com.fsll.app.investor.me.vo.AppNewsItemVo;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.entity.NewsInfoCategory;
import com.fsll.wmes.entity.NewsInfoDoc;

/**
 * 新闻接口
 * @author zxtan
 * @date 2017-01-17
 */
@Service("appMyIfaNewsInfoService")
//@Transactional
public class AppNewsInfoServiceImpl extends BaseService implements AppNewsInfoService {
	
	/**
	 * 【我的投资顾问】得到新闻列表信息
	 * @author zxtan
	 * @date 2017-01-17
	 * @param memberId 用户ID
	 * @param ifaMemberId IfaMemberId
	 * @param langCode 语言
	 * @return
	 */
	public List<AppNewsInfoItemVO> findMyIfaNewsList(String memberId,String ifaMemberId,String langCode){
		List<AppNewsInfoItemVO> voList = new ArrayList<AppNewsInfoItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from NewsInfo i ");
		hql.append(" inner join CommunityTopic t on  t.sourceId=i.id ");
		hql.append(" left join NewsInfoCategory n on n.newsInfo.id=i.id ");//关联新闻栏目
		hql.append(" where t.status=1 and t.creator.id=? ");		
		hql.append("   and ( t.visitor = 'all' or ( t.visitor = 'friend' and exists( select f.id from WebFriend f where f.checkResult='1' and f.toMember.id=t.creator.id and f.fromMember.id=? )) ");
		hql.append(" 		or ( t.visitor = 'client' and exists( select c.id from CrmCustomer c inner join MemberIfa i on i.id=c.ifa.id where i.member.id=t.creator.id and c.member.id=? ))) ");
		
		hql.append(" order by t.createTime desc ");
		List params = new ArrayList();
		params.add(ifaMemberId);
		params.add(memberId);
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
//		List<AppNewsInfoItemVO> iList=new ArrayList<AppNewsInfoItemVO>();
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			NewsInfo info=(NewsInfo)objs[0];
			//NewsCount count=(NewsCount)objs[1];
			NewsInfoCategory category=(NewsInfoCategory)objs[2];
			
			AppNewsInfoItemVO vo = new AppNewsInfoItemVO();
			
			vo.setId(info.getId());
			vo.setRegionType(info.getRegionType());
			vo.setSectionType(info.getSectionType());
			vo.setFlag(info.getFlag());
			vo.setTitle(info.getTitle());
			vo.setWriter(info.getWriter());
			vo.setSource(info.getSource());
			vo.setLitPic(info.getLitPic());
			vo.setPubDate(DateUtil.dateToDateString(info.getPubDate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setSendDate(DateUtil.dateToDateString(info.getSendDate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setKeywords(info.getKeywords());
			vo.setDescription(info.getDescription());
			vo.setClick(String.valueOf(info.getClick()));
			vo.setGoodPost(String.valueOf(info.getGoodPost()));
			vo.setBadPost(String.valueOf(info.getBadPost()));	
			
			if(null!=category && null!=category.getCategory()){
				String categoryName = category.getCategory().getNameSc();
				if("en".equalsIgnoreCase(langCode)){
					categoryName = category.getCategory().getNameEn();
				}else if("tc".equalsIgnoreCase(langCode)){
					categoryName = category.getCategory().getNameTc();
				}
				vo.setCategoryName(categoryName);//得到栏目名称
			}
			
			voList.add(vo);
    	}
		
		return voList;
	}


	public NewsInfo findNewsInfoById(String id) {
		NewsInfo vo=(NewsInfo)this.baseDao.get(NewsInfo.class, id);
		return vo;
	}
	
	/**
	 * 得到新闻列表信息
	 * @author zxtan
	 * @date 2017-03-10
	 * @param id 新闻Id
	 * @return
	 */
	public AppNewsInfoItemVO findNewsInfo(String id){
		AppNewsInfoItemVO vo=new AppNewsInfoItemVO();
		StringBuilder hql = new StringBuilder();
		hql.append(" from NewsInfo m ");
		hql.append(" inner join NewsInfoDoc r on r.id=m.id ");
		hql.append(" where m.id=? ");
		List params = new ArrayList();
		params.add(id);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty())	{
			Object[] objs = (Object[])list.get(0);
			NewsInfo info=(NewsInfo)objs[0];
			NewsInfoDoc doc = (NewsInfoDoc) objs[1];
			
			vo.setId(info.getId());
			vo.setRegionType(info.getRegionType());
			vo.setSectionType(info.getSectionType());
			vo.setFlag(info.getFlag());
			vo.setTitle(info.getTitle());
			vo.setWriter(info.getWriter());
			vo.setSource(info.getSource());
			vo.setLitPic(info.getLitPic());
			vo.setPubDate(DateUtil.dateToDateString(info.getPubDate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setSendDate(DateUtil.dateToDateString(info.getSendDate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setKeywords(info.getKeywords());
			vo.setDescription(info.getDescription());
			vo.setClick(String.valueOf(info.getClick()));
			vo.setGoodPost(String.valueOf(info.getGoodPost()));
			vo.setBadPost(String.valueOf(info.getBadPost()));			
			vo.setBody(doc.getBody());
    	}
		
		return vo;
	}
}
