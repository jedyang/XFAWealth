package com.fsll.wmes.bond.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.bond.service.BondInfoService;
import com.fsll.wmes.bond.vo.BondInfoVO;
import com.fsll.wmes.bond.vo.BondProductVO;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondInfoSc;
import com.fsll.wmes.entity.BondInfoTc;

/**
 * 债券信息查询服务接口实现
 * @author Yan
 * @date 2016-12-09
 */
@Service("bondInfoService")
public class BondInfoServiceImpl extends BaseService implements BondInfoService {

	/**
	 * 通过ID查找一条债券基本信息
	 * @param id
	 * @return
	 */
	public BondInfo findBondInfoById(String id){
		BondInfo info = (BondInfo) baseDao.get(BondInfo.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条债券附加英文信息
	 * @param id
	 * @return
	 */
	public BondInfoEn findBondInfoEnById(String id){
		BondInfoEn info = (BondInfoEn) baseDao.get(BondInfoEn.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条债券附加简体中文信息
	 * @param id
	 * @return
	 */
	public BondInfoSc findBondInfoScById(String id){
		BondInfoSc info = (BondInfoSc) baseDao.get(BondInfoSc.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条债券附加繁体中文信息
	 * @param id
	 * @return
	 */
	public BondInfoTc findBondInfoTcById(String id){
		BondInfoTc info = (BondInfoTc) baseDao.get(BondInfoTc.class, id);
		return info;
	}
	
	/**
	 * 获取债券基础数据列表
	 * @return
	 */
	@Override
	public List<BondProductVO> getBondProductVoList(String langCode) {
		List<BondProductVO> voList = new ArrayList<BondProductVO>();
//		List params = new ArrayList();
		String hql = " FROM BondInfo r "
			+ " INNER JOIN " + this.getLangString("BondInfo", langCode)+" i ON r.id=i.id ";
		List<Object> list = this.baseDao.find(hql, null, false);
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objRead = (Object[])list.get(i);
				BondInfo info = (BondInfo)objRead[0];
				BondProductVO vo = new BondProductVO();
				vo.setId(info.getId());
				if(!"".equals(info.getProduct()) && null!=info.getProduct()){
					vo.setProductId(info.getProduct().getId());
				}
				if("sc".equals(langCode)){
					BondInfoSc sc = (BondInfoSc)objRead[1];
					vo.setName(sc.getBondName());
				} else if("tc".equals(langCode)){
					BondInfoTc tc = (BondInfoTc)objRead[1];
					vo.setName(tc.getBondName());
				} else if("en".equals(langCode)){
					BondInfoEn en = (BondInfoEn)objRead[1];
					vo.setName(en.getBondName());
				}
				voList.add(vo);
			}
		}
		return voList;
	}

	/**
	 * 通过ID查找债券全部信息
	 * @param id
	 * @param langCode 语言
	 * @return
	 */
	public BondInfoVO findBondFullInfoById(String id, String langCode){
		BondInfoVO result = findBondFullInfoById(id);
		result.setDefaultInfoByLang(langCode);
		
		return result;
	}
	
	/**
	 * 通过ID查找债券全部信息（含多语言）
	 * @param id
	 * @return
	 */
	public BondInfoVO findBondFullInfoById(String id){
		BondInfoVO result = new BondInfoVO();
		BondInfo info = findBondInfoById(id);
		if(info != null){
			result.setBondInfo(info);
			result.setBondInfoEn(findBondInfoEnById(id));
			result.setBondInfoSc(findBondInfoScById(id));
			result.setBondInfoTc(findBondInfoTcById(id));
			if(info.getProduct() != null){
				result.setProductId(info.getProduct().getId());
			}
		}
		return result;
	}
	
}
