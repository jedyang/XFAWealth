package com.fsll.wmes.web.action.front;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebWatchlistType;
import com.fsll.wmes.web.service.WebWatchListService;

@Controller
@RequestMapping(value = "/front/web/watchlist")
public class FrontWebWatchListAct extends WmesBaseAct {

	@Autowired
	private WebWatchListService webWatchListService;
	/**
     * 保存watchlist 类型
     * @author mqzou 2017-04-01
     */
    @RequestMapping(value = "/saveWatchType")
	public void saveWatchType(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	HashMap<String, Object> result=new HashMap<String, Object>();
    	try {
    		MemberBase loginUser=getLoginUser(request);
    		String id=request.getParameter("id");
    		String type=request.getParameter("type");
    		WebWatchlistType watchlistType=null;
    		if(null!=id && !"".equals(id)){
    			watchlistType=webWatchListService.findTypeById(id);
    		}
    		if(null==watchlistType){
    			watchlistType=new WebWatchlistType();
    			watchlistType.setCreateTime(DateUtil.getCurDate());
    			watchlistType.setMember(loginUser);
    		}
    		watchlistType.setName(type);
    		watchlistType.setLastUpdate(DateUtil.getCurDate());
    		watchlistType.setIsValid("1");
    		int maxOrder=webWatchListService.getMaxTypeOrderBy(loginUser.getId());
    		watchlistType.setOrderBy(maxOrder+1);
    		watchlistType=webWatchListService.saveType(watchlistType);
    		result.put("result", true);
    		result.put("watchlistType", watchlistType);
		} catch (Exception e) {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
	}
    /**
     * 获取WatchType列表
     * @author mqzou 2017-04-01
     */
    @RequestMapping(value = "/getWatchTypeList")
    public void getWatchTypeList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	MemberBase loginUser=getLoginUser(request);
    	List<WebWatchlistType> list=webWatchListService.findAllType(loginUser.getId());
    	JsonUtil.toWriter(list, response);
    }
    /**
     * 删除WatchType
     * @author mqzou 2017-04-05
     */
    @RequestMapping(value = "/delWatchType")
    public void deleteWatchType(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	HashMap<String, Object> result=new HashMap<String, Object>();
    	String id=request.getParameter("typeId");
    	WebWatchlistType type=webWatchListService.findTypeById(id);
    	if(null!=type){
    		try {
    			webWatchListService.deleteType(type);
    			result.put("result", true);
			} catch (Exception e) {
				result.put("result", false);
			}
    	}
    	JsonUtil.toWriter(result, response);
    }
}
