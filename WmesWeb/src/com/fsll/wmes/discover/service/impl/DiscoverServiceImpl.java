package com.fsll.wmes.discover.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.discover.service.DiscoverService;
import com.fsll.wmes.discover.vo.WebDiscussReplyVO;
import com.fsll.wmes.discover.vo.WebDiscussVO;
import com.fsll.wmes.entity.WebDiscuss;
import com.fsll.wmes.entity.WebDiscussReply;

@Service("discoverService")
//@Transactional
public class DiscoverServiceImpl extends BaseService implements DiscoverService{

	/**
     * 读取“评论”信息
     * @author wwluo
     * @date 2016-11-07 
     * @param moduleType 对应模块，null时获取全部信息
     * @param topicType H：Hot Topic ； L：Latest Topic 排序方式
     * @return
     */
	@SuppressWarnings("unchecked")
	@Override
	public JsonPaging getWebDiscuss(JsonPaging jsonPaging,String relateId,String moduleType, String topicType) {
		StringBuffer hql = new StringBuffer(" from WebDiscuss where status=1 and relateId=?");
		List params = new ArrayList();
		params.add(relateId);
		if(StringUtils.isNotBlank(moduleType)){
			hql.append(" and moduleType=?");
			params.add(moduleType);
		}
		if("H".equals(topicType)){
			hql.append(" order by ups desc");
		}else{
			hql.append(" order by createTime desc");
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(),jsonPaging, false);
		if(!jsonPaging.getList().isEmpty()){
			List<WebDiscussVO> webDiscussVOs = new ArrayList<WebDiscussVO>();
			Long day = (long) (24*60*60*1000);
			Long hour = (long) (60*60*1000);
			for (Object object : jsonPaging.getList()) {
				WebDiscuss webDiscuss = (WebDiscuss) object;
				WebDiscussVO webDiscussVO = new WebDiscussVO();
				BeanUtils.copyProperties(webDiscuss, webDiscussVO);
				Date replyTime = webDiscuss.getCreateTime();
				Calendar calendar = Calendar.getInstance();
				//时间差
				Long timeDiff = calendar.getTimeInMillis()-replyTime.getTime();
				if(timeDiff > day){
					Long temp = timeDiff/day;
					Integer timeNum = Integer.parseInt(temp.toString());
					webDiscussVO.setTimeNum(timeNum);
					webDiscussVO.setTimeType("D");
				}else{
					Long temp = timeDiff/hour;
					Integer timeNum = Integer.parseInt(temp.toString());
					webDiscussVO.setTimeNum(timeNum);
					webDiscussVO.setTimeType("H");
				}
				if(webDiscuss != null && webDiscuss.getMember() != null){
					webDiscussVO.setIconUrl(webDiscuss.getMember().getIconUrl());
					webDiscussVO.setNickName(webDiscuss.getMember().getNickName());
				}
				webDiscussVOs.add(webDiscussVO);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(webDiscussVOs);
			jsonPaging.setTotal(webDiscussVOs.size());
		}
		return jsonPaging;
	}

	/**
	 * 发表“评论”信息
	 * @author wwluo
	 * @date 2016-11-07 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@Override
	public WebDiscuss saveWebDiscuss(WebDiscuss webDiscuss) {
		return (WebDiscuss) this.baseDao.saveOrUpdate(webDiscuss);
	}
    /**
     * 读取“回复”信息
     * @author wwluo
     * @date 2016-11-07 
     * @param request
     * @param response
     * @param model
     * @return
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<WebDiscussReplyVO> getWebDiscussReply(String discussId) {
		List<WebDiscussReplyVO> webDiscussReplyVOs = new ArrayList<WebDiscussReplyVO>();
		if(StringUtils.isNotBlank(discussId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" WebDiscussReply d" +
					" WHERE" +
					" d.status=1" +
					" AND" +
					" d.discuss.id=?" +
					" ORDER BY" +
					" d.replyTime" +
					" DESC");
			List params = new ArrayList();
			params.add(discussId);
			List<WebDiscussReply> webDiscussReplys = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(webDiscussReplys != null && !webDiscussReplys.isEmpty()){
				//封装“回复”实体VO
				for (WebDiscussReply webDiscussReply : webDiscussReplys) {
					WebDiscussReplyVO webDiscussReplyVO = new WebDiscussReplyVO();
					BeanUtils.copyProperties(webDiscussReply, webDiscussReplyVO);
					Date replyTime = webDiscussReply.getReplyTime();
					Calendar calendar = Calendar.getInstance();
					//计算时间差
					Long diffTime = calendar.getTimeInMillis() - replyTime.getTime();
					if(diffTime > CommonConstants.ONE_DAY){ //大于一天
						Long temp = diffTime / CommonConstants.ONE_DAY;
						Integer timeNum = Integer.parseInt(temp.toString());
						webDiscussReplyVO.setTimeNum(timeNum);
						webDiscussReplyVO.setTimeType(CommonConstants.DATE_TYPE_DAY);
					}else{ //小于一天
						Long temp = diffTime / CommonConstants.ONE_HOUR;
						Integer timeNum = Integer.parseInt(temp.toString());
						webDiscussReplyVO.setTimeNum(timeNum);
						webDiscussReplyVO.setTimeType(CommonConstants.DATE_TYPE_HOUR);
					}
					WebDiscussReply parentReply = webDiscussReply.getParent();
					if(parentReply != null && StringUtils.isNotBlank(parentReply.getId()) && parentReply.getMember() != null){
						//存在父节点
						webDiscussReplyVO.setBeRepliedTo(parentReply.getMember().getNickName());
					}else if(webDiscussReply.getDiscuss() != null && webDiscussReply.getDiscuss().getMember() != null){
						//不存在父节点
						webDiscussReplyVO.setBeRepliedTo(webDiscussReply.getDiscuss().getMember().getNickName());
					}
					if(webDiscussReply.getMember() != null){
						webDiscussReplyVO.setNickName(webDiscussReply.getMember().getNickName());
						webDiscussReplyVO.setIconUrl(webDiscussReply.getMember().getIconUrl());
					}
					webDiscussReplyVOs.add(webDiscussReplyVO);
				}
			}
		}
		return webDiscussReplyVOs;
	}
	
	/**
	 * 发表“回复”信息
	 * @author wwluo
	 * @date 2016-11-07 
	 * @param webDiscussReply
	 * @return
	 */
	@Override
	public WebDiscussReply saveWebDiscussReply(WebDiscussReply webDiscussReply) {
		return (WebDiscussReply) this.baseDao.saveOrUpdate(webDiscussReply);
	}

	/**
     * 读取一条“评论”信息
     * @author wwluo
     * @date 2016-11-07 
     * @param discussId
     * @return
     */
	@Override
	public WebDiscuss getWebDiscussById(String discussId) {
		WebDiscuss webDiscuss = null;
		if (StringUtils.isNotBlank(discussId)) {
			webDiscuss = (WebDiscuss) this.baseDao.get(WebDiscuss.class, discussId);
		}
		return webDiscuss;
	}

	/**
     * 读取一条“回复”信息
     * @author wwluo
     * @date 2016-11-07 
     * @param discussReplyId
     * @return
     */
	@Override
	public WebDiscussReply getWebDiscussReplyById(String discussReplyId) {
		WebDiscussReply webDiscussReply = null;
		if (StringUtils.isNotBlank(discussReplyId)) {
			webDiscussReply = (WebDiscussReply) this.baseDao.get(WebDiscussReply.class, discussReplyId);
		}
		return webDiscussReply;
	}

}
