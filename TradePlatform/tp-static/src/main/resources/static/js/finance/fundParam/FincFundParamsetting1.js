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
        //绑定字典内容到指定的Select控件
    	function BindSelect(ctrlName, url,settingFormData) {
    	    var control = $('#' + ctrlName);
    	    $http.get(url).
            success(function(data) {
            	 control.empty();//清空下拉框
     	        $.each(data, function (i, item) {
     	        //	if(settingFormData!=null&&settingFormData!=""&&(settingFormData.join(",")+",").indexOf(item.paramDicKey+",")>=0){
     	        		 control.append("<option selected value='" + item.paramDicKey + "'>&nbsp;" + item.text + "</option>");
     	        	/*}else
     	        		 control.append("<option value='" + item.paramDicKey + "'>&nbsp;" + item.text + "</option>");*/
     	        });
            });


    	}
    	function initfundpanel(data){
    		var pix=HRY.modules.finance+'fundParam/fincfundparamdic/getjson?Q_pparamDicKey_eq_String=';
        	$http.get(pix+"fundMoneyType").
            success(function(data) {
            	 $scope.fundMoneyType = data;
            
            });
    	//	BindSelect("repaymentMethod",pix+'repaymentMethod');
        	BindSelect("repaymentCycle",pix+'repaymentCycle',data.repaymentCycle);
        	BindSelect("preposePayAccrual",pix+'preposePayAccrual',data.preposePayAccrual);
        	BindSelect("interestByOneTime",pix+'interestByOneTime',data.interestByOneTime);
        	BindSelect("repaymentDate",pix+'repaymentDate',data.repaymentDate);
        	BindSelect("dateModel",pix+'dateModel',data.dateModel);
        	BindSelect("ccalculateFirstAndEnd",pix+'ccalculateFirstAndEnd',data.ccalculateFirstAndEnd);
        	BindSelect("intentDateType",pix+'intentDateType',data.intentDateType);
        	BindSelect("consultationMoneyWay",pix+'fundCcalculateType',data.consultationMoneyWay);
        	BindSelect("serviceMoneyWay",pix+'fundCcalculateType',data.serviceMoneyWay);
        	BindSelect("otherOneFundMoneyWay",pix+'fundCcalculateType',data.otherOneFundMoneyWay);
        	BindSelect("otherTwoFundMoneyWay",pix+'fundCcalculateType',data.otherTwoFundMoneyWay);
        	BindSelect("otherTreeFundMoneyWay",pix+'fundCcalculateType',data.otherTreeFundMoneyWay);
        	
        	
    	}
    	 //绑定字典内容到指定的Select控件
    	function previewSelected(ctrlName, url,settingFormData,isHidden) { 
    		var control = $('#'+ctrlName+"span");
    		if(isHidden){
    			control.hide();
    		}else{
		   	 $http.get(url).
		        success(function(data) {
		 	        $.each(data, function (i, item) {
		 	         if(settingFormData!=null&&settingFormData!=""&&(settingFormData.join(",")+",").indexOf(item.paramDicKey+",")>=0){
		 	        	 control.append("<input type='radio' id='"+item.paramDicKey+ ctrlName+"' name='"+ctrlName+"'  />"+
									"<label for='"+item.paramDicKey+ctrlName+"'>"+item.text+"</label>");
				    	        
		 	        	  
		 	          }
		 	         
		 	         });
		        });
    		}
   	 }
        if($stateParams.page=="next"){
           	$scope.formData=$rootScope.settingFormData;
            //  	$rootScope.settingFormData=null;
        	var pix=HRY.modules.finance+'fundParam/fincfundparamdic/getjson?Q_pparamDicKey_eq_String=';
        	$http.get(pix+"fundMoneyType").
            success(function(data) {
            	 $scope.fundMoneyType = data;
            
            });
     
        	var pix=HRY.modules.finance+'fundParam/fincfundparamdic/getjson?Q_pparamDicKey_eq_String=';

        	previewSelected("repaymentMethod",pix+'repaymentMethod',$scope.formData.repaymentMethod,$scope.formData.isHiddenRepaymentMethod);
        	previewSelected("repaymentCycle",pix+'repaymentCycle',$scope.formData.repaymentCycle,$scope.formData.isHiddenRepaymentCycle);
        	previewSelected("preposePayAccrual",pix+'preposePayAccrual',$scope.formData.preposePayAccrual,$scope.formData.isHiddenPreposePayAccrual);
        	previewSelected("interestByOneTime",pix+'interestByOneTime',$scope.formData.interestByOneTime,$scope.formData.isHiddenInterestByOneTime);
        	previewSelected("repaymentDate",pix+'repaymentDate',$scope.formData.repaymentDate,$scope.formData.isHiddenRepaymentDate);
        	previewSelected("dateModel",pix+'dateModel',$scope.formData.dateModel,$scope.formData.isHiddenDateModel);
        	previewSelected("ccalculateFirstAndEnd",pix+'ccalculateFirstAndEnd',$scope.formData.ccalculateFirstAndEnd,$scope.formData.isHiddenCcalculateFirstAndEnd);
        	previewSelected("intentDateType",pix+'intentDateType',$scope.formData.intentDateType,$scope.formData.isHiddenIntentDateType);
        	previewSelected("consultationMoneyWay",pix+'fundCcalculateType',$scope.formData.consultationMoneyWay,$scope.formData.isHiddenConsultationMoneyWay);
        	previewSelected("serviceMoneyWay",pix+'fundCcalculateType',$scope.formData.serviceMoneyWay,$scope.formData.isHiddenServiceMoneyWay);
        	previewSelected("otherOneFundMoneyWay",pix+'fundCcalculateType',$scope.formData.otherOneFundMoneyWay,$scope.formData.isHiddenOtherOneFundMoneyWay);
        	previewSelected("otherTwoFundMoneyWay",pix+'fundCcalculateType',$scope.formData.otherTwoFundMoneyWay,$scope.formData.isHiddenOtherTwoFundMoneyWay);
        	previewSelected("otherTreeFundMoneyWay",pix+'fundCcalculateType',$scope.formData.otherTreeFundMoneyWay,$scope.formData.isHiddenOtherTreeFundMoneyWay);
        	
        	$scope.processForm = function() {
        		if(!isEmpty($scope.formData.repaymentMethod)){
        			$scope.formData.repaymentMethod=$scope.formData.repaymentMethod.join(",");
        		}
        		if(!isEmpty($scope.formData.repaymentCycle)){
        		    $scope.formData.repaymentCycle=$scope.formData.repaymentCycle.join(",");
        		}
        		if(!isEmpty($scope.formData.preposePayAccrual)){
        		    $scope.formData.preposePayAccrual=$scope.formData.preposePayAccrual.join(",");
        		}
        		if(!isEmpty($scope.formData.interestByOneTime)){
        		    $scope.formData.interestByOneTime=$scope.formData.interestByOneTime.join(",");
        		}
        		if(!isEmpty($scope.formData.repaymentDate)){
        		    $scope.formData.repaymentDate=$scope.formData.repaymentDate.join(",");
        	    }
        		if(!isEmpty($scope.formData.dateModel)){
        		    $scope.formData.dateModel=$scope.formData.dateModel.join(",");
        		}
        		if(!isEmpty($scope.formData.ccalculateFirstAndEnd)){
        		    $scope.formData.ccalculateFirstAndEnd=$scope.formData.ccalculateFirstAndEnd.join(",");
        		}
        		if(!isEmpty($scope.formData.intentDateType)){
        		     $scope.formData.intentDateType=$scope.formData.consultationMoneyWay.join(",");
        		}
        		if(!isEmpty($scope.formData.consultationMoneyWay)){
        		     $scope.formData.consultationMoneyWay=$scope.formData.serviceMoneyWay.join(",");
        		}
        		if(!isEmpty($scope.formData.serviceMoneyWay)){
        		     $scope.formData.serviceMoneyWay=$scope.formData.serviceMoneyWay.join(",");
        		}
        		if(!isEmpty($scope.formData.otherOneFundMoneyWay)){
        		     $scope.formData.otherOneFundMoneyWay=$scope.formData.otherOneFundMoneyWay.join(",");
        		}
        		if(!isEmpty($scope.formData.otherTwoFundMoneyWay)){
        		     $scope.formData.otherTwoFundMoneyWay=$scope.formData.otherTwoFundMoneyWay.join(",");
        		} 
        		if(!isEmpty($scope.formData.otherTreeFundMoneyWay)){
        		     $scope.formData.otherTreeFundMoneyWay=$scope.formData.otherTreeFundMoneyWay.join(",");
        		}
				$http({
					method : 'POST',
					url : HRY.modules.finance+'fundParam/fincfundparamsetting/add',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							if (data.success) {
								growl.addInfoMessage('添加成功')
							} else {
								growl.addInfoMessage('添加失败')
							}
	
						});
	
			};
        	
        }
        if($stateParams.page=="add"){
        	
        	
        	
        	initfundpanel();
        	
        	$scope.formData={};
        	$scope.formData.isHiddenDateModel=false;
        	$scope.formData.isHiddenRepaymentMethod=false;
        	$scope.formData.isHiddenRepaymentCycle=false;
        	$scope.formData.isHiddenRepaymentDate=false;
        	$scope.formData.isHiddenPreposePayAccrual=false;
        	$scope.formData.isHiddenInterestByOneTime=false;
        	$scope.formData.isHiddenCcalculateFirstAndEnd=false;
        	$scope.formData.isHiddenIntentDateType=false;
        	$scope.formData.isHiddenServiceMoneyWay=false;
        	$scope.formData.isHiddenConsultationMoneyWay=false;
        	$scope.formData.isHiddenOtherOneFundMoneyWay=false;
        	$scope.formData.isHiddenOtherTwoFundMoneyWay=false;
        	$scope.formData.isHiddenOtherTreeFundMoneyWay=false;
        	
        	
        	$scope.nextForm=function (){
        		
        		$rootScope.settingFormData=$scope.formData;
        	
        		window.location.href='#/finance/fundParam/fincfundparamsetting/next/anon';
        		
        	}
        	
        }
 
			
        /**
         * 列表页面
         */
        if($stateParams.page=="list"){
        	
            fnList();
            
        
            
            $scope.fnList=fnList;//list方法
            $scope.changeClass=changeClass;
            $scope.fnAdd=fnAdd;
            $scope.fnRemove=fnRemove;
            //添加按钮
            function fnAdd(url){
            	window.location.href='#/finance/'+url+'/anon';
            }
            
            function changeClass(manyLevel){
            	var objectclass=$("ul[class=_tt_side_subnav]");
            	var objectclasschildren=objectclass.children();
            	objectclasschildren.removeClass("active");
            	var object=$("li[name="+manyLevel.paramsettingKey+"]");
            	object.addClass("active");
            	$rootScope.selectFincFundParamsettingId=manyLevel.paramsettingId;
            	hryCore.CURD(
    					{
    						url : HRY.modules.finance
    								+ 'fundParam/fincfundparamsetting/load'
    					}).get({
    						id:manyLevel.paramsettingId
    			}, function(data) {
    				/*$.ajax({
    	         		   type: "GET",
    	         		   async :false,
    	         		  url : HRY.modules.finance
							+ 'fundParam/fincfundparamsetting/load?id='+manyLevel.paramsettingId,
    	         		   success: function(data){*/
    	         	//		  $scope.formData=$.parseJSON(data);
    	         			 $scope.formData=	 data
    				
            		   hryCore.initPlugins();
         	
    			}, function(data) {
    				growl.addInfoMessage("error:" + data.msg);
    			});}
            
         
        	//删除按钮
            function fnRemove(url){
            	 $scope.paramsettingId=$rootScope.selectFincFundParamsettingId;
            	 $rootScope.selectFincFundParamsettingId=null;
            	hryCore
				.CURD({
						url:HRY.modules.finance+url+"/"+ $scope.paramsettingId
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
            	
			}
            
           function fnModify(url,manyLevel){
        	   $stateParams.id=manyLevel.paramDicId;
        	   window.location.href='#/finance/'+url+'/'+manyLevel.paramDicId;
           }
            /**
             * 加载数据
             */
            function fnList(){
            	 $http.get( HRY.modules.finance+'fundParam/fincfundparamsetting/list').
                 success(function(data) {
                 	 $scope.list = data.rows;
                 
                 });
         /*   	  
         		hryCore.CURD(
    					{
    						url : HRY.modules.finance
    								+ 'fundParam/fincfundparamsetting/listByone'
    					}).get({
    			}, function(data) {*/
    			$.ajax({
         		   type: "GET",
         		   async :false,
         		   url : HRY.modules.finance+ 'fundParam/fincfundparamsetting/listByone',
         		   success: function(data){
         			    
         			   $scope.formData=$.parseJSON(data)
    			
         		  }, 
       		   error: function(e) { 
       			growl.addInfoMessage("error:" + data.msg);
   			   } 
   		})
            }
     
            
             $scope.nextForm=function (){
        		
        		$rootScope.settingFormData=$scope.formData;
        	
        		window.location.href='#/finance/fundParam/fincfundparamsetting/next/anon';
        		
        	}
    
  
 
        
        }
        hryCore.initPlugins();
    }
 
    return {controller:controller};
});