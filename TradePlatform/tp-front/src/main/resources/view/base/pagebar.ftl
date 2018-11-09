<#include "/base/base.ftl">

<#if frontPage.page!=0>
<nav aria-label="Page navigation">
  <ul class="pagination">
    <li <#if thispage==1>class="disabled" </#if> >
      <a href="javascript:void(0);" aria-label="Previous" title="上一页"  id="hry_lastpage">
        <span aria-hidden="true"><@spring.message code="shangyiye"/></span>
      </a>
    </li>
    
    <#if frontPage??>
    <#list 1..frontPage.page as p>
    <li <#if thispage==p>class="active"</#if>  ><a href="javascript:void(0);" type="hry_pagenumber" >${p}</a></li>
    </#list>
    </#if>
    <li <#if thispage==frontPage.page>class="disabled" </#if>>
    <a href="javascript:void(0);" aria-label="Next" title="下一页" id="hry_nextpage">
        <span aria-hidden="true"><@spring.message code="xiayiye"/></span>
    </a>
    </li>
  </ul>
</nav>
<form id="hry_pageform"  method="post">
	<input type="hidden" name="limit" value="${frontPage.pageSize}">
	<input type="hidden" name="offset" value="0">
</form>
<script type="text/javascript" src="${ctx}/static/${version}/lib/exstatic/static/lib/jquery/jquery-1.9.1.min.js" ></script>
<script type="text/javascript" >
//上一页
$("#hry_lastpage").on("click",function(){
	var thispage = ${thispage};
	if(thispage==1){
		return false;
	}
	$($("#hry_pageform").find("input[name=offset]")[0]).val((thispage-2)*${frontPage.pageSize})
	$("#hry_pageform").submit();
});
//1,2,3,4,5页
$("li").on("click","a[type=hry_pagenumber]",function(){
	//给offset附值
	$($("#hry_pageform").find("input[name=offset]")[0]).val(($(this).html()-1)*${frontPage.pageSize})
	$("#hry_pageform").submit();
})
//下一页
$("#hry_nextpage").on("click",function(){
	var thispage = ${thispage};
	if(thispage==${frontPage.page}){
		return false;
	}
	debugger
	$($("#hry_pageform").find("input[name=offset]")[0]).val(thispage*${frontPage.pageSize})
	$("#hry_pageform").submit();
});
</script>
</#if>
