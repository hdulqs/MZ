/**
 * 框架
 * 使用规则 <hry-search/>
 */
define(['app'], function (app) {
	app.directive('hrySearch', ["$rootScope","$http","$templateCache","$compile",function ($rootScope,$http,$templateCache,$compile) {
   
	return {
       replace: false,
       restrict: 'EA', 
       scope:true,
       link: function(scope,element) {
    	   scope.searchClick=function(){
           	$(".search-pannel").addClass("show");
           	
        	$(".fa-sort-desc").removeClass("fa-sort-desc").addClass("fa-sort-asc");
           }
    	   scope.close=function(){
              	$(".search-pannel").removeClass("show");
              	$(".fa-sort-asc").removeClass("fa-sort-asc").addClass("fa-sort-desc");
             }
       }
      } 

  }])
})
  


