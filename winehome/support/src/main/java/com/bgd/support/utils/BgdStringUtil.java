package com.bgd.support.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("resource")
public class BgdStringUtil {
    private static final Logger logger = LoggerFactory.getLogger(BgdStringUtil.class);
    public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String numberChar = "0123456789";


    /**
     * 判断字符串是否不为空
     *
     * @param value
     * @return 字符串不为空返回true，否则返回false
     */
    public static boolean isStringNotEmpty(String value) {

        if (value != null && !"".equals(value.trim()) && !"null".equals(value)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 把数组变为集合
     *
     * @param strs
     * @return
     */
    public static List<String> arrayToSet(String[] strs) {
        List<String> list = new ArrayList<String>();
        for (String str : strs) {
            if (BgdStringUtil.isStringNotEmpty(str)) {
                list.add(str);
            }
        }
        return list;
    }

    public static String getSerialNumber(int i, int numberMedian) {
        int median = 0;
        for (int a = i; a != 0; a = a / 10) {
            median++;
        }
        int b = numberMedian - median;
        String numberString = "";
        for (int c = 0; c < b; c++) {
            numberString = numberString + "0";
        }
        numberString = getCurrentTimeString() + numberString + i;
        return numberString;
    }

    /**
     * 获取当前时间，格式如下 20140424
     */
    public static String getCurrentTimeString() {
        Date date = new Date();
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    /**
     * 获取六位短信验证码
     */
    public static String getSmsCaptcha() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 获取uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    /**
     * 如果为空返回空字符串，否则返回大写
     */
    public static String getUpperCase(String str) {
        if (BgdStringUtil.isStringEmpty(str)) {
            return "";
        } else {
            return str.toUpperCase();
        }
    }

    /**
     * 判断字符串是邮箱
     */
    public static boolean isEmail(String str) {
        String email = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern pattern = Pattern.compile(email);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 判断字符串是邮箱
     */
    public static boolean isPhone(String str) {
        String phone = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 判断是否为guid
     *
     * @param str
     * @return
     */
    public static boolean isUuid(String str) {
        String uuidtype = "^[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}$";
        Pattern pattern = Pattern.compile(uuidtype);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 判断两个字符串是否相等 忽略大小写
     */
    public static boolean isEqualsIgnoreCase(String a, String b) {
        if (null == a || null == b) {
            return false;
        } else {
            a = a.toUpperCase();
            b = b.toUpperCase();
            return a.equals(b);
        }
    }

    /**
     * 检查字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        if ("".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否空
     *
     * @param value
     * @return 字符串不为空返回true，否则返回false
     */
    public static boolean isStringEmpty(String value) {

        return !isStringNotEmpty(value);
    }

    /**
     *@描述  多参数是否有空
     *@创建人  JackMore
     *@创建时间  2019/3/6
     */
    public static boolean isStringEmpty(Object... values) {
        int len = values.length;
        for (int index = 0; index < len; index++) {
            if (values == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     *
     * <pre>
     * FTStringUtils.isBlank(null)      = true
     * FTStringUtils.isBlank("")        = true
     * FTStringUtils.isBlank(" ")       = true
     * FTStringUtils.isBlank("bob")     = false
     * FTStringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static String MD5(String s) {
        if (isEmpty(s) || isBlank(s)) {
            return null;
        }

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getHashCode(Object object) throws IOException {
        if (object == null)
            return "";

        String ss = null;
        ObjectOutputStream s = null;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try {
            s = new ObjectOutputStream(bo);
            s.writeObject(object);
            s.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                s.close();
                s = null;
            }
        }
        ss = MD5(bo.toString());
        return ss;
    }

    public static int str2Int(String str) {
        if (str == null || "".equals(str.trim())) {
            return 0;
        }
        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("str to int error!", e);
        }
        return result;
    }

    public static long str2Long(String str) {
        if (str == null || "".equals(str.trim())) {
            return 0L;
        }
        long result = 0L;
        try {
            result = Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("str to int error!", e);
        }
        return result;
    }

    public static Double str2Double(String str) {
        if (str == null || "".equals(str.trim())) {
            return 0D;
        }
        Double result = 0D;
        try {
            result = Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("str to int error!", e);
        }
        return result;
    }

    public static String loadStrByFile(String filePath) {
        String result = "";
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(fr);
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            result = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 去掉字符串中空白换行字符
     *
     * @param str
     * @return
     */
    public static String trim(String str) {
        if (str != null && !"".equals(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            String strNoBlank = m.replaceAll("");
            return strNoBlank;
        } else {
            return str;
        }
    }

    public static String getPayType(int type) {
        switch (type) {
            case 1:
                return "支付宝支付";
            case 2:
                return "银联支付";
            case 3:
                return "银行卡支付";
            case 4:
                return "联通SP";
        }
        return "" + type;
    }

    /**
     * 生成md5编码字符串.
     *
     * @param str     源字符串
     * @param charset 编码方式
     * @return
     */
    public static String md5(String str, String charset) {
        if (str == null)
            return null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        MessageDigest md5MessageDigest = null;
        byte[] md5Bytes = null;
        char md5Chars[] = null;
        byte[] strBytes = null;
        try {
            strBytes = str.getBytes(charset);
            md5MessageDigest = MessageDigest.getInstance("MD5");
            md5MessageDigest.update(strBytes);
            md5Bytes = md5MessageDigest.digest();
            int j = md5Bytes.length;
            md5Chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte md5Byte = md5Bytes[i];
                md5Chars[k++] = hexDigits[md5Byte >>> 4 & 0xf];
                md5Chars[k++] = hexDigits[md5Byte & 0xf];
            }
            return new String(md5Chars);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        } finally {
            md5MessageDigest = null;
            strBytes = null;
            md5Bytes = null;
        }
    }

    /***********************************************************************/
    public static String generateBusinessNumbers(String format, Integer median, Integer number) {
        Date date = new Date();
        String dateString = BgdDateUtil.generateDateString6(date);
        String year = dateString.substring(0, 2);
        String month = dateString.substring(2, 4);
        String day = dateString.substring(4, 6);
        String h = dateString.substring(6, 8);
        String m = dateString.substring(8, 10);
        String s = dateString.substring(10, 12);
        String ms = dateString.substring(12);
        String numberString = number + "";
        int length = numberString.length();
        int zeroNumber = median - length;
        String zeroString = "";
        for (int i = 0; i < zeroNumber; i++) {
            zeroString = zeroString + "0";
        }
        numberString = zeroString + numberString;
        format = format.replaceAll("\\{YY\\}", year);
        format = format.replaceAll("\\{MM\\}", month);
        format = format.replaceAll("\\{DD\\}", day);
        format = format.replaceAll("\\{hh\\}", h);
        format = format.replaceAll("\\{mm\\}", m);
        format = format.replaceAll("\\{ss\\}", s);
        format = format.replaceAll("\\{ms\\}", ms);
        format = format.replaceAll("\\{##\\}", numberString);
        return format;
    }

    public static BigDecimal toBigDecimal(String str) {
        BigDecimal s = null;
        if (BgdStringUtil.isStringNotEmpty(str)) {
            try {
                s = new BigDecimal(str);
            } catch (Exception e) {
                logger.error("转换str:" + str + "toBigDecimal异常");
            }
        }
        return s;

    }

    public static String bigDecimalToString(BigDecimal bigDecimal, String median) {
        String str = null;
        try {
            if (null == bigDecimal) {
                bigDecimal = new BigDecimal(0);
            }
            str = String.format("%." + median + "f", bigDecimal);
        } catch (Exception e) {
            logger.error("转换BigDecimal:" + bigDecimal + "ToString异常");
        }
        return str;
    }

    public static Date toDate(String str) {
        Date d = null;
        if (BgdStringUtil.isStringNotEmpty(str)) {
            try {
                d = BgdDateUtil.generateStringDate2(str);
            } catch (Exception e) {
                logger.error("转换str:" + str + "toDate异常");
            }
        }
        return d;
    }

    public static Integer toInteger(String str) {
        return toInteger(str, null);
    }

    public static Integer toInteger(String str, Integer defaultValue) {
        if (BgdStringUtil.isStringEmpty(str)) {
            return defaultValue;
        } else {
            return Integer.parseInt(str);
        }
    }

    public static Integer toInteger(Object obj, Integer defaultValue) {
        if (obj instanceof Integer) {
            return (Integer) obj;
        }

        if (null != defaultValue) {
            return defaultValue;
        }
        return null;
    }

    public static Long toLong(Object obj) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        } else {
            throw new RuntimeException("obj is not a Long");
        }
    }

    public static String toEmptyString(String str) {
        if (BgdStringUtil.isStringEmpty(str)) {
            return "";
        } else {
            // 去除收尾空格
            str = str.trim();
            String strs[] = str.split("");
            String total = "";
            boolean flag = true;
            for (String temp : strs) {
                if ("'".equals(temp)) {
                    if (flag) {
                        total = total + "‘";
                        flag = false;
                    } else {
                        total = total + "’";
                        flag = true;
                    }

                } else {
                    total = total + temp;
                }
            }
            return total;
        }
    }

    public static String toString(String str, String defaultString) {
        if (BgdStringUtil.isStringEmpty(str)) {
            return defaultString;
        } else {
            // 去除收尾空格
            str = str.trim();
            String strs[] = str.split("");
            String total = "";
            boolean flag = true;
            for (String temp : strs) {
                if ("'".equals(temp)) {
                    if (flag) {
                        total = total + "‘";
                        flag = false;
                    } else {
                        total = total + "’";
                        flag = true;
                    }

                } else {
                    total = total + temp;
                }
            }
            return total;
        }
    }

    public static String toString(String str) {
        if (BgdStringUtil.isStringEmpty(str)) {
            return null;
        } else {
            // 去除收尾空格
            str = str.trim();
            String strs[] = str.split("");
            String total = "";
            boolean flag = true;
            for (String temp : strs) {
                if ("'".equals(temp)) {
                    if (flag) {
                        total = total + "‘";
                        flag = false;
                    } else {
                        total = total + "’";
                        flag = true;
                    }

                } else {
                    total = total + temp;
                }
            }
            return total;
        }
    }

    public static String spliceString(String delimiter, String... strings) {
        String total = "";
        for (String str : strings) {
            if (BgdStringUtil.isStringNotEmpty(str)) {
                total = total + str + delimiter;
            }
        }
        total = BgdStringUtil.removingTail(total);
        return total;
    }

    public static String removingTail(String str) {
        if (BgdStringUtil.isStringNotEmpty(str)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 把形如1,2,3,4类型的字符串使用,分隔符分成集合
     *
     * @param str
     * @param delimiter
     * @return
     */
    public static List<String> toListByDelimiter(String str, String delimiter) {
        List<String> list = new ArrayList<String>();
        if (BgdStringUtil.isStringNotEmpty(str)) {
            String[] array = str.split(delimiter);
            if (null != array && 0 != array.length) {
                for (String record : array) {
                    list.add(record);
                }
            }
        }
        return list;
    }

    // / <summary>
    // / 绘制4个随机数
    // / </summary>
    // / <returns></returns>
    public static String GenerateRanNum(int length) {
        StringBuffer sb = new StringBuffer();
        String format = BgdDateUtil.format(new Date(), "yyyyMMddHHmmss");
        Random random = new Random();
        sb.append(format);
        for (int i = 0; i < length; i++) {
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        return sb.toString();
    }

    /**
     * 格式化bigdecimal类型
     *
     * @param count 保留小数位数
     * @param value 格式化的值
     * @return
     */
    public static String FormatBigDecimal(int count, BigDecimal value) {
        if (value == null) {
            value = new BigDecimal(0);
        }
        return value.setScale(count, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String esToCS(String str) {
        boolean flag = false;
        String result = "";
        char[] chars = str.toCharArray();
        String s = "";
        for (char c : chars) {
            s = c + "";
            if (",".equals(s)) {
                result = result + "，";
            } else if ("=".equals(s)) {
                result = result + "＝";
            } else if ("(".equals(s)) {
                result = result + "（";
            } else if (")".equals(s)) {
                result = result + "）";
            } else if (";".equals(s)) {
                result = result + "；";
            } else if ("'".equals(s) && flag == false) {
                result = result + "‘";
                flag = true;
            } else if ("'".equals(s) && flag == true) {
                result = result + "’";
                flag = false;
            } else {
                result = result + s;
            }
        }
        return result;
    }

    /**
     * 获取一定个数的随机字符串
     *
     * @param number
     * @return
     */
    public static StringBuffer getRandomString(int number) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            System.out.println();
            char ch = allChar.charAt(random.nextInt(allChar.length()));
            sb.append(ch);
        }
        return sb;
    }

    /**
     * 将输入字符串流转换为字符串
     *
     * @param in 输入字符串流
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    /**
     * 可以使用QQ表情、符号表情 emoji表情转换(hex -> utf-16)
     *
     * @param hexEmoji
     * @return
     */
    public String emoji(int hexEmoji) {
        return String.valueOf(Character.toChars(hexEmoji));
    }

    /**
     * 将url进行编码
     *
     * @param source
     * @return
     */
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 含有中文的字符串的长度
     *
     * @param s
     * @return
     */
    public static int length_zh(String s) {
        if (BgdStringUtil.isStringEmpty(s))
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    /**
     * 判断是否是英文字符
     *
     * @param c
     * @return
     */
    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    public static String toString(Object obj, String defaultString) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof String) {
            return toString(obj + "", defaultString);
        } else {
            throw new RuntimeException("obj is not a String");
        }
    }

    public static String toString(Object obj) {
        return toString(obj, null);
    }

    public static boolean isNullOrEmpty(String... string) {
        boolean result = false;
        for (String s : string) {
            if (s == null || s.length() == 0) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static String retVal(String value) {
        if (StringUtils.isEmpty(value)) return "";
        if (!value.contains("."))
            return value;
        String _value = new StringBuffer(value).reverse().toString();
        String _v = _value.substring(0, _value.indexOf("."));
        if (_v.length() > 0) {
            boolean hasNumber = false;
            for (int i = 0; i <= _v.length() - 1; i++) {
                if (Integer.valueOf(String.valueOf(_v.charAt(i))).compareTo(Integer.valueOf(0)) > 0) {
                    hasNumber = true;
                }
            }
            if (!hasNumber) {
                String _fv = _value.substring(_value.indexOf(".") + 1);
                return new StringBuilder(_fv).reverse().toString();
            }
        }
        StringBuffer fval = new StringBuffer();
        boolean stop = false;
        for (int i = 0; i <= _value.length() - 1; i++) {
            if ((_value.charAt(i) != '0') && (_value.charAt(i) != '.')) {
                stop = true;
            }
            if (stop) {
                fval.append(_value.charAt(i));
            }
        }
        if (fval.length() == 0) return "0";
        return fval.reverse().toString();
    }
}
