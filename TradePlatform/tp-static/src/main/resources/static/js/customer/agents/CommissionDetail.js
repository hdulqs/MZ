/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams','$location'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ,$location){
    	$rootScope.headTitle = $rootScope.title = "客户账号管理";
    	//初始化js 插件
    	
    	
    	var table =  $("#table2");
 
    	
 // -----------------  modify页面  ---------------------------------------------------------------------   	
    	
    	if($stateParams.page == "modify"){}
    
    	
 //-------------------  添加页面    ------------------------------------------------------------
    	if ($stateParams.page == "add") {}
    	
    	
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
        	 var n=$location.search().name;
	        	//name不为空时 查询单个用户的信息
			 if(n != undefined && n!="" ){
        		 $scope.serchData={
        			 agentsName_like:n
              	};
        	 }else{
        		 $scope.serchData={
          				
               	}; 
         	 }
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.customer+'agents/commissionDetail/list.do';
        		config.ajax.data = function(d){
        								//设置select下拉框R
        								// DT.selectData($scope.serchData);
        								
					        			$.each($scope.serchData, function(name,value){
					        				
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							}, {
										"data" : "agentsName"
									}, {
										"data" : "orderNum"
									},  {
										"data" : "deliveryName"
									},{
										"data" : "fixPriceCoinCode"
									},/* {
										"data" : "fixPriceType"
									},*/
									
									{
										"data" : "category"
									},{
										"data" : "agentsRank"
									}, {
										"data" : "totalFee"
									}, {
										"data" : "rate"
									}, {
										"data" : "deliveryMoney"
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
										},
										
										{
											"targets" :3,"orderable" :false,
												
											"render" : function(data, type, row) {
												if(data!=null){
													return data;
												}else{
													return "暂无"
												}
											}
										},
										/*{
											"targets" : 4,
											"render" : function(data, type, row) {debugger;
												if(data == 1){
													return "虚拟货币"
												}if(data == 0){
													return "真实货币"
												}
												else{
													return "暂无"
												}
											}
										},*/
										
										{
											"targets" : 5,
											"render" : function(data, type, row) {
												if(data == 1){
													
													return "提现"
												}if(data == 21){
													return "交易买"
												}if(data == 22){
													return "交易卖"
												}if(data == 4){
													return "认购"
												}else{
													return " --- "
												}
											}
										},{
											"targets" : 6,"orderable" :false,
												
											"render" : function(data, type, row) {
												if(data == 1){
													return "一级代理商"
												}if(data == 2){
													return "二级代理商"
												}if(data == 3){
													return "三级代理商"
												}else{
													return "未知代理商";
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
            		Materialize.toast('请选择数据', 4000)
            		return false;
            	}else if(ids.length>1){
            		Materialize.toast('只能选择一条数据', 4000)
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
            		Materialize.toast('请选择数据', 4000)
            		return false;
            	}else if(ids.length>1){
            		Materialize.toast('只能选择一条数据', 4000)
            		return false;	
            	}else{
            	   var id = ids[0]
		           $http.get(HRY.modules.customer+"/agents/commissionDeploy/remove/"+id).
		                 success(function(data) {
		                 	if(data.success){
		                 		Materialize.toast(data.msg, 4000)
		                 		
		                 	}else{
		                 		Materialize.toast("error:" + data.msg);
		                 	}
		                 	fnList();
		                 	
                 });
        	    }
          }
       
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});