package com.fsll.wmes.member.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysCountry;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberFi;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIndividual;

public interface MemberManageServiceForConsole {

	/**
	 * Individual列表查询
	 * @author mqzou
	 * @date 2016-06-28
	 */
	public JsonPaging findAllIndividual(JsonPaging jsonpaging, MemberIndividual memberIndividual, String langCode);

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
	 * Corporate管理列表查询
	 * 
	 * @author mqzou
	 * @date 2016-06-28
	 * @param jsonpaging
	 * @param memberCorporate
	 * @return
	 */
	public JsonPaging findAllCorporate(JsonPaging jsonpaging, MemberCorporate memberCorporate, String langCode);

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
	 * 获取国家实体
	 * 
	 * @param id
	 * @return
	 */
	public SysCountry findCountryById(String id);

	/**
	 * 获取ifa所有列表
	 * modify by mqzou 2016-07-19
	 * @author mqzou
	 * @date 2016-06-30
	 * @param jsonPaging
	 * @param memberIfa
	 * @return
	 */
	public JsonPaging findAllIfa(JsonPaging jsonPaging, MemberIfa memberIfa, String langCode);

	/**
	 * 获取ifa所有列表（包含在ifafirm内的）
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 * @param jsonPaging
	 * @param memberIfa
	 * @return
	 */
	public JsonPaging findAllIfainIfaFirm(JsonPaging jsonPaging, MemberIfa memberIfa, String langCode);

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
	 * 获取ifafirm列表
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 * @return
	 */
	public List<MemberIfafirm> getIfafirmlist();

	/**
	 * 根据Id获取ifafirm信息
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 * @param id
	 * @return
	 */
	public MemberIfafirm findIfafirmById(String id);

	/**
	 * Corporate列表查询
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 * @param jsonPaging
	 * @param memberCorporate
	 * @return
	 */
	public JsonPaging findallCoporate(JsonPaging jsonPaging, MemberCorporate memberCorporate, String langCode);

	/**
	 * Corporate获取一条数据
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 * @param id
	 * @return
	 */
	public MemberCorporate findCorporateById(String id);

	/**
	 * Corporate保存信息
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 * @param memberCorporate
	 * @return
	 */
	public MemberCorporate savecCorporate(MemberCorporate memberCorporate);

	/**
	 * fi列表查询
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 * @param jsonPaging
	 * @param memberFi
	 * @return
	 */
	public JsonPaging findallFi(JsonPaging jsonPaging, MemberFi memberFi, String langCode);

	/**
	 * fi获取一条数据
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 * @param id
	 * @return
	 */
	public MemberFi findFiById(String id);

	/**
	 * fi保存信息
	 * 
	 * @author mqzou
	 * @date 2016-06-30
	 * @param memberFi
	 * @return
	 */
	public MemberFi saveFi(MemberFi memberFi);

	/**
	 * 根据Id获取Distributor信息
	 * 
	 * @author tan
	 * @date 2016-07-07
	 * @param id
	 * @return
	 */
	public MemberDistributor findDistributorById(String id);

	/**
	 * Distributor列表查询
	 * 
	 * @author tan
	 * @date 2016-07-07
	 * @param jsonPaging
	 * @return
	 */
	public List<MemberDistributor> findAllDistributors();

	/**
	 * 删除individual 信息
	 * 
	 * @author mqzou
	 * @date 2016-07-08
	 * @param id
	 * @return
	 */
	public MemberIndividual delteIndividual(String id);

	/**
	 * 删除fi 信息
	 * 
	 * @author mqzou
	 * @date 2016-07-08
	 * @param id
	 * @return
	 */
	public MemberFi deleteMemberFi(String id);

	/**
	 * 删除coporate 信息
	 * 
	 * @author mqzou
	 * @date 2016-07-08
	 * @param id
	 * @return
	 */
	public MemberCorporate delelteCorporate(String id);

	/**
	 * 删除ifa 信息
	 * 
	 * @author mqzou
	 * @date 2016-07-08
	 * @param id
	 * @return
	 */
	public MemberIfa deleteIfa(String id);

	/**
	 * 根据账户id获取ifa信息
	 * @param memeberId
	 * @return
	 */
	public MemberIfa findMemberIfaByMemberId(String memeberId);
	/**
	 * 获取管理员历表
	 * @author mqzou
	 * @date 2016-07-19
	 * @param jsonPaging
	 * @param memberAdmin
	 * @return
	 */
	public JsonPaging findAllAdminMember(JsonPaging jsonPaging,MemberAdmin memberAdmin);
	
	/**
	 * 获取管理员历表(存储过程)
	 * @author mqzou
	 * @date 2016-07-19
	 * @param jsonPaging
	 * @param memberAdmin
	 * @return
	 */
	public JsonPaging findAllAdminMemberPro(JsonPaging jsonPaging,MemberAdmin memberAdmin,String langCode);
	/**
	 * 根据Id获取管理员信息
	 * @author mqzou
	 * @date 2016-07-19
	 * @param id
	 * @return
	 */
	public MemberAdmin findAdminById(String id);
	/**
	 * 保存管理员信息
	 * @author mqzou
	 * @date 2016-07-19
	 * @param memberAdmin
	 * @return
	 */
	public MemberAdmin saveAdmin(MemberAdmin memberAdmin);
	
	/**
	 * 根据memberId获取memberadmin
	 * @author mqzou
	 * @date 2016-07-19
	 * @param memberId
	 * @return
	 */
	public MemberAdmin findAdminByMemberId(String memberId);
	
	/**
	 * 删除一条用户（逻辑删除）
	 * @author mqzou
	 * @date 2016-07-19
	 * @param memberId
	 * @return
	 */
	public boolean deleteMember(String memberId);
	
	/**
	 * 检测登录账号的唯一性
	 * @author mqzou
	 * @date 2016-07-21
	 * @param loginCode
	 * @param userId
	 * @return
	 */
	public boolean checkLoginCode(String loginCode,String userId);
	
	
}
