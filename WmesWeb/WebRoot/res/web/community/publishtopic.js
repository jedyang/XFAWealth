/**
 * publishtopic.js 发布帖子
 * @author mqzou 2017-03-14
 */
define(function(require) {
	
	var $ = require('jquery');
	require('webuploader');
	require('layui');
	
//	require("umeditorConfig");//配置文件
//	require("ueditor");//配置文件
//	
//    var ue = UM.getEditor('container',{
//    	toolbar: ['source | undo redo | bold italic underline strikethrough | superscript subscript | forecolor backcolor | removeformat |',
//    	            'insertorderedlist insertunorderedlist | emotion  selectall cleardoc paragraph |fontfamily fontsize' ,
//    	            '| justifyleft justifycenter justifyright justifyjustify |',
//    	            'link unlink | image ',
//    	            '| horizontal']
//		,lang:lang
//		,imageUrl:base_root + "/upload.do?r="+Math.random()+"&moduleId=4"            //图片上传提交地址
//		,imagePath:base_root +'/loadImgSrcByPath.do?filePath='
//		,imageFieldName:"upfile"
//		,imageMaxSize:"200"
//    });
//    ue.ready(function(){
//        $("#edui1").width("100%");
//        $("#edui1_iframeholder").width("100%");
//       /* var insightContent = $("#preContent").html().trim();
//        ue.setContent(insightContent);*/
//    });
	var editindex;
	layui.use('layedit', function(){
		  var layedit = layui.layedit;
		  layedit.set({
			  uploadImage: {
			    url: base_root + "/wmesUpload.do?moduleRelate=topic&uploadType=image" //接口url
			    ,type: 'post' //默认post
			  }
			});
			//注意：layedit.set 一定要放在 build 前面，否则配置全局接口将无效。
		  editindex = layedit.build('topiccontent',{ height: 380 }); ////设置编辑器高度 建立编辑器
	});
	
	$("#btnsave").on('click',function(){
		var layedit = layui.layedit;
//		var content = layedit.getContent(editindex);
		//alert(content);
	});
    
    function saveTopic(){
    	var title=$("#title").val();
    	var isInsight=$(".originalInsight:checked").val();
    	var sectionId=$(".sectionId").val();
    	var publish=$(".publishTo:checked").val();
    	
    	var layedit = layui.layedit;
		var content = layedit.getContent(editindex);
		
    	//var content=ue.getContent();
    	
    	if(title==undefined || title==""){
    		layer.msg(langMutilForJs['topic.detail.comment.tips'],{icon:3});
    		return ;
    	}
    	if(content==undefined || content==""){
    		layer.msg(langMutilForJs['topic.publish.content.tips'],{icon:3});
    		return ;
    	}
    	if(sectionId==undefined || sectionId==""){
    		layer.msg(langMutilForJs['topic.publish.section.tips'],{icon:3});
    		return ;
    	}
    	
    	$.ajax({
    		type:'post',
    		datatype:'json',
    		url:base_root+"/front/community/info/saveTopic.do",
    		data:{title:title,isInsight:isInsight,sectionId:sectionId,publish:publish,content:content},
    		success:function(json){
    			if(json.result){
    				layer.msg("发表成功");
    				window.location.href=base_root+"/front/community/info/community.do";
    			}
    		}
    	})
    }
    
    $(".wmes-topic-submit").on('click',function(){
    	saveTopic();
    })
    
    $(document).on("click",".register_xiala_long input",function(){
		$(this).siblings(".regiter_xiala_ul").show();
	});
	$(document).on('click','.register_xiala_ico',function(){
		if($(this).parents('.register_xiala_long').find('ul').css('display') == 'none'){
			$(this).siblings(".regiter_xiala_ul").show();
		}else{
			$(this).siblings(".regiter_xiala_ul").hide();
		};
	});
	$(document).on('mouseleave','.register_xiala_long',function(){
		$(this).find('.regiter_xiala_ul').hide();
	});
	
    $(".regiter_xiala_ul").on("click","li",function(){
		$(this).parent().siblings('.value_hidden').val( $(this).attr("id") );
		$(this).parent().siblings('.value_show').val( $(this).text() );
		$(".regiter_xiala_ul").hide();
	});
    
})