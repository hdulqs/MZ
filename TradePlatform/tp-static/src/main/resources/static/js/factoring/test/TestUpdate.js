define(['angular'], function (angular) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams){
            $rootScope.headTitle = $rootScope.title = "保理项目修改";
            //获取参数
            $scope.projId = $stateParams.id;
            seeDeatil(HRY.modules.factoring+'project/tsysuser/load/'+$stateParams.id);
            
            function seeDeatil(url){
            	 $http.get(url).
                 success(function(data) {
                 	 $scope.tSysUser = data;
                 });
            }
            
        	$scope.formData = {'id':$stateParams.id};
			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : HRY.modules.factoring+'project/tsysuser/modify',
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
								window.location.href='#/factoring/test/list';
							}

						});

			};
    }
    return {controller:controller};
   })

