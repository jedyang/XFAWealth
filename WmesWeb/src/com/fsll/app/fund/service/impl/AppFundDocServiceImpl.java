/**
 * 
 */
package com.fsll.app.fund.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.fund.service.AppFundDocService;
import com.fsll.app.fund.vo.AppFundDocDataVO;
import com.fsll.app.fund.vo.AppFundDocDetailVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.wmes.entity.FundDoc;
import com.fsll.wmes.entity.FundDocDetail;

/**
 * @author scshi
 *	基金附件文件接口实现
 */
@Service("appFundDocService")
//@Transactional
public class AppFundDocServiceImpl extends BaseService implements AppFundDocService {
	
	/**3.1.9	获取基金附件文件
	 * @author scshi
	 * @param fundId 资金id
	 * @param langCode 语言
	 * @return	<List>FundDocDataVO	基金附件文件及下载链接信息
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<AppFundDocDataVO> fundDocFileList(String fundId,String langCode) {
		String hql = "From FundDoc t where t.isValid='1' ";
		List params = new ArrayList();
		if(null!=fundId && !"".equals(fundId)){
			hql +=" and t.fund.id=? ";
			params.add(fundId);
		}
		
		if(null!=langCode && !"".equals(langCode)){
			hql +=" and t.langCode=? ";
			params.add(langCode);
		}
		
		hql +=" order by t.lastUpdate desc";
		List<FundDoc> preList = this.baseDao.find(hql, params.toArray(), false);
		List<AppFundDocDataVO> voList = new ArrayList();
		if(!preList.isEmpty()){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
			for(FundDoc preVo:preList){
				AppFundDocDataVO vo = new AppFundDocDataVO();
				String documentName = preVo.getDocumentName();
				String lastUpdate = sdf.format(preVo.getLastUpdate()==null?preVo.getCreateTime():preVo.getLastUpdate());
				vo.setDocName(documentName);
				vo.setLastUdate(lastUpdate);
				vo.setFileList(findFundDocDetailList(preVo.getId()));
				voList.add(vo);
			}
		}		
		return voList;
	}
	
	private List<AppFundDocDetailVO> findFundDocDetailList(String docId){
		List<AppFundDocDetailVO> voList = new ArrayList<AppFundDocDetailVO>();
		String hql = "from FundDocDetail t where t.fundDoc.id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(docId);
		List<FundDocDetail> list = baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (FundDocDetail detail : list) {
				AppFundDocDetailVO vo = new AppFundDocDetailVO();
				vo.setId(detail.getId());
				vo.setLangCode(detail.getLangCode());
				vo.setLinkUrl(detail.getLinkUrl());
				vo.setLastUpdate(DateUtil.dateToDateString(detail.getLastUpdate(), CommonConstants.FORMAT_DATE_TIME));
				voList.add(vo);
			}
		}
		return voList;
	}

}
