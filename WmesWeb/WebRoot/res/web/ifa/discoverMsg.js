
define(function(require) {
	//依赖
	var $ = require('jquery');
	require('pagination');
	require('layer');
	var Loading = require('loading');
	
	var oLoading = new Loading($("#div_loading"));
	//切换全部、已读、未读的li标签的样式
	var read_status = getQueryString('status');
	if(read_status=='0')$('#li_status_not_read').addClass('discoverMsg-regulationac').siblings().removeClass('discoverMsg-regulationac').end();
	else if(read_status=='1')$('#li_status_haved_read').addClass('discoverMsg-regulationac').siblings().removeClass('discoverMsg-regulationac').end();
	$('#li_status_not_read').on('click',function(){ 
		$('#ul_data').empty();
		loadReadToDoList('0');
		$('.regiter_xiala_ul li[value="0"]').hide();
		$('.regiter_xiala_ul li[value="1"]').show();
	});
	$('#li_status_haved_read').on('click',function(){
		$('#ul_data').empty();
		loadReadToDoList('1');
		$('.regiter_xiala_ul li[value="0"]').show();
		$('.regiter_xiala_ul li[value="1"]').hide();
	});
	$('#li_status_all').on('click',function(){
		$('#ul_data').empty();
		loadReadToDoList('');
		$('.regiter_xiala_ul li[value="0"]').show();
		$('.regiter_xiala_ul li[value="1"]').show();
	});
	//绑定返回到上一层级页面
	$('.wmes-ruturn').on('click',function(){ $(this).parent('a').removeAttr('href'); top.location.href = base_root + '/front/ifa/info/discover.do'; });
	//第一次进页面，显示全部类型数据
    loadReadToDoList('0');//进入页面时只显示未读类型
    $('.regiter_xiala_ul li[value="0"]').hide();
	$('.regiter_xiala_ul li[value="1"]').show();
    
	$(".comment-show-button").on("click",function(){
		$(this).parents(".conner-contens-rows").toggleClass("conner-comment-show");
	});
	
	//点 击弹出好友添加框
	$(document).on("click",".dialog-friend",function(){
		var webfriendid = $(this).attr('webfriendid');
		var todoid = $(this).attr('todoid');
		layer.confirm(SURETOADDFRIEND, { title:ADDFRIENDTITLE, icon: 3,btn:[SURETOADDFRIEND_BUTTON_YES,SURETOADDFRIEND_BUTTON_NO]  },
				function () { dealFriend(webfriendid,todoid,'1');},
				function(){ dealFriend(webfriendid,todoid,'2')});
	});
	
	function dealFriend(webfriendid,todoid,status){ 
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/ifa/info/dealWithBuddy.do?datestr="+ new Date().getTime(),
			data : {'id':webfriendid,'status':status,'todoid':todoid },
			 beforeSend: function () {},
			 complete: function () {
			 },
			success : function(json) {
				//console.log(json);
				var result = json.result;
//				var msg = json.msg;
				if(result==true||result=='true'){
					layer.msg(GLOBAL_SUCCESS);
					var elementid = '#li_' + todoid;
					var obj = $(elementid);
					obj.fadeTo("slow", 0.01, function(){//fade
					    $(this).slideUp("slow", function() {//slide up
					      $(this).remove();//then remove from the DOM
					    });
					  });
				}else if(result==false||result=='false'){
					layer.msg(GLOBAL_FAILD);
				}
			}
		});
	}
	
	//删除消息
	$(".choosedelete").on("click",function(){
		var idlist = '';
		$('.cbk-selected-msg').each(function(index,element){
			var msgid = $(element).attr('value');
			if($(element).is(':checked')) {
				idlist += msgid + ',';
			}
			
		});
		if(idlist==''){
			return;
		}
		layer.confirm(GLOBAL_CONFIRM_DELETE, { title:GLOBAL_CONFIRM, icon: 3,btn:[SURETOADDFRIEND_BUTTON_YES,SURETOADDFRIEND_BUTTON_NO]  },
				function () { 
			delmsg(idlist);
		},
				function(){ });
	});
	
	//删除消 息
	function delmsg(idlist){
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/ifa/info/delWebReadToDo.do?datestr="+ new Date().getTime(),
			data : {'idlist':idlist },
			 beforeSend: function () {},
			 complete: function () {
			 },
			 success : function(json) {
				////console.log(json);
				var jsondata = JSON.parse(json); 
				var result = jsondata.result;
				////console.log(jsondata.result);
//				var msg = jsondata.msg;
				if(result==true||result=='true'){
					layer.msg(GLOBAL_SUCCESS);
					var array = idlist.split(',');
					for(var k=0;k<array.length;k++){
						var li_id = '#li_' + array[k];
						var li = $(li_id);
						////console.log(li);
						li.fadeTo("slow", 0.01, function(){//fade
						    $(this).slideUp("slow", function() {//slide up
						      $(this).remove();//then remove from the DOM
						    });
						  });
					}
					setTimeout(function(){layer.closeAll();},500);
				}else if(result==false||result=='false'){
					layer.msg(GLOBAL_FAILD);
				}
			}
		});
	}
	
	$(document).on("click",".register_xiala_long input",function(){
		$(this).siblings(".regiter_xiala_ul").show();
	});
	
	$(document).on('click','.register_xiala_ico,.register_xiala_long_menu',function(){
		
		if($(this).parents('.register_xiala_long').find('ul').css('display') == 'none'){
			$(this).siblings(".regiter_xiala_ul").show();
		}else{
			$(this).siblings(".regiter_xiala_ul").hide();
		};
	});
	
	//批量进行操作
	$(".regiter_xiala_ul").on("click","li",function(){ 
//		var readText = $(this).text();
		var readValue = $(this).attr('value'); 
		var read_status = getQueryString('status');//当前查看的消息的读状态
		if(read_status==readValue)return;
		
		bitchDealMsgRead(readValue);
		$(".regiter_xiala_ul").hide();
	});
	$('.discoverMsg-allchoose').on('click',function(){
		if($(this).is(':checked')) {
			$('.messages-wrapper-list input').prop('checked',true);
			$(this).next('span').text('取消');
		} else{
			$('.messages-wrapper-list input').prop('checked',false);
			$(this).next('span').text('全选');
		}
		
		if($(this).prop('checked')==true){
			$('.messages-wrapper-list li input').prop('checked',true);
		}else{
			$('.messages-wrapper-list li input').prop('checked',false);
		}
	});
	$('.messages-wrapper-list li input').on('click',function(e){
		e.stopPropagation();
	});
	
	$('.discoverMsg-regulation li').on('click',function(){
		$(this).addClass('discoverMsg-regulationac').siblings().removeClass('discoverMsg-regulationac');
	});
	
	$('body').on('click',function(){
		if($(".regiter_xiala_ul").css('display')=='block'){
			$('.regiter_xiala_ul').css('display','none');
		}
	});

	$('.messages-wrapper-list li').on('click',function(){
		var self = $(this);
		$('.messages-wrapper-list li').removeClass('messages-liread');
		$(this).addClass('messages-liread');
		setTimeout(function(){
			self.find('.messages-list-title').removeClass('messages-unread');
		},2000)
	});
	//批量标记已读或未读
	function bitchDealMsgRead(status){
		var idlist = '';
		$('.cbk-selected-msg').each(function(index,element){
			var msgid = $(element).attr('value');
			if($(element).is(':checked')) {
				idlist += msgid + ',';
			}
			
		});
		if(idlist==''){
			return;
		}
		
		bitchDealMsgReadStatus(idlist,status);
		//layer.confirm(GLOBAL_CONFIRM_DELETE, { title:GLOBAL_CONFIRM, icon: 3,btn:[SURETOADDFRIEND_BUTTON_YES,SURETOADDFRIEND_BUTTON_NO]  },function () {  },function(){ });
	}
	//批量标记已读或未读
	function bitchDealMsgReadStatus(idlist,status){
		var isAllSelected = $('#li_status_all').hasClass('discoverMsg-regulationac');
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/ifa/info/bitchDealMsgReadStatus.do?datestr="+ new Date().getTime(),
			data : {'idlist':idlist,'status':status },
			 beforeSend: function () {},
			 complete: function () {
			 },
			 success : function(json) {
				////console.log(json);
				var jsondata = JSON.parse(json); 
				var result = jsondata.result;
				////console.log(jsondata.result);
//				var msg = jsondata.msg;
				if(result==true||result=='true'){
					layer.msg(GLOBAL_SUCCESS);
//					var url_read_status = getQueryString('status');//当前查看的消息的读状态
//					if(url_read_status==null||url_read_status==undefined){
//						window.location.href = window.location.href;
//					}
					if(isAllSelected==false){
						var array = idlist.split(',');
						for(var k=0;k<array.length;k++){
							var li_id = '#li_' + array[k];
							var li = $(li_id);
							////console.log(li);
							li.fadeTo("slow", 0.01, function(){//fade
							    $(this).slideUp("fast", function() {//slide up
							      $(this).remove();//then remove from the DOM
							    });
							  });
						}
					}
					setTimeout(function(){layer.closeAll();
					var li_length = $('#ul_data').children('li').length;
					if(li_length==0)$('.wmes-nodata-tips').show();
					else $('.wmes-nodata-tips').hide();
					},1000);
					//if(status=='0')loadReadToDoList('1');
					//else if(status=='1')loadReadToDoList('0');
					
				}else if(result==false||result=='false'){
					layer.msg(GLOBAL_FAILD);
				}
			}
		});
	}
	
	function loadReadToDoList(status){
	 	Initialization(status);
		
	}
	
	function Initialization(status){

		// 初始化数值
	 	//var iPAGE = 1; //第N页数据
	 	//var is_finish = true;
	 	//var page_bol = true;
	 	var pageid = 1;
		$("#ifa_more").html(__clickmore__);
		getDataList(pageid,status);
	}
	
	//执行搜索数据
	function getDataList(pageid,status) {
		var typecode = getQueryString('typecode');
		var keyWord = $('#fundName').val();
		//var readstatus = getQueryString('status');
		var data =  "langCode="+lang+"&rows=5&page=" + pageid + "&typecode=" + typecode +"&status=" + status + "&keyWord=" + keyWord;
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/ifa/info/discoverMsgJson.do?datestr="+ new Date().getTime(),
			data : data,
			beforeSend: function () {oLoading.show();},
            complete: function () {},
			success : function(json) {
				oLoading.hide();
//				console.log(json);
				var total = json.total;
				var rows = json.rows;
				//console.log(rows);
				sumpage = Math.ceil(total / 5);
				$('#ifa_more').attr('pageid',pageid);
				$('#ifa_more').attr('sumpage',sumpage);
				
				if(total=='0'){
					
					//return;
				}
				$('#sp_meet_total').text(total);
				//genTotalHtml(total);
//				var list = json.rows;
				if(pageid == 1)$("#ul_ifa_list").empty();

				var li_html = '';
				$.each(rows,function(i, n) { //console.log(n);
//					var createTime = n.createTime;
					var createTimeFmt = n.createTimeFmt; 
					var id = n.id;
//					var isApp = n.isApp;
					var isRead = n.isRead; 
//					var isValid = n.isValid;
					var moduleType = n.moduleType;
					var msgParam = n.msgParam;
					var msgUrl = n.msgUrl;
//					var readTime = n.readTime;
					var relateId = n.relateId;
					var title = n.title;
//					var type = n.type;
					//组装URL
					var iswaitdealfriend = false;
					if(moduleType=='friend'){
						if(isRead=='0')iswaitdealfriend = true;
						else if(isRead=='1')iswaitdealfriend = false;
					}
					var url = '';
					if(msgUrl!=null&&msgUrl!=''){
						url = base_root + msgUrl + '?readid=' + id + '&' + msgParam;
					} else url = 'javascript:void(0);';
					//输入图标,结合是否已读，已读的显示灰色
					var msgico = '';
					if(typecode=='1'){
						if(isRead=='1')msgico = 'msg_ico011_gray.png';//已读
						else msgico = 'msg_ico01.png';//未读
					}
					else if(typecode=='2'){
						if(isRead=='1')msgico = 'msg_ico031_gray.png';//已读
						else msgico = 'msg_ico03.png';//未读
					}
					else if(typecode=='3'){
						if(isRead=='1')msgico = 'msg_ico02.png';//已读
						else msgico = 'msg_ico021_gray.png';//未读
					}
					var li = '<li id="li_'+ id +'">';
						li += '<div class="account-checkbox" style="margin-top:6px;margin-right:20px">';
						li += '<input type="checkbox"  name="spaceShowAum'+id+'" id="spaceShowAum'+id+'" value="'+ id +'" class="cbk-selected-msg">';
						li += '<label for="spaceShowAum'+id+'"></label>';
						li += '</div>';
						li += '<img class="messages-list-ico" src="'+base_root+'/res/images/discover/'+msgico+'">';
						li += '<div class="messages-list-rows">';
						if(moduleType=='friend'){
							if(iswaitdealfriend==true)li += '<p class="messages-list-title"><a todoid="'+id+'" webfriendid="'+relateId+'" href="javascript:void(0);" class="discover-a-href dialog-friend">'+title+'</a></p>';
							else li += '<p class="messages-list-title"><a href="javascript:void(0);" class="">'+title+'</a></p>';
						}
						else li += '<p class="messages-list-title"><a isread="'+isRead+'" rid="'+id+'" url="'+url+'" class="discover-url discover-a-href" style="cursor:pointer;">'+title+'</a></p>';
						
						li += '<p class="messages-list-time">'+createTimeFmt+'</p>';
						li += '</div>';
						li += '</li>';
						li_html += li;
				});
				$('#ul_data').append(li_html);
				var li_length = $('#ul_data').children('li').length;
				if(total=='0'){
					//判断是否存在LI元素
					if(li_length>0){
						$('#ifa_more').show();
					} else{
						$('.wmes-nodata-tips').show();
						$('#ifa_more').hide();
						
					}
				}
				else {$('.wmes-nodata-tips').hide();$('#ifa_more').show();if(li_length<5)$('#ifa_more').hide();}
			}
		});
	}
	
	$("#ifa_more").on("click",function(){
		var pageid = $(this).attr('pageid');
		var sumpage = $(this).attr('sumpage');
		var readstatus = $('.discoverMsg-regulationac').attr('status'); 
		pageid = parseInt(pageid) +1;
		sumpage = parseInt(sumpage);
		if(pageid <= sumpage){ 
			getDataList(pageid,readstatus);
			$(this).html(__clickmore__);
		}else{
			$(this).html(__nomoredata__);
		}

	});
	
	//当有可点击跳转URL的HREF时
	$(document).on('click','.discover-url',function(){
		var url = $(this).attr('url');
		var id = $(this).attr('rid');
		var isread = $(this).attr('isread');
		if(isread=='1'){
			window.location.href = url;
		} else {
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/ifa/info/bitchDealMsgReadStatus.do?datestr="+ new Date().getTime(),
				data : {'idlist':id,'status':'1' },
				 beforeSend: function () {},
				 complete: function () {
				 },
				 success : function(json) {
					//console.log(json);
					var jsondata = JSON.parse(json); 
//					var result = jsondata.result;
					//console.log(jsondata.result);
//					var msg = jsondata.msg;
//					if(result==true||result=='true'){
//						window.location.href = url;
//					}else if(result==false||result=='false'){
//						//layer.msg(GLOBAL_FAILD);
//						window.location.href = url;
//					}
					window.location.href = url;
				}
			});
		}
		
		
	});
	
	
	//搜索
	$('#searchKeyBtn').on('click',function(){
		var readstatus = $('.discoverMsg-regulationac').attr('status'); 
		$('#ul_data').empty();
		Initialization(readstatus);
	});
	$("#fundName").keyup(function(event){
	    if(event.keyCode == 13){
	       document.getElementById('searchKeyBtn').click()
	    }
	});	
	
});