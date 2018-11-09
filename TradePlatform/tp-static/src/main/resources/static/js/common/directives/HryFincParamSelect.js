/**
 * 返回功能
 * 调用方式：<a href back-button>back</a>;
 */
define(['app'], function (app) {
    app.directive('hryFincparamSelect', ["$rootScope",function ($rootScope) {
        var contextPath = '';
        return {  
            restrict: 'E',  
       //   require: '^ngModel',  
            scope: true, 
            template:'',
            link : function(scope, element, attributes, controllerInstance){
              	var path = contextPath;
            	var select = element;
            	var pparamDicKey = attributes.pparamdickey;
            	var url = HRY.modules.finance+'fundParam/fincfundparamdic/getjson?Q_pparamDicKey_eq_String='+pparamDicKey;
            	var name = "text";   //显示字段
            	var value = "paramDicKey"; //值字段
            	var selectid = attributes.selectid; 
            	var	select2	= ""; //复选框
            	var nmodel=attributes.nmodel;
            	$.ajax({
            		   type: "POST",
            		   async :false,
            		   url: path+"/"+url,
            		   success: function(data){
            			   data = $.parseJSON(data);
            			   if(data!=null&&data.length>0){
            				   //回显key对应的值
            				//   scope.formData1=$.parseJSON(scope.formData)
            				   var echo =  eval("scope.formData."+selectid);
            				 //  var echo="singleInterest";
            				   if(select2==""){
            				   	   var html = "<select class='select2' multiple='' id='"+selectid+"' ng-model='"+nmodel+"' >";
            			   	   }else{
            			   		   var html = "<select  id='"+selectid+"' ng-model='"+nmodel+"' >";
            			   	   }
                			   for(var i = 0 ; i < data.length; i++){
                				   if(echo!=null&&echo!=""&&(echo+",").indexOf(eval("data[i]."+value)+",")>=0){
                					   html += "<option selected value='"+eval("data[i]."+value)+"' >"+eval("data[i]."+name)+"</option>"; 
                				   }else{
                					   html += "<option value='"+eval("data[i]."+value)+"' >"+eval("data[i]."+name)+"</option>"; 
                				   }
                			   }
                			   html += "</select>";
                			   select.after(html);
            			   }else{
            				   select.after("<select><select>");
            			   }
            		   }, 
            		   error: function(e) { 
            			   select.after("<select><select>");
        			   } 
        		})
        		//渲染下拉框
        		conApp.initPlugins();
    			conApp.initMaterialPlugins();
            }
        } 
        
  
      }])

})
