//打印生产任务单
function PrPrintProduct(list,surfaceWay,surfaceWay1,surfaceWay2,dueDate) {

    if(list!=null )
    {

        LODOP = getLodop();

        LODOP.PRINT_INITA(0,0,800,1100,"生产任务单");
        LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");
        LODOP.ADD_PRINT_TEXT(22,271,302,33,"浙江瑞邦智能装备股份有限公司");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
        LODOP.SET_PRINT_STYLEA(0,"Bold",1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(47,350,302,33,"生产任务单");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",13);
        LODOP.SET_PRINT_STYLEA(0,"Bold",1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(77,18,237,32,"下单日期："+isNullValues(list[0].startdate));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(77,287,252,32,"任务单号："+isNullValues(list[0].plancode));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(100,18,275,32,"合 同 号："+isNullValues(list[0].cdefine1));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(100,287,252,32,"需求日期："+isNullValues(dueDate));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(126,19,750,32,"备    注："+isNullValues(list[0].cmemo));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);



        LODOP.ADD_PRINT_TEXT(75,570,150,32,"表面处理方式");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        var str="";
        if(surfaceWay!="")
        {
            str=surfaceWay;
        }
        if(surfaceWay1!="")
        {
            if(str=="")
            {
                str=surfaceWay1;
            }
            else {
                str=str+','+surfaceWay1;
            }


        }
        if(surfaceWay2!="")
        {
            if(str=="")
            {
                str=surfaceWay2;
            }
            else {
                str=str+','+surfaceWay2;
            }


        }
        LODOP.ADD_PRINT_RECT(97,528,13,14,0,1);
        if(surfaceWay1!="")
        {
            LODOP.ADD_PRINT_TEXT(98,528,62,32,"√ 喷砂");

        }
        else {
            LODOP.ADD_PRINT_TEXT(98,528,62,32,"  喷砂");
        }
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_RECT(98,592,13,14,0,1);
        if(surfaceWay!="")
        {
            LODOP.ADD_PRINT_TEXT(98,591,78,32,"√ 拉丝");

        }
        else {
            LODOP.ADD_PRINT_TEXT(98,591,78,32,"  拉丝");

        }
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);



        LODOP.ADD_PRINT_RECT(98,656,13,14,0,1);
        if(surfaceWay2!="")
        {
            LODOP.ADD_PRINT_TEXT(98,654,78,32,"√ 其他");
        }
        else {
            LODOP.ADD_PRINT_TEXT(98,654,78,32,"  其他");
        }

        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_RECT(68,519,199,54,0,4);


        LODOP.ADD_PRINT_TABLE(150,16,780,1000,CreateProductTable(list));
        var lenth=0;
        if(list!=null)
        {
            lenth=list.length;
        }
        if(lenth>18)
        {
            lenth=(150-0)+(150-0)+(35-0)*((lenth-0)+2)+(10-0);
        }
        else
        {
            lenth=(150-0)+(150-0)+(35-0)*(20-0)+(10-0);
            LODOP.ADD_PRINT_TEXT(lenth,16,478,48,"备注： 1、钣金组自检、互检必须由小组长签字。\r\n       2、生产安装结束，品控签字后交由特规主管作为工资结算依据。");

        }



        LODOP.SET_PRINT_MODE("FULL_WIDTH_FOR_OVERFLOW",true);

        LODOP.PRINT_DESIGN();

        // LODOP.PREVIEW();
    }

}
function  CreateProductTable(result) {


    var lenth=0;
    if(result!=null)
    {
        lenth=result.length;
    }
    var css =" <style> table,td,th {border: 1px solid black;border-style: solid;border-collapse: collapse;font-size: 13px;}</style><table  style='border-bottom: none'>";
    //第一行
    var th = " <thead> <tr style='height:35px' ><td  align='center' bgcolor='#a9a9a9'>序号</td><td colspan=3 align='center' bgcolor='#a9a9a9'>图号</td><td  colspan=3  align='center' bgcolor='#a9a9a9'>名称</td><td  colspan=2  align='center' bgcolor='#a9a9a9'>规格</td><td align='center' bgcolor='#a9a9a9'>数量</td><td colspan=3 align='center' bgcolor='#a9a9a9'>客户</td></tr>";
    th=th+" </thead>  ";

    var td="";

    //表格数据加载
    for (var i = 0; i <lenth; i++) {

        td = td+"<tr style='height:35px'><td  align='center'>"+(i+1)+"</td><td  colspan=3  style='word-wrap:break-word;word-break:break-all;'  align='left'>" + isNullValues(result[i].cinvcode) + "</td><td  colspan=3  style='word-wrap:break-word;word-break:break-all;' align='left'>" + isNullValues(result[i].cinvname) +"</td><td  colspan=2  style='word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].cinvstd) + "</td><td style='word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].planqty) + "</td><td  colspan=3  style='word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].ccusabbname) + "</td>  </tr>"
    }
    //表格数据加载
    for (var i = lenth; i<=16; i++) {
        td = td+"<tr style='height:35px'><td  align='center'>"+''+"</td><td  colspan=3  align='center'>"+''+"</td> <td  colspan=3   align='center'>"+''+"</td><td  colspan=2   align='center'>"+''+"</td><td   align='center'>"+''+"</td><td  colspan=3  align='center'>"+''+"</td> </tr>"
    }

    td=td+"<tr style='height:24px' ><td colspan=13 align='center' bgcolor='#a9a9a9' >"+'设备工序时间表'+"</td></tr>";
    td=td+"<tr style='height:24px' ><td rowspan=2   bgcolor='#a9a9a9' align='center' >"+''+"</td><td colspan=3 align='center'  bgcolor='#a9a9a9'>"+'材料时间表'+"</td><td colspan=5 align='center'  bgcolor='#a9a9a9'>"+'钣金组检查表'+"</td><td colspan=4 align='center'  bgcolor='#a9a9a9'>"+'安装组检查表'+"</td></tr>";

    td=td+"<tr style='height:24px' ><td style='width:40px;'align='center' bgcolor='#a9a9a9'>下料</td><td style='width:40px;'align='center' bgcolor='#a9a9a9'>金工</td><td style='width:40px;'align='center' bgcolor='#a9a9a9'>外购</td><td style='width:55px;'align='center' bgcolor='#a9a9a9'>钣金</td><td style='width:55px;' align='center' bgcolor='#a9a9a9'>焊接</td><td  style='width:55px;' align='center' bgcolor='#a9a9a9'>整理</td><td style='width:50px;' align='center' bgcolor='#a9a9a9'>自检</td><td style='width:45px;' align='center' bgcolor='#a9a9a9'>互检</td><td style='width:45px;' align='center' bgcolor='#a9a9a9'>安装</td><td style='width:50px;'   align='center' bgcolor='#a9a9a9'>包装</td><td  style='width:50px;'  align='center' bgcolor='#a9a9a9'>调试</td> <td   style='width:50px;'  align='center' bgcolor='#a9a9a9'>品控</td></tr>";
    td=td+"<tr style='height:24px' ><td  align='center' style='width:50px;' bgcolor='#a9a9a9'>姓名</td><td  align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td  align='center'  >"+''+"</td><td   align='center'  >"+''+"</td><td    align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td></tr>";
    td=td+"<tr style='height:24px' ><td  align='center' style='width:50px;'  bgcolor='#a9a9a9'>下发日期</td><td  align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td  align='center'  >"+''+"</td><td   align='center'  >"+''+"</td><td    align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td></tr>";
    td=td+"<tr style='height:24px' ><td  align='center'  bgcolor='#a9a9a9'>预定日期</td><td  align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td  align='center'  >"+''+"</td><td   align='center'  >"+''+"</td><td    align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td></tr>";
    td=td+"<tr style='height:24px' ><td  align='center'  bgcolor='#a9a9a9'>完成日期</td><td  align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td  align='center'  >"+''+"</td><td   align='center'  >"+''+"</td><td    align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td></tr>";
    td=td+"<tr style='height:24px' ><td  align='center'  bgcolor='#a9a9a9'>结算工价</td><td  colspan=12 align='center' >"+''+"</td>  </tr>";

    td = td+"<tr style='border: none:height:0px' ><td style='width: 60px;border: none'></td><td style='width: 45px;border: none'></td><td style='width: 45px;border: none'></td><td style='width: 45px;border: none'></td><td style='width: 55px;border: none'></td><td style='width: 55px;border: none'></td><td style='width: 55px;border: none'></td><td style='width: 55px;border: none'></td><td style='width: 55px;border: none'></td><td style='width: 55px;border: none'></td><td style='width: 60px;border: none'></td><td style='width: 60px;border: none'></td><td style='width:60px;border: none'></td></tr>"

    // style='width:20px;'
    var txt = css +th+ td+"</table>";

    return txt;

}
// function  CreateProductTable(result) {
//
//
//     var lenth=0;
//     if(result!=null)
//     {
//         lenth=result.length;
//     }
//     var css =" <style> table,td,th {border: 1px solid black;border-style: solid;border-collapse: collapse;font-size: 13px;}</style><table border=1>";
//     //第一行
//     var th = " <thead> <tr style='height:35px' ><td  align='center' bgcolor='#a9a9a9'>序号</td><td colspan=3 align='center' bgcolor='#a9a9a9'>图号</td><td  colspan=3  align='center' bgcolor='#a9a9a9'>名称</td><td  colspan=2  align='center' bgcolor='#a9a9a9'>规格</td><td align='center' bgcolor='#a9a9a9'>数量</td><td colspan=3 align='center' bgcolor='#a9a9a9'>客户</td></tr>";
//     th=th+" </thead>  ";
//
//     var td="";
//
//     //表格数据加载
//     for (var i = 0; i <lenth; i++) {
//
//         td = td+"<tr style='height:35px'><td  align='center'>"+(i+1)+"</td><td  colspan=3  style='word-wrap:break-word;word-break:break-all;'  align='left'>" + isNullValues(result[i].cinvcode) + "</td><td  colspan=3  style='word-wrap:break-word;word-break:break-all;' align='left'>" + isNullValues(result[i].cinvname) +"</td><td  colspan=2  style='word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].cinvstd) + "</td><td style='word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].planqty) + "</td><td  colspan=3  style='word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].ccusabbname) + "</td>  </tr>"
//     }
//     //表格数据加载
//     for (var i = lenth; i<=16; i++) {
//         td = td+"<tr style='height:35px'><td  align='center'>"+''+"</td><td  colspan=3  align='center'>"+''+"</td> <td  colspan=3   align='center'>"+''+"</td><td  colspan=2   align='center'>"+''+"</td><td   align='center'>"+''+"</td><td  colspan=3  align='center'>"+''+"</td> </tr>"
//     }
//
//     td=td+"<tr style='height:24px' ><td colspan=13 align='center' bgcolor='#a9a9a9' >"+'设备工序时间表'+"</td></tr>";
//     td=td+"<tr style='height:24px' ><td rowspan=2   bgcolor='#a9a9a9' align='center' >"+''+"</td><td colspan=3 align='center'  bgcolor='#a9a9a9'>"+'材料时间表'+"</td><td colspan=5 align='center'  bgcolor='#a9a9a9'>"+'钣金组检查表'+"</td><td colspan=4 align='center'  bgcolor='#a9a9a9'>"+'安装组检查表'+"</td></tr>";
//
//     td=td+"<tr style='height:24px' ><td style='width:40px;'align='center' bgcolor='#a9a9a9'>下料</td><td style='width:40px;'align='center' bgcolor='#a9a9a9'>金工</td><td style='width:40px;'align='center' bgcolor='#a9a9a9'>外购</td><td style='width:55px;'align='center' bgcolor='#a9a9a9'>钣金</td><td style='width:55px;' align='center' bgcolor='#a9a9a9'>焊接</td><td  style='width:55px;' align='center' bgcolor='#a9a9a9'>整理</td><td style='width:50px;' align='center' bgcolor='#a9a9a9'>自检</td><td style='width:45px;' align='center' bgcolor='#a9a9a9'>互检</td><td style='width:45px;' align='center' bgcolor='#a9a9a9'>安装</td><td style='width:50px;'   align='center' bgcolor='#a9a9a9'>包装</td><td  style='width:50px;'  align='center' bgcolor='#a9a9a9'>调试</td> <td   style='width:50px;'  align='center' bgcolor='#a9a9a9'>品控</td></tr>";
//     td=td+"<tr style='height:24px' ><td  align='center' style='width:50px;' bgcolor='#a9a9a9'>姓名</td><td  align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td  align='center'  >"+''+"</td><td   align='center'  >"+''+"</td><td    align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td></tr>";
//     td=td+"<tr style='height:24px' ><td  align='center' style='width:50px;'  bgcolor='#a9a9a9'>下发日期</td><td  align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td  align='center'  >"+''+"</td><td   align='center'  >"+''+"</td><td    align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td></tr>";
//     td=td+"<tr style='height:24px' ><td  align='center'  bgcolor='#a9a9a9'>预定日期</td><td  align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td  align='center'  >"+''+"</td><td   align='center'  >"+''+"</td><td    align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td></tr>";
//     td=td+"<tr style='height:24px' ><td  align='center'  bgcolor='#a9a9a9'>完成日期</td><td  align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td  align='center'  >"+''+"</td><td   align='center'  >"+''+"</td><td    align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td><td   align='center' >"+''+"</td></tr>";
//     td=td+"<tr style='height:24px' ><td  align='center'  bgcolor='#a9a9a9'>结算工价</td><td  colspan=12 align='center' >"+''+"</td>  </tr>";
//     // style='width:20px;'
//     var txt = css +th+ td+"</table>";
//
//     return txt;
//
// }





//打印设备总清单
function PrPrintDevice(url, id,name) {
    //print();
    $.ajax({
        type: "POST",
        url: url,
        cache: false,
        async: false,
        data: { querystr: id },
        dataType: 'json',
        success: function (result) {

            if(result!=null&&result.length>0)
            {

                LODOP = getLodop();
                LODOP.PRINT_INITA(0,0,1600,1100,name);
                LODOP.SET_PRINT_PAGESIZE(2,0,0,"A3");
                LODOP.SET_PRINT_MODE("PRINT_NOCOLLATE",1);
                LODOP.ADD_PRINT_TEXT(28,582,534,32,"浙江瑞邦智能装备有限公司     "+name);
                LODOP.SET_PRINT_STYLEA(0,"FontSize",13);
                LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
                LODOP.SET_PRINT_STYLEA(0,"Horient",2);
                if(name.indexOf('设备总清单')!=-1){
                    LODOP.ADD_PRINT_TABLE(67,24,1600,1500,CreateDeviceTableAll(result));

                }
                else {
                    LODOP.ADD_PRINT_TABLE(67,24,1600,1500,CreateDeviceTable(result));

                }

                LODOP.SET_PRINT_STYLEA(0,"TableHeightScope",1);


                //LODOP.PRINT_SETUP();

                //LODOP.PREVIEW();
                //LODOP.PRINT();
                LODOP.SET_PRINT_MODE("FULL_WIDTH_FOR_OVERFLOW",true);
                // LODOP.PRINT_DESIGN();
                LODOP.PREVIEW();
            }



        }
    });
}



//打印设备总清单
function PrPrintDeviceBatch(url, id,name) {
    //print();
    $.ajax({
        type: "POST",
        url: url,
        cache: false,
        async: false,
        data: { querystr: id },
        dataType: 'json',
        success: function (result) {

            if(result!=null&&result.length>0)
            {

                LODOP = getLodop();
                LODOP.PRINT_INITA(0,0,1600,1100,name);
                LODOP.SET_PRINT_PAGESIZE(2,0,0,"A3");
                LODOP.SET_PRINT_MODE("PRINT_NOCOLLATE",1);
                LODOP.ADD_PRINT_TEXT(28,582,534,32,"浙江瑞邦智能装备有限公司     "+name);
                LODOP.SET_PRINT_STYLEA(0,"FontSize",13);
                LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
                LODOP.SET_PRINT_STYLEA(0,"Horient",2);
                if(name.indexOf('设备总清单')!=-1){
                    LODOP.ADD_PRINT_TABLE(67,24,1600,1500,CreateDeviceTableAll(result));

                }
                else {
                    LODOP.ADD_PRINT_TABLE(67,24,1600,1500,CreateDeviceTable(result));

                }



                LODOP.SET_PRINT_STYLEA(0,"TableHeightScope",1);


                //LODOP.PRINT_SETUP();

                //LODOP.PREVIEW();
                //LODOP.PRINT();
                LODOP.SET_PRINT_MODE("FULL_WIDTH_FOR_OVERFLOW",true);
                // LODOP.PRINT_DESIGN();
                LODOP.PREVIEW();
            }



        }
    });
}


function  CreateDeviceTable(result) {

    // var  aa= "<table style='border-collapse:collapse;border:solid 1px'> <thead><tr><td>头Lodop语句<td></tr></thead> <tbody><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td></tr><tr><td>PREVIEW<td></tr><tr><td>PRINT<td></tr><tr><td>PRINT_INIT<td></tr><tr><td>PRINT_SETUP<td></tr><tr><td>ADD_PRINT_HTM<td></tr></tbody> <tfoot><tr><td>尾Lodop语句<td></tr></tfoot></table>";


    var css =" <style> table,td,th {border: 1px solid black;border-style: solid;border-collapse: collapse;font-size: 13px;}</style><table border=1>";
    //第一行
    var th = " <thead><tr style='height:37px'><td  align='center' colspan=2 bgcolor='#a9a9a9'>图号</td><td  align='left' colspan=2>"+result[0].productInvCode+"</td><td  align='center' bgcolor='#a9a9a9'>合同号</td> <td  align='left' colspan=3>"+isNullValues(result[0].conNo)+"</td><td  align='center' bgcolor='#a9a9a9' >数量</td><td  align='left' colspan=5>"+result[0].soQty+"</td><td  align='center' colspan=2></td><td  align='center'></td><td  align='center'></td></tr>"
    //第二行
    th = th+" <tr style='height:37px'><td  align='center' colspan=2 bgcolor='#a9a9a9'>产品名称</td><td  align='left' colspan=2>"+result[0].productInvName+"</td><td  align='center' bgcolor='#a9a9a9'>订单号</td> <td  align='left' colspan=3>"+result[0].orderNo+"</td><td  align='center'  bgcolor='#a9a9a9'>客户名称</td><td  align='left' colspan=5>"+isNullValues(result[0].custName)+"</td><td  align='center' colspan=2></td><td  align='center'></td><td  align='center'></td></tr>"
    th=th+"<tr style='height:37px' ><td  align='center' bgcolor='#a9a9a9'>序号</td><td  align='center' bgcolor='#a9a9a9'>子件编码</td><td align='center' bgcolor='#a9a9a9'>子件代码</td><td  align='center' bgcolor='#a9a9a9'>子件名称</td><td align='center' bgcolor='#a9a9a9'>规格型号</td><td  align='center' bgcolor='#a9a9a9'>单位</td><td  align='center' bgcolor='#a9a9a9'>单耗</td><td align='center' bgcolor='#a9a9a9'>总量</td> <td  align='center' bgcolor='#a9a9a9'>库存量</td><td  align='center' bgcolor='#a9a9a9'>在途量</td><td  align='center' bgcolor='#a9a9a9'>可用量</td><td  align='center' bgcolor='#a9a9a9'>加工类型</td><td  align='center' bgcolor='#a9a9a9'>下料尺寸</td><td align='center' bgcolor='#a9a9a9'>材料名称</td><td  align='center' bgcolor='#a9a9a9'>材料规格</td><td  align='center' bgcolor='#a9a9a9'>部件名称</td><td  align='center' bgcolor='#a9a9a9'>生产备注</td><td  align='center' bgcolor='#a9a9a9'>说明</td></tr>";
    th=th+" </thead>  ";


    var td="";
//表格数据加载
    for (var i = 0; i <result.length; i++) {
        td = td+"<tr style='height:37px'><td style='width:30px;' align='center'>"+(i+1)+"</td><td style='width:120px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].psCode) + "</td><td style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].invAddCode) +"</td><td style='width:155px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].invName) + "</td><td  style='width:100px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].invStd) + "</td><td style='width:27px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].invUnit) + "</td><td style='width:27px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].qty) + "</td><td style='width:42px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].qtys) + "</td> <td style='width:45px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].curQty) + "</td><td style='width:40px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].poQty) + "</td><td style='width:40px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].kyQty) + "</td><td style='width:55px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].proType) + "</td><td style='width:155px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].matSize) + "</td><td style='width:140px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].minvName) + "</td><td style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].minvStd) + "</td><td style='width:120px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].partName) + "</td><td  style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].memo) + "</td><td style='width:30px;word-wrap:break-word;word-break:break-all;' align='center'></td></tr>"
    }



    //尾部的总页数
    var tf="<tfoot><TD  colspan=9 tdata='pageNO' format='#' align='left'> <p align='center'><b>第<font color='#0000FF'>#</font>页</b></p> </TD> <TD  colspan=9  tdata='pageCount' format='#' align='right'> <p align='center'><b>总<font color='#0000FF'>##</font>页</b></TD></tfoot>";

    var txt = css +th+ td+tf+"</table>";
    return txt;

}
function  CreateDeviceTableAll(result) {

    // var  aa= "<table style='border-collapse:collapse;border:solid 1px'> <thead><tr><td>头Lodop语句<td></tr></thead> <tbody><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td><tr><td>PRINT_DESIGN<td></tr><tr><td>PREVIEW<td></tr><tr><td>PRINT<td></tr><tr><td>PRINT_INIT<td></tr><tr><td>PRINT_SETUP<td></tr><tr><td>ADD_PRINT_HTM<td></tr></tbody> <tfoot><tr><td>尾Lodop语句<td></tr></tfoot></table>";


    var css =" <style> table,td,th {border: 1px solid black;border-style: solid;border-collapse: collapse;font-size: 13px;}</style><table border=1>";
    //第一行
    var th = " <thead><tr style='height:37px'><td  align='center' colspan=2 bgcolor='#a9a9a9'>图号</td><td  align='left' colspan=2>"+result[0].productInvCode+"</td><td  align='center' bgcolor='#a9a9a9'>合同号</td> <td  align='left' colspan=3>"+isNullValues(result[0].conNo)+"</td><td  align='center' bgcolor='#a9a9a9' >数量</td><td  align='left' colspan=5>"+result[0].soQty+"</td><td  align='center' colspan=2></td><td  align='center'></td><td  align='center'></td></tr>"
    //第二行
    th = th+" <tr style='height:37px'><td  align='center' colspan=2 bgcolor='#a9a9a9'>产品名称</td><td  align='left' colspan=2>"+result[0].productInvName+"</td><td  align='center' bgcolor='#a9a9a9'>订单号</td> <td  align='left' colspan=3>"+result[0].orderNo+"</td><td  align='center'  bgcolor='#a9a9a9'>客户名称</td><td  align='left' colspan=5>"+isNullValues(result[0].custName)+"</td><td  align='center' colspan=2></td><td  align='center'></td><td  align='center'></td></tr>"
    th=th+"<tr style='height:37px' ><td  align='center' bgcolor='#a9a9a9'>序号</td><td  align='center' bgcolor='#a9a9a9'>子件编码</td><td align='center' bgcolor='#a9a9a9'>子件代码</td><td  align='center' bgcolor='#a9a9a9'>子件名称</td><td align='center' bgcolor='#a9a9a9'>规格型号</td><td  align='center' bgcolor='#a9a9a9'>单位</td><td  align='center' bgcolor='#a9a9a9'>单耗</td><td align='center' bgcolor='#a9a9a9'>总量</td> <td  align='center' bgcolor='#a9a9a9'>库存量</td><td  align='center' bgcolor='#a9a9a9'>在途量</td><td  align='center' bgcolor='#a9a9a9'>可用量</td><td  align='center' bgcolor='#a9a9a9'>加工类型</td><td  align='center' bgcolor='#a9a9a9'>下料尺寸</td><td align='center' bgcolor='#a9a9a9'>材料名称</td><td  align='center' bgcolor='#a9a9a9'>材料规格</td><td  align='center' bgcolor='#a9a9a9'>部件名称</td><td  align='center' bgcolor='#a9a9a9'>生产备注</td><td  align='center' bgcolor='#a9a9a9'>说明</td></tr>";
    th=th+" </thead>  ";

    var data=[];
    var productInvCode="";
    for (var i = 0; i <result.length; i++) {
        if(productInvCode=="")
        {

            productInvCode=result[i].productInvCode;
            var m={
                psCode:result[i].productInvCode,
                invName:result[i].productInvName,
                qty:result[i].soQty,
            };
            data.push(m);
        }
        else
        {
            if(productInvCode!=result[i].productInvCode)
            {
                productInvCode=result[i].productInvCode;
                var m={
                    psCode:result[i].productInvCode,
                    invName:result[i].productInvName,
                    qty:result[i].soQty,
                };
                data.push(m);
            }

        }

    }
    var td="";


    if(data.length<2)
    {
        //表格数据加载
        for (var i = 0; i <result.length; i++) {
            td = td+"<tr style='height:37px'><td style='width:30px;' align='center'>"+(i+1)+"</td><td style='width:120px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].psCode) + "</td><td style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].invAddCode) +"</td><td style='width:155px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].invName) + "</td><td  style='width:100px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].invStd) + "</td><td style='width:27px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].invUnit) + "</td><td style='width:27px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].qty) + "</td><td style='width:42px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].qtys) + "</td> <td style='width:45px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].curQty) + "</td><td style='width:40px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].poQty) + "</td><td style='width:40px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].kyQty) + "</td><td style='width:55px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].proType) + "</td><td style='width:155px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].matSize) + "</td><td style='width:140px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].minvName) + "</td><td style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].minvStd) + "</td><td style='width:120px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].partName) + "</td><td  style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].memo) + "</td><td style='width:30px;word-wrap:break-word;word-break:break-all;' align='center'></td></tr>"
        }
    }
    else
    {
        var productInvCode="";
        //表格数据加载
        for (var i = 0; i <result.length; i++) {
            if(productInvCode=="")
            {
                productInvCode=result[i].productInvCode;

                td = td+"<tr style='height:37px'><td></td><td style='word-wrap:break-word;word-break:break-all;'  align='center' colspan='2'>" + isNullValues(result[i].productInvCode) + "</td><td style='word-wrap:break-word;word-break:break-all;' align='center' colspan='4'>" + isNullValues(result[i].productInvName) + "</td> <td  align='center'>" + isNullValues(result[i].soQty) + "</td> <td ></td><td ></td><td ></td><td></td><td></td><td></td><td></td><td></td><td ></td></tr>"
            }
            else
            {
                if(productInvCode!=result[i].productInvCode)
                {
                    productInvCode=result[i].productInvCode;
                    td = td+"<tr style='height:37px'><td></td><td style='word-wrap:break-word;word-break:break-all;'  align='center' colspan='2'>" + isNullValues(result[i].productInvCode) + "</td><td style='word-wrap:break-word;word-break:break-all;' align='center' colspan='4'>" + isNullValues(result[i].productInvName) + "</td>  <td  align='center'>" + isNullValues(result[i].soQty) + "</td> <td ></td><td ></td><td ></td><td></td><td></td><td></td><td></td><td></td><td ></td></tr>"
                }

            }

            td = td+"<tr style='height:37px'><td style='width:30px;' align='center'>"+(i+1)+"</td><td style='width:120px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].psCode) + "</td><td style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].invAddCode) +"</td><td style='width:155px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].invName) + "</td><td  style='width:100px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].invStd) + "</td><td style='width:27px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].invUnit) + "</td><td style='width:27px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].qty) + "</td><td style='width:42px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].qtys) + "</td> <td style='width:45px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].curQty) + "</td><td style='width:40px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].poQty) + "</td><td style='width:40px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].kyQty) + "</td><td style='width:55px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].proType) + "</td><td style='width:155px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].matSize) + "</td><td style='width:140px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].minvName) + "</td><td style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].minvStd) + "</td><td style='width:120px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].partName) + "</td><td  style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].memo) + "</td><td style='width:30px;word-wrap:break-word;word-break:break-all;' align='center'></td></tr>"
        }
    }




    //尾部的总页数
    var tf="<tfoot><TD  colspan=9 tdata='pageNO' format='#' align='left'> <p align='center'><b>第<font color='#0000FF'>#</font>页</b></p> </TD> <TD  colspan=9  tdata='pageCount' format='#' align='right'> <p align='center'><b>总<font color='#0000FF'>##</font>页</b></TD></tfoot>";

    var txt = css +th+ td+tf+"</table>";
    return txt;

}

function isNullValues(str) {
    if(str == null){
        return "";
    }else{
        return str;
    }

}



//打印委外单明细
function PrPrintOmDetail(m,list) {

    if(m!=null )
    {

        LODOP = getLodop();

        LODOP.PRINT_INITA(0,0,800,1100,"外协加工下料清单");
        LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");

        LODOP.ADD_PRINT_TEXT(22,271,302,33,"瑞邦智能装备股份有限公司");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
        LODOP.SET_PRINT_STYLEA(0,"Bold",1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);


        LODOP.ADD_PRINT_TEXT(54,30,233,33,isNullValues(m.ccode) );
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(47,350,302,33,"外协订单");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",13);
        LODOP.SET_PRINT_STYLEA(0,"Bold",1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(80,30,426,33,"产品名称："+isNullValues(m.cmemo));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.SET_PRINT_STYLEA(0,"LineSpacing",-5);

        LODOP.ADD_PRINT_TEXT(74,470,233,33,"订单号："+isNullValues(m.cdefine2) );
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(92,470,204,33,"数量：1批");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(112,29,338,33,"要求完成时间："+isNullValues(m.darrivedate)+"必须完成" );
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_TEXT(110,470,245,33,"运输方式："+isNullValues(m.cdefine14));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(132,29,338,33,"经办人签名："+isNullValues(m.cmaker));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(131,470,245,33,"经办人电话：13967399377");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(151,29,718,33,"加工单位："+isNullValues(m.cvenname));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(171,29,338,33,"联系人："+isNullValues(m.cvenperson));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(171,469,338,33,"联系电话："+isNullValues(m.cvenphone));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_TABLE(195,27,800,1000,CreateOmDetailTable(list));


        if(list==null|| (list!=null&&list.length<=23))
        {
            var height=195+23*30+100;

            LODOP.ADD_PRINT_TEXT(height,32,236,33,"技术要求：按图精心制作");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height+60,32,168,33,"分管领导签字：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height+20,32,169,33,"附加要求：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height+40,32,169,33,"部门主管签字：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height+40,533,169,33,"加工单位签字：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height+60,563,168,33,"公司盖章：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);

        }
        else
        {
            var height=400+list.length*30+180;
            LODOP.ADD_PRINT_TEXT(height,32,236,33,"技术要求：按图精心制作");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height+60,32,168,33,"分管领导签字：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height+20,32,169,33,"附加要求：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height+40,32,169,33,"部门主管签字：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height+40,533,169,33,"加工单位签字：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height+60,563,168,33,"公司盖章：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);


        }





        LODOP.SET_PRINT_MODE("FULL_WIDTH_FOR_OVERFLOW",true);

        // LODOP.PRINT_SETUP();

        //LODOP.PREVIEW();
        //LODOP.PRINT();
        // LODOP.PRINT_DESIGN();
        LODOP.PREVIEW();
    }

}
function  CreateOmDetailTable(result) {


    var lenth=0;
    if(result!=null)
    {
        lenth=result.length;
    }
    var css =" <style> table,td,th {border: 1px solid black;border-style: solid;border-collapse: collapse;font-size: 13px;}</style><table border=1>";
    //第一行
    var th = " <thead> <tr style='height:30px' ><td  align='center' bgcolor='#a9a9a9'>序号</td><td  align='center' bgcolor='#a9a9a9'>图号</td><td align='center' bgcolor='#a9a9a9'>零件名称</td><td  align='center' bgcolor='#a9a9a9'>数量</td><td align='center' bgcolor='#a9a9a9'>单件加工费</td><td  align='center' bgcolor='#a9a9a9'>加工费合计</td><td  align='center' bgcolor='#a9a9a9'>备注</td></tr>";
    th=th+" </thead>  ";

    var td="";
    var sum=0,sumamount=0;
    //表格数据加载
    for (var i = 0; i <lenth; i++) {
        sum=sum+ (result[i].iquantity-0);
        sumamount=sumamount+(result[i].inatsum-0);
        td = td+"<tr style='height:30px'><td style='width:20px;' align='center'>"+(i+1)+"</td><td style='width:180px;word-wrap:break-word;word-break:break-all;'  align='left'>" + isNullValues(result[i].cinvcode) + "</td><td style='width:250px;word-wrap:break-word;word-break:break-all;' align='left'>" + isNullValues(result[i].cinvname) +"</td><td style='width:60px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].iquantity) + "</td><td style='width:90px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].itaxprice) + "</td><td  style='width:90px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].inatsum) + "</td> <td style='width:45px;  align='center'>"+''+"</td></tr>"
    }
    //表格数据加载
    for (var i = lenth; i<=22; i++) {
        td = td+"<tr style='height:30px'><td  align='center'>"+''+"</td><td   align='center'>"+''+"</td><td    align='center'>"+''+"</td><td   align='center'>"+''+"</td><td   align='center'>"+''+"</td><td   align='center'>"+''+"</td><td   align='center'>"+''+"</td></tr>"
    }
    td = td+"<tr style='height:30px'><td colspan=3  align='center'>"+'合计'+"</td> <td  align='center'>"+sum+"</td><td    align='center'>"+''+"</td><td    align='center'>"+sumamount+"</td><td style='width:35px;  align='center'>"+''+"</td></tr>"

    var txt = css +th+ td+"</table>";

    return txt;

}

//打印委外单列表
function PrPrintOmList(m,list) {

    if(m!=null )
    {

        LODOP = getLodop();

        LODOP.PRINT_INITA(0,0,800,1100,"外协加工下料清单");
        LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");

        LODOP.ADD_PRINT_TEXT(22,271,302,33,"瑞邦智能装备股份有限公司");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
        LODOP.SET_PRINT_STYLEA(0,"Bold",1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_TEXT(45,301,302,33,"外协加工下料清单");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",13);
        LODOP.SET_PRINT_STYLEA(0,"Bold",1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_TEXT(75,28,330,33,"下单日期："+m.ddate);
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_TEXT(97,29,337,33,"外协单位："+isNullValues(m.cvenabbname)  );
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_TEXT(75,360,233,33,"外协订单号："+isNullValues(m.cdefine2) );
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_TEXT(97,361,204,33,"联系人："+isNullValues(m.cvenperson) );
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_TEXT(97,584,204,33,"电话："+isNullValues(m.cvenphone) );
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_TEXT(121,29,767,33,"备注："+isNullValues(m.cmemo) );
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);


        LODOP.ADD_PRINT_TABLE(145,29,800,1000,CreateOmListTable(list));


        if(list==null|| (list!=null&&list.length<=22))
        {
            var height=145+21*36+120;
            LODOP.ADD_PRINT_TEXT(height,127,236,33,"制单人："+isNullValues(m.cmaker)  );
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height,530,168,33,"客户签字：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height,354,169,33,"仓库发货：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        }
        else
        {
            var height=290+list.length*36+120;
            LODOP.ADD_PRINT_TEXT(height,127,236,33,"制单人："+isNullValues(m.cmaker) );
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
            LODOP.ADD_PRINT_TEXT(height,530,168,33,"客户签字：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);

            LODOP.ADD_PRINT_TEXT(height,354,169,33,"仓库发货：");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",11);

        }





        LODOP.SET_PRINT_MODE("FULL_WIDTH_FOR_OVERFLOW",true);
        // if(result!=null&&result.length>0)
        // {
        //
        // }


        //LODOP.PRINT_SETUP();

        //LODOP.PREVIEW();
        //LODOP.PRINT();
        // LODOP.PRINT_DESIGN();
        LODOP.PREVIEW();
    }

}
function  CreateOmListTable(result) {


    var lenth=0;
    if(result!=null)
    {
        lenth=result.length;
    }
    var css =" <style> table,td,th {border: 1px solid black;border-style: solid;border-collapse: collapse;font-size: 13px;}</style><table border=1>";
    //第一行
    var th = " <thead> <tr style='height:30px' ><td  align='center' bgcolor='#a9a9a9'>序号</td><td  align='center' bgcolor='#a9a9a9'>图号</td><td align='center' bgcolor='#a9a9a9'>零件名称</td><td  align='center' bgcolor='#a9a9a9'>材料名称</td><td align='center' bgcolor='#a9a9a9'>材料规格</td><td  align='center' bgcolor='#a9a9a9'>下料尺寸</td><td  align='center' bgcolor='#a9a9a9'>数量</td> <td  align='center' bgcolor='#a9a9a9'>发货日期</td><td  align='center' bgcolor='#a9a9a9'>备注</td></tr>";
    th=th+" </thead>  ";

    var td="";
    var sum=0;
    //表格数据加载
    for (var i = 0; i <lenth; i++) {
        sum=sum+ (result[i].iquantity-0);
        td = td+"<tr style='height:36px'><td style='width:20px;' align='center'>"+(i+1)+"</td><td style='width:110px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].cinvcode) + "</td><td style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].cinvname) +"</td><td style='width:100px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].cinvnames) + "</td><td  style='width:100px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].cinvstds) + "</td><td style='width:110px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].cdefine22) + "</td><td style='width:50px;word-wrap:break-word;word-break:break-all;'  align='center'>" + isNullValues(result[i].iquantity) + "</td><td style='width:70px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(result[i].darrivedate) + "</td> <td style='width:35px;  align='center'>"+''+"</td></tr>"
    }
    //表格数据加载
    for (var i = lenth; i<=21; i++) {
        td = td+"<tr style='height:36px'><td style='width:20px;' align='center'>"+''+"</td><td style='width:110px;word-wrap:break-word;word-break:break-all;'  align='center'>"+''+"</td><td style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>"+''+"</td><td style='width:100px;word-wrap:break-word;word-break:break-all;' align='center'>"+''+"</td><td  style='width:100px;word-wrap:break-word;word-break:break-all;' align='center'>"+''+"</td><td style='width:110px;word-wrap:break-word;word-break:break-all;'  align='center'>"+''+"</td><td style='width:50px;word-wrap:break-word;word-break:break-all;'  align='center'>"+''+"</td><td style='width:70px;word-wrap:break-word;word-break:break-all;' align='center'>"+''+"</td> <td style='width:35px;  align='center'>"+''+"</td></tr>"
    }
    td = td+"<tr style='height:21px'><td style='width:20px;' align='center'>"+''+"</td><td style='width:110px;word-wrap:break-word;word-break:break-all;'  align='center'>"+'合计'+"</td><td style='width:110px;word-wrap:break-word;word-break:break-all;' align='center'>"+''+"</td><td style='width:100px;word-wrap:break-word;word-break:break-all;' align='center'>"+''+"</td><td  style='width:100px;word-wrap:break-word;word-break:break-all;' align='center'>"+''+"</td><td style='width:110px;word-wrap:break-word;word-break:break-all;'  align='center'>"+''+"</td><td style='width:50px;word-wrap:break-word;word-break:break-all;'  align='center'>"+sum+"</td><td style='width:70px;word-wrap:break-word;word-break:break-all;' align='center'>"+''+"</td> <td style='width:35px;  align='center'>"+''+"</td></tr>"

    var txt = css +th+ td+"</table>";

    return txt;

}
