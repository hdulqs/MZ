/**
 * AppConfig.js
 */
define(['app'], function (app) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller(hryCore,$scope,$rootScope,$http,$stateParams){
        $rootScope.headTitle = $rootScope.title = "系统配置";
        
        /**
         * 添加页面
         */
        if($stateParams.page=="add"){
			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : HRY.modules.finance+'fundParam/fincfundparamdic/add',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							if (data.success) {
								growl.addInfoMessage('添加成功')
								window.location.href='#/web/app/appconfig/list/anon';
							} else {
								growl.addInfoMessage('添加失败')
							}
	
						});
	
			};
        }
        
        /**
         * 查看页面
         */
        if($stateParams.page=="see"){
        	$http.get( HRY.modules.finance+"fundParam/fincfundparamdic/see?id="+$stateParams.id).
                 success(function(data) {
                 	 $scope.model = data;
            });
        	
        }
        
        /**
         * 修改页面
         */

		/**
		 * 查看加载数据
		 */
		if ($stateParams.page == "see" || $stateParams.page == "modify") {
             
			$scope.formData = {};// 查找表单数据
			$scope.modify = modify;// 修改方法
			$scope.reset = function reset() {
				$scope.formData = {}
			};// 增加/修改页面重置
            $scope.back=function(){
        		
        		window.location.href='#/finance/fundParam/fincfundparamdic/list/anon';
        	}
			hryCore.CURD(
					{
						url : HRY.modules.finance
								+ 'fundParam/fincfundparamdic/load/:id'
					}).get({
						id : $stateParams.id
			}, function(data) {
				$scope.formData = data;

			}, function(data) {
				growl.addInfoMessage("error:" + data.msg);
			});

			/**
			 * 更新方法
			 */
			function modify() {

				hryCore
						.CURD(
								{
									url : HRY.modules.finance
											+ 'fundParam/fincfundparamdic/modify'
								})
						.save(
								$scope.formData,
								function(data) {
									growl.addInfoMessage(data.msg);

								},
								function(data) {
									growl.addInfoMessage("error:"
											+ data.msg);
								});
			}

		}

	
   
        
       	
        /**
         * 列表页面
         */
        if($stateParams.page=="list"){
        	
            fnList();
            
            $scope.fnToggleAll = fnToggleAll; //全选
            $scope.fnToggleAll = fnToggleAll;//选择一条
            $scope.selected = {};
            $scope.selectAll = false;
            
            $scope.fnModify=fnModify;//see按钮方法
            $scope.fnList=fnList;//list方法
            $scope.changeClass=changeClass;
            $scope.fnAdd=fnAdd;
            
            //添加按钮
            function fnAdd(url){
            	window.location.href='#/finance/'+url+'/anon';
            }
            function changeClass(manyLevel){
            	var objectclass=$("ul[class=_tt_side_subnav]");
            	var objectclasschildren=objectclass.children();
            	objectclasschildren.removeClass("active");
            	var object=$("li[name="+manyLevel.paramDicKey+"]");
            	object.addClass("active");
            	
            	$http.get( HRY.modules.finance+'fundParam/fincfundparamdic/listBypKey?Q_pparamDicKey_eq_String='+manyLevel.paramDicKey).
                success(function(data) {
                	 $scope.listByManyLevel = data.rows;
                	 for(var id in $scope.list){
                     	$scope.selected[$scope.list[id].id]=false;
                    }
                });
         	
            }
             
            //查看按钮
            //ng-click="fnSee(url,selectes)"
            function fnSee(url,paramDicKey){
            	var ids = transform(selectes);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(ids.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		$rootScope.id= ids[0];
            		window.location.href='#/'+url+'/'+$rootScope.id;
            	}
            }
            
         
            
           function fnModify(url,manyLevel){
        	   $stateParams.id=manyLevel.paramDicId;
        	   window.location.href='#/finance/'+url+'/'+manyLevel.paramDicId;
           }
         //自动搜索框
		    $('input.dataSearch').on( 'keyup', function () {
		    	searchList();
		    });
           function searchList(){
           	var object=$("input[name=Q_text_like_String]");
           	 $http.get( HRY.modules.finance+'fundParam/fincfundparamdic/listByoneLevel?Q_text_like_String='+encodeURI(object.val())).
                success(function(data) {
                	 $scope.listByoneLevel = data.rows;
                	 for(var id in $scope.list){
                     	$scope.selected[$scope.list[id].id]=false;
                    }
                });
           	 
          
           	
           }
            /**
             * 加载数据
             */
            function fnList(){
            	 $http.get( HRY.modules.finance+'fundParam/fincfundparamdic/listByoneLevel').
                 success(function(data) {
                 	 $scope.listByoneLevel = data.rows;
                 
                 });
            	 
            	 $http.get( HRY.modules.finance+'fundParam/fincfundparamdic/listByManyLevel').
                 success(function(data) {
                 	 $scope.listByManyLevel = data.rows;
                 	
                 });
            	
            }
            
            
            /**
            * <p> 将对象中 为true 的对象解析为数组，用于删除操作</p> 
            * @param: @param obj
            * @param: @returns {Array} 
            * @return: Array 
            * @throws
             */
            function transform(obj){
                var arr = [];
                for(var item in obj){
                	if(obj[item]){arr.push(item)};
                }
                //重置
                $scope.selected = {};
                return arr;
            }
        	/**
        	* <p> 全选</p> 
        	* @param: @param selectAll
        	* @param: @param selectedItems 
        	* @return: void 
        	* @throws
        	 */
        	function fnToggleAll (selectAll, selectedItems) {
        	    for (var id in selectedItems) {
        	        if (selectedItems.hasOwnProperty(id)) {
        	            selectedItems[id] = selectAll;
        	        }
        	    }
        	}
        	/**
        	 * 选择一条
        	 */
        	function fnToggleOne (selectedItems) {
        	    var me = $scope;
        	    for (var id in selectedItems) {
        	        if (selectedItems.hasOwnProperty(id)) {
        	            if(!selectedItems[id]) {
        	                me.selectAll = false;
        	                return;
        	            }
        	        }
        	    }
        	    me.selectAll = true;
        	}
        
        }
    }
    
    return {controller:controller};
});