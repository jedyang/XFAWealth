package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.wmes.web.service.WebSiteService;

/***
 * 业务接口实现类：网站管理接口类
 * @author wwluo
 * @version 1.0
 * @date 2016-08-01
 */
@Service("webSiteservice")
//@Transactional
public class WebSiteServiceImpl extends BaseService implements WebSiteService{

	/***
     * 根据关联类型查询基础数据
     * @author wwluo
     * @date 2016-08-01
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Object> findByCode(String typeCode) {
		String hql = " from SysParamConfig where isValid=1 and type_id=(" +
				"from SysParamType where typeCode=?) order by order_by";
		List params = new ArrayList();
		params.add(typeCode);
		List<Object> list = this.baseDao.find(hql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list;
		}
		return null;
	}
	
	/***
     * 更新设置
     * @author wwluo
     * @date 2016-08-01
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int update(List<Map> list,String lang) {
		int count = 0;
        for (int i = 0; i < list.size(); i++) {
        	StringBuffer hql = new StringBuffer("update SysParamConfig set");
        	if("en".equals(lang))
        		hql.append(" confValueEn");
        	else if("tc".equals(lang))
        		hql.append(" confValueTc");
        	else
        		hql.append(" confValueSc");
        	hql.append("=? where id=? ");
        	List params = new ArrayList();
        	Map map = list.get(i);
	        params.add(map.get("value"));
	        params.add(map.get("id"));
	        count += this.baseDao.updateHql(hql.toString(),params.toArray());
		}
        return count;
	}
}
