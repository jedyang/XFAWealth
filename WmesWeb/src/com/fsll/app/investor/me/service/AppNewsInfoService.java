package com.fsll.app.investor.me.service;

import java.util.List;

import com.fsll.app.investor.me.vo.AppNewsInfoItemVO;
import com.fsll.app.investor.me.vo.AppNewsItemVo;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.NewsInfo;


/**
 * 新闻接口
 * @author zxtan
 * @date 2017-01-17
 */
public interface AppNewsInfoService {
	
	/**
	 * 得到新闻列表信息
	 * @author zxtan
	 * @date 2017-01-17
	 * @param memberId 用户ID
	 * @param ifaMemberId Ifa用户ID
	 * @return
	 */
	public List<AppNewsInfoItemVO> findMyIfaNewsList(String memberId,String ifaMemberId,String langCode);
	
	/**
	 * 获取新闻的信息实体
	 * @author zxtan
	 * @date 2017-01-17
	 * @param id
	 * @return
	 */
	public NewsInfo findNewsInfoById(String id);
	
	/**
	 * 得到新闻列表信息
	 * @author zxtan
	 * @date 2017-03-10
	 * @param id 新闻Id
	 * @return
	 */
	public AppNewsInfoItemVO findNewsInfo(String id);
}
