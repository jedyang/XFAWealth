define(function(require) {

	var $ = require('jquery');
			require('layer');
			require("jqueryForm");
	
	//关闭iframe
    $('#closeIframe').click(function(){
       window.close();
    });
    
    //文档退回
    $('#reject-show').on('click',function(){
        layer.open({
        	title:'Reject',
        	content:'<textarea id="reject_result"></textarea>',
        	btn:['Reject','Cancle'],
        	yes:function(index,layro){
        		$("#check_result").val($('#reject_result').val())
        		$("#check_status").val("2");
        		$("#checkForm").ajaxSubmit({
        			url:base_root+ "/front/kycDoc/kycDocApprove.do?r="+Math.random(),
        			success:function(data){
        				if(data.result)
        					window.location.reload(); 
        			},
        			error:function(){
        				alert("error");
        			}
        		});
        		layer.close(index);
        	}
        });
    });
    
    //文档更新
    $("#update-show").on("click",function(){
    	var docId = $("#docCheck_docId").val();
    	 parent.layer.open({
    		type: 2,
    		content: base_root+'/front/kycDoc/docDetailEdit.do?docId='+docId+'&r='+Math.random(),
    		area: ['1019px', '630px'],
    		shape:0.5,
    		shapeClose:true,
    		title:false,
    		maxmin: true
    	})
    });
 
	//文档审批    
    $("#approved-show").on("click",function(){
    	$("#check_status").val("1");
    	$("#checkForm").ajaxSubmit({
			url:base_root+ "/front/kycDoc/kycDocApprove.do?r="+Math.random(),
			success:function(data){
				window.location.reload(); 
			},
			error:function(){
				alert("error");
			}
		});
    });
    
    //文档历史
    $("#history-show").on("click",function(){
    	
    })
    
    
});	