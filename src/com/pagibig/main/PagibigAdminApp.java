/*  The application entry point. Launches the LoginFrame 
    on the Swing event thread to start the program. */

package com.pagibig.main;

import javax.swing.SwingUtilities;

public class PagibigAdminApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
