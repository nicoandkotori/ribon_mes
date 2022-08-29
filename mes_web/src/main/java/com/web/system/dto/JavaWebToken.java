package com.web.system.dto;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * Created by taomingzhu on 2018/7/9.
 */
public class JavaWebToken {
    private static Logger log = LoggerFactory.getLogger(JavaWebToken.class);

    private static final long TOKEN_EXP_TIME = 1000*60*60*12;//设置token 2s过期

    private static final String KEY = "vfunsoft";

    private static Key getKey() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

    public static String createJWT(Map<String,Object> claims) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        JwtBuilder builder = Jwts.builder().setClaims(claims)
                .signWith(signatureAlgorithm, getKey())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXP_TIME));
        return builder.compact();
    }

    //解析Token，同时也能验证Token，当验证失败返回null
    public static Claims parseJWT(String jwt) {
        try {
            Claims claims = Jwts.parser().setSigningKey(getKey()).parseClaimsJws(jwt).getBody();
            String jsonString = JSONObject.toJSONString(claims);
            return claims;
        } catch (Exception e) {
            log.error("json web token verify failed");
            return null;
        }
    }
}
