package com.fsll.app.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.common.service.AppSysCountryService;
import com.fsll.app.investor.me.vo.AppIfaSearchItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysCountry;
/***
 * 业务接口实现类：国家管理接口类
 * @author zpzhou
 * @date 2016-7-11
 */
@Service("appSysCountryService")
//@Transactional
public class AppSysCountryServiceImpl extends BaseService implements AppSysCountryService {
    /***
     * 查找所有
	 * @param code 国家编码
	 * @param name 国家名称
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging,String code, String name) {
		String hql=" from SysCountry where 1=1";
		List<Object> params=new ArrayList<Object>();
		if(null!=name && !"".equals(name)){
			hql+=" and (nameSc like ? or nameTc like ? or nameEn like ?)";
			params.add("%"+name+"%");
			params.add("%"+name+"%");
			params.add("%"+name+"%");
		}
		if(null!=code && !"".equals(code)){
			hql+=" and (isoCode2 like ? or isoCode3 like ?)";
			params.add("%"+code+"%");
			params.add("%"+code+"%");
		}

		jsonpaging.setOrder("orderBy");
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);

		return jsonpaging;
	}
	
	/**
	 * 查询所有国家信息，并转换成VO
	 * @param langCode
	 * @return
	 */
	public List<AppIfaSearchItemVO> findAllCountryList(String langCode){
		List<SysCountry> countryList=findAll();
		List<AppIfaSearchItemVO> list = new ArrayList<AppIfaSearchItemVO>();
		for (int i = 0; i < countryList.size(); i++) {
			SysCountry country=countryList.get(i);
			AppIfaSearchItemVO vo = new AppIfaSearchItemVO();
			if (CommonConstants.LANG_CODE_EN.equalsIgnoreCase(langCode)){
				vo.setName(country.getNameEn());
			}
			else if (CommonConstants.LANG_CODE_TC.equalsIgnoreCase(langCode)){
				vo.setName(country.getNameTc());
			}
			else {
				vo.setName(country.getNameSc());
			}
			vo.setCode(country.getId());
			list.add(vo);
		}
		return list;
	}
	
	 /***
     * 查询所有有效的记录
     */
	private List<SysCountry> findAll() {
		String hql=" from SysCountry t order by t.orderBy";
		List<SysCountry> list = this.baseDao.find(hql, null, false);
		if(null != list && !list.isEmpty()){
			return list;
		}else {
			return null;
		}	
	}
	
}
