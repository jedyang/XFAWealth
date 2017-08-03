define(function(require) {
	'use strict';
	var $ = require('jquery'),
	WebUploader = require('webuploader');
	require('layer');
	require("jqueryForm");
	require('laydate');
	require("umeditorConfig");//配置文件
	require("ueditor");//配置文件
	
    var ue = UM.getEditor('container',{
    	toolbar: ['source | undo redo | bold italic underline strikethrough | superscript subscript | forecolor backcolor | removeformat |',
    	            'insertorderedlist insertunorderedlist | emotion  selectall cleardoc paragraph |fontfamily fontsize' ,
    	            '| justifyleft justifycenter justifyright justifyjustify |',
    	            'link unlink | image ',
    	            '| horizontal']
		,lang:lang
		,imageUrl:base_root + "/upload.do?r="+Math.random()+"&moduleId=4"            //图片上传提交地址
		,imagePath:base_root +'/loadImgSrcByPath.do?filePath='
		,imageFieldName:"upfile"
		,imageMaxSize:"200"
    });
    ue.ready(function(){
        $("#edui1").width("100%");
        $("#edui1_iframeholder").width("100%");
        var insightContent = $("#preContent").html().trim();
        ue.setContent(insightContent);
    });
    
    /**
     * 上传控件实例化
     * @author scshi
     * */
    var initUploader = function(){
    	 // 缩略图片容器
    	var $queue = $(".picker").parents("div").find(".filelist"),
    	percentages = {};
    	
        var uploader = WebUploader.create({
        	//自动上传
           auto: true,
           // swf文件路径
           swf:  base_root + '/res/js/Uploader.swf',
           //文件接收服务端。
           //server: base_root + "/front/investor/uploadDoc.do?insightId="+insightid+"&docType=insight_thumb",
           server:base_root +"/upload.do?r="+Math.random()+"&moduleId=2",
           // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
           disableGlobalDnd: true,
           //选择文件的按钮。可选。
           pick: $(".picker"),
           fileNumLimit: 1,
           fileSingleSizeLimit:1024*1024*2,
           // 设定只接受图片文件上传
            accept: {
                title: 'Images',
                extensions: 'jpg,gif,png,jpeg',
                mimeTypes: 'image/*'
            },
            compress:{
                width: 360,
                height: 360,
                // 图片质量，只有type为`image/jpeg`的时候才有效。
                quality: 90,
                // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
                allowMagnify: false,
                // 是否允许裁剪。
                crop: false,
                // 是否保留头部meta信息。
                preserveHeaders: true,
                // 如果发现压缩后文件大小比原来还大，则使用原来图片
                // 此属性可能会影响图片自动纠正功能	
                noCompressIfLarger: false,
                // 单位字节，如果图片大小小于此值，不会采用压缩。
                compressSize: 0
            } 
        });   
         
        uploader.on('uploadSuccess', function(file,ret) {
        	var str= []; 
        	str = ret.filePaths.split(":");
        	//var fileName = str[0];
        	var filepathTemp = str[1];
        	if(file["_info"]!=null){
        		var imgW = file["_info"].width;
            	var imgH = file["_info"].height;
            	if(imgH>imgW){
            		//删除文件
            		$.post(  base_root + "/front/investor/deleteDoc.do" , {"filepath": filepathTemp }, function(ret){
            			//清空文件序列
                    	uploader.removeFile( file );
                    	//提示
                    	layer.msg('请上传横幅图片！');
                	} );
            		return false;
            	}
        	}
        	$("#insight_thumb").val(filepathTemp);
            addFile(file,filepathTemp);
        });
        
        uploader.on( 'uploadError', function( file,ret ) {
        	layer.msg( "上传出错，请检查上传材料是否异常。");
        });
        
        // 当有文件添加进来时执行，负责view的创建
        function addFile( file,filePath ) {
        	////console.log(file);
            var $li = $( '<li id="img_li_' + file.id + '" fileId="'+file.id+'" class="img_li" >' +
                    '<p class="imgWrap"></p>'+
                    '<p class="progress"><span></span></p>' +
                    '</li>' ),
                $btns = $('<div class="file-panel div_btn">' +
                    '<span class="cancel">删除</span>' +
                    '&nbsp;<span class="thumb">预览</span></div>').appendTo( $li ),
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
                    img = $('<img src="'+base_root +'/loadImgSrcByPath.do?filePath='+filePath+'" width="145px" height="85px">');
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
                    ////console.log( file.statusText );
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
                //var imgSrc = $(this).parents("li").find("img").attr("src");
                switch ( index ) {
                    case 0://删除附件
                    	//$.post(  base_root + "/front/investor/deleteDoc.do" , {"accessoryFileId": file.id }, function(ret){
                        $.post(  base_root + "/deleteByPath.do" , {"filePath": filePath }, function(ret){
                    		if(ret.result){
                    			var $li = $('#img_li_'+fileId);
                            	$li.off().find('.file-panel').off().end().remove();
                            	uploader.removeFile( file );
                            	$("#insight_thumb").val('');//清空缩略图路径
                            	$(".filelist li").eq(0).show();
                    		}else{
                    			layer.msg(ret.msg);
                    		}
                    	} );
                        return;
                    case 1://预览
                    	if($('.imgWrap').find('img').attr('status') == 1){
                    		$('.imgWrap').find('img').animate({'width':'145px','height':'85xp'});
                    		$('.imgWrap').find('img').attr('status',0);
                    	}else{
                    		$('.imgWrap').find('img').animate({'width':'100%','height':'100%'});
                    		$('.imgWrap').find('img').attr('status',1);
                    	}
                        break;
                }
            });
            
            $li.appendTo( $queue );
            $(".filelist li").eq(0).hide();
        }//文件view创建成功
    };
    //initUploader();
    
    $(".insight_thumb").on("change",function(){
    	var thumbCheck = $("input[name='thumbType']:checked").val();
    	//缩略图方式切换
    	if("1"==thumbCheck){
    		$(".filelist").slideToggle('fast');
    		$("#insight_thumb").val('');//清空缩略图路径
    	}else{
    		$(".filelist li").eq(0).siblings().remove();
    		$(".filelist li").eq(0).show();
    		$(".filelist").slideToggle('fast');
    		initUploader();
    	}
    });
    
    $("#insight-sector-add").on("click",function(){
    	$('#dialog-mySector').addClass('dispaly-active');
    	$('#dialog-mySector').show();
    });
    
    $("#insight-allocation-add").on("click",function(){
    	$('#dialog-myAllocation').addClass('dispaly-active');
    	$('#dialog-myAllocation').show();
    });
    
    //保存 mySector
    $('.mySector-button-save').click(function(){
    	var sectorTmpStr = '';
    	$.each($('.selected-li-mySector >li'),function(i,n){
			if($(n).attr('id')!=$('.selected-li-mySector >li').last().attr('id')){
				$(this).remove();
			}
		});
    	$('.ul-mySector >li').each(function(i,d){
    		$('.selected-li-mySector >li').eq(0).before('<li class="insight-relate-choose" title="'+$(this).attr('title')+'" style="margin-bottom: 5px;">'+$(this).text()+'</li>');
			var sectorCode = $(this).attr("data-id");
			/*if(typeof(sectorCode)=='undefined'){
				sectorCode = '{'+$(this).data('value')+'}';
			}*/
			//隐藏域值
			sectorTmpStr +=sectorCode+",";
		});
		sectorTmpStr = sectorTmpStr.substring(0,sectorTmpStr.length-1);//去除最后一个逗号；
		$("#insight_sector").val(sectorTmpStr);//隐藏域赋值，用户后台数据保存
		$('#dialog-mySector').removeClass('dispaly-active');
		$('#dialog-mySector').hide();
    });
    
    //保存 myAllocation
    $('.myAllocation-button-save').click(function(){
    	var sectorTmpStr = '';
    	$.each($('.selected-li-myAllocation >li'),function(i,n){
			if($(n).attr('id')!=$('.selected-li-myAllocation >li').last().attr('id')){
				$(this).remove();
			}
		});
    	$('.ul-myAllocation >li').each(function(i,d){
    		$('.selected-li-myAllocation >li').eq(0).before('<li class="insight-relate-choose" title="'+$(this).attr('title')+'" style="margin-bottom: 5px;">'+$(this).text()+'</li>');
    		var sectorCode = $(this).attr("data-id");
    		/*if(typeof(sectorCode)=='undefined'){
    			sectorCode = '{'+$(this).data('value')+'}';
    		}*/
    		//隐藏域值
    		sectorTmpStr +=sectorCode+",";
    	});
    	sectorTmpStr = sectorTmpStr.substring(0,sectorTmpStr.length-1);//去除最后一个逗号；
    	$("#insight_location").val(sectorTmpStr);//隐藏域赋值，用户后台数据保存
    	$('#dialog-myAllocation').removeClass('dispaly-active');
    	$('#dialog-myAllocation').hide();
    });
    
    /**
     * 观点保存
     * @author scshi
     * @date 20160823
     * */
    $("#btn_save").on("click",function(){
    	if(checkData()){
    		$('#hidStatus').val(1);
        	var data = saveInsight();
        	$("#insightContent").val(ue.getContent());//观点内容赋值 encodeURI(ue.getContent())
        	$("#insightForm").ajaxSubmit({
        		url:base_root+"/front/ifa/myInsight/saveInsight.do",
        		data:data,
    			success:function(data){
    				if(data.result){
    					layer.msg(globalProp['global.success.save']);
    					var url = base_root + '/front/ifa/myInsight/list.do?d='+new Date().getTime();
    					setTimeout(function(){window.location.href=url;},1000);
    				}else{
    					layer.msg(globalProp['global.failed.save']);
    				}
    			},
    			error:function(){
    				layer.msg(globalProp['global.failed.save']);
    			}
        	});
    	}
    });
    //保存至草稿
    $("#btn_draft").on("click",function(){
    	if(checkData()){
    		$('#hidStatus').val(0);
        	var data = saveInsight();
        	var content = ue.getContent();
    		if($('#thumbType1').prop('checked') && $(content).find('img').length > 0){
    			var src = $(content).find('img:eq(0)').attr('src');
    			var thumbnail = src.substring(src.indexOf('filePath=')+9,src.length);
    			$('#insight_thumb').val(thumbnail);
    		}
        	$("#insightContent").val(encodeURI(content));//观点内容赋值
        	$("#insightForm").ajaxSubmit({
        		url:base_root+"/front/ifa/myInsight/saveInsight.do",
        		data:data,
    			success:function(data){
    				if(data.result){
    					layer.msg(globalProp['global.success.save']);
    				}else{
    					layer.msg(globalProp['global.failed.save']);
    				}
    			},
    			error:function(){
    				layer.msg(globalProp['global.failed.save']);
    			}
        	});
    	}
    });
    //校验数据
    function checkData(){
    	var title = $('.insight-input-title').val().replace(/(^\s+)|(\s+$)/g,'');
    	if(title==''){
    		layer.msg('The title can not be blank!');
    		return false;
    	}
    	var date = $('#insight-date').val().replace(/(^\s+)|(\s+$)/g,'');
    	if(date==''){
    		layer.msg('The insight date can not be blank!');
    		return false;
    	}
    	var mySectorCount = $('.selected-li-mySector >li').length;
    	if(mySectorCount <= 1){
    		layer.msg('Please select Relate Sector!');
    		return false;
    	}
    	var myAllocationCount = $('.selected-li-myAllocation >li').length;
    	if(myAllocationCount <= 1){
    		layer.msg('Please select Relate Geographic Allocation!');
    		return false;
    	}
    	var content = ue.getContent().replace(/(^\s+)|(\s+$)/g,'');
    	if(content==''){
    		layer.msg('The content can not be blank!');
    		return false;
    	}
    	return true;
    }
    //保存数据
    function saveInsight(){
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
    			if(typeof $('#existingIds').val() != 'undefined' && $('#existingIds').val()!=''){
    				clientFlag = '-1';
    				clientsDetail = $('#existingIds').val();
    			}
    		}
    		if($('#permit_prospect').prop('checked')){
    			prospectFlag = '1';
    			if(typeof $('#prospectIds').val() != 'undefined' && $('#prospectIds').val()!=''){
    				prospectFlag = '-1';
    				prospectsDetail = $('#prospectIds').val();
    			}
    		}
    		if($('#permit_buddy').prop('checked')){
    			buddyFlag = '1';
    			if(typeof $('#buddyIds').val() != 'undefined' && $('#buddyIds').val()!=''){
    				buddyFlag = '-1';
    				buddiesDetail = $('#buddyIds').val();
    			}
    		}
    		if($('#permit_colleague').prop('checked')){
    			colleagueFlag = '1';
    			if(typeof $('#colleagueIds').val() != 'undefined' && $('#colleagueIds').val()!=''){
    				colleagueFlag = '-1';
    				colleaguesDetail = $('#colleagueIds').val();
    			}
    		}
    	}
    	var data = {
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
    	return data;
    }
    
    /**
     * 页面关闭
     * @author scshi_u330p
     * @date 20161031
     * */
    $("#btn_cancle").on("click",function(){
    	//window.close();
    	var url = base_root + '/front/ifa/myInsight/list.do?d='+new Date().getTime();
		window.location.href=url;
    });
    
    //
    $(".character-choose-list").on("click","li",function(){
    	$(this).parents(".character-setting-rows").find(".character-setting-list").append($(this).append('<span class="character-list-close"></span>'));
    });
    $(".character-setting-list").on("click",".character-list-close",function(){
    	$(this).parents(".character-setting-rows").find(".character-choose-list").append($(this).parent());
    });
    $(document).on("click",".wmes-close, .character-button-close",function(){
    	//$(".selectSector").addClass("funds-views-lump");
    	$('#dialog-mySector').removeClass('dispaly-active');
    	$('#dialog-myAllocation').removeClass('dispaly-active');
    	$('#dialog-mySector').hide();
    	$('#dialog-myAllocation').hide();
    });
    $(".character-setting-add").on("blur",function(){
    	if($(this).val().replace(/(^\s*)|(\s*$)/g, "") !=''){
    		var StrHtml = '<li data-value='+ $(this).val() +'>'+ $(this).val() +' <span class="character-list-close"></span></li>'
        	$(this).parents(".character-setting-rows").find(".character-setting-list").append(StrHtml);
        	$(this).val("");
    	}
    });
    //
	$('.insight-choose-pic input').on('click',function(){
		if($('#picadd').prop('checked')){
			$('.insight-choose-picupdate').css('display','block');
		}else{
			$('.insight-choose-picupdate').css('display','none');
		}
	});
	$('.ifa_keyserach_wrap input').change(function(e) {
		if($(this).parents('.view_permissions_setting').find(".letmespecify").prop("checked") == true) {
			$(this).parents('.view_permissions_setting').next().css('display', 'block');
		} else {
			$(this).parents('.view_permissions_setting').next().css('display', 'none');
		}
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
	$('#permissionsAdvanceSetting').click(function(){
		var url=base_root+"/front/strategy/info/userSelector.do";
		openResWin(url,1200,600,"permitASetting");
	});
	$('#remindAdvanceSetting').click(function(){
		var url=base_root+"/front/strategy/info/userSelector.do";
		openResWin(url,1200,600,"pushASetting");
	});
    
	$('#insight-date').click(function(){insightDate('','');});
	function insightDate(min,max){
		laydate({
			   istime: true,
			   min:min,
			   max:max,
			   elem: '#insight-date',
			   format: 'YYYY-MM-DD hh:mm:ss',
			   istoday: false,
			   choose: function(datas){ 
		       } 
		});
	}
	
	var selector = require('ifaSelectUser');
	selector.init();
	$(".j-permitASetting").on("click",function(){
    	var type=$(this).attr("type");
    	//selector.init();
    	selector.create(0,type,type+"Ids",type+"Names",function(){
    		selectorCalback(type+"Names");
    	});
    	$(".selectnamelistbox").css('height',$(document).height());
    	$(".selectnamelistcon").css('top',$(window).height()*0.20+$(document).scrollTop());
		$(".selectnamelistbox").show();
	});
	$("#permit0,#permit1").on("click",function(){
		$("#permit_ext").hide();
	});
	$("#permit2").on("click",function(){
    	$("#permit_ext").toggle();
    });
	$("#permit_ext .setting").on("click",function(){
        $(this).parents("li").toggleClass("setting_active");
    });
	function selectorCalback(type){ 
		if(typeof type == 'undefined' || !type){
			return;
		}
		var memberNames = $('#'+type).val();
		if(typeof memberNames == 'undefined' || !memberNames){
			$('#'+type).closest('li').find('p').remove();
			$('#'+type).closest('li').find('a').before('<p class="pushToSettingName select-all" style="margin-right:30px">All</p>');
			return;
		}
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
		$('#'+type).closest('li').find('p').remove();
		$('#'+type).closest('li').find('a').before(html);
	}
});	