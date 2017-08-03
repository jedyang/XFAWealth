package com.fsll.app.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.common.service.AppSysParamService;
import com.fsll.app.common.vo.AppSysParamVO;
import com.fsll.app.investor.me.vo.AppIfaSearchItemVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;

/***
 * 业务接口实现类：语言管理接口类
 * 
 * @author simon
 * @date 2016-4-11
 */
@Service("appSysParamService")
//@Transactional
public class AppSysParamServiceImpl extends BaseService implements AppSysParamService {
	
	
	/**
	 * 分页获取基础参数列表
	 * @author michael
	 * @param jsonPaging
	 * @param langCode
	 * @param typeCode
	 * @return
	 */
	public JsonPaging getParamList(JsonPaging jsonPaging, String langCode, String typeCode) {
		List params = new ArrayList();
		List<AppSysParamVO> list = new ArrayList();
		if(null == typeCode && "".equals(typeCode)){
			jsonPaging.setList(null);
			return jsonPaging;
		}
		String hql = "select c.id,c.configCode,c." + this.getLangString("name", langCode) + ",c." + this.getLangString("confValue", langCode) + 
		" from SysParamConfig c where c.isValid='1' ";
		if(""!=typeCode && null !=typeCode){
			hql+=" and c.type.typeCode = ?";
			params.add(typeCode);
		}
		hql+=" order by c.orderBy asc ";
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List dataList = jsonPaging.getList();
		if(!dataList.isEmpty()){
			for(int x=0;x<dataList.size();x++){
				AppSysParamVO vo=new AppSysParamVO();
				Object[] objs = (Object[])dataList.get(x);
				vo.setId(null==objs[0]?"":objs[0].toString());
				vo.setCode(null==objs[1]?"":objs[1].toString());
				vo.setName(null==objs[2]?"":objs[2].toString());
				vo.setValue(null==objs[3]?"":objs[3].toString());
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/***
	 * 根据站点名称查询站点对象
	 * @author mjhuang
	 * @param langCode
	 * @param configCode
	 * @return 
	 */
	public AppSysParamVO findParamByCode(String langCode, String configCode) {
		if(null == configCode && "".equals(configCode)){
			return null;
		}
		String hql = "select c.id,c.configCode,c." + this.getLangString("name", langCode) + ",c." + this.getLangString("confValue", langCode) + " from SysParamConfig c where c.isValid='1' ";
		if(""!=configCode && null !=configCode){
			hql+=" and c.configCode = '"+configCode+"'";
		}
		AppSysParamVO vo=new AppSysParamVO();
		List dataList = this.baseDao.find(hql,null, false);
		if(!dataList.isEmpty()){
			Object[] objs = (Object[])dataList.get(0);
			vo.setId(null==objs[0]?"":objs[0].toString());
			vo.setCode(null==objs[1]?"":objs[1].toString());
			vo.setName(null==objs[2]?"":objs[2].toString());
			vo.setValue(null==objs[3]?"":objs[3].toString());
		}
		return vo;
	}
	
	
	/**
	 * 根据参数得到基础数据列表，并转换成vo
	 * @param langCode  //语言
	 * @param typeCode  //类型
	 * @return
	 */
	public List<AppIfaSearchItemVO> getParamListByType(String langCode, String typeCode) {
		List<AppIfaSearchItemVO> list = new ArrayList();
		if(null == typeCode && "".equals(typeCode)){
			return list;
		}
		String hql = "select c.id,c.configCode,c." + this.getLangString("name", langCode) + ",c." + this.getLangString("confValue", langCode) + 
		" from SysParamConfig c where c.isValid='1' ";
		if(""!=typeCode && null !=typeCode){
			hql+=" and c.type.typeCode = '"+typeCode+"'";
		}
		hql+=" order by c.orderBy asc ";
		List dataList = this.baseDao.find(hql,null, false);
		if(!dataList.isEmpty()){
			for(int x=0;x<dataList.size();x++){
				AppIfaSearchItemVO vo=new AppIfaSearchItemVO();
				Object[] objs = (Object[])dataList.get(x);
				vo.setCode(null==objs[1]?"":objs[1].toString());
				vo.setName(null==objs[2]?"":objs[2].toString());
				list.add(vo);
			}
		}
		return list;
	}
}
