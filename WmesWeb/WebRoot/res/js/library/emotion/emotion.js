/**
 * emotion.js 表情组件
 * 
 * @author mqzou 
 */
define(function(require) {
	var $ = require('jquery');
	var emotion={};
	var parent=null;
	
	emotion.selectCallBack=null;
	
	emotion.init=function(obj){
		var _emotion=this;
		
		
		parent=obj;
		var faceHtml = ' <div show="1" id="sinaEmotion" style="display:block" >';
		faceHtml += '<div class="right"></div>';
		faceHtml += '<ul class="faces">';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/pcmoren_huaixiao_thumb.png" alt="[FACE1001]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/pcmoren_tian_thumb.png" alt="[FACE1002]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/pcmoren_wu_thumb.png" alt="[FACE1003]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/huanglianwx_thumb.gif" alt="[FACE1004]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/tootha_thumb.gif" alt="[FACE1005]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/laugh.gif" alt="[FACE1006]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/tza_thumb.gif" alt="[FACE1007]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/kl_thumb.gif" alt="[FACE1008]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/wabi_thumb.gif" alt="[FACE1009]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/cj_thumb.gif" alt="[FACE1010]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/shamea_thumb.gif" alt="[FACE1011]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/zy_thumb.gif" alt="[FACE1012]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/bz_thumb.gif" alt="[FACE1013]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/bs2_thumb.gif" alt="[FACE1014]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/lovea_thumb.gif" alt="[FACE1015]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sada_thumb.gif" alt="[FACE1016]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/heia_thumb.gif" alt="[FACE1017]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/qq_thumb.gif" alt="[FACE1018]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sb_thumb.gif" alt="[FACE1019]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/mb_thumb.gif" alt="[FACE1020]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/landeln_thumb.gif" alt="[FACE1021]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/yhh_thumb.gif" alt="[FACE1022]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/zhh_thumb.gif" alt="[FACE1023]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/x_thumb.gif" alt="[FACE1024]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/cry.gif" alt="[FACE1025]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/wq_thumb.gif" alt="[FACE1026]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/t_thumb.gif" alt="[FACE1027]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/haqianv2_thumb.gif" alt="[FACE1028]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/bba_thumb.gif" alt="[FACE1029]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/angrya_thumb.gif" alt="[FACE1030]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/yw_thumb.gif" alt="[FACE1031]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/cza_thumb.gif" alt="[FACE1032]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/88_thumb.gif" alt="[FACE1033]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sk_thumb.gif" alt="[FACE1034]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sweata_thumb.gif" alt="[FACE1035]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/kunv2_thumb.gif" alt="[FACE1036]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/huangliansj_thumb.gif" alt="[FACE1037]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/money_thumb.gif" alt="[FACE1038]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sw_thumb.gif" alt="[FACE1039]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/cool_thumb.gif" alt="[FACE1040]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/huanglianse_thumb.gif" alt="[FACE1041]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/hatea_thumb.gif" alt="[FACE1042]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/gza_thumb.gif" alt="[FACE1043]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/dizzya_thumb.gif" alt="[FACE1044]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/bs_thumb.gif" alt="[FACE1045]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/crazya_thumb.gif" alt="[FACE1046]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/h_thumb.gif" alt="[FACE1047]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/yx_thumb.gif" alt="[FACE1048]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/numav2_thumb.gif" alt="[FACE1049]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/hufen_thumb.gif" alt="[FACE1050]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/hearta_thumb.gif" alt="[FACE1051]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/unheart.gif" alt="[FACE1052]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/pig.gif" alt="[FACE1053]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/panda_thumb.gif" alt="[FACE1054]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/rabbit_thumb.gif" alt="[FACE1055]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/ok_thumb.gif" alt="[FACE1056]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/ye_thumb.gif" alt="[FACE1057]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/good_thumb.gif" alt="[FACE1058]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/buyao_org.gif" alt="[FACE1059]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/z2_thumb.gif" alt="[FACE1060]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/come_thumb.gif" alt="[FACE1061]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sad_thumb.gif" alt="[FACE1062]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/shenshou_thumb.gif" alt="[FACE1063]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/horse2_thumb.gif" alt="[FACE1064]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/j_thumb.gif" alt="[FACE1065]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/fuyun_thumb.gif" alt="[FACE1066]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/geiliv2_thumb.gif" alt="[FACE1067]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/wg_thumb.gif" alt="[FACE1068]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/vw_thumb.gif" alt="[FACE1069]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/huatongv2_thumb.gif" alt="[FACE1070]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/lazhuv2_thumb.gif" alt="[FACE1071]"></a></li>';
		faceHtml += '<li class="item emotion-item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/cakev2_thumb.gif" alt="[FACE1072]"></a></li>';
		faceHtml += '</ul></div>';
		// 获取父元素
		//var parentElement = $(obj).parent();
		obj.append(faceHtml);
		
		
		$(document).on("click",".emotion-item",function(){
			var alt=$(this).find(".sina-emotion").attr("alt");
			//console.log(alt);
			//$(parent).css("display","none");
			_emotion.selectCallBack(alt);
			return alt;
			
		});
	} 
	
	emotion.parseEmotion=function(html){
		return parseEmotion(html);
	}
	
	emotion.destroy=function(){
		$(document).off("click",".emotion-item");
		$("#sinaEmotion").remove();
	}
	
	function parseEmotion(html){
		var emotionsMap;
		emotionsMap = {};
		emotionsMap['[FACE1001]'] = base_root + '/res/images/cornericon/pcmoren_huaixiao_thumb.png';
		emotionsMap['[FACE1002]'] = base_root + '/res/images/cornericon//pcmoren_tian_thumb.png';
		emotionsMap['[FACE1003]'] = base_root + '/res/images/cornericon//pcmoren_wu_thumb.png';
		emotionsMap['[FACE1004]'] = base_root + '/res/images/cornericon/huanglianwx_thumb.gif';
		emotionsMap['[FACE1005]'] = base_root + '/res/images/cornericon/tootha_thumb.gif';
		emotionsMap['[FACE1006]'] = base_root + '/res/images/cornericon/laugh.gif';
		emotionsMap['[FACE1007]'] = base_root + '/res/images/cornericon/tza_thumb.gif';
		emotionsMap['[FACE1008]'] = base_root + '/res/images/cornericon/kl_thumb.gif';
		emotionsMap['[FACE1009]'] = base_root + '/res/images/cornericon/wabi_thumb.gif';
		emotionsMap['[FACE1010]'] = base_root + '/res/images/cornericon/cj_thumb.gif';
		emotionsMap['[FACE1011]'] = base_root + '/res/images/cornericon/shamea_thumb.gif';
		emotionsMap['[FACE1012]'] = base_root + '/res/images/cornericon/zy_thumb.gif';
		emotionsMap['[FACE1013]'] = base_root + '/res/images/cornericon/bz_thumb.gif';
		emotionsMap['[FACE1014]'] = base_root + '/res/images/cornericon/bs2_thumb.gif';
		emotionsMap['[FACE1015]'] = base_root + '/res/images/cornericon/lovea_thumb.gif';
		emotionsMap['[FACE1016]'] = base_root + '/res/images/cornericon/sada_thumb.gif';
		emotionsMap['[FACE1017]'] = base_root + '/res/images/cornericon/heia_thumb.gif';
		emotionsMap['[FACE1018]'] = base_root + '/res/images/cornericon/qq_thumb.gif';
		emotionsMap['[FACE1019]'] = base_root + '/res/images/cornericon/sb_thumb.gif';
		emotionsMap['[FACE1020]'] = base_root + '/res/images/cornericon/mb_thumb.gif';
		emotionsMap['[FACE1021]'] = base_root + '/res/images/cornericon/landeln_thumb.gif';
		emotionsMap['[FACE1022]'] = base_root + '/res/images/cornericon/yhh_thumb.gif';
		emotionsMap['[FACE1023]'] = base_root + '/res/images/cornericon/zhh_thumb.gif';
		emotionsMap['[FACE1024]'] = base_root + '/res/images/cornericon/x_thumb.gif';
		emotionsMap['[FACE1025]'] = base_root + '/res/images/cornericon/cry.gif';
		emotionsMap['[FACE1026]'] = base_root + '/res/images/cornericon/wq_thumb.gif';
		emotionsMap['[FACE1027]'] = base_root + '/res/images/cornericon/t_thumb.gif';
		emotionsMap['[FACE1028]'] = base_root + '/res/images/cornericon/haqianv2_thumb.gif';
		emotionsMap['[FACE1029]'] = base_root + '/res/images/cornericon/bba_thumb.gif';
		emotionsMap['[FACE1030]'] = base_root + '/res/images/cornericon/angrya_thumb.gif';
		emotionsMap['[FACE1031]'] = base_root + '/res/images/cornericon/yw_thumb.gif';
		emotionsMap['[FACE1032]'] = base_root + '/res/images/cornericon/cza_thumb.gif';
		emotionsMap['[FACE1033]'] = base_root + '/res/images/cornericon/88_thumb.gif';
		emotionsMap['[FACE1034]'] = base_root + '/res/images/cornericon/sk_thumb.gif';
		emotionsMap['[FACE1035]'] = base_root + '/res/images/cornericon/sweata_thumb.gif';
		emotionsMap['[FACE1036]'] = base_root + '/res/images/cornericon/kunv2_thumb.gif';
		emotionsMap['[FACE1037]'] = base_root + '/res/images/cornericon/huangliansj_thumb.gif';
		emotionsMap['[FACE1038]'] = base_root + '/res/images/cornericon/money_thumb.gif';
		emotionsMap['[FACE1039]'] = base_root + '/res/images/cornericon/sw_thumb.gif';
		emotionsMap['[FACE1040]'] = base_root + '/res/images/cornericon/cool_thumb.gif';
		emotionsMap['[FACE1041]'] = base_root + '/res/images/cornericon/huanglianse_thumb.gif';
		emotionsMap['[FACE1042]'] = base_root + '/res/images/cornericon/hatea_thumb.gif';
		emotionsMap['[FACE1043]'] = base_root + '/res/images/cornericon/gza_thumb.gif';
		emotionsMap['[FACE1044]'] = base_root + '/res/images/cornericon/dizzya_thumb.gif';
		emotionsMap['[FACE1045]'] = base_root + '/res/images/cornericon/bs_thumb.gif';
		emotionsMap['[FACE1046]'] = base_root + '/res/images/cornericon/crazya_thumb.gif';
		emotionsMap['[FACE1047]'] = base_root + '/res/images/cornericon/h_thumb.gif';
		emotionsMap['[FACE1048]'] = base_root + '/res/images/cornericon/yx_thumb.gif';
		emotionsMap['[FACE1049]'] = base_root + '/res/images/cornericon/numav2_thumb.gif';
		emotionsMap['[FACE1050]'] = base_root + '/res/images/cornericon/hufen_thumb.gif';
		emotionsMap['[FACE1051]'] = base_root + '/res/images/cornericon/hearta_thumb.gif';
		emotionsMap['[FACE1052]'] = base_root + '/res/images/cornericon/unheart.gif';
		emotionsMap['[FACE1053]'] = base_root + '/res/images/cornericon/pig.gif';
		emotionsMap['[FACE1054]'] = base_root + '/res/images/cornericon/panda_thumb.gif';
		emotionsMap['[FACE1055]'] = base_root + '/res/images/cornericon/rabbit_thumb.gif';
		emotionsMap['[FACE1056]'] = base_root + '/res/images/cornericon/ok_thumb.gif';
		emotionsMap['[FACE1057]'] = base_root + '/res/images/cornericon/ye_thumb.gif';
		emotionsMap['[FACE1058]'] = base_root + '/res/images/cornericon/good_thumb.gif';//res/images/cornericon/'/ext/normal/d8/good_thumb.gif';
		emotionsMap['[FACE1059]'] = base_root + '/res/images/cornericon/buyao_org.gif';
		emotionsMap['[FACE1060]'] = base_root + '/res/images/cornericon/z2_thumb.gif';
		emotionsMap['[FACE1061]'] = base_root + '/res/images/cornericon/come_thumb.gif';
		emotionsMap['[FACE1062]'] = base_root + '/res/images/cornericon/sad_thumb.gif';
		emotionsMap['[FACE1063]'] = base_root + '/res/images/cornericon/shenshou_thumb.gif';
		emotionsMap['[FACE1064]'] = base_root + '/res/images/cornericon/horse2_thumb.gif';
		emotionsMap['[FACE1065]'] = base_root + '/res/images/cornericon/j_thumb.gif';
		emotionsMap['[FACE1066]'] = base_root + '/res/images/cornericon/fuyun_thumb.gif';
		emotionsMap['[FACE1067]'] = base_root + '/res/images/cornericon/geiliv2_thumb.gif';
		emotionsMap['[FACE1068]'] = base_root + '/res/images/cornericon/wg_thumb.gif';
		emotionsMap['[FACE1069]'] = base_root + '/res/images/cornericon/vw_thumb.gif';
		emotionsMap['[FACE1070]'] = base_root + '/res/images/cornericon/huatongv2_thumb.gif';
		emotionsMap['[FACE1071]'] = base_root + '/res/images/cornericon/lazhuv2_thumb.gif';
		emotionsMap['[FACE1072]'] = base_root + '/res/images/cornericon/cakev2_thumb.gif';
		
		html = html
				.replace(/<.*?>/g, function($1) {
					$1 = $1.replace('[', '[');
					$1 = $1.replace(']', ']');
					return $1;
				})
				.replace(
						/\[[^\[\]]*?\]/g,
						function($1) {
							var url = emotionsMap[$1];
							if (url) {
								return '<img class="wmes-emotion" src="'
										+ url
										+ '" alt="'
										+ $1 + '" />';
							}
							return $1;
						});

		return html;
	}
	
	
	
	
	
	
	return emotion;
})