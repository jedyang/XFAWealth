package com.fsll.wmes.crm.service;

import java.util.List;

import com.fsll.wmes.entity.CrmMemo;

/**
 * 客户沟通记录备忘
 * @author mqzou 2016-12-01
 *
 */
public interface CrmMemoService {


	/**
	 * 保存客户沟通记录备忘
	 * @author mqzou 2016-12-01
	 * @param crmMemo
	 */
	public CrmMemo saveOrUpdate(CrmMemo crmMemo);
	
	/**
	 * 保存客户沟通记录备忘，图片作为附件保存
	 * @author mqzou 2017-05-11 
	 * @param memo
	 * @param imgList
	 * @return
	 */
	public CrmMemo saveMemo(CrmMemo memo,List<String> imgList);
	
	/**
	 * 获取客户沟通记录备忘
	 * @author mqzou 2016-12-01
	 * @param id
	 * @return
	 */
	public CrmMemo findById(String id);
	
	/**
	 * 删除客户沟通记录
	 * @author mqzou 2016-12-02
	 * @param crmMemo
	 * @return
	 */
	public boolean deleteCrmMemo(CrmMemo crmMemo);
	
}
