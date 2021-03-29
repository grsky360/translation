package ilio.translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PopupTester extends JFrame {
    private static class MessagePopup extends Popup {
        private final JDialog dialog;

        private final WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                dialog.setVisible(false);
                dialog.removeWindowFocusListener(this);
            }
        };
        public MessagePopup(Frame base, String message) {
            super();
            dialog = new JOptionPane().createDialog( base, "Message" );
            dialog.setModal( false );
            dialog.setContentPane( new JLabel( message ) );
            dialog.addWindowFocusListener(windowAdapter);
        }

        @Override
        public void show() {
            dialog.setVisible(true);
        }
        @Override
        public void hide() {
            dialog.setVisible(false);
        }
    }

    public static void main(String[] args) throws Exception {
        final PopupTester popupTester = new PopupTester();
        popupTester.setSize(300, 100);

        final TrayIcon trayIcon = new TrayIcon(MainKt.getTrayIcon());
        SystemTray.getSystemTray().add(trayIcon);
        var popupMenu = new PopupMenu();
        var item = new MenuItem("Show");
        trayIcon.setPopupMenu(popupMenu);
        popupMenu.add(item);
        item.addActionListener(e -> {
//            var popupTester = new PopupTester();
//            popupTester.setSize(300, 100);
            popupTester.setVisible(true);
            popupTester.setAlwaysOnTop(true);
            popupTester.setAlwaysOnTop(false);
        });

    }
}
