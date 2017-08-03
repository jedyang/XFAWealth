define(function(require) {
	var $ = require('jquery');
	require('pagination');
	require('layui');
	require("scrollbar");
	require('laydate');
	
	sessionStorage.removeItem("clientdetailtab");
	
	var selector =  require('ifaSelectUser');
	selector.init();
	var FILTER = (function(){
		var period = '',
			fromDate = '',
			toDate = '',
			status = '',
			keyWord = '',
			clients = '',
			page = 0,
			sort = 'c.lastUpdate',
			order = 'desc';
		return {
			period:period,
			fromDate:fromDate,
			toDate:toDate,
			status:status,
			keyWord:keyWord,
			clients:clients,
			page:page,
			sort:sort,
			order:order
		};
	})();

	if(_checkList!=undefined && _checkList!=""){
		$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
	}
 	$(".recommend-date-ok").on("click",function(){
        if($("#fromDate" ).val() != "" && $("#toDate").val() != ""){
        	var selection_criterialenght = $(".selection_criteria li").length;
            for(var i = 0; i < selection_criterialenght ;i++){
                    if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
                        $(".selection_criteria li").eq(i).remove();
                    }
            }
            $(".funds_title_selection").before('<li data-value="' + $("#fromDate").val() + ' to ' + $("#toDate").val()  + '" data-name="' + $(this).attr("data-name") + '">' + $("#fromDate").val() + ' to ' + $("#toDate").val()  + '<span class="selection_delete"></span></li>')
            Initialization();
            FILTER.fromDate = $('#fromDate').val();
			FILTER.toDate = $('#toDate').val();
			FILTER.period = "";
			initProposal();
        }else{
        	layer.msg(langMutilForJs['msg.selectDate']);
        }

    });
    if($('.fund_choice_active').index()==0){
			$('.fund_choice_active').addClass('fund_choice_active2');
		}else{
			$('.fund_choice_active').removeClass('fund_choice_active2');
		};
	$(".funds_choice li").on("click",function(){
		if($(this).parent().hasClass("funds_logo_b")){
			return;
		}
		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
        if($(this).hasClass("recommend-date-choice") && $(this).hasClass("fund_choice_active")){
            $(this).parents(".funds_choice").find(".recommend-other-wrap").addClass("recommend-other-block");
            return;
        }else if($(this).parents(".funds_choice").find(".recommend-other-wrap").length > 0){
        	FILTER.fromDate = '';
			FILTER.toDate = '';
			FILTER.period = $(this).parents(".funds_choice").find(".fund_choice_active").data('value');
            $(this).parents(".funds_choice").find(".recommend-other-wrap").removeClass("recommend-other-block");
        }
		var selection_criterialenght = $(".selection_criteria li").length;
		for(var i = 0; i < selection_criterialenght ;i++){
				if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
					$(".selection_criteria li").eq(i).remove();
				}
		}
		
		if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
			$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>');
		}
		Initialization();
		// bindProposal();
		//过滤 period、status
		$('.selection_criteria >li').each(function(){
			if('status' == $(this).data('name')){
				FILTER.status = $(this).data('value');
			}
		});
		if($(this).index()==0){
			$(this).closest('ul').find('.funds_all').addClass('fund_choice_active2');
		}else{
			$(this).closest('ul').find('.funds_all').removeClass('fund_choice_active2');
		};
		initProposal();
	});
	//搜索全部
	$('.funds_all').click(function(){
		if('period' == $(this).data('name')){
			$('#fromDate').val('');
			$('#toDate').val('');
			FILTER.fromDate = '';
			FILTER.toDate = '';
			FILTER.period = '';
			initProposal();
		}else if('status' == $(this).data('name')){
			FILTER.status = '';
			initProposal();
		}
	});
	//执行清除方案点击操作
	$(".funds_title_selection").on("click",function(){
		$(".selection_criteria li").remove();
		$(".fund_logo_active").removeClass("fund_logo_active");
		$(".fund_choice_active").removeClass("fund_choice_active");
		$(".funds_all").addClass("fund_choice_active");
		$('#fromDate').val('');
		$('#toDate').val('');
		FILTER.fromDate = '';
		FILTER.toDate = '';
		FILTER.period = '';
		FILTER.status = '';
		bindProposal();
		$(this).hide();
	});	
	//搜索条件取消点击
	$(".selection_criteria").on("click",".selection_delete",function(){
		var selectName = $(this).parent().data('name');
		$(this).parent().remove();
		$('.funds_choice').each(function(){
			if($(this).data('name') == selectName){
				$(this).children().eq(0).trigger('click');
				return false;
			}
		});
	});	
	/**
	 * 显示清除所有搜索条件按钮
	 * 
	 */
	function selection(){
		var _thisLenght =  $(".selection_criteria").children().length
		if( _thisLenght != 1  ){
			$(".funds_title_selection").css('display','inline-block');
		}else{
			$(".funds_title_selection").css('display','none');
		}
	}	
	
	/**
	 * 实例化操作
	 * 
	 * */
	function Initialization(){
		selection();
	}
	// 更多选择
	$("#yfunds_hidde_title").on("click",function(){
		if($(this).parent().hasClass("funds_more_wrap_show")){
			$(this).parent().removeClass("funds_more_wrap_show");
			 $(".fund_more_content").stop().animate({ 
		    	height: "0px"
			  }, 300 );
	 		$(this).find("span").eq(0).css("display","inline-block");
	 		$(this).find("span").eq(1).css("display","none");
		}else{
			$(this).parent().addClass("funds_more_wrap_show");
			$(".fund_more_content").stop().animate({ 
		    	height: "100%"
			 }, 300 );
			$(this).find("span").eq(1).css("display","inline-block");
	 		$(this).find("span").eq(0).css("display","none");
		}
	});
	/**
	 * 绑定proposal数据
	 */
	bindProposal();
	function bindProposal(){
		var url = base_root+'/front/crm/proposal/getProposal.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			data:
				{
					page:FILTER.page+1,
					period:FILTER.period,
					fromDate:FILTER.fromDate,
					toDate:FILTER.toDate,
					status:FILTER.status,
					keyWord:$("#txtKeyWord").val(),
					clients:FILTER.clients,
					sort:FILTER.sort,
					order:FILTER.order
				},
			url:url,
			success:function(result){
					var table = result.rows;
					var total = result.total;
					var dateFormat=$("#dateFormat").val();
					$('#dataTotal').text(total);
					if(table != null){
						var html = '';
						$.each(table,function(i,n){
							var nickName = n.customerName == null? '' : n.customerName,
							    memberId = n.member == null? '' : n.member.id,
							 	proposalName = n.proposalName == null? '' : n.proposalName,
								initialInvestAmount = n.initialInvestAmount == null? '' : n.initialInvestAmount,
								lastUpdate = n.lastUpdate == null? '' : n.lastUpdate,
							    clientId=n.customerId==null?"":n.customerId,
								status = '',
								actionHtml = '',
								currency=n.currencyName==null?'':n.currencyName;
							initialInvestAmount=formatCurrency(initialInvestAmount);
							var pUrl="#";
							if('1' == n.status){
								status = langMutilForJs['clients.proposal.status.1'];
								pUrl=base_root+"/front/crm/proposal/previewProposal.do?proposalId="+n.id;
							}
							else if('-1' == n.status){
								status = langMutilForJs['clients.proposal.status.-1'];
								pUrl=base_root+"/front/crm/proposal/createProposalSetOne.do?edit=1&id="+n.id+'&memberId='+memberId;
							}else if('0' == n.status){
								status = langMutilForJs['clients.proposal.status.0'];
								actionHtml = '<a class="del-proposal" data-proposal="'+n.id+'" href="javascript:void(0)" ><img src="'+base_root+'/res/images/strategy/porfolio_del.png"/></span></a>';
								pUrl=base_root+"/front/crm/proposal/createProposalSetOne.do?edit=1&id="+n.id+'&memberId='+memberId;
							}else if('-2' == n.status){
								status = langMutilForJs['clients.proposal.status.-2'];
								pUrl=base_root+"/front/crm/proposal/createProposalSetOne.do?edit=1&id="+n.id+'&memberId='+memberId;
							}else if('2' == n.status){
								status =langMutilForJs['clients.proposal.status.2'];
								pUrl=base_root+"/front/crm/proposal/previewProposal.do?proposalId="+n.id;
							}
							if(lastUpdate!=undefined && lastUpdate!='')
								lastUpdate=formatDate(lastUpdate,dateFormat);
							html += 
								'<tr>'
								+'<td class="funds_tables_fnames">'
								+'<a href="'+base_root+'/front/crm/pipeline/clientDetail.do?customerId='+clientId+'" >'
								+ nickName
								+'</a>'
								+'</td>'
								+'<td style="text-align:left;" ><a class="ifa-a-href" target="_self" href="'+pUrl+'" >'
								+ proposalName
								+'</a></td>'
								+'<td class="funds_search_tdcenter">'
								+ initialInvestAmount+"  "+currency
								+'</td>'
								+'<td class="funds_search_tdcenter">'
								+ status
								+'</td>'
								+'<td class="funds_search_tdcenter">'
								+ lastUpdate
								+'</td>'
								+'<td class="funds_search_tdcenter">'
								+ actionHtml
								+'</td>'
								+'</tr>';
						});
						$('.no_list_tips').hide();
						$('.strategies_List tbody tr:gt(0)').empty();
						$('.strategies_List tbody').append(html);
						
						//删除
						$('.del-proposal').on('click',function(){
							var proposalId = $(this).data('proposal');
							
							layer.confirm(globalProp['global.alert.del'], {
								  title:' ',
								  btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
							},function(index){
								$.ajax({
									url:base_root+'/front/crm/proposal/delProposal.do?dateStr=' + new Date().getTime() ,
									data:{proposalId:proposalId},
									type:'post',
									success:function(result){
										if(result.flag){
										 	layer.msg(globalProp['global.success']);
										 	bindProposal();
										}else{
											layer.msg(globalProp['global.failed']);
											bindProposal();
										}
									}
								});
							  layer.close(index);
							});
					   });
				}
				if(total == 0){
					$('#pagination').hide();
					$('.strategies_List tbody tr:gt(0)').empty();
					$('.no_list_tips').show();
				}else{
					$('.no_list_tips').hide();
					$('#pagination').show();
					$("#pagination").pagination(total, {
						callback : pageselectCallback,
						numDetail : '',
						items_per_page : 10,
						num_display_entries : 4,
						current_page : FILTER.page,
						num_edge_entries : 2
					});
					function pageselectCallback(page_id, jq) {
						FILTER.page = page_id;
						bindProposal();
					}
				}
				
			}
		});
	}
	//Inv.Amount、Last Modified 排序点击事件
	$('.arrow_top_inv_amount').click(function(){
		filterOrder(this,'top_active');
		FILTER.sort = 'c.initialInvestAmount';
		FILTER.order = 'asc';
		initProposal();
	});
	$('.arrow_down_inv_amount').click(function(){
		filterOrder(this,'down_active');
		FILTER.sort = 'c.initialInvestAmount';
		FILTER.order = 'desc';
		initProposal();
	});
	$('.arrow_top_last_modified').click(function(){
		filterOrder(this,'top_active');
		FILTER.sort = 'c.lastUpdate';
		FILTER.order = 'asc';
		initProposal();
	});
	$('.arrow_down_last_modified').click(function(){
		filterOrder(this,'down_active');
		FILTER.sort = 'c.lastUpdate';
		FILTER.order = 'desc';
		initProposal();
	});
	
	function initProposal(){
		FILTER.page=0;
		bindProposal();
	}
	
	//字段排序样式切换
	function filterOrder(selt,orderClass){
		if($(selt).hasClass(orderClass)){
			$('.arrow_down').removeClass('down_active');
			$('.arrow_top').removeClass('top_active');
		}else{
			$('.arrow_down').removeClass('down_active');
			$('.arrow_top').removeClass('top_active');
			$(selt).addClass(orderClass);
		}
	}
	$('#searchKeyBtn').click(function(){
		FILTER.clients = $('#memberId').val();
		//console.log(FILTER.clients);
		initProposal();
	});
	$("#memberId").keyup(function(event){
	      	 if(event.keyCode == 13){
	        	document.getElementById('searchKeyBtn').click()
	        }
	    });	
	$('#fromDate').click(function(){
        var max = '';
        if(!$('#toDate').val()){
            max = laydate.now();
        }else{
            max = $('#toDate').val();
        }
        laydate({
               istime: false,
               max:max,
               elem: '#fromDate',
               format: 'YYYY-MM-DD',
               istoday: true,
               choose: function(datas){ 
               } 
        });
    });
    $('#toDate').click(function(){
        var min = '';
        if(!$('#fromDate').val()){
            min = laydate.now();
        }else{
            min = $('#fromDate').val();
        }
        laydate({
            istime: false,
            min:min,
            elem: '#toDate',
            format: 'YYYY-MM-DD'
        });
    });
    
    $("#txtKeyWord").on("click",function(){
    	/*selector.create(1,'client','memberId','txtKeyWord');
		$(".selectnamelistbox").show();*/
    });
    
    $(".wmes-menu-hide").on("click",function(){
		$(this).toggleClass("wmes-menu-hideac");
		if( $(this).hasClass("wmes-menu-hideac")) {
			$(".client-more-screen-wrap").stop().animate({ 
				height: "100%"
			}, 300 );
			$(".client-more-screen-wrap").css({'margin-top':'5px'});
			$('.funds_list_selected').removeClass('ifa-more-ico-hidden');
		}else{
			$(".client-more-screen-wrap").stop().animate({ 
				height: "0px",margin:'0px'
			}, 300 );
			$('.funds_list_selected').addClass('ifa-more-ico-hidden');
		}
	});
  //金额格式化
	function formatCurrency(num) { 
		if(!num)return '-';
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