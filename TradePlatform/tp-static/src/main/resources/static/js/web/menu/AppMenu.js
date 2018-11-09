/**
 * AppUser.js
 */
define([ 'app','hryTable' ], function(app,DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams','$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state,hryCore) {

		$rootScope.headTitle = $rootScope.title = "菜单管理";

		var table = $("#dataTable");

		$scope.hryCore = hryCore;

		// ------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			hryCore.CURD({
				url : HRY.modules.oauth + "user/appuser/see/" + $stateParams.id
			}).get(null, function(data) {
				$scope.formData = data;
				hryCore.RenderHTML(data);// 异步回显控件
			}, function(data) {
				growl.addInfoMessage("error:" + data.msg);
			});
		}

		/**
		 * 添加页面
		 */
		if ($stateParams.page == "add") {

			$scope.formData = {};
			$scope.processForm = function() {
				// 所属公司
				$scope.formData.companySet = $("#companySet").val();
				// 所属门店
				$scope.formData.shopSet = $("#shopSet").val();
				// 所属部门
				$scope.formData.departmentSet = hryCore.ArrayToString($("#departmentSet").val());
				// 拥有角色
				$scope.formData.appRoleSet = hryCore.ArrayToString($("#appRoleSet").val());
				$http({
					method : 'POST',
					url : HRY.modules.oauth + 'user/appuser/add.do',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/oauth/user/appuser/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};
		}

		/**
		 * 修改页面
		 */
		if ($stateParams.page == "modify") {

			$http.get(HRY.modules.oauth + "user/appuser/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
				hryCore.RenderHTML(data);// 异步回显控件
			});

			$scope.processForm = function() {

				// 图片路径
				$scope.formData.picturePath = $("#imgSrc").val();
				// 所属公司
				$scope.formData.companySet = $("#companySet").val();
				// 所属门店
				$scope.formData.shopSet = $("#shopSet").val();
				// 所属部门
				$scope.formData.departmentSet = hryCore.ArrayToString($("#departmentSet").val());
				// 拥有角色
				$scope.formData.appRoleSet = hryCore.ArrayToString($("#appRoleSet").val());

				$http({
					method : 'POST',
					url : HRY.modules.oauth + 'user/appuser/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/oauth/user/appuser/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};

		}

		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "list") {

			$scope.serchData = {};
			
			$http({
				method : 'POST',
				url : HRY.modules.web + 'menu/appmenu/listTree',
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			}).success(function(data) {
				$scope.listTree = data;
			});
			

			$scope.fnList = fnList;// 刷新方法
			// 刷新按钮
			function fnList(){
			
			}
			

			// 删除按钮
			$scope.fnVisible = function(target) {
				var id = target.currentTarget.getAttribute("menuid");
				
				$http({
					method : 'POST',
					url : HRY.modules.web + 'menu/appmenu/setVisible',
					params : {
						id:id
					},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if(data.success){
						$state.go("web_menu_appmenu",
								{page:"list",id:"anon"},
								{reload : true});
						
						$http({
							method : 'POST',
							url : HRY.modules.web + 'menu/appmenu/listTree',
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							$scope.listTree = data;
						});
						
					}
				});

				
			}

		}
		

		
		// 加载插件
		hryCore.initPlugins();
		// 上传插件
		hryCore.uploadPicture();
	}
	// ----------------------------

	return {
		controller : controller
	};
});