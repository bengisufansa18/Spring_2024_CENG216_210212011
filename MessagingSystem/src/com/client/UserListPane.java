package com.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class UserListPane extends JPanel implements UserStatusListener {

    private final ChatClient client;
    private JList<User> userListUI;
    private DefaultListModel<User> userListModel;

    public UserListPane(ChatClient client) {
        this.client = client;
        this.client.addUserStatusListener(this);

        userListModel = new DefaultListModel<>();
        userListUI = new JList<>(userListModel);
        userListUI.setCellRenderer(new UserListCellRenderer());
        userListUI.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setLayout(new BorderLayout());
        add(new JScrollPane(userListUI), BorderLayout.CENTER);

        userListUI.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    User selectedUser = userListUI.getSelectedValue();
                    if (selectedUser != null) {
                        openMessagePane(selectedUser.getLogin());
                    }
                }
            }
        });
    }

    private void openMessagePane(String userLogin) {
        MessagePane messagePane = new MessagePane(client, userLogin);
        JFrame frame = new JFrame("Message: " + userLogin);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500);
        frame.getContentPane().add(messagePane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    @Override
    public void online(String login) {
        User existingUser = findUser(login);
        if (existingUser != null) {
            existingUser.setOnline(true);
            userListUI.repaint(); 
        } else {
            User newUser = new User(login, true);
            userListModel.addElement(newUser);
        }
    }

    @Override
    public void offline(String login) {
        User existingUser = findUser(login);
        if (existingUser != null) {
            existingUser.setOnline(false);
            userListUI.repaint(); 
        }
    }

    private User findUser(String login) {
        for (int i = 0; i < userListModel.getSize(); i++) {
            User user = userListModel.getElementAt(i);
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    private class User {
        private String login;
        private boolean online;

        public User(String login, boolean online) {
            this.login = login;
            this.online = online;
        }

        public String getLogin() {
            return login;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        @Override
        public String toString() {
            return login + (online ? " (Online)" : " (Offline)");
        }
    }

    private class UserListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            User user = (User) value;
            setText(user.getLogin());
            setForeground(Color.BLACK); 
            setFont(getFont().deriveFont(Font.BOLD)); 
            setPreferredSize(new Dimension(150, 30)); 
            setHorizontalAlignment(SwingConstants.CENTER); 
            setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return this;
        }
    }
}
