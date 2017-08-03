package com.fsll.wmes.web.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.wmes.entity.WfProcedureAction;
import com.fsll.wmes.entity.WfProcedureInfo;

public interface WorkFlowService {
	/***
	 * 工作流列表查询的方法
	 * @author 林文伟
	 * @date 2016-06-29
	 * @param jsonpaging
	 * @param emailLog
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonpaging, WfProcedureInfo infoList );
	
	/***
	 * 工作流的动作列表查询的方法
	 * @author 林文伟
	 * @date 2016-06-29
	 * @param jsonpaging
	 * @param emailLog
	 * @return
	 */
	public JsonPaging findActionAll(JsonPaging jsonpaging, String procedureId );
	
	
	/**
	 * 通过ID查找一条数据
	 * @author 林文伟
	 * @date 2016-06-29
	 * @param id
	 * @return
	 */
	public WfProcedureInfo findById(String id);
	
	/***
     * 通过流程ID获取动作列表
     * @author 林文伟
     * @date 2016-06-20
     */
	public List<WfProcedureAction> GetActionList(String procedureId);
	
	/***
	 * 保存对象的方法
	 * @author  
	 * @date 2016-3-23
	 * @param userinfo app用户对象
	 * @return
	 */
	public WfProcedureInfo saveOrUpdate(WfProcedureInfo paramConfig,boolean isAdd);
	
	/***
	 * 删除根据id删除流程对象的方法
	 * @author 林文伟
	 * @date 2016-3-23
	 * @param userinfo app用户对象
	 * @return
	 */
	public boolean deleteById(String id);
}

