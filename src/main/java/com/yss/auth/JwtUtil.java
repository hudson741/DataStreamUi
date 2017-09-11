package com.yss.auth;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.map.HashedMap;

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
                JWTSigner signer = new JWTSigner(Base64.decodeBase64("ldjYWNdcek"));
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
            JWTVerifier jwtVerifier = new JWTVerifier(Base64.decodeBase64("ldjYWNdcek"));
            Map<String, Object> decodedPayload = jwtVerifier.verify(token);
            return decodedPayload;
        } catch (Exception var3) {
            return null;
        }
    }

    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Map<String, Object> claims = new HashedMap();
        claims.put("userId", "dsdsdsssd");
        claims.put("timeStamp", System.currentTimeMillis());
        String sign = sign(claims);
        System.out.println(sign);

        Map<String,Object> map = verify(sign);
        System.out.println(map.get("timeStamp"));
    }



}
