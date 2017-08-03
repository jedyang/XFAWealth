/**
 * 
 */
package com.fsll.wmes.web.action.front;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.CoreConstants;
import com.fsll.wmes.base.WmesBaseAct;

/**
 * @author scshi
 *
 */
@Controller
@RequestMapping("/front/site")
public class FrontSiteAct  extends WmesBaseAct{
	
	@RequestMapping(value = "/changLang", method = RequestMethod.GET)
	public void changLang(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
			String langFlag = request.getParameter("langFlag");
			request.getSession().setAttribute(CoreConstants.LANG_CODE,langFlag);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
			JsonUtil.toWriter(obj, response);
	}
}
