$(function() {
	$(".layui-m-layerbtn").click(function() {
		$("#layui-m-layer0").hide();
	});
	$(document).ready(function() {
		var v = $('.coinnames').val();
		if (v == 'xlm' || v == 'xrp') {
			$("#layui-m-layer0").css('display', 'block');
		}
	})
});
$(function() {
	FastClick.attach(document.body);
})
//点击复制
$('#copys').click(function() {
		var e = document.getElementById("address");
		e.select(); // 选择对象
		document.execCommand("Copy"); // 执行浏览器复制命令
		alert("复制成功");
	})