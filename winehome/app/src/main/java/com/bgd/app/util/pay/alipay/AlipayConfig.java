package com.bgd.app.util.pay.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 支付宝配置
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-11
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipayconfig")
public class AlipayConfig {

    // 商户appid
    private String app_id;
    // 私钥 pkcs8格式的
    private String rsa_private_key;
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问

    private String notify_url;
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址

    private String return_url;
    // 请求网关地址
    private String url = "https://openapi.alipay.com/gateway.do";

    // 编码
    public  String charset;
    // 返回格式
    public  String format;
    // 支付宝公钥
    public String alipay_public_key;
    // RSA2
    public  String signtype;

    // 是否沙箱环境,1:沙箱,0:正式环境
    private Short isSandbox = 0;

    /**
     * 初始化支付宝配置
     *
     * @param configParam
     * @return
     */
    public AlipayConfig init(String configParam) {
        Assert.notNull(configParam, "init alipay config error");
        JSONObject paramObj = JSON.parseObject(configParam);
        //this.setApp_id(paramObj.getString("appid"));
        //this.setRsa_private_key(paramObj.getString("private_key"));
        //this.setAlipay_public_key(paramObj.getString("alipay_public_key"));
        this.setIsSandbox(paramObj.getShortValue("isSandbox"));
        if (this.getIsSandbox() == 1) this.setUrl("https://openapi.alipaydev.com/gateway.do");
        return this;
    }
}

