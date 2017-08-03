define(function(require){
	var $ = require('jquery');
	
	require('layui');
	var selector = {};
	
	selector.arrIds = [];
	selector.arrNames = [];
	selector.userType = '';
	selector.selectSingle = 0; //1：单选 ，0：多选
	selector.openerIdObj = '';
	selector.openerNameObj = '';
	selector.calBack=undefined;
	selector.test = function(){
		layer.msg(selector.userType);
	}
	selector.create = function(singleSelect,userType,openerIdObj,openerNameObj,calBack){
		//实 例化时把这 二个变量重新初始化状态
		selector.arrIds = [];
		selector.arrNames = [];
		
		selector.userType = userType;
		selector.selectSingle = singleSelect;
		selector.openerIdObj = openerIdObj;
		selector.openerNameObj = openerNameObj;
		selector.calBack=calBack;
		function getSelectUserList(userType){			   
				$.ajax({
					type : 'get',
					datatype : 'json',
					url : base_root+'/front/ifa/info/selectUserListJson.do?datestr='+new Date().getTime(),
					data : {
						'userType':userType
					},
					async : false,
					success : function(json) {

				    	$(".selectnamelistbox").empty();				
						var liContent = '';
						
						var list = json.rows;
						//console.log(list);
						$.each(list, function (index, array) { //遍历json数据列	
							var id = array['id'] == null ? "" : array['id'];
							var fullName = array['fullName'] == null ? "" : array['fullName'];
							var iconUrl = array['iconUrl'] == null ? "" : array['iconUrl'];
							var clientType = array['clientType'] == null ? "" : array['clientType'];
							var typeName = '';
							if(userType == 'client'){
								if(clientType == '1'){
									typeName = '('+langMutilForJs['ifa.user.selector.clientType.1']+')';
								}else if(clientType == '0'){
									typeName = '('+langMutilForJs['ifa.user.selector.clientType.0']+')';
								}
							}
												                                  
							liContent += '<li data-id="'+id+'" data-name="'+fullName+'" class="container">'
										+ '<div class="selectedshadow"></div>'
										+ '<b></b>'
										+ '<i></i>'
										+ '<img src="'+base_root+iconUrl+'"/>'
										+ '<p>'+fullName+'</p>'
										+ '<p class="bctype">'+typeName+'</p>'
										+ '</li>';						
						});	
						
						//var title = selector.selectSingle == 0 ? langMutilForJs['ifa.user.selector.multiple.title']:langMutilForJs['ifa.user.selector.single.title'];
						var title = langMutilForJs['ifa.user.selector.title.1'];
						var tips = langMutilForJs['ifa.user.selector.resultTips.1'];
						if(',client,existing,prospect,'.indexOf(userType)>-1){
							title = langMutilForJs['ifa.user.selector.title.1'];
							tips = langMutilForJs['ifa.user.selector.resultTips.1'];
						}else if(userType == 'buddy'){
							title = langMutilForJs['ifa.user.selector.title.2'];
							tips = langMutilForJs['ifa.user.selector.resultTips.2'];
						}else if(userType == 'colleague'){
							title = langMutilForJs['ifa.user.selector.title.3'];
							tips = langMutilForJs['ifa.user.selector.resultTips.3'];
						}
						var html = '<div class="selectnamelistcon" style="background:#fff;">'
								+ '		<div class="wmes-close"></div>'
								+ '		<div class="wmes-wrap-title tanchutitle">'+title+'</div>'
								+ '		<div class="myClient">'
//								+ '			<div class="strategies_detail_list myClienttittle">'
//								+ '				<p class="strategies_detail_product"></p>'
//								+ '			</div>'
								+ '			<div class="selectedtext "';
						if(selector.selectSingle!=0){
							html+='style="display:none">'
						}		
						
						else{
							html +=  '	 >			<div class="selectedtextleft"><span>0</span>'+tips+'</div>'
									+ '				<ul class="selectedtextright">'
									+ '					<li>'+langMutilForJs['ifa.user.selector.select']+'</li>'
									+ '					<li>'
									+ '						<p><input type="checkbox"><p>'
									+ '						<p class="SelectAll">'+langMutilForJs['ifa.user.selector.selectAll']+'</p>'
									+ '					</li>'
									+ '					<li><span>'+langMutilForJs['ifa.user.selector.reverse']+'</span></li>'
									+ '				</ul>';
						}
								

						html += '			</div>'
								+ '			<div class="selectedlist">'
								+ '				<ul>'
								+ 					liContent
								+ '				</ul>'
	
								+ '			</div>'
								+ '		</div>'
								//+ '		<input type="hidden" name="openerMemberIdsObj" id="openerMemberIdsObj" value="${memberIdsObj!""}">'
								//+ '		<input type="hidden" name="openerMemberNamesObj" id="openerMemberNamesObj" value="${memberNamesObj!""}">'
								+ '		<input type="hidden" name="selectedMemberIds" id="selectedMemberIds" value="">'
								+ '		<input type="hidden" name="selectedMemberNames" id="selectedMemberNames" value="">'
								+ '		<div class="creatlistbn" style="margin-top:40px;">'
								+ '			<a class="craetbnCancel" id="btn_cancel">'+langMutilForJs['global.cancel']+'</a>'
								+ '			<a class="craetbnSave" id="btn_update" style="margin-right:15px;">'+langMutilForJs['global.confirm']+'</a>'
								+ '		</div>'
								+ '</div>';

						$(".selectnamelistbox").append(html);
						
						for(i=0;i<$('.bctype').length;i++){
    		  				if($('.selectedlist li').eq(i).find('.bctype').text()=='(Prospect)'){
    		  					$('.selectedlist li').eq(i).closest('li').css('background','#fbebcb');
    		  				};
    		  			}
						
						checkSelection( $('#'+selector.openerIdObj).val());
					}
				})
		
					
		}
		
	    /**
	    * 根据父窗口的类型，和 已选值，自动选定（适于编辑状态）
	    */
	    function checkSelection( selected){
	       
	        if (!selected) return;
	        
	        selector.arrIds = [];//选中用户Id
	        selector.arrNames = [];//选中用户名称

	        var ifAll = false;
	        if (selected.indexOf("ALL")>=0) ifAll = true;
	        
	        var strArr = selected.split(",");
	        if (strArr.length==0) return;
	   
	        $.each(strArr, function(i,val){  
	            //alert("val="+val);
	            if (!val || val.length==0) return true;//continue
		        $(".selectedlist").find("li").each(function(){
		            //var _type = $(this).attr("data-type");
		            var _li = $(this);
//			            if (_type==type){
				        var id = $(this).attr("data-id")+"";
			            if (ifAll || (val && val.length>0 && val == id)){
			             // if (_li.find("a").attr("style")=="display:none")
			                _li.click();
			                if (!ifAll) return false;
		                }
//				        }
			    });
	        });
	    }
	
		getSelectUserList(selector.userType);
	}
	selector.init = function(){
			
	    $(document).on('click','.selectedlist li',function(){//可多选
	    	$(".selected_active").removeClass("selected_active");
	        $(this).addClass('selected_active');
	        var id = $(this).attr('data-id');
	        var name = $(this).attr('data-name');
	        //var name = $(this).children('p').first().text();
	        
	        //alert(name);
	        if(selector.selectSingle == 1){	        	
	        	if($(this).find('b').hasClass('selectedchecked')){
					$(this).find('i').removeClass('selectedshadow1');
					$(this).find('b').removeClass('selectedchecked');
					selector.arrIds = [];
					selector.arrNames= [];
					
				}else{
		        	$(this).siblings().find('b').removeClass('selectedchecked');
		        	$(this).siblings().find('i').removeClass('selectedshadow1');
		        	
					$(this).find('i').addClass('selectedshadow1');
					$(this).find('b').addClass('selectedchecked');
					
					selector.arrIds = [];
					selector.arrNames= [];
					selector.arrIds.push(id);
					selector.arrNames.push(name);						
				}
	        }else{
	        	if($(this).find('b').hasClass('selectedchecked')){
					$(this).find('i').removeClass('selectedshadow1');
					$(this).find('b').toggleClass('selectedchecked');
							
					var idIndex = selector.arrIds.indexOf( id );
					var nameIndex = selector.arrNames.indexOf( name );
					selector.arrIds.splice(idIndex, 1);
					selector.arrNames.splice(nameIndex, 1);
					
				}else{
					$(this).find('i').addClass('selectedshadow1');
					$(this).find('b').toggleClass('selectedchecked');
								  			
					selector.arrIds.push(id);
					selector.arrNames.push(name);				
				}
	        }
	        

        	$('#selectedMemberIds').val(selector.arrIds.join(','));
			$('#selectedMemberNames').val(selector.arrNames.join(','));
	        ////console.log(selector.arrNames);
				        
			if($(this).parents('.myClient').find('.selectedchecked').length !== $(this).parents('.myClient').find('.selectedlist li').length){
				$(this).parents('.myClient').find('.selectedtextright input').prop('checked',false);
			}else{
				$(this).parents('.myClient').find('.selectedtextright input').prop('checked',true);
			};
			$(this).parents('.myClient').find('.selectedtextleft span').html($(this).parents('.myClient').find('.selectedchecked').length);
	        
	    });
	    $(document).on('touchend','.selectedlist li',function(e){
	    	$(this).addClass('selected_active');
	        var id = $(this).attr('data-id');
	        var name = $(this).attr('data-name');
	        //var name = $(this).children('p').first().text();
	        
	        //alert(name);
	        if(selector.selectSingle == 1){	        	
	        	if($(this).find('b').hasClass('selectedchecked')){
					$(this).find('i').removeClass('selectedshadow1');
					$(this).find('b').removeClass('selectedchecked');
					selector.arrIds = [];
					selector.arrNames= [];
					
				}else{
		        	$(this).siblings().find('b').removeClass('selectedchecked');
		        	$(this).siblings().find('i').removeClass('selectedshadow1');
		        	
					$(this).find('i').addClass('selectedshadow1');
					$(this).find('b').addClass('selectedchecked');
					
					selector.arrIds = [];
					selector.arrNames= [];
					selector.arrIds.push(id);
					selector.arrNames.push(name);						
				}
	        }else{
	        	if($(this).find('b').hasClass('selectedchecked')){
					$(this).find('i').removeClass('selectedshadow1');
					$(this).find('b').toggleClass('selectedchecked');
							
					var idIndex = selector.arrIds.indexOf( id );
					var nameIndex = selector.arrNames.indexOf( name );
					selector.arrIds.splice(idIndex, 1);
					selector.arrNames.splice(nameIndex, 1);
					
				}else{
					$(this).find('i').addClass('selectedshadow1');
					$(this).find('b').toggleClass('selectedchecked');
								  			
					selector.arrIds.push(id);
					selector.arrNames.push(name);				
				}
	        }
	        

        	$('#selectedMemberIds').val(selector.arrIds.join(','));
			$('#selectedMemberNames').val(selector.arrNames.join(','));
	        ////console.log(selector.arrNames);
				        
			if($(this).parents('.myClient').find('.selectedchecked').length !== $(this).parents('.myClient').find('.selectedlist li').length){
				$(this).parents('.myClient').find('.selectedtextright input').prop('checked',false);
			}else{
				$(this).parents('.myClient').find('.selectedtextright input').prop('checked',true);
			};
			$(this).parents('.myClient').find('.selectedtextleft span').html($(this).parents('.myClient').find('.selectedchecked').length);
	        e.preventDefault();
	    })
	    
	    //全选
	    $('body').on('click','.SelectAll',function(){
			$(this).parents('.selectedtextright').find('input').click();		
		});
	    
	    //全选
		$('body').on('click','.selectedtextright input',function(){
			
			var _this = $(this);
	        var _parent = _this.parent().parent().parent().parent();
	        if (_this.is(':checked')){//检查checkbox是否选中
		        _parent.parent().find(".selectedlist").find("li").each(function(){
		           var _li = $(this);
		           if (!_li.find("b").hasClass("selectedchecked"))
		               _li.click();
		        });
	        }else{
	        	_parent.parent().find(".selectedlist").find("li").each(function(){
	               var _li = $(this);
	               if (_li.find("b").hasClass("selectedchecked"))
	                   _li.click();
	            });
	        }
	       
	       ////console.log($("#clients").val());
		});
		
		//反选
		$('body').on('click','.selectedtextright span',function(){
	       
	        $("body .selectedlist .container").each(function(){
	        	$(this).click();
	        });

		});

	    
	    /**
	    * 返回（保存）当前选定值，自动设置父窗口对应的对象值，需保证变量定义一致
	    */
	    $("body").on("click","#btn_update",function(){
	            
	        try{
		        $('#'+selector.openerIdObj).val($("#selectedMemberIds").val());
		        ////console.log(selector.openerIdObj);
	        	if(selector.openerNameObj != ''){
			        $('#'+selector.openerNameObj).text($("#selectedMemberNames").val());
			        $('#'+selector.openerNameObj).val($("#selectedMemberNames").val());
			        
			        if(selector.calBack!=undefined){
			        	selector.calBack();
			        }
	        	}

	        }catch(e){
	            layer.msg("error");
	        }
	        //$(".selectnamelistbox").empty();
	        $(".selectnamelistbox").css("display","none");
	    });
	    
	    $("body").on("click","#btn_cancel",function(){
	    	//$(".selectnamelistbox").empty();
	    	$(".selectnamelistbox").css("display","none");
	    });
	    $("body").on("click",".wmes-close",function(){
	    	//$(".selectnamelistbox").empty();
	    	$(".selectnamelistbox").css("display","none");
	    });
	    	    
	    $('body').on('mousemove','.selectedlist li',function(){
			$(this).find('.selectedshadow').addClass('selectedshadow1');
		});
	    
		$('body').on('mouseout','.selectedlist li',function(){
			$(this).find('.selectedshadow').removeClass('selectedshadow1');
		});
	}
	
	
	return selector;
});