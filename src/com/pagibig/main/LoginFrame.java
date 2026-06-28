package com.pagibig.main;

import com.pagibig.data.DataStore;
import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private final JTextField usernameField;
    private final JPasswordField passwordField;

    // Track failed login attempts
    private int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;

    public LoginFrame() {
        setTitle("Pag-IBIG Fund Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 340);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel content = new JPanel();
        content.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        content.setLayout(new BorderLayout(0, 24));
        setContentPane(content);

        JLabel titleLabel = new JLabel("Pag-IBIG Fund Admin Portal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // === Username Segment ===
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0); // Spacing above the box
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        formPanel.add(userLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 16, 0); // Spacing between fields
        usernameField = new JTextField(15); // '15' sets a clean initial column width
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(usernameField, gbc);

        // === Password Segment ===
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 4, 0);
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        formPanel.add(passLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        passwordField = new JPasswordField(15); 
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(passwordField, gbc);

        content.add(formPanel, BorderLayout.CENTER);

        // === Buttons ===
        JButton loginButton = new JButton("Log In");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(100, 36)); 
        loginButton.addActionListener(e -> attemptLogin(loginButton));

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        exitButton.setPreferredSize(new Dimension(100, 36));
        exitButton.addActionListener(e -> System.exit(0));

        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        buttonBar.add(loginButton);
        buttonBar.add(exitButton);
        content.add(buttonBar, BorderLayout.SOUTH);
    }

    private void attemptLogin(JButton loginButton) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if ("admin".equals(username) && "grp3db.OOP".equals(password)) {
            DashboardFrame dashboard = new DashboardFrame(new DataStore());
            dashboard.setVisible(true);
            dispose();
        } else {
            loginAttempts++;
            passwordField.setText("");

            if (loginAttempts >= MAX_ATTEMPTS) {
                // Lock fields
                usernameField.setEnabled(false);
                passwordField.setEnabled(false);
                loginButton.setEnabled(false);

                JOptionPane.showMessageDialog(
                        this,
                        "Too many failed login attempts.\nThis login terminal has been locked.",
                        "Login Locked",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {
                int remaining = MAX_ATTEMPTS - loginAttempts;
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password.\nAttempts remaining: " + remaining,
                        "Login Failed",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }
}
