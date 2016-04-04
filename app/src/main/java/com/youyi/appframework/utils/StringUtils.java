package com.youyi.appframework.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.youyi.appframework.R;
import com.youyi.appframework.common.UIHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rain on 2016/2/17.
 */
public class StringUtils {
    private static final String TAG = StringUtils.class.getName();

    public static final String VERSION_SEPERATOR = ".";
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private final static Pattern mobiler = Pattern
            .compile("^(13|14|15|17|18)\\d{9}$");

    //private final static Pattern password = Pattern.compile("^[A-Za-z0-9]{6,22}$");


    private final static Pattern chineseName = Pattern.compile("^[a-zA-Z\\u4e00-\\u9fa5]{2,16}$");

    private final static Pattern password = Pattern.compile("^[a-zA-Z0-9~!@#$%^&*()_+-=`;:',.<>/\\\\|?\"\\{}\\]\\[]{6,20}$");


    public final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormater_cn_detail = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormater_time = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };

    /**
     * 将字符串转为日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater2.get().parse(sdate);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将字符串转为日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDateTime(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将字符串转为日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDateTimeWithZone(String sdate) {
        try {
            return dateFormater3.get().parse(sdate);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将日期类型转为字符串
     *
     * @param date
     * @return
     */
    public static String dateFormat(Date date) {
        return dateFormater2.get().format(date);
    }

    /**
     * 将日期类型转为字符串
     *
     * @param date
     * @return
     */
    public static String dateFormat(String date) {
        if (isNotEmpty(date)) {
            return dateFormater2.get().format(toDate(date));
        }
        return "";
    }

    /**
     * 将日期类型转为字符串
     *
     * @param date
     * @return
     */
    public static String dateTimeFormat(Date date) {
        return dateFormater.get().format(date);
    }

    /**
     * 将日期类型转为字符串
     *
     * @param date
     * @return
     */
    public static String dateTimeFormat(String date) {
        return dateFormater.get().format(toDate(date));
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim());
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        if (mobile == null || mobile.trim().length() == 0)
            return false;
        return mobiler.matcher(mobile).matches();
    }

    /**
     * 判断两个字符串是否相同
     * @param src
     * @param target
     * @return
     */
    public static boolean equalsString(String src, String target) {
        if (isEmpty(src) || isEmpty(target))
            return false;
        return src.equals(target);
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转长整型
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 对象转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0.0;
    }

    /**
     * 对象转float
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static float toFloat(String obj) {
        try {
            return Float.parseFloat(obj);
        } catch (Exception e) {
        }
        return 0.0f;
    }

    /**
     * 如果含小数位 输出小数位，否则去掉小数位
     *
     * @param price
     * @return
     */
    public static String getPrettyPrice(float price) {
        int intPrice = (int) price;
        if ((price - intPrice) == 0f) {
            return String.valueOf(intPrice);
        }
        return String.valueOf(price);
    }

    /**
     * 将字符串以分隔符转化成list
     * @param str
     * @param seperator
     * @return
     */
    public static List<String> stringToList(String str, String seperator) {
        List<String> itemList = new ArrayList<String>();
        if (isEmpty(str)) {
            return itemList;
        }
        StringTokenizer st = new StringTokenizer(str, seperator);
        while (st.hasMoreTokens()) {
            itemList.add(st.nextToken());
        }
        return itemList;
    }

    /**
     * 以友好的方式显示时间
     *
     * @param time
     * @return
     */
    public static String friendlyTime(String time) {
        String fTime = "";
        long week = 7 * 24 * 60 * 60 * 1000;

        Date date = null;

        if (time == null) return fTime;

        try {
            date = dateFormater3.get().parse(time);
        } catch (Exception e) {
        }
        long onWeek = System.currentTimeMillis() - date.getTime();
        if (onWeek > 0 && onWeek <= week) {
            fTime += "本周" + date.getDay() + "" + date.getHours() + ":" + date.getMinutes();
        } else {
            return dateTimeFormat(date);
        }
        return fTime;
    }

    /**
     * 以友好的方式显示时间
     *
     * @param lTime
     * @return
     */
    public static String friendlyTime(long lTime) {
        Date time = null;
        if (lTime == 0) {
            time = new Date();
        } else {
            time = new Date(lTime);
        }
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前更新";
            else
                ftime = hour + "小时前更新";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前更新";
            else
                ftime = hour + "小时前更新";
        } else if (days == 1) {
            ftime = "昨天更新";
        } else if (days == 2) {
            ftime = "前天更新";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前更新";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 以一种简单的方式格式化字符串
     * 如  String s = StringHelper.format("{0} is {1}", "apple", "fruit");
     * System.out.println(s);	//输出  apple is fruit.
     *
     * @param pattern
     * @param args
     * @return
     */
    public static String format(String pattern, Object... args) {
        for (int i = 0; i < args.length; i++) {
            pattern = pattern.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return pattern;
    }

    /**
     * 当前时间是否早于date1
     *
     * @param date1
     * @return
     */
    public static boolean isEarlier(String date1) {
        if (StringUtils.isEmpty(date1)) {
            return true;
        }
        date1 = date1.replace("T", " ");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date time1 = df.parse(date1);
            Date time2 = new Date();
            return time2.before(time1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * "yyyy-MM-dd'T'HH:mm:ssZ" to  "yyyy年MM月dd日''HH:mm:ss"
     *
     * @param dateTimeStr
     * @return
     */
    public static String getCnDateTimeFromTZ(String dateTimeStr) {
        Date date;
        try {
            date = dateFormater3.get().parse(dateTimeStr);
        } catch (Exception e) {
            return "";
        }

        return dateFormater_cn_detail.get().format(date);
    }

    /**
     * "yyyy-MM-dd'T'HH:mm:ssZ" to  "HH:mm:ss"
     *
     * @param dateTimeStr
     * @return
     */
    public static String getTimeFromTZ(String dateTimeStr) {
        Date date;
        try {
            date = dateFormater3.get().parse(dateTimeStr);
        } catch (Exception e) {
            return "";
        }

        return dateFormater_time.get().format(date);
    }

    /**
     * 判断是不是一个合法的名字,只能包含汉字和字母
     *
     * @param name
     * @return
     */
    public static boolean isChineseName(Context context, EditText name) {
        if (!TextUtils.isEmpty(name.getText().toString().trim())) {
            if (chineseName.matcher(name.getText().toString()).matches()) {
                return true;
            } else {
                UIHelper.toastMessage(context, R.string.input_u_correct_name);
                return false;
            }
        } else {
            UIHelper.toastMessage(context, R.string.empty_name);
            return false;
        }
    }

    /**
     * 判断是不是一个合法密码
     *
     * @param context
     * @param psw
     * @return
     */
    public static boolean isPassword(Context context, EditText psw) {
        if (!TextUtils.isEmpty(psw.getText().toString().trim())) {
            if (password.matcher(psw.getText().toString()).matches()) {
                return true;
            } else {
                UIHelper.toastMessage(context, R.string.input_correct_psw);
                return false;
            }
        } else {
            UIHelper.toastMessage(context, R.string.empty_psw);
            return false;
        }
    }

    /**
     * 保证String格式为phone
     *
     * @param phone
     * @return
     */
    public static String trimPhone(String phone) {
        if (isEmpty(phone)) return null;

        phone = phone.replaceAll("-", "").replaceAll(" ", "");

        if (phone.startsWith("+86"))
            phone = phone.substring(3);

        return phone.trim();
    }

    /*
     * 去str中的空格
     * @return
     */
    public static String getNoSpaceStr(String str) {
        if (str.contains(" ")) {
            str.replace(" ", "");
        }
        return str;
    }

    /**
     * Url正则匹配
     *
     * @param str
     * @return
     */
    public static Pattern getUrlPattern(String str) {
        Pattern pattern;
        if (str.contains("http")) {
            pattern = Pattern.compile("[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.]]*", Pattern.CASE_INSENSITIVE);
        } else if (str.contains("www")) {
            pattern = Pattern.compile("[www]+[.][0-9A-Za-z:/[-]_#[?][=][.]]*", Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile("([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}", Pattern.CASE_INSENSITIVE);
        }
        return pattern;
    }

    /**
     * 检测验证码是否正确
     *
     * @return
     */
    public static boolean checkValidCode(Context context, EditText validCode) {
        if (!TextUtils.isEmpty(validCode.getText().toString().trim())) {
            if (validCode.getText().toString().trim().length() == 4) {
                return true;
            } else {
                UIHelper.toastMessage(context, R.string.input_correct_valid);
                return false;
            }
        } else {
            UIHelper.toastMessage(context, R.string.empty_valid);
            return false;
        }
    }

    /**
     * 检查手机号码是否正确
     * @param context
     * @param phone
     * @return
     */
    public static boolean checkPhoneNum(Context context, EditText phone) {
        if (!TextUtils.isEmpty(phone.getText().toString().trim())) {
            if (isMobile(phone.getText().toString().trim())) {
                return true;
            } else {
                UIHelper.toastMessage(context, R.string.input_u_correct_phone);
                return false;
            }
        } else {
            UIHelper.toastMessage(context, R.string.empty_phone);
            return false;
        }
    }

    /**
     * 在url上添加参数
     */
    public static String appendParam(String url, String param, String value) {
        //hotfix for old version
        Pattern p = Pattern.compile(".*token=$");
        Matcher matcher = p.matcher(url);
        if (matcher.matches() && "token".equals(param)) {
            return url + value;
        }

        //normal start here
        StringBuilder result = new StringBuilder(url);
        if (url.contains("?")) result.append("&");
        else result.append("?");
        result.append(param).append("=").append(value);
        LogUtil.d(TAG, result.toString());
        return result.toString();
    }

    /**
     * 检测路径是否为正常的图片路径
     *
     * @param path
     * @return
     */
    public static boolean isNormalImagePath(String path) {
        String extension = path.substring(path.lastIndexOf('.'));
        boolean ret = extension != null && (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png"));
        return ret;
    }

}
