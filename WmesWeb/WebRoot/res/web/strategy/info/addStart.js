/**
 * form.js 
 * @author michael
 * @date: 2016-08-18
 */

define(function(require) {
	
	var $ = require('jquery');
	require("echarts");
	require('layer');
	 require('jqueryRange');
//	var grid_selector = "#tableList";
//	var pageSize = 10;
	var	aaa = [];
	var allocChart = {
		//饼图
		pChart : function(data){
			var	chartOptionData = [];
			var	chartOption = {
				title : {  
                    text: '',  
                    subtext: '',  
                    x:'center'  
                },
                legend: {
			    	orient : 'vertical',
			        x: '80%',
			        y: '20%',
			        data:data
			    },
			    tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },
			    clickable:false,
			    color:aaa,
			    series : [
			        {
			            name: 'Product Allocation',
			            type: 'pie',
			            radius : '70%',
			            center: ['42%', '40%'],
			            data: chartOptionData ,
			            itemStyle: {
			            	normal:{ 
			                    label:{ 
			                      show: true, 
			                      formatter: '{b} : {c} ({d}%)' 
			                    }, 
			                    labelLine :{show:true} 
			                  } ,
			                emphasis: {
			                    shadowBlur: 10,
			                    shadowOffsetX: 0,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            },
			            label: {
			                normal: {
			                    position: 'inner',
			                    formatter : "{d}%"
			                }
			            }
			        }
			    ]
			};
			
			$("#Create_Strategylevel").width($('.Create_Strategylevelright').width());
			var pieChart = echarts.init(document.getElementById('Create_Strategylevel'));
			pieChart.setOption(chartOption);
			if(data!=undefined && data.length>0)
				$("#Create_Strategylevel").show();
			else{
				$("#Create_Strategylevel").hide();
			};
		//	//console.log(chartOption);
			pieChart.setOption({
				series:{
					data : data?data:getAllocationData()
				}
			});
			
		},
		// 公用事件
		maininit : function(){
			allocChart.pChart(getAllocationData());
			/*$("#createlist input[type='text']").on("blur",function(){
				allocChart.pChart(getAllocationData());
			});*/
		},
		init : function(){
			this.pChart();
			this.maininit();
		}
	};
	
	function getAllocationData(){
		var values = [];
    	var weights = getAssetWeight("allocationTable");
    	weights = weights.substring(weights.indexOf(",")+1).split(",");
		var names = getAssetName("allocationTable");
		names = names.substring(names.indexOf(",")+1).split(",");
		var codes=getAssetCodes("allocationTable");
		codes=codes.substring(codes.indexOf(",")+1).split(",");
		var colors=getAssetColor("allocationTable");
		colors=colors.substring(colors.indexOf(",")+1).split(",");
		
		
		var colors;
		var total = 0;
		if (names && names.length>0)
			aaa = [];
			for (var i=0;i<names.length;i++) {
				if(""!=weights[i] && weights[i]!=undefined ){
					var obj = {};
					obj.name = names[i];
					obj.colors=colors[i];
					/*if(codes[i]=='fund'){
						obj.colors = '#f6ac0a';
					}else if(codes[i]=='stock'){
						obj.colors = '#8c5ec2';
					}else{
						obj.colors = '#9dd84e';
					};*/
					obj.value = weights[i];
					total += parseFloat(obj.value);
					values.push(obj);
					aaa.push(obj.colors);
				}
			}
        return values;
	}
   

    //刷新总数
    function initTotal(){
    	var totalPercent=0;
		   $(".createlist").find(".strategy-slider-input").each(function(){
			   var text=$(this).val().replace(/%/,"");
			   $(this).text(text);
			   totalPercent+=parseFloat(text);
		   });
		   $(".createlist .total").text(totalPercent);
    }
    
    
  //获取一组分配方案Code
    function getAssetCodes(tableName){
    	var result="";
    	$("#"+tableName+" .createlistcontent").each(function(){
    		result+=","+$(this).attr("data-name");
    	});
    	//layer.msg(result);
		return result;
    }
    
    //获取一组分配方案
    function getAssetValues(tableName){
    	var result="";
    	$("#"+tableName+" .itemCode").each(function(){
    		if ($(this).val()) {
   				result+=","+$(this).val();
    		}
    	});
    	//layer.msg(result);
		return result;
    }
    //获取一组分配方案权重
    function getAssetWeight(tableName){
    	var result="";
    	$("#"+tableName+" input[type='text']").each(function(){
    		if ($(this).val()) {
   				result+=","+$(this).val().replace(/%/,"");
    		}
    	});
    	//layer.msg(result);
		return result;
    }
    //获取一组分配方案名字
    function getAssetName(tableName){
    	var result="";
    	$("#"+tableName+" span").each(function(){
    		if ($(this).html()) {
   				result+=","+$(this).html();
    		}
    	});
		return result;
    }
    
    function getAssetColor(tableName){
    	//createlistcontent
    	var result="";
    	$("#"+tableName+" .createlistcontent").each(function(){
    		if ($(this).attr("displayColor")) {
   				result+=","+$(this).attr("displayColor");
    		}
    	});
		return result;
    }
    
    function addTrData(){
    	var name = $(".productTypes").val();//code
    	var code=$(".productTypes").attr("typeCode");
    	var displaycolor=$(".productTypes").attr("displaycolor");
    	
    	if (!code){
    		layer.msg("Please select a product type.");
    		return;
    	}
    	//var name = $("#prodAllocation option:selected").text();
    	addTrVal("prodAllocation","allocationTable",code,name,displaycolor);
    };
    
  //添加当前指定值到数据表
    function addTrVal(selName,tableName,val,name,displaycolor){
    	
    	//检测是否已添加
    	var isExist = false;
    	$(".createlist .itemCode").each(function(){
    		if ($(this).val()==val) {
    			isExist = true;
    			return false;
    		}
    	});
    	
    	if (!isExist){
	    	//var tableObj = $("#"+tableName);
	    	var contain=$(".createlist");
	    	$(".createlistcon").show();
	    	
	    	var existsEl=$(".createlistcon").find(".createlistcontent");
	    	var minValue=100;
	    	var maxValue=100;

	    	var row='';
	    	if(existsEl!=undefined  && existsEl.length>0){
	    		$.each(existsEl,function(){
	    			var itemCode=$(this).attr("data-name");
	    			var itemName=$(this).children().first().text();
	    			var selName1=$(this).find(".itemCode").attr("name");
	    			var color=$(this).attr("displaycolor");
	    			row+='<ul class="createlistcontent" data-name="'+itemCode+'" data-value="" displaycolor="'+color+'">'+
					'<li><span>'+itemName+'</span></li>'+
					'<li > <div class="funds-slider-wrap"><input type="hidden" class="funds-single-slider" value=""></div>'+
					'<input class="strategy-slider-input" type="text" value="" readonly="" />%<input type="hidden" class="itemCode" name="'+selName1+'" value="'+itemCode+'">'+
					'<li><img class="delete" src="'+base_root+'/res/images/application/laji1.png" /></li>'+
				'</ul>';
	    			existsEl.remove();
	    		});
	    	}
	    	
	    	
	    	 row+='<ul class="createlistcontent" data-name="'+val+'" data-value="" displaycolor="'+displaycolor+'">'+
				'<li><span>'+name+'</span></li>'+
				'<li > <div class="funds-slider-wrap"><input type="hidden" class="funds-single-slider" value="'+maxValue+','+maxValue+'"></div>'+
				'<input class="strategy-slider-input" type="text" value="'+maxValue+'" readonly="" />%<input type="hidden" class="itemCode" name="Sel_'+selName+'" value="'+val+'">'+
				'<li><img class="delete" src="'+base_root+'/res/images/application/laji1.png" /></li>'+
			'</ul>';
	    	
	    	$(row).insertBefore(".createlistcontentall");
	    	//tableObj.append(row);
	    	//重新绑定图表刷新事件
	    	initSlider();
	    	allocChart.maininit();
			initTotal();
			
    	}
    	
    	
    }
    var hasLoad=false;
    var instanse=null;
	function initSlider(){
    	var rangeInstanse = $('.ranges-init').find('.funds-single-slider'),
    		rangeCount = rangeInstanse.length;
    	var totalAmt =$("#allocationTable").attr("data-temp"); //$('.total').text();
    	if(hasLoad){
    		$('.strategy-slider-input').each(function(i){
    			if(rangeCount==3 && i==2){
    				$(this).val(34);
    			}else{
    				$(this).val(parseInt(100/rangeCount));
    			}
        		
        	});
    		
    	}
    	instanse=rangeInstanse;
    	rangeInstanse.each(function(index){
     		//选择条 功能初始化
     		var self = $(this),
     			_start = 0,
     			_values = [];
     		if(hasLoad){
     			var value=self.parents("li").find(".strategy-slider-input").val();
    			 self.val(value+','+value);
     		}
     		else{
     			if(self.val().indexOf(",")<0){
     				 self.val(self.val()+','+self.val());
     			}
     		}
     			
     		//self.parent().find(".slider-container").remove();
    	 	var _test = self.jRange({
    				from: 0, 
    				to: 100,
    				step: 1, 
    				format: '%s%', 
    				showLabels: false, 
    				showScale: false,
    				isRange:true
    				, ondragstart :function(){
    					_values = self.val().split(',');
    					var totalValue = 0;
    					instanse.each(function(i,n){
							if(self.parents('li').find('.itemCode').val() != $(n).parents('li').find('.itemCode').val()){
								totalValue += Number(($(n).val().split(','))[0]);
							}
			    		});
						totalValue = 100 - totalValue;
						self.jRange('setValue',_values[0]+','+totalValue);
					}, onstatechange : function(){
						_values = self.val().toString().split(',');
						var tempValue = parseInt(_values[0]);
						var weight = 0;
						if(tempValue>_values[1]){
							self.parents('li').find('.strategy-slider-input').val(_values[1]);
							weight = _values[1];
						}else{
							self.parents('li').find('.strategy-slider-input').val(tempValue);
							weight = tempValue;
						}
						calTotal();
						allocChart.maininit();
					}, ondragend : function(){
						_values = self.val().split(',');
						self.jRange('setValue',_values[0]+','+_values[0]);
					
					}
    		});
     	});
    	hasLoad=true;
    }
	
	function calTotal(){
		var total=0;
		$("#allocationTable").find(".strategy-slider-input").each(function(){
			total+=Number($(this).val());
		})
		$(".total").text(total);
	}
    
    
    
    $(".createlist ").on("click",".type_ratio",function(){
    	layer.open({
		   title: 'Product Allocation Setting',
		   type: 1,
		   skin: 'layui-layer-rim', //加上邊框
		   area: ['770px', '380px'], //寬高
		   content: $('#div_setrange').load(base_root+'/front/ifa/info/testslider.do'),
		   end:function(){
			   initTotal();
			   allocChart.maininit();
		   }
		   
});
    })
    
    $(".createadd").on("click",function(){
    	addTrData();
    });
    
    $(".createlist").on("click",".delete",function(){
    	deleteTr(this);
    });
    function deleteTr(nowTr){ 
       //多一个parent就代表向前一个标签, 
       // 本删除范围为<td><tr>两个标签,即向前两个parent 
       //如果多一个parent就会删除整个table 
       $(nowTr).parent().parent().remove(); 
       initTotal();
       allocChart.maininit();
       hasLoad=false;
       initSlider();
       if($(".createlist").find(".createlistcontent").length==0){
    	   $(".createlistcon").hide();
    	   $("#Create_Strategylevel").hide();
       }
    }
    
    function checkData(){
    	
    }
    
	//保存
    $("#btnSave").on("click",function(){
    	//保存草稿
    	$("#next").val("false");
    	saveData();
    });
    var isNew="0";
    if($("#id").val()==undefined || $("#id").val()=="" || getQueryString("new")=="1" ){
		isNew="1";
	}
    $("#btnNext").on("click",function(){
    	//保存并跳到下一步
    	$("#next").val("true");
    	
    	saveData();
    });
    function saveData(){
    	$("#prdAllocations").val(getAssetValues("allocationTable"));
    	$("#prdAllocationsWeight").val(getAssetWeight("allocationTable"));
    	
	    //检查输入合法性
	    var strategyName = $('#strategyName').val();
	    if (!strategyName){layer.msg(langMutilForJs['msg.input']+" "+langMutilForJs['strategy.info.form.strategyName']); return;}

	    var investmentGoal = $('#investmentGoal').val(); 
	    if (!investmentGoal){layer.msg(langMutilForJs['msg.input']+" "+langMutilForJs['strategy.info.form.goal']); return;}

	    var suitability = $('#suitability').val();
	    if (!suitability){layer.msg(langMutilForJs['msg.input']+" "+langMutilForJs['strategy.info.form.investor']); return;}
	    
	    var riskLevel=$("#riskLevel").val();
	    if (!riskLevel){layer.msg(langMutilForJs['msg.select']+" "+langMutilForJs['strategy.info.form.riskLevel']); return;}
	    
	    var reason=$("#reason").val();
	    if (!reason){layer.msg(langMutilForJs['msg.input']+" "+langMutilForJs['strategy.abstract']); return;}
	    
	    if($("#prdAllocations").val()==""){
	    	layer.msg(langMutilForJs['strategy.info.form.allocationTips']);
	    	return;
	    }
	    var total=$(".total").text();
	    if(Number(total)!=100){
	    	layer.msg(langMutilForJs["strategy.info.form.allocation.notenough"]);
	    	return;
	    }

	    var data = $("#addForm").serialize();
	    //提交表单
	    $.ajax({
	    	type:"POST",
	        url: base_root+"/front/strategy/info/saveStart.do?r="+Math.random(),
	        data: data,
	        success: function(response)
	        {   
	            var dataObj = JSON.parse(response);
	            if(dataObj.result){
	            	var id = dataObj.strategyId;
	            	//layer.msg(dataObj.msg);
	            	if ($("#next").val()=="true"){
	            		var newHtml="";
	            		if(isNew=="1"){
	            			newHtml="&new=1"
	            		}
	            		window.location.href=base_root+"/front/strategy/info/addAllocation.do?id="+id+"&r="+Math.random()+newHtml;
	            	}	
	            	else {
	            		layer.msg(langMutilForJs['global.success'], {time:500},function(){
	            			window.location.href=base_root+"/front/strategy/info/addStart.do?id="+id
	            		});
	            		
	            	}
	            		
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
    
    //页面load完后运行
    $("#investmentGoal").blur();
    $("#suitability").blur();
    $("#strategyName").blur();
    $("#reason").blur();
	allocChart.init();
	initTotal();
	initSlider();
	$(".comtent_text").on("input",function(){
		 calInput(this);
	})
	
	$(".comtent_text").each(function(){
		calInput(this);
	})
	
	function calInput(obj){
		var length=$(obj).val().length;
		var huiche = $(obj).val().split('\n').length-1;
		var maxLength=Number($(obj).attr("maxLength"));
		if(length>maxLength){
			$(obj).parent().find(".inputed").text(maxLength);
			
			$(obj).parent().find(".left").text(0);
			$(obj).val(obj.val().subString(0,maxLength));
		}else{
			$(obj).parent().find(".inputed").text(length+huiche);
			$(obj).parent().find(".left").text(maxLength-length-huiche);
		};
		if($(obj).parent().find(".left").text()=='-1'){
			$(obj).parent().find(".left").text(0);
			$(obj).parent().find(".inputed").text(length+huiche-1);
		}
		if($(obj).parent().find(".left").text()==0){
			$(obj).parent().find(".left").css('color','red');
		}else{
			$(obj).parent().find(".left").css('color','#ccc');
		}
	}
	
	
	$("#btnCancle").on("click",function(){
		layer.confirm(langMutilForJs['strategy.info.form.cancel'],function(){
			window.location.href=base_root+"/front/strategy/info/myList.do";
		});
	})
	
	// 下拉
   $(".investor-xiala").on("click",function(){
       $(this).toggleClass("xiala-show");
   });
   
   //点击下拉筛选数据
   $(".pro_xiala li").on("click",function(e){ 
       $(this).parents(".investor-xiala").toggleClass("xiala-show").find("input").val($(this).html());
       $(this).parents(".investor-xiala").find("input").attr("typeCode",$(this).attr("value"));
       $(this).parents(".investor-xiala").find("input").attr("displayColor",$(this).attr("displayColor"));
       e.stopPropagation(); 
   });   
   
   $(".risk_xiala li").on("click",function(e){ 
       $(this).parents(".investor-xiala").toggleClass("xiala-show").find(".riskName").val($(this).html());
      // $(this).parents(".investor-xiala").find("input").attr("typeCode",$(this).attr("value"));
       $("#riskLevel").val($(this).attr("value"));
       e.stopPropagation(); 
   });   
   
   
   $(".investor-xiala").on("mouseleave",function(){
   		if($(this).hasClass('xiala-show')){
   			 $(this).toggleClass("xiala-show");
   		}
       
    });
  	
  	$(".ifa-more-ico").on("click",function(){
  		$(this).parents(".ifa_keyserach_wrap").toggleClass("ifa_keyserach_hidden");
  	});
});