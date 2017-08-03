/**
 * 
 */
package com.fsll.wmes.crm.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.InvestorDocCheck;
import com.fsll.wmes.entity.InvestorDocHis;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.ifa.vo.DocHistoryVo;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.investor.vo.KycDocEditVo;
import com.fsll.wmes.member.vo.DocListVO;

/**
 * @author scshi_u330p
 *KYC文档审查service
 */
public interface KycDocCheckServic {
	/**
	 * 获取文档详细信息
	 * @author scshi_u330p
	 * @date 20161009
	 * @param docId 文档id
	 * @param langCode 语言设置
	 * */
	public InvestorDoc findDocInfoById(String docId, String langCode);
	
	/**
	 * KYC文档修改提交
	 * @author scshi_u330p
	 * @param moduleType 
	 * @param loginUser 
	 * @param langCode 
	 * @date 20161111 
	 * param vo 封装需要修改的doc
	 * */
	public void docDetailEditSave(KycDocEditVo vo, String langCode, MemberBase loginUser, String moduleType);
	
	/**
	 * KYC文档审批历史
	 * @author scshi_u330p
	 * @date 20161107
	 * */
	public List<DocHistoryVo> findDocHistoryKyc(String docId, String moduleType,String langCode);
	
	
	/**
	 * 获取文档审核记录
	 * @author scshi
	 * @date 20160928
	 * @param docId
	 * */
	public List<InvestorDocCheck> findDocCheckList(String docId);

	/**
	 * 文档审批记录保存
	 * @author scshi_u330p
	 * @date 20161115
	 * @param loginUser 审批人
	 * @param docCheck 审批详细信息
	 * @param moduleType 历史类型
	 * */
	public void kycDocCheckResultSave(MemberBase loginUser,InvestorDocCheck docCheck, String moduleType);
	
	/**
	 * distrubutor/ifaFirm 修改文档审批日期
	 * @author scshi_u330p
	 * @date 20161212
	 * */
	public void expireDateChange(String id, String newExpireDate) throws Exception;

	/**
	 * 判断文档是否有更新
	 * @author scshi_u330p 
	 * @date 20170113
	 * @param vo 获取 memberId，distrubuteId
	 * @param contactId 关联联系人id，导入账户该参数为空
	 * @param clientType 适用客户    Individual
	 * */
	public void checkTemplateUpdate(InvestorAccountVO vo,String contactId,String clientType,String accountId);
	
	/**
	 * 获取文档审批人列表
	 * @author scshi_u330p
	 * @date 20170301
	 * @author scshi_u330p
	 * */
	public List<MemberBase> findComfirmDistributorAccount(MemberDistributor dis);
	/**审批完成以后设置待办状态为已读
	 * @author scshi_u330p
	 * @param docId
	 * @param webReadModuleKycSummit
	 * @param webReadMessageTypeNormal
	 */
	public void webReadedSave(String docId, String webReadModuleKycSummit,String webReadMessageTypeNormal);
}
