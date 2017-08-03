
define(function(require) {
	'use strict';
	var $ = require('jquery');
	require('layer');		
	var pageSize = 10;
	
	$(document).ready(function(){
		bindList(0);
		//console.log(langMutilForJs);
		 //绑定查询按钮事件
        $("#btnSearch").click(function () {    
        	//console.log("btnSearch click");
        	bindList(0);
        });
        
	});		

	
	//用户列表
	function bindList(curPage){	
		
		var keyword = $("#keyword").val();
		//console.log(escape(keyword));
		$.ajax({
			type : 'post',
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype : 'json',
			url : base_root+'/console/ifafirm/info/listCustomerJson.do?datestr='+new Date().getTime(),
			data : { 'page':curPage+1,'rows':pageSize,'keyword': keyword	
			},
			async : false,
			success : function(json) {
				
				var trContent = "";
				var total = json.total;
				var list = json.rows;	
//				console.log(list);

				$("#customerList tbody tr:gt(0)").empty();
				
				$.each(list, function (index, array) { //遍历json数据列	
					var id = array['id'] == null ? "" : array['id'];
					var memberId = array['memberId'] == null ? "" : array['memberId'];
					var nickname = array['nickname'] == null ? "" : array['nickname'];
					var fullName = array['fullName'] == null ? "" : array['fullName'];
					var ifaName = array['ifaName'] == null ? "" : array['ifaName'];	
					var clientType = array['clientType'] == null ? "" : array['clientType'];	
					var isImportant = array['isImportant'] == null ? "" : array['isImportant'];	
					
					if(clientType == "1"){
						clientType = langMutilForJs["ifafirm.customer.clientType.1"];
					}else{
						clientType = langMutilForJs["ifafirm.customer.clientType.0"];
					}

					
					if(isImportant == "1"){
						isImportant = langMutilForJs["global.true"];
					}else{
						isImportant = langMutilForJs["global.false"];
					}
					
					trContent += '<tr><td class="tdleft funds_tables_fnames ifalist_nicheng">'
								+ '<a href="#">'+nickname+'</a></td>'
								+ '<td class="funds_search_tdcenter">'+fullName+'</td>'
								+ '<td class="funds_search_tdcenter">'+ clientType +'</td>'	
								+ '<td class="funds_search_tdcenter">'+isImportant+'</td>'
								+ '<td class="funds_search_tdcenter">'+ifaName+'</td>'
								+ '<td class="funds_search_tdcenter funds_search_tdcenterimg">'
								+ '	<img class="recommend-ico recommend-ico1 viewinfo" data-id="'+memberId+'" src="'+base_root+'/res/images/workbench/chakan_ico.png ">'
								+ '</td></tr>';
				});	
				if(trContent != '' ){
					$("#customerList tbody").append(trContent);
				}
				
				$("#pagination").pagination(total, {
                    callback: pageselectCallback,
                    numDetail: '',
                    items_per_page: pageSize,
                    num_display_entries: 4,
                    current_page: curPage,
                    num_edge_entries: 2
                });
			}
		})
		
		//回调
        function pageselectCallback(page_id, jq) {
			bindList(page_id);
        }
	}
	
	$("body").on("click",".viewinfo",function(){
		var memberId = $(this).attr("data-id");
		var url = base_root+"/console/ifafirm/info/customerInfo.do?memberId="+memberId;
		//window.location.href = base_root+"/console/ifafirm/info/customerInfo.do?memberId="+memberId;
		window.open(url);
	});
		
});