package common.core.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import common.config.JWTConfig;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class JWTutils {

    private final JWTConfig jwtConfig;

    private final Algorithm algorithm;

    private final JWTVerifier verifier;

    public String createToken(Map<String,?> data, long expiresTime, ChronoUnit unit){
        Instant instant = Instant.now();
        String sign = JWT.create()
                .withPayload(data)
                .withExpiresAt(instant.plus(expiresTime, unit))
                .withIssuedAt(instant)
                .sign(algorithm);
        return sign;
    }

    public String createToken(Map<String,?> data, long expiresTime){
        return this.createToken(data,expiresTime,jwtConfig.getTimeUnit());
    }

    public String createToken(Map<String,?> data){
        return this.createToken(data,jwtConfig.getTime());
    }

    public DecodedJWT verityJWT(String JWT){
        try{
            DecodedJWT verify = verifier.verify(JWT);
            return verify;
        } catch (JWTVerificationException e){
            return null;
        }
    }

    /***
     * 性能差,图方便
     * @param jwt
     * @param objectEntity
     * @return
     * @param <T>
     */
    public <T> T toObject(String jwt,T objectEntity){
        Map<String, Claim> claims = verityJWT(jwt).getClaims();
        Field[] declaredFields = objectEntity.getClass().getDeclaredFields();
        for (Field each : declaredFields){
            each.setAccessible(true);
            try {
                each.set(objectEntity,claims.get(each.getName()).as(each.getType()));
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return  objectEntity;
    }

    /***
     * 性能差,图方便
     * @param data
     * @return
     * @param <T>
     */
    public <T> String createToken(T data,long expiresTime, ChronoUnit unit) {
        Class<?> aClass = data.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        Map<String, Object> map = new HashMap<>();
        for(Field each: declaredFields){
            try {
                each.setAccessible(true);
                map.put(each.getName(),each.get(data));
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return this.createToken(map,expiresTime,unit);
    }

    public <T> String createToken(T data,long expiresTime){
        return this.createToken(data,expiresTime, jwtConfig.getTimeUnit());
    }

    public <T> String createToken(T data){
        return this.createToken(data,jwtConfig.getTime());
    }
}
