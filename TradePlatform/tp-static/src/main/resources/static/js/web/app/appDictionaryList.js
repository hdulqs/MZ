define(['app','jquery','ztree'], function(app){
   return app.controller('appDictionaryList', ['$scope','$rootScope','$http',function ($scope,$rootScope,$http) {

            $rootScope.headTitle = $rootScope.title = "系统配置列表";
           
            //测试数据
            loadDictionaryList();
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
                      	$scope.selected[$scope.list[id].configid]=false;
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
            		layer.msg("请选择数据", {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		
            		return false;
            	}else if(ids.length>1){
            		layer.msg("只能选择一条数据", {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;	
            	}else{
            		window.location.href='#/web/app/AppDctionaryDetail/'+ids[0];
            	}
            	
            }
            function modify(selectes){
            	var ids = transform(selectes);
            	if(ids.length==0){
            		layer.msg("请选择数据", {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;
            	}else if(ids.length>1){
            		layer.msg("只能选择一条数据", {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;	
            	}else{
            		window.location.href='#/web/app/AppDctionaryUpdate/'+ids[0];
            	}
            	
            
            }
            /**
             * 删除
             */
            function remove(selectes,url){
            	var ids = transform(selectes);
            	if(ids.length==0){
            		layer.msg("请选择数据", {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;
            	}
            	 $http({
                     method: "POST", 
                     url:url,
                     params:{'ids':ids}
                 }).
                 success(function(data, status,headers, config) {
                	 layer.msg(data.msg, {
 		    		    icon: 1,
 		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
 		    		});
                	if(data.success){
                		//重新加载
                		loadAll(HRY.modules.web+'app/appdctionary/list');
                	}
                	// this.dtInstance.reloadData(callback, true);
                 }).
                 error(function(data, status) {
                	 layer.msg("删除失败,code："+status, {
 		    		    icon: 1,
 		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
 		    		});
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
            
    }])


  

   
	
   
	
	function loadDictionaryList(){
	   var zTree=null;
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
						beforeClick:function(treeId, treeNode){
							//alert(treeId);
						}
				}
			};
		var zNodes =[];
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
				//修改ztree样式
				var treeSpan = $("#tree a");
				for(var i = 0 ; i < treeSpan.length ; i++){
					var ztid = $(treeSpan[i]).attr("ztid");
					$(treeSpan[i]).after("<a class='btn ' title='删除' ng-click='remove(\""+ztid+"\",\HRY.modules.web+"app/appdictionary/remove\")'  ><img src='static/component/ztree/css/zTreeStyle/img/diy/1_close.png'/ ></a>")
					$(treeSpan[i]).after("<a class='btn ' title='修改'  ><img src='static/component/ztree/css/zTreeStyle/img/diy/9.png'/ ></a>")
				}
	   		 }, 
	   		 "json"
 			);
	}
})
