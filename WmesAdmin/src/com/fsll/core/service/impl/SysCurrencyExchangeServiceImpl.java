package com.fsll.core.service.impl;

import java.net.URLDecoder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.service.SysCurrencyExchangeService;
import com.fsll.core.service.SysParamService;
import com.fsll.core.vo.SysParamVO;
import com.fsll.wmes.entity.WebExchangeRate;

@Service("sysCurrencyExchangeService")
//@Transactional
public class SysCurrencyExchangeServiceImpl  extends BaseService implements
		SysCurrencyExchangeService {
	
	@Autowired
	private SysParamService sysParamService;
	
	/**
	 * 管理列表数据
	 * @author scshi_u330p
	 * @date 20161101
	 * @param keyword
	 * 
	 * modify by rqwang 20170512
	 * */
	@Override
	public JsonPaging findAll(JsonPaging jsonpaging, String keyWord,String langCode) throws Exception {
		
		StringBuffer sb = new StringBuffer("from WebExchangeRate t where 1=1 ");
		List params = new ArrayList();
		if(null!=keyWord && !keyWord.isEmpty()){
			keyWord = URLDecoder.decode(keyWord, "utf-8");
			sb.append(" and (t.fromCurrency  like ?  or t.toCurrency like ? )");
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		sb.append(" order by t.fromCurrency,t.toCurrency");
		jsonpaging=this.baseDao.selectJsonPaging(sb.toString(), params.toArray(), jsonpaging, false);
		//start 根据基础数据配置获取汇率名
		List<WebExchangeRate> list = jsonpaging.getList();
		for(WebExchangeRate rate:list){
			String fromCode = rate.getFromCurrency();
			String toCode = rate.getToCurrency();
			SysParamVO fromName = sysParamService.findParamByCode(langCode,fromCode);
			SysParamVO  toName = sysParamService.findParamByCode(langCode,toCode);
			
			rate.setFromCurrency(fromName.getName());
			rate.setToCurrency(toName.getName());
		}
		//end
		
		return jsonpaging;
	}
	
	/**
	 * 根据ID获取货币汇率实体
	 * @author scshi_u330p
	 * @date 20161102
	 * */
	public WebExchangeRate findById(String id){
		WebExchangeRate obj = (WebExchangeRate)this.baseDao.get(WebExchangeRate.class, id);
		return obj;
	}
	
	/**
	 * 根据输入code获取已存在的汇率关系
	 * @author scshi_u330p
	 * @date 20161104
	 * @param currencyCode 
	 * @param currencyType 0--查找目标货币，code为原始货币    ; 1-- 查找原始货币,code为目标货币
	 * */
	public List<WebExchangeRate> findExistExchange(String currencyCode,String currencyType){
		StringBuffer sb = new StringBuffer("from WebExchangeRate t where 1=1 ");
		if("0".equals(currencyType)){
			sb.append(" and t.fromCurrency=? ");
		}else{
			sb.append(" and t.toCurrency=? ");
		}
		List<WebExchangeRate> list = this.baseDao.find(sb.toString(), new String[]{currencyCode}, false);
		return list;
	}

	/**
	 * 保存或更新
	 * @param info
	 * @return
	 */
	@Override
	public WebExchangeRate saveOrUpdate(WebExchangeRate info) {
		WebExchangeRate object = (WebExchangeRate)this.baseDao.saveOrUpdate(info);
		return object;
	}


}
