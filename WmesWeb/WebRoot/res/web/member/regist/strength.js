 define(function(require) {
 	
 		var $ = require('jquery');
//密码强度函数
	var checkPasswordLevel = function(strPassword)
	{
		var result = 0;
		if ( strPassword.length == 0)
			result += 0;
		else if ( strPassword.length<8 && strPassword.length >0 )
			result += 5;
		else if (strPassword.length>10)
			result += 25;
		else
			result += 10;
		
		//check letter
		var bHave = false;
		var bAll = false;
		var capital = strPassword.match(/[A-Z]{1}/);//找大写字母
		var small = strPassword.match(/[a-z]{1}/);//找小写字母
		if ( capital == null && small == null )
		{
			result += 0; //没有字母
			bHave = false;
		}
		else if ( capital != null && small != null )
		{
			result += 20;
			bAll = true;
		}
		else
		{	
			result += 10;
			bAll = true;
		}
		//alert("检查字母："+result);
		
		//检查数字
		var bDigi = false;
		var digitalLen = 0;
		for ( var i=0; i<strPassword.length; i++)
		{
		
			if ( strPassword.charAt(i) <= '9' && strPassword.charAt(i) >= '0' )
			{
				bDigi = true;
				digitalLen += 1;
				//alert(strPassword[i]);
			}
			
		}
		if ( digitalLen==0 )//没有数字
		{
			result += 0;
			bDigi = false;
		}
		else if (digitalLen>2)//2个数字以上
		{
			result += 20 ;
			bDigi = true;
		}
		else
		{
			result += 10;
			bDigi = true;
		}
		//alert("数字个数：" + digitalLen);
		//alert("检查数字："+result);
		
		//检查非单词字符
		var bOther = false;
		var otherLen = 0;
		for (var j=0; j<strPassword.length; j++)
		{
			if ( (strPassword.charAt(j)>='0' && strPassword.charAt(j)<='9') ||  
				(strPassword.charAt(j)>='A' && strPassword.charAt(j)<='Z') ||
				(strPassword.charAt(j)>='a' && strPassword.charAt(j)<='z'))
				continue;
			otherLen += 1;
			bOther = true;
		}
		if ( otherLen == 0 )//没有非单词字符
		{
			result += 0;
			bOther = false;
		}
		else if ( otherLen >1)//1个以上非单词字符
		{
			result +=25 ;
			bOther = true;
		}
		else
		{
			result +=10;
			bOther = true;
		}
		//alert("检查非单词："+result);
		
		//检查额外奖励
		if ( bAll && bDigi && bOther)
			result += 5;
		else if (bHave && bDigi && bOther)
			result += 3;
		else if (bHave && bDigi )
			result += 2;
		//alert("检查额外奖励："+result);

		var level = "";
		//根据分数来算密码强度的等级
		if ( result >=80 )
			level = 7;
		else if ( result>=70)
			level = 6;
		else if ( result>=60)
			level = 5;
		else if ( result>=50)
			level = 4;
		else if ( result>=40)
			level = 3;
		else if ( result>20)
			level = 2;
		else if ( result>0)
			level = 1;
		else
			level = 0;

		return level.toString();
	}	


	//密码强度换图片
	var setPasswordLevel = function(passwordObj)
	{
	var level = 0,
		levelClass;
	level = checkPasswordLevel(passwordObj.val());
	// levelObj.className = level;
		if( level == 7 || level == 6){
			levelClass = "green";
		}else if(level == 5 || level == 4 || level == 3){
			levelClass = "blue";
		}else{
			levelClass = "red";
		}
		var passwLevel = $(".register_strength").find("li");
		passwLevel.removeClass();
		for (var i = 0; i < level; i++){
			passwLevel.eq(i).addClass(levelClass);
		}
		$(".paw_level .register_strength_font").hide().eq(level).show();
	}

	$("#password").on("keyup",function(){
		setPasswordLevel($(this));
	});

	$(".j-password-input").on("keyup",function(){
		setPasswordLevel($(this));
	});
 })	
