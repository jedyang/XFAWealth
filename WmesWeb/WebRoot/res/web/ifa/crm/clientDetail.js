
define(function(require) {

	var $ = require('jquery');
	require("interfaceCheckPwd")
	function windowHeight(){
		var windowHeight = $(window).height();
		windowHeight = windowHeight-100;
		if($('.wmes-content').height() < windowHeight){
				$('.wmes-content').css('min-height',windowHeight);
		};
	};
	var clientDetailTab = sessionStorage.getItem("clientdetailtab");

	$(".tab_list_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".client-detail-contents > div").hide().eq($(this).index()).show();
		sessionStorage.setItem("clientdetailtab",$(this).index());
		windowHeight();
	});
	
//	if(clientDetailTab == 0){
//		$(".tab_list_tab li").eq(0).click();
//	}else if(clientDetailTab == 1){
//		$(".tab_list_tab li").eq(1).click();
//	}else if(clientDetailTab == 2){
//		$(".tab_list_tab li").eq(2).click();
//	}else if(clientDetailTab == 3){
//		$(".tab_list_tab li").eq(3).click();
//	}else if(clientDetailTab == 4){
//		$(".tab_list_tab li").eq(4).click();
//	}else if(clientDetailTab == 5){
//		$(".tab_list_tab li").eq(5).click();
//	}else{
//		$(".tab_list_tab li").eq(0).click();
//	}
	
	switch (clientDetailTab){
  		case 1:
  			$(".tab_list_tab li").eq(1).click();
  		break;
  		case 2:
  			$(".tab_list_tab li").eq(2).click();
  		break;
  		case 3:
  			$(".tab_list_tab li").eq(3).click();
  		break;
  		case 4:
  			$(".tab_list_tab li").eq(4).click();
  		break;
  		case 5:
  			$(".tab_list_tab li").eq(5).click();
  		break;
  		default:
  			$(".tab_list_tab li").eq(0).click();
	}

	var currency_name=$("#currencyName").val();//货币名称
	
	
//	var dateTimeFormat=$("#dateTimeFormat").val();
//	var dateFormat=$("#dateFormat").val();
	getPortfolioData();
	getAccountData();
	getProposalData();
	/*setTimeout(function(){
		getPortfolioData();
	},100)
	
	setTimeout(function(){
		getAccountData();
	},1000)
	setTimeout(function(){
		getProposalData();
	},500)*/
	
	
	
	
	/*
	 * 获取Url传递参数值
	 */
	function getUrlParam(name){  
	    //构造一个含有目标参数的正则表达式对象  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    //匹配目标参数  
	    var r = window.location.search.substr(1).match(reg);  
	    //返回参数值  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
	
//	var Url_type = getUrlParam('type');



	var _distributorId="";
	
	//验证登录
	if(_checkList!=undefined && _checkList!=""){
	$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
	}
	
 	//绑定Account数据
	function getAccountData(){
		var customerMemberId = getUrlParam('customerId');
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/accountListJson.do?datestr='+new Date().getTime(),
			data : {
				'customerMemberId': customerMemberId,'page':1,'rows':100,'sort':'l.distributor.id','order':''
			},
			async: false,
			success : function(json) {
				//var total = json.total;
				//var table = json.rows;
			  //  //console.log("getAccountData");					
				$("#divTabAccount div:gt(1)").empty();
				var divContent = "";
				var list = json.rows;
				
				$.each(list, function (index, array) { //遍历json数据列					
					var distributor_name =array['distributor'];
					var distributorId=array['distributorId'];
					var nextRPQDate =array['nextRPQDate'] == null? "100":array['nextRPQDate'];
					var nextDocCheckDays =array['nextDCDate'] == null? "100":array['nextDCDate'];
					var distributorIconUrl=array['distributorIcon'] == null? "":array['distributorIcon'];
					
					if(_distributorId!=distributorId){
						if(index>0){
							divContent += "</div>"
						}
						divContent += "<div class='client-account-rows'>"
							+ "<div class='client-account-title'>"
							+ "<img class='account-title-dis' src='"+base_root+distributorIconUrl+"' alt=''>"
							+ "<p class='account-title-name'>"+distributor_name+"</p>"
							+ "<span class='client-more-ico'></span>"
							+ "</div>";
						
						divContent += '<p class="client-account-date"><span class="client-account-nexrpq">'
							+langMutilForJs['pipeline.content.kyc.rpqDate']+':  '+nextRPQDate+'&nbsp;'
							+langMutilForJs['account.list.day']+' </span><span class="client-account-checkdate">'
							+langMutilForJs['pipeline.content.kyc.docDate']+':  '+nextDocCheckDays+'&nbsp;'+langMutilForJs['account.list.day']+' </span></p>';
					}
					var account_no = array['accountNo'] == null ? "N/A" : array['accountNo'];
					var acc_type = array['accType'] == null ? "" : array['accType'];
					var cies = array['cies'] == null ? "" : array['cies'];
					var base_currency = array['baseCurrency'] == null ? "" : array['baseCurrency'];
					var baseCurName=array['baseCurName']==null?"":array['baseCurName'];
					var totalAssets= array['totalAssest'] == null ? "0" : array['totalAssest'];
					var accountId=array['id'] == null ? "" : array['id'];
					var openStatus=array['openStatus'] == null ? "" : array['openStatus'];
					var isValid=array['isValid'] == null ? "" : array['isValid'];
					var productValue=array['productValue']==null?"0":array['productValue'];
					var subFlag=array['subFlag']==null?"0":array["subFlag"];
					//alert(acc_type);
					if(acc_type == "J"){
						acc_type = "<li>Joint</li>";
					}else if(acc_type == "I"){
						acc_type = "<li>Individual</li>";
					}else {
						acc_type = "";
					}
					if(subFlag=="0"){
						subFlag="<li>"+langMutilForJs['account.list.masterAccount']+"</li>";
					}else if(subFlag=="1"){
						subFlag="<li>"+langMutilForJs['account.list.subAccount']+"</li>";
					}else{
						subFlag="";
					}
					
					
					if(cies == "CIES"){
						cies = "<li>CIES</li>";
					}else {
						cies = "";
					}
					
					var nameUrl="",name1;
					var rightContent="";
					if(isValid=="1"){
						if(openStatus=="-1"){
							nameUrl="<a href='"+base_root+"/front/investor/accountRPQ.do?id="+accountId+"' target='_self'>";
							name1="<p class='client-account-num'>N/A</p>";
							rightContent='<img class="account-list-old" style="height:50px;width:80px" src="'+base_root+'/res/images/client/reject_ico.png"  >';
						
						}else if(openStatus=="0"){
							nameUrl="<a href='"+base_root+"/front/investor/accountRPQ.do?id="+accountId+"' target='_self'>";
							name1="<p class='client-account-num'>N/A</p>";
							rightContent='<img class="account-list-old"  style="height:50px;width:80px" src="'+base_root+'/res/images/client/draft_ico.png"  >';
							
						}else if(openStatus=="1"){
							nameUrl="<a href='"+base_root+"/front/investor/accountApprove.do?id="+accountId+"' target='_self'>";
							name1="<p class='client-account-num'>N/A</p>";
							rightContent='<img class="account-list-old"  style="height:50px;width:80px" src="'+base_root+'/res/images/client/tobefill_ico.png"  >';
						
						}else if(openStatus=="2"){
							nameUrl="<a href='"+base_root+"/front/investor/accountProgress.do?id="+accountId+"' target='_self'>";
							name1="<p class='client-account-num'>N/A</p>";
							rightContent='<img class="account-list-old"  style="height:50px;width:80px" src="'+base_root+'/res/images/client/processing_ico.png"  >';
						
						}else if(openStatus=="3"){
							nameUrl="<a href='"+base_root+"/front/investor/myAccountDetail.do?id="+accountId+"' target='_self'>";
							name1="<p class='client-account-num'>"+account_no+"</p>";
							rightContent="<div class='client-account-price'>"
								+ "<div class='account-price'>"
								+ "<p class='account-price-title'>"+langMutilForJs['assets.totalAsset']+" </p>"
								+ "<p class='account-price-num'>"+formatMoney(totalAssets,'')+" "+currency_name+"</p>"
								+ "</div>"
								+ "<div class='account-price'>"
								+ "<p class='account-price-title'>"+langMutilForJs['assets.marketValue']+" </p>"
								+ "<p class='account-price-num'>"+formatMoney(productValue,'')+" "+currency_name+"</p>"
								+ "</div>"
								+ "</div>";
						
						}else if(openStatus=="4"){
							nameUrl="<a href='"+base_root+"/front/investor/accountProgress.do?id="+accountId+"' target='_self'>";
							name1="<p class='client-account-num'>N/A</p>";
							rightContent='<img class="account-list-old"  style="height:50px;width:80px" src="'+base_root+'/res/images/client/refused_ico.png"  >';
						}
					}else{
						name1="<p class='client-account-num'>"+account_no+"</p>";
						rightContent='<img class="account-list-old"  style="height:50px;width:80px" src="'+base_root+'/res/images/client/canceled_ico.png"  >';
					}
					
					if(base_currency!=""){
					base_currency="<li>"+baseCurName+"</li>";
			     	}
					                                    
					divContent += nameUrl 
								+ "<div class='client-account-contens client-account-contens-rows'>"
								+ "<div class='client-account-interval'></div>"
								+ "<div class='client-account-left'>"
								+ name1
								+ "<ul class='client-account-describe'>"
								+ subFlag
								+ acc_type
								+ cies
								+ base_currency
								+ "</ul>"
								+ "</div>"
								+ "<div class='client-account-right'>"
								+ rightContent
								+ "</div>"
								+ "</div></a>";

					if(index==list.length){
						divContent += "</div>"
					}
					_distributorId=distributorId;
					
					
                  
              });
              $("#divTabAccount").append(divContent);
				if(list.length==0)
					$(".accountTips").show();
			},
			error:function(data){
				
			}
		})
	}
	
	
 	//绑定Proposal数据
	function getProposalData(){
		var customerMemberId = getUrlParam('customerId');
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/proposalListJson.do?datestr='+new Date().getTime(),
			data : {
				'customerMemberId': customerMemberId,'page':1,'rows':100,'sort':'l.createTime','order':'DESC'
			},
			async: false,
			success : function(json) {
				$("#divTabProposal div:gt(1)").empty();
				var divContent = "";
				var list = json.rows;
								
				$.each(list, function (index, array) { //遍历json数据列					
					var proposal_name = array['proposalName'] == null ? "" : array['proposalName'];
					var base_currency = array['currencyType'] == null ? "" : array['currencyType'];
					var initial_invest_amount = array['initialInvestAmount'] == null ? "" : array['initialInvestAmount'];
					var status = array['status'] == null ? "" : array['status'];
					var id = array['id'] == null ? "" : array['id'];
					
					var url=base_root+'/front/crm/proposal/previewProposal.do?proposalId='+id;
					var pic = "accept";
					if(status == "2"){
						pic = "accepted";
					}else if(status=="0"){
						pic = "draft";
						url=base_root+"/front/crm/proposal/createProposalSetOne.do?id="+id;
					}else if(status == "-2"){
						pic = "canceled";
						url=base_root+"/front/crm/proposal/createProposalSetOne.do?id="+id;
					}
					else if(status == "1"){
						pic = "processing";
					}
					else if(status == "-1"){
						pic = "refused";
						url=base_root+"/front/crm/proposal/createProposalSetOne.do?id="+id;
					}
					var create_time = array['createTimeStr'] == null ? "" : array['createTimeStr'];
					//create_time=formatDate(create_time,dateFormat);
					divContent += '<div class="client-proposal-rows">'
                        		+ '<a href="'+url+'" > <p class="client-proposal-title">'+proposal_name+'</p></a>'
                        		+ '<div class="client-proposal-apr"><img src="'+base_root+'/res/images/client/time_ico.png">'+create_time+'</div>'
                        		+ '<div class="client-proposal-amount">'
                        		+ '<p class="client-proposal-num">'+formatCurrency(initial_invest_amount) + '&nbsp;&nbsp;' + base_currency +'</p>'
                        		+ '<p class="client-proposal-inv">'+langMutilForJs['crm.proposal.initialInvestAmount']+'</p>'
                        		+ '</div>'
                        		+ '<div class="client-proposal-accept">'
                        		+ '<img src="'+base_root+'/res/images/client/'+pic+'_ico_'+lang+'.png"> '
                        		+ '</div>'
                        		+ '</div>';					
                  
				});
				
				$("#divTabProposal").append(divContent);
				if(list.length==0)
					$(".proposalTips").show();
			},
			error:function(data){
				
			}
		})
	}
	
 	//绑定Portfolio数据
	function getPortfolioData(){
		var customerMemberId = getUrlParam('customerId');
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/portfolioListJson.do?datestr='+new Date().getTime(),
			data : {
				'customerMemberId': customerMemberId,'page':1,'rows':100,'sort':'l.createTime','order':'DESC'
			},
			async: false,
			success : function(json) {
				// //console.log("getPortfolioData");
				$("#divTabPortfolio div:gt(1)").empty();
				var divContent = "";
				var list = json.rows;
				////console.log(json.rows);
				var currency=$("#currency").val();
								
				$.each(list, function (index, array) { //遍历json数据列					
					var portfolio_name = array['portfolioName'] == null ? "" :array['portfolioName'];
//					var create_time =array['createTime'] == null ? "" : array['createTime'];
					var return_rate = array['totalReturnRate'] == null ? "" :array['totalReturnRate'];
					var return_value = array['totalReturnValue'] == null ? "" :array['totalReturnValue'];
					var total_amount = array['totalAssets'] == null ? "0" :array['totalAssets'];
					var id= array['portfolioId'] == null ? "" :array['portfolioId'];
//					var riskLevel= array['riskLevel'] == null ? "" :array['riskLevel'];
					var totalAssetsRate=array['assetRate'] == null ? "0" :array['assetRate'];
//					var return_rate_per;
//					var toReview = "";
					//暂定回报率小于-20%就出预警
					if(parseFloat(return_rate) != NaN && parseFloat(return_rate) != 'NaN' ){
						if(parseFloat(return_rate) < -0.2){
//							toReview = "<span>To review</span>";
						}
						if(return_rate==""){
							return_rate="0";
						}
//						return_rate_per = parseFloat(return_rate)*100 + "%";
					}
					
					if(parseFloat(return_rate)!=0){
						return_rate=formatCurrency(return_rate*100)+"%";
					}else{
						return_rate="--";
					}
					
					if(parseFloat(return_value)!=0){
						return_value=formatCurrency(return_value);
					}else{
						return_value="--";
					}
					
					if(parseFloat(total_amount)!=0){
						total_amount=formatCurrency(total_amount);
					}else{
						total_amount="--";
					}
					
					var flag = "rise";
					if(parseFloat(total_amount) != NaN && parseFloat(total_amount) >= 0){
						flag = "rise";
					}else{
						flag = "drop";
					}
					
					divContent+=' <li class="portfolios_list_blue">'
	                           +'<ul class="portfolios_list_content">'
	                           +'<li>'
	                           +' <div class="c100 p'+totalAssetsRate+'" >'
	                           +' <span style="color:#000">'+totalAssetsRate+'%</span>'
	                           +' <div class="slice">'
	                           +'<div class="bar" ></div>'
	                           +'<div class="fill"></div>'
	                           +'</div>'
	                           +'</div>'
	                           +'</li>'
	                           +'<li>'
							   +'<a href="'+base_root+'/front/strategy/info/conservativePortfolio.do?id='+id+'"><p class="my-assets-littletitle">'+portfolio_name+'</p></a>'
							   +'<p class="my-assets-word">'
							   +'<span class="funds_leveal_5"></span>'
							   +'</p>'
							   +'</li>'
	                           +'<li>'
	                           +' <p class="my-assets-title">'+langMutilForJs['assets.totalAsset']+'<span class="my-assets-cur">('+currency+')</span></p>'
	                           +'<p class="my-assets-word">'
	                           +'<span class="eve-hide" >'+total_amount+'</span>'
	                           +'</p>'
	                           +'</li>'
	                           +'<li>'
	                           +'<p class="my-assets-title">'+langMutilForJs['assets.totalReturn']+'<span class="my-assets-cur">('+currency+')</span></p>'
	                           +'<p class="my-assets-word">'
	                           +'<span class="eve-hide client-portfolio-'+flag+'" >'+return_value+'</span>'
	                           +'</p>'
	                           +'</li>'
	                           +' <li>'
	                           +'<p class="my-assets-title">'+langMutilForJs['assets.totalReturn.rate']+'</p>'
	                           +'<p class="my-assets-word"><span class="eve-hide client-portfolio-'+flag+'" >'+return_rate+'</span> </p>'
	                           +'</li>'
	                           /*+'<li>'
	                           +' <p class="my-assets-state">Draft</p>'
	                           +'</li>'*/
	                           +'</ul>'
	                           +'</li>';
									
                                    
								
                  
				});
				$(".portfolios_list").append(divContent);
				//$("#divTabPortfolio").append(divContent);
				if(list.length==0)
					$(".portfolioTips").show();
				
			},
			error:function(data){
				//console.l
			}
		})
	}
	
	getOrderData();
 	//绑定Order数据
	function getOrderData(){
		var customerMemberId = getUrlParam('customerId');
		var status = $("#hdStatus").val();
		var period = $("#hdPeriod").val();
		
		$.ajax({
			type : 'post',
			datatype : 'json',
			 async: false,
			url : base_root+'/front/crm/pipeline/orderListJson.do?datestr='+new Date().getTime(),
			data : {
				'customerMemberId': customerMemberId,'period':period,'beginDate':'','endDate':'','status':status,'page':1,'rows':100,'sort':'','order':''
			},
			success : function(json) {
				// //console.log("getOrderData");	
				$(".client-table tbody tr:gt(0)").empty();
				var divContent = "";
				var list = json.rows;
								
				$.each(list, function (index, array) { //遍历json数据列					
					var product_name = array['productName'] == null ? "" : array['productName'];
					var account_no = array['accountNo'] == null ? "" : array['accountNo'];
					
					var transaction_unit = array['transactionUnit'] == null ? "" : array['transactionUnit'];
					var transaction_amount = array['transactionAmount'] == null ? "" : array['transactionAmount'];
					
					var fee = array['fee'] == null ? "N/A" : array['fee'];
					var order_type = array['orderType'] == null ? "" : array['orderType'];
					var order_date = array['orderDate'] == null ? "" : array['orderDate'];
					var status = array['status'] == null ? "" : array['status'];
					var action = ''; 						
                    if('Wait' == status) {
                    	action = '<img src="'+base_root+'/res/images/client/close_ico.png">';	
                    }               
					divContent += '<tr>'
								+ '<td style="text-align:left;">'+order_date+'</td>'
								+ '<td style="text-align:left;">'+product_name+'</td>'
								+ '<td style="text-align:left;"">'+transaction_unit+'</td>'
								+ '<td style="text-align:left;">'+transaction_amount+'  HKD</td>'
								+ '<td style="text-align:left;"">'+fee+'</td>'
								+ '<td style="text-align:left;">'+order_type+'</td>'
								+ '<td style="text-align:left;">'+account_no+'</td>'
								+ '<td style="text-align:left;"">'+status+'</td>'
								+ '<td style="text-align:left;">'+action+'</td>'
								+ '</tr>';					
					//alert(divContent);	
				});
				
				$(".client-table tbody").append(divContent);
				if(list.length==0){
					$(".transactionTip").show();
				}else{
					$(".transactionTip").hide();
				}
			},
			error:function(data){
				////console.log(data);
			}
		})
	}
	
	$('.client-choice-active').addClass('client-choice-active2');
	var listTime;
	$(".client-choice li").on("click",function(){
		clearTimeout(listTime);
		$(this).siblings().removeClass("client-choice-active").end().addClass("client-choice-active");
		var dataName = $(this).attr("data-name");
		var dataValue = $(this).attr("data-value");
		if("period" == dataName){
//			document.getElementById("hdPeriod").value=dataValue;
			$("#hdPeriod").val(dataValue);
		}
		if("status" == dataName){
//			document.getElementById("hdStatus").value=dataValue;
			$("#hdStatus").val(dataValue);
		}
		
		//var status = $("#hdStatus").val();
		//var period = $("#hdPeriod").val();
		
//		alert("period:"+period+",status:"+status);

		// 解决重复请求的问题
//		var self = this;
			listTime=setTimeout(function(){
				getOrderData();
			}
			,100);
		if($(this).closest('ul').find('.client-choice-active').index()==0){
			$(this).addClass('client-choice-active2');
		}else{
			$(this).closest('ul').find('li').removeClass('client-choice-active2');
		};
	});
	
	/*$("body").on("click",".client-top-portrait",function(){
		var url = base_root+'/front/member/personal/changeIcon.do?flag=crm&flagId=';
		var id = $('#hdId').val();
		//alert(id);
		if( id != null ){
			window.location.href = url + id;
		}

	});*/
	
	$("#addAccount").on("click",function(){
		var investorId = getUrlParam('customerMemberId');
		var url = base_root + "/front/investor/accountStart.do?indId="+investorId;
		window.location.href=url;
	});
	
	/*$('.client-top-name-amind').on('click',function(){
		$('.client-top-name-amind').hide();
		$('.client-top-name1').css('display','block');
		$('.client-top-name').css('display','none');
		$('.client-top-name1').focus();
	});*/
	var nickName="";
	$('.client-top-name').focus(function(){
		if(clientType=='1')
			return;
		this.select();
		nickName=$(this).val();
	});
	$('.client-top-name').blur(function(){
		if(clientType=='1')
			return;
		//$('.client-top-name').text($('.client-top-name1').val());
		$('.client-top-name-amind').show();
		if($(this).val()!=nickName){
			saveNickName();
		}
		
	});
	
	
	
	function saveNickName(){
		var id=$("#id").val();
		var name=$('.client-top-name').val();
		$.ajax({
			type:"post",
			datatype:"json",
			url:base_root+"/front/crm/pipeline/saveNickName.do",
			data:{id:id,nickName:name},
			success:function(json){
			    if(json.result){
			    	layer.msg(langMutilForJs['global.success.edit'],{time:1000});
			    	$('.client-top-name').text($('.client-top-name1').val());
			    }else{
			    	layer.msg(langMutilForJs['global.failed.edit'],{time:1000});
			    	$('.client-top-name1').val($('.client-top-name').text());
			    }	
			    $('.client-top-name').css('display','block');
				$('.client-top-name1').css('display','none');
			}
		})
		
	}
	
	$(".client-more-ico").on("click",function(){
		$(this).toggleClass("client-more-icoactive");
		$(this).closest('.client-account-rows').children().eq(1).toggleClass('client-more-ico-hidden');;
	});
	
	$(document).on("click",".client-more-ico",function(){
		$(this).closest('.client-account-rows').toggleClass('account-rows-hide');
	});
	
	//金额格式化
	function formatCurrency(num) {    
	    num = num.toString().replace(/\$|\,/g,'');    
	    if(isNaN(num))    
	    num = "";    
	    var sign = (num == (num = Math.abs(num)));    
	    num = Math.floor(num*100+0.50000000001);    
	    var cents = num%100;    
	    num = Math.floor(num/100).toString();    
	    if(cents<10)    
	    cents = "0" + cents;    
	    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
	    num = num.substring(0,num.length-(4*i+3))+','+    
	    num.substring(num.length-(4*i+3));    
	    return (((sign)?'':'-') + num + '.' + cents);    
	} 

});