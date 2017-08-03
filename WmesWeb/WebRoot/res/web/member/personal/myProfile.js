
define(function(require) {

	var $ = require('jquery');
	require('layer');
	require('datepick');
	require('jqueryRange');
	 new JsDatePick({
	      useMode:2,
	      target:"bornDate",
	      dateFormat:"%Y-%m-%d"
	    });
	 //$(".bornDate").attr("id","");
	 
	 
		
		
	function savePersonalInfo(){
		var params = $("#RegisterForm").serialize();
		params = decodeURIComponent(params,true);
		$.ajax({
			type:'POST',
			url: _root_ + "/front/member/personal/savePersonalInfo.do",
			datatype:"JSON",
			data: {inputData : params},
			success:function(response){
				var dataObj = JSON.parse(response);;
//				layer.msg(dataObj.result);
				if(dataObj.result){
					window.location.href= _root_+"/front/member/personal/myProfile.do";
				}else{
				    layer.msg(langMutilForJs["global.failed.save"]);
				}
			},
			error:function(data){
				// 
			}
		})
	}
	$(".BasicInfo_title_ico").on("click",function(){
        $(this).parents(".gary_title").find(".BasicInfo_title_ico").hide().end().find(".BasicInfo_title_ico_hide").show();
        $(this).parents(".BasicInfo_show").stop().animate({ 
            height: "60px", 
          }, 300 );
        $(this).parents(".BasicInfo_show").find(".BasicInfo_row").hide();
        $(this).parents(".BasicInfo_show").find(".btn_next").hide();
    });
    $(".BasicInfo_title_ico_hide").on("click",function(){
        $(this).parents(".gary_title").find(".BasicInfo_title_ico").show().end().find(".BasicInfo_title_ico_hide").hide();
        $(this).parents(".BasicInfo_show").stop().animate({ 
            height: "100%", 
          }, 300 );
        $(this).parents(".BasicInfo_show").find(".BasicInfo_row").show();
        $(this).parents(".BasicInfo_show").find(".btn_next").show();
    });
    
    $("#saveButton").on("click",function(){
    	savePwd();
    })
    
	function savePwd(){
	    //检查输入合法性
	    var id = $("#id").val(); 
	    var oldpsw = $('#oldpsw').val(); 
	    if (!oldpsw){layer.msg(propJson['member.personal.oldPwd.input']); return;}
	    var checkOldPwdTips = $('#checkOldPwdTips'); 
	    if (checkOldPwdTips.length < 1){layer.msg(propJson['member.personal.oldPwd.input.reEnter']); return;}
	    
	    var password = $('#password').val();
	    if (!password){layer.msg(propJson['member.personal.password.input']); return;}
	    var checkPasswordTips = $('#checkPasswordTips');
	    if (checkPasswordTips.length < 1){layer.msg(propJson['member.personal.password.input.reEnter']); return;}
	    
	    var confirm_password = $('#confirm_password').val();
	    if (!confirm_password){layer.msg(propJson['member.personal.compfirm.input']); return;}
	    var checkConfirmPasswordTips = $('#checkConfirmPasswordTips');
	    if (checkConfirmPasswordTips.length < 1){layer.msg(propJson['member.personal.compfirm.input.reEnter']); return;}
	    
	    //提交表单
	    $("#saveButton").attr({'disabled':true});
	    $.ajax({
	    	type:"POST",
	        url: _root_+"/front/member/personal/savepass.do?datestr="+new Date().getTime(),
	        data: {oldpsw:oldpsw,password:password},
	        success: function(response)
	        {   
	            $("#saveButton").attr({'disabled':false});
	            var dataObj = JSON.parse(response);;
	            if(dataObj.result){
	            	layer.msg("Password changed successfully！将跳转登录页面",{icon:1,time:2000});
	            	setTimeout(function(){
	            		window.location.href=_root_+"/front/viewLogin.do?r="+Math.random();
	            	},2000);
	            }else{
	            	layer.msg(dataObj.msg);
	            }
	        },
	        error:function()
	        {
	            layer.msg(langMutilForJs["global.failed.save"]+":"+langMutilForJs["error.exceptionThrew"]);
	        }
	    });
	}
	
	$("#editInvestStyle").on("click",function(){
		openResWin(_root_+"/front/member/personal/editInvestStyle.do",800,400,"editInvestStyle");
	});
	
	$("#editExpertType").on("click",function(){
		openResWin(_root_+"/front/member/personal/editExpertType.do",800,400,"editExpertType");
	});
	
	$("#editHobby").on("click",function(){
		openResWin(_root_+"/front/member/personal/editHobby.do",800,400,"editHobby");
	});
	
	$("#editIntroduction").on("click",function(){
		$("#editField").val("introduction");
		showEditContent($("#introduction").val());
	});
	/*$("#editHL").on("click",function(){
		$("#editField").val("highlight");
		showEditContent($("#highlight").val());
	});*/
	
	function showEditContent(content){
		$("#content").val(content);
	    $(".ifa-space-popup").show();
	}
	$("#btn_ok").on("click",function(){
		var field = $("#editField").val();
		if (field=="introduction"){
			$("#introduction").val($("#content").val());
			$("#editIntro").html($("#introduction").val());
		}else if (field=="highlight"){
			$("#highlight").val($("#content").val());
			$("#editHighlight").html($("#highlight").val());
		}
		
        $.ajax({
            type:'post',
            url: base_root + "/front/member/personal/saveContentByField.do",
            datatype:"JSON",
            data: {"field":field, "content":$("#content").val()},
            success:function(data){
                //location.reload();
            },
            error:function(data){
                // 
            }
        })
        
		$(".ifa-space-popup").hide();
	});
	$("#btn_hide").on("click",function(){
		$(".ifa-space-popup").hide();
	});
	$(".space-mask-close").on("click",function(){
		$(".ifa-space-popup").hide();
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
	   //window.open(url,id,"height="+height+", width="+width+", top="+top+", left="+left+",location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	   var winid = parseInt(Math.random()*10000000000)+"";
	   window.open(url,winid,"height="+height+", width="+width+", top="+top+", left="+left+",location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");   
	}  

	function openWinScroll(url,id){ 
	    var myw = screen.availWidth-10;   //定义一个myw，接受到当前全屏的宽    
	    var myh = screen.availHeight;  //定义一个myw，接受到当前全屏的高   
	    var winid = parseInt(Math.random()*10000000000)+"";
		//window.open(url, id, "width=" + myw + ",height=" + myh + ",top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
	    window.open(url, winid, "width=" + myw + ",height=" + myh + ",top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
	}  
	
	edit();
	$('.gary_title_amend li').on('click', function() {
		$('.gary_title_amend li').css('color', '#000');
		$(this).css('color', '#467fb1');
		$('.setting-content').removeClass('setting-contentac').eq($(this).index()).addClass('setting-contentac');
		$(this).siblings().removeClass('backgroundac')
		$(this).addClass('backgroundac');
		if($(this).index()==4){
			$("#setting-basicInforight .register_xiala_long input").unbind();
			$("#setting-basicInforight .register_xiala_long").unbind();
			$('#setting-basicInforight .register_xiala_ico').unbind();
		}else{
			edit();
		};
		
		
		
		var windowHeight = $(window).height();
		windowHeight = windowHeight-118;
		if($('.wmes-content').height() < windowHeight){
				$('.wmes-content').css('min-height',windowHeight);
		};
	});
	
	//edit();
	
	function edit(){
		$(".register_xiala_long input").on("focus",function(){
				$(this).siblings(".regiter_xiala_ul").show();
			});
			$('.register_xiala_ico').on('click',function(){
				if($(this).parents('.register_xiala_long').find('ul').css('display') == 'none'){
					$(this).siblings(".regiter_xiala_ul").show();
				}else{
					$(this).siblings(".regiter_xiala_ul").hide();
				};
								
			});
			$(".register_xiala_long").on("blur","input",function(){
				var _this = $(this);
					//mandatoryVal = _this.siblings(".regiter_xiala_ul").children().eq(0).attr("value");
					//_this.val(mandatoryVal);					
				setTimeout(function(){
					_this.siblings(".regiter_xiala_ul").hide();
				},200);
				
			});
			$(".regiter_xiala_ul").on("click","li",function(){
				$(this).parent().siblings('.value_hidden').val( $(this).attr("value") );
				 $(this).parent().siblings('.value_show').val( $(this).text() );
				 $(".regiter_xiala_ul").hide();
				 if($(this).parent().hasClass("dateFormatSelect")){
					 
						var format=$("#dateFormat").val();
						var date=$(".dateFormat").text();
						//console.log(format);
						//console.log(date);
						$(".dateFormat").text(Format(new Date(date),format));
				   }
					
			});
			
	};
	$('.setting-bn').on('click', function() {
		$('.setting-basicInforight input').removeAttr('readonly');
		$('.setting-basicInforight input').css('border', '1px solid #e5e4e4');
		$('.setting-basicInforight select').css('display', 'block');
		$('.setting-basicInforight select').css('border', '1px solid #e5e4e4');
		$('#setting-basicInforight .register_xiala_long').css('border','1px solid #e5e4e4');
		$('#settinghide').css('display', 'block');
		$('#settinghidden').css('display', 'block');
		$(this).css('display','none');
		$('#setting-basicInforight .register_xiala_ico').css('display', 'block');
		$('#bornDate').css('pointer-events','visible');
		$('.sexcon .sex').addClass('sex1').siblings().css('display','none');
		edit();
	});

	$('#settinghide').on('click', function() {
		savePersonalInfo();
		/*$(this).parents('.setting-content').find('.setting-basicInforight input').attr('readonly', 'readonly');
		$(this).parents('.setting-content').find('.setting-basicInforight input').css('border', '1px solid #fff');
		$('#setting-basicInforight .register_xiala_long').css('border','1px solid #fff');
		$('.setting-basicInforight select').css('display', 'none');
		$('#setting-basicInforight .register_xiala_ico').css('display', 'none');
		$(this).css('display', 'none');
		$('#settinghidden').css('display', 'none');
		$("#setting-basicInforight .register_xiala_long input").unbind();
		$("#setting-basicInforight .register_xiala_long").unbind();
		$('#setting-basicInforight .register_xiala_ico').unbind();
		$('#bornDate').css('pointer-events','none');*/
	});

	$('#settinghidden').on('click', function() {
	
		$(this).parents('.setting-content').find('.setting-basicInforight input').attr('readonly', 'readonly');
		$(this).parents('.setting-content').find('.setting-basicInforight input').css('border', '1px solid #fff');
		$('#setting-basicInforight .register_xiala_long').css('border','1px solid #fff');
		$(this).css('display', 'none');
		$('#settinghide').css('display', 'none');
		$('#setting-basicInforight .register_xiala_ico').css('display', 'none');
		$("#setting-basicInforight .register_xiala_long input").unbind();
		$("#setting-basicInforight .register_xiala_long").unbind();
		$('#setting-basicInforight .register_xiala_ico').unbind();
		$('#bornDate').css('pointer-events','none');
		$('.sexcon .sex').removeClass('sex1').siblings().css('display','block');;
		$(".setting-bn").css("display","block");
	});
	
	
	$('.setting-basicInforight select').on('change', function() {
		$(this).parents('p').find('.selectedvalue').text($(this).val());
	});
	
	$('.setting-basicInforight img').on('click',function(){
		if($(this).attr('src') == '/alpha/res/images/fund/strategies_eve.png'){
			$(this).attr('src',_root_+'/res/images/discover/eve_ico.png')
		}else{
			$(this).attr('src',_root_+'/res/images/fund/strategies_eve.png')
		}
	});
	
	$(function() {
		
		$('.range-slider').jRange({
			from: 0,
			to: 100,
			step: 1,
			scale: ['-100%', '10%'],
			format: '%s',
			width: 300,
			showLabels: true,
			isRange: true
		});
		$('#portfolioCriticalValue').jRange({
			from: -100, //滑动范围的最小值，数字，如0 
			to: 10, //滑动范围的最大值，数字，如100 
			step: 1, //步长值，每次滑动大小 
			scale: ['-100%', '10%'], //滑动条下方的尺度标签，数组类型，如[0,50,100] 
			format: '%s%', //数值格式 
			width: 300, //滑动条宽度 
			showLabels: true, // 是否显示滑动条下方的尺寸标签 
			showScale: true //是否显示滑块上方的数值标签 
		});
		

		$("#portfolioCriticalValue").on('change', function() {
			var aa = $("#portfolioCriticalValue").val();
			$('.range_result').html(aa);
			if(aa>0){
				$('.range_result').css('color','red');
			}else if(aa==0){
				$('.range_result').css('color','#999');
			}else if(aa<0){
				$('.range_result').css('color','green');
			}
		});
		
		$('#portfolioReturnValue').jRange({
			from: 10, //滑动范围的最小值，数字，如0 
			to: 100, //滑动范围的最大值，数字，如100 
			step: 1, //步长值，每次滑动大小 
			scale: ['10%', '100%'], //滑动条下方的尺度标签，数组类型，如[0,50,100] 
			format: '%s%', //数值格式 
			width: 300, //滑动条宽度 
			showLabels: true, // 是否显示滑动条下方的尺寸标签 
			showScale: true //是否显示滑块上方的数值标签 
		});
		
		$("#portfolioReturnValue").on('change', function() {
			var aa = $("#portfolioReturnValue").val();
			$('.return_result').html(aa);
			if(aa>0){
				$('.return_result').css('color','red');
			}else if(aa==0){
				$('.return_result').css('color','#999');
			}else if(aa<0){
				$('.return_result').css('color','green');
			}
		});
		
	});
	
	initDateFormat();
	 function initDateFormat(){
		var format=$("#dateFormat").val();
		var date=$(".dateFormat").text();
		$(".dateFormat").text(Format(new Date(date),format));
	}
	
	$(".dateFormatSelect").change(function(){
		initDateFormat();
		
	});
	function Format(now,mask)
    {
        var d = now;
        var zeroize = function (value, length)
        {
            if (!length) length = 2;
            value = String(value);
            for (var i = 0, zeros = ''; i < (length - value.length); i++)
            {
                zeros += '0';
            }
            return zeros + value;
        };
     
        return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0)
        {
            switch ($0)
            {
                case 'd': return d.getDate();
                case 'dd': return zeroize(d.getDate());
                case 'ddd': return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
                case 'dddd': return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
                case 'M': return d.getMonth() + 1;
                case 'MM': return zeroize(d.getMonth() + 1);
                case 'MMM': return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
                case 'MMMM': return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
                case 'yy': return String(d.getFullYear()).substr(2);
                case 'yyyy': return d.getFullYear();
                case 'h': return d.getHours() % 12 || 12;
                case 'hh': return zeroize(d.getHours() % 12 || 12);
                case 'H': return d.getHours();
                case 'HH': return zeroize(d.getHours());
                case 'm': return d.getMinutes();
                case 'mm': return zeroize(d.getMinutes());
                case 's': return d.getSeconds();
                case 'ss': return zeroize(d.getSeconds());
                case 'l': return zeroize(d.getMilliseconds(), 3);
                case 'L': var m = d.getMilliseconds();
                    if (m > 99) m = Math.round(m / 10);
                    return zeroize(m);
                case 'tt': return d.getHours() < 12 ? 'am' : 'pm';
                case 'TT': return d.getHours() < 12 ? 'AM' : 'PM';
                case 'Z': return d.toUTCString().match(/[A-Z]+$/);
                // Return quoted strings with the surrounding quotes removed
                default: return $0.substr(1, $0.length - 2);
            }
        });
    };
	
    
    function savePreference(){
    	var params = $("#preferenceSetting").serialize();
		params = decodeURIComponent(params,true);
		$.ajax({
			type:"POST",
	        url: _root_+"/front/member/personal/savePersonPreference.do?datestr="+new Date().getTime(),
	        datatype:"JSON",
	        data: {inputData : params},
	        success: function(response)
	        {   
	            $("#preferenceSetting").attr({'disabled':false});
	            var dataObj = JSON.parse(response);;
	            if(dataObj.result){
	            	layer.msg(langMutilForJs["global.success.save"]);
	            	 $.get(base_root+"/front/site/changLang.do?langFlag="+$("#langCode").val(), function(data, status, Request){
                         if(data != null){
                             window.location.href=window.location.href;
                         }
	            	 });
	               
	            }else{
	            	layer.msg(dataObj.msg);
	            }
	        },
	        error:function()
	        {
	            layer.msg(langMutilForJs["global.failed.save"]+":"+langMutilForJs["error.exceptionThrew"]);
	        }
		})
    }
    
    $("#savePreference").on("click",function(){
    	savePreference();
    })
    
    
    $("#saveIfa").on("click",function(){
    	var params = $("#ifaForm").serialize();
		params = decodeURIComponent(params,true);
		$.ajax({
			type:"POST",
	        url: _root_+"/front/member/personal/savePersonalIfaInfo.do?datestr="+new Date().getTime(),
	        datatype:"JSON",
	        data: {inputData : params},
	        success: function(response)
	        {   
	            $("#saveIfa").attr({'disabled':false});
	            var dataObj = JSON.parse(response);;
	            if(dataObj.result){
	            	layer.msg(langMutilForJs["global.success.save"],function(){
	            	    window.location.href=_root_+"/front/member/personal/info.do?id="+id+"&r="+Math.random();
	            	});
	                
	            }else{
	            	layer.msg(dataObj.msg);
	            }
	        },
	        error:function()
	        {
	            layer.msg(langMutilForJs["global.failed.save"]+":"+langMutilForJs["error.exceptionThrew"]);
	        }
		})
    });
    
    $("#saveInvest").on("click",function(){
    	var params = $("#investForm").serialize();
		params = decodeURIComponent(params,true);
		$.ajax({
			type:"POST",
	        url: _root_+"/front/member/personal/savePersonalInvestInfo.do?datestr="+new Date().getTime(),
	        datatype:"JSON",
	        data: {inputData : params},
	        success: function(response)
	        {   
	            $("#saveInvest").attr({'disabled':false});
	            var dataObj = JSON.parse(response);;
	            if(dataObj.result){
	            	layer.msg(langMutilForJs["global.success.save"],function(){
	            	    window.location.href=_root_+"/front/member/personal/info.do?id="+id+"&r="+Math.random();
	            	});
	                
	            }else{
	            	layer.msg(dataObj.msg);
	            }
	        },
	        error:function()
	        {
	            layer.msg(langMutilForJs["global.failed.save"]+":"+langMutilForJs["error.exceptionThrew"]);
	        }
		})
    });
    
    $(".setting-other-add").on("click",function(){
    	var settingName=$(this).attr("settingName");
    	if(settingName=="languageSpoken"){
    		$("#languageSpokenSetting").show();
    	}else if(settingName=="liveRegion"){
    		$("#liveRegionSetting").show();
    	}else if(settingName=="hobby"){
    		$("#hobbySetting").show();
    	}else if(settingName=="investField"){
    		$("#investmentFieldSetting").show();
    	}else if(settingName=="investStyle"){
    		$("#investmentStyleSetting").show();
    	}
    	//$("#CharacterSetting").show();
    });
    
    $(".setting-other-pen").on("click",function(){
    	var settingName=$(this).attr("settingName");
    	if(settingName=="languageSpoken"){
    		$("#languageSpokenSetting").show();
    	}else if(settingName=="liveRegion"){
    		$("#liveRegionSetting").show();
    	}else if(settingName=="hobby"){
    		$("#hobbySetting").show();
    	}else if(settingName=="investField"){
    		$("#investmentFieldSetting").show();
    	}else if(settingName=="investStyle"){
    		$("#investmentStyleSetting").show();
    	}
    })
    $(".character-choose-list").on("click","li",function(){
    	$(this).parents(".character-setting-rows").find(".character-setting-list").append($(this));
    });
    $(document).on("click",".character-close-ico, .character-button-close",function(){
    	$(".investment-wrap").hide();
    	//$("#CharacterSetting").addClass("investment-hide");
    });
    
    $(".character-setting-list").on("click",".character-list-close",function(){
    	$(this).parents(".character-setting-rows").find(".character-choose-list").append($(this).parent());
    });
    $(".character-setting-add").on("blur",function(){
    	if($(this).val().replace(/(^\s*)|(\s*$)/g, "") !=''){
    		var StrHtml = '<li data-value='+ $(this).val() +'>'+ $(this).val() +' <span class="character-list-close"></span></li>'
        	$(this).parents(".character-setting-rows").find(".character-setting-list").append(StrHtml);
        	$(this).val("");
    	}
    });
    
    $(".character-button-save").on("click",function(){
    	var parent=$(this).parent().parent();
    	var type=$(parent).attr("settingName");
    	var codes="";
        $(parent).find(".character-setting-list li").each(function(){
        	var dataCode=$(this).attr("data-code");
        	if("hobby"==type){
        		if(dataCode==undefined || dataCode==null || dataCode==""){
        			codes+="{"+$(this).attr("data-value")+"},";
        		}else{
        			codes+=$(this).attr("data-code")+",";
        		}
        	}else{
        		codes+=$(this).attr("data-code")+",";
        	}
        	////console.log($(this).attr("data-code")+":"+$(this).attr("data-value"));
        });
        
         if(codes.endsWith(",")){
        	 codes=codes.substring(0,codes.length-1);
         }

         $.ajax({
 			type:"POST",
 	        url: _root_+"/front/member/personal/saveCharacter.do?datestr="+new Date().getTime(),
 	        datatype:"JSON",
 	        data: {type:type,codes:codes},
 	        success: function(response){   
 	            var dataObj = JSON.parse(response);;
 	            if(dataObj.result){
 	            	layer.msg(langMutilForJs["global.success.save"],function(){
 	            	   window.location.href=_root_+"/front/member/personal/info.do?r="+Math.random();
 	            	});
 	                
 	            }else{
 	            	layer.msg(dataObj.msg);
 	            }
 	        },
 	        error:function()
 	        {
 	            layer.msg(langMutilForJs["global.failed.save"]+":"+langMutilForJs["error.exceptionThrew"]);
 	        }
 		})
    });
    initPrivacySetting();
    function initPrivacySetting(){
    	if(_privacySetting!=undefined){
    		$(".setting_eye").each(function(){
    			var field=$(this).attr("field");
    			var eValue=eval('_privacySetting.'+field);  
    			if(eValue==1){
    				$(this).addClass("setting_eyeac");
    				$(this).attr("status","1");
    			}else{
    				$(this).attr("status","0");
    			}
    				
    			
    		})
    	}else{
    		$(".setting_eye").attr("status","0");
    	}
    }
    
    $(".setting_eye").on("click",function(){
    	var _this=$(this);
    	var field=_this.attr("field");
    	var status=_this.attr("status");
    	if(status=="1")
    		status="0";
    	else
    		status="1";
    	$.ajax({
    		type:"post",
    		dataType:"json",
    		url:base_root+"/front/member/personal/savePrivacySetting.do",
    		data:{field:field,status:status},
    		success:function(json){
    			if(json.result){
    				layer.msg("设置成功！");
    				_this.toggleClass("setting_eyeac");
    			}else{
//    				layer.msg("设置失败！");
    				layer.msg(langMutilForJs["global.failed.save"]);
    			}
    		}
    	})
    });
    
    $('#oldpsw').on('blur',function(){
    	var oldPwd = $('#oldpsw').val();
    	if (!oldPwd){$('#oldPwdText').text(propJson['member.personal.oldPwd.input']); return;}
    	$.ajax({
 			type:"POST",
 	        url: _root_+"/front/member/personal/checkOldPwd.do?datestr="+new Date().getTime(),
 	        data: {oldPwd:oldPwd},
 	        success: function(response){  
 	           if(response.flag){
 	        	   var tips = '<span id="checkOldPwdTips" style="float:left;display:block;margin: 8px 10px;"><img src="'+_root_+'/res/images/Hook.png" alt=""></span>'
 	        	   if($('#checkOldPwdTips').length < 1){
 	        		  $('#oldpsw').after(tips);
 	        	   }
 	           }else{
 	        	  if($('#checkOldPwdTips').length > 0){
 	        		 $('#checkOldPwdTips').remove();
 	        	  }
 	        	  $('#oldPwdText').text(response.msg);
 	           }
 	        }
 		});
    });
    $("#oldpsw").focus(function(){
    	if($('#checkOldPwdTips').length > 0){
    		 $('#checkOldPwdTips').remove();
    	}
    	$('#oldPwdText').text('');
	});
    $('#password').on('blur',function(){
    	var password = $('#password').val();
    	if (!password){$('#passwordText').text(propJson['member.personal.password.input']); return;}
    	//var strength = $(".register_strength").find("li");
    	//if(!strength.eq(5).hasClass('green')){$('#passwordText').text(propJson['member.personal.password.input.reEnter.1']);return;}
    	if(checkPassword(password)){
        	$.ajax({
     			type:"POST",
     	        url: _root_+"/front/member/personal/checkOldPwd.do?datestr="+new Date().getTime(),
     	        data: {oldPwd:password},
     	        success: function(response){  
     	           if(response.flag){
     	        	  if($('#checkPasswordTips').length > 0){
      	        		 $('#checkPasswordTips').remove();
      	        	  }
     	        	 $('#passwordText').text(propJson['member.personal.password.input.reEnter.3']);
     	           }else{
     	        	  var tips = '<span id="checkPasswordTips" style="float:left;display:block;margin: 8px 10px;"><img src="'+_root_+'/res/images/Hook.png" alt=""></span>'
    	        	  if($('#checkPasswordTips').length < 1){
    	        		 $('#password').after(tips);
    	        	  }
     	           }
     	        }
     		});
    	}else{
    		$('#passwordText').text(propJson['member.personal.password.input.reEnter.1']);return;
    	}

    });
    $("#password").focus(function(){
    	if($('#checkPasswordTips').length > 0){
    		 $('#checkPasswordTips').remove();
    	}
    	$('#passwordText').text("");
	});
    $('#confirm_password').on('blur',function(){
    	var password = $('#password').val();
    	if (!password){
    		$('#confirmText').text(propJson['member.personal.password.input']);
    		return;
    	}
    	var confirmPassword = $('#confirm_password').val();
    	if (!confirmPassword){$('#confirmText').text(propJson['member.personal.compfirm.input']);return;}
    	
    	if(password==confirmPassword){
    		var tips = '<span id="checkConfirmPasswordTips" style="float:left;display:block;margin: 8px 10px;"><img src="'+_root_+'/res/images/Hook.png" alt=""></span>'
			if($('#checkConfirmPasswordTips').length < 1){
				$('#confirm_password').after(tips);
			}
    	}else{
    		if($('#checkConfirmPasswordTips').length > 0){
				$('#checkConfirmPasswordTips').remove();
			}
    		$('#confirmText').text(propJson['member.personal.password.input.reEnter.2']);
    	}
    });
    $("#confirm_password").focus(function(){
    	if($('#checkConfirmPasswordTips').length > 0){
    		 $('#checkConfirmPasswordTips').remove();
    	}
    	$('#confirmText').text("");
	});   
    
    function checkPassword(s){
        var patrn = /(?=.*[a-z])(?=.*\d)(?=.*[#@!~%^&*_-])[a-z\d#@!~%^&*_-]{7,20}/i;
        if (!patrn.exec(s)) return false;
        return true;
    }
    
});
