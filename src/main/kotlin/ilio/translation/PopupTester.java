package ilio.translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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

    public static void main(String[] args) {
        final PopupTester popupTester = new PopupTester();
        popupTester.setLayout(new FlowLayout());
        popupTester.setSize(300, 100);
        popupTester.add(new JButton("Click Me") {
            @Override
            protected void fireActionPerformed(ActionEvent event) {
                MessagePopup popup = new MessagePopup( popupTester, "Howdy" );
                popup.show();
            }
        });
        popupTester.setVisible(true);
        popupTester.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
