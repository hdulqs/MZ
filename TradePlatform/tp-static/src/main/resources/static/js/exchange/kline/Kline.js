/**
 * student.js
 */
define(['app','hryTable','pikadayJq'], function (app, DT) {
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore){
    	
		$scope.formData = {};
		$scope.processForm = function() {
			$scope.formData.coinCode = $("#coinCode").val();
			$http({
				method : 'POST',
				url : HRY.modules.exchange + 'kline/kline',
				data : $.param($scope.formData),
				// params:{'appResourceStr':ids},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			}).success(function(data) {
				if (data.success) {
					growl.addInfoMessage('修复成功')
				} else {
					growl.addInfoMessage('修复失败')
				}

			});
		};

        hryCore.initPlugins();
    }
    return {controller:controller};
});