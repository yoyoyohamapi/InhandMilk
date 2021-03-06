package com.inhand.milk.entity;

import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.utils.ACache;


/**
 * Baby
 * Desc:Baby实体
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 08:37
 */
@AVClassName(Baby.BABY_CLASS)
public class Baby extends Base {
    public static final String BABY_CLASS = "Baby";
    public static final String NICKNAME_KEY = "nickname";
    public static final String BIRTHDAY_KEY = "birthday";
    public static final String HEIGHT_KEY = "height";
    public static final String WEIGHT_KEY = "weight";
    public static final String HEAD_SIZE_KEY = "head_size";
    public static final String USER_KEY = "user";
    public static final String SEX_KEY = "sex";
    public static int FEMALE = 2; // 女性
    public static int MALE = 1; // 男性

    public String getNickname() {
        return this.getString(NICKNAME_KEY);
    }

    public void setNickname(String nickname) {
        this.put(NICKNAME_KEY, nickname);
    }

    public String getBirthday() {
        return this.getString(BIRTHDAY_KEY);
    }

    public void setBirthday(String birthday) {
        this.put(BIRTHDAY_KEY, birthday);
    }

    public float getHeight() {
        return this.getInt(HEIGHT_KEY);
    }

    public void setHeight(float height) {
        this.put(HEIGHT_KEY, height);
    }

    public float getWeight() {
        return this.getInt(WEIGHT_KEY);
    }

    public void setWeight(float weight) {
        this.put(WEIGHT_KEY, weight);
    }

    public float getHeadSize() {
        return this.getInt(HEAD_SIZE_KEY);
    }

    public void setHeadSize(float headSize) {
        this.put(HEAD_SIZE_KEY, headSize);
    }

    public int getSex() {
        return this.getInt(SEX_KEY);
    }

    public void setSex(int sex) {
        this.put(SEX_KEY, sex);
    }

    public void setUser(User user) {
        try {
            this.put(USER_KEY, AVObject.createWithoutData(User.class, user.getObjectId()));
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得当前宝宝的喝奶记录
     *
     * @param ctx          上下文环境
     * @param limit        需求数目
     * @param findCallback 回调接口
     */
    public void getOnedays(Context ctx, int limit, FindCallback<OneDay> findCallback) {
        OneDayDao oneDayDao = new OneDayDao(ctx);
        //oneDayDao.findAllOrLimit(limit, findCallback);
    }

    /**
     * 存储Baby对象，若已存在，则为更新
     * @param saveCallback    回调接口
     */
    public void save(final SaveCallback saveCallback) {
        final Baby baby = this;
        if (baby.getObjectId().length() == 0) {
            baby.setUser(App.getCurrentUser());
        }
        baby.saveInBackground(saveCallback);
    }

    /**
     * 写入缓存,考虑baby对象在离线情况下始终可用，
     *
     * @param ctx 上下文环境
     * @param callback    回调接口
     */
    public void saveInCache(final Context ctx, final CacheSavingCallback callback) {
        final Baby baby = this;
        CacheSavingTask cacheSavingTask =
                new CacheSavingTask(ctx, callback) {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        ACache aCache = ACache.get(ctx);
                        aCache.put(App.BABY_CACHE_KEY, baby.toJSONObject());
                        // 同时更新CurrentBaby
                        App.currentBaby = baby;
                        return super.doInBackground(params);
                    }
                };
        cacheSavingTask.execute();
    }


}
