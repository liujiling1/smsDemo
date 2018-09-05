function areasById(id){
	var list = function(arr,id){
		for(item in arr){
			if(arr[item]['id']==id){
				return arr[item];
			}
		}
	}
	return list(areas["province"],id)||list(areas["city"],id)||list(areas["area"],id)||{"name":""};
}

function addressByAreaId(areaId){
	var area = areasById(areaId);
	var city = areasById(area.parentId);
	var province = areasById(city.parentId);
	return province.name + (province.name == city.name?"":city.name) + area.name;
}

function addrStr(ids){
	var addrArr = [];
	var list = function(arrs,id){
		for(var i = 0;i < arrs.length;i++){
			if(id == arrs[i].id){
				return arrs[i].name;
			}
		}
	}
	addrArr.push(list(areas.province,ids[0]));
	addrArr.push(list(areas.city,ids[1]));
	addrArr.push(list(areas.area,ids[2]));
	
	return addrArr.join(" ");
}

+function($){
	
	"use strict";
	
	$.fn.ysAdds = function(options){ 
		var r;
		var args = Array.prototype.slice.call(arguments, 1);
		this.each(function(){
			var o = $(this).data("ysAdds");
			if(options.destroy && o){
				o._destroy();
				o = null;
			}
			if(!o){
				o = new $.ysAdds();
				o.el = this;
				$.extend(o, options );  
				o.init();
				$(this).data("ysAdds",o);
			}
			if(o[options]){
				r = o[options].apply(o,args);
				return r;
			} 
		});
		return r;	
	}
	/**
	 * ysAdds has options 
	 * province : {
			name : "province",
			id : "${info.province}"
		},
		city : {
			name : "city",
			id : "${info.city}"
		},
		area : {
			name : "area",
			required : true,
			id : "${info.area }"
		},
		address : {
			name : "address",
			value:"dssdsdsd",
			attrs : {
				"aria-required" : "true"
			}
		}
	 */
	$.ysAdds = function(){
		
	};	
	
	$.ysAdds.prototype = {
		_destroy : function(){
			var me = this;
			$(me).data("ysAdds",null);
			$(me.el).empty();
		},
		init : function(){
			var me = this;
			me.render();
			me.initEvent();
		},
		render : function(){
			var me = this;
			$(me.el).addClass('no-padding');
			var els = ['<div class="col-sm-2" style="padding-right:0px;"><select class="form-control province" name="',me.province.name,'" value="',me.province.id,'">'];
			var provinces = areas.province;
			if(!me.province.required){
				els.push('<option value="">请选择</option>');
			}
			var p = me.province.id ? me.province.id : "";
			for(var i = 0;i < provinces.length;i++){
				var s = p == provinces[i].id ? "selected='selected'" : "";
				if(me.province.required && i==0 && p==""){
					p = provinces[i].id;
				}
				els.push('<option value="',provinces[i].id,'" ',s,'>',provinces[i].name,'</option>');
			}
			els.push("</select></div>");
			
			var c  = me.city.id ? me.city.id : "";
			els = els.concat(['<div class="col-sm-2" style="padding-right:0px;padding-left:5px;"><select class="form-control city" name="',me.city.name,'" value="',me.city.id,'">']);
			var citys = me.listChildrens(areas.city,p,me.city.required);
			for(var i = 0;i < citys.length;i++){
				var s = c == citys[i].id ? "selected='selected'" : "";
				if(me.city.required && i==0 && c==""){
					c = citys[i].id;
				}
				els.push('<option value="',citys[i].id,'" ',s,'>',citys[i].name,'</option>');
			}	
			els.push("</select></div>");

			var a  = me.area.id ? me.area.id : '';
			els = els.concat(['<div class="col-sm-2" style="padding-right:0px;padding-left:5px;"><select class="form-control area" name="',me.area.name,'" value="',me.area.id,'">']);
			var ars = me.listChildrens(areas.area,c,me.area.required);
			for(var i = 0;i < ars.length;i++){
				var s = a == ars[i].id ? "selected='selected'" : "";
				els.push('<option value="',ars[i].id,'" ',s,'>',ars[i].name,'</option>');
			}	
			els.push("</select></div>");
			
			var attrsStr = "";
			for(var attr in me.address.attrs){
				attrsStr += attr + "='" + me.address.attrs[attr] + "' ";
			}
			els = els.concat(['<div class="col-sm-6" style="padding-left:5px;"><input  class="form-control address" name="',me.address.name,'" ',attrsStr,' value="',me.address.value,'" /></div>']);

			$(els.join("")).appendTo(me.el);
		},
		initEvent : function(){
			var me = this;
			var province = $(me.el).find(".province"),city = $(me.el).find(".city"),area = $(me.el).find(".area"),addr=$(me.el).find(".address");
			
			province.change(function(){
				city.empty();
				area.empty();
				var val = province.val();
				var citys = me.listChildrens(areas.city,val,me.city.required);
				for(var i = 0;i < citys.length;i++){
					city.append("<option value='"+ citys[i].id +"'>"+ citys[i].name +"</option>"); 
				}
				
				var c = city.val();
				var ars = me.listChildrens(areas.area,c,me.area.required);
				for(var i = 0;i < ars.length;i++){
					area.append("<option value='"+ ars[i].id +"'>"+ ars[i].name +"</option>"); 
				}
			});
			
			city.change(function(){
				area.empty();
				
				var c = city.val();
				var ars = me.listChildrens(areas.area,c,me.area.required);
				for(var i = 0;i < ars.length;i++){
					area.append("<option value='"+ ars[i].id +"'>"+ ars[i].name +"</option>"); 
				}
			});
			
		},
		listChildrens : function(arr,pId,required){
			var regions = required ? [] : [{"id":"","name":"请选择"}];; 
			for(var i = 0;i < arr.length;i++){
				if(arr[i].parentId == pId){
					regions.push(arr[i]);
				}
			}
			return regions;
		}
	}
	
}(jQuery)