define(function(require) {
	var $ = require('jquery');
	require('layui');
	
	$(".j-port-show").on("click",function(){
		$(this).parents(".stepOne-rows").toggleClass("port-parshow");
	});
	
	//SAVE
	$('#btnSave').click(function(){
		var portfolioId = getUrlParam('portfolioId');
		if(portfolioId == null){
			portfolioId = $('#hidPortfolioId').val();
		}
		//1:Only Me，2:Public，3:Let me specify
    	var scopeFlag = '',
    		//1全部，0否，-1部分
    		clientFlag = '',
    		prospectFlag = '',
    		buddyFlag = '',
    		colleagueFlag = '',
    		clientsDetail = '',
    		prospectsDetail = '',
    		buddiesDetail = '',
    		colleaguesDetail = '';
    	$('.view_permissions_setting input').each(function(i,n){
    		if($(n).prop('checked')){
        		scopeFlag = $(this).val();
        	}
    	});
    	if(scopeFlag == '3'){
    		clientFlag = '0',
    		prospectFlag = '0',
    		buddyFlag = '0',
    		colleagueFlag = '0';
    		if($('#permit_client').prop('checked')){
    			clientFlag = '1';
    			if(typeof $('#permit_clients').val() != 'undefined' && $('#permit_clients').val()!=''){
    				clientFlag = '-1';
    				clientsDetail = $('#permit_clients').val();
    			}
    		}
    		if($('#permit_prospect').prop('checked')){
    			prospectFlag = '1';
    			if(typeof $('#permit_prospects').val() != 'undefined' && $('#permit_prospects').val()!=''){
    				prospectFlag = '-1';
    				prospectsDetail = $('#permit_prospects').val();
    			}
    		}
    		if($('#permit_buddy').prop('checked')){
    			buddyFlag = '1';
    			if(typeof $('#permit_buddies').val() != 'undefined' && $('#permit_buddies').val()!=''){
    				buddyFlag = '-1';
    				buddiesDetail = $('#permit_buddies').val();
    			}
    		}
    		if($('#permit_colleague').prop('checked')){
    			colleagueFlag = '1';
    			if(typeof $('#permit_colleagues').val() != 'undefined' && $('#permit_colleagues').val()!=''){
    				colleagueFlag = '-1';
    				colleaguesDetail = $('#permit_colleagues').val();
    			}
    		}
    	}
    	var data = {
			step:4,
			portfolioId:portfolioId,
			scopeFlag : scopeFlag,
			clientFlag : clientFlag,
			prospectFlag : prospectFlag,
			buddyFlag : buddyFlag,
			colleagueFlag : colleagueFlag,
			prospectsDetail : prospectsDetail,
			clientsDetail : clientsDetail,
			buddiesDetail : buddiesDetail,
			colleaguesDetail : colleaguesDetail
    	};
		var url = base_root+'/front/portfolio/arena/savePortfolioArena.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:data,
			success:function(result){
				setTimeout(function(){
					window.location.href = base_root + '/front/portfolio/arena/detail.do?id=' + portfolioId;
				},1000); 
			}
		});
	
	});
	
	//跳转 step3
	$('#btnNextThree').click(function(){
		var strategyId = $('#hidStrategyId').val();
		var portfolioId = $('#hidPortfolioId').val();
		var url = '';
		if(typeof(strategyId) != '' && strategyId != ''){
			url = base_root+'/front/portfolio/arena/createPortfolioThree.do?strategyId='+strategyId+'&portfolioId='+portfolioId;
		}else{
			url = base_root+'/front/portfolio/arena/createPortfolioThree.do?portfolioId='+portfolioId;
		}
		if(getUrlParam('edit') == '1'){
			url = urlUpdateParams(url, 'edit', '1');
		}
		window.location.href = url;
	});
	
	function openResWin(url,width,height,id){ 
	   var myw = screen.availWidth;   //定义一个myw，接受到当前全屏的宽    
	   var myh = screen.availHeight;  //定义一个myw，接受到当前全屏的高   
	   if (width>myw) width = myw;
	   if (height>myh) height = myh;
	   
	   var top = (myh-height)/2-(window.screen.height-myh)/2;
	   if (top<0) top = 0;
	   
	   var left = (myw-width)/2-(window.screen.width-myw)/2;
	   if (left<0) left = 0;
	   window.open(url,id,"height="+height+", width="+width+", top="+top+", left="+left+",location=no,menubar=no,toolbar=no,status=no,resizable=no,scrollbars=yes");   
	}
	$('#remindAdvanceSetting').click(function(){
		var url=base_root+"/front/strategy/info/userSelector.do";
		openResWin(url,1200,600,"pushASetting");
	});
	$("#permit0,#permit1").on("click",function(){
		$("#permit_ext").hide();
	});
	$("#permit2").on("click",function(){
    	$("#permit_ext").toggle();
    });
    
    $("#push2").on("click",function(){
    	$("#push_ext").show();
    });

    $("#permit_ext .setting").on("click",function(){
        $(this).parents("li").toggleClass("setting_active");
    });
    
    $('#refreshSelection').click(function(){
    	var permit_clients = $('#permit_clients').val();
		var permit_prospects = $('#permit_prospects').val();
		var permit_buddies = $('#permit_buddies').val();
		var permit_colleagues = $('#permit_colleagues').val();
		if(!permit_clients){
			var length = $('.a-permit-clients').closest('li').find('.select-all').length;
			if(length == 0){
				$('.a-permit-clients').closest('li').find('p').remove();
				$('.a-permit-clients').before('<p class="pushToSettingName select-all" style="margin-right:30px">All</p>');
			}
		}else{
			$.ajax({
				type:'post',
				url:base_root+'/front/portfolio/arena/getMemberBase.do?dateStr=' + new Date().getTime(),
				data:{
					ids:permit_clients
				},success:function(result){
					var html = '';
					$.each(result.members,function(i,n){
						var nickName = n.nickName;
						if(nickName.length>9){
							nickName = nickName.substring(0,9)+'..';
						}
						if(i>2){
							html += '<p class="pushToSettingName" style="margin-right:30px">...</p>';
							return false;
						}
						if(i==2){
							html += '<p class="pushToSettingName" title="'
								+ n.nickName
								+'" style="margin-right:30px">'
								+ nickName
								+'</p>';
						}else{
							html += '<p class="pushToSettingName" title="'
								+ n.nickName
								+'" style="margin-right:30px">'
								+ nickName
								+'<span> ,</span></p>';
						}
					});
					$('a-permit-buddies').closest('li').find('p').remove();
					$('.a-permit-clients').before(html);
				}
			});
		}
		if(!permit_prospects){
			var length = $('.a-permit_prospects').closest('li').find('.select-all').length;
			if(length == 0){
				$('.a-permit_prospects').closest('li').find('p').remove();
				$('.a-permit_prospects').before('<p class="pushToSettingName select-all" style="margin-right:30px">All</p>');
			}
		}else{
			$.ajax({
				type:'post',
				url:base_root+'/front/portfolio/arena/getMemberBase.do?dateStr=' + new Date().getTime(),
				data:{
					ids:permit_prospects
				},success:function(result){
					var html = '';
					$.each(result.members,function(i,n){
						var nickName = n.nickName;
						if(nickName.length>9){
							nickName = nickName.substring(0,9)+'..';
						}
						if(i>2){
							html += '<p class="pushToSettingName" style="margin-right:30px">...</p>';
							return false;
						}
						if(i==2){
							html += '<p class="pushToSettingName" title="'
								+ n.nickName
								+'" style="margin-right:30px">'
								+ nickName
								+'</p>';
						}else{
							html += '<p class="pushToSettingName" title="'
								+ n.nickName
								+'" style="margin-right:30px">'
								+ nickName
								+'<span> ,</span></p>';
						}
					});
					$('a-permit-buddies').closest('li').find('p').remove();
					$('.a-permit_prospects').before(html);
				}
			});
		}
		if(!permit_buddies){
			var length = $('a-permit-buddies').closest('li').find('.select-all').length;
			if(length == 0){
				$('a-permit-buddies').closest('li').find('p').remove();
				$('a-permit-buddies').before('<p class="pushToSettingName select-all" style="margin-right:30px">All</p>');
			}
		}else{
			$.ajax({
				type:'post',
				url:base_root+'/front/portfolio/arena/getMemberBase.do?dateStr=' + new Date().getTime(),
				data:{
					ids:permit_colleagues
				},success:function(result){
					var html = '';
					$.each(result.members,function(i,n){
						var nickName = n.nickName;
						if(nickName.length>9){
							nickName = nickName.substring(0,9)+'..';
						}
						if(i>2){
							html += '<p class="pushToSettingName" style="margin-right:30px">...</p>';
							return false;
						}
						if(i==2){
							html += '<p class="pushToSettingName" title="'
								+ n.nickName
								+'" style="margin-right:30px">'
								+ nickName
								+'</p>';
						}else{
							html += '<p class="pushToSettingName" title="'
								+ n.nickName
								+'" style="margin-right:30px">'
								+ nickName
								+'<span> ,</span></p>';
						}
					});
					$('a-permit-buddies').closest('li').find('p').remove();
					$('a-permit-buddies').before(html);
				}
			});
		}
		if(!permit_colleagues){
			var length = $('.a-permit-colleagues').closest('li').find('.select-all').length;
			if(length == 0){
				$('.a-permit-colleagues').closest('li').find('p').remove();
				$('.a-permit-colleagues').before('<p class="pushToSettingName select-all" style="margin-right:30px">All</p>');
			}
		}else{
			$.ajax({
				type:'post',
				url:base_root+'/front/portfolio/arena/getMemberBase.do?dateStr=' + new Date().getTime(),
				data:{
					ids:permit_colleagues
				},success:function(result){
					var html = '';
					$.each(result.members,function(i,n){
						var nickName = n.nickName;
						if(nickName.length>9){
							nickName = nickName.substring(0,9)+'..';
						}
						if(i>2){
							html += '<p class="pushToSettingName" style="margin-right:30px">...</p>';
							return false;
						}
						if(i==2){
							html += '<p class="pushToSettingName" title="'
								+ n.nickName
								+'" style="margin-right:30px">'
								+ nickName
								+'</p>';
						}else{
							html += '<p class="pushToSettingName" title="'
								+ n.nickName
								+'" style="margin-right:30px">'
								+ nickName
								+'<span> ,</span></p>';
						}
					});
					$('.a-permit-colleagues').closest('li').find('p').remove();
					$('.a-permit-colleagues').before(html);
				}
			});
		}
    });
    
    //选人
    var userType = '';
	$('.j-permitASetting').click(function(){
		var idsObjId = "memberIdsResult"; //存储选中用户的元素的id,值以逗号隔开
		var namesObjId = "memberNamesResult"; //存储选中用户名的元素id，值以逗号隔开
		userType = $(this).data('userType');  //选用户类型
		if(userType=='prospect'){
			idsObjId = $('#permit_prospects').val();
		}else if(userType=='existing'){
			idsObjId = $('#permit_clients').val();
		}else if(userType=='buddy'){
			idsObjId = $('#permit_buddies').val();
		}else if(userType=='colleague'){
			idsObjId = $('#permit_colleagues').val();
		}
		$("#openerMemberIdsObj").val(idsObjId);
		$("#openerMemberNamesObj").val(namesObjId);
		getSelectUserList(userType);
		$(".selectnamelistbox").css("display","block");
		
		if($(this).closest('li').find('.select-all').length == 0){
			$('.selectedtextleft span').text($(this).closest('li').find('p').length);
		}else{
			$('.selectedtextleft span').text(0);
		}
	});
	var arrIds = [];//选中用户Id
	var arrNames = [];//选中用户名称
    function getSelectUserList(userType){
    	var ids_ = '';
		var names_ = '';
		var checkSelection_ = '';
    	if(userType=='prospect'){
    		ids_ = $('#permit_prospects').val();
    		names_ = $('#permit_prospects_name').val();
    		checkSelection_ = $('#permit_prospects').val();
		}else if(userType=='existing'){
			ids_ = $('#permit_clients').val();
			names_ = $('#permit_clients_name').val();
			checkSelection_ = $('#permit_clients').val();
		}else if(userType=='buddy'){
			ids_ = $('#permit_buddies').val();
			names_ = $('#permit_buddies_name').val();
			checkSelection_ = $('#permit_buddies').val();
		}else if(userType=='colleague'){
			ids_ = $('#permit_colleagues').val();
			names_ = $('#permit_colleagues_name').val();
			checkSelection_ = $('#permit_colleagues').val();
		}
    	if(ids_ && ids_.indexOf(',') > -1){
    		arrIds = ids_.split(',');
    		arrNames = names_.split(',');
    	}else if(ids_){
    		arrIds[0] = ids_;
    		arrNames[0] = names_;
    	}else{
    		arrIds = [];
    		arrNames = [];
    	}
		$.ajax({
			type : 'get',
			datatype : 'json',
			url : base_root+'/front/ifa/info/selectUserListJson.do?datestr='+new Date().getTime(),
			data : {
				'userType':userType
			},
			async : false,
			success : function(json) {
				$(".selectedlist ul").empty();
				var liContent = '';
				var list = json.rows;
				$.each(list, function (index, array) { //遍历json数据列	
					var id = array['id'] == null ? "" : array['id'];
					var fullName = array['fullName'] == null ? "" : array['fullName'];
					var iconUrl = array['iconUrl'] == null ? "" : array['iconUrl'];
					liContent += '<li data-id="'+id+'" data-type="clients" class="container">'
								+ '<div class="selectedshadow"></div>'
								+ '<b></b>'
								+ '<i></i>'
								+ '<img src="'+base_root+iconUrl+'"/>'
								+ '<p>'+fullName+'</p>'
								+ '</li>';						
				});		
				$(".selectedlist ul").append(liContent);
				var openerObjId = $("#openerMemberIdsObj").val();
				checkSelection(checkSelection_);
			}
		});
    }
    $('body').on('mousemove','.selectedlist li',function(){
		$(this).find('.selectedshadow').addClass('selectedshadow1');
	});
	$('body').on('mouseout','.selectedlist li',function(){
		$(this).find('.selectedshadow').removeClass('selectedshadow1');
	});
    $('body').on('click','.selectedlist li',function(){//可多选
        $(this).addClass('selected_active');
        var id = $(this).attr('data-id');
        var type = $(this).attr('data-type');
        var name = $(this).children('p').first().text();
        if($(this).find('b').hasClass('selectedchecked')){
			$(this).find('i').removeClass('selectedshadow1');
			$(this).find('b').toggleClass('selectedchecked');
			var idIndex = arrIds.indexOf( id );
			var nameIndex = arrNames.indexOf( name );
			arrIds.splice(idIndex, 1);
			arrNames.splice(nameIndex, 1);
		}else{
			$(this).find('i').addClass('selectedshadow1');
			$(this).find('b').toggleClass('selectedchecked');
			arrIds.push(id);
			arrNames.push(name);
		};
		$('#memberIds').val(arrIds.join(','));
		$('#memberNames').val(arrNames.join(','));
		if($(this).parents('.myClient').find('.selectedchecked').length !== $(this).parents('.myClient').find('.selectedlist li').length){
			$(this).parents('.myClient').find('.selectedtextright input').prop('checked',false);
		}else{
			$(this).parents('.myClient').find('.selectedtextright input').prop('checked',true);
		};
		$(this).parents('.myClient').find('.selectedtextleft span').html($(this).parents('.myClient').find('.selectedchecked').length);
    });
   /**
    * 根据父窗口的类型，和 已选值，自动选定（适于编辑状态）
    */
    function checkSelection(selected){
        if (!selected) return;
        arrIds = [];//选中用户Id
    	arrNames = [];//选中用户名称
        var ifAll = false;
        if (selected.indexOf("ALL")>=0) ifAll = true;
        var strArr = selected.split(",");
        if (strArr.length==0) return;
        $.each(strArr, function(i,val){  
            if (!val || val.length==0) return true;//continue
	        $(".selectedlist").find("li").each(function(){
	            var _li = $(this);
			        var id = $(this).attr("data-id")+"";
		            if (ifAll || (val && val.length>0 && val == id)){
		                _li.click();
		                if (!ifAll) return false;
	                }
		    });
        });
    }
   /**
    * 根据父窗口的类型，和 已选值，自动选定（适于编辑状态）
    */
    function initial(){
        var openerMemberIdsObj = new Object(); 
        if (window.name == "permitASetting"){
        	var openerMemberIds = $("#openerMemberIdsObj").val();
        	openerMemberIdsObj = window.opener.document.getElementById(openerMemberIds);    
        	checkSelection( openerMemberIdsObj.value);
        }
    }
   /**
    * 返回（保存）当前选定值，自动设置父窗口对应的对象值，需保证变量定义一致
    */
    $("#btn_update").on("click",function(){
    	var openerMemberIds = $("#openerMemberIdsObj").val();
    	var openerMemberNames = $("#openerMemberNamesObj").val();
        $('#'+openerMemberIds).val($("#memberIds").val());
    	if(openerMemberNames != ''){
	        $('#'+openerMemberNames).text($("#memberNames").val());
	        $('#'+openerMemberNames).val($("#memberNames").val());
    	}
    	if(userType=='prospect'){
			$('#permit_prospects').val($("#memberIds").val());
			$('#permit_prospects_name').val($("#memberNames").val());
		}else if(userType=='existing'){
			$('#permit_clients').val($("#memberIds").val());
			$('#permit_clients_name').val($("#memberNames").val());
		}else if(userType=='buddy'){
			$('#permit_buddies').val($("#memberIds").val());
			$('#permit_buddies_name').val($("#memberNames").val());
		}else if(userType=='colleague'){
			$('#permit_colleagues').val($("#memberIds").val());
			$('#permit_colleagues_name').val($("#memberNames").val());
		}
		//$('#refreshSelection').click();
    	refreshSelection(userType);
        $(".selectnamelistbox").css("display","none");
    });
    
    function refreshSelection(userType){
		if(userType=='prospect'){
			var permit_prospects = $('#permit_prospects').val();
    		var memberNames = $('#permit_prospects_name').val();
    		if(!permit_prospects){
    			var length = $('.a-permit_prospects').closest('li').find('.select-all').length;
    			if(length == 0){
    				$('.a-permit_prospects').closest('li').find('p').remove();
    				$('.a-permit_prospects').before('<p class="pushToSettingName select-all" style="margin-right:30px">All</p>');
    			}
    		}else{
				var html = '';
				if(memberNames.indexOf(',') > -1){
					var count = memberNames.split(',').length;
					$.each(memberNames.split(','),function(i,n){
						var nickName = n;
						if(nickName.length>9){
							nickName = nickName.substring(0,9)+'..';
						}
						if(i>2){
							html += '<p class="pushToSettingName" style="margin-right:30px">...</p>';
							return false;
						}
						if(i==2||i==count-1){
							html += '<p class="pushToSettingName" title="'
								+ n
								+'" style="margin-right:30px">'
								+ nickName
								+'</p>';
						}else{
							html += '<p class="pushToSettingName" title="'
								+ n
								+'" style="margin-right:30px">'
								+ nickName
								+'<span> ,</span></p>';
						}
					});
				}else{
					var subName = '';
					if(memberNames.length>9){
						subName = memberNames.substring(0,9)+'..';
					}else{subName = memberNames;}
					html += '<p class="pushToSettingName" title="'
						+ memberNames
						+'" style="margin-right:30px">'
						+ subName
						+'</p>';
				}
				$('.a-permit-prospects').closest('li').find('p').remove();
				$('.a-permit-prospects').before(html);
    		}
		}else if(userType=='existing'){
			var permit_clients = $('#permit_clients').val();
    		var memberNames = $('#permit_clients_name').val();
    		if(!permit_clients){
    			var length = $('.a-permit-clients').closest('li').find('.select-all').length;
    			if(length == 0){
    				$('.a-permit-clients').closest('li').find('p').remove();
    				$('.a-permit-clients').before('<p class="pushToSettingName select-all" style="margin-right:30px">All</p>');
    			}
    		}else{
				var html = '';
				if(memberNames.indexOf(',') > -1){
					var count = memberNames.split(',').length;
					$.each(memberNames.split(','),function(i,n){
						var nickName = n;
						if(nickName.length>9){
							nickName = nickName.substring(0,9)+'..';
						}
						if(i>2){
							html += '<p class="pushToSettingName" style="margin-right:30px">...</p>';
							return false;
						}
						if(i==2||i==count-1){
							html += '<p class="pushToSettingName" title="'
								+ n
								+'" style="margin-right:30px">'
								+ nickName
								+'</p>';
						}else{
							html += '<p class="pushToSettingName" title="'
								+ n
								+'" style="margin-right:30px">'
								+ nickName
								+'<span> ,</span></p>';
						}
					});
				}else{
					var subName = '';
					if(memberNames.length>9){
						subName = memberNames.substring(0,9)+'..';
					}else{subName = memberNames;}
					html += '<p class="pushToSettingName" title="'
						+ memberNames
						+'" style="margin-right:30px">'
						+ subName
						+'</p>';
				}
				$('.a-permit-clients').closest('li').find('p').remove();
				$('.a-permit-clients').before(html);
    		}
		}else if(userType=='buddy'){
			var permit_buddies = $('#permit_buddies').val();
    		var memberNames = $('#permit_buddies_name').val();
    		var html = '';
			if(!permit_buddies){
				var length = $('a-permit-buddies').closest('li').find('.select-all').length;
				if(length == 0){
					$('a-permit-buddies').closest('li').find('p').remove();
					$('a-permit-buddies').before('<p class="pushToSettingName select-all" style="margin-right:30px">All</p>');
				}
			}else{
				if(memberNames.indexOf(',') > -1){
					var count = memberNames.split(',').length;
					$.each(memberNames.split(','),function(i,n){
						var nickName = n;
						if(nickName.length>9){
							nickName = nickName.substring(0,9)+'..';
						}
						if(i>2){
							html += '<p class="pushToSettingName" style="margin-right:30px">...</p>';
							return false;
						}
						if(i==2||i==count-1){
							html += '<p class="pushToSettingName" title="'
								+ n
								+'" style="margin-right:30px">'
								+ nickName
								+'</p>';
						}else{
							html += '<p class="pushToSettingName" title="'
								+ n
								+'" style="margin-right:30px">'
								+ nickName
								+'<span> ,</span></p>';
						}
					});
				}else{
					var subName = '';
					if(memberNames.length>9){
						subName = memberNames.substring(0,9)+'..';
					}else{subName = memberNames;}
					html += '<p class="pushToSettingName" title="'
						+ memberNames
						+'" style="margin-right:30px">'
						+ subName
						+'</p>';
				}
				$('.a-permit-buddies').closest('li').find('p').remove();
				$('.a-permit-buddies').before(html);
			}
		}else if(userType=='colleague'){
    		var permit_colleagues = $('#permit_colleagues').val();
    		var memberNames = $('#permit_colleagues_name').val();
    		if(!permit_colleagues){
    			var length = $('.a-permit-colleagues').closest('li').find('.select-all').length;
    			if(length == 0){
    				$('.a-permit-colleagues').closest('li').find('p').remove();
    				$('.a-permit-colleagues').before('<p class="pushToSettingName select-all" style="margin-right:30px">All</p>');
    			}
    		}else{
				var html = '';
				if(memberNames.indexOf(',') > -1){
					var count = memberNames.split(',').length;
					$.each(memberNames.split(','),function(i,n){
						var nickName = n;
						if(nickName.length>9){
							nickName = nickName.substring(0,9)+'..';
						}
						if(i>2){
							html += '<p class="pushToSettingName" style="margin-right:30px">...</p>';
							return false;
						}
						if(i==2||i==count-1){
							html += '<p class="pushToSettingName" title="'
								+ n
								+'" style="margin-right:30px">'
								+ nickName
								+'</p>';
						}else{
							html += '<p class="pushToSettingName" title="'
								+ n
								+'" style="margin-right:30px">'
								+ nickName
								+'<span> ,</span></p>';
						}
					});
				}else{
					var subName = '';
					if(memberNames.length>9){
						subName = memberNames.substring(0,9)+'..';
					}else{subName = memberNames;}
					html += '<p class="pushToSettingName" title="'
						+ memberNames
						+'" style="margin-right:30px">'
						+ subName
						+'</p>';
				}
				$('.a-permit-colleagues').closest('li').find('p').remove();
				$('.a-permit-colleagues').before(html);
    		}
		}
	}
    $("#btn_cancel").on("click",function(){
    	$(".selectnamelistbox").css("display","none");
    });
    $(".wmes-close").on("click",function(){
    	$(".selectnamelistbox").css("display","none");
    });
    //全选
    $('.SelectAll').on('click',function(){
		$(this).parents('.selectedtextright').find('input').click();		
	});
    //全选
	$('.selectedtextright input').on('click',function(){
		var _this = $(this);
        var _parent = _this.parent().parent().parent().parent();
        if (_this.is(':checked')){//检查checkbox是否选中
	        _parent.parent().find(".selectedlist").find("li").each(function(){
	           var _li = $(this);
	           if (!_li.find("b").hasClass("selectedchecked"))
	               _li.click();
	        });
        }else{
        	_parent.parent().find(".selectedlist").find("li").each(function(){
               var _li = $(this);
               if (_li.find("b").hasClass("selectedchecked"))
                   _li.click();
            });
        }
	});
	//反选
	$('.selectedtextright span').on('click',function(){
        $("body .selectedlist .container").each(function(){
        	$(this).click();
        });
	});
	$('.ifa-more-ico').on('click',function(){
		$(this).toggleClass('ifa-more-icoactive');
		$(this).parents('.myClient').find('.selectedtext').toggleClass('ifa-more-ico-hidden');
		$(this).parents('.myClient').find('.selectedlist').toggleClass('ifa-more-ico-hidden');
	});
	
	function getUrlParam(name){  
 	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
 	    var r = window.location.search.substr(1).match(reg);  
 	    if (r!=null) return unescape(r[2]);  
 	    return null;  
 	}
	function urlUpdateParams(url, name, value) {
        var r = url;
        if (r != null && r != 'undefined' && r != "") {
            value = encodeURIComponent(value);
            var reg = new RegExp("(^|)" + name + "=([^&]*)(|$)");
            var tmp = name + "=" + value;
            if (url.match(reg) != null) {
                r = url.replace(eval(reg), tmp);
            }
            else {
                if (url.match("[\?]")) {
                    r = url + "&" + tmp;
                } else {
                    r = url + "?" + tmp;
                }
            }
        }
        return r;
    }
});