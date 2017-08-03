/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.memcached;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.transcoders.Transcoder;

import org.springframework.stereotype.Component;

/**
 * Memcached 操作工具类
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-7-21
 */
@Component("memcachedDao")
public class MemcachedDao {
	
	//@Autowired
	private MemcachedClient memcachedClient;

	/**
	 * 新增值
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public boolean add(String key, Object value, int expire) {
		try {
			return memcachedClient.add(key, expire, value);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 潜换或者修改值
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public boolean replace(String key, Object value, int expire) {
		try {
			return memcachedClient.replace(key, expire, value);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 潜换或者修改值
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public boolean set(String key, Object value, int expire) {
		try {
			return memcachedClient.set(key, expire, value);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除值
	 * @param key
	 * @return
	 */
	public boolean delete(String key) {
		try {
			return memcachedClient.delete(key);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 刷新缓存
	 * @return
	 */
	public void flush() {
		try {
			memcachedClient.flushAll();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 获得值
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		Object obj = null;
		try {
			obj = memcachedClient.get(key);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 获得多个值
	 * @return
	 */
	public Map<String, Object> getMulti(Collection<String> keys) {
		Map<String, Object> map = null;
		try {
			map = memcachedClient.get(keys);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 升序
	 * @param key
	 * @param by
	 * @param defaultValue
	 * @param expire
	 * @return
	 */
	public long increment(String key, int by, long defaultValue, int expire) {
		try {
			return memcachedClient.incr(key, by, defaultValue, expire);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 升序
	 * @param key
	 * @param by
	 * @return
	 */
	public long increment(String key, int by) {
		try {
			return memcachedClient.incr(key, by);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 降序
	 * @param key
	 * @param by
	 * @param defaultValue
	 * @param expire
	 * @return
	 */
	public long decrement(String key, int by, long defaultValue, int expire) {
		try {
			return memcachedClient.decr(key, by, defaultValue, expire);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 降序
	 * @param key
	 * @param by
	 * @return
	 */
	public long decrement(String key, int by) {
		try {
			return memcachedClient.decr(key, by);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 输出汇总信息
	 * @param stream
	 * @throws IOException
	 */
	public void printStats(OutputStream stream) throws IOException {
		try {
			Map<InetSocketAddress, Map<String, String>> statMap = memcachedClient.getStats();
			//if (stream == null) {
			//	stream = System.out;
			//}
			StringBuffer buf = new StringBuffer();
			Set<InetSocketAddress> addrSet = statMap.keySet();
			Iterator<InetSocketAddress> iter = addrSet.iterator();
			while (iter.hasNext()) {
				SocketAddress addr = iter.next();
				buf.append(addr.toString() + "/n");
				Map<String, String> stat = statMap.get(addr);
				Set<String> keys = stat.keySet();
				Iterator<String> keyIter = keys.iterator();
				while (keyIter.hasNext()) {
					String key = keyIter.next();
					String value = stat.get(key);
					buf.append("  key=" + key + ";value=" + value + "/n");
				}
				buf.append("/n");
			}
			stream.write(buf.toString().getBytes());
			stream.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得转码
	 * @return
	 */
	public Transcoder getTranscoder() {
		return memcachedClient.getTranscoder();
	}
}
