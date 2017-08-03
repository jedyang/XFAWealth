package com.fsll.wmes.ifa.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.crm.vo.ProposalVO;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.ifa.vo.AutoCompleteVO;
import com.fsll.wmes.ifa.vo.IfaDistributorVO;
import com.fsll.wmes.ifa.vo.IfaProposalVO;
import com.fsll.wmes.ifa.vo.StrategyInfoVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.portfolio.vo.CriteriaVO;

/***
 * 业务接口类：ifa信息管理接口
 * @author wwluo
 * @date 2016-08-15
 */
public interface IfaManageService {
	
	/**
    * 根据id查询ifa
    * @author qgfeng
    * @date 2016-12-14
    */
	public MemberIfa findIfaById(String ifaId);
	
	/**
	 * 更新或保存ifa
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	public MemberIfa saveOrUpdateIFA(MemberIfa ifa);
	
	  /**
     * 查询条件自动填充IFA
     * @author qgfeng
     * @param keyWord
     * @return
     */
    public List<AutoCompleteVO> findAutoIfa(String keyWord,String ifaFirmId);
	
	/**
	 * 获取ifafirm下所有的ifa
	 * @author qgfeng
	 * @date 2016-12-13
	 */
    public JsonPaging findIfaByIfaFirm(JsonPaging jsonPaging,IfaVO ifaVo,String langCode);

	/***
	 * 查询ifa关联的"团队"分页显示
	 * @author wwluo 
	 * 			update by qgfeng
	 * 			update by michael
	 * @date 2016-08-15
	 * @param ifaId
	 * @param keyword
	 * @return
	 */
	public JsonPaging findTeamByIfaId(JsonPaging jsonPaging, String ifaId,String keyword);
	
	/**
	 * 查询ifa所属ifafirm下的"团队"
	 * @author wwluo
	 * @date 2016-08-16 
	 */
	public JsonPaging findTeamByIfaFirm(JsonPaging jsonPaging, String ifaFirmId);
	/**
	 * ifa关联team
	 * @author wwluo
	 * @date 2016-08-16 
	 */
	public boolean saveIfafirmTeamIfa(String ifaFirmId, String teamId,String ifaId);
	/**
	 * ifa取消关联team
	 * @author wwluo
	 * @param ifaId 
	 * @date 2016-08-16 
	 */
	public boolean removeIfafirmTeamIfa(String teamId, String ifaId);

	//------ end Team --------
	
   /**
   * 查询策略列表
   * @author qgfeng
   * @date 2016-12-20
   */
    public JsonPaging findStrategyList(JsonPaging jsonPaging,StrategyInfoVO infoVO,String langCode);
    
    /**
     * 查询策略的详细信息
     * @author mqzou
     * @date 2016-08-19
     * @param id
     * @return
     */
    public StrategyInfo findStragyById(String id);
    
    /**
     * 查询IFA创建的投资方案列表
     * @author mqzou modify qgfeng
     * @date 2016-08-25
     */
    public JsonPaging findProposalList(JsonPaging jsonPaging,ProposalVO proposalVO);
    
    /**
     *  查询IFA创建的投资组合列表
     * @author mqzou
     * @date 2016-08-25
     * @param jsonPaging
     * @param portfolioInfo
     * @param langCode
     * @return
     */
    public JsonPaging findPofolioList(JsonPaging jsonPaging, CriteriaVO criteria,String langCode);
    /**
     * 观点查询
     * @author qgfeng
     * @date 2016-12-26
     * @param jsonPaging
     * @param infoVo
     * @param langCode
     * @return
     */
    public JsonPaging findInsightList(JsonPaging jsonPaging,CriteriaVO criter,String langCode);
    
    /**
     * 获取ifafirm下的所有ifaId
     * @author mqzou
     * @date 2016-08-25
     * @param ifafirmId
     * @return
     */
    public List getIfaInIfafirm(String ifafirmId);
    
    /**
	 * IFA审批 （修改状态）
	 * @author qgfeng
	 * @date 2016-12-15
	 */
	public boolean saveApprovalStatus(IfaVO ifaVO);
	
	/**
     * 查询ae账户（IfaDistributor）
     * @author qgfeng
     * @date 2017-1-18
     */
    public JsonPaging findIfaDisList(JsonPaging jsonPaging,IfaDistributorVO vo);
	
    /**
     * 根据ifaID、frimID查找MemberIfaIfafirm
     * @author qgfeng
     * @date 2016-12-15
     */
    public MemberIfaIfafirm getIfaIfaFirmByIFrimId(String ifaId,String firmId);
    
	 /**
     * 根据ifaID、frimID查找MemberIfaIfafirm
     * @author michael
     * @date 2017-3-28
     * @param ifaId
     * @param checkStatus  
     */
	public MemberIfaIfafirm findIfaIfaFirmByIfaId(String ifaId, String checkStatus);
	
    /**
     * 保存IFA-IFAFirm关联关系
     * @author qgfeng
     * @date 2016-12-16
     * @param releInfo
     * @return
     */
    public MemberIfaIfafirm saveOrUpdateIfaIfafirm(MemberIfaIfafirm releInfo);
    
    /**
	 * 获取IFA Team列表
	 * @author 林文伟
	  * @param jsonPaging 分页参数
	 * @param filters 过滤条件
	 * @return
	 */
	public JsonPaging loadIFAMyTeamList(JsonPaging jsonPaging,String ifaId,String keyWords);
    
    /**
     * 获取ifa和ifafirm的关系
     * @author mqzou 2016-12-30
     * @param ifaId
     * @return
     */
    public MemberIfaIfafirm getIfaAndIfafirm(String ifaId);
    
    /**
     * 查找IfaDistributor（AE账户）
     * @param id
     * @return
     */
	public IfaDistributor getIfaDisById(String id) ;

	/**
	 * 保存IfaDistributor（AE账户）
	 * @param ifaDistributor
	 * @return
	 */
	public IfaDistributor saveIfaDis(IfaDistributor ifaDistributor) ;
	
	/**
	 * 删除IfaDistributor（AE账户）
	 * @param id
	 * @return
	 */
	public boolean deleteIfaDis(String id);
	
	/***
	 * 查询"团队"的组长
	 * @author michael
	 * @date 2017-03-01
	 * @param teamId
	 * @param ifaId
	 * @return
	 */
	public List findTeamSV(String teamId);

	/***
	 * 校验targetMemberId是否memberId其supervisor
	 * @author wwluo
	 * @date 2017-03-14
	 * @param targetMemberId
	 * @param memberId
	 * @return
	 */
	public boolean varifySupervisor(String targetMemberId, String memberId);
	
	 /**
     * 根据ifaID 更新MemberIfaIfafirm记录为无效状态
     * @author michael
     * @date 2016-12-15
     */
	public boolean updateIfaIfaFirmToInvalid(String ifaId);
	
	/**
	 * 获取ifa推荐的策略列表
	 * @author mqzou  2017-06-06
	 * @param jsonPaging
	 * @param keyword
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public JsonPaging findIfaFirmRecommendStrategy(JsonPaging jsonPaging,String ifafirmId,String keyword,String beginDate,String endDate,String langCode);
	/**
	 * 获取ifa推荐的组合列表
	 * @author mqzou  2017-06-06
	 * @param jsonPaging
	 * @param ifafirmId
	 * @param keyword
	 * @param riskLevel
	 * @param totalReturn
	 * @param beginDate
	 * @param endDate
	 * @param langCode
	 * @return
	 */
	public JsonPaging findIfaFirmRecommendPortfolio(JsonPaging jsonPaging,String ifafirmId,String keyword,String riskLevel,String totalReturn,String beginDate,String endDate,String langCode);

	 /**
     * IFA创建的投资方案列表
     * @author wwluo
     * @date 2016-08-25
     * @param memberAdmin 当前管理员
     * @param jsonPaging
     * @param proposalVO 
     * @param langCode
     * @return JsonPaging
     */
	public JsonPaging getProposals(MemberAdmin memberAdmin,
			JsonPaging jsonPaging, IfaProposalVO ifaProposalVO, String langCode);
}
