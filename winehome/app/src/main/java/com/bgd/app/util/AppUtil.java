package com.bgd.app.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.RandomUtil;
import com.bgd.support.utils.RedisUtil;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/6
 * @描述
 */
@Slf4j
@Component
public class AppUtil {
    @Autowired
    RedisUtil redis;

    private Long expireTime = 5 * 60L;

    public String sendSms(String phone, String prefix) throws BusinessException {
        try {
            DefaultProfile profile = DefaultProfile.getProfile("default", "LTAIjVvQY9zHo3hT", "fgD8Dzw5V9HHJ1YLqwRYCTqQWvkhDN");
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            String code = RandomUtil.getCode(6);
            JSONObject json = new JSONObject();
            json.put("code", code);
            String jsonCode = json.toJSONString();
            //request.setProtocol(ProtocolType.HTTPS);
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("PhoneNumbers", phone);
            request.putQueryParameter("SignName", "百加得");
            request.putQueryParameter("TemplateCode", "SMS_158905087");
            request.putQueryParameter("TemplateParam", jsonCode);
            CommonResponse response = client.getCommonResponse(request);
            log.info("手机号： " + phone + ",获取验证码为：" + code);
            redis.set(prefix + phone + code, code, expireTime);
            return code;
        } catch (Exception e) {
            log.error("获取验证码失败", e);
            throw new BusinessException();
        }
    }

    private String appId = "uRQHPOoxm17XIX8082Bvf4";
    private String appKey = "Aral2QcCHd5fhbmEACYld";
    private String masterSecret = "TRZfvktcbO6FgK5biBgxJ3";
    private String url = "https://api.getui.com/apiex.htm";

    public void AppPush(String alias) {
        // https连接
        IGtPush push = new IGtPush(appKey, masterSecret, true);
        // 此处true为https域名，false为http，默认为false。Java语⾔推荐使⽤此⽅式
        // IGtPush push = new IGtPush(host, appkey, master);
        // host为域名，根据域名区分是http协议/https协议
        LinkTemplate template = linkTemplateDemo();
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        message.setPushNetWorkType(0); // 可选，判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不限制⽹络环境。
        Target target = new Target();
        target.setAppId(appId);
        //target.setClientId(CID);
        // ⽤户别名推送，cid和⽤户别名只能2者选其⼀
        alias = "个";
        target.setAlias(alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }

    }


    public LinkTemplate linkTemplateDemo() {
        LinkTemplate template = new LinkTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 设置通知栏标题与内容
        template.setTitle("请输⼊通知栏标题");
        template.setText("请输⼊通知栏内容");
        // 配置通知栏图标
        template.setLogo("icon.png");
        // 配置通知栏⽹络图标，填写图标URL地址
        template.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        template.setIsRing(true);
        template.setIsVibrate(true);
        template.setIsClearable(true);
        // 设置打开的⽹址地址
        template.setUrl("http://www.baidu.com");
        return template;
    }
}