package com.xuanyuan.library.utils.text;

import android.text.TextUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：罗发新
 * 时间：2019/6/6 0006    星期四
 * 邮件：424533553@qq.com
 * 说明：文字格式 匹配的工具
 */
public class StringFormatUtils {

    /**
     *  判断 网络 url地址格式是否正确
     */
    public static boolean isUrl(String url) {
        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$");
        return pattern.matcher(url).matches();
    }

    /**
     * @return   检查邮件地址的格式
     */
    public static boolean checkEmail(String email) {
        boolean flag;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    /**
     * 精确到小数点两位
     */
    public static String accurate2(float scale) {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(scale);
    }

    public static String accurate3(float scale) {
        DecimalFormat fnum = new DecimalFormat("##0.000");
        return fnum.format(scale);
    }

    /**
     * 格式化文件大小，保留末尾的0，达到长度一致
     */
    public static String formatFileSize(long len, boolean keepZero) {
        String size;
        DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
        DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
        if (len < 1024) {
            size = len + "B";
        } else if (len < 10 * 1024) {
            // [0, 10KB)，保留两位小数
            size = (len * 100 / 1024 / (float) 100) + "KB";
        } else if (len < 100 * 1024) {
            // [10KB, 100KB)，保留一位小数
            size = len * 10 / 1024 / (float) 10 + "KB";
        } else if (len < 1024 * 1024) {
            // [100KB, 1MB)，个位四舍五入
            size = (len / 1024) + "KB";
        } else if (len < 10 * 1024 * 1024) {
            // [1MB, 10MB)，保留两位小数
            if (keepZero) {
                size = formatKeepTwoZero.format((double) (len * 100 / 1024 / 1024 / (float) 100)) + "MB";
            } else {
                size = (len * 100 / 1024 / 1024 / (float) 100)
                        + "MB";
            }
        } else if (len < 100 * 1024 * 1024) {
            // [10MB, 100MB)，保留一位小数
            if (keepZero) {
                size = (formatKeepOneZero.format((double) (len * 10 / 1024 / 1024) / 10))
                        + "MB";
            } else {
                size = (len * 10 / 1024 / 1024 / (float) 10) + "MB";
            }
        } else if (len < 1024 * 1024 * 1024) {
            // [100MB, 1GB)，个位四舍五入
            size = (len / 1024 / 1024) + "MB";
        } else {
            // [1GB, ...)，保留两位小数
            size = (len * 100 / 1024 / 1024 / 1024 / (float) 100)
                    + "GB";
        }
        return size;
    }
    /**
     * 对字符串进行正则表达式获取的方法
     *
     * @param regex  规则
     * @param source 源字符串
     */
    public static String getMatcher(String regex, String source) {
        String result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result = matcher.group(1);//只取第一组
        }
        return result;
    }

    /**
     * url is usable  验证URL的格式
     */
    public static boolean isUrlUsable(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        URL urlTemp;
        HttpURLConnection connt = null;
        try {
            urlTemp = new URL(url);
            connt = (HttpURLConnection) urlTemp.openConnection();
            connt.setRequestMethod("HEAD");
            int returnCode = connt.getResponseCode();
            if (returnCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (connt != null) {
                connt.disconnect();
            }
        }
        return false;
    }
}


