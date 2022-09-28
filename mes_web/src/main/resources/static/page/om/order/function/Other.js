
/*
 * 将string转换为string类型的小数
 * 若入参不为空，则在小数点后保留两位小数，否则为0
 * @return String result
 */
function getStringDecimal(num){
    let result = '0';
    if (num !== "" && num != null) {
        result = parseFloat(num).toFixed(2);
    }
    return result;
}

/*
 * 将string转换为number类型的小数
 * @return Number result
 */
function getNumberDecimal(num){
    let result = 0;
    if (num !== "" && num != null) {
        result = parseFloat(num).toFixed(2)-0;
    }
    return result;
}



/*
 * 添加部件点击事件
 */
function addPart(){
    console.debug("打开添加部件窗口");
    productHelper.saveCell(CurRow,CurCol);
    let id = productHelper.getSelectedRowId();
    if (id === undefined){
        return;
    }
    //产品信息
    let productData = productHelper.getSelectedRowData();
    //传给部件窗口的部件数据，这里需要没有去重的部件数据
    let partList = getPartDataByPartTableId(getPartTableId(id),partTableIdWithRecordIdMap,recordIdWithPartDataMap);
    //当前产品部件的材料信息
    let materialList = [];
    //当前产品部件的材料信息，这里需要根据已经去重的部件数据去获取
    let disdinctPartList = getPartDataByPartTableId(getPartTableId(id),partTableIdWithRecordIdMap,recordIdWithDistinctPartDataMap)
    if (disdinctPartList != null){
        disdinctPartList.forEach(function (part) {
            let partRowId = part.partRowId;
            let allMaterial = materialHelper.getAllRowData();
            allMaterial.forEach(function (material) {
                if (partRowId === material.partRowId){
                    materialList.push(material);
                }
            })
        })
    }
    //传递数据给打开的部件窗口
    let param = {
        productData:productData,
        partList:partList,
        materialList:materialList
    }
    addPartParams = param;
    WeAdminShow('添加部件','./addpart.html');
}



/*
 * 点击按钮产品表增行事件
 */
function btnAddProductRow() {
    let productObj = new OmMesProduct();
    productObj.setPlanStartDate(planStartDate);
    productObj.setPlanEndDate(planEndDate);
    let recordId = "r"+productMaxId;
    productObj.setRecordId(recordId);
    partTableIdWithRecordIdMap.set(getPartTableId(productMaxId),recordId)
    productHelper.saveCell(CurRow,CurCol);
    addProductRow(productObj);
}

/*
 * 产品表增行
 */
function addProductRow(rowData){
    console.debug("产品表增行");
    let productObj = new OmMesProduct();
    productObj.setEntity(rowData);
    productObj.setTaxRate(DEFAULT_TAX_TATE);
    productObj.setRowId(productMaxId);
    productHelper.addRowData(productMaxId,productObj);
    productMaxId++;
}

/*
  * 点击材料表增行按钮事件
  */
function btnAddMaterialRowEvent(){
    console.debug("点击材料表增行按钮事件");
    productHelper.saveCell(CurRow,CurCol);
    //获取产品表选中行数据
    let productSelectedRowData = productHelper.getSelectedRowData();
    if (productSelectedRowData === undefined){
        return;
    }
    let materialObj = new OmMesMaterial();
    materialObj.setEntity(productSelectedRowData);
    addMaterialRow(materialObj);
}

/*
 * 材料表增行
 */
function addMaterialRow(rowData){
    rowData.rowId = materialMaxId;
    materialHelper.addRowData(materialMaxId,rowData);
    materialMaxId++;
}

/*
  * 产品表删行
  */
function deleteProductRow(){
    //保存正在编辑的单元格
    productHelper.saveCell(CurRow,CurRow);
    let selectedProductData = productHelper.getSelectedRowData();
    if (selectedProductData === undefined){
        return;
    }
    let ableToDelete = true;
    let dataArray = materialHelper.getAllRowData();
    dataArray.forEach(function (material) {
        if (material.recordId === selectedProductData.recordId){
            layer.alert("请先删除该产品下的材料表记录");
            ableToDelete = false;
            return;
        }
    })
    if (ableToDelete){
        productHelper.deleteSelectedRow();
        productMaxId--;
    }

}

/*
 * 点击删行按钮材料表删行
 */
function btnDeleteMaterialRow(){
    console.debug("材料表删行");
    //保存正在编辑的单元格
    materialHelper.saveCell(CurRow1,CurCol1);
    let selectedRowId = materialHelper.getSelectedRowId();
    if (selectedRowId === undefined){
        return;
    }
    let selectedRowData = materialHelper.getRowDataById(selectedRowId);
    materialHelper.deleteSelectedRow();
    materialMaxId--;
    //循环子表合计材料单价和单件材料费
    sumMaterialPriceToProduct(selectedRowData.recordId);
}

/*
 * 获取部件表ID
 */
function getPartTableId(rowId){
    let tableId = "jqGrid_"+rowId+"_t";
    return tableId;
}


/*
 * 导出excel
 */
function exportToExcel(){
    let mesId = $("#id").val();
    window.location.href = encodeURI(URL_EXPORT+"?mesId="+mesId);
}

/*
 * 获取主表及其所有字表数据
 */
function getAllDataByMainId(mainId){
    sendGetReq(URL_GET_ALL_MAIN_DATA_BY_ID, {id: mainId}, function (result) {
        let data = result.data;

        if (data != null){
            console.debug("初始化数据-主表赋值");
            $("form").setForm(data.main);
            layui.form.render();
            //存放已经push的部件表数据的partRowId，用于去重
            let partRowIds = [];
            let productList = data.productList;
            let partList = data.partList;
            let materialList = data.materialList;
            //添加产品数据
            productList.forEach(function (product) {
                addProductRow(product);
            })
            //为每条产品设置对应的部件记录
            let allProductRow = productHelper.getAllRowData();
            allProductRow.forEach(function (productRow) {
                let productObj = getMesProductWithData(productRow);
                let recordId = productObj.getRecordId();
                let partTableId = getPartTableId(productObj.getRowId());
                //存放没有去重的部件数据
                let partArray = [];
                //存放已经去重的部件数据
                let distinctParArray = [];
                let partObj = new OmMesPart();
                //找出部件记录中部件ID和产品ID相同的记录
                partList.forEach(function (part) {
                    partObj.setEntity(part);
                    if (partObj.getDetailId() === productObj.getId()){
                        partArray.push(part);
                        //去重部件数据
                        if (!partRowIds.includes(partObj.getPartRowId())){
                            distinctParArray.push(part);
                        }
                        partRowIds.push(partObj.getPartRowId());
                    }
                })

                partTableIdWithRecordIdMap.set(partTableId,recordId);
                //如果recordId已经存在则不用继续设置map
                if (!recordIdWithPartDataMap.has(recordId)){
                    recordIdWithPartDataMap.set(recordId,partArray);
                }
                if (!recordIdWithDistinctPartDataMap.has(recordId)){
                    recordIdWithDistinctPartDataMap.set(recordId,distinctParArray);
                }

            })
            //添加材料数据
            materialList.forEach(function (material) {
                addMaterialRow(material);
            })
        }
    });
}

/*
 * 根据产品表ID获取产品表数据
 */
function getPartDataByPartTableId(partTableId,partTableIdWithRecordIdMap,recordIdWithPartDataMap){
    console.debug("根据产品表ID获取产品表数据")
    let recordId = partTableIdWithRecordIdMap.get(partTableId);
    let partData = recordIdWithPartDataMap.get(recordId);
    return partData;
}

/*
 * 将partTableIdWithRecordIdMap转换成array
 */
function convertPartDataMapToArray(){
    let partDataArray = [];
    for ([key,value] of recordIdWithPartDataMap){
        let array = value;
        array.forEach(function (row){
            partDataArray.push(row);
        })
    }
    return partDataArray;
}

/*
 * 打印
 */
function FnPrint() {
    console.debug("打印")
    var id = $("#id").val();
    if (id != null && id != "") {
        //获取主表信息
        $.get(URL_GET_MES_MAIN_BY_ID, {id: id}, function (data) {
            if (data != null) {
                let main = getMesMainWithData(data.result);
                let query = {
                    cvencode:main.getVenCode()
                }
                //获取供应商关联信息
                sendGetReq(URL_GET_VENDOR_BY_EQUAL_FIND,{page:1,limit:1,query:query},
                    function (data) {
                    if (data.result != null){
                        let vendorInfo = data.result;
                        //获取产品信息
                        $.ajax({
                            type: "get",
                            url: URL_GET_MES_PRODUCT_BY_MAIN_ID,
                            data: {mainId: id},
                            success: function (data1) {
                                let productList = data1.rows;
                                if(productList!=null&&productList.length>0)
                                {
                                    console.debug("订单表数据↓");
                                    console.debug(data.result);
                                    console.debug("产品表数据↓");
                                    console.debug(data1.rows);
                                    PrPrintOmDetail(main,vendorInfo, productList);
                                }


                            },
                            error: function () {
                            }
                        });
                    } else {
                        layer.msg(data.msg)
                    }
                })


            }
        });

    } else {
        layer.alert("请先保存！");
    }

}

//打印委外单明细
function PrPrintOmDetail(main,vendorInfo,productList) {
    let mainObj = getMesMainWithData(main);
    let productObjList = getMesProductListWithData(productList);
    console.debug("打印委外单明细");
    if(main!=null )
    {
        LODOP = getLodop();

        LODOP.PRINT_INITA(0,0,800,1100,"外协加工下料清单");
        LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");

        LODOP.ADD_PRINT_TEXT(22,271,302,33,"瑞邦智能装备股份有限公司");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
        LODOP.SET_PRINT_STYLEA(0,"Bold",1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);


        LODOP.ADD_PRINT_TEXT(54,30,233,33,isNullValues(mainObj.getVouchCode()) );
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(47,350,302,33,"外协订单");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",13);
        LODOP.SET_PRINT_STYLEA(0,"Bold",1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(80,30,426,33,"产品名称："+isNullValues(mainObj.getRemark()));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.SET_PRINT_STYLEA(0,"LineSpacing",-5);

        LODOP.ADD_PRINT_TEXT(74,470,233,33,"订单号："+isNullValues(mainObj.getContractOm()) );
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(92,470,204,33,"数量：1批");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(112,29,338,33,"要求完成时间："+isNullValues(productObjList[0].getPlanEndDate())+"必须完成" );
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_TEXT(110,470,245,33,"运输方式："+isNullValues(mainObj.getTransportWay()));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(132,29,338,33,"经办人签名："+isNullValues(mainObj.getCreateUser()));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(131,470,245,33,"经办人电话：13967399377");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(151,29,718,33,"加工单位："+isNullValues(mainObj.getVenName()));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(171,29,338,33,"联系人："+isNullValues(vendorInfo.cvenperson));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.ADD_PRINT_TEXT(171,469,338,33,"联系电话："+isNullValues(vendorInfo.cvenphone));
        LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

        LODOP.ADD_PRINT_TABLE(195,27,800,1000,CreateOmDetailTable(productObjList));


        if(productList==null|| (productList!=null&&productList.length<=23))
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
            var height=400+productList.length*30+180;
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
        LODOP.PREVIEW();
    }

}
/*
 * 加载产品表
 */
function  CreateOmDetailTable(productList) {

    var lenth=0;
    if(productList!=null)
    {
        lenth=productList.length;
    }
    var css =" <style> table,td,th {border: 1px solid black;border-style: solid;border-collapse: collapse;font-size: 13px;}</style><table border=1>";
    //第一行
    var th = " <thead> <tr style='height:30px' ><td  align='center' bgcolor='#a9a9a9'>序号</td><td  align='center' bgcolor='#a9a9a9'>图号</td><td align='center' bgcolor='#a9a9a9'>零件名称</td><td  align='center' bgcolor='#a9a9a9'>数量</td><td align='center' bgcolor='#a9a9a9'>单件加工费</td><td  align='center' bgcolor='#a9a9a9'>加工费合计</td><td  align='center' bgcolor='#a9a9a9'>备注</td></tr>";
    th=th+" </thead>  ";

    var td="";
    var sum=0,sumamount=0;
    //表格数据加载
    for (var i = 0; i <lenth; i++) {
        sum=sum+ (productList[i].getProductQty()-0);
        sumamount=sumamount+(productList[i].getTotalWorkAmount()-0);
        td = td+"<tr style='height:30px'><td style='width:20px;' align='center'>"+(i+1)+"</td><td style='width:180px;word-wrap:break-word;word-break:break-all;'  align='left'>" + isNullValues(productList[i].getProductInvCode()) + "</td><td style='width:250px;word-wrap:break-word;word-break:break-all;' align='left'>" + isNullValues(productList[i].getProductInvName()) +"</td><td style='width:60px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(productList[i].getProductQty()) + "</td><td style='width:90px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(productList[i].getWorkPrice()) + "</td><td  style='width:90px;word-wrap:break-word;word-break:break-all;' align='center'>" + isNullValues(productList[i].getTotalWorkAmount()) + "</td> <td style='width:45px;  align='center'>"+''+"</td></tr>"
    }
    //表格数据加载
    for (var i = lenth; i<=22; i++) {
        td = td+"<tr style='height:30px'><td  align='center'>"+''+"</td><td   align='center'>"+''+"</td><td    align='center'>"+''+"</td><td   align='center'>"+''+"</td><td   align='center'>"+''+"</td><td   align='center'>"+''+"</td><td   align='center'>"+''+"</td></tr>"
    }
    td = td+"<tr style='height:30px'><td colspan=3  align='center'>"+'合计'+"</td> <td  align='center'>"+sum+"</td><td    align='center'>"+''+"</td><td    align='center'>"+sumamount+"</td><td style='width:35px;  align='center'>"+''+"</td></tr>"

    var txt = css +th+ td+"</table>";

    return txt;

}

