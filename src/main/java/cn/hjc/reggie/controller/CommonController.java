package cn.hjc.reggie.controller;

import cn.hjc.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${file.path}")
    private String basePath;

    /**
     * 保存上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> pictureUpload(MultipartFile file) {
        log.info("文件上传到: " + basePath);

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重命名，防止id重复
        String fileName = UUID.randomUUID().toString() + suffix;

        //判断是否需要创建目录
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            //保存文件
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try (FileInputStream inputStream = new FileInputStream(new File(basePath + name));
             ServletOutputStream outputStream = response.getOutputStream()) {

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();

        } catch (IOException e) {
            log.info("图片加载失败 ---> " + name);
        }

    }

}
