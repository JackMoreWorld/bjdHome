package com.bgd.app.conf;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @创建人 JackMore
 * @创建时间 2019/3/14
 * @描述 全局配置参数
 */
@Data
@Component
@ConfigurationProperties(prefix = "global")
public class Global {
    private String truePath;
    private String nginxPath;
    private String defaultImg;
    private Integer corePoolSize;

}
