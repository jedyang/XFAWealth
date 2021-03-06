/*
 * Copyright (c) 2009-2012 by fsll
 * All rights reserved.
 */

package com.fsll.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import sun.misc.BASE64Decoder;

/**
 * @author 黄模建 E-mail:huangmojian@163.com
 * @version 1.0
 * Created On: Sep 26, 2011
 */
public class UploadUtil {
	
	private static final Logger log = LoggerFactory.getLogger(UploadUtil.class);
	
	// 上传文件存放路径
	public final static String UPLOADDIR = "/u";
	// 如果是图像，上传时生成的缩略图规格文件名 	add by 林文伟
	public final static String THUMBNAILSWH_112X100 = "_thumbnails_112x100";
	public final static String THUMBNAILSWH_200X150 = "_thumbnails_200x150";
	public final static String THUMBNAILSWH_400X300 = "_thumbnails_400x300";
	public final static String THUMBNAILSWH_600X400 = "_thumbnails_600x400";
	//完整目录
	//public final static String FULL_UPLOADDIR ="E:/apache-tomcat-7.0.65/webapps/wmes";
	
	/**
	 * 路径分隔符
	 */
	public static final char SPT = '/';
	
	public static void saveFile(String fileName,File attachFile){  
		try {  
			  File toSave = new File(fileName);
			  FileUtils.copyFile(attachFile, toSave);
		} catch (FileNotFoundException ex) {  
		    ex.printStackTrace();  
		} catch (IOException ex) {  
		    ex.printStackTrace();  
		}  
   } 
	
	public static void removeFile(String fileName){
		File file = new File(fileName);
		if (file.delete()) {
			log.debug("删除未被使用的文件：{}", file.getName());
		} else {
			log.warn("删除文件失败：{}", file.getName());
		}
   } 
	
	public static void removeFileScore(String filePath){
		File file = new File(filePath);
		try{
		  if(file.exists()){
		    if (file.delete()) {
		   	  log.debug("删除未被使用的文件：{}", file.getName());
		    } else {
			  log.warn("删除文件失败：{}", file.getName());
		    }
		  }
		}catch(Exception e){
			e.printStackTrace();
		}
   } 
	/**
	 * 获文件名
	 * @param fileName 文件名
	 * @param isGenName 是否产生文件名
	 * @param module 文件类型
	 * @param suffix 后缀
	 * @return
	 */
	public static String getFileName(String fileName,boolean isGenName,String module) {
		String uploadPath = UPLOADDIR+SPT+module;
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		uploadPath += "/" + year + (month > 9 ? month : "0" + month);
		String suffix = fileName.substring(fileName.indexOf(".")+1);
		String targetFileName = UUID.randomUUID().toString() +"."+ suffix;
		uploadPath += "/" + targetFileName;
		return uploadPath;
	}
	
	/**
	 * 获文件名
	 * @param fileName 文件名
	 * @param isGenName 是否产生文件名
	 * @param module 文件类型
	 * @param suffix 后缀
	 * @return
	 */
	public static String getBigFileName(String module) {
		String uploadPath = UPLOADDIR+SPT+module;
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		uploadPath += "/" + year + (month > 9 ? month : "0" + month);	
		return uploadPath;
	}
	
	/**
	 * 获得文件名
	 * 4位随机数加上当前时间
	 */
	public static String genFileName(String fileName) {
		String suffix = fileName.substring(fileName.indexOf(".")+1);
		String targetFileName = UUID.randomUUID().toString() +"."+ suffix;
		return targetFileName;
	}
	
	/**
	 * 获得文件路径
	 * @return
	 */
	public static String genFilePath(String module) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		String uploadPath = UPLOADDIR + SPT + module + SPT + year + (month > 9 ? month : "0" + month) + SPT;
		return uploadPath;
	}

	
	/**
	 * 读取图像的二进制流
	 * @param infile
	 * @return
	 */
	public static FileInputStream getByteImage(String infile) {
		FileInputStream inputImage = null;
		File file = new File(infile);
		try {
			inputImage = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return inputImage;
	}
	
	/**
	 * base64字符串转化成图片 并存储
	 * @author qgfeng
	 * @date 2016-9-30
	 * @param imgStr base64字符码
	 * @param filePath 存储路径
	 * @return
	 */
    public static boolean changeToFile(String imgStr,String filePath)  
    {   
        if (imgStr == null) 
            return false;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try   
        {  
            byte[] b = decoder.decodeBuffer(imgStr);  
            for(int i=0;i<b.length;++i)  
            {  
                if(b[i]<0)  
                {
                    b[i]+=256;  
                }  
            }  
            OutputStream out = new FileOutputStream(filePath);      
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        }   
        catch (Exception e)   
        {  
            return false;  
        }  
    }  
    
	/**
	 * 获文件名
	 * @author 林文伟
	 * @param fileName 文件名
	 * @param isGenName 是否产生文件名
	 * @param module 文件类型
	 * @param suffix 后缀
	 * @return
	 */
    public static String getThumbnailsFileName(String fileUuid,String fileName,String thumbnailsWH) {
		String suffix = fileName.substring(fileName.indexOf(".")+1);
		String targetFileName = fileUuid + thumbnailsWH + "."+ suffix;//UUID.randomUUID().toString()
		return targetFileName;
	}
  
    /**
     * 删除文件夹
     * @author mjhuang
     * param folderPath 文件夹完整绝对路径
     */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 删除指定文件夹下所有文件
     * @author mjhuang
     * param folderPath 文件夹完整绝对路径
     */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
     * 创建（复制）目标文件
     * @author wwluo
     * @param reFile 源文件
     * @param tempFile 目标文件
     */
	public static void createsTemporaryFiles(File reFile, File tempFile) {
		if(reFile.isFile()){
			FileInputStream fis;
			FileOutputStream fos;
			try {
				fis = new FileInputStream(reFile);
				fos = new FileOutputStream(tempFile);
				byte[] by = new byte[100];
				int len;
				while((len=fis.read(by)) != -1){
					fos.write(by, 0, len);
				}
				fos.flush();
				fos.close();
				fis.close();
				//tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
     * 文件上传
     * @param request
     * @param module
     */
	public static String upload(HttpServletRequest request, String module) {
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
							String myFilePath = getFileName(oriFileName, true, module);
							File localFile = new File(myFilePath);
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
		return filePaths;
	}
}
