package com.fsll.wmes.ifafirm.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.IfafirmDistributor;
import com.fsll.wmes.ifafirm.vo.IfaFrimDistributorVo;
import com.fsll.wmes.ifafirm.vo.IfafirmDistributorVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;

public interface IfaFirmForDistributorService {

	 /**
    * 分页查询记录
    * @param jsonPaging 分页参数
    * @param infoVo 查询参数
	 * @return
    */
	public JsonPaging findAll(JsonPaging jsonPaging, IfafirmDistributorVO infoVo, String langCode);
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public IfafirmDistributor findById(String id);
	
	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */
	public IfafirmDistributorVO findVoById(String id, String langCode);
	
	/**
	 * 增加或者修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public IfafirmDistributor saveOrUpdate(IfafirmDistributor info);
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);
	
	/**
    * 通过代理商分页查询顾问公司记录
    * @param distributorId 代理商id
	 * @return
    */
	public List findIfafirmByDistributor(String distributorId, String langCode);
	
	/**
    * 通过代理商分页查询IFA
    * @param distributorId 代理商id
	 * @return
    */
	public List findIfaByDistributor(String distributorId);
	
	/**
	 * 获取ifa公司分页数据
	 * @author scshi_u330p
	 * @param jsonPaging 
	 * @date 20170102
	 * */
	public JsonPaging loadIfaFirmList(IfaFrimDistributorVo svo, String language, JsonPaging jsonPaging);
	
	/***
	 * 加载ifaFirm公司信息
	 * @author scshi_u330p
	 * @date 20170109
	 * */
	public MemberIfafirmVO loadIfrFirmInfo(String id, String langCode);
	
	/**
	 * 获取ifafirm公司产品分页信息
	 * @author scshi_u330p
	 * @return 
	 * @date 20170109
	 * */
	public JsonPaging loadFirmProductJson(String firmId,String langCode,JsonPaging jsonPaging);

}
