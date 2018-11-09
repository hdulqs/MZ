/**
 * 商户认证的js
 */
define(function(require,exports,module){
	
	require("lib/jdt/css/style.css");
	require("lib/jdt/js/lib.js");

	
	
	module.exports = {
		enterauth3 : function(){
			bsStep(3);
			
			$("#useBtn").on("click",function(){

				$.ajax({
					type : "post",
					url : _domain + "/admin/enterauth/use.do",
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data!=undefined){
							if(data.success){
								window.location.reload()
							}else{
							}
						}else{
						}
					},
					error : function(e) {
						
					}
				});
			
			});
			
		},
		enterauth2 : function(){
			bsStep(2);
		},
		enterauth1 : function(){
			bsStep(1);
			
			// 表单验证
			$('#form').bootstrapValidator({
				submitButtons : "button[id=addSubmit]",
				message : '不能为空',
				feedbackIcons : {
					valid : 'glyphicon glyphicon-ok',
					invalid : 'glyphicon glyphicon-remove',
					validating : 'glyphicon glyphicon-refresh'
				},
				fields : {
					company : {
						validators : {
							notEmpty : {
								message : "公司名称不能为空"
							}
						}
					},
					address : {
						validators : {
							notEmpty : {
								message : "公司地址不能为空"
							}
						}
					},
					contacts : {
						validators : {
							notEmpty : {
								message : "联系人不能为空"
							}
						}
					},
					contactsmobile : {
						validators : {
							notEmpty : {
								message : "联系人手机号不能为空"
							}
						}
					},
					contactsemail : {
						validators : {
							notEmpty : {
								message : "联系人邮箱不能为空"
							}
						}
					},
					remark : {
						validators : {
							notEmpty : {
								message : "公司经营范围不能为空"
							}
						}
					}
				},
				// bv校验通过则提交
				submitHandler : function(validator, form, submitButton) {
				}
			});
		
			
			// 添加提交
			$("#addSubmit").on("click", function() {
				
				var options = {
					url : _domain + "/admin/enterauth/apply.do",
					type : "post",
					resetForm : true,// 提交后重置表单
					dataType : 'json',
					beforeSubmit : function(formData, jqForm, options) {
						
						// 手动触发验证
						var bootstrapValidator = jqForm.data('bootstrapValidator');
						bootstrapValidator.validate();
						if (!bootstrapValidator.isValid()) {
							return false;
						}
					},
					success : function(data, statusText) {
						if (data != undefined) {
							if (data.success) {
								layer.msg('申请成功!', {icon : 1});
								loadUrl(_domain+"/admin/center.do")
							} else {
								layer.msg(data.msg, {icon : 2});
								//重置validate;
								$('#form').data("bootstrapValidator").resetForm();
							}
						}
					}

				};
				$("#form").ajaxSubmit(options);
			});
			
			
		}
	}
	
	
})