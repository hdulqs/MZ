/**
 * 下拉框插件
 * 调用方式：<hry-select 
 *           multiple  //可选  没有这个属性就是单选下拉框  有这个属性就是复选下拉框
 *           disabled //可选  没有这个属性可编辑      有这个属性为只读
 *           url="oauth/company/subcompany/findCompanyList"   //url  必填   url除去http://localhost/   
 *           name="name"       //必填    下拉框显示的字段
 *           value="id"        //必填    下拉框的值字段
 *           selectId="departmentList"   //必填   当前下拉框的值字段   用$("#departmentList").val()获取    注复选值为数组
 *           placeholder="请选择数据" //可选
 *           parentId="companyList"       //可选  二级连动时  上级的selectID  同一个上级下可以挂多个同级下级，且可以无限级连动
 *        >    
 *       </hry-select>
 * 
 * add by liushilei  2015/12/18 17:37
 */
define(['app'], function (app) {
    app.directive('hrySelect', ["$rootScope",function ($rootScope) {
    return {  
        restrict: 'E',  
   //   require: '^ngModel',               
        scope: true, 
        template:'',
        link : function(scope, element, attributes, controllerInstance){
        	var select = element;		  
        	var url = attributes.url;     //请求url地址
        	
        	if(select.attr("dic")!=undefined){//数据字典
        		url = HRY.modules.web+"dictionary/appdiconelevel/findbykey?Q_pDickey_eq_String="+select.attr("pdickey");
        	}
        	
        	var name = attributes.name;   //显示字段
        	var value = attributes.value; //值字段
        	var selectId = attributes.selectid; //selectId 用于post取值与回显取值
        	var	select2	= attributes.select2; //复选框  有这个属性表示是复选框
        	var	parentId = attributes.parentid; //二级连动Id
        	var dataTableSerch=attributes.datatableserch;
        	var dataTableSerchtrue="";
        	if(dataTableSerch != undefined){
        		dataTableSerchtrue="dataTableSerch";
        	}
        	var placeholder=attributes.placeholder;//提示语句  如 ：请选择。。。默认为请选择
        	
        	var labelName=attributes.labelname;
        	function createSelect(select){
       		 var html;
       		 var selectId = select.attr("selectId");
       		 if(select.attr("multiple")!=undefined){
       			  if(select.attr("disabled")!=undefined){
       				  html = "<select  class='select2' multiple='multiple' id='"+selectId+"' "+dataTableSerchtrue+" ><option value=''>不限</option>";
       			  }else{
       				  html = "<select class='select2' multiple='multiple' id='"+selectId+"' "+dataTableSerchtrue+"><option value=''>不限</option>";
       			  }
			   	 }else{
			   		  
			   		  if(select.attr("disabled")!=undefined){
			   			  if(labelName!=undefined){
			   				 html = "<select disabled  class='select2' id='"+selectId+"'   "+dataTableSerchtrue+"><option value=''>不限</option> "; 
			   			  }else{
			   				 html = "<select disabled class='select2' id='"+selectId+"'   "+dataTableSerchtrue+"><option value=''>不限</option> "; 
			   			  }
			   			 
			   		  }else{
			   			if(labelName!=undefined){
			   			   html = "<select class='select2'  id='"+selectId+"'  "+dataTableSerchtrue+"> <option value=''>不限</option> "; 
			   			}else{
			   			   html = "<select class='select2' id='"+selectId+"'  "+dataTableSerchtrue+"> <option value=''>不限</option>"; 
			   			}
			   		}
			   	 
			   	 }
       		//html=html+"<option value=''>不限</option>";
       		 return html;
       	   }
        	
        	/**
        	 * 渲染select 
        	 */
        	function renderSelect(html,select){
        		var selectId=$(select).attr("selectid");
        		select.append(html);
        		$("#"+selectId).wrap('<div style="width:100%;position:relative;"></div>').select2({
  		        	placeholder: placeholder===undefined?"请选择数据":placeholder,
  		        	allowClear: true
  		        });
        		
        		//添加监听事件
        		select.on("change", function (e) {
        			var selectId=$(this).attr("selectid");
        			var value=$("#"+selectId).val();
        			loadEvent(selectId,value);
        		 });
        	}
        	
        	
        	//如果为只读  这里不加载
        	if(attributes.disabled!=undefined){
        		return false;
        	}
        	//如果为修改 这里不加载
        	if(attributes.modify!=undefined){
        		return false;
        	}
        	if(parentId!=undefined){//有父级  有父级直接渲染空
        		var html = createSelect(select);html +="</select>";
        		renderSelect(html,select);
        	}else{
        	
        		//没有父级
	        	$.ajax({
	        		   type: "POST",
	        		   url: url,
	        		   selectId : selectId,//异步回调时鉴别下拉框
	        		   success: function(data){
	        		   		
	        			   var selectId = this.selectId;
	        			   var str = "hry-select[selectId="+this.selectId+"]"
	        			   var select = $(str);
	        			   
	        			   data = $.parseJSON(data);
	        			   if(data!=null&&data.length>0){
	        				   var html =createSelect(select);
	            			   for(var i = 0 ; i < data.length; i++){
            					   html += "<option value='"+eval("data[i]."+value)+"' >"+eval("data[i]."+name)+"</option>"; 
	            			   }
	            			   html += "</select>";
	        			   }else{
	        				   var html = createSelect(select);html +="<option value=''>无数据</option></select>";
	        			   }
	        			    
	        			   //渲染下拉框
	        			   renderSelect(html,select);
	        			   
	        		   }, 
	        		   error: function(e) { 
	        			   var selectId = this.selectId;
	        			   var str = "hry-select[selectId="+this.selectId+"]"
	        			   var select = $(str);
	        			   var html = createSelect(select);html +="<option value=''>加载异常，请检查后台方法</option></select>";
	        			 //渲染下拉框
	        			   renderSelect(html,select);
	    			   } 
	    		});
	        	
        	}

			//------------------------------联动事件----------------------------------------
			function loadEvent(parentId,val){
	        	$("hry-select[parentId="+parentId+"]").each(function (i) {
	        		       $(this).children().remove()
	        		        var selectId = $(this).attr("selectId");
		                	var url =  $(this).attr("url");     //请求url地址
		                	var name =  $(this).attr("name");   //显示字段
		                	var value =  $(this).attr("value"); //值字段
		                	var	parentId =  $(this).attr("parentid"); //二级连动Id
		                	var data={};
	                	    data[parentId] = val;//参数默认为parentId
		                	
		                	$.ajax({
		 	        		   type: "POST",
		 	        		   url: url,
		 	        		   data : data,
		 	        		   selectId : selectId,//异步回调时鉴别下拉框
		 	        		   success: function(data){
			 	        			   var selectId = this.selectId;
				        			   var str = "hry-select[selectId="+this.selectId+"]"
				        			   var select = $(str);
			 	        			   data = $.parseJSON(data);
			 	        			   
			 	        			   if(data!=null&&data.length>0){
			 	        				   
			 	        				   var html = createSelect(select);
			 	        				   
			 	            			   for(var i = 0 ; i < data.length; i++){
			 	            				   html += "<option value='"+eval("data[i]."+value)+"' >"+eval("data[i]."+name)+"</option>"; 
			 	            			   }
			 	            			   html += "</select>";
			 	            			  
			 	        			   }else{
			 	        				   var html = createSelect(select);html +="<option value=''>无数据</option></select>";select.append(html);
			 	        			   }
			 	        			   
			 	        			//渲染下拉框
			 		        			  renderSelect(html,select);
			 	        			   
			 	        		   }, 
			 	        		   error: function(e) { 
			 	        			   var selectId = this.selectId;
				        			   var str = "hry-select[selectId="+this.selectId+"]"
				        			   var select = $(str);
			 	        			   var html = createSelect(select);html +="<option value=''>加载异常，请检查后台方法</option></select>";
			 	        			//渲染下拉框
				        			   renderSelect(html,select);
			 	        		   } 
			 	    		});
	                

	                	
	               
	                
	             });
	        	//------------------------------监听事件----------------------------------------
			}
        	
        	
        }
    } 
    
//----------------------------------------------------------------------------------------------------------------------------   
//--------------------------------------------下面的注释请勿删除---add by liushilei-----------------------------------------------------------------------------   
//----------------------------------------------------------------------------------------------------------------------------   
/*    return {
    		contextPath : contextPath,
            restrict: 'E',
            replace : true,
            scope: true,
            transclude:true,
            template : "",
            compile: function(element, attributes) {  
            	var path = this.contextPath;
            	var select = element;
            	var url = attributes.url;
            	var name = attributes.name;   //显示字段
            	var value = attributes.value; //值字段
            	var sid = attributes.sid; 
            	$.ajax({
            		   type: "POST",
            		   async :false,
            		   url: path+"/"+url,
            		   success: function(data){
            			   data = $.parseJSON(data);
            			   if(data!=null&&data.length>0){
            				   var html = "<select id='"+sid+"' >";
	            			   for(var i = 0 ; i < data.length; i++){
	            				    
	            				   if(data[i].id==18){
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
        		
                return {  
                    pre: function preLink(scope, element, attributes) {  
                    },  
                    post: function postLink(scope, element, attributes) {  
                    }  
                };  
            }
            
    }*/
  }])

})
