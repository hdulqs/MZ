function validateMsg(id, msg, status){
	//layer.msg(msg, {icon: 2});
	$('#' + id + 'msg').html('<font style="color:red">'+msg+'</font>');
//$('#' + id + 'msg').attr('class', status? 'loginB1': 'false');
}
function valiForm(){
	for(var i in vali){
		$('#' + i).focus();
		$('#' + i).blur();
		if(!vali[i]) {
            event.preventDefault();
        }
	}
	return true;
}
