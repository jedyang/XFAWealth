/**
 * 
 */
package com.fsll.app.fund.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.fund.service.AppFundAnnoService;
import com.fsll.app.fund.vo.AppFundAnncInfoDataVO;
import com.fsll.app.fund.vo.AppFundAnncListDataVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.CoreConstants;
import com.fsll.wmes.entity.FundAnno;

/**
 * @author scshi
 * 基金公告列表数据接口实现
 *
 */
@Service("appFundAnnoService")
//@Transactional
public class AppFundAnnoServiceImpl extends BaseService implements AppFundAnnoService {

	/**获取基金公告
	 * @param queryAnno 分装查询条件
	 * @param jsonPaging 分页信息
	 * @param language 语言编码
	 * @return	jsonPaging	基金公告列表数据
	 */
	@Override
	public JsonPaging fundAnncList(JsonPaging jsonPaging,FundAnno queryAnno, String language) {
		String hql = "from FundAnno  t where t.isValid='1' ";
		List params = new ArrayList();
		String fundId = queryAnno.getFundId();
		String annoName = queryAnno.getAnnoName();	
		if(null!=fundId && !"".equals(fundId)){
			hql += " and t.fund.id=? ";
			params.add(fundId);
		}
		if(null!=language && !"".equals(language)){
			hql += " and t.langCode = ? ";
			params.add(language);
		}
		if(null!=annoName && !"".equals(annoName)){
			hql += " and t.annoName like ? ";
			params.add("%"+annoName+"%");
		}
		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<AppFundAnncListDataVO> list = new ArrayList<AppFundAnncListDataVO>();
		Iterator it = jsonPaging.getList().iterator();
		while(it.hasNext()){
			FundAnno preVo = (FundAnno)it.next();
			AppFundAnncListDataVO vo = new AppFundAnncListDataVO();
			vo.setAnncId(preVo.getId());
			vo.setAnncTitle(preVo.getAnnoName());
			vo.setAnncDate(DateUtil.dateToDateString(preVo.getAnnoDate(), CoreConstants.DATE_TIME_FORMAT));
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**获取基金公告详细信息
	 * @author scshi
	 * @param anncId 公共ID
	 * @return	FundAnno	公共实体
	 */
	@Override
	public AppFundAnncInfoDataVO fundAnncInfo(String anncId) {
		AppFundAnncInfoDataVO vo = new AppFundAnncInfoDataVO();
		String hql ="from FundAnno t where t.isValid='1' and t.id=? order by t.annoDate desc ";
		if(null!=anncId && !"".equals(anncId)){
			List<FundAnno> anncList = this.baseDao.find(hql, new String[]{anncId}, false);
			if(null != anncList && !anncList.isEmpty()){
				FundAnno anno = anncList.get(0);
				vo.setAnncId(anno.getId());
				vo.setAnncTitle(anno.getAnnoName());
				vo.setAnncDate(DateUtil.dateToDateString(anno.getAnnoDate(), CoreConstants.DATE_TIME_FORMAT));
				vo.setAnncContent(anno.getAnnoContent());
				vo.setFileList(findFileAttrVO(anno.getId(),"fund_anno"));
				return vo;
			}
		}
		return null;
	}
}
