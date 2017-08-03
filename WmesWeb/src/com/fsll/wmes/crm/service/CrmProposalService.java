/**
 * 
 */
package com.fsll.wmes.crm.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.crm.vo.ProposalCheckVO;
import com.fsll.wmes.crm.vo.ProposalDataVO;
import com.fsll.wmes.crm.vo.ProposalEmailRecordsVO;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.CrmProposalCheck;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.PortfolioInfoAip;
import com.fsll.wmes.entity.PortfolioInfoProduct;
import com.fsll.wmes.entity.PortfolioInfoProductDetail;
import com.fsll.wmes.entity.ProductInfo;

/**
 * 投资方案信息查询服务接口
 * @author tan
 * @date 2016-8-16
 */
public interface CrmProposalService {

	/**
	 * 获取列表
	 * @param jsonPaging
	 * @param keyword
	 * @param memberAdmin 
	 * @return
	 */
	public JsonPaging findAllProposal(JsonPaging jsonPaging,String keyword, MemberAdmin memberAdmin);
	
	/**
	 * 获取内容信息
	 * @param id
	 * @return
	 */
	public CrmProposal findProposalById(String id);
	
	/**
	 * 获取ifa管理的投资方案
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param customerMemberId
	 * @return
	 */
	public JsonPaging findProposalListForIfa(JsonPaging jsonPaging,String ifaMemberId, String customerMemberId, String langCode);
	
	/**
	 * 获取ifa管理的投资方案(需转换货币)
	 * @author mqzou 2017-02-17
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param customerMemberId
	 * @param currency
	 * @return
	 */
	public JsonPaging findProposalForIfa(JsonPaging jsonPaging,String ifaMemberId, String customerMemberId,String currency);
	
	/**
	 * 删除投资方案（逻辑删除）
	 * @author wwluo
	 * @date 2016-09-18
	 */
	public boolean delProposal(String proposalId);

	/**
	 * 获取ifa的投资方案
	 * @author 
	 * @param MemberBase 当前账号
	 * @param period 期间
	 * @param status 状态
	 * @param keyWord 关键字
	 * @date 2016-09-14
	 */
	public JsonPaging getProposal(JsonPaging jsonPaging, MemberBase loginUser,String period,
			String fromDate,String toDate,String status,String keyWord,String clients,String langCode);
	
	
	
	
	/**
	 * 修改投资方案
	 * @author mqzou
	 * @date 2016-09-29
	 * @param crmProposal
	 * @return
	 */
	public CrmProposal saveCrmProposal(CrmProposal crmProposal);

	/**
	 * 根据方案查找组合
	 * @author wwluo
	 * @date 2016-09-29
	 * @param crmProposal
	 * @return
	 */
	public PortfolioInfo findPortfolioInfoByProposal(CrmProposal proposal);

	/**
	 * 创建方案时保存组合定投信息
	 * @author wwluo
	 * @date 2016-09-29
	 * @param crmProposal
	 * @return
	 */
	public PortfolioInfoAip saveAndUpdatePortfolioInfoAip(
			PortfolioInfoAip portfolioInfoAip);

	/**
	 * 创建方案时保存组合产品信息
	 * @author wwluo
	 * @date 2016-09-29
	 * @param crmProposal
	 * @return
	 */
	public PortfolioInfoProduct saveAndUpdatePortfolioInfoProduct(
			PortfolioInfoProduct portfolioInfoProduct);
	
	/**
	 * 获取投资人的所有proposal
	 * @author mqzou
	 * @date 2016-10-21
	 * @param memberId
	 * @return
	 */
	public List findMyProposal(String memberId, String currencyCode, String langCode, String dateFormat);
	
	/**
	 * proposal 相关所有信息，PDF页面展示
	 * @author wwluo
	 * @date 2016-12-1
	 * @param proposalId CrmProposal 主键
	 * @param langCode 多语言标记
	 * @return ProposalDataVO proposal 相关所有信息VO
	 */
	public ProposalDataVO getProposalDataVO(String proposalId,String langCode);
	
	/**
	 * 获取基金名称
	 * @author wwluo
	 * @date 2016-12-1
	 * @param fundId 
	 * @param langCode 多语言标记
	 * @return String 基金名称
	 */
	public String getFundNameById(String langCode, String fundId);
	
	/**
	 * 获取组合候选产品信息
	 * @author wwluo
	 * @date 2016-12-1
	 * @param portfolio 对应组合 
	 * @param info 主产品
	 * @return List<PortfolioInfoProductDetail> 组合候选产品集合
	 */
	public List<PortfolioInfoProductDetail> getProductDetailByPortfolioProduct(
			PortfolioInfo portfolio, ProductInfo info);
	
	/**
	 * 根据产品获取对应基金
	 * @author wwluo
	 * @date 2016-12-1
	 * @param portfolio 对应产品
	 * @return FundInfo 基金信息
	 */
	public FundInfo getFundInfoByProduct(ProductInfo product);
	
	/**
	 * 根据组合获取产品集合
	 * @author wwluo
	 * @date 2016-12-1
	 * @param portfolio 对应组合
	 * @return List<PortfolioInfoProduct> 产品集合
	 */
	public List<PortfolioInfoProduct> getProductByPortfolio(
			PortfolioInfo portfolio);
	
	/**
	 * 根据组合获取定投信息
	 * @author wwluo
	 * @date 2016-12-1
	 * @param portfolio 对应组合
	 * @return PortfolioInfoAip 定投信息
	 */
	public PortfolioInfoAip getAipByPorfolio(PortfolioInfo portfolio);
	
	/**
	 * 根据方案获取关联的组合
	 * @author wwluo
	 * @date 2016-12-1
	 * @param proposal 对应方案
	 * @return PortfolioInfo 关联的组合
	 */
	public PortfolioInfo getPortfolioByProposal(CrmProposal proposal);
	
	/**
	 * 获取ifa创建的proposal
	 * @author mqzou 2016-12-26
	 * @param jsonPaging
	 * @param strCondition
	 * @param ifaMemberId
	 * @return
	 */
	public JsonPaging findProposal(JsonPaging jsonPaging,String strCondition,String ifaMemberId,String currency,String langCode);

	/**
	 * 根据proposal获取portfolio
	 * @author wwluo
	 * @return
	 */
	public PortfolioInfo findPortfolioByProposalId(String proposalId);

	/**
	 * 保存备选基金
	 * @author wwluo
	 * @return
	 */
	public PortfolioInfoProductDetail saveAndUpdatePortfolioInfoProductDetail(
			PortfolioInfoProductDetail productDetail);

	/**
	 * 获取基金详情信息
	 * @author wwluo
	 * @return
	 */
	public List<PortfolioInfoProductDetail> getProductDetailByPortfolioProduct(
			String portfolioId);

	/**
	 * 获取组合产品
	 * @author wwluo
	 * @return
	 */
	public PortfolioInfoProduct getProduct(String id, String id2);
	
	/**
	 * 获取ifa管理的投资方案
	 * @author michael
	 * @param ifaMemberId
	 * @return list
	 */
	@SuppressWarnings("unchecked")
    public List<CrmProposal> findCrmProposalByIfa(String ifaMemberId);
    
	/**
	 * 修改IFA的客户到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaCrmProposal(String fromMemberId,String toMemberId,MemberBase createBy);

	/**
	 * 获取proposal
	 * @author wwluo
	 * @date 2017-3-1
	 * @param ifaMemberId
	 * @param memberId
	 * @return 
	 */
	public List<CrmProposal> getProposal(String ifaMemberId, String memberId);

	/**
	 * 保存投资方案审核信息
	 * @author wwluo
	 * @date 2017-4-28
	 * @param check 方案审核实体
	 * @return CrmProposalCheck 方案审核实体
	 */
	public CrmProposalCheck saveProposalCheck(CrmProposalCheck check);

	/**
	 * 获取投资方案审核记录
	 * @author wwluo
	 * @date 2017-4-28
	 * @param proposalId 投资方案id
	 * @param langCode 多语言标识
	 * @return List<ProposalCheckVO> 方案审核VO实体集合 
	 */
	public List<ProposalCheckVO> geProposalCheck(String proposalId, String langCode);

	/**
	 * 获取member的投资方案审批记录
	 * @author wwluo
	 * @data 2017-04-27
	 * @param memberId
	 * @param proposalId
	 * @param status 审核状态
	 * @return
	 */
	public List<CrmProposalCheck> ifApprovalForProposal(String memberId, String proposalId,
			String status);

	/**
	 * 获取方案邮件发送记录
	 * @author wwluo
	 * @data 2017-04-27
	 * @param proposalId
	 * @param langCode
	 * @param dateFormat
	 * @return
	 */
	public List<ProposalEmailRecordsVO> getEmailRecords(String proposalId,
			String langCode, String dateFormat);
}
