define(function(require, exports, module) {
	
	require("lib/bootstrap/css/bootskrap.min.css");
	require("lib/layer/layer.js");
	require("lib/layer/css/layer.css");
	
	module.exports = {
		
		//初始化方法
		init : function(){
			 var translate={
		    		  one:0,
		    		  two:1,
		    		  three:2,
		    		  four:3,
		    		  five:4
		     };
			//切换
			window.t=function(e,a,id,step){
			      //如果项目ID不存在，则只能点击0和1
			      if(id==""&&translate[a]<=1){
			    	  if (!e.hasClass("active") ) {
				          e.addClass("active").removeClass("ready").siblings("li").removeClass("active").removeClass("ready"), e.prevAll("li").addClass("ready");
				          $(".launch-part").hide(), $(".launch-part-" + a).show()
				      }
			      }
			      //如果项目ID存在，则获取步骤，可以操作（步骤+1）页面
			      else if(id!=""&&step!=""&&translate[a]<=(parseInt(step)+1)){
			    	  if (!e.hasClass("active") ) {
				          e.addClass("active").removeClass("ready").siblings("li").removeClass("active").removeClass("ready"), e.prevAll("li").addClass("ready");
				          $(".launch-part").hide(), $(".launch-part-" + a).show()
				      }
			      }
			 }
			 
			 //保存
			 window.s=function(_this,N){
					var step=_this.data("step");
					var a=_this.data('to');
					//表单数据处理
					var arr=$('form[name=form-'+step+']').serializeArray();
					var step=translate[step];
					var params={
							id:$("#id").val(),
							step:step
					};
					//提交审核项目
					if(N&a=="six"){
						layer.msg('项目审核提交成功', {icon: 1,time: 1000});
						params["status"]="1"//修改项目状态为待审核
							
					}
					$.each(arr, function(index, value, array) {
						 params[value.name]=value.value;
					});
					$.ajax({
						type:'post',
						dataType:'json',
						data:params,
						url:'/iLaunchProject/saveProjectStep.do',
						success:function(data){
							if(data.success){
								$("#step").val(data.obj.step);
								$("#id").val(data.obj.id);
								if(N){
									var e=$("li[class*='nav-list-"+a+"']");
									var id=$("#id").val();
									var s=translate[a];
								    if(a=="six"){//完成项目
								    	window.location.href="/";
								    }else{
								    	 t(e,a,id,s);
								    }
								}else{
									layer.alert("保存成功");
								}
							}
						}
					})
			 }
			 
			 $(".nav-item-step").on('click',function() {
			      var e = $(this).parents("li");
			      var a = $(this).data("step");
			      var id=$("#id").val();
			      var step=$("#step").val();
			      t(e,a,id,step);
			  });
			 
			//上一步按钮
			$('button[name="front"]').on('click',function(){
				var a=$(this).data('step');
				var e=$("li[class*='nav-list-"+a+"']");
				var id=$("#id").val();
			    var step=$("#step").val();
			    t(e,a,id,step);
			    $('html,body').stop().animate({scrollTop:876},350);
			})
			//保存并下一步按钮
			$('button[name="save_next"]').on('click',function(){
				s($(this),true);
				
				$('html,body').stop().animate({scrollTop:876},350);//窗口滚动条回到选项卡内容容器的顶部位置
			})
			//保存按钮
			$('button[name="save"]').on('click',function(){
				s($(this),false);
			})
		}
	}
	
});