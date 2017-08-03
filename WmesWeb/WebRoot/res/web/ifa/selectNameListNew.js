define(function(require){
	var $ = require('jquery');
	
	$('.selectedlist li').on('mousemove',function(){
		$(this).find('.selectedshadow').addClass('selectedshadow1');
	});
	$('.selectedlist li').on('mouseout',function(){
		$(this).find('.selectedshadow').removeClass('selectedshadow1');
	});
	
	var arrIds = [];//选中用户Id
	var arrNames = [];//选中用户名称
	
    $(".selectedlist li").on("click",function(){//可多选
        $(this).addClass("selected_active");
        var id = $(this).attr("data-id");
        var type = $(this).attr("data-type");
        var name = $(this).children('p').first().text();
        
        //alert(name);
        
        if($(this).find('b').hasClass("selectedchecked")){
			$(this).find('i').removeClass('selectedshadow1');
			$(this).find('b').toggleClass('selectedchecked');
			
			//if (type=="clients") $("#clients").val( $("#clients").val().replace(','+id, ''));			
			var idIndex = arrIds.indexOf( id );
			var nameIndex = arrNames.indexOf( name );
			arrIds.splice(idIndex, 1);
			arrNames.splice(nameIndex, 1);
			
		}else{
			$(this).find('i').addClass('selectedshadow1');
			$(this).find('b').toggleClass('selectedchecked');
						
			//if (type=="clients") $("#clients").val($("#clients").val()+","+id);  			
			arrIds.push(id);
			arrNames.push(name);
			
		};

		$("#memberIds").val(arrIds.join(','));
		$("#memberNames").val(arrNames.join(','));
			        
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
    function checkSelection( selected){
	    //alert(type);
	    //alert(selected);
       
        if (!selected) return;

        var ifAll = false;
        if (selected.indexOf("ALL")>=0) ifAll = true;
        
        var strArr = selected.split(",");
        if (strArr.length==0) return;
   
        $.each(strArr, function(i,val){  
            //alert("val="+val);
            if (!val || val.length==0) return true;//continue
	        $(".selectedlist").find("li").each(function(){
	            //var _type = $(this).attr("data-type");
	            var _li = $(this);
//	            if (_type==type){
			        var id = $(this).attr("data-id")+"";
		            if (ifAll || (val && val.length>0 && val == id)){
		             // if (_li.find("a").attr("style")=="display:none")
		                _li.click();
		                if (!ifAll) return false;
	                }
//		        }
		    });
        });
    }
    
    /**
    * 根据父窗口的类型，和 已选值，自动选定（适于编辑状态）
    */
    function initial(){
        var openerMemberIdsObj = new Object(); 
        
        if (window.name == "permitASetting"){
        	var openerMemberIds = $("#openerMemberIdsObj").val();
        	////console.log(openerMemberIds);
        	openerMemberIdsObj = window.opener.document.getElementById(openerMemberIds);    
        	checkSelection( openerMemberIdsObj.value);
        }
        
        //console.log("clients:"+openerMemberIdsObj.value);
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

        	var openerMemberIds = $("#openerMemberIdsObj").val();
        	var openerMemberNames = $("#openerMemberNamesObj").val();
        	
        	////console.log(openerMemberIds+','+openerMemberNames);

            //alert(window.name);
	        //if (!id) return;
	        //获取父窗口的id对象，添加新id值
//	        if (window.name == "permitASetting"){		        
//
//
//	        }
	        //window.opener.document.getElementById(openerMemberIds).value=$("#memberIds").val();
	        $('#'+openerMemberIds,window.opener.document).val($("#memberIds").val());
        	if(openerMemberNames != ''){
		        $('#'+openerMemberNames,window.opener.document).text($("#memberNames").val());
		        $('#'+openerMemberNames,window.opener.document).val($("#memberNames").val());
        	}

	        //alert(parent_parm_ids.value);
	        //调用父窗口的基金列表刷新方法
	        //window.opener.document.getElementById("refreshSelection").click();
        }catch(e){
        	////console.log(e);
        	alert(e);
            alert("返回时出现错误。");
        }
        window.close();
    });
    
    $("#btn_cancel").on("click",function(){
    	window.close();
    });
    
    //全选
    $('.SelectAll').on('click',function(){
		$(this).parents('.selectedtextright').find('input').click();		
	});
    
    //全选
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
	});
	
	//反选
	$('.selectedtextright span').on('click',function(){
       
        $(".selectedlist .container").each(function(){
        	$(this).click();
        });
	});
	
	
	$('.ifa-more-ico').on('click',function(){
		$(this).toggleClass('ifa-more-icoactive');
		$(this).parents('.myClient').find('.selectedtext').toggleClass('ifa-more-ico-hidden');
		$(this).parents('.myClient').find('.selectedlist').toggleClass('ifa-more-ico-hidden');
		
		
	});
	

    
    //初始化页面
    initial();

});