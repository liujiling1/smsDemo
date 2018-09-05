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

+function($){
	
	"use strict";
	
	$.fn.ystable = function(options){ 
		var r = this;
		var args = Array.prototype.slice.call(arguments, 1);
		this.each(function(){
			var o = options.destroy?null:$(this).data("ystable");
			if(!o){
				o = new $.ystable();
				o.el = this;
				$.extend(o, options );  
				o.init(options);
				$(this).data("ystable",o);
			}
			if(o[options]){
				r = o[options].apply(o,args);
				return r;
			} 
		});
		return r;	
	}
	/**
	 * kstable has options 
	 * Data table options
	 * tableOpts : [{
	 * 	divider:  true "是否分割符"
	 *  optName : "操作名称"
	 *  optFun : 操作函数
	 * }]
	 */
	$.ystable = function(){
		this.pix_name = "dt_optBtn";
		this.defaultTableConf = {
			searching: false,
			ordering:false,
			autoWidth: false,
			lengthChange: false,
			"createdRow": function ( row, data, index ) {
				$(row).find("[limit]").parent().limit();
			}
		}
	};	
		
	$.ystable.prototype = {
		getDataTable : function(){
			return this.dataTable;
		},
		getColumnDefs : function(){
			var me = this;
			if(me.tableOpts == null) return me.columnDefs;
			var columnDefs = me.columnDefs;
			if(columnDefs == null){
				columnDefs = [];
			}
			
            columnDefs[columnDefs.length] = {
        		"render": function ( data, type, row ) {
        			if(me.tableOpts.length == 1){
        				var showName = me.tableOpts[0].optName;
    	            	if(me.tableOpts[0].render){
    	            		var r = me.tableOpts[0].render(row);
    	            		if(r === false) return "";
    	            		if(r) showName = r;
    	            	}
        				var btn = '<button type="button" class="btn btn-primary btn-night-blue btn-xs single-opt" >'+showName+'</button>';
        				return btn;
        			}
        			
        			var optBtns = ['<div class="btn-group"><button data-toggle="dropdown" class="btn btn-primary  btn-night-blue btn-xs  dropdown-toggle">操作 <span class="caret"></span>',
    	            '</button>','<ul class="dropdown-menu">'];
        			var hasMenu = false,j=0;
    	            for(var i=0;i<me.tableOpts.length;i++){
    	            	var showName = me.tableOpts[i].optName;
    	            	if(me.tableOpts[i].render){
    	            		var r = me.tableOpts[i].render(row);
    	            		if(r === false) continue;
    	            		showName = r;
    	            	}
    	            	hasMenu = true;
    	            	if(j > 0){
    	            		optBtns.push( '<li class="divider"></li>');
    	            	}
    	            	j++;
    	            	optBtns.push('<li><a optIdx="');
    	            	optBtns.push(me.pix_name + i);
    	            	optBtns.push('" href="javascript:void(0)">');
    	            	optBtns.push(showName);
    	            	optBtns.push('</a></li>');
    	            }
    	            optBtns.push( '</ul></div>');
        			return hasMenu ? optBtns.join("") : "";    
        		},
                "targets": me.columns.length
            }
            return columnDefs;
		},
		init : function(conf){
			var me = this;
			var opts = {};
			$.extend(opts,me.defaultTableConf,conf);
			opts.columnDefs = me.getColumnDefs();
			
			me.initColumns(opts.columns);
			me.initSearch(opts);
			me.initExport(opts);
			me.dataTable = $(me.el).DataTable(opts);  
            
//			$(me.el).on('click', 'tr button.single-opt', function () {
//				var data = me.dataTable.rows('.selected').data();
//				me.tableOpts[0].optFun.call(me,data[0], $(this));
//			});
			
			$(me.el).on('click', 'ul.dropdown-menu a', function () {
				var idx = $(this).attr("optIdx").replace(me.pix_name,"");
				var data = me.dataTable.rows('.selected').data();
				me.tableOpts[idx].optFun.call(me,data[0], $(this));
			});
			
          	$(me.el).on('click', 'tr', function (evt) {
        	    if(!$(this).hasClass('selected')){
        	    	me.dataTable.$('tr.selected').removeClass('selected');
        	    }
        	    $(this).addClass('selected');
        	    if(me.rowClick){
        	    	var data = me.dataTable.rows('.selected').data();
        	    	me.rowClick.call(me,data[0], $(this));
        	    }
        	    var target = $(evt.target);
        	    if(target.hasClass("single-opt")){
        	    	var data = me.dataTable.rows('.selected').data();
    				me.tableOpts[0].optFun.call(me,data[0], $(this));
        	    }
        	});     
		},
		initColumns : function(cols){
            for(var i = 0;i < cols.length;i++){
				if(!cols[i].name && typeof cols[i].data == 'string'){
					cols[i].name = cols[i].data;
				}
			}
		},
		initExport : function(opts){
			if(!opts.searchOpts || !opts.searchOpts.exportBtn) return;
			var me = this;
			var exportBtn = $("#" + opts.searchOpts.exportBtn);
			$("body").append("<iframe id='"+ opts.searchOpts.exportBtn +"_iframe' target='_blank' style='display:none' />");
			
			$("#" + opts.searchOpts.exportBtn).click(function(){
				var _title = $(this).attr("_title");
				var _fileName = $(this).attr("_fileName");
				var cts = [],cis = [];
				var cols = opts.columns;
				for(var i = 0;i < cols.length;i++){
					cts.push(cols[i].title);
					cis.push(cols[i].data);
				}
				var _columnText = cts.join(",");
				var _columnIndex = cis.join(",");
				
				var formData = me.getAjaxData(opts);
				var url = $(this).attr("_url");
				var mark = url.indexOf("?") > 0 ? "&" : "?";
				
				url += mark + "_title="	+ _title + "&_fileName=" + _fileName + "&_columnText=" + _columnText + "&_columnIndex=" + _columnIndex;
				
				for(var d in formData){
					url += "&"+ d +"=" + formData[d];
				}
				$("#" + this.id + "_iframe").attr("src",url);
			});
		},
		
		getAjaxData : function(opts){
			if(!opts.searchOpts) return {};
			var formData = $("#" + opts.searchOpts.searchForm).serializeFormJson();
			//请求服务器数据之前回调方法
			if(opts.searchOpts.searchBefore){
				opts.searchOpts.searchBefore(formData);
			}
			return formData;
		},
		
		initSearch : function(opts){
			if(!opts.searchOpts) return;
			if(opts.ajax.data) return;
			
			var me = this;
			if(typeof opts.ajax == "string"){
				var url = opts.ajax;
				opts.ajax = {url : url};
			}
			//opts.ajax.method ="POST";
			opts.ajax.data = function(d){
				var formData = me.getAjaxData(opts);
				
				formData.offset = d.start || 0;
				formData.size = d.length;
				formData.page = (d.length == 0 ? 0 : Math.floor(formData.offset/d.length)) + 1;
				formData.e_p = d;
				return formData;
			}
			$("body").on("click","#" + opts.searchOpts.searchBtn, function(){
				var mt = me.dataTable;
				if(!opts.serverSide){
					var formData = $("#" + opts.searchOpts.searchForm).serializeArray();
					var value;
					for(var i = 0;i < formData.length;i++){
						value = formData[i].value;
						if(!value || value.length <= 0){
							value = "";
						}
						mt = me.dataTable.columns(formData[i].name + ":name").search(value);
					}
				}
				mt.draw();
			});
		}
	}
}(jQuery);