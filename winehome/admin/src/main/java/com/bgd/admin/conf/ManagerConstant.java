package com.bgd.admin.conf;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/6
 * @描述
 */
public interface ManagerConstant {

    public interface ADDR_TYPE {
        public static final Integer 默认 = 1; //创建
        public static final Integer 常用 = 2; //默认

    }

    public interface OPERA_TYPE {
        public static final Integer CREATE = 0; //创建
        public static final Integer UPDATE = 1; //修改
        public static final Integer DEL = 2; //删除

    }

    public interface REDIS {

        public static final String ADDR = "vip:addr:";
        public static final String COUNTRY = "mall:country:";//国家
        public static final String CHATEAU = "mall:chateau:";//酒庄
        public static final String CATEGORY = "product:category:";//产品类别
        public static final String PRODUCT = "product:information:";//产品明细
        public static final String COUPONS = "mall:coupons:";//优惠券
        public static final String ACTIVITY = "mall:activity:";//活动
        public static final String ACTIVITYPRO = "mall:activityPro:";//活动商品
        public static final String ADVERTISING = "mall:advertising:";//广告
        public static final String ADVPRO = "mall:advPro:";//广告商品
        public static final String BANNER = "mall:banner:";//广告轮播图
        public static final String ARTICLE = "mall:article:";//贴
        public static final String PUSH = "mall:push:";//首页推荐

    }

    public interface VIP_LEVEL {
        public static final Long 普通 = 0L;
        public static final Long 白银 = 1L;
        public static final Long 黄金 = 2L;
        public static final Long 铂金 = 3L;
        public static final Long 钻石 = 4L;
        public static final Long 至尊 = 5L;
        public static final Long 终生 = 6L;
    }

}
