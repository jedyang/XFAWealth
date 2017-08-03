package com.fsll.wmes.news.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sound.midi.MidiDevice.Info;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.exception.commonException;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.NewsBehavior;
import com.fsll.wmes.entity.NewsCategory;
import com.fsll.wmes.entity.NewsComment;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.entity.NewsInfoCategory;
import com.fsll.wmes.entity.NewsInfoDoc;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.news.service.NewsInfoService;
import com.fsll.wmes.news.vo.NewsCategoryVO;
import com.fsll.wmes.news.vo.NewsCommentVO;
import com.fsll.wmes.news.vo.NewsInfoForListVO;
import com.fsll.wmes.news.vo.NewsInfoVO;
import com.fsll.wmes.news.vo.NewsSimpleInfoVO;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

@Service("newsInfoService")
public class NewsInfoServiceImpl extends BaseService implements NewsInfoService {

	@Autowired
	private MemberBaseService memberBaseService;
	
	
	/**
	 * 获取首页幻灯片新闻
	 * 
	 * @author mqzou 2017-03-06
	 * @return
	 */
	@Override
	public List<NewsSimpleInfoVO> getTopNews(String langCode) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from NewsInfo i left join NewsInfoDoc d on i.id=d.id  where i.langCode=? and i.flag like '%h%'");
		hql.append(" order by i.pubDate desc");
		List<Object> params = new ArrayList<Object>();
		params.add(langCode);
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setRows(5);
		jsonPaging.setPage(1);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<NewsSimpleInfoVO> list = new ArrayList<NewsSimpleInfoVO>();
		if (null != jsonPaging.getList() && !jsonPaging.getList().isEmpty()) {
			Iterator it = jsonPaging.getList().iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				NewsInfo newsInfo = (NewsInfo) objects[0];
				NewsInfoDoc doc = (NewsInfoDoc) objects[1];
				if (null != newsInfo && null != doc) {
					NewsSimpleInfoVO vo = new NewsSimpleInfoVO();
					vo.setId(newsInfo.getId());
					vo.setTitle(newsInfo.getTitle());
					vo.setDescription(newsInfo.getDescription());
					String content = doc.getBody();
					String url = StrUtils.matchOne(content, "img", "src");
					vo.setUrl(url);
					list.add(vo);

				}

			}
		}
		return list;
	}

	/**
	 * 根据栏目获取新闻列表
	 * 
	 * @author mqzou 2017-03-06
	 * @param flag
	 * @param langCode
	 * @return
	 */
	@Override
	public JsonPaging findNewsByCatagoryId(JsonPaging jsonPaging, String catogory, String langCode,String dateFormat) {
		

		StringBuilder hql = new StringBuilder();
		hql.append(" select i,count(c) from NewsInfoCategory n  left join NewsInfo i on n.newsInfo.id=i.id ");
		hql.append(" left join NewsComment c on i.id=c.newsInfo.id and c.checked='1' ");
		hql.append(" left join NewsCategory a on n.category.id=a.id ");
		hql.append(" where ( a.id=? or a.parent.id=?)  and i.langCode=?   and n is not null");
		hql.append(" group by i");
		//hql.append(" order by case when i.flag like '%h%' then CURRENT_TIME() else i.pubDate end desc");
		hql.append(" order by i.pubDate desc");
		List<Object> params = new ArrayList<Object>();
		params.add(catogory);
		params.add(catogory);
		params.add(langCode);
		jsonPaging.setOrder("");
		jsonPaging.setSort("");
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<NewsInfoForListVO> list = new ArrayList<NewsInfoForListVO>();
		if (null != jsonPaging.getList()) {
			Iterator it = jsonPaging.getList().iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				// NewsInfo info=(NewsInfo)it.next();
				NewsInfo info = (NewsInfo) objects[0];
				if (null == info)
					continue;
				Object commentCount = objects[1];
				NewsInfoForListVO vo = new NewsInfoForListVO();
				vo.setId(info.getId());
				vo.setDescription(info.getDescription());
				vo.setGoodPost(null != info.getGoodPost() ? String.valueOf(info.getGoodPost()) : "0");
				vo.setLastUpdate(DateUtil.getDateTimeGoneFormatStr(info.getPubDate(), langCode, dateFormat));
				vo.setTitle(info.getTitle());
				vo.setUrl(info.getLitPic());
				vo.setCommentCount(null != commentCount ? String.valueOf(commentCount) : "0");
				vo.setClick(null != info.getClick() ? String.valueOf(info.getClick()) : "0");
				list.add(vo);
			}
		}
		hql=new StringBuilder();
		
		hql.append(" select count(*) from NewsInfoCategory n  left join NewsInfo i on n.newsInfo.id=i.id ");
		hql.append(" left join NewsCategory a on n.category.id=a.id ");
		hql.append(" where ( a.id=? or a.parent.id=?)  and i.langCode=?   and n is not null");
		
		List countList=baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=countList && !countList.isEmpty()){
			Object object=countList.get(0);
			if(null!=object && null!=object.toString()){
				jsonPaging.setTotal(Integer.valueOf(object.toString()));
			}
		}else {
			jsonPaging.setTotal(0);
		}
		
		
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 根据栏目编码获取新闻列表
	 * 
	 * @author mqzou 2017-03-06
	 * @param flag
	 * @param langCode
	 * @return
	 */
	@Override
	public List<NewsInfoForListVO> findNewsByCatagoryCode(String code, String langCode, int rows,String dateFormat) {

		String sql = "CALL pro_getnewsforcategorycode(?,?,?);";
		List<Object> params = new ArrayList<Object>();
		params.add(code);
		params.add(langCode);
		params.add(rows);
		List resultList = springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		List<NewsInfoForListVO> list = new ArrayList<NewsInfoForListVO>();
		if (null != resultList) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				HashMap<String, Object> map = (HashMap<String, Object>) it.next();
				Object id = getMapValue(map, "id");
				Object title = getMapValue(map, "title");
				Object url = getMapValue(map, "lit_pic");
				Object description = getMapValue(map, "description");
				Object lastUpdate = getMapValue(map, "pub_date");
				Object count = getMapValue(map, "commentCount");
				Object click = getMapValue(map, "click");
				Object goodPost = getMapValue(map, "good_post");
  
				url=PageHelper.getNetImgUrl(null!=url?url.toString():"");
				
				NewsInfoForListVO vo = new NewsInfoForListVO();
				vo.setId(id.toString());
				vo.setDescription(null != description ? description.toString() : "");
				vo.setGoodPost(null != goodPost ? String.valueOf(goodPost) : "0");
				vo.setLastUpdate(DateUtil.getDateTimeGoneFormatStr(DateUtil.StringToDate(lastUpdate.toString(), DateUtil.DEFAULT_DATE_TIME_FORMAT), langCode, dateFormat));
				vo.setTitle(null != title ? title.toString() : "");
				vo.setUrl(null != url ? url.toString() : "");
				vo.setCommentCount(null != count ? String.valueOf(count) : "0");
				vo.setClick(null != click ? String.valueOf(click) : "0");
				list.add(vo);
			}
		}

		/*
		 * StringBuilder hql=new StringBuilder();
		 * hql.append(" select i,count(c)  "); hql.append(
		 * " from NewsInfoCategory n  left join NewsInfo i on n.newsInfo.id=i.id "
		 * ); hql.append(
		 * " left join NewsComment c on i.id=c.newsInfo.id and c.checked='1' ");
		 * hql.append(" left join NewsCategory g on n.category.id=g.id ");
		 * hql.append(" where  g.code=? and i.langCode=?  ");
		 * hql.append(" group by i"); hql.append(
		 * " order by case when i.flag like '%f%' then CURRENT_TIME() else i.xfaLastModifyDate  end desc "
		 * ); List<Object> params=new ArrayList<Object>(); params.add(code);
		 * params.add(langCode); JsonPaging jsonPaging=new JsonPaging();
		 * jsonPaging.setPage(1); jsonPaging.setRows(rows);
		 * jsonPaging.setOrder(""); jsonPaging.setSort(""); jsonPaging
		 * =this.baseDao.selectJsonPaging(hql.toString(), params.toArray(),
		 * jsonPaging, false); List resultList=jsonPaging.getList();
		 * List<NewsInfoForListVO> list=new ArrayList<NewsInfoForListVO>();
		 * if(null!=resultList){ Iterator it=resultList.iterator(); while
		 * (it.hasNext()) { Object[] objects=(Object[])it.next(); NewsInfo
		 * info=(NewsInfo)objects[0]; if(null==info) continue; Object
		 * commentCount=objects[1]; NewsInfoForListVO vo=new
		 * NewsInfoForListVO(); vo.setId(info.getId());
		 * vo.setDescription(info.getDescription());
		 * vo.setGoodPost(null!=info.getGoodPost
		 * ()?String.valueOf(info.getGoodPost()):"0");
		 * vo.setLastUpdate(DateUtil.
		 * dateToDateString(info.getXfaLastModifyDate(),
		 * DateUtil.DEFAULT_DATE_TIME_FORMAT)); vo.setTitle(info.getTitle());
		 * vo.setUrl(info.getLitPic());
		 * vo.setCommentCount(null!=commentCount?String
		 * .valueOf(commentCount):"0");
		 * vo.setClick(null!=info.getClick()?String.
		 * valueOf(info.getClick()):"0"); list.add(vo); } }
		 */
		// jsonPaging.setList(list);
		return list;
	}

	/**
	 * 获取新闻的详细内容
	 * 
	 * @author mqzou 2017-03-06
	 * @param id
	 * @return
	 */
	@Override
	public NewsInfoVO findNewsInfoVO(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from NewsInfo i left join NewsInfoDoc d on i.id=d.id  where i.id=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			Object[] objects = (Object[]) list.get(0);
			NewsInfo newsInfo = (NewsInfo) objects[0];
			NewsInfoDoc doc = (NewsInfoDoc) objects[1];
			NewsInfoVO vo = new NewsInfoVO();

			vo.setId(newsInfo.getId());
			vo.setDescription(newsInfo.getDescription());
			vo.setBody(null != doc ? doc.getBody() : "");
			vo.setKeyWords(newsInfo.getKeywords());
			vo.setShortTitle(newsInfo.getShortTitle());
			vo.setTitle(newsInfo.getTitle());
			vo.setLastUpdate(DateUtil.dateToDateString(newsInfo.getPubDate(), DateUtil.DEFAULT_DATE_TIME_FORMAT));
			vo.setUrl(newsInfo.getLitPic());
			vo.setSectionType(newsInfo.getSectionType());
			return vo;
		}
		return null;
	}

	/**
	 * 根据属性获取文章列表
	 * 
	 * @author mqzou 2017-03-06
	 * @param jsonPaging
	 * @param flag
	 * @param langCode
	 * @return
	 */
	@Override
	public JsonPaging findNewsByFlag(JsonPaging jsonPaging, String flag, String langCode) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select i,count(c) from NewsInfo i left join NewsComment c on i.id= where i.langCode=? and c.checked='1' ");
		List<Object> params = new ArrayList<Object>();
		params.add(langCode);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<NewsInfoForListVO> list = new ArrayList<NewsInfoForListVO>();
		if (null != jsonPaging.getList()) {
			Iterator it = jsonPaging.getList().iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				// NewsInfo info=(NewsInfo)it.next();
				NewsInfo info = (NewsInfo) objects[0];
				Object commentCount = objects[1];
				NewsInfoForListVO vo = new NewsInfoForListVO();
				vo.setId(info.getId());
				vo.setDescription(info.getDescription());
				vo.setGoodPost(null != info.getGoodPost() ? String.valueOf(info.getGoodPost()) : "0");
				vo.setLastUpdate(DateUtil.dateToDateString(info.getPubDate(), DateUtil.DEFAULT_DATE_TIME_FORMAT));
				vo.setTitle(info.getTitle());
				vo.setUrl(info.getLitPic());
				vo.setCommentCount(null != commentCount ? String.valueOf(commentCount) : "0");
				vo.setClick(null != info.getClick() ? String.valueOf(info.getClick()) : "0");
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 根据id获取栏目
	 * 
	 * @author mqzou 2017-03-06
	 * @param id
	 * @return
	 */
	@Override
	public NewsCategoryVO findCategoryById(String id, String langCode) {
		NewsCategory category = (NewsCategory) this.baseDao.get(NewsCategory.class, id);
		if (null != category) {
			NewsCategoryVO vo = new NewsCategoryVO();
			vo.setCode(category.getCode());
			vo.setId(category.getId());
			if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
				vo.setName(category.getNameEn());
			} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
				vo.setName(category.getNameSc());
			} else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
				vo.setName(category.getNameTc());
			}
			vo.setParent(getParentCategory(new ArrayList<String>(), category));
			return vo;
			
		}
		return null;
	}

	/**
	 * 根据编码code获取栏目
	 * 
	 * @author mqzou 2017-03-06
	 * @param id
	 * @return
	 */
	@Override
	public NewsCategoryVO findCategoryByCode(String code, String langCode) {
		String hql = " from NewsCategory r where r.code=?";
		List<Object> params = new ArrayList<Object>();
		params.add(code);
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			NewsCategory category = (NewsCategory) list.get(0);
			if (null != category) {
				NewsCategoryVO vo = new NewsCategoryVO();
				vo.setCode(category.getCode());
				vo.setId(category.getId());
				if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
					vo.setName(category.getNameEn());
				} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					vo.setName(category.getNameSc());
				} else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					vo.setName(category.getNameTc());
				}
				return vo;
			}
		}
		return null;
	}

	/**
	 * 获取编辑推荐的新闻列表
	 * 
	 * @author mqzou 2017-03-06
	 * @param jsoPaging
	 * @param langCode
	 * @return
	 */
	@Override
	public JsonPaging findRecommendNews(JsonPaging jsonPaging, String langCode,String dateFormat) {
		String hql = " from NewsInfo r where r.flag like '%c%' and r.langCode='" + langCode + "'";
		hql+=" order by r.pubDate desc";
		jsonPaging.setOrder("");
		jsonPaging.setSort("");
		jsonPaging = this.baseDao.selectJsonPaging(hql, null, jsonPaging, false);
		List<NewsSimpleInfoVO> list = new ArrayList<NewsSimpleInfoVO>();
		if (null != jsonPaging && null != jsonPaging.getList()) {
			Iterator it = jsonPaging.getList().iterator();
			while (it.hasNext()) {
				NewsInfo info = (NewsInfo) it.next();
				NewsSimpleInfoVO vo = new NewsSimpleInfoVO();
				vo.setId(info.getId());
				vo.setTitle(info.getTitle());
				vo.setDescription(info.getDescription());
				vo.setUrl(info.getLitPic());
				vo.setLastUpdate(DateUtil.dateToDateString(info.getPubDate(), dateFormat));
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 获取最热门的新闻列表
	 * 
	 * @author mqzou 2017-03-06
	 * @param jsonPaging
	 * @param langCode
	 * @return
	 */
	@Override
	public JsonPaging findHotNews(JsonPaging jsonPaging, String langCode,String dateFormat) {
	//	String hql = " from NewsInfo r where r.langCode='" + langCode + "'";
		
		StringBuilder hql=new StringBuilder();
		hql.append(" select r from NewsInfo r left join NewsBehavior b on r.id=b.targetId and b.createTime>? and b.behaviorType='read'");
	    hql.append(" where r.langCode='" + langCode + "'");
	    hql.append(" group by r.id");
	    hql.append(" order by count(b) desc");
		jsonPaging.setOrder(null);
		jsonPaging.setSort(null);
		List params=new ArrayList();
		params.add(DateUtil.getCurDate(Calendar.DATE, -3));
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<NewsSimpleInfoVO> list = new ArrayList<NewsSimpleInfoVO>();
		if (null != jsonPaging && null != jsonPaging.getList()) {
			Iterator it = jsonPaging.getList().iterator();
			while (it.hasNext()) {
				NewsInfo info = (NewsInfo) it.next();
				NewsSimpleInfoVO vo = new NewsSimpleInfoVO();
				vo.setId(info.getId());
				vo.setTitle(info.getTitle());
				vo.setDescription(info.getDescription());
				vo.setUrl(info.getLitPic());
				vo.setLastUpdate(DateUtil.dateToDateString(info.getPubDate(), dateFormat));
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 获取最新的新闻列表
	 * 
	 * @author mqzou 2017-03-06
	 * @param jsonPaging
	 * @param langCode
	 * @return
	 */
	@Override
	public JsonPaging findRecentNews(JsonPaging jsonPaging, String langCode,String dateFormat) {
		String hql = " from NewsInfo r  where r.langCode='" + langCode + "'";
		jsonPaging.setOrder(" desc");
		jsonPaging.setSort(" r.pubDate");
		jsonPaging = this.baseDao.selectJsonPaging(hql, null, jsonPaging, false);
		List<NewsSimpleInfoVO> list = new ArrayList<NewsSimpleInfoVO>();
		if (null != jsonPaging && null != jsonPaging.getList()) {
			Iterator it = jsonPaging.getList().iterator();
			while (it.hasNext()) {
				NewsInfo info = (NewsInfo) it.next();
				NewsSimpleInfoVO vo = new NewsSimpleInfoVO();
				vo.setId(info.getId());
				vo.setTitle(info.getTitle());
				vo.setDescription(info.getDescription());
				vo.setUrl(info.getLitPic());
				vo.setLastUpdate(DateUtil.dateToDateString(info.getPubDate(), dateFormat));
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 获取map的值
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	/*
	 * private Object getMapValue(HashMap map,String key){ if(null!=map &&
	 * map.containsKey(key)){ Object object=map.get(key); return object; }
	 * return null; }
	 */

	/**
	 * 获取栏目列表
	 * 
	 * @author mqzou 2017-03-07
	 * @param langCode
	 * @return
	 */
	@Override
	public List<NewsCategoryVO> findNewsCategoryList(String langCode) {
		String hql = " from NewsCategory r  order by orderBy";
		List resultList = baseDao.find(hql, null, false);
		List<NewsCategoryVO> list = new ArrayList<NewsCategoryVO>();
		List<NewsCategoryVO> childList = new ArrayList<NewsCategoryVO>();
		if (null != resultList && !resultList.isEmpty()) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				NewsCategory category = (NewsCategory) it.next();
				NewsCategoryVO vo = new NewsCategoryVO();
				vo.setCode(category.getCode());
				vo.setId(category.getId());
				if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
					vo.setName(category.getNameEn());
				} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					vo.setName(category.getNameSc());
				} else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					vo.setName(category.getNameTc());
				}
				vo.setParentId(null != category.getParent() ? category.getParent().getId().toString() : "");
				if ("".equals(vo.getParentId())) {
					list.add(vo);
				} else {
					childList.add(vo);
				}
			}
		}

		for (NewsCategoryVO vo : list) {
			vo = appendChild(vo, childList);
		}

		// list=getTreeList(new ArrayList<NewsCategoryVO>() , list,"");

		return list;
	}

	/**
	 * 递归获取栏目的树形结构
	 * 
	 * @author mqzou 2017-03-07
	 * @param vo
	 * @param list
	 * @return
	 */
	private NewsCategoryVO appendChild(NewsCategoryVO vo, List<NewsCategoryVO> list) {
		if (null != list && !list.isEmpty()) {
			List<NewsCategoryVO> childList = new ArrayList<NewsCategoryVO>();//vo.getChildren();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				NewsCategoryVO category = (NewsCategoryVO) it.next();
				if (category.getParentId().equals(vo.getId())) {
					category = appendChild(category, list);
					childList.add(category);
					it.remove();
				}
			}
			vo.setChildren(childList);
		}
		return vo;
	}

	/**
	 * 根据新闻获取新闻所属的栏目
	 * @author mqzou 2017-03-07
	 * @param infoId
	 * @return
	 */
	@Override
	public NewsCategoryVO findCategoryByNews(String infoId,String langCode) {
		String hql=" from NewsInfoCategory r where r.newsInfo.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(infoId);
		List list=baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			NewsInfoCategory infoCategory=(NewsInfoCategory)list.get(0);
			NewsCategory category=infoCategory.getCategory();
			NewsCategoryVO vo=new NewsCategoryVO();
			vo.setCode(category.getCode());
			vo.setId(category.getId());
			if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
				vo.setName(category.getNameEn());
			} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
				vo.setName(category.getNameSc());
			} else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
				vo.setName(category.getNameTc());
			}
			vo.setParent(getParentCategory(new ArrayList<String>(), category));
			return vo;
		}
		return null;
	}
	
	/**
	 * 获取栏目的所有父节点
	 * @param list
	 * @param vo
	 * @return
	 */
	private List<String> getParentCategory(List<String> list,NewsCategory vo){
		if(null==list)
			list=new ArrayList<String>();
		if(null!=vo.getParent()){
			list.add(0, vo.getParent().getCode());
			getParentCategory(list, vo.getParent());
		}
		return list;
	}
	
	/**
	 * 获取头条新闻列表
	 * @author mqzou 2017-03-07
	 * @param langCode
	 * @return
	 */
	@Override
	public JsonPaging findHeadlineNews(JsonPaging jsonPaging, String langCode,String dateFormat) {
		String hql=" from NewsInfo r where r.flag like '%h%' and r.langCode='"+langCode+"'";
		hql+=" order by r.pubDate desc";
		jsonPaging.setOrder("");
		jsonPaging.setSort("");
		jsonPaging=baseDao.selectJsonPaging(hql, null, jsonPaging, false);
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			List resultList=jsonPaging.getList();
			List<NewsInfoForListVO> list=new ArrayList<NewsInfoForListVO>();
			if(null!=resultList && !resultList.isEmpty()){
				Iterator it=resultList.iterator();
				while (it.hasNext()) {
					NewsInfo info = (NewsInfo) it.next();
					NewsInfoForListVO vo=new NewsInfoForListVO();
					vo.setId(info.getId());
					vo.setDescription(info.getDescription());
					vo.setGoodPost(null != info.getGoodPost() ? String.valueOf(info.getGoodPost()) : "0");
					vo.setLastUpdate(DateUtil.dateToDateString(info.getPubDate(),dateFormat));
					vo.setTitle(info.getTitle());
					vo.setUrl(info.getLitPic());
					vo.setClick(null != info.getClick() ? String.valueOf(info.getClick()) : "0");
					list.add(vo);
				}
			}
			jsonPaging.setList(list);
		}
		
		
		return jsonPaging;
	}

	/**
	 * 获取某个新闻相近的推荐新闻
	 * @author mqzou 2017-03-08
	 * @param infoId
	 * @return
	 */
	@Override
	public List<NewsInfoForListVO> findListForProbably(String infoId, int rows,String langCode) {
		String date="";
		NewsInfo newsInfo=findNewsInfo(infoId);
		if(null!=newsInfo){
			date=DateUtil.dateToDateString(newsInfo.getPubDate(), DateUtil.DEFAULT_DATE_FORMAT)+" 59:59:59";
		}
		StringBuilder hql=new StringBuilder();
		hql.append(" from NewsInfo r where r.pubDate < ?");
		hql.append(" and r.id !=? and r.langCode=?");
		hql.append(" order by r.pubDate desc");
		List<Object> params=new ArrayList<Object>();
		params.add(DateUtil.StringToDate(date, DateUtil.DEFAULT_DATE_TIME_FORMAT));
		params.add(infoId);
		params.add(langCode);
		JsonPaging jsonPaging=new JsonPaging();
		jsonPaging.setOrder("");
		jsonPaging.setRows(rows);
		jsonPaging.setPage(1);
		jsonPaging.setSort("");
		jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<NewsInfoForListVO> list=new ArrayList<NewsInfoForListVO>();
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				NewsInfo info = (NewsInfo) it.next();
				NewsInfoForListVO vo=new NewsInfoForListVO();
				vo.setId(info.getId());
				vo.setDescription(info.getDescription());
				vo.setGoodPost(null != info.getGoodPost() ? String.valueOf(info.getGoodPost()) : "0");
				vo.setLastUpdate(DateUtil.dateToDateString(info.getPubDate(), DateUtil.DEFAULT_DATE_TIME_FORMAT));
				vo.setTitle(info.getTitle());
				vo.setUrl(info.getLitPic());
				vo.setClick(null != info.getClick() ? String.valueOf(info.getClick()) : "0");
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 获取新闻实体
	 * @author mqzou 2017-03-08
	 * @param id
	 * @return
	 */
	@Override
	public NewsInfo findNewsInfo(String id) {
		NewsInfo info=(NewsInfo)baseDao.get(NewsInfo.class, id);
		return info;
	}
	
	
	/**
	 * 获取新闻的评论列表
	 *  @author mqzou 2017-03-09
	 * @param jsonPaging
	 * @param infoId
	 * @return
	 */
	@Override
	public JsonPaging findNewsComment(JsonPaging jsonPaging, String infoId,String langCode,MemberBase member) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from NewsComment r where r.newsInfo.id=?  and r.checked='1'");
		hql.append(" and r.replyMember is null  and r.replyComment is null and (r.FType is null or r.FType=? ) and r.status='1'");
		jsonPaging.setOrder("desc");
		jsonPaging.setSort(" r.opTime");
		
		List<Object> params=new ArrayList<Object>();
		params.add(infoId);
		params.add(CommonConstantsWeb.NEWS_COMMENT_TYPE_FEEDBACK);
		jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<NewsCommentVO> list=new ArrayList<NewsCommentVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				NewsComment comment = (NewsComment) it.next();
				NewsCommentVO vo=new NewsCommentVO();
				vo.setId(comment.getId());
				vo.setComment(comment.getComment());
				vo.setGood(null!=comment.getGood()?comment.getGood():0);
				vo.setBad(null!=comment.getBad()?comment.getBad():0);
				vo.setInfoId(comment.getNewsInfo().getId());
				vo.setMemberId(comment.getMember().getId());
				vo.setMemberName(comment.getMember().getNickName());
				vo.setOpTime(DateUtil.getDateTimeGoneFormatStr(comment.getOpTime(), langCode, ""));
				vo.setReplyCount(null!=comment.getTotalPost()?comment.getTotalPost():0);
				String ifLike=checkNewsUpOrDown(vo.getId(), member);
				//vo.setReplyCount(findCommentReplyCount(vo.getId()));
				String gender="";
				if(comment.getMember().getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA){
					MemberIfa ifa=memberBaseService.findIfaMember(vo.getMemberId());
					gender=ifa.getGender();
				}else if (comment.getMember().getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL) {
					MemberIndividual individual=memberBaseService.findIndividualMember(vo.getMemberId());
					gender=individual.getGender();
				}
				vo.setMemberUrl(PageHelper.getUserHeadUrl(comment.getMember().getIconUrl(), gender));
				vo=findCommentReply(vo, langCode,member);
				if(ifLike.equals(CommonConstantsWeb.NEWS_BEHAVIOR_LIKE)){
					vo.setIsLike("1");
				}else if (ifLike.equals(CommonConstantsWeb.NEWS_BEHAVIOR_UNLIKE)) {
					vo.setIsUnlike("1");
				}
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 保存新闻评论信息
	 * @author mqzou 2017-03-09
	 * @param comment
	 * @return
	 */
	@Override
	public NewsComment saveComment(NewsComment comment) {
		comment=(NewsComment)baseDao.saveOrUpdate(comment);//添加评论
		//如果是评论的评论，则更新被评论的评论数
		if(null!=comment.getReplyComment()){
			updateCommentCount(comment.getReplyComment());
		}
		//更新新闻的被评论数
		NewsInfo info=comment.getNewsInfo();
		int count=null!=info.getTotalPost()?info.getTotalPost():0;
		info.setTotalPost(count+1);
		return comment;
	}
	
	/**
	 * 递归更新新闻评论的被评论数
	 * @param comment
	 */
	private void updateCommentCount(NewsComment comment){
		int count=null!=comment.getTotalPost()?comment.getTotalPost():0;
		comment.setTotalPost(count+1);
		baseDao.saveOrUpdate(comment);
		if(null!=comment.getReplyComment()){
			updateCommentCount(comment.getReplyComment());
		}
	}
	
	
	
	
	
	/**
	 * 获取评论实体
	 *  @author mqzou 2017-03-09
	 * @param id
	 * @return
	 */
	@Override
	public NewsComment findNewsComment(String id) {
		NewsComment comment=(NewsComment)baseDao.get(NewsComment.class, id);
		return comment;
	}
/*	*//**
	 * 获取评论的回复数量
	 * @author mqzou 2017-03-09
	 * @param commentId
	 * @return
	 *//*
	private int findCommentReplyCount(String commentId){
		String hql="select count(*) from NewsComment r where r.replyComment.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(hql);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object object=list.get(0);
			if(null!=object && !"".equals(object.toString())){
			    return Integer.parseInt(object.toString());
			}
		}
		return 0;
	}*/
	
	/**
	 * 获取评论的所有回复评论
	 * @author mqzou 2017-03-09
	 */
	private NewsCommentVO findCommentReply(NewsCommentVO commentVO,String langCode,MemberBase memberBase){
		if(null!=commentVO){
			String sql="CALL `pro_getnewscommentreply`(?,?)";
			List<Object> params=new ArrayList<Object>();
			params.add(commentVO.getId());
			params.add(memberBase.getId());
			List resultList=springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
			List<NewsCommentVO> list=new ArrayList<NewsCommentVO>();
			if(null!=resultList && !resultList.isEmpty()){
				Iterator it=resultList.iterator();
				while (it.hasNext()) {
					HashMap<String, Object> map = (HashMap<String, Object>) it.next();
					NewsCommentVO vo=new NewsCommentVO();
					String id=getMapValue(map, "id");
					String comment=getMapValue(map, "comment");
					String good=getMapValue(map, "good");
					String bad=getMapValue(map, "bad");
					String infoId=getMapValue(map, "info_id");
					String memberId=getMapValue(map, "member_id");
					String  opTime=getMapValue(map, "op_time");
					String  replyId=getMapValue(map, "reply_comment_id");
					String likeCount=getMapValue(map, "likeCount");
					String totalPost=getMapValue(map, "total_post");
					MemberBase member=memberBaseService.findById(memberId);
					
					NewsComment reply=findNewsComment(replyId);
					vo.setReplyMemberName(reply.getMember().getNickName());
					
					vo.setId(id);
					vo.setComment(comment);
					vo.setGood(null!=good && !"".equals(good)?Integer.valueOf(good):0);
					vo.setBad(null!=bad && !"".equals(bad)?Integer.valueOf(bad):0);
					vo.setReplyCount(null!=totalPost && !"".equals(totalPost)?Integer.valueOf(totalPost):0);
					vo.setInfoId(infoId);
					vo.setMemberId(memberId);
					vo.setMemberName(member.getNickName());
					vo.setOpTime(DateUtil.getDateTimeGoneFormatStr(DateUtil.StringToDate(opTime, DateUtil.DEFAULT_DATE_TIME_FORMAT), langCode, ""));
					//vo.setReplyCount(findCommentReplyCount(vo.getId()));
					vo.setReplyId(replyId);
					vo.setIsLike("1".equals(likeCount)?"1":"0");
					
					String gender="";
					if(member.getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA){
						MemberIfa ifa=memberBaseService.findIfaMember(vo.getMemberId());
						gender=ifa.getGender();
					}else if (member.getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL) {
						MemberIndividual individual=memberBaseService.findIndividualMember(vo.getMemberId());
						gender=individual.getGender();
					}
					vo.setMemberUrl(PageHelper.getUserHeadUrl(member.getIconUrl(), gender));
					list.add(vo);
				}
			}
			commentVO=appendComment(commentVO, list);
			//commentVO.setReplyCount(list.size());
		}
		return commentVO;
	}
	
	/**
	 * 递归获取评论
	 * 
	 * @author mqzou 2017-03-09
	 * @param vo
	 * @param list
	 * @return
	 */
	private NewsCommentVO appendComment(NewsCommentVO vo, List<NewsCommentVO> list) {
		if (null != list && !list.isEmpty()) {
			List<NewsCommentVO> childList = new ArrayList<NewsCommentVO>();//vo.getChildren();
			List<String> idsList=new ArrayList<String>();
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				NewsCommentVO commentVO = (NewsCommentVO) iterator.next();
				if (commentVO.getReplyId().equals(vo.getId()) && !idsList.contains(commentVO.getId())) {
					commentVO = appendComment(commentVO, list);
					childList.add(commentVO);
					idsList.add(commentVO.getId());
					//iterator.remove();
					
				}
			}
			vo.setReplylist(childList);
		}
		return vo;
	}
	
	/**
	 * 保存新闻或评论的顶或者踩
	 * @author mqzou 2017-03-10
	 * @param behavior
	 * @return
	 */
	@Override
	public NewsBehavior saveUpOrDown(NewsBehavior behavior) {
		//如果是顶或者踩的，需要把之前的数据删掉，再重新保存（只允许一个顶或者一个踩）
		StringBuilder hql=new StringBuilder();
		hql.append(" from NewsBehavior r where r.targetId=? and r.creator.id=? and (r.behaviorType=? or r.behaviorType=?)");
		List<Object> params=new ArrayList<Object>();
		params.add(behavior.getTargetId());
		params.add(behavior.getCreator().getId());
		params.add(CommonConstantsWeb.NEWS_BEHAVIOR_LIKE);
		params.add(CommonConstantsWeb.NEWS_BEHAVIOR_UNLIKE);
		List list=baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			NewsBehavior vo=(NewsBehavior)list.get(0);
			baseDao.delete(vo);
			
			if(CommonConstantsWeb.NEWS_BEHAVIOR_TARGET_NEWS.equals(behavior.getTarget())){//如果是新闻
				if(CommonConstantsWeb.NEWS_BEHAVIOR_LIKE.equals(vo.getBehaviorType())){
					NewsInfo info=findNewsInfo(vo.getTargetId());
					info.setGoodPost(info.getGoodPost()-1);
					baseDao.saveOrUpdate(info);
					
				}else if (CommonConstantsWeb.NEWS_BEHAVIOR_UNLIKE.equals(vo.getBehaviorType())) {
					NewsInfo info=findNewsInfo(vo.getTargetId());
					info.setBadPost(info.getBadPost()-1);
					baseDao.saveOrUpdate(info);
				}
				
			}else if (CommonConstantsWeb.NEWS_BEHAVIOR_TARGET_COMMENT.equals(behavior.getTarget())) {
				if(CommonConstantsWeb.NEWS_BEHAVIOR_LIKE.equals(vo.getBehaviorType())){
					NewsComment reply=findNewsComment(vo.getTargetId());
					reply.setGood(reply.getGood()-1);
					baseDao.saveOrUpdate(reply);
					
				}else if (CommonConstantsWeb.NEWS_BEHAVIOR_UNLIKE.equals(vo.getBehaviorType())) {
					NewsComment reply=findNewsComment(vo.getTargetId());
					reply.setBad(reply.getBad()-1);
					baseDao.saveOrUpdate(reply);
				}
			}
		}
		
		behavior=(NewsBehavior)baseDao.saveOrUpdate(behavior);
		if(CommonConstantsWeb.NEWS_BEHAVIOR_TARGET_NEWS.equals(behavior.getTarget())){//如果是新闻
			if(CommonConstantsWeb.NEWS_BEHAVIOR_LIKE.equals(behavior.getBehaviorType())){
				NewsInfo info=findNewsInfo(behavior.getTargetId());
				info.setGoodPost(info.getGoodPost()+1);
				baseDao.saveOrUpdate(info);
				
			}else if (CommonConstantsWeb.NEWS_BEHAVIOR_UNLIKE.equals(behavior.getBehaviorType())) {
				NewsInfo info=findNewsInfo(behavior.getTargetId());
				info.setBadPost(info.getBadPost()+1);
				baseDao.saveOrUpdate(info);
			}
			
		}else if (CommonConstantsWeb.NEWS_BEHAVIOR_TARGET_COMMENT.equals(behavior.getTarget())) {
			if(CommonConstantsWeb.NEWS_BEHAVIOR_LIKE.equals(behavior.getBehaviorType())){
				NewsComment reply=findNewsComment(behavior.getTargetId());
				int good=null!=reply.getGood()?reply.getGood():0;
				reply.setGood(good+1);
				baseDao.saveOrUpdate(reply);
				
			}else if (CommonConstantsWeb.NEWS_BEHAVIOR_UNLIKE.equals(behavior.getBehaviorType())) {
				NewsComment reply=findNewsComment(behavior.getTargetId());
				int bad=null!=reply.getBad()?reply.getBad():0;
				reply.setBad(bad+1);
				baseDao.saveOrUpdate(reply);
			}
		}
		
		return behavior;
	}
	
	
	
	/**
	 * 取消新闻的点赞或者踩
	 * @author mqzou 2017-03-10
	 * @param infoId
	 * @param member
	 * @return
	 */
	@Override
	public NewsBehavior saveCancelUpOrDown(String targetId, MemberBase member) {
		NewsBehavior vo=findUpOrDown(targetId, member.getId());
		if(null!=vo){
			if(CommonConstantsWeb.NEWS_BEHAVIOR_TARGET_NEWS.equals(vo.getTarget())){
				NewsInfo info=findNewsInfo(targetId);
				if(CommonConstantsWeb.NEWS_BEHAVIOR_UNLIKE.equals(vo.getBehaviorType())){
					info.setBadPost(info.getBadPost()-1);
				}else if (CommonConstantsWeb.NEWS_BEHAVIOR_LIKE.equals(vo.getBehaviorType())) {
					info.setGoodPost(info.getGoodPost()-1);
				}
				  this.baseDao.saveOrUpdate(info);
			}else if (CommonConstantsWeb.NEWS_BEHAVIOR_TARGET_COMMENT.equals(vo.getTarget())) {
				NewsComment comment=findNewsComment(targetId);
				if(CommonConstantsWeb.NEWS_BEHAVIOR_UNLIKE.equals(vo.getBehaviorType())){
					comment.setBad(comment.getBad()-1);
				}else if (CommonConstantsWeb.NEWS_BEHAVIOR_LIKE.equals(vo.getBehaviorType())) {
					comment.setGood(comment.getGood()-1);
				}
				  this.baseDao.saveOrUpdate(comment);
			}
			this.baseDao.delete(vo);
			return vo;
		}
		
		
		return null;
	}
	
	/**
	 * 检测新闻是否被赞或者被踩
	 * @author mqzou 2017-03-10
	 * @param targetId
	 * @return
	 */
	@Override
	public String checkNewsUpOrDown(String targetId, MemberBase member) {
		NewsBehavior behavior=findUpOrDown(targetId, member.getId());
		if(null!=behavior){
			if(!CommonConstantsWeb.NEWS_BEHAVIOR_LIKE.equals(behavior.getBehaviorType()) && !CommonConstantsWeb.NEWS_BEHAVIOR_UNLIKE.equals(behavior.getBehaviorType())){
				return "";
			}else {
				return behavior.getBehaviorType();
			}
		}
		
		return "";
		
	}
	
	/**
	 * 获取新闻内容的点赞或踩的实体
	 * @author mqzou 2017-03-10
	 * @param infoId
	 * @param memberId
	 * @return
	 */
	private NewsBehavior findUpOrDown(String targetId,String memberId){
		StringBuilder hql=new StringBuilder();
		hql.append("  from NewsBehavior r where r.targetId=? and r.creator.id=? and (r.behaviorType=? or r.behaviorType=?)");
		List<Object> params=new ArrayList<Object>();
		params.add(targetId);
		params.add(memberId);
		params.add(CommonConstantsWeb.NEWS_BEHAVIOR_LIKE);
		params.add(CommonConstantsWeb.NEWS_BEHAVIOR_UNLIKE);
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			NewsBehavior vo=(NewsBehavior)list.get(0);
			return vo;
		}
		return null;
	}
	
	/**
	 * 保存新闻的操作动作（如收藏）
	 * @author mqzou 2017-03-10
	 * @param behavior
	 * @return
	 */
	@Override
	public NewsBehavior saveBehavior(NewsBehavior behavior) {
		behavior=(NewsBehavior)baseDao.saveOrUpdate(behavior);
		//如果是新闻的点击动作
		if(CommonConstantsWeb.NEWS_BEHAVIOR_TARGET_NEWS.equals(behavior.getTarget()) && CommonConstantsWeb.NEWS_BEHAVIOR_READ.equals(behavior.getBehaviorType())){
			NewsInfo info=findNewsInfo(behavior.getTargetId());
			if(null!=info){
				int read=null!=info.getClick()?info.getClick():0;
				read++;
				info.setClick(read);
				this.baseDao.saveOrUpdate(info);
			}
		}
		return behavior;
	}
	
	/**
	 * 删除新闻的操作动作（如收藏）
	 * @author mqzou 2017-03-10
	 * @param behavior
	 * @return
	 */
	@Override
	public NewsBehavior deleteBehavior(NewsBehavior behavior) {
		try {
			baseDao.delete(behavior);
			return behavior;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取新闻的操作动作实体（如收藏）
	 * @author mqzou 2017-03-10
	 * @param behavior
	 * @return
	 */
	@Override
	public NewsBehavior findNewsBehavior(String tartgetId, String type,String memberId) {
		String hql=" from NewsBehavior r where r.targetId=? and r.behaviorType=? and r.creator.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(tartgetId);
		params.add(type);
		params.add(memberId);
		List list=baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			NewsBehavior vo=(NewsBehavior)list.get(0);
			return vo;
		}
		return null;
	}
	
	
	/**
	 * 模糊搜索新闻
	 * @author mqzou 2017-03-13
	 * @param jsonPaging
	 * @param keyWord
	 * @param langCode
	 * @return
	 */
	@Override
	public JsonPaging findNewsList(JsonPaging jsonPaging, String keyWord, String langCode,String dateFormat) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from NewsInfo r where r.title like ? and r.langCode=?");
	    List<Object> params=new ArrayList<Object>();
	    params.add("%"+keyWord+"%");
	    params.add(langCode);
	    jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
	    List<NewsInfoForListVO> list=new ArrayList<NewsInfoForListVO>();
	    if(null!=jsonPaging.getList()){
	    	Iterator it=jsonPaging.getList().iterator();
	    	while (it.hasNext()) {
				NewsInfo info = (NewsInfo) it.next();
				NewsInfoForListVO vo=new NewsInfoForListVO();
				vo.setId(info.getId());
				vo.setDescription(info.getDescription());
				vo.setLastUpdate(DateUtil.getDateTimeGoneFormatStr(info.getXfaLastModifyDate(), langCode, dateFormat));
				vo.setTitle(info.getTitle());
				
				NewsCategoryVO categoryVO=findCategoryByNews(vo.getId(), langCode);
				vo.setCategoryId(categoryVO.getId());
				vo.setCategoryName(categoryVO.getName());
				list.add(vo);
			}
	    }
	    jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 获取用户收藏的新闻列表
	 * @author mqzou 2017-03-23
	 * @param jsonPaging
	 * @param memberId
	 * @return
	 */
	@Override
	public JsonPaging findFavouriteNews(JsonPaging jsonPaging, String memberId) {
	    MemberBase base = memberBaseService.findById(memberId);
	    String dateformat = base.getDateFormat();
	    if (StringUtils.isBlank(dateformat)) {
	    	dateformat = DateUtil.DEFAULT_DATE_TIME_FORMAT;
		}
		StringBuilder hql=new StringBuilder();
	    hql.append(" select i from NewsBehavior b left join NewsInfo i on b.targetId=i.id where b.behaviorType='favorite' and b.target='news'");
	    hql.append(" and i is not null and  b.creator.id=?");
	    List<Object> params=new ArrayList<Object>();
	    params.add(memberId);
	    List<NewsInfoForListVO> list=new ArrayList<NewsInfoForListVO>();
	    jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
	    if(null!=jsonPaging && null!=jsonPaging.getList()){
	    	Iterator it=jsonPaging.getList().iterator();
	    	while (it.hasNext()) {
	    		NewsInfo info = (NewsInfo) it.next();
	    		NewsInfoForListVO vo = new NewsInfoForListVO();
	    		vo.setId(info.getId());
	    		vo.setDescription(info.getDescription());
	    		vo.setGoodPost(null != info.getGoodPost() ? String.valueOf(info.getGoodPost()) : "0");
	    		vo.setLastUpdate(DateUtil.dateToDateString(info.getPubDate(), dateformat));
	    		vo.setTitle(info.getTitle());
	    		vo.setUrl(info.getLitPic());
	    		vo.setClick(null != info.getClick() ? String.valueOf(info.getClick()) : "0");
	    		vo.setSectionType(info.getSectionType());
	    		list.add(vo);
			}
	    }
	    
		jsonPaging.setList(list);
		
		return jsonPaging;
	}
	
	
}
