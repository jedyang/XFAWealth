package com.fsll.wmes.rpq.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.RpqPage;
import com.fsll.wmes.entity.RpqPageEn;
import com.fsll.wmes.entity.RpqPageLevel;
import com.fsll.wmes.entity.RpqPageLevelEn;
import com.fsll.wmes.entity.RpqPageLevelSc;
import com.fsll.wmes.entity.RpqPageLevelTc;
import com.fsll.wmes.entity.RpqPageQuest;
import com.fsll.wmes.entity.RpqPageSc;
import com.fsll.wmes.entity.RpqPageTc;
import com.fsll.wmes.entity.RpqPageType;
import com.fsll.wmes.entity.RpqQuest;
import com.fsll.wmes.entity.RpqQuestEn;
import com.fsll.wmes.entity.RpqQuestItem;
import com.fsll.wmes.entity.RpqQuestSc;
import com.fsll.wmes.entity.RpqQuestTc;
import com.fsll.wmes.rpq.vo.RpqPageLangVO;
import com.fsll.wmes.rpq.vo.RpqPageLevelLangVO;
import com.fsll.wmes.rpq.vo.RpqPageView;
import com.fsll.wmes.rpq.vo.RpqQuestItemVO;

public interface RpqManageService {
	
	/***
     * 通过问卷Id查询问卷应用表信息
     * @author Yan
     * @date 2016-11-01
     */
	public RpqPageType findPageTypeByQuestId(String pageId);	
	
	/***
     * 保存问卷应用表
     * @author Yan
     * @date 2016-11-01
     */
	public RpqPageType saveOrUpdateOfRpqPageType(RpqPageType rpqPageType, boolean isAdd);
	
	/***
	 * 问卷列表查询的方法
	 * @author 林文伟
	 * @date 2016-07-19
	 * @param jsonpaging
	 * @param emailLog
	 * @return
	 */
	public JsonPaging findPageAll(JsonPaging jsonpaging, RpqPage list, String pageType, String clientType,String language );
	
	/***
     * 删除问卷下下的所有题目quest
     * @author 林文伟
     * @date 2016-0-20
     */
	public void delPageQuestItemByPageId(String pageId);
	
	/***
     * 设置问卷与题库关联的方法
     * @author 林文伟
     * @date 2016-06-20
     */
	public void setPageQuest(String pageId,List<RpqPageQuest> pageQuestList);
	
	/***
     * 获取问卷下的所有题目
     * @author 林文伟
     * @date 2016-06-20
     */
	public List<RpqQuest> getQuestByPageId(String pageId,String langCode);
	
	/***
     * 通过ID获取问卷信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqPage getPage(String id);
	
	/***
     * 通过ID获取问卷SC信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqPageSc getPageSc(String id) ;
	
	/***
     * 通过ID获取问卷TC信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqPageTc getPageTc(String id) ;
	
	/***
     * 通过ID获取问卷EN信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqPageEn getPageEn(String id) ;
	
	/***
     * 保存
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqPage saveOrUpdate(RpqPage rpqPage,RpqPageLangVO vo, boolean isAdd);
	
	/**************************题库 管理******************************/
	/***
     *题库列表查询的方法
     * @author 林文伟
     * @date 2016-06-29
     */
	public JsonPaging findQuestAll(JsonPaging jsonpaging, RpqQuest info,String langCode);
	
	/***
     * 保存题库的方法
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqQuest save(RpqQuest rpqQuest,RpqQuestSc rpqQuestSc,RpqQuestTc rpqQuestTc,RpqQuestEn rpqQuestEn,List<RpqQuestItemVO> itemList,boolean isAdd);
	
	/***
    * 通过ID获取信息
    * @author 林文伟
    * @date 2016-06-20
    */

	public RpqQuest getQuest(String id);
	
	/***
     * 通过ID获取简体中文信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqQuestSc getQuestSc(String id) ;
	
	/***
     * 通过ID获取繁体中文信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqQuestTc getQuestTc(String id) ;
	
	/***
     * 通过ID获取繁体英文信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqQuestEn getQuestEn(String id) ;
	
	/***
     * 获取题目下的所有选项
     * @author 林文伟
     * @date 2016-0-20
     */
	public List<RpqQuestItem> getRpqQuestItemByQuestId(String questId);
	
	/***
     * 删除quest下的所有item
     * @author 林文伟
     * @date 2016-0-20
     */
	public void delRpqQuestItemByQuestId(String questId);
	
	/***
     * 获取问卷的所有等级数据列表
     * @author 林文伟
     * @date 2016-06-20
     */
	public List<RpqPageLevel> getLevelByPageId(String pageId,String langCode) ;
	
	/***
     * 删除quest
     * @author 林文伟
     * @date 2016-0-20
     */
	public void delRpqQuest(String questId);
	
	/***
     * 删除page
     * @author 林文伟
     * @date 2016-0-20
     */
	public void delRpqPage(String pageId);
	
	/***
     * 保存等级数据
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqPageLevel savePageLevel(RpqPageLevel level,RpqPageLevelLangVO vo ,boolean isAdd);
	
	/***
     * 删除Level
     * @author 林文伟
     * @date 2016-0-20
     */
	public void delRpqPageLevel(String levelId);
	
	/***
     * 通过ID获取等级信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqPageLevel getPageLevel(String id) ;
	
	/***
     * 通过ID获取等级SC信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqPageLevelSc getPageLevelSc(String id);
	
	/***
     * 通过ID获取等级TC信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqPageLevelTc getPageLevelTc(String id);
	
	/***
     * 通过ID获取等级EN信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqPageLevelEn getPageLevelEn(String id);
	
	/***
     * 预览问卷
     * @author 林文伟
     * @date 2016-06-20
     */
	public RpqPageView viewPage(String pageId,String langCode);

	/**
	 * 獲得問卷實體
	 * @author scshi
	 * @date 20170518
	 * */
	public RpqQuest getRpqQuestionById(String id);
	
}


