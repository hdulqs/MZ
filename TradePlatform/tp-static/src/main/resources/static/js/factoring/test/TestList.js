define(['angular'], function (angular) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams){

            $rootScope.headTitle = $rootScope.title = "保理项目list";
            //测试数据
//           $scope.list = [{name:'yzc',id:'1'},{name:'www',id:'2'}];
            loadAll(HRY.modules.factoring+'loanEenterprise/custeloan/addView');
            // $scope.dtOptions = DTOptionsBuilder.newOptions().withPaginationType('full_numbers');
            page();
            $scope.loadAll=loadAll;
            $scope.load=load;
            $scope.remove=remove;
            $scope.modify=modify;
            $scope.selected = {};
            $scope.selectAll = false;
            //全选
            $scope.toggleAll = toggleAll;
            //选择一条
            $scope.toggleOne = toggleOne;
            function page(){
            }
            /**
             * 加载数据
             */
            function loadAll(url){
            	 $http.get(url).
                 success(function(data) {
                 	 $scope.list = data.rows;
                 	 for(var id in $scope.list){
                      	$scope.selected[$scope.list[id].id]=false;
                      }
                   //$scope.$emit('dataloaded');//通知加载完成
                 });
            }
            
            /**
             * 查看
             */
            function load(selectes){
            	var ids = transform(selectes);
            	if(ids.length==0){
            		alert("请选择数据");
            		return false;
            	}else if(ids.length>1){
            		alert("只能选择一条数据");
            		return false;	
            	}else{
            		window.location.href='#/factoring/test/Test/detail/'+ids[0];
            	}
            	
            }
            function modify(selectes){
            	var ids = transform(selectes);
            	if(ids.length==0){
            		alert("请选择数据");
            		return false;
            	}else if(ids.length>1){
            		alert("只能选择一条数据");
            		return false;	
            	}else{
            		window.location.href='#/factoring/test/Test/modify/'+ids[0];
            	}
            	
            
            }
            /**
             * 删除
             */
            function remove(selectes,url){
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
        	function toggleAll (selectAll, selectedItems) {
        	    for (var id in selectedItems) {
        	        if (selectedItems.hasOwnProperty(id)) {
        	            selectedItems[id] = selectAll;
        	        }
        	    }
        	}
        	/**
        	 * 选择一条
        	 */
        	function toggleOne (selectedItems) {
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

    return {controller:controller};

})
