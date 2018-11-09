define(['angular'], function (angular) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams){

				$rootScope.headTitle = $rootScope.title = "保理项目添加";

				$scope.formData = {};

				// process the form
				$scope.processForm = function() {
					$http({
						method : 'POST',
						url : HRY.modules.factoring+'project/tsysuser/add',
						data : $.param($scope.formData), // pass in data as
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						} // set the headers so angular passing info as form
						 // data (not request payload)
					})
					.success(function(data) {
								console.log(data);
								if (!data.success) {
									// if not successful, bind errors to error
									// variables
									$scope.errorName = data.msg;

								} else {
									// if successful, bind success message to
									// message
									$scope.message = data.msg;
								}

							});

				};

    }
    return {controller:controller};
})