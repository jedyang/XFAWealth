package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.IfaMigrateHist;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.WebFriend;
import com.fsll.wmes.ifa.service.IfaMigrateHistService;
import com.fsll.wmes.ifa.vo.IfaClientVO;
import com.fsll.wmes.ifa.vo.MyBuddyVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberVO;
import com.fsll.wmes.web.service.WebFriendService;

/**
 * 朋友记录接口实现
 * @author michael 2017-02-08
 *
 */
@Service("webFriendService")
public class WebFriendServiceImpl  extends BaseService implements WebFriendService{
	@Autowired
	private MemberBaseService memberBaseService ;
	@Autowired
	private IfaMigrateHistService ifaMigrateHistService;
	/**
	 * 通过会员ID查询好友
	 * 
	 * @author michael
	 * @param fromMemberId 会员ID
	 * @return list 好友列表
	 */
	@Override
	public List<WebFriend> findWebFriends(String fromMemberId) {
		List<WebFriend> list = new ArrayList<WebFriend>();
		List<String> params = new ArrayList<String>();

		String hql = "from WebFriend t ";
		if (null != fromMemberId && !"".equals(fromMemberId)) {
			hql += " where fromMember.id = ? )";
			params.add(fromMemberId);
		}
		hql += " order by t.toMember ";

		list = this.baseDao.find(hql, params.toArray(), false);

		return list;
	}
	/**
	 * 通过会员ID和类型查询好友
	 * @author mqzou 2016-12-08
	 * @param fromMemberId 会员ID
	 * @param type 好友类型
	 * @return list 好友列表
	 */
	@Override
	public List<WebFriend> findWebFriendsByType(String fromMemberId, String type,int limit) {
		List<WebFriend> list = new ArrayList<WebFriend>();
		List<Object> params = new ArrayList<Object>();

		String hql = "from WebFriend t where 1=1";
		if (null != fromMemberId && !"".equals(fromMemberId)) {
			hql += " and fromMember.id = ? ";
			params.add(fromMemberId);
		}
		if(null!=type && !"".equals(type)){
			hql+=" and t.relationships=?";
			params.add(type);
		}
		
		hql += " order by t.toMember ";
		/*if(limit>0){
			hql+="limit ?";
			params.add(limit);
		}*/
			
		list = this.baseDao.find(hql, params.toArray(), false);

		return list;
	}

	/**
	 * 通过会员ID查询IFA好友
	 * 
	 * @author michael
	 * @param fromMemberId 会员ID
	 * @return list IFA好友列表
	 */
	@Override
	//@Transactional(readOnly = true)
	public List<MemberIfa> findIfaFriends(String fromMemberId) {
		List<MemberIfa> list = new ArrayList<MemberIfa>();
		List<String> params = new ArrayList<String>();

		String hql = "select m from MemberIfa m , WebFriend f where m.member.id = f.toMember.id  ";

		hql += " and f.fromMember.id = ? ";
		params.add(fromMemberId);

		hql += " order by m.firstName ";

		list = this.baseDao.find(hql, params.toArray(), false);
		List<MemberIfa> result = new ArrayList<MemberIfa>();
		if (!list.isEmpty()){
			List<String> ifaIdList = new ArrayList<String>();
			for (MemberIfa ifa:list){
				if (!ifaIdList.contains(ifa.getId())){
					result.add(ifa);
					ifaIdList.add(ifa.getId());
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 获取webFriend
	 * @author mqzou 2017-02-16
	 * @param fromMemberId
	 * @param toMemberId
	 * @return
	 */
	@Override
	public WebFriend getWebFriend(String fromMemberId, String toMemberId,String type) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from WebFriend r where r.fromMember.id=? and r.toMember.id=? and r.relationships=?");
		List<Object> params=new ArrayList<Object>();
		params.add(fromMemberId);
		params.add(toMemberId);
		params.add(type);
		List list=baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			WebFriend friend=(WebFriend)list.get(0);
			return friend;
		}
			
		return null;
	}
	
	/**
	 * 更新好友信息状态
	 * @author mqzou
	 * @date 2016-09-27
	 * @param webFriend
	 * @return
	 */
	@Override
	public WebFriend updateWebFriend(WebFriend webFriend) {
		WebFriend vo=(WebFriend)this.baseDao.saveOrUpdate(webFriend);
		return vo;
	}
	
	/**
	 * 添加/取消好友
	 * @param fromMemberId 发起的会员ID
	 * @param toMemberId  关联的会员ID
	 * @param applyMsg  申请信息
	 * @param OpType Add-添加;Delete-删除
	 * @return 审核状态,0待审核,1审核通过,3好友删除
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
					return 1;//已经为好友，无须再次添加
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
				return 2;
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
		    return 3;
		}
		return 0;
	}
	
	@Override
	public List<MyBuddyVO> findFriends(WebFriend webFriend,String langCode) {
	    String sql=" SELECT * FROM web_friend r  where 1=1";
	    List params=new ArrayList();
	    boolean isGetNew=false;
	    if(null!=webFriend){
	    	if(null!=webFriend.getFromMember() && null!=webFriend.getFromMember().getId() && !"".equals(webFriend.getFromMember().getId())){

	    		sql+=" and r.from_member_id=? ";
	    	    params.add(webFriend.getFromMember().getId());
	    	}
	    	if(null!=webFriend.getCheckResult() && !"".equals(webFriend.getCheckResult())){
	    		sql+=" and r.check_result=?";
	    		params.add(webFriend.getCheckResult());
	    		if("0".equals(webFriend.getCheckResult())){
	    			isGetNew=true;
	    		}
	    	}
	    	if(null!=webFriend.getToMember() && null!=webFriend.getToMember().getId() && !"".equals(webFriend.getToMember().getId())){
	    		sql+=" and r.to_member_id=? ";
	    	    params.add(webFriend.getToMember().getId());
	    	}
	    }
	    if(isGetNew){
	    	 sql+=" and not exists(select 1 from web_friend f where f.from_member_id=r.from_member_id and f.to_member_id=r.to_member_id and f.check_result<>'0') "; 	
	    }
	   
	    sql+="  ORDER BY CASE  WHEN relationships LIKE '%Client%' THEN 0 WHEN relationships LIKE '%Advisor%' THEN 1 WHEN relationships LIKE '%Prospect%' THEN 2 WHEN relationships LIKE '%Buddy%' THEN 3 END";
	    List reList=this.springJdbcQueryManager.springJdbcQueryForList(sql,params.toArray());
	    Iterator it=reList.iterator();
	    List<MyBuddyVO> list=new ArrayList<MyBuddyVO>();
	    List<String> idList=new ArrayList<String>();
	    while (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			MyBuddyVO vo=new MyBuddyVO();
			//MemberBase memberBase=new MemberBase();
			String id=getMapValue(map, "id");
			String toMemberId=getMapValue(map, "to_member_id");
			String relationship=getMapValue(map, "relationships");
			String applyMsg=getMapValue(map, "apply_msg");
			String fromMemberId=getMapValue(map, "from_member_id");
			MemberBase memberBase=memberBaseService.findById(toMemberId);
			vo.setToMemberType(memberBase.getMemberType().toString());
			memberBase=memberBaseService.findById(fromMemberId);
			vo.setFromMemberType(memberBase.getMemberType().toString());
			if(idList.contains(toMemberId+","+fromMemberId)){
				continue;
			}else {
				idList.add(toMemberId+","+fromMemberId);
			}
			vo.setMsg(applyMsg);
			
			String memberId="";
			if(null!=webFriend.getToMember()){
				memberId=fromMemberId;
			}else {
				memberId=toMemberId;
			}
			MemberIndividual individual= memberBaseService.findIndividualMember(memberId);
			if(null!=individual){
				if(relationship.contains("Client") || relationship.contains("Advisor")){
					vo.setState("1");
				}
				vo.setId(id);
				vo.setMemberId(individual.getMember().getId());
				vo.setEmail(individual.getMember().getEmail());
				vo.setMobileNumber(individual.getMember().getMobileNumber());
				vo.setLoginCode(individual.getMember().getLoginCode());
				vo.setIconUrl(null!=individual.getMember().getIconUrl()?individual.getMember().getIconUrl():"");
				vo.setGender(null!=individual.getGender()?individual.getGender():"M");
				vo.setNickName(getCommonMemberName(individual.getMember().getId(), langCode, "1"));
				vo.setImUserId(individual.getMember().getImUserId());
				list.add(vo);
			}else {
				MemberIfa ifa=memberBaseService.findIfaMember(memberId);
				if(null!=ifa){
					if(relationship.contains("Client") || relationship.contains("Advisor")){
						vo.setState("1");
					}
					vo.setId(id);
					vo.setMemberId(ifa.getMember().getId());
					vo.setEmail(ifa.getMember().getEmail());
					vo.setMobileNumber(ifa.getMember().getMobileNumber());
					vo.setLoginCode(ifa.getMember().getLoginCode());
					vo.setIconUrl(null!=ifa.getMember().getIconUrl()?ifa.getMember().getIconUrl():"");
					vo.setGender(null!=ifa.getGender()?ifa.getGender():"M");
					String nickName=getCommonMemberName(ifa.getMember().getId(), langCode, "2");
					vo.setNickName(nickName);
					//vo.setNickName(ifa.getMember().getNickName());
					vo.setImUserId(ifa.getMember().getImUserId());
					list.add(vo);
				}
			}
			
			
		}
		return list;
	}
	
	
	/**
	 * 根据投资人id获取它的好友IFA数据
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public List<String> findInvestorFriendByInvestorId(String investorId) {
		List<String> ifaList = new ArrayList<String>();
		String hql = " FROM WebFriend b WHERE b.fromMember.id =? AND b.checkResult=1";
		List<WebFriend> list = this.baseDao.find(hql, new String[] { investorId }, false);
		if (null!=list&&!list.isEmpty()){
			for(WebFriend each : list){
				MemberBase ifa = each.getToMember();
				if(null!=ifa&&""!=ifa.getId()){
					if(!ifaList.contains(ifa.getId())){
						ifaList.add(ifa.getId());
					}
				}
			}
		}
		return ifaList;
	}
	
	/**
	 * IFA列表添加好友
	 * @param fromMemberId 发起的会员ID
	 * @param toMemberId  关联的会员ID
	 * @param applyMsg  申请信息
	 */
	public String saveWebFriend(String fromMemberId,String toMemberId,String applyMsg) {
		String returnStr = "";
		String hql = "from WebFriend t where t.fromMember.id=? and t.toMember.id=? ";
		List params = new ArrayList();
		params.add(fromMemberId);
		params.add(toMemberId);
		List<WebFriend> list = baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			WebFriend obj = list.get(0);
			if("1".equals(obj.getCheckResult())){
				returnStr = "1";//已经为好友，无须再次添加
				return returnStr;
			}
		}
		//如果不是好友，则可以发送通知
		if("1"!=returnStr){
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
			WebFriend obj = (WebFriend)baseDao.create(webFriend);
			returnStr = obj.getId();
		}
		
		return returnStr;
	}
	
	/**
	 * 通过会员ID查询IFA的客户列表
	 * 
	 * @author zxtan
	 * @date 2017-01-04
	 * @param memberId  IFA会员ID
	 * @param clientType  Buddy
	 * @return list IFA好友列表
	 */
	public List<IfaClientVO> findMyFriendList(String memberId) {
		List<IfaClientVO> voList = new ArrayList<IfaClientVO>();
		List<String> params = new ArrayList<String>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from MemberBase m ");
		hql.append(" inner join MemberIndividual mi on m.id=mi.member.id ");
		hql.append(" inner join WebFriend f on m.id = f.toMember.id ");
		hql.append(" where f.relationships='Buddy' and f.checkResult='1' and f.fromMember.id=? ");
		hql.append(" order by m.nickName ");
		
		
		params.add(memberId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				IfaClientVO vo = new IfaClientVO();
				Object[] objs = (Object[]) list.get(i);
				MemberBase member = (MemberBase) objs[0];
				MemberIndividual detail = (MemberIndividual) objs[1];
								
				vo.setId(member.getId());
				vo.setFullName(member.getNickName());		
								
				String iconUrl = PageHelper.getUserHeadUrl(member.getIconUrl(),detail.getGender());
				vo.setIconUrl(iconUrl);
				
				voList.add(vo);
			}
		}
		
		return voList;
	}

	/**
	 * 获取IFA的总好友数量
	 */
	public int getIfaWebFriend(String toMemberId ) {
		String returnStr = "";
		String hql = "from WebFriend t where  t.toMember.id=? and checkResult = '1'";
		List params = new ArrayList();
		params.add(toMemberId);
		List<WebFriend> list = baseDao.find(hql, params.toArray(), false);
		if(null==list)return 0;
		else return list.size();
	}
	
	/**
	 * 判断二个memberid是否是好友关系
	 */
	@Override
	public Boolean checkTwoMemberIsFriend(String fromMemberId,String toMemberId ) {
		String returnStr = "";
		String hql = "from WebFriend t where  t.fromMember.id=? and t.toMember.id=? and checkResult = '1'";
		List params = new ArrayList();
		params.add(fromMemberId);
		params.add(toMemberId);
		List<WebFriend> list = baseDao.find(hql, params.toArray(), false);
		if(null!=list&&!list.isEmpty())return true;
		else return false;
	}

	/**
	 * 获取好友信息
	 * @author mqzou
	 * @date 2016-09-27
	 * @param id
	 * @return
	 */
	@Override
	public WebFriend findWebFriendById(String id) {
		WebFriend webFriend=(WebFriend)this.baseDao.get(WebFriend.class, id);
		return webFriend;
	}
	
	/**
	 * 复制IFA的好友关系到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaFriend(String fromMemberId,String toMemberId ,MemberBase createBy){
		List<WebFriend> list = findWebFriends(fromMemberId);
		if (null!=list && !list.isEmpty()){
			MemberBase ifa = memberBaseService.findById(toMemberId);
			for (WebFriend f: list){
				//只要通过的好友，关系类型有没要求，已存在的好友不加？
				if ("1".equals(f.getCheckResult()) && !checkTwoMemberIsFriend(toMemberId, f.getToMember().getId())){
					boolean status = false;
					try {
						WebFriend nFriend = new WebFriend();
						BeanUtils.copyProperties(f, nFriend);
						nFriend.setId(null);
						nFriend.setCheckRemark(nFriend.getCheckRemark()+"\n(copy from "+nFriend.getFromMember().getNickName()+")");
						nFriend.setCreateTime(new Date());
						nFriend.setLastUpdate(new Date());
						nFriend.setCheckTime(new Date());//审核时间需更新？
						nFriend.setFromMember(ifa);
						this.baseDao.saveOrUpdate(nFriend);
						
						//旧记录设为冻结状态
//						f.setIsValid("0");
//						this.baseDao.update(f);
	                    
						status = true;
                    } catch (Exception e) {
	                    // TODO: handle exception
                    }
                    
					//保存日志
					IfaMigrateHist hist = new IfaMigrateHist();
					hist.setCreateBy(createBy);
					hist.setCreateTime(new Date());
					hist.setFromMember(f.getFromMember());
					hist.setToMember(ifa);
					hist.setCusMember(f.getToMember());
					hist.setDataType("WebFriend");
					hist.setStatus(status?"1":"0");
					
					ifaMigrateHistService.saveOrUpdate(hist);
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 获取我的朋友
	 * @author wwluo
	 * @date 2017-03-29
	 * @param memberId
	 * @param relationships 好友关系
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MemberVO> getWebFriends(String memberId, String relationships, String langCode, Integer maxResults) {
		List<MemberVO> vos = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT DISTINCT"
					+ " f.toMember"
					+ " FROM"
					+ " WebFriend f"
					+ " WHERE"
					+ " f.checkResult=1"
					+ " AND"
					+ " f.fromMember.id=?"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			if (StringUtils.isNotBlank(relationships)) {
				hql.append(" AND f.relationships=?");
				params.add(relationships);
			}
			hql.append(" ORDER BY f.lastUpdate DESC");
			List<MemberBase> members = this.baseDao.find(hql.toString(), params.toArray(), false, maxResults);
			if(members != null && !members.isEmpty()){
				vos = new ArrayList<MemberVO>();
				for (MemberBase memberBase : members) {
					MemberVO vo = new MemberVO();
					vo.setId(memberBase.getId());
					Integer memberType = memberBase.getMemberType();
					String name = null;
					String iconUrl = null;
					if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == memberType){
						MemberIfa ifa = this.findIfaMember(memberBase.getId());
						if(ifa != null){
							if(CommonConstants.LANG_CODE_EN.equals(langCode)){
					    		name = ifa.getLastName() + " " + ifa.getFirstName();
					    	}else {
					    		name = ifa.getNameChn();
							}
							iconUrl = PageHelper.getUserHeadUrl(memberBase.getIconUrl(), ifa.getGender());
						}else{
							iconUrl = PageHelper.getUserHeadUrl(memberBase.getIconUrl(), null);
						}
						if (StringUtils.isBlank(name)) {
				    		name = memberBase.getNickName();
				    		if (StringUtils.isBlank(name)) {
				        		name = memberBase.getLoginCode();
				        	}
						}
					}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberType){
						MemberIndividual individual = this.findIndividualMember(memberBase.getId());
						if(individual != null){
					    	if(CommonConstants.LANG_CODE_EN.equals(langCode)){
					    		name = individual.getLastName() + " " + individual.getFirstName();
					    	}else {
								name = individual.getNameChn();
							}
					    	iconUrl = PageHelper.getUserHeadUrl(memberBase.getIconUrl(), individual.getGender());
						}else{
							iconUrl = PageHelper.getUserHeadUrl(memberBase.getIconUrl(), null);
						}
						if (StringUtils.isBlank(name)) {
				    		name = individual.getMember().getNickName();
				    		if (StringUtils.isBlank(name)) {
					    		name = individual.getMember().getLoginCode();
							}
						}
					}else{
						name = memberBase.getNickName();
						iconUrl = PageHelper.getUserHeadUrl(memberBase.getIconUrl(), null);
					}
					vo.setName(name);
					vo.setIconUrl(iconUrl);
					vo.setFollowers(getIfaWebFriend(memberBase.getId()));
					vos.add(vo);
				}
			}
		}
		return vos;
	}
	
	/**
	 * 获取独立投资人信息
	 * @author wwluo
	 * @date 2017-03-29
	 */
	private MemberIndividual findIndividualMember(String memberId) {
		MemberIndividual individual = null;
		if (StringUtils.isNotBlank(memberId)) {
			String hql = " FROM MemberIndividual t WHERE t.member.id=?";
			List<MemberIndividual> list = this.baseDao.find(hql, new String[]{memberId}, false);
			if(list != null && !list.isEmpty()){
				individual = list.get(0);
			}
		}
		return individual;
	}
	
	/**
	 * 获取IFA个人信息
	 * @author wwluo
	 * @date 2017-03-29
	 */
	private MemberIfa findIfaMember(String memberId) {
		MemberIfa ifa = null;
		if (StringUtils.isNotBlank(memberId)) {
			String hql = "from MemberIfa t where t.member.id=? ";
			List<MemberIfa> list = this.baseDao.find(hql, new String[]{memberId}, false);
			if(list != null && !list.isEmpty()){
				ifa = (MemberIfa)list.get(0);
			}
		}
		return ifa;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
