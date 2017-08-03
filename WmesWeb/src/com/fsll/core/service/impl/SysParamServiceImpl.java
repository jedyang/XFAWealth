package com.fsll.core.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.entity.SysParamType;
import com.fsll.core.service.SysParamService;
import com.fsll.core.vo.SysParamVO;
import com.fsll.wmes.ifa.vo.IfaSearchItemVO;

/***
 * 业务接口实现类：语言管理接口类
 * 
 * @author simon
 * @date 2016-4-11
 */
@Service("sysParamService")
//@Transactional
public class SysParamServiceImpl extends BaseService implements SysParamService {
	/***
	 * 查询
	 * 
	 * @author simon
	 * @date 2016-03-17
	 */
	@Override
	//@Transactional(readOnly = true)
	public JsonPaging findAll(JsonPaging jsonpaging, SysParamConfig paramConfig) {
		String hql = " from SysParamConfig r where 1=1";
		List<Object> params = new ArrayList<Object>();
		if (null != paramConfig.getType() && !"".equals(paramConfig.getType().getId())) {
			hql += " and r.type.id = ? ";
			params.add(paramConfig.getType().getId());
		}
		else {
			hql += " and r.type.id != null ";
		}
		
		if (null != paramConfig.getNameSc() && !"".equals(paramConfig.getNameSc())) {
			hql += " and (r.nameSc like ? or r.nameTc like ? or r.nameEn like ? or  r.configCode like ? )";
			params.add("%" + paramConfig.getNameSc() + "%");
			params.add("%" + paramConfig.getNameTc() + "%");
			params.add("%" + paramConfig.getNameEn() + "%");
			params.add("%" + paramConfig.getConfigCode() + "%");
		}
		if (null != paramConfig.getTypeCode() && !"".equals(paramConfig.getTypeCode())) {
			hql += " and r.type.typeCode=?";
			params.add(paramConfig.getTypeCode());
		}
		if (null != paramConfig.getIsValid() && !"".equals(paramConfig.getIsValid())) {
			hql += " and r.isValid = ? ";
			params.add(paramConfig.getIsValid());
		} else {
			hql += " and r.isValid = '1' ";
		}
		if (jsonpaging.getSort() != null && jsonpaging.getSort().contains("orderBy")) {
			jsonpaging.setSort("r.type.id,r.orderBy");
			jsonpaging.setOrder("asc,asc");
		}
		jsonpaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		Integer indexNumber = (jsonpaging.getPage() - 1) * jsonpaging.getRows();
		Integer index = 0;
		while (it.hasNext()) {
			index++;
			SysParamConfig obj = (SysParamConfig) it.next();
			obj.setTypeCode(obj.getType().getTypeCode());
			obj.setTypeName(obj.getType().getName());
			if (obj.getParent() != null) {
				obj.setParentCode(obj.getParent().getConfigCode());
				obj.setParentId(obj.getParent().getId());

				obj.setParentName(obj.getParent().getNameSc());
			} else {
				obj.setParentName("根");
			}
			obj.setXh(Integer.toString(indexNumber + index));
			list.add(obj);
		}
		jsonpaging.setList(list);
		return jsonpaging;
	}

	/***
	 * 根据id查询对象的方法
	 * 
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo
	 * @param jsonpaging
	 * @return
	 */
	//@Transactional(readOnly = true)
	public SysParamConfig findParamConfigById(SysParamConfig paramConfig) {
		String hql = "from SysParamConfig where id=?";
		List params = new ArrayList();
		params.add(paramConfig.getId());
		List<SysParamConfig> list = this.baseDao.find(hql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/***
	 * 根据编码查询站点对象
	 * 
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysParamConfig findByCode(String code) {
		String hql = "from SysParamConfig where configCode=?";
		List params = new ArrayList();
		params.add(code);
		List<SysParamConfig> list = this.baseDao.find(hql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			SysParamConfig obj = list.get(0);
			obj.setTypeCode(obj.getType().getTypeCode());
			obj.setTypeName(obj.getType().getName());
			if (obj.getParent() != null) {
				obj.setParentCode(obj.getParent().getConfigCode());
				obj.setParentId(obj.getParent().getId());
				obj.setParentName(obj.getParent().getNameSc());
			} else {
				obj.setParentName("根");
			}
			return obj;
		}
		return null;
	}

	/***
	 * 根据站点名称查询站点对象
	 * 
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysParamConfig findBySysParamName(String name) {
		String hql = "from SysParamConfig where nameSc=?";
		List params = new ArrayList();
		params.add(name);
		List<SysParamConfig> list = this.baseDao.find(hql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/***
	 * 删除用户对象的方法
	 * 
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo
	 *            app用户对象
	 * @return
	 */
	public boolean deleteObject(String ids) {
		if (!"".equals(ids)) {
			String tmpStr = "";
			String[] arr = null;
			if (ids.endsWith(",")) {
				tmpStr = ids.substring(0, ids.length() - 1);
				arr = tmpStr.split(",");
			} else {
				arr = new String[] { ids };
			}
			for (String id : arr) {
				if (!"".equals(id)) {
					deleteById(id);
				}
			}
		}
		return false;
	}

	/***
	 * 删除根据id删除用户对象的方法
	 * 
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo
	 *            app用户对象
	 * @return
	 */
	private boolean deleteById(String id) {
		if (null != id) {
			SysParamConfig site = new SysParamConfig();
			site.setId(id);
			site = findById(site);
			if (null != site) {
				// 删除该指定站点信息
				this.baseDao.delete(site);
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 根据应用对象和角色对象加载树的方法，###2015-11-12 修改取得 应用角色 的逻辑 by Michael
	 * 根据用户所在组加载组下所有的节点。
	 * 
	 * @author simon
	 * @date 2016-3-23
	 * @param companyinfo
	 * @param userinfo
	 * @return
	 */
	//@Transactional(readOnly = true)
	public String getLoadParamTree() {
		List paramsList = new ArrayList();
		String hql = "";
		List<SysParamConfig> listauth = new ArrayList<SysParamConfig>();
		hql = "from SysParamConfig t where t.parent is null ";
		listauth = this.baseDao.find(hql, paramsList.toArray(), true);
		return getComTree(listauth);
	}

	/**
	 * 传入应用的id的加载应用已经添加了的功能
	 * 
	 * @author simon
	 * @date 2015-09-14
	 * @param appid
	 *            应用id
	 * @return
	 */
	public String getComTree(List<SysParamConfig> listauth) {
		List<String> listNodeStr = new ArrayList<String>();
		String treeTypeStr = "[";
		String hql = " from SysParamConfig t where 1=1 ";
		List params = new ArrayList();
		if (null != listauth && !listauth.isEmpty()) {
			hql += " and  t.parent.id in (";
		}
		for (int i = 0; i < listauth.size(); i++) {
			SysParamConfig objArr = listauth.get(i);
			if (i < listauth.size() - 1) {
				hql += "?,";
			} else {
				hql += "?";
			}
			String stringtempobj = "{id:\"" + objArr.getId() + "\",pId:0,name:\"" + objArr.getNameSc() + "\",open:true}";
			listNodeStr.add(stringtempobj);
			params.add(listauth.get(i).getId());
		}
		if (null != listauth && !listauth.isEmpty()) {
			hql += ")";
		} else {
			hql += "";
		}
		List<SysParamConfig> typeList = baseDao.find(hql, params.toArray(), false);
		if (typeList != null && !typeList.isEmpty()) {
			for (int i = 0; i < typeList.size(); i++) {
				SysParamConfig objArr = typeList.get(i);
				String stringtempobj = "{id:\"" + objArr.getId() + "\",pId:\"" + objArr.getParent().getId() + "\",name:\"" + objArr.getNameSc() + "\",open:true}";
				listNodeStr.add(stringtempobj);
				List<String> tempchildes = getChildren(objArr.getId());
				if (null != tempchildes && !tempchildes.isEmpty()) {// 当前节点auth_code不是传入的父节点code
					listNodeStr.addAll(tempchildes);
				}
			}
		}
		for (int i = 0; i < listNodeStr.size(); i++) {
			if (i < listNodeStr.size() - 1) {
				treeTypeStr += listNodeStr.get(i) + ",";
			} else {
				treeTypeStr += listNodeStr.get(i);
			}
		}
		treeTypeStr += "]";
		return treeTypeStr;
	}

	/**
	 * 循环添加子节点的方法
	 * 
	 * @author simon
	 * @date 2015-09-14
	 * @param parentID
	 *            父节点的id
	 * @return
	 */
	private List<String> getChildren(String parentID) {
		List<String> listNodeStr = new ArrayList<String>();
		String hql = "  from SysParamConfig t where t.parent.id = ?";
		List params = new ArrayList();
		params.add(parentID);
		List<SysParamConfig> typeList = baseDao.find(hql, params.toArray(), true);
		if (typeList != null && !typeList.isEmpty()) {
			for (int i = 0; i < typeList.size(); i++) {
				SysParamConfig objArr = typeList.get(i);
				String stringtempobj = "{id:\"" + objArr.getId() + "\",pId:\"" + objArr.getParent().getId() + "\",name:\"" + objArr.getNameSc() + "\"}";
				listNodeStr.add(stringtempobj);
				List<String> tempchildes = getChildren(objArr.getId());
				if (null != tempchildes && !tempchildes.isEmpty()) {// 当前节点auth_code不是传入的父节点code
					listNodeStr.addAll(tempchildes);
				}
			}
		}
		return listNodeStr;
	}

	@Override
	public SysParamConfig saveOrUpdate(SysParamConfig paramConfig, boolean isAdd) {
		paramConfig = (SysParamConfig) this.baseDao.saveOrUpdate(paramConfig, isAdd);
		return paramConfig;
	}

	@Override
	public SysParamConfig findById(SysParamConfig paramConfig) {
		String hql = "from SysParamConfig where id=?";
		List params = new ArrayList();
		params.add(paramConfig.getId());
		List<SysParamConfig> list = this.baseDao.find(hql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/***
	 * 获得所以基础数据类型的列表
	 * 
	 * @author simon
	 * @date 2016-4-12
	 * @return
	 */
	public List<SysParamType> getAllParamTypeList() {
		String hql = "From SysParamType ";
		List<SysParamType> listparamtype = this.baseDao.find(hql, null, false);
		if (null != listparamtype && !listparamtype.isEmpty()) {
			return listparamtype;
		}
		return null;
	}

	/***
	 * 根据类型的id查询类型对象
	 * 
	 * @author simon
	 * @date 2016-4-12
	 * @param type
	 * @return
	 */
	public SysParamType findParamTypeById(SysParamType type) {
		String hql = "FROM SysParamType where id=?";
		List param = new ArrayList();
		param.add(type.getId());
		List<SysParamType> list = this.baseDao.find(hql, param.toArray(), false);
		if (null != list && !list.isEmpty())
			return list.get(0);
		return null;

	}

	/***
	 * 检查SysParamType是否存在子节点
	 * 
	 * @author simon
	 * @date 2016-4-12
	 * @param type
	 * @return
	 */
	public boolean checkChildrenExist(SysParamConfig paramConfig) {
		String hql = "FROM SysParamConfig t where t.parent.id=?";
		List param = new ArrayList();
		param.add(paramConfig.getId());
		List<SysParamConfig> listtype = this.baseDao.find(hql, param.toArray(), false);
		if (null != listtype && !listtype.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean saveStatus(String ids, String status) {
		if (!"".equals(ids)) {
			String tmpStr = "";
			String[] arr = null;
			if (ids.endsWith(",")) {
				tmpStr = ids.substring(0, ids.length() - 1);
				arr = tmpStr.split(",");
			} else {
				arr = new String[] { ids };
			}
			for (String id : arr) {
				String hql = "update SysParamConfig v set v.status = ? where v.id =  ? ";
				List<Object> params = new ArrayList<Object>();
				params.add(status);
				if (!"".equals(id)) {
					params.add(id);
					this.baseDao.updateHql(hql, params.toArray());
				}
			}
		}
		return true;
	}

	/***
	 * 根据type查询对象的方法
	 * 
	 * @author michael
	 * @date 2016-6-23
	 * @param typeCode
	 *            参数类型编码
	 * @return list
	 */
	//@Transactional(readOnly = true)
	public List<SysParamConfig> findParamConfigByType(String typeCode) {
		List params = new ArrayList();

		String hql = "from SysParamConfig c where c.isValid='1' ";
		if (null != typeCode && !"".equals(typeCode)) {
			hql += " and c.type.typeCode=?";
			params.add(typeCode);
		}
		hql += " order by orderBy ";
		List<SysParamConfig> list = this.baseDao.find(hql, params.toArray(), false);

		return list;
	}

	@Override
	public List<SysParamVO> getParamList(String langCode, String typeCode) {
		List<SysParamVO> list = new ArrayList();
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
				SysParamVO vo=new SysParamVO();
				Object[] objs = (Object[])dataList.get(x);
				vo.setId(null==objs[0]?"":objs[0].toString());
				vo.setCode(null==objs[1]?"":objs[1].toString());
				vo.setName(null==objs[2]?"":objs[2].toString());
				vo.setValue(null==objs[3]?"":objs[3].toString());
				list.add(vo);
			}
		}
		return list;
	}
	
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
		List<SysParamVO> list = new ArrayList();
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
				SysParamVO vo=new SysParamVO();
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
	public SysParamVO findParamByCode(String langCode, String configCode) {
		if(null == configCode && "".equals(configCode)){
			return null;
		}
		String hql = "select c.id,c.configCode,c." + this.getLangString("name", langCode) + ",c." + this.getLangString("confValue", langCode) + " from SysParamConfig c where c.isValid='1' ";
		if(""!=configCode && null !=configCode){
			hql+=" and c.configCode = '"+configCode+"'";
		}
		SysParamVO vo=new SysParamVO();
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
	public List<IfaSearchItemVO> getParamListByType(String langCode, String typeCode) {
		List<IfaSearchItemVO> list = new ArrayList();
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
				IfaSearchItemVO vo=new IfaSearchItemVO();
				Object[] objs = (Object[])dataList.get(x);
				vo.setCode(null==objs[1]?"":objs[1].toString());
				vo.setName(null==objs[2]?"":objs[2].toString());
				list.add(vo);
			}
		}
		return list;
	}
	
	/***
     * 根据关联类型查询基础数据
     * @author wwluo
     * @date 2016-08-01
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Object> findListByCode(String typeCode) {
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
	
	@Override
	public String findNameByCode(String code,String langCode) {
		String sql="SELECT fn_getconfigname('"+code+"','"+langCode+"') name    ";
		List list=this.springJdbcQueryManager.springJdbcQueryForList(sql);
		if(null!=list && !list.isEmpty()){
			Map map=(Map)list.get(0);
			Object object= map.get("name");
			if(null!=object){
				return object.toString();
			}
		}
		return "";
	}
	
	/**
	 * 根据基础数据的value获取name值
	 * @author mqzou	modify by wwluo 2017/2/15
     * @date 2016-11-09
	 * @param value
	 * @param langCode
	 * @return
	 */
	@Override
	public String findNameByValue(String type, String value, String langCode) {
	/*String hql=" select r."+getLangString("name", langCode)+" from SysParamConfig r where r."+getLangString("confValue", langCode)+"=? and r.type.typeCode=?";
		List params = new ArrayList();
		params.add(value);
		params.add(type);
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			Object obj = (Object) list.get(0);
			if (null != obj)
				return obj.toString();
		}
		return null;*/
		String name = null;
		if(StringUtils.isNotBlank(value) 
				&& StringUtils.isNotBlank(langCode)
					&& StringUtils.isNotBlank(type)){
			String lang = langCode.substring(0, 1).toUpperCase()+langCode.substring(1, 2);
			StringBuffer hql = new StringBuffer("" +
					" SELECT" +
					" c.name" + lang +
					" FROM" +
					" SysParamConfig c" +
					" WHERE" +
					" c.type.typeCode=?" +
					" AND" +
					" c.configCode=?");
			List<Object> params = new ArrayList<Object>();
			params.add(type);
			params.add(value);
			List<String> sysParamConfigs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(sysParamConfigs != null && !sysParamConfigs.isEmpty()){
				name = sysParamConfigs.get(0);
			}
		}
		return name;
		
	}
}
