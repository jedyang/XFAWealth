/**
 * @author tejay zhu
 * @date: 2016-08-23
 */

define(function(require) {
	var $ = require('jquery');
	require("echarts");
	require("layui");
	var selector =  require('ifaSelectUser');
	selector.init();

	$(".strategies_more").on("click",function(){
    	$(this).parents(".strategies_list_wrap").toggleClass("strategies_narrow");
    });
    // $(".strategies_fav_img").click( function () { 
    //     $.post( base_root+'/front/strategy/info/strategyFollow.do' , { id:  $(this).attr('strategyid') , type: "Follow" },
    //        function(data){
    //         // console.info( "** JSON.stringify( data): "+JSON.stringify( data) );
    //        }
    //     );        
    // });	

	var data = [], dataname = [];

	$.each(_allocationJson, function(i, n) {
		data.push({
			value : n.itemWeight,
			name : n.itemName
		});
		dataname.push(n.itemName);
	})

	loadFundList();
	
	methodType();
	function methodType(){
		var type=$(".strategiesdetail_inf_trapezoidnav1active").attr("typeName");
        $.ajax({
        	type:"post",
        	datatype:"json",
        	url:base_root+"/front/strategy/info/strategyAllocationMethodType.do",
        	data:{id:_id_,type:'fund'},
        	success:function(json){
        		if(json.length>0){
        			var html="";
        			var height= 245 / json.length;
        			var lineHeight=(245 / json.length)*0.7;
        			var methodType="";
            		$.each(json,function(i,n){
            			if(i==0)
            				methodType=n.methodType;
            			html+='<div class="strategiesdetail_inf_lc" value="'+Number(i+1)+'" name="'+n.methodType+'" style="height:'+height.toFixed(2)+
            			'px;line-height:'+ lineHeight.toFixed(2)+ 'px;">'+n.methodTypeName+'</div>';
            			
            			});
            		$("#methodType").empty().append(html);
            		bindChart(methodType);
            	
        		}else{
        			$("#methodType").parents(".strategiesdetail_inf_trapezoid").hide();
        		}
        		
        	}
        });
	}

	var trapenzoidarr = [];
	var trapenzoidlongcolor = [];//['#f6ac0a', '#9dd84e', '#8c5ec2']
	var count=0;
	$(".strategiesdetail_inf_trapezoidnav1").each(function() {
		var trapezoid = eval($(this).attr("data-value"));
		var displayColor=$(this).attr("displayColor");
		trapenzoidarr.push(trapezoid);
		trapenzoidlongcolor.push(displayColor);
		count++;
	});
	for(i = 0; i < count; i++) {
		var trapenzoidlong = (parseFloat(trapenzoidarr[i])-1) + '%';
		trapenzoidlongcolor1 = trapenzoidlongcolor[i];
		$(".strategiesdetail_inf_trapezoidnav1").eq(i).css({'width': trapenzoidlong,'background-color': trapenzoidlongcolor1,'border-width':'3px','border-style':'solid','border-color':trapenzoidlongcolor1});
		/*$(".strategiesdetail_inf_trapezoidnav1").on('click',function(){
			$(".strategiesdetail_inf_trapezoidnav1").removeClass('strategiesdetail_inf_trapezoidnav1active');
			$(this).addClass('strategiesdetail_inf_trapezoidnav1active');
			navative();
			amount();
			trapezoid();
			$('.trapezoid_link').css('backgroundColor','#e3534b');
		});*/
		
	};
	
	//右侧饼图
	function trapezoid() {
		var element=$("#strategiesdetail_inf_trapezoidright");
		if(element.length==0)
			return;
		var myChart = echarts.init(document.getElementById('strategiesdetail_inf_trapezoidright'));
		var temp1 = $('#strategiesdetail_inf_trapezoidright').attr('data-value');
		if(""==temp1 || undefined ==temp1)
			return ;
		var temp1 = JSON.parse(temp1);
		option = {
			title: {
				text: temp1.text1,
				x: 'left'
			},
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b} : {c} ({d}%)"
			},
			legend: {
				orient: 'vertical',
				left: '60%',
				top: '30%',
				data: temp1.name
			},
			series: [{
				color: ['#6cca2d', '#d3005d', '#efb000', '#00b2ea', '#5f7ac1'],
				type: 'pie',
				radius: '58%',
				center: ['35%', '55%'],
				data: [{
					value: temp1.value[0],
					name: temp1.name[0]
				}, {
					value: temp1.value[1],
					name: temp1.name[1]
				}, {
					value: temp1.value[2],
					name: temp1.name[2]
				}, {
					value: temp1.value[3],
					name: temp1.name[3]
				}, {
					value: temp1.value[4],
					name: temp1.name[4]
				}],
				label: {
	                normal: {
	                    position: 'inner',
	                    formatter : "{d}%"
	                }
	            },
				itemStyle: {
					emphasis: {
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowColor: 'rgba(0, 0, 0, 0.5)'
					}
				}
			}]
		};
		myChart.setOption(option);
	}
	trapezoid();
	
	$(document).on('click', ".funds_cart1", function() {
		if($(this).css('background') == 'rgba(0, 0, 0, 0) url("'+base_root+'/res/images/icon-herat-1.png") no-repeat scroll 0% 0% / auto padding-box border-box') {
			$(this).css('background', 'url("'+base_root+'/res/images/icon-herat-11.png") no-repeat')
		} else {
			$(this).css('background', 'url("'+base_root+'/res/images/icon-herat-1.png") no-repeat');
		};
	});
	var amounttemp = 5;
	
	////console.log($(".strategiesdetail_inf_trapezoidnav1active").attr('value'));
	function navative(){
		if($(".strategiesdetail_inf_trapezoidnav1active").attr('value')==1){
			$(".strategiesdetail_inf_lc:eq(3)").css('display','none');
			$(".strategiesdetail_inf_lc:eq(4)").css('display','none');
			amounttemp = 3;
		}else if($(".strategiesdetail_inf_trapezoidnav1active").attr('value')==2){
			$(".strategiesdetail_inf_lc:eq(3)").css('display','block');
			$(".strategiesdetail_inf_lc:eq(4)").css('display','block');
			amounttemp = 5;
		}else if($(".strategiesdetail_inf_trapezoidnav1active").attr('value')==3){
			$(".strategiesdetail_inf_lc:eq(3)").css('display','block');
			$(".strategiesdetail_inf_lc:eq(4)").css('display','none');
			amounttemp = 4;
		}
	}
	navative();
	
	
	
	function amount() {
		var amount = 0;
		$(".strategiesdetail_inf_trapezoidboard .strategiesdetail_inf_lc").each(function() {
			amount += 1;
		});
		
/*		amount = amounttemp;
		$('.strategiesdetail_inf_trapezoidboard .strategiesdetail_inf_lc').css({
			'height': 245 / amount,
			'line-height': (245 / amount)*0.7 + 'px'
		});*/
		
		$('.trapezoid_link').css({'top':39+(245 / amount)/2+"px"});
		
	}
	amount();

	$('.strategiesdetail_inf_trapezoidboard').on('click','.strategiesdetail_inf_lc', function() {
		var methodType=$(this).attr("name");
		var amount = 0;
		$(".strategiesdetail_inf_trapezoidboard .strategiesdetail_inf_lc").each(function() {
			amount += 1;
		});
		//amount = amounttemp;
		var tempidx = $(this).attr('value');
		if(tempidx == 1) {
			$('.strategiesdetail_inf_trapezoid_link').css({
				'background': '#e3534b',
				'top': '73px'
			});
			$('#strategiesdetail_inf_trapezoidright').css('border', '1px solid #e3534b');
			/*var data= bindChart(methodType);
    		$('#strategiesdetail_inf_trapezoidright').attr('data-value', data);*/
    		$('.trapezoid_link').css({
				'top':39+(245 / amount)*0.7+(245 / amount)*(tempidx-1)+"px",
				'background':'#e3534b'
			});
			
			//trapezoid();
		} else if(tempidx == 2) {
			$('.strategiesdetail_inf_trapezoid_link').css({
				'background': '#e9b06d',
				'top': '123px'
			});
			$('#strategiesdetail_inf_trapezoidright').css('border', '1px solid #e9b06d');
			/*var data= bindChart(methodType);
    		$('#strategiesdetail_inf_trapezoidright').attr('data-value', data);*/
			$('.trapezoid_link').css({
				'top':39+(245 / amount)*0.7+(245 / amount)*(tempidx-1)+"px",
				'background':'#e9b06d'
			});
			//trapezoid();
		} else if(tempidx == 3) {
			$('.strategiesdetail_inf_trapezoid_link').css({
				'background': '#64c28f',
				'top': '173px'
			});
			$('#strategiesdetail_inf_trapezoidright').css('border', '1px solid #64c28f');
			/*var data= bindChart(methodType);
    		$('#strategiesdetail_inf_trapezoidright').attr('data-value', data);*/
			$('.trapezoid_link').css({
				'top':39+(245 / amount)*0.7+(245 / amount)*(tempidx-1)+"px",
				'background':'#64c28f'
			});
			//trapezoid();
		} else if(tempidx == 4) {
			$('.strategiesdetail_inf_trapezoid_link').css({
				'background': '#679ec9',
				'top': '223px'
			});
			$('#strategiesdetail_inf_trapezoidright').css('border', '1px solid #679ec9');
			/*var data= bindChart(methodType);
    		$('#strategiesdetail_inf_trapezoidright').attr('data-value', data);*/
			$('.trapezoid_link').css({
				'top':39+(245 / amount)*0.7+(245 / amount)*(tempidx-1)+"px",
				'background':'#679ec9'
			});
			//trapezoid();
		} else if(tempidx == 5) {
			$('.strategiesdetail_inf_trapezoid_link').css({
				'background': '#7c6390',
				'top': '273px'
			});
			$('#strategiesdetail_inf_trapezoidright').css('border', '1px solid #7c6390');
			/*var data= bindChart(methodType);
    		$('#strategiesdetail_inf_trapezoidright').attr('data-value', data);*/
			$('.trapezoid_link').css({
				'top':39+(245 / amount)*0.7+(245 / amount)*(tempidx-1)+"px",
				'background':'#7c6390'
			});
			//trapezoid();
		};
		bindChart(methodType);

	});
	
	function bindChart(methodType){
		var type=$(".strategiesdetail_inf_trapezoidnav1active").attr("typeName");
		$.ajax({
			type:"post",
			datatype:"json",
			url:base_root+"/front/strategy/info/strategyAllocation.do",
			data:{type:'fund',id:_id_,methodType:methodType},
			success:function(json){
				var name='';
				var value='';
				if(json.length>0){
					$.each(json,function(i,n){
						name+='"'+n.itemName+'",';
						value+=n.itemWeight+",";
					});
				}
				if(name.length>0){
					name=name.substring(0, name.length-1);
					value=value.substring(0, value.length-1);
				}
				var data='{"name": ['+name+'],"value": ['+value+']}';
				$('#strategiesdetail_inf_trapezoidright').attr('data-value', data);
				trapezoid();
				
			}
		})
	}
	
	function loadFundList() {
		
		var url = base_root + "/front/fund/info/getFundListForSelect.do?id=" +_fundIds;
		url += "&view=true";
		$("#tableList").load(url, null, function(text, status, xmlhttp) {
			// initInnerHeight('mainFrame','mainFrame');
		});

	}
	
	$(".portfolio_fav_img_amend1").on("click",function(){
		var status="";
		var isFollow=$(this).attr("isFollow");
		var _this=$(this);
		if(isFollow=="1"){
			status="Delete";
		}else{
			status="Follow";
		}
		$.ajax({
			type:"post",
			datatype:"json",
			url:base_root+"/front/strategy/info/strategyFollow.do",
			data:{type:status,id:_id_},
			success:function(json){
				if(json.result){
					if(isFollow=="1")
						isFollow="0";
					else
						isFollow="1";
					_this.attr("isFollow",isFollow);
					_this.toggleClass("portfolio_fav_img_amend1active");
					if(isFollow=="0"){
						layer.msg(langMutilForJs['favour.remove']);
					}else{
						layer.msg(langMutilForJs['favour.add']);
					}
				}
				
				
			}
		})
	});
	
	$(".ifa-more-ico").on("click",function(){
		$(this).toggleClass("ifa-more-icoactive");
		$(this).parents('#strategies_list').children().eq(1).toggleClass('ifa-more-ico-hidden');
		$(this).parents('.strategies_inf_word').children().eq(1).toggleClass('ifa-more-ico-hidden');
		$(this).parents('.strategies_inf_word').children().eq(2).toggleClass('ifa-more-ico-hidden');
	});
	
	$(document).on('click','.amend-type',function(){
		$('.view_permissions_settingcon').css('display','block');
	});
	
	$("input:radio[name='permit']").on("click",function(){
	    var permitCheckResult=$("input:radio[name='permit']:checked").val();
	    if (permitCheckResult && permitCheckResult=='3') {
	    	$("#permit_ext").show();
	    }
	    	
	    else $("#permit_ext").hide();
	    $("#permitSetting").val(permitCheckResult);
	    $("#permit").val($(this).val());
    });
    	
	$("input:radio[name='push']").on("click",function(){
	    var pushCheckResult=$("input:radio[name='push']:checked").val();
	    if (pushCheckResult && pushCheckResult=='2'){
	    	$("#push_ext").show();
	    }
	    else $("#push_ext").hide();
	    $("#pushSetting").val(pushCheckResult);
	});
	
	$("#permit_ext .setting").on("click",function(){
        $(this).parents("li").toggleClass("setting_active");
        var type=$(this).attr("permitType");
        if($(this).parents("li").hasClass("setting_active")){
        	$("#permit_"+type).val("1");
        }else{
        	$("#permit_"+type).val("0");
        }
    });
	
	
	 $(".j-permitASetting").on("click",function(){
	    	var type=$(this).attr("type");
	    	selector.create(0,type,type+"Ids",type+"Names");
			$(".selectnamelistbox").show();
	    	
	    });
	function savePermit(){
		if($("#permit2").attr("checked")|| $("#permit2").attr("checked")=="checked"){
    		if (!$("#permit_client").attr("checked") || $("#permit_client").attr("checked")!="checked"){
    	    	$("#permit_clients").val("");
    	    }
    	    if (!$("#permit_prospect").attr("checked") || $("#permit_prospect").attr("checked")!="checked"){
    	    	$("#permit_prospects").val("");
    	    }
    	    if (!$("#permit_buddy").attr("checked") || $("#permit_buddy").attr("checked")!="checked"){
    	    	$("#permit_buddies").val("");
    	    }
    	    if (!$("#permit_colleague").attr("checked") || $("#permit_colleague").attr("checked")!="checked"){
    	    	$("#permit_colleagues").val("");
    	    }
    	}else{
    		$("#permit_clients").val("");
    		$("#permit_prospects").val("");
    		$("#permit_buddies").val("");
    		$("#permit_colleagues").val("");
    	}
		if($("#existingIds").val().length>0)
    		$("#permit_client").val(-1);
    	if($("#prospectIds").val().length>0)
    		$("#permit_prospect").val(-1);
    	if($("#buddyIds").val().length>0)
    		$("#permit_buddie").val(-1);
    	if($("#colleagueIds").val().length>0)
    		$("#permit_colleague").val(-1);
    	
    	var data = $("#addForm").serialize();
	    //layer.msg(data);
	    //提交表单
	    $.ajax({
	    	type:"POST",
	        url: base_root+"/front/strategy/info/saveRelease.do?datestr="+new Date().getTime(),
	        data: data,
	        success: function(response)
	        {   
	            var dataObj = JSON.parse(response);;
	            if(dataObj.result){
//	            	layer.msg("Password changed successfully.");
	            	var id = $("#id").val();
	            	layer.msg(dataObj.msg);
	            	window.location.href=window.location.href;
	            	/*if ($("#ifPublish").val()=="true")
	            		window.location.href=base_root+"/front/strategy/info/myList.do?r="+Math.random();
	            	else 
	            		window.location.href=base_root+"/front/strategy/info/addRelease.do?id="+id+"&r="+Math.random();*/
	            }else{
	            	layer.msg(dataObj.msg);
	            }
	        },
	        error:function()
	        {
	            layer.msg("error found while saving data.");
	        }
	    });    	
		
	}
	
	$("#btnSave").on('click',function(){
		savePermit();
	})
	
	$(".setting-close").on('click',function(){
		$(".view_permissions_settingcon").hide();
	})
	
	$("#btnCancle").on('click',function(){
		$(".view_permissions_settingcon").hide();
	})
});


