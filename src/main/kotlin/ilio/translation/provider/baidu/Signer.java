package ilio.translation.provider.baidu;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Signer {
    
    public static void main(String[] args) {
        System.out.println(Hashing.md5().hashString("hyia", StandardCharsets.UTF_8).toString());
    }
}
