package ilio.translation.provider.ocr.baidu;

import ilio.translation.utils.Clipboard;

public class BaiduOcrProvider {

    public static void main(String[] args) {
        System.out.println(Clipboard.INSTANCE.readTextClipboard());
    }
}
