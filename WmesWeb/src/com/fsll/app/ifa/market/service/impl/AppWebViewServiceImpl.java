package com.fsll.app.ifa.market.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.app.ifa.market.service.AppWebViewService;
import com.fsll.app.ifa.market.vo.AppSelectMemberVO;
import com.fsll.app.ifa.market.vo.AppWebViewDetailVO;
import com.fsll.app.ifa.market.vo.AppWebViewVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.ifa.vo.IfaClientVO;
/**
 * 设置权限接口服务实现
 * @author zxtan
 * @date 2017-03-28
 */
@Service("appIfaMarketWebViewService")
public class AppWebViewServiceImpl extends BaseService implements
		AppWebViewService {


	/**
	 * 获取我的（策略，组合...）权限
	 * @param memberId
	 * @param relateId
	 * @param langCode
	 * @return
	 */
	public AppWebViewVO findWebView(String memberId, String relateId,String langCode) {
		AppWebViewVO vo = new AppWebViewVO();
		String hql = "from WebView m where m.fromMember.id=? and m.relateId=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add(relateId);
		List<WebView> list = baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){
			WebView info = list.get(0);
			vo.setId(info.getId());
			vo.setModuleType(info.getModuleType());
			vo.setRelateId(relateId);
			vo.setScopeFlag(info.getScopeFlag());
			vo.setClientFlag(info.getClientFlag());
			vo.setProspectFlag(info.getProspectFlag());
			vo.setBuddyFlag(info.getBuddyFlag());
			vo.setColleagueFlag(info.getColleagueFlag());
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), CommonConstants.FORMAT_DATE_TIME));
			
			if("-1".equals(info.getClientFlag())){
				List<AppWebViewDetailVO> subList = findWebViewDetailList(memberId,info.getId(), "Client",langCode);
				vo.setClientList(subList);
			}
			if("-1".equals(info.getProspectFlag())){
				List<AppWebViewDetailVO> subList = findWebViewDetailList(memberId,info.getId(), "Prospect",langCode);
				vo.setProspectList(subList);
			}
			if("-1".equals(info.getBuddyFlag())){
				List<AppWebViewDetailVO> subList = findWebViewDetailList(memberId,info.getId(), "Buddy",langCode);
				vo.setBuddyList(subList);
			}
			if("-1".equals(info.getColleagueFlag())){
				List<AppWebViewDetailVO> subList = findWebViewDetailList(memberId,info.getId(), "Team",langCode);
				vo.setColleagueList(subList);
			}			
		}
		return vo;
	}
	
	/**
	 * 获取权限设定的人员
	 * @param viewId
	 * @param type
	 * @param langCode
	 * @return
	 */
	private List<AppWebViewDetailVO> findWebViewDetailList(String ifaMemberId,String viewId,String type,String langCode){
		List<AppWebViewDetailVO> voList = new ArrayList<AppWebViewDetailVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from WebViewDetail m ");
		hql.append(" inner join MemberBase b on b.id=m.toMember.id ");
		if("Client".equalsIgnoreCase(type) || "Prospect".equalsIgnoreCase(type)){
			hql.append(" inner join CrmCustomer c on c.member.id=b.id ");
			hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		}
		
		if("Team".equalsIgnoreCase(type)){
			hql.append(" inner join MemberIfa i on i.member.id=b.id ");
		}		

		hql.append(" where m.view.id=? and m.type=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(viewId);
		params.add(type);
		
		if("Client".equalsIgnoreCase(type) || "Prospect".equalsIgnoreCase(type)){
			hql.append(" and i.member.id=? ");
			params.add(ifaMemberId);
		}

		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				WebViewDetail info = (WebViewDetail) objs[0];
				
				AppWebViewDetailVO vo = new AppWebViewDetailVO();
				vo.setId(info.getId());
				vo.setType(info.getType());
				MemberBase member = info.getToMember();
				vo.setToMemberId(member.getId());
				vo.setToMemberName(member.getNickName());
				
				if("Prospect".equalsIgnoreCase(type)){
					CrmCustomer customer = (CrmCustomer) objs[2];	
					vo.setToMemberName(customer.getNickName());
				}
				
				if("Client".equalsIgnoreCase(type) ){					
					String name = getCommonMemberName(member.getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
					vo.setToMemberName(name);
				}

				if("Team".equalsIgnoreCase(type)){
					MemberIfa ifa = (MemberIfa) objs[2];
					String ifaName = getCommonMemberName(ifa.getMember().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
					vo.setToMemberName(ifaName);
				}
				voList.add(vo);
			}
		}
		return voList;
	}
	
	/**
	 * 保存我的（策略，组合...）权限
	 * @param memberId
	 * @param relateId
	 * @param langCode
	 * @return
	 */
	public boolean updateWebView(JSONObject webViewObject) {
		try {
			String memberId = webViewObject.optString("memberId", "");
			String relateId = webViewObject.optString("relateId", "");
			String moduleType = webViewObject.optString("moduleType", "");
			String scopeFlag = webViewObject.optString("scopeFlag", "");
			String clientFlag = webViewObject.optString("clientFlag", "");
			String prospectFlag = webViewObject.optString("prospectFlag", "");
			String buddyFlag = webViewObject.optString("buddyFlag", "");
			String colleagueFlag = webViewObject.optString("colleagueFlag", "");
					
			String hql = "from WebView m where m.fromMember.id=? and m.relateId=? ";
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			params.add(relateId);
			List<WebView> list = baseDao.find(hql, params.toArray(), false);
			WebView webView = new WebView();
			if(null != list && !list.isEmpty()){
				webView = list.get(0);	
				
				String delHql = "delete WebViewDetail d where d.view.id=? ";
				baseDao.updateHql(delHql, new Object[]{webView.getId()});			
			}else {
				webView.setId(null);
				webView.setCreateTime(new Date());
			}
			
			webView.setModuleType(moduleType);
			webView.setRelateId(relateId);
			webView.setScopeFlag(scopeFlag);
			webView.setClientFlag(clientFlag);
			webView.setProspectFlag(prospectFlag);
			webView.setBuddyFlag(buddyFlag);
			webView.setColleagueFlag(colleagueFlag);
			webView.setLastUpdate(new Date());
			
			webView = (WebView) baseDao.saveOrUpdate(webView);
			
			
			if("-1".equals(clientFlag)){				
				saveWebViewDetail(webView, "Client", webViewObject.optJSONArray("clientList"));
			}
			if("-1".equals(prospectFlag)){
				saveWebViewDetail(webView, "Prospect", webViewObject.optJSONArray("prospectList"));				
			}
			if("-1".equals(buddyFlag)){
				saveWebViewDetail(webView, "Buddy", webViewObject.optJSONArray("buddyList"));				
			}
			if("-1".equals(colleagueFlag)){
				saveWebViewDetail(webView, "Team", webViewObject.optJSONArray("colleagueList"));				
			}	
		
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	/**
	 * 获取权限设定的人员
	 * @param viewId
	 * @param type
	 * @param langCode
	 * @return
	 */
	private boolean saveWebViewDetail(WebView view,String type,JSONArray array){
		if(null != array && !array.isEmpty()){
			for (int i = 0; i < array.size(); i++) {
				String toMemberId = array.getJSONObject(i).optString("toMemberId", "");
				MemberBase toMember = (MemberBase) baseDao.get(MemberBase.class, toMemberId);
				if(null != toMember){
					WebViewDetail info = new WebViewDetail();
					info.setId(null);
					info.setType(type);
					info.setView(view);
					info.setToMember(toMember);					
					baseDao.create(info);
				}				
			}
		}

		return true;
	}
	

	
	
	
	/**
	 * 获取待选择用户
	 * @author zxtan
	 * @date 2017-03-28
	 * @param memberId IfaMemberId
	 * @param userType 用户类型 prospect,client,buddy,colleague
	 * @param langCode 语言
	 * @return
	 */
	public List<AppSelectMemberVO> findToBeSelectedList(String memberId, String userType,String langCode){
		List<AppSelectMemberVO> voList = new ArrayList<AppSelectMemberVO>();
		
		if("prospect".equalsIgnoreCase(userType)){
			voList = findMyClientList(memberId, "0",langCode);
		}else if("client".equalsIgnoreCase(userType)){
			voList = findMyClientList(memberId, "1",langCode);
		}else if("buddy".equalsIgnoreCase(userType)){
			voList = findMyBuddyList(memberId);
		}else if("colleague".equalsIgnoreCase(userType)){
			voList = findMyColleagueList(memberId, langCode);
		}
		
		return voList;
	}
	
	
	/**
	 * 通过会员ID查询IFA的客户列表
	 * 用户类型：prospect,existingc client
	 * @author zxtan
	 * @date 2017-03-28
	 * @param memberId  IFA会员ID
	 * @param clientType  1：正式，0：潜在 
	 * @return list IFA客户列表
	 */
	private List<AppSelectMemberVO> findMyClientList(String memberId, String clientType,String langCode) {
		List<AppSelectMemberVO> voList = new ArrayList<AppSelectMemberVO>();
		List<String> params = new ArrayList<String>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from MemberBase m ");
		hql.append(" inner join MemberIndividual mi on m.id=mi.member.id ");
		hql.append(" inner join CrmCustomer c on m.id=c.member.id ");
		hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		hql.append(" where i.member.id=? ");

		params.add(memberId);
		
		if("0,1,".indexOf(clientType)>-1 ){
			hql.append(" and c.clientType =? ");
			params.add(clientType);
		}else{
			hql.append(" and 1=2 ");
		}
		
		hql.append(" order by c.clientType desc, c.nickName ");
				
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppSelectMemberVO vo = new AppSelectMemberVO();
				Object[] objs = (Object[]) list.get(i);
				MemberBase member = (MemberBase) objs[0];
//				MemberIndividual detail = (MemberIndividual) objs[1];
				CrmCustomer customer = (CrmCustomer) objs[2];
				vo.setMemberId(member.getId());
								
				if("1".equals(customer.getClientType())){
					//existing
					vo.setNickName(getCommonMemberName(member.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				}else {
					//prospect
					String fullName = customer.getNickName();
					if(StringUtils.isBlank(fullName)){
						fullName = getCommonMemberName(member.getId(), langCode, CommonConstants.MEMBER_NAME_NICKNAME);
					}
					vo.setNickName(fullName);
				}
				
				//暂时先用base表中
//				String iconUrl = member.getIconUrl();
//				iconUrl = PageHelper.getUserHeadUrl(iconUrl,detail.getGender());				
//				vo.setIconUrl(iconUrl);
				
				voList.add(vo);
			}
		}
		
		return voList;
	}
	
	/**
	 * 通过会员ID查询IFA的同事列表
	 * 用户类型：Colleague
	 * @author zxtan
	 * @date 2017-03-28
	 * @param memberId  IFA会员ID
	 * @param langCode   
	 * @return list IFA同事列表
	 */
	private List<AppSelectMemberVO> findMyColleagueList(String memberId, String langCode) {
		List<AppSelectMemberVO> voList = new ArrayList<AppSelectMemberVO>();
		List<String> params = new ArrayList<String>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from MemberBase m ");
		hql.append(" inner join MemberIfa mi on m.id=mi.member.id ");
		hql.append(" inner join IfafirmTeamIfa f on mi.id = f.ifa.id ");
		hql.append(" where m.id !=? and f.team.id in ( ");
		hql.append(" select ti.team.id from IfafirmTeamIfa ti where ti.ifa.member.id=? ");
		hql.append(" ) ");
		
		if("sc".equalsIgnoreCase(langCode) || "tc".equalsIgnoreCase(langCode)){
			hql.append(" order by mi.nameChn ");
		}else {
			hql.append(" order by mi.firstName ");
		}
				
		params.add(memberId);
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppSelectMemberVO vo = new AppSelectMemberVO();
				Object[] objs = (Object[]) list.get(i);
				MemberIfa ifa = (MemberIfa) objs[1];
								
				vo.setMemberId(ifa.getMember().getId());				
				String ifaName = getCommonMemberName(ifa.getMember().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
				vo.setNickName(ifaName);								
//				String iconUrl = PageHelper.getUserHeadUrl(ifa.getMember().getIconUrl(),ifa.getGender());
//				vo.setIconUrl(iconUrl);
				voList.add(vo);
			}
		}
		
		return voList;
	}
	
	
	/**
	 * 通过会员ID查询IFA的客户列表
	 * 
	 * @author zxtan
	 * @date 2017-03-28
	 * @param memberId  IFA会员ID
	 * @param clientType  Buddy
	 * @return list IFA好友列表
	 */
	private List<AppSelectMemberVO> findMyBuddyList(String memberId) {
		List<AppSelectMemberVO> voList = new ArrayList<AppSelectMemberVO>();
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
				AppSelectMemberVO vo = new AppSelectMemberVO();
				Object[] objs = (Object[]) list.get(i);
				MemberBase member = (MemberBase) objs[0];
//				MemberIndividual detail = (MemberIndividual) objs[1];								
				vo.setMemberId(member.getId());
				vo.setNickName(member.getNickName());							
				
				voList.add(vo);
			}
		}
		
		return voList;
	}

}
