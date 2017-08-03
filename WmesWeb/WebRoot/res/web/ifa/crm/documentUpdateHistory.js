define(function(require) {
	var $ = require('jquery');
			require("swiper");
			require("layui");
			require("jqueryForm");
	var WebUploader = require('webuploader');

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
    
    $(".document-images-img").on("click",function(){
        var swiper_html = "",
            _this = $(this);
            var _parent = _this.parents(".document-images-list").find(".document-images-img");
            _parent.each(function(){
            swiper_html += '<div class="swiper-slide" style="background-image:url('+ $(this).attr("data-src") +')"></div>'
        });
        $(".document-gall-total").html(_parent.length);
        $(".document-gall-serial").html(_this.parent().index()+1);
        $(".gallery-top").remove();
        $(".gallery-thumbs").remove();
        $(".document-gallery-contents").prepend('<div class="swiper-container gallery-top"><div class="swiper-wrapper" id="galler-big-img">'+swiper_html+'</div></div>');

        $(".document-gall-name-wrap").before('<div class="swiper-container gallery-thumbs"><div class="swiper-wrapper" id="galler-small-img">'+swiper_html+'</div></div>');


        swiperInit(_this.parent().index());

        $(".document-gallery-images").addClass("document-gallery-blocl");
        $(".document-mask").show();
    });
    
    //点击图片遮罩层时，调用点击图片的方法
    $(".filelist-images-title").on("click",function(){
    	$(this).prev().click();
    })
    
    $("#document-gallery-close").on("click",function(){
        $(".document-gallery-images").removeClass("document-gallery-blocl");
        if( $('.document-history-images').css('display') == "block" ){
                    return false;
                }
        $(".document-mask").hide();
    });

    $("#document-j-update").on("click",function(){
        $(".document-deposit-images").show();
        $(".document-mask").show();
        
        //移除上传未提交图片
        $(".img").remove();
        //显示删除未提交的图片,清空删除图片id序列，清空上传文件序列
        $(".document-deposit-rows").removeClass("beforeImglogicDel").addClass("maybeUpload");
        $(".j-deposit-btn").removeClass("maybeUpload");
        $("#deleteFileArray").val(null);
        $("#addFileArray").val(null);
        //上传控件实例化
        uploadController();
    });
	
    $("#document-deposit-close").on("click",function(){
        $(".document-deposit-images").hide();
        $(".document-mask").hide();
    });

    $(".document-deposit-cancel").on("click",function(){
        $(".document-deposit-images").hide();
        $(".document-mask").hide();
    });

//    $(".document-deposit-determine").on("click",function(){
//        $(".document-deposit-images").hide();
//        $(".document-mask").hide();
//    });
    
    $("#document-j-history").on("click",function(){
        $(".document-history-images").show();
        $(".document-mask").show();
    });

    $("#document-history-close").on("click",function(){
        $(".document-history-images").hide();
        $(".document-mask").hide();
    });
    
    $(".history-images-img-wrap").on("click",function(){

        var _str = $(this).attr("data-src").split(","),
            _length = _str.length,
            _html = "";

            for (var i = 0 ; i < _length ; i++){
                _html += '<div class="swiper-slide" style="background-image:url('+ _str[i] +')"></div>'
            }

        $(".document-gall-total").html(_length);
        $(".document-gall-serial").html(1);
        $(".gallery-top").remove();
        $(".gallery-thumbs").remove();
        $(".document-gallery-contents").prepend('<div class="swiper-container gallery-top"><div class="swiper-wrapper" id="galler-big-img">'+_html+'</div></div>');

        $(".document-gall-name-wrap").before('<div class="swiper-container gallery-thumbs"><div class="swiper-wrapper" id="galler-small-img">'+_html+'</div></div>');


        swiperInit(0);

        $(".document-gallery-images").addClass("document-gallery-blocl");

    });
    
    
    //文档提交
    $("#docSubmit").on('click',function(){
		var addFileArray = $("#addFileArray").val();
		$(".img").each(function(){
			var isNew = $(this).attr("isNew");
			if('y'==isNew){
				var fpt = $(this).attr("fpt");
				var fnm = $(this).attr("fnm");
				addFileArray+=fnm+":"+fpt+",";
			}
		});
		
		//无上传文件，不允许提交
		if(!addFileArray && 0==$(".maybeUpload").length){
			layer.msg(langMutilForJs["ifa.crm.kyc.chooseToUpload"],{icon:7,time:2000});
			return false;
		}
		
		$("#addFileArray").val(addFileArray);
		$("#imgUpdateForm").ajaxSubmit({
			url:base_root + "/front/kycDoc/docDetailEditSave.do?r="+Math.random(),
			success:function(data){
				if(data.result){
					$(".document-deposit-images").hide();
			        $(".document-mask").hide();
					window.location.reload();
				}else{
					layer.msg(langMutilForJs["global.failed.save"],{icon:5,time:1000});
				}
			},
			error:function(){
				layer.msg(langMutilForJs["global.failed.save"],{icon:5,time:1000});
			}
		});
		
	});
 
  //文档退回
    $('#reject-show').on('click',function(){
    	layer.prompt({
    		title:langMutilForJs["ifa.crm.kyc.DocumentApproval"],
    		scrollbar: false,
    		tipsMore: true,
    		area: '516px',
    		content:'<textarea id="reject_result" class="rejectArea"></textarea>',
    		btn:[langMutilForJs["ifa.crm.kyc.StatusRejected"],langMutilForJs["global.cancel"]],
    		yes:function(val, index){
    			var checkResult = $('#reject_result').val();
    			if(!checkResult){
    				layer.tips(langMutilForJs["ifa.crm.kyc.fillCheckResult"], '.rejectArea', {
					  tipsMore: true
					});
    				return;
    			}
    			$("#check_result").val(checkResult)
        		$("#check_status").val("2");
        		$("#checkForm").ajaxSubmit({
        			url:base_root+ "/front/kycDoc/kycDocApprove.do?r="+Math.random()+"&moduleId=5 ",
        			success:function(data){
        				if(data.result)
        					window.location.reload(); 
        			},
        			error:function(){
        				layer.msg(langMutilForJs["global.confirm.error"],{icon:5,time:1000});
        			}
        		});
        		layer.close(index);
    		}
    	});
    });
    
  //文档审批    
    $("#approved-show").on("click",function(){
    	layer.confirm(langMutilForJs["global.confirm.approve"], 
			{title:langMutilForJs["global.prompt"], 
			icon: 3,
			btn: [langMutilForJs["global.confirm"],langMutilForJs["global.cancel"]]},
			function (index){
		    	$("#check_status").val("1");
		    	$("#checkForm").ajaxSubmit({
					url:base_root+ "/front/kycDoc/kycDocApprove.do?r="+Math.random()+"&moduleId=5 ",
					success:function(data){
						window.location.reload(); 
					},
					error:function(){
						layer.msg(langMutilForJs["global.confirm.error"],{icon:5,time:1000});
					}
				});
		    	layer.close(index);
			});
    });
        
//上传控件实例化
    function uploadController(){
		  $(".picker").each(function(){
		// 缩略图片容器
		        var $queue = $(this).parents("div").find(".fileList"),
		        
		// 所有文件的进度信息，key为file id
		        percentages = {};

		        var uploader = WebUploader.create({
		        	//自动上传
		           auto: true,
		           // swf文件路径
		           swf:  base_root + '/res/js/Uploader.swf',
		           //文件接收服务端。
		           //server: base_root + "/front/investor/uploadDoc.do?investDocId="+row.attr("investDocId")+"&docType=investor_doc",
		           server:base_root +"/upload.do?r="+Math.random()+"&moduleId=5",
		           // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
		           disableGlobalDnd: true,
		           //选择文件的按钮。可选。
		           pick: $(this),
		           fileNumLimit: 10,
		           // 设定只接受图片文件上传
		            accept: {
		                title: 'Images',
		                extensions: 'jpg,gif,png,jpeg',
		                mimeTypes: 'image/*'
		            },
		           resize: false
		       });	
		        
//上传成功以后		        
		        uploader.on( 'uploadSuccess', function( file,ret) {
			        var retArry = [];
	        		retArry = ret.filePaths.split(":");
	        		var fileNameTemp = retArry[0];
	        		var filePathTemp = retArry[1];
		            addFile(file,fileNameTemp,filePathTemp);
		        });
//		    	
//错误信息提示
		    	uploader.on('error', function( code ) {
		    		var errorText = '';
		    		if('F_EXCEED_SIZE'==code){
		    			errorText +='文件大小超出限制';
		    		}
		    		if('Q_TYPE_DENIED'==code){
		    			errorText += '文件格式错误';
		    		}
		    		if('Q_EXCEED_NUM_LIMIT'==code){
		    			errorText += '上传文件数量超出限制。'
		    		}
		    		if(''!=errorText){
		    			alert(errorText)
		    		}
		    	});
		    	
//上传进度显示处理
//		    	uploader.onUploadProgress = function( file, percentage ) {
//		    	    var $li = $('#'+file.id),
//		    	    $percent = $li.find('.progress').children();
//		    	    $percent.eq(0).text( Math.round( percentage * 100 ) + '%' );
//		    	    $percent.eq( 1 ).css( 'width', Math.round( percentage * 100 ) + '%' );
//		    	    percentages[ file.id ][ 1 ] = percentage;
//		    	};
		    	
//文件移出序列以后调用
		        function addFile(file,fileNameTemp,filePathTemp){
		        	var imgSrc = base_root+"/loadImgSrcByPath.do?filePath="+filePathTemp;
		        	var $fileArea = $('<li class="document-deposit-rows img" fileid="'+file.id+'" isNew="y" fpt="'+filePathTemp+'" fnm="'+fileNameTemp+'" id="img_'+file.id+'">'+
		        						'<div class="document-deposit-img-wrap">'+
		        						'<img class="document-deposit-img" src="'+imgSrc+'">'+
		        						'<div class="ducument-deposit-del"><img src="'+base_root+'/res/images/client/deposit_del.png"></div>'+
		        						'</div>'+
		        						'<p class="document-deposit-name">'+fileNameTemp+'</p>'+
		        						'</li>'),
		    			$prgress = $fileArea.find('.progress'),
		    			$btn = $fileArea.find('.ducument-deposit-del'),
		    			$info = $('<p class="error"></p>'),
		    		 showError = function( code ) {
		    			var text ='';
		    	        switch( code ) {
		    	            case 'exceed_size':
		    	                text = '文件大小超出';
		    	                break;

		    	            case 'interrupt':
		    	                text = '上传暂停';
		    	                break;

		    	            default:
		    	                text = '上传失败，请重试';
		    	                break;
		    	        }
		    	        $info.text( text ).appendTo( $fileArea );
		    	    };
		    		
		    		if ( file.getStatus() === 'invalid' ) {
		    	    	showError( file.statusText );
		    	    }else{
		    	    	percentages[ file.id ] = [ file.size, 0 ];
		                file.rotation = 0;
		    	    }
		    	    
		    	    file.on('statuschange', function( cur, prev ) {
		    	        if ( prev === 'progress' ) {
		    	            //$prgress.hide().width(0);
		    	        } 
		    	        // 成功
		    	        if ( cur === 'error' || cur === 'invalid' ) {
		    	            showError( file.statusText );
		    	        } 
		    	        else if ( cur === 'interrupt' ) {
		    	            showError( 'interrupt' );
		    	        } 
		    	        else if ( cur === 'queued' || cur ==='progress' ) {
		    	            $info.remove();
		    	            $prgress.css('display', 'block');
		    	        } 
		    	        else if ( cur === 'complete' ) {
		    	        	setTimeout(function () { 
		    	        		$prgress.hide().width(0);
		    	            }, 500);
		    	        }
		    	    });
		    	    //上传文档删除
		    	    $btn.on("click","img",function(){
		    	    	$.post(base_root+"/deleteByPath.do?r="+Math.random(),{"filePath": filePathTemp },function classBack(data){
		    	    		if(data.result){
		    	    			$("#img_"+file.id).remove();
		    	    		}else{
		    	    			layer.msg(data.msg,{icon:5,time:1000});
		    	    		}
                    	});
		    	    })
		    	    $fileArea.appendTo( $queue );
		    	}
		    	
		  });//对页面每个picker绑定完成
	}
    
    /**
     * 原文件逻辑删除
     * @author scshi_u330p
     * @date 20170102
     * */
    $(".ducument-deposit-del").on("click","img",function(){
    	var selectImg = $(this);
    	layer.confirm(langMutilForJs["global.confirm.delete"], 
		{title:langMutilForJs["global.prompt"], 
		icon: 3,
		btn: [langMutilForJs["global.confirm"],langMutilForJs["global.cancel"]]},
		function (index){
			var delId = selectImg.attr("imgId");
        	var delFileIds = $("#deleteFileArray").val();
        	delFileIds += delId+",";
        	$("#deleteFileArray").val(delFileIds);
			selectImg.parents("li").removeClass("maybeUpload").addClass("beforeImglogicDel");
			layer.close(index);
		});
    });
    
    /**
     * 返回上一页点击事件
     * @author scshi_u330p
     * @date 20170413
     * */
    $(".his_go_pre").on("click",function(){
    	var pre = document.referrer;
    	if(pre.indexOf("&flag=12")<0){
    		pre +="&flag=12"
    	}
    	window.location.href=pre;
    })
});
