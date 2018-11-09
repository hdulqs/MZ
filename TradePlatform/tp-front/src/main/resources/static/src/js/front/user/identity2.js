define(function(require, exports, module) {
	require("lib/google/css/intlTelInput.css");
	require("js/base/googleauth/intlTelInput.js");
	this._table = require("js/base/table");
	this.validate = require("js/base/validate");
	require("lib/google/css/global.css")
	require("css/user/identify2.css")
	require("css/upload/bootstrap-fileinput.css")
	require("js/front/user/upload/bootstrap-fileinput.js")
	require("js/base/base.js")
	require("jqueryForm");

	module.exports = {
		
		//初始化方法
		init : function(){
		$("#mobile-number").intlTelInput();

			$("#authenticationForm2").hide();
		    
		   var radios =$('input[name="type"]')
		   for (var i = 0; i < radios.length; i++) {
		         radios[i].indexs = i + 1;
		       radios[i].onchange = function () {
		           if (this.checked) {
		             $('#authenticationForm1').hide();
		             $('#authenticationForm2').hide();
		             $('#authenticationForm'+ this.indexs).show();
		            
		           
		           }
		       }
		       //清空所有数据
		       radios[i].onclick=function () {
		    	   $('#authenticationForm1')[0].reset();
		    	   $('#authenticationForm2')[0].reset();
		    	   for(var i=1;i<7;i++){
//		    		   $("#picImg"+i).attr("src","new_src");
		    		   $("#picImg"+i).attr("src",""); 
		    	   }
		       }
		   }
		  //  select
		  $('.select-country-header').on('click',function(){
		    $('.select-input-list').slideToggle();
		  })
		  $(".select-input-list li").on('click', function() {
		        var e = $(this).data("val");
		        $("#countryName").val($(this).text()), $('input[name="countryOrigin"]').val(e), $(".select-input-list").hide()
		    })
	
		  $("#btn-submit2").on("click",function(){
			 
			  var sign=2;

			  var trueName = $("#trueName"+sign).val();
				var sex=$("input[name='sex']:checked").val();
				var surname = $("#surname"+sign).val();
				var country = $("#mobile-number").val();
				var cardType = $("#cardType").val();
				var cardId = $("#cardId"+sign).val();
				var picIDa = $("#picIDa"+sign).val();
				var picIDb = $("#picIDb"+sign).val();
				var picIDc = $("#picIDc"+sign).val();
				if(!trueName){
					layer.msg(xingminisnull, {icon: 2});
					return false;
				}
			
				if(!surname){
					layer.msg(xingshiisnull, {icon: 2});
					return false;
				}/*if(!country){
					layer.msg(diquisnull, {icon: 2});
					return false;
				}*/
				if(!sex){
					layer.msg(xingbiesinull, {icon: 2});
					return false;
				}
				if(!cardId){
					layer.msg(zhengjianhaoisnull, {icon: 2});
					return false;
				}
					
				if(!picIDa || picIDa.length==0){
					layer.msg(qingchuanzhenmian, {icon: 2});
					return false;
				}
				if(!picIDb || picIDb.length==0){
					layer.msg(qingchuangchuanbeimian, {icon: 2});
					return false;
				}
				if(!picIDc || picIDc.length==0){
					layer.msg(qingshouchi, {icon: 2});
					return false;
				}
				 $("#btn-submit2").attr("disabled","disabled");
				$("#authenticationForm2").ajaxSubmit({
					 type: "post",
			         url: _ctx + "/user/identity",
			         dataType: "JSON",
			         data : {},
			         resetForm : true,
			         success: function(data) {
			         if(data.success){
			        	layer.msg(data.msg, {icon: 1,time:1000},function(){
								//跳转到个人中心
								window.location.href = _ctx+"/user/center";
							})
			         }else{
			        	 layer.alert(data.msg);
			        	  //清空所有数据
			        	 for(var i=1;i<7;i++){
				    		   $("#picImg"+i).attr("src","new_src");   
				    	   }
			        		   $("#btn-submit2").removeAttr("disabled");// 按钮可用
			         }
			         }
				})
				
				
		  })
		  
		
		    $("#picIDa1").on("change",function(event){
		    //var e = event  || window.event;
			 // var ae=event;
			 // var e=event.srcElement ? event.srcElement : event.target;
			  var f=document.getElementById("picIDa1").value;
			   for (var i = 0; i < event.target.files.length; i++) {  
		            var file = event.target.files.item(i);  
		         /*   if (!(/^image\/.*$/i.test(file.type))) {  
		                continue; //不是图片 就跳出这一次循环  
		            } */
		            if(!/\.(jpg|png|jpeg|bmp|gif|JPG|PNG|JPEG|BMP|GIF)$/.test(f)){
		            	 layer.alert(tupiangeshi);
		            	 return false;
		            }
		        	if(((event.target.files[i].size).toFixed(2))>=(3*1024*1024)){
		                  
		        		layer.msg(tupianxianzhi,{icon: 2});
				                 return false;
				          }
		            //实例化FileReader API  
		            var freader = new FileReader();  
		            freader.readAsDataURL(file);  
		            freader.onload = function(event) {  
		                $("#picImg"+1).attr("src",event.target.result);  
		            }  
		        }
		  })
		  
		  
		    $("#picIDb1").on("change",function(event){
			  var f=document.getElementById("picIDb1").value;
			   for (var i = 0; i < event.target.files.length; i++) {  
		            var file = event.target.files.item(i);  
		            if(!/\.(jpg|png|jpeg|bmp|gif|JPG|PNG|JPEG|BMP|GIF)$/.test(f)){
		            	 layer.alert(tupiangeshi);
		            	 return false;
		            }
		        	if(((event.target.files[i].size).toFixed(2))>=(3*1024*1024)){
		                  
		        		layer.msg(tupianxianzhi,{icon: 2});
				                 return false;
				          }
		            //实例化FileReader API  
		            var freader = new FileReader();  
		            freader.readAsDataURL(file);  
		            freader.onload = function(event) {  
		                $("#picImg"+2).attr("src",event.target.result);  
		            }  
		        }
		  })
		  
		  $("#picIDc1").on("change",function(event){
		 // var e=event;
		  var f=document.getElementById("picIDc1").value;

		   for (var i = 0; i < event.target.files.length; i++) {  
	            var file = event.target.files.item(i);  
	            if(!/\.(jpg|png|jpeg|bmp|gif|JPG|PNG|JPEG|BMP|GIF)$/.test(f)){
	            	 layer.alert(tupiangeshi);
	            	 return false;
	            }
	        	if(((event.target.files[i].size).toFixed(2))>=(3*1024*1024)){
	                  
	        		layer.msg(tupianxianzhi,{icon: 2});
			                 return false;
			          }
	            //实例化FileReader API  
	            var freader = new FileReader();  
	            freader.readAsDataURL(file);  
	            freader.onload = function(event) {  
	                $("#picImg"+3).attr("src",event.target.result);  
	            }  
	        }
	  })
	  
	  
	  
	  
	  
	    $("#picIDa2").on("change",function(event){
			 // var e=event;
			  var f=document.getElementById("picIDa2").value;

			   for (var i = 0; i < event.target.files.length; i++) {  
		            var file = event.target.files.item(i);  
		            if(!/\.(jpg|png|jpeg|bmp|gif|JPG|PNG|JPEG|BMP|GIF)$/.test(f)){
		            	 layer.alert(tupiangeshi);
		            	 return false;
		            }
		        	if(((event.target.files[i].size).toFixed(2))>=(3*1024*1024)){
		                  
		        		layer.msg(tupianxianzhi,{icon: 2});
				                 return false;
				          }
		            //实例化FileReader API  
		            var freader = new FileReader();  
		            freader.readAsDataURL(file);  
		            freader.onload = function(event) {  
		                $("#picImg"+4).attr("src",event.target.result);  
		            }  
		        }
		  })
		  
		  
		  
		  
		   $("#picIDb2").on("change",function(event){
			 // var event=event;
			  var f=document.getElementById("picIDb2").value;

			   for (var i = 0; i < event.target.files.length; i++) {  
		            var file = event.target.files.item(i);  
		            if(!/\.(jpg|png|jpeg|bmp|gif|JPG|PNG|JPEG|BMP|GIF)$/.test(f)){
		            	 layer.alert(tupiangeshi);
		            	 return false;
		            }
		        	if(((event.target.files[i].size).toFixed(2))>=(3*1024*1024)){
		                  
		        		layer.msg(tupianxianzhi, {icon: 2});
				                 return false;
				          }
		            //实例化FileReader API  
		            var freader = new FileReader();  
		            freader.readAsDataURL(file);  
		            freader.onload = function(event) {  
		                $("#picImg"+5).attr("src",event.target.result);  
		            }  
		        }
		  })
		  
		  
		  
		   $("#picIDc2").on("change",function(event){
			  //var e=event;
			  var f=document.getElementById("picIDc2").value;

			   for (var i = 0; i < event.target.files.length; i++) {  
		            var file = event.target.files.item(i);  
		            if(!/\.(jpg|png|jpeg|bmp|gif|JPG|PNG|JPEG|BMP|GIF)$/.test(f)){
		            	 layer.alert(tupiangeshi);
		            	 return false;
		            }
		        	if(((event.target.files[i].size).toFixed(2))>=(3*1024*1024)){
		                  
		        		layer.msg(tupianxianzhi, {icon: 2});
				                 return false;
				          }
		            //实例化FileReader API  
		            var freader = new FileReader();  
		            freader.readAsDataURL(file);  
		            freader.onload = function(event) {  
		                $("#picImg"+6).attr("src",event.target.result);  
		            }  
		        }
		  })
		  
	  
		 $("#btn-submit").on("click",function(){
	
		  var sign=1;
		  var trueName = $("#trueName"+sign).val();
			var sex = $("#sex").val();
			var surname = $("#surname"+sign).val();
			var cardType = $("#cardType").val();
			var cardId = $("#cardId"+sign).val();
			var picIDa = $("#picIDa"+sign).val();
			var picIDb = $("#picIDb"+sign).val();
			var picIDc = $("#picIDc"+sign).val();
			
			if(!surname){
				layer.msg(xingshiisnull, {icon: 2});
				return false;
			}else{
				var reg = /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi;
				if(!reg.test(surname)){
					layer.msg(xingshibzhongwen, {icon: 2});
					return false;
				}
			}
			if(!trueName){
				layer.msg(xingmingisnull, {icon: 2});
				return false;
			}else{
				var reg = /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi;
				if(!reg.test(trueName)){
					layer.msg(xingmingzhongwen, {icon: 2});
					return false;
				}
			}
		
			if(sex==2 && !sex){
				layer.msg(xingbieisnull, {icon: 2});
				return false;
			}
			
			if(!cardId){
				layer.msg(zhengjianhaoisnull, {icon: 2});
				return false;
			}
			if(!validate.check_card(cardId)){
				layer.msg(zhengjianahaogeshi, {icon: 2});
				return false;
			}
				
			if(!picIDa || picIDa.length==0){
				layer.msg(qingchuanzhenmian, {icon: 2});
				return false;
			}
			if(!picIDb || picIDb.length==0){
				layer.msg(qingchuangchuanbeimian, {icon: 2});
				return false;
			}
			if(!picIDc || picIDc.length==0){
				layer.msg(qingshouchi, {icon: 2});
				return false;
			}
			
			$("#btn-submit").attr("disabled","disabled");
			$("#authenticationForm1").ajaxSubmit({
				 type: "post",
		         url: _ctx + "/user/identity",
		         dataType: "JSON",
		         data : {},
		         resetForm : true,
		         success: function(data) {
		         if(data.success){
		        	 
		        	layer.msg(data.msg, {icon: 1,time:1000},function(){
							//跳转到个人中心
		        		//window.location.href = _ctx+"/user/realinfo?tokenId="+data.obj;
		        		loadUrl(_ctx+"/user/realinfo")
		        		
						})
		         }else{
		        	 layer.alert(data.msg);
		        	  //清空所有数据
		        	 for(var i=1;i<7;i++){
			    		   $("#picImg"+i).attr("src","new_src");   
			    	   }
		        	 	   $("#btn-submit").removeAttr("disabled");// 按钮可用
		         }
		         }
			})
			//return false; //阻止表单默认提交
			
			
				
				
				
			
		  })
		},
		
	img:function(e){
	//图片回显
    for (var i = 0; i < e.target.files.length; i++) {  
        var file = e.target.files.item(i);  
        if (!(/^image\/.*$/i.test(file.type))) {  
            continue; //不是图片 就跳出这一次循环  
        }  
        //实例化FileReader API  
        var freader = new FileReader();  
        freader.readAsDataURL(file);  
        freader.onload = function(e) {  
            $("#picImg"+1).attr("src",e.target.result);  
        }  
    }

}
	
	}
});