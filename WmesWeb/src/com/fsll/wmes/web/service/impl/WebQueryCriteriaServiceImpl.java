package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.WebOperLog;
import com.fsll.wmes.entity.WebQueryCriteria;
import com.fsll.wmes.web.service.WebQueryCriteriaService;
/***
 * 业务接口类：基金筛选条件
 * @author zxtan
 * @date 2016-10-19
 */
@Service("webQueryCriteriaService")
//@Transactional
public class WebQueryCriteriaServiceImpl extends BaseService implements WebQueryCriteriaService {
 
  
	/***
	 * 根据id查询对象
	 * @author zxtan
	 * @date 2016-10-19
	 * @param id
	 * @return
	 */
	@Override
	public WebQueryCriteria findById(String id) {
		String hql="from WebQueryCriteria where id=? ";
		List params=new ArrayList();
		params.add(id);
		List<WebQueryCriteria> list=this.baseDao.find(hql, params.toArray(), false);
		if (null!=list&&!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	
	
	
	/***
	 * 保存对象的方法
	 * @author zxtan
	 * @date 2016-10-19
	 * @param WebQueryCriteria 对象
	 * @return
	 */
	@Override
	public WebQueryCriteria saveOrUpdate(WebQueryCriteria obj) {
		return (WebQueryCriteria)baseDao.saveOrUpdate(obj);
	}


	@Override
	public boolean deleteById(String id) {
		// TODO Auto-generated method stub
		if (id == null) {
			return false;
		}else{
			WebQueryCriteria obj = findById(id);
			if(obj != null){
				baseDao.delete(obj);				
				return true;
			}else{
				return false;
			}
		}
	}

	/***
	 * 保存对象的方法
	 * @author zxtan
	 * @date 2016-10-19
	 * @param WebQueryCriteria 对象
	 * @return
	 */
	@Override
	public List<WebQueryCriteria> findMyCriteriaList(String memberId) {
		// TODO Auto-generated method stub
		String hql=" from WebQueryCriteria l where l.type='0' ";
		List<Object> params=new ArrayList<Object>();
		
		if( null!=memberId && !"".equals( memberId) ){
			hql += " and l.member.id=? ";
			params.add( memberId );
		}

		hql += " order by l.createDate desc ";
		
		List<WebQueryCriteria> list = this.baseDao.find(hql, params.toArray(),0,3, false);

		return list;
	}
  

	

}
