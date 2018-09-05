$(function(){
	var lastRequest = $.cookie('lastRequest');
	if(lastRequest && lastRequest.length > 0){
		$("#J_iframe").attr('src',lastRequest);
	    modifyMenuName($.cookie('menuName'),$.cookie('menuId'),lastRequest);
	}else{
		$("#J_iframe").attr('src',ys.path + "/main");
	    modifyMenuName('首页');
	}
	$.cookie('lastRequest',null);

	var menus = JSON.parse($("#menus").val());

    var level1Menus =
        _.chain(menus)
        .filter(function(menu){
            return menu.pid == 0;
        })
        .map(function(menu){
            menu.text = '<li class="line dk"></li>'+
                '<li class="hidden-folded padder m-t m-b-sm text-muted text-xs" id="menu_'+menu.id+'">'+
                '<span class="ng-scope">'+menu.menuName+'</span>'+
                '</li>';
            return menu;
        })
        .sortBy('sort').reverse()
        .value();
    _.each(level1Menus,function(level1Menu){
        renderChildren(level1Menu,$("#menus"));
    });

    function renderChildren(currMenu, parentMenu){
        currMenu.sort < 1000 ? parentMenu.after(currMenu.text):parentMenu.prepend(currMenu.text);
        var children =
            _.chain(menus)
                .filter(function(menu){
                    return menu.pid == currMenu.id;
                })
                .map(function(menu){
                    menu.text = menu.leaf==0 ?
                    '<li id="menu_'+menu.id+'"><a href="#"><i class="fa fa-'+menu.code+'"></i> <span class="nav-label">'+menu.menuName+'</span><span class="fa arrow"></span></a><ul id="ul_'+menu.id+'" class="nav nav-second-level collapse"></ul></li>'
                        : '<li><a class="J_menuItem" menuId="'+menu.id+'" href="'+ys.path+menu.href+'"><i class="fa fa-'+menu.code+'"></i> <span class="nav-label">'+menu.menuName+'</span></a></li>';
                    return menu;
                })
                .sortBy('sort').reverse()
                .value();
        _.each(children,function(child){
            renderChildren(child,$((child.sort < 1000 ? "#menu_" : "#ul_")+currMenu.id));
        });
    }


    //菜单点击
    $(".J_menuItem").on('click',function(){
        var url = $(this).attr('href');
        $("#J_iframe").attr('src',url);
        modifyMenuName($(this).find("span").html(),$(this).attr("menuId"),url);
        return false;
    });

    $('#side-menu').metisMenu();
    
 // MetsiMenu
    //$('#side-menu').metisMenu();

    // 打开右侧边栏
    $('.right-sidebar-toggle').click(function () {
        $('#right-sidebar').toggleClass('sidebar-open');
    });

    //固定菜单栏
    $('.sidebar-collapse').slimScroll({
        height: '100%',
        railOpacity: 0.9,
        alwaysVisible: false
    });


    // 菜单切换
    $('.navbar-minimalize').click(function () {
        $("body").toggleClass("mini-navbar");
        SmoothlyMenu();
    });


    // 侧边栏高度
    function fix_height() {
        var heightWithoutNavbar = $("body > #wrapper").height() - 61;
        $(".sidebard-panel").css("min-height", heightWithoutNavbar + "px");
    }
    fix_height();

    $(window).bind("load resize click scroll", function () {
        if (!$("body").hasClass('body-small')) {
            fix_height();
        }
    });

    //侧边栏滚动
    $(window).scroll(function () {
        if ($(window).scrollTop() > 0 && !$('body').hasClass('fixed-nav')) {
            $('#right-sidebar').addClass('sidebar-top');
        } else {
            $('#right-sidebar').removeClass('sidebar-top');
        }
    });

    $('.full-height-scroll').slimScroll({
        height: '100%'
    });

    $('#side-menu>li').click(function () {
        if ($('body').hasClass('mini-navbar')) {
            NavToggle();
        }
    });
    $('#side-menu>li li a').click(function () {
        if ($(window).width() < 769) {
            NavToggle();
        }
    });

    $('.nav-close').click(NavToggle);

    //ios浏览器兼容性处理
    if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
        $('#content-main').css('overflow-y', 'auto');
    }
    
});

function _iframe_click(){
	$("body").click();
}

function modifyMenuName(menuName,menuId,url){
	var addBtn = "";
	if(!hasExistMenu(menuId) && menuName != '首页'){
		addBtn = '<i class="fa fa-plus" style="cursor: pointer;"></i>';
	}
	$("#menu_name").attr("menuId",menuId);
	$("#menu_name").attr("menuName",menuName);
	$("#menu_name").attr("url",url);
	$("#menu_name").html(menuName + "&nbsp;&nbsp;" +addBtn);
	
	$("#menu_name").find("i").click(function(){
		var me = this;
		BootstrapDialog.show({
            title:"添加常用功能",
            message: "<div id='t_info'></div>您确认要添加【"+menuName+"】为常用功能吗？",
            buttons: [{
                label: '确定',
                action: function(dialog) {
                    $.ajax({
                        "type":"post",
                        "url":ys.path+"/index/addUserMenu",
                        "data":{"menuId":menuId},
                        "success":function(ret){
                    		$("#t_info").html('<div class="alert alert-success alert-dismissable">'+
                                        '<button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>保存成功!</div>');
                    		 setTimeout(function(){
                    			 dialog.close();
                             },800);
                    		 addMenu(menuId,menuName);
                    		 $(me).remove();
                        }
                    });
                }
            }, {
                label: '取消',
                action: function(dialog) {
                    dialog.close();
                }
            }]
        })
	});
}

var menu_conf_data = null;

function hasExistMenu(menuId){
	var d = getUserConfMenus();
	if(!d) return false;
	for(var i = 0;i < d.length;i++){
		if(menuId == d[i].menuId){
			return true;
		}
	}
	return false;
}

function addMenu(menuId,menuName){
	if(!menu_conf_data) menu_conf_data = [];
	menu_conf_data.push({
		menuId : menuId,
		menuName : menuName
	});
}

function removeMenu(menuId){
	var d = getUserConfMenus();
	if(!d) return;
	_.remove(menu_conf_data,function(n){
		return n.menuId == menuId;
	});
}

function getUserConfMenus(){
	return "a";
	/*if(menu_conf_data) return menu_conf_data;
	$.ajax({
        "type":"post",
        "async":false,
        "url":ys.path+"/index/listUserMenu",
        "success":function(ret){
        	if(ret.success){
        		menu_conf_data = ret.data;
        	}
        }
	});*/
}
getUserConfMenus();

window.onunload=function(){
	var h = $("#J_iframe").contents()[0].location.href;
	if(h.indexOf("user/login") < 0){
		$.cookie('lastRequest',h);
	}
	$.cookie('menuName', $("#menu_name").attr("menuName"));
	$.cookie('menuId',	$("#menu_name").attr("menuId"));
}
