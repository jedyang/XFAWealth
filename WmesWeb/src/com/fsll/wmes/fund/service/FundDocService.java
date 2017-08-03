/**
 * 
 */
package com.fsll.wmes.fund.service;

import java.util.List;

import com.fsll.wmes.fund.vo.FundDocDataVO;

/**
 * @author scshi
 *基金附件文件接口
 */
public interface FundDocService {
	
	/**3.1.9	获取基金附件文件
	 * @author scshi
	 * @param fundId 资金id
	 * @param langCode 语言
	 * @return	<List>FundDocDataVO	基金附件文件及下载链接信息
	 */
	public List<FundDocDataVO> fundDocFileList(String fundId,String langCode);

}
