/**
 * openAccountSubmit.js basicinfo
 * @author michael
 */
define(function(require) {
	var $ = require('jquery'),
	WebUploader = require('webuploader');
	require('layer');

	//投资人提交开户申请到IFA
	$("#submitToIFA").on("click",function(){
		if (document.getElementById("msg")){
			if (!($("#msg").val())){
				layer.alert(langMutilForJs['open.account.approval.submit.inputComment']);
				return;
			} 
		}
		var msg = $("#msg").val();
        $.ajax({
            url:base_root+"/front/investor/submitWorkFlow.do",
            data:{"accountId":accountId,"msg":msg,"r":Math.random()},
            dataType:"json",
            type:"post",
            success:function(data){
                if(data.result){
//                    alert(data.msg);
                    layer.msg(data.msg,{time:2000});
//                    window.location.href = base_root+"/front/investor/applicationList.do";
                    window.location.href = base_root+"/front/investor/myAccountList.do";
                }else{
//                    alert(data.msg);
                    layer.alert(data.msg);
                }
            },
            error:function(){
//                alert("Save fail.");
                layer.alert(langMutilForJs['global.failed.save']);
            }
        })
        //window.location.href = base_root + "/front/investor"
    });

	//IFA退回或发送给投资人确认
	$("#sendToInvestor").on("click",function(){
		approve("0");
    });

	//IFA提交上级审批
	$("#submitToOfficer").on("click",function(){
		approve("1");
    });
	
	function approve(checkStatus){
		if (checkStatus=="1"){
			if (document.getElementById("msg")){
				if (!($("#msg").val())){
					layer.alert(langMutilForJs['open.account.approval.submit.inputComment']);
					return;
				} 
			}
		} 
		var msg = $("#msg").val();
		//alert(checkStatus);
        $.ajax({
            url:base_root+"/front/investor/approveWorkFlow.do",
            data:{"accountId":accountId,"checkStatus":checkStatus,"msg":msg,"r":Math.random()},
            dataType:"json",
            type:"post",
            success:function(data){
                if(data.result){
//                    alert(data.msg);
                    layer.msg(langMutilForJs['global.success.save']);
                    setTimeout(function(){
                        //window.location.href = base_root+"/front/investor/applicationList.do";
                        window.location.href = base_root+"/front/ifa/space/clientManagement.do";
                    },2000);
                }else{
//                    alert(data.msg);
                    layer.alert(data.msg);
                }
            },
            error:function(){
//                alert("Save fail.");
                layer.msg(langMutilForJs['global.failed.save'],{time:2000});
            }
        })
	}
	
//上传控件初始化 开始 ----------------------------------------------------------
	
	$(".div_btn").on( 'click', 'span', function() {
	    var index = $(this).index();
	    var fileId = $(this).parent().parent().attr("fileId");
	    var imgSrc = $(this).parents("li").find("img").attr("src");
	    
	    switch ( index ) {
	        case 0://删除附件
	        	$.post(  base_root + "/front/investor/deleteDoc.do" , {"accessoryFileId": fileId }, function(ret){
	        		var $li = $('#img_li_'+fileId);
	            	$li.off().find('.file-panel').off().end().remove();
	        	} );
	            return;
	        case 1://预览
	        	$("#img_zone").attr("src",imgSrc);
	            $(".doc_imgWrap").show();
//	        	var d = dialog({
//	        		title:'图片查看', 
//	        		content:'<img width="817" src="'+imgSrc+'" />', 
//	        		fixed:true,
//	    		});
//	        	d.show();
	            break;
	    }
	});
	$(".doc_close_ico").on("click",function(){
	    $(".doc_imgWrap").hide();
	});
	$(".img_li").on( 'mouseenter', function() {
		$(this).children("div").stop().animate({height: 30});;
	});
	$(".img_li").on( 'mouseleave', function() {
		$(this).children("div").stop().animate({height: 0});
	});
	
	// 缩略图片容器
	var $queue = $(".picker").parents("div").find(".filelist"),
    	        
 	// 所有文件的进度信息，key为file id
	percentages = {};
    	        
	// 判断浏览器是否支持图片的base64
    // isSupportBase64 = ( function() {
    //     var data = new Image();
    //     var support = true;
    //     data.onload = data.onerror = function() {
    //         if( this.width != 1 || this.height != 1 ) {
    //             support = false;
    //         }
    //     }
    //     data.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
    //     return support;
    // } )();

    var uploader = WebUploader.create({
    	//自动上传
       auto: true,
       // swf文件路径
       swf:  base_root + '/res/js/Uploader.swf',
       //文件接收服务端。
       server: base_root + "/front/investor/uploadDoc.do?r="+Math.random(),
       // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
       disableGlobalDnd: true,
       //选择文件的按钮。可选。
       pick: $(".picker"),
       fileNumLimit: 10,
       formData: { 
          "investDocId":accountId,
          "docType":"submit_doc",
          "moduleId":387
          
       },
       // 设定只接受图片文件上传
        accept: {
            title: 'Images',
            extensions: 'jpg,gif,png,jpeg',
            mimeTypes: 'image/*'
        },
       resize: false
    });
    
    uploader.on( 'uploadSuccess', function( file,ret) {
    	var fileId = ret.accessoryFile.id;
    	var filePath = base_root+"/loadImgSrcByPath.do?filePath="+ret.accessoryFile.filePath;
        addFile(file,fileId,filePath);
    });

    uploader.on( 'uploadError', function( file,ret ) {
//        alert( "上传出错，请检查上传材料是否异常。" );
        layer.alert("上传出错，请检查上传材料是否异常。");
    });
    	        
	// 当有文件添加进来时执行，负责view的创建
    function addFile( file,fileId,filePath ) {

        var $li = $( '<li id="img_li_' + fileId + '" fileId="'+fileId+'" class="img_li">' +
                //'<p class="title">' + file.name + '</p>' +
                '<p class="imgWrap"></p>'+
                '<p class="progress"><span></span></p>' +
                '</li>' ),
            $btns = $('<div class="file-panel div_btn">' +
                '<span class="cancel">删除</span>' +
                '&nbsp;<span class="thumb">预览</span>').appendTo( $li ),
            $prgress = $li.find('p.progress span'),
            $wrap = $li.find( 'p.imgWrap' ),
            $info = $('<p class="error"></p>'),
            text,
            showError = function( code ) {
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

                $info.text( text ).appendTo( $li );
            };

        if ( file.getStatus() === 'invalid' ) {
            showError( file.statusText );
        } else {
            // @todo lazyload
            $wrap.text( '预览中' );
            uploader.makeThumb( file, function( error, src ) {
                var img;

                if ( error ) {
                    $wrap.text( '不能预览' );
                    return;
                }

                img = $('<img src="'+filePath+'" height="85px">');
                $wrap.empty().append( img );
            });

            percentages[ file.id ] = [ file.size, 0 ];
            file.rotation = 0;
        }

        file.on('statuschange', function( cur, prev ) {
            if ( prev === 'progress' ) {
                $prgress.hide().width(0);
            } else if ( prev === 'queued' ) {
                $li.off( 'mouseenter mouseleave' );
                $btns.remove();
            }
            // 成功
            if ( cur === 'error' || cur === 'invalid' ) {
                showError( file.statusText );
                percentages[ file.id ][ 1 ] = 1;
            } else if ( cur === 'interrupt' ) {
                showError( 'interrupt' );
            } else if ( cur === 'queued' ) {
                $info.remove();
                $prgress.css('display', 'block');
                percentages[ file.id ][ 1 ] = 0;
            } else if ( cur === 'progress' ) {
                $info.remove();
                $prgress.css('display', 'block');
            } else if ( cur === 'complete' ) {
                $prgress.hide().width(0);
                $li.append( '<span class="success"></span>' );
            }

            $li.removeClass( 'state-' + prev ).addClass( 'state-' + cur );
        });

        $li.on( 'mouseenter', function() {
            $btns.stop().animate({height: 30});
        });

        $li.on( 'mouseleave', function() {
            $btns.stop().animate({height: 0});
        });

        $btns.on( 'click', 'span', function() {
            var index = $(this).index();
            var fileId = $(this).parent().parent().attr("fileId");
            var imgSrc = $(this).parents("li").find("img").attr("src");
            switch ( index ) {
                case 0://删除附件
                	$.post(  base_root + "/front/investor/deleteDoc.do" , {"accessoryFileId": fileId }, function(ret){
                		var $li = $('#img_li_'+fileId);
                    	$li.off().find('.file-panel').off().end().remove();
                	} );
                    return;
                case 1://预览
                	$("#img_zone").attr("src",imgSrc);
                    $(".doc_imgWrap").show();
//    	                    	var d = dialog({
//    	                    		title:'图片查看', 
//    	                    		content:'<img max-width="817" src="'+imgSrc+'" />', 
//    	                    		});
//    	                    	d.show();
                    break;
            }
        });
        
        $li.appendTo( $queue );
        // $queue.find("li").eq(0).after($li) ;
        
    }
//上传控件初始化 结束 ----------------------------------------------------------
      
  	$(function($) {
  	  document.onkeydown=function(ev)
  	  {
  		  var oEvent=ev||event;//获取事件对象(IE和其他浏览器不一样，这里要处理一下浏览器的兼容性event是IE；ev是chrome等)
  		  //Esc键的keyCode是27
  		  if(oEvent.keyCode==27)
  		  {
  			  $(".doc_imgWrap").hide();
  		  }
  	  }
  	  if (submitBy=="IFA"){
  		var url = base_root+"/front/investor/approvalListInfo.do?accountId="+accountId;
  		$('#approvalList').load(url, null,
  		 	function(text, status, xmlhttp){
//  		 	  initInnerHeight('mainFrame','mainFrame');
  			}
  		);
  	  }
  	})
  	
	$(".printForm").click(function() {
		//弹出即全屏
		//layer.full(index);
		layer.msg("Under construction");
	});
  	
	$(".viewPdf").click(function() {
		//弹出即全屏
		/*
		var index = layer.open({
			skin : 'layui-layer-lan',
			title : '查看pdf',
			type : 2,
			content : '/wmes/front/investor/dialogshowpdf.do',
			area : [ '320px', '195px' ],
			maxmin : true
		});
		layer.full(index);
		 */
		var url=base_root+"/d/sample.pdf";
		window.open(url,"_blank");
	});
	
	$(".expPdf").click(function() {
		//弹出即全屏
		//layer.full(index);
		//layer.msg("Under construction");
		var url=base_root+"/d/sample.pdf";
		//$.download(url,"fileName=test.pdf");
		window.open(url,"_blank");
	});
	
	$.download = function (url, data, method) {
		// 获取url和data
		if (url && data) {
		    // data 是 string 或者 array/object
		    data = typeof data == 'string' ? data : $.param(data);
		    // 把参数组装成 form的  input
		    var inputs = '';
		    $.each(data.split('&'), function () {
				var pair = this.split('=');
				inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
		    });
		    // request发送请求
		    $('<form action="' + url + '" method="' + (method || 'post') + '">' + inputs + '</form>')
		    	.appendTo('body').submit().remove();
		};
	};
});	