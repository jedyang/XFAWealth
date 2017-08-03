package com.fsll.wmes.fund.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.FundHouse;
import com.fsll.wmes.entity.FundHouseEn;
import com.fsll.wmes.entity.FundHouseSc;
import com.fsll.wmes.entity.FundHouseTc;
import com.fsll.wmes.fund.vo.FundHouseDataVO;

/**
 * 基金公司信息查询服务接口
 * @author Yan
 * @date 2016-11-16
 */
public interface FundHouseService {
	/**
	 * 增加或者修改一条数据
	 * @param 操作日志 
	 * @return 
	 
	public  FundHouseDataVO saveOrUpdate(FundHouseDataVO fundHouse);*/

	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);
	
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids);
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public FundHouseDataVO findById(String id);
	
	/**
	 * 通过ID查找一条基金公司信息
	 * @param id
	 * @return
	 */
	public FundHouse findFundHouseById(String id);
	
	/**
	 * 通过ID查找一条基金公司附加英文信息
	 * @param id
	 * @return
	 */
	public FundHouseEn findFundHouseEnById(String id);
	
	/**
	 * 通过ID查找一条基金公司附加简体中文信息
	 * @param id
	 * @return
	 */
	public FundHouseSc findFundHouseScById(String id);
	
	/**
	 * 通过ID查找一条基金公司附加繁体中文信息
	 * @param id
	 * @return
	 */
	public FundHouseTc findFundHouseTcById(String id);
	
    /**
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param fundHouse 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonPaging, String houseName, String startDate, String endDate, String langCode);
	
	/**
	 * 更新/保存方法
	 * @param fundHouse
	 * @param fundHouseSc
	 * @param fundHouseTc
	 * @param fundHouseEn
	 * @param isAdd
	 * @return
	 */
	public FundHouse save(FundHouse fundHouse, FundHouseSc fundHouseSc, FundHouseTc fundHouseTc
			, FundHouseEn fundHouseEn, boolean isAdd);
	
	/**
	 * 获取fundHouse列表
	 * @return
	 */
	public List<FundHouseDataVO> getFundHouseVoList();
}
