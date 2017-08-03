
define(function(require) {
	'use strict';
	var $ = require('jquery');
	require('layer');		
	var pageSize = 10;
	
	$(document).ready(function(){
		bindList(0);
		 //绑定查询按钮事件
        $("#btnSearch").click(function () {    
        	//console.log("btnSearch click");
        	bindList(0);
        });
        
	});		

	
	//用户列表
	function bindList(curPage){	
		//console.log($("#keyword").val());
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/console/ifafirm/info/listAccountJson.do?datestr='+new Date().getTime(),
			data : { 'page':curPage+1,'rows':pageSize,'keyword': $("#keyword").val(),'status': $("#flowStatus").val()		
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
					var accountNo = array['accountNo'] == null ? "" : array['accountNo'];	
					var createTime = array['createTime'] == null ? "" : array['createTime'];	
					var flowStatus = array['flowStatus'] == null ? "" : array['flowStatus'];
					

					
					if(flowStatus == "1"){
						flowStatus = langMutilForJs["ifafirm.account.flowStatus.1"];
					}else if(flowStatus == "2"){
						flowStatus = langMutilForJs["ifafirm.account.flowStatus.2"];
					}else if(flowStatus == "3"){
						flowStatus = langMutilForJs["ifafirm.account.flowStatus.3"];
					}else {
						flowStatus = langMutilForJs["ifafirm.account.flowStatus.4"];
					}
					
					trContent += '<tr><td class="tdleft funds_tables_fnames ifalist_nicheng">'
								+ '<a href="#">'+nickname+'</a></td>'
								+ '<td class="funds_search_tdcenter">'+fullName+'</td>'
								+ '<td class="funds_search_tdcenter">'+ accountNo +'</td>'	
								+ '<td class="funds_search_tdcenter">'+createTime+'</td>'
								+ '<td class="funds_search_tdcenter">'+flowStatus+'</td>'
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