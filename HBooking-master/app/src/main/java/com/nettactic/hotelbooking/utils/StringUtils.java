/*
 * StringUtils.java
 * classes : cn.com.donson.naf.other.util.StringUtils
 * @author 浣欑倻
 * V 1.0.0
 * Create at 2012-5-10 涓嬪崍04:41:18
 */
package com.nettactic.hotelbooking.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@SuppressLint("SimpleDateFormat") public class StringUtils {
    /**
     * 是否为空
     * 
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        return TextUtils.isEmpty(string) || "null".equalsIgnoreCase(string);
    }

    /**
     * 是否为数字
     * 
     * @param obj
     * @return
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        String str = obj.toString();
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数组是否为空
     * 
     * @param values
     * @return
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    /**
     * 数组是否为空
     * 
     * @param values
     * @return
     */
    public static boolean areEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = true;
        } else {
            for (String value : values) {
                result &= isEmpty(value);
            }
        }
        return result;
    }

    /**
     * @param unicode
     * @return
     */
    public static String unicodeToChinese(String unicode) {
        StringBuilder out = new StringBuilder();
        if (!isEmpty(unicode)) {
            for (int i = 0; i < unicode.length(); i++) {
                out.append(unicode.charAt(i));
            }
        }
        return out.toString();
    }

    /**
     * @param input
     * @return
     */
    public static String stripNonValidXMLCharacters(String input) {
        if (input == null || ("".equals(input)))
            return "";
        StringBuilder out = new StringBuilder();
        char current;
        for (int i = 0; i < input.length(); i++) {
            current = input.charAt(i);
            if ((current == 0x9) || (current == 0xA) || (current == 0xD)
                    || ((current >= 0x20) && (current <= 0xD7FF))
                    || ((current >= 0xE000) && (current <= 0xFFFD))
                    || ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }

    /**
     * 字符串按字节截取
     * 
     * @param str
     *            原字符
     * @param len
     *            截取长度
     * @return
     */
    public static String splitString(String str, int len) {
        return splitString(str, len, "...");
    }

    /**
     * 字符串按字节截取
     * 
     * @param str
     *            原字符
     * @param len
     *            截取长度
     * @param elide
     *            省略符
     * @return String
     */
    public static String splitString(String str, int len, String elide) {
        if (str == null)
            return "";
        int strlen = str.length();
        if (strlen - len > 0) {
            str = str.substring(0, len) + elide.trim();
        }
        return str;
    }

    /**
     * 将json中的首字母大写处理成小写
     * 
     * @param jsonInput
     * @return
     */
    public static String dealWithFirstChar(String jsonInput) {
        String originalInput = jsonInput;
        StringBuilder inputStr = new StringBuilder(jsonInput);
        String regex = "\"(\\w+)\":";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(inputStr);
        List<String> result = new ArrayList<String>();
        while (m.find()) {
            String valueName = m.group(1);
            String newValueName = null;
            char[] words = valueName.toCharArray();
            if (Character.isUpperCase(words[0])) {// 首字母大写,不符合变量命名规范

                words[0] = Character.toLowerCase(words[0]);
                newValueName = new String(words);
//        System.out.println("orignal value:"+valueName+" new value :"+ newValueName);
//        String regexWord = "\""+valueName+"\":";
                String regx1 = "\"" + valueName + "\":";
                String replace = "\"" + newValueName + "\":";
                originalInput = originalInput.replaceAll(regx1, replace);
            }
            result.add(valueName);
            inputStr.delete(0, m.end(0));
            m = p.matcher(inputStr);
        }
        return originalInput;

    }

    /**
     * 字符半角转全角
     * 
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /*
     * Java文件操作 获取文件扩展名
     *
     *  Created on: 2011-8-2
     *      Author: blueeagle
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /*
     * Java文件操作 获取不带扩展名的文件名
     *
     *  Created on: 2011-8-2
     *      Author: blueeagle
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    /**
     * 把String转化为2位小数的Double在转化String
     * @param str
     * @return
     */
    public static String format2point(String str) {
    	double d = Double.parseDouble(str);
		String price = new DecimalFormat("#0.00").format(d);
		return price;
	}
    
    public static String longToDate(long timeStamp){
		if (timeStamp == 0) {
			return "";
		}
		timeStamp = timeStamp * 1000;
		String result = "";
		SimpleDateFormat format = new SimpleDateFormat("M月d");
		result = format.format(new Date(timeStamp));
		return result;
    }

    public static String longToDate02(long timeStamp){
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        result = format.format(new Date(timeStamp));
        return result;
    }
    
    /**
	 * 匹配短信中间的6个数字（验证码等）
	 * 
	 * @param patternContent
	 * @return
	 */
	public static String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile("(?<!\\d)\\d{6}(?!\\d)");
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}


    /**
     * 将10位数的卡号 每四位加空格一个。
     * @param for_num
     * @return
     */
    public static String getFourSpace(String for_num) {
        String later = for_num.substring(0, 4)
                + " "
                + for_num.substring(4, 8)
                + " "
                + for_num.substring(8, for_num.length());
        return later;
    }
	
}
