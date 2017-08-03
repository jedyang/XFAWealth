/**
 * 
 */
package com.fsll.wmes.ifafirm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.IfafirmFee;
import com.fsll.wmes.ifafirm.service.IfafirmFeeService;

/**
 * 公司费用接口实现
 * @author Yan
 * @date 2016-11-28
 */
@Service("ifafirmFeeService")
//@Transactional
public class IfafirmFeeServiceImpl extends BaseService implements IfafirmFeeService {

	/**
	 * 通过ID查找一条产品基本信息
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public IfafirmFee findById(String id){
		IfafirmFee info = (IfafirmFee) baseDao.get(IfafirmFee.class, id);
		return info;
	}
	
	/**
	 * 查找ifafirm的费用设置信息(取有效数据)
	 * @param ifafirmId
	 * @param ifafirmName
	 * @param langCode
	 * @return
	 */
	//@Transactional(readOnly = true)
	public JsonPaging getList(JsonPaging jsonPaging, String ifafirmId, String ifafirmName, String langCode){
		return getList(jsonPaging, ifafirmId, ifafirmName, langCode, "1");
	}
	
	/**
	 * 查找ifafirm的费用设置
	 * @param jsonPaging
	 * @param ifafirmId
	 * @param ifafirmName
	 * @param langCode
	 * @param status
	 * @return
	 */
	public JsonPaging getList(JsonPaging jsonPaging, String ifafirmId, String ifafirmName, String langCode, String status){
		String hql = " FROM IfafirmFee f "
			+ " LEFT JOIN SysParamConfig c ON f.feeType=c.id "
			+ " LEFT JOIN MemberIfafirm i ON f.ifafirm.id=i.id "
			+ " LEFT JOIN " + this.getLangString("MemberIfafirm", langCode) + " ii ON i.id=ii.id "
			+ " WHERE 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StrUtils.isEmpty(status)){
			hql += " and f.isValid=? ";
			params.add(status);
		}
		if (!StrUtils.isEmpty(ifafirmId)){
			hql += " and f.ifafirm.id=? ";
			params.add(ifafirmId);
		}		
		if(StringUtils.isNotBlank(ifafirmName)){
			hql += " AND ii.companyName LIKE ? ";
			params.add("%"+ifafirmName+"%");
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}	
	
	/**
	 * 保存信息
	 * @param info
	 * @return
	 */
	public IfafirmFee saveOrUpdate(IfafirmFee info, boolean isAdd){
		if(isAdd){
			return (IfafirmFee)this.baseDao.create(info);
		}else{
			return (IfafirmFee)this.baseDao.saveOrUpdate(info);
		}
	}
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	@Override
	public boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			IfafirmFee info = findById(id);
			if(info != null){
				baseDao.delete(info);
				return true;
			}else{
				return false;
			}
		}
	}

}
