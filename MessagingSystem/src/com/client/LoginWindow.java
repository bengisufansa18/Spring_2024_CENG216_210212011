package com.client;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginWindow extends JFrame {
    private final ChatClient client;
    private JTextField loginField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton loginButton = new JButton("Login");

    public LoginWindow() {
        super("Modern Chat Login");

        this.client = new ChatClient("localhost", 8818);
        client.connect();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);
        p.add(new JLabel("Login"), gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        p.add(new JLabel("Username:"), gbc);

        gbc.gridx++;
        gbc.ipadx = 150; 
        loginField.setFont(loginField.getFont().deriveFont(16f)); 
        p.add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        p.add(new JLabel("Password:"), gbc);

        gbc.gridx++;
        passwordField.setFont(passwordField.getFont().deriveFont(16f)); 
        p.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        p.add(loginButton, gbc);

        getContentPane().add(p, BorderLayout.CENTER);

        loginButton.setBackground(new Color(65, 131, 215));
        loginButton.setForeground(Color.WHITE);

        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(54, 109, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(65, 131, 215));
            }
        });

        getContentPane().setBackground(Color.WHITE);

        pack();
        setLocationRelativeTo(null); 
        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });
    }

    private void doLogin() {
        String login = loginField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (client.login(login, password)) {

                UserListPane userListPane = new UserListPane(client);
                JFrame frame = new JFrame("User List");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 600);

                frame.getContentPane().add(userListPane, BorderLayout.CENTER);
                frame.setVisible(true);

                setVisible(false);
            } else {

                JOptionPane.showMessageDialog(this, "Invalid login/password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginWindow();
            }
        });
    }
}
