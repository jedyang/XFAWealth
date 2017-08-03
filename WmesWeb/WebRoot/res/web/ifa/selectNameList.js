define(function(require){
	var $ = require('jquery');
	
	$('.selectedlist li').on('mousemove',function(){
		$(this).find('.selectedshadow').addClass('selectedshadow1');
	});
	$('.selectedlist li').on('mouseout',function(){
		$(this).find('.selectedshadow').removeClass('selectedshadow1');
	});
	
    $(".selectedlist li").on("click",function(){//可多选
        $(this).addClass("selected_active");
        var id = $(this).attr("data-id");
        var type = $(this).attr("data-type");
        /*if ($(this).find("a").attr("style")=="display:none"){
            $(this).find("a").attr("style","display:block");

	        if (type=="clients") $("#clients").val($("#clients").val()+","+id);
	        if (type=="prospects") $("#prospects").val($("#prospects").val()+","+id);
	        if (type=="buddies") $("#buddies").val($("#buddies").val()+","+id);
	        if (type=="colleagues") $("#colleagues").val($("#colleagues").val()+","+id);

        }else{
            $(this).find("a").attr("style","display:none");

	        if (type=="clients") $("#clients").val( $("#clients").val().replace(','+id, ''));
	        if (type=="prospects") $("#prospects").val($("#prospects").val().replace(','+id, ''));
	        if (type=="buddies") $("#buddies").val($("#buddies").val().replace(','+id, ''));
	        if (type=="colleagues") $("#colleagues").val($("#colleagues").val().replace(','+id, ''));
        }*/
        //alert($("#clients").val());
      
        
        
        if($(this).find('b').hasClass("selectedchecked")){
			$(this).find('i').removeClass('selectedshadow1');
			$(this).find('b').toggleClass('selectedchecked');
			
			if (type=="clients") $("#clients").val( $("#clients").val().replace(','+id, ''));
			if (type=="prospects") $("#prospects").val($("#prospects").val().replace(','+id, ''));
			if (type=="buddies") $("#buddies").val($("#buddies").val().replace(','+id, ''));
			if (type=="colleagues") $("#colleagues").val($("#colleagues").val().replace(','+id, ''));
		}else{
			$(this).find('i').addClass('selectedshadow1');
			$(this).find('b').toggleClass('selectedchecked');
			
			 if (type=="clients") $("#clients").val($("#clients").val()+","+id);
	        if (type=="prospects") $("#prospects").val($("#prospects").val()+","+id);
	        if (type=="buddies") $("#buddies").val($("#buddies").val()+","+id);
	        if (type=="colleagues") $("#colleagues").val($("#colleagues").val()+","+id);
	        
			
		};
		
		  var span = $(this).parent().parent().find("span.count");
	        var val = "";
	        if (type=="clients") val = $("#clients").val();
	        if (type=="prospects") val = $("#prospects").val();
	        if (type=="buddies") val = $("#buddies").val();
	        if (type=="colleagues") val = $("#colleagues").val();
	        //alert(val);
	        $(span).html(countVal(val));
	        
		if($(this).parents('.myClient').find('.selectedchecked').length !== $(this).parents('.myClient').find('.selectedlist li').length){
			$(this).parents('.myClient').find('.selectedtextright input').prop('checked',false);
		}else{
			$(this).parents('.myClient').find('.selectedtextright input').prop('checked',true);
		};
		$(this).parents('.myClient').find('.selectedtextleft span').html($(this).parents('.myClient').find('.selectedchecked').length);
    });
    
    /**
    * 根据父窗口的类型，和 已选值，自动选定（适于编辑状态）
    */
    function checkSelection(type, selected){
	    //alert(type);
	    //alert(selected);
        if (!type) return;
        if (!selected) return;

        var ifAll = false;
        if (selected.indexOf("ALL")>=0) ifAll = true;
        
        var strArr = selected.split(",");
        if (strArr.length==0) return;
   
        $.each(strArr, function(i,val){  
            //alert("val="+val);
            if (!val || val.length==0) return true;//continue
	        $(".selectedlist").find("li").each(function(){
	            var _type = $(this).attr("data-type");
	            var _li = $(this);
	            if (_type==type){
			        var id = $(this).attr("data-id")+"";
		            if (ifAll || (val && val.length>0 && val == id)){
		             // if (_li.find("a").attr("style")=="display:none")
		                _li.click();
		                if (!ifAll) return false;
	                }
		        }
		    });
        });
    }
    
    /**
    * 根据父窗口的类型，和 已选值，自动选定（适于编辑状态）
    */
    function initial(){
        var parent_parm_clients = new Object(); 
        var parent_parm_prospects = new Object(); 
        var parent_parm_buddies = new Object(); 
        var parent_parm_colleagues = new Object(); 
        
        if (window.name == "permitASetting"){
            parent_parm_clients = window.opener.document.getElementById("permit_clients");
            parent_parm_prospects = window.opener.document.getElementById("permit_prospects");
            parent_parm_buddies = window.opener.document.getElementById("permit_buddies");
            parent_parm_colleagues = window.opener.document.getElementById("permit_colleagues");
        }
        if (window.name == "pushASetting"){
            parent_parm_clients = window.opener.document.getElementById("push_clients");
            parent_parm_prospects = window.opener.document.getElementById("push_prospects");
            parent_parm_buddies = window.opener.document.getElementById("push_buddies");
            parent_parm_colleagues = window.opener.document.getElementById("push_colleagues");
        }
        checkSelection("clients", parent_parm_clients.value);
        checkSelection("prospects", parent_parm_prospects.value);
        checkSelection("buddies", parent_parm_buddies.value);
        checkSelection("colleagues", parent_parm_colleagues.value);
        
        //console.log("clients:"+parent_parm_clients.value);
        //console.log("prospects:"+parent_parm_prospects.value);
        //console.log("buddies:"+parent_parm_buddies.value);
        //console.log("colleagues:"+parent_parm_colleagues.value);
    }
    
    /**
    * 返回（保存）当前选定值，自动设置父窗口对应的对象值，需保证变量定义一致
    */
    $("#btn_update").on("click",function(){
        if (!window.opener){
            alert("Getting opener unsuccessfully.\nThe result can not be return.\n");
            return;
        }
            
        try{
	       
	        window.opener.document.getElementById("cnt_clients").value=cnt_clients;
	        window.opener.document.getElementById("cnt_prospects").value=cnt_prospects;
	        window.opener.document.getElementById("cnt_buddies").value=cnt_buddies;
	        window.opener.document.getElementById("cnt_colleagues").value=cnt_colleagues;

            //alert(window.name);
	        //if (!id) return;
	        //获取父窗口的id对象，添加新id值
	        if (window.name == "permitASetting"){
		        var parent_parm_clients = window.opener.document.getElementById("permit_clients");
		        parent_parm_clients.value=$("#clients").val();
		        
		        var parent_parm_prospects = window.opener.document.getElementById("permit_prospects");
		        parent_parm_prospects.value =$("#prospects").val();
		        
		        var parent_parm_buddies = window.opener.document.getElementById("permit_buddies");
		        parent_parm_buddies.value=$("#buddies").val();
		        
		        var parent_parm_colleagues = window.opener.document.getElementById("permit_colleagues");
		        parent_parm_colleagues.value=$("#colleagues").val();
	        }
	        else if (window.name == "pushASetting"){
	            var parent_parm_clients = window.opener.document.getElementById("push_clients");
	            parent_parm_clients.value=$("#clients").val();
	            
	            var parent_parm_prospects = window.opener.document.getElementById("push_prospects");
	            parent_parm_prospects.value =$("#prospects").val();
	            
	            var parent_parm_buddies = window.opener.document.getElementById("push_buddies");
	            parent_parm_buddies.value=$("#buddies").val();
	            
	            var parent_parm_colleagues = window.opener.document.getElementById("push_colleagues");
	            parent_parm_colleagues.value=$("#colleagues").val();
	        }
	        //alert(parent_parm_ids.value);
	        //调用父窗口的基金列表刷新方法
	        window.opener.document.getElementById("refreshSelection").click();
        }catch(e){
            alert("返回时出现错误。");
        }
        window.close();
    });
    
    $('.SelectAll').on('click',function(){
		$(this).parents('.selectedtextright').find('input').click();
		 
		
	});
	$('.selectedtextright input').on('click',function(){
		/*if($(this).prop("checked")){
			$(this).parents('.myClient').find('.selectedlist li b').addClass('selectedchecked');
			$(this).parents('.myClient').find('.selectedlist li i').addClass('selectedshadow1');
		};
		$(this).parents('.myClient').find('.selectedtextleft span').html($(this).parents('.myClient').find('.selectedchecked').length);
		*/
		var _this = $(this);
        var _parent = _this.parent().parent().parent().parent();
        if (_this.is(':checked')){//检查checkbox是否选中
	        _parent.parent().find(".selectedlist").find("li").each(function(){
	           var _li = $(this);
	           if (!_li.find("b").hasClass("selectedchecked"))
	               _li.click();
	        });
        }else{
        	_parent.parent().find(".selectedlist").find("li").each(function(){
               var _li = $(this);
               if (_li.find("b").hasClass("selectedchecked"))
                   _li.click();
            });
        }
       
       //console.log($("#clients").val());
       //console.log($("#prospects").val());
       //console.log($("#buddies").val());
       //console.log($("#colleagues").val());
	});
	$('.selectedtextright span').on('click',function(){
/*		$(this).parents('.myClient').find('.selectedlist li b').toggleClass('selectedchecked');
		$(this).parents('.myClient').find('.selectedlist li i').toggleClass('selectedshadow1');
		$(this).parents('.myClient').find('.selectedtextleft span').html($(this).parents('.myClient').find('.selectedchecked').length);
		if($(this).parents('.myClient').find('.selectedchecked').length !== $(this).parents('.myClient').find('.selectedlist li').length){
			$(this).parents('.myClient').find('.selectedtextright input').prop('checked',false);
		}else{
			$(this).parents('.myClient').find('.selectedtextright input').prop('checked',true);
		};*/
        var _this = $(this);
        var _parent = _this.parent().parent().parent().parent();
        //alert(_parent.html());
        //只需每个li做一次click事件即可反选
        _parent.find("li.container").each(function(){
        	if($(this).find("b").hasClass("selectedchecked"))
              $(this).click();
        });
	});
	$('.ifa-more-ico').on('click',function(){
		$(this).toggleClass('ifa-more-icoactive');
		$(this).parents('.myClient').find('.selectedtext').toggleClass('ifa-more-ico-hidden');
		$(this).parents('.myClient').find('.selectedlist').toggleClass('ifa-more-ico-hidden');
		
		
	});

    
    function countVal(str){
        if (!str) return 0;
        str = str.substring(str.indexOf(",")+1);//去掉首字符为逗号
        str = str.replace("ALL,");
        var strArr = str.split(",");
        return strArr.length;
    }
    
    //初始化页面
    initial();

});