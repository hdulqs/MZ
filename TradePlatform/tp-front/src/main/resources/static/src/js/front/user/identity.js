    $(function(){
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
   }
  //  select
  $('.select-country-header').on('click',function(){
    $('.select-input-list').slideToggle();
  })
  $(".select-input-list li").on('click', function() {
        var e = $(this).data("val");
        $("#countryName").val($(this).text()), $('input[name="countryOrigin"]').val(e), $(".select-input-list").hide()
    })
    })
    
    
    function show(){
    	$("#authenticationForm2").show();
    	$("#authenticationForm1").hide();
    }
    //表单验证
    function check(sign){debugger;
			var trueName = $("#trueName"+sign).val();
			var sex = $("#sex").val();
			var surname = $("#surname"+sign).val();
			var country = $("#country"+sign).val();
			var cardType = $("#cardType").val();
			var cardId = $("#cardId"+sign).val();
			var picIDa = $("#picIDa"+sign).val();
			var picIDb = $("#picIDb"+sign).val();
			var picIDc = $("#picIDc"+sign).val();
			if(!trueName){
				layer.msg('姓名不能为空', {icon: 2});
				return false;
			}else{
				var reg = /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi;
				if(!reg.test(trueName)){
					layer.msg('姓名必须为中文', {icon: 2});
					return false;
				}
			}
			if(sex==2 && !sex){
				layer.msg('姓別不能为空', {icon: 2});
				return false;
			}
			if(!surname){
				layer.msg('姓氏不能为空', {icon: 2});
				return false;
			}
			if(!cardId){
				layer.msg('证件号不能为空', {icon: 2});
				return false;
			}
			if(!country){
				layer.msg('地区不能为空', {icon: 2});
				return false;
			}
			if(!validate.check_card(cardId1)){
				layer.msg('证件号格式不正确', {icon: 2});
				return false;
			}
				
			if(!picIDa || picIDa.length()==0){
				layer.msg('照片不能为空', {icon: 2});
				return false;
			}
			if(!picIDb || picIDb.length()==0){
				layer.msg('照片不能为空', {icon: 2});
				return false;
			}
			if(!picIDc || picIDc.length()==0){
				layer.msg('照片不能为空', {icon: 2});
				return false;
			}
				
       }
    
    
    //图片回显
    function changImg(e,str){
        for (var i = 0; i < e.target.files.length; i++) {  
            var file = e.target.files.item(i);  
            if (!(/^image\/.*$/i.test(file.type))) {  
                continue; //不是图片 就跳出这一次循环  
            }  
            //实例化FileReader API  
            var freader = new FileReader();  
            freader.readAsDataURL(file);  
            freader.onload = function(e) {  
                $("#picImg"+str).attr("src",e.target.result);  
            }  
        }  
    }
    function changImg1(e){
    	var str="1";
        changImg(e,str);
    }
    function changImg2(e){
    	var str="2";
        changImg(e,str);
    }
    function changImg3(e){
        var str="3";
        changImg(e,str);
    }
     function changImg4(e){
        var str="4";
        changImg(e,str);
    }
     function changImg5(e){
        var str="5";
        changImg(e,str);
    }
     function changImg6(e){
        var str="6";
        changImg(e,str);
    }