package com.fsll.app.ifa.market.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.app.ifa.market.service.AppCrmProposalService;
import com.fsll.app.ifa.market.vo.AppCrmProposalItemVO;
import com.fsll.app.ifa.market.vo.AppCrmProposalVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmProposal;

/**
 * IFA-Market投资方案服务
 * @author zxtan
 * @date 2017-03-29
 */
@Service("appIfaMarketProposalService")
public class AppCrmProposalServiceImpl extends BaseService implements
AppCrmProposalService {

	
	/**
	 * 得到我的投资方案列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param clientMemberId Client member id
	 * @param keyword 搜索关键词
	 * @param status 状态
	 * @param minAmount 初始投资额下限
	 * @param maxAmount 初始投资额上限
	 * @return
	 */
	public JsonPaging findMyProposalList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String status,String minAmount,String maxAmount,String toCurrency,String langCode){
		List<AppCrmProposalItemVO>  voList = new ArrayList<AppCrmProposalItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from CrmProposal m ");
		hql.append(" inner join MemberIfa i on i.id=m.ifaMember.id ");
		hql.append(" inner join MemberBase b on b.id=m.member.id ");
		hql.append(" inner join CrmCustomer c on c.member.id=b.id and c.ifa.id=i.id ");
		hql.append(" where m.isValid='1' and i.member.id=? ");
		List params = new ArrayList();
		params.add(ifaMemberId);
		
		if(!"".equals(keyword)){
			hql.append(" and m.proposalName like ? ");
			params.add("%"+keyword+"%");
		}

		if(!"".equals(status)){
			hql.append(" and m.status = ? ");
			params.add(status);
		}
		
		if(!"".equals(minAmount)){
			hql.append(" and get_exchange_rate(m.baseCurrId,?,m.initialInvestAmount) >= ? ");
			params.add(toCurrency);
			params.add(Double.parseDouble(minAmount));
		}
		
		if(!"".equals(maxAmount)){
//			hql.append(" and m.initialInvestAmount < ? ");
			hql.append(" and get_exchange_rate(m.baseCurrId,?,m.initialInvestAmount) < ? ");
			params.add(toCurrency);
			params.add(Double.parseDouble(maxAmount));
		}
		
		hql.append(" order by m.lastUpdate desc ");

		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);				
		List list = jsonPaging.getList();
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[]) list.get(i);
				CrmProposal info = (CrmProposal) objs[0];
				CrmCustomer customer = (CrmCustomer) objs[3];
				
				AppCrmProposalItemVO vo =new AppCrmProposalItemVO();
				vo.setId(info.getId());
				vo.setProposalName(info.getProposalName());
				
				if("".equals(toCurrency)){
					vo.setBaseCurrId(getParamConfigName(langCode, info.getBaseCurrId(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
					vo.setInitialInvestAmount(getFormatNum(info.getInitialInvestAmount(),info.getBaseCurrId()));
				}else {
					vo.setBaseCurrId(toCurrencyName);
					vo.setInitialInvestAmount(getFormatNumByCurrency(info.getInitialInvestAmount(),info.getBaseCurrId(),toCurrency));	
				}
				
//				vo.setBaseCurrId(info.getBaseCurrId());
//				vo.setInitialInvestAmount(getFormatNum(info.getInitialInvestAmount(),info.getBaseCurrId()));
				vo.setStatus(info.getStatus());
				vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				vo.setMemberId(customer.getMember().getId());
				if("1".equals(customer.getClientType())){
					vo.setClientName(getCommonMemberName(customer.getMember().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				}else {
					if(StringUtils.isNotBlank(customer.getNickName())){
						vo.setClientName(customer.getNickName());
					}else {
						vo.setClientName(getCommonMemberName(customer.getMember().getId(), langCode, CommonConstants.MEMBER_NAME_NICKNAME));
					}
				}
				
				vo.setIconUrl(PageHelper.getUserHeadUrlForWS(customer.getMember().getIconUrl(), null));
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}
	
	
	/**
	 * 得到投资方案基本信息
	 * @param proposalId 方案ID
	 * @return
	 */
	public AppCrmProposalVO findProposal(String proposalId,String toCurrency,String langCode){
		AppCrmProposalVO vo =new AppCrmProposalVO();
		String hql = " from CrmProposal p  where p.id=? ";
		List params = new ArrayList();
		params.add(proposalId);
		List list = baseDao.find(hql, params.toArray(), false);
//		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null!=list && !list.isEmpty()){
			CrmProposal info=(CrmProposal)list.get(0);
			vo.setId(info.getId());
			vo.setProposalName(info.getProposalName());
			vo.setBaseCurrId(info.getBaseCurrId());
			if("".equals(toCurrency)){
				vo.setBaseCurrId(getParamConfigName(langCode, info.getBaseCurrId(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
				vo.setInitialInvestAmount(getFormatNum(info.getInitialInvestAmount(),info.getBaseCurrId()));
			}else {
				vo.setBaseCurrId(getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY));
				vo.setInitialInvestAmount(getFormatNumByCurrency(info.getInitialInvestAmount(),info.getBaseCurrId(),toCurrency));	
			}
			
			vo.setLiquidityNeed(info.getLiquidityNeed());
			vo.setTimeFrame(info.getTimeFrame());
			vo.setTaxConcerns(info.getTaxConcerns());
			vo.setLrf(info.getLrf());
			vo.setUnp(info.getUnp());
			vo.setRemark(info.getRemark());
			vo.setStatus(info.getStatus());
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			String pdfUrl = findProposalPdfFile(info.getId(),langCode);
			vo.setPdfUrl(pdfUrl);
		}
		return vo;
	}
	
	private String findProposalPdfFile(String proposalId,String langCode){
		String hql = "from AccessoryFile f where f.moduleType='crm_proposal' and f.relateId=? and f.langCode=?";
		List<Object> params = new ArrayList<Object>();
		params.add(proposalId);
		params.add(langCode);
		List<AccessoryFile> files = baseDao.find(hql, params.toArray(), false);
		if(null != files && !files.isEmpty()){
			return files.get(0).getFilePath();
		}
		return null;
	}

}
