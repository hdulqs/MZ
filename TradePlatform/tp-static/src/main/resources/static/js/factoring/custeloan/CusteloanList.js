/**
 * Created by YuanZhicheng on 2015/7/10.
 */
define(
		[ 'app', 'angular', 'ngRoute' ],
		function(app) {
			// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
			app.controller.$inject = [ '$compile','$scope', '$rootScope', '$http',
					'$stateParams' ];
			function controller($compile,$scope, $rootScope, $http, $stateParams,
					hryCore, DTOptionsBuilder,DTColumnBuilder) {
				// 初始化js 插件
				hryCore.initPlugins();
				
				/**
				 * 列表页面
				 */
				if ($stateParams.page == "list") {
					$scope.remove = remove;// remove方法
					$scope.loadList = loadList;// 加载列表
					$scope.search = search;// 查找
					$scope.seeOrUpdate = seeOrUpdate;// seeOrUpdate 跳转页面方法
					 /**
					  * 加载列表
					  * @params 参数
					  */
					 function loadList(params){
						// 加载datatable 属性
							$scope.dtOptions = hryCore
									.dtOptions(DTOptionsBuilder, {url:HRY.modules.factoring
										+ 'loanEenterprise/custeloan/list',params:params},$compile,$scope);

							// 加载 数据列
							 hryCore.dtColumns(DTColumnBuilder,{},$scope).dtColumns.push(
		                           { "data": "id" },
		           	               { "data": "shopName","bSearchable": false },
		           	               { "data": "name","bSearchable": false, "bSortable": false},
		           	               { "data": "organizeCode" },
		           	               { "data": "cciaa" },
		           	               { "data": "linkmanName" },
		           	               { "data": "linkmanTelphone" },
		           	               { "data": "establishDate" },
		           	               { "data": "tradeType" });
							
					 }
					 
					/**
					 * 删除
					 */
					function remove(selected) {

						var ids = hryCore.transform(selected);
						hryCore
								.CURD(
										{
											url : HRY.modules.factoring
													+ 'loanEenterprise/custeloan/remove/:ids'
										})
								.remove(
										{
											ids : ids
										},
										function(data) {
											growl.addInfoMessage(data.msg);
											// 重新加载
											list($scope.searchData);

										},
										function(data) {
											growl.addInfoMessage("error:"
													+ data.msg);
										});
					}

					/**
					 * 查找数据
					 */
					function search() {
						 
						$scope.dtOptions={};
						loadList($scope.searcheData)
					}

					/**
					 * 查看跳转页面 method see/update
					 */
					function seeOrUpdate(selectes, method) {
						var ids = hryCore.transform(selectes);
						if (ids.length == 0) {
							growl.addInfoMessage('至少选中一条数据!', 1000);
							return false;
						} else if (ids.length > 1) {
							growl.addInfoMessage('只能选择一条数据!', 1000);
							return false;
						} else {
							var pk = ids[0];
							window.location.href = '#/factoring/custeloan/custeloan/'
									+ method + '/' + pk;
						}
					}

					// 加载列表
					 loadList($scope.searcheData);

				}

				/**
				 * 保存方法
				 */
				if ($stateParams.page == "add") {

					$scope.formData = {};// 查找表单数据
					$scope.reset = function reset() {
						$scope.formData = {}
					};// 增加/修改页面重置
					$scope.add = add;// add方法
					function add() {
						hryCore.CURD(
								{
									url : HRY.modules.factoring
											+ 'loanEenterprise/custeloan/add'
								}).save($scope.formData, function(data) {
							growl.addInfoMessage(data.msg);

						}, function(data) {
							growl.addInfoMessage("error:" + data.msg);
						});
					}

				}

				/**
				 * 查看加载数据
				 */
				if ($stateParams.page == "see" || $stateParams.page == "update") {

					$scope.formData = {};// 查找表单数据
					$scope.modify = modify;// 修改方法
					$scope.reset = function reset() {
						$scope.formData = {}
					};// 增加/修改页面重置

					hryCore.CURD({
								url : HRY.modules.factoring
										+ 'loanEenterprise/custeloan/load/:id'
							}).get({
						id : $stateParams.id
					}, function(data) {
						$scope.formData = data;

					}, function(data) {
						growl.addInfoMessage("error:" + data.msg);
					});

					/**
					 * 更新方法
					 */
					function modify() {

						hryCore
								.CURD(
										{
											url : HRY.modules.factoring
													+ 'loanEenterprise/custeloan/modify'
										})
								.save(
										$scope.formData,
										function(data) {
											growl.addInfoMessage(data.msg);

										},
										function(data) {
											growl.addInfoMessage("error:"
													+ data.msg);
										});
					}

				}

			}
			return {
				controller : controller
			};
		});