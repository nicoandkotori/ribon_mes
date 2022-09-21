
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
 * 点击按钮产品表增行事件
 */
function btnAddProductRow() {
    let productObj = new OmMesProduct();
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
 * 获取空MesProduct对象，封装成函数后，以后可以根据常量配置动态返回实体类，类似于工厂模式
 */
function getEmptyMesProduct(){
    return new OmMesProduct();
}

/*
 * 获取有数据MesProduct对象
 */
function getMesProductWithData(data){
    let productObj = getEmptyMesProduct();
    productObj.setEntity(data);
    return productObj;
}

/*
 * 获取空MesMaterial对象，封装成函数后，以后可以根据常量配置动态返回实体类，类似于工厂模式
 */
function getEmptyMesMaterial(){
    return new OmMesMaterial();
}

/*
 * 获取有数据的MesMaterial对象
 */
function getMesMaterialWithData(data){
    let materialObj = getEmptyMesMaterial();
    materialObj.setEntity(data);
    return materialObj;
}

/*
 * 导出excel
 */
function exportToExcel(){
    let mesId = $("#id").val();
    window.location.href = encodeURI(URL_EXPORT+"?mesId="+mesId);
}

/*
 * 打印
 */
function FnPrint() {
    console.debug("打印")
    /**
     * DATE: 2022/9/21
     * mijiahao TODO: 打印待完善！
     */
    var id = $("#u8Id").val();
    if (id != null && id != "") {
        $.get('/om/metalworkcommittee/getbyid', {id: id}, function (data) {
            if (data != null) {
                $.ajax({
                    type: "post",
                    url: "/om/metalworkcommittee/getdetaillist",
                    data: {id: id},
                    success: function (data1) {
                        if(data1!=null&&data1.length>0)
                        {
                            data.darrivedate=data1[0].darrivedate;
                        }


                        PrPrintOmDetail(data, data1);

                    },
                    error: function () {
                    }
                });

            }
        });

    } else {
        layer.alert("请先保存！");
    }

}