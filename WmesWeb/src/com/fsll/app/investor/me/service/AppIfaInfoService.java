package com.fsll.app.investor.me.service;

import java.util.List;

import com.fsll.app.investor.me.vo.AppIfaItemVO;
import com.fsll.app.investor.me.vo.AppIfaSearchItemVO;
import com.fsll.common.util.JsonPaging;


/**
 * 基金信息查询服务接口
 * @author zpzhou
 * @date 2016-7-25
 */
public interface AppIfaInfoService {
	
	/**
	 * 得到distributor信息
	 * @return
	 */
	public List<AppIfaSearchItemVO> getDistributorMess();
	
	/**
	 * @param jsonPaging 分页参数
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @param serviceRegion 服务区域
	 * @param expertiseType 擅长领域 
	 * @param country 居住地
	 * @param distributor 分销商
	 * @return
	 */
	public JsonPaging findIfaList(JsonPaging jsonPaging,String langCode,String userId,String serviceRegion,String expertiseType, String country,String distributor);
	
	/**
	 * 添加/取消好友
	 * @param fromMemberId 发起的会员ID
	 * @param toMemberId  关联的会员ID
	 * @param applyMsg  申请信息
	 * @param OpType Add-添加;Delete-删除
	 * @return
	 */
	public int saveWebFriendMess(String fromMemberId,String toMemberId,String applyMsg,String opType);
	
	/**
	 * 得到Ifa信息
	 * @author zxtan
	 * @date 2017-01-16
	 * @return
	 */
	public List<AppIfaItemVO> findMyIfaList(String memberId,String langCode);
	
}
