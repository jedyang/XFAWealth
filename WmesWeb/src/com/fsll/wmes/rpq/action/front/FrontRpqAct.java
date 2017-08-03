package com.fsll.wmes.rpq.action.front;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.UploadUtil;
import com.fsll.core.WSConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.RpqExamAnswer;
import com.fsll.wmes.entity.RpqExamEn;
import com.fsll.wmes.entity.RpqExamQuest;
import com.fsll.wmes.entity.RpqExamQuestEn;
import com.fsll.wmes.entity.RpqExamQuestItem;
import com.fsll.wmes.entity.RpqExamQuestItemEn;
import com.fsll.wmes.entity.RpqExamQuestItemSc;
import com.fsll.wmes.entity.RpqExamQuestItemTc;
import com.fsll.wmes.entity.RpqExamQuestSc;
import com.fsll.wmes.entity.RpqExamQuestTc;
import com.fsll.wmes.entity.RpqExamSc;
import com.fsll.wmes.entity.RpqExamTc;
import com.fsll.wmes.entity.RpqPage;
import com.fsll.wmes.entity.RpqPageEn;
import com.fsll.wmes.entity.RpqPageLevel;
import com.fsll.wmes.entity.RpqPageSc;
import com.fsll.wmes.entity.RpqPageTc;
import com.fsll.wmes.entity.RpqQuest;
import com.fsll.wmes.entity.RpqQuestItem;
import com.fsll.wmes.entity.RpqQuestItemEn;
import com.fsll.wmes.entity.RpqQuestItemSc;
import com.fsll.wmes.entity.RpqQuestItemTc;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.rpq.service.RpqService;
import com.fsll.wmes.rpq.vo.FillRPQPaperVO;
import com.fsll.wmes.rpq.vo.RpqListVO;
import com.lowagie.text.pdf.BaseFont;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
@RequestMapping("/front/rpq")
public class FrontRpqAct extends WmesBaseAct{
	
	@Autowired
	private RpqService rpqService;
	
	@Autowired
	private InvestorService investorService;
	
	@Autowired
	private MemberBaseService memberBaseService;

	/**
	 * step 2 - 根据传入的问卷id及得分查询 回答的风险等级
	 * @author tejay zhu
	 * @param request
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/queryMyRpqLevel", method = RequestMethod.POST)
	public void queryMyRpqLevel(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String pageId = request.getParameter("pageId");
		int score = Integer.parseInt( request.getParameter("score") );

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("ret", WSConstants.SUCCESS);
		obj.put("data", rpqService.findRpqPageLevel(pageId, score, "using", "1",this.getLoginLangFlag(request)) );
		JsonUtil.toWriter(obj, response);
	}	
	
	/**
	 * step2 - save
	 * @author tejay zhu
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/saveRPQ", method = RequestMethod.POST)
	public void saveRPQ(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		
//		System.out.println( "** 进入 saveRPQ" );
		
		if (loginUser!=null){
			try {
				String accountId = request.getParameter("accountId");
				String pageId = request.getParameter("pageId");
				int score = Integer.parseInt( request.getParameter("score") );
				InvestorAccount investorAccount = rpqService.getInvestorAccount(accountId);
				// 转换用户答题情况
				String userAnswer = request.getParameter("ans");
				JSONObject jsonObject = JSONObject.fromObject(userAnswer);
				Iterator<String> iterator = jsonObject.keys();
				String jsonKey = null;
				String jsonValue = null;
				Map<String, String> ansMap = new HashMap<String, String>();
				while (iterator.hasNext()) {
					jsonKey = iterator.next();
					jsonValue = (String)jsonObject.get(jsonKey);
					ansMap.put( jsonKey , jsonValue);
				}
				
				
				//  保存RPQ信息
				// 1.保存前的材料准备，查询
				// 1.1 获取问卷
				RpqPage rpqPage = rpqService.getRpqPage(pageId);
				// 1.2 查询风险评级
				RpqPageLevel rpqPageLevel = rpqService.findRpqPageLevel(pageId, score, "using", "1",this.getLoginLangFlag(request) );
				
				List<RpqPageLevel> levelSc = investorService.findRpqPageLevel(pageId, score, "using", "1","sc");
				List<RpqPageLevel> levelTc = investorService.findRpqPageLevel(pageId, score, "using", "1","tc");
				List<RpqPageLevel> levelen = investorService.findRpqPageLevel(pageId, score, "using", "1","en");
				
				// 1.3 查出问卷中的问题及问题对应的选项
				List<RpqListVO> list = rpqService.findRpqQuestList("1", investorAccount.getDistributor().getId(), "Individual", this.getLoginLangFlag(request), "O");
				
				// 2.1 保存RpqExam
				RpqExam rpqExam = new RpqExam();
				rpqExam.setId(null);
				rpqExam.setMember(loginUser);
				rpqExam.setType( rpqPage.getType() );
				rpqExam.setFromMember( rpqPage.getMember() );
				rpqExam.setDistributor( rpqPage.getDistributor() );
				rpqExam.setTotalScore( score );
				rpqExam.setRiskLevel( rpqPageLevel.getRiskLevel() );
				rpqExam.setStatus( "waiting_approve" );
				rpqExam.setIsValid("1");
				rpqExam.setPageId(pageId);
				RpqExam rpqExamPersistent = rpqService.saveOrUpdateRpqExam(rpqExam);
				//investorAccount.setRpqExam(rpqExamPersistent);//用户关联问卷
//				System.out.println( "** 新建保存的 rpqExamPr getId:"+rpqExamPersistent.getId() );
				
				//英文版保存exam保存
				RpqExamEn examEn = new RpqExamEn();
				RpqPageEn pageEn = rpqService.getRpqPageEn(pageId);
				examEn.setId(rpqExamPersistent.getId());
				examEn.setTitle(pageEn.getTitle());
				examEn.setRemark(pageEn.getRemark());
				examEn.setRiskResult(levelen.get(0).getResult());
				rpqService.saveOrUpdateExamEn(examEn);
				
				//繁體版exam保存
				RpqExamTc examTc = new RpqExamTc();
				RpqPageTc pageTc = rpqService.getRpqPageTc(pageId);
				examTc.setId(rpqExamPersistent.getId());
				examTc.setTitle(pageTc.getTitle());
				examTc.setRemark(pageTc.getRemark());
				examTc.setRiskResult(levelTc.get(0).getResult());
				//RpqExamTc exam_tcEntity = 
					rpqService.saveOrUpdateExamTc(examTc);
				
				//簡體保存
				RpqExamSc examSc = new RpqExamSc();
				RpqPageSc pageSc = rpqService.getRpqPageSc(pageId);
				examSc.setId(rpqExamPersistent.getId());
				examSc.setTitle(pageSc.getTitle());
				examSc.setRemark(pageSc.getRemark());
				examSc.setRiskResult(levelSc.get(0).getResult());
				//RpqExamSc exam_scEntity = 
					rpqService.saveOrUpdateExamSc(examSc);
				
				
				// 2.2 一对多保存
				// 2.2.1 保存RpqExamQuest
				for (int i = 0; i < list.size(); i++) {
					// TODO 风险点提示： 此代码块应增加判断，避免出错，当前未有时间处理 -------------------------
					String currentQuestAns = ansMap.get( list.get(i).getQuestId() );	// 对于本问题，用户的选择项
					RpqQuestItem rpqQuestItem = rpqService.getRpqQuestItem( currentQuestAns );
					int currentQuestAnsScore = rpqQuestItem.getScoreValue();	// 对于本问题，用户的得分值
					
//					System.out.println( "** 问题的id:"+list.get(i).getQuestId() );
//					System.out.println( "** 选项的id："+currentQuestAns + " <> currentQuestAnsScore:"+currentQuestAnsScore );
					// 此代码块应增加判断，避免出错，当前未有时间处理 -------------------------
					
					RpqExamQuest rpqExamQuest = new RpqExamQuest();
					rpqExamQuest.setId(null);
					rpqExamQuest.setExam(rpqExamPersistent);
					rpqExamQuest.setScoreValue( currentQuestAnsScore );	// 此题的得分，应该通过一个方法实现查询
//					rpqExamQuest.setTitle( list.get(i).getQuestTitle() );
//					rpqExamQuest.setRemark( list.get(i).getQuestRemark() );
					rpqExamQuest.setOrderBy( list.get(i).getQuestOrder() );
					RpqExamQuest rpqExamQuestPersistent = rpqService.saveOrUpdateRpqExamQuest(rpqExamQuest);
					
					//簡體保存rpqExamQuest
					RpqExamQuestSc examQuestionSc  =  new RpqExamQuestSc();
					RpqQuest rpqQuestionSc = rpqService.loadRpqQuestionById( list.get(i).getQuestId(), "sc");
					examQuestionSc.setId(rpqExamQuestPersistent.getId());
					examQuestionSc.setTitle(rpqQuestionSc.getTitle());
					examQuestionSc.setRemark(rpqQuestionSc.getRemark());
					//RpqExamQuestSc examQuestion_scEntity = 
						rpqService.saveOrUpdateRpqExamQuestSc(examQuestionSc);
					//繁體rqpExamQuestion 
					RpqExamQuestTc examQuestionTc = new RpqExamQuestTc();
					RpqQuest rpqQuestionTc= rpqService.loadRpqQuestionById( list.get(i).getQuestId(), "tc");
					examQuestionTc.setId(rpqExamQuestPersistent.getId());
					examQuestionTc.setTitle(rpqQuestionTc.getTitle());
					examQuestionTc.setRemark(rpqQuestionTc.getRemark());
					//RpqExamQuestTc examQuestion_tcEntity = 
						rpqService.saveOrUpdateRpqExamQuestTc(examQuestionTc);
					//英文rqpExamQuestion保存
					RpqExamQuestEn examQuestionEn = new RpqExamQuestEn();
					RpqQuest rpqQuestionEn = rpqService.loadRpqQuestionById(list.get(i).getQuestId(), "en");
					examQuestionEn.setId(rpqExamQuestPersistent.getId());
					examQuestionEn.setTitle(rpqQuestionEn.getTitle());
					examQuestionEn.setRemark(rpqQuestionEn.getRemark());
					//RpqExamQuestEn examQuestion_enEntity = 
						rpqService.saveOrUpdateExamQuestSc(examQuestionEn);
					
					
					// 2.2.2 保存RpqExamQuestItem
					RpqExamQuestItem itemForUserSelected = null;
					List<RpqQuestItem> rpqQuestItemList = list.get(i).getRpqQuestItemList();
					for (int itemIndex = 0; itemIndex < rpqQuestItemList.size(); itemIndex++) {
						RpqQuestItem item = rpqQuestItemList.get(itemIndex);
						RpqExamQuestItem examQuestItem = new RpqExamQuestItem();
						examQuestItem.setId(null);
						examQuestItem.setQuest(rpqExamQuestPersistent);
						examQuestItem.setType( item.getType() );
						//examQuestItem.setTitle( item.getTitle() );
						examQuestItem.setScoreValue( item.getScoreValue() );
						examQuestItem.setOrderBy( item.getOrderBy() );
						//examQuestItem.setRemark( item.getRemark() );
						RpqExamQuestItem examQuestItemPersistent = rpqService.saveOrUpdateRpqExamQuestItem(examQuestItem);
						// 若当前选项为用户的选择
						if( item.getId().equalsIgnoreCase( currentQuestAns ) ){
							itemForUserSelected = examQuestItemPersistent;
						}
						
						//英文選項保存
						RpqQuestItemEn questionItemEn = rpqService.getRpqQuestItemEn(item.getId());
						RpqExamQuestItemEn examQuestItemEn = new RpqExamQuestItemEn();
						examQuestItemEn.setId(examQuestItemPersistent.getId());
						examQuestItemEn.setTitle(questionItemEn.getTitle());
						examQuestItemEn.setRemark(questionItemEn.getRemark());
						//RpqExamQuestItemEn examQuestItem_enEntity = 
							rpqService.saveOrUpdateRpqExamQuestItemEn(examQuestItemEn);
						
						//繁體中文
						RpqQuestItemTc questionItemTc = rpqService.getRpqQuestItemTc(item.getId());
						RpqExamQuestItemTc examQuestItemTc = new RpqExamQuestItemTc();
						examQuestItemTc.setId(examQuestItemPersistent.getId());
						examQuestItemTc.setTitle(questionItemTc.getTitle());
						examQuestItemTc.setRemark(questionItemTc.getRemark());
						//RpqExamQuestItemTc questionItemTcEntity = 
							rpqService.saveOrUpdateRpqExamQuestItemTc(examQuestItemTc);
						
						
						//簡體保存
						RpqQuestItemSc questionItemSc = rpqService.getRpqQuestItemSc(item.getId());
						RpqExamQuestItemSc examQuestItemSc = new RpqExamQuestItemSc();
						examQuestItemSc.setId(examQuestItemPersistent.getId());
						examQuestItemSc.setRemark(questionItemSc.getRemark());
						examQuestItemSc.setTitle(questionItemSc.getTitle());
						//RpqExamQuestItemSc examQuestItem_scEntity = 
							rpqService.saveOrUpdateRpqExamQuestItemSc(examQuestItemSc);
					}
					
					// 2.2.3 保存RpqExamAnswer
					RpqExamAnswer examAnswer = new RpqExamAnswer();
					examAnswer.setId(null);
					examAnswer.setExam( rpqExamPersistent );
					examAnswer.setQuest( rpqExamQuestPersistent );
					examAnswer.setItem( itemForUserSelected );
					//RpqExamAnswer examAnswerPersistent = 
						rpqService.saveOrUpdateRpqExamAnswer(examAnswer);
				}
				
				result.put("result",true);
				result.put("accountId",accountId);
				result.put("code","global.success.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success.save",null));
				
			} catch (Exception e) {
				e.printStackTrace();
				result.put("result",false);
				result.put("code","global.failed.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed.save",null));
			}
		}else{//未登录，跳转到登录页面
			result.put("result",true);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		JsonUtil.toWriter(result, response);
	}
	
	
	
	
	//RPQ问卷更新
	
	@RequestMapping(value = "/updateRPQ", method = RequestMethod.POST)
	public void updateRPQ(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
//		System.out.println( "** 进入updateRPQ" );
		
		if (loginUser!=null){
			String accountId = request.getParameter("accountId");
			String examId = request.getParameter("examId");
			String pageId = request.getParameter("pageId");
			int score = Integer.parseInt( request.getParameter("score") );
			// 转换用户答题情况
			String userAnswer = request.getParameter("ans");
			userAnswer = StringEscapeUtils.unescapeHtml(userAnswer);
			JSONObject jsonObject = JSONObject.fromObject(userAnswer);
			Iterator<String> iterator = jsonObject.keys();
			String jsonKey = null;//问题id
			String jsonValue = null;//用户选择选项id
			Map<String, String> ansMap = new HashMap<String, String>();
			while (iterator.hasNext()) {
				jsonKey = iterator.next();
				jsonValue = (String)jsonObject.get(jsonKey);
				ansMap.put( jsonKey , jsonValue);
			}
			
			//更新问卷，评分，等级
			// 1.2 查询风险评级
			RpqPageLevel rpqPageLevel = rpqService.findRpqPageLevel(pageId, score, "using", "1",this.getLoginLangFlag(request) );
			RpqPageLevel levelSc = rpqService.findRpqPageLevel(pageId, score, "using", "1","sc" );
			RpqPageLevel levelTc = rpqService.findRpqPageLevel(pageId, score, "using", "1","tc" );
			RpqPageLevel levelen = rpqService.findRpqPageLevel(pageId, score, "using", "1","en" );
			
			RpqExam exam = rpqService.getRpqExam(examId);
			exam.setLastUpdate(new Date());
			exam.setRiskLevel(rpqPageLevel.getRiskLevel());
			exam.setTotalScore(score);
			rpqService.saveOrUpdateRpqExam(exam);
			rpqService.saveOrUpdateRpqExamLanguage(levelSc,levelTc,levelen,examId);//多语言评级结果保存
			
			
			//更新答案选项
			List<RpqListVO> examList = rpqService.findExamList(examId,this.getLoginLangFlag(request));
			if(!examList.isEmpty()){
				for(int x=0;x<examList.size();x++){
					String questionId = examList.get(x).getQuestId();
					String currentQuestAns = ansMap.get(questionId);	// 对于本问题，用户的选择项，返回examItemiD
					RpqExamQuestItem rpqExamItem = rpqService.getRpqExamQuestItem( currentQuestAns );
					int currentQuestAnsScore = rpqExamItem.getScoreValue();	// 对于本问题，用户的得分值
					
					//获取问题，更新分值
					RpqExamQuest examQuestion = rpqService.getRpqExamQuest(examList.get(x).getQuestId());
					examQuestion.setScoreValue(currentQuestAnsScore);
					rpqService.saveOrUpdateRpqExamQuest(examQuestion);
					
					//更新问题对应答案
					RpqExamAnswer examAns =  rpqService.getRpqExamAns(examId,questionId);
					examAns.setItem(rpqExamItem);
					rpqService.saveOrUpdateRpqExamAnswer(examAns);
					
					result.put("result",true);
					result.put("accountId",accountId);
					result.put("code","global.success.save");
					result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success.save",null));
				}
			}
			
		}else{
			result.put("result",true);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * step1 填写问卷
	 * @author qgfeng  modify by wwluo 20170420
	 */
	@RequestMapping(value = "/fillRpqPaper", method = RequestMethod.GET)
	public String fillRpqPaper(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String distributorId = request.getParameter("distributorId");
		RpqExam  rpqExam = rpqService.saveExamFromPage(distributorId,"Individual", "K", request);
		RpqListVO rpqExamVO = null;
		if(rpqExam != null){
			rpqExamVO = rpqService.getRpqExamVoById(rpqExam.getId(), langCode);
		}
		FillRPQPaperVO rpqPaper = new FillRPQPaperVO();
		rpqPaper.setMemberId(loginUser.getId());
		String mobilePhone = loginUser.getMobileNumber();
		if (StringUtils.isNotBlank(loginUser.getMobileCode())) {
			mobilePhone = loginUser.getMobileCode() + " " + mobilePhone;
		}
		rpqPaper.setMobilePhone(mobilePhone);
		rpqPaper.setEmail(loginUser.getEmail());
		rpqPaper.setName(getCommonMemberName(loginUser.getId(), langCode, "2"));
		rpqPaper.setIconUrl(PageHelper.getUserHeadUrl(loginUser.getIconUrl(), null));
		model.put("rpqExam", rpqExamVO);
		model.put("rpqPaper", rpqPaper);
		return "rpq/fillRpqPaper";	
	}
	/**
	 * step1.1 取消rpq
	 * @author qgfeng
	 */
	@RequestMapping(value = "/cancelFill", method = RequestMethod.POST)
	public void cancelFill(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String examId = request.getParameter("examId");
			RpqExam rpqExam = rpqService.getRpqExam(examId);
			rpqExam.setStatus("-2");
			rpqService.saveOrUpdateRpqExam(rpqExam);
			result.put("result",true);
			result.put("code","global.cancel");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
	    }catch (Exception e) {
			e.printStackTrace();
			result.put("result",false);
			result.put("code","global.cancel");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
	    }finally{
		  JsonUtil.toWriter(result, response);
	  }
	}
	
	/**
	 * step1.1 提交问卷保存答案
	 * @author qgfeng
	 */
	@RequestMapping(value = "/saveAnswer", method = RequestMethod.POST)
	public void saveAnswer(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		//String pageId = request.getParameter("pageId");
		if (loginUser != null){
			try {
				String answerJson = request.getParameter("answerJson");
				String examId = request.getParameter("examId");
				answerJson = StringEscapeUtils.unescapeHtml(answerJson);
				JSONArray jArry = JSONArray.fromObject(answerJson);
				if(jArry!=null && !jArry.isEmpty()){
					for(int i=0;i<jArry.size();i++){
						JSONObject obj = (JSONObject) jArry.get(i);
						String questId = (String) obj.get("questId");
						String answerId = (String) obj.get("answerId");
						String answerValue = (String) obj.get("answerValue");
						RpqExamQuestItem item = new RpqExamQuestItem();
						item.setId(answerId);
						//是否是答卷的更改
						RpqExamAnswer answer = rpqService.getRpqExamAns(examId, questId);
						answer.setItem(item);
						answer.setItemValue(answerValue);
						rpqService.saveOrUpdateRpqExamAnswer(answer);
					}
					RpqExam exam = rpqService.getRpqExam(examId);
					if(null!=exam){
						rpqService.updateStatus(loginUser.getId(), exam.getModuleType(), examId);
					}
					result.put("result",true);
				}else{
					result.put("result",false);
				}
				
		  }catch (Exception e) {
			e.printStackTrace();
			result.put("result",false);
			result.put("code","global.failed.save");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed.save",null));
		  }finally{
			  JsonUtil.toWriter(result, response);
		  }
		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
			JsonUtil.toWriter(result, response);
		}
	}
	
	/**
	 * step2确认问卷结果
	 * @author qgfeng
	 */
	@RequestMapping(value = "/confirmRpqPaper", method = RequestMethod.POST)
	public String confirmRpqPaper(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		FillRPQPaperVO rpqPaper = new FillRPQPaperVO();
		rpqPaper.setMemberId(loginUser.getId());
		String mobilePhone = loginUser.getMobileNumber();
		if (StringUtils.isNotBlank(loginUser.getMobileCode())) {
			mobilePhone = loginUser.getMobileCode() + " " + mobilePhone;
		}
		rpqPaper.setMobilePhone(mobilePhone);
		rpqPaper.setEmail(loginUser.getEmail());
		rpqPaper.setName(getCommonMemberName(loginUser.getId(), langCode, "2"));
		rpqPaper.setIconUrl(PageHelper.getUserHeadUrl(loginUser.getIconUrl(),""));
		model.put("rpqPaper", rpqPaper);
		String pageId = request.getParameter("pageId");
		String examId = request.getParameter("examId");
		int score = Integer.parseInt( request.getParameter("score"));
		RpqPageLevel rpqLevel = rpqService.findRpqPageLevel(pageId, score, "using", "1",this.getLoginLangFlag(request));
		List<RpqPageLevel> lessRpqLevels = rpqService.findRpqPageLevelList(pageId, "using", "1",rpqLevel.getRiskLevel(),this.getLoginLangFlag(request));
		model.put("rpqLevel", rpqLevel);
		model.put("lessRpqLevels", lessRpqLevels);
		model.put("score", score);
		model.put("examId",examId);
		model.put("pageId",pageId);
		return "rpq/confirmRpqPaper";	
	}
	
	/**
	 * step3 完成问卷
	 * @author qgfeng
	 */
	@RequestMapping(value = "/completeRpqPaper", method = RequestMethod.POST)
	public String completeRpqPaper(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String langCode = this.getLoginLangFlag(request);
		MemberBase loginUser = getLoginUser(request);
		String userLevelId = request.getParameter("userLevelId");
		String levelId = request.getParameter("levelId");
		String examId = request.getParameter("examId");
		int score = Integer.parseInt(request.getParameter("score"));
		String pageId = request.getParameter("pageId");
		String lang = getLoginLangFlag(request);
		FillRPQPaperVO rpqPaper = new FillRPQPaperVO();
		rpqPaper.setMemberId(loginUser.getId());
		String mobilePhone = loginUser.getMobileNumber();
		if (StringUtils.isNotBlank(loginUser.getMobileCode())) {
			mobilePhone = loginUser.getMobileCode() + " " + mobilePhone;
		}
		rpqPaper.setMobilePhone(mobilePhone);
		rpqPaper.setEmail(loginUser.getEmail());
		rpqPaper.setName(getCommonMemberName(loginUser.getId(), langCode, "2"));
		rpqPaper.setIconUrl(PageHelper.getUserHeadUrl(loginUser.getIconUrl(), null));
		model.put("rpqPaper", rpqPaper);
		rpqService.saveCompleteExam(levelId, userLevelId, examId, score);
		RpqExam exam = rpqService.getRpqExamRiskLevel(examId,lang);
		exam.setStatus("1");
		exam.setLastUpdate(new Date());
		exam = rpqService.saveOrUpdateRpqExam(exam);
		String dateFormat = CommonConstants.FORMAT_DATE_TIME;
		if (StringUtils.isNotBlank(loginUser.getDateFormat())) {
			dateFormat = loginUser.getDateFormat();
		}
		exam.setCreateTimeStr(DateUtil.dateToDateString(exam.getCreateTime(), dateFormat));
		exam.setExpireDateStr(DateUtil.dateToDateString(exam.getExpireDate(), dateFormat));
		model.put("exam",exam);
		String accountId = exam.getRelateId();
		Integer riskLevel = null;
		if (exam.getUserRiskLevel() != null) {
			riskLevel = exam.getUserRiskLevel();
		}else{
			riskLevel = exam.getRiskLevel();
		}
		rpqService.updateAccountRpqLevel(accountId, riskLevel);
		//展示答卷
		RpqListVO  rpqExam = rpqService.getRpqExamVoById(examId, lang);
		model.put("rpqExam", rpqExam);
		//展示确认卷
		RpqPageLevel rpqLevel = rpqService.findRpqPageLevel(pageId, score, "using", "1",this.getLoginLangFlag(request));
		List<RpqPageLevel> lessRpqLevels = rpqService.findRpqPageLevelList(pageId, "using", "1",rpqLevel.getRiskLevel(),this.getLoginLangFlag(request));
		model.put("rpqLevel", rpqLevel);
		model.put("lessRpqLevels", lessRpqLevels);
		model.put("userLevelId", userLevelId);
		model.put("levelId", levelId);
		return "rpq/completeRpqPaper";	
	}
	
	/**
	 * rpq结果详情
	 * @author qgfeng
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		if (loginUser!=null){
			String examId = request.getParameter("examId");
			RpqListVO  rpqExam = rpqService.getRpqExamVoById(examId, getLoginLangFlag(request));
			
			RpqPageLevel rpqLevel = rpqService.findRpqPageLevel(rpqExam.getRpqPageid(), rpqExam.getTotalScore(), "using", "1",this.getLoginLangFlag(request));
			List<RpqPageLevel> lessRpqLevels = rpqService.findRpqPageLevelByScore(rpqExam.getRpqPageid(), rpqExam.getTotalScore(), "using", "1",rpqLevel.getRiskLevel(),this.getLoginLangFlag(request));
			model.put("rpqExam", rpqExam);
			model.put("rpqLevel", rpqLevel);
			model.put("lessRpqLevels", lessRpqLevels);
			return "rpq/detail";	
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	
	/**
	 * 生成 PDF
	 * @author qgfeng  modify by wwluo 20170420
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/generatePDF", method = RequestMethod.POST)
	public void generatePDF(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		try {
			String langCode = this.getLoginLangFlag(request); 
			MemberBase loginUser = this.getLoginUser(request);
			String examId = request.getParameter("examId");
			RpqListVO  rpqExam = rpqService.getRpqExamVoById(examId, getLoginLangFlag(request));
			RpqPageLevel rpqLevel = rpqService.findRpqPageLevel(rpqExam.getRpqPageid(), rpqExam.getTotalScore(), "using", "1",this.getLoginLangFlag(request));
			//List<RpqPageLevel> lessRpqLevels = rpqService.findRpqPageLevelByScore(rpqExam.getRpqPageid(), rpqExam.getTotalScore(), "using", "1",rpqLevel.getRiskLevel(),this.getLoginLangFlag(request));
			List<RpqPageLevel> lessRpqLevels = rpqService.findRpqPageLevelList(rpqExam.getRpqPageid(), "using", "1",rpqLevel.getRiskLevel(),this.getLoginLangFlag(request));
			// 获取Servlet容器对象实际路径，用于放置生成的pdf
	    	String ctxPath = request.getSession().getServletContext().getRealPath("/");
	    	String originalFilename = UUID.randomUUID().toString().replace("-", "")+".pdf";
	    	HashMap<Long,String> moduleRelate = new HashMap<Long,String>();
	    	//moduleRelate.put( 1l , "openAccountDoc");
	    	moduleRelate.put( 1l , "investor_doc");
			String filePath = UploadUtil.getFileName(originalFilename,true,moduleRelate.get(1l));
			String filename = ctxPath + filePath;
			
			File localFile = new File(filename);
			if(!localFile.getParentFile().exists()){
				localFile.getParentFile().mkdirs();
			}
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("rpqExam", rpqExam);
			map.put("rpqLevel", rpqLevel);
			map.put("lessRpqLevels", lessRpqLevels);
			
			map.put("resPath", "file:/"+request.getRealPath("/"));
			map.put("filePath", request.getContextPath() + filePath);
			
			freemarker.template.Configuration configuration = new Configuration();
			configuration.setServletContextForTemplateLoading( ContextLoader.getCurrentWebApplicationContext().getServletContext() , "/WEB-INF/web/rpq/pdfTemplate");
			String fileName = "rpqResult" + langCode.substring(0, 1).toUpperCase() + langCode.substring(1, langCode.length()) + ".html";
			Template template = configuration.getTemplate(fileName,"UTF-8");
			StringWriter stringWriter = new StringWriter();
			BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
			
			template.setEncoding("UTF-8");
			template.setOutputEncoding("UTF-8");
			template.process(map, bufferedWriter);
			String htmlBody = stringWriter.toString();
			bufferedWriter.flush();
			bufferedWriter.close();
			
			String outputFile = filename;
			
	        OutputStream os = new FileOutputStream(outputFile);  
	        ITextRenderer renderer = new ITextRenderer();  
	        ITextFontResolver fontResolver = renderer.getFontResolver();  
			//宋体
			fontResolver.addFont(ctxPath+"res/fonts/simsunb.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			//微软雅黑
			fontResolver.addFont(ctxPath+"res/fonts/msyh.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
	        StringBuffer html = new StringBuffer();  
	        // DOCTYPE 必需写否则类似于 这样的字符解析会出现错误  
	        html.append("<!DOCTYPE html>");  
	        html.append("<html lang=\"en\">").append("<head>")  
	            .append("<meta charset=\"utf-8\"></meta>")  
	            .append( "<style type=\"text/css\" mce_bogus=\"1\">body {font-family:Microsoft YaHei,SimSunb;font-size: 16px;}</style>" )
	            .append("</head>")  
	            .append("<body>");  
	        html.append( htmlBody );  
	        html.append("</body></html>");  
	        renderer.setDocumentFromString(html.toString());  
	        renderer.layout();  
	        renderer.createPDF(os);
	        os.flush();
	        os.close();
	        
	        String downName = this.getProperty(langCode, "kyc.rpq.paper.show.rpq.result.title");
	        downName += "_" + getCommonMemberName(loginUser.getId(), langCode, "2") + ".pdf";
	        
	        //文件下载
	        java.io.BufferedInputStream bis = null;
			java.io.BufferedOutputStream bos = null;
	        try {
				long fileLength = localFile.length();
				response.setContentType("application/x-msdownload;");
				response.setHeader("Content-disposition", "attachment; filename="+ new String(downName.getBytes("utf-8"), "ISO8859-1"));
				response.setHeader("Content-Length", String.valueOf(fileLength));
				bis = new BufferedInputStream(new FileInputStream(filename));
				bos = new BufferedOutputStream(response.getOutputStream());
				byte[] buff = new byte[2048];
				int bytesRead;
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
				if (localFile.isFile() && localFile.exists()) {  
					localFile.delete();  
				}  
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
