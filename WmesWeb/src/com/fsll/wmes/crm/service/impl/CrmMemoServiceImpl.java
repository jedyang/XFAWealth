package com.fsll.wmes.crm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmMemoService;
import com.fsll.wmes.entity.CrmMemo;
import com.fsll.wmes.entity.MemberBase;

/***
 * 业务接口实现类：客户日程记录
 * @author mqzou
 * @date 2016-12-01
 */
@Service("crmMemoService")
//@Transactional
public class CrmMemoServiceImpl extends BaseService implements CrmMemoService {

	@Override
	public CrmMemo saveOrUpdate(CrmMemo crmMemo) {
		CrmMemo vo=(CrmMemo)this.baseDao.saveOrUpdate(crmMemo);
		return vo;
	}

	@Override
	public CrmMemo findById(String id) {
		CrmMemo crmMemo=(CrmMemo)this.baseDao.get(CrmMemo.class, id);
		return crmMemo;
	}
	
	/**
	 * 删除客户沟通记录
	 * @author mqzou 2016-12-02
	 * @param crmMemo
	 * @return
	 */
	@Override
	public boolean deleteCrmMemo(CrmMemo crmMemo) {
		try {
			this.baseDao.delete(crmMemo);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	/**
	 * 保存客户沟通记录备忘，图片作为附件保存
	 * @author mqzou 2017-05-11 
	 * @param memo
	 * @param imgList
	 * @return
	 */
	@Override
	public CrmMemo saveMemo(CrmMemo memo, List<String> imgList) {
		CrmMemo vo=(CrmMemo)this.baseDao.saveOrUpdate(memo);
		String hql=" delete from AccessoryFile r where r.moduleType='crm_memo' and r.relateId='"+vo.getId()+"'";
		this.baseDao.updateHql(hql, null);
		if(null!=vo && null!=imgList && !imgList.isEmpty()){
			for (String img : imgList) {
				if(null==img || "".equals(img))
					continue;
				AccessoryFile file=new AccessoryFile();
				file.setCreateBy(vo.getIfa().getMember());
				file.setCreateTime(DateUtil.getCurDate());
				String[] path=img.split("/");
				file.setFileName(path[path.length-1]);
				file.setFilePath(img);
				file.setModuleType("crm_memo");
				file.setRelateId(vo.getId());
				file.setRemark("沟通记录附件");
				path=img.split("\\.");
				file.setFileType("."+path[path.length-1]);
				baseDao.saveOrUpdate(file);
			}
		}
		return vo;
	}

	
}
