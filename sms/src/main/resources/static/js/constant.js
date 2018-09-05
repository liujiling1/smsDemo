(function(window,undefined){
    var constantEnum = {
        carType : {
            xm:{"val":"1","txt":"小面"},
            zm:{"val":"6","txt":"中面"},
            dm:{"val":"7","txt":"大面"},
            jb:{"val":"2","txt":"金杯"},
            xh:{"val":"3","txt":"厢货"},
            xxpb:{"val":"4","txt":"小型平板"},
            dxpb:{"val":"5","txt":"大型平板"}
        },
        fuelType:{
            xnyc:{"val":"1","txt":"新能源车"},
            ryc:{"val":"2","txt":"燃油车"},
            hhdlc:{"val":"3","txt":"混合动力车"}
        },
        cooType:{
            zzjm:{"val":"1","txt":"有车司机加入"},
            lyz:{"val":"2","txt":"司机创业计划"},
            zysj:{"val":"3","txt":"全职司机招募"}
        },
        orderType:{
            normal:{"val":"1","txt":"常规运单"},
            unnormal:{"val":"2","txt":"非常规运单"}
        },
        orderStatus:{
            unaudit:{"val":"11","txt":"待审核"},
            audit_failed:{"val":"12","txt":"审核失败"},
            undispatched:{"val":"21","txt":"待调度"},
            unstarted:{"val":"22","txt":"未开始"},
            in_process:{"val":"23","txt":"进行中"},
            finish:{"val":"24","txt":"已结束"},
            cancel:{"val":"25","txt":"已取消"}
        },
        orderDispatchReason:{
            normal:{"val":"1","txt":"派单调度"},
            replace:{"val":"2","txt":"换车"},
            add:{"val":"3","txt":"新增车辆"}
        },
        orderItemStatus:{
            //{"1":"待确认","2":"已确认","3":"前往发货地","4":"到达发货地","5":"装货完成","6":"前往收货地","7":"到达收货地","8":"卸货完成","9":"进行中","10":"已结束","11":"已取消"}',
            unconfirmed:{"val":"1","txt":"待确认"},
            confirmed:{"val":"2","txt":"已确认"},
            goto_shipping:{"val":"3","txt":"前往发货地"},
            arrive_shipping:{"val":"4","txt":"到达发货地"},
            finish_shipping:{"val":"5","txt":"装货完成"},
            goto_receive:{"val":"6","txt":"前往收货地"},
            arrive_receive:{"val":"7","txt":"到达收货地"},
            finish_receive:{"val":"8","txt":"卸货完成"},
            in_process:{"val":"9","txt":"进行中"},
            finish:{"val":"10","txt":"已结束"},
            cancel:{"val":"11","txt":"已取消"}
        },
        useTimeType:{
            Monday :{"val":"1","txt":"每周一"},
            Tuesday :{"val":"2","txt":"每周二"},
            Wednesday :{"val":"3","txt":"每周三"},
            Thursday :{"val":"4","txt":"每周四"},
            Friday :{"val":"5","txt":"每周五"},
            Saturday :{"val":"6","txt":"每周六"},
            Sunday:{"val":"7","txt":"每周日"},
            EveryDay:{"val":"8","txt":"每天"}
        },
        generateOrderTimeType:{
            One:{"val":"1","txt":"用车前1小时"},
            OneHalf:{"val":"2","txt":"用车前1.5小时"},
            Two:{"val":"3","txt":"用车前2小时"},
            TwoHalf:{"val":"4","txt":"用车前2.5小时"},
            Three:{"val":"5","txt":"用车前3小时"}
        },
        driverWorkStatus:{//'工作状态：1未上岗，2待派单，3运单中，4休息中，5终止合作',
            OutBoard:{"val":"1","txt":"未上岗"},
            BeingDispatch:{"val":"2","txt":"待派单"},
            Ordering:{"val":"3","txt":"运单中"},
            Rest:{"val":"4","txt":"休息中"},
            Stop:{"val":"5","txt":"终止合作"}
        },
        erpType:{//企业类型：1承运人，2发货人
            Carrier:{"val":"1","txt":"承运人"},
            Shipper:{"val":"2","txt":"发货人"}
        },
        orderAuditStatus:{//1.审核成功2.审核失败
            Pass:{"val":"1","txt":"审核成功"},
            Fail:{"val":"2","txt":"审核失败"}
        },
        taskTerm:{
            Long:{"val":"1","txt":"长期"},
            Short:{"val":"2","txt":"短期"}
        },
        is:{
            Yes:{"val":"1","txt":"是"},
            No:{"val":"0","txt":"否"}
        },
        orderItemReceiveReason:{//修改原因(1.发货方临时紧急调派2.收货方改接收单3.司机报错点位)
            Shipping:{"val":"1","txt":"发货方临时紧急调派"},
            Receive:{"val":"2","txt":"收货方改接收单"},
            Driver:{"val":"3","txt":"司机报错点位"}
        }
    };

    window.ys_const = constantEnum;
    window.ys_cfn = function(type){
        return function(_val){
            return (_.find(_.values(_.isString(type)?(constantEnum[type]||[]):type),function(ele){return ele.val == _val})||{}).txt;
        }
    }

    var receiveDesDefaultValArr = [];
    receiveDesDefaultValArr.push('配送区域：中轴线以东，长安街以北');
    receiveDesDefaultValArr.push('点位个数：9-13');
    receiveDesDefaultValArr.push('是否返仓：是/否');
    receiveDesDefaultValArr.push('总公里数：80km');
    receiveDesDefaultValArr.push('到仓时间：早X点');
    receiveDesDefaultValArr.push('配送周期：周一到周日');
    receiveDesDefaultValArr.push('限行是否配送：是/否');
    receiveDesDefaultValArr.push('是否需要回单：是/否');
    receiveDesDefaultValArr.push('是否需要代收货款：是/否');
    receiveDesDefaultValArr.push('长期/短期任务：长期');
    receiveDesDefaultValArr.push('货物体积：7m³');
    receiveDesDefaultValArr.push('货物重量：1吨');
    receiveDesDefaultValArr.push('是否需要司机装卸：否');
    window.receiveDesDefaultVal = receiveDesDefaultValArr.join("\n");
})(window);
