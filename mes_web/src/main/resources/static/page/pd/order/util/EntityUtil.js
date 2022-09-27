/*
 * 实体类转换工具类
 */


function getEmptyMesMain(){
    return new OmMesMain();
}

function getMesMainWithData(data){
    let mainObj = getEmptyMesMain();
    mainObj.setEntity(data);
    return mainObj;
}

function getMesMainListWithData(mainList){
    let mainObjList = [];
    mainList.forEach(function (main) {
        let mainObj = getMesMainWithData(main)
        mainObjList.push(mainObj);
    })
    return mainObjList;
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

function getMesProductListWithData(productList){
    let productObjList = [];
    productList.forEach(function (product) {
        let productObj = getMesProductWithData(product);
        productObjList.push(productObj);
    })
    return productObjList;
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