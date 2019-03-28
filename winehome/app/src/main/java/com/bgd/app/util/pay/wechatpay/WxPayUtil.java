package com.bgd.app.util.pay.wechatpay;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 微信支付配置
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-11
 */
@Data
@Component
@ConfigurationProperties(prefix = "wxconfig")
public class WxPayUtil implements WXPayConfig {


    private String appDi;
    private byte[] certData;
    private String appKey;
    private String mchDi;
    private String notifyUrl;
    private String certRootPath;
    private String spbillCreateIp;
    private String refundNotifyUrl;

    public WxPayUtil() throws Exception {
        ClassPathResource resource = new ClassPathResource("apiclient_cert.p12");
        InputStream inputStream = resource.getInputStream();
        this.certData = InputStreamToByte(inputStream);
    }


    private byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int ch;
        while ((ch = is.read(buffer)) != -1) {
            stream.write(buffer, 0, ch);
        }
        byte data[] = stream.toByteArray();
        stream.close();
        return data;
    }


    @Override
    public String getAppID() {
        return this.appDi;
    }

    @Override
    public String getMchID() {
        return this.mchDi;
    }

    @Override
    public String getKey() {
        return this.getAppKey();
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 8000;
    }
}
