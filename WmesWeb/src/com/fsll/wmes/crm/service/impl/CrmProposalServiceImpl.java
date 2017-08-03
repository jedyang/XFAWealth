/**
 * 
 */
package com.fsll.wmes.crm.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.UriEncoder;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmProposalService;
import com.fsll.wmes.crm.vo.PortfolioInfoProductDetailVO;
import com.fsll.wmes.crm.vo.PortfolioInfoProductVO;
import com.fsll.wmes.crm.vo.ProposalCheckVO;
import com.fsll.wmes.crm.vo.ProposalDataVO;
import com.fsll.wmes.crm.vo.ProposalEmailRecordsVO;
import com.fsll.wmes.crm.vo.ProposalVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.CrmProposalCheck;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.IfaMigrateHist;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.OrderPlanCheck;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.PortfolioInfoAip;
import com.fsll.wmes.entity.PortfolioInfoProduct;
import com.fsll.wmes.entity.PortfolioInfoProductDetail;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.ifa.service.IfaMigrateHistService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.trade.vo.OrderPlanCheckVO;


/**
 * 投资方案信息查询服务接口实现
 * @author tan
 * @date 2016-8-16
 */
@Service("crmProposalService")
//@Transactional
public class CrmProposalServiceImpl extends BaseService implements CrmProposalService {
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private IfaMigrateHistService ifaMigrateHistService;
	
	@Override
	public JsonPaging findAllProposal(JsonPaging jsonPaging, String keyword, MemberAdmin memberAdmin) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from CrmProposal l ");
//		hql.append(" left join CrmCustomer r on l.customer.id=r.id ");
		hql.append(" where 1=1 ");
		if(keyword != null && !"".equals(keyword)){
			hql.append(" and ( l.proposalName like ?  or l.currencyType like ? )");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		
//		String amdinType = memberAdmin.getType();
//		
//		if(CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equalsIgnoreCase(amdinType)){
//			
//		}else if(CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equalsIgnoreCase(amdinType)){
//			
//		}else {
//			
//		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);

		return jsonPaging;
	}

	@Override
	public CrmProposal findProposalById(String id) {
		// TODO Auto-generated method stub
		CrmProposal obj = (CrmProposal) this.baseDao.get(CrmProposal.class, id);
		return obj;
	}
	
	/**
	 * 获取Ifa管理的投资方案
	 * @author zxtan   modify by wwluo 20170424
	 * @date 2016-09-06
	 */
	@Override
	public JsonPaging findProposalListForIfa(JsonPaging jsonPaging,String ifaMemberId, String customerMemberId, String langCode) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from CrmProposal l ");
		hql.append(" where l.isValid='1' and l.ifaMember.member.id=? and l.member.id=? ");
		params.add(ifaMemberId);
		params.add(customerMemberId);
				
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);
		if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
			List<CrmProposal> crmProposals = jsonPaging.getList();
			List<ProposalVO> vos = new ArrayList<ProposalVO>();
			for (CrmProposal crmProposal : crmProposals) {
				ProposalVO vo = new ProposalVO();
				BeanUtils.copyProperties(crmProposal, vo);
				if(crmProposal.getCreateTime() != null){
					vo.setCreateTimeStr(DateUtil.dateToDateString(crmProposal.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
				}
				if (StringUtils.isNotBlank(crmProposal.getBaseCurrId())) {
					vo.setCurrencyType(this.getParamConfigName(langCode, crmProposal.getBaseCurrId(), CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY));
				}
				vos.add(vo);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(vos);
		}
		return jsonPaging;
	} 
	
	/**
	 * 获取ifa的投资方案
	 * @param MemberBase 当前账号
	 * @param period 期间
	 * @param status 状态
	 * @param keyWord 关键字
	 * @date 2016-09-14
	 */
	@Override
	public JsonPaging getProposal(JsonPaging jsonPaging, MemberBase loginUser,String period,
			String fromDate,String toDate,String status,String keyWord, String clients, String langCode) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(" from CrmProposal c left join CrmCustomer r on c.ifaMember.id=r.ifa.id and c.member.id=r.member.id ");
		hql.append(" where c.isValid=1 and r !=null");
		//过滤   期间
		if (StringUtils.isNotBlank(period)) {
			Calendar calendarc = Calendar.getInstance();
			switch (Integer.parseInt(period)) {
				case 1:calendarc.add(Calendar.MONTH, -1);
					break;
				case 3:calendarc.add(Calendar.MONTH, -3);
					break;
				case 6:calendarc.add(Calendar.MONTH, -6);
					break;
				case 12:calendarc.add(Calendar.YEAR, -1);
					break;
				case 36:calendarc.add(Calendar.YEAR, -3);
					break;
			}
			hql.append(" and c.createTime between ? and CURDATE()");
			params.add(calendarc.getTime());
		}
		//过滤  时间
		if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				hql.append(" and c.lastUpdate between ? and ?");
				params.add(dateFormat.parse(fromDate));
				params.add(dateFormat.parse(toDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		//过滤   客户姓名
		if (StringUtils.isNotBlank(keyWord)) {
			hql.append(" and (r.nickName like ? or c.proposalName like ?  )");
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		//过滤   状态
		if (StringUtils.isNotBlank(status)) {
			hql.append(" and c.status=?");
			params.add(status);
		}
		if (StringUtils.isNotBlank(clients)) {
			hql.append(" and r.member.id=?");
			params.add(clients);
		}
		//过滤   ifa
		if(loginUser != null){
			hql.append(" and c.creator=?");
			params.add(loginUser);
			jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
			
		}
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			List list=new ArrayList();
			while (it.hasNext()) {
				Object[] object = (Object[]) it.next();
				CrmProposal proposal=(CrmProposal)object[0];
				CrmCustomer customer=(CrmCustomer)object[1];
				ProposalVO vo=new ProposalVO();
				vo.setId(proposal.getId());
				vo.setCreateTime(proposal.getCreateTime());
				vo.setCreatorId(proposal.getCreator().getId());
				vo.setCreatorName(getCommonMemberName(proposal.getCreator().getId(), langCode, "2"));
				vo.setCurrencyName(this.getParamConfigName(langCode, proposal.getBaseCurrId(), CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE));
				vo.setCurrencyType(proposal.getBaseCurrId());
				vo.setStatus(proposal.getStatus());
				vo.setCustomerId(customer.getId());
				String nickName=getCommonMemberName(proposal.getMember().getId(), langCode, "2");
				vo.setCustomerName(nickName);
				vo.setInitialInvestAmount(proposal.getInitialInvestAmount());
				vo.setProposalName(proposal.getProposalName());
				vo.setLastUpdate(DateUtil.dateToDateString(proposal.getLastUpdate(), DateUtil.DEFAULT_DATE_TIME_FORMAT));
				list.add(vo);
				
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(list);
		}
		return jsonPaging;
	}

	/**
	 * 删除投资方案（逻辑删除）
	 * @author wwluo
	 * @date 2016-09-18
	 */
	@Override
	public boolean delProposal(String proposalId) {
		boolean flag = false;
		if (StringUtils.isNotBlank(proposalId)) {
			CrmProposal crmProposal = 
				(CrmProposal) this.baseDao.get(CrmProposal.class, proposalId);
			if(null != crmProposal){
				crmProposal.setIsValid("0");
				this.baseDao.update(crmProposal);
				flag = true;
			}
		}
		return flag;
	}

	/*@Override
	public CrmProposal addCrmProposal(CrmProposal crmProposal) {
		CrmProposal proposal=(CrmProposal)this.baseDao.saveOrUpdate(crmProposal, true);
		return proposal;
	}
*/
	@Override
	public CrmProposal saveCrmProposal(CrmProposal crmProposal) {
		CrmProposal proposal=(CrmProposal)this.baseDao.saveOrUpdate(crmProposal);
		return proposal;
	}

	/**
	 * 根据方案查找组合
	 * @author wwluo
	 * @date 2016-09-29
	 * @param crmProposal
	 * @return
	 */
	@Override
	public PortfolioInfo findPortfolioInfoByProposal(CrmProposal proposal) {
		PortfolioInfo portfolioInfo = null;
		StringBuffer hql = new StringBuffer(" from PortfolioInfo where isValid=1 and proposal=?");
		List params = new ArrayList();
		params.add(proposal);
		List<PortfolioInfo> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			portfolioInfo = list.get(0);
		}
		return portfolioInfo;
	}

	/**
	 * 创建方案时保存组合定投信息
	 * @author wwluo
	 * @date 2016-09-29
	 * @param crmProposal
	 * @return
	 */
	@Override
	public PortfolioInfoAip saveAndUpdatePortfolioInfoAip(
			PortfolioInfoAip portfolioInfoAip) {
		return (PortfolioInfoAip) this.baseDao.saveOrUpdate(portfolioInfoAip);
	}

	/**
	 * 创建方案时保存组合产品信息
	 * @author wwluo
	 * @date 2016-09-29
	 * @param crmProposal
	 * @return
	 */
	@Override
	public PortfolioInfoProduct saveAndUpdatePortfolioInfoProduct(
			PortfolioInfoProduct portfolioInfoProduct) {
		return (PortfolioInfoProduct) this.baseDao.saveOrUpdate(portfolioInfoProduct);
	}

	/**
	 * 获取投资人的所有proposal
	 * @author mqzou modify by wwluo 20170424
	 * @date 2016-10-21
	 * @param memberId
	 * @return
	 */
	@Override
	public List findMyProposal(String memberId, String currencyCode, String langCode,String dateFormat) {
		List<ProposalVO> vos = null;
		String hql="  from CrmProposal r where r.member.id=? and r.isValid='1' and r.status!='-2' and r.status!='0' order by r.lastUpdate DESC";
		List params=new ArrayList();
		params.add(memberId);
		List<CrmProposal> list=this.baseDao.find(hql, params.toArray(), false);
		if(list != null && !list.isEmpty()){
			vos = new ArrayList<ProposalVO>();
			for (CrmProposal crmProposal : list) {
				ProposalVO vo = new ProposalVO();
				BeanUtils.copyProperties(crmProposal, vo);
				Double exChangeRage = null;
				if(StringUtils.isNotBlank(crmProposal.getBaseCurrId())){
					exChangeRage = this.getExchangeRate(crmProposal.getBaseCurrId(), currencyCode);
				}
				if(exChangeRage == null){
					exChangeRage = 1.00;
				}
				if(crmProposal.getInitialInvestAmount() != null){
					vo.setInitialInvestAmount(crmProposal.getInitialInvestAmount() * exChangeRage);
				}
				vo.setCurrencyName(this.getParamConfigName(langCode, currencyCode, CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY));
				if(crmProposal.getLastUpdate() != null){
					vo.setLastUpdate(DateUtil.dateToDateString(crmProposal.getLastUpdate(), dateFormat));
				}
				vos.add(vo);
			}
		}
		return vos;
	}

	/**
	 * proposal 相关所有信息，PDF页面展示
	 * @author wwluo
	 * @date 2016-12-1
	 * @param proposalId CrmProposal 主键
	 * @param langCode 多语言标记
	 * @return ProposalDataVO proposal 相关所有信息VO
	 */
	@Override
	public ProposalDataVO getProposalDataVO(String proposalId,String langCode) {
		ProposalDataVO vo = new ProposalDataVO();
		if (StringUtils.isNotBlank(proposalId)) {
			CrmProposal proposal = (CrmProposal) this.baseDao.get(CrmProposal.class, proposalId);
			vo.setProposal(proposal);
			if(proposal != null){
				PortfolioInfo portfolio = getPortfolioByProposal(proposal);
				vo.setPortfolio(portfolio);
				if(portfolio != null){
					PortfolioInfoAip aip = getAipByPorfolio(portfolio);
					vo.setAip(aip);
					List<PortfolioInfoProduct> products = getProductByPortfolio(portfolio);
					if(products != null && !products.isEmpty()){
						List<PortfolioInfoProductVO> productVOs = new ArrayList<PortfolioInfoProductVO>();
						for (PortfolioInfoProduct product : products) {
							PortfolioInfoProductVO productVO = new PortfolioInfoProductVO();
							BeanUtils.copyProperties(product, productVO);
							ProductInfo info = product.getProduct();
							if(info != null){
								FundInfo fundInfo = getFundInfoByProduct(product.getProduct());
								productVO.setFundInfo(fundInfo);
								String fundName = getFundNameById(langCode, fundInfo.getId());
								productVO.setFundName(fundName);
								List<PortfolioInfoProductDetail> productDetails = getProductDetailByPortfolioProduct(
										portfolio, info);
								if(productDetails != null && !productDetails.isEmpty()){
									List<PortfolioInfoProductDetailVO> details = new ArrayList<PortfolioInfoProductDetailVO>();
									for (PortfolioInfoProductDetail productDetail : productDetails) {
										PortfolioInfoProductDetailVO detailVO = new PortfolioInfoProductDetailVO();
										BeanUtils.copyProperties(productDetail, detailVO);
										detailVO.setSpareProduct(productDetail.getCandidateProduct());
										FundInfo spareFundInfo = getFundInfoByProduct(productDetail.getCandidateProduct());
										detailVO.setSpareFundInfo(spareFundInfo);
										String spareFundName = null;
										if(spareFundInfo != null){
											spareFundName = getFundNameById(langCode, spareFundInfo.getId());
										}
										detailVO.setSpareFundName(spareFundName);
										details.add(detailVO);
									}
									productVO.setProductDetails(details);
								}
							}
							productVOs.add(productVO);
 						}
						vo.setProducts(productVOs);
					}
				}
			}
		}
		return vo;
	}

	/**
	 * 获取基金名称
	 * @author wwluo
	 * @date 2016-12-1
	 * @param fundId 
	 * @param langCode 多语言标记
	 * @return String 基金名称
	 */
	@Override
	public String getFundNameById(String langCode, String fundId) {
		String fundName = null;
		if (StringUtils.isNotBlank(fundId)) {
			if(CommonConstants.LANG_CODE_SC.equals(langCode)){
				FundInfoSc fundInfoSc = (FundInfoSc) this.baseDao.get(FundInfoSc.class, fundId);
				if(fundInfoSc != null){
					fundName = fundInfoSc.getFundName();
				}
			}else if(CommonConstants.LANG_CODE_TC.equals(langCode)){
				FundInfoTc fundInfoTc = (FundInfoTc) this.baseDao.get(FundInfoTc.class, fundId);
				if(fundInfoTc != null){
					fundName = fundInfoTc.getFundName();
				}
			}else if(CommonConstants.LANG_CODE_EN.equals(langCode)){
				FundInfoEn fundInfoEn = (FundInfoEn) this.baseDao.get(FundInfoEn.class, fundId);
				if(fundInfoEn != null){
					fundName = fundInfoEn.getFundName();
				}
			}
		}
		return fundName;
	}

	/**
	 * 获取组合候选产品信息
	 * @author wwluo
	 * @date 2016-12-1
	 * @param portfolio 对应组合 
	 * @param info 主产品
	 * @return List<PortfolioInfoProductDetail> 组合候选产品集合
	 */
	@Override
	public List<PortfolioInfoProductDetail> getProductDetailByPortfolioProduct(
			PortfolioInfo portfolio, ProductInfo info) {
		StringBuffer productDetailHql = new StringBuffer("" +
			" FROM" +
			" PortfolioInfoProductDetail i" +
			" WHERE" +
			" i.portfolio=?" +
			" AND" +
			" i.product=?" +
			"");
		List<Object> productDetailParams = new ArrayList<Object>();
		productDetailParams.add(portfolio);
		productDetailParams.add(info);
		List<PortfolioInfoProductDetail> productDetails = this.baseDao.find(productDetailHql.toString(), productDetailParams.toArray(), false);
		return productDetails;
	}

	/**
	 * 根据产品获取对应基金
	 * @author wwluo
	 * @date 2016-12-1
	 * @param portfolio 对应产品
	 * @return FundInfo 基金信息
	 */
	@Override
	public FundInfo getFundInfoByProduct(ProductInfo product) {
		FundInfo fundInfo = null;
		if(product != null){
			StringBuffer fundInfoHql = new StringBuffer("" +
					" FROM" +
					" FundInfo i" +
					" WHERE" +
					" i.isValid=1" +
					" AND" +
					" i.product=?" +
			"");
			List<Object> fundInfoParams = new ArrayList<Object>();
			fundInfoParams.add(product);
			List<FundInfo> fundInfos = this.baseDao.find(fundInfoHql.toString(), fundInfoParams.toArray(), false);
			if(fundInfos != null && !fundInfos.isEmpty()){
				fundInfo = fundInfos.get(0);
			}
		}
		return fundInfo;
	}

	/**
	 * 根据组合获取产品集合
	 * @author wwluo
	 * @date 2016-12-1
	 * @param portfolio 对应组合
	 * @return List<PortfolioInfoProduct> 产品集合
	 */
	@Override
	public List<PortfolioInfoProduct> getProductByPortfolio(
			PortfolioInfo portfolio) {
		StringBuffer productHql = new StringBuffer("" +
				" FROM" +
				" PortfolioInfoProduct i" +
				" WHERE" +
				" i.portfolio=?" +
		"");
		List<Object> productParams = new ArrayList<Object>();
		productParams.add(portfolio);
		List<PortfolioInfoProduct> products = this.baseDao.find(productHql.toString(), productParams.toArray(), false);
		return products;
	}

	/**
	 * 根据组合获取定投信息
	 * @author wwluo
	 * @date 2016-12-1
	 * @param portfolio 对应组合
	 * @return PortfolioInfoAip 定投信息
	 */
	@Override
	public PortfolioInfoAip getAipByPorfolio(PortfolioInfo portfolio) {
		PortfolioInfoAip aip = null;
		if(portfolio != null){
			StringBuffer aipHql = new StringBuffer("" +
					" FROM" +
					" PortfolioInfoAip i" +
					" WHERE" +
					" i.portfolio=?" +
					" ORDER BY" +
					" i.lastUpdate" +
					" DESC" +
					"");
			List<Object> aipParams = new ArrayList<Object>();
			aipParams.add(portfolio);
			List<PortfolioInfoAip> aips = this.baseDao.find(aipHql.toString(), aipParams.toArray(), false);
			if(aips != null && !aips.isEmpty()){
				aip = aips.get(0);
			}
		}
		return aip;
	}
	
	/**
	 * 根据方案获取关联的组合
	 * @author wwluo
	 * @date 2016-12-1
	 * @param proposal 对应方案
	 * @return PortfolioInfo 关联的组合
	 */
	@Override
	public PortfolioInfo getPortfolioByProposal(CrmProposal proposal) {
		PortfolioInfo portfolioInfo = null;
		if(proposal != null){
			StringBuffer portfolioHql = new StringBuffer("" +
					" FROM" +
					" PortfolioInfo i" +
					" WHERE" +
					" isValid=1" +
					" AND" +
					" i.proposal=?" +
					"");
			List<Object> portfolioParams = new ArrayList<Object>();
			portfolioParams.add(proposal);
			List<PortfolioInfo> portfolioInfos = this.baseDao.find(portfolioHql.toString(), portfolioParams.toArray(), false);
			if(portfolioInfos != null && !portfolioInfos.isEmpty()){
				portfolioInfo = portfolioInfos.get(0);
			}
		}
		return portfolioInfo;
	}
	
	/**
	 * 获取ifa创建的proposal
	 * @author mqzou 2016-12-26
	 * @param jsonPaging
	 * @param keyWord
	 * @param ifaMemberId
	 * @return
	 */
	@Override
	public JsonPaging findProposal(JsonPaging jsonPaging, String strCondition, String ifaMemberId,String currency,String langCode) {
		StringBuilder hql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		hql.append(" from CrmProposal r   ");
		hql.append(" left join MemberIfa i on r.creator.id=i.member.id");
		hql.append(" left join CrmCustomer c on r.member.id=c.member.id and c.ifa.id=i.id");
		hql.append(" where r.isValid='1' and r.creator.id=?");
		params.add(ifaMemberId);
		if(null!=strCondition && !"".equals(strCondition)){
			hql.append(strCondition);
		}
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		Iterator it=jsonPaging.getList().iterator();
		List<ProposalVO> list=new ArrayList<ProposalVO>();
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();
			CrmProposal proposal=(CrmProposal)object[0];
			CrmCustomer customer=(CrmCustomer)object[2];
			ProposalVO vo=new ProposalVO();
			vo.setId(proposal.getId());
			if(null!=customer){
				vo.setCustomerId(customer.getId());
				if("1".equals(customer.getClientType())){
					String nickName=getCommonMemberName(proposal.getMember().getId(), langCode, "2");
					vo.setCustomerName(nickName);
				}else {
					vo.setCustomerName(customer.getNickName());
				}
				
			}
			
			vo.setProposalName(proposal.getProposalName());
			vo.setLastUpdate(DateUtil.getDateStr(proposal.getLastUpdate())); 
			String baseCurrency=proposal.getBaseCurrId();
			if(currency.equals(baseCurrency)){
				vo.setInitialInvestAmount(proposal.getInitialInvestAmount());
			}else {
				if(null==baseCurrency || "".equals(baseCurrency))
					baseCurrency=CommonConstants.DEF_CURRENCY_HKD;
				vo.setInitialInvestAmount(getNumByCurrency(proposal.getInitialInvestAmount(), baseCurrency, currency));
			}
			
			vo.setStatus(proposal.getStatus());
			
			list.add(vo);
			
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 根据proposal获取portfolio
	 * @author wwluo
	 * @return
	 */
	@Override
	public PortfolioInfo findPortfolioByProposalId(String proposalId) {
		PortfolioInfo info = null;
		if(StringUtils.isNotBlank(proposalId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioInfo p" +
					" WHERE" +
					" p.isValid=1" +
					" And" +
					" p.proposal.id=?" +
					" ORDER BY" +
					" p.lastUpdate" +
					" DESC");
			List<Object> params = new ArrayList<Object>();
			params.add(proposalId);
			List<PortfolioInfo> infos = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(infos != null && !infos.isEmpty()){
				info = infos.get(0);
			}
		}
		return info;
	}
	
	/**
	 * 保存备选基金
	 * @author wwluo
	 * @return
	 */
	@Override
	public PortfolioInfoProductDetail saveAndUpdatePortfolioInfoProductDetail(
			PortfolioInfoProductDetail productDetail) {
		productDetail = (PortfolioInfoProductDetail) this.baseDao.saveOrUpdate(productDetail);
		return productDetail;
	}

	/**
	 * 获取基金详情信息
	 * @author wwluo
	 * @return
	 */
	@Override
	public List<PortfolioInfoProductDetail> getProductDetailByPortfolioProduct(
			String portfolioId) {
		List<PortfolioInfoProductDetail> details = null;
		if (StringUtils.isNotBlank(portfolioId)) {
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioInfoProductDetail d" +
					" WHERE" +
					" d.portfolio.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(portfolioId);
			details = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return details;
	}

	/**
	 * 获取组合产品
	 * @author wwluo
	 * @return
	 */
	@Override
	public PortfolioInfoProduct getProduct(String portfolioId, String productId) {
		PortfolioInfoProduct product = null;
		if (StringUtils.isNotBlank(portfolioId) && StringUtils.isNotBlank(productId)) {
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioInfoProduct p" +
					" WHERE" +
					" p.portfolio.id=?" +
					" AND" +
					" p.product.id=? ");
			List<Object> params = new ArrayList<Object>();
			params.add(portfolioId);
			params.add(productId);
			List<PortfolioInfoProduct> infoProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(!infoProducts.isEmpty() && infoProducts != null){
				product = infoProducts.get(0);
			}
		}
		return product;
	}
	
	/**
	 * 获取ifa管理的投资方案(需转换货币)
	 * @author mqzou 2017-02-17
	 */
	@Override
	public JsonPaging findProposalForIfa(JsonPaging jsonPaging, String ifaMemberId, String customerMemberId, String currency) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from CrmProposal l ");
		hql.append(" where l.ifaMember.member.id=? and l.member.id=? ");
		params.add(ifaMemberId);
		params.add(customerMemberId);
				
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);
		Iterator it=jsonPaging.getList().iterator();
		while (it.hasNext()) {
			CrmProposal proposal = (CrmProposal) it.next();
			double investAmount=StrUtils.getDoubleVal(proposal.getInitialInvestAmount());
			if(investAmount==0)
				continue;
			String baseCur=proposal.getBaseCurrId();
			investAmount=getNumByCurrency(investAmount, baseCur, currency);
			proposal.setBaseCurrId(currency);
			proposal.setInitialInvestAmount(investAmount);
		}
		return jsonPaging;
	}
	
	/**
	 * 获取ifa管理的投资方案
	 * @author michael
	 * @param ifaMemberId
	 * @return list
	 */
	@SuppressWarnings("unchecked")
    public List<CrmProposal> findCrmProposalByIfa(String ifaMemberId) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from CrmProposal l ");
		hql.append(" where l.isValid='1' and l.ifaMember.member.id=? ");
		params.add(ifaMemberId);
				
		List<CrmProposal> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		return list;
	}
	
	/**
	 * 检测是否存在交易计划
	 * @author michael
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfExistProposal(String ifaMemberId, String memberId) {
		String hql=" from CrmProposal r where r.isValid='1' and r.ifaMember.member.id=? and r.member.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * 修改IFA的客户到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaCrmProposal(String fromMemberId,String toMemberId,MemberBase createBy) {
		List<CrmProposal> list = findCrmProposalByIfa(fromMemberId);
		MemberIfa ifa = memberBaseService.findIfaMember(toMemberId);
		if (null!=list && !list.isEmpty() && null!=ifa && !StrUtils.isEmpty(ifa.getId())){
			for (CrmProposal f: list){
				boolean status = false;
				try{
					//更新方式
					f.setIfaMember(ifa);
					this.baseDao.update(f);
					
					//复制方式
	//				if (!checkIfExistProposal(toMemberId, f.getIfaMember().getMember().getId())){
	//					CrmProposal nProposal = new CrmProposal();
	//					BeanUtils.copyProperties(f, nProposal);
	//					nProposal.setId(null);
	//					nProposal.setRemark(nProposal.getRemark()+"\n(copy from "+nProposal.getIfaMember().getMember().getNickName()+")");
	//					nProposal.setCreateTime(new Date());
	//					nProposal.setLastUpdate(new Date());
	//					this.baseDao.saveOrUpdate(nProposal);
						
	//					//旧记录设为冻结状态
	//					f.setIsValid("0");
	//					this.baseDao.update(f);
	//				}
					status = true;
	            } catch (Exception e) {
	                // TODO: handle exception
	            }
	            
				//保存日志
				IfaMigrateHist hist = new IfaMigrateHist();
				hist.setCreateBy(createBy);
				hist.setCreateTime(new Date());
				hist.setFromMember(memberBaseService.findById(fromMemberId));
				hist.setToMember(ifa.getMember());
				hist.setCusMember(f.getMember());
				hist.setDataType("CrmProposal");
				hist.setStatus(status?"1":"0");
				ifaMigrateHistService.saveOrUpdate(hist);
			}
			return true;
		}
		return false;
	}

	/**
	 * 获取proposal
	 * @author wwluo
	 * @date 2017-3-1
	 * @param ifaMemberId
	 * @param memberId
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CrmProposal> getProposal(String ifaMemberId, String memberId) {
		List<CrmProposal> proposals = null;
		if (StringUtils.isNotBlank(ifaMemberId) && StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(""
					+ " FROM"
					+ " CrmProposal c"
					+ " WHERE"
					+ " c.member.id=?"
					+ " AND"
					+ " c.ifaMember.id=?"
					+ " AND"
					+ " c.isValid=1"
					+ " ORDER BY"
					+ " c.lastUpdate"
					+ " DESC"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			params.add(ifaMemberId);
			proposals = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return proposals;
	}

	/**
	 * 保存方案审核信息
	 * @author wwluo
	 * @date 2017-4-28
	 * @param check 方案审核实体
	 * @return CrmProposalCheck 方案审核实体
	 */
	@Override
	public CrmProposalCheck saveProposalCheck(CrmProposalCheck check) {
		return (CrmProposalCheck) this.baseDao.saveOrUpdate(check);
	}

	/**
	 * 获取投资方案审核记录
	 * @author wwluo
	 * @date 2017-4-28
	 * @param proposalId 投资方案id
	 * @param langCode 多语言标识
	 * @return List<ProposalCheckVO> 方案审核VO实体集合 
	 */
	@Override
	public List<ProposalCheckVO> geProposalCheck(String proposalId,
			String langCode) {
		List<ProposalCheckVO> checkVOs = null;
		if(StringUtils.isNotBlank(proposalId)){
			StringBuffer hql = new StringBuffer("" +
					  " FROM"
					+ " CrmProposalCheck c"
					+ " WHERE"
					+ " c.proposal.id=?"
					+ " ORDER BY"
					+ " c.checkTime"
					+ " DESC");
			List<Object> params = new ArrayList<Object>();
			params.add(proposalId);
			List<CrmProposalCheck> checks = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(checks != null && !checks.isEmpty()){
				checkVOs = new ArrayList<ProposalCheckVO>();
				for (CrmProposalCheck proposalCheck : checks) {
					ProposalCheckVO vo = new ProposalCheckVO();
					BeanUtils.copyProperties(proposalCheck, vo);
					if(proposalCheck.getProposal() != null){
						vo.setProposalId(proposalCheck.getProposal().getId());
					}
					if(proposalCheck.getCheck() != null){
						vo.setCheckId(proposalCheck.getCheck().getId());
						String name = getCommonMemberName(proposalCheck.getCheck().getId(), langCode, "2");
						vo.setApproval(name);
					}
					vo.setCheckStatusDisplay(PropertyUtils.getPropertyValue(langCode, "create.proposal.check.status." + proposalCheck.getCheckStatus(), null));
					checkVOs.add(vo);
				}
			}
		}
		return checkVOs;
	}

	/**
	 * 获取member的投资方案审批记录
	 * @author wwluo
	 * @data 2017-04-27
	 * @param memberId
	 * @param proposalId
	 * @param status 审核状态
	 * @return
	 */
	@Override
	public List<CrmProposalCheck> ifApprovalForProposal(String memberId,
			String proposalId, String status) {
		List<CrmProposalCheck> checks = null;
		if(StringUtils.isNotBlank(proposalId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" CrmProposalCheck c" +
					" WHERE" +
					" c.proposal.id=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(proposalId);
			if (StringUtils.isNotBlank(memberId)) {
				hql.append(" AND c.check.id=?");
				params.add(memberId);
			}
			if (StringUtils.isNotBlank(status)) {
				hql.append(" AND c.checkStatus=?");
				params.add(status);
			}
			hql.append(" ORDER BY c.checkTime DESC");
			checks = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return checks;
	}

	/**
	 * 获取方案邮件发送记录
	 * @author wwluo
	 * @data 2017-04-27
	 * @param proposalId
	 * @param langCode
	 * @param dateFormat
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProposalEmailRecordsVO> getEmailRecords(String proposalId,
			String langCode, String dateFormat) {
		List<ProposalEmailRecordsVO> records = null;
		if (StringUtils.isNotBlank(proposalId)) {
			StringBuffer hql = new StringBuffer(""
					+ " FROM"
					+ " WebEmailLog w"
					+ " WHERE"
					+ " w.relateId=?"
					+ " AND"
					+ " w.moduleType=?"
					+ " ORDER BY"
					+ " w.sendedTime"
					+ " DESC");
			List<Object> params = new ArrayList<Object>();
			params.add(proposalId);
			params.add(CommonConstantsWeb.WEB_EMAIL_LOG_PROPOSAL);
			List<WebEmailLog> logs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(logs != null && !logs.isEmpty()){
				records = new ArrayList<ProposalEmailRecordsVO>();
				for (WebEmailLog webEmailLog : logs) {
					ProposalEmailRecordsVO vo = new ProposalEmailRecordsVO();
					BeanUtils.copyProperties(webEmailLog, vo);
					if(webEmailLog.getToMember() != null){
						vo.setToMemberId(webEmailLog.getToMember().getId());
						vo.setToMemberName(getCommonMemberName(webEmailLog.getToMember().getId(), langCode, "2"));
					}
					if(webEmailLog.getSendedTime() != null){
						if (StringUtils.isBlank(dateFormat)) {
							dateFormat = CommonConstants.FORMAT_DATE_TIME;
						}
						vo.setSendedTimeStr(DateUtil.dateToDateString(webEmailLog.getSendedTime(), dateFormat));
					}
					MemberBase creator = memberBaseService.findById(webEmailLog.getCreatorId());
					vo.setCreator(getCommonMemberName(creator.getId(), langCode, "2"));
					CrmProposal proposal = findProposalById(webEmailLog.getRelateId());
					vo.setProposalName(proposal.getProposalName());
					vo.setProposalCreateTime(DateUtil.dateToDateString(proposal.getCreateTime(), dateFormat));
					vo.setToEmailContent(UriEncoder.encode(new String(webEmailLog.getToEmailContent())));
					records.add(vo);
				}
			}
		}
		return records;
	}
}
