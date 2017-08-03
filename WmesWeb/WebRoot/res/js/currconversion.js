/**
 * Currency conversion.js  通用货币转换JS
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-07-04
 */


define(function(require) {

	var $ = require('jquery');

	function conversion(conversionStr,conversionName){
		var self = this;
		$.ajax({
			url:base_root +"/front/fund/info/fundExchage.do?r="+Math.random(),
			type:"get",
			dataType:"json",
			success:function(request){
				var conversionJson = request;
				//conversionJson = JSON.parse(conversionJson);

				if(!conversionJson.result){
					$.Tips({ content: "Failed to get the exchange rate" });
					return;
				}

				self.conversionStr = conversionStr;
				self.conversionName = conversionName;
				self.conversionData = conversionJson.list;
				self.Classnameinit();
			},
			error:function(){
				
			}
		})
	}
	conversion.prototype = {
		/*
		 * Title : 类名货币转换
		 * Class : Currency_conversion
		 * Tips  : 匹配该类名进行汇率修改
		 */
		Classnameinit : function(){
			var self = this;
			// 循环带有货币标识的地方
			$(".Currency_conversion").each(function(){
				for(var data_item in self.conversionData){
					if(self.conversionStr == $(this).attr("data-exchange")){
						$(this).find(".conversion_Num").html($(this).attr("data-original"));
						$(this).find(".conversion_Type").html(self.conversionName);
						break;
					}else if(self.conversionStr == "Original"){
						//默认货币
						$(this).find(".conversion_Num").html($(this).attr("data-original"));
						$(this).find(".conversion_Type").html($(this).attr("data-name"));
					}
					else if
					( 
					self.conversionData[data_item].fromCurrency == $(this).attr("data-exchange") && 
					self.conversionData[data_item].toCurrency == self.conversionStr
					)
					{	
//						console.log( self.conversionData[data_item].toCurrency + '---' + $(this).attr("data-exchange") + '---' + self.conversionData[data_item].fromCurrency + '---' + self.conversionStr + '---' +$(this).attr("data-original") + '---' + self.conversionData[data_item].rate)
						var resultsHtml = Number($(this).attr("data-original")) * Number(self.conversionData[data_item].rate);
						$(this).find(".conversion_Num").html(resultsHtml.toFixed(2));
						$(this).find(".conversion_Type").html(self.conversionName);
						break;
					}
				} 

			});
		},

		/*
		 * 开始时页面货币处理
		 */
		 conversionAgain : function(){
		 	//do something
		 }
	}

	
	return conversion;
});