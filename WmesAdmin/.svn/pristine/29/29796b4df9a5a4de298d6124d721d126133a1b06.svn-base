package com.fsll.core.service;

import java.util.List;
import java.util.Map;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.entity.SysParamType;
import com.fsll.core.vo.SysParamVO;

/***
 * 业务接口类：语言设置管理
 * 
 * @author simon
 * @date 2016-3-17
 */
public interface SysParamService {
	/***
	 * 语言管理列表查询的方法
	 * 
	 * @author simon
	 * @date 2016-3-17
	 * @param site
	 * @param jsonpaging
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonpaging, SysParamConfig paramConfig);

	/***
	 * 根据app用户的id查询用户对象的方法
	 * 
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo
	 * @param jsonpaging
	 * @return
	 */
	public SysParamConfig findParamConfigById(SysParamConfig paramConfig);

	/***
	 * 保存用户对象的方法
	 * 
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo
	 *            app用户对象
	 * @return
	 */
	public SysParamConfig saveOrUpdate(SysParamConfig paramConfig, boolean isAdd);

	/***
	 * 删除用户对象的方法
	 * 
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo
	 *            app用户对象
	 * @return
	 */
	public boolean deleteObject(String ids);

	/***
	 * 根据域名查询站点对象
	 * 
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysParamConfig findById(SysParamConfig paramConfig);

	/***
	 * 根据域名查询站点对象
	 * 
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysParamConfig findByCode(String code);

	/***
	 * 根据站点名称查询站点对象
	 * 
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysParamConfig findBySysParamName(String name);

	/**
	 * 根据应用对象和角色对象加载树的方法，###2015-11-12 修改取得 应用角色 的逻辑 by Michael
	 * 根据用户所在组加载组下所有的节点。
	 * 
	 * @author simon
	 * @date 2016-3-23
	 * @param companyinfo
	 * @param userinfo
	 * @return
	 */
	public String getLoadParamTree();

	/***
	 * 获得所以基础数据类型的列表
	 * 
	 * @author simon
	 * @date 2016-4-12
	 * @return
	 */
	public List<SysParamType> getAllParamTypeList();

	/***
	 * 根据类型的id查询类型对象
	 * 
	 * @author simon
	 * @date 2016-4-12
	 * @param type
	 * @return
	 */
	public SysParamType findParamTypeById(SysParamType type);

	/***
	 * 检查SysParamType是否存在子节点
	 * 
	 * @author simon
	 * @date 2016-4-12
	 * @param type
	 * @return
	 */
	public boolean checkChildrenExist(SysParamConfig paramConfig);

	/****
	 * 修改基础数据状态的方法
	 * 
	 * @author simon
	 * @date 2016-4-12
	 * @param ids
	 * @param status
	 * @return
	 */
	public boolean saveStatus(String ids, String status);

	/***
	 * 根据type查询对象的方法
	 * 
	 * @author michael
	 * @date 2016-6-23
	 * @param typeCode
	 *            参数类型编码
	 * @return list
	 */
	public List<SysParamConfig> findParamConfigByType(String typeCode);
	
	/**
	 * 根据语言和type查询基础数据列表
	 * @author mqzou
	 * @date 2016-07-07
	 * @param langCode
	 * @param typeCode
	 * @return
	 */
	public List<SysParamVO> getParamList(String langCode,String typeCode);
	
	/**
	 * 分页获取基础参数列表
	 * @author michael
	 * @param jsonPaging
	 * @param langCode
	 * @param typeCode
	 * @return
	 */
	public JsonPaging getParamList(JsonPaging jsonPaging, String langCode, String typeCode);
	
	/***
	 * 根据站点名称查询站点对象
	 * @author mjhuang
	 * @param langCode
	 * @param configCode
	 * @return 
	 */
	public SysParamVO findParamByCode(String langCode, String configCode);

	/**
	 * 根据关联类型查询基础数据
	 * @author wwluo
     * @date 2016-08-01
	 */
	public List<Object> findListByCode(String typeCode) ;
	
	/**
	 * 根据基础数据的code获取name值，支持多个（逗号隔开）
	 * @author mqzou	
     * @date 2016-08-22
	 * @param code
	 * @return
	 */
	public String findNameByCode(String code,String langCode) ;
}
