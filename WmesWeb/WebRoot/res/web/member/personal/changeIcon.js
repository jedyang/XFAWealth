seajs.use('layer',function(){
	var iconUrl = $("#changeIcon_form").find("input[name='iconUrl']").val();
	var gender = $("#changeIcon_form").find("input[name='gender']").val();
	if(iconUrl == ""){
		if(gender == "F"){
			iconUrl = "/res/images/hyzx_touxiang_08.png";
		}else{
			iconUrl = "/res/images/hyzx_touxiang_07.png";
		}
	}
	//控件参数
	var options ={
        thumbBox: '.thumbBox',
        spinner: '.spinner',
        imgScroll:false,//是否滚动鼠标滑轮放大放小
        imgSrc: _root_+iconUrl
    };
    var cropper = $('#imageBox').cropbox(options);
    $(document).on('change','#file',function(){
    	//alert("aaaaaaaaaaaaa");
    	var f=document.getElementById("file").value;
    	var fi=document.getElementById("file").files;
    	if(f=="")
    		return;
    	$(".edit_contentbox").show();
    	$(".edit_content").show();
		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(f)){
			//window.//console.log("图片类型必须是.gif,jpeg,jpg,png中的一种");
			layer.msg("图片类型必须是.gif,jpeg,jpg,png中的一种");
			$(".edit_contentbox").hide();
        	$(".edit_content").hide();
			return ;
		}else{
			/*var image = new Image();
		    image.src = f;
		    var height = image.height;
		    var width = image.width;
		    var filesize = image.filesize;
		    //console.log(height);
		    //console.log(width);
		    //console.log(filesize);
		    if(width>80 && height>80 && filesize>102400){
    		    alert('请上传80*80像素 或者大小小于100k的图片');
    		    return ;
		    }*/
			var size = fi[0].size;
			if(size>4194304){	//2M:2048*2048=4194304
				layer.msg('请上传少于2M的图片');
    			$(".edit_contentbox").hide();
            	$(".edit_content").hide();
				return ;
			}
		}
        var reader = new FileReader();
        reader.onload = function(e) {
            options.imgSrc = e.target.result;
            cropper = $('#imageBox').cropbox(options);
            //设置range
            var img = new Image();
            img.src = options.imgSrc;
            img.onload = function(){
            	var width = img.width;
            	var imgRange = $("#change_img_range");
            	imgRange.val(width);
            	//imgRange.val($("#imageBox").width());
            	imgRange.attr("max",width*1.5);
            	return; 
            };
        };
        reader.readAsDataURL(this.files[0]);
      //  $("#imageBox").css("background-size","100% 100%");
        this.files = [];
      //  alert('1');
    });
    $('#btnCrop').on('click', function(){
        var img = cropper.getDataURL();
        $('.cropped').append('<img src="'+img+'">');
    });
    //图片放大
    $('#btnZoomIn').on('click', function(){
    	var size = $("#imageBox").css("background-size");
    	var width=0;
    	if(size=="100%"){
    		width=$("#imageBox").width();
    	}else{
    		var arry = size.split(" ");
         	 width = arry[0].replace("px","");
    	}
    	
    	var imgRange = $("#change_img_range");
    	var max = imgRange.attr("max");
    	if(parseFloat(width)>=parseFloat(max)){
    		return;
    	}
		imgRange.val(width);
        cropper.zoomIn();
    });
    //图片缩小
    $('#btnZoomOut').on('click', function(){
    	var size = $("#imageBox").css("background-size");
    	var width=0;
    	if(size=="100%"){
    		width=$("#imageBox").width();
    	}else{
    		var arry = size.split(" ");
         	 width = arry[0].replace("px","");
    	}
    	var imgRange = $("#change_img_range");
    	var min = imgRange.attr("min");
    	if(parseFloat(width)<=parseFloat(min)){
    		return;
    	}
		imgRange.val(width);
        cropper.zoomOut();
    });
    //取消操作
	$("#btn_cancel").on("click",function(){
		$("#file").attr("accept","");
		$(".edit_content").hide();
		$(".edit_contentbox").hide();
	});
	//保存操作
	$("#btn_save").on("click",function(){
		saveIconPath();
	});
	//---上传头像图片---
    function saveIconPath(){
    	var img = cropper.getDataURL();
    	var imgCode = img.replace("data:image/png;base64,","");
    	var flag = $("#changeIcon_form").find("input[name='flag']").val();
    	var flagId = $("#changeIcon_form").find("input[name='flagId']").val();
		setTimeout(function(){
			$.ajax({
    			type:"post",
    			url:base_root+"/front/member/personal/saveIcon.do",
    			data:{
    				"imgCode":imgCode,
    				"flag":flag,
    				"flagId":flagId
    			},
    			success:function(response){
    				if(response.result == true){
    					//头像路径
    					var filePath = response.filePath;
    					var gender = response.gender;
    					$("#cropImg").attr("src",base_root+filePath);
    					$("#bigImg").attr("src",base_root+filePath);
    					$("#littleImg").attr("src",base_root+filePath);
    					//刷新菜单栏头像路径
    					$("img.ls-port-img").attr("src",base_root+filePath);
    					$("#content").find(".view_content").show();
    			   		//$("#content").find(".edit_content").hide();
    			   		//$("#content").find(".edit_contentbox").hide();
    			   		$(".edit_content").css("display","none");
    			   		$(".edit_contentbox").css("display","none");
    			   	//	window.//console.log(langMutilForJs[response.code]);
    			   		//layer.msg("aaaaaaaaaaaaaaa");
    			   		$("#file").val('');
    			   		layer.msg(langMutilForJs[response.code]);
    			   		
    				}else{
    					layer.msg(langMutilForJs[response.code]);
    				}
    			},
    			error:function(response){}
			});
		},300);
    };
});