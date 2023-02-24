import com.supergo.user.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

public class JwtTest {

    @Test
    //生成token
    public void testJwt() {
        String token = Jwts.builder()//jwt构建起
                .setId("123456") //指定载荷内容  id:123456
                .setSubject("hello world") // 指定载荷内容 su:hello world
                .setIssuedAt(new Date()) // 指定载荷内容 iat:2020:8:21
                .signWith(SignatureAlgorithm.HS256, "thisisapassword") //指定私钥
                .compact();//生成
        System.out.println(token);
    }


    @Test
    //解析(校验)token
    public void parseToken() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJzdWIiOiJoZWxsbyB3b3JsZCIsImlhdCI6MTU5ODAxODQ2NX0.drfoD9biSOJqtbP1llHAWOkHcz_OaDqa-y6PEs1gDa8";
        Jws<Claims> claimsJws = Jwts.parser() //获得解析器
                .setSigningKey("thisisapassword") // 填入校验私钥 , 校验失败抛出异常
                .parseClaimsJws(token); // 解析
        Claims body = claimsJws.getBody(); //获得载荷部分
        System.out.println(body.getId()); //取出载荷中的键值对
        System.out.println(body.getSubject()); //取出载荷中的键值对
        System.out.println(body.getIssuedAt()); //取出载荷中的键值对
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testJwtExpire1() {
        //过期时间，10s以后时效
        long exp = System.currentTimeMillis() + 25 * 1000;


        String token = Jwts.builder()
                .setId("123456")
                .setSubject("hello world")
                .setIssuedAt(new Date())
                //设置过期时间
                .setExpiration(new Date(exp))//在例子1基础上 加入时效
                .signWith(SignatureAlgorithm.HS256, "thisisapassword")
                .compact();
        System.out.println(token);

    }

    @Test
    public void testJwtExpire2() {
       String token= "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJzdWIiOiJoZWxsbyB3b3JsZCIsImlhdCI6MTU5ODAxODk2MSwiZXhwIjoxNTk4MDE4OTg2fQ.ZsBtV2gIjtpkinBuSBwvA9l43RezpxRDH_rJhgEnBA8";
        //读取token
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey("thisisapassword")
                .parseClaimsJws(token);
        Claims body = jws.getBody();
        System.out.println(body.getId());
        System.out.println(body.getSubject());
        System.out.println(body.getIssuedAt());
        System.out.println(body.getExpiration());

    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void jwtClaims1() {
        long exp = System.currentTimeMillis() + 1000 * 60;
        String token = Jwts.builder()
                .setId("123456")
                .setSubject("hello world")
                .setIssuedAt(new Date())
                //设置过期时间
                .setExpiration(new Date(exp))
                .claim("key1", 123456)  //添加自定义载荷键值对
                .claim("key2", "abcde")  //添加自定义载荷键值对
                .claim("key3", "222222")  //添加自定义载荷键值对
                .signWith(SignatureAlgorithm.HS256, "thisisapassword")
                .compact();
        System.out.println(token);

    }
    @Test
    public void jwtClaims2() {
   String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJzdWIiOiJoZWxsbyB3b3JsZCIsImlhdCI6MTU5ODAxOTAzNCwiZXhwIjoxNTk4MDE5MDk0LCJrZXkxIjoxMjM0NTYsImtleTIiOiJhYmNkZSIsImtleTMiOiIyMjIyMjIifQ.JDDIRd1CEPzLd2E6cxnLOKrNGS0D84bOFitEl6HRprA";
        Jws<Claims> jws = Jwts.parser().setSigningKey("thisisapassword")
                .parseClaimsJws(token);
        Claims body = jws.getBody();
        System.out.println(body.getId());
        System.out.println(body.getSubject());
        System.out.println(body.getIssuedAt());
        System.out.println(body.getExpiration());
        System.out.println(body.get("key1"));
        System.out.println(body.get("key2"));
        System.out.println(body.get("key3"));

    }

}
