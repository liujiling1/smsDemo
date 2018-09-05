var BASE_URL = ys.path + "/js/plugins/webuploader";

+function($){
	
	"use strict";
	
	$.fn.ysUploader = function(options){ 
		var r;
		var args = Array.prototype.slice.call(arguments, 1);
		this.each(function(){
			var o = $(this).data("ysUploader");
			if(!o){
				o = new $.ysUploader();
				o.el = this;
				$.extend(o, options );  
				o.init();
				$(this).data("ysUploader",o);
			}
			if(o[options]){
				r = o[options].apply(o,args);
				return r;
			} 
		});
		return r;	
	}
	/**
	 * ysUploader has options 
	 */
	$.ysUploader = function(){
		this.resultData = [];
	};	
		
	$.ysUploader.prototype = {
		getUploadData : function(){
			var me = this;
			var rds = me.resultData;
			var ups = me.uploader.getFiles("complete");
			var srds = [];
			for(var i = 0;i < ups.length;i++){
				for(var j =0;j < rds.length;j++){
					if(ups[i].id == rds[j].file_id){
						srds.push(rds[j]);
						break;
					}
				}
				if(j >= rds.length){
					srds.push({
						path:ups[i].name
					});
				}
			}
			return srds;
		},
		noBlank : function(){
			var me = this;
			me.els.wrap.find(".queueList").addClass("no-null");
		},
		init : function(){
			var me = this;
			if ( !WebUploader.Uploader.support() ) {
		        alert( 'Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
		        throw new Error( 'WebUploader does not support the browser you are using.' );
		    }
			
			var me = this;

			if(!me.uploadConf) me.uploadConf = {};
			var conf = {};
			if(me.uploadConf.imageUpload && !me.uploadConf.accept ){
				me.uploadConf.accept = me.getImageAccept();
			}
			if(!me.uploadConf.pickLabel){
				me.uploadConf.pickLabel = "点击选择文件";
			}
			$.extend(conf,{
				pick: {
		            id: '#filePicker_' + me.el.id,
		            label: me.uploadConf.pickLabel,
		            multiple : me.uploadConf.multiple
		        },
				swf: BASE_URL + '/Uploader.swf',
		        disableGlobalDnd: true,
		        chunked: false,
		        fileNumLimit: 20,
		        resize: false,
		        server : me.getServer(),
		        fileSingleSizeLimit: 20 * 1024 * 1024    // 10 M
			},me.uploadConf);
			
			me.uploadConf = conf;
			if(me.noView && !me.uploadConf.multiple){
				me.uploadConf.auto = true;
				me.renderNoView();
			}else{
				me.render();
			}
			me.createUploader();
			if(me.disabled){
				me.disabledUpload();
			}
			me.initEvent();
			me.initData();
		},
		renderNoView : function(){
			var me = this;
			me.els = {};
			me.els.wrap = $(me.el);
			//加载容器
			$('<div class="col-sm-3" id="filePicker_'+me.el.id+'"></div><div class="col-sm-8" style="padding-top:10px" id="info_' + me.el.id + '"></div>').appendTo(me.els.wrap); 
		},
		disabledUpload : function(){
			var me = this;
			setTimeout(function(){
				$("#filePicker_" + me.el.id).find("input[type=file]").prop( "disabled", true );;
			},300)
		},
		render : function(){
			var me = this;
			me.els = {};
			me.els.wrap = $(me.el);
			//加载容器
			me.els.wrap.addClass("uploader");
			me.els.wrap.addClass(me.noView ? "no-view" : "view");
			$('<div class="queueList"><div class="placeholder"><div id="filePicker_'+me.el.id+'"></div></div></div>').appendTo(me.els.wrap); 
			
			var btnShow = "";
			if(!me.uploadConf.multiple){
				me.uploadConf.auto = true;
				btnShow = "style='display:none'";
			}
			
			var statusBarArray = ['<div class="statusBar" style="display:none;"><div class="progress"><span class="text">0%</span><span class="percentage"></span></div>'
				,'<div class="info"></div><div class="btns"><div id="filePicker2_'+me.el.id+'" class="filePicker2"></div><div class="uploadBtn" ',
				btnShow
				,'>开始上传</div></div></div>'];
			
			$(statusBarArray.join("")).appendTo(me.els.wrap);
            
	        // 图片容器
			me.els.queue = $('<ul class="filelist"></ul>').appendTo( me.els.wrap.find('.queueList') );
	        // 状态栏，包括进度和控制按钮
			me.els.statusBar = me.els.wrap.find('.statusBar');
	        // 文件总体选择信息。
			me.els.info = me.els.statusBar.find('.info');
	        // 上传按钮
			me.els.upload = me.els.wrap.find('.uploadBtn');
	        // 没选择文件之前的内容。
			me.els.placeHolder = me.els.wrap.find('.placeholder');
	        // 总体进度条
			me.els.progress = me.els.statusBar.find('.progress').hide();
			// 添加的文件数量
			me.els.fileCount = 0;
	        // 添加的文件总大小
			me.els.fileSize = 0;
	        // 优化retina, 在retina下这个值是2
			me.els.ratio = window.devicePixelRatio || 1;
	        // 缩略图大小
			me.els.thumbnailWidth = 500 * me.els.ratio;
			me.els.thumbnailHeight = 500 * me.els.ratio;
	        // 可能有pedding, ready, uploading, confirm, done.
			me.els.state = 'pedding';
	        // 所有文件的进度信息，key为file id
			me.els.percentages = {};
			me.els.supportTransition = (function(){
	            var s = document.createElement('p').style,
	                r = 'transition' in s ||
	                      'WebkitTransition' in s ||
	                      'MozTransition' in s ||
	                      'msTransition' in s ||
	                      'OTransition' in s;
	            s = null;
	            return r;
	        })();
			
			if(me.disabled){
				me.els.statusBar.hide();
			}
		},
		initNoViewData : function(){
			var me = this;
			$("#info_" + me.el.id).html("<a target='_blank' href='" +me.data[0]+"' >点击下载</a>");
		},
		initData : function(){
			var me = this;
			if(!me.data || me.data.length <= 0 || me.data[0].length <= 0) return;
			if(me.noView && !me.uploadConf.multiple){
				me.initNoViewData();
				return;
			}
			var files = [];
			for(var i = 0;i < me.data.length;i++){
		        files.push({
		           	name: me.data[i],
		            size: 0.01,
		            url: me.data[i],
		            type: '*/*',
		            lastModified: new Date().getTime(),
		            lastModifiedDate: new Date(),
		            webkitRelativePath: ""
		    	});
			}
			
			setTimeout(function(){
				me.uploader.addFiles(me.uploader.createFileObj(files));
				var ups = me.uploader.getFiles();
				for(var i = 0;i < ups.length;i++){
					ups[i].setStatus("complete");
				}
			},100);
		},
		createUploader : function(){
			var me = this; 
			me.uploader = WebUploader.create(me.uploadConf);
			
			if(me.uploadConf.multiple){
				me.uploader.addButton({
			        id: '#filePicker2_' + me.el.id,
			        label: '继续添加'
			    });
			}
		},
		getServer : function(){
			var me = this;
			var folder = me.uploadConf.folder;
			return me.uploadConf.imageUpload ? ys.path + '/oss/upload?folder='+folder : ys.path + '/oss/upload?folder='+folder;
		},
		initEvent : function(){
			var me = this;
			var uploader = me.uploader;
			uploader.onError = function( code ) {
		    	var msg = code;
		    	if(code == "F_EXCEED_SIZE"){
		    		msg = "文件大于" + (me.uploadConf.fileSingleSizeLimit/(1024 * 1024)).toFixed(3) + "M";
		    	}
		    	if(code == "Q_TYPE_DENIED"){
		    		msg = "支持的文件的类型有：[" + me.uploadConf.accept.extensions +"]";
		    	}
		    	if(code == "F_DUPLICATE"){
		    		msg = "该文件已上传，请选择其他文件";
		    	}
		    	if(code == "Q_EXCEED_NUM_LIMIT"){
		    		msg = "已超过文件上传上限，最多上传"+ me.uploadConf.fileNumLimit  +"个文件";
		    	}
		    	if(me.noView  && !me.uploadConf.multiple){
		    		$("#info_" + me.el.id).html("上传失败：" + msg);
		    	}else{
		    		if(!me.disabled){
		    			me.els.statusBar.show();
					}
			    	me.els.info.html( "上传失败：" + msg );
		    	}
		    };
		    
			uploader.on( 'uploadSuccess', function( file,res) {
				if(res.data && res.data.length > 0){
					var data = res.data[0];
					data.file_id = file.id;
					me.resultData.push(data);
					if(me.noView && !me.uploadConf.multiple){
						$("#info_" + me.el.id).html("上传成功：" + data.originalName);
					}
				}
				if(me.uploadSuccess) me.uploadSuccess(file,res);
			});
			
			if(me.noView  && !me.uploadConf.multiple) return;
			uploader.onUploadProgress = function( file, percentage ) {
		        var $li = $('#'+file.id),
		            $percent = $li.find('.progress span');

		        $percent.css( 'width', percentage * 100 + '%' );
		        me.els.percentages[ file.id ][ 1 ] = percentage;
		        me.updateTotalProgress();
		    };

		    uploader.onFileQueued = function( file ) {
		    	me.els.fileCount++;
		    	me.els.fileSize += file.size;

		        if ( me.els.fileCount === 1 ) {
		        	me.els.placeHolder.addClass( 'element-invisible' );
		        	if(!me.disabled){
		    			me.els.statusBar.show();
					}
		        }

		        me.addFile(file);
                
                if(me.uploadConf.multiple){
	                var $li = $('#'+file.id);
	                $li.append( '<span class="ready" title="多文件上传需要手动点击【开始上传】按钮，文件才能上传称成功"></span>' );
                }
		        
		        me.setState( 'ready' );
		        me.updateTotalProgress();
		    };

		    uploader.onFileDequeued = function( file ) {
		    	me.els.fileCount--;
		    	me.els.fileSize -= file.size;
		        if ( !me.els.fileCount ) {
		        	me.setState( 'pedding' );
		        }
		        me.removeFile( file );
		        me.updateTotalProgress();
		        if(me.deleteFile) me.deleteFile(file);
		    };

		    if(!me.uploadConf.auto){
		    	uploader.on( 'all', function( type ) {
			        var stats;
			        switch( type ) {
			            case 'uploadFinished':
			            	me.setState( 'confirm' );
			                break;
			            case 'startUpload':
			            	me.setState( 'uploading' );
			                break;
			            case 'stopUpload':
			            	me.setState( 'paused' );
			                break;
			        }
			    });
		    }
		    
		    me.els.upload.on('click', function() {
		        if ( $(this).hasClass( 'disabled' ) ) {
		            return false;
		        }

		        if (  me.els.state === 'ready' ) {
		            me.uploader.upload();
		        } else if ( me.els.state === 'paused' ) {
		        	me.uploader.upload();
		        } else if ( me.els.state === 'uploading' ) {
		        	me.uploader.stop();
		        }
		    });

		    me.els.info.on( 'click', '.retry', function() {
		    	me.uploader.retry();
		    } );

		    me.els.info.on( 'click', '.ignore', function() {
		        alert( 'todo' );
		    } );

		    me.els.upload.addClass( 'state-' + me.els.state );
		    me.updateTotalProgress();
		},
		getImageAccept : function(){
			return {
	            title: 'Images',
	            extensions: 'jpg,jpeg,png',
	            mimeTypes: 'image/jpeg,image/png,image/jpg'
	        }
		},
		addFile : function(file){
			var me = this;
			var $li = $( '<li id="' + file.id + '">' +
	                '<p class="title">' + file.name + '</p>' +
	                '<p class="imgWrap"></p>'+
	                '<p class="progress"><span></span></p>' +
	                '</li>' ),
	            $prgress = $li.find('p.progress span'),
	            $wrap = $li.find( 'p.imgWrap' ),
	            $info = $('<p class="error"></p>'),
	            showError = function( code ) {
	                switch( code ) {
	                    case 'exceed_size':
	                        text = '文件大小超出';
	                        break;

	                    case 'interrupt':
	                        text = '上传暂停';
	                        break;

	                    default:
	                        text = '上传失败，请重试';
	                        break;
	                }

	                $info.text( text ).appendTo( $li );
	            };
	        var $btns = null;    
	        if(!me.disabled){
	            $btns = $('<div class="file-panel">' +
	                '<span class="cancel">删除</span>' +
	                '</div>').appendTo( $li );
	        }
	        if ( file.getStatus() === 'invalid' ) {
	            showError( file.statusText );
	        } else {
	            // @todo lazyload
	            $wrap.text( '预览中' );
            	if(file.size == 0.01){
            		var img = "";
            		if(file.name && file.name.length){
        				if(file.name.lastIndexOf(".") == file.name.length - 1){
        					file.name = file.name.substring(0,file.name.length-1);
        				}
        			}
            		if(me.noView  && me.uploadConf.multiple){
            			img = $('<a target="_blank" href="' + file.name+'" >点击下载</a>');
            		}else{
            			img = $('<a class="fancybox" href="' + file.name+'"><img src="' + file.name+'" width="136" height="136" /></a>');
            		}
	                $wrap.empty().append( img );
	            }else{
		            me.uploader.makeThumb( file, function( error, src ) {
		                if ( error ) {
		                    $wrap.text( '不能预览' );
		                    return;
		                }
		                var img = $('<a class="fancybox" href="'+src+'"><img src="'+src+'" /></a>');
		                $wrap.empty().append( img );
		            }, me.els.thumbnailWidth, me.els.thumbnailHeight );
	            }
	            me.els.percentages[ file.id ] = [ file.size, 0 ];
	            file.rotation = 0;
	        }

	        file.on('statuschange', function( cur, prev ) {
	            if ( prev === 'progress' ) {
	            	$prgress.hide().width(0);
	            } else if ( prev === 'queued' ) {
	              //  $li.off( 'mouseenter mouseleave' );
	              //  $btns.remove();
	            }
	            // 成功
	            if ( cur === 'error' || cur === 'invalid' ) {
	                showError( file.statusText );
	                me.els.percentages[ file.id ][ 1 ] = 1;
	            } else if ( cur === 'interrupt' ) {
	                showError( 'interrupt' );
	            } else if ( cur === 'queued' ) {
	            	me.els.percentages[ file.id ][ 1 ] = 0;
	            } else if ( cur === 'progress' ) {
	            	$info.remove();
	            	$prgress.css('display', 'block');
	            } else if ( cur === 'complete' ) {
	                $li.append( '<span class="success"></span>' );
	            }
	            $li.removeClass( 'state-' + prev ).addClass( 'state-' + cur );
	        });
	        if($btns){
	        	$li.on( 'mouseenter', function() {
	        		$btns.stop().animate({height: 30});
	        	});
	        	$li.on( 'mouseleave', function() {
	        		$btns.stop().animate({height: 0});
	        	});
		        $btns.on( 'click', 'span', function() {
		            var index = $(this).index(),
		                deg;
		            switch ( index ) {
		                case 0:
		                	me.uploader.removeFile( file );
		                    return;
		                case 1:
		                    file.rotation += 90;
		                    break;
		                case 2:
		                    file.rotation -= 90;
		                    break;
		            }
		            if (me.els.supportTransition ) {
		                deg = 'rotate(' + file.rotation + 'deg)';
		                $wrap.css({
		                    '-webkit-transform': deg,
		                    '-mos-transform': deg,
		                    '-o-transform': deg,
		                    'transform': deg
		                });
		            } else {
		                $wrap.css( 'filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')');
		            }
		        });
	        }
	        $li.appendTo( me.els.queue );
		},
		removeFile : function(file){
			var me = this;
			var $li = $('#'+file.id);

	        delete me.els.percentages[ file.id ];
	        me.updateTotalProgress();
	        $li.off().find('.file-panel').off().end().remove();
		},
		updateTotalProgress : function(file){
			var me = this;
			var loaded = 0,
            total = 0,
            spans = me.els.progress.children(),
            percent;

	        $.each(me.els.percentages, function( k, v ) {
	            total += v[ 0 ];
	            loaded += v[ 0 ] * v[ 1 ];
	        } );
	
	        percent = total ? loaded / total : 0;
	
	        spans.eq( 0 ).text( Math.round( percent * 100 ) + '%' );
	        spans.eq( 1 ).css( 'width', Math.round( percent * 100 ) + '%' );
	        me.updateStatus();
		},
		updateStatus : function() {
			var me = this;
	        var text = '', stats;
	        if (me.els.state === 'ready' ) {
	            text = '选中' + me.els.fileCount + '张图片，共' +
	                    WebUploader.formatSize( me.els.fileSize ) + '。';
	        } else if (me.els.state === 'confirm' ) {
	            stats = me.uploader.getStats();
	            if ( stats.uploadFailNum ) {
	                text = '已成功上传' + stats.successNum+ '张照片至XX相册，'+
	                    stats.uploadFailNum + '张照片上传失败，<a class="retry" href="#">重新上传</a>失败图片或<a class="ignore" href="#">忽略</a>'
	            }
	        } else {
	        	if(me.els.fileCount==0) me.els.fileSize=0;
	            stats = me.uploader.getStats();
	            text = '共' + me.els.fileCount + '张（' +
	                    WebUploader.formatSize(me.els.fileSize )  +
	                    '）';
	        }
	        me.els.info.html( text );
	    },
	    setState : function( val ) {
	    	var me = this;
	        var file, stats;

	        if ( val === me.els.state ) {
	            return;
	        }

	        me.els.upload.removeClass( 'state-' + me.els.state );
	        me.els.upload.addClass( 'state-' + val );
	        me.els.state = val;

	        switch ( me.els.state ) {
	            case 'pedding':
	            	me.els.placeHolder.removeClass( 'element-invisible' );
	            	me.els.queue.parent().removeClass('filled');
	            	me.els.queue.hide();
	                me.els.statusBar.hide();
	                me.uploader.refresh();
	                break;

	            case 'ready':
	            	me.els.placeHolder.addClass( 'element-invisible' );
	                $( '#filePicker2_' + me.el.id ).removeClass( 'element-invisible');
	                me.els.queue.parent().addClass('filled');
	                me.els.queue.parent().removeClass('no-null');
	                me.els.queue.show();
	                me.els.statusBar.removeClass('element-invisible');
	                me.uploader.refresh();
	                break;

	            case 'uploading':
	                //$( '#filePicker2_' + me.el.id ).addClass( 'element-invisible' );
	                me.els.progress.show();
	                me.els.upload.text( '暂停上传' );
	                break;

	            case 'paused':
	            	me.els.progress.show();
	            	me.els.upload.text( '继续上传' );
	                break;

	            case 'confirm':
	            	me.els.progress.hide();
	            	me.els.upload.text( '开始上传' );

	                stats = me.uploader.getStats();
	                if ( stats.successNum && !stats.uploadFailNum ) {
	                    me.setState( 'finish' );
	                    return;
	                }
	                break;
	            case 'finish':
	                stats = me.uploader.getStats();
	                if ( stats.successNum ) {
	                } else {
	                    // 没有成功的图片，重设
	                	me.els.state = 'done';
	                    location.reload();
	                }
	                break;
	        }
	        me.updateStatus();
	    }
	}
}(jQuery);