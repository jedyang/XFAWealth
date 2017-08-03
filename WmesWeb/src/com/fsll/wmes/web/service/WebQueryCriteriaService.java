package com.fsll.wmes.web.service;

import java.util.List;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.OperLog;
import com.fsll.wmes.entity.WebQueryCriteria;
/***
 * 业务接口类：基金筛选条件管理
 * @author zxtan
 * @date 2016-10-19
 */
public interface WebQueryCriteriaService {
	
	
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean deleteById(String id);
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public WebQueryCriteria findById(String id);
	
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
	 * @return
     */
	public List<WebQueryCriteria> findMyCriteriaList(String memberId);

	/**
	 * 新增 、修改
	 * @param obj
	 * @return
	 */
	public WebQueryCriteria saveOrUpdate(WebQueryCriteria obj);
	
}
