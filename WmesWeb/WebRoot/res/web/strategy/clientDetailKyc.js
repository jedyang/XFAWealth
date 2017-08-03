
define(function(require) {

	var $ = require('jquery');
		pagination = require('pagination');
		require("echarts");
		require('layui');
		require('laydate');
		require('datepick');
		require("swiper");
		require("jqueryForm");
		require("wmes_upload");
		var interfaceObj = require("interfaceCheckPwd");
		
		if(_checkList!=undefined && _checkList!=""){
			$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
		}
	
		
		$(".strategies_more").on("click",function(){
			 $(this).parents(".strategies_list_wrap").toggleClass("strategies_narrow");
			 
	    });
		
		$(".doc_history,.document-close,.investment-close-ico").on("click",function(){
			$(".investment-wrap").toggleClass("investment-hide");
			var docId = $(this).attr("docid");
			$.ajax({
				url:base_root + '/front/kycDoc/docHistoryKyc.do?docId='+docId+'&r='+Math.random(),
				type:'get',
				dataType:'json',
				success:function(data){
					////console.log(data.toSource());
					if(!!data){
						var swiperHtml = "";
						for(var x=0;x<data.length;x++){
							swiperHtml +='<div class="document-swiper-rows swiper-slide>';
							if(!!data.docAtt){
								for(var y=0;y<data.docAtt.length;y++){
									swiperHtml +='<img data-src="'+data.docAtt.filePath+'" >';
								}
							}
							swiperHtml += '</div>';	
						}
					}
				},
				error:function(){
					
				}
			});
//			layer.open({
//	    		type: 2,
//	    		content: base_root+'/front/kycDoc/docHistoryKyc.do?docId='+docId+'&r='+Math.random(),
//	    		area: ['1000px', '618px'],
//	    		shape:0.5,
//	    		shapeClose:true,
//	    		title:false,
//	    		maxmin: true
//	    	});
		});
		
		$(".doc_list").on("click",function(){
			var docId = $(this).attr("docid");
			 layer.open({
	    		type: 2,
	    		content: base_root+'/front/kycDoc/docDetail.do?docId='+docId+'&r='+Math.random(),
	    		area: ['1000px', '618px'],
	    		shape:0.5,
	    		shapeClose:true,
	    		title:false,
	    		maxmin: true
	    	});
		})
		
		function assetChart(){
			var name=[];
			var data=[];
			if(_marketRatio>0){
				name.push(titleMarketValue);
				var dataMdl={};
				dataMdl.name=titleMarketValue;
				dataMdl.value=_marketRatio;
				data.push(dataMdl);
			}
			if(_cashRatio>0){
				name.push(titleCash);
				var dataMdl={};
				dataMdl.name=titleCash;
				dataMdl.value=_cashRatio;
				data.push(dataMdl);
			}
			
			if(name.length==0){
				$(".client-detail-chart").css("display","none");
				return;
			}
			
			
				$(".client-detail-chart").each(function(){
					var option = {
					    
					    legend: {
					    	orient : 'vertical',
					        x: 'right',
					        itemWidth:'15',
					        itemHeight:'15',
					        y:'60px',
					        data:name
					    },
					    series: [
					        {
					            name:'account',
					            type:'pie',
					            center:	['30%', '50%'],
					            radius : [0,"80%"],
					            color :["#c3ef56","#8fc3ff"],
					            label: {
					                normal: {
					                    position: 'inner',
					                    formatter : "{d}%"
					                }
					            },
					       
					            data:data
					        }
					    ]
					};

					var myChart = echarts.init($(this)[0]);
		  			myChart.setOption(option,true);
				});
		}
		assetChart();
	$(".clientDetail_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(this).closest('ul').next().children("div").hide().eq($(this).index()).show();
	});
	$(".client-tab li").on("click",function(){
		$(this).siblings().removeClass("tab-active").end().addClass("tab-active");
		$(this).closest('ul').next().children("div").hide().eq($(this).index()).show();
	});
	
	/******************************KYC的背景、培训功能 BY linwenwei***********************************/
	
	//点击backgroup按钮，弹出窗口
	$('#btn-addbackground').on('click',showAddbackgrounddiaog);
	//点击培训按钮，弹出窗口
	function showAddbackgrounddiaog(){
		$('#txtName').val();
		$('#txtStartime').val();
		$('#txtEndtime').val();
		$('#txtOrganization').val();
		$('#txtDescription').val();
		layer.open({
		      type: 1,
		      title: '',
		      shadeClose: false,
		      //shade: true,
		      area: ['1000px', '580px'],
		      anim:1,
		      content: $('#backgroundForm'),
		      success: function(layero, index){
		    	  
		      }
		    });
		
		
	}
	
	//保存backgroup信息
	$('#btn-savebackground').on('click',addbackground);
	
	function addbackground(){
		//序列化表单数据
		var serializeValue = $("#form-background").serialize();
		var data =  "langCode=sc&" + serializeValue ;
		//if()
		
		$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/investor/saveInvestorBackground.do?datestr="+ new Date().getTime(),
				data : data,
				beforeSend: function () {},
                complete: function () {},
				success : function(json) {
					var obj = $.parseJSON(json); 
					if(obj.result==true){
						layer.msg('add successful!', {icon: 1},function(){layer.closeAll();});
					}
				}
			});
	}
	
	//点击培训按钮，弹出窗口
	$('#btn-addtraining').on('click',function(){
		showAddtrainingDialog();
		//$('.layui-layer').css("top",$(window).scrollTop() + 20 +"px");
	});
	//点击培训按钮，弹出窗口
	function showAddtrainingDialog(){
		$('.application-title-training-record').text(props['account.detail.kyc.trainingrecord.form.title']);
		initTrainingDialog();
		layer.open({
	      type: 1,
	      title: '',
	      shadeClose: false,
	      //shade: true,
	      area: ['1000px', 'auto'],
	      offset: '4%',
	      anim:1,
	      content: $('#trainingForm'),
	      success: function(layero, index){
	    	  $(".upload-album").InitUploader({ uploadtype:'image',bgimagetype:2,btntext: "", multiple: true, water: true, thumbnail: true, filesize: "8000",modulerelate:"training" });
	      }
	   });
	}
	//保存培训信息
	$('#btn-savetraining').on('click',addtraining);
	function validTrainingData(){
		var flag = true;
		var trainingId = $('#hidTrainingId').val();
		var name = $('#txtName').val();
		if(!name || name.length == 0){
			$('.input-empty-valid-name').show();
			flag = false;
		}
		var starTime = $('#txtStartime').val();
		var endTime = $('#txtEndtime').val();
		if(!starTime || !endTime){
			$('.input-empty-valid-time').show();
			flag = false;
		}
		var organization = $('#txtOrganization').val();
		if(!organization || !organization){
			$('.input-empty-valid-organization').show();
			flag = false;
		}
		var description = $('#areDescription').val();
		var distributorId = $('#hidDistributorId').val();
		//分析上传附件
		var img_list = $('.photo-list ul li');
		var imgData = [];
		$.each(img_list,function(i,n){
			if($(this).find('.upload-eachimg-cla').val() != ''){
				imgData.push($(this).find('.upload-eachimg-cla').val());
			}
		});
		var data = {
			id:trainingId,	
			name:encodeURI(name),	
			starTime:starTime,	
			endTime:endTime,	
			organization:encodeURI(organization),	
			description:encodeURI(description),
			imgData:encodeURI(JSON.stringify(imgData)),
			distributorId:distributorId
		};
		return{
			flag:flag,
			data:data
		};
	}
	function addtraining(){
		var result = validTrainingData();
		if(!result.flag){
			return;
		}
		var data = result.data;
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/investor/saveInvestorTraining.do?datestr="+ new Date().getTime(),
			data : data,
			beforeSend: function () {},
            complete: function () {},
			success : function(json) {
				var obj = $.parseJSON(json); 
				if(obj.result==true){
					layer.closeAll();
					layer.msg(props['global.success.save']);
					getTrainings();
				}
			}
		});
	}
	$('#txtName').on('blur',function(){
		if($(this).val() && $(this).val().length > 0){
			$('.input-empty-valid-name').hide();
		}
	});
	$('#txtOrganization').on('blur',function(){
		if($(this).val() && $(this).val().length > 0){
			$('.input-empty-valid-organization').hide();
		}
	});
	
	
	//修改培训信息
	$('#btn-edittraining').click(function(){
		//序列化表单数据
		var serializeValue = $("#form-training").serialize();
		var data =  "langCode=sc&" + serializeValue ;
		//分析上传附件
		var img_list = $('.photo-list ul li');
		var img_data = '';
		$.each(img_list,function(i,n){
			//JPG|IMG_2064.JPG|25477412-c31e-4798-8ad3-3309d5a95b30.JPG|/u/corner/201611/25477412-c31e-4798-8ad3-3309d5a95b30.JPG
			var hid_data = $(this).find('input').val();
			////console.log(src);
			if(hid_data!=''){
				img_data += hid_data + '$';
			}
		});
		if(img_data!='')data = data + '&imgData=' +  img_data + '&moduleType=investor_training';
		$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/investor/saveInvestorTraining1.do?datestr="+ new Date().getTime(),
				data : data,
				beforeSend: function () {},
                complete: function () {},
				success : function(json) {
					var obj = $.parseJSON(json); 
					if(obj.result==true){
						layer.msg('add successful!', {icon: 1},function(){layer.closeAll();});
					}
				}
			});
		
	});
	//删除培训信息
	$("body").on('click', '.client-table-tr-img', '', function () {
		var id = $(this).attr('data-id');
		var tr = $(this).parent().parent();
		if(id!=''){
			layer.confirm(props['kyc.training.record.alert.delete.record'], 
					{title:props['global.info'],btn: [props['global.confirm'],props['global.cancel']] },
			function () { 
				$.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/investor/delInvestorTraining.do?datestr="+ new Date().getTime(),
					data : {'id':id},
					beforeSend: function () {},
	                complete: function () {},
					success : function(json) {
						var obj = $.parseJSON(json); 
						if(obj.result){
							layer.msg(props['global.success.delete']);
							getTrainings();
						}
					}
				});
		  });
		}
	});
	
	/****************************持仓列表**********************************/
	
	
   //交易记录，点击条个筛选条件进行数据筛选
 	$(".conservative-choice li").on("click",function(){
		if($(this).index()==0){
			$(this).addClass('conservative-choice-active2');
		}else{
			$('.conservative-choice li').removeClass('conservative-choice-active2');
		};
 		$(this).siblings().removeClass("conservative-choice-active2").removeClass("conservative-choice-active").end().addClass("conservative-choice-active").addClass("conservative-choice-active2");
 		var dataValue = $(this).attr('data-value');	
 		var transactionType = $('#txttransactiontype').attr('data-value');
 		loadOrderReturnList(dataValue,transactionType,0);
// 		var this_letter	 = $(this).attr("data-letter"),
// 			funds_logo  = $("#service_region").children(),
// 			funds_logo_lenght = funds_logo.length;
// 		for (var k = 0 ; k< funds_logo_lenght; k++){
// 			if( this_letter == funds_logo.eq(k).attr("data-letter") ){
//  				funds_logo.eq(k).show();
// 			}else{
// 				funds_logo.eq(k).hide();
// 			}
// 		}
// 		if($(this).hasClass("funds_all") && $(this).hasClass("funds_aloac")){
// 			$(".funds_logo_choice li").removeClass("fund_logo_active");
// 			$(this).removeClass("funds_aloac");
// 			var selection_criterialenght = $(".selection_criteria li").length;
// 			for(var i = 0; i < selection_criterialenght ;i++){
// 				if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
// 					$(".selection_criteria li").eq(i).addClass("thisremove");
// 				}
// 			}
// 			$(".thisremove").remove();
// 			loadIFAList(this);
// 		}
 	});
 	
 	loadOrderReturnList("","today",0);//页面加载时显示当天的数据
 	//通过条件筛选显示交易列表信息
 	function loadOrderReturnList(dataValue,transactionType,pageid){
 		var laymsgindex = null;
 		var accountId = getQueryString('id');
 		var dataStr =  "langCode="+lang+"&rows=5&page=" + (pageid+1) + "&id=" + accountId + "&dataValue=" + dataValue + "&transactionType=" + transactionType ;
 		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/investor/getOrderReturnList.do?datestr="+ new Date().getTime(),
			data : dataStr,
			 beforeSend: function () {},
			 complete: function () {
				 //layer.close();
			 },
			success : function(data) {
				console.log(data);
				json = JSON.parse(data);
				var total = json.total;
				var tr_html = '';
				$('#table-order-return tbody tr').eq(0).nextAll().remove();
				var dataList = json.list;
				var orderLeng = dataList.length;
				if(orderLeng==0){
					$('.orderListTips').show();
					$('#pagination').hide();
					return ;
				} 
				$('.orderListTips').hide();
				$('#pagination').show();
				//alert(dataList[0].transactionNo);
				$.each(dataList,function(i,n){
					//[{"id":"1","transactionType":"1","recordDate":"2017-01-03","currency":"RMB","stockCode":"A001","stockName":"TEST NAME","amount":12000.0,"quantity":null,"orderNumber":"12","remark":"AAAAAA"}
					var id = dataList[i].id;
					var transactionType = dataList[i].transactionType;
					var transactionTypeName = '';
					switch(transactionType)
					{
						case "1":transactionTypeName = 'Subscriptionbreak';break;
						case "2":transactionTypeName = 'Redemption';break;
						case "3":transactionTypeName = 'Buy';break;
						case "4":transactionTypeName = 'Sell';break;
						case "5":transactionTypeName = 'Switch In';break;
						case "6":transactionTypeName = 'Switch Out';break;
						case "7":transactionTypeName = 'Deposit';break;
						case "8":transactionTypeName = 'Withdrawn';break;
						case "9":transactionTypeName = 'Asset Transfer In';break;
						case "10":transactionTypeName = 'Asset Transfer Out';break;
						case "11":transactionTypeName = 'Coupon';break;
						case "12":transactionTypeName = 'Interest';break;
						case "13":transactionTypeName = 'Fee Payment';break;
						case "14":transactionTypeName = 'Corporate Action';break;
						case "15":transactionTypeName = 'FX Voucher';break;
						case "16":transactionTypeName = 'Transaction Fee';break;
						case "17":transactionTypeName = 'Cash Dividend';break;
					}
					var recordDate = dataList[i].recordDate;
					var currency = dataList[i].currency;
					var stockCode = dataList[i].stockCode;
				    var stockName = dataList[i].stockName;
				    var amount = dataList[i].amount;
				    var quantity = dataList[i].quantity;
				    if(quantity==null)quantity='';
				    var orderNumber = dataList[i].orderNumber;
					var remark = dataList[i].remark;
					var tr = '<tr>';
						tr += '<td class="portfolio-tdcenter">';
						tr += '<p>'+recordDate+'</p>';
						//tr += '<p>15:55:12</p>';
						tr += '</td>';
						tr += '<td class="portfolio-tdcenter" style="text-align:left;">';
						tr += '<p>'+stockName+' </p>';
						tr += '<p class="kyc-fund-num">'+stockCode+'</p>';
						tr += '</td>';
						tr += '<td class="portfolio-tdcenter">'+transactionTypeName+'</td>';
						tr += '<td class="portfolio-tdcenter">'+currency+'</td>';
						tr += '<td class="portfolio-tdcenter">'+fmoney(amount,0)+'</td>';
						tr += '<td class="portfolio-tdcenter">'+quantity+'</td>';
						tr += '<td class="portfolio-tdcenter">'+orderNumber+'</td>';
						tr += '<td class="portfolio-tdcenter">'+remark+'</td>';
						tr += '</tr>';
						tr_html += tr;
				});
				
				$('#table-order-return tbody').append(tr_html);
				
				$("#pagination").pagination(total, {
					callback : pageselectCallback,
					numDetail : '',
					items_per_page : 5,
					num_display_entries : 4,
					current_page : pageid,
					num_edge_entries : 2
				});
			}
		});
 		
 		function pageselectCallback(page_id, jq) {
 			loadOrderReturnList(dataValue,transactionType,page_id);
		}
 	}
	
	$("#showPdf").on("click",function(){
		/*layer.open({
		  type: 2,
		  title: false,
		  closeBtn: 0,
		  area:  [ '1000px', '516px' ],
		  shadeClose: true,
		  content: base_root+'/front/crm/pipeline/dialogSelectClient.do'
		});*/
	})
	
	//RPQ 列表修改   wwluo 20161101
	$('.rpq-td-title').click(function(){
		var examId = $(this).closest('tr').attr('id');
		var	url = base_root + '/front/rpq/detail.do?examId=' + examId;
		window.location.href = url;
	});
	$('#btn-addRpq').click(function(){
		var url = base_root + '/front/rpq/fillRpqPaper.do?accountId='+_id+'&distributorId='+_distributorId;
		window.location.href = url;
	});
	
	$(".proposal_xiala").on("click",function(){
		$(this).toggleClass("xiala-show");
	});
	
	$('.proposal_xiala').on('mouseleave',function(){
		$(this).removeClass('xiala-show');
	});
	
	$(".account-cur-choose li").on("click",function(e){
		$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input").val($(this).html());
		$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input").attr("code",$(this).attr("code"));
		e.stopPropagation(); 
		var currency=$("#currency").attr("code");
		window.location.href=base_root+"/front/investor/myAccountDetail.do?id="+_id+"&cur="+currency;
	});
	
	$(".acc-transaction-choose li").on("click",function(e){
		var dataValue = $(this).attr('data-value'); 
		$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input").val($(this).html()).attr('data-value',dataValue);
		var choiceType = $('.conservative-choice-active2').attr('data-value');
		loadOrderReturnList(choiceType,dataValue,0);
		e.stopPropagation(); 
	});
	//显示每条背景数据信息
	$("body").on('click', '.background-table-tr', '', function () {
		var id = $(this).attr('lbid');
		showBackgroundViewDialog(id);
	});
	
	function showBackgroundViewDialog(id){
		layer.open({
			  type: 2,
			  title: '',
			  shadeClose: true,
			  //shade: 0.8,
			  area: ['1000px', '90%'],
			  content: base_root + "/front/investor/getInvestorBackground.do?id=" + id //iframe的url
			}); 
	}
	
//	//显示每条培训数据信息
//	$("body").on('click', '.a_trainingName', '', function () {
//		var id = $(this).attr('lbid');
//		showTrainingViewDialog(id);
//	});
	
	function showTrainingViewDialog(id){
		layer.open({
			  type: 2,
			  title: '',
			  shadeClose: true,
			  //shade: 0.8,
			  area: ['1000px', '400px'],
			  content: base_root + "/front/investor/getInvestorTraining.do?id=" + id //iframe的url
			}); 
	}
	
	//修改培训信息
	$("body").on('click', '.a_modifyTraining', '', function () {
		$('.application-title-training-record').text(props['account.detail.kyc.trainingrecord.form.edit.title']);
		initTrainingDialog();
		var id = $(this).attr('lbid');
		editTrainingViewDialog(id);
	});
	//弹出需要修改的表单
	function editTrainingViewDialog(id){
		$.ajax({
			type : 'get',
			datatype : 'json',
			url : base_root + "/front/investor/getInvestorTrainingJson.do?datestr="+ new Date().getTime(),
			data : {'id':id},
			 beforeSend: function () {},
			 complete: function () {},
			 success : function(json) {
				var content = json.content;
				var createTime = json.createTime;
				var endTime = json.endTime;
				var startTime = json.startTime;
				var id = json.id;
				var name = json.name;
				var organization = json.organization;
				var li_html = '';
				var filelist = json.fileList;
				$.each(filelist,function(i,n){
					var filetype = n.fileType;
					var filename = n.fileName;
					var filePath = n.thumbnailFilePath112x100;
					var filePath_ = n.filePath;
					var id = n.id;
					var li = 
					 '<li>'
					+'<input type="hidden" class="upload-eachimg-cla" name="hid_photo_name" value="'+filetype+'|'+filename+'|'+filename+'|'+filePath_+'">'
					+'<div class="img-box selected" onclick="javascript:void(0);">'
					+'<img imgindex="0" id="img_" width="112" height="100" class="upload-eachimg upload-eachimg-click" src="' + base_root + '/loadImgSrcByPath.do?filePath=' + filePath + '" bigsrc="' + filePath_ + '">'
					+'</div>'
					+'<div class="delatebc">'
					+'<a id="delete_'+ id +'" class="class-delimg a-href" href="javascript:;"></a>&nbsp;&nbsp;'
					+'<a id="download_'+ id +'" class="class-download a-href" filepath="' + filePath_ + '" href="javascript:void(0);"></a>'
					+'</div>'
					+'</li>';
					li_html += li;
				});
				$('.photo-list ul').append(li_html);
				$('#txtName').val(name);
				$('#txtStartime').val(startTime);
				$('#txtEndtime').val(endTime);
				$('#txtOrganization').val(organization);
				$('#areDescription').val(content);
				$('#hidTrainingId').val(id);
				layer.open({
			      type: 1,
			      title: '',
			      shadeClose: false,
			      area: ['1000px', '580px'],
			      anim:1,
			      content: $('#trainingForm'),
			      success: function(layero, index){
			    	  $(".upload-album").InitUploader({ uploadtype:'image',bgimagetype:2,btntext: "", multiple: true, water: true, thumbnail: true, filesize: "8000",modulerelate:"training"});
			      }
			    });
				$('.class-delimg').unbind('click');
			    $('.class-delimg').on('click',function(){
			    	var parentObj = $(this).parent().parent();
			        var focusPhotoObj = parentObj.parent().siblings(".focus-photo");
			        var smallImg = $(this).siblings(".img-box").children("img").attr("src");
			        $(this).closest('li').remove(); 
			    });
			    $('.class-download').unbind('click');
				$('.class-download').on('click',function(){ 
			    	var filepath = $(this).attr('filepath');
				    var a = $('<a></a>').attr('target','_self').attr('href', base_root+'/download.do?fileName='+filepath).appendTo('body');
				    a[0].click();
				    a.remove();
			    });
				$('.upload-eachimg-click').unbind('click');
			    $('.upload-eachimg-click').on('click',function(){
				  imageReview(this);
		        });
			}
		});
	}
	function imageReview(self){
		//每次点击时获取所有图片列表
    	var imgindex = $(self).attr('imgindex');
		var html = '<div class="swiper-container gallery-top">';
		html += '<div class="swiper-wrapper">';
		var img_list = $('.photo-list').find('ul');
		var thumbs_html='';
		thumbs_html += '<div class="swiper-container gallery-thumbs">';
		thumbs_html += '<div class="swiper-wrapper">';
		$(img_list.html()).find('.img-box').each(function(i){
			var big_src = $(this).find('img').attr('bigsrc');
			var thumb_src = $(this).find('img').attr('src');
			var li_html = '<div class="swiper-slide">';
			li_html += '<img data-src="'+ base_root + "/loadImgSrcByPath.do?filePath=" + big_src + '" class="swiper-lazy">';
			li_html += '<div class="swiper-lazy-preloader swiper-lazy-preloader-white"></div>';
			li_html += '</div>';
			html += li_html;
			thumbs_html += '<div class="swiper-slide" style="background-image:url('+thumb_src+');background-repeat:no-repeat;width:112px;height:100px;"></div>';
		});
		html += '</div>';
		html += '<div class="swiper-pagination swiper-pagination-white"></div>';
		html += '<div class="swiper-button-next swiper-button-white"></div>';
		html += '<div class="swiper-button-prev swiper-button-white"></div>';
		html += '</div>';
		//缩 略图
		thumbs_html += '</div>';
		thumbs_html += '</div>';
		html += thumbs_html;
		layer.open({
		  type: 1,
		  title:'',
		  shade: 1,
		  shadeClose:true, 
		  skin: '', //加上边框
		  area: ['90%', '83%'], //宽高
		  content: html,
		  success:function(){ 
			  var galleryTop = new Swiper('.gallery-top', {
			        nextButton: '.swiper-button-next',
			        prevButton: '.swiper-button-prev',
			        pagination: '.swiper-pagination',
			        paginationClickable: true,
			        preloadImages: false,
			        initialSlide :imgindex=='0'?0:imgindex,//计算元素时可能会出现延迟，导致//可能会出现延迟imgindex没有值
			        lazyLoading: true
			    });
			  var galleryThumbs = new Swiper('.gallery-thumbs', {
			        spaceBetween: 10,
			        centeredSlides: true,
			        slidesPerView: 'auto',
			        touchRatio: 0.2,
			        initialSlide :imgindex=='0'?0:imgindex,//可能会出现延迟,导致//可能会出现延迟imgindex没有值
			        slideToClickedSlide: true
			    });
			  galleryTop.params.control = galleryThumbs;
			  galleryThumbs.params.control = galleryTop;
			  $('.layui-layer-close2').css({'z-index':'999'});
			  $('.gallery-thumbs').css('background','#2b2c30');
		  }
		});
	}
	//修改培训信息
	$("body").on('click', '.training-cancel-bn', '', function () {
		layer.closeAll();
	});
	
	

/**文档审查历史swarper*/
	// list逻辑
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
			$(".detail-view-filelist").on("click",".filelist-image-click",function(){
				var swiper_html = "";
		        $(".filelist-images").each(function(){
		            swiper_html += '<div class="swiper-slide" style="background-image:url('+ $(this).attr("data-src") +')"></div>'
		        });

		        $("#galler-big-img").html(swiper_html);
		        $("#galler-small-img").html(swiper_html);

				List.bigImages(1);
				$(".gallery-bigger-images").addClass("gallery-bigger-blocl");
			});

			$(document).on("click","#pipelin-investors-close",function(){
				$(".gallery-bigger-images").removeClass("gallery-bigger-blocl");
			});
			
			//文档更新
			$("#update-show").on("click",function(){
				var docId = $("#docId_for_update").val();
				docUpdateGrid(docId);
				$("#view-filelist-update").addClass("view-filelist-show");
			});

			$("#view-update-close").on("click",function(){
				$("#view-filelist-update").removeClass("view-filelist-show");
			});

			//文档详情
			$(".document_img").on("click",function(){
				//var docId = $(this).attr("docid");
				//docDetailGrid(docId);
				//$(".detail-view-filelist").addClass("view-filelist-show");
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

    //文档退回
    $('#reject-show').on('click',function(){
        layer.open({
        	title:'Reject',
        	content:'<textarea id="reject_result"></textarea>',
        	btn:['Reject','Cancle'],
        	yes:function(index,layro){
        		$("#check_result").val($('#reject_result').val())
        		$("#check_status").val("2");
        		$("#checkForm").ajaxSubmit({
        			url:base_root+ "/front/kycDoc/kycDocApprove.do?r="+Math.random(),
        			success:function(data){
        				if(data.result){
        					var docId = $("#docId_for_update").val();
        					docDetailGrid(docId);
        				}
        			},
        			error:function(){
        				layer.msg("error", {icon: 5});
        			}
        		});
        		layer.close(index);
        	}
        });
    });
	
	//文档详情grid生成/更新后grid刷新
	function docDetailGrid(docId){
		$.ajax({
			url:base_root+'/front/kycDoc/docDetail.do?docId='+docId+'&r='+Math.random(),
			success:function(data){
				//生成HTML
				if(data){
					$("#docId_for_update").val(docId);
					$("#docCheck_id").val(docId);
					//文档信息
					var docInfo = data.investorDocInfo;//文档信息
					var imgs = data.docImages;//图片信息
					var checkStatus = docInfo.checkStatus;//审批状态0.审批中  1.审批通过 2.审批退回
					var checkList = data.checkList;//审批信息
					//按钮权限
					if('0'==checkStatus){//审批中
						if("31"!=sub_user_type){//非代理商登录
							$(".filelist-contents-heading").remove();//按钮区域
						}else{
							$("#update-show").remove();
						}
					}else if('1'==checkStatus){//审批通过
						if(!!data.compareDateFlag ){//true未过期，false过期
							$(".filelist-contents-heading").remove();//按钮区域
						}else{
							if("11"!=sub_user_type){
								$(".filelist-contents-title").show();//过期/更新提示
								$(".checkBtn").remove();
							}
						}
					}else{//退回
						if("11"!=sub_user_type){//非投资人登录
							$(".filelist-contents-heading").remove();//更新按钮区域
						}else{
							$(".checkBtn").remove();
						}
					}
					
					$(".view-filelist-heading").html(docInfo.docName);
					$("#updateCycle").html(docInfo.updateCycle+"M");
					$("#lastUpdate").html(docInfo.lastUpdate);
					var imgsContent = $(".filelist-images-list");//图片列容器
					imgsContent.children().remove();//清空图片列表
					
					var imgsHtml = "";
					for(var x=0;x<imgs.length;x++){
						var imgsPath = base_root + imgs[x].filePath;
						imgsHtml +='<li class="filelist-images-rows filelist-image-click">'+
									'<img class="filelist-images"  data-src="'+imgsPath +'" src="'+imgsPath+'"/>'+
									'</li>';
					}
					imgsContent.append(imgsHtml);
					
					//审批信息列
					if(!!checkList){
						var checkListTr = $("#checkListTr");
						checkListTr.siblings().remove();//清空审判里列表，避免重复添加
						
						var checkListObj = "";
						for(var y=0;y<checkList.length;y++){
							var checkObj = checkList[y];
							var checkStatus = checkObj.checkStatus;
							if('1'==checkStatus){
								checkVal = 'Pass';
							}else if('2'==checkStatus){
								checkVal ='Reject';
							}else{
								checkVal = 'Submit';
							}
							var checkRole = checkObj.checkRole=='D'?'Distributor':'Investor';
							
							checkListObj += '<tr>'+
                            				'<td class="portfolio-tdcenter">'+(y+1)+'</td>'+
                            				'<td class="portfolio-tdleft">'+checkObj.checkTime+'</td>'+
                            				'<td class="portfolio-tdcenter">'+checkObj.loginCode+'</td>'+
                            				'<td class="portfolio-tdcenter">'+checkVal+'</td>'+
                            				'<td class="portfolio-tdcenter">'+checkObj.checkResult+'</td>'+
                            				'<td class="portfolio-tdcenter">'+checkRole+'</td>'+
                            				'</tr>';
						}
						checkListTr.after(checkListObj);
					}
				}
			}
		});		
	}
	
	//文档更新grid生成
	function docUpdateGrid(docId){
		$.ajax({
			url:base_root+'/front/kycDoc/docDetailEdit.do?docId='+docId+'&r='+Math.random(),
			type:"get",
			dataType:"json",
			success:function(data){
				//生成文档img列表
				if(!!data){
					var doc_info = data.investorDocInfo;
					$(".view-update-word").html(doc_info.docName);
					var docImages = data.docImages;
					var docUpdateGrid = "";
					//清空原列表
					$(".view-update-add").siblings().remove();
					for(var k=0;k<docImages.length;k++){
						var imgId = docImages[k].id;
						var imgPath = base_root+docImages[k].filePath;
						docUpdateGrid += '<li class="img_li view-update-rows" fileid="'+imgId+'" id="img_li_'+imgId+'">';
						docUpdateGrid += '<p class="imgWrap"><img class="view-update-images" src="'+imgPath+'"></p>';
						docUpdateGrid += '<div class="file-panel div_btn" >';
						docUpdateGrid += '<span class="cancel" delId="'+imgId+'">删除</span>'
						docUpdateGrid += '<span class="thumb">预览</span>';
						//docUpdateGrid += '<img src="${base}${image.filePath!}" style="display:none"/>';
						docUpdateGrid += '</div></li>';
					}
					$(".view-update-add").after(docUpdateGrid);
					$("#docUpdate_id").val(doc_info.id);
					
					//图片操作按钮
					// $(".img_li").on( 'mouseenter', function() {
					// 	$(this).children("div").stop().animate({height: 30});;
					// });

					// $(".img_li").on( 'mouseleave', function() {
					// 	$(this).children("div").stop().animate({height: 0});
					// });

					$(".div_btn").on( 'click', 'span', function() {
						var index = $(this).index();
					    switch ( index ) {
					        case 0://删除附件dom,提交 
					        	var delId = $(this).attr("delId");
					        	var delFileIds = $("#deleteFileArray").val();
					        	delFileIds += delId+",";
					        	$("#deleteFileArray").val(delFileIds);
					        	$(this).parents("li").remove();
					            return;
					        case 1://预览
					        	var imgSrc = $(this).next().attr("src");
					        	//$(".doc_imgWrap").show();
					        	parent.parent.layer.open({
					        		  type: 2,
					        		  title: false,
					        		  area: ['630px', '360px'],
					        		  shade: 0.8,
					        		  closeBtn: 0,
					        		  shadeClose: true,
					        		  content: imgSrc
					        		});
					            break;
					    }
					});	
				}
			},
			error:function(data){
				
			}
		});
	}

	$(".client-detail-button").on("click",function(){
		var index = layer.open({
			skin : 'layui-layer-lan',
			title : '查看pdf',
			type : 2,
			content : base_root+'/front/investor/dialogshowpdf.do',
			area : [ '320px', '195px' ],
			maxmin : true
		});
		layer.full(index);
	});

	$(".client-more-ico").on("click",function(){
		$(this).closest("ul").next('.client-detail-mainc').toggleClass("client-more-ico-active");
	});
	
	//点击各个类型获取持仓的产品信息
	$('.wmes-community-tab li').on('click',function(){
		$(this).addClass('wmes-community-tabac').siblings().removeClass('wmes-community-tabac');
		$('.myAccountDetail-type-con').eq($(this).index()).addClass('myAccountDetail-type-conac').siblings().removeClass('myAccountDetail-type-conac');
		var typeid = $(this).attr('typeid');
		var accountId = getQueryString('id'); 
		$.ajax({
			type : 'get',
			datatype : 'json',
			url : base_root + "/front/investor/getPortfolioHoldProductByType.do?datestr="+ new Date().getTime(),
			data : {'type':typeid,'id':accountId},
			 beforeSend: function () {},
			 complete: function () {},
			 success : function(json) {
				////console.log(json)
				var tr_html = '';
				var length = json.length;
				
				$.each(json,function(i, n) { 
				var accountId = n.accountId;
				var accountNo = n.accountNo;
				var availableUnit = formatMoney(n.availableUnit,'');
				var baseCurrency = n.baseCurrency;
				var holdingUnit = formatMoney(n.holdingUnit,'');
				var latestMarketPrice =formatMoney( n.latestMarketPrice,'');
				var memberId = n.memberId;
				var number = n.number;
				var productInformation = n.productInformation;
				var referencecost = formatMoney(n.referencecost,'');
				var tr = '<tr>'
					tr += '<td class="portfolio-tdcenter">'+number+'</td>'
					tr += '<td class="portfolio-tdcenter">'+productInformation+'</td>'
					tr += '<td class="portfolio-tdcenter">'+baseCurrency+' </td>'
					tr += '<td class="portfolio-tdcenter"><font class="price_zero">'+latestMarketPrice+'</font></td>'
					tr += '<td class="portfolio-tdcenter">'+holdingUnit+'</td>'
					tr += ' <td class="portfolio-tdcenter">'+availableUnit+'</td>'
					tr += '<td class="portfolio-tdcenter">'+referencecost+'</td>'
					tr += '</tr>';
					tr_html += tr;
				}); 
				//$('#table-hoding-list').remove().find('tr:eq(1)').empty();
				////console.log($('#table-hoding-list'));
				$("#table-hoding-list tr").eq(0).nextAll().remove();
				$('#table-hoding-list').append(tr_html);
				if(length==0)$('.portfolio-table-tips').show();
				else $('.portfolio-table-tips').hide();
			}
		});
		
	});
	
	function fmoney(s, n)
	{
	   n = n > 0 && n <= 20 ? n : 2;
	   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
	   var l = s.split(".")[0].split("").reverse(),
	   r = s.split(".")[1];
	   t = "";
	   for(i = 0; i < l.length; i ++ )
	   {
	      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "")
	   }
	   return t.split("").reverse().join("") + "." + r;
	}
	
	//选中文档审查
	if(!!GetQueryString("flag") && "12"==GetQueryString("flag")){
		//alert($(".clientDetail_tab li").siblings().eq(1).html());
		var docCheckTab = $(".clientDetail_tab li").siblings().eq(1);
		docCheckTab.click();
		$('html, body').animate({  
            scrollTop: docCheckTab.offset().top  
        }, 10);
	}
	
	$('#txtStartime').click(function(){
		laydate({
		   istime: false,
		   elem: '#txtStartime',
		   format: 'YYYY-MM-DD',
		   istoday: false,
		   choose: function(datas){
			   if($('#txtEndtime').val() != ''){
				   var timeE = new Date($('#txtEndtime').val()).getTime();
				   var timeS = new Date(datas).getTime();
				   if(timeS > timeE){
					   $('#txtEndtime').val(datas);
				   }
			   }
			   if($('#txtStartime').val() != '' && $('#txtEndtime').val() != ''){
				   $('.input-empty-valid-time').hide();
			   }
		   } 
		});
	});
	$('#txtEndtime').click(function(){
		laydate({
			istime: false,
			elem: '#txtEndtime',
			format: 'YYYY-MM-DD',
			istoday: false,
			choose: function(datas){
				if($('#txtStartime').val() != ''){
				   var timeS = new Date($('#txtStartime').val()).getTime();
				   var timeE = new Date(datas).getTime();
				   if(timeS > timeE){
					   $('#txtStartime').val(datas);
				   }
			   }
			   if($('#txtStartime').val() != '' && $('#txtEndtime').val() != ''){
				   $('.input-empty-valid-time').hide();
			   }
			} 
		});
	});
	function getTrainings(){
		var url = base_root + '/front/investor/getTrainings.do?d=' + new Date().getTime();
		var accountId = $('#hidAccountId').val();
		$.ajax({
			type:'post',
			url:url,
			data:{
				accountId:accountId
			},
			success:function(re){
				if(re.flag && re.trainings != null){
					var trainings = re.trainings;
					var memberType = re.memberType;
					var html = '';
					$.each(trainings,function(i,n){
						var actionHtml = '';
						if(memberType == 1){ 
							// investor
							actionHtml = '<img class="client-table-tr-img" data-id="'+ n.id +'" style="width:22px;height:22px;" src="'+ base_root +'/res/images/ifa/garbage_ico.png">'
										+'<img lbid="'+ n.id +'" class="a_modifyTraining" style="width:22px;height:22px;" src="'+ base_root +'/res/images/ifa/modify_ico.png">';
						}
						var name = n.name == null ? '' : n.name;
						var startTime = n.startTime == null ? '' : n.startTime;
						var endTime = n.endTime == null ? '' : n.endTime;
						var organization = n.organization == null ? '' : n.organization;
						//var content = n.content == null ? '' : n.content;
						html +=
						 '<tr class="training-table-tr" lbid="'+ n.id +'">'
                        +'<td class="portfolio-tdcenter">'+ (i + 1) +'</td>'
                        +'<td class="portfolio-tdcenter"><a lbid="'+ n.id +'" class="a_trainingName" href="javascript:void(0);">'
                        + name
                        +'</a></td>'
                        +'<td class="portfolio-tdcenter">'
                        + startTime + ' ~ ' + endTime
                        +'</td><td class="portfolio-tdcenter">'
                        + organization
                        +'</td><td class="portfolio-tdcenter">'
                        + actionHtml
                        +'</td>'
                        +'</tr>';
					});
					$('.table-account-training tr:gt(0)').remove();
					if(html == ''){
						html = '<tr>'
                        +'<td colspan="5" class="portfolio-tdcenter">'
                        +'<div class="wmes-nodata-tips portfolioTips" style="display:block;padding-top:50px;">'
                        +'<img class="wmes-nodata-img" src="' + base_root + '/res/images/no_data_ico.png">'
                        +'<span class="wmes-nodate-word">' + props["global.nodata.tips"] + '</span>'
                        +'</div>'
                        +'</td>'
                        +'</tr>';
					}
					$('.table-account-training').append(html);
				}
			}
		});
	}
	function initTrainingDialog(){
		$(".upload-album").empty();
		$(".photo-list ul").empty();
		$('#hidTrainingId').val('');
		$('#txtName').val('');
		$('#txtStartime').val('');
		$('#txtEndtime').val('');
		$('#txtOrganization').val('');
		$('#txtOrganization').val('');
		$('#areDescription').val('');
	}
	
	$(document).on('click','.wmes-close',function(){
		$(this).closest('.layui-layer').find('.layui-layer-close2').click();
	});
});