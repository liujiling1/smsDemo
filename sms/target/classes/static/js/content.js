// var $parentNode = window.parent.document;

// function $childNode(name) {
//     return window.frames[name]
// }

// // tooltips
// $('.tooltip-demo').tooltip({
//     selector: "[data-toggle=tooltip]",
//     container: "body"
// });

// // 使用animation.css修改Bootstrap Modal
// $('.modal').appendTo("body");

// $("[data-toggle=popover]").popover();


//判断当前页面是否在iframe中
if (top == this) {
    var gohome = '<div class="gohome"><a class="animated bounceInUp" href="/user/center" title="返回首页"><i class="fa fa-home"></i></a></div>';
    $('body').append(gohome);
}

//animation.css
function animationHover(element, animation) {
    element = $(element);
    element.hover(
        function () {
            element.addClass('animated ' + animation);
        },
        function () {
            //动画完成之前移除class
            window.setTimeout(function () {
                element.removeClass('animated ' + animation);
            }, 2000);
        });
}

//拖动面板
function WinMove() {
    var element = "[class*=col]";
    var handle = ".ibox-title";
    var connect = "[class*=col]";
    $(element).sortable({
            handle: handle,
            connectWith: connect,
            tolerance: 'pointer',
            forcePlaceholderSize: true,
            opacity: 0.8,
        })
        .disableSelection();
};


function ys_prompt(type,href,msg){
	var d = type > 0;
	toastr.options = { 
	  "progressBar": true,
	  "positionClass": "toast-top-right",
	  "showDuration": "400",
	  "hideDuration": "500",
	  "timeOut": "3000",
	  "extendedTimeOut": "3000",
	  "showEasing": "swing",
	  "hideEasing": "linear",
	  "showMethod": "fadeIn",
	  "hideMethod": "fadeOut",
	  onclick : function(){
		d = false; 
	  },
	  onHidden : function(){
		if(d){
			window.location.href = href;
		}
		d = true; 
	  }
	};
	
	if(type > 0){
		toastr['success']("3秒后自动返回列表，点击取消返回。", "保存成功");
	}else{
		toastr['error'](msg, "保存失败");
	}
}

$(function(){
	$.widget( "ui.timespinner", $.ui.spinner, {
		options: {
			// seconds
			step: 60 * 1000,
			// hours
			page: 60
		},

		_parse: function( value ) {
			if ( typeof value === "string" ) {
				// already a timestamp
				if ( Number( value ) == value ) {
					return Number( value );
				}
				return +Globalize.parseDate( value );
			}
			return value;
		},

		_format: function( value ) {
			return Globalize.format( new Date(value), "t" );
		}
	});
});

//验证
jQuery.validator.addMethod("phone", function (value, element) {
    //var phone = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9])|(17[0-9]))\d{8}$/;
	var phone = /^1[3|4|5|7|8|9][0-9]\d{8}$/;
    return this.optional(element) || (phone.test(value));
}, "请填写正确手机号码");

jQuery.validator.addMethod("vehicle", function (value, element) {
    var re = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/;
    return this.optional(element) || (re.test(value));
}, "请填写正确车牌号");
/**正则验证**/
jQuery.validator.addMethod("regular", function (value, element,param) {
    var re = new RegExp(param);
    return this.optional(element) || (re.test(value));
}, "请填写正确数据");
/**数字范围验证**/
jQuery.validator.addMethod("rangeEx", function (value, element,param) {
	param = eval('('+param+')');
	$.validator.messages.rangeEx = "请输入"+param[0]+"-"+param[1]+"之间的数";
	if(param[0]>value || param[1]<value){
		return false;
	}
	//小数点最多位数
	var d = Math.max((param[0].toString().split("\.").length>1?param[0].toString().split("\.")[1].length:0),
	(param[1].toString().split("\.").length>1?param[1].toString().split("\.")[1].length:0),1);
	var re = new RegExp("^\\d+(\\.\\d{1,"+d+"})?$")
    return this.optional(element) || (re.test(value.toString()));
}, "请填写正确数据");
/**身份证号码校验**/
jQuery.validator.addMethod("cardNo", function (value, element,param) {
	var re = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    return this.optional(element) || (re.test(value));
}, "请填写正确身份证号");

jQuery.validator.addMethod("error", function (value, element,param) {
	$.validator.messages.error = $(element).attr("error")
    return this.optional(element) || false;
}, "请填写正确数据");

jQuery.validator.addMethod("telephoneNo", function (value, element,param) {
	var tno = /^\d{11,12}$/;
    return this.optional(element) || (tno.test(value));
}, "请填写正确电话号码");

$(function(){
	$('.fancybox').fancybox({
		type : 'image',
		 helpers : {
		        overlay : {
		            locked : false // try changing to true and scrolling around the page
		        }
		    }
    });
	
	$("body").click(function(){
		if(window.parent._iframe_click && !window._iframe_click){
			window.parent._iframe_click();
		}
	})
})

$.extend({
    cookie: function(name, value, options) {
        if (typeof value != 'undefined') { // name and value given, set cookie
            options = options || {};
            if (value === null) {
                value = '';
                options.expires = -1;
            }
            var expires = '';
            if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
                var date;
                if (typeof options.expires == 'number') {
                    date = new Date();
                    date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
                } else {
                    date = options.expires;
                }
                expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
            }
            var path = options.path ? '; path=' + options.path : '';
            var domain = options.domain ? '; domain=' + options.domain : '';
            var secure = options.secure ? '; secure' : '';
            document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
        } else { // only name given, get cookie
            var cookieValue = null;
            if (document.cookie && document.cookie != '') {
                var cookies = document.cookie.split(';');
                for (var i = 0; i < cookies.length; i++) {
                    var cookie = jQuery.trim(cookies[i]);
                    // Does this cookie string begin with the name we want?
                    if (cookie.substring(0, name.length + 1) == (name + '=')) {
                        cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                        break;
                    }
                }
            }
            return cookieValue;
        }
    }
 
}); 