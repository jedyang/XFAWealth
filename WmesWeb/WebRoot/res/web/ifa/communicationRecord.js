
define(function(require) {
	var $ = require('jquery');
		var angular = require('angular');	
		var mybase = angular.module('ifaTable', ['truncate']);
		require('layer');

		require('laydate');
//	var 	WebUploader = require('webuploader');
	require("wmes_upload");
	require("swiper");
	var selector =  require('ifaSelectUser');
	selector.init();
	
	var dateFormat=$("#dateFormat").val();
	
	var Loading = require('loading');
	var oLoading = new Loading($(".communication-contents-list"));
	var swiper= new Swiper('.order-sapce-wrapper .swiper-container', {
	    slidesPerView: 'auto',
        grabCursor: true,
        preventClicks : false,
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev'
    });
	$('#recommend-date-to').click(function(){
        var max = '';
        if(!$('#recommend-date-from').val()){
            max = laydate.now();
        }else{
            max = $('#recommend-date-from').val();
        }
        laydate({
               istime: false,
               max:max,
               elem: '#recommend-date-to',
               format: 'YYYY-MM-DD',
               istoday: true,
               choose: function(datas){ 
               } 
        });
    });
    $('#recommend-date-from').click(function(){
        var min = '';
        if(!$('#recommend-date-to').val()){
            min = laydate.now();
        }else{
            min = $('#recommend-date-to').val();
        }
        laydate({
            istime: false,
            min:min,
            elem: '#recommend-date-from',
            format: 'YYYY-MM-DD'
        });
    });

	$("#searchKey").val($("#nickName").val());
	var find=$("#noTips").val();
	if(find==1){
		layer.msg(langMutilForJs["msg.findClient.fail"]);
	}
	/*new JsDatePick({
      useMode:2,
      target:"recommend-date-to",
      dateFormat:"%Y-%m-%d"
    });
     new JsDatePick({
      useMode:2,
      target:"recommend-date-from",
      dateFormat:"%Y-%m-%d"
    });*/
   	$(".document-list-swiper").each(function(){
		var swiper = new Swiper($(this).find('.document-swiper-wrapper'), {
	        slidesPerView: 'auto',
	        preventClicks : false,
	        nextButton: $(this).find('.swiper-button-next'),
	        prevButton: $(this).find('.swiper-button-prev'),
	    });
	});
	function swiperInit(number){
        var galleryTop = new Swiper('.gallery-top', {
            nextButton: '.swiper-button-next',
            prevButton: '.swiper-button-prev',
            initialSlide :number,
            spaceBetween: 10,
            onSlideChangeStart:function(swiper){
                $(".document-gall-serial").html(swiper.activeIndex+1);
            }
        });
        var galleryThumbs = new Swiper('.gallery-thumbs', {
            spaceBetween: 10,
            centeredSlides: true,
            slidesPerView: 'auto',
            touchRatio: 0.2,
            initialSlide :number,
            slideToClickedSlide: true
        });
        galleryTop.params.control = galleryThumbs;
        galleryThumbs.params.control = galleryTop;
    }
	
    $(document).on("click",".upload-eachimg111",function(){
        var swiper_html = "",
            _this = $(this);
            var _parent = _this.parents(".photo-list").find(".upload-eachimg");
            _parent.each(function(){
            swiper_html += '<div class="swiper-slide" style="background-image:url('+ $(this).attr("src") +')"></div>'
        });
        $(".document-gall-total").html(_parent.length);
        $(".document-gall-serial").html(_this.parent().index());
        $(".gallery-top").remove();
        $(".gallery-thumbs").remove();
        $(".document-gallery-contents").prepend('<div class="swiper-container gallery-top"><div class="swiper-wrapper" id="galler-big-img">'+swiper_html+'</div></div>');

        $(".document-gall-name-wrap").before('<div class="swiper-container gallery-thumbs"><div class="swiper-wrapper" id="galler-small-img">'+swiper_html+'</div></div>');


        swiperInit(_this.parent().index());

        $(".document-gallery-images").addClass("document-gallery-blocl");
        
        $('.document-gallery-images').css('margin-top',$(document).scrollTop());
        $(".document-mask").show();
    });
    
    $("#document-gallery-close").on("click",function(){
        $(".document-gallery-images").removeClass("document-gallery-blocl");
        if( $('.document-history-images').css('display') == "block" ){
                    return false;
                }
        $(".document-mask").hide();
    });
 
     
	 mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
			var iPAGE = 1,
			 is_finish = true,// 当前数据是否加载完成
			 page_bol = true,// 总页数控制
			 searchData="",
			 search_bol=false;
			
			$scope.dateFormat=$("#dateFormat").val();
			
			// 初始化数据
			$scope.dataList = '';
			// 数字调用
			$scope.counter = Array;
			// 监听视图渲染是否结束
	    	$scope.checkLast = function($last){
				if($last){
					
					setTimeout(function(){
						$(".photo-list ul").each(function(){
							$(this).append($($(this).parent().attr("imgText")));
							$(this).parent().attr("imgText","");
							$(this).find(".class-delimg").hide();
						});
						 textereaHeight();
						initUpload();
			    	},100);	
					
				}
			}
			// 固定每次拿10条数据
			var rows = 10;
			// 正常拿数据
	    	function getDataList(){
	    		oLoading.show();
	    		$(".communication-contents-list").show();
	    		is_finish=false;
	    		var url = base_root + "/front/ifa/info/communicationRecordJson.do?datestr="+new Date().getTime(),
	    			 data =  "&rows="+ rows +"&page=" + iPAGE +"&checkIsMyFollow=1"+searchData;
	    			$http({
	                  url: url,
	                  method: 'POST',
	                  data : data,
	                  headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
	             	 })
	    			.success(function(response){
	    				
	    				oLoading.hide();
	                      if(response.list.length > 0){
	                    	 
	                    	  $(".wmes-nodata-tips").hide();
	                    	// 总页数
	                  		var sumPage = Math.ceil(response.total / rows);
		                  	  if(iPAGE > sumPage){
		                  		  return;
		                  	  }
	                		 if( iPAGE === 1 ){
	                		 	 $scope.dataList = '';
	                           $scope.dataList =  response.list;
	                        }else{
	                           $scope.dataList = $scope.dataList.concat(response.list);
	                        }
                      		if(iPAGE >= sumPage){
                      			page_bol = false;
                      		}
	                		}else{
	               			 if( iPAGE === 1 ){
	               				 $scope.dataList = '';
	               				 $(".wmes-nodata-tips").show();
	                			 }
	                		}
			    			iPAGE++;
			    			$scope.datatotal = response.total;
			    			is_finish=true;
			    			
			    			//绑定点击事件
			    			setTimeout(function(){
			    				var contentobj = $('.photo-list');
			    				//下载图片
				    			contentobj.find('.class-download').click(function(){
				    				var filepath = ($(this).attr('filepath'));
				    				var a = $('<a></a>').attr('target','_self').attr('href', base_root+'/download.do?fileName='+filepath).appendTo('body');
				    			    a[0].click();
				    			    a.remove();
				    			});
				    			//点击浏览图片
				    			contentobj.find('.img-box').click(function(){
				    				var imgindex = $(this).children('img').attr('imgindex');
					    			var html = '<div class="swiper-container gallery-top">';
					    			html += '<div class="swiper-wrapper">';
					    			var img_list = $(this).parents('.photo-list').find('ul');
					    			var thumbs_html='';
					    			thumbs_html += '<div class="swiper-container gallery-thumbs">';
					    			thumbs_html += '<div class="swiper-wrapper">';
					    			$(img_list.html()).find('.img-box').each(function(i){
					    				var big_src = $(this).find('img').attr('bigsrc');
					    				var thumb_src = $(this).find('img').attr('src');
					    				var li_html = '<div class="swiper-slide">';
					    				li_html += '<img data-src="'+big_src+'" class="swiper-lazy">';
					    				li_html += '<div class="swiper-lazy-preloader swiper-lazy-preloader-white"></div>';
					    				li_html += '</div>';
					    				html += li_html;
					    				thumbs_html += '<div class="swiper-slide" style="background-image:url('+thumb_src+');background-repeat:no-repeat;width:112px;height:100px;"></div>';
					    			});
					    			html += '</div>';
					    			html += '<div class="swiper-pagination swiper-pagination-white"></div>';
					    			html += '<div class="swiper-button-next swiper-button-white"></div>';
					    			html += '<div class="swiper-button-prev swiper-button-white"></div>';
					    			html += '</div>';
					    			//缩 略图
					    			thumbs_html += '</div>';
					    			thumbs_html += '</div>';
					    			html += thumbs_html;
					    			layer.open({
					    			  type: 1,
					    			  title:'',
					    			  shade: 1,
					    			  shadeClose:true, 
					    			  skin: '', //加上边框
					    			  area: ['90%', '83%'], //宽高
					    			  content: html,
					    			  success:function(){ 
					    				  var galleryTop = new Swiper('.gallery-top', {
					    				        nextButton: '.swiper-button-next',
					    				        prevButton: '.swiper-button-prev',
					    				        pagination: '.swiper-pagination',
					    				        paginationClickable: true,
					    				        // Disable preloading of all images
					    				        preloadImages: false,
					    				        initialSlide :imgindex=='0'?0:imgindex,//计算元素时可能会出现延迟，导致//可能会出现延迟imgindex没有值
					    				        // Enable lazy loading
					    				        lazyLoading: true
					    				    });
					    				  var galleryThumbs = new Swiper('.gallery-thumbs', {
					    				        spaceBetween: 10,
					    				        centeredSlides: true,
					    				        slidesPerView: 'auto',
					    				        touchRatio: 0.2,
					    				        initialSlide :imgindex=='0'?0:imgindex,//可能会出现延迟,导致//可能会出现延迟imgindex没有值
					    				        slideToClickedSlide: true
					    				    });
					    				  
					    				  galleryTop.params.control = galleryThumbs;
					    				  galleryThumbs.params.control = galleryTop;
					    			        
					    				  $('.layui-layer-close2').css({'z-index':'999'});
					    				  $('.gallery-thumbs').css('background','#2b2c30');
					    			  }
					    			});
				    			});
				    			
				    			
				    			
			    			},1000);
			    			
			    			
	                });
	    	}
	    	Initialization();
	    	
	    	// 滚动条件
	    	var scrollBol = false;

	    	// 滚动拿数据
	    	setTimeout(function(){
	    		$(window).on('scroll', GetScroll);
	    	},1000);	

	    	function GetScroll(){
	    		var docheight = $(window).scrollTop() + $(window).height(),
	    			listheight = $('.communication-contents-list ').offset().top + $('.communication-contents-list ').height() + $(".wmes_top").height();
	    		
	    		if( docheight > listheight ){
	    			scrollBol = true;
	    			
	    		}else{
	    			scrollBol = false;
	    		}

				if (scrollBol && is_finish && page_bol ){
					getDataList();
				}

	    	}
	
	    	function searchList(){
	    		searchData="";
	    	 var period=$(".communication-choice").find(".fund_choice_active").attr("data-value");
	    	 if(period=="other"){
	    		 period=$("#recommend-date-to").val()+"to"+$("#recommend-date-from").val();
	    	 }
	    	 if(period!=undefined && period!=""){
	    		 searchData+="&period="+period;
	    	 }
	    	 
	    	 
	    	 var clientName=$("#searchKey").val();
//	    	 //console.log(clientName);
	    	 if(clientName!=undefined && clientName!=""){
	    		 searchData+="&clientName="+clientName;
	    	 }
	    	  var keyWord=$("#fundName").val();
	    	  if(keyWord!=undefined && keyWord!=""){
		    		 searchData+="&keyWord="+keyWord;
		    	 }
	    	}
	    	
	    	$(".recommend-date-button").on("click",function(){
	    		 search_bol=true;
	    		 Initialization();
	    	});
	    	
	    	function Initialization(){
	    		$(".communication-contents-list").find("li").remove();
	    		// 初始化数值
	    		iPAGE = 1; // 第N页数据
	    		page_bol=true;
	    		searchList();
	    		//searchData = $("#paramsForm").serialize();
	    		getDataList();
	    	}
	    	$("#searchKeyBtn,#searchClientBtn").on("click",function(){
	    		//alert($("#searchKey").val());
	    		
	    		Initialization();
	    	});
	    	$("#fundName").keyup(function(event){
	      	 	if(event.keyCode == 13){
	        		document.getElementById('searchKeyBtn').click()
	        	}
	    	});	
    	  $(".funds_choice li").on("click",function(){
    		 /* $(".communication-contents-list li").each(function(){
    			  if($(this).attr("itemsid")==undefined || $(this).attr("itemsid")=="" ){
    				  $(this).remove();
    			  }
    		  })*/
    	  		
				if($(this).parent().hasClass("funds_logo_b")){
					return;
				}
				$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
				if($('.fund_choice_active').hasClass('funds_all')){
					$(this).addClass('fund_choice_active2');
				}else{
					$('.funds_choice li').removeClass('fund_choice_active2');
				};
				if($(this).hasClass("recommend-date-choice") && $(this).hasClass("fund_choice_active")){
					$(this).parents(".funds_choice").find(".recommend-other-wrap").addClass("recommend-other-block");
					return;
				}else{
					
					$(this).parents(".funds_choice").find(".recommend-other-wrap").removeClass("recommend-other-block");
					Initialization();
				}
//				var selection_criterialenght = $(".selection_criteria li").length;
				$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
	
			});
	
    	  function initUpload(){
    		 /* $(".communication-contents-list .upload-album").InitUploader({ 
					btntext: "", multiple: true, water: true, thumbnail: true, filesize: "8000",modulerelate:"corner" 
						});*/
    		  $(".communication-contents-list .upload-album").find(".webuploader-container").remove();
    		 
    		  $(".communication-contents-list .upload-album").InitUploader({ uploadtype:'image',bgimagetype:1,btntext: "", 
    			  multiple: true, water: true, thumbnail: true, filesize: "8000",modulerelate:"memo"  });

    	  }


    	  $(".communication-contents-list").on("click",".class-delimg",function(){
    		  
    		  $(this).parent().parent().remove();
    	  })
	    

	 	$(".mylist-new-bn").on("click",function(){
	 		$(".wmes-nodata-tips").hide();
	    	var StrName  = '';

	    	var memberId=$("#memberId").val();
	    	var nickName=$("#nickName").val();
	    	if(memberId==undefined)
	    		memberId="";
	    	if(nickName==undefined)
	    		nickName="";
	    	//date time
			var myDate   = new Date(),
				StrYear  = myDate.getFullYear();
				//StrYear  = StrYear.toString().substr(2);
			var	StrDate  = StrYear + "-" + (myDate.getMonth() + 1) + "-" + myDate.getDate(),
				StrTime  = myDate.getHours() + ":" + myDate.getMinutes()+":"+myDate.getSeconds();
			StrDate=formatDate(StrDate,dateFormat);
	    	
	    	var StrHtml  = '<li class="communication-contents-rows communication-contents-active" itemsId="">';
	    		StrHtml += '<div class="communication-rows-left">';
	    		StrHtml += '<p class="communication-rows-date">' + StrDate + '</p>';
				StrHtml += '<p class="communication-rows-time">' + StrTime + '</p>	';
				StrHtml += '<div class="communication-name-wrap">';
				StrHtml += '<img class="communication-name-img" " src="'+ base_root +'/res/images/client/the_villain.png">';
				if(memberId!="" && nickName!=""){
					StrHtml+='<p class="communication-name" memberid="'+memberId+'">'+nickName+'</p>'
                }else{
                	StrHtml += '<p class="communication-name">'+ StrName +'</p>';
                }
	    		StrHtml+='</div></div>';
	    		StrHtml += '<div class="communication-rows-right">';
	    		StrHtml += '<textarea class="communication-textarea" placeholder="'+langMutilForJs['global.input.tips']+'"></textarea>';
	    		StrHtml += '<span class="communication-rows-edit"></span><span class="communication-rows-del"></span>';
	    		StrHtml += '<div class="communication-row-img"></div>';
	    		StrHtml += '<div class="photo-list"><ul></ul></div>';
	    		StrHtml += ' <div class="upload-box upload-album"></div>';

	    	$(".communication-contents-list").prepend(StrHtml);
	    	initUpload();
	    	textereaHeight();
	    });
    	  
    	  var curElement=null;
    	  $(".communication-contents-list").on("click",".communication-name-img",function(){
    		//  alert("success");
    		  curElement=$(this).parent();
    		 /* $(".order-plan-sapce").toggleClass("order-plan-block");
    		  swiper.onResize()*/
    		  selector.create(1,'client','memberId','nickName',selectCallBack);
    		  $(".selectnamelistbox").show();
    	  });
    	  
    	  function selectCallBack(){
    		  $(curElement).find(".communication-name").attr("memberid",$("#memberId").val());
    		  $(curElement).find(".communication-name").text($("#nickName").val());
    		  $(curElement).find(".addCrm").remove();
    	  }
    	  function searchSelect(){
    		  $("#searchKey").val($("#nickName").val());
    		  Initialization();
    	  }
    	 
    	  $("#searchKey").on("click",function(){
    		   selector.create(1,'client','memberId','nickName',searchSelect);
    		  $(".selectnamelistbox").show();
    		  
    		 
    	  })
    	  

	    $(document).on("click",".communication-rows-edit",function(){

	    	var _selfParents = $(this).parents(".communication-contents-rows");
	    	

	    	if( _selfParents.hasClass("communication-contents-active") ){
	    		saveMemo(this,_selfParents);
	    		
	    	}else{
	    		_selfParents.find(".communication-textarea").removeAttr("disabled");
	    		_selfParents.find(".upload-album").show();
	    		_selfParents.find(".class-delimg").show();
	    		_selfParents.toggleClass("communication-contents-active");
	    	}
			
	    });
		
		function textereaHeight(){
			$('textarea').each(function () {
	  			this.setAttribute('style', 'height:' + (this.scrollHeight) + 'px;overflow-y:hidden;');
			}).on('input', function () {
	  		this.style.height = 'auto';	  
	  		this.style.height = (this.scrollHeight) + 'px';
			});
		};
    	

		$(document).on("click",".communication-rows-del",function(){
			var obj=this;
			layer.confirm(langMutilForJs["global.confirm.delete"],
					{
				     btn: [langMutilForJs["global.true"],langMutilForJs["global.false"]],
			         title:''
			        	 },function(){
				var id=$(obj).parents(".communication-contents-rows").attr("itemsId");
				
				if(id==undefined || id==""){
					$(obj).parents(".communication-contents-rows").remove();
					delFinnish();
					layer.closeAll('dialog');
					return;
				}
				$.ajax({
					type:"post",
					datatype:"json",
					url:base_root+"/front/ifa/info/deleteMemo.do",
					data:{id:id},
					success:function(json){
						if(json.result){
							layer.msg(langMutilForJs['global.success.delete'],{
								  time: 1000 //2秒关闭（如果不配置，默认是3秒）
								},function(){
								$(obj).parents(".communication-contents-rows").remove();
								delFinnish();
								layer.closeAll('dialog');
							});
							
						}else{
							layer.msg(langMutilForJs['global.failed.delete']);
						}
					}
				});
			})
			
			/*setTimeout(function(){
				
			},1200)*/
		});
		
		function delFinnish(){
			if($('.communication-contents-list li').length==0){
				$(".wmes-nodata-tips").show();
			}else{
				$(".wmes-nodata-tips").hide();
			}
		}
		
		
		function saveMemo(obj,_selfParents){
			var memberId=$(obj).parent().parent().find(".communication-name").attr("memberId");
			if(memberId==undefined || memberId==""){
				layer.msg(langMutilForJs["account.list.client.placeholder"]);
				return;
			}
			var memo=$(obj).parent().find(".communication-textarea").val();
			var id=$(obj).parents(".communication-contents-rows").attr("itemsId");
			var imgEl=$(obj).parent().find(".photo-list ul li .upload-eachimg-cla");
			
			var imgList="";
			$.each(imgEl,function(){
				var val=$(this).val().split("|");
				imgList+=val[val.length-1]+",";//$(this).attr("src").replace(base_root,"")+",";
			})
			//var imgHmtl=$(obj).parent().find(".photo-list ul").html();
			if(memo==undefined || memo==""){
				layer.msg(langMutilForJs['global.input.remind']);
				return;
			}
			
			$.ajax({
				type:"post",
				datatype:"json",
				url:base_root+"/front/ifa/info/saveMemo.do",
				data:{id:id,content:memo,imglist:imgList,memberId:memberId}, //间隔符号 → &rarr;
				success:function(json){
					if(json.result){
						$(obj).parents(".communication-contents-rows").attr("itemsId",json.id);
						layer.msg(langMutilForJs["global.success.save"]);
						_selfParents.find(".communication-textarea").attr("disabled","");
			    		_selfParents.find(".upload-album").hide();
			    		_selfParents.find(".class-delimg").hide();
			    		_selfParents.toggleClass("communication-contents-active");
					}else{
						layer.msg(langMutilForJs["global.failed.save"]);
					}
				}
			})
			
			
		}	
		
		
		 $(".order-plan-sapce").on("click",".order-space-rows",function(){
		    	var memberId=$(this).attr("memberid");
		    	var name=$(this).find(".order-space-name").html();
		    	$(curElement).parent().find(".communication-name").text(name);
		    	$(curElement).parent().find(".communication-name").attr("memberId",memberId);
		    	$(curElement).parent().find(".addCrm").remove();
		    	$(".order-plan-sapce").removeClass("order-plan-block");
		    })
			 $(".order-sapce-wrapper").on("mouseenter",function(){
		        $(this).find(".swiper-button-next, .swiper-button-prev").show();
		    });
		    $(".order-sapce-wrapper").on("mouseleave",function(){
		        $(this).find(".swiper-button-next, .swiper-button-prev").hide();
		    });
		    
		    
		    $(document).on("click",".communication-name",function(){
		    	if($(this).parents(".communication-contents-rows").attr("itemsId")==undefined || $(this).parents(".communication-contents-rows").attr("itemsId")==""){
		    		  curElement=$(this).parent();
		    		  selector.create(1,'client','memberId','nickName',selectCallBack);
		    		  $(".selectnamelistbox").show();
		    		/*  $(".order-plan-sapce").toggleClass("order-plan-block");
		    		  swiper.onResize()*/
		    	}
		    });
		    
	    }]);// angular.js end
	
	 

});