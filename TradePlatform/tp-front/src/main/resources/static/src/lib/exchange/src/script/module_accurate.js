/**
 * Created by Jason on 2015/3/18.
 */
(function(){
    var BitAcc = {
        //精确算法 - 加法
        'Add': function (arg1, arg2) {
            if(arg1==undefined||arg2==undefined) return;
            var r1, r2, m, Mu = this.Mul;
            try {
                r1 = arg1.toString().split(".")[1].length
            } catch (e) {
                r1 = 0
            }
            try {
                r2 = arg2.toString().split(".")[1].length
            } catch (e) {
                r2 = 0
            }
            m = Math.pow(10, Math.max(r1, r2));
            return (Mu(arg1, m) + Mu(arg2, m)) / m
        },
        //精确算法 - 减法
        'Sub': function (arg1, arg2) {
            if(arg1==undefined||arg2==undefined) return;
            var r1, r2, m, n, Mu = this.Mul;
            try {
                r1 = arg1.toString().split(".")[1].length
            } catch (e) {
                r1 = 0
            }
            try {
                r2 = arg2.toString().split(".")[1].length
            } catch (e) {
                r2 = 0
            }
            m = Math.pow(10, Math.max(r1, r2));
            n = (r1 >= r2) ? r1 : r2;
            return ((Mu(arg1, m) - Mu(arg2, m)) / m).toFixed(n);
        },
	    // 格式化数字
	    'format_number': function (num) {
			return (num.toFixed(0) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
		},
	    // 格式化数字
	    'BtcRound' : function (num, n) {
			var dd = 1;
			var tempnum;
			for (var i = 0; i < n; i++) {
				dd *= 10;
			}
			tempnum = num * dd;
			tempnum = Math.round(tempnum);
			return (tempnum / dd);
		},
        //精确算法 - 乘法
        'Mul': function (arg1, arg2) {
            if(arg1==undefined||arg2==undefined) return;
            var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
            try {
                m += s1.split(".")[1].length;
            } catch (e) {
            }
            try {
                m += s2.split(".")[1].length;
            } catch (e) {
            }
            return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
        },
        //精确除法
        'Div' : function (arg1,arg2) {
            if(arg1==undefined||arg2==undefined) return;
            var t1 = 0,t2 = 0,r1,r2;
            try {
                t1 = arg1.toString().split(".")[1].length
            } catch (e) {
            }
            try {
                t2 = arg2.toString().split(".")[1].length
            } catch (e) {
            }
            return Number(arg1.toString().replace(".","")) / Number(arg2.toString().replace(".","")) * Math.pow(10,t2 - t1)
        }
    };
    "function" == typeof define ? define(function() {
        return BitAcc
    }) : "undefined" != typeof exports ? module.exports = BitAcc : window.BitAcc = BitAcc

})();