package com.bgd.app.conf;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/6
 * @描述
 */
public interface AppConstant {


    // 验证码类型
    public interface PHONE_CODE {
        public static final String 注册 = "redis:reg:";
        public static final String 密码 = "redis:pass:";
        public static final String 支付密码 = "redis:pay:";
    }

    // 优惠券类型
    public interface ARTICLE_TYPE {
        public static final Integer 发帖 = 0;
        public static final Integer 跟帖 = 1;
        public static final Integer 转载 = 2;
    }

    // 优惠券类型
    public interface COUPONS_TYPE {
        public static final Integer 通用 = 0;
        public static final Integer 酒庄 = 1;
    }

    // 优惠券使用状态
    public interface USED_TYPE {
        public static final Integer 未领 = 0;
        public static final Integer 已领 = 1;
        public static final Integer 已用 = 2;
        public static final Integer 作废 = 3;
    }

    //会员操作类型 1签到 2领券 ....
    public interface RECORD_TYPE {
        public static final Integer 签到 = 1;
        public static final Integer 领券 = 2;
    }

    //排序规则
    public interface SORT_TYPE {
        public static final Integer 综合 = 0;
        public static final Integer 销量 = 1;
        public static final Integer 信用 = 2;
    }

    public interface ACT_TYPE {
        public static final Integer 限时购 = 0;
        public static final Integer 有好货 = 1;
        public static final Integer 每日推荐 = 2;
        public static final Integer 新品 = 3;
        public static final Integer 主打酒 = 4;
    }


    public interface ADDR_TYPE {
        public static final Integer 默认 = 1; //创建
        public static final Integer 常用 = 2; //默认

    }


    public interface OPERA_TYPE {
        public static final Integer CREATE = 0; //创建
        public static final Integer UPDATE = 1; //修改
        public static final Integer DEL = 2; //删除

    }

    public interface CODE_TYPE {
        public static final Integer REG = 0; //注册
        public static final Integer BACKPASSWORD = 1; //找回密码
        public static final Integer ALTER = 2; //修改信息
    }

    public interface VIP_FOCUS {
        public static final Integer UNDO = -1; //取消关注
        public static final Integer DO = 1; //关注

    }


    public interface REDIS {
        public static final String REGION = "mall:region:";
        public static final String ADDR = "vip:addr:";
        public static final String CODE = "vip:code:";
        public static final String BANNER = "vip:banner:";
        public static final String AD = "vip:ad:";
        public static final String ACTIVITY = "vip:activity:";
        public static final String CART = "vip:cart:";
        public static final String CATEGORY = "vip:category:";
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

    //订单状态
    public interface ORDER_STATUS {
        //待付款
        public static final Integer DFK = 1;
        //待发货
        public static final Integer DFH = 2;
        //待退款
        public static final Integer DTK = 3;
        //已退款
        public static final Integer YTK = 4;
        //已发货
        public static final Integer YFH = 5;
        //待评价
        public static final Integer DPJ = 6;
        //已完成
        public static final Integer YWC = 7;
    }

    //付款状态
    public interface PAY_STATUS {
        public static final Integer DFK = 0;
        public static final Integer YFH = 1;
    }


    //支付方式
    public interface PAY_TYPE {
        //余额支付
        public static final Integer BALANCE_PAY = 0;
        //微信支付
        public static final Integer WeCHAT_PAY = 1;
        //支付宝支付
        public static final Integer ALI_PAY = 2;
    }

    //用户类型
    public interface USER_TYPE {
        //用户
        public static final Integer VIP = 0;
        //平台
        public static final Integer PLATFORM = 1;
    }

    //资金类型
    public interface CAPITAL_TYPE {
        //收入
        public static final Integer IN = 0;
        //支出
        public static final Integer OUT = 1;
    }

    //资金来源
    public interface CAPITAL_SOURCE {
        //订单
        public static final Integer ORDER = 0;
        //充值
        public static final Integer RECHARGE = 1;
        //退款
        public static final Integer REFUND = 2;
    }
}
