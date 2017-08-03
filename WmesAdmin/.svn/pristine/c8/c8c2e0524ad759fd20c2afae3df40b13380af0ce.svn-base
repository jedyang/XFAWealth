package com.fsll.wmes.web.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.DistributorOrg;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;

public interface DistributorService {
	/***
	 * 代理商列表查询的方法
	 * @author 林文伟
	 * @date 2016-07-01
	 * @param jsonpaging
	 * @param emailLog
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonpaging, MemberDistributor list ,String langCode);
	
	/***
     * 获取代理商的组织架构数据
     * @author 林文伟
     * @date 2016-06-29
     */

	public List<DistributorOrg> findDistributorOrgAll(String distributorId) ;
	
	/**
	 * 通过id获取代理商主数据
	 * @param id
	 * @return
	 */
	public MemberDistributor findDistributorById(String id);
	
	/**
	 * 获取所有的代理商
     * @author mqzou
     * @date 2016-07-19
	 * @return
	 */
	public List<MemberDistributor> findAllDistributor();
	
	/***
     * 保存代理商信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public MemberDistributor saveOrUpdate(MemberDistributor memberDistributor);
	
	/***
     * 通过ID获取单个代理商信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public MemberDistributor getMemberDistributor(String id) ;
	
	/***
     * 删除代理商
     * @author 林文伟
     * @date 2016-0-20
     */
	public int delDistributor(String id) ;
	
	/***
     * 获取管理员
     * @author 林文伟
     * @date 2016-06-29
     */
	public MemberAdmin findDistributorMemberAdmin(String memberId);
}


