package com.bgd.support.base;

import lombok.Data;

/**
 * 上传图片的信息
 * @author JackMore
 */
@Data
public class UploadFileInfo {
    private String name;
    private String oldName;
    private String aliasName;
    private String fileType;
    private String suffix;
    private String domain;
    private String contextPath;
    private long size;


}
