/**
 * coin.js
 */
define(['app','hryTable'], function (app,DT) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	
    	//初始化js 插件
    	
    	var table =  $("#table2");
    	
		//------------------------查看页面路径---------------------------------------------
	
/*    	$scope.creatSelect = function (){
    		$http.get(HRY.modules.exchange+"product/exProductparameter/selectPeoductParameter").
	            	success(function(data) {
	            		 $scope.parameter=data[0]
	                	 $scope.productParameter = data;
	                	 var html = "";
	                	 for(var i = 0;i<data.length;i++){
	            		      html += "<option value='"+data[i].id+"' >"+data[i].name+"</option>"; 
	                	 }
	                	$("#wselectid").append(html); 
	                    hryCore.initPlugins(); 
	                
	          });
    	
    	}*/
    	
  	
	

       // 
       	
      
        
        
        
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.exchange+'purse/cointransaction/list';
        		config.ajax.data = function(d){
        			
        		}
        	
        		$http.get(HRY.modules.exchange+"purse/cointransaction/list").
            	success(function(data) {
            	
                   $scope.list=angular.fromJson(data);
            		
                	
                
          });
            
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
         
        
        }
        
        
        
        
        //----------------------  2  --列表页面路径---------------------------------------------
        if($stateParams.page=="publish"){
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.exchange+'product/exproduct/list';
        		config.ajax.data = function(d){
					        			$.each($scope.serchData, function(name,value){
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							}, {
										"data" : "name"
									}, {
										"data" : "totalNum"
									}, {
										"data" : "issueTotalMoney"
									}, {
										"data" : "issueState"
									}, {
										"data" : "coinCode"
									}, {
										"data" : "issueName"
									}, {
										"data" : "issueTime"
									} // 
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,
											"orderable" :false,	
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},
										{
											"targets" : 4,
											"orderable" :false,	
											"render" : function(data, type, row) {
												if(data!=null&&data=="0"){
													return "尚未发行"
												}
												if(data!=null&&data=="1"){
													return "发行中"
												}
												if(data!=null&&data=="2"){
													return "停牌"
												}
												if(data!=null&&data=="3"){
													return "退市"
												}
												return "";
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
            $scope.fnPublish=fnPublish; //发布产品
            $scope.delistProduct = delistProduct;  // 产品退市
            	  
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
         
            
            
            

        
  }
       
    }
    return {controller:controller};
});