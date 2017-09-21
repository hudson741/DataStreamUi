package com.yss.auth;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/9/11
 */
public class JwtUtil {
    static final String clientSecretCode = "ldjYWNdcek";

    public JwtUtil() {
    }

    public static String sign(Map<String, Object> map) {
        if(null != map && !map.isEmpty()) {
            try {
                new Base64(true);
                JWTSigner signer = new JWTSigner(Base64.decodeBase64(clientSecretCode));
                String token = signer.sign(map);
                return token;
            } catch (Exception var3) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Map<String, Object> verify(String token) {
        try {
            new Base64(true);
            JWTVerifier jwtVerifier = new JWTVerifier(Base64.decodeBase64(clientSecretCode));
            Map<String, Object> decodedPayload = jwtVerifier.verify(token);
            return decodedPayload;
        } catch (Exception var3) {
            return null;
        }
    }

    public static String signPassWord(String password){
        Map<String,Object> map = new HashMap<>();
        map.put("passwd",password);
        String token = JwtUtil.sign(map);
        return token;

    }

    public static String getPassWord(String token){
        Map<String,Object> map = verify(token);
        return (String)map.get("passwd");
    }


    public static void main(String[] args) {
        System.out.println(signPassWord("Tudou=123"));
        String token = signPassWord("Tudou=123");
        System.out.println(getPassWord(token));
    }



}
