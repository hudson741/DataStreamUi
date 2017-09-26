package com.yss.storm.controller;

import com.yss.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Project Name:DataStreamUi
 * File Name:StormSDKDownloadController.java
 * Author:zhangc
 * Date:2017年5月24日下午5:16:26
 *
 */
@RestController
public class StormSDKDownloadController {

    private Logger logger = LoggerFactory.getLogger(StormSDKDownloadController.class);

    @RequestMapping(
        value  = "/filed",
        method = RequestMethod.GET
    )
    public String handleFileDown(@RequestParam("dir") String dir, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        if ((null != dir) &&!"".equals(dir)) {

            String source_directory = FileUtil.getJarPath(StormSDKDownloadController.class) + "/"+dir;


            /* String realPath = request.getServletContext().getRealPath(source_directory); */
            File file = new File(source_directory, "execute.sh");
            System.out.println(file.length());

            if (file.exists()) {
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition", "attachment;fileName=" + "execute.sh");

                byte[]              bytes = new byte[1024];
                FileInputStream     fis   = null;
                BufferedInputStream bis   = null;

                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);

                    OutputStream os = response.getOutputStream();
                    int          i  = -1;

                    while ((i = bis.read(bytes)) != -1) {
                        os.write(bytes, 0, i);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (null != bis) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (null != fis) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                throw new RuntimeException("the [" + file.getAbsolutePath() + "] file is not exists");
            }
        }

        return null;
    }
}
