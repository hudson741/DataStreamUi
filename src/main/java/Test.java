import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.command.impl.SYST;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/30
 */
public class Test {

    private FTPClient ftp;

    public void copyFile(String sourceFileName, String sourceDir, String targetDir) throws IOException {
        ByteArrayInputStream in = null;
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        try {
//            if (!existDirectory(targetDir)) {
//                createDirectory(targetDir);
//            }
            ftp.setBufferSize(1024 * 2);
            // 变更工作路径
            ftp.changeWorkingDirectory(sourceDir);
            // 设置以二进制流的方式传输
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // 将文件读到内存中
            ftp.retrieveFile(new String(sourceFileName.getBytes("GBK"), "iso-8859-1"), fos);
            in = new ByteArrayInputStream(fos.toByteArray());
            if (in != null) {
                ftp.changeWorkingDirectory(targetDir);
                ftp.storeFile(new String(sourceFileName.getBytes("GBK"), "iso-8859-1"), in);
            }
        } finally {
            // 关闭流
            if (in != null) {
                in.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
    /**
     * @param path     上传到ftp服务器哪个路径下
     * @param addr     地址
     * @param port     端口号
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */
    private boolean connect(String path, String addr, int port, String username, String password) throws Exception {
        boolean result = false;
        ftp = new FTPClient();
        int reply;
        ftp.connect(addr, port);
        ftp.login(username, password);
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return result;
        }
        ftp.changeWorkingDirectory(path);
        result = true;
        ftp.enterLocalPassiveMode();
        return result;
    }

    /**
     * @param file 上传的文件或文件夹
     * @throws Exception
     */
    private void upload(File file) throws Exception {
            BufferedInputStream  fiStream =new BufferedInputStream(new FileInputStream(file));
            ftp.pasv();
        ftp.enterLocalPassiveMode();
            ftp.storeFile("aa.sh", fiStream);
            FTPFile ftpFile = ftp.mlistFile("aa.sh");
           System.out.println(" aa "+ftpFile.getTimestamp().getTimeInMillis());
        fiStream.close();
    }

    public  long getFtpFileTimeStamp(String address,int port,String userName,String password, String filePath) throws Exception {
        connect("/",address,port,userName,password);
        FTPFile ftpFile = ftp.mlistFile(filePath);
        System.out.println(" bb "  +ftpFile.getTimestamp().getTimeInMillis());

        FTPFile[] f=  ftp.listFiles(filePath);

        System.out.println("cc  "+f[0].getName()+"  "+f[0].getTimestamp().getTimeInMillis());


        return ftpFile.getTimestamp().getTimeInMillis();
    }

    //下载
    public void downLoad(String ftpFile, String dstFile) throws IOException {
        File file = new File(dstFile);
        FileOutputStream fos = new FileOutputStream(file);
        ftp.retrieveFile(ftpFile, fos);
        fos.close();
    }

    private Path makeAbsolute(Path workDir, Path path) {
        return path.isAbsolute()?path:new Path(workDir, path);
    }

    public static void delAuthorizedKeysMatch(String fileStr,String lineToRemove) throws IOException {
        File file = new File(fileStr);
        if(!file.exists() || !file.isFile()){
            return;
        }
        File tempFile = new File(file.getAbsolutePath() + ".tmp");
        BufferedReader br = new BufferedReader(new FileReader(file));
        PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

        String line = null;

        while ((line = br.readLine()) != null) {

            if (!line.trim().endsWith(lineToRemove)) {
                pw.println(line);
                pw.flush();
            }
        }
        pw.close();
        br.close();

        //Delete the original file
        if (!file.delete()) {
            System.out.println("Could not delete file");
            return;
        }

        //Rename the new file to the filename the original file had.
        if (!tempFile.renameTo(file))
            System.out.println("Could not rename file");
    }


    public static void main(String[] args) throws Exception {

    }
}
