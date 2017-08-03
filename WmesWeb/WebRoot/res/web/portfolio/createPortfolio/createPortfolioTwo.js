define(function(require) {
	var $ = require('jquery');
			require('layui');
	$("#portfolio-one").on("click",function(){
		$("#portfolio-show").show();
		$("#portfolio-hide").hide();
	});
	$("#portfolio-two").on("click",function(){
		$("#portfolio-show").hide();
		$("#portfolio-hide").show();
	});
	$(".step-portrait-name").on("click",function(){
		$(this).parents(".step-portrait-name-wrap").toggleClass("step-portrait-name-show");
	});
	
	//riskLevel 下拉
	$('.proposal_xiala').click(function(){
		$('#proposal-choice li').click(function(){
			$('#txtRiskLevel').val($(this).attr('riskLevelText'));
			$('#txtRiskLevel').attr('riskLevel',$(this).val());
		});
		$('#proposal-choice').toggle();
	});
	
	//字体长度控制	
	if($('#txtInvestmentGoal').val().length > 500){
		$('#txtInvestmentGoal').val($('#txtInvestmentGoal').val().substring(0, 500));
		$('#txtInvestmentGoal-inputed').text(500);
		$('#txtInvestmentGoal-left').text(0);
	}else{
		$('#txtInvestmentGoal-inputed').text($('#txtInvestmentGoal').val().length);
		$('#txtInvestmentGoal-left').text(500-$('#txtInvestmentGoal').val().length);
	}
	
	if($('#txtSuitability').val().length > 500){
		$('#txtSuitability').val($('#txtSuitability').val().substring(0, 500));
		$('#txtSuitability-inputed').text(500);
		$('#txtSuitability-left').text(0);
	}else{
		$('#txtSuitability-inputed').text($('#txtSuitability').val().length);
		$('#txtSuitability-left').text(500-$('#txtSuitability').val().length);
	}
	
	if($('#txtReason').val().length > 150){
		$('#txtReason').val($('#txtReason').val().substring(0, 150));
		$('#txtReason-inputed').text(150);
		$('#txtReason-left').text(0);
	}else{
		$('#txtReason-inputed').text($('#txtReason').val().length);
		$('#txtReason-left').text(150-$('#txtReason').val().length);
	}
	
	$('#txtReason,.stepOne-rows-text').on('input',function(){
		var length = $(this).val().length;
		var huiche = $(this).val().split('\n').length-1;
		var maxLength=Number($(this).attr("maxLength"));
		if(length>maxLength){
			$(this).closest('.stepOne-constrains-rows').find(".inputed").text(maxLength);
			$(this).closest('.stepOne-constrains-rows').find(".left").text(0);
			$(this).val(this.val().subString(0,maxLength));
		}else{
			$(this).closest('.stepOne-constrains-rows').find(".inputed").text(length+huiche);
			$(this).closest('.stepOne-constrains-rows').find(".left").text(maxLength-length-huiche);
		};
		if($(this).closest('.stepOne-constrains-rows').find(".left").text()=='-1'){
			$(this).closest('.stepOne-constrains-rows').find(".left").text(0);
			$(this).closest('.stepOne-constrains-rows').find(".inputed").text(length+huiche-1);
		}
	});

	//跳转 step1
	$('#btnNextOne').click(function(){
		var strategyId = $('#hidStrategyId').val();
		//var portfolioId = $('#hidPortfolioId').val();
		var url = '';
		if(typeof(strategyId) != '' && strategyId != ''){
			url = base_root+'/front/portfolio/arena/createPortfolioOne.do?strategyId='+strategyId+'&portfolioId='+urlRequest('portfolioId');
		}else{
			url = base_root+'/front/portfolio/arena/createPortfolioOne.do?portfolioId='+urlRequest('portfolioId');
		}
		window.location.href = url;
	});
	
	//保存按钮
	$('#btnSave').click(function(){
		//var portfolioId = $('#hidPortfolioId').val();
		var portfolioName = $('#txtPortfolioName').val();
		var investmentGoal = $('#txtInvestmentGoal').val();
		var suitability = $('#txtSuitability').val();
		var txtReason = $('#txtReason').val();
		if(!portfolioName){
			layer.msg(props['create.portfolio.step.two.notEmpty.0'], {icon:3});return;
		}
		if(!txtReason){
			layer.msg(props['create.portfolio.step.two.alert.abstract.empty'], {icon:3});return;
		}
		if(!investmentGoal){
			layer.msg(props['create.portfolio.step.two.notEmpty.1'], {icon:3});return;
		}
		if(!suitability){
			layer.msg(props['create.portfolio.step.two.notEmpty.2'], {icon:3});return;
		}
		var mySectorCount = $('.selected-li-mySector li:gt(0)').length;
		if(mySectorCount == 0){
			layer.msg(props['create.portfolio.step.two.notEmpty.3'], {icon:3});return;
		}
		var mySectorCount = $('.selected-li-myAllocation li:gt(0)').length;
		if(mySectorCount == 0){
			layer.msg(props['create.portfolio.step.two.notEmpty.4'], {icon:3});return;
		}
		var result = savePortfolio();
		if(result.flag){
			layer.msg(globalProp['global.success.save'], {icon:2});
			$('#hidPortfolioId').val(result.portfolioId);
			window.location.href = urlUpdateParams(window.location.href, "portfolioId", result.portfolioId);
		}
	});
	
	//SAVE
	function savePortfolio(){
		var portfolioId = urlRequest('portfolioId');
		var portfolioName = $('#txtPortfolioName').val();
		var investmentGoal = $('#txtInvestmentGoal').val();
		var suitability = $('#txtSuitability').val();
		var reason = $('#txtReason').val();
		var geoAllocation = $('#hidGeoAllocation').val();
		var sector = $('#hidSector').val();
		var data = {
			step:2,
			portfolioId:portfolioId,
			portfolioName:encodeURI(portfolioName),
			investmentGoal:encodeURI(investmentGoal),
			suitability:encodeURI(suitability),
			geoAllocation:encodeURI(geoAllocation),
			sector:sector,
			reason:encodeURI(reason)
		};
		var result = ''; 
		var url = base_root+'/front/portfolio/arena/savePortfolioArena.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			async:false,
			url:url,
			data:data,
			success:function(obj){
				result = obj;
			}
		});
		return result;
	}
	//跳转 step3
	$('#btnNextThree').click(function(){
		var portfolioId = $('#hidPortfolioId').val();
		var strategyId = $('#hidStrategyId').val();
		var portfolioName = $('#txtPortfolioName').val();
		var investmentGoal = $('#txtInvestmentGoal').val();
		var suitability = $('#txtSuitability').val();
		if(!portfolioName){
			layer.msg(props['create.portfolio.step.two.notEmpty.0'], {icon:3});return;
		}
		var txtReason = $('#txtReason').val();
		if(!txtReason){
			layer.msg(props['create.portfolio.step.two.alert.abstract.empty'], {icon:3});return;
		}
		if(!investmentGoal){
			layer.msg(props['create.portfolio.step.two.notEmpty.1'], {icon:3});return;
		}
		if(!suitability){
			layer.msg(props['create.portfolio.step.two.notEmpty.2'], {icon:3});return;
		}
		var mySectorCount = $('.selected-li-mySector li:gt(0)').length;
		if(mySectorCount == 0){
			layer.msg(props['create.portfolio.step.two.notEmpty.3'], {icon:3});return;
		}
		var mySectorCount = $('.selected-li-myAllocation li:gt(0)').length;
		if(mySectorCount == 0){
			layer.msg(props['create.portfolio.step.two.notEmpty.4'], {icon:3});return;
		}
		var result = savePortfolio();
		if(result.flag){
			portfolioId = result.portfolioId;
			$('#hidPortfolioId').val(result.portfolioId);
		}
		var url = '';
		if(typeof(strategyId) != '' && strategyId != ''){
			url = base_root+'/front/portfolio/arena/createPortfolioThree.do?strategyId='+strategyId+'&portfolioId='+portfolioId;
		}else{
			url = base_root+'/front/portfolio/arena/createPortfolioThree.do?portfolioId='+portfolioId;
		}
		if(urlRequest('edit') == '1'){
			url = urlUpdateParams(url, 'edit', '1');
		}
		window.location.href = url;
	});
	
	$("#insight-sector-add").on("click",function(){
    	$('#dialog-mySector').addClass('dispaly-active');
    	$('#dialog-mySector').show();
    });
    
    $("#insight-allocation-add").on("click",function(){
    	$('#dialog-myAllocation').addClass('dispaly-active');
    	$('#dialog-myAllocation').show();
    });
    
    $(".character-choose-list").on("click","li",function(){
    	if($(this).parents(".character-setting-rows").find(".character-setting-list>li").length >= 3){
    		layer.msg(props['create.portfolio.step.two.select.selectUpToThree'], {icon : 3});
    	}else{
    		$(this).parents(".character-setting-rows").find(".character-setting-list").append($(this).append('<span class="character-list-close"></span>'));
    	}
    });
    $(".character-setting-list").on("click",".character-list-close",function(){
    	$(this).parents(".character-setting-rows").find(".character-choose-list").append($(this).parent());
    });
    
    $(document).on("click",".wmes-close, .character-button-close",function(){
    	//$(".selectSector").addClass("funds-views-lump");
    	$('#dialog-mySector').removeClass('dispaly-active');
    	$('#dialog-myAllocation').removeClass('dispaly-active');
    	$('#dialog-mySector').hide();
    	$('#dialog-myAllocation').hide();
    });
	 //保存 mySector
    $('.mySector-button-save').click(function(){
    	var sectorTmpStr = '';
    	$.each($('.selected-li-mySector >li'),function(i,n){
			if($(n).attr('id')!=$('.selected-li-mySector >li').last().attr('id')){
				$(this).remove();
			}
		});
    	$('.ul-mySector >li').each(function(i,d){
    		$('.selected-li-mySector >li').eq(0).before('<li title="'+$(this).attr('title')+'" class="setting-other-bn">'+$(this).text()+'</li>');
			var sectorCode = $(this).attr("data-id");
			/*if(typeof(sectorCode)=='undefined'){
				sectorCode = '{'+$(this).data('value')+'}';
			}*/
			//隐藏域值
			sectorTmpStr +=sectorCode+",";
		});
		sectorTmpStr = sectorTmpStr.substring(0,sectorTmpStr.length-1);//去除最后一个逗号；
		$("#hidSector").val(sectorTmpStr);//隐藏域赋值，用户后台数据保存
		$('#dialog-mySector').removeClass('dispaly-active');
		$('#dialog-mySector').hide();
    });
    //保存 myAllocation
    $('.myAllocation-button-save').click(function(){
    	var sectorTmpStr = '';
    	$.each($('.selected-li-myAllocation >li'),function(i,n){
			if($(n).attr('id')!=$('.selected-li-myAllocation >li').last().attr('id')){
				$(this).remove();
			}
		});
    	$('.ul-myAllocation >li').each(function(i,d){
    		$('.selected-li-myAllocation >li').eq(0).before('<li title="'+$(this).attr('title')+'" class="setting-other-bn">'+$(this).text()+'</li>');
    		var sectorCode = $(this).attr("data-id");
    		/*if(typeof(sectorCode)=='undefined'){
    			sectorCode = '{'+$(this).data('value')+'}';
    		}*/
    		//隐藏域值
    		sectorTmpStr +=sectorCode+",";
    	});
    	sectorTmpStr = sectorTmpStr.substring(0,sectorTmpStr.length-1);//去除最后一个逗号；
    	$("#hidGeoAllocation").val(sectorTmpStr);//隐藏域赋值，用户后台数据保存
    	$('#dialog-myAllocation').removeClass('dispaly-active');
    	$('#dialog-myAllocation').hide();
    });
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
    function urlRequest(m) {
        var sValue = location.search.match(new RegExp("[\?\&]" + m + "=([^\&]*)(\&?)", "i"));
        return sValue ? sValue[1] : sValue;
    }
});