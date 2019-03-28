package com.bgd.admin.service;

import com.bgd.admin.conf.Global;
import com.bgd.support.base.FileTypeEnum;
import com.bgd.support.base.UploadFileInfo;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdStringUtil;
import com.bgd.support.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件管理 Service
 * @author Sunxk
 * @since 2019-3-25
 */
@Slf4j
@Service
public class UploadManagerService {

    @Autowired
    Global global;

    /**
     *  上传图片
     * @param file 图片文件信息
     */
    public UploadFileInfo uploadImg(MultipartFile file){
        log.info("上传图片 fileName = "+file.getOriginalFilename());
        try{
            if (null == file)
                return null;

            String fileType;
            if (file.getContentType().contains("image")) {
                fileType = FileTypeEnum.PICTURE.toCode();
            } else {
                fileType = FileTypeEnum.DOCUMENT.toCode();
            }
            String path = global.getTruePath();
            String nginxPath = global.getNginxPath();
            UploadFileInfo fileInfo = new UploadFileInfo();
            fileInfo.setSuffix(FileUtils.getSuffix(file.getOriginalFilename()));
            fileInfo.setAliasName(BgdStringUtil.GenerateRanNum(8) + "." + fileInfo.getSuffix());
            fileInfo.setFileType(fileType);
            fileInfo.setSize(file.getSize() / 1000);// save size as kb
            fileInfo.setContextPath(nginxPath + fileType + FileUtils.separator() + fileInfo.getAliasName());
            FileUtils.saveFileAsStream(file.getInputStream(), path + fileType + FileUtils.separator() + fileInfo.getAliasName());
            return fileInfo;
        }catch(Exception e){
            log.error("上传图片失败 fileName = "+file.getOriginalFilename(),e);
            throw new BusinessException("上传图片失败");
        }
    }

}
