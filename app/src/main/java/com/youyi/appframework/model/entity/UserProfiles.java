package com.youyi.appframework.model.entity;

/**
 * APP用户一些配置信息
 * Created by Rain on 2016/2/1.
 */
public class UserProfiles {

    private String ssoId;

    private String realName;

    private String phone;

    private String cityId;

    private String cityName;

    private String provinceId;

    private String provinceName;

    private String domain;

    private int gender;

    private int isVip;

    private int active_im_status;

    private String meQQ;

    private String meWeiXin;

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getActive_im_status() {
        return active_im_status;
    }

    public void setActive_im_status(int active_im_status) {
        this.active_im_status = active_im_status;
    }

    public String getMeQQ() {
        return meQQ;
    }

    public void setMeQQ(String meQQ) {
        this.meQQ = meQQ;
    }

    public String getMeWeiXin() {
        return meWeiXin;
    }

    public void setMeWeiXin(String meWeiXin) {
        this.meWeiXin = meWeiXin;
    }

    @Override
    public String toString() {
        return "UserProfiles{" +
                "ssoId='" + ssoId + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", domain='" + domain + '\'' +
                ", gender=" + gender +
                ", isVip=" + isVip +
                ", active_im_status=" + active_im_status +
                ", meQQ='" + meQQ + '\'' +
                ", meWeiXin='" + meWeiXin + '\'' +
                '}';
    }
}
