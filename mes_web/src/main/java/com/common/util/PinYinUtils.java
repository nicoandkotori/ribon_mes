package com.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Vfun01 on 2018-04-27.
 */
public class PinYinUtils {

    private static Map<String, List<String>> pinyinMap = new HashMap<String, List<String>>();

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变，特殊字符丢失 支持多音字，生成方式如（长沙市长:cssc,zssz,zssc,cssz）
     *
     * @param chines
     *            汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            char ch = nameChar[i];
            if (ch > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat);
                    if (strs != null) {
                        int len = strs.length;
                        if (len == 1){  //不是多音字
                            String py = strs[0];
                            if(py.contains("u:")){  //过滤 u:
                                py = py.replace("u:", "v");
                            }
                            pinyinName.append(py.charAt(0));
                        }else if(strs[0].equals(strs[1])){ //非多音字 有多个音，取第一个
                            pinyinName.append(strs[0].charAt(0));
                        }else{ // 多音字
                            int length = chines.length();
                            boolean flag = false;
                            String s = null;
                            List<String> keyList =null;

                            for (int j = 0; j < strs.length; j++) {
                                String py = strs[j];
                                if(py.contains("u:")){  //过滤 u:
                                    py = py.replace("u:", "v");
                                }
                                keyList = pinyinMap.get(py);

                                if (i + 3 <= length) {   //后向匹配2个汉字
                                    s = chines.substring(i, i + 3);
                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py.charAt(0));
                                        flag = true;
                                        break;
                                    }
                                }

                                if (i + 2 <= length) {   //后向匹配 1个汉字
                                    s = chines.substring(i, i + 2);
                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py.charAt(0));
                                        flag = true;
                                        break;
                                    }
                                }

                                if ((i - 2 >= 0) && (i+1<=length)) {  // 前向匹配2个汉字
                                    s = chines.substring(i - 2, i+1);
                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py.charAt(0));
                                        flag = true;
                                        break;
                                    }
                                }

                                if ((i - 1 >= 0) && (i+1<=length)) {  // 前向匹配1个汉字
                                    s = chines.substring(i - 1, i+1);
                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py.charAt(0));
                                        flag = true;
                                        break;
                                    }
                                }

                                if ((i - 1 >= 0) && (i+2<=length)) {  //前向1个，后向1个
                                    s = chines.substring(i - 1, i+2);
                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py.charAt(0));
                                        flag = true;
                                        break;
                                    }
                                }
                            }

                            if (!flag) {    //都没有找到，匹配默认的 读音

                                s = String.valueOf(ch);

                                for (int x = 0; x < len; x++) {

                                    String py = strs[x];
                                    if(py.contains("u:")){  //过滤 u:
                                        py = py.replace("u:", "v");
                                    }
                                    keyList = pinyinMap.get(py);

                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py.charAt(0));//拼音首字母
                                        flag = true;
                                        break;
                                    }
                                }
                            }

                            if (!flag){
                                pinyinName.append(strs[0].charAt(0));
                            }
                        }
                    }
                    else
                    {
                        pinyinName.append("*");
                    }
                    // else {
                    // pinyinName.append(nameChar[i]);
                    // }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
//                    e.printStackTrace();
                    pinyinName.append("*");
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
            pinyinName.append(" ");
        }
        // return pinyinName.toString();
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }

    /**
     * 汉字转换位汉语全拼，英文字符不变，特殊字符丢失
     * 支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen
     * ,chongdangshen,zhongdangshen,chongdangcan）
     *
     * @param chines
     *            汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            char ch = nameChar[i];
            if (ch > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat);
                    if (strs != null) {
                        int len = strs.length;
                        if (len == 1){  //不是多音字
                            String py = strs[0];
                            if(py.contains("u:")){  //过滤 u:
                                py = py.replace("u:", "v");
                            }
                            pinyinName.append(py);
                        }else if(strs[0].equals(strs[1])){ //非多音字 有多个音，取第一个
                            pinyinName.append(strs[0]);
                        }else{ // 多音字
                            int length = chines.length();
                            boolean flag = false;
                            String s = null;
                            List<String> keyList =null;

                            for (int j = 0; j < strs.length; j++) {
                                String py = strs[j];
                                if(py.contains("u:")){  //过滤 u:
                                    py = py.replace("u:", "v");
                                }
                                keyList = pinyinMap.get(py);

                                if (i + 3 <= length) {   //后向匹配2个汉字
                                    s = chines.substring(i, i + 3);
                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py);
                                        flag = true;
                                        break;
                                    }
                                }

                                if (i + 2 <= length) {   //后向匹配 1个汉字
                                    s = chines.substring(i, i + 2);
                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py);
                                        flag = true;
                                        break;
                                    }
                                }

                                if ((i - 2 >= 0) && (i+1<=length)) {  // 前向匹配2个汉字
                                    s = chines.substring(i - 2, i+1);
                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py);
                                        flag = true;
                                        break;
                                    }
                                }

                                if ((i - 1 >= 0) && (i+1<=length)) {  // 前向匹配1个汉字
                                    s = chines.substring(i - 1, i+1);
                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py);
                                        flag = true;
                                        break;
                                    }
                                }

                                if ((i - 1 >= 0) && (i+2<=length)) {  //前向1个，后向1个
                                    s = chines.substring(i - 1, i+2);
                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py);
                                        flag = true;
                                        break;
                                    }
                                }
                            }

                            if (!flag) {    //都没有找到，匹配默认的 读音

                                s = String.valueOf(ch);

                                for (int x = 0; x < len; x++) {

                                    String py = strs[x];
                                    if(py.contains("u:")){  //过滤 u:
                                        py = py.replace("u:", "v");
                                    }
                                    keyList = pinyinMap.get(py);

                                    if (keyList != null && (keyList.contains(s))) {
                                        pinyinName.append(py);//拼音首字母
                                        flag = true;
                                        break;
                                    }
                                }
                            }

                            if (!flag){
                                pinyinName.append(strs[0]);
                            }
                        }
                    }
                    else
                    {
                        pinyinName.append("*");
                    }

                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    pinyinName.append("*");
//                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
            pinyinName.append(" ");
        }
        // return pinyinName.toString();
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }

    /**
     * 去除多音字重复数据
     *
     * @param theStr
     * @return
     */
    private static List<Map<String, Integer>> discountTheChinese(String theStr) {
        // 去除重复拼音后的拼音列表
        List<Map<String, Integer>> mapList = new ArrayList<Map<String, Integer>>();
        // 用于处理每个字的多音字，去掉重复
        Map<String, Integer> onlyOne = null;
        String[] firsts = theStr.split(" ");
        // 读出每个汉字的拼音
        for (String str : firsts) {
            onlyOne = new Hashtable<String, Integer>();
            String[] china = str.split(",");
            // 多音字处理
            for (String s : china) {
                Integer count = onlyOne.get(s);
                if (count == null) {
                    onlyOne.put(s, new Integer(1));
                } else {
                    onlyOne.remove(s);
                    count++;
                    onlyOne.put(s, count);
                }
            }
            mapList.add(onlyOne);
        }
        return mapList;
    }

    /**
     * 解析并组合拼音，对象合并方案(推荐使用)
     *
     * @return
     */
    private static String parseTheChineseByObject(
            List<Map<String, Integer>> list) {
        Map<String, Integer> first = null; // 用于统计每一次,集合组合数据
        // 遍历每一组集合
        for (int i = 0; i < list.size(); i++) {
            // 每一组集合与上一次组合的Map
            Map<String, Integer> temp = new Hashtable<String, Integer>();
            // 第一次循环，first为空
            if (first != null) {
                // 取出上次组合与此次集合的字符，并保存
                for (String s : first.keySet()) {
                    for (String s1 : list.get(i).keySet()) {
                        String str = s + s1;
                        temp.put(str, 1);
                    }
                }
                // 清理上一次组合数据
                if (temp != null && temp.size() > 0) {
                    first.clear();
                }
            } else {
                for (String s : list.get(i).keySet()) {
                    String str = s;
                    temp.put(str, 1);
                }
            }
            // 保存组合数据以便下次循环使用
            if (temp != null && temp.size() > 0) {
                first = temp;
            }
        }
        String returnStr = "";
        if (first != null) {
            // 遍历取出组合字符串
            for (String str : first.keySet()) {
                returnStr += (str + ",");
            }
        }
        if (returnStr.length() > 0) {
            returnStr = returnStr.substring(0, returnStr.length() - 1);
        }
        return returnStr;
    }

    /**
     * 初始化 所有的多音字词组
     *
     * @param fileName
     */
    public static void initPinyin(String fileName) {
        // 读取多音字的全部拼音表;
        InputStream file = PinyinHelper.class.getResourceAsStream(fileName);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(file, "utf-8"));

            String s = null;

            while ((s = br.readLine()) != null) {

                if (s != null) {
                    String[] arr = s.split("#");
                    String pinyin = arr[0];
                    String chinese = arr[1];

                    if(chinese!=null){
                        String[] strs = chinese.split(" ");
                        List<String> list = Arrays.asList(strs);
                        pinyinMap.put(pinyin, list);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
