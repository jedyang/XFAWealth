define(function(require) {
	var $ = require('jquery');
	// angular = require('angular');
	//pagination = require('pagination');
	require('layer');
	require("scrollbar");
	require('laydate');

	//点击每个选项，在下面的已选方案中添加该选项
//	var listTime;
	$(".funds_choice li").on("click",function(){
//		clearTimeout(listTime)
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
		
		loadDataList(this);
		//Initialization();
		// 解决重复请求的问题
//		var self = this;
//			listTime=setTimeout(function(){
//				loadIFAList(self);
//			}
//			,100);
	});
	
	//
	$("#sel-portfolio").on("change",function(){
		var self = $('#sel-portfolio option:selected');
		var dataKey = $('#sel-portfolio option:selected').attr('data-key');
		var dataValue = $('#sel-portfolio option:selected').attr('data-value');
		var dataName = $('#sel-portfolio option:selected').attr('data-name');
		
		var selection_criterialenght = $(".selection_criteria li").length;
		if(dataKey==''){//all
			for(var i = 0; i < selection_criterialenght ;i++){
				if($(".selection_criteria li").eq(i).attr("data-name") == dataName ){
					$(".selection_criteria li").eq(i).remove();
				}
			}
		}

		for(var j = 0; j < selection_criterialenght ;j++){
				if($(".selection_criteria li").eq(j).attr("data-name") == dataName ){
					$(".selection_criteria li").eq(j).remove();
				}
		}
		if(dataKey != undefined && dataValue != ""){
			$(".funds_title_selection").before('<li data-value="' + dataValue + '" data-name="' + dataName + '">' + dataKey + '<span class="selection_delete"></span></li>')
		}
		loadDataList(self);
		//Initialization();
		// 解决重复请求的问题
//		var self = this;
//			listTime=setTimeout(function(){
//				loadIFAList(self);
//			}
//			,100);
	});
	
	//
	$("#sel-investorAccount").on("change",function(){
		var self = $('#sel-investorAccount option:selected');
		var dataKey = $('#sel-investorAccount option:selected').attr('data-key');
		var dataValue = $('#sel-investorAccount option:selected').attr('data-value');
		var dataName = $('#sel-investorAccount option:selected').attr('data-name');

		var selection_criterialenght = $(".selection_criteria li").length;
		if(dataKey==''){//all
			for(var i = 0; i < selection_criterialenght ;i++){
				if($(".selection_criteria li").eq(i).attr("data-name") == dataName ){
					$(".selection_criteria li").eq(i).remove();
				}
			}
		}
		for(var j = 0; j < selection_criterialenght ;j++){
				if($(".selection_criteria li").eq(j).attr("data-name") == dataName ){
					$(".selection_criteria li").eq(j).remove();
				}
		}
		if(dataKey != undefined && dataValue != ""){
			$(".funds_title_selection").before('<li data-value="' + dataValue + '" data-name="' + dataName + '">' + dataKey + '<span class="selection_delete"></span></li>')
		}
		loadDataList(self);
		//Initialization();
		// 解决重复请求的问题
//		var self = this;
//			listTime=setTimeout(function(){
//				loadIFAList(self);
//			}
//			,100);
	});
	
	//执行清除方案点击操作
	$(".funds_title_selection").on("click",function(){
		$(".selection_criteria li").remove();

		$(".fund_logo_active").removeClass("fund_logo_active");

		$(".fund_choice_active").removeClass("fund_choice_active");
		$(".funds_all").addClass("fund_choice_active");
		//$("#listForm").find("input").val("");
		//Searchdata = $("#listForm").serialize();
		Initialization();
	});	
	
	/**搜索条件取消点击
	 * author scshi
	 * date 20160821
	 */
	$(".selection_criteria").on("click",".selection_delete",function(){
		$(this).parent().remove();
		var funds_all_lenght = $('.funds_all').length;
		for( var i = 0; i < funds_all_lenght ; i++){
			if($(this).parent().attr("data-name") == "FundHouse"){
				var fundslenght = $("#funds_logo li").length;
				for(var funds = 0 ; funds < fundslenght ;funds++){
					if( $(this).parent().attr("data-value") ==  $("#funds_logo li").eq(funds).attr("data-value") ){
						$("#funds_logo li").eq(funds).click();
					}
				}
				break;
			}else if( $(this).parent().attr("data-name") ==  $('.funds_all').eq(i).attr("data-name") ){
				$('.funds_all').eq(i).click();
			}
		}
		
		var prefCount=0;
		$(".selection_criteria").find("li").each(function(){
			$(this).attr("data-value")=="pref";
			prefCount++
		})
		if(prefCount==0)$("#per_all").addClass("per_active");
	});	

	/**
	 * 显示清除所有搜索条件按钮
	 * */
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
	Initialization();//加载页面时全部显示
	
	function Initialization(){
		selection();
		getDataList();
	}
	
	//组装查询条件
	function loadDataList(searchLi){
		
		var dataName = $(searchLi).attr("data-name");
		var dataValue = $(searchLi).attr("data-value");
		if(!dataValue)dataValue="";
		
		//Keyword
		if("Keyword"==dataName){
			$('#hidKeyword').val(dataValue);
		}
		//Period
		if("Period"==dataName){
			$('#hidPeriod').val(dataValue);
		}
		//Status
		if("Status"==dataName){
			$('#hidStatus').val(dataValue);
		}
		//portfolio
		if("portfolio"==dataName){
			$('#hidPortfolio').val(dataValue);
		}
		//investorAccount
		if("investorAccount"==dataName){
			$('#hidAccount').val(dataValue);
		}
		
	 	
	 	Initialization();
	}
	
	function getDataList(){
		var Searchdata = $("#listForm").serialize();
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/transaction/info/getTransactionRecordJson.do?datestr="+ new Date().getTime()+'&'+Searchdata,
			data : {  },
			 beforeSend: function () {},
			 complete: function () {
			 },
			success : function(json) {
				var html = '';
				//下拉组装数据
				// var xiala_html = '';
				$('.watching_xiala').find('ul').empty().append('<li data-value="">All</li>');
				var dataList = JSON.parse(json);
				var historyCount = 0;
				$.each(dataList,function(i,n){
					var portfolioName = n.portfolioName;
					//输出类型头部信息
					// var element_id = 'watching-rows-'+i;
					var type_html = '<div class="record-table-rows">';
					type_html += '<table class="record_List">';
					type_html += '<tbody>';
					type_html += '<tr class="funds_tables_th record_List_th">';
					type_html += '<th width="10%" class="funds_search_tdcenter">Date</th>';
					type_html += '<th width="23%" class="funds_search_tdcenter">Product Name</th>';
					type_html += '<th width="10%" class="funds_search_tdcenter">Transaction Unit</th>';
					type_html += '<th width="10%" class="funds_search_tdcenter">Transaction Amount</th>';
					type_html += '<th width="10%" class="funds_search_tdcenter">Transaction Fee</th>';
					type_html += '<th width="10%" class="funds_search_tdcenter">Transaction Type</th>';
					type_html += '<th width="10%" class="funds_search_tdcenter">Account No</th>';
					type_html += '<th width="10%" class="funds_search_tdcenter">Status</th>';
					type_html += '<th width="7%" class="funds_search_tdcenter">Action</th>';
					type_html += '</tr>';
					type_html += '</tbody>';
					type_html += '</table>';
					type_html += '<p class="record-List-title">';
					type_html += '<span>'+portfolioName+'</span>';
					type_html += '<img class="record-list-img" src="'+base_root+'/res/images/fund/record_xiala01.png">';
					type_html += '</p>';
					var orderHistoryList = n.orderHistoryList; //一个新list
					if(orderHistoryList!=null){
						type_html += '<table class="funds_search_information record_List record_List_iof">';
						type_html += '<tbody>';
						$.each(orderHistoryList,function(p,t){
							// var watchingId = t.watchingId;
							// var remark = t.remark;
							//获取每条基金信息
							historyCount ++;
							var eachFund = t;
							////console.log(eachFund);
							    var historyId = eachFund.id;
							
							
								var orderDate = eachFund.orderDate;
								var productName = eachFund.productName;
								var transactionUnit = eachFund.commissionUnit;
								var transactionAmount = eachFund.commissionAmount;
								var transactionFee = eachFund.fee;
								var transactionType	 = eachFund.orderType;
								var accountNo = eachFund.account.accountNo;
								var status = eachFund.status;
								var row = '<tr>';
								row += '<td class="funds_search_tdcenter tdleft">'+orderDate+'</td>';
								row += '<td class="funds_search_tdcenter tdleft">'+productName+'</td>';
								row += '<td class="funds_search_tdcenter">'+transactionUnit+'</td>';
								row += '<td class="funds_search_tdcenter">'+transactionAmount+'</td>';
								row += '<td class="funds_search_tdcenter">'+transactionFee+'</td>';
								row += '<td class="funds_search_tdcenter">'+transactionType+'</td>';
								row += '<td class="funds_search_tdcenter">'+accountNo+'</td>';
								row += '<td class="funds_search_tdcenter">'+status+'</td>';
								row += '<td class="funds_search_tdcenter">';
								if(status=='Success')row += '<img historyId="'+historyId+'" class="img-success" src="'+base_root+'/res/images/fund/record_ico01.png">';
								row += '</td>';
								row += '</tr>';
								type_html += row;
						});
						
						type_html += '</tbody>';
						type_html += '</table>';
					}
					type_html += '</div>';
					html += type_html;
				});
				
				$('.funds_serach_digital').text(historyCount);
				$('.record-table-wrap').empty().append(html);
			}
		});
	}
	
	//按钮关键字
	$("#searchKeyBtn").on("click",function(){
		var keyWords = $("#fundName").val();
		document.getElementById("hidKeyword").value=keyWords;
		Searchdata = $("#listForm").serialize();
	 	Initialization();
	});
	
	//按回车键自动搜索
	$("#fundName").keyup(function(event){
   	 if(event.keyCode == 13){
        	document.getElementById('searchKeyBtn').click();
        }
    });	
	
	//当记录为成功状态时，可以删除
	$("body").on('click', '.img-success', '', function () {
		var historyId = $(this).attr('historyId');
		var self = $(this);
		if(historyId!=''&&historyId!=undefined){
				layer.confirm('Do you sure to delete the record?', { icon: 3  },function () { 
				$.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/transaction/info/delTransactionRecord.do?datestr="+ new Date().getTime()+'&'+Searchdata,
					data : { 'historyId':historyId },
					 beforeSend: function () {},
					 complete: function () {},
					 success : function(json) {
						json = JSON.parse(json);
						if(json.result){
							layer.msg('Delete TransactionRecord Successful!', {icon: 1});
							self.parents('tr').remove();
						}
					}
				});
			});
		}
	});
});	