/**
 * 
 */
package com.fsll.wmes.fund.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.entity.FundDoc;
import com.fsll.wmes.fund.service.FundDocService;
import com.fsll.wmes.fund.vo.FundDocDataVO;

/**
 * @author scshi
 *	基金附件文件接口实现
 */
@Service("fundDocService")
//@Transactional
public class FundDocServiceImpl extends BaseService implements FundDocService {
	
	/**3.1.9	获取基金附件文件
	 * @author scshi
	 * @param fundId 资金id
	 * @param langCode 语言
	 * @return	<List>FundDocDataVO	基金附件文件及下载链接信息
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<FundDocDataVO> fundDocFileList(String fundId,String langCode) {
		String hql = "From FundDoc t where t.isValid='1' ";
		List params = new ArrayList();
		if(null!=fundId && !"".equals(fundId)){
			hql +=" and t.fund.id=? ";
			params.add(fundId);
		}
		hql +=" order by t.lastUpdate desc";
		List<FundDoc> preList = this.baseDao.find(hql, params.toArray(), false);
		List<FundDocDataVO> voList = new ArrayList();
		if(!preList.isEmpty()){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
			for(FundDoc preVo:preList){
				FundDocDataVO vo = new FundDocDataVO();
				String documentName = preVo.getDocumentName();
				String lastUpdate = sdf.format(preVo.getLastUpdate()==null?preVo.getCreateTime():preVo.getLastUpdate());
				vo.setDocName(documentName);
				vo.setLastUdate(lastUpdate);
				vo.setFileList(this.findFileAttrVO(preVo.getId(),"fund_doc"));
				voList.add(vo);
			}
		}		
		return voList;
	}

}
