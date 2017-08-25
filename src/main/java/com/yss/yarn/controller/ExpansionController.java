package com.yss.yarn.controller;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.yss.Expansion.ExpYamlDataBase;
import com.yss.Expansion.JschProxy;
import com.yss.Expansion.JschService;
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
                                          @RequestParam("remoteHost") String remoteHost) throws Exception {



        JschService.generateRemoteSshKeygen(remoteUser, password, remoteHost);
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
        JschService.setRemoteKnownHosts(user,password,host);
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
           try {
               logger.info("host : "+physicalNode.getHost());
               boolean isNodeManagerRunning = JschService.isNodeManagerRunning(physicalNode.getUser(),physicalNode.getPassword(),physicalNode.getHost());
               boolean isResourceManagerRunning = JschService.isResourceManagerRunning(physicalNode.getUser(),physicalNode.getPassword(),physicalNode.getHost());
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
        boolean result = JschService.checkConnect("hadoop", "Tudou=123",physicalNode.getHost());
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
    public String registerRoot(@RequestParam("admin") String admin,@RequestParam("pwd") String pwd) throws Exception {

        String localHostName = null;
        try {
            localHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("error ",e);
            return "请确保本地hosts配置正确";
        }

        try {
            registerHadoop(admin,pwd,localHostName);
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
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws JSchException
     */
    private void registerHadoop(String root,String password,String host) throws Exception {

        String localHostName = null;
        try {
            localHostName = InetAddress.getLocalHost().getHostName();
            boolean exists = JschService.checkLocalUserExsits("hadoop");
            if (!exists) {
                File del = new File("/home/hadoop");
                if (del.exists()) {
                    del.delete();
                }
                logger.warn("本地不存在hadoop用户，下面开始创建.....");
                //安全起见，先创建/home目录，已存在则忽略
                JschService.mkdirRemoteDir(root,password,host,"/home");

                boolean result = false;
                try {
                    result = JschService.localUserAdd(root,password, "hadoop", "Tudou=123");
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
                if(!JschService.localUserPasswd(root,password,localHostName, "hadoop", "Tudou=123")){
                    throw new PasswdModifyException("修改hadoop密码失败，请删除hadoop用户进行重试");
                }
                //使用新用户创建.ssh目录
                JschService.mkdirRemoteDir("hadoop","Tudou=123",localHostName, "/home/hadoop/.ssh/");
                //将本地设置信任列表
                JschService.setRemoteKnownHosts("hadoop", "Tudou=123", localHostName);
                //生成本机的秘钥key
                JschService.generateRemoteSshKeygen("hadoop","Tudou=123",localHostName);
                //更新本机的authorized_keys
                JschService.localWriteFileAppend("/home/hadoop/.ssh/id_rsa.pub",
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
    public String remoteHadoopST(@RequestParam("key") final String key,@RequestParam("hcmd") final String hcmd) throws Exception {
        LinkedHashMap<String,ExpYamlDataBase.PhysicalNode> map = ExpYamlDataBase.getNodes();
        if(StringUtils.isEmpty(hcmd) || "undefined".equals(hcmd)){
            return "请选择启动模式";
        }
        if(StringUtils.isEmpty(key)){
            return "请选择启动节点";
        }
        final ExpYamlDataBase.PhysicalNode physicalNode =  map.get(key);
        try {
            String path = PropertiesUtil.getProperty("yarnHadoopHome");
            String cmd = "cd /home/hadoop/expand ; sh hadoop.sh "+hcmd+" "+path;
            String result =  JschProxy.execmd(physicalNode.getUser(),physicalNode.getPassword(),physicalNode.getHost(),cmd);
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
                    String cmd = "cd /home/hadoop/expand ; sh expand.sh";

                    boolean isInstalled = JschService.remoteInstallHadoop(physicalNode.getUser(),physicalNode.getPassword(),physicalNode.getHost(),cmd,key);
                    if(isInstalled){
                        JschProxy.execmd(physicalNode.getUser(),physicalNode.getPassword(),physicalNode.getHost(),"source /etc/profile");
                        physicalNode.setInstalled("已安装");
                        ExpYamlDataBase.updateNode(physicalNode);
                    }
                } catch (JSchException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
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
        String logPath = FileUtil.getJarPath(JschService.class) + "/log/"+key+".log";
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
                         @RequestParam("host") String host) throws Exception {
        boolean exists = JschService.checkLocalUserExsits("hadoop");
        if(!exists){
            return "请先创建本地hadoop用户";
        }

        Session hadoopLocalSession = null;
        Session hadoopRemoteSession = null;
        try {
            List<String> data = Lists.newArrayList();
            data.add(ip + "   " + host);
            String localHostName = InetAddress.getLocalHost().getHostName();

            //3，清除/etc/hosts旧的匹配
            JschService.delFileMatch("/etc/hosts", " " + host);
            //4,更新本地/etc/hosts 加入扩展节点的hosts 和 ip映射
            JschService.localWriteFileAppend(data, "/etc/hosts");

            //5,创建远程hadoop用户,如果存在，则不给于添加
            boolean isRemoteUserExsits = JschService.checkRemoteUserExsits(rAdmin, password, host, "hadoop");
            if (!isRemoteUserExsits) {
                //安全起见，先创建/home目录，已存在则忽略
                JschService.mkdirRemoteDir(rAdmin,password,host,"/home");
                logger.info("开始创建远程hadoop用户");
                boolean result = JschService.remoteAddUser(rAdmin,password, host, "hadoop", "Tudou=123");
                if (!result) {
                    return "远程hadoop用户验证或创建失败，请查看系统日志，并请确认root密码，权限是否正确";
                }
                //修改远程用户hadoop密码
                JschService.remoteUserPasswd(rAdmin, password, host, "hadoop", "Tudou=123");
            }

            //6,设置远程hosts加入本机信任列表
            JschService.setRemoteKnownHosts("hadoop", "Tudou=123", host);

            //7,清空远程ssh目录
            JschService.delRemoteSSHDir(rAdmin,password,host, "hadoop");
            //8,连接到远程机器，生成秘钥串
            JschService.generateRemoteSshKeygen("hadoop","Tudou=123",host);
            //9,将远程生成的公钥传入到本地
            String lfile = JschProxy.scpRemoteIdRsaPub( "hadoop", "Tudou=123",host);
            //10,清除旧的匹配
            JschService.delFileMatch("/home/hadoop/.ssh/authorized_keys", "hadoop@" + host);
            JschService.localWriteFileAppend(lfile,
                    "/home/hadoop/.ssh/authorized_keys");
            //11,将本机hosts覆盖至远程hosts                              //需做分发
            JschService.updateRemoteHosts(rAdmin,password,host);
            //12,将本地的机器信任列表传输至远程机器上
            JschService.updateRemoteKnownHosts("hadoop","Tudou=123", host);   //需做分发
            //13,将本地的authorized_keys覆盖远程机器                     //需做分发
            JschService.updateRemoteHostAuthorizedKeys("hadoop","Tudou=123", host);
            LinkedHashMap<String, ExpYamlDataBase.PhysicalNode> list = ExpYamlDataBase.getNodes();

            //14,更新其他已注册的物理节点
            for (ExpYamlDataBase.PhysicalNode physicalNode : list.values()) {
                JschService.updateRemoteHosts(physicalNode.getAdmin(), physicalNode.getAdminPassWord(), physicalNode.getHost());
                JschService.updateRemoteKnownHosts(physicalNode.getUser(),physicalNode.getPassword(),physicalNode.getHost());
                JschService.updateRemoteHostAuthorizedKeys(physicalNode.getUser(),physicalNode.getPassword(),physicalNode.getHost());

            }

            //添加至已注册物理节点信息
            ExpYamlDataBase.addNode(host, ip, "hadoop", "Tudou=123",rAdmin,password);
            //8,将本地hadoop,jdk,安装脚本传输至远程服务器
            String expandDirectory = FileUtil.getJarPath(ExpansionController.class) + "/expand";
            JschService.scpLocalDirToRemote("hadoop","Tudou=123",localHostName, "hadoop", host, "/home/hadoop", expandDirectory);
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
    public String confSyn(@RequestParam("key") String key) throws Exception {
        String localHostName = InetAddress.getLocalHost().getHostName();
        ExpYamlDataBase.PhysicalNode physicalNode = ExpYamlDataBase.getNodes().get(key);
        String confDir = FileUtil.getJarPath(ExpansionController.class) + "/conf/hadoop";
        JschService.scpLocalDirToRemote("hadoop","Tudou=123",localHostName,
                physicalNode.getUser(),physicalNode.getHost(),"/home/hadoop/hadoop-2.8.0/etc/",confDir);
        return "true";

    }


}
