define(function(require) {
	var $ = require('jquery');
	require('pagination');
	
	 var keyword=getQueryString("key");
	 
	 $(".keyWord").text(keyword);
	 
	 var pageIndex=0;
	 var pageSize=10;
	 var total=0;
	 getDataList();
	 function getDataList(){
		
		 $.ajax({
			 type:"post",
			 datatype:"json",
			 url:base_root+"/front/news/info/searchNewsJson.do",
			 data:{page:pageIndex+1,rows:pageSize,key:keyword},
			 async:false,
			 success:function(json){
				 total=json.total;
				 if(total>pageIndex){
					 $("#ifa_more").show();
				 }else{
					 $("#ifa_more").hide();
				 }
				 var html="";
				 $.each(json.list,function(i,n){
					 
					 var title=highlightFomat(n.title,keyword);
					  
					 html+='<div class="article">'
							+'<div class="article-right">'
							+'<div class="article-title">'
							
							+'<a class="title" href="newsContent.do?id='+n.id+'">'+title+'</a>'
							+'</div>'
							+'<div class="article-text">'
							+'<p>'+n.description+'</p>'
							
							//+'<a href="newsContent.do?id='+n.id+'"><img src="'+base_root+'/res/css/news/images/more.png" alt="" /></a>'
							+'<div class="clearfix"></div>'
							+'<div style="height:16px">'
							+'<div>'
							+'<a href="'+base_root+'/front/news/info/newsList.do?id='+n.categoryId+'" style="float:left;text-decoration: none;font-size: 18px;font-weight: bold;">'
							+'<p >'+langMutilForJs['news.category']+':'+n.categoryName+'</p></a>'
							+'</div>'
							+'<p style="float:right;">'+n.lastUpdate+'</p>'
							
							+'</div>'
							+'</div>'
							+'</div>'
							+'<div class="clearfix"></div>'
							+'</div>';
				 })
				 $(".data-list").empty().append(html);
				
				 $("#pagination").show();
				 $("#pagination").pagination(total, {
						callback : pageselectCallback,
						numDetail : '',
						items_per_page : 10,
						num_display_entries : 4,
						current_page : pageIndex,
						num_edge_entries : 2
					});
					function pageselectCallback(page_id, jq) {
						pageIndex= page_id;
						getDataList();
						$(document).scrollTop(0);
					}
			 }
		 })
	 }
	 $("#searchKeyWord").keydown(function(e){
		   if(e.keyCode==13){
		     search()
		}
		});
		$("#searchKeyBtn").on("click",function(){
		search()
		})
		function search(){

		 var key=$("#searchKeyWord").val();
			  window.location.href=base_root+"/front/news/info/searchNews.do?key="+key;
		}

});