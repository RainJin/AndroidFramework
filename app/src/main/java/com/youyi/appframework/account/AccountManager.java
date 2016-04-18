package com.youyi.appframework.account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.youyi.appframework.AppContext;
import com.youyi.appframework.model.entity.UserProfiles;
import com.youyi.appframework.utils.AndroidUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 帐号管理者
 * Created by Rain on 2016/3/4.
 */
public class AccountManager {
    private static SharedPreferences mSpf;

    private static Account mAccount = null;

    private static class SingelHelper {
        public static AccountManager instance = new AccountManager();
    }

    public static AccountManager getInstance() {
        return SingelHelper.instance;
    }

    private AccountManager() {
        mSpf = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance());
        mAccount = getAccountFromCache();
    }

    /**
     * 保存部分数据到SP
     *
     * @param data
     */
    public synchronized void saveAccount(Bundle data) {
        Set<String> keys = data.keySet();
        SharedPreferences.Editor editor = mSpf.edit();
        for (String k : keys) {
            Object value = data.get(k);
            if (value instanceof String) {
                editor.putString(k, (String) value);
            } else if (value instanceof Long) {
                editor.putLong(k, (Long) value);
            } else if (value instanceof Integer) {
                editor.putInt(k, (Integer) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(k, (Boolean) value);
            }
        }
        editor.apply();
    }

    public synchronized Account getAccount() {
        return mAccount;
    }

    /**
     * 更新用户信息
     * 同时更新缓存在内存上的和文件上（SP）的
     *
     * @param hashMap
     */
    public void updateAccount(HashMap<NameKey, Object> hashMap) {
        if (hashMap == null || hashMap.isEmpty())
            return;

        synchronized (mAccount) {
            Set keys = hashMap.keySet();
            Iterator keyI = keys.iterator();
            //替换内存中的信息
            while (keyI.hasNext()) {
                NameKey key = (NameKey) keyI.next();
                Object v = hashMap.get(key);
                updateAccountItem(key, v);
            }
            saveAccount2Cache(hashMap);
        }
    }

    /**
     * 更新用户单条信息
     * 同时更新缓存在内存上的和文件上的
     *
     * @param key
     * @param v
     */
    public synchronized void updateAccount(NameKey key, Object v) {
        updateAccountItem(key, v);
        saveAccountItem(mSpf.edit(), key, v);
    }

    /**
     * 替换内存中每条信息
     *
     * @param key
     * @param v
     */
    private void updateAccountItem(NameKey key, Object v) {
        switch (key) {
            case SSOID:
                mAccount.ssoid = (String) v;
                break;
            case CHATUUID:
                mAccount.chatUuid = (String) v;
                break;
            case TARGETCONTACTSID:
                mAccount.targetContactsId = (String) v;
                break;
            case REALNAME:
                mAccount.realName = (String) v;
                break;
            case PHONE:
                mAccount.phone = (String) v;
                break;
            case TOKEN:
                mAccount.token = (String) v;
                break;
            case DOMAIN:
                mAccount.domain = (String) v;
                break;
            case ISVIP:
                mAccount.isVip = (String) v;
                break;
            case VERSIONCODE:
                mAccount.versionCode = (Integer) v;
                break;
            case OPENCHATED:
                mAccount.openChated = (Boolean) v;
                break;
            case MEPIC:
                mAccount.mePic = (String) v;
                break;
            case GENDER:
                mAccount.gender = (Integer) v;
                break;
            case DESCRIPT:
                mAccount.descript = (String) v;
                break;
            case CITYID:
                mAccount.cityId = (String) v;
                break;
            case CITYNAME:
                mAccount.cityName = (String) v;
                break;
            case PROVINCEID:
                mAccount.provinceId = (String) v;
                break;
            case PROVINCENAME:
                mAccount.provinceName = (String) v;
                break;
            case MEPICSTATUS:
                mAccount.mePicStatus = (Integer) v;
                break;
            case MEQQ:
                mAccount.meQQ = (String) v;
                break;
            case UUID:
                mAccount.uuid = (String) v;
                break;
            case APPUSERSID:

            case MEWEIXIN:
                mAccount.meWeiXin = (String) v;
                break;
            case NICKNAME:
                mAccount.nickName = (String) v;
                break;
            case LASTSHARETIME:
                mAccount.lastShareTime = (long) v;
                break;
        }
    }

    /**
     * 把用户信息保存在本地缓存文件（sp）
     *
     * @param hashMap
     */
    private void saveAccount2Cache(HashMap<NameKey, Object> hashMap) {
        if (hashMap == null || hashMap.isEmpty())
            return;

        Set keys = hashMap.keySet();
        Iterator keyI = keys.iterator();
        SharedPreferences.Editor editor = mSpf.edit();
        while (keyI.hasNext()) {
            NameKey key = (NameKey) keyI.next();
            Object v = hashMap.get(key);
            saveAccountItem(editor, key, v);
        }
    }

    /**
     * 保存一条数据到本地缓存文件(sp)
     *
     * @param editor
     * @param key
     * @param v
     */
    private void saveAccountItem(SharedPreferences.Editor editor, NameKey key, Object v) {
        if (v instanceof String) {
            if (needCache(key, (String) v))
                editor.putString(key.getName(), (String) v);
        } else if (v instanceof Integer) {
            if (needCache(key, (Integer) v))
                editor.putInt(key.getName(), (Integer) v);
        } else if (v instanceof Boolean) {
            editor.putBoolean(key.getName(), (Boolean) v);
        } else if (v instanceof Long) {
            if (needCache(key, (Long) v))
                editor.putLong(key.getName(), (Long) v);
        }
        editor.apply();
    }


    /**
     * 以下几个函数：是根据类型判断是否缓存
     * 判断是否需要缓存
     *
     * @param key
     * @param value
     * @return
     */
    private boolean needCache(NameKey key, String value) {
        switch (key) {
            case TARGETCONTACTSID:
            case REALNAME:
            case PHONE:
            case TOKEN:
            case SSOID:
            case ISVIP:
            case NICKNAME:
            case DOMAIN:
            case CHATUUID:
            case MEPIC:
                return !TextUtils.isEmpty(value);
            default:
                return true;
        }
    }

    private boolean needCache(NameKey key, int value) {
        switch (key) {
            case VERSIONCODE:
                return value != 0;
            default:
                return true;
        }
    }

    private boolean needCache(NameKey key, long value) {
        switch (key) {
            case LASTSHARETIME:
                return value != 0;
            default:
                return true;
        }
    }

    /**
     * 退出登录变成游客的sp
     * 清除所有sp中的数据
     */
    public synchronized void logoutToClearSp() {

        int versionCode = AndroidUtils.getAppVersionCode(AppContext.getInstance());
        String uuid = mAccount.uuid;
        mAccount = new Account();
        mAccount.versionCode = versionCode;
        //先擦除以前的再添加，后续再拿出来
        SharedPreferences.Editor editor = mSpf.edit();
        editor.clear();
        editor.putInt(NameKey.VERSIONCODE.getName(), versionCode);
        editor.putString(NameKey.UUID.getName(), uuid);
        editor.commit();

        mAccount = getAccountFromCache();
    }


    /**
     * 获取用户详情保存至本地
     *
     * @param profiles
     */
    public synchronized void setLocalProfilesForLoadProfiles(UserProfiles profiles) {
        if (profiles == null) return;
        HashMap<NameKey, Object> info = new HashMap<NameKey, Object>();
        if (!TextUtils.isEmpty(profiles.getSsoId())) {
            mAccount.ssoid = profiles.getSsoId();
            info.put(NameKey.SSOID, profiles.getSsoId());

        }
        if (!TextUtils.isEmpty(profiles.getRealName())) {
            mAccount.realName = profiles.getRealName();
            info.put(NameKey.REALNAME, profiles.getRealName());
        }
        if (!TextUtils.isEmpty(profiles.getPhone())) {
            mAccount.phone = profiles.getPhone();
            info.put(NameKey.PHONE, profiles.getPhone());
        }
        if (!TextUtils.isEmpty(profiles.getDomain())) {
            mAccount.domain = profiles.getDomain();
            info.put(NameKey.DOMAIN, profiles.getDomain());
        }
        mAccount.provinceName = profiles.getProvinceName();
        info.put(NameKey.PROVINCENAME, profiles.getProvinceName());
        mAccount.gender = profiles.getGender();
        info.put(NameKey.GENDER, profiles.getGender());
        mAccount.cityId = profiles.getCityId();
        info.put(NameKey.CITYID, profiles.getCityId());
        mAccount.cityName = profiles.getCityName();
        info.put(NameKey.CITYNAME, profiles.getCityName());
        mAccount.provinceId = profiles.getProvinceId();
        info.put(NameKey.PROVINCEID, profiles.getProvinceId());
        mAccount.isVip = String.valueOf(profiles.getIsVip());
        info.put(NameKey.ISVIP, String.valueOf(profiles.getIsVip()));
        mAccount.openChated = profiles.getActive_im_status() == 1;
        info.put(NameKey.OPENCHATED, profiles.getActive_im_status() == 1);
        mAccount.meQQ = profiles.getMeQQ();
        info.put(NameKey.MEQQ, profiles.getMeQQ());
        mAccount.meWeiXin = profiles.getMeWeiXin();
        info.put(NameKey.MEWEIXIN, profiles.getMeWeiXin());

        saveAccount2Cache(info);
    }



    /**
     * 读取本地缓存的用户信息
     *
     * @return account
     */
    private Account getAccountFromCache() {
        Account account = new Account();
        account.ssoid = mSpf.getString(NameKey.SSOID.getName(), "");
        account.token = mSpf.getString(NameKey.TOKEN.getName(), "");
        account.phone = mSpf.getString(NameKey.PHONE.getName(), "");
        account.chatUuid = mSpf.getString(NameKey.CHATUUID.getName(), "");
        account.realName = mSpf.getString(NameKey.REALNAME.getName(), "");
        account.nickName = mSpf.getString(NameKey.NICKNAME.getName(), "");
        account.domain = mSpf.getString(NameKey.DOMAIN.getName(), "");
        account.isVip = mSpf.getString(NameKey.ISVIP.getName(), "");
        account.versionCode = mSpf.getInt(NameKey.VERSIONCODE.getName(), 0);
        account.mePic = mSpf.getString(NameKey.MEPIC.getName(), "");
        account.mePicStatus = mSpf.getInt(NameKey.MEPICSTATUS.getName(), 0);
        account.gender = mSpf.getInt(NameKey.GENDER.getName(), 0);
        account.descript = mSpf.getString(NameKey.DESCRIPT.getName(), "");
        account.cityId = mSpf.getString(NameKey.CITYID.getName(), "");
        account.cityName = mSpf.getString(NameKey.CITYNAME.getName(), "");
        account.provinceId = mSpf.getString(NameKey.PROVINCEID.getName(), "");
        account.provinceName = mSpf.getString(NameKey.PROVINCENAME.getName(), "");
        account.meQQ = mSpf.getString(NameKey.MEQQ.getName(), "");
        account.meWeiXin = mSpf.getString(NameKey.MEWEIXIN.getName(), "");
        account.appUsersId = mSpf.getString(NameKey.APPUSERSID.getName(), "");
        account.uuid = mSpf.getString(NameKey.UUID.getName(), "");
        account.targetContactsId = mSpf.getString(NameKey.TARGETCONTACTSID.getName(), "");
        account.openChated = mSpf.getBoolean(NameKey.OPENCHATED.getName(), false);
        account.lastShareTime = mSpf.getLong(NameKey.LASTSHARETIME.getName(), 0);
        return account;
    }

    /**
     * 用户信息缓存文件中各属性对应的key
     */
    public enum NameKey {
        SSOID(0, "yy_uid"),
        TOKEN(1, "token"),
        PHONE(2, "yy_phone"),
        CHATUUID(3, "chatUuid"),
        REALNAME(4, "yy_real_name"),
        NICKNAME(5, "nick_name"),
        DOMAIN(6, "domain"),
        ISVIP(7, "is_vip"),
        VERSIONCODE(8, "version_code"),
        MEPIC(9, "me_pic"),
        MEPICSTATUS(10, "me_pic_status"),
        GENDER(11, "gender"),
        DESCRIPT(12, "descript"),
        CITYID(13, "city_id"),
        CITYNAME(14, "city_name"),
        PROVINCEID(15, "province_id"),
        PROVINCENAME(16, "province_name"),
        MEQQ(17, "me_qq"),
        MEWEIXIN(18, "me_weixin"),
        APPUSERSID(19, "app_users_id"),
        UUID(20, "yy_uuid"),
        TARGETCONTACTSID(21, "target_contacts_id"),
        OPENCHATED(22, "had_open_chat"),
        LASTSHARETIME(23, "last_shared_time");
        private int id;
        private String name;

        NameKey(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }


}
