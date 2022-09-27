/*
 * 存放打开其它页面后的回调函数
 */


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
    console.debug("增加部件表-部件赋值-部件表id："+partTableId);
    partDataMap.set(partTableId,partArray);
    array = partArray;
    let productObj = new OmMesProduct();
    productObj.setEntity(product);
    //合计该产品下所有材料的材料单价和单件材料费
    sumMaterialPriceToProduct(productObj.getRecordId())
    console.debug("增加部件表-带回材料表数据↓");
    console.debug(materialArray);
    //删除材料表中partRowId已存在的记录
    console.debug("增加部件表-删除材料表中partRowId已存在的记录");
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
* 选择产品信息回调函数
*/
function chooseInventoryCallBack(u8Data){
    //根据选择物料类型来为产品表或材料表赋值，不算优雅的设计，但受限于框架没有更好的实现方式
    switch (chooseInvType) {
        case 'product':
            console.debug("选择产品信息");
            productHelper.saveCell(CurRow,CurCol);
            materialHelper.saveCell(CurRow1,CurCol1);
            let mesProduct = new OmMesProduct();
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
            let materialObj = new OmMesMaterial();
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