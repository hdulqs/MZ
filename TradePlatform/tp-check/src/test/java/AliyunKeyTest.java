import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Frank on 2018/8/15.
 */
public class AliyunKeyTest {

    private static final String secret = "d8775ddc975b474e675b1d3f72b4ad9a";

    public static void main(String[] args) throws Exception {
        String stringToSign = "GET" + "\n" +
                "application/html" + "\n" +
                "" + "\n" +
                "" + "\n" +
                "" + "\n" +
                "/";
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        byte[] keyBytes = secret.getBytes("UTF-8");
        hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
        String sign = new String(Base64.encodeBase64(hmacSha256.doFinal(
                stringToSign.getBytes("UTF-8"))), "UTF-8");
        System.out.println(sign);
    }
}
