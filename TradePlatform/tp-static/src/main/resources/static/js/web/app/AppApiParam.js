/**
 * appapi.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	
    	var table =  $("#table2");
    	
	//------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {

			 hryCore.CURD({
						url : HRY.modules.web+"app/appapiparam/see/"+$stateParams.id
					}).get(null,
				function(data) {
            		$scope.formData = data;
			    }, function(data) {
					growl.addInfoMessage("error:" + data.msg);
				});
			
			
			
		}

    	
//------------------------增加页面路径------------------------------------------------------------------------------------------------------
//------------------------增加页面路径------------------------------------------------------------------------------------------------------

		
		if($stateParams.page=="add"){
         
			
			$scope.processForm = function() {
				var id=$("#id_EQ").val();
						$scope.formData.apiId=id;
						$http({
		 					method : 'POST',
		 					url : HRY.modules.web+'app/appapiparam/add',
		 					data : $.param($scope.formData), 
		 					headers : {
		 						'Content-Type' : 'application/x-www-form-urlencoded'
		 					}
		 				})
						.success(function(data) {
									if (data.success) {
										growl.addInfoMessage('添加成功')
										window.location.href='#/web/app/appapiparam/list/'+id;
									} else {
										growl.addInfoMessage('添加失败')
									}
			
						});
			
					};	
        }

        
 //------------------------修改页面路径-----------------------------------------------------------------------------------------------
 //------------------------修改页面路径-----------------------------------------------------------------------------------------------     
        
        
        
        if($stateParams.page=="modify"){

				$http.get(HRY.modules.web+"app/appapiparam/see/"+$stateParams.id).
                success(function(data) {
                	 $scope.model = data;
                	 $scope.formData = data;
                	
               });
			
			
				$scope.processForm = function() {
					
				  $http({
						method : 'POST',
						url : HRY.modules.web+'/app/appapiparam/modify',
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
									window.location.href='#/web/app/appapi/list/anon';
								}
					});
				};
		
        }
        
       	
 //------------------------列表页面路径--------------------------------------------------------------------------------------------------------
 //------------------------列表页面路径-------------------------------------------------------------------------------------------------------
   
        if($stateParams.page=="list"){
        	
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        	    config.iDisplayLength=10;
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.web+'/app/appapiparam/list?apiId='+$stateParams.id;
        		config.ajax.data = function(d){
        		
					        			$.each($scope.serchData, function(name,value){
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							}/*, {
										"data" : "title"
									},{
										"data" : "description" 
									}*/, {
										"data" : "param"
									}, {
										"data" : "paramName"
									}, {
										"data" : "paramType"
									}, {
										"data" : "paramDesc"
									}, {
										"data" : "isRequired"
									}, {
										"data" : "example"
									}, {
										"data" : "sort"
									}
        		                  ]
        		                  
        	config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},
										{
											"targets" : 5,
												
											"render" : function(data, type, row) {
											    if(data == 1){
													return "否";
												}
												return "是"
											}
										}
        						]
        		                  
        	
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
        		
            $scope.fnAdd=fnAdd;//add按钮方法
            $scope.fnSee=fnSee;//see按钮方法
            $scope.fnModify=fnModify;//see按钮方法
            $scope.fnRemove=fnRemove;//remove方法
            $scope.fnList=fnList;//刷新方法
        
         
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
            
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
            	window.location.href='#/web/'+url+'/anon';
            }
            
            //查看按钮
            //ng-click="fnSee(url,selectes)"
            function fnSee(url){
            		window.location.href='#/web/'+url+'/anon';
            }
            
            //修改按钮
            //ng-click="fnModify(url,selectes)"
            function fnModify(url){
            	 
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(ids.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		$rootScope.id= ids[0];
            		window.location.href='#/web/'+url+"/"+$rootScope.id;
            	}
            }
            
            //删除按钮
            //ng-click="fnRemove(url,selectes)"
            function fnRemove(url){
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}
            	
            	mes="确定删除这些数据吗？"
            	
        		layer.confirm(mes,{
    				btn: ['确定','取消'], //按钮
    				ids: ids
    			}, function(){
    				
    				layer.closeAll();
    				
    				hryCore.CURD({
						url:HRY.modules.web+url+"?ids[]="+ids
					 })
					.remove(null,
						function(data) {
		                	if(data.success){
		                		//提示信息
		                		growl.addInfoMessage('删除成功')
		                		//重新加载list
		                		fnList();
		                	}else{
		                		growl.addInfoMessage(data.msg)
		                	}
		                },
						function(data) {
							growl.addInfoMessage("error:"+ data.msg);
						});
    			})
			}
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});