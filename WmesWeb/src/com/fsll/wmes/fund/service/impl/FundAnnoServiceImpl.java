/**
 * 
 */
package com.fsll.wmes.fund.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.UriEncoder;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.FundAnno;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.fund.service.FundAnnoService;
import com.fsll.wmes.fund.vo.FundAnncListDataVO;
import com.fsll.wmes.fund.vo.FundAnnoVO;

/**
 * @author scshi
 * 基金公告列表数据接口实现
 *
 */
@Service("fundBonusService")
//@Transactional
public class FundAnnoServiceImpl extends BaseService implements FundAnnoService {

	/**3.1.11	获取基金公告
	 * @author scshi
	 * @param queryAnno 分装查询条件
	 * @param jsonPaging 分页信息
	 * @param language 语言编码
	 * @return	jsonPaging	基金公告列表数据
	 */
	@Override
	//@Transactional(readOnly=true)
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
		//hql += "order by t.annoDate desc";
		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		
		List list = new ArrayList();
		Iterator it = jsonPaging.getList().iterator();
		Integer indexNumber = (jsonPaging.getPage()-1)*jsonPaging.getRows();
		Integer index = 0;
    	while(it.hasNext()){
			index++;
			FundAnno preVo = (FundAnno)it.next();
			preVo.setXh(Integer.toString(indexNumber+index));
			list.add(preVo);
    	}
		return jsonPaging;
	}
	
	/**3.1.12	获取基金公告详细信息
	 * @author scshi
	 * @param anncId 公共ID
	 * @return	FundAnno	公共实体
	 */
	@Override
	//@Transactional(readOnly=true)
	public FundAnno fundAnncInfo(String anncId) {
		String hql ="from FundAnno t where t.isValid='1' and t.id=? order by t.annoDate desc ";
		if(null!=anncId && !"".equals(anncId)){
			List<FundAnno> anncList = this.baseDao.find(hql, new String[]{anncId}, false);
			if(null != anncList && !anncList.isEmpty()){
				FundAnno anno = anncList.get(0);
				try {
					anno.setFileList(this.findFileAttrVO(anno.getId(),"fund_anno"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return anno;
			}
		}
		return null;
	}

	/**
	 * 获取基金公告
	 * @author wwluo
	 * @param langCode
	 * @param maxResult
	 * @return
	 */
	@Override
	public List<FundAnnoVO> getFundAnnos(MemberBase loginUser, String langCode, Integer maxResult) {
		List<FundAnnoVO> vos = null;
		StringBuffer hql = new StringBuffer(""
				+ " FROM"
				+ " FundAnno a"
				+ " WHERE"
				+ " a.isValid=1"
				+ " ORDER BY"
				+ " a.lastUpdate"
				+ " DESC"
				+ "");
		List<FundAnno> fundAnnos = this.baseDao.find(hql.toString(), null, false, maxResult);
		if(fundAnnos !=null && !fundAnnos.isEmpty()){
			vos = new ArrayList<FundAnnoVO>();
			for (FundAnno fundAnno : fundAnnos) {
				FundAnnoVO vo = new FundAnnoVO();
				BeanUtils.copyProperties(fundAnno, vo);
				vo.setAnnoContent(UriEncoder.encode(new String(fundAnno.getAnnoContent())));
				String dateFormat = loginUser.getDateFormat();
				if (StringUtils.isBlank(dateFormat)) {
					dateFormat = CommonConstants.FORMAT_DATE;
				}
				if(vo.getAnnoDate() != null){
					vo.setAnnoDateFormat(DateUtil.dateToDateString(vo.getAnnoDate(), dateFormat));
				}
				vos.add(vo);
			}
		}
		return vos;
	}

}
