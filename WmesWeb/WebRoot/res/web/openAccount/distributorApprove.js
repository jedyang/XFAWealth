define(function(require) {
	var $ = require('jquery');
	require('layer');
	require('swiper');
	$('.application-information-tab li').on('click',function(){
		$(this).toggleClass('tab-active');
		$(this).siblings().toggleClass('tab-active');
		$('.applications-tabcontrol').toggleClass('tabactive');
		$('.applications-tabcontrol1').toggleClass('tabactive');
	});
	$(".member-comment-text").on("keyup",function(){
    	var length=$(this).val().length;
    	$(".inputLength").text(length);
    	$(".leftLength").text(1000-Number(length));
    	
    });
	$("#btn_approve").click(function() {
		var comment = $(".member-comment-text").val();
		var status = 1;
		layer.confirm(langMutilForJs["open.account.confirm.approve"], 
            {title:langMutilForJs["global.prompt"], 
            icon: 3,
            btn: [langMutilForJs["global.confirm"],langMutilForJs["global.cancel"]]},
            function (index){
    			$.ajax({
    				type : "post",
    				datatype : "json",
    				url : base_root+"/front/distributor/updateFlowStatus.do?",
    				data : {
    					accountId : accountId,
    					comment : comment,
    					status : status
    				},
    				success : function(json) {
    					if (json.result) {
    
    						var status = json.status;
    						var actionStatus = json.actionStatus;
    						if (status == "0") {//转入下一环节，选择下一个审批人
    							var roleId = json.roleId;
    							var instanceId = json.instanseId;
    							var accountId = json.accountId;
    							//console.log("roleid="+roleId);
    							layer.open({
    					    		type: 2,
    					    		content: base_root+'/front/distributor/showSelectUserDialog.do?roleId='+roleId +"&accountId="+accountId,
    					    		area: ['520px', '220px'],
    					    		closeBtn: 0, //不显示关闭按钮
    					    		btn: [langMutilForJs['global.confirm'],langMutilForJs['global.cancel']],
    					    		shape:0.5,
    					    		shapeClose:true,
    					    		title:langMutilForJs['open.account.approval.selectApprover'],
    					    		maxmin: true,
    					    		yes:function(index){
    					    			var id = layer.getChildFrame('#ddlUser', index).val();
    					    			//console.log(id);
    					    			if (id){
    										$.ajax({
    											type : "post",
    											datatype : "json",
    											url : base_root+"/front/distributor/submitApprover.do",
    											data : {
    												userId : id,
    												instanceId : instanceId,
    												accountId : accountId,
    												status : actionStatus
    											},
    											success : function(data) {
    												if (data.result) {
    													layer.msg(langMutilForJs['global.success.save'],function(){
    														if(role=="IFA"){
    															window.location.href=base_root+"/front/investor/accountsWaitforProcess.do";
    														}else{
    															window.location.href=base_root+"/front/investor/accountsWaitforProcessAgent.do";
    														}
    													});
    													
    												} else {
    													layer.alert(data.msg);
    												}
    											}
    										});
    										layer.close(index);
    					    			}else{
    					    				layer.alert(langMutilForJs['open.account.approval.selectApprover']);
    					    			}
    					    		}
    					    	});
    							//console.log("sel=end");
    							
    						} else if (status == "1") {//已审批过，待下一个环节审批人处理
    //							layer.alert(json.msg);
    							layer.alert(json.msg+"\n<br>"+langMutilForJs['open.account.approval.status.waitForNextStep'],function(){
                                    if(role=="IFA"){
                                        window.location.href=base_root+"/front/investor/accountsWaitforProcess.do";
                                    }else{
                                        window.location.href=base_root+"/front/investor/accountsWaitforProcessAgent.do";
                                    }
                                    
                                });
    
    						} else if (status == "3") {//已审批过，待当前环节下一审批人处理
    							layer.alert(json.msg+"\n<br>"+langMutilForJs['open.account.approval.status.waitForNext'],function(){
                                    if(role=="IFA"){
                                        window.location.href=base_root+"/front/investor/accountsWaitforProcess.do";
                                    }else{
                                        window.location.href=base_root+"/front/investor/accountsWaitforProcessAgent.do";
                                    }
                                    
                                });
    
    						} else if (status == "2") {//流程结束
    							var accountId = json.accountId;
    							//录入客户的账户号码
    							layer.prompt({
    								  formType: 2,
    								  value: langMutilForJs['open.account.inputAccountNo.label'],
    								  title: langMutilForJs['open.account.inputAccountNo.title'],
    								  area: ['500px', '250px'] //自定义文本域宽高
    								}, function(value, index, elem){
    									//console.log(value); //得到value
    									$.ajax({
    										type : "post",
    										datatype : "json",
    										url : base_root+"/front/distributor/completeAccountNo.do",
    										data : {
    											accountId : accountId,
    											accountNo : value
    										},
    										success : function(data) {
    											if (data.result) {
    												layer.msg(langMutilForJs['global.success.save'],function(){
    													if(role=="IFA"){
    														window.location.href=base_root+"/front/investor/accountsWaitforProcess.do";
    													}else{
    														window.location.href=base_root+"/front/investor/accountsWaitforProcessAgent.do";
    													}
    												});
    											} else {
    												layer.alert(data.msg);
    											}
    										}
    									});
    									layer.close(index);
    								});
    						}
    					} else {
    						layer.alert(json.msg);//审批失败
    					}
    				}
    			})
			    layer.close(index);
            });//confirm
		});
	
		$("#btn_reject").click(function() {
			var comment = $("#msg").val();
			var status = 0;
			layer.confirm(langMutilForJs["open.account.confirm.reject"], 
	            {title:langMutilForJs["global.prompt"], 
	            icon: 3,
	            btn: [langMutilForJs["global.confirm"],langMutilForJs["global.cancel"]]},
	            function (index){
    			$.ajax({
    				type : "post",
    				datatype : "json",
    				url : base_root+"/front/distributor/updateFlowStatus.do?",
    				data : {
    					accountId : accountId,
    					comment : comment,
    					status : status
    				},
    				success : function(json) {
    					if (json.result) {
    
    						var status = json.status;
    						var actionStatus = json.actionStatus;
    						if (status == "0") {//待选择下一个审批人
    							var roleId = json.roleId;
    							var instanceId = json.instanseId;
    							BootstrapDialog.show({
    								title : langMutilForJs['open.account.approval.selectApprover'],
    								cssClass : 'login-dialog',
    								type : BootstrapDialog.TYPE_PRIMARY,
    								size : BootstrapDialog.SIZE_SMALL,
    								draggable : true,
    								message : $('<div></div>').load('selectApprover.do?roleId=' + roleId),
    								buttons : [ {
    									label : "[@lang_res  k='global.confirm'/]",
    									cssClass : 'btn-primary',
    									action : function(dialogItself) {
    										var id = $("#ddlUser").val();
    
    										$.ajax({
    											type : "post",
    											datatype : "json",
    											url : base_root+"/front/distributor/submitApprover.do",
    											data : {
    												userId : id,
    												instanceId : instanceId,
    												accountId : acccount,
    												status : actionStatus
    											},
    											success : function(data) {
    												if (data.result) {
    													layer.msg(langMutilForJs['global.success.save'],function(){
                                                            if(role=="IFA"){
                                                                window.location.href=base_root+"/front/investor/accountsWaitforProcess.do";
                                                            }else{
                                                                window.location.href=base_root+"/front/investor/accountsWaitforProcessAgent.do";
                                                            }
                                                        });
    												} else {
    													layer.msg(data.msg,{time:2000});
    												}
    											}
    										});
    
    										dialogItself.close();
    									}
    								}, {
    									label : "[@lang_res  k='global.back'/]",
    									action : function(dialogItself) {
    										dialogItself.close();
    									}
    								} ]
    							});
    						} else if (status == "1") {//已审批过，待下一个环节审批人处理
    //                          layer.alert(json.msg);
                                layer.alert(json.msg+"\n<br>"+langMutilForJs['open.account.approval.status.waitForNextStep'],function(){
                                    if(role=="IFA"){
                                        window.location.href=base_root+"/front/investor/accountsWaitforProcess.do";
                                    }else{
                                        window.location.href=base_root+"/front/investor/accountsWaitforProcessAgent.do";
                                    }
                                    
                                });
    
                            } else if (status == "3") {//已审批过，待当前环节下一审批人处理
                                layer.alert(json.msg+"\n<br>"+langMutilForJs['open.account.approval.status.waitForNext'],function(){
                                    if(role=="IFA"){
                                        window.location.href=base_root+"/front/investor/accountsWaitforProcess.do";
                                    }else{
                                        window.location.href=base_root+"/front/investor/accountsWaitforProcessAgent.do";
                                    }
                                });
                            }
    					}else {
    	                    layer.alert(json.msg);//审批失败
    	                }
    				}
    				
    			});
    			layer.close(index);
            });//confirm
		});
		
		$(".viewPdf").click(function() {
			//弹出即全屏
			var index = layer.open({
				skin : 'layui-layer-lan',
				title : '查看pdf',
				type : 2,
				content : '/wmes/front/investor/dialogshowpdf.do',
				area : [ '320px', '195px' ],
				maxmin : true
			});
			layer.full(index);
		});
		
		
		var List = {
				bigImages :function(number){
					var galleryTop = new Swiper('.gallery-top', {
			            nextButton: '.swiper-button-next',
			            prevButton: '.swiper-button-prev',
			            initialSlide :number,
			            spaceBetween: 10,
			        });
			        var galleryThumbs = new Swiper('.gallery-thumbs', {
			            spaceBetween: 10,
			            centeredSlides: true,
			            slidesPerView: 'auto',
			            touchRatio: 0.2,
			            initialSlide :number,
			            slideToClickedSlide: true
			        });
			        galleryTop.params.control = galleryThumbs;
			        galleryThumbs.params.control = galleryTop;
				},
				init:function(){

					$(".application-informationcon li").on("click",function(){
						var swiper_html = "";
					       $(".application-informationcon li").each(function(){
					    	   var images=$(this).find(".application-information-yangban");
					            swiper_html += '<div class="swiper-slide" style="background-image:url('+ $(images).attr("data-srcsrc") +')"></div>'
					        });

					        $("#galler-big-img").html(swiper_html);
					        $("#galler-small-img").html(swiper_html);

							List.bigImages($(this).index(".application-informationcon"));
							$(".gallery-bigger-images").addClass("gallery-bigger-blocl");
					})

					$(document).on("click","#pipelin-investors-close",function(){
						$(".gallery-bigger-images").removeClass("gallery-bigger-blocl");
					});

					$("#update-show").on("click",function(){
						$("#view-filelist-update").addClass("view-filelist-show");
					});

					$("#view-update-close").on("click",function(){
						$("#view-filelist-update").removeClass("view-filelist-show");
					});

					$(".document_img").on("click",function(){
						$(".detail-view-filelist").addClass("view-filelist-show");
					});

					$("#view-file-close").on("click",function(){
						$(".detail-view-filelist").removeClass("view-filelist-show")
					});

					$(".doc-show").on("click",function(){
						$("#view-filelist-update").addClass("view-filelist-show");
					});

					$(".document_ico").on("click",function(){
						$(".document-wrap").addClass("view-filelist-show");
						$(".document-list-swiper").each(function(){
							var swiper = new Swiper($(this).find('.document-swiper-wrapper'), {
						        slidesPerView: 'auto',
						        preventClicks : false,
						        nextButton: $(this).find('.swiper-button-next'),
						        prevButton: $(this).find('.swiper-button-prev'),
						    });
						});
					});

					$("#view-document-close").on("click",function(){
						$(".document-wrap").removeClass("view-filelist-show");
					});

					$(".document-swiper-rows").on("click",function(){
				        var swiper_html = ""
				        $(this).parent().find("img").each(function(){
				            swiper_html += '<div class="swiper-slide" style="background-image:url('+ $(this).attr("data-src") +')"></div>'
				        });
				        $("#galler-big-img").html(swiper_html);
				        $("#galler-small-img").html(swiper_html);

						List.bigImages($(this).index());
						$(".gallery-bigger-images").addClass("gallery-bigger-blocl");
				    });
				}()
			}
		
		
});