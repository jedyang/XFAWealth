/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.memcached;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Memcached 操作示例类
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-7-21
 */
@Controller
@RequestMapping("/memcached/demo")
public class MemcachedDemo {

	@Autowired
	private MemcachedDao memcachedDao;

	@RequestMapping("add")
	public void add(HttpServletResponse response, HttpServletRequest request) {
		try {
			boolean flag = memcachedDao.add("key1", "add", 1000);
			if (flag) {
				this.responseText("add成功,key=key1,value=add", response);
			} else {
				this.responseText("add失败！", response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("get")
	public void get(HttpServletResponse response, HttpServletRequest request) {
		try {
			Object val = memcachedDao.get("key1");
			this.responseText("get,key=key1,value=" + val, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("replace")
	public void replace(HttpServletResponse response, HttpServletRequest request) {
		try {
			boolean flag = memcachedDao.replace("key1", "replace", 1000);
			if (flag) {
				this.responseText("replace成功,key=key1,value=replace", response);
			} else {
				this.responseText("replace失败！", response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("set")
	public void set(HttpServletResponse response, HttpServletRequest request) {
		try {
			boolean flag = memcachedDao.set("key1", "set", 1000);
			if (flag) {
				this.responseText("set成功,key=key1,value=set", response);
			} else {
				this.responseText("set失败！", response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 响应字符串 用于ajax请求响应
	 * 
	 * @param str
	 * @throws Exception
	 */
	public void responseText(String str, HttpServletResponse reponse)throws Exception {
		reponse.setContentType("text/html;charset=UTF-8");
		PrintWriter out = reponse.getWriter();
		out.write(str);
		out.flush();
		out.close();
	}

}
