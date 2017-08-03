/*
 * Copyright (c) 2009-2012 by fsll
 * All rights reserved.
 */

package com.fsll.common.web;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 产生图形验证码
 * 
 * @author 黄模建 E-mail:huangmojian@163.com
 * @version 1.0 Created On: 2009-9-7
 */
public class AuthImg extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		OutputStream os = response.getOutputStream();
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);

		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		String sRand = "";
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}			
		request.getSession().setAttribute("randStr", sRand);
		g.dispose();			
		//Weblogic下需要增加以下一句，TOMCAT不用增加。
		response.reset();
		ImageIO.write(image, "JPEG", os);
		//注意看以下几句的使用
		os.flush();
		os.close();
		os = null;
		response.flushBuffer();
		//weblogic需要屏蔽以下两句。TOMCAT不用屏蔽。
		//out.clear();
		//out = pageContext.pushBody();
	}

	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

}
