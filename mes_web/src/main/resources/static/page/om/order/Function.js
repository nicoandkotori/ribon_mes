
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
 * 控制台输出
 */
function consoleLog(msg){
    if (DEBUG_MODEL){
        console.log(msg);
    }
}

/*
* 选择产品信息回调函数
*/
function chooseInventoryCallBack(u8Data){
    //根据选择物料类型来为产品表或材料表赋值，不算优雅的设计，但受限于框架没有更好的实现方式
    switch (chooseInvType) {
        case 'product':
            consoleLog("选择产品信息");
            productHelper.saveCell(CurRow,CurCol);
            materialHelper.saveCell(CurRow1,CurCol1);
            let mesProduct = new omMesProduct();
            mesProduct.setEntity(productHelper.getSelectedRowData());
            //获取产品表选中行数据
            mesProduct.setDataFromU8Data(u8Data);
            //设置产品表当前行数据
            productHelper.setSelectedRowData(mesProduct);
            //同步设置材料表相关联的行数据
            let selectedProductRowData = productHelper.getSelectedRowData();
            let partIdArray = materialHelper.getAllRowsId();
            partIdArray.forEach(function (id) {
                let rowData = materialHelper.getRowDataById(id);
                if (rowData.recordId === selectedProductRowData.recordId){
                    materialHelper.setRowDataById(id,mesProduct);
                }
            })
            break;
        case 'material':
            materialHelper.saveCell(CurRow1,CurCol1);
            let selectedMaterialRow = materialHelper.getSelectedRowData();
            let materialObj = new omMesMaterial();
            materialObj.setEntity(selectedMaterialRow);
            var iprice=0;
            $.ajax({
                type: "post",
                async: false,
                dataType: "json",
                url: "/basicinfo/inventory/getprice",
                data: {cinvcode:u8Data.cinvcode},
                success: function (data) {
                    iprice=parseFloat(data- 0).toFixed(2);
                    u8Data.cdefine26 = iprice;
                    //转换u8字段
                    materialObj.setMaterialInfoFromU8Data(u8Data);
                    //选中记录赋值
                    materialHelper.setSelectedRowData(materialObj);
                    sumMaterialPriceToProduct(materialObj.getRecordId())

                },

            });

            break;
        default:
            break
    }


}

/*
 * 添加部件点击事件
 */
function addPart(){
    consoleLog("打开添加部件窗口");
    productHelper.saveCell(CurRow,CurCol);
    let id = productHelper.getSelectedRowId();
    if (id === undefined){
        return;
    }
    //产品信息
    let productData = productHelper.getSelectedRowData();
    //当前产品的部件信息
    let partList = partDataMap.get(getPartTableId(id));
    //当前产品部件的材料信息
    let materialList = [];
    if (partList != null){
        partList.forEach(function (part) {
            let partRowId = part.partRowId;
            let allMaterial = materialHelper.getAllRowData();
            allMaterial.forEach(function (material) {
                if (partRowId === material.partRowId){
                    materialList.push(material);
                }
            })
        })
    }

    let param = {
        productData:productData,
        partList:partList,
        materialList:materialList
    }
    addPartParams = param;
    WeAdminShow('添加部件','./addpart.html');
}

/*
 * 增加部件表回调函数
 * @param product,产品表记录信息
 * @param partArray,部件记录数组
 * @param materialArray,材料记录数组
 * @param otherParams,其它参数
 */
function addPartCallBack(product,partArray,materialArray,otherParams){
    //产品表赋值
    productHelper.setSelectedRowData(product)
    //部件表赋值
    let partTableId = getPartTableId(productHelper.getSelectedRowId());
    consoleLog("增加部件表-部件赋值-部件表id："+partTableId);
    partDataMap.set(partTableId,partArray);
    array = partArray;
    let productObj = new omMesProduct();
    productObj.setEntity(product);
    //合计该产品下所有材料的材料单价和单件材料费
    sumMaterialPriceToProduct(productObj.getRecordId())
    consoleLog("增加部件表-带回材料表数据↓");
    consoleLog(materialArray);
    //删除材料表中partRowId已存在的记录
    consoleLog("增加部件表-删除材料表中partRowId已存在的记录");
    //先删除
    materialArray.forEach(function (newMaterial){
        let allOldMaterial = materialHelper.getAllRowData();
        allOldMaterial.forEach(function (oldMaterial) {
            if (newMaterial.partRowId === oldMaterial.partRowId){
                materialHelper.deleteRowById(oldMaterial.rowId)
            }
        })
    })
    //再增行
    materialArray.forEach(function (material) {
        addMaterialRow(material);
    })
}

/*
 * 产品表保存单元格后函数
 */
function productAfterSaveCell(rowid, name, val, iRow, iCol) {
    //同步更新所有开始日期
    if (name == PLAN_START_DATE) {
        var ids = productHelper.getAllRowsId();
        let productObj = new omMesProduct();
        for (var i = 0; i < ids.length; i++) {
            productObj.setEntity(productHelper.getRowDataById(ids[i]));
            productObj.setPlanStartDate(val);
            productHelper.setRowDataById(ids[i],productObj);
        }

    }
    //同步更新所有结束日期
    if (name == PLAN_END_DATE) {
        let ids = productHelper.getAllRowsId();
        let productObj = new omMesProduct();
        for (var i = 0; i < ids.length; i++) {
            productObj.setEntity(productHelper.getRowDataById(ids[i]));
            productObj.setPlanEndDate(val);
            productHelper.setRowDataById(ids[i],productObj);
        }

    }
    if (name == PRODUCT_INV_CODE) {
        SetInventory(rowid, "1");
    } else if (name == PRODUCT_QTY) {
        consoleLog("修改了数量！");
        //同步更新该产品下材料的数量字段
        updateMaterialProductQty(rowid);
        //更新加工费合计
        updateTotalWorkAmount(rowid);
        //更新合计
        updateAmount(rowid);
    } else if (name == WORK_PRICE || name == MATERIAL_AMOUNT) {
        updatePrice(rowid);
        updateTotalWorkAmount(rowid);
    } else if (name == PRICE){
        updateAmount(rowid);
    }

}

/*
 * 材料表afterSaveCell
 */
function materialAfterSaveCell(rowid, name, val, iRow, iCol) {
    consoleLog("保存单元格name:"+name);
    if (name == INV_CODE) {
        SetInventory(rowid, "2");
    } else if (name == UNIT_MATERIAL_PRICE ||  name == UNIT_MATERIAL_AMOUNT ||name == INV_LAND || name == INV_LEN || name == INV_WIDTH || name == INV_EXTERNAL_DIAMETER || name == INV_INTERNAL_DIAMETER || name == INV_DENSITY ) {
        //更新单耗
        updateIqty(rowid);
        //更新下料尺寸
        updateInvSize(rowid);
    } else if (name == IQTY) {
        //手动修改单耗后
        //更新单件材料费
        updateUnitMaterialAmount(rowid);
        //更新总量
        updateTqty(rowid);
    }

}

//设置存货信息
function SetInventory(rowid, itype) {
    consoleLog("设置存货信息");
    if (itype == "1") {
        var rowData = $("#jqGrid").jqGrid('getRowData', rowid);
        let productObj = new omMesProduct();
        productObj.setEntity(productHelper.getRowDataById(rowid));
        let query = new U8QueryDTO(productObj);
        $.ajax({
            type: "get",
            async: false,
            url: "/om/metalworkcommittee/gethistorybycinvcode1",
            data: query,
            success: function (data) {
                if (data["mData"] != null && data["mData"] != "") {
                    data["mData"].recordId = productObj.getRecordId();
                    productHelper.setRowDataById(rowid,data["mData"])
                    var ids = materialHelper.getAllRowsId();
                    var izExist = false;
                    let materialObj = new omMesMaterial();
                    for (var i = 0; i < ids.length; i++) {
                        materialObj.setEntity(materialHelper.getRowDataById(ids[i]))
                        if (materialObj.getRecordId() == productObj.getRecordId()) {
                            izExist = true;
                            materialObj.setProductInfoFromU8Data(data["mData"])
                            materialHelper.setRowDataById(ids[i],materialObj);
//                                    $("#jqGridDetail").jqGrid("delRowData", ids[i]);
                        }
                    }

                    if (izExist == false) {
                        if (data["mDatas"] != null && data["mDatas"] != "") {
                            var gridData = $("#jqGridDetail").jqGrid("getRowData");
                            for (var i = 0; i < data["mDatas"].length; i++) {
                                data["mDatas"][i].recordId = rowData.recordId;
                                irowid = irowid + 1;
                                $("#jqGridDetail").jqGrid("addRowData", irowid, data["mDatas"][i]);


                            }
                        }


                    }


                }


            },
        });
    } else {
        let query = new U8QueryDTO(materialHelper.getRowDataById(rowid));
        $.ajax({
            type: "get",
            async: false,
            url: "/basicinfo/inventory/getbyid",
            data: query,
            success: function (data) {
                if (data != null && data != "") {
                    data.cdefine26 = parseFloat(data.iinvncost - 0).toFixed(2);
                    let materialObj = new omMesMaterial();
                    materialObj.setEntity(materialHelper.getRowDataById(rowid));
                    materialObj.setMaterialInfoFromU8Data(data);
                    materialHelper.setRowDataById(rowid,materialObj);
                } else {
                    materialHelper.setRowDataById(rowid,new omMesMaterial());
                }

            },
        });
        updateInvSize(rowid);
    }


}

//------字段更新函数链-----

/*
 * 更新产品表合计字段
 */
function updateAmount(rowId){
    consoleLog("更新产品表合计字段");
    let productRow = productHelper.getRowDataById(rowId);
    let productObj = new omMesProduct();
    productObj.setEntity(productRow);
    let price = getStringDecimal(productObj.getPrice());
    let productQty = getStringDecimal(productObj.getProductQty());
    productObj.setEntity(productRow);
    productObj.setAmount((price * productQty).toFixed(2));
    productHelper.setRowDataById(rowId,productObj);
}

//修改数量后，同步更新该产品下材料的数量字段
function updateMaterialProductQty(rowid) {
    let product = new omMesProduct();
    product.setEntity(productHelper.getRowDataById(rowid));
    let productQty = getStringDecimal(product.getProductQty());
    let ids = materialHelper.getAllRowsId();
    for (let i = 0; i < ids.length; i++) {
        let productMaterialRow = materialHelper.getRowDataById(ids[i]);
        let productMaterialObj = new omMesMaterial();
        productMaterialObj.setEntity(productMaterialRow);
        if (productMaterialObj.getRecordId() === product.getRecordId()) {
            //获得产品数量
            let materialQty = getStringDecimal(productQty);
            //获得材料的单耗
            let iqty = getStringDecimal(productMaterialObj.getIqty());
            //同步材料表数量
            productMaterialObj.setProductQty(materialQty);
            //计算材料表总量
            productMaterialObj.setTqty((materialQty * iqty).toFixed(2));
            consoleLog(productMaterialObj);
            materialHelper.setRowDataById(ids[i],productMaterialObj);
        }


    }
}

/*
 * 更新加工费合计字段
 */
function updateTotalWorkAmount(rowId){
    let productRow = productHelper.getRowDataById(rowId);
    let productObj = new omMesProduct();
    productObj.setEntity(productRow);
    let productQty = getNumberDecimal(productObj.getProductQty());
    let price = getNumberDecimal(productObj.getPrice());
    let totalWorkAmount =( productQty * price).toFixed(2);
    productObj.setTotalWorkAmount(totalWorkAmount);
    productHelper.setRowDataById(rowId,productObj);
}

/*
 * 合计某产品下所有材料的材料单价和单件材料费
 */
function sumMaterialPriceToProduct(recordId){
    if (recordId === undefined){
        layer.alert("recordId不能为空");
        return;
    }
    //查询对应的产品记录
    var idsMain = productHelper.getAllRowsId();
    var rowMainId = "";
    var rowMain = new omMesProduct();
    for (let i = 0; i < idsMain.length; i++) {
        var rowData1 = $("#jqGrid").getRowData(idsMain[i]);
        if (rowData1.recordId == recordId) {
            rowMainId = idsMain[i];
            rowMain.setEntity(rowData1);
            break;
        }
    }
    //循环子表合计材料单价和单件材料费
    var totalUnitMaterialPrice = 0, totalUnitMaterialAmount = 0;
    var ids = materialHelper.getAllRowsId();
    let materialObjForTotalPrice = new omMesMaterial();
    for (var i = 0; i < ids.length; i++) {
        let materialRow = materialHelper.getRowDataById(ids[i]);
        materialObjForTotalPrice.setEntity(materialRow);
        if (materialRow.recordId ===recordId) {
            totalUnitMaterialPrice = totalUnitMaterialPrice + parseFloat(materialObjForTotalPrice.getUnitMaterialPrice() - 0);
            totalUnitMaterialAmount = totalUnitMaterialAmount + parseFloat(materialObjForTotalPrice.getUnitMaterialAmount() - 0);
        }
    }

    if (rowMain != null) {
        //单件加工费
        let workPrice = getStringDecimal(rowMain.getWorkPrice())
        var price = ((totalUnitMaterialAmount - 0) + (workPrice - 0)) - 0;
        rowMain.setMaterialPrice(totalUnitMaterialPrice.toFixed(2));
        rowMain.setMaterialAmount(totalUnitMaterialAmount.toFixed(2));
        rowMain.setPrice(price);
        rowMain.setAmount((price * rowMain.getProductQty).toFixed(2));
        productHelper.setRowDataById(rowMainId,rowMain);
        //更新单件价格字段
        updatePrice(rowMainId)
    }
}

/*
 * 更新单件价格字段
 */
function updatePrice(rowId){
    let productRow = productHelper.getRowDataById(rowId);
    let productObj = new omMesProduct();
    productObj.setEntity(productRow);
    let materialAmount = getNumberDecimal(productObj.getMaterialAmount());
    let workPrice = getNumberDecimal(productObj.getWorkPrice());
    productObj.setPrice(materialAmount+workPrice)
    productHelper.setRowDataById(rowId,productObj);
    updateAmount(rowId);
}

/*
 * 更新单耗字段
 */
function updateIqty(rowId){
    consoleLog("更新单耗字段");
    let rowData = materialHelper.getRowDataById(rowId);
    let materialObj = new omMesMaterial();
    materialObj.setEntity(rowData);
    //长
    let invLen = getStringDecimal(materialObj.getInvLen());
    //厚
    let invLand = getStringDecimal(materialObj.getInvLand());
    //宽
    let invWidth = getStringDecimal(materialObj.getInvWidth());
    //外径
    let externalDiameter = getStringDecimal(materialObj.getInvExternalDiameter());
    //内径
    let internalDiameter = getStringDecimal(materialObj.getInvInternalDiameter());
    //密度
    let density = getStringDecimal(materialObj.getInvDensity());
    //计算单耗
    var iqty = ((invLen * invWidth + 3.1415926 / 4 * (externalDiameter * externalDiameter - internalDiameter * internalDiameter)) * invLand / 1000000 * density).toFixed(2) - 0;
    let query = new U8QueryDTO(materialObj);
    $.ajax({
        type: "get",
        async: false,
        url: "/basicinfo/inventory/getbyid",
        data: query,
        success: function (data) {
            if (data != null && data != "") {

                if ((iqty == null || iqty == 0 || iqty == "0")) {
                    if (data.cinvdefine1 == "A" || data.cinvdefine1 == "a") {
                        iqty = parseFloat((invLand * invLen * invWidth * density / 1000000).toFixed(3));
                    } else if (data.cinvdefine1 == "B" || data.cinvdefine1 == "b") {
                        iqty = parseFloat((invLen * density * (externalDiameter * externalDiameter - internalDiameter * internalDiameter) * 3.1415926 / 4 / 1000000).toFixed(3));

                    } else if (data.cinvdefine1 == "C" || data.cinvdefine1 == "c") {
                        iqty = parseFloat((invLen / 1000).toFixed(3));
                    } else if (data.cinvdefine1 == "D" || data.cinvdefine1 == "d") {
                        iqty = parseFloat((invLen * invWidth / 1000000).toFixed(3));
                    } else if (data.cinvdefine1 == "E" || data.cinvdefine1 == "e") {
                        iqty = parseFloat((invLen / 1000).toFixed(3));
                    } else if (data.cinvdefine1 == "F" || data.cinvdefine1 == "f") {
                        iqty = parseFloat((invLand / 1000 * invLen / 1000 * invWidth / 1000 * 1).toFixed(3));
                    } else if (data.cinvdefine1 == "G" || data.cinvdefine1 == "g") {
                        iqty = parseFloat((invLen / 1000 * 1).toFixed(3));
                    }
                }
            }
        },
    });
    materialObj.setIqty(iqty);
    materialHelper.setRowDataById(rowId,materialObj);
    updateUnitMaterialAmount(rowId);
    updateTqty(rowId);
}

/*
 * 更新单件材料费
 */
function updateUnitMaterialAmount(rowid){
    consoleLog("更新单件材料费");
    let rowData = materialHelper.getRowDataById(rowid);
    let materialObj = new omMesMaterial();
    materialObj.setEntity(rowData);
    let unitMaterialPrice = getNumberDecimal(materialObj.getUnitMaterialPrice());
    let iqty = getNumberDecimal(materialObj.getIqty());
    let unitMaterialAmount = (unitMaterialPrice * iqty).toFixed(2);
    materialObj.setUnitMaterialAmount(unitMaterialAmount);
    materialHelper.setRowDataById(rowid,materialObj);
    //循环子表合计材料单价和单件材料费
    sumMaterialPriceToProduct(materialObj.getRecordId());
}

//设置下料尺寸
function updateInvSize(rowid) {
    consoleLog("设置下料尺寸！");
    let rowData = materialHelper.getRowDataById(rowid);
    let materialObj = new omMesMaterial();
    materialObj.setEntity(rowData);
    var strSize = "";
    //厚度
    let invLand = getStringDecimal(materialObj.getInvLand());
    if (invLand !== 0){
        strSize = "δ" + materialObj.getInvLand();
    }
    //长
    let invLen = getStringDecimal(materialObj.getInvLen());
    if (invLen !== 0){
        if (strSize != "") {
            strSize = strSize + ",";
        }
        strSize = strSize + "L" + materialObj.getInvLen();
    }
    //宽
    let invWidth = getStringDecimal(materialObj.getInvWidth());
    if (invWidth !== 0){
        if (strSize != "") {
            strSize = strSize + ",";
        }
        strSize = strSize + "W" + materialObj.getInvWidth();
    }
    //外径
    let externalDiameter = getStringDecimal(materialObj.getInvExternalDiameter());
    if (externalDiameter !== 0){
        if (strSize != "") {
            strSize = strSize + ",";
        }
        strSize = strSize + "外φ" + materialObj.getInvExternalDiameter();
    }
    //内径
    let internalDiameter = getStringDecimal(materialObj.getInvInternalDiameter());
    if (internalDiameter !== 0){
        if (strSize != "") {
            strSize = strSize + ",";
        }
        strSize = strSize + "内φ" + materialObj.getInvExternalDiameter();
    }
    materialObj.setInvSize(strSize);
    materialHelper.setRowDataById(rowid,materialObj)
}

//更新总量
function updateTqty(rowid) {
    consoleLog("修改单耗后同步修改总量");
    let materialObj = new omMesMaterial();
    materialObj.setEntity(materialHelper.getRowDataById(rowid));
    //数量
    let productQty = getNumberDecimal(materialObj.getProductQty());
    //单耗
    let iqty = getNumberDecimal(materialObj.getIqty());
    materialObj.setTqty((productQty * iqty).toFixed(2));
    consoleLog(materialObj);
    materialHelper.setRowDataById(rowid,materialObj);

}



/*
 * 点击按钮产品表增行事件
 */
function btnAddProductRow() {
    let productObj = new omMesProduct();
    productObj.setPlanStartDate(planStartDate);
    productObj.setPlanEndDate(planEndDate);
    productObj.setRecordId("r"+productMaxId);
    productHelper.saveCell(CurRow,CurCol);
    addProductRow(productObj);
}

/*
 * 产品表增行
 */
function addProductRow(rowData){
    consoleLog("产品表增行");
    let productObj = new omMesProduct();
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
    consoleLog("点击材料表增行按钮事件");
    productHelper.saveCell(CurRow,CurCol);
    //获取产品表选中行数据
    let productSelectedRowData = productHelper.getSelectedRowData();
    if (productSelectedRowData === undefined){
        return;
    }
    let materialObj = new omMesMaterial();
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
    consoleLog("材料表删行");
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
