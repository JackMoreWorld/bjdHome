package com.bgd.support.utils;


import java.io.*;
import java.util.Map;

/**
 * 
 * @ClassName:  FileUtils   
 * @Description: 文件操作
 * @author: JackMore
 * @date:   2019年2月25日 下午2:18:35
 */
@SuppressWarnings({"unchecked", "resource"})
public class FileUtils {
    /**
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: newLine
     * @Description: 文本文件内容换行
     */
    public static String newLine() {
        return System.getProperty("line.separator");
    }

    /**
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: separator
     * @Description: 文件路径分割
     */
    public static String separator() {
        return System.getProperty("file.separator");
    }

    /**
     * 读取文件为对象
     *
     * @param fileName 文件名
     * @param <T>      对象泛型
     * @return 读取结果
     * @throws Exception 读取异常
     */
    public static final <T> T readFile2Obj(String fileName) throws Exception {
        try {
            InputStream input = new FileInputStream(fileName);
            readStream2Obj(input);
        } catch (Exception e) {
            throw e;
        }
        return null;
    }

    /**
     * 读取输入流为对象
     *
     * @param inputStream 输入流
     * @param <T>         对象泛型
     * @return 读取结果
     * @throws Exception 读取异常
     */

    public static final <T> T readStream2Obj(InputStream inputStream) throws Exception {
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(inputStream);
            return (T) stream.readObject();
        } finally {
            if (stream != null)
                stream.close();
        }
    }

    /**
     * 写出对象到文件(序列化)
     *
     * @param obj      需要写出的对象
     * @param fileName 文件名称
     * @throws Exception 写出异常
     */
    public static final void writeObj2File(Serializable obj, String fileName) throws Exception {
        try {
            OutputStream out = new FileOutputStream(fileName);
            writeObj2Steam(obj, out);
        } catch (Exception e) {
            throw e;
        }
    }

    public static final void writeString2File(String str, String fileName, boolean appends, boolean newline) throws Exception {
        writeString2File(str, fileName, "GB2312", appends, newline);
    }

    /**
     * @param @param  map
     * @param @param  fileName
     * @param @param  appends
     * @param @param  newline
     * @param @throws Exception 设定文件
     * @return void 返回类型
     * @throws
     * @Title: writeMap2File
     * @Description: 将map对象写入文件，写为key:value格式
     */
    public static final void writeMap2File(Map<String, String> map, String fileName, boolean appends, boolean newline) throws Exception {
        for (String key : map.keySet())
            writeString2File(key + ":" + map.get(key), fileName, appends, newline);
    }

    /**
     * @param @param  filepath
     * @param @param  str
     * @param @param  appends
     * @param @param  newline
     * @param @throws IOException 设定文件
     * @return void 返回类型
     * @throws
     * @Title: writeString2File
     * @Description: 追加文件内容，不换行
     */

    public static void writeString2File(String str, String fileName, String encode, boolean appends, boolean newline) throws IOException {
        File file = new File(fileName);
        makeFolders(file.getParentFile());
        Writer out = new OutputStreamWriter(new FileOutputStream(file, appends), encode);
        out.write(str);
        if (newline) {
            out.write(FileUtils.newLine());
        }
        out.flush();
    }

    /**
     * 写出对象到输出流
     *
     * @param obj          需要写出的对象
     * @param outputStream 输出流
     * @throws Exception 写出异常
     */
    public static final void writeObj2Steam(Serializable obj, OutputStream outputStream) throws Exception {
        ObjectOutputStream stream = new ObjectOutputStream(outputStream);
        stream.writeObject(obj);
        stream.flush();
    }

    /**
     * 获取文件后缀名
     *
     * @param file 文件对象
     * @return 文件后缀名
     */
    public static String getSuffix(File file) {
        return getSuffix(file.getName());
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return 文件后缀名
     */
    public static String getSuffix(String fileName) {
        if (fileName == null)
            return "";
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }
        return "";
    }

    /**
     * @param @param  dir
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     * @Title: deleteDir
     * @Description: 递归删除
     */
    public static boolean deleteDir(File dir) {
        // 文件必须存在
        if (!dir.exists()) {
            return true;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static boolean isExists(File file) {
        return file.exists();
    }

    public static boolean isExists(String file) {
        return isExists(new File(file));
    }

    public static boolean makeFolders(String file) {
        return makeFolders(new File(file));
    }

    public static boolean makeFolders(File file) {
        if (!isExists(file)) {
            file.mkdirs();
        }
        return true;
    }

    public static File saveFileAsStream(InputStream inputStream, String path) throws Exception {
        FileOutputStream out = new FileOutputStream(path);
        // 自定义缓冲对象
        byte[] b = new byte[1024];
        while ((inputStream.read(b)) != -1) {
            out.write(b, 0, b.length);
        }
        inputStream.close();
        out.close();
        return new File(path);
    }


}
