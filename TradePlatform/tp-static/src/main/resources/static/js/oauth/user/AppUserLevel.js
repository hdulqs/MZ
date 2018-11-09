/**
 * AppUserLevel.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
        $rootScope.headTitle = $rootScope.title = "上下级关系配置";
        $scope.hryCore = hryCore;
       	
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	/**
        	 * 递归拼数组
        	 * nodes  子节点
        	 * data   数组
        	 * superior  上级节点
        	 */
        	function createAppUserLevelArr(nodes,data,superior){
        		for(var i = 0 ; i < nodes.length ; i++ ){
        			if(superior!=null){
	        			var appUserLevel = {
	        					userId : nodes[i].userId,
	        					name : nodes[i].name,
	        					superiorId : superior.userId
	        			};
        			}else{
        				var appUserLevel = {
	        					userId : nodes[i].userId,
	        					name : nodes[i].name,
	        					superiorId : 0
	        			};
        			}
        			data.push(appUserLevel);
        			if(nodes[i].children!=null&&nodes[i].children.length>0){
        				createAppUserLevelArr(nodes[i].children,data,nodes[i]);
        			}
        		}
        	}
        	
        	$scope.formData = {};
			$scope.processForm = function() {
				
				var data = [];
				
				var ztree = $.fn.zTree.getZTreeObj("treeDemo");
				var nodes = ztree.getNodes();
				createAppUserLevelArr(nodes,data,null);
				
				$.ajax({
    	 		     type: 'POST',
    	 		     url : HRY.modules.oauth+'user/appuserlevel/addBatch.do',
    	 		     data :JSON.stringify(data),
    	 		     dataType: "json",
    	 		     contentType: "application/json",
    	 		     success: function(data){
    						if (data.success) {
    							growl.addInfoMessage('保存成功')
    						} else {
    							growl.addInfoMessage(data.msg)
    						}
    	 		     }
    	 		});
				
			};
             
        }
        
        //加载插件
		hryCore.initPlugins();
		//上传插件
		hryCore.uploadPicture();
    }
    //----------------------------

    
    return {controller:controller};
});