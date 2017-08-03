package com.fsll.wmes.bond.service;

import java.util.List;

import com.fsll.wmes.bond.vo.BondInfoVO;
import com.fsll.wmes.bond.vo.BondProductVO;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondInfoSc;
import com.fsll.wmes.entity.BondInfoTc;
import com.fsll.wmes.stock.vo.StockProductVO;

/**
 * 债券信息管理
 * @author Yan
 * @date 2016-12-09
 */
public interface BondInfoService {
	
	/**
	 * 获取债券基础数据列表
	 * @return
	 */
	public List<BondProductVO> getBondProductVoList(String langCode);
	/**
	 * 通过ID查找一条债券基本信息
	 * @param id
	 * @return
	 */
	public BondInfo findBondInfoById(String id);
	
	/**
	 * 通过ID查找一条债券附加英文信息
	 * @param id
	 * @return
	 */
	public BondInfoEn findBondInfoEnById(String id);
	
	/**
	 * 通过ID查找一条债券附加简体中文信息
	 * @param id
	 * @return
	 */
	public BondInfoSc findBondInfoScById(String id);
	
	/**
	 * 通过ID查找一条债券附加繁体中文信息
	 * @param id
	 * @return
	 */
	public BondInfoTc findBondInfoTcById(String id);
	
	/**
	 * 通过ID查找债券全部信息
	 * @param id
	 * @param langCode 语言
	 * @return
	 */
	public BondInfoVO findBondFullInfoById(String id, String langCode);
	
	/**
	 * 通过ID查找债券全部信息（含多语言）
	 * @param id
	 * @return
	 */
	public BondInfoVO findBondFullInfoById(String id);
	
	
}
