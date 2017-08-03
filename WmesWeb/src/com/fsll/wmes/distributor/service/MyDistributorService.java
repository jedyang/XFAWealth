package com.fsll.wmes.distributor.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.member.vo.MemberAdminVO;
/**
 * 工作台我的代理商管理接口（代理商管理员）
 * @author qgfeng
 * @date 2016-12-9
 * @return
 */
public interface MyDistributorService {

	/**
	 * 获取distributor管理员历表
	 * @author qgfeng
	 * @date 2016-12-2
	 * 
	 */
	public JsonPaging findDisMember(JsonPaging jsonPaging,MemberAdminVO adminVo);
	/**
	 * 查找 distributorMember
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	public MemberBase getDisMemberById(String id);
	/**
	 * 根据memberBase ID 及distributorId查找
	 * @author qgfeng
	 * @date 2016-12-28
	 * @param firmId 代理公司id
	 * @param memberId 人员id
	 */
	public MemberAdmin getDisMemberByMid(String distributorId,String memberId);
	/**
	 * 保存distributor Member
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	public MemberAdmin saveOrUpdateDisMember(MemberAdmin disMember);
	/**
	 * 删除distributor Member 级联删除MemberBase
	 * @author qgfeng
	 * @date 2016-12-28
	 * @param ids 代理商ids
	 */
	public boolean deleteDistByIds(String ids);

}
