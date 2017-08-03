define(function(require) {
	//依赖
	var $ = require('jquery');
	require('layer');
	/*var _chat=require('chat');
	 //_chat.init();
	 _chat.setType(2);
	
	$(".my-buddy-contens").on("click",".my-ifa-liaotian",function(){
		var memberId=$(this).attr("memberId");
		var nickName=$(this).attr("nickName");
		var iconUrl=$(this).parent().find(".new-buddy-portrait").attr("src");
		_chat.load(memberId,nickName,iconUrl);
	})*/
	
	$(".acceptbtn").on("click",function(){
		var id=$(this).attr("relateId");
		// var element=$(this);
	   $.ajax({
		   type:'post',
		   datatype:'json',
		   url:base_root + '/front/ifa/info/dealWithBuddy.do',
		   data:{id:id,status:1},
		   success:function(json){
			   if(json.result){
				  // alert("成功");
				  window.location.href=base_root + '/front/ifa/info/mybuddy.do';
			   }
		   }
	   })
		
	})
	$(".rejectbtn").on("click",function(){
		var id=$(this).attr("relateId");
		$.ajax({
			   type:'post',
			   datatype:'json',
			   url:base_root + '/front/ifa/info/dealWithBuddy.do',
			   data:{id:id,status:2},
			   success:function(json){
				   if(json.result){
					//  layer.msg("");
					   window.location.href=base_root + '/front/ifa/info/mybuddy.do';
				   }
			   }
		   })
	});
	
	
	function bindMemberInfo(memberId){
    	$.ajax({
    		type:"post",
    		datatype:"json",
    		url:base_root+"/front/crm/pipeline/getMemberInfo.do",
    		data:{memberId:memberId},
    		success:function(json){
    			
    			var primarySetting=json.primarySetting;
    			////console.log(primarySetting);
    			
    			var loginCode=json.loginCode;
    			var iconUrl=json.iconUrl;
    			var education=null!=json.education?json.education:"N/A";
    			var email=null!=json.email?json.email:"N/A";
    			var facebookCode=null!=json.facebookCode?json.facebookCode:"N/A";
    			
    			var introduction=null!=json.introduction?json.introduction:"N/A";
    			var employment=null!=json.employment?json.employment:'N/A';
    			var lastLoginDate=null!=json.lastLoginDate?json.lastLoginDate:"N/A";
    			var lineCode=null!=json.lineCode?json.lineCode:"N/A";
    			var mobileCode=json.mobileCode?json.mobileCode:"";
    			var mobileNumber=json.mobileNumber?json.mobileNumber:"N/A";
    			var nationality=null!=json.nationality?json.nationality:"N/A";
    			var occupation=null!=json.occupation?json.occupation:"";
    			var registrationDate=null!=json.registrationDate?json.registrationDate:"N/A";
    			var residence=null!=json.residence?json.residence:"N/A";
    			var webchatCode=null!=json.webchatCode?json.webchatCode:"N/A";
    			var weibo=null!=json.weiboCode?json.weiboCode:"N/A";
    			var linkIn=null!=json.lineCode?json.lineCode:'N/A';
    			var tiwtter=null!=json.twitterCode?json.twitterCode:'N/A';
    			var gender=null!=json.gender?json.gender:"N/A";
    			var dateFormat=json.dateFormat;
    			if(lastLoginDate!='N/A' && lastLoginDate!='')
    				lastLoginDate=formatDate(lastLoginDate,dateFormat);
    			if(registrationDate!='N/A' && registrationDate!='')
    				registrationDate=formatDate(registrationDate,dateFormat);
    			var nickName=null!=json.nickName?json.nickName:loginCode;
    			if(iconUrl==null || iconUrl==""){
    				if(gender=="F"){
    					iconUrl=base_root+"/res/images/head_f.png";
    				}else{
    					iconUrl=base_root+"/res/images/head_m.png";
    				}
    			}else{
    				iconUrl=base_root+iconUrl;
    			}
    			
    			var favoriteInvestmentField=json.favoriteInvestmentField;
    			var hobby=json.hobby;
    			var investmentStyle=json.investmentStyle;
    			var liveRegion=json.liveRegion;
    			var languageSpoken=json.languageSpoken;
    			
    			$(".information-topper-name").text(nickName);
    			$(".registrationDate").text(registrationDate);
    			$(".loginDate").text(lastLoginDate);
    			$(".mobile").text(mobileCode+'-'+mobileNumber);
    			$(".email").text(email);
    			$(".wetchat").text(webchatCode);
    			$(".weibo").text(weibo);
    			$(".facebook").text(facebookCode);
    			$(".twitter").text(tiwtter);
    			$(".linkedin").text(linkIn);
    			$(".residence").text(residence);
    			$(".nationality").text(nationality);
    			$(".education").text(education);
    			$(".occupation").text(occupation);
    			$(".introduction").text(introduction);
    			$(".employment").text(employment);
    			$(".information-plan-portrait").attr("src",iconUrl);
    			
    			var fieldHtml="";
    			var hobbyHtml="";
    			var styleHtml="";
    			var regionHtml="";
    			var spokenHtml="";
    			$.each(favoriteInvestmentField,function(i,n){
    				fieldHtml+='<li class="information-describe-blue">'+n+'</li>';
    			});
    			$.each(hobby,function(i,n){
    				hobbyHtml+='<li class="information-describe-red">'+n+'</li>';
    			});
    			$.each(liveRegion,function(i,n){
    				regionHtml+='<li class="information-describe-red">'+n+'</li>';
    			});
    			$.each(languageSpoken,function(i,n){
    				spokenHtml+='<li class="information-describe-red">'+n+'</li>';
    			});
    			$.each(investmentStyle,function(i,n){
    				styleHtml+='<li class="information-describe-yellow">'+n+'</li>';
    			});
    			$(".style").empty().append(styleHtml);
    			$(".field").empty().append(fieldHtml);
    			$(".hobby").empty().append(hobbyHtml);
    			$(".region").empty().append(regionHtml);
    			$(".spoken").empty().append(spokenHtml);
    			
    			$(".investment-wrap").removeClass("investment-hide");
    		}
    	})
    }
	
	$(".user-head").on("click",function(){
		var memberId=$(this).attr("relateId");
		//bindMemberInfo(memberId);
	});
	
	$(".investment-wrap").on("click",".investment-close-ico",function(){
	    $(".investment-wrap").addClass("investment-hide");
	});
	   
	$(".clientsdetail-tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".clientdetail-contents-wrap").removeClass("clientdetail-contents-active").eq($(this).index()).addClass("clientdetail-contents-active");
	});
	
	$('.initial').on('click',function(){
		//console.log("initialinitialinitialinitialinitialinitialinitial");
		$.ajax({
			type:"post",
			datatype:'json',
			url:base_root+"/front/web/webchat/initUserChatInfo.do",
			success:function(json){
				
			}
		})
	})
	
});