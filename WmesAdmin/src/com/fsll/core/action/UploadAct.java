/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.UploadUtil;
import com.fsll.core.base.CoreBaseAct;

/**
 * 控制器:系统首页
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
@Controller
public class UploadAct extends CoreBaseAct{
	
	private static HashMap<Long,String> moduleRelate = new HashMap<Long,String>();
	static{
		moduleRelate.put(1L,"m");//菜单模块	
		moduleRelate.put(20L,"portrait");//头像存放目录
	}
	
	/**
	 * 通过的文件上传
	 */
	@RequestMapping("/upload")
	public String upload(HttpServletRequest request,Long moduleId,HttpServletResponse response){
    	String ctxPath = request.getSession().getServletContext().getRealPath("/");
    	String filePaths = "";
		try {
			//创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			//判断 request 是否有文件上传,即多部分请求
			if(multipartResolver.isMultipart(request)){
				//转换成多部分request  
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
				//取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();
				while(iter.hasNext()){
					//取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if(file != null){
						//取得当前上传文件的文件名称
						String oriFileName = file.getOriginalFilename();
						//如果名称不为"",说明该文件存在，否则说明该文件不存在
						if(!"".equals(oriFileName.trim())){
							String myFilePath = UploadUtil.getFileName(oriFileName,true,moduleRelate.get(moduleId));
							File localFile = new File(ctxPath+myFilePath);
							if(!localFile.getParentFile().exists()){
								localFile.getParentFile().mkdirs();
							}
							file.transferTo(localFile);
							if("".equals(filePaths)){
								filePaths = oriFileName+":"+myFilePath;
							}else{
								filePaths += ";"+oriFileName+":"+myFilePath;
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("filePaths",filePaths);
		JsonUtil.toWriter(obj, response);
		return null;
	}
	
	/**
	 * 指定文件上传
	 */
    @RequestMapping("/uploadSec")
	public String uploadSec(@RequestParam MultipartFile file,Long moduleId,HttpServletRequest request,HttpServletResponse response) {
    	String ctxPath = request.getSession().getServletContext().getRealPath("/");
    	String fileName = file.getOriginalFilename();
    	String filePath = "";
		try {
	    	InputStream is = file.getInputStream();
	    	filePath = UploadUtil.getFileName(fileName,true,moduleRelate.get(moduleId));
			File localFile = new File(ctxPath+filePath);
			if(!localFile.getParentFile().exists()){
				localFile.getParentFile().mkdirs();
			}
	        FileOutputStream os = new FileOutputStream(ctxPath+filePath);
	        int byteCount = 0;
	        byte[] bytes = new byte[1024];
	        while ((byteCount = is.read(bytes)) != -1){
	        	os.write(bytes, 0, byteCount);
	        }
	        os.close();
	        is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("fileName",fileName);
		obj.put("filePath",filePath);
		JsonUtil.toWriter(obj, response);
		return null;
	}
    
	/**
	 * 文件下载
	 * 
	 */
	@RequestMapping("/download")
	public String download(HttpServletRequest request,HttpServletResponse response,String fileName) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
		//String ctxPath = request.getSession().getServletContext().getRealPath("/")+ "\\" + "d\\";
		//String downLoadPath = ctxPath + fileName;
		String downLoadPath = fileName;
		try {
			long fileLength = new File(downLoadPath).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
		return null;
	}
	
	/**
	 * 上传图片，支持多文件，多缩略图，水印等
	 * @author 林文伟
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping( value = "/wmesUpload")
	public void upload(HttpServletRequest request,HttpServletResponse response){
    	//String IsWater = request.getParameter("IsWater");
    	String modulerelate = request.getParameter("moduleRelate");
    	
//    	String ctxPath = request.getSession().getServletContext().getRealPath("/");
    	try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
          //取得上传文件
            MultipartFile multipartFile = multipartRequest.getFile(("file").toString());
            if(multipartFile != null){
            	//取得当前上传文件的文件名称
				String oriFileName = multipartFile.getOriginalFilename();
				//如果名称不为"",说明该文件存在，否则说明该文件不存在
				if(!"".equals(oriFileName.trim())){
					String myFilePath = UploadUtil.getFileName(oriFileName,true,modulerelate);
//					File localFile = new File(ctxPath+myFilePath);
					File localFile = new File(myFilePath);
					if(!localFile.getParentFile().exists()){
						localFile.getParentFile().mkdirs();
					}
					//上传文件
					multipartFile.transferTo(localFile);
		            //生成缩略图
		            String [] savePathBySplitTemp = myFilePath.split("/");
		            /*
		            String temp = "";
		            for(int i = 0;i < savePathBySplitTemp.length-1; i++){
		                if(i!=savePathBySplitTemp.length-2){
		                    temp+=savePathBySplitTemp[i]+"/";
		                }else{
		                    temp+=savePathBySplitTemp[i];
		                }
		            }
		            */
		            String fileName = savePathBySplitTemp[savePathBySplitTemp.length-1];
//		            String pathTemp = request.getSession().getServletContext().getRealPath(temp); 
//		            pathTemp = pathTemp.replaceAll("\\\\", "//");
//		            pathTemp += "\\";
		            //生成新的缩略图文件名称
		            String thumbnail112x100FileName = UploadUtil.getThumbnailsFileName(oriFileName,UploadUtil.THUMBNAILSWH_112X100);
//		            String thumbnail112x100FilePath = pathTemp + thumbnail112x100FileName;
		            String thumbnail200x150FileName = UploadUtil.getThumbnailsFileName(oriFileName,UploadUtil.THUMBNAILSWH_200X150);
//		            String thumbnail200x150FilePath = pathTemp + thumbnail200x150FileName;
		            String thumbnail400x300FileName = UploadUtil.getThumbnailsFileName(oriFileName,UploadUtil.THUMBNAILSWH_400X300);
//		            String thumbnail400x300FilePath = pathTemp + thumbnail400x300FileName;
		            String thumbnail600x400FileName = UploadUtil.getThumbnailsFileName(oriFileName,UploadUtil.THUMBNAILSWH_600X400);
//		            String thumbnail600x400FilePath = pathTemp + thumbnail600x400FileName;
		            //设置前端要显示的缩略图规格
		            String frontThumbnail112x100FilePath = UploadUtil.genFilePath(modulerelate) + "/" + thumbnail112x100FileName;
		            String frontThumbnail200x150FilePath = UploadUtil.genFilePath(modulerelate) + "/" + thumbnail200x150FileName;
		            String frontThumbnail400x300FilePath = UploadUtil.genFilePath(modulerelate) + "/" + thumbnail400x300FileName;
		            String frontThumbnail600x400FilePath = UploadUtil.genFilePath(modulerelate) + "/" + thumbnail600x400FileName;
		            //上传时同时生成各个规格的缩略图
		            //Thumbnails.of(localFile).size(112,100).toFile(thumbnail112x100FilePath);
		    		//Thumbnails.of(localFile).size(200,150).toFile(thumbnail200x150FilePath);
		    		//Thumbnails.of(localFile).size(400,300).toFile(thumbnail400x300FilePath);
		    		//Thumbnails.of(localFile).size(600,400).toFile(thumbnail600x400FilePath);
		    		
		    		
		    		//获取后缀名
		    		String suffix = fileName.substring(fileName.indexOf(".")+1);
		    		//返回实体
		    		Map<String, Object> obj = new HashMap<String, Object>();
		            obj.put("status", 1);
		            obj.put("msg", "upload success");
		            obj.put("orifilename", oriFileName);//原文件名
		            obj.put("newfilename", fileName);//新文件名
		            obj.put("filepath", myFilePath);//新文件路径（路径+名称）
		            obj.put("filepath_112x100", frontThumbnail112x100FilePath);//各个缩略图路径（路径+名称）
		            obj.put("filepath_200x150", frontThumbnail200x150FilePath);
		            obj.put("filepath_400x300", frontThumbnail400x300FilePath);
		            obj.put("filepath_600x400", frontThumbnail600x400FilePath);
		            obj.put("modulerelate", modulerelate);
		            obj.put("size", multipartFile.getSize());
		            obj.put("suffix",suffix);

					ResponseUtils.renderHtml(response, JsonUtil.toJson(obj));
				}
            }

        }catch (Exception e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
 
    }
	
    /**
	 * @author scshi_u330p
	 * @date 20161215
	 *页面重新获取图片SRC，显示图片
	 * */
	@RequestMapping(value = "/loadImgSrcByPath")
	public String loadImgSrcByPath(HttpServletRequest request,HttpServletResponse response,String filePath) throws IOException {
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
		String downLoadPath = filePath;
		try {
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
		return null;
	}
	
	/**
	 * layedit 编辑器文件上传
	 * @author wwluo
	 */
	@RequestMapping("/layeditUpload")
	public void layeditUpload(HttpServletRequest request,String moduleId,HttpServletResponse response){
		String langCode = getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", null); // 0表示成功，其它失败
		result.put("msg", null); // 一般上传失败后返回
		result.put("data", null);
		String filePaths = UploadUtil.upload(request, moduleId);
		if (StringUtils.isNotBlank(filePaths)) {
			result.put("code", "0"); 
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("src", "/loadImgSrcByPath.do?filePath=" + (filePaths.split(":"))[1]);
			data.put("title", (filePaths.split(":"))[0]);
			result.put("data", data); 
		}else {
			result.put("msg", PropertyUtils.getPropertyByLang(langCode, "uploaded.failed"));
		}
		JsonUtil.toWriter(result, response);
	}
}