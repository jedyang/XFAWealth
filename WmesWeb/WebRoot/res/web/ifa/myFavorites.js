
define(function(require) {
	//依赖
	var $ = require('jquery');
	require('layer');
	var angular = require('angular');
	
	var mybase = angular.module('ifaTable', ['truncate']);
	
	        require('layer');
	$(".tab_list_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".myfav-list").hide().eq($(this).index()).show();
	});

	$(document).on("click",".ifa-more-ico",function(){
    	$(this).parents(".watching-rows").toggleClass("watching-narrow");
    });
	
	loadMyWatchListData("");
	function loadMyWatchListData(typeId){
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/ifa/info/getMyWatchlistJson.do?datestr="+ new Date().getTime(),
			data : { 'typeId':typeId },
			 beforeSend: function () {},
			 complete: function () {
			 },
			success : function(json) {////console.log(json);
				var html = '';
				//下拉组装数据
				//$('.watching_xiala').find('ul').empty().append('<li data-value="">All</li>');
				//组装下拉数据
				//var li_html = '<li data-value="1">Fund</li><li data-value="2">Stock</li><li data-value="3">Bond</li>';
				//$('.watching_xiala').find('ul').append(li_html);
				var dataList = JSON.parse(json);
				var count = dataList.length;
				$.each(dataList,function(i,n){
					var typeId = n.typeId;
					var typeName = n.typeName;
					
					//输出类型头部信息
					if(typeId=="1")typeName=MYFAVOURITES_WATCHINGLIST_TYPE_FUND;
					else if(typeId=="2")typeName=MYFAVOURITES_WATCHINGLIST_TYPE_STOCK;
					else if(typeId=="3")typeName=MYFAVOURITES_WATCHINGLIST_TYPE_BOND;
					var element_id = 'watching-rows-'+typeId;
					var type_html = '<div id="'+element_id+'" class="watching-rows">';
					type_html += '<div class="watching-title-wrap">';
					type_html += '<div class="watching-box"></div>';
					type_html += '<input class="watching-title" type-id="'+typeId+'" value="'+typeName+'" value="" name=""  readonly=""><span class="watching-title-val">111</span>';
					type_html += '<div class="group-list-ico" style="display:inline-block" typeId="'+typeId+'"><span class="group-list-edit"></span><span class="group-list-close"></span></div>';
					if(typeId=='10001'){
						type_html += '<img style="display:none;" class="watching-edit watching-type-edit" src="'+base_root+'/res/images/ifa/modify_ico.png">';
						type_html += '<img style="display:none;" class="watching-edit watching-type-update" src="'+base_root+'/res/images/ifa/save.png">';
					}
					else{
						//type_html += '<img class="watching-edit watching-type-edit" src="'+base_root+'/res/images/ifa/modify_ico.png">';
						//type_html += '<img style="display:none;" class="watching-edit watching-type-update" src="'+base_root+'/res/images/ifa/save.png">';
					}
					//type_html += '<img typeid="'+typeId+'" class="watching-deleted" src="'+base_root+'/res/images/ifa/garbage_ico.png">';
					type_html += '<span class="ifa-more-ico"></span>';
					type_html += '</div>';
					type_html += '<div class="watching-list-wrap">';
					
					var myFavoritesWatchingListVO = n.myFavoritesWatchingListVO; //一个新list
					//console.log(myFavoritesWatchingListVO);
					if(myFavoritesWatchingListVO!=null){
						type_html += '<table class="watching-table">';
						type_html += '<tbody>';
						type_html += '<tr class="watching-table-th">';
						type_html += '<th width="20%" class="watching-tables_fnames">'+MYFAVOURITES_WATCHINGLIST_LIST_PORDUCTNAME+'</th>';
						type_html += '<th width="10%" class="watching-tables-header">'+MYFAVOURITES_WATCHINGLIST_LIST_CUR+'</th>';
						type_html += '<th width="10%" class="watching-tables-header">'+MYFAVOURITES_WATCHINGLIST_LIST_RISK+'</th>';
						type_html += '<th width="10%" class="watching-tables-header">'+MYFAVOURITES_WATCHINGLIST_LIST_LATESTNAVPRICE+'</th>';
						type_html += '<th width="10%" class="watching-tables-header">'+MYFAVOURITES_WATCHINGLIST_LIST_VALUATIONDATE+'</th>';
						type_html += '<th width="10%" class="watching-tables-header">'+MYFAVOURITES_WATCHINGLIST_LIST_DAILY+'</th>';
						type_html += '<th width="10%" class="watching-tables-header">'+MYFAVOURITES_WATCHINGLIST_LIST_ONEYEARRETURN+'</th>';
						type_html += '<th width="10%" class="watching-tables-header">'+MYFAVOURITES_WATCHINGLIST_LIST_YTDDATE+'</th>';
						type_html += '<th width="10%" class="watching-tables-header">'+MYFAVOURITES_WATCHINGLIST_LIST_ACTIONS+'</th>';
						type_html += '</tr>';
						
						var fund_list_html = '';
						$.each(myFavoritesWatchingListVO,function(p,t){
							var watchingId = t.watchingId;
							var remark = t.remark;
							if(remark==null)remark='';
							//获取每条基金信息
							var eachFund = t.fundInfo;console.log(eachFund);
								var fundName = eachFund.fundName;
								var fundId = eachFund.fundId;
								var productId = eachFund.productId;
								var fundCurrencyCode = eachFund.fundCurrencyCode;
								var fundCurrency = eachFund.fundCurrency;
								var risk = eachFund.fundInfo.riskLevel;
								var lastNav = eachFund.fundInfo.lastNav;
								var lastNavUpdate = eachFund.fundInfo.lastNavUpdate;
								var dayReturn = eachFund.fundInfo.dayReturn;
								var issueDate = eachFund.fundInfo.issueDate;
								var issueDateFormat = formatdate(lastNavUpdate,DEFFORMATDATE);
								var fundReturnYear1 = eachFund.fundReturnYear1;
								var fundReturnYTD = eachFund.fundReturnYTD;
								 var row = '<tr data-value="test">';
		                         row += '<td class="watching-tables-fnames"><a class="ifa-a-href" href="'+base_root+'/front/fund/info/fundsdetail.do?id='+fundId+'"><span watchingid="'+watchingId+'" class="span_productname">'+fundName+'</span></a></td>';
								 row += '<td class="watching-tdcenter">'+fundCurrency+'</td>';
								 row += '<td class="watching-tdcenter">'+risk+'</td>';
								 row += '<td class="watching-tdcenter">'+genColorFloadNum(lastNav)+'</td>';
								 row += '<td class="watching-tdcenter">'+issueDateFormat+'</td>';
								 row += '<td class="watching-tdcenter">'+genColorFloadNum(dayReturn)+'</td>';
								 //row += '<td class="watching-tdcenter"><img  src="'+base_root+'/res/images/strategy/strategy_chart_03.png"></td>';
								 row += '<td class="watching-tdcenter">'+genColorFloadNum2(fundReturnYear1)+'</td>';
								 row += '<td class="watching-tdcenter">'+genColorFloadNum2(fundReturnYTD)+'</td>';
								 row += '<td class="watching-tdcenter">';
								 row += '<span fundid="'+fundId+'" productid="'+productId+'" wid="'+watchingId+'" class="watching-cart cancel-watching-img"></span>';/*class="watching-delete "*/
								 //if(remark!=''&&remark!=undefined)
								 row += '<img id="img_remark_'+watchingId+'" watchingid="'+watchingId+'" remark-value="'+remark+'" class="watching-list-edit" src="'+base_root+'/res/images/ifa/modify_ico.png">';
								 //row += '<div id="box1111ppp" onmouseover="display()" onmouseout="disappear()"></div>';
								 row += '</td>';
								 row += '</tr>';
								fund_list_html += row;
						});
						
						fund_list_html += '</tbody>';
						fund_list_html += '</table>';
						type_html+=fund_list_html;
						
					}
					type_html += '</div>';
					type_html += '</div>';
					html += type_html;
				});
				
				if(count==0){
					//$('.watching_xiala_wrap').hide();
					var nodata_html = '<div class="wmes-nodata-tips" style="display:block;margin-top:0%">'; 
					nodata_html += '<div class="wmes-nodata-tips portfolioTips" style="display:block;padding: 0px 0;">';
					nodata_html += '<img class="wmes-nodata-img" src="'+base_root+'/res/images/no_data_ico.png">';
					nodata_html += '<span class="wmes-nodate-word">'+IFASPACE_LANG_NODATA_TIPS+'</span>';
					nodata_html += '</div>';
					$('.myfav-list-rows').empty().append(nodata_html);
				} else{
					
					$('.myfav-list-rows').empty().append(html);
				}
				
				for(i=0;i<$('.watching-title-val').length;i++){
						$('.watching-title-val').eq(i).text($('.watching-title-val').eq(i).prev().val());
						//console.log($('.watching-title-val').eq(i).prev().val());
					}
			}
		});
	}
	
	function genColorFloadNum(number){
		var fmtnumber = (parseFloat(number));
		var rcolor="color:#24b31d!important;";
		var fcolor="color:red!important;";
		if(MEMBER_DEFDISPLAYCOLOR=='1'){
			var rcolor="color:#24b31d!important;";
			var fcolor="color:red!important;";
		} else if(MEMBER_DEFDISPLAYCOLOR=='2'){
			var rcolor="color:red!important;";
			var fcolor="color:#24b31d!important;";
		}
		if(fmtnumber>0){
			return '<span style="'+rcolor+'">'+number+'</span>';
		} else if(fmtnumber==0){
			return '<span style="">'+number+'</span>';
		} else if(fmtnumber<0){
			return '<span style="'+fcolor+'">'+number+'</span>';
		} else return '<span style="">'+number+'</span>';
	}
	
	function genColorFloadNum2(number){
		//var number = numberPercent.replace('%','');
		var fmtnumber = (parseFloat(number));
		var rcolor="color:#24b31d!important;";
		var fcolor="color:red!important;";
		if(MEMBER_DEFDISPLAYCOLOR=='1'){
			var rcolor="color:#24b31d!important;";
			var fcolor="color:red!important;";
		} else if(MEMBER_DEFDISPLAYCOLOR=='2'){
			var rcolor="color:red!important;";
			var fcolor="color:#24b31d!important;";
		}
		if(fmtnumber>0){
			return '<span style="'+rcolor+'">'+number+'%</span>';
		} else if(fmtnumber==0){
			return '<span style="">'+number+'%</span>';
		} else if(fmtnumber<0){
			return '<span style="'+fcolor+'">'+number+'%</span>';
		} else return '<span style="">'+number+'%</span>';
	}
	
	function formatdate(datestr,fmt) { 
		var date = new Date(datestr);  
	     var o = { 
	        "M+" : date.getMonth()+1,                 //月份 
	        "d+" : date.getDate(),                    //日 
	        "h+" : date.getHours(),                   //小时 
	        "m+" : date.getMinutes(),                 //分 
	        "s+" : date.getSeconds(),                 //秒 
	        "q+" : Math.floor((date.getMonth()+3)/3), //季度 
	        "S"  : date.getMilliseconds()             //毫秒 
	    }; 
	    if(/(y+)/.test(fmt)) {
	            fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	    }
	     for(var k in o) {
	        if(new RegExp("("+ k +")").test(fmt)){
	             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
	         }
	     }
	    return fmt; 
	}    
	
	
	
	function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURIComponent(r[2]);
    return null;
	};
	
	$('.tab_list_tab li').eq(getQueryString('jp')).click();
	//点击下拉筛选类型
	$("body").on('click', '.watching_xiala ul li', '', function () {
		var typeId = $(this).attr('data-value');
		var typeName = $(this).text();
		loadMyWatchListData(typeId);
		if(typeId!=''){
			$('#txt-xiala-selected').val(typeName);
			//同级的隐藏
			//var cur_element_id = '#watching-rows-'+typeId;
			
			
			//$(cur_element_id).show().siblings().hide();
		} else{ //选择全部
			$('#txt-xiala-selected').val(typeName);
			$('.watching-rows').show();
		}
		$(this).parents('.watching_xiala').toggleClass("xiala-show");
		$(this).parents(".watching_xiala").toggleClass("xiala-show").find("input").val($(this).html());       
	     //e.stopPropagation(); 
		
	});
	
	//点击弹出备注窗口
	$("body").on('click', '.watching-list-edit', '', function () {
		var remark = $(this).attr('remark-value');
		var watchingid = $(this).attr('watchingid');
		//页面层
		layer.open({
		  type: 1,
		  title:MYFAVOURITES_WATCHINGLIST_LIST_DIALOGREMARKTITLE,
		  btn: [MYFAVOURITES_WATCHINGLIST_LIST_DIALOGBTNSAVE, MYFAVOURITES_WATCHINGLIST_LIST_DIALOGBTNCLOSE],
		  yes:function(index){ 
			  var elelent_id = '#'+watchingid;
			  var newRemark = $(elelent_id).val();
			  //保存备注信息
			  $.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/ifa/info/saveMyWatchlistRemark.do?datestr="+ new Date().getTime(),
					data : {'id':watchingid,'remark':newRemark},
					beforeSend: function () {},
	                complete: function () {},
					success : function(json) {
						var obj = $.parseJSON(json); 
						if(obj.result){
							//layer.msg(MYFAVOURITES_WATCHINGLIST_LIST_DIALOGSAVESUCCESS);
							layer.msg(langMutilForJs['global.success.save'],{icon:2});
							var element_img_remark_id = '#img_remark_'+watchingid;
							$(element_img_remark_id).attr('remark-value',newRemark);
							setTimeout(function(){layer.closeAll();},500);
						}
					}
				});
		  },
		  skin: 'layui-layer-rim', //加上边框
		  area: ['520px', '340px'], //宽高
		  content: '<textarea id="'+watchingid+'" style=" height: 200px;width: 95%;margin: 5px 5px 5px 5px;border:1px solid #009ee5;">'+remark+'</textarea>'
		});
		
	});
	
	//鼠标移到图标上面时
	$("body").on('mouseover', '.span_productname', '', function () {
		var watchingid = $(this).attr('watchingid');
		var td_element = $(this).parents('.watching-tables-fnames').next();
		var element_img_remark_id = '#img_remark_'+watchingid;
		var ps = td_element.position();  
        $("#box").css("position", "absolute");  
        $("#box").css("left", ps.left +80); //距离左边距  
        $("#box").css("top", ps.top + 32); //距离上边距  
        var remark_value = $(element_img_remark_id).attr('remark-value');
        if(remark_value!=''&&remark_value!=undefined){ //当没有备注信息时不显示弹出层出来
        	$("#box").text($(element_img_remark_id).attr('remark-value')).show();  
        }
        
	});
	
	//
	$("body").on('mouseout', '.span_productname', '', function () {
        $("#box").hide();  
	});
	
	//修改类别名称
	$("body").on('click', '.watching-type-edit', '', function () {
		$(this).prev().css({'border':'1px solid #009ee5'}).removeAttr('readonly');
		$(this).hide();
		//显示更新按钮
		$(this).next().show();
	});
	
	//更新类别名称
	$("body").on('click', '.watching-type-update', '', function () {
		var typeName = $(this).prev().prev().val();
		var typeId = $(this).prev().prev().attr('type-id');
		var element = $(this);
		//$(this).prev().css({'border':'1px solid #009ee5'}).removeAttr('readonly');
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/ifa/info/modifyMyWatchTypeName.do?datestr="+ new Date().getTime(),
			data : {'typeId':typeId,'typeName':typeName},
			beforeSend: function () {},
            complete: function () {},
			success : function(json) {
				var obj = $.parseJSON(json); 
				if(obj.result){
					//layer.msg('Modify Type Name Successful!' );
					layer.msg(langMutilForJs['global.success.save'],{icon:2});
					element.prev().prev().css({'border':'0px solid White'}).attr('readonly','readonly');
					element.prev().show();
					element.hide();
				}
			}
		});
	});
	
	//删除类型下的所有自选基金
	$("body").on('click', '.watching-deleted', '', function () {
		var typeid = $(this).attr('typeid'); 
		var tr = $(this).parent().parent();
		//tr.remove();
		if(typeid!=''){
			layer.confirm('Do you sure to delete the type?', { icon: 3 ,btn: ['Sure','Cancel'] },function () { 
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/ifa/info/delMyWatchlist.do?datestr="+ new Date().getTime(),
				data : {'typeId':typeid},
				beforeSend: function () {},
                complete: function () {},
				success : function(json) {
					var obj = $.parseJSON(json); 
					if(obj.result){
						//layer.msg('Delete Inverstor Training Successful!', {icon: 1});
						layer.msg(langMutilForJs['global.success.delete'],{icon:2});
						////console.log($(this).html());
						//$(this).parent().parent().remove();
						tr.remove();
						layer.closeAll();
						//window.location.href = window.location.href;
					}
				}
			});
		  });
		}
	});
	
	//删除我备注的组合策略数据
	$("body").on('click', '.cancel-webfollow-img', '', function () {
		var followid = $(this).attr('followid'); 
		var li = $(this).parents('li');
		//tr.remove();
		if(followid!=''&&followid!=undefined){
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/ifa/info/deleteMyWebFollow.do?datestr="+ new Date().getTime(),
				data : {'id':followid},
				beforeSend: function () {},
                complete: function () {},
				success : function(json) {
					if(json.result==true){
						//layer.msg('Delete Successful' );
						layer.msg(langMutilForJs['global.success.delete'],{icon:2});
						li.fadeTo("slow", 0.01, function(){//fade
						    $(this).slideUp("slow", function() {//slide up
						      $(this).remove();//then remove from the DOM
						    });
						  });
						setTimeout(function(){layer.closeAll();},500);
					}
				}
			});
		}
	});
	
	
	//下拉
	$(".watching_xiala").on("click",function(){
		if( $(this).find("ul").hasClass("funds_currency_active") ){
			$(this).find("ul").removeClass("funds_currency_active").hide();
		}else{
			$(this).find("ul").addClass("funds_currency_active").show();
		}
	});
	$(".watching_xiala").on('mouseleave',function(){
		$(this).find("ul").removeClass("funds_currency_active").hide();
	});
    $(".watching-edit").on("click",function(){
    	//修改proposal 名字
    	$(this).siblings(".watching-title").toggleClass("watching-edit-name");
    	if( $(this).siblings(".watching-title").hasClass("watching-edit-name") ){
  　　		$(this).siblings(".watching-title").removeAttr("readonly");
    	}else{
    		$(this).siblings(".watching-title").attr("readonly","readonly")
    	}
    });
    $(".watching-title").on("blur",function(){
    	$(this).removeClass("watching-edit-name")
    	$(this).attr("readonly","readonly")
    });

    $(".watching-title").each(function(){
    	$(this).attr("size",$(this).val().length);
    });

     // 下拉
    $(".watching_xiala1").on("click",function(){
        $(this).toggleClass("xiala-show"); alert('3');
    });
//    $(".watching_xiala li").on("click",function(e){
//        $(this).parents(".watching_xiala").toggleClass("xiala-show").find("input").val($(this).html());       
//        e.stopPropagation(); 
//    });   

    $(".watching-list-edit").on("click",function(){
        var self = $(this),
            selfVal = $(this).parents("tr").attr("data-value");
        layer.prompt({
              title: 'Remarks',
              value: selfVal,
              formType: 0 
            }, function(pass){
                self.parents("tr").attr("data-value",pass);
                layer.closeAll();
        });
    });
    
    $("body").on('click', function () {
    	//$('.watching-title').css({'border':'0px solid White'}).attr('readonly','readonly');
	});
    
    // 取消关注
	$("body").on("click",".cancel-watching-img",function(){
		var loginVal = 'true',
			_this = $(this),
			wid  = _this.attr("wid"),funid=_this.attr('fundid'),productid=_this.attr('productid');
		var tr = $(this).parents('tr');
		if(loginVal == 'true'){
			$.ajax({
	    		url:base_root+"/front/fund/info/setFoundCollection.do?r="+Math.random(),
	    		data:{'opType':'Delete','fundId':funid,'productId':productid},
	    		dataType:"json",
	    		type:"get",
	    		success:function(data){ 
	    			if(data.result==true){
	    				tr.fadeTo("slow", 0.01, function(){//fade
						    $(this).slideUp("slow", function() {//slide up
						      $(this).remove();//then remove from the DOM
						    });
						  });
	    				layer.msg(langMutilForJs['global.success'],{icon:2});
						setTimeout(function(){layer.closeAll();},200);
	    			}
	    		},
	    		error:function(){
	    			lay.msg('error',{icon:2});
	    		}
	    	});
	    	
		}else{
			window.parent.location.href=base_root+"/front/viewLogin.do?r="+Math.random();
			return;
		}		
	});
	
	
	mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
		$scope.newsList='';
		$scope.newsPage=1;
		$scope.topicList='';
		$scope.topicPage=1;
		var rows=10;
		getNewsList();
		
		function getNewsList(){
			var url = base_root + "/front/news/info/favouriteNewsJson.do",
			data = "rows=" + rows + "&page=" + $scope.newsPage ;
			$http({
					url: url,
					method: 'POST',
					data: data,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				})
				.success(function(response) {
					//console.log(response);
					if(response.list.length > 0){
					if( $scope.newsPage === 1 ){
             		 	 $scope.newsList = '';
                         $scope.newsList =  response.list;
                     }else{
                        $scope.newsList = $scope.newsList.concat(response.list);
                     }
					
					var totalPage=Math.ceil(response.total/rows);
					if($scope.newsPage<totalPage){
						$('.newskmore').show();
					}else{
						$('.newskmore').hide();
					}
					
	         		}else{
	        			 if( $scope.newsPage === 1 ){
	        				 $(".no_news_tips").show();
	         			 }
	         		}
					
				}).then(function(data){
					if($scope.newsPage===1){
						getTopicList();
					}
					
				})
		}
		
		$(".newskmore").on('click',function(){
			$scope.newsPage++;
			getNewsList();
		})
		
		$('.topicMore').on('click',function(){
			$scope.topicPage++;
			getTopicList();
		})
		
		function getTopicList(){
			var url = base_root + "/front/community/space/getMyFavoriteList.do",
			data = "rows=" + rows + "&page=" + $scope.topicPage;
			$http({
					url: url,
					method: 'POST',
					data: data,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				})
				.success(function(response) {console.log(response);
					if(response.list.length > 0){
					if($scope.topicList!=''){
						$scope.topicList=$scope.topicList.concat(response.list);
					}else{
						$scope.topicList=response.list;
					}
					
					var totalPage=Math.ceil(response.total/rows);
					if($scope.topicPage<totalPage){
						$('.topicMore').show();
					}else{
						$('.topicMore').hide();
					}
					}
					else{
						if($scope.topicPage===1){
							 $(".no_topic_tips").show();
						}
					}
					
					
					
				});
		}
		
		//取消收藏topic
		$(".mf-fav-topic").on("click",".cancel-webfollow-img",function(){
			var li=$(this).parents("li");
			var id=$(this).attr("id");
			$.ajax({
	    		type:"post",
	    		datatype:"json",
	    		url:base_root+'/front/community/info/saveBehavior.do',
	    		data:{id:id,type:"t",isCancel:"1",behavior:'favorite'},
	    		success:function(json){
	    			if(json.result){
	    				li.fadeTo("slow", 0.01, function(){//fade
						    $(this).slideUp("slow", function() {//slide up
						      $(this).remove();//then remove from the DOM
						    });
						  });
	    			}
	    			
	    		}
	    		
	    	})
		})
		//取消收藏news
		$(".mf-fav-news").on("click",".cancel-webfollow-img",function(){
			var infoId=$(this).attr("newsId");
			var li=$(this).parents("li");
			$.ajax({
				type:"post",
				datatype:"json",
				url:base_root+"/front/news/info/saveNewsFavorite.do",
				data:{status:"0",infoId:infoId},
				success:function(json){
					if(json.result){
						li.fadeTo("slow", 0.01, function(){//fade
						    $(this).slideUp("slow", function() {//slide up
						      $(this).remove();//then remove from the DOM
						    });
						  });
					}else{
						////console.log("collection faile");
					}
				}
			})
		})
		$(document).on('click','.group-list-edit',function(){
			$(this).closest('.watching-title-wrap').find('.watching-title').removeAttr('readonly');
			$(this).closest('.watching-title-wrap').find('.watching-title').next("span").text($(this).closest('.watching-title-wrap').find('.watching-title').val());
			$(this).closest('.watching-title-wrap').find('.watching-title').next("span").css("display","none");
			$(this).closest('.watching-title-wrap').find('.watching-title').css("display","block");
			$(this).closest('.group-list-ico').addClass('my-group-edit');
		});
		
		$(document).on('click','.my-group-edit .group-list-edit',function(){
			$(this).closest('.watching-title-wrap').find('.watching-title').css("display","none");
			$(this).closest('.watching-title-wrap').find('.watching-title').next("span").css("display","block");
			var _this=this;
			var id=$(this).parents('.group-list-ico').attr("typeId");
			var name=$(this).parents(".watching-title-wrap").find("input").val();
			if(name==undefined || name==""){
				layer.msg(langMutilForJs['fund.favourite.type.tips'],{icon:3});
				return ;
			}
			$.ajax({
				type:'post',
				datatype:'json',
				url:base_root+'/front/web/watchlist/saveWatchType.do',
				data:{id:id,type:name},
				success:function(json){
					if(json.result){
						var id=json.watchlistType.id;
						$(_this).parents('.group-list-ico').attr("typeId",id);
						$(_this).closest('.watching-title-wrap').find('.watching-title').attr('readonly','readonly');
						$(_this).closest('.watching-title-wrap').find('.watching-title').next("span").text(name);
						$(_this).closest('.group-list-ico').removeClass('my-group-edit');
						layer.msg(langMutilForJs['global.success.save'],{icon:2});
						
					}else{
						layer.msg(langMutilForJs['global.failed.save'],{icon:1});
					}
				}
			})
			
			
		});
		
		$(document).on('click','.group-list-close',function(){
			var typeId=$(this).parents('.group-list-ico').attr('typeId');
			var _this=this;
			if(typeId!=undefined && typeId!=""){
				   layer.confirm(langMutilForJs['fund.favourite.type.deleteTips'], {
					   btn: [langMutilForJs['global.true'],langMutilForJs['global.false']] //按钮
					 }, function(){
						 $.ajax({
							   type:'post',
							   datatype:'json',
							   url:base_root+'/front/web/watchlist/delWatchType.do',
							   data:{typeId:typeId},
							   success:function(json){
								   layer.closeAll();
								   if(json.result){
									   $(_this).parents(".watching-rows").fadeTo("slow", 0.01, function(){//fade
										    $(this).slideUp("slow", function() {//slide up
										      $(this).remove();//then remove from the DOM
										    });
										  });
									   
									   layer.msg(langMutilForJs['global.success.delete'],{icon:2});
								   }else{
									   layer.msg(langMutilForJs['global.failed.delete'],{icon:1});
								   }
								  
							   }
						   })
						   
					 })
				   
			   }else{
				   $(_this).parents(".watching-rows").fadeTo("slow", 0.01, function(){//fade
					    $(this).slideUp("slow", function() {//slide up
					      $(this).remove();//then remove from the DOM
					    });
					  });
			   }
		})
		
		$(".mylist-new-bn").on('click',function(){
			var html='<div id="watching-rows-1" class="watching-rows">'
				+'<div class="watching-title-wrap">'
				+'<div class="watching-box"></div>'
				+'<input class="watching-title" type-id="1" value="" name="" focus style="border: 1px solid rgb(215, 220, 228);"><span class="watching-title-val">111</span>'
				+'<div class="group-list-ico my-group-edit" style="display:inline-block">'
				+'<span class="group-list-edit"></span>'
				+'<span class="group-list-close"></span>'
				+'</div>'
				+'<span class="ifa-more-ico"></span>'
				+'</div>'
				+'<div class="watching-list-wrap"></div>'
				+'</div>'
				$('.myfav-list-rows').prepend(html);
			   $('.myfav-list-rows').children("div").eq(0).find("input").css("display","block");
			   $('.myfav-list-rows').children("div").eq(0).find("input").next("span").css("display","none");
			   $('.myfav-list-rows').children("div").eq(0).find("input").focus();
			
		})
	}])
    
});