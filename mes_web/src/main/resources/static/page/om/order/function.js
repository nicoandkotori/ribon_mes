
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


