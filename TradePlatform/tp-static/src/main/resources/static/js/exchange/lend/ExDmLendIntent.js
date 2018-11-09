/**
 * ExDmLend.js
 */
define(['app','hryTable','hryUtil'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams','$location'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore,$location ){
    	$rootScope.headTitle = $rootScope.title = "客户账号管理";
    	//初始化js 插件
    	
    	
    	var table =  $("#table2");
       	
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="find"){
        	  var id=$location.search().id;
        	$scope.serchData={
        		//	customerId_EQ : id
        	};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.exchange+'lend/exdmLendIntent/find';
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
										"data" : "trueName"
									}, {
										"data" : "lendCoinType"
									}, {
										"data" : "lendCount"
									}, {
										"data" : "lendCount"
									},  {
										"data" : "repayCount"
									},  {
										"data" : "factTime"
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
											"targets" : 3,"orderable" :false,
											"render" : function(data, type, row) {
												if(row.lendCoinType == 'virtualCoin'){
													return " ---- "
												}else{
													return "融资"
												}
											}
										},
										
										{
											"targets" : 4,"orderable" :false,
											"render" : function(data, type, row) {
												if(row.lendCoinType == 'money'){
													return data
												}else{
													return " ---- "
												}
											}
										}
										,
										{
											"targets" : 5,"orderable" :false,
											"render" : function(data, type, row) {
												if(row.lendCoinType == 'virtualCoin'){
													return data
												}else{
													return " ---- "
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
            
            //导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"杠杆收费台账");
			}
			
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
            	window.location.href='#/oauth/'+url+'/anon';
            }
            
        
        
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});