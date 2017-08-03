

define(function(require) {
	var $ = require('jquery');
	var angular = require('angular');
	pagination = require('pagination');
	require("swiper");
	require('layer');
	require("scrollbar");
	
    //第一次进页面，加载 所有IFA数据
    loadIFAList(0);	
    $('#searchKeyBtn').on('click',loadIFAList);
	
	
	//关闭每个搜索条件项时
	$(".selection_criteria").on("click",".selection_delete",function(){
		var data_value = $(this).parent().attr("data-value");//data-value="8080804056a201d90156b01c3aeb0019"
		$(this).parent().remove();
		var dataName = $(this).parent().attr("data-name");
		var element = '.funds_choice[data-name="'+dataName+'"]';
		////console.log($(element).html());
		if(dataName=='IfafirmName'){
			var fundslenght = $(".funds_logo_choice li").length;
			for(var funds = 0 ; funds < fundslenght ;funds++){
				if( $(this).parent().attr("data-value") ==  $(".funds_logo_choice li").eq(funds).attr("data-value") ){
					$(".funds_logo_choice li").eq(funds).click();
				}
			}
		}
		else if(dataName=='BelongCountryName'){
			var cur_selected_element = 'li[data-value="'+data_value+'"]';
			$(cur_selected_element).removeClass('fund_choice_active');
			$('#belong_country').hide();
			$('#belong_country_choice li').eq(0).addClass('fund_choice_active').click();
			$('#hidBelongCountry').val('');//清除选项时，同时把隐藏域的值也置空
		}
		else {
			$(element).find('li').eq(0).addClass('fund_choice_active').click();
		}
		//判断条件是否为0，条件为0，则显示show all
		var selected_condition_length = $('.selection_criteria li').length;
		if(selected_condition_length==0){
			$('.funds_selected_title').text(select_condition_showall);	
		}
		loadIFAList($(element).find('li').eq(0));
	});	
	
	

	//组装查询条件
	function loadIFAList(searchLi){
		////console.log($(searchLi).attr('data-name'));
		var dataName = $(searchLi).attr("data-name");
		
	 	Searchdata = $("#listForm").serialize();
	 	getDataList(0);
	}
	
	//执行搜索数据
	function getDataList(pageid) {
		var keywords = $('#txtKeywords').val();
		var data =  "langCode="+lang+"&rows=5&page=" + (pageid+1) + "&keyword=" + keywords ;
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/ifafirm/info/getifateamlistJson.do?datestr="+ new Date().getTime(),
			data : data,
			beforeSend: function () {},
            complete: function () {},
			success : function(json) {
				////console.log(json);
				var total = json.total;
				if(total=='0'){
					$("#pagination").remove();
					 $(".no_list_tips").show();
				}
				$('#sp_meet_total').text(total);
				
				
				
				var list = json.rows;
				if(pageid == 1)$("#ul_ifa_list").empty();
				
				var table_html = '';
				$('#myifalist .funds_tables_th').siblings().remove();
				$.each(list,function(i, n) {
					var email = n.email; 
					var englistName = n.englistName;
					var id = n.id;
					var loginId = n.loginId;
					var memberId = n.memberId;
					var name = n.name;
					var nickname = n.nickname;
					var phoneNo = n.phoneNo;
					var teamId = n.teamId;
					var teamName = n.teamName;
					var englistName = n.englistName;
					var englistName = n.englistName;

					var tr_html = '<tr>'
						+ '<td class="funds_search_tdcenter"><a href="'+base_root+'/front/ifa/info/detail.do?memberId='+memberId+'">'+name+'</a></td>'
						+ '<td class="funds_search_tdcenter">'+englistName+'</td>'
						+ '<td class="funds_search_tdcenter">'+teamName+'</td>'
						+ '<td class="funds_search_tdcenter">'+phoneNo+'</td>'
						+ '<td class="funds_search_tdcenter">'+email+'</td>'
						+ '<td class="funds_search_tdcenter">'+loginId+'</td>'
						+ '<td class="funds_search_tdcenter">'+nickname+'</td>'
						+ '</tr>';
					table_html += tr_html;
					
				});
				$('#myifalist').append(table_html);
				
				$("#pagination").pagination(total, {
					callback : pageselectCallback,
					numDetail : '',
					items_per_page : 5,
					num_display_entries : 4,
					current_page : pageid,
					num_edge_entries : 2
				});
			}
		});
		
		function pageselectCallback(page_id, jq) {
			getDataList(page_id);
		}
	}
});