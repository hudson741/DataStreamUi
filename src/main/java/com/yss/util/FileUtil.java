package com.yss.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/20
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String copy(InputStream inputStream, String targetPath, String fileName) {
        try {
            init(targetPath);

            String target     = targetPath + "/" + fileName;
            Path   path       = new File(target).toPath();
            File   targetFile = new File(path.toString());

            if (targetFile.exists()) {
                targetFile.delete();
            }

            Files.copy(inputStream, path);

            return path.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void deleteAll(String path) {
        FileSystemUtils.deleteRecursively(Paths.get(path).toFile());
    }

    public static void init(String path) {
        try {
            if (Files.notExists(Paths.get(path))) {
                logger.info("create path "+path);

//                Files.createDirectory(Paths.get(path));

                if(!new File(path).mkdir()){
                    logger.info("不知道为啥 创建不了");
                }

            }else{
                logger.info("无双=============");
            }
        } catch (Exception e) {
            logger.error("error ", e);
        }
    }

    public static Path load(String path, String filename) {
        return Paths.get(path).resolve(filename);
    }

    public static URI store(String path1, MultipartFile file) throws Exception {
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }

            init(path1);

            Path path = Paths.get(path1).resolve(file.getOriginalFilename());

            File   targetFile = new File(path.toString());

            if (targetFile.exists()) {
                targetFile.delete();
            }

            Files.copy(file.getInputStream(), path);

            return path.toUri();
        } catch (IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public static String getJarPath(Class<?> my_class) throws IOException {
        ClassLoader loader     = my_class.getClassLoader();
        String      class_file = my_class.getName().replaceAll("\\.", "/") + ".class";

        for (Enumeration<URL> itr = loader.getResources(class_file); itr.hasMoreElements(); ) {
            URL url = itr.nextElement();

            if ("jar".equals(url.getProtocol())) {
                String toReturn = url.getPath();

                toReturn = toReturn.replaceAll("\\+", "%2B");
                toReturn = URLDecoder.decode(toReturn, "UTF-8");

                String returnP = toReturn.replaceAll("!.*$", "");

                return new File(returnP).getParent().replace("file:", "");

            }
        }

        File   file = new File(loader.getResource(class_file).getFile());
        String pro  = System.getProperty("user.dir");

        while (true) {
            if (file.exists() && file.getAbsolutePath().endsWith(pro)) {
                return file.getAbsolutePath().replace("file:", "");
            } else {
                File file1 = file;

                file = new File(file1.getParent());
                logger.info("hudson " + file.getAbsolutePath());

                if (!file.exists()) {
                    return null;
                }
            }
        }
    }

    public static void main(String[] args){
        String a= "file:/Users/zhangchi/sourceCode/DataStreamUi/target/jar";
        init(a);
    }
}
