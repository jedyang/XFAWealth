/**
 * 
 */
package com.fsll.app.fund.service;

import net.sf.json.JSONObject;

import com.fsll.app.fund.vo.AppFundBasicDataVO;
import com.fsll.app.fund.vo.AppFundSearchDetailVO;
import com.fsll.common.util.JsonPaging;

/**
 * 基金信息查询服务接口
 * @author zpzhou
 * @date 2016-6-20
 */
public interface AppFundInfoService {
	
	/**
	 * 得到首页最热基金更多列表信息
	 * @param jsonPaging 分页信息
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 显示语言
	 * @param num 返回的组合列表条数
	 * @return	JsonPaging	分页基金数据
	 */
	public JsonPaging findHotFundList(JsonPaging jsonPaging,String langCode,String periodCode,int num);
	
	/**
	 * 分页查询基金列表信息
	 * @param jsonPaging 分页参数
	 * @param keyWord 搜索关键词
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @param fundType 基金类型
	 * @param dateType 基金回报类型
	 * @param toCurrency 转换货币
	 * @return
	 */
	public JsonPaging findAllFundList(JsonPaging jsonPaging,String keyWord,String langCode,String userId,String fundType,String dateType, String toCurrency);
	
	
	/**
	 * 获取我关注的基金名单列表
	 * @param jsonPaging
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @param dateType 基金回报类型
	 * @param toCurrency 转换货币
	 * @return
	 */
	public JsonPaging getFundFollowList(JsonPaging jsonPaging,String langCode,String userId,String dateType, String toCurrency);
	
	
	/**
	 * 搜索基金信息
	 * @param jsonPaging 分页参数
	 * @param keyWord 搜索关键词
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @return
	 */
	public JsonPaging queryFundList(JsonPaging jsonPaging,String keyWord,String langCode,String userId);
	
	/**
	 * 得到
	 * @param fundId 基金ID
	 * @param langCode  语言
	 * @param userId 用户ID
	 * @param toCurrency 要转换的货币类型
	 * @return
	 */
	public AppFundBasicDataVO getFundBasicDataMess(String fundId, String langCode, String userId, String toCurrency);
	
	/**
	 * 根据基金类型统计
	 * @param fundId 同类基金ID
	 * @param typeCode 基金类型编码
	 * @param typeName 基金类型名称
	 * @return
	 */
	public int findFundTotalByType(String fundId, String typeCode, String typeName);

	/**
	 * 获取基金搜索的查询条件
	 * @author zxtan
	 * @date 2017-06-01
	 * @param langCode
	 * @return
	 */
	public AppFundSearchDetailVO findFundSearchDetail(String langCode);
	
	/**
	 * 搜索基金信息
	 * @param jsonPaging 分页参数
	 * @param jsonObject 搜索内容
	 * @return
	 */
	public JsonPaging queryFundListByAdvance(JsonPaging jsonPaging,JSONObject jsonObject);
}
