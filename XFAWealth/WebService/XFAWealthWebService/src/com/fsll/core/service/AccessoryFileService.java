package com.fsll.core.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.AccessoryFile;

public interface AccessoryFileService {

	/**
	 * 获取附件列表
	 * @author mqzou
	 * @date 2016-08-04
	 * @param jsonPaging
	 * @param accessoryFile
	 * @return
	 */
	public JsonPaging findAllAccessoryFile(JsonPaging jsonPaging,AccessoryFile accessoryFile);
	
	/**
	 * 获取附件详细信息
	 * @author mqzou
	 * @date 2016-08-04
	 * @param Id
	 * @return
	 */
	public AccessoryFile findAccessoryFileById(String id);
	
	/**
	 * 保存附件信息
	 * @author wwluo
	 * @date 2016-12-07
	 * @param 
	 * @return
	 */
	public AccessoryFile saveAccessoryFile(AccessoryFile accessoryFile); 
	
	/**
	 * 获取附件地址
	 * @author wwluo
	 * @date 2016-12-07
	 * @param 
	 * @return
	 */
	public AccessoryFile getAccessoryFile(String relateId,String moduleType,String fileName,String langCode);

	/**
	 * 删除附件信息
	 * @author wwluo
	 * @date 2016-12-07
	 * @param 
	 * @return
	 */
	public void delAccessoryFile(String relateId, String moduleType,
			String fileName, String langCode);
}
