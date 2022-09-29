/*
 * 存放与字段计算有关的函数
 */

/*
 * 产品表保存单元格后函数
 */
function productAfterSaveCell(rowid, name, val, iRow, iCol) {
    //同步更新所有开始日期
    if (name == PLAN_START_DATE) {
        var ids = productHelper.getAllRowsId();
        let productObj = getEmptyMesProduct();
        for (var i = 0; i < ids.length; i++) {
            productObj.setEntity(productHelper.getRowDataById(ids[i]));
            productObj.setPlanStartDate(val);
            productHelper.setRowDataById(ids[i], productObj);
        }

    }
    //同步更新所有结束日期
    if (name == PLAN_END_DATE) {
        let ids = productHelper.getAllRowsId();
        let productObj = getEmptyMesProduct();
        for (var i = 0; i < ids.length; i++) {
            productObj.setEntity(productHelper.getRowDataById(ids[i]));
            productObj.setPlanEndDate(val);
            productHelper.setRowDataById(ids[i], productObj);
        }

    }
    if (name == PRODUCT_INV_CODE) {
        SetInventory(rowid, "1");
    } else if (name == PRODUCT_QTY) {
        console.debug("修改了数量！");
        //同步更新该产品下材料的数量字段
        updateMaterialProductQty(rowid);
        //更新加工费合计
        updateTotalWorkAmount(rowid);
        //更新合计
        updateAmount(rowid);
    } else if (name == WORK_PRICE) {
        updatePrice(rowid);
        updateTotalWorkAmount(rowid);
        updateWorkPriceWithoutTax(rowid)
    } else if (name == PRICE) {
        updateAmount(rowid);
    }

}

/*
 * 产品表行双击函数
 */
function productOndblClickRow(rowid, iRow, iCol, e){
    if ( iCol == 6 || iCol == 7) {
        //选择产品
        chooseInvType = 'product'
        choice(rowid);
    }
}

/*
 * 材料表afterSaveCell
 */
function materialAfterSaveCell(rowid, name, val, iRow, iCol) {
    console.debug("保存单元格name:" + name);
    if (name == INV_CODE) {
        SetInventory(rowid, "2");
    } else if (name == UNIT_MATERIAL_PRICE || name == UNIT_MATERIAL_AMOUNT || name == INV_LAND || name == INV_LEN || name == INV_WIDTH || name == INV_EXTERNAL_DIAMETER || name == INV_INTERNAL_DIAMETER || name == INV_DENSITY) {
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

/*
 * 材料表行双击函数
 */
function materialOndblClickRow(rowid, iRow, iCol, e) {
    if (iCol == 14 || iCol == 15) {
        chooseInvType = 'material';
        //选择材料
        choice(rowid);
    }
}


//------字段计算更新函数链-----

/*
 * 更新产品表合计字段
 */
function updateAmount(rowId) {
    console.debug("更新产品表合计字段");
    let productRow = productHelper.getRowDataById(rowId);
    let productObj = getMesProductWithData(productRow);
    let price = getStringDecimal(productObj.getPrice());
    let productQty = getStringDecimal(productObj.getProductQty());
    productObj.setAmount((price * productQty).toFixed(2));
    productHelper.setRowDataById(rowId, productObj);
}

/*
 * 更新不含税单价
 */
function updateWorkPriceWithoutTax(rowId) {
    let productRow = productHelper.getRowDataById(rowId);
    let productObj = getMesProductWithData(productRow);
    let workPrice = getNumberDecimal(productObj.getWorkPrice());
    let workPriceWithoutTax = (workPrice / (1 + DEFAULT_TAX_TATE * 0.01)).toFixed(2);
    productObj.setWorkPriceWithoutTax(workPriceWithoutTax);
    productHelper.setRowDataById(rowId, productObj);
}

//修改数量后，同步更新该产品下材料的数量字段
function updateMaterialProductQty(rowid) {
    let product = getMesProductWithData(productHelper.getRowDataById(rowid));
    let productQty = getStringDecimal(product.getProductQty());
    let ids = materialHelper.getAllRowsId();
    for (let i = 0; i < ids.length; i++) {
        let productMaterialRow = materialHelper.getRowDataById(ids[i]);
        let productMaterialObj = getEmptyMesMaterial();
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
            console.debug(productMaterialObj);
            materialHelper.setRowDataById(ids[i], productMaterialObj);
        }


    }
}

/*
 * 更新加工费合计字段
 */
function updateTotalWorkAmount(rowId) {
    let productRow = productHelper.getRowDataById(rowId);
    let productObj = getMesProductWithData(productRow);
    let productQty = getNumberDecimal(productObj.getProductQty());
    let workPrice = getNumberDecimal(productObj.getWorkPrice());
    let totalWorkAmount = (productQty * workPrice).toFixed(2);
    productObj.setTotalWorkAmount(totalWorkAmount);
    productHelper.setRowDataById(rowId, productObj);
}

/*
 * 合计某产品下所有材料的材料单价和单件材料费
 */
function sumMaterialPriceToProduct(recordId) {
    if (recordId === undefined) {
        layer.alert("recordId不能为空");
        return;
    }
    //查询对应的产品记录
    var idsMain = productHelper.getAllRowsId();
    var rowMainId = "";
    var rowMain = getEmptyMesProduct();
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
    let materialObjForTotalPrice = getEmptyMesMaterial();
    for (var i = 0; i < ids.length; i++) {
        let materialRow = materialHelper.getRowDataById(ids[i]);
        materialObjForTotalPrice.setEntity(materialRow);
        if (materialRow.recordId === recordId) {
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
        productHelper.setRowDataById(rowMainId, rowMain);
        //更新单件价格字段
        updatePrice(rowMainId)
    }
}

/*
 * 更新单件价格字段
 */
function updatePrice(rowId) {
    let productRow = productHelper.getRowDataById(rowId);
    let productObj = getMesProductWithData(productRow);
    let materialAmount = getNumberDecimal(productObj.getMaterialAmount());
    let workPrice = getNumberDecimal(productObj.getWorkPrice());
    productObj.setPrice(materialAmount + workPrice)
    productHelper.setRowDataById(rowId, productObj);
    updateAmount(rowId);
}

/*
 * 更新单耗字段
 */
function updateIqty(rowId) {
    console.debug("更新单耗字段");
    let rowData = materialHelper.getRowDataById(rowId);
    let materialObj = getMesMaterialWithData(rowData);
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
    materialHelper.setRowDataById(rowId, materialObj);
    updateUnitMaterialAmount(rowId);
    updateTqty(rowId);
}

/*
 * 更新单件材料费
 */
function updateUnitMaterialAmount(rowid) {
    console.debug("更新单件材料费");
    let rowData = materialHelper.getRowDataById(rowid);
    let materialObj = getMesMaterialWithData(rowData);
    let unitMaterialPrice = getNumberDecimal(materialObj.getUnitMaterialPrice());
    let iqty = getNumberDecimal(materialObj.getIqty());
    let unitMaterialAmount = (unitMaterialPrice * iqty).toFixed(2);
    materialObj.setUnitMaterialAmount(unitMaterialAmount);
    materialHelper.setRowDataById(rowid, materialObj);
    //循环子表合计材料单价和单件材料费
    sumMaterialPriceToProduct(materialObj.getRecordId());
}

//设置下料尺寸
function updateInvSize(rowid) {
    console.debug("设置下料尺寸！");
    let rowData = materialHelper.getRowDataById(rowid);
    let materialObj = getMesMaterialWithData(rowData);
    var strSize = "";
    //厚度
    let invLand = getStringDecimal(materialObj.getInvLand());
    if (invLand !== 0) {
        strSize = "δ" + materialObj.getInvLand();
    }
    //长
    let invLen = getStringDecimal(materialObj.getInvLen());
    if (invLen !== 0) {
        if (strSize != "") {
            strSize = strSize + ",";
        }
        strSize = strSize + "L" + materialObj.getInvLen();
    }
    //宽
    let invWidth = getStringDecimal(materialObj.getInvWidth());
    if (invWidth !== 0) {
        if (strSize != "") {
            strSize = strSize + ",";
        }
        strSize = strSize + "W" + materialObj.getInvWidth();
    }
    //外径
    let externalDiameter = getStringDecimal(materialObj.getInvExternalDiameter());
    if (externalDiameter !== 0) {
        if (strSize != "") {
            strSize = strSize + ",";
        }
        strSize = strSize + "外φ" + materialObj.getInvExternalDiameter();
    }
    //内径
    let internalDiameter = getStringDecimal(materialObj.getInvInternalDiameter());
    if (internalDiameter !== 0) {
        if (strSize != "") {
            strSize = strSize + ",";
        }
        strSize = strSize + "内φ" + materialObj.getInvExternalDiameter();
    }
    materialObj.setInvSize(strSize);
    materialHelper.setRowDataById(rowid, materialObj)
}

//更新总量
function updateTqty(rowid) {
    console.debug("修改单耗后同步修改总量");
    let materialObj = getMesMaterialWithData(materialHelper.getRowDataById(rowid));
    //数量
    let productQty = getNumberDecimal(materialObj.getProductQty());
    //单耗
    let iqty = getNumberDecimal(materialObj.getIqty());
    materialObj.setTqty((productQty * iqty).toFixed(2));
    console.debug(materialObj);
    materialHelper.setRowDataById(rowid, materialObj);

}

//设置存货信息
function SetInventory(rowid, itype) {
    console.debug("设置存货信息");
    if (itype == "1") {
        var rowData = $("#jqGrid").jqGrid('getRowData', rowid);
        let productObj = getEmptyMesProduct();
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
                    productHelper.setRowDataById(rowid, data["mData"])
                    var ids = materialHelper.getAllRowsId();
                    var izExist = false;
                    let materialObj = getEmptyMesMaterial();
                    for (var i = 0; i < ids.length; i++) {
                        materialObj.setEntity(materialHelper.getRowDataById(ids[i]))
                        if (materialObj.getRecordId() == productObj.getRecordId()) {
                            izExist = true;
                            materialObj.setProductInfoFromU8Data(data["mData"])
                            materialHelper.setRowDataById(ids[i], materialObj);
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
                    let materialObj = getEmptyMesMaterial();
                    materialObj.setEntity(materialHelper.getRowDataById(rowid));
                    materialObj.setMaterialInfoFromU8Data(data);
                    materialHelper.setRowDataById(rowid, materialObj);
                } else {
                    materialHelper.setRowDataById(rowid, getEmptyMesMaterial());
                }

            },
        });
        updateInvSize(rowid);
    }


}