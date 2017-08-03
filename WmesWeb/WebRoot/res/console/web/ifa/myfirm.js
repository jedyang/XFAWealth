define(function(require) {
	var $ = require('jquery');
			require('layer');
			require('zTree');
	var angular = require('angular');
	$('#tab-search li').click(function(){
		$(this).addClass('tab-li-crr').siblings().removeClass('tab-li-crr');
		if(0 == $(this).index()){
			$('.tab-search-wrap').hide();
			$('.tab-org-wrap').show();
		}else if(1 == $(this).index()){
			$('.tab-search-wrap').hide();
			$('.tab-ifa-approval-wrap').show();
			$('#tbnGetMemberIfaIfafirm').click();
		}
	});
	/**
	 * Org tree
	 */
	function getTreeNodes(){
		var url = base_root + '/console/myfirm/getOrg.do?d=' + Math.random();
		$.ajax({
			type:'post',
			url:url,
			success:function(re){ //console.log(re);
				if(re.teams != null && re.teams.length > 0){
					var zNodes = [];
					var root = {};
					var rootId = 'root';
					root['id'] = rootId;
					root['open'] = true;
					root['font'] = {'font-weight':'bold'};
					root['icon'] = base_root + '/res/images/team/team_root.png';
					root['name'] = props['ifafirm.team.org.tree.root'];
					zNodes.push(root);
					$.each(re.teams, function(i, n){
						var zNode = {};
						zNode['id'] = n.id;
						if(n.parentId == '' || n.parentId == null){
							zNode['pId'] = rootId;
						}else{
							zNode['pId'] = n.parentId;	
						}
						zNode['name'] = n.name;
						zNode['icon'] = base_root + '/res/images/team/group.png';
						zNodes.push(zNode);
					});
					$.fn.zTree.init($("#orgTree"), getTreeSetting(), zNodes);
					$.fn.zTree.init($("#addOrgTree"), getAddTreeSetting(), zNodes);
					$('#orgTree_1_a').click();
				}
			}
		});
	}
	getTreeNodes();
	function getTreeSetting(){
		var setting = {
	        view: {
	        	fontCss: getFont,
				//nameIsHTML: true,
				//showTitle: false,
	            addHoverDom: addHoverDom,
	            removeHoverDom: removeHoverDom,
	            selectedMulti: false
	        },
	        data: {
	            simpleData: {
	                enable: true
	            }
	        },
	        edit: {
	            enable: true
	        },
	        callback: {
	        	onClick:zTreeOnClick,
	        	beforeRemove:removeTeam,
	        	onRename:renameTeam
			}
	    };
		return setting;
	}
	function getAddTreeSetting(){
		var setting = {
				view: {
		        	fontCss: getFont
		        	//nameIsHTML: true,
		        	//showTitle: false
		        },
				data: {
					simpleData: {
						enable: true
					}
				},
				check: {
					enable: true,
					chkStyle: "radio",
					radioType: "all"
				},
				callback: {
		        	onCheck:zTreeOnCheck,
				}
		};
		return setting;
	}
	function getFont(treeId, node) {
		return node.font ? node.font : {};
	}
    //var newCount = 1;
    function addHoverDom(treeId, treeNode) {
        var sObj = $("#" + treeNode.tId + "_span");
        if(0 == treeNode.level){
        	sObj.closest('a').find('.edit').remove();
        	sObj.closest('a').find('.remove').remove();
    		return false;
    	}
        if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
        var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
            + "' title='"+props["global.add"]+"' onfocus='this.blur();'></span>";
        sObj.after(addStr);
        var addBtn = $("#addBtn_"+treeNode.tId);
        if (addBtn) addBtn.bind("click", function(){
        	addTeam(treeNode);
            return false;
        });
    };
    function removeHoverDom(treeId, treeNode) {
    	$("#addBtn_" + treeNode.tId).unbind().remove();
    };
    function removeTeam(treeId,treeNode){
    	var url = base_root + '/console/myfirm/deleteOrg.do?d=' + Math.random();
      	var removeFlag = false;
    	$.ajax({
      		type:'post',
      		url:url,
      		async:false,
      		data:{teamId:treeNode.id},
      		success:function(re){
      			if(re.flag){
      				layer.msg(props['global.success.delete'], {icon:2});
      				removeFlag = true;
      			}else{
      				layer.msg(props['global.failed.delete'], {icon:1});
      			}
      			$('#addTeam').hide();
      		},error:function(){
      			layer.msg(props['global.failed.delete'], {icon:1});
      		}
      	});
    	return removeFlag;
    }
    function renameTeam(event, treeId, treeNode, isCancel){
    	var data = {
			teamId:treeNode.id,
    		name:encodeURI(treeNode.name)
    	};
    	var url = base_root + '/console/myfirm/addOrg.do?d=' + Math.random();
    	$.ajax({
    		type:'post',
    		url:url,
    		data:data,
    		success:function(re){
    			if(re.flag){
    				//getTreeNodes();
    				layer.msg(props['global.success.save'], {icon:2});
    			}else{
    				layer.msg(props['global.failed.save'], {icon:1});
    				getTreeNodes();
    			}
    			$('#addTeam').hide();
    		},error:function(){
    			layer.msg(props['global.failed.save'], {icon:1});
    			getTreeNodes();
    		}
    	});
    }
    function addTeam(treeNode){
    	initTeamForm();
    	$('.team-add-parent-wrap').show();
    	$('#txtParentId').val(treeNode.id);
    	$('#txtParentName').val(treeNode.name);
    	$('#addTeam').show();
    }
    $('#btnAddTeam').click(function(){
    	initTeamForm();
    	$('.team-add-parent-wrap').hide();
    	$('#addTeam').show();
    });
    $('.wmes-close,.team-add-cancel').click(function(){
		$('.client-portfolio-new-mask').hide();
	});
    $('.team-add-confirm').click(function(){
    	var validFlag = addTeamValid();
    	if(!validFlag){
    		return false;
    	}
    	var parentId = $('#txtParentId').val();
    	var name = $('#txtName').val();
    	var code = $('#txtCode').val();
    	var orderBy = $('#txtOrderBy').val();
    	var remark = $('#texRemark').val();
    	var data = {
    		parentId:parentId,
    		name:encodeURI(name),
    		code:encodeURI(code),
    		orderBy:encodeURI(orderBy),
    		remark:encodeURI(remark)
    	};
    	var url = base_root + '/console/myfirm/addOrg.do?d=' + Math.random();
    	$.ajax({
    		type:'post',
    		url:url,
    		data:data,
    		success:function(re){
    			if(re.flag){
    				getTreeNodes();
    				layer.msg(props['global.success.save'], {icon:2});
    			}else{
    				layer.msg(props['global.failed.save'], {icon:1});
    			}
    			$('#addTeam').hide();
    		},error:function(){
    			layer.msg(props['global.failed.save'], {icon:1});
    		}
    	});
    });
    function addTeamValid(){
    	var validFlag = true;
    	var name = $('#txtName').val();
    	var code = $('#txtCode').val();
    	var orderBy = $('#txtOrderBy').val();
    	if(!name){
    		$('.team-tips-name').show();
    		validFlag = false;
    	}
    	if(!code){
    		$('.team-tips-code').show();
    		validFlag = false;
    	}
    	if(!orderBy){
    		$('.team-tips-orderBy').show();
    		validFlag = false;
    	}
    	return validFlag;
    }
    function initTeamForm(){
    	$('#txtParentId').val('');
    	$('#txtParentName').val('');
    	$('#txtName').val('');
    	$('#txtCode').val('');
    	$('#txtOrderBy').val('');
    	$('#texRemark').val('');
    	$('.team-tips').hide();
    }
    $('.team-add-input').on('input',function(){
    	if($(this).val() && $(this).val().length > 0){
    		$(this).closest('div').find('span').hide();
    	}
    });
    function zTreeOnClick(event, treeId, treeNode) {
    	if(treeNode.level == 0){
    		 $('#hidTeamId').val('');
    	}else{
    		$('#hidTeamId').val(treeNode.id);
    	}
	    $('#tbnGetIfalist').click();
	};
    var mybase = angular.module('myFirm', ['wmespage','truncate']);
	mybase.controller('myFirmCtrl', ['$scope', '$http', function($scope, $http) {
		var iPAGE = 1;
		function getIfalist(){
			var teamId = $('#hidTeamId').val();
			var keyWord = $('#txtTeamIfaKeyWord').val();
	    	var data = {
	    		page:iPAGE,
	    		keyWord:keyWord,
				teamId:teamId
	    	};
	    	var url = base_root + '/console/myfirm/getIfafirmTeamIfa.do?d=' + Math.random();
			$http({
              url: url,
              method: 'POST',
              headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
              params : data
         	 })
			.success(function(re){ //console.log(re);
				$scope.ifafirmTeamIfa = re.rows;
				$scope.ifaConf.totalItems = re.total;
				if(re.total > 0){
					$('.no_list_tips_ifa').hide();
				}else{
					$('.no_list_tips_ifa').show();
				}
            });
    	}
		getIfalist();
		$('#tbnGetIfalist').click(getIfalist);
		$scope.ifaConf = {
            itemsPerPage:10,
            onChange: function(){
            	iPAGE = $scope.ifaConf.currentPage;
            	getIfalist();
            }
        };
		$('#btnKeyWord').click(function(){
			getIfalist();
		});
		$('#btnKeyWord1').click(function(){
			getMemberIfaIfafirm();
		});
		$(document).keypress(function(e) {  
	       if(e.which == 13) {  
	    	if($('#tab-search li').eq(1).hasClass('tab-li-crr')){
	    		getMemberIfaIfafirm();
	    	}else{
	    		getIfalist(); 
	    	}
	       }  
	    });
		var jPAGE = 1;
		function getMemberIfaIfafirm(){
			var keyWord = $('#txtKeyWord').val();
			var status = $('#selIsApproval').val();
	    	var data = {
	    		page:jPAGE,
	    		filterKeyWord:keyWord,
	    		filterStatus:status
	    	};
	    	var url = base_root + '/console/myfirm/getMemberIfaIfafirm.do?d=' + Math.random();
			$http({
              url: url,
              method: 'POST',
              headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
              params : data
         	 })
			.success(function(re){ //console.log(re);
				$scope.memberIfaIfafirm = re.rows;
				$scope.memberIfaIfafirmConf.totalItems = re.total;
				if(re.total > 0){
					$('.no_list_tips_memberIfaIfafirm').hide();
				}else{
					$('.no_list_tips_memberIfaIfafirm').show();
				}
            });
    	}
		$scope.memberIfaIfafirmConf = {
            itemsPerPage:10,
            onChange: function(){
            	jPAGE = $scope.memberIfaIfafirmConf.currentPage;
            	getMemberIfaIfafirm();
            }
        };
		$('#selIsApproval').on('change',function(){
			getMemberIfaIfafirm();
		});
		$('#txtKeyWord').on('input',function(){
			getMemberIfaIfafirm();
		});
		$('#tbnGetMemberIfaIfafirm').click(getMemberIfaIfafirm);
	}]); 
	$(document).on('click','.action-modify-team',function(){
		$('#hidOldTeamId').val('');
		$('#hidCurIfaId').val('');
		$('#hidCurTeamId').val('');
		$('#hidOldTeamId').val($(this).data('teamId'));
		$('#hidCurIfaId').val($(this).data('ifaId'));
		$('#changeTeam').show();
	});
	$(document).on('click','.action-approval-ifa',function(){
		var selIsApproval = $('#selIsApproval').val();
		if(selIsApproval == 2){
			$('#ifa-reject').hide();
		}else{
			$('#ifa-reject').show();
		}
		$('#ifaApproval #hidIfaId').val('');
		$('#ifaApproval #txtIfaName').val('');
		$('#ifaApproval #texOpinion').val('');
		var id = $(this).data('id');
		var name = $(this).data('name');
		$('#ifaApproval #hidIfaId').val(id);
		$('#ifaApproval #txtIfaName').val(name);
		$('#ifaApproval').show();
	});
	$(document).on('click','#ifa-confirm',function(){
		confirmIfa("1");
	});
	$(document).on('click','#ifa-reject',function(){
		var opinion = $('#ifaApproval #texOpinion').val();
		if(!opinion){
			$('.team-tips-opinion').show();
			return false;
		}
		confirmIfa("2");
	});
	function confirmIfa(status){
		var id = $('#ifaApproval #hidIfaId').val();
		var opinion = $('#ifaApproval #texOpinion').val();
		var data = {
			ifaId:id,
			status:status,
			opinion:opinion
		};
		var url = base_root + '/console/myfirm/confirmIfa.do';
		$.ajax({
			type:'post',
			url:url,
			data:data,
			success:function(re){
				if(re.flag){
					layer.msg(props['global.success'],{icon:2});
				}else{
					layer.msg(props['global.failed'],{icon:1});
				}
				$('#ifaApproval').hide();
				$('#tbnGetMemberIfaIfafirm').click();
			},error:function(){
				layer.msg(props['global.failed'],{icon:1});
				$('#ifaApproval').hide();
				$('#tbnGetMemberIfaIfafirm').click();
			}
		});
	}
	$(document).on('click','.action-delete-team',function(){
		var ifaId = $(this).data('ifaId');
		var teamId = $(this).data('teamId');
		var data = {
			ifaId:ifaId,
			teamId:teamId
		};
		layer.confirm(props['ifafirm.team.delete.confirm.tips'], {
		  btn: [props['global.confirm'],props['global.cancel']],title:props['global.info']
		}, function(){
			var url = base_root + '/console/myfirm/delIfa.do';
			$.ajax({
				type:'post',
				url:url,
				data:data,
				success:function(re){
					if(re.flag){
						layer.msg(props['global.success'],{icon:2});
					}else{
						layer.msg(props['global.failed'],{icon:1});
					}
					$('#tbnGetIfalist').click();
				},error:function(){
					layer.msg(props['global.failed'],{icon:1});
					$('#tbnGetIfalist').click();
				}
			});
		}, function(){
		  layer.close();
		});
	});
	$(document).on('click','.action-set-leader',function(){
		var ifaId = $(this).data('ifaId');
		var teamId = $(this).data('teamId');
		var isSupervisor = '1';
		var data = {
				isSupervisor:isSupervisor,
				ifaId:ifaId,
				teamId:teamId
		};
		layer.confirm(props['ifafirm.team.setSupervisor.confirm.tips'], {
			btn: [props['global.confirm'],props['global.cancel']],title:props['global.info']
		}, function(){
			setLeader(data);
		}, function(){
			layer.close();
		});
	});
	$(document).on('click','.action-cancel-leader',function(){
		var ifaId = $(this).data('ifaId');
		var teamId = $(this).data('teamId');
		var isSupervisor = '0';
		var data = {
				isSupervisor:isSupervisor,
				ifaId:ifaId,
				teamId:teamId
		};
		layer.confirm(props['ifafirm.team.cancelSupervisor.confirm.tips'], {
			btn: [props['global.confirm'],props['global.cancel']],title:props['global.info']
		}, function(){
			setLeader(data);
		}, function(){
			layer.close();
		});
	});
	function setLeader(data){
		var url = base_root + '/console/myfirm/setLeader.do';
		$.ajax({
			type:'post',
			url:url,
			data:data,
			success:function(re){
				if(re.flag){
					layer.msg(props['global.success'],{icon:2});
				}else{
					layer.msg(props['global.failed'],{icon:1});
				}
				$('#tbnGetIfalist').click();
			},error:function(){
				layer.msg(props['global.failed'],{icon:1});
				$('#tbnGetIfalist').click();
			}
		});
	}
	function zTreeOnCheck(event, treeId, treeNode){
		$('#hidCurTeamId').val(treeNode.id);
	}
	$('#btnSaveTeam').click(function(){
		$('#changeTeam').hide();
		var oldTeamId = $('#hidOldTeamId').val();
		var ifaId = $('#hidCurIfaId').val();
		var newTeamId = $('#hidCurTeamId').val();
		var data = {
			newTeamId:newTeamId,	
			oldTeamId:oldTeamId,	
			ifaId:ifaId	
		};
		var url = base_root + '/console/myfirm/updateIfaTeam.do';
		$.ajax({
			type:'post',
			url:url,
			data:data,
			success:function(re){//console.log(re);
				if(re.flag){
					layer.msg(props['global.success'],{icon:2});
				}else{
					layer.msg(props['global.failed'],{icon:1});
				}
				$('#tbnGetIfalist').click();
			},error:function(){
				layer.msg(props['global.failed'],{icon:1});
				$('#tbnGetIfalist').click();
			}
		});
	});
});	