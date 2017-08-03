package com.fsll.wmes.crm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.crm.service.DocTemplateManageService;
import com.fsll.wmes.crm.vo.DocInfoVo;
import com.fsll.wmes.crm.vo.DocListVo;
import com.fsll.wmes.crm.vo.DocTemplateVo;
import com.fsll.wmes.entity.DocList;
import com.fsll.wmes.entity.DocListEn;
import com.fsll.wmes.entity.DocListSc;
import com.fsll.wmes.entity.DocListTc;
import com.fsll.wmes.entity.DocTemplate;
import com.fsll.wmes.entity.DocTemplateEn;
import com.fsll.wmes.entity.DocTemplateSc;
import com.fsll.wmes.entity.DocTemplateTc;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;


@Service("docTemplateManageService")
//@Transactional
public class DocTemplateManageServiceImpl extends BaseService implements DocTemplateManageService{
	/**
	 * 文档审查模板列表数据
	 * @author scshi_u330p
	 * @date 20161215
	 * */
	public JsonPaging findPageAll(JsonPaging jsonPaging, MemberAdmin admin,
			String language, String docTemplateTitle, String docTemplateStatus){
		
		MemberDistributor distributor = admin.getDistributor();
		if(null==distributor)return jsonPaging;
		
		List params = new ArrayList();
		StringBuffer sbf = new StringBuffer("select out.title,t.code,t.isDefault,t.langCode,t.clientType,t.createTime,t.status,t.id ");
		sbf.append(" from DocTemplate t ");
		sbf.append(" inner join "+this.getLangString("DocTemplate", language));
		sbf.append(" out on t.id=out.id ");
		sbf.append(" where 1=1 ");
		
		sbf.append(" and t.distributor.id=? ");
		params.add(distributor.getId());
		
		if(StringUtils.isNoneBlank(docTemplateTitle)){
			sbf.append(" and out.title like ? ");
			params.add("%"+docTemplateTitle+"%");
		}
		
		if(StringUtils.isNotBlank(docTemplateStatus)){
			sbf.append(" and t.status=? ");
			params.add(docTemplateStatus);
		}
		
		sbf.append(" order by t.createTime desc ");
		jsonPaging = this.baseDao.selectJsonPaging(sbf.toString(), params.toArray(), jsonPaging, false);
		
		List list = jsonPaging.getList();
		List<DocTemplate> voList = new ArrayList();
		for(int x=0;x<list.size();x++){
			DocTemplate doc = new DocTemplate();
			Object[] objs = (Object[])list.get(x);
			doc.setTitle(objs[0]==null?"":objs[0].toString());
			doc.setCode(objs[1]==null?"":objs[1].toString());
			String isDefault = objs[2]==null?"":objs[2].toString();
			if(StringUtils.isNotBlank(isDefault)){
				if(isDefault.equals("true"))
					doc.setIsDefault(true);
				else 
					doc.setIsDefault(false);
			}
			doc.setLangCode(objs[3]==null?"":objs[3].toString());
			doc.setClientType(objs[4]==null?"":objs[4].toString());
			String createTime = objs[5]==null?"":objs[5].toString();
			if(StringUtils.isNoneBlank(createTime)){
				doc.setCreateTime(DateUtil.StringToDate(createTime, "yyyy-MM-dd HH:mm:ss"));
			}
			doc.setStatus(objs[6]==null?"":objs[6].toString());
			doc.setId(objs[7]==null?"":objs[7].toString());
			voList.add(doc);
		}
		jsonPaging.setList(voList);
		return jsonPaging;	
	}
	
	/**
	 * 文档模板新增/查看/编辑表单
	 * @author scshi_u330p
	 * @date 20161216
	 * */
	public DocTemplateVo loadeDocTemplateDetail(String id, String language){
		DocTemplateVo vo = new DocTemplateVo();
		if(StringUtils.isNoneBlank(id)){
			DocTemplate docTemp = (DocTemplate)this.baseDao.get(DocTemplate.class,id);
			DocTemplateEn docTempEn = (DocTemplateEn)this.baseDao.get(DocTemplateEn.class, id);
			DocTemplateTc docTempTc = (DocTemplateTc)this.baseDao.get(DocTemplateTc.class, id);
			DocTemplateSc docTempSc = (DocTemplateSc)this.baseDao.get(DocTemplateSc.class, id);
			
			vo.setTemp(docTemp);
			vo.setTempEn(docTempEn);
			vo.setTempSc(docTempSc);
			vo.setTempTc(docTempTc);
		}
		return vo;
	}
	
	/**
	 * 验证模板编号是否被使用
	 * @author scshi_u330p
	 * @date 20161219
	 * 1--被使用，0--未被使用
	 * */
	@Override
	public String checkCodeUnique(String code,String tempId) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("from DocTemplate t where t.code=? ");
		params.add(code);
		
		if(StringUtils.isNotBlank(tempId)){
			sb.append(" and t.id<> ? ");
			params.add(tempId);
		}
		
		List<DocTemplate> list = this.baseDao.find(sb.toString(), params.toArray(), false);
		if(list.isEmpty())return "0";
		
		return "1";
	}
	
	/**
	 * 文档模板信息保存
	 * @author scshi_u330p
	 * @date 200161219
	 * */
	public DocTemplate saveDocTempInfo(DocTemplateVo vo, MemberAdmin admin){
		DocTemplate tempObj =  vo.getTemp();
		if(StringUtils.isBlank(tempObj.getId())){
			tempObj.setId(null);
			tempObj.setCreateBy(admin.getMember());
			tempObj.setCreateTime(new Date());
			tempObj.setDistributor(admin.getDistributor());
			tempObj.setIsValid(1);
		}
		
		//如果当前模板设置为启用且默认，则该代理商同类型启用模板默认值设置为否
		if("using".equals(tempObj.getStatus()) && tempObj.getIsDefault() ){
			List tempListParams = new ArrayList();
			StringBuffer tempListHql = new StringBuffer("update DocTemplate t set t.isDefault=false where t.clientType=? and t.isDefault=? and t.status=? and t.distributor.id=?");
			tempListParams.add(tempObj.getClientType());
			tempListParams.add(tempObj.getIsDefault());
			tempListParams.add(tempObj.getStatus());
			tempListParams.add(admin.getDistributor().getId());
			this.baseDao.updateHql(tempListHql.toString(), tempListParams.toArray());
		}
		
		this.baseDao.saveOrUpdate(tempObj);
		
		
		DocTemplateEn tempEn = (DocTemplateEn)this.baseDao.get(DocTemplateEn.class,tempObj.getId());
		if(null==tempEn){
			DocTemplateEn obj = new DocTemplateEn();
			obj.setId(tempObj.getId());
			obj.setTitle(vo.getTempEn().getTitle());
			this.baseDao.create(obj);
		}else{
			tempEn.setTitle(vo.getTempEn().getTitle());
			this.baseDao.saveOrUpdate(tempEn);
		}
		
		
		
		DocTemplateSc tempSc = (DocTemplateSc)this.baseDao.get(DocTemplateSc.class, tempObj.getId());
		if(null==tempSc){
			DocTemplateSc objSc = new DocTemplateSc();
			objSc.setId(tempObj.getId());
			objSc.setTitle(vo.getTempSc().getTitle());
			this.baseDao.create(objSc);
		}else{
			tempSc.setTitle(vo.getTempSc().getTitle());
			this.baseDao.saveOrUpdate(tempSc);
		}
		
		DocTemplateTc tempTc = (DocTemplateTc)this.baseDao.get(DocTemplateTc.class, tempObj.getId());
		if(null==tempTc){
			DocTemplateTc objTc = new DocTemplateTc();
			objTc.setId(tempObj.getId());
			objTc.setTitle(vo.getTempTc().getTitle());
			this.baseDao.create(objTc);
		}else{
			tempTc.setTitle(vo.getTempTc().getTitle());
			this.baseDao.saveOrUpdate(tempTc);
		}
		return tempObj;
	}
	
	/**
	 * 文档列表json
	 * @author scshi_u330p
	 * @date20161216
	 * */
	public JsonPaging loadDocListPageJson(String tempId, String keyword,String language,JsonPaging jsonPaging){
		StringBuffer sb = new StringBuffer(" select out.docName,t.updateCycle,t.isNecessary,t.isValid,t.effectDate,t.createTime,t.id ");
		sb.append(" from DocList t");
		sb.append(" inner join "+this.getLangString("DocList", language));
		sb.append(" out on t.id=out.id where 1=1 ");
		List params = new ArrayList();
		if(null!=tempId){
			sb.append(" and t.template.id=? ");
			params.add(tempId);
		}else{
			sb.append(" and t.template.id is null ");
		}
		
		if(StringUtils.isNotBlank(keyword)){
			sb.append(" and out.docName like ?");
			params.add("%"+keyword+"%");
		}
		sb.append(" order by t.createTime desc ");
		jsonPaging = this.baseDao.selectJsonPaging(sb.toString(), params.toArray(), jsonPaging, false);
		List<DocListVo> voList = new ArrayList();
		List list = jsonPaging.getList();
		if(list.isEmpty())return jsonPaging;
		for(int y=0;y<list.size();y++){
			Object[] objs = (Object[])list.get(y);
			DocListVo vo = new DocListVo();
			vo.setDocName(objs[0]==null?"":objs[0].toString());
			vo.setUpdateCycle(objs[1]==null?0:Integer.parseInt(objs[1].toString()));
			vo.setIsNecessary(objs[2]==null?"":objs[2].toString());
			vo.setIsValid(objs[3]==null?"":objs[3].toString());
			vo.setEffectDate(objs[4]==null?null:DateUtil.StringToDate(objs[4].toString(), "yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(objs[5]==null?null:DateUtil.StringToDate(objs[5].toString(), "yyyy-MM-dd HH:mm:ss"));
			vo.setId(objs[6]==null?"":objs[6].toString());
			DocListSc docListSc = (DocListSc)this.baseDao.get(DocListSc.class, objs[6].toString());
			DocListTc docListTc = (DocListTc)this.baseDao.get(DocListTc.class, objs[6].toString());
			DocListEn docListEn = (DocListEn)this.baseDao.get(DocListEn.class, objs[6].toString());
			vo.setDocNameSc(docListSc.getDocName());
			vo.setDocNameEn(docListEn.getDocName());
			vo.setDocNameTc(docListTc.getDocName());
			voList.add(vo);
		}
		jsonPaging.setList(voList);
		return jsonPaging;
	}
	
	/**
	 * 文档新增/新增/查看/编辑
	 * @author scshi_u330p
	 * @date 20161216
	 * */
	public DocInfoVo loadDocInfo(String docId, String language){
		DocInfoVo vo = new DocInfoVo();
		if(StringUtils.isNotBlank(docId)){
			DocList doc = (DocList)this.baseDao.get(DocList.class, docId);
			DocListTc docTc = (DocListTc)this.baseDao.get(DocListTc.class, docId);
			DocListSc docSc = (DocListSc)this.baseDao.get(DocListSc.class, docId);
			DocListEn docEn = (DocListEn)this.baseDao.get(DocListEn.class, docId);
			
			vo.setDoc(doc);
			vo.setDocEn(docEn);
			vo.setDocSc(docSc);
			vo.setDocTc(docTc);
		}
		return vo;
	}
	
	/**
	 * 文档列表信息保存
	 * @author scshi_u330p
	 * @date 20161219
	 * */
	public void saveDocListInfo(DocInfoVo vo){
		DocList doc = vo.getDoc();
		if(StringUtils.isBlank(doc.getId())){
			doc.setId(null);
			doc.setCreateTime(new Date());
			doc.setIsValid("1");
		}
		
		//文档定义中添加的文档，模板id为空
		if(null==doc.getTemplate() || StringUtils.isBlank(doc.getTemplate().getId()))
			doc.setTemplate(null);
		
		if (null==doc.getCreateTime())
			doc.setCreateTime(new Date());

		String effectDate = vo.getEffectTime();
		doc.setEffectDate(StringUtils.isNotBlank(effectDate)?DateUtil.StringToDateFormat(effectDate, "yyyy-MM-dd"):null);
		this.baseDao.saveOrUpdate(doc);
		
		DocListEn infoEn = (DocListEn)this.baseDao.get(DocListEn.class,doc.getId());
		
		if(null == infoEn){
			DocListEn enObj = new DocListEn();
			enObj.setId(doc.getId());
			enObj.setDocName(vo.getDocEn().getDocName());
			enObj.setRemark(vo.getDocEn().getRemark());
			this.baseDao.create(enObj);
		}else{
			infoEn.setDocName(vo.getDocEn().getDocName());
			infoEn.setRemark(vo.getDocEn().getRemark());
			this.baseDao.update(infoEn);
		}
		
		DocListTc infoTc = (DocListTc)this.baseDao.get(DocListTc.class, doc.getId());
		if(null == infoTc){
			DocListTc tcObj = new DocListTc();
			tcObj.setId(doc.getId());
			tcObj.setDocName(vo.getDocTc().getDocName());
			tcObj.setRemark(vo.getDocTc().getRemark());
			this.baseDao.create(tcObj);
		}else{
			infoTc.setDocName(vo.getDocTc().getDocName());
			infoTc.setRemark(vo.getDocTc().getRemark());
			this.baseDao.update(infoTc);
		}
		
		DocListSc infoSc = (DocListSc)this.baseDao.get(DocListSc.class, doc.getId());
		if(null == infoSc){
			DocListSc scObj = new DocListSc();
			scObj.setId(doc.getId());
			scObj.setDocName(vo.getDocSc().getDocName());
			scObj.setRemark(vo.getDocSc().getRemark());
			this.baseDao.create(scObj);
		}else{
			infoSc.setDocName(vo.getDocSc().getDocName());
			infoSc.setRemark(vo.getDocSc().getRemark());
			this.baseDao.update(infoSc);
		}
	}
	
	/**
	 * 文档模板删除
	 * @author scshi_u330p
	 * @date 20161227
	 * */
	public void kycDocTempDelete(String tempId){
		StringBuffer sb = new StringBuffer(" from DocList t where t.template.id=? ");
		List<DocList> list = this.baseDao.find(sb.toString(), new String[]{tempId}, false);
		if(!list.isEmpty()){
			//先删除模板下的文档
			for(int x=0;x<list.size();x++){
				DocList doc = list.get(x);
				this.kycDocInfoDelete(doc.getId());
			}
		}
		StringBuffer delSb = new StringBuffer("delete from DocTemplate t where t.id=? ");
		
		StringBuffer delSbEn = new StringBuffer("delete from DocTemplateEn t where t.id=? ");
		StringBuffer delSbTc = new StringBuffer("delete from DocTemplateTc t where t.id=? ");
		StringBuffer delSbSc = new StringBuffer("delete from DocTemplateSc t where t.id=? ");
		
		this.baseDao.updateHql(delSbEn.toString(), new String[]{tempId});
		this.baseDao.updateHql(delSbTc.toString(), new String[]{tempId});
		this.baseDao.updateHql(delSbSc.toString(), new String[]{tempId});
		
		this.baseDao.updateHql(delSb.toString(), new String[]{tempId});
	}
	
	/***
	 * 文档删除
	 * @author scshi_u330p
	 * @date 20161226
	 * */
	public void kycDocInfoDelete(String docId){
		
		StringBuffer sbEn = new StringBuffer(" delete from DocListEn t where t.id=? ");
		StringBuffer sbCn = new StringBuffer(" delete from DocListSc t where t.id=? ");
		StringBuffer sbTc = new StringBuffer(" delete from DocListTc t where t.id=? ");
		StringBuffer sb = new StringBuffer(" delete from DocList t where t.id=? ");
		
		this.baseDao.updateHql(sbEn.toString(), new String[]{docId});
		this.baseDao.updateHql(sbCn.toString(), new String[]{docId});
		this.baseDao.updateHql(sbTc.toString(), new String[]{docId});
		this.baseDao.updateHql(sb.toString(), new String[]{docId});
	}
}
