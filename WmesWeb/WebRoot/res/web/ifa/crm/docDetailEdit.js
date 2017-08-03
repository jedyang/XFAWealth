define(function(require) {
	var $ = require('jquery');
	require("layui");
			require("jqueryForm");
	var WebUploader = require('webuploader');
	
	//图片操作按钮
	// $(".img_li").on( 'mouseenter', function() {
	// 	$(this).children("div").stop().animate({height: 30});;
	// });

	// $(".img_li").on( 'mouseleave', function() {
	// 	$(this).children("div").stop().animate({height: 0});
	// });

	$(".div_btn").on( 'click', 'span', function() {
		var index = $(this).index();
	    switch ( index ) {
	        case 0://删除附件dom,提交 
	        	var delId = $(this).attr("delId");
	        	var delFileIds = $("#deleteFileArray").val();
	        	delFileIds += delId+",";
	        	$("#deleteFileArray").val(delFileIds);
	        	$(this).parents("li").remove();
	            return;
	        case 1://预览
	        	var imgSrc = $(this).next().attr("src");
	        	//$(".doc_imgWrap").show();
	        	parent.parent.layer.open({
	        		  type: 2,
	        		  title: false,
	        		  area: ['630px', '360px'],
	        		  shade: 0.8,
	        		  closeBtn: 0,
	        		  shadeClose: true,
	        		  content: imgSrc
	        		});
	            break;
	    }
	});	
	
	$("#docSubmit").on('click',function(){
		var addFileArray = $("#addFileArray").val();
		$(".img_li").each(function(){
			var isNew = $(this).attr("isNew");
			if('y'==isNew){
				var fpt = $(this).attr("fpt");
				var fnm = $(this).attr("fnm");
				addFileArray+=fnm+":"+fpt+",";
			}
		});
		$("#addFileArray").val(addFileArray);
		$("#imgUpdateForm").ajaxSubmit({
			url:base_root + "/front/kycDoc/docDetailEditSave.do?r="+Math.random(),
			success:function(data){
				if(data.result){
					//var docId = $("#docUpdate_id").val();
					//docDetailGrid(docId);
					//$("#view-filelist-update").removeClass("view-filelist-show");
					//return;
					parent.window.location.reload();
					parent.layer.close(index);
				}
			},
			error:function(){
				alert("error");
			}
		});
	});

	function accountDocController(){
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
		    		var $fileArea = $('<li class="img_li view-update-rows" fileid="'+file.id+'" id="img_li_'+file.id+'" isNew="y" fpt="'+filePathTemp+'" fnm="'+fileNameTemp+'">'+
										'<p class="imgWrap"><img src="'+imgSrc+'" height="260px" class="view-update-images"/></p>'+
										'<p class="progress"><span></span></p>'+
									'</li>'),		
					  $btns = $('<div class="file-panel div_btn">' +
			                    '<span class="cancel">删除</span>' +
			                    '<span class="thumb" attSrc="'+base_root+filePathTemp+'">预览</span>').appendTo( $fileArea ),									
		    			$prgress = $fileArea.find('.progress'),
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
//		    	        else if( cur ==='progress' ){    
//		    	            $info.remove();
//		    	            $prgress.css('display', 'block');
//		    	        } 
		    	        else if ( cur === 'complete' ) {
		    	        	setTimeout(function () { 
		    	        		$prgress.hide().width(0);
		    	            }, 500);
		    	        }
		    	    });
		    	    
		    	    $fileArea.on( 'mouseenter', function() {
		                $btns.stop().animate({height: 30});
		            });

		    	    $fileArea.on( 'mouseleave', function() {
		                $btns.stop().animate({height: 0});
		            });

		            $btns.on( 'click', 'span', function() {
		                var index = $(this).index();
		                switch ( index ) {
		                    case 0://删除附件dom
		                    	$.post(base_root+"/deleteByPath.do?r="+Math.random(),{"filePath": filePathTemp },function classBack(data){
		                    		$("#img_li_"+file.id).remove();
		                    	});
		                        return;
		                    case 1://预览
		                    	var imgSrc = $(this).attr("attSrc");
		                    	$("#img_zone").attr("src",imgSrc);
		        	            $(".doc_imgWrap").show();
		                        break;
		                }
		            });
		    	    $fileArea.appendTo( $queue );
		    	}
		    	
		  });//对页面每个picker绑定完成
	}
	
//实例化webuploader	
	$(function($) {
		  accountDocController();
	});
});	