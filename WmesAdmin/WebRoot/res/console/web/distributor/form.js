
define(function(require) {

	var $ = require('jquery');
		require("echarts");
		require('layer');
		require('datepick');
		require("swiper");
		require("jqueryForm");
		require("wmes_upload");
	
		//先gen出国家数据
		loadCountry();
		//保存公司信息
		$('#btnSave1').on('click',saveFirm);
		//gen出国家数据
		function loadCountry()
		{
			$.ajax({
            type: "post",
            url : "${base}/console/country/langListJson.do?datestr="+ new Date().getTime(),
            data: {},
            async: false,
            dataType: "json",
            beforeSend: function () {
                    },
            error: function (XMLHttpRequest, textStatus, errorThrown) {

            },
            success: function (json) {
            	var selCountry = $("#selCountry").attr("sel");
            	var selPlace = $("#selIncorporationPlace").attr("sel");
            	if (json != undefined && json.result == true) {
						var data = JSON.parse(json.countryJson);
						var html = "<option value=''>请选择国家</option>";
						$.each(data, function(i, n) {
							html += "<option value='"+n.id+"'>" + n.name + "</option>";
						});
						$("#selIncorporationPlace").append(html);
						$("#selCountry").append(html);
						$("#selIncorporationPlace").find("option[value='"+selPlace+"']").attr("selected",true);
						$("#selCountry").find("option[value='"+selCountry+"']").attr("selected",true);
					}
	            }
        	});
		}
		$(".upload-album").InitUploader({ btntext: "批量上传", multiple: true, water: true, thumbnail: true, filesize: "8000",modulerelate:"training" });
		//提交验证
		$("#frm").bootstrapValidator({
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				companyName : {
					message : '',
					validators : {
						notEmpty : {
							message : '代理商名称不能为空'
						}
						
					}
				},
				registerNo : {
					validators : {
						notEmpty : {
							message : '注册代码不能为空'
						}
					}
				}
			}
		}).on('success.form.bv', function(e) {
			e.preventDefault();
			saveFirm();
		});
		//保存
		function saveFirm() {
			var id = getQueryString('id');
			var companyName = $('#txtCompanyName').val();
			var entitytype = $('#selEntitytype').val();
			var registerNo = $('#txtRegisterNo').val();
			var isFinancial = $('#selIsFinancial').val();
			var giin = $('#txtGiin').val();
			var incorporationPlace = $('#selIncorporationPlace').val();
			var registeredAddress = $('#txtRegisteredAddress').val();
			var mailingAddress = $('#txtMailingAddress').val();
			var naturePurpose = $('#txtNaturePurpose').val();
			var country = $('#selCountry').val();
			var website = $('#txtWebsite').val();
			var incorporationDate = $('#dtIncorporationDate').val();
			var postData = { 'id':id,'companyName': companyName, 'entitytype': entitytype, 'registerNo': registerNo, 'isFinancial': isFinancial, 'giin': giin, 'incorporationPlace': incorporationPlace, 'registeredAddress': registeredAddress, 'mailingAddress': mailingAddress, 'country': country,'naturePurpose':naturePurpose,'website':website,'incorporationDate':incorporationDate}; 
        	$.ajax({
	            type: "post",
	            url : "${base}/console/distributor/saveDistributorInfo.do?datestr="+ new Date().getTime(),
	            data: postData,
	            async: false,
	            dataType: "json",
	            beforeSend: function () {
	                    },
	            error: function (XMLHttpRequest, textStatus, errorThrown) {
	
	            },
	            success: function (data, textStatus) {
                console.log(data);
               var result = data.result;
               if(result==true)
               { 
                
               	layer.msg('保存成功', { icon: 1, time: 1500 });
               	window.parent.bindList(0);
               }
               else layer.msg('保存失败', { icon: 0, time: 1500}, function () {  });
            }
        });
        
		}
	

});