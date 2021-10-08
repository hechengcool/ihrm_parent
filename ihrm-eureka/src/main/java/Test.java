import org.apache.shiro.crypto.hash.Md5Hash;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi;
import sun.security.provider.MD5;

/**
 * Created with IntelliJ IDEA.
 * User: cheng
 * Date: 2021/9/16
 * Time: 17:31
 * Description: No Description
 */
public class Test {
    public static void main(String[] args) {
        String password = new Md5Hash("123456", "13800000002", 3).toString();
        System.out.println(password);
    }
}
