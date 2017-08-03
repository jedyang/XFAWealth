define(function(require) {
	var $ = require('jquery');
			require('layui');
	var angular = require('angular');	
	
	/**
	 * bind announcement
	 */
	var statementAngular = angular.module('statement', ['truncate','wmespage']);
	statementAngular.controller('statementCtrl', ['$scope', '$http', function($scope, $http) {
		var rows_ = 10;
		$scope.bindStatement = function (type,page){
			var url = base_root + '/front/investor/getStatement.do?d=' + new Date().getTime();
			$http({
	            url : url,
	            method :'POST',
	            params : {
	            	type : type,
	            	page : page,
	            	rows : rows_
	            }
	       	}).success(function(re){
	       		$scope.statementConf.totalItems = re.total;
	       		$scope.statement = re.rows;
	       	});
		}
		$scope.statementConf = {
            itemsPerPage:rows_,
            onChange: function(){
            	var page = $scope.statementConf.currentPage;
            	$scope.bindStatement('',page);
            }
        };
		$scope.bindStatement('',1);
	}]);
});	
