
$(function(){


		function assetChart(){
			$(".client-chart").each(function(){
				var option = {
				    
				    legend: {
				    	orient : 'vertical',
				        x: 'right',
				        itemWidth:'15',
				        itemHeight:'15',
				        y:'60px',
				        data:['Cash','Market Value']
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
				       
				            data:[
				                {value:20, name:'Cash'},
				                {value:80, name:'Market Value'}
				            ]
				        }
				    ]
				};

				var myChart = echarts.init($(this)[0]);
	  			myChart.setOption(option,true);
			});
		}
		assetChart();


		// 下拉
		$(".proposal_xiala").on("click",function(){
			$(this).toggleClass("xiala-show");
		});
		$(".proposal_xiala li").on("click",function(e){
			$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input").val($(this).html().trim());
			e.stopPropagation(); 
		});
		$("#in_use").on("change", function () {
			 refreshform();
		 })
		 $("#InApproval").on("change", function () {
			 refreshform();
		 })
		 $("#Cancellation").on("change", function () {
			 refreshform();
		 })
		 
		 function loadList(){
			selection();
			refreshform();
		}
		 
		 function refreshform(){
			 selection();
			 var ifafirmId="";
			 
			 $(".selection_criteria li").each(function(){
				 if($(this).attr("data-name")=='ifafirm'){
					 ifafirmId=$(this).attr("data-value");
				 }
			 })
			 
			 var in_use="";
			 var inApproval="";
			 var cancellation="";
			 if($("#in_use").is(':checked')){
				 in_use="1";
			 }else{
				 in_use="";
			 }
				
			 if($("#InApproval").is(':checked')){
				 inApproval="1";
			 }else{
				 inApproval=""; 
			 }
				
			 if($("#Cancellation").is(':checked')){
				 cancellation="1";
			 }else{
				 cancellation="";
			 }
				
			 var currency=$("#currency").val();
			 var keyWord=$("#txtKeyWord").val();
			 var url="/wmes/console/distributor/info/accountList.do?in_use="+in_use+"&inApproval="+inApproval+"&cancellation="+cancellation+"&cur="+currency;
			 $.ajax({
				 type:'post',
				 datatype:'json',
				 url:url,
				 data:{ifafirmId:ifafirmId,clientName:keyWord},
				 success:function(json){
					 if(json.accountList!=undefined){
						 var html="";
						 $.each(json.accountList,function(i,n){
							 var cies=n.cies;
							 var faca=n.faca;
							 var subFlag=n.subFlag;
							 var subFlagValue="";
							 if(subFlag=='0')
								 subFlagValue='Master Account';
							 else if(subFlag=='1')
								 subFlagValue='Sub Account';
							 if(n.openStatus=="1"){
								
								 html+='<div class="account-list-contsnts">'+
            		                   '<div class="client-list-cleft">'+
            			               '<div class="client-list-nwrap">'+
            				           '<p class="client-list-name"><span>'+n.accountNo+'</span>'+n.loginCode+'</p>'+
            				           '<ul class="client-list-describe">';
								 if(subFlagValue!=''){
									 html+='<li>'+subFlagValue+'</li>';
								 }
								 html+= '<li>4 Risk</li>';
								 if(cies!=''){
									 html+='<li>'+cies+'</li>';
								 }
								 if(faca!=''){
									 html+='<li>'+faca+'</li>';
								 }
								 html+='</ul>'+
            			               '</div>'+
            			               '<div class="client-list-days">'+
            				           '<img class="client-invest-ico" src="/wmes/res/images/ifa/invest_ico.png">'+
            				           '<div class="client-half-day">'+
            					       '<div>'+
            					 	   '<p class="client-half-word">'+n.nextRPQDate+'<span>Days</span></p>'+
            						   '<p class="client-half-time">Next RPQ Time</p>'+
            					       '</div>'+
            					       '<div>'+
            						   '<p class="client-half-word">'+n.nextDCDate+'<span>Days</span></p>'+
            						   '<p class="client-half-time">Next Document Check Time</p>'+
            					       '</div>'+
            				           '</div>'+
            			               '</div>'+
            		                   '</div>'+
            		
            		                   '<div class="client-list-cright">'+
            			               '<p class="client-chart-cur">Basic CUR <span>'+n.baseCurrency+'</span></p>'+
            			               '<p class="client-total">'+n.totalAssest+'</p>'+
            			               '<p class="client-total-word">Total AuM</p>'+
            			               '<div class="client-list-chart">'+
            			               '<div class="client-chart"></div>'+
            			               '</div>'+
            			               '<div class="client-list-total">'+
            			               '<p class="client-list-hkd">'+n.cash+'</p>'+
            			               '<p class="client-list-hkd">'+n.productValue+'</p>'+
            			               '</div>'+
            		                   '</div>'+
            		                   '</div>';
							 }else{
								html+='<div class="client-list-approval">'+
				            		  '<div class="client-list-cleft">'+
		            			      '<div class="client-list-nwrap">'+
		            				  '<p class="client-list-name"><span>N/A</span> '+n.loginCode+'</p>'+
		            				  '<ul class="client-list-describe">';
								if(subFlagValue!=''){
									 html+='<li>'+subFlagValue+'</li>';
								 }
								 html+= '<li>4 Risk</li>';
								 if(cies!=''){
									 html+='<li>'+cies+'</li>';
								 }
								 if(faca!=''){
									 html+='<li>'+faca+'</li>';
								 }
		            					
								 html+='</ul>'+
		            			       '</div>'+
		            			       '<div class="client-list-days">'+
		            				   '<img class="client-invest-ico" src="/wmes/res/images/ifa/invest_ico_02.png">'+
		            				   '<div class="client-half-day">'+
		            				   '<div>'+
		            				   '<p class="client-half-word">N/A</p>'+
		            				   '<p class="client-half-time">Next RPQ Time</p>'+
		            				   '</div>'+
		            				   '<div>'+
		            				   '<p class="client-half-word">N/A</p>'+
		            				   '<p class="client-half-time">Next Document Check Time</p>'+
		            				   '</div>'+
		            				   '</div>'+
		            			       '</div>'+
		            		           '</div>'+
		            		           '<img class="account-list-old" src="/wmes/res/images/client/in_old_ico.png">'+
		            	               '</div>';
							 }
							
						 });
						 $('#list').empty().append(html);
					 }
					 /*if(json.distributorList!=undefined){
						 var html='<li class="conservative-choice-active">All</li>';
						 $.each(json.distributorList,function(i,n){
							 html+='<li data-name="distributor" data-key="'+n.companyName+'" data-value="'+n.id+'">'+n.companyName+'</li>';
						 });
						 $('.conservative-choice').empty().append(html);
						 
					 }*/
					 assetChart();
				 }
			 })
		 }
		
		
		//点击每个选项，在下面的已选方案中添加该选项
		var listTime;
		$(".conservative-choice li").on("click",function(){
			clearTimeout(listTime)
			if($(this).parent().hasClass("funds_logo_b")){
				return;
			}
			var selection_criterialenght = $(".selection_criteria li").length;
			for(var i = 0; i < selection_criterialenght ;i++){
					if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") || $(this).text()=='All' ){
						$(".selection_criteria li").eq(i).remove();
					}
			}
			$(this).siblings().removeClass("conservative-choice-active").end().addClass("conservative-choice-active");
			if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
				$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
			}

			// 解决重复请求的问题
			//var self = this;
				listTime=setTimeout(function(){
					loadList();//loadIFAList(self);
				}
				,100);
		});
		//执行清除方案点击操作
		$(".funds_title_selection").on("click",function(){
			$(".selection_criteria li").remove();

			$(".fund_logo_active").removeClass("fund_logo_active");
			$(".conservative-choice-active").removeClass("conservative-choice-active");
			$(".conservative_all").addClass("conservative-choice-active");
			$("#listForm").find("input").val("");
			loadList();
		});
		$('.conservative-choice-button').on('click',function(){
			loadList();
		})
		
		function selection(){
			var _thisLenght =  $(".selection_criteria").children().length;
			
			if( _thisLenght != 1  ){
				$(".funds_title_selection").css('display','inline-block');
			}else{
				$(".funds_title_selection").css('display','none');
			}
		}
});