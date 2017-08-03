package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.web.service.EmailLogService;
import com.fsll.wmes.web.vo.EmailLogVO;


/***
 * 业务接口实现类：邮件管理接口类
 * @author mqzou
 * @date 2016-06-22
 */
@Service("emailService")
//@Transactional
public class EmailLogServiceImpl extends BaseService implements EmailLogService {

	/***
     * 查询email管理
     * @author wwluo
     * @date 2016-06-01
     */
	@Override
	public JsonPaging findAll(JsonPaging jsonPaging, EmailLogVO emailLog, String langCode) {
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " l"
				+ " FROM"
				+ " WebEmailLog l"
				+ " LEFT JOIN"
				+ " MemberBase m"
				+ " ON"
				+ " l.toMember.id=m.id"
				+ " OR"
				+ " l.creator.id=m.id"
				+ " WHERE 1=1"
				+ "");
		List<Object> params = new ArrayList<Object>();
		if(emailLog != null){
			if (StringUtils.isNotBlank(emailLog.getToEmailTitle())) {
				hql.append(" AND l.toEmailTitle LIKE ?");
				params.add("%" + emailLog.getToEmailTitle() + "%");
			}
			if (StringUtils.isNotBlank(emailLog.getSendFlag())) {
				hql.append(" AND l.sendFlag=?");
				params.add(emailLog.getSendFlag());
			}
			if (StringUtils.isNotBlank(emailLog.getToMemberName())) {
				hql.append(" AND m.nickName LIKE ?");
				params.add("%" + emailLog.getToMemberName() + "%");
			}
			if (StringUtils.isNotBlank(emailLog.getCreatorName())) {
				hql.append(" AND m.nickName LIKE ?");
				params.add("%" + emailLog.getCreatorName() + "%");
			}
			if (StringUtils.isNotBlank(emailLog.getStartTimeStr()) && StringUtils.isNotBlank(emailLog.getEndTimeStr())) {
				hql.append(" AND l.sendedTime BETWEEN ? AND ?");
				params.add(DateUtil.StringToDate(emailLog.getStartTimeStr(), CommonConstants.FORMAT_DATE_TIME));
				params.add(DateUtil.StringToDate(emailLog.getEndTimeStr(), CommonConstants.FORMAT_DATE_TIME));
			}
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
			List<WebEmailLog> emailLogs = jsonPaging.getList();
			List<EmailLogVO> vos = new ArrayList<EmailLogVO>();
			for (WebEmailLog webEmailLog : emailLogs) {
				EmailLogVO vo = new EmailLogVO();
				BeanUtils.copyProperties(webEmailLog, vo);
				if(webEmailLog.getToMember() != null){
					// vo.setToMemberName(getCommonMemberName(webEmailLog.getToMember().getId(), langCode, "2"));
					vo.setToMemberName(webEmailLog.getToMember().getNickName());
				}
				if(webEmailLog.getCreator() != null){
					// vo.setCreatorName(getCommonMemberName(webEmailLog.getCreator().getId(), langCode, "2"));
					vo.setCreatorName(webEmailLog.getCreator().getNickName());
				}
				vos.add(vo);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(vos);
		}
		return jsonPaging;
	}

	@Override
	public WebEmailLog findById(String id) {
		WebEmailLog log = (WebEmailLog) baseDao.get(WebEmailLog.class, id);
		return findDetail(log);
	}
	
	/**
	 * 获得一条记录的详细信息
	 * @param 
	 * @return
	 */
	private WebEmailLog findDetail(WebEmailLog log){
		
		return log;
	}

}
