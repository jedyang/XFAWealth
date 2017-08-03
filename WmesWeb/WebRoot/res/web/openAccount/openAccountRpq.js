/**
 * openAccountRpq.js RPQ wmes风险问卷
 * @author  赵晓聪
 */
define(function(require) {
	var $ = require('jquery');

	//tab切换
	$(".OpenAccount_tab li").on("click",function(){
	    $(".OpenAccount_tab li a").removeClass("now").eq($(this).index()).addClass("now");
	    $(".rpqContent > div").hide().eq($(this).index()).show();
	});	

// 方法定义 ------------------------------------
	// 重置整张问卷的答题项为未选
	function resetPage(){
		$(".rpqContent").find("input").attr("checked",false);
		$('body,html').animate({scrollTop:0},1000);
	}
// 方法定义 ------------------------------------


// 事件绑定 ------------------------------------
	// 重新修改问卷按钮事件
	$(".rpqSummeryHere").on("click",function(){
		$(".rpqSummary").hide();
		$("#rpa_subbtn").hide();
		$(".rpq_btn").show();
		//resetPage();//清空选项
	});

	// 重置问卷按钮事件
	$(".rpaReset").on("click",function(){
		var indexVal = $(this).attr("index")
		$("#rqpForm_"+indexVal).find("input").attr("checked",false);
		$('body,html').animate({scrollTop:0},1000);
	});		
	
//RPQ更新
	$(".rpaUpdate").on("click",function(){
		var indexVal = $(this).attr("index");
		var valiFlag = true;
		// 1.检查当前是否仍有未回答的题目
		if(!$("#rqpForm_"+indexVal).validationEngine('validate'));
		
		
		var questList ={};
		var totalScore = 0;
		//questionList初始化，算分
		$("#rqpForm_"+indexVal+" input[type='radio']:checked").each(function(index, el) {
			var t = $(this);
			questList[t.attr("name")] = t.val();
			totalScore += parseInt( $(this).attr("scoreValue") );
		});
		var pageId = $("#pageGen_"+indexVal).attr("pageId");
		var examId = $("#examId_"+indexVal).val();
		
//		console.info( JSON.stringify( questList ) );
//		console.info( "** totalScore:"+totalScore );
		if (totalScore>100) totalScore=100;
		
		// 2.查询当前用户得分评级
		$.post(  base_root + "/front/rpq/queryMyRpqLevel.do" , {"pageId": pageId, "score": totalScore }, function(ret){
			//console.info( JSON.stringify( ret ) );
			if( ret.data ){
				var r = ret.data;
				//console.log(r);
				var rlb = $("#rpqSummaryType_"+indexVal);
				var rldesc = $("#rpqRightContent_"+indexVal);

				if (r && r.riskLevel)
					rlb.text(langMutilForJs['open.account.rpq.riskLevel']+":"+(r.riskLevel==null?"":r.riskLevel) + " " + (r.result==null?"":r.result));
				else
					rlb.text(langMutilForJs['open.account.rpq.riskScore']+" "+totalScore);//没有匹配的等级项。
				////console.log(rlb.text());
				
				rldesc.text(r.remark==null?"":r.remark );
				
				//rpaUpdate时保存
				var saveRPQFrom = {};
				saveRPQFrom.accountId = accountId;
				saveRPQFrom.pageId = pageId;
				saveRPQFrom.examId=examId;
				saveRPQFrom.score = totalScore;
				saveRPQFrom.ans = JSON.stringify(questList);
				////console.log("ans=="+JSON.stringify(questList) +'--'+JSON.stringify(questList2));
				// 风险点提示：此处JSON.stringify可能在较低版本ie下有浏览器兼容性问题，建议项目应提供支持浏览器兼容的统一json对象转换字符串的工具函数
				$.post(  base_root + "/front/rpq/updateRPQ.do" , saveRPQFrom , function(ret){
					if(ret.result){
						$("#rpqSummary_"+indexVal).show();
						$("#rpq_btn_"+indexVal).hide();
						$("form[id^='rqpForm_']").each(function(index,el){
							if( $(this).validationEngine('validate') ){
								$(".OpenAccount_tab li a").eq(index).removeClass("openAccount_tab_error");
								return true;//相当于continue	
							}else{
								valiFlag = false;
								$(".OpenAccount_tab li a").eq(index).addClass("openAccount_tab_error");
							}
						})
						
						//console.log("valiFlag="+valiFlag);
						if(valiFlag){
							$("#rpa_subbtn").show();//全部问卷填写完成后显示下一步按钮	
						}else{
							$('body,html').animate({scrollTop:0},1000);//有表单未填写完成，滚动到头部
						}
					}
				});
			}
		});

	})
	
	//  点击下一页按钮
	$("#btn_next").on("click",function(){
		window.location.href = base_root+"/front/investor/accountBasic.do?accountId="+accountId+"&r="+Math.random();
	});
	
	//草稿
	$("#btn_dreft").on("click",function(){
//		$.Tips({ content: "保存成功！"});
		layer.msg(langMutilForJs['global.success.save'],{time:2000});
	});
	
	//取消
	$("#btn_cancle").on("click",function(){
		window.location.href=base_root + "/index.do";
	});
	
});
