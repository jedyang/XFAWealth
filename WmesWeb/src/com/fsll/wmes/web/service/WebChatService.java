package com.fsll.wmes.web.service;

import java.util.List;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebChat;
import com.fsll.wmes.web.vo.WebChatMemberVO;
/**
 * 聊天记录接口
 * @author mqzou 2017-02-08
 *
 */
public interface WebChatService {

	/**
	 * 写入聊天记录
	 * @author mqzou 2017-02-08
	 * @param webChat
	 * @return
	 */
	public WebChat addWebChat(WebChat webChat);
	
	/**
	 * 获取IFA的客户列表
	 * @author mqzou 2017-02-09
	 * @param ifaMemberid
	 * @return
	 */
	public List<WebChatMemberVO> findInvestorForIfa(String ifaId);
	
	/**
	 * 获取ifa的同事列表
	 * @author mqzou 2017-02-09
	 * @param ifaId
	 * @return
	 */
	public List<WebChatMemberVO> findColleagueForIfa(String ifaId,String langCode);
	
	/**
	 * 获取投资人的ifa
	 * @author mqzou 2017-02-09
	 * @param memberId
	 * @return
	 */
	public List<WebChatMemberVO> findIfaForInvestor(String memberId);
	
	/**
	 * 获取所有需要初始化聊天信息的用户
	 * @author mqzou  2017-02-27
	 * @return
	 */
	public List<MemberBase> findAllMember();
}
