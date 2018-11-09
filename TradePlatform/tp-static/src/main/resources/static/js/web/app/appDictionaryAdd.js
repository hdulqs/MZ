define(['app'], function(app) {
	return app.controller('appDictionaryAdd', ['$scope', '$rootScope', '$http',
			function($scope, $rootScope, $http) {

				$rootScope.headTitle = $rootScope.title = "系统配置添加";

				$scope.formData = {};
				//加载数据
				loadMenuList();
						
				
				
				function loadMenuList(){

					var zTree;

					var setting = {
						view: {
							dblClickExpand: false,
							showLine: true,
							selectedMulti: false
						},
						data: {
							simpleData: {
								enable:true,
								idKey: "id",
								pIdKey: "pId",
								rootPId: ""
							}
						},
						callback: {
							beforeDblClick: function(treeId, treeNode) {
								$("#pidValue").val(treeNode.name);
								if(treeNode.id==-1){
									$("#pid").val(0);
								}else{
									$("#pid").val(treeNode.id);
								}
								$("#treediv").slideToggle("fast");
							}
						}
					};

					var zNodes =[
						
					];
					zNodes = [{id:-1,pId:0,name:"无父节点"}];
					$.post(HRY.modules.web+"app/appdictionary/findlist.do?Q_t.dicType_eq_Integer=1",
						 null,
				  		 function(data){
				   			for(var i = 0 ; i < data.length ; i++) {
				   				var item = {
				   							id:data[i].id,
				   							pId:data[i].pid,
				   							name:data[i].text,
				   							open : false
				   							//drop : false
				   						   }
				   				zNodes.push(item);
				   			}
				   			//初始化菜单树
							$.fn.zTree.init($("#tree"), setting, zNodes);
						//	$("#treediv").hide();
				   		 }, 
				   		 "json"
			  		);
					
					
					$("#pidValue").on("click", function() {
						$("#treediv").slideToggle("fast");
					});
				}
				

				
					
				
					
					
				// process the form
				$scope.processForm = function() {
					$http({
						method : 'POST',
						url : HRY.modules.web+'app/appdictionary/add',
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

			}])

})