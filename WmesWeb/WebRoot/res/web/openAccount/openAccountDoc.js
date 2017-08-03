/**
 * openAccountDoc.js basicinfo
 * @author tejay zhu
 */
define(function(require) {
	var $ = require('jquery'),
	WebUploader = require('webuploader');
	require('layer');
	
 	//investor点击下一步，保存数据，进入文档检查
    $("#btn_draft").on("click",function(){
    	saveAccount(false);
    });

 	//investor点击下一步，保存数据，进入文档检查
    $("#btn_next").on("click",function(){
    	saveAccount(true);
    });
				    
    //investor点击下一步，保存数据
    function saveAccount(next){
		$.ajax({
			url:base_root+"/front/investor/accountDocSave.do",
			data:{"accountId":accountId,"r":Math.random()},
			dataType:"json",
			type:"post",
			success:function(data){
				if(data.result){
					//保存成功，检查是否上传完，发出提示
					var all = true;
					$(".doc_tr").each(function(){
						var isNecessary = $(this).attr("isNecessary");
						var fileLength = $(this).find(".img_li");
						////console.log("lengt"+fileLength);
						if('1'==isNecessary && fileLength.length<1){
							all = false;
						}
					})
					if (!all){
						layer.alert("有必填附件未上传！");
//						return ;
					}
					
					if (next) window.location.href = base_root+"/front/investor/accountDeclare.do?accountId="+accountId;
				}else{
//					alert(data.msg);
					layer.alert(data.msg);
				}
			},
			error:function(){
//				$.Tips({ content: "Save fail."});
				layer.alert(langMutilForJs['global.failed.save']);
			}
		})
	}

	//取消
	$("#btn_cancle").on("click",function(){
		window.location.href=base_root + "/index.do";
	});
	
	//tab切换
	$(".OpenAccount_tab li").on("click",function(){
	    $(".OpenAccount_tab li a").removeClass("now").eq($(this).index()).addClass("now");
	    $(".OpenAccount_rpa_table > table").hide().eq($(this).index()).show();
	    accountDocController();
	});

	$(".img_li").on( 'mouseenter', function() {
		$(this).children("div").stop().animate({height: 30});;
	});

	$(".img_li").on( 'mouseleave', function() {
		$(this).children("div").stop().animate({height: 0});
	});

	$(".doc_close_ico").on("click",function(){
	    $(".doc_imgWrap").hide();
	});

	$(".div_btn").on( 'click', 'span', function() {
	    var index = $(this).index();
	    var fileId = $(this).parent().parent().attr("fileId");
	    var imgSrc = $(this).parents("li").find("img").attr("src");
	    
	    switch ( index ) {
	        case 0://删除附件
	        	$.post(  base_root + "/front/investor/deleteDoc.do" , {"accessoryFileId": fileId }, function(ret){
	        		var $li = $('#img_li_'+fileId);
	            	$li.off().find('.file-panel').off().end().remove();
	                accountDocController();
	        	} );
	            return;
	        case 1://预览
	        	$("#img_zone").attr("src",imgSrc);
	            $(".doc_imgWrap").show();
	            break;
	    }
	});


	function getClientDate(){
	  var myDate = new Date();
	  var d = myDate.getFullYear() + "-" + (myDate.getMonth()+1) + "-" + myDate.getDate();
	  return d;
	}

	/****************************20160801begin**************************/

	function accountDocController(){

	  $(".picker").each(function(){
	        var row = $(this).parents("tr");
	// 缩略图片容器
	        var $queue = $(this).parents("tr").find(".filelist"),
	        
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
	           pick: $(this),
	           fileNumLimit: 10,
	           formData: { 
	              "investDocId":row.attr("investDocId"),
	              "docType":"investor_doc",
	              "moduleId":5
	           },
	           // 设定只接受图片文件上传
	            accept: {
	                title: 'Images',
	                extensions: 'jpg,bmp,png,jpeg,',
	                mimeTypes: 'image/*'
	            },
	           resize: false
	       });
	        
	        uploader.on( 'uploadSuccess', function( file,ret) {
	        	
	        	var fileId = ret.accessoryFile.id;
	        	//var filePath = base_root+ret.accessoryFile.filePath;
	        	var filePath = base_root+"/loadImgSrcByPath.do?filePath="+ret.accessoryFile.filePath;
	            addFile(file,fileId,filePath);
	            accountDocController();
	        });

	        uploader.on( 'uploadError', function( file,ret ) {
	            //alert( "上传出错，请检查上传材料是否异常。" );
	        });
	        
	     // 当有文件添加进来时执行，负责view的创建
	        function addFile( file,fileId,filePath ) {
	            var $li = $( '<li id="img_li_' + fileId + '" fileId="'+fileId+'" class="img_li" >' +
	                    //'<p class="title">' + file.name + '</p>' +
	                    '<p class="imgWrap"></p>'+
	                    '<p class="progress"><span></span></p>' +
	                    '</li>' ),
	                $btns = $('<div class="file-panel div_btn">' +
	                    '<span class="cancel">删除</span>' +
	                    '<span class="thumb">预览</span>').appendTo( $li ),
	                $prgress = $li.find('p.progress span'),
	                $wrap = $li.find( 'p.imgWrap' ),
	                $info = $('<p class="error"></p>');
	                //var text;
	                var showError = function( code ) {
	                	var text='';
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
	                            accountDocController();
	                    	} );
	                        return;
	                    case 1://预览
	                    	$("#img_zone").attr("src",imgSrc);
	                        $(".doc_imgWrap").show();
	                        break;
	                }
	            });
	            
	            $li.appendTo( $queue );
	            
	        }
	        
	  });
	}
	/**********************************20160801end**********************/

	$(function($) {
	  accountDocController();
	  document.onkeydown=function(ev)
	  {
		  var oEvent=ev||event;//获取事件对象(IE和其他浏览器不一样，这里要处理一下浏览器的兼容性event是IE；ev是chrome等)
		  //Esc键的keyCode是27
		  if(oEvent.keyCode==27)
		  {
			  $(".doc_imgWrap").hide();
		  }
	  }
	})	
});

