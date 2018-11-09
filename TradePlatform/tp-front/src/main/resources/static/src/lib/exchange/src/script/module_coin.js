/**
 * Created by Jason on 2015/6/1.
 */
define(function(require, exports, module) {

	var $ = require('jquery'), M = require('./module_tmpl'), A = require('./module_ajax');

	// 加载币种信息列表
	function GetCoinList(options) {
		//var op = options || {}, wrap = $('#view_coin_list'), temp = 'tmp_coin_list', Fn;
		var op = options || {}, wrap = $('#all_coin'), temp = 'temp_all_coin', Fn;
		Fn = new A({
			"url" : HRY.host + "/coin/findAllCoin",
			datatype : 'json'
		}, function(data) {
			var _code = data.code;

			switch (_code) {

			case 'error':
				// error
				break;
			case 'success':
				if (data.data != undefined && data.data != "") {
					var data=sort(data.data);
					wrap.html(M(temp, data));
					
				}

			}
		});
		
		  $(".pro-select-header").on('click',function() {
	          $(".pro-coin-list").slideToggle()
	      })
	      $(".pro-coin-list li").on('click',function() {
	          var e = $(this).data("val");
	          $(".pro-coin-txt").html($(this).text()), $('input[name="progess_id"]').val(e), $(".pro-coin-list").hide()
	      })

		return Fn;
	}

	/**
	 * 冒泡排序
	 */
	function sort(arr) {
		for (var i = 0; i < arr.length; i++) {
			for (var j = 0; j < arr.length - i - 1; j++) {
				if (arr[j].sort > arr[j + 1].sort) {
					var temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}
		return arr;
	}

	exports.GetCoinList = GetCoinList;

});
