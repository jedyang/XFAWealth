define(function(require) {
	var jQuery = require('jquery');
	
(function($){
    $.fn.validationEngineLanguage = function(){
    };
    $.validationEngineLanguage = {
        newLang: function(){
            $.validationEngineLanguage.allRules = {
                "required": { // Add your regex rules here, you can take telephone as an example
                    "regex": "none",
                    "alertText": "* Fill necessarily",
                    "alertTextCheckboxMultiple": "* Please choose one",
                    "alertTextCheckboxe": "* Please choose this column hook",
                    "alertTextDateRange": "* Please choose a date range"
                },
                "requiredInFunction": { 
                    "func": function(field, rules, i, options){
                        return (field.val() == "test") ? true : false;
                    },
                    "alertText": "* Field must equal test"
                },
                "dateRange": {
                    "regex": "none",
                    "alertText": "* Invalid ",
                    "alertText2": " date range"
                },
                "dateTimeRange": {
                    "regex": "none",
                    "alertText": "* Invalid ",
                    "alertText2": " time range"
                },
                "minSize": {
                    "regex": "none",
                    "alertText": "* At least ",
                    "alertText2": " characters"
                },
                "maxSize": {
                    "regex": "none",
                    "alertText": "* Not more than ",
                    "alertText2": " characters"
                },
				"groupRequired": {
                    "regex": "none",
                    "alertText": "* Please choose one of them"
                },
                "min": {
                    "regex": "none",
                    "alertText": "* The minimum value for "
                },
                "max": {
                    "regex": "none",
                    "alertText": "* The maximum value for "
                },
                "past": {
                    "regex": "none",
                    "alertText": "* Date must be earlier than "
                },
                "future": {
                    "regex": "none",
                    "alertText": "* Date must be later than "
                },	
                "maxCheckbox": {
                    "regex": "none",
                    "alertText": "* choose ",
                    "alertText2": " most"
                },
                "minCheckbox": {
                    "regex": "none",
                    "alertText": "* Please choose at least ",
                    "alertText2": " item"
                },
                "equals": {
                    "regex": "none",
                    "alertText": "* Please enter the password that was the same as the above "
                },
                "creditCard": {
                    "regex": "none",
                    "alertText": "* Invalid credit card number "
                },
                "phone": {
                    // credit: jquery.h5validate.js / orefalo
                    "regex": /^0\d{2,3}-?\d{7,8}$/,
                    "alertText": "* Invalid phone number <br/>For example：020-1234567"
                },
				"telephone":{   
                    "regex":/^1[3|4|5|7|8]\d{9}$/,   
                    "alertText":"* Please enter a valid phone number "
				},
                "email": {
                    // Shamelessly lifted from Scott Gonzalez via the Bassistance Validation plugin http://projects.scottsplayground.com/email_address_validation/
                    "regex": /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i,
                    "alertText": "* Incorrect email address format"
                },
                "integer": {
                    "regex": /^[\-\+]?\d+$/,
                    "alertText": "* Is not a valid integer "
                },
                "number": {
                    // Number, including positive, negative, and floating decimal. credit: orefalo
                    "regex": /^[\-\+]?((([0-9]{1,3})([,][0-9]{3})*)|([0-9]+))?([\.]([0-9]+))?$/,
                    "alertText": "* Is not a valid number "
                },
                "date": {
                    "regex": /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/,
                    "alertText": "* Is not a valid date, format required for YYYY-MM-DD"
                },
                "ipv4": {
                    "regex": /^((([01]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))[.]){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))$/,
                    "alertText": "* Invalid IP address"
                },
                "url": {
                    "regex": /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i,
                    "alertText": "* Invalid URL"
                },
                "onlyNumberSp": {
                    "regex": /^[0-9\ ]+$/,
                    "alertText": "* Number only"
                },
                "onlyLetterSp": {
                    "regex": /^[a-zA-Z\ \']+$/,
                    "alertText": "* English letter case only "
                },
				"onlyLetterAccentSp":{
                    "regex": /^[a-z\u00C0-\u017F\ ]+$/i,
                    "alertText": "* English letter case only "
                },
                "onlyLetterNumber": {
                    "regex": /^[0-9a-zA-Z]+$/,
                    "alertText": "* Don't accept special characters "
                },
                // --- CUSTOM RULES -- Those are specific to the demos, they can be removed or changed to your likings
                "ajaxUserCall": {
                    "url": "ajaxValidateFieldUser",
                    // you may want to pass extra data on the ajax call
                    "extraData": "name=eric",
                    "alertText": "* This name has been used ",
                    "alertTextLoad": "* This name are being established for others to use, please wait a moment. "
                },
				"ajaxUserCallPhp": {
                    "url": "phpajax/ajaxValidateFieldUser.php",
                    // you may want to pass extra data on the ajax call
                    "extraData": "name=eric",
                    // if you provide an "alertTextOk", it will show as a green prompt when the field validates
                    "alertTextOk": "* This name can be used ",
                    "alertText": "* This name has been used ",
                    "alertTextLoad": "* This name are being established for others to use, please wait a moment. "
                },
                "ajaxNameCall": {
                    // remote json service location
                    "url": "ajaxValidateFieldName",
                    // error
                    "alertText": "* This name can be used ",
                    // if you provide an "alertTextOk", it will show as a green prompt when the field validates
                    "alertTextOk": "* This name has been used ",
                    // speaks by itself
                    "alertTextLoad": "* This name are being established for others to use, please wait a moment. "
                },
				 "ajaxNameCallPhp": {
	                    // remote json service location
	                    "url": "phpajax/ajaxValidateFieldName.php",
	                    // error
	                    "alertText": "* This name has been used ",
	                    // speaks by itself
	                    "alertTextLoad": "* This name are being established for others to use, please wait a moment. "
	                },
                "validate2fields": {
                    "alertText": "* Please enter HELLO"
                },
	            //tls warning:homegrown not fielded 
                "dateFormat":{
                    "regex": /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$|^(?:(?:(?:0?[13578]|1[02])(\/|-)31)|(?:(?:0?[1,3-9]|1[0-2])(\/|-)(?:29|30)))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^(?:(?:0?[1-9]|1[0-2])(\/|-)(?:0?[1-9]|1\d|2[0-8]))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^(0?2(\/|-)29)(\/|-)(?:(?:0[48]00|[13579][26]00|[2468][048]00)|(?:\d\d)?(?:0[48]|[2468][048]|[13579][26]))$/,
                    "alertText": "* Invalid date format "
                },
                //tls warning:homegrown not fielded 
				"dateTimeFormat": {
	                "regex": /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])\s+(1[012]|0?[1-9]){1}:(0?[1-5]|[0-6][0-9]){1}:(0?[0-6]|[0-6][0-9]){1}\s+(am|pm|AM|PM){1}$|^(?:(?:(?:0?[13578]|1[02])(\/|-)31)|(?:(?:0?[1,3-9]|1[0-2])(\/|-)(?:29|30)))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^((1[012]|0?[1-9]){1}\/(0?[1-9]|[12][0-9]|3[01]){1}\/\d{2,4}\s+(1[012]|0?[1-9]){1}:(0?[1-5]|[0-6][0-9]){1}:(0?[0-6]|[0-6][0-9]){1}\s+(am|pm|AM|PM){1})$/,
                    "alertText": "* Invalid date or time format ",
                    "alertText2": "The acceptable format： ",
                    "alertText3": "mm/dd/yyyy hh:mm:ss AM|PM or ", 
                    "alertText4": "yyyy-mm-dd hh:mm:ss AM|PM"
	            },
	            "loginIdUnicode": {
        			'url': base_root+"/front/checkUniqueAjax.do?r="+Math.random(), /* 验证程序地址 */
        			'extraDataDynamic': ['#loginCode'], /* 额外参数 */
    				'alertTextOk': 'This account can be used',
    				'alertText': 'This account has been used',
    				'alertTextLoad': 'Being validated '
            	},
	            "emailUnique": {
        			'url': base_root+"/front/checkUniqueAjax.do?r="+Math.random(), /* 验证程序地址 */
        			'extraDataDynamic': ['#email'], /* 额外参数 */
    				'alertTextOk': 'This email can be used',
    				'alertText': 'This email has been used',
    				'alertTextLoad': 'Being validated '
            	},
            	"mobileUnique": {
        			'url': base_root+"/front/checkUniqueAjax.do?r="+Math.random(), /* 验证程序地址 */
        			'extraDataDynamic': ['#regPhoneKey'], /* 额外参数 */
    				'alertTextOk': 'This mobile phone number can be used',
    				'alertText': 'This mobile phone number has been used',
    				'alertTextLoad': 'Being validated '
            	},
            	"loginCodeReg": {
                    "regex": /^\w+$/,
                    "alertText": "* Only fill in the letters, Numbers, underscores"
                },
                "pswRuleCustome":{
            		"regex":/(?=.*[a-z])(?=.*\d)(?=.*[#@!~%^&*_-])[a-z\d#@!~%^&*_-]{7,20}/i,
            		"alertText":"Password must contain: letters + numbers + special characters(example：@.&~%#_)!"
            	}
            };
            
        }
    };
    $.validationEngineLanguage.newLang();
})(jQuery);

})
