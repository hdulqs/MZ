/**
 * AppConfig.js
 */
define(['app','hryTable'], function (app, DT) {
	
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
     	        		 control.append("<option  value='" + item.paramDicKey + "'>&nbsp;" + item.text + "</option>");
     	        	/*}else
     	        		 control.append("<option value='" + item.paramDicKey + "'>&nbsp;" + item.text + "</option>");*/
     	        });
            });


    	}
    	function initfundpanel(){
    		var pix=HRY.modules.finance+'fundParam/fincfundparamdic/getjson?Q_pparamDicKey_eq_String=';
        	$http.get(pix+"fundMoneyType").
            success(function(data) {
            	 $scope.fundMoneyType = data;
            
            });
        	BindSelect("repaymentMethod",pix+'repaymentMethod');
        	BindSelect("repaymentCycle",pix+'repaymentCycle');
        	BindSelect("preposePayAccrual",pix+'preposePayAccrual');
        	BindSelect("interestByOneTime",pix+'interestByOneTime');
        	BindSelect("repaymentDate",pix+'repaymentDate');
        	BindSelect("dateModel",pix+'dateModel');
        	BindSelect("ccalculateFirstAndEnd",pix+'ccalculateFirstAndEnd');
        	BindSelect("intentDateType",pix+'intentDateType');
        	BindSelect("consultationMoneyWay",pix+'fundCcalculateType');
        	BindSelect("serviceMoneyWay",pix+'fundCcalculateType');
        	BindSelect("otherOneFundMoneyWay",pix+'fundCcalculateType');
        	BindSelect("otherTwoFundMoneyWay",pix+'fundCcalculateType');
        	BindSelect("otherTreeFundMoneyWay",pix+'fundCcalculateType');
        	
/*        	
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
        	BindSelect("otherTreeFundMoneyWay",pix+'fundCcalculateType',data.otherTreeFundMoneyWay);*/
        	
        	
    	}
    	 //绑定字典内容到指定的Select控件
    	function previewSelected(ctrlName, url,settingFormData,isHidden) { 
    		var control = $('#'+ctrlName+"span");
    		var contralrate=$('#'+ctrlName+"spanrate");
    		
    		if(isHidden){
    			control.hide();
    			
    		}
    		if(null==settingFormData){
    			contralrate.remove();
    			control.hide();
    		}
    		
		   	   $http.get(url).
		          success(function(data) {
		 	        $.each(data, function (i, item) {
		 	         if(settingFormData!=null&&settingFormData!=""&&(","+settingFormData.join(",")+",").indexOf(","+item.paramDicKey+",")>=0){
		 	        	var width=120;
		 	        	 if(item.text.length>=6&&item.text.length<=8){
		 	        		width=160;
		 	        	}
		 	        	if(item.text.length>=9){
		 	        		width=200;
		 	        	}
		 	        		control.append("<span class='left w-"+width+"'><input type='radio' value='"+item.paramDicKey+"' checked  id=\""+item.paramDicKey+ ctrlName+"\" name=\""+ctrlName+"\" ng-model=\"formcaluData."+ctrlName+"\" />"+
									"<label for='"+item.paramDicKey+ctrlName+"'>"+item.text+"</label></span>");
		 	        	
		 	        	 if(ctrlName=="repaymentCycle"&&item.paramDicKey=="owerPay"){
				    		 
				    		 control.append("&nbsp;&nbsp;&nbsp;&nbsp;<span class='left'><input  id='dayOfEveryPeriod' type='text' ng-model='formcaluData.dayOfEveryPeriod' style='margin: 0!important;' ></span><span class='left'>日/每期</span>&nbsp;&nbsp;&nbsp;&nbsp;");
				    	 } 
		 	        	if(ctrlName=="repaymentDate"&&item.paramDicKey=="fixedDate"){
				    		 
				    		 control.append("&nbsp;&nbsp;&nbsp;&nbsp;<span class='left'>固定在&nbsp;&nbsp;&nbsp;&nbsp;</span><span class='left'><input  id='payintentPerioDate' type='text' ng-model='formcaluData.payintentPerioDate' style='margin: 0!important;'></span><span class='left'>号还款&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;&nbsp;&nbsp;&nbsp;");
				    	 } 
		 	          }
		 	         
		 	         });
		        	
		        	     
		        });
    		
   	 }
        if($stateParams.page=="next"){
        	$scope.back=function(){
        		
        		window.location.href='#/finance/fundParam/fincfundparamsetting/list/anon';
        	}
        	if($stateParams.id=="anon"){
        		$scope.formData=$rootScope.settingFormData;
        		initnext();
        		
        	}else{
        		
        		$("#saveNext").remove();
        	   $http.get(HRY.modules.finance+ 'fundParam/fincfundparamsetting/get?id='+$stateParams.id).
		        success(function(data) {
	         			 $scope.formData=data;
	         			if(!isEmpty($scope.formData.repaymentMethod)){
	            			$scope.formData.repaymentMethod=$scope.formData.repaymentMethod.split(",");
	            		}
	            		if(!isEmpty($scope.formData.repaymentCycle)){
	            		    $scope.formData.repaymentCycle=$scope.formData.repaymentCycle.split(",");
	            		}
	            		if(!isEmpty($scope.formData.preposePayAccrual)){
	            		    $scope.formData.preposePayAccrual=$scope.formData.preposePayAccrual.split(",");
	            		}
	            		if(!isEmpty($scope.formData.interestByOneTime)){
	            		    $scope.formData.interestByOneTime=$scope.formData.interestByOneTime.split(",");
	            		}
	            		if(!isEmpty($scope.formData.repaymentDate)){
	            		    $scope.formData.repaymentDate=$scope.formData.repaymentDate.split(",");
	            	    }
	            		if(!isEmpty($scope.formData.dateModel)){
	            		    $scope.formData.dateModel=$scope.formData.dateModel.split(",");
	            		}
	            		if(!isEmpty($scope.formData.ccalculateFirstAndEnd)){
	            		    $scope.formData.ccalculateFirstAndEnd=$scope.formData.ccalculateFirstAndEnd.split(",");
	            		}
	            		if(!isEmpty($scope.formData.intentDateType)){
	            		     $scope.formData.intentDateType=$scope.formData.intentDateType.split(",");
	            		}
	            		if(!isEmpty($scope.formData.consultationMoneyWay)){
	            		     $scope.formData.consultationMoneyWay=$scope.formData.consultationMoneyWay.split(",");
	            		}
	            		if(!isEmpty($scope.formData.serviceMoneyWay)){
	            		     $scope.formData.serviceMoneyWay=$scope.formData.serviceMoneyWay.split(",");
	            		}
	            		if(!isEmpty($scope.formData.otherOneFundMoneyWay)){
	            		     $scope.formData.otherOneFundMoneyWay=$scope.formData.otherOneFundMoneyWay.split(",");
	            		}
	            		if(!isEmpty($scope.formData.otherTwoFundMoneyWay)){
	            		     $scope.formData.otherTwoFundMoneyWay=$scope.formData.otherTwoFundMoneyWay.split(",");
	            		} 
	            		if(!isEmpty($scope.formData.otherTreeFundMoneyWay)){
	            		     $scope.formData.otherTreeFundMoneyWay=$scope.formData.otherTreeFundMoneyWay.split(",");
	            		}
	         			initnext()
     	 
				}, function(data) {
					growl.addInfoMessage("error:" + data.msg);
				});
        	}
        	
        	
        	function initnext(){
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
            	
            	
              	var pix=HRY.modules.finance+'fundParam/fincfundparamdic/getjson?Q_pparamDicKey_eq_String=';
            	$http.get(pix+"fundMoneyType").
                success(function(data) {
                    $scope.fundMoneyType = data;
                	$("#fundMoneyTypeThOne").html($scope.fundMoneyType[0].text)
       			    $("#fundMoneyTypeThTwo").html($scope.fundMoneyType[1].text)
       			    $("#fundMoneyTypeThThree").html($scope.fundMoneyType[2].text)
       			    $("#fundMoneyTypeThFour").html($scope.fundMoneyType[3].text)
       			    $("#fundMoneyTypeThFive").html($scope.fundMoneyType[4].text)
                	 
    	           $scope.formcaluData={};
    	   		   $("#startDate").bind('blur', function() { 
       			   
       			    if(!isEmpty( $(this).val())){
       				     if(isEmpty($scope.formcaluData.payintentPeriod)){
       				    	$(this).val(null);
       				    	 layer.msg('请先填写期数', {
       				    		 icon:3
       	                		});
       				    	 return false;
       				     }
       				  $scope.formcaluData.dayOfEveryPeriod= $("#dayOfEveryPeriod").val();
       				  $scope.formcaluData.repaymentCycle= $('input:radio[name=repaymentCycle]:checked').val();
	                  	if($scope.formcaluData.repaymentCycle=="owerPay"&&isEmpty($scope.formcaluData.dayOfEveryPeriod)){
	                  		$(this).val(null);
	                  		layer.msg('请先填自定义天数', {
	                     		    icon:3
	                     		});
	                  		
	                  		return false;
	                  	} 
       				   
       					$scope.formcaluData.repaymentMethod=$('input:radio[name=repaymentMethod]:checked').val();
       	            	
       	            	$scope.formcaluData.repaymentDate=$('input:radio[name=repaymentDate]:checked').val();
       	            	$scope.formcaluData.ccalculateFirstAndEnd=$('input:radio[name=ccalculateFirstAndEnd]:checked').val();
       	            	$scope.formcaluData.interestByOneTime=$('input:radio[name=interestByOneTime]:checked').val();
       	            	$scope.formcaluData.preposePayAccrual=$('input:radio[name=repaymentMethod]:checked').val();
       	            	$scope.formcaluData.dateModel=$('input:radio[name=dateModel]:checked').val();
       	            	$scope.formcaluData.intentDateType=$('input:radio[name=intentDateType]:checked').val();
       	            	$scope.formcaluData.consultationMoneyWay=$('input:radio[name=consultationMoneyWay]:checked').val();
       	            	$scope.formcaluData.serviceMoneyWay=$('input:radio[name=serviceMoneyWay]:checked').val();
       	            	$scope.formcaluData.otherOneFundMoneyWay=$('input:radio[name=otherOneFundMoneyWay]:checked').val();
       	            	$scope.formcaluData.otherTwoFundMoneyWay=$('input:radio[name=otherTwoFundMoneyWay]:checked').val();
       	            	$scope.formcaluData.otherTreeFundMoneyWay=$('input:radio[name=otherTreeFundMoneyWay]:checked').val();
       	            	 
       				  $http({
       						method : 'POST',
       						url : HRY.modules.finance+'fundParam/fincfundparamdic/caluIntentDate',
       						data : $.param($scope.formcaluData), 
       						headers : {
       							'Content-Type' : 'application/x-www-form-urlencoded'
       						}
       					})
       					.success(function(data) {
       						     // $("#intentDate").val(data);
       						      $scope.formcaluData.intentDate=data;
       							});
       			  }
       			  });
       		   
       		  
       			var table =  $("#dataTable");
               	$scope.serchData={};
               	//--------------------加载dataTable--------------------------------
               	var config = DT.config();
               		config.bAutoSerch = false; //是否开启键入搜索
               		config.bPaginate= false,//是否开启分页
               		config.ajax.url = HRY.modules.finance+'fund/fincborrowerfund/fundCalcu';
               		config.ajax.data = function(d){
               		  d.payintentPeriod=$scope.formcaluData.payintentPeriod;
               		  d.projectMoney=$scope.formcaluData.projectMoney;
               		  d.startDate=$scope.formcaluData.startDate;
               		  d.intentDate=$scope.formcaluData.intentDate;
               		  d.loanInterestMoneyDayRate=$scope.formcaluData.loanInterestMoneyDayRate;
               		  d.consultationMoneyDayRate=$scope.formcaluData.consultationMoneyDayRate;
               		  d.serviceMoneyDayRate=$scope.formcaluData.serviceMoneyDayRate;
               		  d.otherOneFundMoneyDayRate=$scope.formcaluData.otherOneFundMoneyDayRate;
               		  d.otherTwoFundMoneyDayRate=$scope.formcaluData.otherTwoFundMoneyDayRate;
               		  d.otherTreeFundMoneyDayRate=$scope.formcaluData.otherTreeFundMoneyDayRate;
               	      d.dayOfEveryPeriod= $("#dayOfEveryPeriod").val();
               	      d.payintentPerioDate= $("#payintentPerioDate").val();
               		  d.repaymentMethod=$('input:radio[name=repaymentMethod]:checked').val();
               		  d.repaymentCycle= $('input:radio[name=repaymentCycle]:checked').val();
               		  d.repaymentDate= $('input:radio[name=repaymentDate]:checked').val();
               		  d.ccalculateFirstAndEnd= $('input:radio[name=ccalculateFirstAndEnd]:checked').val();
               		  d.interestByOneTime= $('input:radio[name=interestByOneTime]:checked').val();
               		  d.preposePayAccrual= $('input:radio[name=preposePayAccrual]:checked').val();
               		 
               		
               		  d.dateModel=$('input:radio[name=dateModel]:checked').val();
               		  d.intentDateType=$('input:radio[name=intentDateType]:checked').val();
               		  d.consultationMoneyWay=$('input:radio[name=consultationMoneyWay]:checked').val();
               		  d.serviceMoneyWay=$('input:radio[name=serviceMoneyWay]:checked').val();
               		  d.otherOneFundMoneyWay=$('input:radio[name=otherOneFundMoneyWay]:checked').val();
               		  d.otherTwoFundMoneyWay=$('input:radio[name=otherTwoFundMoneyWay]:checked').val();
               		  d.otherTreeFundMoneyWay=$('input:radio[name=otherTreeFundMoneyWay]:checked').val();
               		  
               		  
               		//  d.paramsettingId=$stateParams.id;
                    };
                    
                    config.columns = [	{
    						"data" : "payintentPeriod"
    					},{
    						"data" : "intentDate"
    					}, {
    						"data" : "principalLendingMoney.payMoney"
    					}, {
    						"data" : "principalRepaymentMoney.incomeMoney"
    					}, {
    						"data" : "loanInterestMoney.incomeMoney"
    					}];
    				 if($scope.formData.consultationMoneyWay!=null){
    					 config.columns.push({
    							"data" : "consultationMoney.incomeMoney"
    						});
    				 }else{
    					 $("#fundMoneyTypeThOne").remove();
    				 }
    				 if($scope.formData.serviceMoneyWay!=null){
    					 config.columns.push({
    							"data" : "serviceMoney.incomeMoney"
    						});
    				 }else{
    					 $("#fundMoneyTypeThTwo").remove();
    				 }
    				 if($scope.formData.otherOneFundMoneyWay!=null){
    					 config.columns.push({
    							"data" : "otherOneFundMoney.incomeMoney"
    						});
    				 }else{
    					 $("#fundMoneyTypeThThree").remove();
    				 }
    				 if($scope.formData.otherTwoFundMoneyWay!=null){
    					 config.columns.push({
    							"data" : "otherTwoFundMoney.incomeMoney"
    						});
    				 }else{
    					 $("#fundMoneyTypeThFour").remove();
    				 }
    				 if($scope.formData.otherTreeFundMoneyWay!=null){
    					 config.columns.push({
    							"data" : "otherTreeFundMoney.incomeMoney"
    						});
    				 }else{
    					 $("#fundMoneyTypeThFive").remove();
    				 }
               		config.columnDefs  = [
							{
								"targets" : 1,
								"orderable" :true,	
								"render" : function(data, type, row) {
									return data.substring(0,10)
								},
							}
       									 ]
               	DT.draw(table,config);
                   
        
                
                });
        	}
      
          $scope.fnCalcu=function(url){
              		
      			    fnList1();
             		
             	    }
            //刷新按钮
            function fnList1(){
            /*	$scope.formcaluData.repaymentMethod=$('#repaymentMethodspan input[name="repaymentMethod"]').val();
            	$scope.formcaluData.repaymentCycle=$('#repaymentCyclespan input[name="repaymentCycle"]').val();
            	$scope.formcaluData.dayOfEveryPeriod=$('#dayOfEveryPeriodspan input[name="dayOfEveryPeriod"]').val();
            	$scope.formcaluData.ccalculateFirstAndEnd=$('#ccalculateFirstAndEndspan input[name="ccalculateFirstAndEnd"]').val();
            	$scope.formcaluData.interestByOneTime=$('#interestByOneTimespan input[name="interestByOneTime"]').val();
            	$scope.formcaluData.preposePayAccrual=$('#preposePayAccrualspan input[name="preposePayAccrual"]').val();
            	$scope.formcaluData.dateModel=$('#dateModelspan input[name="dateModel"]').val();
            	$scope.formcaluData.intentDateType=$('#intentDateTypespan input[name="intentDateType"]').val();
            	$scope.formcaluData.consultationMoneyWay=$('#consultationMoneyWayspan input[name="consultationMoneyWay"]').val();
            	$scope.formcaluData.serviceMoneyWay=$('#serviceMoneyWayspan input[name="serviceMoneyWay"]').val();
            	$scope.formcaluData.otherOneFundMoneyWay=$('#otherOneFundMoneyWayspan input[name="otherOneFundMoneyWay"]').val();
            	$scope.formcaluData.otherTwoFundMoneyWay=$('#otherTwoFundMoneyWayspan input[name="otherTwoFundMoneyWay"]').val();
            	$scope.formcaluData.otherTreeFundMoneyWay=$('#otherTreeFundMoneyWayspan input[name="otherTreeFundMoneyWay"]').val();
            	alert($scope.formcaluData.repaymentCycle);*/
            	
            	
            	$scope.formcaluData.repaymentDate= $('input:radio[name=repaymentDate]:checked').val();
            	$scope.formcaluData.payintentPerioDate= $("#payintentPerioDate").val();
            	if($scope.formcaluData.repaymentDate=="fixedDate"&&isEmpty($scope.formcaluData.payintentPerioDate)){
            		layer.msg('请先填写固定日', {
               		    icon:3
               		});
                    return false;
            	}
            	$scope.formcaluData.dayOfEveryPeriod= $("#dayOfEveryPeriod").val();
            	$scope.formcaluData.repaymentCycle= $('input:radio[name=repaymentCycle]:checked').val();
            	if($scope.formcaluData.repaymentCycle=="owerPay"&&isEmpty($scope.formcaluData.dayOfEveryPeriod)){
            		 $scope.formcaluData.startDate=null;
            		layer.msg('请先填自定义天数', {
               		    icon:3
               		});
            		return false;
            	}
            	
            	$scope.formcaluData.repaymentMethod=$('input:radio[name=repaymentMethod]:checked').val();
            	$scope.formcaluData.ccalculateFirstAndEnd= $('input:radio[name=ccalculateFirstAndEnd]:checked').val();
            	$scope.formcaluData.interestByOneTime= $('input:radio[name=interestByOneTime]:checked').val();
            	$scope.formcaluData.preposePayAccrual= $('input:radio[name=preposePayAccrual]:checked').val();
            	$scope.formcaluData.dateModel=$('input:radio[name=dateModel]:checked').val();
            	$scope.formcaluData.intentDateType=$('input:radio[name=intentDateType]:checked').val();
            	$scope.formcaluData.consultationMoneyWay=$('input:radio[name=consultationMoneyWay]:checked').val();
            	$scope.formcaluData.serviceMoneyWay=$('input:radio[name=serviceMoneyWay]:checked').val();
            	$scope.formcaluData.otherOneFundMoneyWay=$('input:radio[name=otherOneFundMoneyWay]:checked').val();
            	$scope.formcaluData.otherTwoFundMoneyWay=$('input:radio[name=otherTwoFundMoneyWay]:checked').val();
            	$scope.formcaluData.otherTreeFundMoneyWay=$('input:radio[name=otherTreeFundMoneyWay]:checked').val();
            	$http({
						method : 'POST',
						url : HRY.modules.finance+'fundParam/fincfundparamdic/caluIntentDate',
						data : $.param($scope.formcaluData), 
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					})
					.success(function(data) {
						     // $("#intentDate").val(data);
						      $scope.formcaluData.intentDate=data;
						      var table =  $("#dataTable");
				            	table.DataTable().draw();
							});
            	
            	
            }
        	
 
       //    	$scope.formData=$rootScope.settingFormData;
  
     
      
        	$scope.processForm = function() {
        		if(!isEmpty($scope.formData.repaymentMethod)&&$scope.formData.repaymentMethod.constructor===Array){
        			$scope.formData.repaymentMethod=$scope.formData.repaymentMethod.join(",");
        		}
        		if(!isEmpty($scope.formData.repaymentCycle)&&$scope.formData.repaymentCycle.constructor===Array){
        		    $scope.formData.repaymentCycle=$scope.formData.repaymentCycle.join(",");
        		}
        		if(!isEmpty($scope.formData.preposePayAccrual)&&$scope.formData.preposePayAccrual.constructor===Array){
        		    $scope.formData.preposePayAccrual=$scope.formData.preposePayAccrual.join(",");
        		}
        		if(!isEmpty($scope.formData.interestByOneTime)&&$scope.formData.interestByOneTime.constructor===Array){
        		    $scope.formData.interestByOneTime=$scope.formData.interestByOneTime.join(",");
        		}
        		if(!isEmpty($scope.formData.repaymentDate)&&$scope.formData.repaymentDate.constructor===Array){
        		    $scope.formData.repaymentDate=$scope.formData.repaymentDate.join(",");
        	    }
        		if(!isEmpty($scope.formData.dateModel)&&$scope.formData.dateModel.constructor===Array){
        		    $scope.formData.dateModel=$scope.formData.dateModel.join(",");
        		}
        		if(!isEmpty($scope.formData.ccalculateFirstAndEnd)&&$scope.formData.ccalculateFirstAndEnd.constructor===Array){
        		    $scope.formData.ccalculateFirstAndEnd=$scope.formData.ccalculateFirstAndEnd.join(",");
        		}
        		if(!isEmpty($scope.formData.intentDateType)&&$scope.formData.intentDateType.constructor===Array){
        		     $scope.formData.intentDateType=$scope.formData.intentDateType.join(",");
        		}
        		if(!isEmpty($scope.formData.consultationMoneyWay)&&$scope.formData.consultationMoneyWay.constructor===Array){
        		     $scope.formData.consultationMoneyWay=$scope.formData.consultationMoneyWay.join(",");
        		}
        		if(!isEmpty($scope.formData.serviceMoneyWay)&&$scope.formData.serviceMoneyWay.constructor===Array){
        		     $scope.formData.serviceMoneyWay=$scope.formData.serviceMoneyWay.join(",");
        		}
        		if(!isEmpty($scope.formData.otherOneFundMoneyWay)&&$scope.formData.otherOneFundMoneyWay.constructor===Array){
        		     $scope.formData.otherOneFundMoneyWay=$scope.formData.otherOneFundMoneyWay.join(",");
        		}
        		if(!isEmpty($scope.formData.otherTwoFundMoneyWay)&&$scope.formData.otherTwoFundMoneyWay.constructor===Array){
        		     $scope.formData.otherTwoFundMoneyWay=$scope.formData.otherTwoFundMoneyWay.join(",");
        		} 
        		if(!isEmpty($scope.formData.otherTreeFundMoneyWay)&&$scope.formData.otherTreeFundMoneyWay.constructor===Array){
        		     $scope.formData.otherTreeFundMoneyWay=$scope.formData.otherTreeFundMoneyWay.join(",");
        		}
                if(!isEmpty($scope.formData.paramsettingId)){
                	hryCore
					.CURD(
							{
								url : HRY.modules.finance+'fundParam/fincfundparamsetting/modify',
							})
					.save(
							$scope.formData,
							function(data) {
								growl.addInfoMessage(data.msg);
								$("#saveNext").attr('disabled',"true");
							},
							function(data) {
								growl.addInfoMessage("error:"
										+ data.msg);
							});
                }else{
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
    								$("#saveNext").attr('disabled',"true");
    							} else {
    								growl.addInfoMessage('添加失败')
    							}
    	
    						});
                	
                }
				
			
				
	
			};
        	
			
            
            
          
         
            
           
            
        
             
        
        } 
        
        if($stateParams.page=="calcu"){}
        if($stateParams.page=="modify"){
        	hryCore.CURD(
					{
						url : HRY.modules.finance
								+ 'fundParam/fincfundparamsetting/get'
					}).get({
						id:$stateParams.id
			}, function(data) {
	         			 $scope.formData=data;
	         			
	         			 hryCore.RenderHTML($scope.formData);//异步回显控件
     	
			}, function(data) {
				growl.addInfoMessage("error:" + data.msg);
			});
		    
         	var pix=HRY.modules.finance+'fundParam/fincfundparamdic/getjson?Q_pparamDicKey_eq_String=';
        	$http.get(pix+"fundMoneyType").
            success(function(data) {
            	 $scope.fundMoneyType = data;
            
            });
        	
           $scope.nextForm=function (){
        	$scope.formData.repaymentMethod=$('#repaymentMethod').val();
           	$scope.formData.repaymentCycle=$('#repaymentCycle').val();
           	$scope.formData.repaymentDate=$('#repaymentDate').val();
           	$scope.formData.ccalculateFirstAndEnd=$('#ccalculateFirstAndEnd').val();
           	$scope.formData.interestByOneTime=$('#interestByOneTime').val();
           	$scope.formData.preposePayAccrual=$('#preposePayAccrual').val();
           	$scope.formData.dateModel=$('#dateModel').val();
           	$scope.formData.intentDateType=$('#intentDateType').val();
           	$scope.formData.consultationMoneyWay=$('#consultationMoneyWay').val();
           	$scope.formData.serviceMoneyWay=$('#serviceMoneyWay').val();
           	$scope.formData.otherOneFundMoneyWay=$('#otherOneFundMoneyWay').val();
           	$scope.formData.otherTwoFundMoneyWay=$('#otherTwoFundMoneyWay').val();
           	$scope.formData.otherTreeFundMoneyWay=$('#otherTreeFundMoneyWay').val();
           	
        	  
        		$rootScope.settingFormData=$scope.formData;
        		window.location.href='#/finance/fundParam/fincfundparamsetting/next/anon';
        		
        	}
		 	$scope.modify=function (){
	        		if(!isEmpty($scope.formData.repaymentMethod)){
	        			$scope.formData.repaymentMethod=$("#repaymentMethod").val().join(",");
	        		}
	        		if(!isEmpty($scope.formData.repaymentCycle)){
	        		    $scope.formData.repaymentCycle=$("#repaymentCycle").val().join(",");
	        		}
	        		if(!isEmpty($scope.formData.preposePayAccrual)){
	        		    $scope.formData.preposePayAccrual=$("#preposePayAccrual").val().join(",");
	        		}
	        		if(!isEmpty($scope.formData.interestByOneTime)){
	        		    $scope.formData.interestByOneTime=$("#interestByOneTime").val().join(",");
	        		}
	        		if(!isEmpty($scope.formData.repaymentDate)){
	        		    $scope.formData.repaymentDate=$("#repaymentDate").val().join(",");
	        	    }
	        		if(!isEmpty($scope.formData.dateModel)){
	        		    $scope.formData.dateModel=$("#dateModel").val().join(",");
	        		}
	        		if(!isEmpty($scope.formData.ccalculateFirstAndEnd)){
	        		    $scope.formData.ccalculateFirstAndEnd=$("#ccalculateFirstAndEnd").val().join(",");
	        		}
	        		if(!isEmpty($scope.formData.intentDateType)){
	        		     $scope.formData.intentDateType=$("#intentDateType").val().join(",");
	        		}
	        		if(!isEmpty($scope.formData.consultationMoneyWay)){
	        		     $scope.formData.consultationMoneyWay=$("#consultationMoneyWay").val().join(",");
	        		}
	        		if(!isEmpty($scope.formData.serviceMoneyWay)){
	        		     $scope.formData.serviceMoneyWay=$("#serviceMoneyWay").val().join(",");
	        		}
	        		if(!isEmpty($scope.formData.otherOneFundMoneyWay)){
	        		     $scope.formData.otherOneFundMoneyWay=$("#otherOneFundMoneyWay").val().join(",");
	        		}
	        		if(!isEmpty($scope.formData.otherTwoFundMoneyWay)){
	        		     $scope.formData.otherTwoFundMoneyWay=$("#otherTwoFundMoneyWay").val().join(",");
	        		} 
	        		if(!isEmpty($scope.formData.otherTreeFundMoneyWay)){
	        		     $scope.formData.otherTreeFundMoneyWay=$("#otherTreeFundMoneyWay").val().join(",");
	        		}
	        		

					hryCore
							.CURD(
									{
										url : HRY.modules.finance
												+ 'fundParam/fincfundparamsetting/modify'
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
				
				/*	$http({
						method : 'POST',
						url : HRY.modules.finance+'fundParam/fincfundparamsetting/modify',
						data : $.param($scope.formData), 
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					})
					.success(function(data) {
								if (data.success) {
									growl.addInfoMessage('修改成功')
								} else {
									growl.addInfoMessage('修改失败')
								}
		
							});
		
				}*/
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
            $scope.fnModify=fnModify;
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
    								+ 'fundParam/fincfundparamsetting/get'
    					}).get({
    						id:manyLevel.paramsettingId
    			}, function(data) {
    	         			 $scope.formData=data;
    	         			 hryCore.RenderHTML($scope.formData);//异步回显控件
         	
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
            
           function fnModify(url){
        	   window.location.href='#/finance/'+url+'/'+$rootScope.selectFincFundParamsettingId;
           }
            /**
             * 加载数据
             */
            function fnList(){
            	 $http.get( HRY.modules.finance+'fundParam/fincfundparamsetting/list').
                 success(function(data) {
                 	 $scope.list = data.rows;
                 });
            	var pix=HRY.modules.finance+'fundParam/fincfundparamdic/getjson?Q_pparamDicKey_eq_String=';
            	$http.get(pix+"fundMoneyType").
                success(function(data) {
                	 $scope.fundMoneyType = data;
                
                });
            	
         		hryCore.CURD(
    					{
    						url : HRY.modules.finance
    								+ 'fundParam/fincfundparamsetting/listByone'
    					}).get({
    			}, function(data) {
         			    $scope.formData=data;
    				    $rootScope.selectFincFundParamsettingId=data.paramsettingId;
         			    hryCore.RenderHTML($scope.formData);//异步回显控件
    			
         		  },
					function(data) {
						growl.addInfoMessage("error:"+ data.msg);
					});
            }
     
            
             $scope.nextForm=function (){
        		
        		$rootScope.settingFormData=$scope.formData;
        	
        		window.location.href='#/finance/fundParam/fincfundparamsetting/next/'+$rootScope.selectFincFundParamsettingId;
        		
        	}
    
  
 
        
        }
        hryCore.initPlugins();
    }
 
    return {controller:controller};
});