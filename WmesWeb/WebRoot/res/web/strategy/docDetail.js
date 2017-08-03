define(function(require) {

	var $ = require('jquery');
			require('layer');
	
	//关闭iframe
  	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    $('#closeIframe').click(function(){
        parent.layer.close(index);
    });
    
    $('#rejectDoc').on('click',function(){
        layer.open({
        	title:'Reject',
        	content:'<textarea id="reject_result"></textarea>',
        	btn:['Reject','Cancle'],
        	yes:function(index,layro){
        		$("#check_result").val($('#reject_result').val())
        		layer.close(index);
        	}
        })
    });
    
});	