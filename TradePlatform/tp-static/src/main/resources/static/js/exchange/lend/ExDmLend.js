/**
 * ExDmLend.js
 */
define(['app','hryTable','pikadayJq' ,'hryUtil'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams','$location'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore,$location ){
    	$rootScope.headTitle = $rootScope.title = "客户账号管理";
    	//初始化js 插件
    	$scope.unitTips=tips.unitTips;
    	
    	var table =  $("#table2");
    	
	 //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="find"){
    	
        	  var id=$location.search().id;
          	$scope.serchData={
          			customerId_EQ : id
          	};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.exchange+'lend/exdmLend/find';
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
										"data" : "userName"
									}, {
										"data" : "lendNum"
									}, {
										"data" : "lendCount"
									},  {
										"data" : "lendCoin"
									},{
										"data" : "status"
									}
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
												
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},{
											"targets" : 5,
												
											"render" : function(data, type, row) {
												if(data!=null&&data=="0"){
													return "借款开始"
												}else if(data=="1"){
													return "借款中"
												}else if(data=="2"){
													return "部分还款"
												}else if(data=="3"){
													return "全部还完"
												}
												
											}
										}
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
       		 
           

       
            
     
            //刷新按钮
            function fnList(){
            	   
            	 table.DataTable().draw();
            }
            
            
          
            
        
        
        }
        if($stateParams.page=="lendTimes"){
    	
        	  var id=$location.search().id;
          	$scope.serchData={
          			customerId_EQ : id
          	};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.exchange+'lend/exdmLend/lendTimes';
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
										"data" : "userName"
									}, {
										"data" : "lendNum"
									}, {
										"data" : "lendCount"
									},  {
										"data" : "lendCoin"
									},{
										"data" : "status"
									}
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
												
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},{
											"targets" : 5,
												
											"render" : function(data, type, row) {
												if(data!=null&&data=="0"){
													return "借款开始"
												}else if(data=="1"){
													return "借款中"
												}else if(data=="2"){
													return "部分还款"
												}else if(data=="3"){
													return "全部还完"
												}
												
											}
										}
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
       		 
           

       
            
     
            //刷新按钮
            function fnList(){
            	   
            	 table.DataTable().draw();
            }
            
            
          
            
        
        
        }
        
        
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="see"){
        	$http({
				method : 'GET',
				url : HRY.modules.exchange+'lend/exdmLend/getLending',
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			}).success(function(data) {
				$scope.lending = data;
			});
        	
        	
        	  var id=$location.search().id;
          	$scope.serchData={
          			customerId_EQ : id
          	};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        	
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.exchange+'lend/exdmLend/see';
        		config.ajax.data = function(d){
        								//设置select下拉框
        								DT.selectData($scope.serchData);
        								
					        			$.each($scope.serchData, function(name,value){
					        				
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	{//targets
        								"data" : "id"
        							},{
										"data" : "lendTime"
									}, {
										"data" : "userName"
									}, {
										"data" : "trueName"
									}, {
										"data" : "lendCoin"
									}, {
										"data" : "lendCoinType"
									}, {
										"data" : "lendCount"
									}, {
										"data" : "lendRate"
									}, {
										"data" : "lendDay"
									},{
										"data" : "interestCount" // 总利息
									},{
										"data" : "repayInterestCount" // 已还利息
									},{
										"data" : "repayInterestCount" // 未还利息
									},{
										"data" : "repayLendCount" // 已还本金
									},{
										"data" : "status"
									}
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
												
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},{
											"targets" : 4,
												
											"render" : function(data, type, row) {
												return data;
											}
										},{
											"targets" : 5,
												
											"render" : function(data, type, row) {
												if(row.lendCoinType=="1"){
													return data
												}
													return "—— ——"
												
											}
										},{
											"targets" : 6,
												
											"render" : function(data, type, row) {
												if(row.lendCoinType=="2"){
													return data
												}
													return "—— ——"
												
											}
										},{
											"targets" : 11,
											"render" : function(data, type, row) {
												 return row.interestCount - row.repayInterestCount;
												//  return "";
											}
										},{
											"targets" : 13,
											"render" : function(data, type, row) {
												if(data!=null&&data=="0"){
													return "借款开始"
												}else if(data=="1"){
													return "借款中"
												}else if(data=="2"){
													return "部分还款"
												}else if(data=="3"){
													return "全部还完"
												}
												
											}
										}
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
       		 
           

       
            
     
            //刷新按钮
            function fnList(){
            	   
            	 table.DataTable().draw();
            }
            
            
            $scope.fnExcel = function() {
				DT.excel(table,this.serchData,"杠杆收费台账");
			}
            
        
        
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});