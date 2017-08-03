/**
 * 中文加密
 */
function myFilterEncode(originalStr)
{
    var encodeString = encodeURI(encodeURI(originalStr,"utf-8"),"utf-8");
    return encodeString;
}

/**
 * 表单重置
 */
function clearData(formName)
{
	var formObj = document.forms[formName];     
	var formEl = formObj.elements;     
	for (var i=0; i<formEl.length; i++)     
	{         
		var element = formEl[i];         
		if (element.type == 'submit' 
			|| element.type == 'reset' 
			|| element.type == 'button' 
			|| element.type == 'hidden'
			) 
		{ 
			continue; 
		}                
		if (element.type == 'text') { element.value = ''; }         
		if (element.type == 'textarea') { element.value = ''; }         
		if (element.type == 'checkbox') { element.checked = false; }         
		if (element.type == 'radio') { element.checked = false; }   
		if (element.type == 'select-multiple') { element.selectedIndex = 0; }         
		if (element.type == 'select-one') { element.selectedIndex = 0; }     
	} 
}

/**
 * 添加记录
 */
function addRecord(url)
{
	if(url.indexOf("?") > 0 )   
	{   
	    url = url+"&oper=add&datestr="+new Date().getTime(); 
	}else{
	    url = url+"?oper=add&datestr="+new Date().getTime(); 
	}
	window.location.href=url;
}

/**
 * 修改记录
 */
function editRecord(id,url,width,height)
{
	var rows = $('#list').datagrid('getChecked');;
	if(rows==null || rows.length == 0){
	  alert('请选择记录');
	}else if(rows.length>1){
	  alert('只能选择一条记录');
	}else{
		if(url.indexOf("?") > 0 )   
		{   
		    url = url+"&id="+rows[0].id+"&oper=edit&datestr="+new Date().getTime(); 
		}else{
		    url = url+"?id="+rows[0].id+"&oper=edit&datestr="+new Date().getTime(); 
		}
		url = url+"&openWinId="+id; 
		lhgOpenWin(id,'修改',url,width,height);
	}
}

/**
 * 查看记录
 */
function viewRecord(id,url,width,height) {
	var rows =$('#list').datagrid('getChecked');;
	if(rows==null || rows.length == 0){
	  alert('请选择记录');
	}else if(rows.length>1){
	  alert('只能选择一条记录');
	}else{
		if(url.indexOf("?") > 0 )   
		{   
		    url = url+"&id="+rows[0].id+"&oper=view&datestr="+new Date().getTime(); 
		}else{
		    url = url+"?id="+rows[0].id+"&oper=view&datestr="+new Date().getTime(); 
		}
		url = url+"&openWinId="+id; 
		lhgOpenWin(id,'查看',url,width,height);
	}
}

/**
 * 激活
 */
function startRecord(url){
	var ids = [];
	var rows = $('#list').datagrid('getChecked');;
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
	}
	if(ids==null || ids.length == 0){
	   alert('请选择记录');
	}else{
	   if(url.indexOf("?") > 0 )   
	   {   
		    url = url+"&oper=start&datestr="+new Date().getTime(); 
	   }else{
		    url = url+"?oper=start&datestr="+new Date().getTime(); 
	   }
	   if (confirm("您确定要激活选择的记录吗?")) { 
	       $.get(url+"&status=1&ids=" +ids.join(','), function(data, status, Request){
		      if(data != null){
		        alert(data.msg);
		      }
		      gridReload();
	       });
	   }   
	}
}
/**
 * 停用
 */
function stopRecord(url){
	var ids = [];
	var rows = $('#list').datagrid('getChecked');
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
	}
	if(ids==null || ids.length == 0){
	   alert('请选择记录');
	}else{
	   if(url.indexOf("?") > 0 )   
	   {   
		    url = url+"&oper=stop&datestr="+new Date().getTime(); 
	   }else{
		    url = url+"?oper=stop&datestr="+new Date().getTime(); 
	   }
	   if (confirm("您确定要冻结选择的记录吗?")) { 
	       $.get(url+"&status=0&ids=" +ids.join(','), function(data, status, Request){
		      if(data != null){
		        alert(data.msg);
		      }
		      gridReload();
	       });
	   }
	}
}

/**
 * 删除记录
 */
function deleteRecord(url) {
	var ids = [];
	var rows = $(grid_list).datagrid('getChecked');
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
	}
	if(ids==null || ids.length == 0){
	   alert('请选择记录');
	}else{
	   if(url.indexOf("?") > 0 )   
	   {   
		    url = url+"&oper=delete&datestr="+new Date().getTime(); 
	   }else{
		    url = url+"?oper=delete&datestr="+new Date().getTime(); 
	   }
	   if (confirm("您确定要永久删除选择的记录吗?")) { 
	       $.get(url+"&ids=" +ids.join(','), function(data, status, Request){
		      if(data != null){
		        alert(data.msg);
		      }
		      gridReload(); 
	       });
	   }
	}
}

/**
 * 记录状态修改
 */
function statusChange(url){
   if(url.indexOf("?") > 0 )   
   {   
	    url = url+"&datestr="+new Date().getTime(); 
   }else{
	    url = url+"?datestr="+new Date().getTime(); 
   }
   if (confirm("您确定要修改选择的记录吗?")) { 
       $.get(url, function(data, status, Request){
	      if(data != null){
	        alert(data.msg);
	      }
	      gridReload();
       });
   }
}

/**
 * 删除记录
 */
function deleteRecordSec(url){
   if(url.indexOf("?") > 0 )   
   {   
	    url = url+"&datestr="+new Date().getTime(); 
   }else{
	    url = url+"?datestr="+new Date().getTime(); 
   }
   if (confirm("您确定要删除选择的记录吗?")) { 
       $.get(url, function(data, status, Request){
	      if(data != null){
	        alert(data.msg);
	      }
	      gridReload();
       });
   }
}

function lhgOpenWin(id,title,url,width,height) {
   //var dG = $.dialog({
   $.dialog({
	   id: id, 
	   title: title,
	   width: width, 
	   height: height,
	   content: 'url:'+url,
	   lock: true,
	   
	   max: true, 
	   min: false,
	   
	   drag: false, 
	   resize: false, 
	   padding: 0
   });
   //dG.ShowDialog();
}

function lhgOpenAlert(content) {
	$.dialog.alert(content);
}

function lhgOpenChildWin(title,url,width,height){
    if(url.indexOf("?") > 0 )   
    {   
	    url = url+"&datestr="+new Date().getTime(); 
    }else{
	    url = url+"?datestr="+new Date().getTime(); 
    }
    //var cDG = W.$.dialog({
    W.$.dialog({
	   id: 'open_child_win', 
	   title: title,
	   width: width, 
	   height: height,
	   content: 'url:'+url,
	   lock: true,
	   max: true, 
	   min: false,
	   drag: false, 
	   resize: false,
	   parent:api,
	   padding: 0
   });
   //cDG.ShowDialog();
}



/**居中*/
function center(obj){
	 var windowWidth = document.documentElement.clientWidth;   
	 var windowHeight = document.documentElement.clientHeight;   
	 var popupHeight = $(obj).height();   
	 var popupWidth = $(obj).width();    
	 $(obj).css({   
	  "top": (windowHeight-popupHeight)/2+$(document).scrollTop(),   
	  "left": (windowWidth-popupWidth)/2   
	 });  
}

//检测结尾字符串
String.prototype.endWith=function(str){
    if(str==null||str==""||this.length==0||str.length>this.length)
      return false;
    if(this.substring(this.length-str.length)==str)
      return true;
    else
      return false;
    return true;
}
//检测开始字符串
String.prototype.startWith=function(str){
    if(str==null||str==""||this.length==0||str.length>this.length)
      return false;
    if(this.substr(0,str.length)==str)
      return true;
    else
      return false;
    return true;
}

