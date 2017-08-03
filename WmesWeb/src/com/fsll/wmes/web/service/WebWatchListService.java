/**
 * 
 */
package com.fsll.wmes.web.service;


import java.util.List;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.entity.WebWatchlistType;

/**
 * @author zxtan
 * 
 * 收藏状态接口
 *
 */

public interface WebWatchListService {
	
	/** 	收藏/取消
	 * @author zxtan
	 * @date 2016-12-30
	 * @param productId 对应产品id
	 * @param memberId 网站会员ID
	 * @parame OpType	Follow-设置关注;Delete-取消关注
	 * @param typeId 对应模块
	 */
	public WebWatchlist saveWebWatchlist(String productId, String memberId,String opType, String typeId);
	
	
	/**	获取基金收藏状态
	 * @author zxtan
	 * @date 2016-12-30
	 * @param productId 对应产品id
	 * @param memberId 网站会员ID
	 * @param typeId 对应模块
	 */
	public String getWebWatchStatus(String productId, String memberId,String typeId);

	/**
	 * 获取type
	 * @author mqzou 2017-04-01
	 * @param id
	 * @return
	 */
	public WebWatchlistType findTypeById(String id);
	

	/**
	 * 保存type
	 * @author mqzou 2017-04-01
	 * @param id
	 * @return
	 */
	public WebWatchlistType saveType(WebWatchlistType type);
	
	/**
	 * 获取用户的所有分类中的最大排序号
	 * @author mqzou 2017-04-01
	 * @param memberId
	 * @return
	 */
	public int getMaxTypeOrderBy(String memberId);
	
	/**
	 * 获取用户的所有分类
	 * @author mqzou 2017-04-01
	 * @param memberId
	 * @return
	 */
	public List<WebWatchlistType> findAllType(String memberId);
	
	/**
	 * 新增type
	 *  @author mqzou 2017-04-01
	 * @param name
	 * @param member
	 * @return
	 */
	public WebWatchlistType saveWatchlistType(String name,MemberBase member);
	
	/**
	 * 删除
	 * @author mqzou 2017-04-05
	 * @param type
	 */
	public void deleteType(WebWatchlistType type);
	
}
