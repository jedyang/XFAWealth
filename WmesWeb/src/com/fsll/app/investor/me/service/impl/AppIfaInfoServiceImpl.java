package com.fsll.app.investor.me.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.me.service.AppIfaInfoService;
import com.fsll.app.investor.me.vo.AppIfaItemVO;
import com.fsll.app.investor.me.vo.AppIfaRecommendedItemVO;
import com.fsll.app.investor.me.vo.AppIfaSearchItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.WebFriend;

/**
 * 基金信息查询服务接口
 * @author zpzhou
 * @date 2016-7-25
 */
@Service("appIfaInfoService")
//@Transactional
public class AppIfaInfoServiceImpl extends BaseService implements AppIfaInfoService {
	/**
	 * 得到distributor信息
	 * @return
	 */
	public List<AppIfaSearchItemVO> getDistributorMess(){
		List<AppIfaSearchItemVO> list = new ArrayList();
		String hql = "select c.id,c.companyName from MemberDistributor c ";
		hql+=" order by c.companyName asc ";
		List dataList = this.baseDao.find(hql,null, false);
		if(!dataList.isEmpty()){
			for(int x=0;x<dataList.size();x++){
				AppIfaSearchItemVO vo=new AppIfaSearchItemVO();
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
		String sql = "select m.id,m.member_id as memberId, m.name_chn as nickName,i.company_name as companyName," +
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
	 * 添加/取消好友
	 * @param fromMemberId 发起的会员ID
	 * @param toMemberId  关联的会员ID
	 * @param applyMsg  申请信息
	 * @param OpType Add-添加;Delete-删除
	 * @return 0 :添加成功 1:删除成功   2:已经为好友，无须再次添加
	 */
	public int saveWebFriendMess(String fromMemberId,String toMemberId,String applyMsg,String opType) {
		if("Add".equals(opType)){
			String hql = "from WebFriend t where t.fromMember.id=? and t.toMember.id=? ";
			List params = new ArrayList();
			params.add(fromMemberId);
			params.add(toMemberId);
			List<WebFriend> list = baseDao.find(hql, params.toArray(), false);
			if(!list.isEmpty()){
				WebFriend obj = list.get(0);
				if("1".equals(obj.getCheckResult())){
					return 2;//已经为好友，无须再次添加
				}else{
					obj.setCheckResult("0");
					obj.setLastUpdate(new Date());
					obj.setApplyMsg(applyMsg);
					baseDao.saveOrUpdate(obj, false);
					return 0;
				}
			}else{
				WebFriend webFriend = new WebFriend();
				MemberBase fromMember = (MemberBase)this.baseDao.get(MemberBase.class, fromMemberId);
				MemberBase toMember = (MemberBase)this.baseDao.get(MemberBase.class, toMemberId);
				webFriend.setFromMember(fromMember);
				webFriend.setToMember(toMember);
				webFriend.setRelationships("Buddy Client Advisor");
				webFriend.setApplyMsg(applyMsg);
				webFriend.setCheckResult("0");
				webFriend.setCreateTime(new Date());
				webFriend.setLastUpdate(new Date());
				baseDao.create(webFriend);
				return 0;
			}
		}else if("Delete".equals(opType)){
			//先删除自己的那条记录，再修改对方记录的状态为删除状态
			String hqlDel = "delete from WebFriend t where t.fromMember.id=? and t.toMember.id=? ";
			List paramDel = new ArrayList();
			paramDel.add(fromMemberId);
			paramDel.add(toMemberId);
			baseDao.updateHql(hqlDel, paramDel.toArray());
			String hqlStatus = "from WebFriend t where t.fromMember.id=? and t.toMember.id=? ";
			List paramStatus = new ArrayList();
			paramStatus.add(toMemberId);
			paramStatus.add(fromMemberId);
			List<WebFriend> list = baseDao.find(hqlStatus, paramStatus.toArray(), false);
			if(!list.isEmpty()){
				WebFriend obj = list.get(0);
				obj.setCheckResult("3");
				obj.setCheckRemark("");
				obj.setLastUpdate(new Date());
				baseDao.saveOrUpdate(obj, false);
			}
		    return 1;
		}
		return 0;
	}
	
	/**
	 * 得到Ifa信息
	 * @author zxtan
	 * @date 2017-01-16
	 * @return
	 */
	public List<AppIfaItemVO> findMyIfaList(String memberId,String langCode){
		List<AppIfaItemVO> voList = new ArrayList<AppIfaItemVO>();
		List<String> params = new ArrayList<String>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from MemberIfa m ");
		hql.append(" inner join MemberIfaIfafirm l on l.ifa.id=m.id ");
		hql.append(" inner join MemberIfafirm f on f.id=l.ifafirm.id ");
		hql.append(" inner join "+getLangString("MemberIfafirm", langCode)+" t on t.id=f.id ");
		hql.append(" where exists (");
		hql.append(" select a.id from InvestorAccount a where a.ifa.id=m.id and a.openStatus='3' and a.member.id=? ) ");
		if("sc".equalsIgnoreCase(langCode) || "tc".equalsIgnoreCase(langCode)){
			hql.append(" order by m.nameChn ");
		}else {
			hql.append(" order by m.firstName ");
		}
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(),params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				AppIfaItemVO vo=new AppIfaItemVO();				
				Object[] objs = (Object[])list.get(i);
				MemberIfa ifa = (MemberIfa) objs[0];
				MemberBase info = ifa.getMember();
				MemberIfafirmSc ifafirm = new MemberIfafirmSc();
				BeanUtils.copyProperties(objs[3], ifafirm);
				
				vo.setId(ifa.getId());
				vo.setMemberId(info.getId());
				vo.setCompanyName(ifafirm.getCompanyName());
				
				String userName = getCommonMemberName(info.getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
				vo.setFullname(userName);
								
				String gender = ifa.getGender()==null?"":ifa.getGender();
				String iconUrl = PageHelper.getUserHeadUrlForWS(info.getIconUrl(), gender);
				vo.setIconUrl(iconUrl);
				
				List<AppIfaRecommendedItemVO> recommendedCountList = findMyIfaRecommendedCountList(info.getId());
				vo.setRecommendedCountList(recommendedCountList);
				if(null != recommendedCountList && !recommendedCountList.isEmpty()){
					String lastUpdate = recommendedCountList.get(0).getLastUpdate();
					vo.setRecommendedTime(lastUpdate);
				}
								
				voList.add(vo);
			}
		}
		return voList;
	}
	
	/**
	 * 得到Ifa推荐统计信息
	 * @author zxtan
	 * @date 2017-02-15
	 * @return
	 */
	private List<AppIfaRecommendedItemVO> findMyIfaRecommendedCountList(String memberId){
		List<AppIfaRecommendedItemVO> voList = new ArrayList<AppIfaRecommendedItemVO>();
		List<String> params = new ArrayList<String>();
		StringBuilder hql = new StringBuilder();
		hql.append(" select r.moduleType,count(id) as recommendCount,MAX(DATE_FORMAT(r.recommendTime,'%Y-%m-%d %H:%i:%s')) as lastUpdate from WebRecommended r ");
		hql.append(" where r.creator.id=? ");
		hql.append(" GROUP BY r.moduleType ");
		hql.append(" ORDER BY r.recommendTime DESC ");
		
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(),params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				AppIfaRecommendedItemVO vo = new AppIfaRecommendedItemVO();				
				Object[] objs = (Object[])list.get(i);
								
				vo.setModuleType(objs[0]==null?"":objs[0].toString());
				vo.setCount(objs[1]==null?"":objs[1].toString());
//				String lastUpdate = objs[2]==null?"":objs[2].toString();
				
				vo.setLastUpdate(objs[2]==null?"":objs[2].toString());				
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	
	
	
}
