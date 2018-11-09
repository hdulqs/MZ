/*
 *table 表格排序
 * 使用说明  :
 * 方法1:
 *     new TableSorter("tb1");
 *     效果:
 *         id为tb1的table的第一行任意单元格都可以点击进行排序.
 *
 *         方法2:
 *             new TableSorter("tb1", 0, 1, 3);
 *             效果:
 *                 id为tb1的table的第一行0,1,3单元格可以进行点击排序.
 *                 */
define(function(require, exports, module){
require("style/js/mobile/js/hhSwipe.js");
function TableSorter(table) {
    this.Table = this.$(table);
    if (this.Table.rows.length <= 1) {
        return;
    }
    var args = [];
    if (arguments.length > 1) {
        for (var x = 1; x < arguments.length; x++) {
            args.push(arguments[x]);
        }
    }
    this.Init(args);
}

TableSorter.prototype = {
    $: function(element) //简写document.getElementById
        {
            return document.getElementById(element);
        },
    Init: function(args) //初始化表格的信息和操作
        {
            this.Rows = [];
            this.Header = [];
            this.ViewState = [];
            this.LastSorted = null;
            this.NormalCss = "bg-th";
            this.SortAscCss = "SortAscCss";
            this.SortDescCss = "SortDescCss";
            for (var x = 0; x < this.Table.rows.length; x++) {
                this.Rows.push(this.Table.rows[x]);
            }
            this.Header = this.Rows.shift().cells;
            for (var x = 0; x < (args.length ? args.length : this.Header.length); x++) {
                var rowIndex = args.length ? args[x] : x;
                if (rowIndex >= this.Header.length) {
                    continue;
                }
                this.ViewState[rowIndex] = false;
                this.Header[rowIndex].style.cursor = "pointer";
                this.Header[rowIndex].onclick = this.GetFunction(this, "Sort", rowIndex);
            }
        },
    GetFunction: function(variable, method, param) //取得指定对象的指定方法.
        {
            return function() {
                variable[method](param);
            }
        },
    Sort: function(column) //执行排序.
        {
            if (this.LastSorted) {
                this.LastSorted.className = this.NormalCss;
                this.LastSorted.style.color = "#333";
            }
            var SortAsNumber = true;
            for (var x = 0; x < this.Rows.length && SortAsNumber; x++) {
                SortAsNumber = this.IsNumeric($(this.Rows[x].cells[column]).children(0).html());
            }

            this.Rows.sort(
                function(row1, row2) {
                    var result;
                    var value1, value2;
                    value1 = $(row1.cells[column]).children(0).html().replace(/%/, "");
                    value2 = $(row2.cells[column]).children(0).html().replace(/%/, "");
                    var sear = new RegExp('万');
                    var sear1 = new RegExp('亿');
                    if (sear.test(value1)) {
                        value1 = /\d+(?:\.\d+)?/.exec(value1);
                        value1 = value1 * 10000;
                    }
                    if (sear.test(value2)) {
                        value2 = /\d+(?:\.\d+)?/.exec(value2);
                        value2 = value2 * 10000;
                    }
                    if (sear1.test(value1)) {
                        value1 = /\d+(?:\.\d+)?/.exec(value1);
                        value1 = value1 * 100000000;
                    }
                    if (sear1.test(value2)) {
                        value2 = /\d+(?:\.\d+)?/.exec(value2);
                        value2 = value2 * 100000000;
                    }

                    if (value1 == value2) {
                        return 0;
                    }
                    if (SortAsNumber) {
                        result = parseFloat(value1) > parseFloat(value2);
                    } else {
                        result = value1 > value2;
                    }
                    result = result ? -1 : 1;
                    return result;
                })
            this.Header[column].style.color = "#f60";
            if (this.ViewState[column]) {
                this.Rows.reverse();
                this.ViewState[column] = false;
                this.Header[column].className += ' ' + this.SortAscCss;

            } else {
                this.ViewState[column] = true;
                this.Header[column].className += ' ' + this.SortDescCss;
            }
            this.LastSorted = this.Header[column];
            var frag = document.createDocumentFragment();
            for (var x = 0; x < this.Rows.length; x++) {
                frag.appendChild(this.Rows[x]);
            }
            this.Table.tBodies[0].appendChild(frag);
            this.OnSorted(this.Header[column], this.ViewState[column]);
        },
    IsNumeric: function(num) //验证是否是数字类型.
        {
            //			num=num.replace(/\s+/, "").replace(/[+-]/, "").replace(/%/,"").replace(/[↑↓]/,"");
            return /\d+(?:\.\d+)?/.exec(num);
        },
    OnSorted: function(cell, IsAsc) //排序完后执行的方法.cell:执行排序列的表头单元格,IsAsc:是否为升序排序.
        {
            return;
        }
}

new TableSorter("click-sort1", 2, 3, 4);
//new TableSorter("click-sort2", 2, 3, 4);
//new TableSorter("click-sort3", 2, 3, 4);
//new TableSorter("click-sort4", 2, 3, 4);
//new TableSorter("click-sort5", 2, 3, 4);


var bullets = document.getElementById('position').getElementsByTagName('li');

var banner = Swipe(document.getElementById('mySwipe'), {
    auto: 4000,
    continuous: true,
    disableScroll: false,
})

    $("#s-Tab tbody tr").on("tap", function() {
        $(this).siblings().css({
            'background': "none"
        });
        $(this).css({
            'background': "#f6f6f6"
        });
    })
    $(".s-nav").on("tap", ".tags_coin_click", function() {
        var pageId = $("#page .s-tab"),
            i = $(this).index();
        pageId.eq(i).show().siblings().hide()
        $(this).addClass('s-aClick').siblings().removeClass("s-aClick")
    })
})