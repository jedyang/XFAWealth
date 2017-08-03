

define(function(require) {

    var $ = require('jquery');
    require('layui');
	require('jqueryForm');
	require("swiper");
	require('layui');
	var selector =  require('ifaSelectUser');
	selector.init();
	
	
	var swiper= new Swiper('.order-sapce-wrapper .swiper-container', {
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev'
    });
	
	$(".step-portrait-choose li").on("click",function(){
		$(this).parents(".step-portrait-name-wrap").toggleClass("step-portrait-name-show").find(".step-portrait-name").html($(this).html());
	});
	$(".step-portrait-name").on("click",function(){
		/*$(".order-plan-sapce").toggleClass("order-plan-block");
		swiper.onResize();*/
		if(getUrlParam('memberId')==null || getUrlParam('memberId')==''){
			selector.create(1,'client','memberId','invName',selectFinish);
			$(".selectnamelistbox").show();
		}
	});
	
	function selectFinish(){
		var url=$(".selectnamelistbox").find(".selected_active").find("img").attr("src");
		$(".client-top-portrait").attr("src",url);
		//console.log(url);
	}
	// 下拉
	$(".proposal_xiala").on("click",function(){
		$(this).toggleClass("xiala-show");
	});
	$(".proposal_xiala").on("mouseleave",function(){
		$(this).removeClass("xiala-show");
	});
	$(".proposal_xiala li").on("click",function(e){
		$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("#hidBaseCurrId").val($(this).data('code'));
		$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("#baseCurrId").val($(this).text());
		e.stopPropagation(); 
	});
	
	$("#btnNext").on("click",function(){
		var checkResult=checkData();
		if(!checkResult)
			return;
		var memberId=$("#memberId").val();
    	var name=$("#invName").text();
    	
		var url = base_root+"/front/crm/proposal/createProposalSetTwo.do?memberId="+memberId+"&name="+name+"&datestr=" + new Date().getTime();
		save(url);
		
	});
	
	$("#btnDraft").on("click",function(){
		var checkResult=checkData();
		if(!checkResult)
			return;
		save();
	});
	
	function checkData(){
		var memberId=$("#memberId").val();
		if(memberId==undefined || memberId==""){
			layer.msg(props['create.proposal.step.one.alert.select.customer'],{icon:3});
			return false;
		}
		var proposalName=$(".proposalName").val();
		if(proposalName==undefined || proposalName==""){
			layer.msg(props['create.proposal.step.one.alert.input.proposal.name'],{icon:3});
			return false;
		}
		var initialAmount=$(".initialInvestAmount").val();
		if(initialAmount==undefined || initialAmount==""){
			layer.msg(props['create.proposal.step.one.alert.input.initial.amount.investment'],{icon:3});
			return false;
		}
		initialAmount = initialAmount.replace(/,/g,'');
		if(isNaN(initialAmount)){
			layer.msg(props['create.proposal.step.one.number.limit'],{icon:3});
			return false;
		}else if(initialAmount > 10000000000){
			layer.msg(props['create.proposal.step.one.number.exceeds.limit'],{icon:3});
			return false;
		}else if(initialAmount <= 0){
			layer.msg(props['create.proposal.step.one.number.lower.limit'],{icon:3});
			return false;
		}
		$(".initialInvestAmount").val(initialAmount);
		var currency=$("#baseCurrId").val();
		if(currency==undefined || currency==""){
			layer.msg(props['create.proposal.step.one.alert.select.currency.type'],{icon:3});
			return false;
		}
		return true;
	}
	
	
    $(".order-plan-sapce").on("click",".order-space-rows",function(){
    	var memberId=$(this).attr("memberid");
    	var name=$(this).find(".order-space-name").html();
    	var imgSrc=$(this).find(".order-space-portrait").attr("src");
    	$("#invName").text(name);
    	$("#memberId").val(memberId);
    	$(".step-portrait-img").attr("src",imgSrc);
    	$(".order-plan-sapce").removeClass("order-plan-block");
    })
	 $(".order-sapce-wrapper").on("mouseenter",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").show();
    });
    $(".order-sapce-wrapper").on("mouseleave",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").hide();
    });
	
	function save(nextUrl){
		 var data = $("#frm").serialize();
		//提交表单
		    $.ajax({
		    	type:"POST",
		        url: base_root+"/front/crm/proposal/saveProposalSet.do?status="+status+"&datestr="+Math.random(),
		        data: data,
		        success: function(dataObj)
		        {   
		        //	var dataObj = JSON.parse(response);
		            if(dataObj.result){
		            	var id = dataObj.id;
		            	if (nextUrl != undefined) {
		                       var url=nextUrl+"&id="+id;
		                       if(getUrlParam('edit') == '1'){
									url = urlUpdateParams(url, 'edit', '1');
		                       }
		                       window.location.href=url;
							}else{
								layer.msg(langMutilForJs['global.success.save'],{icon:2})
								var url = window.location.href;
								if(getUrlParam('edit') == '1'){
									url = urlUpdateParams(url, 'edit', '1');
								}
								window.location.href=url;
							}
		            	
		            	/*if ($("#next").val()=="true"){
		            		var newHtml="";
		            		if(isNew=="1"){
		            			newHtml="&new=1"
		            		}
		            		window.location.href=base_root+"/front/strategy/info/addAllocation.do?id="+id+"&r="+Math.random()+newHtml;
		            	}	
		            	else 
		            		window.location.href=base_root+"/front/crm/proposal/createProposalSetOne.do?id="+id;*/
		            }else{
		            	layer.msg(dataObj.msg);
		            }
		        }
		        });
		/*$("#frm").ajaxSubmit({
			url : base_root + "/front/crm/proposal/saveProposalSet.do?status="+status+"&datestr=" + new Date().getTime(),
			iframe : true,
			success : function(data, status) {
				var dataObj = $.parseJSON(data.replace(/<.*?>/ig, ""));
				if (dataObj.result) {
					//alert("成功");
					var id = dataObj.id;
					if (nextUrl != undefined) {
                       var url=nextUrl+"&id="+id;
                       window.location.href=url;
					}else{
						layer.msg("save success!")
						window.location.href=base_root+"/front/crm/proposal/createProposalSetOne.do?id="+id;
					}
				} else {
					$('#contact-form-error').show().fadeOut(10000);
				}
			},
			error : function() {
				$('#contact-form-error').show().fadeOut(10000);
			}
		});*/
	}
	$(".stepOne-rows-text").each(function(){
		  var length=$(this).val().length;
		   var huiche = $(this).val().split('\n').length-1;
		   var max=Number($(this).attr("maxLength"));
		   if(length==max)
			   $(this).val($(this).val().substring(0,max)); 
		   $(this).parent().find(".inputed").text(length+huiche);
		   $(this).parent().find(".left").text(max-length-huiche);
	})
	
	$(".stepOne-rows-text").on("input",function(){
		   var length=$(this).val().length;
		   var huiche = $(this).val().split('\n').length-1;
		   var max=Number($(this).attr("maxLength"));
		   if(length==max)
			   $(this).val($(this).val().substring(0,max)); 
		   $(this).parent().find(".inputed").text(length+huiche);
		   $(this).parent().find(".left").text(max-length-huiche);
	   });
   
   function getUrlParam(name){  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	};
	function urlUpdateParams(url, name, value) {
        var r = url;
        if (r != null && r != 'undefined' && r != "") {
            value = encodeURIComponent(value);
            var reg = new RegExp("(^|)" + name + "=([^&]*)(|$)");
            var tmp = name + "=" + value;
            if (url.match(reg) != null) {
                r = url.replace(eval(reg), tmp);
            }
            else {
                if (url.match("[\?]")) {
                    r = url + "&" + tmp;
                } else {
                    r = url + "?" + tmp;
                }
            }
        }
        return r;
    }
	
	$("#initialInvestAmount").keypress(function(event) { 
		var keyCode = event.which; 
		if($("#initialInvestAmount").val().length >= 10 && keyCode != 8){
			return false;
		}
        if (keyCode == 8 || keyCode == 46 || (keyCode >= 48 && keyCode <=57))  
            return true;  
        else  
            return false;  
    }).focus(function() {  
        this.style.imeMode='disabled';  
    }); 
	$("#initialInvestAmount").on('change',function(){
		var val = $(this).val();
		if(val){
			val = val.replace(/,/g,'');
			if(isNaN(val)){
				layer.msg(props['create.proposal.step.one.number.limit'],{icon:3});
			}
			$(this).val(formatCurrency($(this).val()));
		}
	});
	function formatCurrency(num) {    
	    num = num.toString().replace(/\$|\,/g,'');    
	    if(isNaN(num))    
	    num = "";    
	    var sign = (num == (num = Math.abs(num)));    
	    num = Math.floor(num*100+0.50000000001);    
	    var cents = num%100;    
	    num = Math.floor(num/100).toString();    
	    if(cents<10)    
	    cents = "0" + cents;    
	    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
	    num = num.substring(0,num.length-(4*i+3))+','+    
	    num.substring(num.length-(4*i+3));    
	    return (((sign)?'':'-') + num + '.' + cents);    
	}
});