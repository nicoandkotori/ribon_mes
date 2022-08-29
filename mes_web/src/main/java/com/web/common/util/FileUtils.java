package com.web.common.util;

import java.io.File;

/**
 * 文件操作类
 * @author tao
 * @date 2018/8/25 10:39
 */
public class FileUtils {

    //删除文件，可以是文件或文件夹
    public static void delete(String fileName) throws RuntimeException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new RuntimeException("删除文件失败:" + fileName + "不存在！");
        }
        if (file.isFile()) {
            deleteFile(fileName);
        }else {
            deleteDirectory(fileName);
        }
    }

    //删除单个文件
    public static void deleteFile(String fileName) throws RuntimeException {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                throw new RuntimeException("删除单个文件" + fileName + "失败！");
            }
        } else {
            throw new RuntimeException("删除单个文件失败：" + fileName + "不存在！");
        }
    }

    //删除目录及目录下的文件
    public static void deleteDirectory(String dir) throws RuntimeException {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            throw new RuntimeException("删除目录失败：" + dir + "不存在！");
        }
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                deleteFile(files[i].getAbsolutePath());
            }else if (files[i].isDirectory()) {// 删除子目录
                deleteDirectory(files[i].getAbsolutePath());
            }
        }
        // 删除当前目录
        if (!dirFile.delete()) {
            throw new RuntimeException("删除目录" + dir + "失败！");
        }
    }
}
