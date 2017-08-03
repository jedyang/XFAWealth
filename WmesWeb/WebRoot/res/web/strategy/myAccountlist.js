
define(function(require) {

	var $ = require('jquery');
			require("echarts");
		$(".account-chart").each(function(){
			var option = {
			    
			    legend: {
			    	orient : 'vertical',
			        x: 'right',
			        y:'40px',
			        data:['cash','Product Value']
			    },
			    series: [
			        {
			            name:'account',
			            type:'pie',
			            center:	['35%', '50%'],
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
	$(".proposal-more-ico").on("click",function(){
		$(this).parents(".account-rows").toggleClass("account-rows-hide");
	});
	// 下拉
		$(".proposal_xiala").on("click",function(){
			$(this).toggleClass("xiala-show");
		});
		$(".proposal_xiala li").on("click",function(e){
			$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input").val($(this).html());
			e.stopPropagation(); 
		});

		
});