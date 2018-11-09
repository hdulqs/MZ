/**
 * 
 */
define(['app','hryTable'], function (app,DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "第三方参数设置";
    	//初始化js 插件
    	
    	var table =  $("#table2");
    	
		//------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
		  $scope.infoModel={};
		    var currentType=$("#currentType").val();
			 
			 $http({
					method : 'POST',
					url : HRY.modules.thirdpay+"app/appconfig/getInfo",
					params : {'currentType':currentType}, 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
					 $scope.infoModel = data;
				});
			 
		}

    	
		//------------------------增加页面路径---------------------------------------------
        if($stateParams.page=="add"){
        	$scope.formData = {};
			$scope.processForm = function() {
				 
				//附值权限id
				$scope.formData.appResourceSet = $("#appResourceSet").val();
				$http({
					method : 'POST',
					url : HRY.modules.web+'app/appsetting/add',
					data : $.param($scope.formData), 
				//	params:{'appResourceStr':ids},
					headers : {	
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							if (data.success) {
								growl.addInfoMessage('添加成功')
								window.location.href='#/web/app/appsetting/list/anon';
							} else {
								growl.addInfoMessage('添加失败')
							}
	
				});
	
			};
        }
        
      //------------------------修改页面路径---------------------------------------------
        if($stateParams.page=="modify"){
        	$http.get(HRY.modules.web+"/pay/appthirdpayconfig/current").
            success(function(data) {
          	    $scope.formData = data;
            
		   });
        	
         $scope.fnQuery=fnQuery;
        	  function fnQuery(url){
        		 
        		window.location.href='#/third/pay/appthirdpayconfig/see/anon';
					
             }
        	  $scope.fnUpdate=fnUpdate;
        	  
        	  function fnUpdate(url){
        		  var environment=$scope.formData.thirdPayEnvironment;
        		
        		var thirdPayConfig=$("#thirdPaySet").val();
        		   $http({
     					method : 'POST',
     					url : HRY.modules.thirdpay+"pay/appthirdpayconfig/modify",
     					params : {'thirdPayConfig':thirdPayConfig,"environment":environment}, 
     					headers : {
     						'Content-Type' : 'application/x-www-form-urlencoded'
     					}
     				})
     				.success(function(data) {
     					$http.get(HRY.modules.web+"/pay/appthirdpayconfig/current").
     		            success(function(data) {
     		          	    $scope.formData = data;
     		            
     				   });
     					//window.location.href='#/third/pay/appthirdpayconfig/modify/anon';
     				});
         		 
          		
  					
               }
        }
        
       	
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	
        	
        	$http({
				method : 'POST',
				url : HRY.modules.thirdpay+"/pay/appThirdPayConfig/select",
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			})
			.success(function(data) {
					$scope.configData=JSON.parse(data.obj);
			
			});
           $scope.fnAdd=fnAdd;//see按钮方法
            $scope.fnModify=fnModify;//see按钮方法
            $scope.fnList=fnList;//刷新方法
            $scope.fnUpdate=fnUpdate;
            $scope.fnQuery=fnQuery;
            
            function fnUpdate(){
            	var value =""; 
            	$('input[name="checkboxName"]:checked').each(function(){ 
            		value=value+","+$(this).val(); 
            	}); 
            	
        		$http({
					method : 'POST',
					url : HRY.modules.web+'app/appcache/update',
					params:{"types":value},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							if (data.success) {
								growl.addInfoMessage('刷新成功')
							} else {
								growl.addInfoMessage('刷新失败')
								
							}
	
				});
           }
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
            
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
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
            		window.location.href='#/web/'+url+'/'+ids[0];
            	}
            }
            
            
            
            
            
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});