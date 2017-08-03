package com.fsll.wmes.web.service;

import java.util.List;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.WebChat;
import com.fsll.wmes.entity.WebFriend;
import com.fsll.wmes.ifa.vo.IfaClientVO;
import com.fsll.wmes.ifa.vo.MyBuddyVO;
import com.fsll.wmes.member.vo.MemberVO;
import com.fsll.wmes.web.vo.WebChatMemberVO;
/**
 * 朋友记录接口
 * @author michael 2017-02-08
 *
 */
public interface WebFriendService {
	/**
	 * 通过会员ID查询好友
	 * @author michael
	 * @param fromMemberId 会员ID
	 * @return list 好友列表
	 */
	public List<WebFriend> findWebFriends(String fromMemberId);
	
	/**
	 * 通过会员ID和类型查询好友
	 * @author mqzou 2016-12-08
	 * @param fromMemberId 会员ID
	 * @param type 好友类型
	 * @return list 好友列表
	 */
	public List<WebFriend> findWebFriendsByType(String fromMemberId,String type,int limit);

	/**
	 * 通过会员ID查询IFA好友
	 * @author michael
	 * @param fromMemberId 会员ID
	 * @return list IFA好友列表
	 */
	public List<MemberIfa> findIfaFriends(String fromMemberId);
	
	
	/**
	 * 获取webFriend
	 * @author mqzou 2017-02-16
	 * @param fromMemberId
	 * @param toMemberId
	 * @return
	 */
	public WebFriend getWebFriend(String fromMemberId,String toMemberId,String type);
	
	/**
	 * 添加/取消好友
	 * @param fromMemberId 发起的会员ID
	 * @param toMemberId  关联的会员ID
	 * @param applyMsg  申请信息
	 * @param OpType Add-添加;Delete-删除
	 * @return
	 */
	public int saveWebFriendMess(String fromMemberId,String toMemberId,String applyMsg,String opType);

	/**
	 * 获取我的好友列表
	 * @author mqzou
	 * @date 2016-09-27
	 * @param webFriend
	 * @return
	 */
	public List<MyBuddyVO> findFriends(WebFriend webFriend,String langCode);
	
	/**
	 * 获取好友信息
	 * @author mqzou
	 * @date 2016-09-27
	 * @param id
	 * @return
	 */
	public WebFriend findWebFriendById(String id);
	
	/**
	 * 更新好友信息状态
	 * @author mqzou
	 * @date 2016-09-27
	 * @param webFriend
	 * @return
	 */
	public WebFriend updateWebFriend(WebFriend webFriend);
	
	/**
	 * IFA列表添加好友
	 * @author 林文伟
	 * @param fromMemberId 发起的会员ID
	 * @param toMemberId  关联的会员ID
	 * @param applyMsg  申请信息
	 */
	public String saveWebFriend(String fromMemberId,String toMemberId,String applyMsg);
	
	/**
	 * 获取ifa朋友
	 * @author zxtan 2017-01-04
	 * @param memberId
	 * @return
	 */
	public List<IfaClientVO> findMyFriendList(String memberId);
	
	/**
	 * 判断二个memberid是否是好友关系
	 */
	public Boolean checkTwoMemberIsFriend(String fromMemberId,String toMemberId );
	
	/**
	 * 根据投资人id获取它的好友IFA数据
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public List<String> findInvestorFriendByInvestorId(String investorId);
	
	/**
	 * 获取IFA的总好友数量
	 */
	public int getIfaWebFriend(String toMemberId );
	
	/**
	 * 复制IFA的好友关系到另一IFA
	 */
	public Boolean migrateIfaFriend(String fromMemberId,String toMemberId ,MemberBase createBy);

	/**
	 * 获取我的朋友
	 * @author wwluo
	 * @date 2017-03-29
	 * @param memberId
	 * @param relationships 好友关系
	 * @param maxResults 最多返回结果数量
	 * @return
	 */
	public List<MemberVO> getWebFriends(String memberId, String relationships, String langCode, Integer maxResults);
}
