/**
 * Created by kenkozheng on 2015/7/10.
 */
define(['app'], function (app) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams){
        $rootScope.headTitle = $rootScope.title = "用户";
        
        /**
         * 查看页面
         */
        if($stateParams.page=="add"){

        	$scope.formData = {};
			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : HRY.modules.oauth+'user/appuser/add.do',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							console.log(data);
							if (!data.success) {
								$scope.errorName = data.msg;
							} else {
								$scope.message = data.msg;
								window.location.href='#/oauth/user/appuser/list/anon';
							}
	
						});
	
			};
		
        	
        }
        
        /**
         * 查看页面
         */
        if($stateParams.page=="see"){
        	$http.get(HRY.modules.oauth+"user/appuser/see.do?id="+$stateParams.id).
                 success(function(data) {
                 	 $scope.model = data;
            });
        	
        }
       	
        /**
         * 列表页面
         */
        if($stateParams.page=="list"){
        	

            $scope.fnSee=fnSee;//see方法
            $scope.fnRemove=fnRemove;//remove方法
            $scope.fnToggleAll = fnToggleAll; //全选
            $scope.fnToggleAll = fnToggleAll;//选择一条
            $scope.selected = {};
            $scope.selectAll = false;
            
            
            /**
             * 查看
             */
            function fnSee(selectes){
            	 
            	var ids = transform(selectes);
            	if(ids.length==0){
            		alert("请选择数据");
            		return false;
            	}else if(ids.length>1){
            		alert("只能选择一条数据");
            		return false;	
            	}else{
            		$rootScope.id= ids[0];
            		window.location.href='#/oauth/user/appuser/see/'+$rootScope.id;
            	}
            }
            
            /**
             * 删除
             */
            function fnRemove(selectes,url){
            	var ids = transform(selectes);
            	if(ids.length==0){
            		alert("请选择数据");
            		return false;
            	}
            	 $http({
                     method: "POST", 
                     url:url,
                     params:{'ids':ids}
                 }).
                 success(function(data, status,headers, config) {
                	 alert(data.msg);
                	if(data.success){
                		//重新加载
                		loadAll(HRY.modules.factoring+'project/tsysuser/list');
                	}
                	// this.dtInstance.reloadData(callback, true);
                 }).
                 error(function(data, status) {
                	 alert("删除失败，code："+status);
               })
			}
            /**
            * <p> 将对象中 为true 的对象解析为数组，用于删除操作</p> 
            * @param: @param obj
            * @param: @returns {Array} 
            * @return: Array 
            * @throws
             */
            function transform(obj){
                var arr = [];
                for(var item in obj){
                	if(obj[item]){arr.push(item)};
                }
                return arr;
            }
        	/**
        	* <p> 全选</p> 
        	* @param: @param selectAll
        	* @param: @param selectedItems 
        	* @return: void 
        	* @throws
        	 */
        	function fnToggleAll (selectAll, selectedItems) {
        	    for (var id in selectedItems) {
        	        if (selectedItems.hasOwnProperty(id)) {
        	            selectedItems[id] = selectAll;
        	        }
        	    }
        	}
        	/**
        	 * 选择一条
        	 */
        	function fnToggleOne (selectedItems) {
        		 
        	    var me = $scope;
        	    for (var id in selectedItems) {
        	        if (selectedItems.hasOwnProperty(id)) {
        	            if(!selectedItems[id]) {
        	                me.selectAll = false;
        	                return;
        	            }
        	        }
        	    }
        	    me.selectAll = true;
        	}
        
        }
        
    }
    return {controller:controller};
});