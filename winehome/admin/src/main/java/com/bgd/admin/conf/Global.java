package com.bgd.admin.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局配置参数
 * @author Sunxk
 * @since 2019-3-25
 */

@Data
@Component
@ConfigurationProperties(prefix = "global")
public class Global {
    private String truePath;
    private String nginxPath;
    private String defaultImg;

}
