package com.fsll.wmes.ifafirm.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.crm.vo.ClientSearchVO;
import com.fsll.wmes.crm.vo.ClientsBasicVO;
import com.fsll.wmes.crm.vo.CrmClientForIfaVO;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.ifafirm.vo.TransRecordVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.trade.vo.TransactionRecordFilterVO;

public interface IfaClientService {

	/**
	 * ifafirm-distributor 客户信息分页
	 * @param isDis 
	 * */
	public JsonPaging findExistingCustomerListForIfa(JsonPaging jsonPaging,
			String ifaMemberId, String areaId, String period,
			String clientStatus, String keyword, String character, Boolean isDis);

	/**
	 * ifafirm-distributor 交易记录分页
	 * */
	public JsonPaging getTransactionRecord(MemberSsoVO loginUser,
			JsonPaging jsonPaging, TransactionRecordFilterVO filter);

	/**
	 * 获取IFA Firm的所有客户的概况信息
	 * @author michael
	 * @date 2016-12-15
	 * @param ifaFirmId
	 * @return
	 */
	public ClientsBasicVO findClientsBasicByFirm(String ifaFirmId);
	
	/**
	 * 获取IFA Firm的所有客户的总资产
	 * @author michael
	 * @date 2016-12-15
	 * @param ifaFirmId
	 * @return
	 */
	public double findClientAuMByFirm(String ifaFirmId,String currency);
	
	/**
	 * 获取ifa firm的客户列表
	 * @author michael
	 * @param ifaFirmId
	 * @param keyWord
	 * @param type 客户类型
	 * @return
	 */
	public List<CrmClientForIfaVO> findClientForIfaFirm(String ifaFirmId, String keyWord,String type);
	
	/**
	 * 获取IFA Firm的客户列表
	 * @author michael
	 * @date 2016-12-15
	 * @param jsonPaging
	 * @param clientSearchVO
	 * @return
	 */
	public JsonPaging findClentsByIfaFirm(JsonPaging jsonPaging, ClientSearchVO clientSearchVO);
	
	/**
	 * 获取Distributor的客户列表
	 * @author michael
	 * @param distributorId
	 * @param keyWord
	 * @param type 客户类型
	 * @return
	 */
	public List<CrmClientForIfaVO> findClientForDistributor(String distributorId, String keyWord,String type);

	/**
	 * 交易记录查询
	 * @author wwluo
	 * @param memberAdmin 当前管理员
	 * @param jsonPaging
	 * @param transRecordVO 查询条件
	 * @param langCode
	 * @param currencyCode
	 * @return jsonPaging
	 */
	public JsonPaging getTransRecords(MemberAdmin memberAdmin,
			JsonPaging jsonPaging, TransRecordVO transRecordVO, String langCode, String currencyCode);
	
}
