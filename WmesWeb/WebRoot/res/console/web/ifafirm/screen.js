
define(function(require) {
	'use strict';
	var $ = require('jquery');

		require("range");
		require('layer');
		
		var listTime;
		
		$(document).ready(function(){
			//页面第一次加载时选第一个类别
			firstLoad();

			getPerformanceStat();
			setAttributeFilterData();		
//			getFundsCount();

			getQueryCriteria();
			
			setTimeout(function(){
				getFundsTotal();
			}
			,100);
			
		});
		


		
		
		function firstLoad(){
			$(".choose-already").height($(".screener-choose-left").height());
		}
		
		//绑定回报统计的边界数据（最小值，最大值）
		function getPerformanceStat(){	
			$.ajax({
				type : 'get',
				datatype : 'json',
				url : base_root+'/front/ifa/info/fundsreturnstat.do?datestr='+new Date().getTime(),
				data : {	'page':1,'rows':100				
				},
				async : false,
				success : function(json) {
									
					//var divContent = "";
					var list = json.rows;			
					$.each(list, function (index, array) { //遍历json数据列	
						var periodCode = array['period_code'] == null ? "" : array['period_code'];
						var minIncrease = array['min_increase'] == null ? "" : array['min_increase'];
						var maxIncrease = array['max_increase'] == null ? "" : array['max_increase'];
						//alert("period_code:"+period_code+",min_increase:"+min_increase+",max_increase:"+max_increase);
						rangeDraw(periodCode,minIncrease,maxIncrease);
					});							
				}
			})

		}
		
		
		function rangeDraw(periodCode,minValue,maxValue){
			var rangWidth = $(".screener-attributes-show .attributes-draw-wrap").width() - 150 - $(".screener-attributes-show .attributes-leftInput").width() * 2;
			var fromValue = parseFloat(minValue);
			var toValue = parseFloat(maxValue);
			$('.'+periodCode).val(minValue+','+maxValue);
			$('.'+periodCode).jRange({
		        from: fromValue,
		        to: toValue,
		        step: 0.01,
		        width: rangWidth,
		        showLabels: true,
		        isRange : false
		    });	

		}
		
		function rangeDraw2(period,curValue){
			
			var periodCode;
			switch(period){
			case "trailing1Mon":
				periodCode = "fund_return_period_code_1M";
				break;
			case "trailing3Mon":
				periodCode = "fund_return_period_code_3M";
				break;
			case "trailing6Mon":
				periodCode = "fund_return_period_code_6M";
				break;
			case "trailingYTD":
				periodCode = "fund_return_period_code_YTD";
				break;
			case "trailing1Year":
				periodCode = "fund_return_period_code_1Y";
				break;
			case "trailing3Year":
				periodCode = "fund_return_period_code_3Y";
				break;
			case "trailing5Year":
				periodCode = "fund_return_period_code_5Y";
				break;
			case "sinceLaunch":
				periodCode = "fund_return_period_code_LAUNCH";
				break;
			default:
				return;
			}
			
			$('.'+periodCode).jRange('setValue', curValue);					
		}
		

		//rangeDraw();
	    window.onresize = function () {
	    	$(".slider-container").remove();
	    	$(".range-slider").each(function(){
	    		var self =  $(this).clone();
	    		$(this).after(self).remove();
	    	});
//    		rangeDraw();
    		getPerformanceStat();
		}
	    
	    function getFundsTotal(){
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root+'/front/ifa/info/fundstotal.do?datestr='+new Date().getTime(),
				data : {	'page':1,'rows':100000					
				},
				async : true,
				success : function(json) {									
//					var list = json.rows;	
					var total = json.total;
					$('.screener-matches-sum').text(total);
//					$('#matches-number').text(total);
//					alert(total);
					clearTimeout(listTime);
					listTime=setTimeout(function(){
						getFundsCount();
					}
					,10);
				}
			})
	    }
	    
	    function getFundsCount(){
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root+'/front/ifa/info/fundscount.do?datestr='+new Date().getTime(),
				data : {	'page':1,'rows':100000,
					'fundType':$('#fundType').val(),
					'sectorType':$('#sectorType').val(),
					'currency':$('#currency').val(),
					'geoAllocation':$('#geoAllocation').val(),
					'inceptionDate':$('#inceptionDate').val(),
					'riskLevel':$('#riskLevel').val(),
					'fundSize':$('#fundSize').val(),
					'mgtFee':$('#mgtFee').val(),
					'minInitialInv':$('#minInitialInv').val(),
					'fundHouseIds':$('#fundHouseIds').val(),
					'trailingYTD':$('#trailingYTD').val(),
					'trailing1Mon':$('#trailing1Mon').val(),
					'trailing3Mon':$('#trailing3Mon').val(),
					'trailing6Mon':$('#trailing6Mon').val(),
					'trailing1Year':$('#trailing1Year').val(),
					'trailing3Year':$('#trailing3Year').val(),
					'trailing5Year':$('#trailing5Year').val(),
					'sinceLaunch':$('#sinceLaunch').val(),
				},
				async : true,
				success : function(json) {
									
//					var list = json.rows;	
					var total = json.total;	
					$('#matches-number').text(total);
//					alert(total);						
				}
			})
	    }
	    
	    //点击类别加载数据
		$(".screener-condition li").on("click",function(){
			
			$(this).siblings().removeClass("now").end().addClass("now");
			$(".choose-already-bottom .choose-already").removeClass("choose-already-active").eq($(this).index()).addClass("choose-already-active");
			//展现出数据
			// genAllocation(data_key);

			//把全选与取消选择去掉
			$('#btn_clearselectedall').removeClass('now');
			$('#btn_selectedall').removeClass('now');
		});

		//$(".choose-already-word").on("click",function(){
			//$(this).addClass("now");
		//});

		
		
		$(".screener-xiala").on("click",function(){
			$(this).parents(".funds-screnner-rows").toggleClass("screener_narrow");
		});

		//关闭属性选择
		$(".attributes-close-ico").on("click",function(){
			var self = $(this).parents("li").attr("data-criteria");
			$(this).parents("li").removeClass("screener-attributes-show");
			$(".screener-criteria-button").each(function(){
				if( $(this).attr("data-criteria") == self){
					$(this).addClass("screener-block-button").siblings(".screener-hide-button").removeClass("screener-block-button");
				}
			});
			var key = $(this).attr("data-name");
			setAttributeFilter(key,"","");
			
			getFundsCount();
		});
		
		function setAttributeFilter(key,value,text){
					
			switch(key){
			case "inceptionDate":
				$("#inceptionDate").val(value);
				$("#inceptionDateText").val(text);
				break;
			case "minInitialInv":
				$("#minInitialInv").val(value);
				$("#minInitialInvText").val(text);
				break;
			case "mgtFee":
				$("#mgtFee").val(value);
				$("#mgtFeeText").val(text);
				break;
			case "fundSize":
				$("#fundSize").val(value);
				$("#fundSizeText").val(text);
				break;
			case "riskLevel":
				$("#riskLevel").val(value);
				$("#riskLevelText").val(text);
				break;
			case "FundHouse":
				$("#fundHouseIds").val(value);
				$("#fundHouseIdsText").val(text);
				break;
			default:
				$("#"+key).val(value);
				$("#"+key+"Text").val(text);
				break;
			}

		}
		
		//初始化回报率数据
		function setAttributeFilterData(){
			$(".screener-attributes-show").each(function(){
				var criteria = $(this).attr("data-criteria");
				if( "trailingYTD,trailing1Mon,trailing3Mon,trailing6Mon,trailing1Year,trailing1Year,trailing3Year,trailing5Year,sinceLaunch,".indexOf(criteria) > -1 ){
					var fromValue = $("#"+criteria+"_leftInput").val();
					var toValue = $("#"+criteria+"_rightInput").val();
					$("#"+criteria).val(fromValue+","+toValue);
					$("#"+criteria+"Text").val(fromValue+"% - "+toValue+"%");
				}else if("riskLevel" == criteria ){
					
				}
			});
		}
		

		$(".fund-criteria-close").on("click",function(){
			$(this).parents(".screener-add-attributes").hide();
		});
		$("#addCriteria").on("click",function(){
			$(".screener-add-attributes").css("display","inline-block");
		});
		
		//全 选
		$("#btn_selectedall").on("click",function(){
			$(this).addClass("now");
			$(this).siblings().removeClass("now");
			//把全部数据设置为已选
			$('.choose-already-active li').removeClass("unchoose").addClass("now");
			//设置左侧选中个数信息
			genLeftSelectedItemNum();
			
			clearTimeout(listTime);
			listTime=setTimeout(function(){
				getFundsCount();
			}
			,10);
		});
		
		//取消全 选
		$("#btn_clearselectedall").on("click",function(){
			$(this).addClass("now");
			$(this).siblings().removeClass("now");
			//把全部数据设置为非选
			$('.choose-already-active li').removeClass("now").addClass("unchoose");
			//清除左侧选中个数信息
			genLeftSelectedItemNum();
			
			clearTimeout(listTime);
			listTime=setTimeout(function(){
				getFundsCount();
			}
			,10);
		});
		
		
		
		//点击每一个基金项
		$("body").on('click', '.choose-already-word', '', function () { 
			$(this).parent().removeClass("unchoose").addClass("now");
			//把全选与取消选择去掉
			$('#btn_clearselectedall').removeClass('now');
			$('#btn_selectedall').removeClass('now');
			var key = $(this).attr("data-name");
			var value = $(this).attr("data-value");
			var text = $(this).attr("data-text");
			var flag = "add";
			setGeneralFilter(key,flag,value,text);
			
			//获取该面板中所有已选择的基金个数
			genLeftSelectedItemNum();
			
			//总数计算
			clearTimeout(listTime);
			listTime=setTimeout(function(){
				getFundsCount();
			}
			,10);
	    });
		
		
		function setGeneralFilter(key,flag,value,text){
			//alert("key:"+key+",value:"+value);
			var cValue = '';
			switch(key){
			case "currency":
				cValue = appendValue("currency",flag,value);					
				$("#currency").val(cValue);
				cValue = '';
				cValue = appendValue("currencyText",flag,text);					
				$("#currencyText").val(cValue);
				break;
			case "geoAllocation":
				cValue = appendValue("geoAllocation",flag,value);					
				$("#geoAllocation").val(cValue);
				cValue = '';
				cValue = appendValue("geoAllocationText",flag,text);					
				$("#geoAllocationText").val(cValue);
				break;
			case "fundType":
				cValue = appendValue("fundType",flag,value);					
				$("#fundType").val(cValue);
				cValue = '';
				cValue = appendValue("fundTypeText",flag,text);					
				$("#fundTypeText").val(cValue);
				break;
			case "sectorType":
				cValue = appendValue("sectorType",flag,value);					
				$("#sectorType").val(cValue);
				cValue = '';
				cValue = appendValue("sectorTypeText",flag,text);					
				$("#sectorTypeText").val(cValue);
				break;
			case "distributor":
				cValue = appendValue("distributor",flag,value);					
				$("#distributor").val(cValue);
				cValue = '';
				cValue = appendValue("distributorText",flag,text);					
				$("#distributorText").val(cValue);
				break;
				default:
					break;
			}
		}
		
	
		function appendValue(objId,flag,value){
			var returnValue = '';
			var oValue = $("#"+objId).val();
			if(flag == "add"){
				if( (","+oValue+",").indexOf(","+value+",") == -1 )
				{
					if( '' == oValue){
						returnValue = value;
					}else{
						returnValue = oValue+','+value;
					}							
				}
				else
					returnValue = oValue;
			}else if(flag == "remove"){
				var arr = oValue.split(",");
				for(var i=0;i<arr.length;i++ ){
					if(arr[i] == value) { arr.splice(i,1) };
				}
//				console.log(arr.join(","));
				returnValue = arr.join(","); // oValue.replace(value+',','');
			}
			return returnValue;	
		}
				
		
		//点击X号关闭面板
		$("body").on('click', '.choose-already-close', '', function (e) { 
			$(this).parents("li").removeClass("now").addClass("unchoose");
			
			//去掉条件
			var key = $(this).attr("data-name");
			var value = $(this).attr("data-value");
			var text = $(this).attr("data-text");
			var flag = "remove";
			setGeneralFilter(key,flag,value,text);
		
			genLeftSelectedItemNum();

			getFundsCount();
			e.stopPropagation(); 
	    });
		
		
		//设置左侧选中个数信息
		function genLeftSelectedItemNum()
		{	

			//设置左侧选中个数信息
			var selected_text = '';
			var length = $('.choose-already-active .now').length;
			var total = $('.choose-already-active li').length;
			if(length>0){
				selected_text = length + ' of ' + total;
				$('.screener-condition li[class="now"]').find('span').text(selected_text);
				
				$('.choose-already-active .now').each(function(){					
					var key = $(this).children().eq(0).attr("data-name");
					var value = $(this).children().eq(0).attr("data-value");
					var text = $(this).children().eq(0).attr("data-text");
					var flag = "add";
					setGeneralFilter(key,flag,value,text);
//					console.log($(this).children().eq(0).attr("data-value"));
				});
				
			} else{
				$('.screener-condition li[class="now"]').find('span').text('');
				
				$('.choose-already-active .unchoose').each(function(){					
					var key = $(this).children().eq(0).attr("data-name");
					var value = $(this).children().eq(0).attr("data-value");
					var text = $(this).children().eq(0).attr("data-text");
					var flag = "remove";
					setGeneralFilter(key,flag,value,text);
//					console.log($(this).children().eq(0).attr("data-value"));
				});
				
			}
			//设置该类别的基 金总数
			$('.screener-condition li[class="now"]').attr('data_selectednum',length);
		}
		
		
		//数字加逗号
		function splitNumber(number)
		{
			var str=number.split('').reverse().join('').replace(/(\d{3})/g,'$1,').replace(/\,$/,'').split('').reverse().join('');
			return str;
		};
		
		$(".watchlist-view").on("click",function(){
			window.location.href = base_root+"/front/ifa/info/fundslist.do";
		});

		$(".screener-contains-left li").on("click",function(){
			var self = $(this).attr("data-type");
			$(this).siblings().removeClass("screener-contains-active").end().addClass("screener-contains-active");
			$(".screenr-criteria-ul").hide().each(function(){
				if( $(this).attr("data-type") == self){
					$(this).show();
					return false;
				}
			});
		});
		
		//添加Attribute条件
		$(".screener-criteria-button").on("click",function(){
			
			var criteria = $(this).attr("data-criteria");
			$(this).removeClass("screener-block-button").siblings(".screener-hide-button").addClass("screener-block-button");
			$(".screener-attributes-wrap > li").each(function(){
				if( $(this).attr("data-criteria") == criteria){
					$(this).appendTo(".screener-attributes-wrap").addClass("screener-attributes-show");
					
					$("."+criteria +" > li").removeClass("attributes-date-active");
					if( criteria.indexOf("sinceLaunch") > -1 || criteria.indexOf("trailing") > -1 ){
						setReturnData(criteria,"add");
						clearTimeout(listTime);
						listTime=setTimeout(function(){
							getFundsCount();
						}
						,10);
					}
					
					return false;
				}
			});
		});
		
		//移除Attribute条件
		$(".screener-hide-button").on("click",function(){
			var criteria = $(this).attr("data-criteria");
			$(this).removeClass("screener-block-button").siblings(".screener-criteria-button").addClass("screener-block-button");
			$(".screener-attributes-wrap > li").each(function(){
				if( $(this).attr("data-criteria") == criteria){
					$(this).removeClass("screener-attributes-show");
					
					setReturnData(criteria,"remove");
					clearTimeout(listTime);
					listTime=setTimeout(function(){
						getFundsCount();
					}
					,10);
					return false;
				}
			});
		});
		
		//设置回报率的选中条件
		function setReturnData(key,flag){
			var fromValue = "";
			var toValue = "";
			if("add" == flag){
				fromValue = $("#"+key+"_leftInput").val();
				toValue = $("#"+key+"_rightInput").val();
				$("#"+key).val(fromValue+","+toValue);	
			}else{
				$("#"+key).val("");	
			}		

		}

		$(".attributes-house-ul li").on("click",function(){
			$(this).siblings().removeClass("attributes-house-active").end().addClass("attributes-house-active");

			var this_letter		  = $(this).attr("data-letter"),
				funds_logo 		  = $("#logo-choice").children(),
				funds_logo_lenght = funds_logo.length;
			if(this_letter == ""){
				$(".attributes-logo-choice li").removeClass("attributes-logo-active");
				$("#fundHouseIds").val("");
				$("#fundHouseIdsText").val("");
				funds_logo.hide();
				getFundsCount();				
			}else{
				for (var k = 0 ; k< funds_logo_lenght; k++){
					if( this_letter.indexOf(funds_logo.eq(k).attr("data-letter")) > -1 ){
		 				funds_logo.eq(k).show();
					}else{
						funds_logo.eq(k).hide();
					}
				}
			}
		});

		//点击FundHouse
		$(".attributes-logo-choice li").on("click",function(){

			var value = $(this).attr("data-value");		
			var text = $(this).attr("data-text");		
			var flag = "remove";
			if($(this).hasClass("attributes-logo-active") ){
				//remove
				flag = "remove";
			}else{
				//add
				flag = "add";			
			}

			var cValue = appendValue("fundHouseIds",flag,value);				
			$("#fundHouseIds").val(cValue);
			cValue = "";
			cValue = appendValue("fundHouseIdsText",flag,text);				
			$("#fundHouseIdsText").val(cValue);
			
			$(this).toggleClass("attributes-logo-active");
			clearTimeout(listTime);
			listTime=setTimeout(function(){
				getFundsCount();
			}
			,10);
		});

		//点击属性项(inceptionDate,minInitialInv,mgtFee,riskLevel)
		$(".attributes-date-ul li").on("click",function(){
//			console.log("yes");
			$(this).siblings().removeClass("attributes-date-active").end().addClass("attributes-date-active");

			var key = $(this).attr("data-name");
			if("inceptionDate,minInitialInv,mgtFee,fundSize,riskLevel,".indexOf( key )>-1 ){
				var text = $(this).text();	
				var value = $(this).attr("data-value");
				setAttributeFilter(key,value,text);	
			}
			clearTimeout(listTime);
			listTime=setTimeout(function(){
				getFundsCount();
			}
			,10);
		});
		
		$("#viewMyCriteria").on("click",function(){
			$(".my-criteria-list").toggle();
		});
		
		
		$("#resetCriteria").on("click",function(){
			window.location.href = base_root+'/front/ifa/info/fundsscreener.do';
		});
		
		
		var jsonFilterData = "";
		function genFilterJsonData(key){
			if($("#"+key).val()!= ""){
				var label = $("#"+key+"Label").val();
//				var text = $("#"+key+"Text").val();				
				var value = $("#"+key).val();
									
				var splitFlag = "";
				if(jsonFilterData != ""){
					splitFlag = ",";
				}
				jsonFilterData += splitFlag + "{'code':'"+key+"','title':'"+label+"','value':'"+value+"'}";
			}	
		}
		
		/*
		 * 获取Url传递参数值
		 */
		function getUrlParam(name){  
		    //构造一个含有目标参数的正则表达式对象  
		    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
		    //匹配目标参数  
		    var r = window.location.search.substr(1).match(reg);  
		    //返回参数值  
		    if (r!=null) return unescape(r[2]);  
		    return null;  
		}
				
		$("#viewMatches").on("click",function(){
			
			var matchesNum = $("#matches-number").text();
			if(matchesNum == "0" || matchesNum == ""){
				return;
			}
			
			jsonFilterData = "";
			genFilterJsonData("currency");
			genFilterJsonData("geoAllocation");	
			genFilterJsonData("fundType");
			genFilterJsonData("sectorType");
			genFilterJsonData("distributor");
			genFilterJsonData("fundHouseIds");
			genFilterJsonData("fundSize");
			genFilterJsonData("inceptionDate");
			genFilterJsonData("minInitialInv");
			genFilterJsonData("mgtFee");
			genFilterJsonData("riskLevel");	
			genFilterJsonData("sinceLaunch");
			genFilterJsonData("trailingYTD");
			genFilterJsonData("trailing1Mon");
			genFilterJsonData("trailing3Mon");
			genFilterJsonData("trailing6Mon");
			genFilterJsonData("trailing1Year");
			genFilterJsonData("trailing3Year");
			genFilterJsonData("trailing5Year");
			
			if(jsonFilterData != ""){
//				console.log(jsonFilterData);
				jsonFilterData = encodeURIComponent("{'criteria':["+jsonFilterData+"]}");
			}	
					
			var criteriaId = ''; // getUrlParam("criteriaId");
			
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root+'/front/ifa/info/saveQueryCriteria.do?datestr='+new Date().getTime(),
				data : {'criteriaId':criteriaId	,'criteria':jsonFilterData,'type':'1','description':''
				},
				async : false,
				success : function(json) {	
//					console.log(json);
					criteriaId = json.id;
					window.location.href = base_root+'/front/ifa/info/fundslist.do?datestr='+new Date().getTime()+'&criteriaId='+criteriaId;
				}
			})			
			
		});
		
		
		//筛选条件
		function getQueryCriteria(){	
			var criteriaId = getUrlParam('criteriaId');
			if(criteriaId == null || criteriaId == ""){ return;}
			
			$.ajax({
				type : 'get',
				datatype : 'json',
				url : base_root+'/front/ifa/info/queryCriteria.do?datestr='+new Date().getTime(),
				data : {	'criteriaId':criteriaId			
				},
				async : false,
				success : function(json) {		
					var jsonObj = eval("("+json.criteria+")");
					$.each(jsonObj.criteria, function (idx, item) {
		                
		                $("#"+item.code).val(item.value);
		                		                
		                if(",currency,geoAllocation,fundType,sectorType,distributor,".indexOf(item.code)>-1){
		                	var arr = item.value.split(",");
			                
		                	for(var i=0;i<arr.length;i++){
//			                	console.log(arr[i]);
			                	$("p[data-name='"+item.code+"'][data-value='"+arr[i]+"'][class='choose-already-word']").eq(0).parent().removeClass("unchoose").addClass("now");
			                }
		                	
		                	genLeftSelectedItemNum2(item.code,arr.length);
		                }

		                if(",inceptionDate,minInitialInv,mgtFee,fundSize,riskLevel,".indexOf(item.code)>-1){
		                	
	                		$(".screener-attributes-wrap > li").each(function(){
	            				if( $(this).attr("data-criteria") == item.code){
	            					$(this).appendTo(".screener-attributes-wrap").addClass("screener-attributes-show");
	            					
	            					$("."+item.code +" > li").removeClass("attributes-date-active");
	            					$("."+item.code +" > li").filter("[data-value='"+item.value+"']").eq(0).addClass("attributes-date-active");//		            					
	            				}
	            			});
		                	
		                }
		                
		                if(",trailing1Mon,trailing3Mon,trailing6Mon,trailingYTD,trailing1Year,trailing3Year,trailing5Year,sinceLaunch,".indexOf(item.code)>-1){
		                	rangeDraw2(item.code,item.value);
		                }
		                
		            });
				}
			})
		}
		
		//编辑筛选条件时设置左侧选中个数信息
		function genLeftSelectedItemNum2(itemCode,number)
		{	
			var itemIndex = 0;
			switch(itemCode){
			case "currency":
				itemIndex = 0;
				break;
			case "geoAllocation":
				itemIndex = 1;
				break;
			case "fundType":
				itemIndex = 2;
				break;
			case "sectorType":
				itemIndex = 3;
				break;
			case "distributor":
				itemIndex = 4;
				break;
			default:
				return;
			}
			//设置左侧选中个数信息
			var selected_text = '';
			var total = $(".choose-already-bottom .choose-already").eq(itemIndex).children().length;

			if(number>0){
				selected_text = number + ' of ' + total;
				$('.screener-condition li').eq(itemIndex).find('span').text(selected_text);

				//设置该类别的基 金总数
				$('.screener-condition li').eq(itemIndex).attr('data_selectednum',length);
			} 
		}
		
		function saveCriteria(description,criteriaId){
			if(null == criteriaId || "" == criteriaId){
				criteriaId = getUrlParam("criteriaId");
			}
						
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root+'/front/ifa/info/saveQueryCriteria.do?datestr='+new Date().getTime(),
				data : {'criteriaId':criteriaId	,'type':'0','description':description
				},
				async : true,
				success : function(json) {	
//					console.log(json);
					getMyQueryCriteriaList();
					layer.msg("Operation is successful");
				}
			})
		}
		
		function deleteCriteria(criteriaId){
							
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root+'/front/ifa/info/deleteQueryCriteria.do?datestr='+new Date().getTime(),
				data : {'criteriaId':criteriaId
				},
				async : true,
				success : function(json) {	
//					console.log(json);
					getMyQueryCriteriaList();
					layer.msg("Operation is successful");
				}
			})
		}
		
		function getMyQueryCriteriaList(){				
			$.ajax({
				type : 'get',
				datatype : 'json',
				url : base_root+'/front/ifa/info/queryCriteriaList.do?datestr='+new Date().getTime(),
				data : {
				},
				async : true,
				success : function(json) {	
//					console.log(json);
					if(json.flag == true){
						var list = json.rows;
						$("#srenner-criteria-list").empty();
						var liContent = '';
						$.each(list, function (index, array) { //遍历json数据列
//							console.log(array.description);
							liContent += '<li class="srenner-criteria-rows" data-value="'+array.id+'">'
	                                   + '<input data-url="'+base_root+'/front/ifa/info/fundslist.do?criteriaId='+array.id+'"' 
	                                   + 'class="srenner-criteria-input" type="" name="" value="' + array.description + '" readonly="" maxlength="18">'
	                                   + '<span class="screener-list-edit"></span><span class="screener-list-close"></span></li>';
						});
						if(liContent == ''){
							liContent = '<li>no criteria</li>';
						}
						$("#srenner-criteria-list").append(liContent);
					}
//					layer.msg("Operation is successful");				
				}
			})
		}

		getMyQueryCriteriaList();

	$(".funds-screnner-rows").on("click",".screener-list-edit",function(){
		$(this).parents(".srenner-criteria-rows").toggleClass("screener-group-edit");

		if($(this).parents(".srenner-criteria-rows").hasClass("screener-group-edit") ){
			$(this).parents(".srenner-criteria-rows").find(".srenner-criteria-input").removeAttr("readonly");
		}else{
			$(this).parents(".srenner-criteria-rows").find(".srenner-criteria-input").attr("readonly","readonly");

			var criteriaId = $(this).parents(".srenner-criteria-rows").attr("data-value"),
			criteriaName = $(this).parents(".srenner-criteria-rows").find("input").val();
//			console.log(criteriaName+' -- '+ criteriaId);
			saveCriteria(criteriaName,criteriaId);
		}
	});
	$(".funds-screnner-rows").on("click",".srenner-criteria-input",function(){
		if(!$(this).parents(".srenner-criteria-rows").hasClass("screener-group-edit") ){
			 window.location.href= $(this).attr("data-url");
		}
	});
	$(".funds-screnner-rows").on("click",".screener-list-close",function(){
//		$(this).parents(".srenner-criteria-rows").remove();
		var criteriaId = $(this).parents(".srenner-criteria-rows").attr("data-value");
    	layer.confirm('Are you sure you want to delete?', {
			  btn: ['Yes','No'] //按钮
			}, function(){
				layer.closeAll('dialog');
				deleteCriteria(criteriaId);
				$(this).parents(".srenner-criteria-rows").remove();		    			    	
			}, function(){
			  
			});
	});

	$("#j-criteria").on("click",function(){
		$(this).parent().toggleClass("srenner-criteria-show");
	});

	$(document).on("click",function(event){
		//點擊其他地方隐藏元素
		var _criteria = $("#j-criteria").parent();

		if(!_criteria.is(event.target)  && _criteria.has(event.target).length === 0){ 

			_criteria.removeClass("srenner-criteria-show");  

		}

	});
		
});