/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.user.util;

/**
 * @author Bubble
 * @date 2025.02.19 14:48
 */
public class UserInfoUtil {

    /**
     * 计算用户等级
     * <p>
     *     0、LV0-非正式会员：经验值为0
     *     1、LV1-探花：经验值1~199，为新用户。<br>
     *     2、LV2-花魁：经验值200~1499，可以发布弹幕、评论等互动功能。<br>
     *     3、LV3-大夫：经验值1500~4499，可发布视频，开启直播等功能。<br>
     *     4、LV4-进士：经验值4500~10799，可以发起投稿活动，提高曝光度。<br>
     *     5、LV5-状元：经验值10800~28799，可以开通付费课程，提高收益。<br>
     *     6、LV6-天官赐福：经验值28800及以上，具有网站内特殊的身份象征和专属特权。
     * </p>
     * @param exp 用户经验值
     * @return 用户等级
     */
    public static Integer getLevel(Integer exp) {
        int level;
        if (exp > 0 && exp < 200) {
            level = 1;
        } else if (exp >= 200 && exp < 1500) {
            level = 2;
        } else if (exp >= 1500 && exp < 4500) {
            level = 3;
        } else if (exp >= 4500 && exp < 10800) {
            level = 4;
        } else if (exp >= 10800 && exp < 28800) {
            level = 5;
        } else if (exp >= 28800) {
            level = 6;
        } else {
            level = 0;
        }

        return level;
    }
}
