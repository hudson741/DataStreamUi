package com.yss.Expansion;

import java.io.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.yss.auth.JwtUtil;
import org.apache.commons.lang.StringUtils;

import org.assertj.core.util.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import com.yss.util.FileUtil;
import com.yss.util.PropertiesUtil;
import com.yss.Expansion.Exception.*;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/7/26
 */
@RestController
public class ExpansionController {

    static class Node{
        private String name;
        private String age;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

    private Logger logger = LoggerFactory.getLogger(ExpansionController.class);

    /**
     * 测试本地是否已和指定节点有互通
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @RequestMapping(
        value  = "/checkConnect",
        method = RequestMethod.GET
    )
    public String checkConnect(@RequestParam("id") String id) throws IOException, InterruptedException {
        LinkedHashMap<String, ExpYamlDataBase.PhysicalNode> map          = ExpYamlDataBase.getNodes();
        String                                              hadoopUser   = PropertiesUtil.getProperty("hadoopUser");
        String                                              hadoopUserPd = JwtUtil.getPassWord(PropertiesUtil.getProperty("hadoopUserPd"));
        ExpYamlDataBase.PhysicalNode                        physicalNode = map.get(id);
        boolean                                             result       = JschService.checkConnect(hadoopUser,
                                                                                                    hadoopUserPd,
                                                                                                    physicalNode.getHost());

        if (result) {
            return "已成功注册";
        } else {
            return "请删除节点，并重新注册";
        }
    }

    /**
     * 配置同步
     * @param key
     * @return
     * @throws IOException
     * @throws JSchException
     */
    @RequestMapping(
        value  = "/confSyn",
        method = RequestMethod.GET
    )
    public String confSyn(@RequestParam("key") String key) throws Exception {
        String                       localHostName = InetAddress.getLocalHost().getHostName();
        ExpYamlDataBase.PhysicalNode physicalNode  = ExpYamlDataBase.getNodes().get(key);
        String                       confDir       = FileUtil.getJarPath(ExpansionController.class) + "/conf/hadoop";

        String lUser = PropertiesUtil.getProperty("hadoopUser");
        String lpassword =JwtUtil.getPassWord(PropertiesUtil.getProperty("hadoopUserPd"));

        String yarnhome = PropertiesUtil.getProperty("yarnHadoopHome");

        JschService.scpLocalDirToRemote(lUser,
                                        lpassword,
                                        localHostName,
                                        physicalNode.getUser(),
                                        physicalNode.getHost(),
                                        yarnhome+"/etc/",
                                        confDir);

        return "true";
    }

    /**
     * 删除已注册的物理节点
     * @return
     */
    @RequestMapping(
        value  = "/delPNodes",
        method = RequestMethod.GET
    )
    public String delPhysicalNodes(@RequestParam("key") String key) throws IOException {
        LinkedHashMap<String, ExpYamlDataBase.PhysicalNode> map          = ExpYamlDataBase.getNodes();
        ExpYamlDataBase.PhysicalNode                        physicalNode = map.get(key);
        String                                              hadoopUser   = PropertiesUtil.getProperty("hadoopUser");

        // 停止远程服务器上启动的yarn进程
        try {
            String   path = PropertiesUtil.getProperty("yarnHadoopHome");
            String[] cmds       = { "cd /home/" + hadoopUser + "/expand",
                    " echo '" + physicalNode.getPassword() + "' |sudo -S sh hadoop.sh  rstop " + path,
                    " echo '" + physicalNode.getPassword() + "' |sudo -S sh hadoop.sh  nstop " + path};

            JschProxy.execmd(physicalNode.getAdmin(), physicalNode.getAdminPassWord(), physicalNode.getHost(), cmds);
        } catch (Exception e) {
            logger.error("error ", e);
        }

        // 清理已建立的ssd连接
        JschPoolKey jschPoolKey = new JschPoolKey();

        jschPoolKey.setHost(physicalNode.getHost());
        jschPoolKey.setPassword(physicalNode.getPassword());
        jschPoolKey.setUser(physicalNode.getUser());
        JschProxy.clear(jschPoolKey);
        ExpYamlDataBase.removeNode(key);

        return "已删除";
    }

    /**
     * 一键扩容yarn节点
     * @param rAdmin
     * @param password
     * @param host
     * @return
     */
    @RequestMapping(
        value  = "/expand",
        method = RequestMethod.POST
    )
    public String expand(@RequestParam("rAdmin") String rAdmin, @RequestParam("password") String password,
                         @RequestParam("IP") String ip, @RequestParam("host") String host) {


//        String      hadoopUser1          = PropertiesUtil.getProperty("hadoopUser");
//        String      id1                  = UUID.randomUUID().toString();
//        String      hadoopUserPd1        = JwtUtil.getPassWord(PropertiesUtil.getProperty("hadoopUserPd"));
//
//        try {
//            ExpYamlDataBase.addNode(host, ip, hadoopUser1, hadoopUserPd1, rAdmin, password, id1);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//        if(StringUtils.isNotEmpty(hadoopUser1)) {
//            return "true";
//        }

        boolean exists = false;

        try {
            String user = PropertiesUtil.getProperty("hadoopUser");
            exists = JschService.checkLocalUserExsits(user);
        } catch (IOException e) {
            logger.error("error ", e);

            return "本地hadoop用户验证失败，产生IO错误，请查看系统日志";
        } catch (InterruptedException e) {
            return "本地hadoop用户验证失败，请稍后尝试";
        }

        if (!exists) {
            return "请先创建本地hadoop用户";
        }

        String      hadoopUser          = PropertiesUtil.getProperty("hadoopUser");
        String      hadoopUserPd        = JwtUtil.getPassWord(PropertiesUtil.getProperty("hadoopUserPd"));
        String      id                  = UUID.randomUUID().toString();
        PrintWriter pw                  = null;

        try {
            String logPath = null;

            //先看远程服务器是否有安装docker daemon
            if(!JschService.checkIsDockerInstalled(rAdmin,password,host)){
                return "请预先安装docker运行环境,并创建好overlay网络环境";
            }

            try {
                logPath = FileUtil.getJarPath(JschService.class) + "/log";

                File logDir = new File(logPath);

                if (!logDir.exists()) {
                    logDir.mkdir();
                }

                String logFile = (logPath + "/" + id + ".log").replace("file:", "");
                File   file    = new File(logFile);

                if (file.exists()) {
                    file.delete();
                }

                FileWriter fileWritter = new FileWriter(logFile, true);

                pw = new PrintWriter(fileWritter);
            } catch (IOException e) {
                return "创建本地log文件失败，请查看系统错误日志并重试";
            }

            List<String> data = Lists.newArrayList();

            data.add(ip + "   " + host);

            String localHostName = getLocalHostName();

            // 3，清除/etc/hosts旧的匹配
            JschService.delFileMatch("/etc/hosts", " " + host);
            pw.println("已清除 /etc/hosts 关于" + host + "的旧匹配行");
            pw.flush();

            // 4,更新本地/etc/hosts 加入扩展节点的hosts 和 ip映射
            JschService.localWriteFileAppend(data, "/etc/hosts");
            pw.println("更新本地/etc/hosts 加入扩展节点" + host + " " + ip + "的映射");
            pw.flush();

            // 5,创建远程hadoop用户,如果存在，则不给于添加
            boolean isRemoteUserExsits = JschService.checkRemoteUserExsits(rAdmin, password, host, hadoopUser);

            pw.println("查看远程hadoop用户是否存在");
            pw.flush();

            if (!isRemoteUserExsits) {

                // 安全起见，先创建/home目录，已存在则忽略
                logger.info("开始创建远程hadoop用户");
                pw.println("开始创建远程hadoop用户");
                pw.flush();

                boolean result = JschService.remoteAddUser(rAdmin, password, host, "hadoop", hadoopUserPd);

                if (!result) {
                    return "远程hadoop用户验证或创建失败，请查看系统日志，并请确认root密码，权限是否正确";
                }

                // 修改远程用户hadoop密码
                boolean isPasswd = JschService.remoteUserPasswd(rAdmin, password, host, hadoopUser, hadoopUserPd);

                pw.println("修改远程hadoop用户密码");
                pw.flush();

                if (!isPasswd) {
                    return "修改远程hadoop密码失败，请查看系统日志并重试";
                }
            }

            // 添加远程hadoop用户sudo权限
            JschService.sudoRemoteHadoop(rAdmin, password, host, hadoopUser);
            pw.println("为远程hadoop用户添加sudo权限");
            pw.flush();

            // 6,设置远程hosts加入本机信任列表
            JschService.setRemoteKnownHosts(hadoopUser, hadoopUserPd, host);
            pw.println("设置远程hosts加入本机信任列表");
            pw.flush();

            // 7,清空远程ssh目录,连接到远程机器，生成秘钥串
            JschService.generateRemoteSshKeygen(hadoopUser, hadoopUserPd, host);
            pw.println("清空远程/home/hadoop/.ssh目录");
            pw.println("生成远程hadoop秘钥");
            pw.flush();

            // 9,将远程生成的公钥传入到本地
            String lfile = JschProxy.scpRemoteIdRsaPub(hadoopUser, hadoopUserPd, host);

            pw.println("传输远程hadoop秘钥至本地");
            pw.flush();

            // 10,清除旧的匹配
            JschService.delFileMatch("/home/hadoop/.ssh/authorized_keys", hadoopUser + "@" + host);
            pw.println("清除本地authorized关于远程hadoop的旧匹配行");
            pw.flush();
            JschService.localWriteFileAppend(lfile, "/home/" + hadoopUser + "/.ssh/authorized_keys");
            pw.println("覆盖远程hadoop的authorized_keys");
            pw.flush();

            // 11,将本机hosts覆盖至远程hosts                              //需做分发
            JschService.updateRemoteHosts(rAdmin, password, host);
            pw.println("覆盖远程hosts文件");
            pw.flush();

            // 12,将本地的机器信任列表传输至远程机器上
            JschService.updateRemoteKnownHosts(hadoopUser, hadoopUserPd, host);    // 需做分发

            // 13,将本地的authorized_keys覆盖远程机器                     //需做分发
            JschService.updateRemoteHostAuthorizedKeys(hadoopUser, hadoopUserPd, host);

            LinkedHashMap<String, ExpYamlDataBase.PhysicalNode> list = ExpYamlDataBase.getNodes();

            // 14,更新其他已注册的物理节点
            for (ExpYamlDataBase.PhysicalNode physicalNode : list.values()) {
                JschService.updateRemoteHosts(physicalNode.getAdmin(),
                                              physicalNode.getAdminPassWord(),
                                              physicalNode.getHost());
                JschService.updateRemoteKnownHosts(physicalNode.getUser(),
                                                   physicalNode.getPassword(),
                                                   physicalNode.getHost());
                JschService.updateRemoteHostAuthorizedKeys(physicalNode.getUser(),
                                                           physicalNode.getPassword(),
                                                           physicalNode.getHost());
            }

            pw.println("已更新其他注册的物理节点机器");
            pw.flush();

            // 8,将本地hadoop,jdk,安装脚本传输至远程服务器
            logger.info("开始传输hadoop压缩包，jdk压缩包至 " + host + " 此过程相对时间较长");
            pw.println("开始传输hadoop压缩包，jdk压缩包至 " + host + " 此过程相对时间较长");
            pw.flush();

            String expandDirectory = null;

            try {
                expandDirectory = FileUtil.getJarPath(ExpansionController.class) + "/expand";
            } catch (IOException e) {
                logger.error("error ", e);

                return "获取扩容expand文件夹所在位置失败，请查看系统日志，并重试";
            }

            JschService.scpLocalDirToRemote(hadoopUser,
                                            hadoopUserPd,
                                            localHostName,
                                            hadoopUser,
                                            host,
                                            "/home/" + hadoopUser,
                                            expandDirectory);

            logger.info("开始解压hadoop压缩包,此过程相对时间较长");

            // 添加至已注册物理节点信息
            pw.println("远程安装hadoop,jdk,此过程相对时间较长");
            pw.flush();

            boolean isUntar = JschService.remoteInstallHadoop(hadoopUser, hadoopUserPd, host, id);

            if (!isUntar) {
                return "远程解压hadoop,jdk安装包失败，请查看系统日志并重试";
            }

            pw.println("设置远程机器环境变量");
            pw.flush();

            logger.info("开始设置远程机器环境变量");

            boolean envSet = JschService.remoteEnvSet(rAdmin, password, host, id);

            if (!envSet) {
                return "远程环境变量设置失败";
            }

            logger.info("落地存储新增节点");
            pw.println("落地存储新增节点");
            pw.flush();
            ExpYamlDataBase.addNode(host, ip, hadoopUser, hadoopUserPd, rAdmin, password, id);

            try {

                logger.info("程执行 source /etc/profile ,使得环境变量立即生效");
                pw.println("远程执行 source /etc/profile ,使得环境变量立即生效");
                pw.flush();
                JschProxy.execmd(rAdmin, password, host, new String[] { "source /etc/profile" });
            } catch (Exception e) {
                logger.error("error :", e);
            }



            // 执行配置同步
            String confDir = null;

            try {
                logger.info("正在同步hadoop配置文件至远程......");
                confDir = FileUtil.getJarPath(ExpansionController.class) + "/conf/hadoop";
                JschService.scpLocalDirToRemote(hadoopUser,
                                                hadoopUserPd,
                                                localHostName,
                                                "hadoop",
                                                host,
                                                "/home/"+hadoopUser+"/hadoop-2.7.4/etc/",
                                                confDir);
                pw.println("执行hadoop配置同步");
                pw.flush();
            } catch (IOException e) {
                logger.error("error ", e);

                return "远程安装已完成，执行配置同步时出错，请手动执行配置同步";
            }
        } catch (DelFileMatchException e) {
            pw.println(e);
            pw.flush();

            return e.getMessage();
        } catch (LocalHostException e) {
            pw.println(e);
            pw.flush();

            return "请确认本地hosts配置正确";
        } catch (SudoRemoteHadoopException e) {
            return "为远程hadoop用户添加sudo权限失败，请查看系统错误日志并重试";
        } catch (WriteFileException e) {
            pw.println(e);
            pw.flush();

            return e.getMessage();
        } catch (RemoteKnownHostsSetException e) {
            pw.println(e);
            pw.flush();

            return "设置远程knownHosts失败，请查看系统错误日志并重试";
        } catch (GenerateRemoteSSHkeyException e) {
            pw.println(e);
            pw.flush();

            return "远程生成sshkey失败，请查看系统错误日志并重试";
        } catch (ScpRemoteIdRsaPubException e) {
            pw.println(e);
            pw.flush();

            return "传输远程hadoop的rsa_pub至本地失败，请查看系统错误日志并重试";
        } catch (RemoteHostsUpdateException e) {
            pw.println(e);
            pw.flush();

            return "覆盖远程/etc/hosts失败，请查看系统错误日志并重试";
        } catch (RemoteKnownHostsUpdateException e) {
            pw.println(e);
            pw.flush();

            return "更新远程hadoop的KnownHosts失败，请查看系统错误日志并重试";
        } catch (RemoteHostsAuthorizedKeysUpdateException e) {
            pw.println(e);
            pw.flush();

            return "更新远程hadoop的AuthorizedKeys失败，请查看系统错误日志并重试";
        } catch (LocalDirScpException e) {
            pw.println(e);
            pw.flush();

            return "传输本地文件夹至远程失败，请查看系统错误日志并重试";
        } catch (YamlDataBaseException e) {
            pw.println(e);
            pw.flush();

            return "本地yaml存储失败,请删除yaml文件并重试";
        } catch (RemoteConnectionException e) {
            pw.println(e);
            pw.flush();

            return "连接远程"+host+"失败，请确认超管用户名密码正确，并查看系统日志";
        }

        return "扩建完成";
    }

    /**
     * 创建指定ip地址的sshKey   (3)
     * @param remoteUser
     * @param password
     * @param remoteHost
     * @return
     * @throws InterruptedException
     * @throws JSchException
     * @throws IOException
     */
    @RequestMapping(
        value  = "/gRSshKey",
        method = RequestMethod.GET
    )
    public String generateRemoteSshKeygen(@RequestParam("remoteUser") String remoteUser,
                                          @RequestParam("password") String password,
                                          @RequestParam("remoteHost") String remoteHost)
            throws Exception {
        JschService.generateRemoteSshKeygen(remoteUser, password, remoteHost);

        return "true";
    }

    // 读取文件指定行。
    @RequestMapping(
        value  = "/logio",
        method = RequestMethod.GET
    )
    static String logio(@RequestParam("key") String key) throws IOException {

        // BufferedReader br = new BufferedReader(new FileReader("/opt/log.log"));
        String logPath = FileUtil.getJarPath(JschService.class) + "/log/" + key + ".log";

        if (!new File(logPath).exists()) {
            return "";
        }

        BufferedReader br           = new BufferedReader(new FileReader(logPath));
        StringBuffer   stringBuffer = new StringBuffer();
        String         line;

        while ((line = br.readLine()) != null) {
            stringBuffer.append(line + "------>");
        }

        return stringBuffer.toString();
    }

    /**
     * 注册hadoop用户
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws JSchException
     */
    private void registerHadoop(String root, String password, String host) throws Exception {
        String localHostName = null;

        try {
            String hadoopUser   = PropertiesUtil.getProperty("hadoopUser");
            String hadoopUserPd = JwtUtil.getPassWord(PropertiesUtil.getProperty("hadoopUserPd"));

            localHostName = InetAddress.getLocalHost().getHostName();

            boolean exists = JschService.checkLocalUserExsits(hadoopUser);

            if (!exists) {
                File del = new File("/home/" + hadoopUser);

                if (del.exists()) {
                    del.delete();
                }

                logger.warn("本地不存在hadoop用户，下面开始创建.....");

                // 安全起见，先创建/home目录，已存在则忽略
                JschService.mkdirRemoteDir(root, password, host, "/home");

                boolean result = false;

                try {
                    result = JschService.localUserAdd(root, password, hadoopUser, hadoopUserPd);
                } catch (IOException e) {
                    logger.error("error ", e);

                    throw new HadoopUserAddException("本地hadoop用户验证或创建失败，请查看系统日志，并请确认root密码，权限是否正确");
                } catch (InterruptedException e) {
                    logger.error("error ", e);

                    throw new HadoopUserAddException("本地hadoop用户验证或创建失败，请查看系统日志，并请确认root密码，权限是否正确");
                }

                if (!result) {
                    throw new HadoopUserAddException("本地hadoop用户验证或创建失败，请查看系统日志，并请确认root密码，权限是否正确");
                }

                // 修改新增用户hadoop的密码
                if (!JschService.localUserPasswd(root, password, localHostName, hadoopUser, hadoopUserPd)) {
                    throw new PasswdModifyException("修改hadoop密码失败，请删除hadoop用户进行重试");
                }

                // 使用新用户创建.ssh目录
                JschService.mkdirRemoteDir(hadoopUser, hadoopUserPd, localHostName, "/home/" + hadoopUser + "/.ssh/");

                // 将本地设置信任列表
                JschService.setRemoteKnownHosts(hadoopUser, hadoopUserPd, localHostName);

                // 生成本机的秘钥key
                JschService.generateRemoteSshKeygen(hadoopUser, hadoopUserPd, localHostName);

                // 更新本机的authorized_keys
                JschService.localWriteFileAppend("/home/" + hadoopUser + "/.ssh/id_rsa.pub",
                                                 "/home/" + hadoopUser + "/.ssh/authorized_keys");
            }

        } catch (UnknownHostException e) {
            logger.error("e ", e);

            throw new KnownHostException("请确保本地hosts配置正确");
        } catch (InterruptedException e) {
            logger.error("e ", e);

            throw new HadoopUserAddException("设置hadoop用户与本机实现免密登录失败，请手动完成，或删除后重试");
        } catch (IOException e) {
            logger.error("e ", e);

            throw new HadoopUserAddException("设置hadoop用户与本机实现免密登录失败，请手动完成，或删除后重试");
        } catch (JSchException e) {
            logger.error("e ", e);

            throw new HadoopUserAddException("设置hadoop用户与本机实现免密登录失败，请手动完成，或删除后重试");
        }
    }

    @RequestMapping(
        value  = "/registerRoot",
        method = RequestMethod.POST
    )
    public String registerRoot(@RequestParam("admin") String admin, @RequestParam("pwd") String pwd)  {
        String localHostName = null;

        try {
            localHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("error ", e);

            return "请确保本地hosts配置正确";
        }

        try {
            registerHadoop(admin, pwd, localHostName);
        } catch (HadoopUserAddException e) {
            return e.getMessage();
        } catch (KnownHostException e) {
            return e.getMessage();
        } catch (PasswdModifyException e) {
            return e.getMessage();
        } catch (Exception e) {
           return "注册失败";
        }

        return "注册成功";
    }

    /**
     * 远程启动hadoop
     * @param key
     * @return
     * @throws IOException
     * @throws JSchException
     */
    @RequestMapping(
        value  = "/hcmd",
        method = RequestMethod.GET
    )
    public String remoteHadoopST(@RequestParam("key") final String key, @RequestParam("hcmd") final String hcmd){
        LinkedHashMap<String, ExpYamlDataBase.PhysicalNode> map = ExpYamlDataBase.getNodes();

        if (StringUtils.isEmpty(hcmd) || "undefined".equals(hcmd)) {
            return "请选择启动模式";
        }

        if (StringUtils.isEmpty(key)) {
            return "请选择启动节点";
        }

        final ExpYamlDataBase.PhysicalNode physicalNode = map.get(key);

        try {
            String   hadoopUser = PropertiesUtil.getProperty("hadoopUser");
            String   path       = PropertiesUtil.getProperty("yarnHadoopHome");
            String[] cmds       = { "cd /home/" + hadoopUser + "/expand",
                                    " echo '" + physicalNode.getPassword() + "' |sudo -S sh hadoop.sh " + hcmd + " "
                                    + path };
            List<String> result = JschProxy.execmd(physicalNode.getUser(),
                                                   physicalNode.getPassword(),
                                                   physicalNode.getHost(),
                                                   cmds);

            return "执行结果:" + JschService.list2String(result);
        } catch (JSchException e) {
            logger.error("error ", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("errpr ", e);
        } catch (Exception e) {
            logger.error("errpr ", e);
        }

        return "启动失败,请查看系统日志";
    }

    /**
     * 远程部署安装
     * @param key
     * @return
     * @throws IOException
     * @throws JSchException
     */
    @RequestMapping(
        value  = "/remoteInstall",
        method = RequestMethod.GET
    )
    public String remoteInstallHadoop(@RequestParam("key") final String key){
        LinkedHashMap<String, ExpYamlDataBase.PhysicalNode> map          = ExpYamlDataBase.getNodes();
        final ExpYamlDataBase.PhysicalNode                  physicalNode = map.get(key);

        // 8,将本地hadoop,jdk,安装脚本传输至远程服务器
        new Thread() {
            @Override
            public void run() {
                try {
                    String  hadoopUser   = PropertiesUtil.getProperty("hadoopUser");
                    String  hadoopUserPd = JwtUtil.getPassWord(PropertiesUtil.getProperty("hadoopUserPd"));
                    boolean isUntar      = JschService.remoteInstallHadoop(hadoopUser,
                                                                           hadoopUserPd,
                                                                           physicalNode.getHost(),
                                                                           key);

                    if (isUntar) {
                        boolean envSet = JschService.remoteEnvSet(physicalNode.getAdmin(),
                                                                  physicalNode.getAdminPassWord(),
                                                                  physicalNode.getHost(),
                                                                  key);

                        if (envSet) {
                            JschProxy.execmd(physicalNode.getAdmin(),
                                             physicalNode.getAdminPassWord(),
                                             physicalNode.getHost(),
                                             new String[] { "source /etc/profile" });
                            ExpYamlDataBase.updateNode(physicalNode);
                        }
                    }
                } catch (JSchException e) {
                    logger.error("error ",e);
                } catch (IOException e) {
                    logger.error("error ",e);
                } catch (InterruptedException e) {
                    logger.error("error ",e);
                } catch (Exception e) {
                    logger.error("error ",e);
                }
            }
        }.start();

        return "正在执行远程部署，可能需要3至5分钟的时间，可通过日志，查看进度，也可通过刷新界面查看物理节点是否安装成功";
    }

    @RequestMapping(
        value  = "/runningProcess",
        method = RequestMethod.GET
    )
    public String runningProcess(@RequestParam("id") String id)  {
        LinkedHashMap<String, ExpYamlDataBase.PhysicalNode> map          = ExpYamlDataBase.getNodes();
        ExpYamlDataBase.PhysicalNode                        physicalNode = map.get(id);
        Map<String, Boolean>                                result       = null;
        try {
            result = JschService.isHadoopProcessRunning(physicalNode.getUser(),
                                               physicalNode.getPassword(),
                                               physicalNode.getHost());
        } catch (Exception e) {
            logger.error("error ",e);
            return "连接失败";
        }
        StringBuilder resultBuilder = new StringBuilder();

        if (result.get("rm")) {
            resultBuilder.append("resourceManager is running ");
        }

        if (result.get("nm")) {
            resultBuilder.append("nodeManager is running ");
        }

        if (result.get("nn")) {
            resultBuilder.append("nameNode is running ");
        }

        if (result.get("dn")) {
            resultBuilder.append("dataNode is running ");
        }

        String resultStr = resultBuilder.toString();

        if (StringUtils.isEmpty(resultStr)) {
            return "无hadoop应用运行";
        }

        return resultStr;
    }

    private String getLocalHostName() throws LocalHostException {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("error ", e);

            throw new LocalHostException(e.getMessage());
        }
    }

    /**
     * 获取已注册的物理节点信息
     * @return
     */
    @RequestMapping(
        value  = "/nodeM",
        method = RequestMethod.GET
    )
    public String getPhysicalNodes() {
        LinkedHashMap<String, ExpYamlDataBase.PhysicalNode> map   = ExpYamlDataBase.getNodes();
        List<ExpYamlDataBase.PhysicalNode>                  nodes = Lists.newArrayList();

        for (String id : map.keySet()) {
            StringBuilder                runningProcess = new StringBuilder();
            ExpYamlDataBase.PhysicalNode physicalNode   = map.get(id);

            physicalNode.setId(id);

            try {
                logger.info("host : " + physicalNode.getHost());
                physicalNode.setRunningProcess(runningProcess.toString());
            } catch (Throwable e) {
                logger.error("error ", e);
                runningProcess.append("节点连接异常,请重新进行配置");
            }

            nodes.add(physicalNode);
        }

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("nodes", nodes);

        return jsonObject.toJSONString();
    }

    /**
     * 设置远程机器至本机knownhosts (2)
     * @param user
     * @return
     * @throws JSchException
     * @throws UnknownHostException
     */
    @RequestMapping(
        value  = "/setRemoteKnownHosts",
        method = RequestMethod.GET
    )
    public String setRemoteKnownHosts(@RequestParam("user") String user, @RequestParam("password") String password,
                                      @RequestParam("host") String host)
            throws JSchException, UnknownHostException {

//      JschService.setRemoteKnownHosts(user,password,host);
        return "true";
    }
}
