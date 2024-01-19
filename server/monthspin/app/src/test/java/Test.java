import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

public class Test {
    public static void main(String[] args) {

        // 使用 HS512 算法创建满足规范的密钥
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();

        // 将密钥字节数组进行 Base64 编码
        String base64EncodedKey = Base64.getEncoder().encodeToString(keyBytes);

        // 打印或使用创建的密钥字符串
        System.out.println("HS512 Key: " + base64EncodedKey);
    }
}
