/**
 * 
 */
 define(function(require, exports, module) {
	//引进信赖文件
	var $ = require('jquery');
	var WebUploader = require('webuploader');
	require('layer');
	require('swiper');
	
	//按钮信息
	var dialoginfotitle = 'Info';
	var confirmbtnname = 'OK';
	if(lang=='sc')confirmbtnname='确定';
	if(lang=='tc')confirmbtnname='確定';
	if(lang=='sc')dialoginfotitle='提示';
	if(lang=='tc')dialoginfotitle='提示';
	//初始化绑定默认的属性
    $.upLoadDefaults = $.upLoadDefaults || {};
    $.upLoadDefaults.property = {
        multiple: false, //是否多文件
        water: false, //是否加水印
        thumbnail: false, //是否生成缩略图
        sendurl: null, //发送地址
        filetypes: "jpg,jpge,png,gif,jpeg,doc,docx,pdf,xls,xlsx", //文件类型 //,doc,docx,pdf,xls,xlsx
        filesize: "2048", //文件大小
        btntext: "select file...", //上传按钮的文字
        swf: null, //SWF上传控件相对地址
        count:null,//如果需要限制上传文件数量，则在这里配置，比如count:5，表示限制最多只能上传5个
        
        /*存放文件夹模块，请参考core/action/UploadAct程序，这里前端直接传portrait、portrait、docCheck、investor_doc、corner等即可。
            moduleRelate.put(1L,"m");//菜单模块	
			moduleRelate.put(20L,"portrait");//头像存放目录
			moduleRelate.put(2L, "insight");//观点
			moduleRelate.put(3L, "docCheck");//开户文档审批
			moduleRelate.put(4L, "umEditorUpload");//um_editor上传
			moduleRelate.put(5L, "investor_doc");//与开户文档审查
         * */
        modulerelate: null 
    };
    //初始化上传控件
    $.fn.InitUploader = function (b) { 
        var fun = function (parentObj) {//console.log(parentObj);
            var p = $.extend({}, $.upLoadDefaults.property, b || {});
            var newid = new Date().getTime(); 
            var btnObj = $('<div class="upload-btn-'+newid+'">' + ((checkBtnIsBgImage(p.bgimagetype)==true)?'':p.btntext) + '</div>').appendTo(parentObj);
            //初始化参数属性
            var sendurl = "?a=b";
            //是否设置水印
            if (p.water) {
                sendurl += "&IsWater=1";
            }
            //是否生成缩略图
            if (p.thumbnail) {
                sendurl += "&IsThumbnail=1";
            }
            //生成图片存放位置，根据模块来
            if (p.modulerelate) {
                sendurl += "&moduleRelate=" + p.modulerelate;
            }

            if (p.uploadtype) { 
                sendurl += "&uploadType=" + p.uploadtype;
            }
            //如果删除，删除路径
            if (!p.multiple) {
                sendurl += "&DelFilePath=" + parentObj.siblings(".upload-path").val();
            }
            //alert(base_root + "/wmesUpload.do" + sendurl);
            //初始化WebUploader
            var uploader = WebUploader.create({
                auto: true, //自动上传
                fileNumLimit:p.count,//上传文件总数
                swf: base_root + '/res/js/Uploader.swf', //SWF路径
                server: base_root + "/wmesUpload.do" + sendurl, //上传地址与参数
                pick: {
                    id: btnObj,
                    multiple: p.multiple
                },
                accept: {
                    /*title: 'Images',*/
                    extensions: p.filetypes
                    /*mimeTypes: 'image/*'*/
                },
                formData: {
                    'DelFilePath': '' //定义参数
                },
                //fileVal: 'Filedata', //上传域的名称
                fileSingleSizeLimit: p.filesize * 1024 //文件大小
            });

            //imagetype参数用来设置显示点击图片的尺寸，如果为0或不设置，则默认为按钮
            var uploadbtnObj = '.upload-btn-'+newid;
            if(p.bgimagetype==5){ 
            	$(uploadbtnObj).find('.webuploader-pick').addClass('webuploader-pick-imgtype-5');
            }
            else if(p.bgimagetype==4){ 
            	$(uploadbtnObj).find('.webuploader-pick').addClass('webuploader-pick-imgtype-4');
            }
            else if(p.bgimagetype==3){ 
            	$(uploadbtnObj).find('.webuploader-pick').addClass('webuploader-pick-imgtype-3');
            }
            else if(p.bgimagetype==2){
            	$('.webuploader-pick').addClass('webuploader-pick-imgtype-2');
            }
            else if(p.bgimagetype==1){ 
            	$(uploadbtnObj).find('.webuploader-pick').addClass('webuploader-pick-imgtype-1');
            }
            else{
            	$(uploadbtnObj).find('.webuploader-pick').addClass('webuploader-pick-imgtype-0');
            }
            
            //当validate不通过时，会以派送错误事件的形式通知
            uploader.on('error', function (type) {
                switch (type) {
                    case 'Q_EXCEED_NUM_LIMIT':
                    	if(lang=='en')layer.alert("Error：Upload files to marry!",{ title:dialoginfotitle, btn: [confirmbtnname]});
                    	else if(lang=='sc')layer.alert("错误：上传文件数量过多！",{ title:dialoginfotitle, btn: [confirmbtnname]});
                    	else if(lang=='tc')layer.alert("錯誤：上傳文件數量過多！",{ title:dialoginfotitle, btn: [confirmbtnname]});
                        break;
                    case 'Q_EXCEED_SIZE_LIMIT':
                    	if(lang=='en')layer.alert("Error：Total file size exceeds limit!",{ title:dialoginfotitle, btn: [confirmbtnname]});
                    	else if(lang=='sc')layer.alert("错误：文件总大小超出限制！",{ title:dialoginfotitle, btn: [confirmbtnname]});
                    	else if(lang=='tc')layer.alert("錯誤：文件總大小超出限制！",{ title:dialoginfotitle, btn: [confirmbtnname]});
                        break;
                    case 'F_EXCEED_SIZE':
                    	if(lang=='en')layer.alert("Error： File size exceeds limit!",{ title:dialoginfotitle, btn: [confirmbtnname]});
                    	else if(lang=='sc')layer.alert("错误：文件大小超出限制！",{ title:dialoginfotitle, btn: [confirmbtnname]});
                    	else if(lang=='tc')layer.alert("錯誤：文件大小超出限制！",{ title:dialoginfotitle, btn: [confirmbtnname]});
                        break;
                    case 'Q_TYPE_DENIED':
                    	if(lang=='en')layer.alert("Error： Forbidden to upload the file type!",{ title:dialoginfotitle, btn: [confirmbtnname]});
                    	else if(lang=='sc')layer.alert("错误：禁止上传该类型文件！",{ title:dialoginfotitle, btn: [confirmbtnname]});
                    	else if(lang=='tc')layer.alert("錯誤：禁止上傳該類型文件！",{ title:dialoginfotitle, btn: [confirmbtnname]});
                        break;
                    default:
	                    if(lang=='en')layer.alert("Error code：" + type,{ title:dialoginfotitle, btn: [confirmbtnname]});
	                	else if(lang=='sc')layer.alert("错误代码："+type,{ title:dialoginfotitle, btn: [confirmbtnname]});
	                	else if(lang=='tc')layer.alert("錯誤代码："+type,{ title:dialoginfotitle, btn: [confirmbtnname]});
                        break;
                }
            });

            //当有文件添加进来的时候
            uploader.on('fileQueued', function (file) { 
                //如果是单文件上传，把旧的文件地址传过去
                if (!p.multiple) {
                    uploader.options.formData.DelFilePath = parentObj.siblings(".upload-path").val();
                }
                //防止重复创建
                if (parentObj.children(".upload-progress").length == 0) {
                    //创建进度条
                    var fileProgressObj = $('<div class="upload-progress"></div>').appendTo(parentObj);
                    var progressText = $('<span class="txt">Loading...</span>').appendTo(fileProgressObj);
                    var progressBar = $('<span class="bar"><b></b></span>').appendTo(fileProgressObj);
                    var progressCancel = $('<a class="close" title="Cancel">Close</a>').appendTo(fileProgressObj);
                    //绑定点击事件
                    progressCancel.click(function () {
                        uploader.cancelFile(file);
                        fileProgressObj.remove();
                    });
                }
            });

            //文件上传过程中创建进度条实时显示
            uploader.on('uploadProgress', function (file, percentage) {
                var progressObj = parentObj.children(".upload-progress");
                progressObj.children(".txt").html(file.name);
                progressObj.find(".bar b").width(percentage * 100 + "%");
            });

            //当文件上传出错时触发
            uploader.on('uploadError', function (file, reason) {
                uploader.removeFile(file); //从队列中移除
                alert(file.name + "上传失败，错误代码：" + reason);
            });

            //当文件上传成功时触发
            uploader.on('uploadSuccess', function (file, data) {//console.log(file);console.log(data);console.log($(parentObj).html());
                if (data.status == '0') {
                    var progressObj = parentObj.children(".upload-progress");
                    progressObj.children(".txt").html(data.msg);
                } 
                if (data.status == '1') {
                    //如果是单文件上传，则赋值相应的表单
                	if (!p.multiple) {
                        parentObj.siblings(".upload-name").val(data.filename);
                        parentObj.siblings(".upload-path").val(data.filepath);
                        parentObj.siblings(".upload-size").val(data.size);
                        
                        addImage(parentObj, data,p.multiple,p);
                    } else { 
                        //addImage(parentObj, data.filepath, data.filepath_112x100);
                    	addImage(parentObj, data,p.multiple,p);
                    }
                    
                    var progressObj = parentObj.children(".upload-progress");
                    progressObj.children(".txt").html("上传成功：" + file.name);
                }
                uploader.removeFile(file); //从队列中移除
                if(p.successCallBack){
                	p.successCallBack(data.filepath,data.orifilename);//上传成功回调方法
                }
            });
            	
            //不管成功或者失败，文件上传完成时触发
            uploader.on('uploadComplete', function (file) {
                var progressObj = parentObj.children(".upload-progress");
                progressObj.children(".txt").html("upload completed!");
                //如果队列为空，则移除进度条
                    if (uploader.getStats().queueNum == 0) {
                        progressObj.remove();
                    }
                });
            };
            return $(this).each(function () {
                fun($(this));
            });
        }
    
    function checkBtnIsBgImage(type){
    	if(type==1||type==2||type==3||type==4||type==5)return true;
    	else return false;
    }
/*图片相册处理事件
=====================================================*/
//添加图片相册
function addImage(targetObj, data,isMultiple,p) { 
    //插入到相册UL里面
	var id = new Date().getTime();
	var elementId = '#'+id;
	var orifilename = data.orifilename;
	var newfilename = data.newfilename;
	var filepath = data.filepath;
	var suffix = data.suffix;
    var newLi = '';
    if(p.uploadtype=='image'){
    	//上传时判断已上传的图片数量
    	//$('.photo-list')
    	//console.log(targetObj.parent().find('.photo-list').find('ul>li').length);
    	var imgindex = targetObj.parent().find('.photo-list').find('ul>li').length;
    	 newLi = $('<li>'
    			    + '<input type="hidden" class="upload-eachimg-cla" name="hid_photo_name" value="'+suffix+'|'+orifilename+'|' + newfilename + '|' + filepath + '" />'
    			    + '<div class="img-box selected" onclick="javascript:void(0);">'
    			    + '<img imgindex="'+imgindex+'" id="img_'+id+'"width="112" height="100" class="upload-eachimg upload-eachimg-click"  src="'+base_root+'/loadImgSrcByPath.do?filePath='+data.filepath_112x100+'" bigsrc="'+base_root+'/loadImgSrcByPath.do?filePath='+data.filepath+'" />'
    			    + '</div>'
    			    + '<div class="delatebc">'
    			    + '<a id="delete_'+id+'" class="class-delimg a-href" href="javascript:;"></a>&nbsp;&nbsp;<a id="download_'+id+'" class="class-download a-href" filepath="'+data.filepath+'" href="javascript:void(0);"  ></a>'
    			    + '</div>'
    			    + '</li>');
    } else {
    	newLi = $('<li>'
			    + '<input type="hidden" class="upload-eachimg-cla" name="hid_photo_name" value="'+suffix+'|'+orifilename+'|' + newfilename + '|' + filepath + '" />'
			    + '<div class="img-box selected" onclick="javascript:void(0);">'
			    + '<p class="p-filename">'+orifilename+'</p>'
			    + '</div>'
			    + '<div class="delatebc">'
			    + '<a id="delete_'+id+'" class="class-delimg a-href" href="javascript:;"></a>&nbsp;&nbsp;<a id="download_'+id+'" class="class-download a-href" filepath="'+base_root+'/loadImgSrcByPath.do?filePath='+data.filepath+'" href="javascript:void(0);" target="_blank"></a>'
			    + '</div>'
			    + '</li>');
    }
   
    if(p.bgimagetype=='5')
    	targetObj.siblings(".photo-list").css({'padding': '60px 0 0 0'});
    else if(p.bgimagetype=='4')
    	targetObj.siblings(".photo-list").css({'padding': '55px 0 0 0'});
    else if(p.bgimagetype=='3')
    	targetObj.siblings(".photo-list").css({'padding': '40px 0 0 0'});
    else if(p.bgimagetype=='2')
    	targetObj.siblings(".photo-list").css({'padding': '30px 0 0 0'});
    else if(p.bgimagetype=='1')
    	targetObj.siblings(".photo-list").css({'padding': '20px 0 0 0'});
    //如果是单文件，则如果已存在图片，则替换它
    if(!isMultiple){
    	if(targetObj.siblings(".photo-list").children("ul").has("li").length > 0){ 
        	//获取该LI元素，并删掉
        	var firstLi = targetObj.siblings(".photo-list").children("ul").empty();
        }
    }
    newLi.appendTo(targetObj.siblings(".photo-list").children("ul"));
    //console.log(targetObj.siblings(".photo-list").children("ul").html())
    //监听图片点击事件
    var eachImgElement = '#img_'+id;
    $(eachImgElement).on('click',function(){
    	//每次点击时获取所有图片列表
    	var imgindex = $(this).attr('imgindex');
		var html = '<div class="swiper-container gallery-top">';
		html += '<div class="swiper-wrapper">';
		var img_list = $('.photo-list').find('ul');
		//console.log(img_list.html());
		var thumbs_html='';
		thumbs_html += '<div class="swiper-container gallery-thumbs">';
		thumbs_html += '<div class="swiper-wrapper">';
		$(img_list.html()).find('.img-box').each(function(i){
			var big_src = $(this).find('img').attr('bigsrc');
			var thumb_src = $(this).find('img').attr('src');
			//console.log(big_src);
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
		//console.log(html);
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
    //绑定删除监听事件
    var deleteElement = '#delete_'+id;
    $(deleteElement).on('click',function(){
    	var parentObj = $(this).parent().parent();
        var focusPhotoObj = parentObj.parent().siblings(".focus-photo");
        var smallImg = $(this).siblings(".img-box").children("img").attr("src");
        $(this).closest('li').remove(); 
    });
    //绑定下载图片监听事件
    var downloadElement = '#download_'+id;
	 $(downloadElement).on('click',function(){ 
	    	var filepath = $(this).attr('filepath');
		    var a = $('<a></a>').attr('target','_self').attr('href', base_root+'/download.do?fileName='+filepath).appendTo('body');
		    a[0].click();
		    a.remove();
	 });
}

function DownLoadReportIMG(imgPathURL) {  
    //如果隐藏IFRAME不存在，则添加  
    if (!document.getElementById("IframeReportImg"))  
        $('<iframe style="display:none;" id="IframeReportImg" name="IframeReportImg" onload="DoSaveAsIMG();" width="0" height="0" src="about:blank"></iframe>').appendTo("body");  
    if (document.all.IframeReportImg.src != imgPathURL) {  
        //加载图片  
        document.all.IframeReportImg.src = imgPathURL;  
    }  
    else {  
        //图片直接另存为  
        DoSaveAsIMG();  
    }  
}  
function DoSaveAsIMG() {  
    if (document.all.IframeReportImg.src != "about:blank")  
        window.frames["IframeReportImg"].document.execCommand("SaveAs");  
}  
//判断是否为ie浏览器  
function browserIsIe() {  
    if (!!window.ActiveXObject || "ActiveXObject" in window)  
        return true;  
    else  
        return false;  
}  

//删除图片LI节点
function delImg(obj) {
    var parentObj = $(obj).parent().parent();
    var focusPhotoObj = parentObj.parent().siblings(".focus-photo");
    var smallImg = $(obj).siblings(".img-box").children("img").attr("src");
    $(obj).parent().remove(); 
}
});