

define(function(require) {

	var $ = require('jquery');
			require("echarts");
			require('jqueryForm');
			require('layui');
	//姓名切换
	$(".step-portrait-choose li").on("click",function(){
		$(this).parents(".step-portrait-name-wrap").toggleClass("step-portrait-name-show").find(".step-portrait-name").html($(this).html());
	});
	$(".step-portrait-name, .stepOne-constrains-xiala").on("click",function(){
		$(this).parents(".step-portrait-name-wrap").toggleClass("step-portrait-name-show");
	});

	//Template 切换
	$(".stepOne-constrains-xiala").on("click",function(){
		$(this).toggleClass("step-portrait-name-show");
	});
	$(".step-growth-choose li").on("click",function(e){
		$(this).parents(".stepOne-constrains-xiala").toggleClass("step-portrait-name-show").find(".step-growth-value").val($(this).html());
		e.stopPropagation(); 
	});

	require("swiper");
	$(".proposal-charts-wrap").each(function(){
		var swiper = new Swiper($(this), {
	        slidesPerView: 'auto',
	        grabCursor: true,
	        initialSlide: $(this).find(".proposal-chart-active").index(),
	        preventClicks : false,
	        nextButton: '.swiper-button-next',
	        prevButton: '.swiper-button-prev',
	    });
	})
	

	

	$(".create-proposal-charts").each(function(){
		////console.log($(this).attr("data-value"));
		var selfData = eval($(this).attr("data-value"));
			//设置颜色
            for(var item in selfData){
                if( selfData[item].name =="fund" ){
                	selfData[item].name = props['allocation.fund'];
                    selfData[item]['itemStyle'] = {normal:{color:'#fab00a'}}
                }else if(selfData[item].name =="stock"){
                	selfData[item].name = props['allocation.stock'];
                    selfData[item]['itemStyle'] = {normal:{color:'#8f60c2'}}
                }else if( selfData[item].name =="bond" ){
                	selfData[item].name = props['allocation.bond'];
                    selfData[item]['itemStyle'] = {normal:{color:'#a0d54e'}}
                }else if( selfData[item].name =="insure" ){
                	selfData[item].name = props['allocation.insure'];
                    selfData[item]['itemStyle'] = {normal:{color:'#78a288'}}	
                }
            }
			var option = {
			    series: [
			        {
			            type:'pie',
			            label: {
			                normal: {
			                    position: 'inner',
			                    formatter : "{b}\n{d}%"
			                }
			            },
			       
			            data:selfData
			        }
			    ]
			};

		var myChart = echarts.init($(this)[0]);
			myChart.setOption(option,true);
	});


	$(".proposal-chart-rows").on("click",function(){
		$(".proposal-chart-rows").removeClass("porposal-active").end().addClass("porposal-active");
		
	});

	$(".proposal-charts-wrap").on("mouseenter",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").show();
    });
    $(".proposal-charts-wrap").on("mouseleave",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").hide();
    });

    
    //点击事件
    $(".proposal-chart-rows").on("click",function(){
    	$('#portfolio-one').addClass('portfolio-state-active');
    	var background = $(this).closest('.proposal-choose-rows').find('.proposal-choose-title').css('background');
    	$('#portfolio-one').css('background',background);
    	var strategyId=$(this).attr("strategyId");
    	if($(this).hasClass("proposal-chart-active")){
    		$('.proposal-chart-rows').attr('style','border:\'\'');
    		$(this).removeClass("proposal-chart-active");
    		$('#strategyId').val('');
    	}else{
    		$(".proposal-chart-rows").removeClass("proposal-chart-active");
	    	$(this).addClass("proposal-chart-active");
	    	$('.proposal-chart-rows').attr('style','border:\'\'');
	    	background = (background.split(' none'))[0];
	    	$('.proposal-chart-active').attr('style','border:1px solid '+background);
	    	$("#strategyId").val(strategyId);
    	}
    });
    
    /*$(".proposal-chart-rows").on("click",function(){
    	$(".proposal-chart-rows").removeClass("proposal-chart-active")
    	$(this).addClass("proposal-chart-active");
    	
    	var background = $(this).closest('.proposal-choose-rows').find('.proposal-choose-title').css('background');
    	$('.proposal-chart-rows').attr('style','border:\'\'');
    	background = (background.split(' none'))[0];
    	$('.proposal-chart-active').attr('style','border:2px solid '+background);
    	
    	var strategyId=$(this).attr("strategyId");
    	$("#strategyId").val(strategyId);
    });*/
    $("#btnPrevious").on("click",function(){
		var memberId=$("#memberId").val();
		var name=$(".step-portrait-name").text();
    	
		var url = base_root+"/front/crm/proposal/createProposalSetOne.do?memberId="+memberId+"&name="+name+"&datestr=" + new Date().getTime();

		save(url);
    });
    $("#btnNext").on("click",function(){
		var memberId=$("#memberId").val();
    	var name=$(".step-portrait-name").text();
    	
		var url = base_root+"/front/crm/proposal/createProposalSetThree.do?memberId="+memberId+"&name="+name+"&datestr=" + new Date().getTime();

		save(url);
    });
    $("#btnDraft").on("click",function(){
    	save();
    });
    
    function save(nextUrl){
		var status="";
		if(nextUrl!=undefined && nextUrl!="" ){
			status="-2";
		}
		var data=$("#frm").serialize();
		$.ajax({
	    	type:"POST",
	        url: base_root+"/front/crm/proposal/saveProposalTwo.do?status="+status+"&datestr="+Math.random(),
	        data: data,
	        success: function(dataObj)
	        {   
	            if(dataObj.result){
	            	var id = dataObj.id;
	            	if (nextUrl != undefined) {
	                       var url=nextUrl+"&id="+id;
	                       if(getUrlParam('edit') == '1'){
								url = urlUpdateParams(url, 'edit', '1');
							}
	                       window.location.href=url;
						}else{
							layer.msg(langMutilForJs['global.success.save'], {icon:2});
							var url = window.location.href;
							if(getUrlParam('edit') == '1'){
								url = urlUpdateParams(url, 'edit', '1');
							}
							window.location.href = url;
						}

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
						window.location.href=base_root+"/front/crm/proposal/createProposalSetTwo.do?id="+id;
					}
				} else {
					$('#contact-form-error').show().fadeOut(10000);
				}
			},
			error : function() {
				//alert("失败");
				$('#contact-form-error').show().fadeOut(10000);
			}
		});*/
	}
	/*function next(){
		var memberId=$("#memberId").val();
    	var name=$("#invName").text();
    	
		var url = base_root+"/front/crm/proposal/createProposalSetTwo.do?memberId="+memberId+"&name="+name+"&datestr=" + new Date().getTime();
		$('#frm').attr('action',url);
		$('#frm').submit();
	}
	function previous(){
		var memberId=$("#memberId").val();
    	var name=$("#invName").text();
    	
		var url = base_root+"/front/crm/proposal/createProposalSetOne.do?memberId="+memberId+"&name="+name+"&datestr=" + new Date().getTime();
		$('#frm').attr('action',url);
		$('#frm').submit();
	}*/
    
    $('.proposal-charts-title').click(function(event){
		var strategyId = $(this).closest('div').attr('strategyId');
		window.open(base_root + '/front/strategy/info/strategiesdetail.do?id='+strategyId);
		event.stopPropagation();
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
});