package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.WebChat;
import com.fsll.wmes.web.service.WebChatService;
import com.fsll.wmes.web.vo.WebChatMemberVO;

/**
 * 聊天记录接口实现
 * @author mqzou 2017-02-08
 *
 */
@Service("webChatService")
public class WebChatServiceImpl  extends BaseService implements WebChatService{

	/**
	 * 写入聊天记录
	 * @author mqzou 2017-02-08
	 * @param webChat
	 * @return
	 */
	@Override
	public WebChat addWebChat(WebChat webChat) {
		webChat=(WebChat)baseDao.saveOrUpdate(webChat);
		return webChat;
	}

	/**
	 * 获取IFA的客户列表
	 * @author mqzou 2017-02-09
	 * @param ifaMemberid
	 * @return
	 */
	@Override
	public List<WebChatMemberVO> findInvestorForIfa(String ifaId) {
		StringBuilder hql=new StringBuilder() ;
		hql.append(" select i from CrmCustomer c ");
		hql.append(" left join MemberBase b on c.member.id=b.id");
		hql.append(" left join MemberIndividual i on b.id=i.member.id where c.ifa.id=? and c.isValid='1'");
		List<Object> params=new ArrayList<Object>();
		params.add(ifaId);
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		List<WebChatMemberVO> list=new ArrayList<WebChatMemberVO>();
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				MemberIndividual individual=(MemberIndividual)it.next();
				if(null==individual)
					continue;
				WebChatMemberVO vo=new WebChatMemberVO();
				vo.setGender(individual.getGender());
				vo.setIconUrl(individual.getMember().getImNickUrl());
				vo.setMemberId(individual.getMember().getId());
				vo.setNickName(individual.getMember().getImNickName());
				vo.setUserId(individual.getMember().getImUserId());
				
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 获取ifa的同事列表
	 * @author mqzou 2017-02-09
	 * @param ifaId
	 * @return
	 */
	@Override
	public List<WebChatMemberVO> findColleagueForIfa(String ifaId,String langCode) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from IfafirmTeamIfa r where r.team.id in (select i.team.id from IfafirmTeamIfa i where i.ifa.id=?)");
		List<Object> params=new ArrayList<Object>();
		params.add(ifaId);
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		List<WebChatMemberVO> list=new ArrayList<WebChatMemberVO>();
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				IfafirmTeamIfa object = (IfafirmTeamIfa) it.next();
				MemberIfa ifa=object.getIfa();
				WebChatMemberVO vo=new WebChatMemberVO();
				vo.setGender(ifa.getGender());
				vo.setIconUrl(ifa.getMember().getImNickUrl());
				vo.setMemberId(ifa.getMember().getId());
				String nickName=getCommonMemberName(ifa.getMember().getId(), langCode, "2");
				vo.setNickName(nickName);
				vo.setUserId(ifa.getMember().getImUserId());
				vo.setEmail(ifa.getMember().getEmail());
				vo.setMobileNumber(ifa.getMember().getMobileNumber());
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 获取投资人的ifa
	 * @author mqzou 2017-02-09
	 * @param memberId
	 * @return
	 */
	@Override
	public List<WebChatMemberVO> findIfaForInvestor(String memberId) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from CrmCustomer c where c.isValid='1' and c.member.id=?");
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		List<WebChatMemberVO> list=new ArrayList<WebChatMemberVO>();
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				CrmCustomer object = (CrmCustomer) it.next();
				MemberIfa ifa=object.getIfa();
				WebChatMemberVO vo=new WebChatMemberVO();
				vo.setGender(ifa.getGender());
				vo.setIconUrl(ifa.getMember().getImNickUrl());
				vo.setMemberId(ifa.getMember().getId());
				vo.setNickName(ifa.getMember().getImNickName());
				vo.setUserId(ifa.getMember().getImUserId());
				list.add(vo);
				
			}
		}
		return list;
	}

	/**
	 * 获取所有需要初始化聊天信息的用户
	 * @author mqzou  2017-02-27
	 * @return
	 */
	@Override
	public List<MemberBase> findAllMember() {
		StringBuilder hql=new StringBuilder();
		hql.append(" from MemberBase r where (r.imUserId is null or r.imUserId='')  and r.isValid='1' and r.status='1' ");
		List resultList=this.baseDao.find(hql.toString(), null, false);
		List<MemberBase> list=new ArrayList<MemberBase>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				MemberBase member = (MemberBase) it.next();
				list.add(member);
				
			}
		}
		return list;
	}
	
	
}
