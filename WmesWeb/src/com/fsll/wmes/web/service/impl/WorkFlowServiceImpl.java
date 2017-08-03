package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.wmes.entity.WfProcedureAction;
import com.fsll.wmes.entity.WfProcedureInfo;
import com.fsll.wmes.web.service.WorkFlowService;


/***
 * 业务接口实现类：工作流管理接口类
 * @author 林文伟
 * @date 2016-06-22
 */
@Service("workflowService")
//@Transactional
public class WorkFlowServiceImpl extends BaseService implements WorkFlowService {

	/***
     * 查询工作流信息
     * @author 林文伟
     * @date 2016-06-29
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging, WfProcedureInfo infoList) {
		String hql=" from WfProcedureInfo r where 1=1";
		List<Object> params=new ArrayList<Object>();
		if(null!=infoList.getProcedureName()&&!"".equals(infoList.getProcedureName())){
			hql+=" and procedureName like '%"+infoList.getProcedureName()+"%'";
		}

		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		//Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
		//Integer index = 0;
    	while(it.hasNext()){
			//index++;
    		WfProcedureInfo obj = (WfProcedureInfo)it.next();
    		WfProcedureInfo vo = new WfProcedureInfo();
			BeanUtils.copyProperties(obj,vo);//拷贝信息;
			
			list.add(vo);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
	}
	
	/***
     * 查询工作流的动作列表信息
     * @author 林文伟
     * @date 2016-06-29
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findActionAll(JsonPaging jsonpaging, String procedureId) {
		String hql=" from WfProcedureAction r where r.procedure.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(procedureId);
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		//Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
		//Integer index = 0;
    	while(it.hasNext()){
			//index++;
    		WfProcedureAction obj = (WfProcedureAction)it.next();
			list.add(obj);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
	}

	@Override
	public WfProcedureInfo findById(String id) {
		WfProcedureInfo log = (WfProcedureInfo) baseDao.get(WfProcedureInfo.class, id);
		return findDetail(log);
	}
	
	/**
	 * 获得一条记录的详细信息
	 * @param 
	 * @return
	 */
	private WfProcedureInfo findDetail(WfProcedureInfo log){
		
		return log;
	}
	
	/***
     * 通过流程ID获取动作列表
     * @author 林文伟
     * @date 2016-06-20
     */
	public List<WfProcedureAction> GetActionList(String procedureId)
	{
		String hql=" from WfProcedureAction  where procedure.id=? order by flowCode asc";
		List<Object> params=new ArrayList<Object>();
		params.add(procedureId);
		
		List<WfProcedureAction> list=this.baseDao.find(hql, params.toArray(), false);

		return list;
	}
	
	/***
     * 通过流程ID获取动作列表
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public WfProcedureInfo saveOrUpdate(WfProcedureInfo paramConfig, boolean isAdd) {
		paramConfig=(WfProcedureInfo)this.baseDao.saveOrUpdate(paramConfig,isAdd);
		return paramConfig;
	}
	
	/***
	 * 删除根据id删除流程对象的方法
	 * @author 林文伟
	 * @date 2016-6-29
	 * @param String 主键
	 * @return
	 */
	public boolean deleteById(String id){
		if(null!=id){
			WfProcedureInfo info=new WfProcedureInfo();
			info.setId(id);
			info=findById(id);
			if(null!=info){
				//删除流 程下的动作信息
				DelAction(id);
				this.baseDao.delete(info);
			}
		}else{
			return false;
		}
		return true;
	}
	
	/***
     * 删除流程下的动作数据
     * @author 林文伟
     * @date 2016-06-20
     */
	public Boolean DelAction(String procedureId)
	{
		String hql1 = " delete WfProcedureAction where  procedure.id =? ";
		List<Object> params = new ArrayList<Object>();
		params.add(procedureId);
		int rs = this.baseDao.updateHql(hql1, params.toArray());
		return rs>0?true:false;
	}

}

