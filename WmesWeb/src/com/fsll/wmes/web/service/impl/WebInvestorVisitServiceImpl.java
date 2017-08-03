package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.web.service.WebInvestorVisitService;

/**
 * @author Yan
 * @date 2017-01-07
 */
@Service("webInvestorVisitService")
//@Transactional
public class WebInvestorVisitServiceImpl extends BaseService implements WebInvestorVisitService {

	/**
	 * 保存访问记录
	 */
	@Override
	public WebInvestorVisit addLog(WebInvestorVisit log) {
		WebInvestorVisit info = new WebInvestorVisit();
		if(null != log){
			info = (WebInvestorVisit)this.baseDao.create(log);
		}
		return info;
	}

	/**
	 * 根据条件获取访问记录
	 * @author mqzou 2017-02-06
	 * @param visit
	 * @return
	 */
	@Override
	public WebInvestorVisit findInvestorVisit(String moduleType,String relateId,String memberId) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from WebInvestorVisit r where r.moduleType=? and r.relateId=? and r.member.id=?");
		List<Object> params=new ArrayList<Object>();
		params.add(moduleType);
		params.add(relateId);
		params.add(memberId);
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			WebInvestorVisit visit=(WebInvestorVisit)list.get(0);
			return visit;
		}
		return null;
	}
	
	/**
	 * 保存访问记录
	 * @author mqzou 2017-02-06
	 * @param moduleType
	 * @param relateId
	 * @param memberId
	 * @return
	 */
	@Override
	public WebInvestorVisit checkAndSaveVisit(String moduleType, String relateId, MemberBase member) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from WebInvestorVisit r where r.moduleType=? and r.relateId=? and r.member=?");
		List<Object> params=new ArrayList<Object>();
		params.add(moduleType);
		params.add(relateId);
		params.add(member);
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			WebInvestorVisit visit=(WebInvestorVisit)list.get(0);
			return visit;
		}else {
			WebInvestorVisit visit=new WebInvestorVisit();
			visit.setMember(member);
			visit.setModuleType(moduleType);
			visit.setRelateId(relateId);
			visit.setVistiTime(DateUtil.getCurDate());
			visit=(WebInvestorVisit)this.baseDao.saveOrUpdate(visit);
			return visit;
		}
	}
}
