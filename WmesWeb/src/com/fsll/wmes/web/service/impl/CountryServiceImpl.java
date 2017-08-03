package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.vo.SysCountryVO;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.web.service.CountryService;
import com.fsll.wmes.web.vo.EmailLogVO;

/***
 * 业务接口实现类：国家管理接口类
 * @author wwluo
 * @version 1.0
 * @date 2016-08-03
 */
@Service("countryService")
//@Transactional
public class CountryServiceImpl extends BaseService implements CountryService {

	/***
     * 查询国家列表
     * @author wwluo
     * @date 2016-08-03
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging, String keyWord) {
		StringBuffer hql = new StringBuffer(" from SysCountry where 1=1");
		if(StringUtils.isNotBlank(keyWord)){
			hql.append(" and ( nameSc like '%" + keyWord.trim() + "%'");
			hql.append(" or nameTc like '%" + keyWord.trim() + "%'");
			hql.append(" or nameEn like '%" + keyWord.trim() + "%'");
			hql.append(" or isoCode2 like '%" + keyWord.trim() + "%'");
			hql.append(" or isoCode3 like '%" + keyWord.trim() + "%'");
			hql.append(" or areaCode like '%" + keyWord.trim() + "%')");
		}
		hql.append(" order by order_by");
		jsonpaging = this.baseDao.selectJsonPaging(hql.toString(), null, jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
    	while(it.hasNext()){
    		SysCountry obj = (SysCountry)it.next();
    		SysCountryVO vo = new SysCountryVO();
			BeanUtils.copyProperties(obj,vo);//拷贝信息;
			if(null != obj.getOrderBy() || "".equals(obj.getOrderBy()))
				vo.setOrderBy(obj.getOrderBy().toString());
			else 
				vo.setOrderBy(null);
			list.add(vo);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
	}
	
	/***
     * 保存国家信息（save or update）
     * @author wwluo
     * @date 2016-08-03
     */
	@Override
	//@Transactional(readOnly=true)
	public boolean saveOrUpdate(SysCountry sysCountry) {
		boolean flag = false;
		sysCountry = (SysCountry) baseDao.saveOrUpdate(sysCountry);
		if(null != sysCountry || "".equals(sysCountry))
			flag = true;
		return flag;
	}
	
	/***
     * 根据id查找sysCountry实体
     * @author wwluo
     * @date 2016-08-03
     */
	@Override
	//@Transactional(readOnly=true)
	public SysCountryVO findById(String id) {
		SysCountry sysCountry = (SysCountry) baseDao.get(SysCountry.class, id);
		SysCountryVO vo = new SysCountryVO();
		BeanUtils.copyProperties(sysCountry,vo);//拷贝信息;
		vo.setOrderBy(sysCountry.getOrderBy().toString());
		return vo;
	}
	
	/***
     * 根据id删除sysCountry实体
     * @author wwluo
     * @date 2016-08-03
     */
	@Override
	public void deleteById(String id) {
		baseDao.delete(baseDao.get(SysCountry.class, id));
	}

	/**
	 * 查找国家列表
	 * @author michael
	 * @param keyWord 关键字
	 * @param langCode 语言
	 * @return
	 */
	//@Transactional(readOnly = true)
	public List<GeneralDataVO> findCountryList(String keyWord, String langCode){
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		List<String> params = new ArrayList<String>();

		String hql = "select t.id,"+this.getLangString("t.name", langCode)+" as name from SysCountry t ";
		if (null!=keyWord && !"".equals(keyWord)){
			hql += " and ( t.nameEn like ? or t.nameSc like ? or t.nameTc like ? )";
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		hql += " order by t.orderBy ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setId(StrUtils.getString(objs[0]));
				vo.setName(StrUtils.getString(objs[1]));
				list.add(vo);
			}
		}
		return list;
	}
}
