
define(function(require) {

	var $ = require('jquery'),
	angular = require('angular');
	require("fullcalendar");//日程控件
	require("fullcalendarlang");

	
	//验证登录
	if(_checkList!=undefined && _checkList!=""){
	$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
	}
	
	$(".tab_list_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".client-detail-contents > div").hide().eq($(this).index()).show();
		
		if($(this).hasClass('tab-schdule')){
			$('.fc-today-button').trigger('click');
		}
	});
	
	$(".administration-client-tab p").on("click",function(){
		$(this).siblings().removeClass("client-now").end().addClass("client-now");
		$(this).parent().siblings(".administration-client-wrap").children().hide().eq($(this).index()).show();
	});

	$(".client-more-ico").on("click",function(){
		$(this).parents(".client-account-rows").toggleClass("client-more-ico-active");
	});

	$(".funds_choice li").on("click",function(){
		if($(this).parent().hasClass("funds_logo_b")){
			return;
		}
		var selection_criterialenght = $(".selection_criteria li").length;
		for(var i = 0; i < selection_criterialenght ;i++){
				if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
					$(".selection_criteria li").eq(i).remove();
				}
		}
		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
		if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
			$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
		}
	});
		
	// 数据控制
	var mybase = angular.module('ifaTable', ['truncate','wmespage']);
	

	
    mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
    	   $scope.prospectList='';
    	   $scope.existingList='';
    	   $scope.proposalList='';
    	   $scope.accountList='';
    	   $scope.portfolioList='';
    	   $scope.insightList='';
    	   $scope.fundList='';
    	   $scope.strategyList='';
    	   $scope.rePofolioList='';

    	   $scope.currency=$("#currency").val();
    	   $scope.defColor=$("#defColor").val();
    	   $scope.dateFormat=$("#dateFormat").val();
    	   $scope.dateTimeFormat=$scope.dateFormat+" HH:mm";
	    	// 数字调用
	    	$scope.counter = Array;
	    	
	    	//监听视图渲染是否结束
	    	$scope.checkLast = function($last){
				if($last){

					//删除渲染效果
					$(".animated").one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
				    		$(".animated").removeClass();
				    });

					//you can also do something 

				}
			}
	    	
//	    	 var page_bol=false;
		     var rows=10;
		     
	    	bindProspect();
	    	function bindProspect(page){
//	    		var type=$(".administration-client-tab").find(".client-now").attr("data-value");
	    		var keyWord=$("#clientSearch").val();
	    		
	    		$http({
                    url: base_root+"/front/ifa/info/ifaClient.do",
                    method: 'POST',
                    data : "memberId="+memberId+"&type=0&keyWord="+keyWord+"&rows="+rows+"&page="+page,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
               	 })
	    			.success(function(response){
	    				
	    				if(response.list.length > 0){
	    					$(".prospectTips").hide();
	    					$scope.prospect_page.totalItems = response.total;//设置总数
	             		 	$scope.prospectList = '';
	                        $scope.prospectList =  response.list;
	                 	}else{//无返回数据时
	                 		$scope.prospectList = '';
	                 	}
	    				if(response.total==0 && page==1 ){
	    					 $(".prospectTips").show();
	    				}
	    			}).then(function(data){
	    				bindExisting();
	    			})
	    	}
	    	
	    	$scope.prospect_page = {
	                itemsPerPage:rows,//每条多少数据
	                onChange: function(){
	                	bindProspect($scope.prospect_page.currentPage);
	            	}
	            };

	    	     $(document).on("click",".prospect_page li",function(){
	    	    	 var page=$(this).attr("data-page");
	    	    	 bindProspect(page);
	    	     })	
	    	
	     function	bindExisting(page){
//	    		var type=$(".administration-client-tab").find(".client-now").attr("data-value");
	    		var keyWord=$("#clientSearch").val();
	    		
	    		$http({
                    url: base_root+"/front/ifa/info/ifaClient.do",
                    method: 'POST',
                    data : "memberId="+memberId+"&type=1&keyWord="+keyWord+"&rows="+rows+"&page="+page,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
               	 })
	    			.success(function(response){
	    				
	    				if(response.list.length > 0){
	    					$(".existingTips").hide();
	    					$scope.existing_page.totalItems = response.total;//设置总数
	             		 	$scope.existingList = '';
	                        $scope.existingList =  response.list;
	                 	}else{//无返回数据时
	                 		$scope.existingList = '';
	                 	}
	    				if(response.total==0 && page==1 ){
	    					 $(".existingTips").show();
	    				}
	    			}).then(function(data){
	    				bindProposal(1);
	    			})
	    	 }
	    	     $scope.existing_page = {
	 	                itemsPerPage:rows,//每条多少数据
	 	                onChange: function(){
	 	                	bindExisting($scope.existing_page.currentPage);
	 	            	}
	 	            };

	 	    	     $(document).on("click",".existing_page li",function(){
	 	    	    	 var page=$(this).attr("data-page");
	 	    	    	bindExisting(page);
	 	    	     })	
	    
	     
	     
	     function bindProposal(IPAGE){
	    	 var keyWord=$("#proposalKey").val();
	    	 var order_ =$("#hidProposalOrder").val();
	    	 var sort_ =$("#hidProposalSort").val();
	    	 $http({
                 url: base_root+"/front/ifa/info/ifaProposal.do",
                 method: 'POST',
                 data : "memberId="+memberId+"&keyWord="+keyWord+"&rows="+rows+"&page="+IPAGE+"&order="+order_+"&sort="+sort_,
                 headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
            	 })
	    			.success(function(response){
	    				if(response.list.length > 0){
	    					$(".proposalTips").hide();
	    					$scope.proposal_page.totalItems = response.total;//设置总数
	             		 	$scope.proposalList = '';
	                        $scope.proposalList =  response.list;
	                 	}else{//无返回数据时
	                 		$scope.proposalList = '';
	                 	}
	    				if(response.total==0 && IPAGE==1 ){
	    					 $(".proposalTips").show();
	    				}
	          }).then(function(data){
	        	  bindAccountList(1);
  			});
	     }
	    $scope.proposal_page = {
            itemsPerPage:rows,//每条多少数据
            onChange: function(){
            	bindProposal($scope.proposal_page.currentPage);
        	}
        };

	     $(document).on("click",".proposal_page li",function(){
	    	 var page=$(this).attr("data-page");
	    	 bindProposal(page);
	     })
	     
	     
	 	
	     function	bindAccountList(page){
	    		var keyWord=$("#accountKey").val();
	    		var distributorId=$(".distributor_choice").find(".fund_choice_active").attr("data-value");
	    		
	    		$http({
                    url: base_root+"/front/ifa/info/ifaClientAccount.do",
                    method: 'POST',
                    data : "memberId="+memberId+"&distributorId="+distributorId+"&keyWord="+keyWord+"&rows="+rows+"&page="+page,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
               	 })
	    			.success(function(response){
	    				
	    				if(response.list.length > 0){
	    					$(".accountTips").hide();
	    					$scope.account_page.totalItems = response.total;//设置总数
	             		 	$scope.accountList = '';
	                        $scope.accountList =  response.list;
	                 	}else{//无返回数据时
	                 		$scope.accountList = '';
	                 	}
	    				if(response.total==0 && page==1 ){
	    					 $(".accountTips").show();
	    				}
	    				
	    				 /*$scope.accountList=response;
	    				 if($scope.accountList.length==0){
	    					 $(".accountTips").show();
	    				 }*/
	    			}).then(function(data){
	    				bindPortfolio(1);
	      			});
	    	}
	     
	     $(document).on("click",".distributor_choice li",function(){
	    	 bindAccountList(1); 
	     });
	     $scope.account_page = {
	             itemsPerPage:rows,//每条多少数据
	             onChange: function(){
	             	bindProposal($scope.account_page.currentPage);
	         	}
	         };

	 	     $(document).on("click",".account_page li",function(){
	 	    	 var page=$(this).attr("data-page");
	 	    	 bindProposal(page);
	 	     })
	     
	     
	     function bindPortfolio(IPAGE){
	    	 var keyWord=$("#portfolioKey").val();
	    	 $http({
                 url: base_root+"/front/ifa/info/ifaClientPortfolio.do",
                 method: 'POST',
                 data : "memberId="+memberId+"&keyWord="+keyWord+"&rows="+rows+"&page="+IPAGE,
                 headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
            	 })
	    			.success(function(response){
	    				$(".portfolioTips").hide();
	    				if(response.list.length > 0){
	             		 	 $scope.portfolioList = '';
	                        $scope.portfolioList =  response.list;
	                        $scope.portfolioPage.totalItems = response.total;//设置总数
	                        
	                 	}else{//无返回数据时
	                 		$scope.portfolioList = '';
	                 	}
	    				if(response.total==0 && IPAGE==1){
	    					$(".portfolioTips").show();
	    				}
	          }).then(function(data){
	        	  bindInsight(1);
    			});
	     }
	     $scope.portfolioPage = {
	             itemsPerPage:rows,//每条多少数据
	             onChange: function(){
	            	 bindPortfolio($scope.portfolioPage.currentPage);
	         	}
	         };

	     $(document).on("click",".portfolio_page li",function(){
	    	 var page=$(this).attr("data-page");
	    	 bindPortfolio(page);
	     })
	     
	     
	    
	     function bindInsight(IPAGE){
	    	 var keyWord=$("#insightKey").val();
	    	 var chanel=$(".chanel_choice").find(".fund_choice_active").attr("data-value");
	    	 $http({
                 url: base_root+"/front/ifa/info/ifaInsight.do",
                 method: 'POST',
                 data : "memberId="+memberId+"&keyWord="+keyWord+"&rows="+rows+"&page="+IPAGE+"&chanel="+chanel,
                 headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
            	 })
	    			.success(function(response){
	    				if(response.list.length > 0){
	    					$scope.insightConf.totalItems = response.total;//设置总数
	             		 	$scope.insightList = '';
	                        $scope.insightList =  response.list;
	                 	}else{//无返回数据时
	                 		$scope.insightList = '';
	                 	}
	    				if(response.total==0){
	    					$(".insightTips").show();
	    				}
	          }).then(function(data){
	        	  bindStrategyList(1);
  			});
	     }
	    $scope.insightConf = {
            itemsPerPage:rows,
            onChange: function(){
            	bindInsight($scope.insightConf.currentPage);
        	}
        };
	     $(document).on("click",".insight_page li",function(){
	    	 var page=$(this).attr("data-page");
	    	 bindInsight(page);
	     })
	     
	     
	     
	     
	     function bindStrategyList(IPAGE){
	    	 var keyWord=$("#re_strategyKey").val();
	    	 $http({
                 url: base_root+"/front/ifa/space/viewStrategiesListJson.do?",
                 method: 'POST',
                 data : "id="+memberId+"&keyWord="+keyWord+"&rows="+rows+"&page="+IPAGE,
                 headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
            	 })
    			.success(function(response){
    				if(response.list.length > 0){
    					$scope.strategyListConf.totalItems = response.total;//设置总数
             		 	$scope.strategyList = '';
                        $scope.strategyList =  response.list;
                 	}else{//无返回数据时
                 		$scope.strategyList = '';
                 	}
    				if(response.total==0){
    					$(".restrategyTips").show();
    				}
    				
	          }).then(function(data){
	        	  bindRePortfolioList(1);
	  			});
	     }
	    $scope.strategyListConf = {
            itemsPerPage:rows,
            onChange: function(){
            	bindStrategyList($scope.strategyListConf.currentPage);
        	}
        };
	     $(document).on("click",".strategy_page li",function(){
	    	 var page=$(this).attr("data-page");
	    	 bindStrategyList(page);
	     })
	     
	     
	    
	     function bindRePortfolioList(IPAGE){
	    	 var keyWord=$("#rePortfolioKey").val();
	    	 $http({
                 url: base_root+"/front/ifa/space/viewPofolioListJson.do?",
                 method: 'POST',
                 data : "id="+memberId+"&keyWord="+keyWord+"&rows="+rows+"&page="+IPAGE,
                 headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
            	 })
	    			.success(function(response){
	    				if(response.list.length > 0){
	    					$scope.rePofolioListConf.totalItems = response.total;//设置总数
	             		 	 $scope.rePofolioList = '';
	                        $scope.rePofolioList =  response.list;
	                 	}else{//无返回数据时
	                 		$scope.rePofolioList = '';
	                 	}
	    				if(response.total==0){
	    					$(".reportfolioTips").show();
	    				}
	    				
	          }).then(function(data){
	        	  bindFundList(1);
	  			});;
	     }
	    $scope.rePofolioListConf = {
            itemsPerPage:rows,
            onChange: function(){
            	bindRePortfolioList($scope.rePofolioListConf.currentPage);
        	}
        };
	     $(document).on("click",".re_portfolio_page li",function(){
	    	 var page=$(this).attr("data-page");
	    	 bindRePortfolioList(page);
	     })
	     
	     
	     
	    
	     function bindFundList(IPAGE){
	    	 var keyWord=$("#portfolioKey").val();
	    	 $http({
                 url: base_root+"/front/ifa/info/ifaRecomentFund.do",
                 method: 'POST',
                 data : "memberId="+memberId+"&keyWord="+keyWord+"&rows="+rows+"&page="+IPAGE,
                 headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
            	 })
	    			.success(function(response){
	    				if(response.list.length > 0){
	    					$scope.fundListConf.totalItems = response.total;//设置总数
	             		 	 $scope.fundList = '';
	                        $scope.fundList =  response.list;
	                 	}else{//无返回数据时
	                 		$scope.fundList = '';
	                 	}
	    				if(response.total==0){
	    					$(".refundTips").show();
	    				}
	          })
	     }
	     $scope.fundListConf = {
            itemsPerPage:rows,
            onChange: function(){
            	bindFundList($scope.fundListConf.currentPage);
        	}
        };
	     $(document).on("click",".fund_page li",function(){
	    	 var page=$(this).attr("data-page");
	    	 bindFundList(page);
	     })
	     
	     
	     $("#clientSearch").keyup(function(event){
	       	 if(event.keyCode == 13){
	       		 if($(".client-now").attr("data-value")=="0"){
	       			bindProspect();
	       		 }
	       		 else{
	       			bindExisting();
	       		 }
		        }
	       	 
	     });
	     $("#proposalKey").keyup(function(event){
	       	 if(event.keyCode == 13){
	       		bindProposal(1);
		        }
	       	 
	     });
	     $("#portfolioKey").keyup(function(event){
	       	 if(event.keyCode == 13){
	       		bindPortfolio(1);
		        }
	     });
	     $("#accountKey").keyup(function(event){
	       	 if(event.keyCode == 13){
	       		 bindAccountList()
		        }
	       	 
	     });
	     $("#re_strategyKey").keyup(function(event){
	       	 if(event.keyCode == 13){
	       		 bindStrategyList(1);
		        }
	       	 
	     });
	     $("#insightKey").keyup(function(event){
	       	 if(event.keyCode == 13){
	       		 bindInsight(1);
		        }
	       	 
	     });
	     $("#rePortfolioKey").keyup(function(event){
	       	 if(event.keyCode == 13){
	       		 bindRePortfolioList(1);
		        }
	       	 
	     });
	     
	     $(document).on('click','.sort-click',function(){
	    	 if($(this).hasClass('sort-initAmount')){
	    		 $("#hidProposalSort").val('r.initialInvestAmount');
	    	 }else if($(this).hasClass('sort-lastUpdate')){
	    		 $("#hidProposalSort").val('r.lastUpdate');
	    	 }
	    	 if($(this).find('.down_active').length > 0){
	 			$('.arrow_top').removeClass('top_active')
	 			$('.arrow_down').removeClass('down_active')
	 			$(this).find('.arrow_top').addClass('top_active');
	 			$("#hidProposalOrder").val('asc');
	 			bindProposal(1);
	 		}else{
	 			$('.arrow_top').removeClass('top_active')
	 			$('.arrow_down').removeClass('down_active')
	 			$(this).find('.arrow_down').addClass('down_active');
	 			$("#hidProposalOrder").val('desc');
	 			bindProposal(1);
	 		}
	 	});   
	    	
    }]);
	
	/*******************schdule start**********************/
	//组装日历视图数据
	function initFullcalendar() {
		var initLocale
		if('tc'==lang){
			initLocale = 'zh-tw';
		}else if('sc'==lang){
			initLocale = 'zh-cn';;
		}
		$('.ifa-home-calendar').fullCalendar({
			header : {
				left : 'prev,next today',
				center : 'title',
				right : 'month,agendaWeek,agendaDay'
			},
			year: new Date().getFullYear(),   
        	month: new Date().getMonth(),   
       		day: new Date().getDate(),   
       		defaultView: 'month',
       		/*theme: true,  //调用jQueryUI样式
       		buttonIcons: 
			{
				prev: 'glyphicon glyphicon-chevron-left',
				next: 'glyphicon glyphicon-chevron-right'
			},*/
       		locale: lang,
			navLinks : true, 
			editable : true,
			eventLimit : true, 
			//events : TaskListEventData.scheduleEvents,  //配置事件源
			events : [],  //配置事件源
			//编辑事件
			eventClick: function(calEvent, jsEvent, view) {
				clearForm('.register_table');
				$('.regiter_xiala_ul').hide();
				$('#add_Schedule').show();
				$('#add_Schedule .space-mask').css('margin-top',$(window).scrollTop());
				//$('.space-mask-investors').css('top',$(document).height()*0.2+$(window).scrollTop());
				$('.schedule-dialog-creator').show();
				$('#schedule-creator-text').text(calEvent.creatorName);
				if(calEvent.id != null && calEvent.id != ''){
					$('#add_schedule_delete').show();
				}
				$('#hidScheduleId').val(calEvent.id);
				$('#txtTitle').val(calEvent.title);
				if(calEvent.eventType=='1'){
					$("#cheRecurrentFlag").prop("checked",true);
					$('.repeat_typecon').show();
					if(calEvent.repeatFrom!=''){
						$('#txtStartAt').val(calEvent.repeatFrom.replace('T',' '));
					}
					if(calEvent.repeatTo!=''){
						$('#txtEndAt').val(calEvent.repeatTo.replace('T',' '));
					}
				}else{
					$("#cheRecurrentFlag").prop("checked",false);
					$('.repeat_typecon').hide();
					$('#txtStartAt').val((calEvent.start._d.toISOString().replace('T',' ').split('.'))[0]);
					if(calEvent.end!=null){
						$('#txtEndAt').val((calEvent.end._d.toISOString().replace('T',' ').split('.'))[0]);
					}else{
						$('#txtEndAt').val((calEvent.start._d.toISOString().replace('T',' ').split('.'))[0]);
					}
				}
				if(calEvent.ifImportant=='1'){
					$("#cheImportantFlag").prop("checked",true);
				}else{
					$("#cheImportantFlag").prop("checked",false);
				}
				$('#txtCustomer').val(calEvent.customerName);
				$('#txtCustomer').attr('data-value',calEvent.customerId);
				$('#txtGroup').val(calEvent.groupName);
				$('#txtGroup').attr('data-value',calEvent.groupId);
				$('#txtRemind').val(calEvent.remindSettingName);
				$('#txtRemind').attr('data-value',calEvent.remindSetting);
				$('#txtContent').val(calEvent.content);
				if(calEvent.repeatDay != ''){
					for ( var int = 0; int < calEvent.repeatDay.length; int++) {
						if(calEvent.repeatDay.charAt(int) == '1'){
							$("#repeatDay-"+int).prop("checked",true);
						}
					}
				}	
				// 字数控制
				titleLength();
				contentLength();
				// 初始化时间
				var initStartTime;
				var initEndTime = '';
				if(calEvent.start != null){
					initStartTime = calEvent.start._d.toISOString();
				}
				if(calEvent.end != null){
					initEndTime = calEvent.end._d.toISOString();
				}
				//initDate(initStartTime,initEndTime);
            },
			selectable: false
		});
	}
	initFullcalendar();
	$('#add_schedule_cancel').click(function(){$('#add_Schedule').hide();});
	$('#add_Schedule-close').click(function(){$('#add_Schedule').hide();});
	
	// 获取客户
	function getCustomers(){
		$.ajax({
			type:'post',
			url: base_root+'/front/crm/schedule/getCustomers.do?dateStr=' + new Date().getTime(),
			success:function(result){
				if(null != result || typeof(result)!='undefined'){
					var obj = eval('('+result.customersJson+')');
					var html = '';
					$.each(obj,function(index,n){
						var nickName = n.nickName == null?'':n.nickName;
						html += '<li value='+n.id+'>'+nickName+'</li>';
					});
					$('#selCustomer').empty().append(html);
					$('.regiter_xiala_ul >li').unbind('click');
					$('.regiter_xiala_ul >li').click(function(){
						$(this).closest('div').find('.value_show').val($(this).text());
						$(this).closest('div').find('.value_show').attr('data-value',$(this).attr('value'));
						$(this).parent().hide();
					});
				}
			}
		});
	}
	getCustomers();
	
	// 字体长度控制
	function titleLength(){
		var inpLen = $('#txtTitle').val().length;
		if(inpLen>150){
			layer.msg(prop['schedule.create.schedule.control.title']);
			$('#title-inputed').text(150);
			$('#title-left').text(0);
			$('#txtTitle').val($('#txtTitle').val().substring(0,150));
		}else{
			$('#title-inputed').text(inpLen);
			$('#title-left').text(150-inpLen);
		}
	}
	// 字体长度控制
	function contentLength(){
		var inpLen = $('#txtContent').val().length;
		if(inpLen>500){
			layer.msg(prop['schedule.create.schedule.control.contant']);
			$('#content-inputed').text(500);
			$('#content-left').text(0);
			$('#txtContent').val($('#txtContent').val().substring(0,500));
		}else{
			$('#content-inputed').text(inpLen);
			$('#content-left').text(500-inpLen);
		}
	}
	function clearForm(objE){
        $(objE).find(':input').each(  
            function(){  
	            switch(this.type){  
	                case 'passsword':  
	                case 'select-multiple':  
	                case 'select-one':  
	                case 'text':  
	                case 'textarea':  
	                    $(this).val('');  
	                    break;  
	                case 'checkbox':  
	                case 'radio':  
	                    this.checked = false;  
	            }  
            }     
        );  
    }
	//获取日程数据
	function bindSchedule(){
		var groupId = '',
			order = 'DESC',
			sort = '',
//			ifaMemberId = getUrlParam('memberId'),//ifa
		 	eventType = '';
		$.ajax({
			type:'post',
			async:false,
			data:
			{
				sort:sort,
				teamIfa:'1',
				order:order,
				groupId:groupId,
				eventType:eventType
			},
			url: base_root+'/front/crm/schedule/getCRMSchedule.do?dateStr=' + new Date().getTime(),
			success:function(result){
				if(result.crmScheduleJson!=null){
					var events = eval('('+result.crmScheduleJson+')');
					var scheduleEvents = [];
					//初始化日程数据源
					$.each(events,function(index,n){
						scheduleEvents.push({ 
							dataType: 'S',//代表日程数据 
					 		id: n.id, 
					 		color: n.color == null?'':n.color, 
					 		content: n.content == null?'':n.content, 
				            title: n.title == null?'':n.title,   
				            start: n.start == null?'':n.start, 
		            		create: n.createTime == null?'':n.createTime, 
				            repeatTo: n.repeatTo == null?'':n.repeatTo,
				            repeatFrom: n.repeatFrom == null?'':n.repeatFrom,
				            groupId: n.groupId == null?'':n.groupId,
				            groupName: n.groupName == null?'':n.groupName,
				            repeatDay: n.repeatDay == null?'':n.repeatDay,
				            end: n.end == null?'':n.end,
				            remindSetting: n.remindSetting == null?'':n.remindSetting,
		            		remindSettingName: n.remindSettingName == null?'':n.remindSettingName,
				            eventType: n.eventType == null?'':n.eventType, 
		            		customerId: n.customerId == null?'':n.customerId, 
		            		customerName: n.customerName == null?'':n.customerName,
            				creatorName:n.creatorName == null?'':n.creatorName,
            				ifImportant:n.ifImportant == null?'':n.ifImportant 
				        });
					});
					//排序
					scheduleEvents = scheduleEvents.sort(function(a, b){
						var aDate = '';
						var bDate = '';
						if(a.start.indexOf('T')){
							aDate = (a.start.split('T'))[0];
						}
						if(b.start.indexOf('T')){
							bDate = (b.start.split('T'))[0];
						}
						aDate = new Date(aDate);
						bDate = new Date(bDate);
						return bDate-aDate;	
					});
					$('.ifa-home-calendar').fullCalendar( 'addEventSource', scheduleEvents );
				}
			}
		});
	}
	bindSchedule();
	/*******************schdule end  **********************/
	
	/*
	 * 获取Url传递参数值
	 */
	function getUrlParam(name){  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
});