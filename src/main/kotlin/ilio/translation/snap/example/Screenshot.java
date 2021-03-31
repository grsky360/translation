package ilio.translation.snap.example;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class Screenshot extends JFrame {
    private BufferedImage buff_img;
    private BufferedImage showImg;
    private BufferedImage sub_buff_img;
    private Rectangle rec;
    private Robot robot;
    private JLabel label;
    private Point start;
    private Point end;
    private int x, y, width, height;
    private boolean over = false;
    //private JPanel back_panel;
    private Color backGroundCol;
//    private JLabel show_sub;
    ImageIcon icon;
//    private SubImgAction subAction;

    public Screenshot() {
        this.setLayout(null);
        backGroundCol = new Color(200, 150, 150);
        icon = new ImageIcon();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setUndecorated(true);
        setExtendedState(MAXIMIZED_BOTH);

        setResizable(false);//设置不可改变大小
        setAlwaysOnTop(true);//设置总是在顶层(最上层)
        start = new Point();
        end = new Point();
        label = new JLabel();
//        show_sub = new JLabel();
//        show_sub.setIcon(icon);

//        add(show_sub);
        try {
            robot = new Robot();
            buff_img = robot.createScreenCapture(new Rectangle(0, 0,
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height));//获得整个屏幕

//            subAction = new SubImgAction(show_sub, buff_img, icon);
//            show_sub.addMouseListener(subAction);
//            show_sub.addMouseMotionListener(subAction);
//            show_sub.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));

            label = new JLabel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(buff_img, 0, 0, this);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(backGroundCol);
                    AlphaComposite composite = AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 60 / 100.0F);
                    g2d.setComposite(composite);
                    g2d.fill(new RoundRectangle2D.Float(0, 0, this.getWidth(), this
                        .getHeight(), 0, 0));
                }
            };
            label.setBounds(0, 0, getWidth(), this.getHeight());
            add(label);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        setVisible(true);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Screenshot();
    }

}
