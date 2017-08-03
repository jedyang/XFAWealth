package com.fsll.wmes.group.service;

import java.util.List;
import java.util.Map;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysAdmin;
import com.fsll.wmes.entity.MemberGroup;
import com.fsll.wmes.group.vo.MemberGroupVO;

public interface MemberGroupService {

	/**
	 *  客户分组数据获取
	 * @author wwluo
	 * @date 2017-06-01
	 * @param jsonPaging
	 * @param langCode
	 * @return JsonPaging
	 */
	public JsonPaging getGroups(JsonPaging jsonPaging, String langCode, String isValid);
	
	/**
	 *  客户分组实体获取
	 * @author wwluo
	 * @date 2017-06-01
	 * @param id member_group.id
	 * @return MemberGroup
	 */
	public MemberGroup findById(String id);

	/**
	 *  客户分组实体保存
	 * @author wwluo
	 * @date 2017-06-01
	 * @param group member_group 实体
	 * @return MemberGroup
	 */
	public MemberGroup save(MemberGroup group);

	/**
	 *  客户组成员数据获取
	 * @author wwluo
	 * @date 2017-06-01
	 * @param jsonPaging
	 * @param groupId member_group.id
	 * @param keyWord 关键字模糊查询（客户名称，投资账户）
	 * @param langCode
	 * @return JsonPaging
	 */
	public JsonPaging getGroupDetails(JsonPaging jsonPaging, String groupId, String keyWord, String langCode);

	/**
	 *  客户分组信息保存
	 * @author wwluo
	 * @date 2017-06-01
	 * @param group member_group 实体
	 * @param admin sys_admin 当前管理员
	 * @return Map<String, Object>
	 */
	public Map<String, Object> saveGroup(MemberGroup group, SysAdmin admin);

	/**
	 *  客户分组状态修改
	 * @author wwluo
	 * @date 2017-06-01
	 * @param id member_group.id
	 * @param isValid member_group.is_valid
	 * @return Map<String, Object>
	 */
	public Map<String, Object> updateGroup(String id, String isValid);

	/**
	 *  客户分组删除成员
	 * @author wwluo
	 * @date 2017-06-01
	 * @param id member_group_detail.id
	 * @return Boolean
	 */
	public Boolean delete(String id);

	/**
	 *  客户组添加成员(支持多个组分别加多个成员)
	 * @author wwluo
	 * @date 2017-06-01
	 * @param ids member_base.id字符串
	 * @param groupIds member_group.id字符串
	 * @return Map<String, Object>
	 */
	public Map<String, Object> addMember(String ids, String groupIds);

	/**
	 *  获取所有开户成功的客户
	 * @author wwluo
	 * @date 2017-06-01
	 * @param jsonPaging
	 * @param keyWord 关键字过滤（客户名称，投资账户）
	 * @param langCode
	 * @return JsonPaging
	 */
	public JsonPaging getClients(JsonPaging jsonPaging, String keyWord, String langCode);

	/**
	 *  获取会员属于的所有小组
	 * @author wwluo
	 * @date 2017-06-01
	 * @param memberId
	 * @return List<MemberGroup>
	 */
	public List<MemberGroup> getGroupsByMember(String memberId);
	
	/**
	 *  客户分组数据获取
	 * @author wwluo
	 * @date 2017-06-01
	 * @param langCode
	 * @return List<MemberGroupVO>
	 */
	public List<MemberGroupVO> getGroups(String langCode, String isValid);
	
	/**
	 *  获取分组内成员数量
	 * @author wwluo
	 * @date 2017-06-01
	 * @param groupId
	 * @return Integer 
	 */
	public Integer getGroupMemberCount(String groupId);
}
