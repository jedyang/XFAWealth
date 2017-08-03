package com.fsll.app.ifa.market.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.axis.components.image.ImageIO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fsll.app.ifa.market.service.AppCrmMemoService;
import com.fsll.app.ifa.market.vo.AppCrmMemoVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.UploadUtil;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.vo.AccessoryFileVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmMemo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;

/**
 * IFA-Market投资组合服务
 * @author zxtan
 * @date 2017-03-29
 */
@Service("appIfaMarketMemoService")
public class AppCrmMemoServiceImpl extends BaseService implements
		AppCrmMemoService {

	
	/**
	 * 获取IFA客户详情的销售记录列表
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public JsonPaging findCrmMemoList(JsonPaging jsonPaging,String ifaMemberId, String clientMemberId) {
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmMemo m ");
		hql.append(" inner join MemberIfa i on i.id=m.ifa.id ");
		hql.append(" inner join CrmCustomer c on c.member.id=m.member.id and c.ifa.id=i.id ");
		hql.append(" where i.member.id=? ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		if(!"".equals(clientMemberId)){
			hql.append(" and m.member.id=? ");
			params.add(clientMemberId);
		}
		
		hql.append(" order by m.createTime desc ");
		
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<AppCrmMemoVO> voList = new ArrayList<AppCrmMemoVO>();
		List list = jsonPaging.getList();
		if(null != list &&!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				CrmMemo memo = (CrmMemo) objs[0];
				CrmCustomer customer = (CrmCustomer) objs[2];
				
				AppCrmMemoVO vo = new AppCrmMemoVO();
				vo.setId(memo.getId());
				vo.setCustomerId(customer.getId());
				vo.setMemberId(memo.getMember().getId());
				vo.setNickName(customer.getNickName());
				vo.setMemoText(memo.getMemoText());
				vo.setCreateTime(DateUtil.dateToDateString(memo.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
				vo.setLastModify(DateUtil.dateToDateString(memo.getLastModify(), CommonConstants.FORMAT_DATE_TIME));
				List<AccessoryFile> fileList = findFileAttr(memo.getId(),"crm_memo");
				List<AccessoryFileVO> voFileList = new ArrayList<AccessoryFileVO>();
				if(null != fileList && !fileList.isEmpty()){
					for (AccessoryFile item : fileList) {
						AccessoryFileVO tmpVO = new AccessoryFileVO();
						tmpVO.setId(item.getId());
						tmpVO.setFileName(item.getFileName());
						tmpVO.setFilePath(PageHelper.getImgUrlForWS(item.getFilePath()));
						tmpVO.setFileType(item.getFileType());
						tmpVO.setModuleType(item.getModuleType());
						tmpVO.setLangCode(item.getLangCode());
						tmpVO.setCreateTime(DateUtil.dateToDateString(item.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
						voFileList.add(tmpVO);
					}
				}
				vo.setFileList(voFileList);				
				voList.add(vo);				
			}
		
			jsonPaging.setList(voList);
		}
		
		
		return jsonPaging;
	}
	
	/**
	 * 获取IFA客户详情的销售记录列表
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public AppCrmMemoVO findCrmMemo(String memoId) {
		AppCrmMemoVO vo = new AppCrmMemoVO();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmMemo m ");
		hql.append(" inner join MemberIfa i on i.id=m.ifa.id ");
		hql.append(" inner join CrmCustomer c on c.member.id=m.member.id and c.ifa.id=i.id ");
		hql.append(" where m.id=? ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(memoId);
		
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		if(null != list &&!list.isEmpty()){
			Object[] objs = (Object[]) list.get(0);
			CrmMemo memo = (CrmMemo) objs[0];
			CrmCustomer customer = (CrmCustomer) objs[2];
			
			vo.setId(memo.getId());
			vo.setCustomerId(customer.getId());
			vo.setMemberId(customer.getMember().getId());
			vo.setNickName(customer.getNickName());
			vo.setMemoText(memo.getMemoText());
			vo.setCreateTime(DateUtil.dateToDateString(memo.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
			vo.setLastModify(DateUtil.dateToDateString(memo.getLastModify(), CommonConstants.FORMAT_DATE_TIME));
			List<AccessoryFile> fileList = findFileAttr(memo.getId(),"crm_memo");
			List<AccessoryFileVO> voFileList = new ArrayList<AccessoryFileVO>();
			if(null != fileList && !fileList.isEmpty()){
				for (AccessoryFile item : fileList) {
					AccessoryFileVO tmpVO = new AccessoryFileVO();
					tmpVO.setId(item.getId());
					tmpVO.setFileName(item.getFileName());
					tmpVO.setFilePath(PageHelper.getImgUrlForWS(item.getFilePath()));
					tmpVO.setFileType(item.getFileType());
					tmpVO.setModuleType(item.getModuleType());
					tmpVO.setLangCode(item.getLangCode());
					tmpVO.setCreateTime(DateUtil.dateToDateString(item.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
					voFileList.add(tmpVO);
				}
			}
			vo.setFileList(voFileList);
		}
		
		
		return vo;
	}
	
	/**
	 * 更新/删除IFA客户详情的销售记录
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public boolean deleteCrmMemo(String id) {
		if(StringUtils.isNotBlank(id)){
    		String  hqlDelete = "delete AccessoryFile t where t.relateId=? ";
    		baseDao.updateHql(hqlDelete, new Object[]{id});
		}
		CrmMemo info = (CrmMemo) baseDao.get(CrmMemo.class, id);
		baseDao.delete(info);
		return true;
	}
	
	
	/**
	 * 更新/新增销售记录
	 * @author zxtan
	 * @date 2017-04-27
	 */
	public boolean saveCrmMemo(HttpServletRequest request) {
		
		String id = "";
		String ifaMemberId = "";
		String customerId = "";
		String memoText = "";
		String langCode = "";	
		
		// 声明图片后缀名数组 
		String imageArray [] = { 
		    "bmp", "dib", "gif", "jfif", "jpe","jpeg","jpg","tiff","ico","png"
		}; 
		
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
	                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
		if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
           
    		memoText = multiRequest.getParameter("memoText");
    		ifaMemberId = multiRequest.getParameter("ifaMemberId")==null?"":multiRequest.getParameter("ifaMemberId");
    		customerId = multiRequest.getParameter("customerId")==null?"":multiRequest.getParameter("customerId");
    		id = multiRequest.getParameter("id")==null?"":multiRequest.getParameter("id");
    		langCode = multiRequest.getParameter("langCode");    		
    		langCode = StringUtils.isBlank(langCode)?CommonConstants.DEF_LANG_CODE:langCode;
    		CrmMemo info = (CrmMemo) baseDao.get(CrmMemo.class, id);
    		if(null == info){
    			info = new CrmMemo();
    			info.setId(null);	
    			
    			MemberIfa ifa = getIfaByMemberId(ifaMemberId);
    			CrmCustomer customer = (CrmCustomer) baseDao.get(CrmCustomer.class, customerId);
    			if(null == ifa || null == customer || null == customer.getMember()) return false;
    			
    			info.setIfa(ifa);
    			info.setMember(customer.getMember());
    			info.setCreateTime(new Date());
    		}
    		info.setMemoText(memoText);
    		info.setLastModify(new Date());
    		info = (CrmMemo) baseDao.saveOrUpdate(info);
    		
    		
    		if(StringUtils.isNotBlank(info.getId())){
	    		String  hqlDelete = "delete AccessoryFile t where t.relateId=? ";
	    		baseDao.updateHql(hqlDelete, new Object[]{info.getId()});
    		}
    		
    		String yearMonth = DateUtil.getDateStr(DateUtil.getCurDate(), "yyyyMM");
    		String prePath = "/u/memo/"+yearMonth+"/";
    		File dir = new File(prePath);
    		if(dir.exists()){
    			if(!dir.isDirectory()){
    				dir.mkdirs();
    			}
    		}else {
				dir.mkdirs();
			}
    		
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();             
            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                                
                if(file!=null)
                {
                	String uuid = UUID.randomUUID().toString();
                	String oriName = file.getOriginalFilename();
                	String suffix = oriName.substring(oriName.indexOf(".")+1);
                	String fileName = uuid+"."+suffix;
                    String filePath = prePath+fileName;
                    //上传
                    try {
                    	File destFile = new File(filePath);
						file.transferTo(destFile);	
						AccessoryFile fileInfo = new AccessoryFile();
						fileInfo.setId(null);
						fileInfo.setFileName(oriName);
						fileInfo.setFilePath(filePath);
						fileInfo.setFileType(suffix);
						fileInfo.setModuleType("crm_memo");
						fileInfo.setLangCode(langCode);
						fileInfo.setRelateId(info.getId());
						fileInfo.setCreateBy(info.getIfa().getMember());
						fileInfo.setCreateTime(new Date());
						baseDao.saveOrUpdate(fileInfo);
						
						for (String imgSuffix : imageArray) {
							if(suffix.equalsIgnoreCase(imgSuffix)){
								String thumbnail112x100FileName = uuid+UploadUtil.THUMBNAILSWH_112X100+"."+suffix;
								String thumbnail200x150FileName = uuid+UploadUtil.THUMBNAILSWH_200X150+"."+suffix;
								String thumbnail600x400FileName = uuid+UploadUtil.THUMBNAILSWH_600X400+"."+suffix;
								Thumbnails.of(destFile.getAbsoluteFile()).size(112,100).toFile(prePath+thumbnail112x100FileName);
								Thumbnails.of(destFile.getAbsoluteFile()).size(200,150).toFile(prePath+thumbnail200x150FileName);
								Thumbnails.of(destFile.getAbsoluteFile()).size(600,400).toFile(prePath+thumbnail600x400FileName);
								break;
							}
						}
						
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }                 
            }           
        }
		
		return true;
	}

	private MemberIfa getIfaByMemberId(String memberId){
		MemberIfa ifa = null;
		String hql = "from MemberIfa i where i.member.id=?";
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List<MemberIfa> list = baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){
			ifa = list.get(0);
		}
		return ifa;
	}

}
