/**
 * AppDictionary.js
 */
define(['app','layer','hrytree'], function (app,layer) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$compile','$stateParams','hrytree'];
    function controller($scope,$rootScope,$http, $compile,$stateParams,hrytree){
        $rootScope.headTitle = $rootScope.title = "单级数据字典";
        
        /**
         * 列表页面
         */
        if($stateParams.page=="list"){
        	  loadDictionaryList();
            $scope.fnAdd=fnAdd;//add按钮方法
            $scope.fnModify=fnModify;//see按钮方法
            $scope.fnRemove=fnRemove;//remove方法
            
            
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
            	window.location.href='#/'+url+'/anon';
            }
            $scope.processForm = function() {
    			$scope.formData.dicType=1;
    			if(null==$scope.formData.pDicKey){
    				
    				layer.msg("父级字典不能为空", {
    	    		    icon: 2,
    	    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
    	    		});
    				return false;
    			}
    		
    			$http({
    				method : 'POST',
    				url : HRY.modules.web+'dictionary/appdiconelevel/add.do',
    				data : $.param($scope.formData), 
    				headers : {
    					'Content-Type' : 'application/x-www-form-urlencoded'
    				}
    			})
    			.success(function(data) {
    						if (data.success) {
    							layer.closeAll();
    							 loadDictionaryList();
    							growl.addInfoMessage(data.msg);
    						} else {
    							layer.msg(data.msg, {
    				    		    icon: 2,
    				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
    				    		});
    						}

    					});

    		};
            //修改按钮
          function fnModify(treeNodes){
        	  var ztid=treeNodes.id;
        //	  window.location.href='#'+ HRY.modules.web +'app/appdictionary/modify/'+ztid;
        	  $http.get( HRY.modules.web+"dictionary/appdiconelevel/see.do?id="+ztid).
              success(function(data) {
              	 $scope.model = data;
              	 $scope.formData = data;
         });
      		$('#modifyNodeDiv').removeClass("hide");
			layer.open({
				type : 1,
				title : "修改字典项",
				closeBtn : 2,
				area : [ '710px', '400px' ],
				shadeClose : true,
				content : $('#modifyNodeDiv')
			});
          
          }
          
          /**
  		 * 增加按钮
  		 * @paramer treeNode 父节点id
  		 */
          function fnAdd(treeNode){
        	  $scope.formData = {};
      	  $('#modifyNodeDiv').removeClass("hide");
      	  $scope.formData.pDicKey=treeNode.dicKey;
      	  if(treeNode.pDicKey==""){
      	     $scope.formData.rootKey=treeNode.dicKey;
      	  }else{
      	     $scope.formData.rootKey=treeNode.pDicKey;
      	  }
      	  $scope.$apply();
			layer.open({
				type : 1,
				title : "添加字典项",
				area : [ '710px', '400px' ],
				closeBtn : 2,
				content : $('#modifyNodeDiv')
			});
          }
            
            //删除按钮
            function fnRemove(treeNodes){
            	var ztid=treeNodes.id;
				$http({
					method : 'POST',
					url : HRY.modules.web + 'dictionary/appdiconelevel/remove',
					data : $.param({ids:ztid}),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						layer.msg("删除成功", {
							icon : 1,
							time : 2000
						});
						loadDictionaryList();
					} else {
						layer.msg(data.msg, {
							icon : 1,
							time : 2000
						});
					}
				});
			
        
			}
			
			/**
			 * 加载数据
			 */
			  function 	loadDictionaryList(){
				//加载树
			    	$http({
						method : 'POST',
						data :'json',
						url :HRY.modules.web+"/dictionary/appdiconelevel/findlist.do",
					}).then(function(result) {
		               var data=result.data;
		               var zNodes=[];
			   			for(var i = 0 ; i < data.length ; i++) {
			   				var item = {
			   						    dicKey:data[i].dicKey,
			   							pDicKey:data[i].pDicKey,
			   							name:data[i].itemName,
			   							open : true,
			   							id:data[i].id
			   						//	drop : false
			   						   }
			   				
			   				zNodes.push(item);
			   				
			   			}
			   			var conf = hrytree.config();
			   			conf.setting.data = {
							simpleData: {
								enable:true,
								idKey: "dicKey",
								pIdKey: "pDicKey",
								id:"id",
								rootPId: ""
							}
						}
			   			conf.fnModify=fnModify;
				    	conf.fnAdd=fnAdd;
				    	conf.fnRemove=fnRemove;
				    	hrytree.init("tree",conf,zNodes);
					})
				  
				  
			    	 
			  }
        
        }
        
    }
    return {controller:controller};
});