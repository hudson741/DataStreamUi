package com.yss.yarn.controller;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.yss.Expansion.ExpYamlDataBase;
import com.yss.Expansion.JschUtil;
import com.yss.config.Conf;
import com.yss.util.FileUtil;
import com.yss.util.PropertiesUtil;
import com.yss.yarn.Exception.HadoopUserAddException;
import com.yss.yarn.Exception.KnownHostException;
import com.yss.yarn.Exception.PasswdModifyException;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/7/26
 */

@RestController
public class ExpansionController {

    private Logger logger = LoggerFactory.getLogger(ExpansionController.class);

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
                                          @RequestParam("remoteHost") String remoteHost) throws InterruptedException, JSchException, IOException {



        JschUtil.generateRemoteSshKeygen(JschUtil.createSession(remoteUser, password, remoteHost));
        return "true";
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
    public String setRemoteKnownHosts(@RequestParam("user") String user,
                                      @RequestParam("password") String password,
                                      @RequestParam("host") String host) throws JSchException, UnknownHostException {
        JschUtil.setRemoteKnownHosts(user,password,host);
        return "true";
    }

    /**
     * 设置本机knownhosts (1)
     * @param user
     * @return
     * @throws JSchException
     * @throws UnknownHostException
     */
    @RequestMapping(
            value  = "/setLocalKnownHosts",
            method = RequestMethod.GET
    )
    public String setLocalKnownHosts( @RequestParam("user") String user,
                                      @RequestParam("password") String password) throws JSchException, UnknownHostException {
        JschUtil.setLocalKnownHosts(user,password);
        return "true";
    }

    /**
     * 传输远程id_rsa至本地并写入authorized_keys（4）
     * @param remoteUser
     * @param password
     * @param remoteHost
     * @return
     * @throws InterruptedException
     * @throws JSchException
     * @throws IOException
     */
    @RequestMapping(
            value  = "/scpRemoteIdRsaPub",
            method = RequestMethod.GET
    )
    public String scpRemoteIdRsaPub(@RequestParam("remoteUser") String remoteUser,
                                    @RequestParam("password") String password,
                                    @RequestParam("remoteHost") String remoteHost) throws InterruptedException, JSchException, IOException {
        JschUtil.scpRemoteIdRsaPub(JschUtil.createSession(remoteUser, password, remoteHost), remoteUser, remoteHost);
        JschUtil.localWriteFileAppend("/"+remoteUser+"/.ssh/"+remoteHost+".pub",
                "/"+remoteUser+"/.ssh/authorized_keys");
        return "true";
    }

    /**
     * 将本地knownHosts同步更新至远程服务器 (5)
     * @param user
     * @param password
     * @param host
     * @return
     */
    @RequestMapping(
            value  = "/urkhs",
            method = RequestMethod.GET
    )
    public String updateRemoteKnownHosts(@RequestParam("user") String user,
                                         @RequestParam("password") String password,
                                         @RequestParam("host") String host) throws JSchException {

        Session session = JschUtil.createSession(user, password, host);

        JschUtil.updateRemoteKnownHosts(session,user);
        return "true";
    }

    /**
     * 将本地authorized_keys同步更新至远程服务器 (6)
     * @param user
     * @param password
     * @param host
     * @return
     */
    @RequestMapping(
            value  = "/urHaks",
            method = RequestMethod.GET
    )
    public String updateRemoteHostAuthorizedKeys(@RequestParam("user") String user,
                                                 @RequestParam("password") String password,
                                                 @RequestParam("host") String host) throws JSchException {

        JschUtil.updateRemoteHostAuthorizedKeys(JschUtil.createSession(user, password, host),user);
        return "true";
    }


    /**
     * 一键初始化本地节点
     * @param user
     * @param password
     * @return
     */
    @RequestMapping(
            value  = "/initLocal",
            method = RequestMethod.GET
    )
    public String initLocalNode(@RequestParam("user") String user,
                                @RequestParam("password") String password,
                                @RequestParam("host") String host) throws JSchException, IOException, InterruptedException {


        Session session = JschUtil.createSession(user, password, host);
        JschUtil.delRemoteSSHDir(session,user);
        //1,将本机加入本机的信任列表中
        JschUtil.setLocalKnownHosts(user,password);
        //1，生成本机的秘钥key
        JschUtil.generateRemoteSshKeygen(session);
        //2,更新本机的authorized_keys
        JschUtil.localWriteFileAppend("/"+user+"/.ssh/id_rsa.pub",
                "/"+user+"/.ssh/authorized_keys");

        return "true";
    }


    /**
     * 获取已注册的物理节点信息
     * @return
     */
    @RequestMapping(
            value  = "/nodeM",
            method = RequestMethod.GET
    )
    public String getPhysicalNodes()  {

        LinkedHashMap<String,ExpYamlDataBase.PhysicalNode> map = ExpYamlDataBase.getNodes();

        List<ExpYamlDataBase.PhysicalNode> nodes = Lists.newArrayList();

        for(String id:map.keySet()){
            StringBuilder runningProcess = new StringBuilder();

            ExpYamlDataBase.PhysicalNode physicalNode = map.get(id);
           physicalNode.setId(id);
           Session session = null;
           try {
               session = JschUtil.createSession(physicalNode.getUser(),physicalNode.getPassword(),physicalNode.getHost());
               logger.info("host : "+physicalNode.getHost());
               boolean isNodeManagerRunning = JschUtil.isNodeManagerRunning(session);
               boolean isResourceManagerRunning = JschUtil.isResourceManagerRunning(session);
               if(isNodeManagerRunning){
                   runningProcess.append("NodeManager ");
               }
               if(isResourceManagerRunning){
                   runningProcess.append("resourceManager ");
               }
               physicalNode.setRunningProcess(runningProcess.toString());

           } catch (Throwable e) {
               logger.error("error ",e);
               runningProcess.append("节点连接异常,请重新进行配置");

           }

           nodes.add(physicalNode);
       }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nodes",nodes);
        return jsonObject.toJSONString();

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
        ExpYamlDataBase.removeNode(key);
        return "已删除";

    }

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
        LinkedHashMap<String,ExpYamlDataBase.PhysicalNode>  map =  ExpYamlDataBase.getNodes();
        ExpYamlDataBase.PhysicalNode physicalNode =  map.get(id);
        boolean result = JschUtil.checkConnect(physicalNode.getUser(), physicalNode.getPassword(),physicalNode.getHost());
        if(result){
            return "已成功注册";
        }else{
            return "请删除节点，并重新注册";
        }
    }


    @RequestMapping(
            value = "/registerRoot",
            method = RequestMethod.POST
    )
    public String registerRoot(@RequestParam("admin") String admin,@RequestParam("pwd") String pwd) {

        String localHostName = null;
        try {
            localHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("error ",e);
            return "请确保本地hosts配置正确";
        }

        Session rootLocalSession = null;
        try {
            rootLocalSession = JschUtil.createSession(admin,pwd,localHostName);
        } catch (JSchException e) {
            logger.error("error",e);
            return "admin用户验证失败，请确保用户密码正确，用户可用";
        }

        try {
            registerHadoop(rootLocalSession);
        } catch (HadoopUserAddException e) {
            return e.getMessage();
        } catch (KnownHostException e) {
            return e.getMessage();
        } catch (PasswdModifyException e) {
            return e.getMessage();
        }
        return "注册成功";

    }

    /**
     * 注册hadoop用户
     * @param rootLocalSession
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws JSchException
     */
    private void registerHadoop(Session rootLocalSession) throws HadoopUserAddException, KnownHostException, PasswdModifyException {

        String localHostName = null;
        try {
            localHostName = InetAddress.getLocalHost().getHostName();
            boolean exists = JschUtil.checkLocalUserExsits("hadoop");
            if (!exists) {
                File del = new File("/home/hadoop");
                if (del.exists()) {
                    del.delete();
                }
                logger.warn("本地不存在hadoop用户，下面开始创建.....");
                //安全起见，先创建/home目录，已存在则忽略
                JschUtil.mkdirRemoteDir(rootLocalSession,"/home");

                boolean result = false;
                try {
                    result = JschUtil.localUserAdd(rootLocalSession, "hadoop", "Tudou=123");
                } catch (IOException e) {
                    logger.error("error ",e);
                    throw new HadoopUserAddException("本地hadoop用户验证或创建失败，请查看系统日志，并请确认root密码，权限是否正确");

                } catch (InterruptedException e) {
                    logger.error("error ",e);
                    throw new HadoopUserAddException("本地hadoop用户验证或创建失败，请查看系统日志，并请确认root密码，权限是否正确");

                }
                if (!result) {
                    throw new HadoopUserAddException("本地hadoop用户验证或创建失败，请查看系统日志，并请确认root密码，权限是否正确");
                }

                //修改新增用户hadoop的密码
                if(!JschUtil.passwdUser(rootLocalSession, "hadoop", "Tudou=123")){
                    throw new PasswdModifyException("修改hadoop密码失败，请删除hadoop用户进行重试");
                }
                //获取新用户session
                Session hadoopLocalSession = JschUtil.createSession("hadoop", "Tudou=123", localHostName);
                //使用新用户创建.ssh目录
                JschUtil.mkdirRemoteDir(hadoopLocalSession, "/home/hadoop/.ssh/");
                //将本地设置信任列表
                JschUtil.setRemoteKnownHosts("hadoop", "Tudou=123", localHostName);
                //生成本机的秘钥key
                JschUtil.generateRemoteSshKeygen(hadoopLocalSession);
                //更新本机的authorized_keys
                JschUtil.localWriteFileAppend("/home/hadoop/.ssh/id_rsa.pub",
                        "/home/hadoop/.ssh/authorized_keys");

            }
            logger.info("已创建hadoop用户，无需再创建");
        } catch (UnknownHostException e) {
            logger.error("e ",e);
            throw new KnownHostException("请确保本地hosts配置正确");
        } catch (InterruptedException e) {
            logger.error("e ",e);
            throw new HadoopUserAddException("设置hadoop用户与本机实现免密登录失败，请手动完成，或删除后重试");
        } catch (IOException e) {
            logger.error("e ",e);
            throw new HadoopUserAddException("设置hadoop用户与本机实现免密登录失败，请手动完成，或删除后重试");
        } catch (JSchException e) {
            logger.error("e ",e);
            throw new HadoopUserAddException("设置hadoop用户与本机实现免密登录失败，请手动完成，或删除后重试");
        }


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
    public String remoteHadoopST(@RequestParam("key") final String key,@RequestParam("hcmd") final String hcmd) {
        LinkedHashMap<String,ExpYamlDataBase.PhysicalNode> map = ExpYamlDataBase.getNodes();
        if(StringUtils.isEmpty(hcmd) || "undefined".equals(hcmd)){
            return "请选择启动模式";
        }
        if(StringUtils.isEmpty(key)){
            return "请选择启动节点";
        }
        final ExpYamlDataBase.PhysicalNode physicalNode =  map.get(key);
        try {
            Session session = JschUtil.createSession(physicalNode.getUser(),physicalNode.getPassword(),physicalNode.getHost());
            String path = PropertiesUtil.getProperty("yarnHadoopHome");
            String cmd = "cd /home/hadoop/expand ; sh hadoop.sh "+hcmd+" "+path;
            String result =  JschUtil.execmd(session,cmd);
            return "执行结果:"+result;
        } catch (JSchException e) {
            logger.error("error ",e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("errpr ",e);
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
    public String remoteInstallHadoop(@RequestParam("key") final String key) throws IOException, JSchException, InterruptedException {
        LinkedHashMap<String,ExpYamlDataBase.PhysicalNode>  map =  ExpYamlDataBase.getNodes();
        final ExpYamlDataBase.PhysicalNode physicalNode =  map.get(key);
        //8,将本地hadoop,jdk,安装脚本传输至远程服务器
        new Thread(){

            @Override
            public void run(){
                try {
                    Session session = JschUtil.createSession(physicalNode.getUser(),physicalNode.getPassword(),physicalNode.getHost());
                    String cmd = "cd /home/hadoop/expand ; sh expand.sh";
                    boolean isInstalled = JschUtil.remoteInstallHadoop(session,cmd,key);
                    if(isInstalled){
                        JschUtil.execmd(session,"source /etc/profile");
                        physicalNode.setInstalled("已安装");
                        ExpYamlDataBase.updateNode(physicalNode);
                    }
                } catch (JSchException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }.start();

        return "正在执行远程部署，可能需要3至5分钟的时间，可通过日志，查看进度，也可通过刷新界面查看物理节点是否安装成功";
    }

    // 读取文件指定行。
    @RequestMapping(
            value  = "/logio",
            method = RequestMethod.GET
    )
    static String logio(@RequestParam("key") String key)
            throws IOException {
        //BufferedReader br = new BufferedReader(new FileReader("/opt/log.log"));
        String logPath = FileUtil.getJarPath(JschUtil.class) + "/log/"+key+".log";
        if(!new File(logPath).exists()){
            return "";
        }

        BufferedReader br = new BufferedReader(new FileReader(logPath));
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while((line = br.readLine())!=null){
            stringBuffer.append(line+"------>");
        }

        return stringBuffer.toString();
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
    public String expand(@RequestParam("rAdmin") String rAdmin,
                         @RequestParam("password") String password,
                         @RequestParam("IP") String ip,
                         @RequestParam("host") String host) throws JSchException, IOException, InterruptedException {
        boolean exists = JschUtil.checkLocalUserExsits("hadoop");
        if(!exists){
            return "请先创建本地hadoop用户";
        }

        Session hadoopLocalSession = null;
        Session hadoopRemoteSession = null;
        Session rootRemoteSession = JschUtil.createSession(rAdmin,password,host);
        try {
            List<String> data = Lists.newArrayList();
            data.add(ip + "   " + host);
            String localHostName = InetAddress.getLocalHost().getHostName();

            //3，清除/etc/hosts旧的匹配
            JschUtil.delFileMatch("/etc/hosts", " " + host);
            //4,更新本地/etc/hosts 加入扩展节点的hosts 和 ip映射
            JschUtil.localWriteFileAppend(data, "/etc/hosts");

            //5,创建远程hadoop用户,如果存在，则不给于添加
            boolean isRemoteUserExsits = JschUtil.checkRemoteUserExsits(rAdmin, password, host, "hadoop");
            if (!isRemoteUserExsits) {
                //安全起见，先创建/home目录，已存在则忽略
                JschUtil.mkdirRemoteDir(rootRemoteSession,"/home");
                logger.info("开始创建远程hadoop用户");
                boolean result = JschUtil.remoteAddUser(rootRemoteSession, host, "hadoop", "Tudou=123");
                if (!result) {
                    return "远程hadoop用户验证或创建失败，请查看系统日志，并请确认root密码，权限是否正确";
                }
                //修改远程用户hadoop密码
                JschUtil.remoteUserPasswd(rAdmin, password, host, "hadoop", "Tudou=123");
            }
            hadoopRemoteSession = JschUtil.createSession("hadoop", "Tudou=123", host);


            //6,设置远程hosts加入本机信任列表
            JschUtil.setRemoteKnownHosts("hadoop", "Tudou=123", host);

            //7,清空远程ssh目录
            JschUtil.delRemoteSSHDir(hadoopRemoteSession, "hadoop");
            //8,连接到远程机器，生成秘钥串
            JschUtil.generateRemoteSshKeygen(hadoopRemoteSession);
            //9,将远程生成的公钥传入到本地
            String lfile = JschUtil.scpRemoteIdRsaPub(hadoopRemoteSession, "hadoop", host);
            //10,清除旧的匹配
            JschUtil.delFileMatch("/home/hadoop/.ssh/authorized_keys", "hadoop@" + host);
            JschUtil.localWriteFileAppend(lfile,
                    "/home/hadoop/.ssh/authorized_keys");
            //11,将本机hosts覆盖至远程hosts                              //需做分发
            JschUtil.updateRemoteHosts(rootRemoteSession);
            //12,将本地的机器信任列表传输至远程机器上
            JschUtil.updateRemoteKnownHosts(hadoopRemoteSession,"hadoop");   //需做分发
            //13,将本地的authorized_keys覆盖远程机器                     //需做分发
            JschUtil.updateRemoteHostAuthorizedKeys(hadoopRemoteSession, "hadoop");
            LinkedHashMap<String, ExpYamlDataBase.PhysicalNode> list = ExpYamlDataBase.getNodes();

            //14,更新其他已注册的物理节点
            for (ExpYamlDataBase.PhysicalNode physicalNode : list.values()) {
                Session sessionSend = JschUtil.createSession(physicalNode.getAdmin(), physicalNode.getAdminPassWord(), physicalNode.getHost());
                JschUtil.updateRemoteHosts(sessionSend);
                Session sessionUser = JschUtil.createSession(physicalNode.getUser(),physicalNode.getPassword(),physicalNode.getHost());
                JschUtil.updateRemoteKnownHosts(sessionUser,physicalNode.getUser());
                JschUtil.updateRemoteHostAuthorizedKeys(sessionUser, physicalNode.getUser());

            }

            //添加至已注册物理节点信息
            ExpYamlDataBase.addNode(host, ip, "hadoop", "Tudou=123",rAdmin,password);
            //8,将本地hadoop,jdk,安装脚本传输至远程服务器
            String expandDirectory = FileUtil.getJarPath(ExpansionController.class) + "/expand";
            if (hadoopLocalSession == null) {
                hadoopLocalSession = JschUtil.createSession("hadoop", "Tudou=123", localHostName);
            }
            JschUtil.scpLocalDirToRemote(hadoopLocalSession, "hadoop", host, "/home/hadoop", expandDirectory);
        }finally {

        }

        return "true";
    }

    /**
     * 配置同步
     * @param key
     * @return
     * @throws IOException
     * @throws JSchException
     */
    @RequestMapping(
            value="/confSyn",
            method = RequestMethod.GET
    )
    public String confSyn(@RequestParam("key") String key) throws IOException, JSchException, InterruptedException {
        String localHostName = InetAddress.getLocalHost().getHostName();
        ExpYamlDataBase.PhysicalNode physicalNode = ExpYamlDataBase.getNodes().get(key);
        String confDir = FileUtil.getJarPath(ExpansionController.class) + "/conf/hadoop";
        Session hadoopLocalSession = JschUtil.createSession("hadoop", "Tudou=123", localHostName);
        JschUtil.scpLocalDirToRemote(hadoopLocalSession,physicalNode.getUser(),physicalNode.getHost(),"/home/hadoop/hadoop-2.8.0/etc/",confDir);
        return "true";

    }


}
