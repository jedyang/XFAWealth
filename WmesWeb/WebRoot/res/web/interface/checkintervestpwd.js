/**
 * 与ORM账号密码PIN码登录验证javascript插件
 * author:林文伟
 * date:2017-01-05
 * 参数格式：accountlist 调用者组装待验证账号的列表，格式为账号|类型，账号|类型， targetid 为调用者调用插件后要把数据存储到那个元素中去,暂时确定为元素id
 * 推荐Exmple:
 * var jsonlist = JSON.parse('{"accountlist":"mqzou001|1,mic123|1,mqzou004|1","targetid":"txttitle"}');
   $("#btninterface").InitInterface({ jsonlist:jsonlist});
 */
define(function(require) {
	'use strict';
	var $ = require('jquery');
	require('layui');
	

    $.paramsDefaults = $.paramsDefaults || {};
    $.paramsDefaults.property = {
    };
    
    $.fn.InitInterface = function (b) { 
        var fun = function (parentObj) {
	            var p = $.extend({}, $.paramsDefaults.property, b || {});
	            var newid = new Date().getTime(); 
	            var sendurl = "?a=b";
	            //{"accountlist":accountlist,"accounttype":"1","isopen":'1' }
	            var accountlist = p.accountlist;
	            var accounttype = p.accounttype;
	            var isopen = p.isopen;
	            
	            if(isopen=='1'){
	            	showdialog1(accountlist,accounttype);
	            } else{
	            	$(parentObj).on('click',p,showdialog);
	            }
	            
            };
            
            return $(this).each(function () {
                fun($(this));
            });
    }
    
    function showdialog1(accountlist,accounttype){ 
    	//var accountlist = event.accountlist;
    	//var targetid = event.targetid;
    	layer.open({
			  type: 2,
			  title: 'Input Account Password',
			  shadeClose: false,
			  closeBtn: 0,
			  scrollbar: false,
			  anim:2,
			  //shade: 0.8,
			  area: ['610px', '400px'],
			  content: base_root + "/front/investorinterface/inputTransactionPassword.do?accounttype="+accounttype+"&accountList="+accountlist 
		}); 
    }
    
    function showdialog(p){ //console.log(event);
    	var accountlist = p.data.accountlist;
    	var accounttype = p.data.accounttype;
    	layer.open({
			  type: 2,
			  title: 'Input Account Password',
			  shadeClose: false,
			  scrollbar: false,
			  closeBtn: 0,
			  //shade: 0.8,
			  area: ['610px', '400px'],
			  content: base_root + "/front/investorinterface/inputTransactionPassword.do?accounttype="+accounttype+"&accountList="+accountlist 
		}); 
    }
    
    var interfaceObj = {};
    interfaceObj.showDialog = function(accountList){
    	
    }
    
    $('.transaction-step-one-btn').click(function(){
    		var accountno= $(this).attr('accountno');
    		var accounttype= $(this).attr('accounttype');
    		var accountpwd= $(this).prev('input').val();
    		var thisObj = $(this); 
    		if(accountpwd==''||accountpwd==undefined){
    			layer.msg('please input the password!');
				return false;
    		} 
    		if(accountno!=''&&accountno!=undefined&&accounttype!=''&&accounttype!=undefined&&accountpwd!=''&&accountpwd!=undefined){
    			$.ajax({
    				type : 'post',
    				datatype : 'json',
    				url : base_root + "/front/investorinterface/saveLogin.do?datestr="+ new Date().getTime(),
    				data : {'accountNo':accountno,'accountPwd':accountpwd,'accountType':accounttype },
    				 beforeSend: function () {},
    				 complete: function () {
    					 //layer.closeAll();
    				 },
    				success : function(json) {
    					var flag = json.flag;
    					if(flag==true){
    						var pinPos = json.pingPos;//2,1,6
    						$('#hidpincode').val(pinPos);
    						//生成3个输入框
    						var transactionsteptwoelement = '#transaction-step-two-'+accountno;
    						if(pinPos!=''&&pinPos!=undefined){
    							var array = pinPos.split(',');
    							for(var i=0;i<array.length;i++){
    								if(array[i]!=''){
    									//2
    									$(transactionsteptwoelement)
    									.children('.transaction-pin-number')
    									.find('input[index="'+array[i]+'"]')
    									.css({'border':'1px solid #e0dfdf','width':'15px','margin':'0px 5px'})
    									.removeAttr('readonly')
    									.attr('iscaninput','1')
    									.val('');
    								}
    							}
    						} else{
    							layer.msg('Pin code data error,please try again!');
        						return false;			
    						}
    						
    						thisObj.parent('.transaction-step-one').hide();
    						thisObj.parent('.transaction-step-one').next().show();
    						var validityelement = '#validity-'+accountno;
    						$(validityelement).show();
    					} else{
    						layer.msg('password error!');
    						return false;
    					}
    					
    				}
    			});
    		}
    	});
    
	    //$('.transaction-pin-number input').on('change',function(){alert('');});
    
	    $('.transaction-step-two-btn').click(function(){
	    	var thisObj = $(this);
	    	//get pin code 2,1,6
	    	var inputcodelist='';
	    	var accountno= $(this).attr('accountno');
	    	var accounttype = $(this).attr('accounttype');
	    	var transactionsteptwoelement = '#transaction-step-two-'+accountno;
	    	var pinPos = $(this).prev().find('#hidpincode').val();
	    	if(pinPos!=''&&pinPos!=undefined){
				var array = pinPos.split(',');
				for(var i=0;i<array.length;i++){
					if(array[i]!=''){
						//2
						var eachcode = $(transactionsteptwoelement)
						.children('.transaction-pin-number')
						.find('input[index="'+array[i]+'"]').val();
						if(eachcode==''||eachcode==undefined){
							layer.msg('please input the pin code!');
							return false;
						} else{
							inputcodelist += eachcode + ',';
						}
						
					}
				}
			} else{ // if the pin code is empty
				for(var i=0;i<6;i++){
					var eachcode = $(transactionsteptwoelement)
					.children('.transaction-pin-number')
					.find('input[index="'+(i+1)+'"]').val();
					if(eachcode==''||eachcode==undefined){
						layer.msg('please input the pin code!');
						return false;
					} else{
						inputcodelist += eachcode + ',';
					}
				}
			}
	    	
	    	var validityelement = '#validity-'+accountno;
	    	var isforever = $(validityelement).children('input[type="checkbox"]').is(':checked')==true?"1":"0";
			if(inputcodelist!=''&&inputcodelist!=undefined){
				//inputcodelist = substring(inputcodelist,0,inputcodelistl)
				$.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/investorinterface/savePin.do?datestr="+ new Date().getTime(),
					data : {'inputPinCode':inputcodelist,'accountNo':accountno,'isForever':isforever,'accountType':accounttype },
					 beforeSend: function () {layer.msg('Loading....', {icon: 9,time: 20000});},
					 complete: function () {},
					success : function(json) {//console.log(json);
						layer.closeAll();
						var flag = json.flag;
						if(flag==true){
							thisObj.parent('.transaction-step-two').hide();
							thisObj.parent('.transaction-step-two').next().show();
    						$(validityelement).hide();
    						//
    						thisObj.parents('.transaction-account-contents').find('p[name="transaction-account-'+accountno+'"]').attr('loginresult','1');
						} else{
							layer.msg('pin code error!');
							return false;
						}
						
					}
				});
			} else{
				layer.msg('please input the pin code!');
				return false;
			}
		});
	    
	    $('.transaction-step-close-btn').click(function(){
	    	var targetid = getQueryString('targetid');
	    	var accountobjlist = $('.transaction-account-name');
	    	var jsonData = '[';
	    	accountobjlist.each(function(i,n){
	    		var loginresult = $(n).attr('loginresult');
	    		var accountno = $(n).attr('accountno');
	    		jsonData += '{"accountno":"'+accountno+'","result":"'+loginresult+'"},';
	    	});
	    	
	    	jsonData = jsonData.substring(0,jsonData.length-1) + "]" ;
	    	var resultjson = JSON.parse(jsonData);
	    	var targetelement = targetid;
	    	//after colse the dialog ,call the holding interface

	    	//window.parent.document.getElementById(targetelement).value=resultjson;  
	    	window.parent.callbackloginresult(resultjson);
	    	//parent.document.$('#hidloginresult').val(resultjson);
	        //parent.layer.tips('Look here', '#parentIframe', {time: 5000});
	    	/*if($.fn.callBackFun!=undefined){
	    		$.fn.callBackFun();
	    	}*/
	    
	        parent.layer.closeAll();
	        
	    });
	    
	    function getQueryString(name) {
	        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	        var r = window.location.search.substr(1).match(reg);
	        if (r != null) return decodeURIComponent(r[2]);
	        return null;
	    }
	    
	    //限制只能输入数字跟自动切换到一个可输入框中
	    $('body').on('keyup','input[iscaninput="1"]',function(){
	    	$(this).val($(this).val().replace(/\D/g,''));  
	    	var char = $(this).val();
	    	if(char.length==1)$(this).nextAll('input[iscaninput="1"]').eq(0).focus();
	    });
	    
    

    return interfaceObj;
    var psw = {};

    psw.testcheckpwd = function(){
    	alert('123');
    }

    return psw;
});
