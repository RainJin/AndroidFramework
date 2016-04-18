package com.youyi.appframework.account;

/**
 * 帐号体系bean
 * Created by Rain on 2016/3/4.
 */
public class Account {

    private final static String TAG = Account.class.getSimpleName();

    /**
     * ssoId: 用户唯一标示id
     */
    public String ssoid;

    /**
     * 登录token
     */
    public String token;
    /**
     * 手机号码
     */
    public String phone;

    /**
     * 微聊UUID
     */
    public String chatUuid;

    /**
     * 真实姓名
     */
    public String realName;
    /**
     * 昵称
     */
    public String nickName;

    /**
     * 第三方平台：eg博客/新浪微博地址
     */
    public String domain;
    /**
     * 是否vip用户
     */
    public String isVip;
    /**
     * 当前app版本号
     */
    public int versionCode;

    /**
     * 头像
     */
    public String mePic;
    /**
     * 头像状态
     */
    public int mePicStatus;

    /**
     * 性别
     */
    public int gender;
    /**
     * 个人说明
     */
    public String descript;

    /**
     * 省份id
     */
    public String provinceId;
    /**
     * 省份名字
     */
    public String provinceName;

    /**
     * 城市id
     */
    public String cityId;
    /**
     * 城市名字
     */
    public String cityName;

    /**
     * qq号
     */
    public String meQQ;

    /**
     * 用户微信号
     */
    public String meWeiXin;

    /**
     * app_user ： app里面的联系人
     */
    public String appUsersId;

    /**
     * 匿名/游客用户的id
     */
    public String uuid;

    /**
     * 微聊对家id
     */
    public String targetContactsId;

    /**
     * 是否激活了微聊
     */
    public boolean openChated;

    /**
     * 最后分享时间
     */
    public long lastShareTime;

    @Override
    public String toString() {
        return "Account{" +
                "ssoid='" + ssoid + '\'' +
                ", token='" + token + '\'' +
                ", phone='" + phone + '\'' +
                ", chatUuid='" + chatUuid + '\'' +
                ", realName='" + realName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", domain='" + domain + '\'' +
                ", isVip='" + isVip + '\'' +
                ", versionCode=" + versionCode +
                ", mePic='" + mePic + '\'' +
                ", mePicStatus=" + mePicStatus +
                ", gender=" + gender +
                ", descript='" + descript + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", meQQ='" + meQQ + '\'' +
                ", meWeiXin='" + meWeiXin + '\'' +
                ", appUsersId='" + appUsersId + '\'' +
                ", uuid='" + uuid + '\'' +
                ", targetContactsId='" + targetContactsId + '\'' +
                ", openChated=" + openChated +
                ", lastShareTime=" + lastShareTime +
                '}';
    }
}
