/**
 * AppDictionary.js
 */
define(['app','ztree'], function (app) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$compile','$stateParams'];
    function controller($scope,$rootScope,$http, $compile,$stateParams){
        $rootScope.headTitle = $rootScope.title = "数据字典";
        
        /**
         * 添加页面
         */
        if($stateParams.page=="add"){
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
								$scope.formData.pDicKey="0";
								$scope.formData.pid="0";
							}else{
								$scope.formData.pDicKey=treeNode.dicKey;
								$scope.formData.pid=treeNode.id;
							}
							$("#treediv").slideToggle("fast");
						}
					}
				};

				var zNodes =[
					
				];
				zNodes = [{id:-1,pId:0,name:"无父节点"}];
				$.post(  HRY.modules.web+"/app/appdictionary/findlist.do?Q_t.dicType_eq_Integer=1",
					 null,
			  		 function(data){
			   			for(var i = 0 ; i < data.length ; i++) {
			   				var item = {
			   							id:data[i].id,
			   							pId:data[i].pid,
			   							name:data[i].itemName,
			   							dicKey:data[i].dicKey,
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
					url : HRY.modules.web+'app/appdictionary/add.do',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							if (data.success) {
								growl.addInfoMessage(data.msg);
								window.location.href='#/web/app/appdictionary/list/anon';
							} else {
								layer.msg(data.msg, {
					    		    icon: 2,
					    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					    		});
							}

						});

			};
        }
        
		
        
        /**
         * 修改页面
         */
        if($stateParams.page=="modify"){
        	$http.get( HRY.modules.web+"app/appdictionary/see.do?id="+$stateParams.id).
                 success(function(data) {
                 	 $scope.formData = data;
            });
        	
      //  	$scope.formData = {};
			$scope.processFormModfiy = function() {
				$http({
					method : 'POST',
					url :HRY.modules.web+'app/appdictionary/modify.do',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href='#/web/app/appdictionary/list/anon';
					} else {
						layer.msg(data.msg, {
			    		    icon: 2,
			    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
			    		});
					}

				});
	
			};
        	
        	
        }
        
       	
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
    				url : HRY.modules.web+'app/appdictionary/add.do',
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
          function fnModify(ztid){
        //	  window.location.href='#'+ HRY.modules.web +'app/appdictionary/modify/'+ztid;
        	  $http.get( HRY.modules.web+"app/appdictionary/see.do?id="+ztid).
              success(function(data) {
              	 $scope.model = data;
              	 $scope.formData = data;
         });
      		$('#modifyNodeDiv').removeClass("hide");
			layer.open({
				type : 1,
				title : "修改字典项",
				closeBtn : 2,
				area : [ '600px', '400px' ],
				shadeClose : true,
				content : $('#modifyNodeDiv')
			});
          
          
          }
            
            //删除按钮
            function fnRemove(ztid){
            	layer.confirm('你确定要删除吗？', {
	    			btn: ['确定','取消'], //按钮
	    			id: ztid
				}, function(){
					$http({
						method : 'POST',
						url : HRY.modules.web + 'app/appdictionary/remove',
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
				})
        
			}
            
        	// 菜单上的上移按钮
			$scope.fnUp = function(id){
				$http({
					method : 'POST',
					url : HRY.modules.web + 'app/appdictionary/move',
					data : $.param({id:id,type:"up"}),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					loadDictionaryList();
				});
			}
			
			// 菜单上的下移按钮
			$scope.fnDown = function(id){
				$http({
					method : 'POST',
					url : HRY.modules.web + 'app/appdictionary/move',
					data : $.param({id:id,type:"down"}),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					loadDictionaryList();
				});
			}
			
       
        	
        	
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
        						idKey: "dicKey",
        						pIdKey: "pDicKey",
        						id:"id",
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
        		$.post(HRY.modules.web+"/app/appdictionary/findlist.do?Q_t.dicType_eq_Integer=1",
        			 null,
        	  		 function(data){
        	   			for(var i = 0 ; i < data.length ; i++) {
        	   				var item = {
        	   						    dicKey:data[i].dicKey,
        	   							pDicKey:data[i].pDicKey,
        	   							name:data[i].itemName,
        	   							open : true,
        	   							id:data[i].id,
        	   						//	drop : false
        	   						   }
        	   				zNodes.push(item);
        	   			}
        	   			//初始化菜单树
        				$.fn.zTree.init($("#tree"), setting, zNodes);
        				//修改ztree样式
        				var treeSpan = $("#tree a");
        				for(var i = 0 ; i < treeSpan.length ; i++){
        					var ztid = $(treeSpan[i]).attr("ztid");
        					$(treeSpan[i]).after("<a class='btn ' title='下移' ng-click='fnDown(" + ztid + ")' ><img src='" + HRY.staticUrl + "/static/lib/ztree/css/zTreeStyle/img/diy/1_open.png'/ ></a>")
							$(treeSpan[i]).after("<a class='btn ' title='上移' ng-click='fnUp(" + ztid + ")' ><img src='" + HRY.staticUrl + "/static/lib/ztree/css/zTreeStyle/img/diy/1_close.png'/ ></a>")
							$(treeSpan[i]).after("<a class='btn ' title='修改' ng-click='fnModify(" + ztid + ")' ><img src='" + HRY.staticUrl + "/static/lib/ztree/css/zTreeStyle/img/diy/3.png'/ ></a>")
							$(treeSpan[i]).after("<a class='btn ' title='删除' ng-click='fnRemove(" + ztid + ")' ><img src='" + HRY.staticUrl + "/static/lib/ztree/css/zTreeStyle/img/diy/9.png'/ ></a>")
        				}
        				// 修改完样式后重新编译dom
    					$compile($("#tree"))($scope);
        	   		 }, 
        	   		 "json"
         			);
        	}
        
        }
        
    }
    return {controller:controller};
});