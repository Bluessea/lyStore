package com.composer.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.sun.imageio.plugins.jpeg.JPEG;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    private static final List<String> CONTENT_TYPES = Arrays.asList("image/gif","image/jpeg","application/x-jpg");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    public String uploadImage(MultipartFile file) {

        String originalFileName = file.getOriginalFilename();
        //StringUtils.substringAfterLast(originalFileName,".");
        //校验文件类型
        String contentType = file.getContentType();
        if (!CONTENT_TYPES.contains(contentType)){
            LOGGER.info("文件类型不合法: {}",originalFileName);
            return null;
        }
        try {
            //校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null ){
                LOGGER.info("文件内容不合法: {}",originalFileName);
                return null;
            }
            //保存到文件的服务器中
            //file.transferTo(new File("H:\\STUDENT\\IDEA_Project_Menu\\OnlineStore\\testImage\\"+originalFileName));
            String ext = StringUtils.substringAfterLast(originalFileName,".");
            StorePath storePath = this.fastFileStorageClient.uploadFile(file.getInputStream(),file.getSize(),ext,null);

            //返回url进行回写
            return "http://image.ols.com/"+storePath.getFullPath();
        } catch (IOException e) {
            LOGGER.info("服务器内部错误:"+originalFileName);
            e.printStackTrace();
        }
        return null;
    }
}
