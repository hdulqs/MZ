/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "客户账号管理";
    	//初始化js 插件
    	
    	
    	var table =  $("#table2");
 
    	
 // -----------------  modify页面  ---------------------------------------------------------------------   	
    	
    	if($stateParams.page == "modify"){
    		
    	var commissionDeploy = null ;
    		
			$http.get(HRY.modules.customer+"/agents/commissionDeploy/see/"+$stateParams.id).
                 success(function(data) {
                 	 commissionDeploy = data;
                 	 $scope.formData = data;
                 	 
                 	 if(null != data){
                 	 	if(null != data.costName){
		            		$("#costName").val(data.costId)
		            	}if(null != data.states){
		            		$("#states").val(data.states)
		            	}
                 	 }
                 	   // 渲染下拉框   
				 conApp.initPlugins();
	        	 conApp.initMaterialPlugins();
				
            });
            
		// 表单提交 
		
		$scope.formData = {};
			
		$scope.processForm = function() {
		
			layer.confirm('如果状态是可用状态这将会覆盖之前的可用列？', {
    			btn: ['确定','取消'] //按钮
    			// ids: ids
			}, function(){
    	    	//关闭提示框
    	    	layer.closeAll();
			
			var costId = $("#costName").val();
			var costName = $('#costName option:selected').text();
			
			$scope.formData.states = $("#states").val();
			$scope.formData.costName = costName;
			$scope.formData.costId = costId;
			
			$http({
				method : 'POST',
				url : HRY.modules.customer+'agents/commissionDeploy/modified',
				data : $.param($scope.formData), 
			//	params:{'appResourceStr':ids},
				headers : {	
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			})
			.success(function(data) {
						if (data.success) {
							growl.addInfoMessage("修改成功");
							window.location.href='#/customer/agents/commissiondeploy/list/anon';
						} else {               
							growl.addInfoMessage('添加失败--- '+data.msg);
						}
				});
			});
	};
		
}
    	
    	
    	
    	
    	
 //-------------------  添加页面    ------------------------------------------------------------
    	if ($stateParams.page == "add") {
    		
    	// 重新渲染页面
    		conApp.initMaterialPlugins();
    		
 		 // 表单提交
    		
    	$scope.formData = {};
			
		$scope.processForm = function() {
			
			var costId = $("#costName").val();
			
			$http.get(HRY.modules.customer+"/agents/commissionDeploy/getCommissionDeploy?costId="+costId).
                 success(function(data) {
                 
                 	if("" != data){
                 		
                 		layer.confirm('已添加同类型收费参数配置方案,请选择修改。', {
			    			btn: ['确定','取消'] //按钮
			    			// ids: ids
						}, function(){
			    	    	//关闭提示框
			    	    	layer.closeAll();
			    	        window.location.href='#/customer/agents/commissiondeploy/modify/'+costId;
                 		})
					}else{
					 
						var costName = $('#costName option:selected').text();
						$scope.formData.states = $("#states").val();
						
						$scope.formData.oneRankRatio = $("#oneRankRatio").val();
						$scope.formData.twoRankRatio = $("#twoRankRatio").val();
						$scope.formData.threeRankRatio = $("#threeRankRatio").val();
						$scope.formData.costName = costName;
						$scope.formData.costId = costId;
						
						$http({
							method : 'POST',
							url : HRY.modules.customer+'agents/commissionDeploy/add',
							data : $.param($scope.formData), 
						    //	params:{'appResourceStr':ids},
							headers : {	
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						})
						.success(function(data) {
									if (data.success) {
										growl.addInfoMessage("添加成功");
										window.location.href='#/customer/agents/commissiondeploy/list/anon';
									} else {               
										growl.addInfoMessage('添加失败--- '+data.msg);
									}
							});
					}
      		 });
		};
    		
}
    	
    	
//------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			 hryCore.CURD({
						url : HRY.modules.customer+"agents/commissionDeploy/see/"+$stateParams.id
					}).get(null,
				function(data) {
             	$scope.model = data;
             	 
		    }, function(data) {
				Materialize.toast("error:" + data.msg, '1000');
			});
		}
       	
//------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	 
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.customer+'agents/commissionDeploy/list.do';
        		config.ajax.data = function(d){
        								//设置select下拉框
        								DT.selectData($scope.serchData);
        								
					        			$.each($scope.serchData, function(name,value){
												if(""!=value){
													eval("d."+name+"='"+value+"'");
												}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							}, {
										"data" : "costName"
									}, {
										"data" : "oneStandardValue"
									}, {
										"data" : "oneRankRatio"
									},  {
										"data" : "twoStandardValue"
									},{
										"data" : "twoRankRatio"
									}, {
										"data" : "threeStandardValue"
									}, {
										"data" : "threeRankRatio"
									},/* {
										"data" : "settlementMoney"
									},*/ {
										"data" : "states"
									}, {
										"data" : "created"
									}
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},{
											"targets" : 8,"orderable" :false,
											"render" : function(data, type, row) {
												if(data == 0){
													return "关闭";
												}else if(data == 1){
													return "开启";
												}
											}
										}
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
        		
            $scope.fnAdd=fnAdd;//add按钮方法
            $scope.fnSee=fnSee;//see按钮方法
            $scope.fnModify=fnModify;
            $scope.fnRemove=fnRemove;//remove方法
            $scope.fnList=fnList;//刷新方法

            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
            
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
            	window.location.href='#/customer/'+url+'/'+'anon';
            }
            
            //查看按钮
            //ng-click="fnSee(url,selectes)"
            function fnSee(url){
            	var ids = DT.getSelect(table);
            	//var ids = transform(selectes);
            	if(ids.length==0){
            		Materialize.toast('请选择数据', 4000)
            		return false;
            	}else if(ids.length>1){
            		Materialize.toast('只能选择一条数据', 4000)
            		return false;	
            	}else{               
            		window.location.href='#/customer/'+url+'/'+ids[0];
            	}
            }
            
            //修改按钮
            //ng-click="fnModify(url,selectes)"
            function fnModify(url){
            	 
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		layer.msg('请选择数据', {
	        		    icon: 1,
	        		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
	        		});
            		return false;
            	}else if(ids.length>1){
            		layer.msg('只能选一条数据', {
	        		    icon: 1,
	        		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
	        		});
            		return false;	
            	}else{
            		$rootScope.id= ids[0];  
            		window.location.href='#/customer/'+url+'/'+ids[0];
            	}
            }
            
            //删除按钮
            //ng-click="fnRemove(url,selectes)"
            function fnRemove(url){
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		layer.msg('请选择数据', {
	        		    icon: 1,
	        		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
	        		});
            		return false;
            	}else if(ids.length>1){
            		layer.msg('只能选一条数据', {
	        		    icon: 1,
	        		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
	        		});
            		return false;	
            	}else{
            		
        			layer.confirm('确认删除吗？', {
		    			btn: ['确定','取消'] //按钮
		    			// ids: ids
					}, function(){
		    	    	//关闭提示框
		    	    	layer.closeAll();
            		
            	   var id = ids[0]
		           $http.get(HRY.modules.customer+"/agents/commissionDeploy/remove/"+id).
		                 success(function(data) {
		                 	if(data.success){
		                 		layer.msg(data.msg, {
		    	        		    icon: 1,
		    	        		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    	        		});
		                 	}else{
		                 		layer.msg(data.msg, {
		    	        		    icon: 1,
		    	        		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    	        		});
		                 	}
		                 	fnList();
               		  });
				 });
        	  }
          }
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});