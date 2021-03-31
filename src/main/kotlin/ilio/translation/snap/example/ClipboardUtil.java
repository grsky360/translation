package ilio.translation.snap.example;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;


public class ClipboardUtil {

    private Clipboard  cli;
    /*
     * 写入系统剪切板
     */
    public void writeToClipboard(String str){
        if(str != null){
            cli = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable text = new StringSelection(str);
            cli.setContents(text, null);
        }
    }

    public void writeToClipboard(BufferedImage image){
        cli = Toolkit.getDefaultToolkit().getSystemClipboard();
        cli.setContents(new ImageChange(image),null);
    }

    /**
     * 获取系统剪切板中的文本（相当于粘贴）
     *
     * @return 系统剪切板中的文本
     */
    public  String getSysClipboardText() {
        String ret = "";
        cli = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = cli.getContents(null);
        if (clipTf != null) {
            // 检查内容是否是文本类型
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    ret = (String) clipTf
                        .getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }


    class ImageChange implements Transferable {
        private BufferedImage theImage;
        public ImageChange(BufferedImage image) {
            theImage = image;
        }
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.imageFlavor };
        }
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(DataFlavor.imageFlavor);
        }
        public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException {
            if (flavor.equals(DataFlavor.imageFlavor)) {
                return theImage;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }
}
