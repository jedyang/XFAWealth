/**
 * 
 */
package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.web.service.WebFollowStatusService;

/**
 * @author scshi
 *关注/取消关注接口实现
 */
@Service("webFollowStatusService")
//@Transactional
public class WebFollowStatusServiceImpl extends BaseService implements WebFollowStatusService {
	
	/**3.1.13	关注/取消
	 * @author scshi
	 * @param relateId 对应类型id
	 * @param memberID 网站会员ID
	 * @parame OpType	Follow-设置关注;Delete-取消关注
	 * @param moduleType 对应模块,ifa关注,article文章关注,strategy策略关注分类,portfolio_info组合关注分类
	 */
	public WebFollow saveWebFollowStatus(String relateId, String memberID,String opType, String moduleType) {
		String hql = "from WebFollow t where t.relateId=? and t.member.id=? and t.moduleType=? and t.isValid='1' ";
		List params = new ArrayList();
		params.add(relateId);
		params.add(memberID);
		params.add(moduleType);
		List<WebFollow> list = this.baseDao.find(hql, params.toArray(), false);
		if("Follow".equals(opType) && list.isEmpty()){
			WebFollow follow = new WebFollow();
			MemberBase member = (MemberBase)this.baseDao.get(MemberBase.class, memberID);
			follow.setCreateTime(new Date());
			follow.setIsValid("1");
			follow.setMember(member);
			follow.setModuleType(moduleType);
			follow.setRelateId(relateId);
			this.baseDao.create(follow);
			return follow;
		}else if("Delete".equals(opType) && !list.isEmpty()){
			WebFollow follow = list.get(0);
            this.baseDao.delete(follow);
            return new WebFollow();
		}
		return null;
		
	}

	/**3.1.14	获取基金关注状态
	 * @author scshi
	 * @param relateID 对应类型id
	 * @param memberID 网站会员ID
	 * @param moduleType 对应模块,fund基金关注,ifa关注,article文章关注
	 */
	//@Transactional(readOnly=true)
	public String getWebFollowStatus(String relateID, String memberID,String moduleType) {
		String hql = "from WebFollow t where t.relateId=? and t.member.id=? and t.moduleType=? and t.isValid='1' ";
		List params = new ArrayList();
		params.add(relateID);
		params.add(memberID);
		params.add(moduleType);
		List<WebWatchlist> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			//WebSelf obj = list.get(0);
			return "1";
		}
		return null;
	}
	
	/**3.1.15	我关注的基金名单列表   作废/scshi/20160719,更换到FundinfoService下
	 * @author scshi
	 * @param relateID 对应类型id
	 * @param memberID 网站会员ID
	 * @param jsonPaging 
	 * @param moduleType 对应模块,fund基金关注,ifa关注,article文章关注
	 */
	public JsonPaging getWebFollowList(String memberID,String moduleType,String lang,JsonPaging jsonPaging) {
		
		//String hql = "select t.id, t.isinCode,t.riskLevel,out.fundCurrencyCode,t.issueDate,out.fundName,w.followFlag ";
		String hql = "SELECT i.id, i.isinCode, i.riskLevel, i.lastNav, i.lastNavUpdate, i.issuePrice, i.issueDate, " +
				"i.mgtFee, i.minInitialAmount, i.minSubscribeAmount, i.minHoldingAmount, i.minRedemptionAmount, " +
				"i.minRspAmount, i.createTime, i.lastUpdate, i.isValid, f.id, f.fundName, f.domicileCode, f.domicile, f.fundHouse, " +
				"f.fundManCo, f.fundManager, f.fundSize, f.fundCurrencyCode, f.fundCurrency, f.fundTypeCode, f.fundType, f.sectorTypeCode, " +
				"f.sectorType, f.geoAllocationCode, f.geoAllocation, f.invTarget, f.keyRisks ";
		hql += "from FundInfo i ";
//		if("en".equals(lang)){
//			hql += "inner join FundInfoEn ";
//		}else if("tc".equals(lang)){
//			hql += "inner join FundInfoTc ";
//		}else{
//			hql += "inner join FundInfoSc ";
//		}
		hql += "inner join "+this.getLangString("FundInfo",lang);
		hql +=" f on i.id=f.id ";
		hql += " inner join WebFollow w on i.id=w.relateId and w.member.id=? and w.moduleType=? and w.isValid='1' ";
		List params = new ArrayList();
		params.add(memberID);
		params.add(moduleType);
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
//		List voList = this.baseDao.find(hql, params.toArray(), false);
//		List list = new ArrayList();
		
//		Iterator it = jsonPaging.getList().iterator();
//		Iterator it = voList.iterator();
//		Integer indexNumber = (jsonPaging.getPage()-1)*jsonPaging.getRows();
//		Integer index = 0;
//    	while(it.hasNext()){
//			index++;
//			Object[] objs = (Object[])it.next();
//			FoundFollowVo  vo = new FoundFollowVo();
//			vo.setId(objs[0]==null?"":objs[0].toString());
//			vo.setIsinCode(objs[1]==null?"":objs[1].toString());
//			vo.setRiskLevel(objs[2]==null?"":objs[2].toString());
//			vo.setFundCurrencyCode(objs[3]==null?"":objs[3].toString());
//			vo.setIssueDate(objs[4]==null?"":objs[4].toString());
//			vo.setFundName(objs[5]==null?"":objs[5].toString());
//			vo.setFollowFlag(objs[6]==null?"":objs[6].toString());
//			vo.setXh(Integer.toString(indexNumber+index));
//			list.add(vo);
//    	}
		return jsonPaging;
	}
}
