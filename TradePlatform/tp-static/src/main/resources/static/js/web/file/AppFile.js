/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams','$state'];
    function controller($scope,$rootScope,$http,$stateParams,$state,hryCore){
    	var table =  $("#table2");
      	$scope.reset=reset;
      	//重置按钮
    	function reset(){
    		$scope.serchData=hryCore.reset($scope.serchData);
    		fnList();
    	}
    	$scope.fnList=fnList;
    	 //刷新按钮
        function fnList(){
        	 table.DataTable().draw();
        }
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	 
        	var type=$stateParams.id;
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索                               
        		config.ajax.url = HRY.modules.web+'/file/appfile/list';
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
        								"data" : "fileid"
        							}, {
										"data" : "filename"
									}, {
										"data" : "extendname"
									}, {
										"data" : "filesize"
									}, {
										"data" : "created"
									}, {
										"data" : "fileRemotePath"
							
									}, {
										"data" : "fileLocalpath"
									}, {
										"data" : "created"
									}
        		                  ]
            config.columnDefs  = [{
				"targets" : 0,"orderable" :false,
					
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
				}
            },
			{
				"targets" : 5,
					
				"render" : function(data, type, row) {
					if(null!=row.fileRemotePath){
						return "ftp"
					}else{
						return "本地"
					}
				}
		  },
			{
				"targets" : 6,
					
				"render" : function(data, type, row) {
					
					if(null!=row.fileRemotePath){
						return data;
					}else{
						return row.fileLocalpath;
					}
				}
		    },
				{
					"targets" : 7,
						
					"render" : function(data, type, row) {
						
						return "<a ng-click='download("+row.fileid+")'>下载</a>"
					}
			}]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
        		
            $scope.fnList=fnList;//刷新方法
            
            
            $scope.download=download;
            function download(fileid){
            	alert(fileid);
            	
            }
             $scope.fnAdd=fnAdd;//add按钮方法
             function fnAdd(url){
            	window.location.href='#/'+url+'/anon';
            }
            
        }
        /**
         * 添加页面
         */
        if($stateParams.page=="upload"){
        	 $("#myUpload").hryDropzone({ctx : HRY.modules.web,mark:"mark"});
        	   $scope.fnBack = fnBack;
        	  function fnBack(){
            	window.location.href='#/web/file/appfile/list/anon';
            	fnList();
            }
        }
        
        if($stateParams.page=="manage"){
        	
        	
        	
        }
        
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});