var HRY={
host:'',
frontHost:"",
filehost:'',
staticUrl:'',
ver:'20151203',
head:'互融云软件',
modules:{
	mstatic:"/static/",
	web:'/manage/',
	oauth:'/oauth/',
	ico:'/manage/',
	factoring:'/manage/',
	finance:'/manage/',
	exchange:'/manage/',
	customer:'/manage/',
	account:'/manage/',
	sms:'/manage/',
	calculate:'/manage/',
	thirdpay:'/manage/',

	}
}

//如果logo、banner不存在，使用默认值
window.nofind=function(key){
	if(key!=null&&key!=""&&key!=undefined){
		var img=event.srcElement;
		if(key=="logo"){
			img.src=+"static/style/images/nofind/"+key+".png";
		}else if(key=="banner"){
			img.src= HRY.modules.mstatic+"/static/style/images/nofind/"+key+".png";
		}
		img.onerror=null; //控制不要一直跳动
		img.alt=null;
	}
}