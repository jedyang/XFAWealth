/**
 * form.js 
 * @author michael
 * @date: 2016-08-18
 */

define(function(require) {
	
	var $ = require('jquery');
	require('layer');
	require("echarts");
	 require('jqueryRange');
//	var grid_selector = "#tableList";
//	var pageSize = 10;
	var global_level = 1;//初始化
	

	//定义配置对象类
	(function ($) {
	    $.AllocationObject = function (arg) {
	    	// var id = arg.id;//本对象id
	    	var name = arg.name;//这个是私有变量，外部无法访问
	    	var parentDiv = arg.parentDiv;//父div容器
	    	var parentObj = $("#"+parentDiv);
	    	var strategyId = arg.strategyId;//策略id
	    	var level = arg.level;//配置层
	    	var method = arg.method;//allocation method
	    	var selectionName = arg.selectionName;//下拉选择对象名
	    	var tableName = arg.tableName;//配置表名
	    	
	    	// var chartName = arg.chartName;//配置饼图名
	    	var htmlStr = "";
	        
	        this.create = function(){
	        	//创建页面内容
	        	htmlStr+='<div class="Create_Strategylevel" id="id_alloc_'+method+'">'+
				    '<ul class="Create_Strategylevel_title">'+
					'<li>'+langMutilForJs["strategy.info.allocation.level"]+'<input type="text" style="border:0px;background-color:#d2e8ff;width:30px;" class="allocLevel" name="allocLevel_'+method+'" value="'+level+'"></li>'+
					'<li>'+langMutilForJs["strategy.info.allocation.allocationMethod"]+'：<a herf="">'+name+'</a></li>'+
					'<li><span class="ifa-more-ico"></span></li>'+
					'<li><img class="delAllocation" data-name="'+name+'" data-value="'+method+'"  src="'+base_root+'/res/images/application/laji.png" /></li>'+
					'</ul>'+
					'<div class="Create_Strategylevel_content">'+
					'<div class="Create_Strategylevelright">'+
					'<div class="createAddcon">';
				htmlStr += getSelObjFromArr(method,selectionName);
	        	htmlStr+='<div class="createadd" id="btnAdd'+tableName+'">'+langMutilForJs["global.add"]+'</div>'+
					'</div>'+
					'<div class="createlistcon" style="display:none;">'+
					'<ul class="createlisttitle">'+
					'<li>'+langMutilForJs["strategy.info.allocation.item"]+'</li>'+
					'<li>'+langMutilForJs["strategy.info.form.weight"]+'</li>'+
					'</ul>'+
					'<div class="createlist"  id="'+tableName+'" data-temp="100" >'+
					
					'<ul class="createlistcontentall"  id="total_'+tableName+'" data-name="All" data-value="100">'+
					'<li>'+langMutilForJs["global.all"]+'</li>'+
					'<li><span class="total_value"></span>%</li>'+
					'<li></li>'+
					'</ul>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'<div class="Create_Strategylevelleft">'+
					'<div id="chart_'+tableName+'" style="height: 362px;display:none;" ></div>'+
					'</div>'+
				    '</div>'+
			        '</div>';
	        	parentObj.append(htmlStr);
	        	
	        	//设置已选项
//	        	setSelectedVals(method,strategyId);
	        	setTimeout(setSelectedVals(selectionName,tableName,method,strategyId),1000);
	        	
	        	//定义页面对象
		    	var _self = $("#id_alloc_"+method);
		    	var _thisSetting = $("#setting_"+tableName);

	        	//绑定页面事件
		        $("#btnAdd"+tableName).bind("click", function(){
		        	addTrData();
		        	//刷新选择项
		        	refreshFundList();
		        });
		        
		       
		        $("#"+tableName).on("click",".delete",function(){
		        	var el=this;
		        	layer.confirm(langMutilForJs['strategy.info.form.delItem'],function(){
		        	deleteTr(el);
		        	//刷新选择项
		        	refreshFundList();
		        	calTotal(tableName);
		        	assetChart(tableName);
		        	layer.closeAll();
		        	})
		        });
		        $("#"+tableName).on("keyup",".slider_input",function(){
		        	var value=$(this).val();
		        	if(!isNaN(value)){
		        		$(this).parent().parent().attr("data-value",value);
		        		calTotal(tableName);
			        	assetChart(tableName);
		        	}
		        	
		        });
		        
		        $(_self).on("click",".delAllocation",function(){
		        	//把删除的配置类型添加回选项中
		        	var name = $(this).attr("data-name");
		        	var val = $(this).attr("data-value");
		        	$("#methodSelect").append("<li value='"+val+"'>"+name+"</li>");
		        	 $(".addnextlevel").show();

		        	//调整删除后还存在的配置的level值
		        	var levelObj = parseInt($(_self).find(".allocLevel").val());
//		        	layer.msg("levelObj="+levelObj);
		        	// var me = $(this);
		        	$(".allocLevel").each(function(){
//		        		layer.msg($(this).val());
		        		if (parseInt($(this).val())>levelObj) $(this).val(parseInt($(this).val())-1);
		        	})
		        	
		        	//删除当前配置类型
		        	$(_self).remove();
		        	global_level --;//公共变量
//		        	//console.log("#id_alloc_"+method);
		        });
		        
		        $(_self).on("click",".ifa-more-ico",function(){
		        	$(this).toggleClass("ifa-more-icoactive");
		    		$(this).parents('.Create_Strategylevel').find('.Create_Strategylevel_content').toggleClass('ifa-more-ico-hidden');
		        });
		    
	        };
	        
	        function addTrData(){
	        	
	        	var val = $("#"+selectionName).find(".itemCode").val();//code
//	        	//console.log($("#"+selectionName+" option:selected"));
//	        	layer.msg(val);
	        	if (!val){
	        		layer.msg(langMutilForJs['strategy.info.tips.selectOption']);
	        		return;
	        	}
	        	var name = $("#"+selectionName).find(".itemName").val();
	        	addTrVal(selectionName,tableName,val,name);
	        };
	        
	        this.remove = function(){
	        	$("#setting_"+tableName).html("");
	        };
	        
	        this.hide = function(){
	        	$("#setting_"+tableName).hide();
	        };
	        
	        this.show = function(){
	        	$("#setting_"+tableName).show();
	        };
	    };
	})($);
	
	function calTotal(tableName){
		var total = 0;
		$("#" + tableName + " .strategy-slider-input").each(function() {
			total += parseFloat($(this).val());

		});
		 $("#total_"+tableName+" .total_value").text(total);
	}

	
    //定义textarea输入控制 --start
	//分配类型点击
	$(".select_header_option li").on("click",function(){
		$(".select_header_option li").removeClass("select_active")
		$(this).addClass("select_active");
		var current = $(this).attr("currentSort");
		$("#allocationMethod").val(current);

		//切换配置
		if (current=="Geographical"){
			$("#allocationTableDiv").show();
			$("#sectorTableDiv").hide();
			$("#fundTypeTableDiv").hide();
		}else if (current=="Sector"){
			$("#allocationTableDiv").hide();
			$("#sectorTableDiv").show();
			$("#fundTypeTableDiv").hide();
		}else if (current=="FundType"){
			$("#allocationTableDiv").hide();
			$("#sectorTableDiv").hide();
			$("#fundTypeTableDiv").show();
		}
	});
	
	//添加行 
	/*
    $("#btnAddGeo").on("click",function(){
    	addTr("geoAllocation","allocationTable");
    });
    $("#btnAddSector").on("click",function(){
    	addTr("sectorAllocation","sectorTable");
    });
    $("#btnAddFT").on("click",function(){
    	addTr("ftAllocation","fundTypeTable");
    });*/
    $(".addAlo").on("click",function(){
    	if ($(this).attr("id")=="btnAddGeo")
    		addTr("geoAllocation","allocationTable");
    	else if ($(this).attr("id")=="btnAddSector")
    		addTr("sectorAllocation","sectorTable");
    	else if ($(this).attr("id")=="btnAddFT")
    		addTr("ftAllocation","fundTypeTable");
    	//刷新选择项
    	refreshFundList();
    });
    //添加当前选择的值到数据表
    function addTr(selName,tableName){
    	var val = $("#"+selName).val();//code
    	if (!val){
    		layer.msg(langMutilForJs['strategy.info.tips.selectOption']);
    		return;
    	}
    	var name = $("#"+selName+" option:selected").text();
    	addTrVal(selName,tableName,val,name);
    }
    //添加当前指定值到数据表
    function addTrVal(selName,tableName,val,name){
    	
    	//检测是否已添加
    	var isExist = false;
    	$("#"+tableName+" input[type='hidden']").each(function(){
    		if ($(this).val()==val) {
    			isExist = true;
    			layer.msg(langMutilForJs['strategy.info.form.addItem']);
    			return false;
    		}
    	});
    	
    	if (!isExist){
	    	var tableObj = $("#"+tableName);
	    	var existsEl=tableObj.find(".createlistcontent");
	    	var minValue=100;
	    	var maxValue=100;
	    	
	    	var row='';
	    	if(existsEl!=undefined  && existsEl.length>0){
	    		$.each(existsEl,function(){
	    			var itemCode=$(this).find(".itemCode").val();
	    			var itemName=$(this).find(".strategy-slider-input").attr("fundname");
	    			var selName1=$(this).find(".itemCode").val();
	    			row+='<ul class="createlistcontent" data-name="'+itemName+'" data-value="">'+
					'<li><span>'+itemName+'</span></li>'+
					'<li > <div class="funds-slider-wrap"><input type="hidden" class="funds-single-slider " value=""></div>'+
					'<input class="strategy-slider-input slider_'+tableName+'" type="text" fundname="'+itemName+'" value="0" readonly="" />%<input type="hidden" class="itemCode" name="'+selName1+'" value="'+itemCode+'">'+
					'<li><img class="delete" src="'+base_root+'/res/images/application/laji1.png" /></li>'+
				'</ul>';
	    			$(this).remove();
	    		});
	    	}
	    	
	    	row+='<ul class="createlistcontent" data-name="'+name+'" data-value="">'+
			'<li><span>'+name+'</span></li>'+
			'<li > <div class="funds-slider-wrap"><input type="hidden" class="funds-single-slider " value=""></div>'+
			'<input class="strategy-slider-input slider_'+tableName+'" type="text" fundname="'+name+'" value="0" readonly="" />%<input type="hidden" class="itemCode" name="'+selName+'" value="'+val +'">'+
			'<li><img class="delete" src="'+base_root+'/res/images/application/laji1.png" /></li>'+
		  '</ul>';
	    	/*var row='<ul class="createlistcontent" data-name="'+name+'" data-value="0">'+
					'<li>'+name+'</li>'+
					'<li><input type="text" value=0 style="width:10%;" class="slider_input slider_'+tableName+'" fundname="'+name+'"/>%<input type="hidden" name="Sel_'+selName+'" value="'+val+'"> </li>'+
					'<li><a class="delete"  href="javascrpit:;" itemId=""><img src="'+base_root+'/res/images/application/laji1.png" /></a></li>'+
					'</ul>';*/
	    	

	    	$(row).insertBefore("#total_"+tableName);
	    	//tableObj.append(row);
	    	 
	    	 tableObj.parent().show();
	    	 $("#chart_"+tableName).show();
	    	
	    	 initSlider(tableName);
	    	 assetChart(tableName);
	    	 calTotal(tableName);
    	}
    }
    //添加当前指定值到数据表
    function addSelValToTable(selName,tableName,id,code,name,weight){
    	
    	//检测是否已添加
    	var isExist = false;
    	$("#"+tableName+" input[type='hidden']").each(function(){
    		if ($(this).val()==code) {
    			isExist = true;
    			//layer.msg("It's exist.");
//    			return false;
    		}
    	});

    	if (!isExist){
	    	var tableObj = $("#"+tableName);
	    	
	    	var existsEl=tableObj.find(".createlistcontent");
	    	var minValue=100;
	    	var maxValue=100;
	    	
	    	var row='';
	    	/*if(existsEl!=undefined  && existsEl.length>0){
	    		$.each(existsEl,function(){
	    			var itemCode=$(this).attr("data-name");
	    			var itemName=$(this).children().first().text();
	    			var selName1=$(this).find(".itemCode").attr("name");
	    			row+='<ul class="createlistcontent" data-name="'+itemCode+'" data-value="">'+
					'<li><span>'+itemName+'</span></li>'+
					'<li > <div class="funds-slider-wrap"><input type="hidden" class="funds-single-slider " value=""></div>'+
					'<input class="strategy-slider-input slider_'+tableName+'" type="text" fundname="'+name+'" value="'+weight+'" readonly="" />%<input type="hidden" class="itemCode" name="'+selName1+'" value="'+itemCode+'">'+
					'<li><img class="delete" src="'+base_root+'/res/images/application/laji1.png" /></li>'+
				'</ul>';
	    			existsEl.remove();
	    		});
	    	}*/
	    	
	    	row='<ul class="createlistcontent" data-name="'+name+'" data-value="">'+
			'<li><span>'+name+'</span></li>'+
			'<li > <div class="funds-slider-wrap"><input type="hidden" class="funds-single-slider " value="'+weight+'"></div>'+
			'<input class="strategy-slider-input slider_'+tableName+'" type="text" fundname="'+name+'" value="'+weight+'" readonly="" />%<input type="hidden" class="itemCode" name="'+selName+'" value="'+code+'">'+
			'<li><img class="delete" src="'+base_root+'/res/images/application/laji1.png" /></li>'+
		  '</ul>';
	    	/*var row='<ul class="createlistcontent" data-name="'+name+'" data-value="'+weight+'">'+
			'<li>'+name+'</li>'+
			'<li><input type="text"  style="width:10%;" class="slider_input slider_'+tableName+'" fundname="'+name+'" value='+weight+' >%<input type="hidden" name="Sel_'+selName+'" value="'+code+'"> </li>'+
			'<li><a class="delete"  href="javascrpit:;" itemId=""><img src="'+base_root+'/res/images/application/laji1.png" /></a></li>'+
			'</ul>';*/
	    	
	    	$(row).insertBefore("#total_"+tableName);
	    	tableObj.parent().show();
	    	$("#chart_"+tableName).show();
	    	// assetChart(tableName);
	    	//initSlider(tableName);
    	}
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
    	$("#"+tableName+" .strategy-slider-input").each(function(){
    		if ($(this).val()) {
   				result+=","+$(this).val();
    		}
    	});
    	//layer.msg(result);
		return result;
    }

    /**
     * 删除当前区域 
     * */
    /*
    $("#allocationTable").on("click",".delete",function(){
    	deleteTr(this);
    });
    $("#sectorTable").on("click",".delete",function(){
    	deleteTr(this);
    });
    $("#fundTypeTable").on("click",".delete",function(){
    	deleteTr(this);
    });*/
    $(".dataTable").on("click",".delete",function(){
    	layer.confirm(langMutilForJs['strategy.info.form.delItem'],function(){
    		deleteTr(this);
        	//刷新选择项
        	refreshFundList();
    	})
    	
    });
    function deleteTr(nowTr){ 
       //多一个parent就代表向前一个标签, 
       // 本删除范围为<td><tr>两个标签,即向前两个parent 
       //如果多一个parent就会删除整个table 
       $(nowTr).parent().parent().remove(); 
    }
    
    //定义用于添加基金产品 --start
    var _ids_ = $('#ids').val();
    $(".funds_keyword_xiala_search").on("click","li",function(){
    	$('.funds_keyword_input').val($(this).html());
    	
		if (_ids_.length>0)
			_ids_ += ","+$(this).attr("data-id");
		else
			_ids_ = $(this).attr("data-id");
		$('#ids').val(_ids_);
		loadFundList();
    });
    function loadFundList(){
		var url = base_root+"/front/fund/info/getFundListForSelect.do?id="+$('#ids').val()+"&fav=n";

  		$("#tableList").load(url, null,
  		 	function(text, status, xmlhttp){
//      		 	  initInnerHeight('mainFrame','mainFrame');
  			}
  		);
    }
    
    $("#btnAdd").on("click",function(){
    	var url=base_root+"/front/fund/info/fundSelector.do";
    	openResWin(url,500,600,"fundSelector");
    });
    //用于在弹出窗口刷新本页面
    $("#popupWinReturn").on("click",function(){
    	loadFundList();
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
	   //var winid = parseInt(Math.random()*10000000000)+"";
	   window.open(url,id,"height="+height+", width="+width+", top="+top+", left="+left+",location=no,menubar=no,toolbar=no,status=no,resizable=no,scrollbars=yes");   
	}
    //定义用于添加基金产品 -- end
	
	//保存
    $("#btnSave").on("click",function(){
    	//保存草稿
    	$("#next").val("false");
    	saveData();
    });
    var isNew="0";
    
    $("#btnNext").on("click",function(){
    	//保存并跳到下一步
    	$("#next").val("true");
    	if(getQueryString("new")=="1" ){
    		isNew="1";
    	}
    	saveData();
    });
    function saveData(){
	    //检查输入合法性
    	
    	
    	var region=getAssetValues("allocationTable");
    	var sectors=getAssetValues("sectorTable");
    	var types=getAssetValues("fundTypeTable");
    	
    	if((region=="" || region==undefined) && (sectors=="" || sectors==undefined) && (types=="" || types==undefined) ){
    		layer.msg(langMutilForJs['strategy.info.form.allocationTips']);
    		return;
    	}
    	
    	$(".Create_Strategylevel").find(".allocLevel").each(function(i,n){
    		if($(this).attr("name").indexOf("type")>0){
    			$("#typesLevel").val($(this).val());
    		}else if($(this).attr("name").indexOf("region")>0){
    			$("#regionsLevel").val($(this).val());
    		}else if($(this).attr("name").indexOf("sector")>0){
    			$("#sectorsLevel").val($(this).val());
    		}
    	})
    	
    	$("#regions").val(region);
    	$("#sectors").val(sectors);
    	$("#types").val(types);
    	$("#regionsWeight").val(getAssetWeight("allocationTable"));
    	$("#sectorsWeight").val(getAssetWeight("sectorTable"));
    	$("#typesWeight").val(getAssetWeight("fundTypeTable"));
	    var data = $("#addForm").serialize();
	    //layer.msg(data);
	    //提交表单
	    $.ajax({
	    	type:"POST",
	        url: base_root+"/front/strategy/info/saveAllocation.do?r="+Math.random(),
	        data: data,
	        success: function(response)
	        {   
	            var dataObj = JSON.parse(response);;
	            if(dataObj.result){
//	            	layer.msg("Password changed successfully.");
	            	var id = $("#id").val();
	            	layer.msg(langMutilForJs['global.success.save']);
	            	if ($("#next").val()=="true"){
	            		var newHtml="";
	            		if(isNew=="1"){
	            			newHtml="&new=1"
	            		}
	            		window.location.href=base_root+"/front/strategy/info/addRelease.do?id="+id+"&r="+Math.random()+newHtml;
	            	}
	            	else 
	            		window.location.href=base_root+"/front/strategy/info/addAllocation.do?id="+id
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

    $("#quickAdd").on("click",function(){
    	var regions = $("#regions").val();//"Region_Asia_01,Region_China,Region_BRIC";//$("#regions").val()
    	var sectors =$("#sectors").val();// "fund_sector_02,fund_sector_03,fund_sector_04,fund_sector_07";//$("#sectors").val()
    	var url=base_root+"/front/fund/info/fundSelectorForAllocation.do?regions="+regions+"&sectors="+sectors;
    	openResWin(url,925,600,"fundSelectorForAllocation");
    });

    $("#screenAdd").on("click",function(){
    	var url=base_root+"/front/fund/info/list.do?select=true";
    	openResWin(url,925,600,"fundSelectorScreen");
    });

    function loadAllocationTable(selName,tableName,vals){
    	if (!vals) return;
    	//layer.msg(vals);
    	$("#"+selName).children("option").each(function(){
    		if(vals.indexOf($(this).val()) >= 0 && $(this).val() && $(this).val().length>1){
    				addTrVal(selName,tableName,$(this).val(),$(this).text());
    		}
    	});
    }
    function loadAllocationTables(){
    	loadAllocationTable("geoAllocation","allocationTable",$("#regions").val());
    	loadAllocationTable("sectorAllocation","sectorTable",$("#sectors").val());
    	loadAllocationTable("ftAllocation","fundTypeTable",$("#types").val());
    }
    
    function refreshFundList(){
    	//刷新选择项
    	$("#regions").val(getAssetValues("allocationTable"));
    	$("#sectors").val(getAssetValues("sectorTable"));
    	$("#types").val(getAssetValues("fundTypeTable"));    	
    	loadFundList();
    }

    //异步获取选项
	function getSelObj(parentObj,type,name){
		$.ajax({
		      url: base_root+"/front/strategy/info/getSelection.do?method="+type+"&selectionName="+name,
		      type: "post", 
		      async: true,
		      dataType: 'json',
		      success: function(data) {
//	          complete: function(data) {
//		    	  layer.msg(data);
//		    	  htmlStr +=data);
		    	  var objStr = ""; 
		    	  objStr+=' <div id="'+name+'" name="'+name+'" class="investor-xiala pro_xiala" style="width:160px;">'+
  				          '<input type="text" class="productTypes" value="" typecode="stock" placeholder="'+langMutilForJs["strategy.info.allocation.selectItem"]+'" readonly="" style="width:120px">'+
  				        '<input type="hidden" class="itemCode" value="">'+
  				          '<span class="icon_xiala"></span>'+
			  			  ' <ul style="width: 160px;top: 27px;">';
				    	  for (var i=0;i<data.length;i++) {
				    		  var x = data[i];
				    		  objStr +='	<li value="'+x.itemCode+'">'+x.name+'</li>';//"\n    <option value=\""+x.itemCode+"\">"+x.name+"</option>";
		                  } 
			      			
				    	  objStr+='</ul>'+
							'</div>';

		    	 /* var objStr = "\n<select id=\""+name+"\" name=\""+name+"\" class=\"insight-input-select\" style=\"float: left;\">";
		    	  objStr += "\n    <option value=\"\">=== Please select a item ===</option>";
		    	  for (var i=0;i<data.length;i++) {
		    		  var x = data[i];
		    		  objStr += "\n    <option value=\""+x.itemCode+"\">"+x.name+"</option>";
                  } 
		    	  objStr += "\n</select>";*/
//		    	  layer.msg(objStr);
		    	  parentObj.append(objStr);
		      },
		      error: function(error) {
		      }
	    });
	}

    //异步获取选项
	function getSelObjFromArr(type,name){
		var data = null;
		if (type=="region") data = regionList;
		if (type=="fund_sector") data = sectorList;
		if (type=="fund_type") data = fundTypeList;
		 var objStr = ""; 
   	  objStr+=' <div id="'+name+'" name="'+name+'" class="investor-xiala pro_xiala" style="width:160px;">'+
			          '<input type="text" class="itemName" value="" placeholder="'+langMutilForJs["strategy.info.allocation.selectItem"]+'" readonly="" style="width:120px">'+
			          '<input type="hidden" class="itemCode" value="">'+
			          '<span class="icon_xiala"></span>'+
	  			  ' <ul style="width: 160px;top: 27px;height:78px;overflow-y:auto;">';
		    	  for (var i=0;i<data.length;i++) {
		    		  var x = data[i];
		    		  objStr +='	<li value="'+x.itemCode+'">'+x.name+'</li>';//"\n    <option value=\""+x.itemCode+"\">"+x.name+"</option>";
                 } 
	      			
		    	  objStr+='</ul>'+
					'</div>';		
		/*var objStr = "\n<select id=\""+name+"\" name=\""+name+"\" class=\"insight-input-select\" style=\"float:left;margin-top:5px;\">";
		objStr += "\n    <option value=\"\">=== "+langMutilForJs["strategy.info.allocation.selectItem"]+"===</option>";
		if (data)
		for (var i=0;i<data.length;i++) {
			var x = data[i];
			objStr += "\n    <option value=\""+x.itemCode+"\">"+x.name+"</option>";
		} 
		objStr += "\n</select>";*/
		return objStr;
	}
	
	//异步获取已选项
	function setSelectedVals(selName,tableName,type,id){
		$.ajax({
		      url: base_root+"/front/strategy/info/getSelectedData.do?method="+type+"&strategyId="+id,
		      type: "post", 
		      dataType: 'json',
		      success: function(data) {
//		      complete: function(data) {
//		    	  layer.msg(data);

		    	  for (var i=0;i<data.length;i++) {
		    		  var x = data[i];
//		    		  layer.msg(x.name);
		    		  addSelValToTable(selName,tableName,x.id,x.itemCode,x.name,x.itemWeight);
                  } 
		    	  initSlider(tableName);
		    	  calTotal(tableName);
			    	assetChart(tableName);
		      },
		      error: function(error) {
		      }
	    });
	}
	
	// 下拉
	   $("#settings").on("click",".investor-xiala",function(){
	       $(this).toggleClass("xiala-show");
	   });
	   
	   //点击下拉筛选数据
	   $("#settings").on("click",".pro_xiala li",function(e){ 
	       $(this).parents(".investor-xiala").toggleClass("xiala-show").find(".itemName").val($(this).html());
	       $(this).parents(".investor-xiala").find(".itemCode").val($(this).attr("value"));
	       e.stopPropagation(); 
	   }); 
	
	$("#addAllocation").on("click",function(){
		var method = $("#method").val();
		
		if (!method){
			layer.msg("Please select a method.");
			return;
		}
		//$("#method").find("value:"+method).remove();
      
		if (method=="region"){
			var a = new $.AllocationObject({id:"id_alloc_region",name:langMutilForJs["strategy.info.allocation.method.G"],
				parentDiv:"settings",
				strategyId:$("#id").val(),
				level:global_level++,
				method:"region",
				selectionName:"geoAllocation",
				tableName:"allocationTable",
				chartName:"geoChart"});
			a.create();
		}
		else if (method=="fund_sector"){
			var b = new $.AllocationObject({id:"id_alloc_fund_sector",name:langMutilForJs["strategy.info.allocation.method.S"],
				parentDiv:"settings",
				strategyId:$("#id").val(),
				level:global_level++,
				method:"fund_sector",
				selectionName:"sectorAllocation",
				tableName:"sectorTable",
				chartName:"sectorChart"});
			b.create();
		}
		else if (method=="fund_type"){
			var c = new $.AllocationObject({id:"id_alloc_fund_type",name:langMutilForJs["strategy.info.allocation.method.F"],
				parentDiv:"settings",
				strategyId:$("#id").val(),
				level:global_level++,
				method:"fund_type",
				selectionName:"ftAllocation",
				tableName:"fundTypeTable",
				chartName:"ftChart"});
			c.create();
		}	
		 $("#methodSelect li[value="+method+"]").remove();
		 $("#method").val("");
		 $("#methodText").val(langMutilForJs['strategy.info.allocation.selectMethod']);
		 if($("#methodSelect li").length==0){
			 $(".addnextlevel").hide();
		 }
	});
	
    //页面load完后运行
	loadFundList();
	
	//根据已选的分配类型显示模块
	if (selectedAllocation && selectedAllocation.length > 0) {
		//按level从小到大排序
		selectedAllocation.sort(function(a,b){
            return a.level-b.level});
		
		for (var i=0;i<selectedAllocation.length;i++) {
			var x = selectedAllocation[i];
			if (x.type == "G"){
				$("#method").val("region");
				$("#addAllocation").click();
			}
			if (x.type == "S"){
				$("#method").val("fund_sector");
				$("#addAllocation").click();
			}
			if (x.type == "F"){
				$("#method").val("fund_type");
				$("#addAllocation").click();
			}
		} 
	}
	
	
	function assetChart(tableName) {
		var creatlistname = [];
		var creatlistvalue = [];
		var total=0;
		$("#"+tableName+" .createlistcontent").each(function() {
			
			creatlistname.push($(this).attr("data-name"));
			var obj = {};
			obj.name = $(this).attr("data-name");
			obj.value = $(this).find(".strategy-slider-input").val();//$(this).attr("data-value");
			total+=parseFloat(obj.value );
			//creatlistvalue.push({"name":$(this).attr("data-name")},{"value":$(this).attr("data-value")});
			creatlistvalue.push(obj);
		});
		
		var myChart = echarts.init(document.getElementById('chart_'+tableName));

		option = {
			tooltip: {
				formatter: "{d}%"
			},
			legend: {
				orient: 'vertical',
				left: "70%",
				top: 'center',
				data: creatlistname
			},
			series: [{
				type: 'pie',
				radius: '60%',
				center: ['30%', '40%'],
				color: ["#e04e91", "#f0ae29", "#65c1af", "#99c33d", "#2489cb"],
				data: creatlistvalue,
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
	function edit(){
			$('.register_xiala_long input').on('click',function(){
				if($(this).parents('.register_xiala_long').find('ul').css('display') == 'none'){
					$(this).siblings(".regiter_xiala_ul").show();
				}else{
					$(this).siblings(".regiter_xiala_ul").hide();
				};
								
			});
			$('.register_xiala_ico').on('click',function(){
				if($(this).parents('.register_xiala_long').find('ul').css('display') == 'none'){
					$(this).siblings(".regiter_xiala_ul").show();
				}else{
					$(this).siblings(".regiter_xiala_ul").hide();
				};
								
			});
//			$(".register_xiala_long").on("blur","input",function(){
//				var _this = $(this);
					//mandatoryVal = _this.siblings(".regiter_xiala_ul").children().eq(0).attr("value");
					//_this.val(mandatoryVal);					
//				setTimeout(function(){
//					_this.siblings(".regiter_xiala_ul").hide();
//				},200);
//				
//			});
			$('.register_xiala_long').on('mouseleave',function(){
				$(this).find('.regiter_xiala_ul').hide();
			});
			$(".regiter_xiala_ul").on("click","li",function(){
				$(this).parent().siblings('.value_hidden').val( $(this).attr("value") );
				 $(this).parent().siblings('.value_show').val( $(this).text() );
				 $(".regiter_xiala_ul").hide();
			});
	};
	edit();
	
	
	$(".craetbnPrevious").on("click",function(){
		if(getQueryString("new")=="1" ){
			isNew="1";
		}
		var newHtml="";
		if(isNew=="1"){
			newHtml="&new=1"
		}
		window.location.href=base_root+"/front/strategy/info/addStart.do?id="+$("#id").val()+newHtml;
	});
	
	
    var hasLoad=false;
	function initSlider(tableName){
    	var rangeInstanse = $("#"+tableName).find('.funds-single-slider'),
    		rangeCount = rangeInstanse.length;
    	var totalAmt =$("#"+tableName).attr("data-temp"); //$('.total').text();
    	if(hasLoad){
    		$("#"+tableName).find('.strategy-slider-input').each(function(i){
    			if(rangeCount>2 && i==rangeCount-1){
    				$(this).val(parseInt(100/rangeCount)+100%rangeCount);
    			}else{
    				$(this).val(parseInt(100/rangeCount));
    			}
        		
        	});
    		
    	}
    	rangeInstanse.each(function(index){
     		//选择条 功能初始化
     		var self = $(this),
     			_start = 0,
     			_values = [];
     		if(hasLoad){
     			var value=self.parents("li").find(".strategy-slider-input").val();
     			 self.val(value+','+value);
     			//self.jRange('setValue',parseInt(100/rangeCount)+','+parseInt(100/rangeCount));
     		}
     		  
     		else
     			 self.val(self.val()+','+self.val());
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
						rangeInstanse.each(function(i,n){
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
						calTotal(tableName);
						 assetChart(tableName);
					}, ondragend : function(){
						_values = self.val().split(',');
						self.jRange('setValue',_values[0]+','+_values[0]);
					
					}
    		});
     	});
    	hasLoad=true;
    }
	
	$("#btnCancle").on("click",function(){
		layer.confirm(langMutilForJs['strategy.info.form.cancel'],function(){
			window.location.href=base_root+"/front/strategy/info/myList.do";
		});
	})
});