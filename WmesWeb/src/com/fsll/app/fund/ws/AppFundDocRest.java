package com.fsll.app.fund.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.fund.service.AppFundDocService;
import com.fsll.app.fund.vo.AppFundDocDataVO;
import com.fsll.common.CommonConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * @author scshi
 *基金附件文件
 */
@RestController
@RequestMapping("/service/fundDoc")
public class AppFundDocRest extends WmesBaseRest{
	@Autowired
	private AppFundDocService fundDocService;
	
	/**
	 * 获取基金附件文件， 通过body传递参数
	 * 调用示例:[地址]/service/fundDoc/getFundDocFileList.r
	 * {"fundId":"1679091C5A880FAF6FB5E6087EB1B2DC","langCode":"sc"}
	 */
	@RequestMapping(value = "/getFundDocFileList")
	@ResponseBody
	public ResultWS getFundDocFileList(HttpServletRequest request,@RequestBody String body){
		
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		result = parseParam(request,jsonObject,result,"list");//参数验证
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		String fundId=jsonObject.getString("fundId");
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		List<AppFundDocDataVO> list = fundDocService.fundDocFileList(fundId,langCode);
		if(list.isEmpty()){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE2001);
			result.setErrorMsg(WSConstants.MSG2001);
			return result;
		}else{
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData(list);
			result.setTotal(list.size());
		}
		return result;
	}
	
	
	/**
	 * 分析参数,必需参数是否为空
	 * @param request
	 * @param jsonObject
	 * @param result
	 * @param oper add mod
	 * @return
	 */
	private ResultWS parseParam(HttpServletRequest request,JSONObject jsonObject,ResultWS result,String oper){
		result.setRet(WSConstants.SUCCESS);//假定通过验证
		if("list".equals(oper)){
			String fundId = jsonObject.getString("fundId")==null?"":jsonObject.getString("fundId");
			if("".equals(fundId)){//参数不能为空
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“fundId”"+WSConstants.MSG1002);
			}
		}
		return result;
	}
}
