/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams','$state'];
    function controller($scope,$rootScope,$http,$stateParams,$state,hryCore){
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.spage=="list"){
        	
           //图片
	 	   $scope.fnSearchImg = function(){
	 		  $http({
					method : 'POST',
					url : HRY.modules.web+"/file/appfilerelation/findByOrgId",
					data : $.param({
						fileType : "img",
						fileName : $scope.fileName,
						orgId : $stateParams.ppage
					}),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					$scope.imgList = data;
				});
	 		   
	 	   }
	 	  //第一次进来加载图片
	 	  $scope.fnSearchImg();
	 	   
	 	  //文件
	 	  $scope.fnSearchFile = function(){
	 		  $http({
					method : 'POST',
					url : HRY.modules.web+"/file/appfilerelation/findByOrgId",
					data : $.param({
						fileType : "file",
						fileName : $scope.fileName,
						orgId : $stateParams.ppage
					}),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					$scope.fileList = data;
				});
	 	   }
	 	 
	 	  /**
	 	   * 下载说明 
	 	   * 二次请求
	 	   * 第一次ajax请求校验文件是否存在
	 	   * 第二次form请求下载文件流
	 	   */
	 	 $scope.fnDown = function(target){
	 		var fileWebPath = target.currentTarget.getAttribute("path");
	 		function DownLoad(fileWebPath) { 
	            var form = $("<form>");   //定义一个form表单
	            form.attr('style', 'display:none');   //在form表单中添加查询参数
	            form.attr('target', '');
	            form.attr('method', 'post');
	            form.attr('action', HRY.modules.web+"/file/download");
	            var input1 = $('<input>');
	            input1.attr('type', 'hidden');
	            input1.attr('name', 'fileWebPath');
	            input1.attr('value', fileWebPath);
	            $('body').append(form);  //将表单放置在web中 
	            form.append(input1);   //将查询参数控件提交到表单上
	            form.submit();
	         }
	 		 
	 		$http({
				method : 'POST',
				url : HRY.modules.web+"/file/checkfile",
				data : $.param({
					fileWebPath:fileWebPath
				}),
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			}).success(function(data) {
				if(data!=undefined&&data.success){
					DownLoad(fileWebPath);
				}else{
					growl.addInfoMessage("error:" + data.msg);
				}
			 });
	 		 
	 	 }
	 	 
	 	/**
	 	   * 下载说明 
	 	   * 二次请求
	 	   * 第一次ajax请求校验文件是否存在
	 	   * 第二次form请求下载文件流
	 	   */
	 	 $scope.fnDelete = function(target,type){
	 		var fileWebPath = target.currentTarget.getAttribute("path");
	 		$http({
				method : 'POST',
				url : HRY.modules.web+"/file/delete",
				data : $.param({
					fileWebPath:fileWebPath
				}),
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			}).success(function(data) {
				 
				if(data!=undefined&&data.success){
					
					growl.addInfoMessage("删除成功");
					if(type=="img"){
						$scope.fnSearchImg();
					}else if(type="file"){
						$scope.fnSearchFile();
					}
				}else{
					growl.addInfoMessage("error:" + data.msg);
				}
			 });
	 		 
	 	 }
	 	
	 	 
        	
        }
        hryCore.initPlugins();
    }
    return {controller:controller};
});