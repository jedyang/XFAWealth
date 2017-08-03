package com.fsll.wmes.member.service;

import java.util.Map;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberFi;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.member.vo.MemberAdminVO;
import com.fsll.wmes.member.vo.MemberBaseVO;

public interface MemberManageServiceForConsole {

	/**
	 * Individual列表查询
	 * @author qgfeng
	 * @date 2016-11-3
	 * @param jsonpaging
	 * @param keyword 关键字
	 * @param langCode 语音标识
	 * @return
	 */
	public JsonPaging findAllIndividual(JsonPaging jsonpaging,MemberBaseVO memberVo,String langCode);
	
	
	/**
	 * 根据id获取一条数据 Individual
	 * 
	 * @author mqzou
	 * @date 2016-06-28
	 * @param id
	 * @return
	 */
	public MemberIndividual findIndividualById(String id);
	
	/**
	 * 保存Individual信息
	 * @author qgfeng
	 * @date 2016-11-04
	 * @param memberIndividual
	 * @return
	 */
	public MemberIndividual saveIndividual(MemberIndividual memberIndividual);
	
	/**
	 * 保存Individual信息
	 * 
	 * @author mqzou
	 * @date 2016-06-29
	 * @param memberIndividual
	 * @return
	 */
	public MemberIndividual saveIndividual(MemberIndividual memberIndividual, MemberBase memberBase);
	
	/**
	 * 删除individual 信息(注：物理删除)
	 * @author mqzou modify qgfeng
	 * @date 2016-07-08
	 */
	public boolean delteIndividual(String id);
	
	
	//-----  end Individual -----------
	
	/**
	 * fi列表查询
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 */
	public JsonPaging findallFi(JsonPaging jsonPaging,MemberBaseVO memberVo, String langCode);
	
	/**
	 * fi获取一条数据
	 * @author mqzou
	 * @date 2016-06-30
	 */
	public MemberFi findFiById(String id);
	
	/**
	 * fi保存信息
	 * @author mqzou
	 * @date 2016-06-30
	 */
	public MemberFi saveFi(MemberFi memberFi);
	
	/**
	 * 删除fi 信息（注：物理删除）
	 * @author mqzou modify qgfeng 
	 * @date 2016-07-08
	 */
	public boolean deleteMemberFi(String id);
	
	//-------end Fi---------
	
	/**
	 * Corporate列表查询
	 * @author qgfeng
	 * @date 2016-12-6
	 */
	public JsonPaging findallCoporate(JsonPaging jsonPaging,MemberBaseVO memberVo, String langCode);

	/**
	 * Corporate获取一条数据
	 * @author mqzou
	 * @date 2016-06-30
	 */
	public MemberCorporate findCorporateById(String id);
	
	/**
	 * Corporate保存信息
	 * @author mqzou
	 * @date 2016-06-30
	 */
	public MemberCorporate savecCorporate(MemberCorporate memberCorporate);
	
	/**
	 * 删除coporate 信息(物理删除)
	 * @author mqzou modify qgfeng
	 * @date 2016-07-08
	 */
	public boolean delelteCorporate(String id);
	
	//-------end Corporate---------
	
	/**
	 * 获取ifa所有列表
	 * @author qgfeng
	 * @date 2016-12-6
	 */
	public JsonPaging findAllIfa(JsonPaging jsonPaging,MemberBaseVO memberVo, String langCode);

	/**
	 * 根据Id获取ifa信息
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 * @param id
	 * @return
	 */
	public MemberIfa finIfaById(String id);
	
	/**
	 * 保存ifa信息
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 * @return
	 */
	public MemberIfa saveIfa(MemberIfa memberIfa);
	
	/**
	 * 删除ifa 信息(物理删除)
	 * @author mqzou modify qgfeng
	 * @date 2016-07-08
	 */
	public boolean deleteIfa(String id);
	
	//-------end MemberIfa---------

	/**
	 * 获取管理员历表(存储过程)
	 * @author mqzou
	 * @date 2016-07-19
	 */
	public JsonPaging findAllAdminMemberPro(JsonPaging jsonPaging,MemberAdmin memberAdmin,String langCode);
	
	
	//-----------MemberAdmin----------
	
	/**
	 * 获取ifaFime(或者Distributor)管理员历表
	 * type:1:ifafrim,2:distributor
	 * @author qgfeng
	 * @date 2016-12-2
	 * 
	 */
	public JsonPaging findMemberAdmin(JsonPaging jsonPaging,MemberAdminVO memberAdmin,String langCode);
	
	/**
	 * 根据Id获取管理员信息
	 * @author mqzou
	 * @date 2016-07-19
	 */
	public MemberAdmin findAdminById(String id);
	/**
	 * 更新或保存管理员信息
	 * @author qgfeng
	 * @date 2016-12-7
	 * @param memberAdmin
	 * @return
	 */
	public MemberAdmin saveOrUpdateAdmin(MemberAdmin memberAdmin);
	
	/**
	 * 根据memberId获取memberadmin
	 * @author mqzou
	 * @date 2016-07-19
	 */
	public MemberAdmin findAdminByMemberId(String memberId);
	
	/**
	 * 删除MemberAdmin(物理删除)
	 * @author qgfeng
	 * @date 2016-12-12
	 */
	public void deleteMemberAdmin(String id);
	
	//-------公共---------
	
	/**
	 * 根据id查找memberBase
	 * @author qgfeng
	 * @date 2016-12-13
	 * @return
	 */
	public MemberBase findMemberBaseById(String id);
	
	/**
	 * 删除一条用户（逻辑删除公共）
	 * @author mqzou
	 * @date 2016-07-19
	 */
	public boolean deleteMember(String memberId);
	
	/**
	 * 检测登录账号的唯一性(公共)
	 * @author mqzou
	 * @date 2016-07-21
	 */
	public boolean checkLoginCode(String loginCode,String memberId);

	

	/**
	 * 
	 * @author rqwang
	 * @date 2017-06-08
	 */
	public JsonPaging disFindMemberAdmin(JsonPaging jsonPaging, String disId,
			MemberAdminVO memberAdmin, String loginLangFlag);

	/**
	 * 获取用户的分组信息
	 * @author wwluo
	 * @date 2017-06-09
	 * @param memberIds
	 * @param langCode
	 * @return Map<String, Object>
	 */
	public Map<String, Object> findMemberGroupInfo(String memberIds,
			String langCode);
	
	
}
