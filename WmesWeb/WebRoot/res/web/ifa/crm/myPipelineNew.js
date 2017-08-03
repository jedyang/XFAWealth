

define(function(require) {

	var $ = require('jquery');
		require("swiper");
		require("layui");
		require('pagination');
	//require("interfaceCheckPwd");
		
	var Loading = require('loading');
	var oLoading = new Loading($('.pipeline_loading'));
	var iLoading = new Loading($('.home-investor-list'));// home-investor-list
	
	
	var swiper= new Swiper('.pipeline-existing .swiper-container', {
	    slidesPerView: 'auto',
	    preventClicks : false,
	    nextButton: '.swiper-button-next',
	    prevButton: '.swiper-button-prev',
	});
	
//	var salesNewJson,salesContactJson,salesProposalJson,salesCloseJson;
	
	var setGroupListLiContent = '';// 设置分组数据的内容
	sessionStorage.removeItem("clientdetailtab");
    function getCustomerGroupList(){
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root+'/front/crm/pipeline/crmGroupListJson.do?datestr='+new Date().getTime(),
				data : {
					'page':1,'rows':100,'sort':'l.createTime','order':''
				},
				async : false,
				success : function(json) {
									
					$("#pipelin-choice").empty();
					$("#my-group-list").empty();
					var divContent = '';
					var liContent = '';
					setGroupListLiContent = '';
					
					var list = json.rows;
					// $(".pipelinenew-Client_Type").empty();
					$(".pipelinenew-Client_Type li:gt(0)").remove();
					// alert(list);
					// divContent = '<li
					// class="pipelinenew-Client_Typeac">All</li>';
					$.each(list, function (index, array) { // 遍历json数据列
						var id = array['id'] == null ? "" : array['id'];
						var groupName = array['groupName'] == null ? "" : array['groupName'];
											                                  
						divContent += '<li data-value="'+id+'">'+groupName+'</li>';
						liContent += '<li class="my-group-li">'
									+ '<input type="text" maxlength="18" readonly="" class="group-list-word" data-value="'+id+'" value="'+groupName+'">'
									+ '<div class="group-list-ico">'
									+ '<span class="group-list-edit"></span>'
									+ '<span class="group-list-close"></span>'
									+ '</div>'
									+ '</li>';
						
						setGroupListLiContent += '<li data-group-id="'+id+'">'+groupName+'</li>';
						
					});									
					$(".pipelinenew-Client_Type").append(divContent);
					$("#my-group-list").append(liContent);
				}
			})
	    }
		    
	    // load CustomerGroup
	    getCustomerGroupList();
	    
		 
		 $(document).on("click",function(event){
				// 點擊其他地方隐藏元素
				var _pipelin_xiala = $('.pipelin_xiala'); 

				if(!_pipelin_xiala.is(event.target) && _pipelin_xiala.has(event.target).length === 0){ 
					_pipelin_xiala.removeClass("pipelin_xiala_active");  
				 }

				 $(".pipelin-bottom-more").each(function(){
				 	var self = $(this);
				 	if(!self.is(event.target) && self.has(event.target).length === 0){ 
						self.removeClass("pipelin-more-show");  
						
					 }

					 if( self.parents(".pipelin-list-swiper").find(".pipelin-more-show").length <= 0){
					 	self.parents(".pipelin-list-swiper").removeClass("pipelin-swiper-active");
					 }
				 });
				 
			});

	

	var saleStageId = "";
//	var clientStatus = "";
    var SwiperBol = true;
	// document.addEventListener('touchmove', function (e) { e.preventDefault();
	// }, false);//监听touchmove 事件冒泡
	$(".pipelin-bell")
	.on("mouseenter",function(){
		$(this).addClass("pipelin-bell-active");})
	.on("mouseleave",function(){
        $(this).removeClass("pipelin-bell-active");
    });
	
	$(".Small_cake_ico, #pipeline-search-close, #pipelin-bless").on("click",function(){
		$("#ifa-pipeline-search").toggleClass("ifa-space-active");
	});
	/*
	 * $("#pipelin-schedule, #pipelin-date-close").on("click",function(){
	 * $("#ifa-pipeline-date").toggleClass("ifa-space-active"); });
	 */
	
	$(".pipelin-bell-tab li").on("click",function(){
		$(this).siblings().removeClass("bell-tab-active").end().addClass("bell-tab-active");
// $(".pipelin-bell-contents li").hide().eq($(this).index()).show();
	});

	$(document).on("click",".pipelin-bottom-more",function(e){
		e.stopPropagation();
		$(this).parents('.pipelin-list').css('height','186px');
		$('.pipelin-list-eject').css('display','none');
    	$(this).closest('li').css({"width":220,"padding-right" : 12});
		var selfIndex = $(this).parents(".pipelin-list").index();
		$(this).parents(".pipelin-list-swiper").addClass("pipelin-swiper-active");
		$(this).toggleClass("pipelin-more-show");
		$(this).closest('.pipelin-list-contents').addClass('kejiande');
		if( $(this).parents(".pipelin-list-swiper").find(".pipelin-more-show").length > 0  ){
			$(this).parents(".pipelin-list-swiper").css("z-index",20 - selfIndex)
		}else{
			$(this).parents(".pipelin-list-swiper").css("z-index",0);
		}
	});

	$(document).on('mouseleave','.pipelin-list-bottom',function(){
		$(this).find('.pipelin-bottom-more').removeClass('pipelin-more-show');
	});

	$(".pipelin-date-title").on("click",function(){
		$('.tanchutitle').text($(this).text());
		$('.tanchutitle').attr('saleStageId', $(this).attr('saleStageId'));
		$(window).scrollTop('0');
		$('.pipeline-search-choose-existing').removeClass('pipeline-search-choose-existingac');
		if($(this).closest('.pipelin-list').index()==2){
			$('.pipeline-search-choose').eq(1).addClass('pipeline-search-chooseac').siblings().removeClass('pipeline-search-chooseac');
			$('.pipeline_choose_list').eq($(this).closest('.pipelin-list').index()).addClass('pipeline_choose_listac').siblings().removeClass('pipeline_choose_listac');
		}else{
			$('.pipeline-search-choose').eq(0).addClass('pipeline-search-chooseac').siblings().removeClass('pipeline-search-chooseac');
			$('.pipeline_choose_list').eq($(this).closest('.pipelin-list').index()).addClass('pipeline_choose_listac').siblings().removeClass('pipeline_choose_listac');
		}
		
		$(this).parent().toggleClass("pipelin-date-active");
		$(".pipelin-search-mask, .pipelin-list-search").toggleClass("pipelin-search-show");
		
		$("#pipelinKeyWord").val('');
		var totalOf = $('.pipeline_choose_listac .pipelin-list-contents').length;
		$('#totalOf').text(totalOf);
		
		for(var i=0;i<$('.pipeline-search-chooseac .wmes_choice_wrap').length;i++){
			$('.wmes_choice li').removeClass('wmes_choice_active');
			$('.pipeline-search-chooseac .wmes_choice').eq(i).find('li').eq(0).addClass('wmes_choice_active');
			$('.pipeline-search-chooseac .wmes_choice').eq(i).find('li').eq(0).addClass('wmes_choice_active2');
			
		}
	});
	
	$(".pipelin-date-title-existing").on("click",function(){
		$('.tanchutitle').text($(this).text());		
		$('.tanchutitle').attr('clientstatus', $(this).attr('clientstatus'));
		$(window).scrollTop('0');
		$('.pipeline-search-choose').removeClass('pipeline-search-chooseac');
		if($(this).closest('.pipelin-list').index()==0){
			$('.pipeline-search-choose-existing').eq(0).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		}else if($(this).closest('.pipelin-list').index()==1){
			$('.pipeline-search-choose-existing').eq(1).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		}else if($(this).closest('.pipelin-list').index()==2){
			$('.pipeline-search-choose-existing').eq(2).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		}else if($(this).closest('.pipelin-list').index()==3){
			$('.pipeline-search-choose-existing').eq(3).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		}else if($(this).closest('.pipelin-list').index()==4){
			$('.pipeline-search-choose-existing').eq(4).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		}else{
			$('.pipeline-search-choose-existing').eq(5).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		};
		
		$('.pipeline_choose_list1').eq($(this).closest('.pipelin-list').index()).addClass('pipeline_choose_listac').siblings().removeClass('pipeline_choose_listac');
				
		$(this).parent().toggleClass("pipelin-date-active");
		$(".pipelin-search-mask, .pipelin-list-search").toggleClass("pipelin-search-show");

		$("#pipelinKeyWord").val('');
		var totalOf = $('.pipeline_choose_listac .pipelin-list-contents').length;
		// //console.log(totalOf);
		$('#totalOf').text(totalOf);
		
		for(var i=0;i<$('.pipeline-search-choose-existingac .wmes_choice_wrap').length;i++){
			$('.wmes_choice li').removeClass('wmes_choice_active');
			$('.pipeline-search-choose-existingac .wmes_choice').eq(i).find('li').eq(0).addClass('wmes_choice_active');
			$('.pipeline-search-choose-existingac .wmes_choice').eq(i).find('li').eq(0).addClass('wmes_choice_active2');
		}
	});
	
	// 弹出层查询潜在客户
	$('body').on('click','.pipeline-search-chooseac .wmes_choice li',function(){
		$(this).siblings().removeClass("wmes_choice_active").end().addClass("wmes_choice_active");
		if($(this).closest('ul').find('.wmes_choice_active').index()==0){
			$(this).addClass('wmes_choice_active2');
		}else{
			$(this).closest('ul').find('li').removeClass('wmes_choice_active2');
		};
		searchProspectList();	
	});
	
	// 弹出层查询潜在客户
	$('body').on('click','.pipeline-search-choose-existingac .wmes_choice li',function(){
		$(this).siblings().removeClass("wmes_choice_active").end().addClass("wmes_choice_active");
		if($(this).closest('ul').find('.wmes_choice_active').index()==0){
			$(this).addClass('wmes_choice_active2');
		}else{
			$(this).closest('ul').find('li').removeClass('wmes_choice_active2');
		};
		searchExistingList();
	});
	
	function searchExistingList(){
		var clientstatus = $('.tanchutitle').attr('clientstatus');
		switch(clientstatus){
		case 'customer-care':
			searchExistingList4Care();
			break;
		case 'kyc':
			searchExistingList4KYC();
			break;
		case 'open-account':
			searchExistingList4OpenAccount();
			break;
		case 'proposal':
			searchExistingList4Proposal();
			break;
		case 'portfolio':
			searchExistingList4Portfolio();
			break;
		case 'not-yet-invest':
			searchExistingList4NotYetInvest();
			break;
			default:				
		}
	}
	
	// 弹出层查询潜在客户
	function searchProspectList(){
		var totalOf = 0;
		var stageId = $('.tanchutitle').attr('saleStageId');
		var keyword = $.trim( $('#pipelinKeyWord').val());
		
		var searchChar,searchProposalStatus,invAmountFrom,invAmountTo;
		$('.pipeline-search-chooseac .wmes_choice_active').each(function(){
			var dataName = $(this).attr('data-name');
			if(dataName == 'name'){
				searchChar = $(this).attr('data-letter');
			}else if(dataName == 'Status'){
				searchProposalStatus = $(this).attr('data-value');
			}else if(dataName == 'Inv.Amount'){
				invAmountFrom = $(this).attr('data-value-from');
				invAmountTo = $(this).attr('data-value-to');
			}
		});				
			
		$('.pipeline_choose_listac .pipelin-list-contents').hide();
		$(".pipeline_choose_listac .pipelin-list-contents").each(function(){
			var nameFlag = false;
			var statusFlag = false;
			var invFlag = false;
			var keywordFlag = false;
			
			var pinyin = $(this).attr('pinyin');
			if(searchChar == undefined || searchChar.indexOf(pinyin)>-1){
				nameFlag = true;
			}
						
			if(keyword == undefined || keyword == ''){
				keywordFlag = true;
			}else{
				var nickname = $(this).find('.nickname').text();
				if(nickname.indexOf(keyword)>-1){
					keywordFlag = true;
				}
			}
			
			if(stageId == 'sales_proposal'){
				var pStatus = $(this).attr('proposalStatus');
				if(searchProposalStatus == undefined || searchProposalStatus == pStatus){
					statusFlag = true;
				}
				
				if(invAmountFrom == undefined){
					invFlag = true;
				}else{
					if($(this).attr('proposalInvAmount') == undefined || $(this).attr('proposalInvAmount') == ''){
						invFlag = false;
					}else{
						var invAmount = parseFloat($(this).attr('proposalInvAmount'));
						var amountFrom = parseFloat(invAmountFrom);
						var amountTo = parseFloat(invAmountTo);
						
						if(invAmountTo == undefined){
							if(invAmount >= amountFrom){
								invFlag = true;
							}else{
								invFlag = false;
							}							
						}else{
							if(invAmount >= amountFrom && invAmount <= amountTo){
								invFlag = true;
							}else{
								invFlag = false;
							}
						}
					}										
				}
			}else{
				statusFlag = true;
				invFlag = true;
			}
						
			if(nameFlag && statusFlag && invFlag && keywordFlag){
				totalOf++;
				$(this).show();
			}
		});
		
		$('#totalOf').text(totalOf);
	}
	
	
	
	// 弹出层查询正式客户 Care
	function searchExistingList4Care(){
		var totalOf = 0;
//		var stageId = $('.tanchutitle').attr('clientstatus');
		var keyword = $.trim( $('#pipelinKeyWord').val());
		
		var searchChar,returnFrom,returnTo,invAmountFrom,invAmountTo;
		$('.pipeline-search-choose-existingac .wmes_choice_active').each(function(){
			var dataName = $(this).attr('data-name');
			if(dataName == 'name'){
				searchChar = $(this).attr('data-letter');
			}else if(dataName == 'Total_Return'){
				returnFrom = $(this).attr('data-value-from');
				returnTo = $(this).attr('data-value-to');
			}else if(dataName == 'Inv.Amount'){
				invAmountFrom = $(this).attr('data-value-from');
				invAmountTo = $(this).attr('data-value-to');
			}
		});				
			
		$('.pipeline_choose_listac .pipelin-list-contents').hide();
		$(".pipeline_choose_listac .pipelin-list-contents").each(function(){
			var nameFlag = false;
			var returnFlag = false;
			var invFlag = false;
			var keywordFlag = false;
			
			var pinyin = $(this).attr('pinyin');
			if(searchChar == undefined || searchChar.indexOf(pinyin)>-1){
				nameFlag = true;
			}
						
			if(keyword == undefined || keyword == ''){
				keywordFlag = true;
			}else{
				var nickname = $(this).find('.nickname').text();
				if(nickname.indexOf(keyword)>-1){
					keywordFlag = true;
				}
			}
			
			if(returnFrom == undefined && returnTo == undefined){
				returnFlag = true;
			}else{
				var returnRate = $(this).find('#returnRate').eq(0).text();
				// //console.log(marketValue+','+returnRate);
				if( returnRate == undefined || returnRate == '' ){
					returnFlag = false;
				}else{
					returnRate = parseFloat(returnRate.replace('%',''));
					var rateFrom = parseFloat(returnFrom);
					var rateTo = parseFloat(returnTo);
					
					if(returnFrom == undefined){
						if(returnRate < rateTo){
							returnFlag = true;
						}else{
							returnFlag = false;
						}							
					}else if(returnTo == undefined){
						if(returnRate >= rateFrom){
							returnFlag = true;
						}else{
							returnFlag = false;
						}							
					}else{
						if(returnRate >= rateFrom && returnRate <= returnTo){
							returnFlag = true;
						}else{
							returnFlag = false;
						}
					}
				}										
			}
							
			if(invAmountFrom == undefined){
				invFlag = true;
			}else{
				var marketValue =  $(this).find('#marketValue').eq(0).text();
				if(marketValue == undefined || marketValue == ''){
					invFlag = false;
				}else{
					var invAmount = parseFloat(marketValue);
					var amountFrom = parseFloat(invAmountFrom);
					var amountTo = parseFloat(invAmountTo);
					
					if(invAmountFrom == undefined){
						if(invAmount <= amountTo){
							invFlag = true;
						}else{
							invFlag = false;
						}							
					}else if(invAmountTo == undefined){
						if(invAmount >= amountFrom){
							invFlag = true;
						}else{
							invFlag = false;
						}							
					}else{
						if(invAmount >= amountFrom && invAmount <= amountTo){
							invFlag = true;
						}else{
							invFlag = false;
						}
					}
				}										
			}
		
						
			if(nameFlag && returnFlag && invFlag && keywordFlag){
				totalOf++;
				$(this).show();
			}
		});
		
		$('#totalOf').text(totalOf);
	}
	
	// 弹出层查询正式客户 KYC
	function searchExistingList4KYC(){
		var totalOf = 0;
//		var stageId = $('.tanchutitle').attr('clientstatus');
		var keyword = $.trim( $('#pipelinKeyWord').val());
		
		var searchChar,searchDistributor;
		$('.pipeline-search-choose-existingac .wmes_choice_active').each(function(){
			var dataName = $(this).attr('data-name');
			if(dataName == 'name'){
				searchChar = $(this).attr('data-letter');
			}else if(dataName == 'Distributor'){
				searchDistributor = $(this).attr('data-value');
			}
		});				
			
		$('.pipeline_choose_listac .pipelin-list-contents').hide();
		$(".pipeline_choose_listac .pipelin-list-contents").each(function(){
			var nameFlag = false;
			var distributorFlag = false;
			var keywordFlag = false;
			
			var pinyin = $(this).attr('pinyin');
			if(searchChar == undefined || searchChar.indexOf(pinyin)>-1){
				nameFlag = true;
			}
						
			if(keyword == undefined || keyword == ''){
				keywordFlag = true;
			}else{
				var nickname = $(this).find('.nickname').text();
				if(nickname.indexOf(keyword)>-1){
					keywordFlag = true;
				}
			}
			
			var disId = $(this).attr('distributorId');
			if( searchDistributor == undefined || searchDistributor == disId ){
				distributorFlag = true;
			}
			
		
						
			if(nameFlag && distributorFlag && keywordFlag){
				totalOf++;
				$(this).show();
			}
		});
		
		$('#totalOf').text(totalOf);
	}
	
	// 弹出层查询正式客户 OpenAccount
	function searchExistingList4OpenAccount(){
		var totalOf = 0;
//		var stageId = $('.tanchutitle').attr('clientstatus');
		var keyword = $.trim( $('#pipelinKeyWord').val());
		
		var searchChar,searchStatus,searchDistributor;
		$('.pipeline-search-choose-existingac .wmes_choice_active').each(function(){
			var dataName = $(this).attr('data-name');
			if(dataName == 'name'){
				searchChar = $(this).attr('data-letter');
			}else if(dataName == 'Status'){
				searchStatus = $(this).attr('data-value');
			}else if(dataName == 'Distributor'){
				searchDistributor = $(this).attr('data-value');
			}
		});				
			
		$('.pipeline_choose_listac .pipelin-list-contents').hide();
		$(".pipeline_choose_listac .pipelin-list-contents").each(function(){
			var nameFlag = false;
			var distributorFlag = false;
			var keywordFlag = false;
			var statusFlag = false;
			
			var pinyin = $(this).attr('pinyin');
			if(searchChar == undefined || searchChar.indexOf(pinyin)>-1){
				nameFlag = true;
			}
						
			if(keyword == undefined || keyword == ''){
				keywordFlag = true;
			}else{
				var nickname = $(this).find('.nickname').text();
				if(nickname.indexOf(keyword)>-1){
					keywordFlag = true;
				}
			}
			
			var status = $(this).attr('openStatus');
			if( searchStatus == undefined || searchStatus == status ){
				statusFlag = true;
			}
			
			var disId = $(this).attr('distributorId');
			if( searchDistributor == undefined || searchDistributor == disId ){
				distributorFlag = true;
			}
			
		
						
			if(nameFlag && statusFlag && distributorFlag && keywordFlag){
				totalOf++;
				$(this).show();
			}
		});
		
		$('#totalOf').text(totalOf);
	}
	
	
	// 弹出层查询正式客户 Proposal
	function searchExistingList4Proposal(){
		var totalOf = 0;
//		var stageId = $('.tanchutitle').attr('clientstatus');
		var keyword = $.trim( $('#pipelinKeyWord').val());
		
		var searchChar,searchStatus,invAmountFrom,invAmountTo;
		$('.pipeline-search-choose-existingac .wmes_choice_active').each(function(){
			var dataName = $(this).attr('data-name');
			if(dataName == 'name'){
				searchChar = $(this).attr('data-letter');
			}else if(dataName == 'Status'){
				searchStatus = $(this).attr('data-value');
			}else if(dataName == 'Inv.Amount'){
				invAmountFrom = $(this).attr('data-value-from');
				invAmountTo = $(this).attr('data-value-to');
			}
		});				
			
		$('.pipeline_choose_listac .pipelin-list-contents').hide();
		$(".pipeline_choose_listac .pipelin-list-contents").each(function(){
			var nameFlag = false;
			var statusFlag = false;
			var invFlag = false;
			var keywordFlag = false;
			
			var pinyin = $(this).attr('pinyin');
			if(searchChar == undefined || searchChar.indexOf(pinyin)>-1){
				nameFlag = true;
			}
						
			if(keyword == undefined || keyword == ''){
				keywordFlag = true;
			}else{
				var nickname = $(this).find('.nickname').text();
				if(nickname.indexOf(keyword)>-1){
					keywordFlag = true;
				}
			}
			
			var status = $(this).attr('proposalStatus');
			if( searchStatus == undefined || searchStatus == status ){
				statusFlag = true;
			}
							
			if(invAmountFrom == undefined){
				invFlag = true;
			}else{
				var marketValue =  $(this).find('#marketValue').eq(0).text();
				if(marketValue == undefined || marketValue == ''){
					invFlag = false;
				}else{
					var invAmount = parseFloat(marketValue);
					var amountFrom = parseFloat(invAmountFrom);
					var amountTo = parseFloat(invAmountTo);
					
					if(invAmountFrom == undefined){
						if(invAmount <= amountTo){
							invFlag = true;
						}else{
							invFlag = false;
						}							
					}else if(invAmountTo == undefined){
						if(invAmount >= amountFrom){
							invFlag = true;
						}else{
							invFlag = false;
						}							
					}else{
						if(invAmount >= amountFrom && invAmount <= amountTo){
							invFlag = true;
						}else{
							invFlag = false;
						}
					}
				}										
			}
		
						
			if(nameFlag && statusFlag && invFlag && keywordFlag){
				totalOf++;
				$(this).show();
			}
		});
		
		$('#totalOf').text(totalOf);
	}
	
	// 弹出层查询正式客户 Portfolio
	function searchExistingList4Portfolio(){
		var totalOf = 0;
//		var stageId = $('.tanchutitle').attr('clientstatus');
		var keyword = $.trim( $('#pipelinKeyWord').val());
		
		var searchChar,searchStatus;
		$('.pipeline-search-choose-existingac .wmes_choice_active').each(function(){
			var dataName = $(this).attr('data-name');
			if(dataName == 'name'){
				searchChar = $(this).attr('data-letter');
			}else if(dataName == 'Status'){
				searchStatus = $(this).attr('data-value');
			}
		});				
		
		
		
		$('.pipeline_choose_listac .pipelin-list-contents').hide();
		$(".pipeline_choose_listac .pipelin-list-contents").each(function(){
			var nameFlag = false;
			var statusFlag = false;
			var keywordFlag = false;
			
			var pinyin = $(this).attr('pinyin');
			if(searchChar == undefined || searchChar.indexOf(pinyin)>-1){
				nameFlag = true;
			}
						
			if(keyword == undefined || keyword == ''){
				keywordFlag = true;
			}else{
				var nickname = $(this).find('.nickname').text();
				if(nickname.indexOf(keyword)>-1){
					keywordFlag = true;
				}
			}
			
			var status = $(this).attr('planStatus');
			
			if( searchStatus == undefined ){				
				statusFlag = true;
			}else {
				var arrStatus = searchStatus.split(',');
				if(arrStatus.indexOf(status)>-1){
					statusFlag = true;
//					//console.log(arrStatus);
//					//console.log(status);
//					//console.log(nameFlag && statusFlag && keywordFlag);
				}
			}
									
			if(nameFlag && statusFlag && keywordFlag){
				totalOf++;
				
				$(this).show();
			}
		});
		//console.log(totalOf);
		
		$('#totalOf').html(totalOf);
	}
	
	// 弹出层查询正式客户 NotYetInvest
	function searchExistingList4NotYetInvest(){
		var totalOf = 0;
//		var stageId = $('.tanchutitle').attr('clientstatus');
		var keyword = $.trim( $('#pipelinKeyWord').val());
		
		var searchChar;
		$('.pipeline-search-choose-existingac .wmes_choice_active').each(function(){
			var dataName = $(this).attr('data-name');
			if(dataName == 'name'){
				searchChar = $(this).attr('data-letter');
			}
		});				
			
		$('.pipeline_choose_listac .pipelin-list-contents').hide();
		$(".pipeline_choose_listac .pipelin-list-contents").each(function(){
			var nameFlag = false;
			var keywordFlag = false;
			
			var pinyin = $(this).attr('pinyin');
			if(searchChar == undefined || searchChar.indexOf(pinyin)>-1){
				nameFlag = true;
			}
						
			if(keyword == undefined || keyword == ''){
				keywordFlag = true;
			}else{
				var nickname = $(this).find('.nickname').text();
				if(nickname.indexOf(keyword)>-1){
					keywordFlag = true;
				}
			}									
						
			if(nameFlag && keywordFlag){
				totalOf++;
				$(this).show();
			}
		});
		
		$('#totalOf').text(totalOf);
	}
	
	$(document).on('click','.touming',function(event){
			event.stopPropagation();
	});
	
	$(document).on('click','.pipelin-list-bottom img',function(event){
		$(this).closest('.pipelin-list-contents').find('.pipelin-bottom-more').removeClass('pipelin-more-show');
		event.stopPropagation();
		$('.pipelin-list-contents').removeClass('kejiande');
		$(this).closest('li').css({"width":220,"padding-right" : 12});
		$(this).closest('.pipelin-list-contents').find('.mask-layer').css('display','block');
		$(this).closest('.pipelin-list-contents').find('.touming').css('display','block');
		$(this).closest('.pipelin-list-contents').find('.touming').animate({'opacity':'0.9'},500);
	});
	
	$('body').on('click','.mask-layer',function(e){
		e.stopPropagation();
		if($(this).find('.mask-layerbox').css('right') == '-134px'){
			$(this).css('display','none');
			$(this).closest('.pipelin-list-contents').find('.touming').css('display','none');
			$(this).closest('.pipelin-list-contents').find('.touming').css('opacity','0');
		}else if($(this).closest('.pipeline-div-active').hasClass('pipeline-existing')){
			$(this).css('display','none');
			$(this).closest('.pipelin-list-contents').find('.touming').css('display','none');
			$(this).closest('.pipelin-list-contents').find('.touming').css('opacity','0');
		}
		
	});
	
	$("#pipelin-search-close").on("click",function(){
		$(".pipelin-date-active").removeClass("pipelin-date-active");
		$(".pipelin-search-mask, .pipelin-list-search").removeClass("pipelin-search-show");
	});
	
	$(".wmes-close").on("click",function(){
		$(".pipelin-date-active").removeClass("pipelin-date-active");
		$(".pipelin-search-mask, .pipelin-list-search").removeClass("pipelin-search-show");
	});
	
	/*$('#add_Schedule-close').on('click',function(){
		$('.ifa-space-popup').removeClass('ifa-space-active');
	});*/
	
//	var expiredAeCode = $("#hdExpiredAeCode").val();
//	if(expiredAeCode != ''){
//		//var jsonlist = JSON.parse('{"accountlist":"'+expiredAeCode+'","targetid":"txtTitle"}');
//		// //console.log(jsonlist);
//		$(this).InitInterface({ 'accountlist':expiredAeCode,'accounttype':'0','isopen':'1' });
//	}
	var swiper1;
	$(".pipelin-bell-wrap").css("width","410");
	$(".noline_tab_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".pipeline-div").removeClass("pipeline-div-active").eq($(this).index()).addClass("pipeline-div-active");
		$(".pipelin-bell-contents li").hide().eq($(this).index()).show();
		if($(this).index() == 0){
			$(".pipelin-bell-wrap").css("width","410");
			$("#hdClientType").val("1");
			$('.pipelin-bell-existing').css('display','block');
		}else {
			$(".pipelin-bell-wrap").css("width","410");
			$("#hdClientType").val("0");
			$('.pipelin-bell-existing').css('display','none');		 
		}
		if($(this).index()!=0 && SwiperBol){
			swiper1 = new Swiper('.pipeline-prospect .swiper-container', {
		        slidesPerView: 'auto',
		        grabCursor: true,
		        nextButton: '.swiper-button-next',
		        prevButton: '.swiper-button-prev',
		    });
		    SwiperBol = false;
		}
	});
	
	
	
	$(".pipelin-contents-more").each(function(){
		var contentsNum = 0;
		$(this).find(".pipelin-list-eject").each(function(index){
			contentsNum++;
			$(this).css("left",228 * contentsNum);
		});
	});
	$("body").on("click",".pipelin-contents-more",function(n){
		$(this).toggleClass("pipelin-contents-more-show");
		$('.pipelin-list-eject').css('display','block');
		$('.pipelin-list-contents').removeClass('kejiande');
		if($(this).hasClass("pipelin-contents-more-show")){
			var contentsNum = 0;
			$(this).find(".pipelin-list-eject").each(function(index){	
				contentsNum++;
			});
			$(this).css({
				"width":220 + 220 * contentsNum,
				"padding-right" : 220 * contentsNum + 12
			});
		}else{
			$(this).css({
				"width":220,
				"padding-right" : 12
			});	
		}
		$.each(swiper,function(i,n){
			n.onResize();
		});
	});
	
	$("body").on("click",".j-exclamatory-ico",function(){
		$(this).parent().toggleClass("pipelin-topper-active");
	});
	
	$(".pipelin_xiala").on("click",function(){
		$(this).toggleClass("pipelin_xiala_active");
	});

	$(".plipelin-checkbox-ico").on("click",function(){
		$(this).toggleClass("plipelin-checkbox-ico-active");
		if($(this).hasClass("plipelin-checkbox-ico-active")){
			$(".pipeline-div .pipelin-list-contents").each(function(){
				if( $(this).find(".pipelin-list-heart").length <= 0){$(this).hide();}
			});
		}else{
			$(".pipeline-div .pipelin-list-contents").show();
		}
	});
	
	
	$("body").on('click',"#pipelin-choice li",function(e){

		var self = $(this);

		if( self.hasClass("pipelin-show-all") ){
			// show All
			self.parents(".pipelin_xiala").toggleClass("pipelin_xiala_active").find("input").val("");
			
			$(".pipelin-list-contents").show();
		}else if( self.hasClass("pipelin-new-group") ){

			return;
		}else{
			self.parents(".pipelin_xiala").toggleClass("pipelin_xiala_active").find("input").val( self.html() );
			
			$(".pipelin-list-contents").hide();

			$(".pipelin-list-contents").each(function(index){


				var _gropu = $(this).attr("data-group");

				if(!_gropu) _gropu = '';

				if ( _gropu.indexOf( self.html() ) >= 0 ) {

					$(this).show();
				}

			});
		}
		e.stopPropagation(); 
	});
	
	// 第二版页面
	$(".investor-chioce li p").hide();

	$(".investor-chioce-list .investor-chioce-button").on("click",function(e){
			$(this).toggleClass("investor-chioce-active");
			$(this).closest('.investor-chioce-list').siblings().find('.investor-chioce-button').addClass('investor-chioce-white');
			$(this).closest('.investor-chioce-list').siblings().find('.investor-chioce-button').removeClass('investor-chioce-active');
			$(this).closest('.investor-chioce-list').siblings().find('.investor-chioce-button').next('.allowheight').children().stop(true).hide('1000');
			
			if ($(this).hasClass("investor-chioce-active")) {
				$(this).next('.allowheight').children().stop(true).show('1000');
				$(this).removeClass('investor-chioce-white');
			}else{
				$(this).next('.allowheight').children().stop(true).hide('1000');
				$(this).addClass('investor-chioce-white');
			};
	});
	
	var pageIndex=0;
	
	$(".investor-region-list").on("click",function(){
		$(this).toggleClass("investor-region-active");
		if($(this).hasClass('investor-region-active')){
			$(this).closest('.investor-chioce-list').find('.xuanzhongac').css('display','block');
		}else{
			if($(this).closest('.allowheight').find('.investor-region-active').length == 0){
				$(this).closest('.investor-chioce-list').find('.xuanzhongac').css('display','none');
			}
		};
		// var saleStageId=$(".pipelin-date-active p").attr("saleStageId");
		pageIndex=0;
		genInvestor(saleStageId);
    	setTimeout(function(){
    		if($('.home-investors-sum').text() <= 6){
    			$('.ifa_list_paging').css('display','none');
    		}else{
    			$('.ifa_list_paging').css('display','block');
    		};
    	},500);
    	
	})
	$(".mid-inverstor-left").on("mousemove",".home-investor-list-li",function(){
        $(this).find(".ifa-home-mask").stop(true).animate({ opacity: "0.7"}, 100,"linear");
        
        
	});         
    $(".mid-inverstor-left").on("mouseleave",".home-investor-list-li",function(){
    	$(this).find(".ifa-home-mask").stop(true).animate({ opacity: "0"}, 100,"linear");
    });
    $(document).on("click",".investor-j-chat",function(){
    	$(this).siblings(".investor-hide-chat").toggleClass("investor-show-chat");
    });
    $(".investor-noyet").on("click",function(){
    	$(this).toggleClass("investor-chioce-white");
    
    	// var saleStageId=$(".pipelin-date-active p").attr("saleStageId");
    	pageIndex=0;
    	genInvestor(saleStageId);
    })

    // 点击提醒数量时筛选数据
	$(".pipelin-bell-contents li .pipelin-bell-mesWrap").on("click",function(){
//		var seleNum = 	$(this).find(".pipelin-bell-num").html(),
		var selfWrap = $(".pipeline-existing .kyc .pipelin-list-contents");
		selfWrap.show();
		
		var careWrap = $(".pipeline-existing .customer-care .pipelin-list-contents");
		careWrap.show();
		
		$(this).toggleClass("bell-mesWrap-active");
		if($(this).hasClass("bell-mesWrap-active")){
				$(".pipelin-bell-contents li .pipelin-bell-mesWrap").removeClass("bell-mesWrap-active");
				$(this).addClass("bell-mesWrap-active");
				if($(this).attr("data-type") == "kyc"){

					selfWrap.hide();
//					var portfolioRemind=0,kycRemind=0;
					// var period =100;
					var period = $('#hdPeriod').val();
					$('.pipeline-existing .kyc .pipelin-list-contents').each(function(){
											
						var rpqDays = parseInt( $(this).find('.rpq-expire-days').eq(0).text() );
						var docDays = parseInt( $(this).find('.doc-expire-days').eq(0).text() );
						
						var kycFlag = false;
						if( isNaN(rpqDays) == false &&　rpqDays <= period){
							kycFlag = true;
						}
						
						if( isNaN(docDays) == false && docDays <= period){
							kycFlag = true;
						}
						
						if(kycFlag == true){
							$(this).show();
						}
						
					});
				}else{
					careWrap.hide();
					$('.pipeline-existing .customer-care .pipelin-list-contents').each(function(){
						if( $(this).find('.portfolio-review').length > 0 ){
							$(this).show();
						}						
					});
				}
		}
		
	});
    
	 // 加载6条数据
    function genInvestor(saleStageId){
    	$('.home-investor-list').empty();
    	iLoading.show();
    	var langCode = "";
		var styleCode = "";
		var intrestCode = "";
		var region="";
		$(".investor-chioce li .investor-region-active").each(function(i, n) {
			var type = $(n).parents(".investor-chioce-list").attr("type");
			if ("language" == type) {
				langCode += $(n).attr("code")+",";
			} else if ("style" == type) {
				styleCode += $(n).attr("code")+",";
			} else if ("interest" == type) {
				intrestCode += $(n).attr("code")+",";
			}else if ("region" == type) {
				region += $(n).attr("code")+",";
			}
		});
		var noIfa = "";
		if (!$(".investor-noyet").hasClass("investor-chioce-white")) {
			noIfa = "1";
		}
    	$.ajax({
    		type:"post",
    		datatype:"json",
    		url:base_root+'/front/crm/pipeline/getInverstorList.do?datestr='+new Date().getTime(),
    		data:{'clientType': '0',
    			'areaId':'',
    			'period':'',
    			'saleStageId':saleStageId,
    			'page':pageIndex+1,
    			'rows':6,
    			'sort':'',
    			'order':'',
    				'langCode' : langCode,
    				'invStyle' : styleCode,
    				'intrest' : intrestCode,
    				'region':region,
    				'noIfa' : noIfa
    			},
    		success:function(json){
    			
// var divContent = "";
				var list = json.rows;
				var html = '';
				$.each(list, function (i, array) { // 遍历json数据列
					/*
					 * var name=array['loginCode'] == null ? "" :
					 * array['loginCode']; if(lang=="en") name=array['nameEn'] ==
					 * null ? "" : array['nameEn']; else name=array['nameChn'] ==
					 * null ? "" : array['nameChn'];
					 */
					var name=array['nickName'] == null ? "" : array['nickName'];
					var mobile= array['mobileNumber'] == null ? "" : array['mobileNumber'];
					var email= array['email'] == null ? "" : array['email'];
					var sameLang=array['sameLang'] == null ? "" : array['sameLang'];
					var sameStyle=array['sameStyle'] == null ? "" : array['sameStyle'];
					var country=array['country'] == null ? "" : array['country'];
					var read=array['readInsight'] == null ? "" : array['readInsight'];
					var noIfa=array['noIfa'] == null ? "" : array['noIfa'];
					var memberId=array['memberId'] == null ? "" : array['memberId'];
					var mobileCode=array['mobileCode']==null?"":array['mobileCode']+"-";
					
					var mobileNumber=mobile;
					if(mobileNumber.substring(0,1)!="*"){
						mobileNumber=mobileCode+mobile;
					}
					
					var li = '<li class="home-investor-list-li" memberId="'+memberId+'">';
	                li += '<p class="investor-list-name">'+name+'</p>';
	                li += '<div class="investor-iof-wrap">';
	                li += '<p class="investor-list-title">';
	                li += '<img class="investor-list-img" src="'+base_root+'/res/images/ifa/ifa_phone_ico.png" alt="">';
	                li += '<span class="investor-list-ifo">'+mobileNumber+'</span>';
	                li += '</p>';
	                li += '<p class="investor-list-title">';
	                li += '<img class="investor-list-img" src="'+base_root+'/res/images/ifa/ifa_email_ico.png" alt="">';
	                li += '<span class="investor-list-ifo">'+email+'</span>';
	                li += '</p>';
	                li += '</div>';	                
	                li += '<div class="investor-chat-wrap">';
	                li += '<img class="investor-list-add" memberId="'+memberId+'" src="'+base_root+'/res/images/ifa/addf.png" alt="">';
	                li += '</div>';  
	                li += '<ul class="ifa-home-mask" memberId="'+memberId+'">'; 
	                if(sameLang=="1")
	                li += '<li>'+langMutilForJs['pipeline.findInv.sameLang']+'</li>'; 
	                if(sameStyle=="1")
	                li += '<li>'+langMutilForJs['pipeline.findInv.sameInv']+'</li>'; 
	                li += '<li>'+country+'</li>'; 
	                if(read=="1")
	                li += '<li>'+langMutilForJs['pipeline.findInv.readInsights']+'</li>'; 
	                if(noIfa=="1")
	                li += '<li>'+langMutilForJs['pipeline.findInv.noifa']+'</li>'; 
	                li += '</ul>';
	                li += '</li>';
				html += li;
				});
		    	$('.home-investor-list').empty().append(html).hide().fadeToggle(500);// .show('1000');
		    	$(".home-investors-sum").text(json.total);
		    	
		    	if(json.total==0){
		    		$("#pagination").hide();
		    	}else{
		    		$("#pagination").show();
		    		$("#pagination").pagination(json.total, {
						callback : pageselectCallback,
						numDetail : '',
						items_per_page : 6,
						num_display_entries : 4,
						current_page : pageIndex,
						num_edge_entries : 2
					});
					function pageselectCallback(page_id, jq) {
						pageIndex = page_id;
						genInvestor();
					}
		    	};
		    	iLoading.hide();
    		}
    	})
    }


    $(".pipelin-date-bottom").on("click",function(){
    	$(window).scrollTop('0');
		$("#ifa-pipeline-investors").toggleClass("ifa-space-active");
		saleStageId=$(this).parent().parent().find(".pipelin-date-title").attr("saleStageId");// $(".pipelin-date-active
																								// p").attr("saleStageId");
		pageIndex=0;
		$(".investor-chioce").find(".investor-chioce-active").removeClass("investor-chioce-active").addClass("investor-chioce-white");
		$(".investor-chioce").find(".investor-region-active").removeClass("investor-region-active");
		$(".investor-chioce").find(".investor-region-list").css("display","none");
		genInvestor(saleStageId);
    });

    $("#pipelin-investors-close").on("click",function(){
    	$("#ifa-pipeline-investors").removeClass("ifa-space-active");
    });

    $("#ifa-pipeline-investors").on("click",".investor-list-add",function(event,n){
    	event.stopPropagation();
        var memberId=$(this).attr("memberId");
        if(memberId!=""){
    		$.ajax({
    			type:"post",
        		datatype:"json",
        		url:base_root+'/front/crm/pipeline/addCustomer.do?datestr='+new Date().getTime(),
        		data:{memberId:memberId,salesStageId:saleStageId,clientType:'0'},
        		success:function(json){
        			if(json.result){
        				layer.msg("Set success",{icon:2});
        				//window.location.href=base_root+"/front/crm/pipeline/clientListNew.do";
        				getProspectDataList();
        				genInvestor();
        			}else{
        				layer.msg("Fail to add Customer",{icon:1});
        			};
        			$.each(swiper1,function(i,n){
						n.onResize();
					});
        		}
    		})
    	};
    	
    });
    $(".order-sapce-wrapper").on("mouseenter",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").show();
    });
    $(".order-sapce-wrapper").on("mouseleave",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").hide();
    });

   
    $(".order-plan-sapce").on("click",".order-space-rows",function(){
    	var memberId=$(this).attr("memberId");
    	
    	if(memberId!=""){
    		$.ajax({
    			type:"post",
        		datatype:"json",
        		url:base_root+'/front/crm/pipeline/addCustomer.do?datestr='+new Date().getTime(),
        		data:{memberId:memberId,salesStageId:saleStageId,clientType:'0'},
        		success:function(json){
        			if(json.result){
        				window.location.href=base_root+"/front/crm/pipeline/clientListNew.do";
        			}else{
        				layer.msg("Fail to add Customer",{icon:1});
        			}
        		}
    		})
    	}
    });
    

    
 
  
    $("#ifa-pipeline-investors").on("click",".home-investor-list-li",function(){
    	
    	var memberId=$(this).attr("memberId");
    	bindMemberInfo(memberId);
    	
    });
    $(".investment-wrap").on("click",".investment-close-ico",function(){
    	$(".investment-wrap").addClass("investment-hide");
    });
    
    
    function bindMemberInfo(memberId){
    	$.ajax({
    		type:"post",
    		datatype:"json",
    		url:base_root+"/front/crm/pipeline/getMemberInfo.do",
    		data:{memberId:memberId},
    		success:function(json){
//  			var primarySetting=json.primarySetting;
    			
    			var loginCode=json.loginCode;
    			var iconUrl=json.iconUrl;
    			var education=null!=json.education?json.education:"N/A";
    			var email=null!=json.email?json.email:"N/A";
    			var facebookCode=null!=json.facebookCode?json.facebookCode:"N/A";
    			
    			var introduction=null!=json.introduction?json.introduction:"N/A";
    			var employment=null!=json.employment?json.employment:'N/A';
    			var lastLoginDate=null!=json.lastLoginDate?json.lastLoginDate:"N/A";
//  			var lineCode=null!=json.lineCode?json.lineCode:"N/A";
    			var mobileCode=json.mobileCode?json.mobileCode:"";
    			var mobileNumber=json.mobileNumber?json.mobileNumber:"N/A";
    			var nationality=null!=json.nationality?json.nationality:"N/A";
//  			var occupation=null!=json.occupation?json.occupation:"";
    			var registrationDate=null!=json.registrationDate?json.registrationDate:"N/A";
    			var residence=null!=json.residence?json.residence:"N/A";
    			var webchatCode=null!=json.webchatCode?json.webchatCode:"N/A";
    			var weibo=null!=json.weiboCode?json.weiboCode:"N/A";
    			var linkIn=null!=json.lineCode?json.lineCode:'N/A';
    			var tiwtter=null!=json.twitterCode?json.twitterCode:'N/A';
    			var gender=null!=json.gender?json.gender:"N/A";
    			var dateFormat=json.dateFormat;
    			if(lastLoginDate!='N/A' && lastLoginDate!='')
    				lastLoginDate=formatDate(lastLoginDate,dateFormat);
    			if(registrationDate!='N/A' && registrationDate!='')
    				registrationDate=formatDate(registrationDate,dateFormat);
    			var nickName=null!=json.nickName?json.nickName:loginCode;
    			if(iconUrl==null || iconUrl==""){
    				if(gender=="F"){
    					iconUrl=base_root+"/res/images/head_f.png";
    				}else{
    					iconUrl=base_root+"/res/images/head_m.png";
    				}
    			}else{
    				iconUrl=base_root+iconUrl;
    			}
    			
    			var favoriteInvestmentField=json.favoriteInvestmentField;
    			var hobby=json.hobby;
    			var investmentStyle=json.investmentStyle;
    			var liveRegion=json.liveRegion;
    			var languageSpoken=json.languageSpoken;
    			
    			$(".information-topper-name").text(nickName);
    			$(".registrationDate").text(registrationDate);
    			$(".loginDate").text(lastLoginDate);
    			$(".mobile").text(mobileCode+'-'+mobileNumber);
    			$(".email").text(email);
    			$(".wetchat").text(webchatCode);
    			$(".weibo").text(weibo);
    			$(".facebook").text(facebookCode);
    			$(".twitter").text(tiwtter);
    			$(".linkedin").text(linkIn);
    			$(".residence").text(residence);
    			$(".nationality").text(nationality);
    			$(".education").text(education);
    			$(".occupation").text(occupation);
    			$(".introduction").text(introduction);
    			$(".employment").text(employment);
    			$(".information-plan-portrait").attr("src",iconUrl);
    			
    			var fieldHtml="";
    			var hobbyHtml="";
    			var styleHtml="";
    			var regionHtml="";
    			var spokenHtml="";
    			$.each(favoriteInvestmentField,function(i,n){
    				fieldHtml+='<li class="information-describe-blue">'+n+'</li>';
    			});
    			$.each(hobby,function(i,n){
    				hobbyHtml+='<li class="information-describe-red">'+n+'</li>';
    			});
    			$.each(liveRegion,function(i,n){
    				regionHtml+='<li class="information-describe-red">'+n+'</li>';
    			});
    			$.each(languageSpoken,function(i,n){
    				spokenHtml+='<li class="information-describe-red">'+n+'</li>';
    			});
    			$.each(investmentStyle,function(i,n){
    				styleHtml+='<li class="information-describe-yellow">'+n+'</li>';
    			});
    			$(".style").empty().append(styleHtml);
    			$(".field").empty().append(fieldHtml);
    			$(".hobby").empty().append(hobbyHtml);
    			$(".region").empty().append(regionHtml);
    			$(".spoken").empty().append(spokenHtml);
    			
    			$(".investment-wrap").removeClass("investment-hide");
    			
    			$(".primary").each(function(i,n){
    				var text=$(this).text();
    				if(text!="N/A" && text!=""){
    					var field=$(this).attr("field");
    					//console.log(primarySetting);
//  					var setting= JSON.parse(primarySetting);
    					//console.log(setting);
    	    			var eValue=eval("setting."+field); 
    	    			if(eValue=="0"){
    	    				text="*****"+text.substring(text.length-1,text.length);
    	    				$(this).text(text);
    	    			}
    	    			//console.log(eValue);
    				}
    				//console.log(text);
    			})
    		}
    	})
    }
	
		/*
		 * 获取Url传递参数值
		 */
	function getUrlParam(name){  
	    // 构造一个含有目标参数的正则表达式对象
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    // 匹配目标参数
	    var r = window.location.search.substr(1).match(reg);  
	    // 返回参数值
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
	
	$("#btnSearchAll").on("click",function(){
		getProspectDataList();
		getExistingDataList();
		// getExistingClientDataList();
	});
	
	
	$("#txtKeyword").keyup(function(event){
      	if(event.keyCode == 13){
	        document.getElementById('btnSearchAll').click()
	    }
	});	
	
	
	$("#btnSearch").on("click",function(){
		var stageId = $('.tanchutitle').attr('saleStageId');
		if(stageId == undefined){
			searchExistingList();
		}else{
			searchProspectList();
		}		
	});
	
	
	$("#pipelinKeyWord").keyup(function(event){
      	 if(event.keyCode == 13){
	       	document.getElementById('btnSearch').click();
	     }
	});	
	
	
	$(".pipelin-bell-tab li").on("click",function(){
		$(this).siblings().removeClass("bell-tab-active").end().addClass("bell-tab-active");
		// $(".pipelin-bell-contents li").hide().eq($(this).index()).show();
		// var dataName = $(this).attr("data-name");
		var dataValue = parseInt( $(this).attr("data-value") );
		// //console.log(dataValue);
		$("#hdPeriod").val(dataValue);
		getRemind(dataValue);		
	});
	
	
	// 第一次加载数据
	getProspectDataList();
	getExistingDataList();
	
	/*
	 * getClientRemind(); getExistingClientDataList();
	 * getExistingClientRemind();
	 */	

	

	
	function getProspectDataList(){
		getProspectList('sales_new');
		getProspectList('sales_contact');
		getProspectList('sales_proposal');
		getProspectList('sales_close');
	}
	
	
	function getRemind(period){
		var portfolioRemind=0,kycRemind=0;
		$('.pipeline-existing .customer-care .pipelin-list-contents').each(function(){
			if( $(this).find('.portfolio-review').length > 0 ){
				// //console.log($(this).find('.portfolio-review'));
				portfolioRemind++;
			}						
		});
		
		$('.pipeline-existing .kyc .pipelin-list-contents').each(function(){
			
			var rpqDays = parseInt( $(this).find('.rpq-expire-days').eq(0).text() );
			var docDays = parseInt( $(this).find('.doc-expire-days').eq(0).text() );
			// period = 100;
			var kycFlag = false;
			if( isNaN(rpqDays) == false && rpqDays <= period){
				kycFlag = true;
			}
			
			if( isNaN(docDays) == false && docDays <= period){
				kycFlag = true;
			}
			
			if(kycFlag == true){
				kycRemind++;
			}
		});
						
		if(portfolioRemind > 0){
			if($("#portfolioRemindNum").hasClass("pipelin-bell-num") == false){
				$("#portfolioRemindNum").addClass("pipelin-bell-num")
			}
			$("#portfolioRemindNum").text(portfolioRemind);
		}else{
			if($("#portfolioRemindNum").hasClass("pipelin-bell-num") == true){
				$("#portfolioRemindNum").removeClass("pipelin-bell-num")
			}
			$("#portfolioRemindNum").text("");
		}
			
		
		if(kycRemind > 0){
			if($("#kycRemindNum").hasClass("pipelin-bell-num") == false){
				$("#kycRemindNum").addClass("pipelin-bell-num")
			}
			$("#kycRemindNum").text(kycRemind);
		}else{
			if($("#kycRemindNum").hasClass("pipelin-bell-num") == true){
				$("#kycRemindNum").removeClass("pipelin-bell-num")
			}
			$("#kycRemindNum").text("");
		}
		
	}
	
		
	
 	// 绑定潜在客户数据
	function getProspectList(saleStageId){
		var period = $('#hdPeriod').val();
		var keyword = $('#txtKeyword').val();
		var salesNewJson,salesContactJson,salesProposalJson,salesCloseJson;
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/prospectListJson.do?datestr='+new Date().getTime(),
			data : {
				'period':period,'saleStageId':saleStageId,'keyword':keyword,'page':1,'rows':100,'sort':'','order':''
			},
			success : function(json) {
								
				$("."+saleStageId).empty();
				var divContent = "";
				var list = json.rows;
				// alert(list);
				var salesNewClass = '';
				var salesContactClass = '';
				var salesProposalClass = '';
				var salesCloseClass = '';
				// var setSalesStage ='';
				switch(saleStageId){
					case 'sales_new':
						salesNewClass = ' class="groupingac"';
						salesNewJson = list;
						break;
					case 'sales_contact':
						salesContactClass = ' class="groupingac"';
						salesContactJson = list;
						break;
					case 'sales_proposal':
						salesProposalClass = ' class="groupingac"';
						salesProposalJson = list;
						break;
					case 'sales_close':
						salesCloseClass = ' class="groupingac"';
						salesCloseJson = list;
						break;
					default:
						break;
				}
				 	
				// suspand
				var loss = '';
				if(saleStageId == 'sales_close'){
					loss = '<div class="pipelin-contents-loss">'
						+ '<div class="pipelin-loss-mask"></div>'
						+ '<div class="pipelin-loss-word">LOSS</div>'
						+ '</div>';
				}
							
				$.each(list, function (index, array) { // 遍历json数据列
					var id = array['id'] == null ? "" : array['id'];
					 var memberId = array['memberId'] == null ? "" : array['memberId'];		
					var nickName = array['nickName'] == null ? "" : array['nickName'];		
					var pinyin = array['pinyin'] == null ? "" : array['pinyin'];			
					var mobileCode = array['mobileCode'] == null ? "" : array['mobileCode'];
					var mobileNumber = array['mobileNumber'] == null ? "" : array['mobileNumber'];
					var email = array['email'] == null ? "" : array['email'];
					
					if(mobileNumber=='' && email=='' ){
						email = langMutilForJs['pipeline.content.nopubishinfo'];
					}
					
					var createTime = array['createTime'] == null ? "" : array['createTime'];
					var lastUpdate = array['lastUpdate'] == null ? "" : array['lastUpdate'];
					
					var contactTimes = (array['contactTimes'] == null || array['contactTimes'] == '' ) ? "N/A" : array['contactTimes'];					
					var lastContact = array['lastContact'] == null ? "N/A" : array['lastContact'];					

					var proposalId = array['proposalId'] == null ? "" : array['proposalId'];
					var proposalInvAmount = array['proposalInvAmount'] == null ? "" : array['proposalInvAmount'];					
					var proposalCurrency = array['proposalCurrency'] == null ? "" : array['proposalCurrency'];
					var proposalStatus = array['proposalStatus'] == null ? "" : array['proposalStatus'];					
					var proposalLastUpdate = array['proposalLastUpdate'] == null ? "" : array['proposalLastUpdate'];
					
					var proposalStatusCSS = '';
					var salesContent = '';
					switch(saleStageId){
					case 'sales_new':
						salesContent = '<div class="sales_new_content">'+createTime+'</div>';
						break;
					case 'sales_contact':
						salesContent = '<div class="sales_contact_content">'
									+ '		<div style="overflow: hidden;margin:20px 8px 12px 0;">'
									+ '			<div style="float:left;color:#999">'+langMutilForJs['pipeline.content.contact.contactTimes']+'</div>'
									+ '			<div style="float:right">'+contactTimes+'</div>'
									+ '		</div>'
									+ '		<div style="overflow: hidden;margin-right:8px;">'
									+ '			<div style="float:left;color:#999">'+langMutilForJs['pipeline.content.contact.lastContact']+'</div>'
									+ '			<div style="float:right">'+lastContact+'</div>'
									+ '		</div>'
									+ '</div>';
						break;
					case 'sales_proposal':
						var proposalStatusColor = '';
						var proposalStatusName = '';
						if(proposalStatus == '0'){
							proposalStatusName = langMutilForJs['pipeline.search.proposal.status.draft'];
							proposalStatusColor = '#fbae0a';
							proposalStatusCSS = ' pipelin-list-bottom_draft';
						}else if(proposalStatus == '1'){
							proposalStatusName = langMutilForJs['pipeline.search.proposal.status.processing']+'('+proposalLastUpdate+')';
							proposalStatusColor = '#33cc33';
							proposalStatusCSS = ' pipelin-list-bottom_tobe3';
						}else if(proposalStatus == '-1'){
							proposalStatusName = langMutilForJs['pipeline.search.proposal.status.reject'];
							proposalStatusColor = '#666';
							proposalStatusCSS = ' pipelin-list-bottom_rejuect';
						}else {
							proposalStatusName = '';
						}
						
						proposalCurrency = proposalCurrency==''?'HKD':proposalCurrency;
						proposalInvAmount = proposalInvAmount==''?'N/A' : proposalInvAmount;
						
						salesContent = '<div class="sales_contact_content" proposalId="'+proposalId+'">'
									+ '<div style="overflow: hidden;margin:20px 8px 12px 0;">'
									+ '<div style="float:left;color:#999;width:50%;margin-top:10px;font-size:12px;">'+langMutilForJs['pipeline.content.proposal.invAmt']+'('+proposalCurrency+')</div>'
									+ '<div style="float:right;margin-top:10px;">'+proposalInvAmount+'</div>'
									+ '</div>'
									+ '<p style="text-align: center;color:'+proposalStatusColor+';font-weight: 200;font-size:14px;">'+proposalStatusName+'</p>'
									+ '</div>';
						
						break;
					case 'sales_close':
						salesContent = '<div class="sales_new_content">'+lastUpdate+'</div>';
						break;
					default:
						break;
				}
											
					
					var dataGroup = array['groupName'] == null ? "" : array['groupName'];
					var dataGroupContent = '';
					if(dataGroup != ''){
						dataGroupContent = 'data-group="'+dataGroup+'"';
					}
					var groupList = dataGroup.split(",");
					var liContent = setGroupListLiContent;
					for(var i=0;i<groupList.length;i++){
						liContent = liContent.replace('>'+groupList[i]+'</li>',' class="set-group-active">'+groupList[i]+'</li>');
					}
								
					                                  
					divContent += '<li class="pipelin-list-contents  swiper-slide" proposalInvAmount="'+proposalInvAmount+'" proposalStatus="'+proposalStatus+'" pinyin="'+pinyin+'" '+dataGroupContent+'>'
								// + '<p class="nickname" customerId="'+id+'"
								// customerMemberId="'+memberId+'" ><a
								// href="'+base_root+'/front/crm/pipeline/clientDetail.do?customerId='+id+'&customerMemberId='+memberId+'"
								// target="_self">'+nickName+'</a></p>'
								+ '<p class="nickname" customerId="'+id+'" customerMemberId="'+memberId+'" >'+nickName+'</p>'
								+ salesContent
								+ '<div class="pipelin-list-bottom'+proposalStatusCSS+'" style="margin:0;width:100%;border:0">'								
								+ '<div style="float:left;margin-left:10px;"><img src="'+base_root+'/res/images/application/pipeline_email.png"></div>'
								+ '<div class="pipelin-bottom-more">'
								+ '<span></span>'
								+ '<span></span>'
								+ '<span></span>'
								+ '<ul class="pipelin-list-more">'
								+ '<li  data-name="createProposal" data-value-saleStageId="'+saleStageId+'" data-value-investorId="'+memberId+'" >'+langMutilForJs['pipeline.list.more.createProposal']+'</li>'
								+ '<li class="pipelin-list-more-line" data-name="openAccount" data-value-investorId="'+memberId+'">'+langMutilForJs['pipeline.list.more.openAccount']+'</li>'
								+ '<li data-name="setAppointment" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >'+langMutilForJs['pipeline.list.more.appointment']+'</li>'
								+ '<li data-name="communication" data-value-saleStageId="'+saleStageId+'" data-value-investorId="'+memberId+'" >'+langMutilForJs['pipeline.list.more.communication']+'</li>'
								+ '<li class="pipelin-list-more-line">'+langMutilForJs['pipeline.list.more.chat']+'</li>'								
								+ '<li data-name="setCharacter" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-list-character" >'+langMutilForJs['pipeline.list.more.character']+'</li>'
								+ '<li data-name="setGroup" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-set-group" >'+langMutilForJs['pipeline.list.more.setGroup']
								+ '	<span class="pipelin-group-arrow"></span>'
			    				+ '	<ul class="set-group-list">'
			    				+ liContent								
			    				+ '	</ul>'
								+ '</li>';
					if(saleStageId != 'sales_proposal' ){
						divContent += '<li data-name="delete" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >'+langMutilForJs['pipeline.list.more.delete']+'</li>'
					}
								
								
					divContent += '</ul>'
								+ '</div>'
								+ '</div>'
								+ '<div class="pipeline_page"></div>'
								+ '<div class="mask-layer">'
								+ '<div class="mask-layerbox">'
								+ '	<div class="mask-layerleft">'
								+ '		<span></span>'
								+ '	</div>'
								+ '	<ul class="mask-layerright">'
								+ '		<li customerId="'+id+'" saleStageId="sales_new"'+salesNewClass+'>'+langMutilForJs['pipeline.prospect.list.type.new']+'</li>'
								+ '		<li customerId="'+id+'" saleStageId="sales_contact"'+salesContactClass+'>'+langMutilForJs['pipeline.prospect.list.type.contact']+'</li>'
								+ '		<li customerId="'+id+'" saleStageId="sales_proposal"'+salesProposalClass+'>'+langMutilForJs['pipeline.prospect.list.type.proposal']+'</li>'
								+ '		<li customerId="'+id+'" saleStageId="sales_close"'+salesCloseClass+'>'+langMutilForJs['pipeline.prospect.list.type.close']+'</li>'
								+ '	</ul>'
								+ '</div>'
								+ '<div class="touming" style="text-align: center;height:124px;padding-top:40px;color:#fff;background:#bfbfbf">'
								+ '	<p style="font-weight:400;">'+mobileCode+' '+mobileNumber+'</p>'
								+ '	<p style="font-weight:400;">'+email+'</p>'
								+ '</div>'
								+ '</div>'
								+ loss
								+ '</li>';	
				});
								
				$("."+saleStageId).append(divContent);
				$(".pipelin-contents-more").each(function(){
					var contentsNum = 0;
					$(this).find(".pipelin-list-eject").each(function(index){
						contentsNum++;
						$(this).css("left",228 * contentsNum);
					});
				});
				
				
				
			}
		})
	}

	

	
	function getExistingList(clientStatus){
		var period = $('#hdPeriod').val();
		var keyword = $('#txtKeyword').val();
		var displayColor = $('#hdDisplayColor').val();
		oLoading.show();
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/existingListJson.do?datestr='+new Date().getTime(),
			data : {
				'period':period,'clientStatus':clientStatus,'keyword':keyword,'page':1,'rows':100,'sort':'','order':'','toCurrency':'HKD'
			},
			success : function(json) {
				$("."+clientStatus).empty();
				var divContent = "";
				var list = json.rows;
								 	
							
				$.each(list, function (index, array) { // 遍历json数据列
					var id = array['id'] == null ? "" : array['id'];
					 var memberId = array['memberId'] == null ? "" : array['memberId'];		
					var nickName = array['nickName'] == null ? "" : array['nickName'];		
					var pinyin = array['pinyin'] == null ? "" : array['pinyin'];			
					var mobileCode = array['mobileCode'] == null ? "" : array['mobileCode'];
					var mobileNumber = array['mobileNumber'] == null ? "" : array['mobileNumber'];
					var email = array['email'] == null ? "" : array['email'];

					if(mobileNumber=='' && email=='' ){
						email = langMutilForJs['pipeline.content.nopubishinfo'];
					}
					
//					var createTime = array['createTime'] == null ? "" : array['createTime'];
//					var lastUpdate = array['lastUpdate'] == null ? "" : array['lastUpdate'];
					
					// open account
					var openStatus = array['openStatus'] == null ? "" : array['openStatus'];
					var accountSum = array['accountSum'] == null ? "" : array['accountSum'];						
					var accountLastUpdate = array['accountLastUpdate'] == null ? "" : array['accountLastUpdate'];
					// proposal
					var proposalId = array['proposalId'] == null ? "" : array['proposalId'];
					var proposalInvAmount = array['proposalInvAmount'] == null ? "" : array['proposalInvAmount'];					
					var proposalCurrency = array['proposalCurrency'] == null ? "" : array['proposalCurrency'];
					var proposalStatus = array['proposalStatus'] == null ? "" : array['proposalStatus'];					
					var proposalLastUpdate = array['proposalLastUpdate'] == null ? "" : array['proposalLastUpdate'];
					
					// portfolio
					var holdId = array['portfolioHoldId'] == null ? "" : array['portfolioHoldId'];
//					var portfolioId = array['portfolioId'] == null ? "" : array['portfolioId'];	
					var planId = array['planId'] == null ? "" : array['planId'];	
					var planBuy = array['planBuy'] == null ? "" : array['planBuy'];		
					var planSell = array['planSell'] == null ? "" : array['planSell'];					
					var portfolioCurrency = array['portfolioCurrency'] == null ? "" : array['portfolioCurrency'];
					var planStatus = array['planStatus'] == null ? "" : array['planStatus'];					
					var planLastUpdate = array['planLastUpdate'] == null ? "" : array['planLastUpdate'];
						
					// account
					var accountId = array['investorId'] == null ? "" : array['investorId'];
					var accountNo = array['accountNo'] == null ? "" : array['accountNo'];
					var distributorId = array['distributorId'] == null ? "" : array['distributorId'];
//					var companyName = array['companyName'] == null ? "" : array['companyName'];
					var logofile = array['logofile'] == null ? "" : array['logofile'];
					var openTime = array['openTime'] == null ? "" : array['openTime'];
					
					// kyc
					var rpqExpireDays = array['rpqExpireDays'] == null ? "-" : array['rpqExpireDays'];
					var docExpireDays = array['docExpireDays'] == null ? "-" : array['docExpireDays'];
					
					
					var bottomCSS = '';
					var statusColor = '';
					var statusName = '';
					var careMore = '';
					
					var holdListContent = '';
					
					var salesContent = '';
					var ifFresh = '';// 是否新客户
					var ifAlert = '';// 持仓收益涨跌提示
					
					
					switch(clientStatus){
					case 'customer-care':
						var holdList = array['holdList'];

						holdId = holdList[0]['portfolioHoldId']==null?"":holdList[0]['portfolioHoldId'];
						var marketValue = holdList[0]['totalMarketValue'];
						var returnRate = holdList[0]['totalReturnRate'];
						var baseCurrency = holdList[0]['baseCurrency'];
						var ascAlert = holdList[0]['ascAlert'];
						var descAlert = holdList[0]['descAlert'];
						var ifSummary = holdList[0]['ifSummary']==null?"":holdList[0]['ifSummary'];
						
						var holdCss = 'price_positive';
						var tempValue = returnRate.replace('%','');
						tempValue = parseFloat(tempValue);
						if(displayColor == '1'){
							holdCss = tempValue < 0 ? 'price_negative':(tempValue > 0 ?'price_positive':'price_zero');
						}else{
							holdCss = tempValue < 0 ? 'price_positive':(tempValue > 0 ?'price_negative':'price_zero');
						}
						
						ifAlert = (ascAlert == '1'|| descAlert=='1') ? '<span class="pipeline-title-img portfolio-review"></span>':'';
						salesContent = '<div class="sales_contact_content" ifSummary="'+ifSummary+'" holdId="'+holdId+'">'
									+ '		<div style="overflow: hidden;margin:20px 8px 12px 0;">'
									+ '			<div id="marketValue" style="float:left;">'+marketValue+'</div>'
									+ '			<div id="returnRate" class="'+holdCss+'" style="float:right">'+returnRate+'</div>'
									+ '		</div>'
									+ ' 	<div class="content-wrap1">'
									+ ' 		<div style="float:left;">'+langMutilForJs['pipeline.content.care.aum']+' ( '+baseCurrency+' )</div>'
									+ ' 		<div style="float:right">'+langMutilForJs['pipeline.content.care.totalReturn']+'</div>'
									+ '		</div>'
									+ '</div>';
						
						$.each(holdList, function (i, n) {
							if(i==0) return true;
							var subPortfolioHoldId = n['portfolioHoldId'];
							var subPortfolioName = n['portfolioName'];
							var subMarketValue = n['totalMarketValue'];
							var subReturnRate = n['totalReturnRate'];
							var subBaseCurrency = n['baseCurrency'];
							var subAscAlert = n['ascAlert'];
							var subDescAlert = n['descAlert'];

							var subIfAlert = (subAscAlert == '1'|| subDescAlert=='1') ? '<span class="pipeline-title-img"></span>':'';
							
							var subHoldCss = 'price_positive';

							tempValue = subReturnRate.replace('%','');
							tempValue = parseFloat(tempValue);
							if(displayColor == '1'){
								subHoldCss = tempValue < 0 ? 'price_negative':(tempValue > 0 ?'price_positive':'price_zero');
							}else{
								subHoldCss = tempValue < 0 ? 'price_positive':(tempValue > 0 ?'price_negative':'price_zero');
							}
							
							careMore = 'pipelin-contents-more';
							holdListContent += '<div class="pipelin-list-eject" style="padding-top:9px;padding-right:12px;border-left:1px solid #c1c1c1;">'
											+ '		<p class="sub-portfolio-name" holdId="'+subPortfolioHoldId+'">'+subPortfolioName+subIfAlert+'</p>'
											+ '		<div class="sales_contact_content">'
											+ '			<div style="overflow: hidden;margin:20px 8px 12px 0;">'
											+ '				<div style="float:left;">'+subMarketValue+'</div>'
											+ '				<div class="'+subHoldCss+'" style="float:right">'+subReturnRate+'</div>'
											+ '			</div>'
											+ '			<div class="content-wrap1">'
											+ '				<div style="float:left;">'+langMutilForJs['pipeline.content.care.aum']+' ( '+subBaseCurrency+' )</div>'
											+ '				<div style="float:right">'+langMutilForJs['pipeline.content.care.totalReturn']+'</div>'
											+ '			</div>'
											+ '		</div>'
											+ '		<div>'
											+ '			<div class="sales_new_email"></div>'
											+ '			<div class="sales_new_slh"></div>'
											+ '		</div>'
											+ '</div>';
						});
						
						break;
					case 'kyc':
						salesContent = '<div class="sales_new_content">'
									+ '		<div class="pipeline-KYC-left" style="float:left;width:49%;border-right:1px solid #ccc">'
									+ '			<div class="rpq-expire-days" style="color:red;font-size:20px;">'+rpqExpireDays+'</div>'
									+ '			<div style="color:#ccc">'+langMutilForJs['pipeline.content.kyc.rpqDate']+'</div>'
									+ '		</div>'
									+ '		<div class="pipeline-KYC-right" style="float:left;width:46%;margin-left:3%;">'
									+ '			<div class="doc-expire-days" style="color:red;font-size:20px;">'+docExpireDays+'</div>'
									+ '			<div style="color:#ccc">'+langMutilForJs['pipeline.content.kyc.docDate']+'</div>'
									+ '		</div>'
									+ ' </div>';
						break;
					case 'open-account':
						if(accountSum =='' || accountSum == '0'){
							ifFresh = '<span> ('+langMutilForJs['pipeline.content.fresh']+')</span>';							
						}
						
						if(openStatus == '0'){
							statusName = langMutilForJs['pipeline.search.status.draft'];
							statusColor = '#fbae0a';
							bottomCSS = ' pipelin-list-bottom_draft';
						}else if(openStatus == '1'){
							statusName = langMutilForJs['pipeline.search.status.confirm']+'('+accountLastUpdate+')';
							statusColor = '#33cc33';
							bottomCSS = ' pipelin-list-bottom_tobe3';
						}else if(openStatus == '2'){
							statusName = langMutilForJs['pipeline.search.status.processing'];
							statusColor = '#72a5eb';
							bottomCSS = ' pipelin-list-bottom_approval';
						}else if(openStatus == '-1'){
							statusName = langMutilForJs['pipeline.search.status.reject'];
							statusColor = '#666';
							bottomCSS = ' pipelin-list-bottom_rejuect';
						}else {
							statusName = '';
						}
						
						salesContent = '<div class="sales_contact_content">'
									+ '		<div style="overflow: hidden;margin:20px 8px 12px 0;"></div>'
									+ '		<p class="openaccount_content" style="color:'+statusColor+'">'+statusName+'</p>'
									+ '</div>';
						break;
					case 'proposal':
						if(proposalStatus == '0'){
							statusName = langMutilForJs['pipeline.search.proposal.status.draft'];
							statusColor = '#fbae0a';
							bottomCSS = ' pipelin-list-bottom_draft';
						}else if(proposalStatus == '1'){
							statusName = langMutilForJs['pipeline.search.proposal.status.processing']+'('+proposalLastUpdate+')';
							statusColor = '#33cc33';
							bottomCSS = ' pipelin-list-bottom_tobe3';
						}else if(proposalStatus == '-1'){
							statusName = langMutilForJs['pipeline.search.proposal.status.reject'];
							statusColor = '#666';
							bottomCSS = ' pipelin-list-bottom_rejuect';
						}else {
							statusName = '';
						}
						//console.log("statusName:"+statusName);
//						alert(statusName);
						salesContent = '<div class="sales_contact_content" proposalId="'+proposalId+'">'
									+ '<div style="overflow: hidden;margin:20px 8px 12px 0;">'
									+ '<div style="float:left;color:#999;width:48%;margin-top:10px;font-size:12px;">AMT('+proposalCurrency+')</div>'
									+ '<div style="float:right;margin-top:10px;">'+proposalInvAmount+'</div>'
									+ '</div>'
									+ '<p style="text-align: center;color:'+statusColor+';font-weight: 200;font-size:14px;">'+statusName+'</p>'
									+ '</div>';
						break;
					case 'portfolio':						
						if(planStatus == '0' || planStatus == '-1' ){
							statusName = langMutilForJs['pipeline.search.status.draft'];
							statusColor = '#fbae0a';
							bottomCSS = ' pipelin-list-bottom_draft';
						}else if(planStatus == '1' || planStatus == '3'){
							statusName = langMutilForJs['pipeline.search.status.confirm']+'('+planLastUpdate+')';
							statusColor = '#33cc33';
							bottomCSS = ' pipelin-list-bottom_tobe3';
						}else if(planStatus == '2'){
							statusName = langMutilForJs['pipeline.search.status.reject'];
							statusColor = '#666';
							bottomCSS = ' pipelin-list-bottom_rejuect';
						}else if(planStatus == '4'){
							statusName = langMutilForJs['pipeline.search.status.processing'];
							statusColor = '#72a5eb';
							bottomCSS = ' pipelin-list-bottom_approval';
						}else {
							statusName = '';
						}
						
						salesContent = '<div class="sales_contact_content" holdId="'+holdId+'" planId="'+planId+'">'
									+ '		<div style="overflow: hidden;margin:0 8px 0px 0;">'
									+ '			<div style="float:left;color:#999;margin-top:10px;font-size:12px;">'+langMutilForJs['pipeline.content.portfolio.buy']+'</div>'
									+ '			<div style="float:right;margin-top:10px;">'+planBuy+'('+portfolioCurrency+')</div>'
									+ '		</div>'
									+ '		<div style="overflow: hidden;margin:6px 8px 12px 0;">'
									+ '			<div style="float:left;color:#999;font-size:12px;">'+langMutilForJs['pipeline.content.portfolio.sell']+'</div>'
									+ '			<div style="float:right;">'+planSell+'('+portfolioCurrency+')</div>'
									+ '		</div>'
									+ '		<p style="text-align: center;color:'+statusColor+';font-weight: 200;font-size:14px;">'+statusName+'</p>'
									+ '</div>';
						break;
					case 'not-yet-invest':
						var imgSrc = (logofile=='')? '':base_root+logofile ;
						var days = langMutilForJs['pipeline.content.days'];
						if(openTime == '0' || openTime=='1') days = days.replace('s','');
						salesContent = '<div class="sales_new_content">'
									+ '		<p>'+accountNo+'</p>'
									+ '		<img style="height:30px;margin-top:4px;" src="'+imgSrc+'">'
									+ '		<p>'+openTime+' '+days+'</p>'
									+ '</div>';
						break;
					default:
						break;
				}
					
											
					
					var dataGroup = array['groupName'] == null ? "" : array['groupName'];
					var dataGroupContent = '';
					if(dataGroup != ''){
						dataGroupContent = 'data-group="'+dataGroup+'"';
					}
					var groupList = dataGroup.split(",");
					var liContent = setGroupListLiContent;
					for(var i=0;i<groupList.length;i++){
						liContent = liContent.replace('>'+groupList[i]+'</li>',' class="set-group-active">'+groupList[i]+'</li>');
					}
								
					                                  
					divContent += '<li class="pipelin-list-contents '+careMore+' swiper-slide" distributorId="'+distributorId+'" openStatus="'+openStatus+'" proposalInvAmount="'+proposalInvAmount+'" proposalStatus="'+proposalStatus+'" planStatus="'+planStatus+'" pinyin="'+pinyin+'" '+dataGroupContent+'>'
								// + '<p class="nickname"><a
								// href="'+base_root+'/front/crm/pipeline/clientDetail.do?customerId='+id+'&customerMemberId='+memberId+'">'+nickName+ifFresh+'</a>'+ifAlert+'</p>'
								+ '<p class="nickname" customerId="'+id+'" customerMemberId="'+memberId+'" accountId="'+accountId+'" >'+nickName+ifFresh+ifAlert+'</p>'
								+ salesContent
								+ '<div class="pipelin-list-bottom'+bottomCSS+'" style="margin:0;width:100%;border:0">'								
								+ '<div style="float:left;margin-left:10px;"><img src="'+base_root+'/res/images/application/pipeline_email.png"></div>'
								+ '<div class="pipelin-bottom-more">'
								+ '<span></span>'
								+ '<span></span>'
								+ '<span></span>'
								+ '<ul class="pipelin-list-more">'
								+ '<li  data-name="createProposal" data-value-saleStageId="'+saleStageId+'" data-value-investorId="'+memberId+'" >'+langMutilForJs['pipeline.list.more.createProposal']+'</li>'
								+ '<li class="pipelin-list-more-line" data-name="openAccount" data-value-investorId="'+memberId+'">'+langMutilForJs['pipeline.list.more.openAccount']+'</li>'
								+ '<li data-name="setAppointment" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >'+langMutilForJs['pipeline.list.more.appointment']+'</li>'
								+ '<li data-name="communication" data-value-saleStageId="'+saleStageId+'" data-value-investorId="'+memberId+'" >'+langMutilForJs['pipeline.list.more.communication']+'</li>'
								+ '<li class="pipelin-list-more-line">'+langMutilForJs['pipeline.list.more.chat']+'</li>'								
								+ '<li data-name="setCharacter" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-list-character" >'+langMutilForJs['pipeline.list.more.character']+'</li>'
								+ '<li data-name="setGroup" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-set-group" >'+langMutilForJs['pipeline.list.more.setGroup']
								+ '	<span class="pipelin-group-arrow"></span>'
			    				+ '	<ul class="set-group-list">'
			    				+ liContent								
			    				+ '	</ul>'
								+ '</li>'

								//+ '<li data-name="delete" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-list-removed">Removed</li>'
								
								+ '</ul>'
								+ '</div>'
								+ '</div>'
								// + '<div class="pipeline_page"></div>'
								+ '<div class="mask-layer">'
								+ '		<div class="touming" style="text-align: center;height:124px;padding-top:40px;color:#fff;background:#bfbfbf">'
								+ '			<p style="font-weight:400;">'+mobileCode+' '+mobileNumber+'</p>'
								+ '			<p style="font-weight:400;">'+email+'</p>'
								+ '		</div>'
								+ '</div>'
								+ holdListContent
								+ '</li>';	
				});
								
				$("."+clientStatus).append(divContent);
				$(".pipelin-contents-more").each(function(){
					var contentsNum = 0;
					$(this).find(".pipelin-list-eject").each(function(index){
						contentsNum++;
						$(this).css("left",228 * contentsNum);
					});
				});
				
				getRemind(7);
				oLoading.hide();
				
//				var swiper= new Swiper('.pipeline-existing .swiper-container', {
//	    			slidesPerView: 'auto',
//	    			preventClicks : false,
//	    			nextButton: '.swiper-button-next',
//	    			prevButton: '.swiper-button-prev',
//				});
				$.each(swiper,function(i,n){
					$('.pipeline-existing .swiper-button-prev').click();
					n.onResize();
					$('.swiper-wrapper').css('transform','translate3d(0px, 0px, 0px)');
				});
			}
		})
	}
	

		
	$(document).on("click",".pipelin-list-contents",function(event){
		$(this).find('.pipelin-bottom-more').removeClass('pipelin-more-show');
		event.stopPropagation();
		var url = '';
		var customerId,accountId,proposalId,proposalStatus;
		var memberId = $(this).children('.nickname').first().attr('customerMemberId');
		var parent = $(this).closest('.swiper-wrapper');
		if(parent.hasClass('sales_new') == true){
			customerId = $(this).children('.nickname').first().attr('customerId');
			url = base_root+'/front/crm/pipeline/clientDetail.do?customerId='+customerId+'&customerMemberId='+memberId;
			location.href=url;
		}else if(parent.hasClass('sales_contact') == true){
			url = base_root+'/front/ifa/info/communicationRecord.do?memberId='+memberId;
			location.href=url;
		}else if(parent.hasClass('sales_proposal') == true){
			proposalId = $(this).children('.sales_contact_content').first().attr('proposalid');
			proposalStatus = $(this).attr('proposalstatus'); // '1';
			if(proposalStatus == '1'){
				url = base_root+'/front/crm/proposal/previewProposal.do?proposalId='+proposalId;
			}else{
				url = base_root+'/front/crm/proposal/createProposalSetOne.do?id='+proposalId+'&memberId='+memberId;
			}
			location.href=url;
		}else if(parent.hasClass('sales_close') == true){
			customerId = $(this).children('.nickname').first().attr('customerId');
			url = base_root+'/front/crm/pipeline/clientDetail.do?customerId='+customerId+'&customerMemberId='+memberId;
			location.href=url;
		}else if(parent.hasClass('customer-care') == true){
			var holdId = $(this).find('.sales_contact_content').first().attr('holdId');
			if(holdId != null && holdId != ''){
				url = base_root+'/front/strategy/info/conservativePortfolio.do?id='+holdId;
				location.href=url;
			}

		}else if(parent.hasClass('kyc') == true){
			accountId = $(this).children('.nickname').first().attr('accountId');
			url = base_root+'/front/investor/myAccountDetail.do?id='+accountId;
			location.href=url;
		}else if(parent.hasClass('open-account') == true){
			accountId = $(this).children('.nickname').first().attr('accountId');
			url = base_root+'/front/investor/accountProgress.do?id='+accountId;
			location.href=url;
		}else if(parent.hasClass('proposal') == true){
			proposalId = $(this).find('.sales_contact_content').first().attr('proposalId');
			proposalStatus = $(this).attr('proposalstatus'); // '1';
			if(proposalStatus == '1'){
				url = base_root+'/front/crm/proposal/previewProposal.do?proposalId='+proposalId;
			}else{
				url = base_root+'/front/crm/proposal/createProposalSetOne.do?id='+proposalId+'&memberId='+memberId;
			}
			
			location.href=url;
		}else if(parent.hasClass('portfolio') == true){			
			
			var orderPlanStatus = $(this).attr('planStatus');
			var orderPlanId;
			if( ",1,2,3,".indexOf(orderPlanStatus)>-1 ){
				orderPlanId = $(this).find('.sales_contact_content').first().attr('planId');
				url = base_root+'/front/tradeMain/previewOrderPlan.do?id='+orderPlanId;
			}else{
//				var holdId = $(this).find('.sales_contact_content').first().attr('holdId');
				orderPlanId = $(this).find('.sales_contact_content').first().attr('planId');
				url = base_root+'/front/tradeMain/orderPlan.do?planId='+orderPlanId;
			}
			
			location.href=url;
		}else if(parent.hasClass('not-yet-invest') == true){
			accountId = $(this).children('.nickname').first().attr('accountId');
			url = base_root+'/front/investor/myAccountDetail.do?id='+accountId;
			location.href=url;
		}
	});
	
	$(document).on("click",".pipelin-list-contents .nickname",function(event){
		event.stopPropagation();
		var customerId = $(this).attr('customerId');
		var memberId = $(this).attr('customerMemberId');
		var url = base_root+'/front/crm/pipeline/clientDetail.do?customerId='+customerId+'&customerMemberId='+memberId;
		// if($(this).find('.mask-layerbox').css('right') == '-134px'){
			location.href=url;
		// };
		
		
	});
	
	
	$(document).on("click",".pipelin-list-contents .pipelin-list-eject",function(event){
		event.stopPropagation();
		var holdId = $(this).find('.sub-portfolio-name').first().attr('holdId');
		var url = base_root+'/front/strategy/info/conservativePortfolio.do?id='+holdId;
		location.href=url;		
	});
	
	$(document).on("click",".pipelin-list-contents .nickname .portfolio-review",function(event){
		event.stopPropagation();			
	});
	
	
	
	function getExistingDataList(){	
		getExistingList('customer-care');
		getExistingList('open-account');
		getExistingList('proposal');
		getExistingList('portfolio');
		getExistingList('not-yet-invest');
		getExistingList('kyc');
	}
	

	// getExistingDataList();
	
	
 	// 绑定Portfolio数据
	function getPortfolioData(customerMemberId){
		// var customerMemberId = getUrlParam('customerMemberId');
		var divContent = "";
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/portfolioListJson.do?datestr='+new Date().getTime(),
			data : {
				'customerMemberId': customerMemberId,'page':1,'rows':100,'sort':'l.createTime','order':'DESC'
			},
			async : false,
			success : function(json) {
								
				// var divContent = "";
				var list = json.rows;			
				$.each(list, function (index, array) { // 遍历json数据列
					// alert('customerMemberId:'+customerMemberId);
					var portfolio_name = array[0]['portfolioName'] == null ? "" : array[0]['portfolioName'];
					var base_currency = array[0]['baseCurrency'] == null ? "" : array[0]['baseCurrency'];
					// var create_time = array[0]['createTime'] == null ? "" :
					// array[0]['createTime'];
					var return_rate = array[1]['returnRate'] == null ? "" : array[1]['returnRate'];
					// var total_return = array[1]['totalReturn'] == null ? "" :
					// array[1]['totalReturn'];
					var total_amount = array[1]['totalAmount'] == null ? "" : array[1]['totalAmount'];
					
					var return_rate_per;
					if(parseFloat(return_rate) != NaN ){
						// if(parseFloat(return_rate) < 0){
						// var toReview = "<span>To review</span>";
						// }
						return_rate_per = parseFloat(return_rate)*100.0 + "%";
					}
					

					var flag = "";
					if(parseFloat(total_amount) != NaN && parseFloat(total_amount) >= 0){
						flag = "";
					}else{
						flag = "-drop";
					}
									
                                    
					divContent += '<div class="pipelin-list-eject">'
                        		+ '<p class="pipelin-eject-name">'+portfolio_name+'</p>'
                        		+ '<p class="pipelin-eject-price'+flag+'">'+total_amount+'<span class="pipelin-eject-cur">'+base_currency+'</span></p>'
                        		+ '<p class="pipelin-eject-percentage'+flag+'">'+return_rate_per+'</p>'
                                + '</div>';
				});
						
			}
		})
// alert( divContent);
		return divContent;
	}

	
	// 动态输出内容绑定事件
	$("body").on("click",".pipelin-list-more li",function(){
		var dataName = $(this).attr("data-name");
		// var saleStageId = $(this).attr("data-value-saleStageId");
		var customerId = $(this).attr("data-value-customerId");
		var investorId,url;
		// alert("dataName:"+dataName);
		if("setImportant" == dataName ){
			updateClient(dataName,customerId);
		}else if ( "delete" == dataName ){
			var saleStageId = $(this).attr("data-value-saleStageId");
			layer.confirm(langMutilForJs['global.confirm.delete'], {
				title:"",
				  btn: [langMutilForJs['global.true'],langMutilForJs['global.false']] // 按钮
				}, function(){
					layer.closeAll('dialog');
					updateClient(dataName,customerId,saleStageId);
				}, function(){
				  
				});
			
		}else if( "setRemark" == dataName ){
// layer.msg("setRemark");
			var remark = $(this).attr("data-value-remark");

			 layer.open({
				  type: 1,
				  area: ['500px', '240px'],
				  shadeClose: true, // 点击遮罩关闭
				  content: '\<\div style="padding:20px;"><textarea id="txtRemark" style="width:100%;"  rows="6">'+remark+'</textarea>\<\/div>',
				  btn: ['保存'],
				  yes: function(index,layero){
					  var remark = $("#txtRemark").val();
// layer.msg("save remark:"+remark);
					  updateClient(dataName,customerId,remark);
					  layer.close(index);
				  }
				  });
			 
		}else if ( "openAccount" == dataName ){
			investorId = $(this).attr("data-value-investorId"); 
			url = base_root + "/front/investor/accountStart.do?indId="+investorId;
			location.href=url;
		}else if ( "createProposal" == dataName ){
			investorId = $(this).attr("data-value-investorId"); 
			url = base_root + "/front/crm/proposal/createProposalSetOne.do?memberId="+investorId;
			location.href=url;
		}else if ( "communication" == dataName ){
			investorId = $(this).attr("data-value-investorId"); 
			url = base_root + "/front/ifa/info/communicationRecord.do?memberId="+investorId;
			location.href=url;
		}
	});
	
	function updateClient(updateType,customerId,saleStageId){
		// alert("customerId:"+customerId+",saleStageId:"+saleStageId);
		var clientType = $("#hdClientType").val();
		
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/updateDetail.do?datestr='+new Date().getTime(),
			data : {
				'updateType':updateType,'customerId':customerId,'saleStageId':saleStageId
			},
			success : function(json) {
				if( json.result == true ){
					// getClientList(saleStageId);
					if( "0" == clientType ){
						layer.msg("Set Success",{icon:2});	
						getProspectDataList();
						// bindClient(saleStageId);
					}else{
						getExistingDataList();
						// getExistingClientDataList();
						// bindExistingClient(clientStatus);
					}		
					
				}							
			}
		})		
	}
	
	
    $(document).on("click",".ifa-home-mask",function(){
    	$("#MemberInformation").removeClass("investment-hide");
    });
    $(document).on("click",".investment-close-ico",function(){
    	$("#MemberInformation").addClass("investment-hide");
    });
    
    // add wwluo Character Setting
    $(document).on("click",".pipelin-list-character",function(){
    	$('#character-choose-list').empty();
    	$('#hobbytypes-choose-list').empty();
    	$('#character-selected-option').find('li').remove();
    	$('#hobby-selected-option').find('li').remove();
    	// $("#CharacterSetting").removeClass("investment-hide");
    	$("#CharacterSetting").show();
    	var customerId = $(this).data('valueCustomerid');
    	var url = base_root + '/front/crm/pipeline/characterSetting.do?dateStr=' + new Date().getTime();
    	$.ajax({
    		type:'post',
    		data:{customerId:customerId},
    		url:url,
    		success:function(result){
    			// //console.log(result);
    			if(result.flag){
    				var customer = result.customer,
    					characters = result.clientCharacters,
    					hobbyTypes = result.hobbyTypes;
    				// all
    				if(characters != null){
    					var cahrachersLi = '';
    					$.each(characters,function(i,n){
    						cahrachersLi +=
    							'<li data-code="'+n.itemCode+'" data-value="'+n.name +'">'
    							+ n.name 
    							+'<span class="character-list-close"></span></li>';
    					});
    					$('#character-choose-list').append(cahrachersLi);
    				}
    				if(hobbyTypes != null){
    					var hobbyTypesLi = '';
    					$.each(hobbyTypes,function(i,n){
    						hobbyTypesLi +=
    							'<li data-code="'+n.itemCode+'" data-value="'+n.name +'">'
    							+ n.name 
    							+'<span class="character-list-close"></span></li>';
    					});
    					$('#hobbytypes-choose-list').append(hobbyTypesLi);
    				}
    				// select
    				var characterCode = customer.character,
    					character = customer.characterName,
    					hobbyCode = customer.hobby,
    					hobby = customer.hobbyName,
    					selected = '';
    				if(character != null && character.indexOf(',')>-1){
    					character = character.split(',');
    					selected = '';
    					$.each(character,function(i,n){
    						selected +=
	    						'<li data-code="'+(characterCode.split(','))[i]+'" data-value="'+n+'">'
	    						+ n
	    						+'<span class="character-list-close"></span></li>';
    					});
    					$('#character-selected-option').append(selected);
    				}else if(character != null && character.indexOf(',')<0){
    					$('#character-selected-option').empty().append(
    							'<li data-code="'+characterCode+'" data-value="'+character+'">'
	    						+ character
	    						+'<span class="character-list-close"></span></li>');
    				}
    				if(hobby != null && hobby.indexOf(',')>-1){
    					hobby = hobby.split(',');
    					selected = '';
    					$.each(hobby,function(i,n){
    						selected +=
    							'<li data-code="'+(hobbyCode.split(','))[i]+'" data-value="'+n+'">'
    							+ n
    							+'<span class="character-list-close"></span></li>';
    					});
    					$('#hobby-selected-option').append(selected);
    				}else if(hobby != null && hobby.indexOf(',')<0){
    					$('#hobby-selected-option').empty().append(
    							'<li data-code="'+hobbyCode+'" data-value="'+hobby+'">'
	    						+ hobby
	    						+'<span class="character-list-close"></span></li>');
    				}
    				$('#txtSpecial').val(customer.special);
    				$('#txtDislike').val(customer.dislike);
    				$('.character-button-save').attr('data-customer-id',customer.id);
    			}
    		}
    	});
    });
    // add wwluo Save Character Setting
    $(document).on("click",".character-button-save",function(){
    	var customerId = $(this).data('customerId'),
	    	special = $('#txtSpecial').val(),
			dislike = $('#txtDislike').val(),
    		character = '',
    		hobby = '';
    	// character
    	$('#character-selected-option >li').each(function(){
    		var code = $(this).data('code');
    		if(typeof(code) == 'undefined'){
    			code = '{'+$(this).data('value')+'}';
    		}
    		character += code + ',';
    	});
    	if(character != ''){
    		character = character.substring(0,character.length-1);
    	}
    	// hobby
    	$('#hobby-selected-option >li').each(function(){
    		var code = $(this).data('code');
    		if(typeof(code) == 'undefined' || code.indexOf('{')>-1){
    			code = '{'+$(this).data('value')+'}';
    		}
    		hobby += code + ',';
    	});
    	if(character != ''){
    		hobby = hobby.substring(0,hobby.length-1);
    	}
    	var url = base_root + '/front/crm/pipeline/saveCharacterSetting.do?dateStr=' + new Date().getTime();
    	$.ajax({
    		type:'post',
    		url:url,
    		data:{
    			customerId:customerId,
    			special:special,
    			dislike:dislike,
    			character:character,
    			hobby:hobby
    		},
    		success:function(result){
    			if(result.flag){
    				layer.msg("Save Success",{icon:2});	
    				$("#CharacterSetting").addClass("investment-hide");
    				setTimeout(function(){$("#CharacterSetting").hide();layer.closeAll();},800); 
    			}
    		}
    	});
    	
    });
    // add end
    
    $(document).on("click",".character-close-ico, .character-button-close",function(){
    	$("#CharacterSetting").hide();
    	// $("#CharacterSetting").addClass("investment-hide");
    });

    $(".character-choose-list").on("click","li",function(){
    	$(this).parents(".character-setting-rows").find(".character-setting-list").append($(this));
    });
    $(".character-setting-list").on("click",".character-list-close",function(){
    	$(this).parents(".character-setting-rows").find(".character-choose-list").append($(this).parent());
    });

    $(".character-setting-add").on("blur",function(){
    	if($(this).val().replace(/(^\s*)|(\s*$)/g, "") !=''){
    		var StrHtml = '<li data-value='+ $(this).val() +'>'+ $(this).val() +' <span class="character-list-close"></span></li>'
        	$(this).before(StrHtml);
        	$(this).val("");
    	}
    });

	// 添加或编辑分组
    $("body").on("click",".my-group-list .group-list-edit",function(){

		var groupId = $(this).parents(".my-group-li").find("input").attr("data-value"),
		groupName = $(this).parents(".my-group-li").find("input").val(),
    		_bol = true,
    		self = $(this);

    	if(self.parents(".my-group-li").hasClass("my-group-edit")){
    	
	    	$("#my-group-list .group-list-word").each(function(){

	    		if( $(this).val() == groupName && !$(this).parents(".my-group-li").hasClass("my-group-edit")){
	    			
	    			layer.msg(langMutilForJs['pipeline.clientType.exists'],{icon:1});
	    			
	    			_bol = false;
	    		};
	    	});

	    	if( groupName == ""){

	    		layer.msg(langMutilForJs['pipeline.clientType.empty'],{icon:1});
	    			
	    		_bol = false;
	    	}
	    	
    	}
    	if(_bol){
			
	    	self.parents(".my-group-li").toggleClass("my-group-edit");

	    	if(self.parents(".my-group-li").hasClass("my-group-edit") ){

	    		self.parents(".my-group-li").find(".group-list-word").removeAttr("readonly");

	    		self.attr("data-value",self.parents(".my-group-li").find("input").val());
	    		
// layer.msg("111");

	    	}else{
// layer.msg("222");
    			saveCustomerGroup(groupId,groupName);
    			
	    		$(".pipelin-choice-group").each(function(){
	    			
	    			if($(this).html() == self.attr("data-value")){
	    				$(this).html( groupName );
	    				$(".set-group-list li").each(function(){
	    					if($(this).html() == self.attr("data-value")){
	    						$(this).html( groupName );
	    					}
	    				});

	    				$(".pipelin-list-contents").each(function(){

	    					var _gropu = $(this).attr("data-group");

							if(!_gropu) _gropu = '';

							if ( _gropu.indexOf( self.attr("data-value") ) >= 0 ) {


		    					var ArrTemp = _gropu.split(",");

		    					var ArrIndex = ArrTemp.indexOf( self.attr("data-value") );

					    		ArrTemp.splice(ArrIndex, 1 , groupName);

					    		ArrTemp = ArrTemp.join(",");

					    		$(this).attr("data-group",ArrTemp);
	    					}
	    					
	    				});
	    				_bol = false;
	    			}
	    		});
	    		if(_bol){
	    			$(".pipelin-new-group").before('<li class="pipelin-choice-group">'+ groupName +'</li>');
	    			$(".set-group-list").each(function(){
	    				$(this).append('<li>'+ groupName +'</li>')
	    			});
	    			getCustomerGroupList();
	    			getProspectDataList();
	    			getExistingDataList();
	    		}

	    		self.parents(".my-group-li").find(".group-list-word").attr("readonly","readonly");
	    		self.removeAttr("data-value");
	    	}
    	};
    });
    
	var	myScroll2 = new IScroll('#group-list-wrapper', {
        scrollbars: true, 
        interactiveScrollbars: true, 
        shrinkScrollbars: 'scale', 
        fadeScrollbars: true, 
        click: true,
        scrollX: false,
         scrollY: true, 
        preventDefault: true

  	});
    // delete group
    $(".my-group-list").on("click",".group-list-close",function(){
    	var groupId = $(this).parents(".my-group-li").find("input").attr("data-value");
    	var self = $(this).parents(".my-group-li").find("input").val();
    	
    	layer.confirm(langMutilForJs['pipeline.clientType.delete.confirm'], {
			title:false,  
    		btn: [langMutilForJs['pipeline.clientType.delete.yes'],langMutilForJs['pipeline.clientType.delete.no']] // 按钮
			}, function(){
				layer.closeAll('dialog');
				
				deleteCustomerGroup(groupId);
		    	
		    	$(this).parents(".my-group-li").remove();

		    	$(".pipelin-choice-group").each(function(){

					if($(this).html() == self){
						$(this).remove();
					};

				});
		    	myScroll2.refresh();
			}, function(){
			  
			});
    	
    	
    });
    $(".group-title-close").on("click",function(){

    	$(".my-group-wrapper").hide();

    })

//		document.addEventListener('touchmove', function (e) { e.preventDefault(); 
//
//   e.stopPropagation(); }, false);

	$("body").on("click",".pipelin-new-group",function(){

		$(".my-group-wrapper").show();

	});


    $(".group-list-button").on("click",function(){

    	var _Dom = $(".my-group-list")
    		
    	,	StrLenght = _Dom.children().length;

    	if( _Dom.find(".my-group-edit").length > 0){

    		layer.msg(langMutilForJs['pipeline.clientType.saveInfo'],{icon:1});
		    return;
	    }
    	var StrName = ""
    	,	StrTemp = [];

    	$("#my-group-list .group-list-word").each(function(){  		
    		StrTemp.push($(this).val());
    	});
    	
    	if(StrLenght>=5){
    		layer.msg(langMutilForJs['pipeline.clientType.atmost'],{icon:1});
    		return;
    	}

    	for (var i = 0 ;i < StrLenght + 1; i++){
 
    		var Strsum = Number(i) + 1;

    		StrName = langMutilForJs['pipeline.clientType.prefix'] + Strsum;
    		if ( StrTemp.indexOf(StrName) < 0){
    			break;
    		}
    	}


    	
    	var StrHtml = '<li class="my-group-li my-group-edit">';

    		StrHtml+= '<input type="text" maxlength="18" class="group-list-word" value="'+ StrName +'">';
    		
    		StrHtml+= '<div class="group-list-ico"><span class="group-list-edit"></span><span class="group-list-close"></span></div></li>'
    		
    		_Dom.append(StrHtml);

    		var _input = _Dom.children().eq( StrLenght - 1 ).find("input")

    		,	_val = _input.val();

    			_input.val("").focus().val(_val); 

    	var StrHeight = StrLenght * (Number($(".my-group-li").height()) + 5);

    		if( _Dom.height() > $(".group-list-wrapper").height()){

    			myScroll2.scrollTo(0, -StrHeight , 0,true);

    		};

    		myScroll2.refresh()
    		
    });

    $(document).on("click",".set-group-list li",function(){

    	var self = $(this)

    	,   ArrTemp = []

    	,   StrGroup = self.parents(".pipelin-list-contents").attr("data-group");

    	self.siblings().removeClass("set-group-active").end().addClass("set-group-active");
// self.toggleClass("set-group-active");

    	if( self.hasClass("set-group-active") ){
// if(StrGroup){
// //单个分组
// // ArrTemp = StrGroup.split(",");
// ArrTemp.push( self.html() );
// ArrTemp = ArrTemp.join(",");
// }else{
// ArrTemp.push( self.html() );
// ArrTemp = ArrTemp.join(",");
// };
	    	
	    	ArrTemp.push( self.html() );
    		ArrTemp = ArrTemp.join(",");
    	}else{
    		if(StrGroup){
	    		ArrTemp = StrGroup.split(",");
	    		var ArrIndex = ArrTemp.indexOf( self.html() );
	    		ArrTemp.splice(ArrIndex, 1);
	    		ArrTemp = ArrTemp.join(",");
	    	};
    	}
    	

    	var customerId = self.parents("li .pipelin-set-group").eq(0).attr("data-value-customerid");
    	var groupId = self.attr("data-group-id");
// layer.msg(customerId+','+groupId);
    	saveCustomerGroupRelationship(groupId,customerId);
    	self.parents(".pipelin-list-contents").attr("data-group",ArrTemp);    	
    });
	

    
    function saveCustomerGroup(groupId,groupName){
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/saveCrmGroup.do?datestr='+new Date().getTime(),
			data : {
				'groupId':groupId,'groupName':groupName
			},
			success : function(json) {
				if(json.result == true){
					// layer.msg("ok");
					getCustomerGroupList();
				}							
			}
		})
    }
    
    function deleteCustomerGroup(groupId){
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/deleteCrmGroup.do?datestr='+new Date().getTime(),
			data : {
				'groupId':groupId
			},
			success : function(json) {
				if(json.result == true){
					// layer.msg("ok");
					getCustomerGroupList();
				}							
			}
		})
    }
    
    function saveCustomerGroupRelationship(groupId,customerId){
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/saveCrmGroupRelationship.do?datestr='+new Date().getTime(),
			data : {
				'groupId':groupId,'customerId':customerId
			},
			success : function(json) {
				if(json.result == true){
					layer.msg("Set success",{icon:2});
				}							
			}
		})
    }
    
    $('body').on("click",".information-more-ico",function(){
    	$(this).parents(".information-plan-rows").toggleClass("information-plan-hide");
    });  
    
    // 展开-调整客户状态
    $(document).on('click','.pipeline_page',function(e){
    	e.stopPropagation();
    	$('.pipelin-list-contents').removeClass('kejiande');
    	$(this).closest('li').css({"width":220,"padding-right" : 12});
    	$(this).closest('li').find('.mask-layer').css('display','block');
    	$(this).closest('li').find('.mask-layer .mask-layerbox').stop().animate({'right':0},500);
    	
    });
    
    // 收缩-调整客户状态
    $('body').on('click','.mask-layerleft',function(e){
    	e.stopPropagation();
    	$(this).closest('.mask-layerbox').animate({'right':'-134px'},500,function(){
    		$(this).closest('.mask-layer').css('display','none')
    	});
    });
    $('body').on('mouseleave','.mask-layer',function(e){
    	e.stopPropagation();
    	$(this).find('.mask-layerbox').animate({'right':'-134px'},500,function(){
    		$(this).closest('.mask-layer').css('display','none')
    	});
    	if($(this).find('.mask-layerbox').css('right') == '-134px' || $(this).closest('.pipeline-div-active').hasClass('pipeline-existing')){
			$(this).css('display','none');
			$(this).closest('.pipelin-list-contents').find('.touming').css('display','none');
			$(this).closest('.pipelin-list-contents').find('.touming').css('opacity','0');
		};
    });
    // 调整客户状态
    $('body').on('click','.mask-layerright li',function(){
    	// $(this).addClass('groupingac').siblings().removeClass('groupingac');
    	if($(this).hasClass('groupingac') == false ){
    		//console.log('other');
    		var customerId = $(this).attr("customerId");
    		var saleStageId = $(this).attr("saleStageId");
    		updateClient("setStageId",customerId,saleStageId);
    	}
    });
    
    $("body").on("click",".addclienttype",function(){
    	$(".my-group-wrapper").show();
    	myScroll2.refresh();
	});
	
	if($('.pipelinenew-Client_Typeac').index()==0){
		$('.pipelinenew-Client_Typeac').addClass('pipelinenew-Client_Typeac1');
	}else{
		$('.pipelinenew-Client_Typeac').removeClass('pipelinenew-Client_Typeac1');
	};
    // 分组类型选择
	$('body').on('click','.pipelinenew-Client_Type li',function(){
		$(this).addClass('pipelinenew-Client_Typeac').siblings().removeClass('pipelinenew-Client_Typeac');
		if($('.pipelinenew-Client_Typeac').index()==0){
			$('.pipelinenew-Client_Typeac').addClass('pipelinenew-Client_Typeac1');
		}else{
			$('.pipelinenew-Client_Type li').removeClass('pipelinenew-Client_Typeac1');
		};
		var self = $(this);		
		if($(this).index() == 0){
			$(".pipeline-div .pipelin-list-contents").show();
		}
		else{
			$(".pipelin-list-contents").hide();

			$(".pipelin-list-contents").each(function(index){
				var groupName = $(this).attr("data-group");
				if(!groupName) groupName = '';

				if ( (','+ groupName+',').indexOf( ','+ self.html()+',' ) >= 0 ) {
					$(this).show();
				}
			});
		}

		
	});
	
	$(".pipelin-list-swiper").on("mouseenter",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").show();
    });
    $(".pipelin-list-swiper").on("mouseleave",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").hide();
    });
    
    $('#Appointment').on('click',function(){
    	$('#add_Schedule').addClass('ifa-space-active');
    });
    
    $('.repeat_typebox input').on('click',function(){
    	if($('.repeat_typebox input:checked').val()=='Repeat_events'){
    		$('.repeat_typecon').removeClass('repeat_typecon_none');
    	}else{
    		$('.repeat_typecon').addClass('repeat_typecon_none');
    	}
    });
    
    $('#btnCallBack').on('click',function(){
    	// alert('ok1');
    	getExistingList('customer-care');
    });
    
});