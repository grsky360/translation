package ilio.translation.provider.baidu;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Signer {

    private static String md5(String str) {
        return md5(str, StandardCharsets.UTF_8);
    }

    @SuppressWarnings("deprecation")
    private static String md5(String str, Charset charset) {
        return Hashing.md5().hashString(str, charset).toString();
    }

}
