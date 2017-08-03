
//由数字、26个英文字母或者下划线组成的字符串
function isCheckInput(s) 
{ 
  var patrn=/^[a-zA-Z@.&~%#0-9_-]{1,}$/; 
  if (!patrn.exec(s)) return false 
  return true 
}

/*******--------js base64加解密的方式begin---------***********/
var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
//将Ansi编码的字符串进行Base64编码
function encode64(input) {
	var output = "";
	var chr1, chr2, chr3 = "";
	var enc1, enc2, enc3, enc4 = "";
	var i = 0;
	do {
	chr1 = input.charCodeAt(i++);
	chr2 = input.charCodeAt(i++);
	chr3 = input.charCodeAt(i++);
	enc1 = chr1 >> 2;
	enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
	enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
	enc4 = chr3 & 63;
	if (isNaN(chr2)) {
	enc3 = enc4 = 64;
	} else if (isNaN(chr3)) {
	enc4 = 64;
	}
	output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2)
	+ keyStr.charAt(enc3) + keyStr.charAt(enc4);
	chr1 = chr2 = chr3 = "";
	enc1 = enc2 = enc3 = enc4 = "";
	} while (i < input.length);
	return output;
}

//将Base64编码字符串转换成Ansi编码的字符串
function decode64(input) {
	var output = "";
	var chr1, chr2, chr3 = "";
	var enc1, enc2, enc3, enc4 = "";
	var i = 0;
	if (input.length % 4 != 0) {
	return "";
	}
	var base64test = /[^A-Za-z0-9\+\/\=]/g;
	if (base64test.exec(input)) {
	return "";
	}
	do {
	enc1 = keyStr.indexOf(input.charAt(i++));
	enc2 = keyStr.indexOf(input.charAt(i++));
	enc3 = keyStr.indexOf(input.charAt(i++));
	enc4 = keyStr.indexOf(input.charAt(i++));
	chr1 = (enc1 << 2) | (enc2 >> 4);
	chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
	chr3 = ((enc3 & 3) << 6) | enc4;
	output = output + String.fromCharCode(chr1);
	if (enc3 != 64) {
	output += String.fromCharCode(chr2);
	}
	if (enc4 != 64) {
	output += String.fromCharCode(chr3);
	}
	chr1 = chr2 = chr3 = "";
	enc1 = enc2 = enc3 = enc4 = "";
	} while (i < input.length);
	return output;
}

function utf16to8(str) {
	var out, i, len, c;
	out = "";
	len = str.length;
	for(i = 0; i < len; i++) {
	  c = str.charCodeAt(i);
	  if ((c >= 0x0001) && (c <= 0x007F)) {
	    out += str.charAt(i);
	  } else if (c > 0x07FF) {
	    out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
	    out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));
	    out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
	  } else {
	    out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));
	    out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
	  }
	}
	return out;
}

function utf8to16(str) {
	var out, i, len, c;
	var char2, char3;
	out = "";
	len = str.length;
	i = 0;
	while(i < len) {
	  c = str.charCodeAt(i++);
	  switch(c >> 4) {
	    case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
	      // 0xxxxxxx
	      out += str.charAt(i-1);
	      break;
	    case 12: case 13:
	      // 110x xxxx   10xx xxxx
	      char2 = str.charCodeAt(i++);
	      out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
	      break;
	    case 14:
	      // 1110 xxxx  10xx xxxx  10xx xxxx
	      char2 = str.charCodeAt(i++);
	      char3 = str.charCodeAt(i++);
	      out += String.fromCharCode(((c & 0x0F) << 12) |
	      ((char2 & 0x3F) << 6) |
	      ((char3 & 0x3F) << 0));
	      break;
	  }
	}
	return out;
}
/*******--------js base64加解密的方式end---------***********/

/***************通用工具方法*****************/
//获取URL参数
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURIComponent(r[2]);
    return null;
}
//日期格式化 mqzou 2017-01-20
function formatDate(date,format)
{
	if(format==undefined || format=="")
		format="yyyy-MM-dd HH:mm:ss";
   // var d = new Date(date);
	var d= new Date(Date.parse(date.replace(/-/g,"/")));
    var zeroize = function (value, length)
    {
        if (!length) length = 2;
        value = String(value);
        for (var i = 0, zeros = ''; i < (length - value.length); i++)
        {
            zeros += '0';
        }
        return zeros + value;
    };
 
    return format.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0)
    {
        switch ($0)
        {
            case 'd': return d.getDate();
            case 'dd': return zeroize(d.getDate());
            case 'ddd': return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
            case 'dddd': return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
            case 'M': return d.getMonth() + 1;
            case 'MM': return zeroize(d.getMonth() + 1);
            case 'MMM': return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
            case 'MMMM': return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
            case 'yy': return String(d.getFullYear()).substr(2);
            case 'yyyy': return d.getFullYear();
            case 'h': return d.getHours() % 12 || 12;
            case 'hh': return zeroize(d.getHours() % 12 || 12);
            case 'H': return d.getHours();
            case 'HH': return zeroize(d.getHours());
            case 'm': return d.getMinutes();
            case 'mm': return zeroize(d.getMinutes());
            case 's': return d.getSeconds();
            case 'ss': return zeroize(d.getSeconds());
            case 'l': return zeroize(d.getMilliseconds(), 3);
            case 'L': var m = d.getMilliseconds();
                if (m > 99) m = Math.round(m / 10);
                return zeroize(m);
            case 'tt': return d.getHours() < 12 ? 'am' : 'pm';
            case 'TT': return d.getHours() < 12 ? 'AM' : 'PM';
            case 'Z': return d.toUTCString().match(/[A-Z]+$/);
            // Return quoted strings with the surrounding quotes removed
            default: return $0.substr(1, $0.length - 2);
        }
    });
    
    
};

//关键字高亮 by mqzou 2017-03-13
function highlightFomat(content,key){
	var start=content.toLowerCase().indexOf(key.toLowerCase());
	var end=start+key.length;
	var keyword=content.substring(start,end);
	var startText=content.substring(0,start);
	var endText=content.substring(end,content.length-1);
	return startText+'<font class="keyWord" style="color:red;">'+keyword+'</font>'+endText;
}
//foot位置固定
function windowHeight(){
	var windowHeight = $(window).height();
	windowHeight = windowHeight-90;
	if($('.wmes-content').height() < windowHeight){
			$('.wmes-content').css('min-height',windowHeight);
            $('.wmes-notop-content').css('min-height',windowHeight);
	};
};
//金额格式化
function formatMoney(num,suffix){
	if (!num) return '0.00'+suffix;  		
	num = num.toString().replace(/\$|\,/g,'');    
    if(isNaN(num))    
    num = "";    
    var sign = (num == (num = Math.abs(num)));    
    num = Math.floor(num*100+0.50000000001);    
    var cents = num%100;    
    num = Math.floor(num/100).toString();    
    if(cents<10)    
    cents = "0" + cents;    
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
    num = num.substring(0,num.length-(4*i+3))+','+    
    num.substring(num.length-(4*i+3)); 
    var value = (((sign)?'':'-') + num + '.' + cents);
    if(suffix){value = value+suffix;}
    return value;
}
