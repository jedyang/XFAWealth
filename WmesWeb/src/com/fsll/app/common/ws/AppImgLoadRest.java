package com.fsll.app.common.ws;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.wmes.base.WmesBaseRest;

/**
 * 
 * 公用接口类服务
 * 
 * @author mjhuang
 * @date 2016年2月15日
 */
@RestController
@RequestMapping("/service/common")
public class AppImgLoadRest extends WmesBaseRest{	
	/**
	 * @author scshi_u330p
	 * @date 20161215
	 * 页面重新获取图片SRC，显示图片
	 *
	 **/
	@RequestMapping(value = "/loadImg")
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
	
}
