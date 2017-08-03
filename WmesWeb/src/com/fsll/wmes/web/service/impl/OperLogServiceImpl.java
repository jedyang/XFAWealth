package com.fsll.wmes.web.service.impl;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.OperLog;
import com.fsll.wmes.web.service.OperLogService;

/***
 * 业务接口实现类：web操作日志管理
 * @author mjhuang
 * @date 2016-6-13
 */
@Service("operLogService")
//@Transactional
public class OperLogServiceImpl extends BaseService implements OperLogService {
	
	/**
	 * 增加一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public  OperLog create(OperLog log){
		log.setId(null);
		log.setCreateTime(new Date());
		mongoDao.insert(log);
		return log;
	}
	
	/**
	 * 修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public  OperLog update(String id,Update update){
		Query query = new Query(Criteria.where("id").is(id));
		OperLog log =  new OperLog();
		mongoDao.update(query, update, log);
		return log;
	}
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	private boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			OperLog log = findById(id);
			if(log != null){
				mongoDao.remove(log);
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids){
		if (!"".equals(ids)) {
			String tmpStr = ids;
			if(ids.endsWith(","))tmpStr = ids.substring(0,ids.length()-1);
			String[] arr = tmpStr.split(",");
			for (String id : arr) {
				boolean result = deleteById(id);
				if(!result){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly=true)
	public OperLog findById(String id){
		Query query = new Query(Criteria.where("id").is(id));
		OperLog log =  new OperLog();
		log =  (OperLog)mongoDao.findOne(query,log);
		return log;
	}
	
	/**
	 * 查找记录
	 */
	//@Transactional(readOnly=true)
	public List<OperLog> findAll(){
		Query query = new Query();
		OperLog log =  new OperLog();
		List list =  mongoDao.find(query,log);
		return list;
	}
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param log 查询参数
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging,OperLog log) {
		Query query = new Query();
		if(null!=log.getLoginCode()&&!"".equals(log.getLoginCode())){
			//不区分大小写的完全匹配
			//Pattern pattern = Pattern.compile("^"+log.getLoginCode()+"$", Pattern.CASE_INSENSITIVE);
			//query.addCriteria(Criteria.where("loginCode").regex(pattern));
			
			//不区分大小写的右匹配
			//Pattern pattern = Pattern.compile("^.*"+log.getLoginCode()+"$", Pattern.CASE_INSENSITIVE);
			//query.addCriteria(Criteria.where("loginCode").regex(pattern));
			
			//不区分大小写的左匹配
			//Pattern pattern = Pattern.compile("^"+log.getLoginCode()+".*$", Pattern.CASE_INSENSITIVE);
			//query.addCriteria(Criteria.where("loginCode").regex(pattern));
			
			//不区分大小写的模糊查询
			Pattern pattern = Pattern.compile("^.*"+log.getLoginCode()+".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("loginCode").regex(pattern));
		}

		if(null!=log.getNickName()&&!"".equals(log.getNickName())){
			Pattern pattern = Pattern.compile("^.*"+log.getLoginCode()+".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("nickName").regex(pattern));
		}
		
		if(null!=log.getModuleType()&&!"".equals(log.getModuleType())){
			query.addCriteria(Criteria.where("moduleType").is(log.getModuleType()));
		}
		
		//时间段的查询 开始
		String startTimeTmp = log.getStartTime();
		String endTimeTmp = log.getEndTime();
		Date startTime = DateUtil.getDate(startTimeTmp, "yyyy-MM-dd HH:mm:ss");
		Date endTime = DateUtil.getDate(endTimeTmp, "yyyy-MM-dd HH:mm:ss");;
		if (startTime!=null && (endTime==null)){
			query.addCriteria(Criteria.where("createTime").gte(startTime));
		}
		if (endTime!=null && (startTime==null)){
			query.addCriteria(Criteria.where("createTime").lte(endTime));
		}
		if(startTime!=null && endTime!=null){
			query.addCriteria(Criteria.where("createTime").gte(startTime).andOperator(Criteria.where("createTime").lte(endTime)));
		}
		//时间段的查询 结束
		jsonpaging = mongoDao.selectJsonPaging(jsonpaging,query,log);
		return jsonpaging;
	}
	
}
