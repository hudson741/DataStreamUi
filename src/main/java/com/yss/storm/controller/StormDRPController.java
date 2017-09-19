package com.yss.storm.controller;

import com.yss.storm.StormNodesService;
import com.yss.storm.node.DrpcNode;
import com.yss.util.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.DRPCExecutionException;
import org.apache.storm.thrift.TException;
import org.apache.storm.thrift.transport.TTransportException;
import org.apache.storm.utils.DRPCClient;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/9/15
 */

@RestController
public class StormDRPController {

    @Autowired
    private StormNodesService stormNodesService;

    private Logger logger = LoggerFactory.getLogger(StormDRPController.class);


    @RequestMapping(
            value  = "/drpcProcess",
            method = RequestMethod.GET
    )
    public String drpcProcess(@RequestParam("func") String func,@RequestParam("arg") String arg) {
        List<DrpcNode> list = stormNodesService.getDrpcNodeList();
        //随机算法，取drpcNode节点

        if(CollectionUtils.isEmpty(list)){
            return Response.getInstance(false,"节点为空").toString();
        }
        int size = list.size();

        int randomNode = new Random().nextInt(size) % (size - 0 + 1) + 0;;
        DrpcNode drpcNode = list.get(randomNode);
        Map config = Utils.readDefaultConfig();
        DRPCClient client = null;
        try {
            client = new DRPCClient(config,drpcNode.getHost(), drpcNode.getPort());
            String result = client.execute(func, arg);
            logger.info("drpc "+result);
            return Response.getInstance(true,result).toString();
        } catch (TTransportException e) {
            logger.error("error ",e);
        } catch (DRPCExecutionException e) {
            logger.error("error ",e);
        } catch (AuthorizationException e) {
            logger.error("error ",e);
        } catch (TException e) {
            logger.error("error ",e);
        }
        return Response.getInstance(false,"drpc调用失败").toString();


    }


}
