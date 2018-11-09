define(['app'], function(app){
   return app.controller('appDictionaryUpdate', ['$scope','$rootScope','$http', '$stateParams', function ($scope,$rootScope,$http, $stateParams) {

            $rootScope.headTitle = $rootScope.title = "保理项目修改";
            //获取参数
            $scope.projId = $stateParams.id;
            seeDeatil(HRY.modules.web+'app/appdctionary/load/'+$stateParams.id);
            
            function seeDeatil(url){
            	 $http.get(url).
                 success(function(data) {
                 	 $scope.appConfig = data;
                 });
            }
            
        	$scope.formData = {'id':$stateParams.id};
			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : HRY.modules.web+'app/appdctionary/modify',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
					 
							console.log(data);
							if (!data.success) {
								$scope.message = data.msg;

							} else {
								$scope.message = data.msg;
							}

						});

			};

    }])

})

