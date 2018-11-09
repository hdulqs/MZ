define(function(require, exports, module) {
	
	require("lib/bootstrap/css/bootstrap.min.css");
	require("lib/layer/layer.js");
	require("lib/layer/css/layer.css");
	
	module.exports = {
		
		//初始化方法
		init : function(){
			//切换步骤
			var stepSwitch=function(_this,listStep){
				var index=listStep.index(_this);
				//这里添加状态判断，如果目前项目状态大于等于点击的状态那么跳转，否则不跳转
				if(project_step>=index){
					//清除所有激活步骤
					$("li[class*='step']").removeClass('active');
					//激活点击步骤
					$(_this).addClass("active");
					//切换form表单
					var divId="projdct_step";
					$("#"+divId).empty();
					//加载对应的页面
					$.ajax({
						type : "post",
						url : _ctx+"/iLaunchProject/projectStep.do",
						async:false,
						data:{
							projectId:project_id,
							step:index
						},
						dataType : "html",
						success : function(data) {
							//进行渲染
							$("#"+divId).html(data);
						},
						error : function(e) {
							$("#"+divId).html("<div class='row'><h1>此路径不存在：404</h1></div>");
						}
					});
				}else{
					layer.open({
						title:"提示",
						content:"请先保存",
						icon:"2",
						time:"2000"
					});
				}
			}
			//获得所有步骤
			var listStep=$("li[class*='step']");
			//切换步骤点击事件
			listStep.click(function(){
				stepSwitch(this,listStep);
			})
			//根据项目step显示form
			stepSwitch(listStep[project_step],listStep);
			//封装保存下一步方法
			var saveNext=function(next){
				var i = $("li[class*='step']").index($("li[class*='step active']")) + 1;
				var t = $("#projdct_step form").serializeArray();
				var d = {
					submit:next,//是否提交
					step : i,
					projectId:project_id
				};
				$.each(t, function() {
					d[this.name] = this.value;
				});
				$.ajax({
					type:'post',
					url:_ctx+'/iLaunchProject/saveProjectStep',
					data:d,
					async:false,
					dataType:'json',
					success:function(data){
						try {
							project_id=data.obj.id;
							project_step=data.obj.step;
							window.project_status=data.obj.status;
						} catch (e) {
							// TODO: handle exception
						}
					}
				});
				//判断是否继续下一步
				if(project_status==0){
					next&&stepSwitch(listStep[project_step],listStep);
				}else if(project_status==1){
					loadUrl(_ctx+"/user/center.do");
				}
			}
			//保存
			$("#project_save").click(function(){
				saveNext(false);
			})
			//保存并下一步
			$("#project_save_next").click(function(){
				saveNext(true);
			})
		}
	}
	
});