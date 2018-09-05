/**
 * jquery本地方法集合
 */
$.extend({
	/**
	 * ajax扩展，新增用户失效验证并自动重登录
	 */
	zAjax : function(opts){
		var defaultOpts = {
			type:"POST",
			contentType : "application/json; charset=utf-8",
			dataType : "json"
		}	
		var zopts = $.extend({},defaultOpts,opts);
		var zsuccess = function(r){
			if(r.code == zjn.authc.relogin.code){
				zjn.authc.relogin.detail(zopts);
				return;
			}
			opts.success.apply(this,arguments);;
		}
		zopts.success = zsuccess;
		$.ajax(zopts);
	},
	/**
	 * cookie操作方法
	 * 
	 */
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
/**
 * 格式化form中的数据为json
 */
$.fn.serializeFormJson = function() {
	var arrayData, objectData;
	arrayData = this.serializeArray();
	objectData = {};
	$.each(arrayData, function() {
	    var value;
	    if (this.value != null) {
	      value = this.value;
	    } else {
	      value = '';
	    }
	    if (objectData[this.name] != null) {
	      if (!objectData[this.name].push) {
	        objectData[this.name] = [objectData[this.name]];
	      }
	      objectData[this.name].push(value);
	    } else {
	      objectData[this.name] = value;
	    }
	});
	return objectData;
};

/**
 * 刷新数据事件检测
 */
$.fn.reloadData = function(opts) {
	var startY = 0,endY = 0,s = false;
	$(this).bind({
        "touchstart" : function(e){
            var touch = e.originalEvent.targetTouches[0];
            if(!touch) return;
            s = false;
            startY = touch.pageY;
        },
        "touchmove" : function(e){
            var touch = e.originalEvent.targetTouches[0];
            if(!touch) return;
            s = true;
            endY = touch.pageY;
        },
        "touchend" : function(e){
            if(startY - endY > 100 && s){
               opts.callback.apply();
            }
        }
    });
};

/**
 * 滑动展示操作信息
 */
$.fn.touchShowOpts = function(opts) {
	var startX = 0,endX = 0,s = false;
	$(this).bind({
        "touchstart" : function(e){
            var touch = e.originalEvent.targetTouches[0];
            if(!touch) return;
            s = false;
            startX = touch.pageX;
        },
        "touchmove" : function(e){
            var touch = e.originalEvent.targetTouches[0];
            if(!touch) return;
            s = true;
            endX = touch.pageX;
        },
        "touchend" : function(e){
            if(startX - endX > 50 && s){
            	$(this).scrollLeft(3000);
            	if(opts && opts.rightHandler) opts.rightHandler.apply(this);
            }
            if(endX - startX > 50 && s){
            	$(this).scrollLeft(0);
            	if(opts && opts.leftHandler) opts.leftHandler.apply(this);
            }
        }
    });
};

/**
 * tab标签
 */
$.fn.tab = function(opts) {
	var ctab = $(this).find(".selected");
	$(this).find("span").click(function(){
		var ctabCon = ctab.attr("tabCon");
		var tabCon = $(this).attr("tabCon");
		if(ctabCon == tabCon) return;
		ctab.removeClass("selected");
		$("#"+ ctabCon).hide();
		
		$(this).addClass("selected");
		$("#"+ tabCon).show();
		
		if(opts.change) opts.change.apply(this,ctab);
		ctab = $(this);
	});
};

/**
 * 下拉选择框
 */
$.fn.pullSelect = function(opts) {
	$(this).click(function(){
		var ps = $("<div class='zjn_pull_select'></div>");
		var con = ["<div class='pull_select'>"];
		$.each(opts.dataItems,function(index,obj){
			con.push('<div class="select_item"><div item_id="'+ obj.id +'" class="select_item_con">'+ obj.name +'</div></div>');
		});
		con.push("</div>");
		ps.append(con.join(""));
		ps.append('<div class="zjn_win_btns" style="width: 100%;float:left;">'+
						'<div class="zjn_win_btn win_btn_cancel">取消</div>'+
						'<div class="zjn_win_btn win_btn_confirm">确认</div>'+
					'</div>');
		
		$("body").append(ps);
		$("body").append('<div class="win_mask" style="height:'+zjn.message.getMaskHeight()+'px"></div>');
		ps.find(".select_item_con").click(function(){
			var itemCon = ps.find(".select_item_con");
			itemCon.removeClass("selected");
			$(this).addClass("selected");
			ps.data("curSelectItem",$(this));
		});
		
		ps.find(".win_btn_confirm").click(function(){
			var citem = ps.data("curSelectItem");
			if(citem){
				opts.callback(ps.data("curSelectItem"));
			}
			ps.remove();
			$(".win_mask").remove();
			
		});
		
		ps.find(".win_btn_cancel").click(function(){
			ps.remove();
			$(".win_mask").remove();
		});
		
		$(".win_mask").click(function(){
			ps.remove();
			$(".win_mask").remove();
		});
	})
};

/**
 * 数字化操作
 */
$.fn.numberSel = function(){
	$(this).find(".sub").click(function(){
		var input = $(this).parent().find("input");
		var v = parseInt(input.val(),10);
		if(v == 0) return;
		input.val(v - 1);
	});

	$(this).find(".add").click(function(){
		var input = $(this).parent().find("input");
		var v = parseInt(input.val(),10);
		input.val(v + 1);
	});
}

zjn = {
	FILE_PATH : "http://new.ceceplube.com/file/",
	authc : {
		/**
		 * 本地是否存在username
		 */
		hasUsername : function(){
			var username = $.cookie('username');
			return username && username.length > 0;
		},
		/**
		 * 用户重登陆
		 */
		relogin : {
			code : 1401,
			detail : function(zopts){
				if(!zjn.authc.hasUsername()){
					zjn.authc.reAuthc.detail();
					return;
				}
				var data = {
					username: $.cookie('username')
				}
				$.ajax({
					type:"POST",
					url:"/user/wxlogin",
					contentType : "application/json; charset=utf-8",
					dataType : "json",
					data:JSON.stringify(data),
					success:function(result) {
						if(result.code == 1200){
							$.ajax(zopts);
						}else{
							zjn.authc.reAuthc.detail();
						}
					}
				});
			}
		},
		/**
		 * 重新到微信端登陆
		 */
		reAuthc : {
			//code : 1100,
			detail : function(){
				$.ajax({
					type:"POST",
					url:"/user/getredirect",
					contentType : "application/json; charset=utf-8",
					dataType : "json",
					data:"{}",
					success:function(result) {
						if(result.code == 1200){
							window.location.href=result.data[0].redirect;
						}
					}
				});
			}
		}
	},
	/**
	 * 获取请求参数
	 */
	getReqParams : function(){
		var paramJson = {};
		var href = window.location.href;
		var m = href.indexOf("?");
		if(m <= 0) return paramJson;
		href = href.substring(m + 1,href.length);
		var params = href.split("&");
		for(var i = 0;i < params.length;i++){
			var kv = params[i].split("=");
			paramJson[kv[0]] = kv[1];
		}
		return paramJson;
	},
	/**
	 * 弹出提示信息
	 */
	message : {
		confirm : function(title,message,callback){
			var winConfirmEl = zjn.message.winConfirmEl;
			if(!winConfirmEl){
				var top = zjn.message.getTop();
				var w = $(window).width() - 40;
				var content = ['<div class="zjn_win" style="top:',top,'px;width:',w,'px">',
					'<div class="zjn_win_con">',
						'<div class="zjn_win_title">',title,'</div>',
						'<div class="zjn_win_message">',message,'</div>',
					'</div>',
					'<div class="zjn_win_btns">',
						'<div class="zjn_win_btn win_btn_cancel">取消</div>',
						'<div class="zjn_win_btn win_btn_confirm">确认</div>',
					'</div>',
				'</div>','<div class="win_mask" style="height:',zjn.message.getMaskHeight(),'px"></div>'];
				
				zjn.message.winConfirmEl = $(content.join(""));
				$("body").append(zjn.message.winConfirmEl);
				
				zjn.message.winConfirmEl.find(".win_btn_cancel").click(function(){
					zjn.message.winConfirmEl.hide();
					zjn.message.winConfirmEl.callback(false);
				});
				zjn.message.winConfirmEl.find(".win_btn_confirm").click(function(){
					zjn.message.winConfirmEl.hide();
					zjn.message.winConfirmEl.callback(true);
				});
				
				$("body").find(".win_mask").click(function(){
					zjn.message.winConfirmEl.hide();
				});
			}else{
				winConfirmEl.find(".zjn_win_title").html(title);
				winConfirmEl.find(".zjn_win_message").html(message);
				winConfirmEl.show();
			}
			zjn.message.winConfirmEl.callback = callback;
		},
		getTop : function(){
			return ($(window).height() - 220) * 0.4;
		},
		getMaskHeight : function(){
			var wh = $(window).height();
			var bh = $("body").height();
			return wh > bh ? wh : bh;
		},
		info : function(message){
			var winInfoEl = zjn.message.winInfoEl;
			if(!winInfoEl){
				var content = ['<div class="msg_info">',
					'<div>',message,'</div>',
				'</div>'];
				zjn.message.winInfoEl = $(content.join(""));
				$("body").append(zjn.message.winInfoEl);
			}else{
				winInfoEl.find("div").html(message);
			}
			zjn.message.winInfoEl.show();
			setTimeout(function(){
				zjn.message.winInfoEl.hide();
			},3000);
		},
		success : function(message){
			zjn.message.tip("success",message);
		},
		error : function(message){
			zjn.message.tip("error",message);
		},
		tip : function(type,message){
			var winTipEl = zjn.message.winTipEl;
			if(!winTipEl){
				zjn.message.winTipEl = $(zjn.message.getSEContent(type,message));
				$("body").append(zjn.message.winTipEl);
				
				$("body").find(".win_mask").click(function(){
					zjn.message.winTipEl.hide();
				});
			}else{
				winTipEl.find(".zjn_win_icon img").attr("src","/images/" + type + ".png");
				winTipEl.find(".zjn_win_message").html(message);
			}
			zjn.message.winTipEl.show();
			setTimeout(function(){
				zjn.message.winTipEl.hide();
			},3000);
		},
		getSEContent : function(type,message){
			var top = zjn.message.getTop();
			var w = $(window).width() - 40;
			var content = ['<div class="zjn_win zjn_tip_win" style="top:',top,'px;width:',w,'px">',
				'<div class="zjn_win_con">',
					'<div class="zjn_win_icon"><img src="/images/',type,'.png" /></div>',
					'<div class="zjn_win_message">',message,'</div>',
				'</div>',
			'</div>','<div class="win_mask" style="height:',zjn.message.getMaskHeight(),'px"></div>'];
			return content.join("");
		},
		/**
		 * 弹出框
		 * zjn.message.dialog({
			title : "测试",
			content : "<div>ce shi xin xi<br /><br /><br /><br /><br /><br /><br /><br /><input class='ttt' /></div>",
			cancel : {
				handler : function(){
					console.log('tets');
				}
			},
			confirm : {
				handler : function(){
					var v = $(this).find(".ttt").val();
					console.log(v);
				}
			}
		})
		 */
		dialog : function(opts){
			$(".zjn_win_dialog").remove();
			var top = zjn.message.getTop();
			var w = $(window).width() - 40;
			var content = ['<div class="zjn_win_dialog"><div class="zjn_win zjn_dialog" style="top:',top,'px;width:',w,'px">',
				'<div class="zjn_win_head">',
					'<div class="zjn_win_head_title">',opts.title,'</div>',
				'</div>',
				'<div class="zjn_win_con">',
					opts.content,
				'</div>',
				'<div class="zjn_win_btns">',
					'<div class="zjn_win_btn win_btn_cancel">取消</div>',
					'<div class="zjn_win_btn win_btn_confirm">确认</div>',
				'</div>',
			'</div>','<div class="win_mask" style="height:',zjn.message.getMaskHeight(),'px"></div></div>'];
			
			var dialogEl = $(content.join(""));
			$("body").append(dialogEl);
			
			dialogEl.find(".win_btn_cancel").click(function(){
				dialogEl.hide();
				if(opts.cancel && opts.cancel.handler) opts.cancel.handler.apply(dialogEl);
			});
			dialogEl.find(".win_btn_confirm").click(function(){
				var v = true;
				if(opts.confirm && opts.confirm.beforeHandler){
					v = opts.confirm.beforeHandler.apply(dialogEl);
				}
				if(v === false) return;
				dialogEl.hide();
				if(opts.confirm && opts.confirm.handler) opts.confirm.handler.apply(dialogEl);
			});
			
			dialogEl.find(".win_mask").click(function(){
				dialogEl.hide();
				if(opts.cancel && opts.cancel.handler) opts.cancel.handler.apply(dialogEl);
			});
		}
	}, 
	/**
	 * 随机数生成等数学方法
	 */
	math : {
		validCode  : function(){
			var num=""; 
			for(var i=0;i<6;i++){ 
				num+=Math.floor(Math.random()*10); 
			} 
			return num;
		}
	},
	/**
	 * form 操作方法
	 */
	form : {
		checkbox : {
			init : function(){
				$(".s_checkbox").click(function(){
					var isALL = function(){
						var c = $(".s_checkbox");
						$("#all_checkbox").attr("src","/images/checked.png");
						for(var i=0;i<c.length;i++){
							if($(c[i]).attr("src")=='/images/nochecked.png'){
								$("#all_checkbox").attr("src","/images/nochecked.png");
								break;
							}
						}
					}
					if($(this).attr("disable")){
						return;
					}
					var src = $(this).attr("src");
					if(src == '/images/nochecked.png'){
						$(this).attr("src","/images/checked.png");
					}else{
						$(this).attr("src","/images/nochecked.png");
					}
					isALL();
				});
				
				$("#all_checkbox").click(function(){
					var src = $(this).attr("src");
					if(src == '/images/nochecked.png'){
						$(this).attr("src","/images/checked.png");
						$(".s_checkbox").attr("src","/images/checked.png");
					}else{
						$(this).attr("src","/images/nochecked.png");
						$(".s_checkbox").attr("src","/images/nochecked.png");
					}
				});
			},
			getCheckedValues : function(){
				var els = [];
				$(".s_checkbox").each(function(){
					var src = $(this).attr("src");
					if(src == '/images/checked.png'){
						els.push($(this));
					}
				});
				return els;
			},
			destory : function(){
				$(".s_checkbox").attr("src","/images/nochecked.png");
				$("#all_checkbox").attr("src","/images/nochecked.png");
			}
		},
		valid : function(){
			var valid = true;
			$(".valid").each(function(){
				var guideStr = $(this).attr("guides");
				var vname = $(this).attr("vname");
				var val = $(this).val();
				if(guideStr && guideStr.length > 0){
					var validGuides = zjn.form.validGuides;
					var guides = guideStr.split(" ");
					for(var i = 0; i < guides.length;i++){
						for(var j = 0; j < validGuides.length;j++){
							if(validGuides[j].name == guides[i]){
								if(!validGuides[j].guide(val,$(this))){
									zjn.message.info(vname + "" + validGuides[j].message);
									return valid = false;
								}
							}
						}
					}
				}
			});
			return valid;
		},
		validGuides : [{
			name : "require",
			message : "不能为空",
			guide : function(v){
				return v && v.trim().length > 0;
			}
		},{
			name : "phone",
			message : "请填写正确手机号码",
			guide : function(v){
				var phone = /^1[3|4|5|7|8|9][0-9]\d{8}$/;
			    return phone.test(v);
			}
		},{
			name : "validCode",
			message : "请填写正确验证码",
			guide : function(v){
				var validCode = /^\d{6,6}$/;
			    return validCode.test(v);
			}
		},{
			name : "carNo",
			message : "请填写正确车牌号码",
			guide : function(v){
				var carNo = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/;
                var carNo1 = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{5}[A-Z0-9挂学警港澳]{1}$/;
			    return carNo.test(v) || carNo1.test(v);
			}
		},{
			name : "dateGtCurDate",
			message : "时间必须大于等于今天",
			guide : function(v){
				if(v.indexOf(".") > 0){
					v = v.replace(".","-").replace(".","-");
				}
				var today = moment(moment(new Date()).format("YYYY-MM-DD"));
			    return today.isBefore(moment(v)) || today.isSame(moment(v));
			}
		},{
			name : "dateGtCompare",
			message : "必须大于等于开始时间",
			guide : function(v,obj){
				if(v.indexOf(".") > 0){
					v = v.replace(".","-").replace(".","-");
				}
				var gtCompare = $("#" + obj.attr("gtCompare"));
				var v1 = gtCompare.val();
				if(v1.indexOf(".") > 0){
					v1 = v1.replace(".","-").replace(".","-");
				}
			    return moment(v).isAfter(v1)  || moment(v).isSame(v1);
			}
		},{
			name : "rangeEx",
			message : "请人输入正确的金额",
			guide : function(value,obj){
				var param = obj.attr("range").split(",");
				this.message = "可输入"+param[0]+"-"+param[1]+"之间的金额";
				param[0]=parseFloat(param[0]);
				param[1]=parseFloat(param[1]);
				if(param[0]>value || param[1]<value){
					return false;
				}
				//小数点最多位数
				var d = Math.max((param[0].toString().split("\.").length>1?param[0].toString().split("\.")[1].length:0),
				(param[1].toString().split("\.").length>1?param[1].toString().split("\.")[1].length:0),1);
				var re = new RegExp("^\\d+(\\.\\d{1,"+d+"})?$")
			    return re.test(value.toString());
			}
		}]
	},
	/**
	 * 文件上传
	 */
	upload : {
		wxBase64Img : function(callback){
			wx.chooseImage({
		        count: 1, // 默认9
		        sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有 ['original', 'compressed']
		        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有 ['album', 'camera'],
		        success: function (res) {
		            var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片

					wx.uploadImage({
						localId: localIds[0], // 需要上传的图片的本地ID，由chooseImage接口获得
						isShowProgressTips: 1, // 默认为1，显示进度提示
						success: function (res) {
							var serverId = res.serverId; // 返回图片的服务器端ID
								var data = {
									img :　serverId,
									folder: "activity"
								};
							     $.zAjax({
							           type:"POST",
							           url:"/fileUpload/base64Img",
									contentType : "application/json; charset=utf-8",
							           dataType : "json",
							           data: JSON.stringify(data),
							           success:function(result) {
							           	if(result.code == 1200){
							           		callback(result);
							           	}
							           } ,
							           error: function (rest) {

										}
								});
						}
					});
                    // wx.getLocalImgData({
			        //     localId: localIds[0], // 图片的localID
			        //     success: function (res) {
			        //         var localData = res.localData; // localData是图片的base64数据，可以用img标签显示
                    //
						// 	//"{\"img\":\""+ "1234" +"\",\"folder\":\"activity\"}"
						// 	// var newBase64 = base64.replace(/\+/g, "%2B");
						// 	//localData.replace(/\+/g, "%2B")
						// 	var data = {
						// 		img :　"13434",
						// 		folder: "activity"
						// 	};
						// 	var imgdata = JSON.stringify(data);
						// 	alert(imgdata);
			        //         $.zAjax({
					 //            type:"POST",
					 //            url:"/fileUpload/base64Img",
						// 		contentType : "application/json; charset=utf-8",
					 //            dataType : "json",
					 //            data: imgdata,
					 //            success:function(result) {
					 //            	if(result.code == 1200){
					 //            		callback(result);
					 //            	}
					 //            	alert("sucess fail");
					 //            } ,
					 //            error: function (rest) {
						// 			alert(JSON.stringify(rest));
						// 			alert("error fail");
						// 		}
						// 	});
			        //     }
			        // });
		        }
		    });
		}
	},

	dateFormate :{
		formate : function (now,mask) {
            var d = now;
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

            return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0)
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
        }
	},

	returnType:function (key) {
		return itemType[key];
    },
    updateUrl:function (url,key) {

        var key= (key || 't') +'=';  //默认是"t"
        var reg=new RegExp(key+'\\d+');  //正则：t=1472286066028
        var timestamp=+new Date();
        if(url.indexOf(key)>-1){ //有时间戳，直接更新
            return url.replace(reg,key+timestamp);
        }else{  //没有时间戳，加上时间戳
            if(url.indexOf('\?')>-1){
                var urlArr=url.split('\?');
                if(urlArr[1]){
                    return urlArr[0]+'?'+key+timestamp+'&'+urlArr[1];
                }else{
                    return urlArr[0]+'?'+key+timestamp;
                }
            }else{
                if(url.indexOf('#')>-1){
                    return url.split('#')[0]+'?'+key+timestamp+location.hash;
                }else{
                    return url+'?'+key+timestamp;
                }
            }
        }
    }

}
//页面加载执行方法
$(function() {
	$(".upperCase").on("blur",function(){
		$(this).val($(this).val().toUpperCase());
	});
});

//1洗车，2一般维修，3普通保养，4事故，5免费检测，6美容，7其它',
var itemType = {1:"洗车",2:"一般维修",3:"普通保养",4:"事故",5:"免费检测",6:"美容"};

/**
 * 常用常量
 */
Constant={

		/**
		 * 维修类型
		 */
		//1洗车，2一般维修，3普通保养，4事故，5免费检测，6美容，7其它',
		itemType:{"1":"洗车","2":"一般维修","3":"普通保养","4":"事故","5":"免费检测","6":"美容"},

		perType:{"1":"洗车","2":"普通保养"},

		//支付类型：1支付宝，2微信，3现金，4刷卡，5套餐；
		costType:{"1":"支付宝","2":"微信","3":"现金","4":"刷卡","5":"套餐"}
}
