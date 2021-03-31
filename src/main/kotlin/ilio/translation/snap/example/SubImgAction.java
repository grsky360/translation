package ilio.translation.snap.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class SubImgAction implements MouseListener,MouseMotionListener{

    private JLabel label;
    private int x;
    private int y;
    private BufferedImage image;
    private ImageIcon icon;

    public SubImgAction(JLabel label,BufferedImage image,ImageIcon icon) {
        this.label = label;
        this.image = image;
        this.icon = icon;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point dragPoint = new Point(e.getPoint());
        SwingUtilities.convertPointToScreen(dragPoint, label);
        icon.setImage(image.getSubimage(dragPoint.x - x <= 0 ? 0:dragPoint.x - x,
            dragPoint.y - y <= 0?0:dragPoint.y - y,
            label.getWidth(),label.getHeight()));
        label.setLocation(dragPoint.x - x, dragPoint.y - y);
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2){
            new ClipboardUtil().writeToClipboard(image.getSubimage(label.getX(),
                label.getY(),label.getWidth(),label.getHeight()));
            System.exit(0);
        }

    }
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }
    @Override
    public void mousePressed(MouseEvent e) {
        Point clickPoint = new Point(e.getPoint());
        SwingUtilities.convertPointToScreen(clickPoint, label);
        x = clickPoint.x - label.getX();
        y = clickPoint.y - label.getY();

    }
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
