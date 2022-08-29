package com.modules.security.util;

import com.common.util.Constant;
import com.modules.security.PreSecurityUser;
import com.web.system.dto.UserCurrent;
import com.web.system.entity.User;
import com.web.system.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Classname JwtUtil
 * @Description JWT工具类
 * @Author 李号东 lihaodongmail@163.com
 * @Date 2019-05-07 09:23
 * @Version 1.0
 */
@Log4j2
@Component
public class JwtUtil {
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 用户名称
     */
    private static final String USERNAME = Claims.SUBJECT;

    private static final String USERID = "userid";
    /**
     * 创建时间
     */
    private static final String CREATED = "created";
    /**
     * 权限列表
     */
    private static final String AUTHORITIES = "authorities";
    /**
     * 密钥
     */
    private static final String SECRET = "abcdefgh";

    private static final String VID="vid";
    /**
     * 有效期3小时
     */
    private static final long EXPIRE_TIME = 10*60 * 60 * 1000;

  /*  @Value("${jwt.header}")
    private String header;
*/


    /**
     * 生成令牌
     *
     * @return 令牌
     */
    public static String generateToken(PreSecurityUser userDetail) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(USERID,userDetail.getUserId());
        claims.put(USERNAME, userDetail.getUsername());
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, userDetail.getAuthorities());
        return generateToken(claims);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    /**
     * 创建jwt
     *生成token，该方法只在用户登录成功后调用
     * @param payload，可以存储用户id，token生成时间，token过期时间等自定义字段
     * @param issuer
     * @param subject
     * @return
     * @throws Exception
     */
    public static String createJWT(Map<String, Object> payload, String issuer, String subject) {
        // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。
        // 一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        SecretKey key = generalKey();

        //添加构成JWT的参数,下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
                .setHeaderParam("typ", "JWT")
                .setClaims(payload)          // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(Constant.JWT_ID)                  // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           // iat: jwt的签发时间
                .setIssuer(issuer)          // issuer：jwt签发人
                .setSubject(subject)        // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setExpiration(new Date(nowMillis + Constant.JWT_TTL))
                .signWith(signatureAlgorithm, key); // 设置签名使用的签名算法和签名使用的秘钥
        return builder.compact();
    }

    //生成签名密钥
    public static SecretKey generalKey() {
        String stringKey = Constant.JWT_SECRET;
        // 本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 根据请求令牌获取登录认证信息
     *
     * @return 用户名
     */
    public PreSecurityUser getUserFromToken(String token) {
        // 获取请求携带的令牌

        if (StringUtils.isNotEmpty(token)) {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return null;
            }
            String usercode = claims.getSubject();
            if (usercode == null) {
                return null;
            }
            if (isTokenExpired(token)) {
                return null;
            }
            // 解析对应的权限以及用户id
            Object authors = claims.get(AUTHORITIES);
            String userId =  String.valueOf(claims.get(USERID));
            /*
            * 这里还要加上 通过userid从redis中 取出 用户的信息（包括是否管理员  ， 租户信息，权限等）。
            * 暂时全部设置成管理员
            * */
            String tenantId=(String)claims.get(VID);
            Set<String> perms = new HashSet<>();
            if (authors instanceof List) {
                for (Object object : (List) authors) {
                    perms.add(((Map) object).get("authority").toString());
                }
            }
            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(perms.toArray(new String[0]));
            if (validateToken(token, usercode)){
                boolean izAdmin=false;
                String realName="";
                UserCurrent user=(UserCurrent)redisTemplate.opsForValue().get(token);

                //如果 redis中没有， 需要从数据库中 取出
                if(user==null){
                    User retUser=  userService.getById(userId);
                    izAdmin=retUser.getGroupId()>0?true:false;
                    realName=retUser.getUserName();
                    //重新将用户信息 存入redis
                    //PreSecurityUser userDetail = new PreSecurityUser(userId,username, retUser.getPwd(),izAdmin,tenantId,authorities,null);
                    //redisTemplate.opsForValue().set(token,userDetail,2, TimeUnit.HOURS);  //两小时
                    userService.setUserToRedis(token,userId);
                }else{
                    realName=user.getUserName();
                    izAdmin=user.getIzAdmin()>0;
                }



                // 未把密码放到jwt
                return new PreSecurityUser(userId,usercode,usercode,realName,"",authorities,null);
            }
        }
        return null;
    }



    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 验证令牌
     *
     * @param token
     * @param username
     * @return
     */
    private static Boolean validateToken(String token, String username) {
        String userName = getUsernameFromToken(token);
        return (userName.equals(username) && !isTokenExpired(token));
    }

    /**
     * 刷新令牌
     *
     * @param token
     * @return
     */
    public static String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    private static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //String token=CookieUtils.getCookieValue(request,tokenKey,false);
        /*if (StringUtils.isNotEmpty(token)) {
            token = token.substring(authTokenStart.length());
        }*/
        return token;
    }

    /**
     * PDA端
     * @param token
     * @return
     */
    public static  User getPDAToken(IUserService userService ,String token) {
        Claims claims = JwtUtil.parseJWT(token);
        String userid = claims.get("uid").toString();
        User user=  userService.getById(userid);
        if(user!=null)
        {
            user.setPwd(null);//敏感字段置空
        }
        return user;
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) {
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)                 //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();     //设置需要解析的jwt
        return claims;
    }
}
