import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptTest {
    @Test
    public void genPassowrd() {
        //密码加密
        String passsword = BCrypt.hashpw("123", BCrypt.gensalt());
        System.out.println(passsword);
        //密码校验
        boolean result = BCrypt.checkpw("123", passsword);
        System.out.println(result);
    }
}
