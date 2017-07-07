package com.yss.yarn.discovery;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.net.InetSocketAddress;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/7/7
 */
public class ThriftClientObjectFactory extends BasePooledObjectFactory<TServiceClient> {

    private  TServiceClientFactory clientFactory;

    private ServerAddressDiscovery serverAddressDiscovery;

    public ThriftClientObjectFactory(TServiceClientFactory clientFactory,ServerAddressDiscovery serverAddressDiscovery){
        this.clientFactory = clientFactory;
        this.serverAddressDiscovery = serverAddressDiscovery;
    }

    @Override
    public TServiceClient create() throws Exception {

        InetSocketAddress address = serverAddressDiscovery.getServiceAdress();
        if(address == null){
            return null;
        }
        TTransport transport = new TSocket(address.getHostName(),address.getPort());
        transport.open();
        // 协议层
        TProtocol protocol = new TBinaryProtocol(transport);
        return clientFactory.getClient(protocol);


    }

    @Override
    public PooledObject<TServiceClient> wrap(TServiceClient tServiceClient) {
        return new DefaultPooledObject<TServiceClient>(tServiceClient);
    }


    public TServiceClientFactory getClientFactory() {
        return clientFactory;
    }

    public void setClientFactory(TServiceClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }


}
