package com.fsll.core.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.core.vo.AccessoryFileVO;

/**
 * 附件接口实现类
 * @author mqzou
 * @date 2016-08-04
 *
 */
@Service("accessoryFileService")
//@Transactional
public class AccessoryFileServiceImpl extends BaseService implements AccessoryFileService {

	@Override
	public JsonPaging findAllAccessoryFile(JsonPaging jsonPaging, AccessoryFile accessoryFile) {
		String hql=" from AccessoryFile r where 1=1 ";
		if(null!=accessoryFile  ){
			if(null!=accessoryFile.getFileName() && !"".equals(accessoryFile.getFileName())){
				hql+=" and r.fileName like '%"+accessoryFile.getFileName()+"%'";
			}
			if(null!=accessoryFile.getModuleType() && !"".equals(accessoryFile.getModuleType())){
				hql+=" and r.moduleType like '%"+accessoryFile.getModuleType()+"%'";
			}
		}
		List params=new ArrayList();
		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List list=new ArrayList();
		Iterator it=(Iterator)jsonPaging.getList().iterator();
		while (it.hasNext()) {
			AccessoryFile file = (AccessoryFile) it.next();
			AccessoryFileVO vo=new AccessoryFileVO();
			vo.setId(file.getId());
			vo.setCreateBy(null!=file.getCreateBy()?file.getCreateBy().getNickName():"");
			vo.setCreateTime(file.getCreateTime().toLocaleString());
			vo.setFileName(file.getFileName());
			vo.setFilePath(file.getFilePath());
			vo.setFileType(file.getFileType());
			vo.setLangCode(file.getLangCode());
			vo.setModuleType(file.getModuleType());
			vo.setRelateId(file.getRelateId());
			vo.setRemark(file.getRemark());
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public AccessoryFile findAccessoryFileById(String id) {
		AccessoryFile vo=(AccessoryFile)this.baseDao.get(AccessoryFile.class, id);
		return vo;
	}
	
	/**
	 * 保存附件信息
	 * @author wwluo
	 * @date 2016-12-07
	 * @param 
	 * @return
	 */
	@Override
	public AccessoryFile saveAccessoryFile(AccessoryFile accessoryFile) {
		AccessoryFile vo=(AccessoryFile)this.baseDao.saveOrUpdate(accessoryFile);
		return vo;
	}
	
	/**
	 * 获取附件地址
	 * @author wwluo
	 * @date 2016-12-07
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AccessoryFile getAccessoryFile(String relateId,String moduleType,String fileName,String langCode) {
		AccessoryFile accessoryFile = null;
		if(StringUtils.isNotBlank(relateId) && StringUtils.isNotBlank(moduleType)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" AccessoryFile" +
					" WHERE" +
					" moduleType=?" +
					" AND relateId=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(moduleType);
			params.add(relateId);
			if (StringUtils.isNotBlank(fileName)) {
				hql.append(" AND fileName=?");
				params.add(fileName);
			}
			if (StringUtils.isNotBlank(langCode)) {
				hql.append(" and langCode=?");
				params.add(langCode);
			}
			hql.append(" ORDER BY createTime DESC");
			List<AccessoryFile> accessoryFiles = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(accessoryFiles != null && !accessoryFiles.isEmpty()){
				accessoryFile = accessoryFiles.get(0);
			}
		}
		return accessoryFile;
	}

	/**
	 * 删除附件信息
	 * @author wwluo
	 * @date 2016-12-07
	 * @param 
	 * @return
	 */
	@Override
	public void delAccessoryFile(String relateId, String moduleType,
			String fileName, String langCode) {
		if(StringUtils.isNotBlank(relateId) && StringUtils.isNotBlank(moduleType)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" AccessoryFile" +
					" WHERE" +
					" moduleType=?" +
					" AND relateId=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(moduleType);
			params.add(relateId);
			if (StringUtils.isNotBlank(fileName)) {
				hql.append(" AND fileName=?");
				params.add(fileName);
			}
			if (StringUtils.isNotBlank(langCode)) {
				hql.append(" and langCode=?");
				params.add(langCode);
			}
			List<AccessoryFile> accessoryFiles = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(accessoryFiles != null && !accessoryFiles.isEmpty()){
				for (AccessoryFile accessoryFile : accessoryFiles) {
					this.baseDao.delete(accessoryFile);
				}
			}
		}
	}

}
