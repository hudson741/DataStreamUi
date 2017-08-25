package com.yss.Expansion;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/8/22
 */
public class JschSessionObjectFactory extends BaseKeyedPooledObjectFactory<String,Session>{


    @Override
    public Session create(String jschPoolKeyStr) throws Exception {
        JschPoolKey jschPoolKey = JschPoolKey.key2Obj(jschPoolKeyStr);
        JSch jSch = new JSch();
        Session session = jSch.getSession(jschPoolKey.getUser(),jschPoolKey.getHost(),22);
        session.setPassword(jschPoolKey.getPassword());
        session.setUserInfo(new JschService.MyUserInfo());
        session.connect(3000);
        return session;
    }

    @Override
    public PooledObject<Session> wrap(Session session) {
        return new DefaultPooledObject<Session>(session);
    }
}
