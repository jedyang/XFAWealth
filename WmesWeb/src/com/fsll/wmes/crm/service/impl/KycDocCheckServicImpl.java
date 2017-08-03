/**
 * 
 */
package com.fsll.wmes.crm.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.UploadUtil;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.wmes.crm.service.KycDocCheckServic;
import com.fsll.wmes.entity.DocList;
import com.fsll.wmes.entity.DocListEn;
import com.fsll.wmes.entity.DocListSc;
import com.fsll.wmes.entity.DocListTc;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorAccountContact;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.InvestorDocCheck;
import com.fsll.wmes.entity.InvestorDocEn;
import com.fsll.wmes.entity.InvestorDocHis;
import com.fsll.wmes.entity.InvestorDocHisEn;
import com.fsll.wmes.entity.InvestorDocHisSc;
import com.fsll.wmes.entity.InvestorDocHisTc;
import com.fsll.wmes.entity.InvestorDocSc;
import com.fsll.wmes.entity.InvestorDocTc;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.ifa.vo.DocHistoryVo;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.investor.vo.KycDocEditVo;
import com.fsll.wmes.member.vo.DocListVO;

/**
 * @author scshi_u330p
 *KYC文档审查service实现
 */
@Service("kycDocCheckServic")
//@Transactional
public class KycDocCheckServicImpl extends BaseService implements KycDocCheckServic{
	
	/**
	 * KYC获取文档审核记录
	 * @author scshi
	 * @date 20160928
	 * @param docId
	 * */
	public List<InvestorDocCheck> findDocCheckList(String docId) {
		StringBuffer strBuf = new StringBuffer("from InvestorDocCheck  t ");
		strBuf.append(" where t.docId=? order by t.checkTime desc ");
		List<InvestorDocCheck> list = this.baseDao.find(strBuf.toString(), new String[] { docId }, false);
		if (null != list && !list.isEmpty()){
			for(int x=0;x<list.size();x++){
				InvestorDocCheck docCheck = list.get(x);
				MemberBase mBase = docCheck.getCheck();
				docCheck.setLoginCode(mBase.getLoginCode());
			}
			return list;
		}
		return null;

	}

	
	/**
	 * 获取文档详细信息
	 * 
	 * @author scshi_u330p
	 * @date 20161009
	 * @param docId
	 *            文档id
	 * @param langCode
	 *            语言设置
	 * */
	public InvestorDoc findDocInfoById(String docId, String langCode) {
		StringBuffer strBuf = new StringBuffer("select t.id,out.docName,t.updateCycle,t.submitDate,t.expireDate,t.checkStatus,t.lastUpdate,t.isValid ");
		strBuf.append("  from InvestorDoc t inner join " + this.getLangString("InvestorDoc", langCode));
		strBuf.append(" out on out.id=t.id ");
		strBuf.append(" where t.id=? order by t.lastUpdate desc ");

		List list = this.baseDao.find(strBuf.toString(), new String[] { docId }, false);
		if (null != list && !list.isEmpty()) {
			Object[] objs = (Object[]) list.get(0);
			InvestorDoc doc = new InvestorDoc();
			doc.setId(objs[0]==null?"":objs[0].toString());
			doc.setDocName(objs[1]==null?"":objs[1].toString());
			doc.setUpdateCycle(objs[2]==null?0:Integer.parseInt(objs[2].toString()));
			doc.setSubmitDate(objs[3]==null?null:DateUtil.StringToDate(objs[3].toString(), "yyyy-MM-dd hh:mm:ss"));
			doc.setExpireDate(objs[4]==null?null:DateUtil.StringToDate(objs[4].toString(), "yyyy-MM-dd hh:mm:ss"));
			doc.setCheckStatus(objs[5]==null?"":objs[5].toString());
			doc.setLastUpdate(objs[6]==null?null:DateUtil.StringToDate(objs[6].toString(), "yyyy-MM-dd hh:mm:ss"));
			String isValid = objs[7]==null?"":objs[7].toString();
			if("0".equals(isValid)){
				doc.setCheckStatus("3");//废除
			}
			
			return doc;
		}
		return null;
	}
	
	/**
	 * KYC文档审批历史
	 * @author scshi_u330p
	 * @date 20161107
	 * */
	public List<DocHistoryVo> findDocHistoryKyc(String docId,String moduleType,String langCode){
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(" select t.id,out.docName,t.submitDate ");
		sb.append(" from InvestorDocHis t inner join " + this.getLangString("InvestorDocHis", langCode));
		sb.append(" out on out.id=t.id where 1=1 ");
		if(null!=docId && !"".equals(docId)){
			sb.append(" and t.docId=? ");
			params.add(docId);
		}
		sb.append(" order by t.submitDate desc ");
		List list = this.baseDao.find(sb.toString(), params.toArray(), false);
		List<DocHistoryVo> voList = new ArrayList();
		if(!list.isEmpty()){
			for(int x=0;x<list.size();x++){
				Object[] objs = (Object[])list.get(x);
				DocHistoryVo vo = new DocHistoryVo();
				String id = objs[0]==null?"":objs[0].toString();
				String submitDate = objs[2]==null?"":objs[2].toString();
				vo.setId(id);
				vo.setSummitDate(submitDate);
				StringBuffer sbAtt = new StringBuffer(" from AccessoryFile a where a.relateId=? and a.moduleType=? order by a.createTime desc ");
				List<AccessoryFile> attList = this.baseDao.find(sbAtt.toString(), new String[]{id,moduleType}, false);
				vo.setDocAtt(attList);
				voList.add(vo);
			}
			return voList;
		}
		return null;
	}
	
	/**
	 * KYC文档修改提交
	 * @author scshi_u330p
	 * @date 20161111 
	 * param vo 封装需要修改的doc
	 * */
	@Override
	public void docDetailEditSave(KycDocEditVo vo, String langCode, MemberBase loginUser, String moduleType) {
		
		String docId = vo.getDocId();
		String deleteIds = vo.getDeleteIds();
		String[] delIds = deleteIds.split(",");
		for(int y=0;y<delIds.length;y++){
			String delId = delIds[y];
			AccessoryFile delFile = (AccessoryFile)this.baseDao.get(AccessoryFile.class,delId);
			if(null==delFile)continue;
			
			String delPath = delFile.getFilePath();
			//删除数据库记录
			this.baseDao.delete(delFile);
			//删除服务器文件
			UploadUtil.removeFileScore(delPath);
		}
		
		String addStr = vo.getAddFileStr();
		String[] fileArry = addStr.split(",");
		
		//if(fileArry.length>0 && !"".equals(fileArry[0])){
			InvestorDoc doc = (InvestorDoc)this.baseDao.get(InvestorDoc.class, docId);
			if(!"2".equals(doc.getCheckStatus()))
				//首次上传创建临时历史记录，关联附件的moduleType增加temp标示
				this.createTempHistoryAttr(docId,moduleType);
			
			//添加checklist记录
			InvestorDocCheck saveObj = new InvestorDocCheck();
			saveObj.setId(null);
			saveObj.setCheck(loginUser);
			saveObj.setCheckTime(new Date());
			saveObj.setCheckResult(null);
			saveObj.setCheckRole("I");
			saveObj.setCheckStatus("0");
			saveObj.setDocId(docId);
			this.baseDao.saveOrUpdate(saveObj);
			//更新文档审批状态/上传时间/过期时间
			doc.setCheckStatus("0");
			doc.setSubmitDate(new Date());
			
			//过期时间时间更新
			Calendar calendar=Calendar.getInstance();  
			calendar.setTime(new Date());
			int updateCycle = doc.getUpdateCycle();
			calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+updateCycle);
			doc.setExpireDate(calendar.getTime());
			this.baseDao.saveOrUpdate(doc);
		//}
		
		for(int x=0;x<fileArry.length;x++){
			if("".equals(fileArry[x]))continue;
			
			String[] fileStr = fileArry[x].split(":");
			String fileName = fileStr[0];
			String filePath = fileStr[1];
			
			
			//根据路径检查附件数据是否存在，如果记录已存在，不入库，跳入下一次循环
			//boolean flag = this.checkFileExist(filePath);
			//if(flag){
				AccessoryFile file = new AccessoryFile();
				file.setFileName(fileName);
				file.setFilePath( filePath );
				file.setFileType( fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()) );
				file.setModuleType(moduleType);//investor_doc
				file.setLangCode(langCode);
				file.setRelateId( docId );
				file.setCreateBy( loginUser );
				file.setCreateTime(new Date());
				this.baseDao.saveOrUpdate(file);
			//}
		}
		
	}
	
	/**
	 * 文档审批记录保存
	 * @author scshi_u330p
	 * @date 20161115
	 * @param loginUser 审批人
	 * @param docCheck 审批详细信息
	 * */
	public void kycDocCheckResultSave(MemberBase loginUser,InvestorDocCheck docCheck,String moduleType){
		//添加checklist
		InvestorDocCheck saveObj = new InvestorDocCheck();
		saveObj.setId(null);
		saveObj.setCheck(loginUser);
		saveObj.setCheckTime(new Date());
		saveObj.setCheckResult(docCheck.getCheckResult());
		saveObj.setCheckRole("D");
		saveObj.setCheckStatus(docCheck.getCheckStatus());
		saveObj.setDocId(docCheck.getDocId());
		this.baseDao.saveOrUpdate(saveObj);
		
		//修改文档状态
		StringBuffer sb = new StringBuffer("update InvestorDoc t set t.checkStatus=?,t.lastUpdate=? where t.id=?  ");
		List params = new ArrayList();
		params.add(docCheck.getCheckStatus());
		params.add(new Date());
		params.add(docCheck.getDocId());
		this.baseDao.updateHql(sb.toString(), params.toArray());
		
		//审批通过后生成历史
		if("1".equals(docCheck.getCheckStatus()))
			this.createCheckHistory(docCheck.getDocId(), moduleType);
		
	}
	
	//根据路径检查附件数据是否存在
	private boolean checkFileExist(String filePath){
		StringBuffer sb = new StringBuffer("from AccessoryFile t where t.filePath=? ");
		List list = this.baseDao.find(sb.toString(), new String[]{filePath}, false);
		if(list.isEmpty())return true;
		return false;
	}
	
	//生成历史图片记录
	private void createCheckHistory(String docId,String moduleType){
		// 查询已上传附件
		String fileHql = " from AccessoryFile t where t.relateId=? and t.moduleType=? order by t.createTime desc ";
		List fileParams = new ArrayList();
		fileParams.add(docId);
		fileParams.add(moduleType+"_temp");
		List<AccessoryFile> fileList = this.baseDao.find(fileHql, fileParams.toArray(), false);
		
		if(!fileList.isEmpty()){
			InvestorDoc doc = (InvestorDoc)this.baseDao.get(InvestorDoc.class, docId);
			InvestorDocHis idh = new InvestorDocHis();
			idh.setId(null);
			idh.setDocId(docId);
			//idh.setCheckResult(doc.getc)
			idh.setCheckStatus(doc.getCheckStatus());
			idh.setContact(doc.getContact());
			idh.setCreateTime(new Date());
			idh.setDistributor(doc.getDistributor());
			idh.setDocTemplate(doc.getDocTemplate());
			idh.setExpireDate(doc.getExpireDate());
			idh.setIfafirm(doc.getIfafirm());
			idh.setIsNecessary(doc.getIsNecessary());
			idh.setIsValid(doc.getIsValid());
			idh.setLastUpdate(doc.getLastUpdate());
			idh.setMember(doc.getMember());
			idh.setStatus(doc.getStatus());
			idh.setSubmitDate(doc.getSubmitDate());
			idh.setUpdateCycle(doc.getUpdateCycle());
			this.baseDao.saveOrUpdate(idh);
			
			//多语言保存
			InvestorDocHisEn idhEn = new InvestorDocHisEn();
			InvestorDocEn docEn = (InvestorDocEn)this.baseDao.get(InvestorDocEn.class, docId);
			
			InvestorDocHisSc idhSc = new InvestorDocHisSc();
			InvestorDocSc docSc = (InvestorDocSc)this.baseDao.get(InvestorDocSc.class, docId);
			
			InvestorDocHisTc idhTc = new InvestorDocHisTc();
			InvestorDocTc docTc = (InvestorDocTc)this.baseDao.get(InvestorDocTc.class, docId);
			
			idhEn.setId(idh.getId());
			idhEn.setDocName(docEn.getDocName());
			this.baseDao.create(idhEn);
			
			idhSc.setId(idh.getId());
			idhSc.setDocName(docSc.getDocName());
			this.baseDao.create(idhSc);
			
			idhTc.setId(idh.getId());
			idhTc.setDocName(docTc.getDocName());
			this.baseDao.create(idhTc);
			
			for(int x=0;x<fileList.size();x++){
				AccessoryFile fileObj = fileList.get(x);
				//修改关联id为历史记录的id，去除temp标示
				fileObj.setRelateId(idh.getId());
				fileObj.setModuleType(moduleType);
				this.baseDao.saveOrUpdate(fileObj);
			}
		}
	}
	
	/**
	 * distrubutor/ifaFirm 修改文档审批日期
	 * @author scshi_u330p
	 * @date 20161212
	 * */
	public void expireDateChange(String id, String newExpireDate) throws Exception{
		InvestorDoc doc = (InvestorDoc)this.baseDao.get(InvestorDoc.class, id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		doc.setExpireDate(sdf.parse(newExpireDate+" 23:23:59"));
		doc.setLastUpdate(new Date());
		this.baseDao.saveOrUpdate(doc);
	}
	
	/**
	 * 获取文档检查文档
	 * @author scshi_u330p
	 * @date20161222
	 * */
	public List<DocListVO> findContactDocListForKyc(String contactId,String investorAccoutId,String langCode) {
		String hql = " select t.id,out.docName,t.docTemplate.id,t.isNecessary,t.updateCycle,t.expireDate,t.submitDate,";
		hql += " t.checkStatus ,t.isValid";
		hql += " from InvestorDoc t inner join " + this.getLangString("InvestorDoc", langCode);
		hql += " out on t.id=out.id ";
		hql += " where t.contact.id=? and t.account.id=? ";

		List params = new ArrayList();
		params.add(contactId);
		params.add(investorAccoutId);
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if (voList.isEmpty())
			return null;

		List<DocListVO> list = new ArrayList<DocListVO>();
		for (int z = 0; z < voList.size(); z++) {
			Object[] objs = (Object[]) voList.get(z);
			DocListVO vo = new DocListVO();
			vo.setId(objs[0] == null ? "" : objs[0].toString());
			vo.setDocName(objs[1] == null ? "" : objs[1].toString());
			vo.setTemplateId(objs[2] == null ? "" : objs[2].toString());
			vo.setIsNecessary(objs[3] == null ? "" : objs[3].toString());
			vo.setUpdateCycle(objs[4] == null ? 0 : Integer.parseInt(objs[4].toString()));
			vo.setEffectDate(objs[5] == null ? new Date() : DateUtil.StringToDate(objs[5].toString(), "yyyy-MM-dd hh:mm:ss"));
			vo.setCreateTime(objs[6] == null ? new Date() : DateUtil.StringToDate(objs[6].toString(), "yyyy-MM-dd hh:mm:ss"));
			vo.setCheckStatus(objs[7] == null ? "" : objs[7].toString());
			String isValid = objs[8]==null?"1":objs[8].toString();
			if("0".equals(isValid)){
				vo.setCheckStatus("3");//文档失效
			}
			String expireStatus = vo.getEffectDate().after(new Date())?"0":"1";
			vo.setExpireStatus(expireStatus);
			Calendar betweenDay = Calendar.getInstance();
			betweenDay.setTime(vo.getEffectDate());
			int dayEff = betweenDay.get(Calendar.DAY_OF_YEAR);
			betweenDay.setTime(new Date());
			int dayNow = betweenDay.get(Calendar.DAY_OF_YEAR);
			vo.setEffectDateBetween(dayNow-dayEff);
			
			
			// 查询已上传附件
			String fileHql = " from AccessoryFile t where t.relateId=? and t.moduleType=? order by t.createTime desc ";
			List fileParams = new ArrayList();
			fileParams.add(objs[0].toString());
			fileParams.add("investor_doc");
			List<AccessoryFile> fileList = this.baseDao.find(fileHql, fileParams.toArray(), false);
			vo.setFileList(fileList);
			list.add(vo);
		}
		return list;
	}
	
	/**
	 * 创建临时历史记录
	 * @author scshi_u330p
	 * @date 20170103
	 * */
	public void createTempHistoryAttr(String docId,String moduleType){
		List params = new ArrayList();
		StringBuffer sbHql = new StringBuffer(" update AccessoryFile set moduleType=? where relateId=? and moduleType=? ");
		params.add(moduleType+"_temp");
		params.add(docId);
		params.add(moduleType);
		this.baseDao.updateHql(sbHql.toString(), params.toArray());
	}
	
	/**
	 * 判断文档是否需要更新
	 * @author scshi_u330p
	 * @date 20170112
	 * **/
	public void checkTemplateUpdate(InvestorAccountVO vo,String contactId,String clientType,String accountId){
		
		String memberId = vo.getMemberId();
		String distributorId = vo.getDistributorId();
		
		StringBuffer docSql = new StringBuffer("select t,dt from DocList t left join DocTemplate dt on t.template.id=dt.id ");
		docSql.append("where dt.distributor.id=? and dt.clientType=? ");
		docSql.append("and dt.isDefault=1 and dt.isValid=1 and t.isValid='1' and dt.status='using' ");
		docSql.append(" and t.id not in (select d.docListId from InvestorDoc d where d.isValid='1' ");
		List params = new ArrayList();
		params.add(distributorId);
		params.add(clientType);
		if(StringUtils.isNotBlank(accountId)){
			docSql.append(" and d.account.id=? ) ");
			params.add(accountId);
		}else{//只作为备用，doclist与联系人脱离关系，使用账户id关联
			docSql.append(" and d.contact.id=? )");
			params.add(contactId);
		}
		
		MemberBase member = new MemberBase();
		member.setId(memberId);

		MemberDistributor distributor = new MemberDistributor();
		distributor.setId(distributorId);

		InvestorAccountContact contact = new InvestorAccountContact();
		contact.setId(contactId);
		
		InvestorAccount account = new InvestorAccount();
		account.setId(accountId);
		
		//获取需要新增的模板
		List docList = this.baseDao.find(docSql.toString(), params.toArray(), false);
		if(!docList.isEmpty()){
			for(int x=0;x<docList.size();x++){
				Object[] objs = (Object[]) docList.get(x);
				DocList docModel = (DocList)objs[0];
				
				String docModelId = docModel.getId();
				InvestorDoc investDoc = new InvestorDoc();

				investDoc.setId(null);
				investDoc.setCreateTime(new Date());
				investDoc.setDistributor(distributor);
				investDoc.setMember(member);
				investDoc.setDocTemplate(docModel.getTemplate());
//				if(StringUtils.isNotBlank(contactId)){
//					investDoc.setContact(contact);
//				}
				investDoc.setIsNecessary(docModel.getIsNecessary());
				investDoc.setUpdateCycle(docModel.getUpdateCycle());
				investDoc.setCreateTime(new Date());
				investDoc.setLastUpdate(new Date());
				investDoc.setIsValid("1");
				investDoc.setAccount(account);
				investDoc.setDocListId(docModel.getId());
				this.baseDao.saveOrUpdate(investDoc);

				// 简体中文版checklist保存
				InvestorDocSc investDocSc = new InvestorDocSc();
				DocListSc docModelSc = (DocListSc) this.baseDao.get(DocListSc.class, docModelId);
				investDocSc.setId(investDoc.getId());
				investDocSc.setDocName(docModelSc.getDocName());
				this.baseDao.create(investDocSc);

				// 繁体
				InvestorDocTc investDocTc = new InvestorDocTc();
				DocListTc docModelTc = (DocListTc) this.baseDao.get(DocListTc.class, docModelId);
				investDocTc.setId(investDoc.getId());
				investDocTc.setDocName(docModelTc.getDocName());
				this.baseDao.create(investDocTc);

				// 英文
				InvestorDocEn investDocEn = new InvestorDocEn();
				DocListEn docModelEn = (DocListEn) this.baseDao.get(DocListEn.class, docModelId);
				investDocEn.setId(investDoc.getId());
				investDocEn.setDocName(docModelEn.getDocName());
				this.baseDao.create(investDocEn);
			}
		}
		
		//逻辑删除文档，文档设置为不可用状态
		List delParams = new ArrayList();
		StringBuffer delSql = new StringBuffer("from InvestorDoc t where t.docListId not in( ");
		delSql.append(" select l.id from DocList l left join DocTemplate dt on l.template.id=dt.id ");
		delSql.append(" where dt.distributor.id=? and dt.clientType=? ");
		delSql.append(" and dt.isDefault=1 and dt.isValid=1 and l.isValid='1' ) and t.isValid='1' ");
		delParams.add(distributorId);
		delParams.add(clientType);
		
		if(StringUtils.isNotBlank(accountId)){
			delSql.append(" and t.account.id=?  ");
			delParams.add(accountId);
		}else{//只作为备用，doclist与联系人脱离关系，使用账户id关联
			delSql.append(" and t.contact.id=? ");
			delParams.add(contactId);
		}
		
		List<InvestorDoc> delList = this.baseDao.find(delSql.toString(), delParams.toArray(), false);
		if(!delList.isEmpty()){
			for(int y=0;y<delList.size();y++){
				InvestorDoc delDoc = delList.get(y);
				delDoc.setIsValid("0");
				this.baseDao.saveOrUpdate(delDoc);
			}
		}
		
		//end
	}
	
	/**
	 * 获取文档审批人列表
	 * @author scshi_u330p
	 * @date 20170301
	 * @author scshi_u330p
	 * */
	public List<MemberBase> findComfirmDistributorAccount(MemberDistributor dis){
		StringBuffer sb = new StringBuffer("from MemberAdmin m where m.type='2' and m.distributor.id=? ");
		sb.append(" and m.member.id in (select role.member.id from MemberConsoleCheckRole role where role.roleType='KR') ");
		List<MemberAdmin> list = this.baseDao.find(sb.toString(), new String[]{dis.getId()}, false);
		if(list.isEmpty())return null;
		List<MemberBase> disAccountList = new ArrayList();
		for(MemberAdmin admin:list){
			disAccountList.add(admin.getMember());
		}
		return disAccountList;
	}
	
	/**审批完成以后设置待办状态为已读
	 * @author scshi_u330p
	 * @param docId
	 * @param webReadModuleKycSummit
	 * @param webReadMessageTypeNormal
	 */
	public void webReadedSave(String docId, String webReadModuleKycSummit,String webReadMessageTypeNormal){
		StringBuffer updateSql = new StringBuffer("update WebReadToDo t set t.isRead='1' ");
		updateSql.append(" where t.relateId=? and t.moduleType=? and t.type=? ");
		this.baseDao.updateHql(updateSql.toString(), new String[]{docId,webReadModuleKycSummit,webReadMessageTypeNormal});
	}
	
}
