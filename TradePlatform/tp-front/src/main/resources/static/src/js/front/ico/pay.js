define(function(require, exports, module) {
	this.validate = require("js/base/validate");
	
	module.exports = {
		one : function(){debugger
			if($("#getMoney").val()!='1'){
				var sumMoney = $("#sumMoney").val();
				$("#bili").empty().text($("#getMoney").val()/sumMoney);
			}
			
			$("#getMoney").on("blur",function(){
				var sumMoney = $("#sumMoney").val();
				if($(this).val()=='' || $(this).val()==null){
					layer.msg("金额不能为空",{icon:2});
					return false;
				}
				if(!validate.isNumber($(this).val())){
	        		layer.msg("金额必须为数字",{icon:2});
	        		return false;
	        	}
				$("#bili").empty().text($(this).val()/sumMoney);
			})
			
			//向第二步走
			$("#one_next_btn").on("click",function(){
				window.location.href= _ctx +"/ico/payJump2.do?projectId="+$("#projectId").val()+"&coinType="+$("#coinType").val()+
						"&proportions="+$("#bili").text()+"&money="+$("#getMoney").val();
			})
		},
		two : function(){
			//跳回第一步
			$("#two_prev_btn").on("click",function(){
				window.location.href= _ctx + "/ico/payJump.do?projectId="+$("#projectId").val()+"&getMoney="+$("#getMoney").val();
			})
			
			//跳到第三步
			$("#two_next_btn").on("click",function(){
				window.location.href= _ctx +"/ico/payJump3.do?projectId="+$("#projectId").val()+"&coinType="+$("#coinType").val()+
						"&proportions="+$("#bili").text()+"&money="+$("#getMoney").val();
			})
		},
		three : function(){
			//立即支付
			$("#immediatePayment").on("click",function(){
				var should_pay = $("#should_pay").val();
				if(should_pay=='' || should_pay==null){
					layer.msg("金额不能为空",{icon:2});
					return false;
				}
				if(!validate.isNumber(should_pay)){
	        		layer.msg("金额必须为数字",{icon:2});
	        		return false;
	        	}
				var projectId = $("#projectId").val();
				var coinType = $("#coinType").val();
				var proportions = $("#proportions").val();
				$.ajax({
					type : "post",
					url : _ctx + "/ico/immediatePayment",
					data : {
						money : should_pay,
						projectId : projectId,
						proportions : proportions
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data.success){
							layer.msg("支持成功！",{icon:1,time:1500});
							$("#hotMoney").empty().text(data.obj.hotMoney);
						}else{
							layer.msg(data.msg,{icon:2});
						}
					}
				})
			})
			$("#three_next_btn").on("click",function(){
				window.location.href= _ctx + "/ico/listProject?isDisabled=all";
			})
		}
	}
})