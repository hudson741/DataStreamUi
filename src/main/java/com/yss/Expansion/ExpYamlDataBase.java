package com.yss.Expansion;

import com.yss.Expansion.Exception.YamlDataBaseException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;

import static com.yss.util.FileUtil.getJarPath;

/**
 * @Description   本地文件数据库，使用yaml实现，存储已注册的物理节点信息，用于一键扩容
 * @author: zhangchi
 * @Date: 2017/8/7
 */
public class ExpYamlDataBase {

    private  static org.slf4j.Logger logger = LoggerFactory.getLogger(ExpYamlDataBase.class);


    public static void main(String[] args) throws IOException {
        LinkedHashMap<String,PhysicalNode>  map =  ExpYamlDataBase.getNodes();

        LinkedHashMap<String,PhysicalNode>  map2 =  ExpYamlDataBase.getNodes();

        String a = (String)map2.keySet().toArray()[0];

        ExpYamlDataBase.removeNode(a);

        LinkedHashMap<String,PhysicalNode>  map3 =  ExpYamlDataBase.getNodes();


    }

    /**
     * 加载本地已注册物理服务节点
     */
    static LinkedHashMap<String,PhysicalNode> nodes = new LinkedHashMap<>();

    /**
     * 本地存储文件
     */
    private static  String fileStore = null;

    /**
     * 本地存储目录
     */
    private static  String fileStorDir = null;

    static Yaml yaml = new Yaml();

    static{

        try {
            fileStorDir = getJarPath(ExpYamlDataBase.class) + "/store";
            fileStore = fileStorDir+"/NodeStore.yaml";
        } catch (IOException e) {
            e.printStackTrace();
        }

        File dir = new File(fileStorDir);
        if(!dir.exists()){
            dir.mkdir();
        }

        File file = new File(fileStore);
        if(file.exists()) {
            try {
                LinkedHashMap<String,PhysicalNode> nodes1 = yaml.loadAs(new FileInputStream(fileStore), LinkedHashMap.class);
                if(MapUtils.isNotEmpty(nodes1)){
                    nodes.putAll(nodes1);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 新增一条已注册的物理节点信息
     * @param host
     * @param ip
     * @param user
     * @param password
     * @throws IOException
     */
    public static void addNode(String host,String ip,String user,String password,String admin,String adminPassWord,String id) throws YamlDataBaseException {
        try {
            PhysicalNode physicalNode = new PhysicalNode();
            physicalNode.setHost(host);
            physicalNode.setIp(ip);
            physicalNode.setUser(user);
            physicalNode.setPassword(password);
            physicalNode.setAdmin(admin);
            physicalNode.setAdminPassWord(adminPassWord);
            physicalNode.setId(id);
            nodes.put(id, physicalNode);
            yaml.dump(nodes, new FileWriter(fileStore));
        }catch(Exception e){
            logger.error("error ",e);
            throw new  YamlDataBaseException(e.getMessage());
        }
    }

    /**
     * 更新节点
     * @param physicalNode
     * @throws IOException
     */
    public static void updateNode(PhysicalNode physicalNode) throws IOException {
        nodes.put(physicalNode.getId(),physicalNode);
        yaml.dump(nodes,new FileWriter(fileStore));
    }

    /**
     * 获取所有已注册的物理节点信息
     * @return
     * @throws FileNotFoundException
     */
    public static LinkedHashMap<String,PhysicalNode>  getNodes()  {
        return nodes;
    }

    /**
     * 删除一个节点
     * @param key
     * @throws IOException
     */
    public static void removeNode(String key) throws IOException {
        nodes.remove(key);
        yaml.dump(nodes,new FileWriter(fileStore));

    }


    public static class PhysicalNode{

        private String id;

        private String host;

        private String ip;

        private String user;

        private String password;

        private String runningProcess;

        private String admin = "";

        private String adminPassWord = "";

        public String getAdmin() {
            return admin;
        }

        public void setAdmin(String admin) {
            this.admin = admin;
        }

        public String getAdminPassWord() {
            return adminPassWord;
        }

        public void setAdminPassWord(String adminPassWord) {
            this.adminPassWord = adminPassWord;
        }

        public String getRunningProcess() {
            return runningProcess;
        }

        public void setRunningProcess(String runningProcess) {
            this.runningProcess = runningProcess;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

}
