package com.fsll.core.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysParamType;
/***
 * 业务接口类：语言设置管理
 * @author simon
 * @date 2016-3-17
 */
public interface SysParamTypeService {
	/***
	 * 语言管理列表查询的方法
	 * @author simon
	 * @date 2016-3-17
	 * @param site
	 * @param jsonpaging
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonpaging,SysParamType paramType);
	/***
	 * 根据app用户的id查询用户对象的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo
	 * @param jsonpaging
	 * @return
	 */
	public SysParamType findParamTypeById(SysParamType paramType);
	 
	/***
	 * 保存用户对象的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo app用户对象
	 * @return
	 */
	public SysParamType saveOrUpdate(SysParamType paramType,boolean isAdd);
	/***
	 * 删除用户对象的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo app用户对象
	 * @return
	 */
	public boolean deleteObject(String ids);
	/***
	 * 根据域名查询站点对象
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysParamType findById(SysParamType paramType);
	/***
	 * 根据域名查询站点对象
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysParamType findByCode(String code);
	/***
	 * 根据站点名称查询站点对象
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysParamType findByParamTypeName(String name);
    /***
     * 检查SysParamType是否存在子节点
     * @author simon
     * @date 2016-4-12
     * @param type
     * @return
     */
    public boolean checkChildrenExist(SysParamType paramType);
    /****
     * 修改基础数据状态的方法
     * @author simon
     * @date 2016-4-12
     * @param ids
     * @param status
     * @return
     */
    public boolean saveStatus(String ids, String status);
	
	 /***
     * 查询所有有效的类型
     * @author tan
     * @date 2016-07-08
     */
    public List<SysParamType> findAllParamType();
	 
	  

}
