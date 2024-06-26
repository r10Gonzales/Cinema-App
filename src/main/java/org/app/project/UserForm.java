package org.app.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class UserForm extends JFrame {

    private JTable userTable;
    private JTextField usernameField, passwordField, fullNameField, emailField, phoneNumberField;

    public UserForm() {
        setTitle("Manage Users");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout and Components
        JPanel panel = new JPanel(new BorderLayout());

        // Table
        userTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Form components
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JTextField();
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameField = new JTextField();
        formPanel.add(fullNameLabel);
        formPanel.add(fullNameField);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        formPanel.add(emailLabel);
        formPanel.add(emailField);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberField = new JTextField();
        formPanel.add(phoneNumberLabel);
        formPanel.add(phoneNumberField);

        JButton addButton = new JButton("Add User");
        formPanel.add(addButton);

        JButton updateButton = new JButton("Update User");
        formPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete User");
        formPanel.add(deleteButton);

        panel.add(formPanel, BorderLayout.WEST);

        // Button listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        add(panel);

        loadUsers();

        setVisible(true);
    }

    private void loadUsers() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }

            userTable.setModel(new DefaultTableModel(data, columnNames));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();

        String sql = "INSERT INTO users (username, password, full_name, email, phone_number) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, email);
            pstmt.setString(5, phoneNumber);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User added successfully!");

            // Clear input fields and reload users
            clearFields();
            loadUsers();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to update.");
            return;
        }

        int userId = (int) userTable.getValueAt(selectedRow, 0);
        String username = usernameField.getText();
        String password = passwordField.getText();
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();

        String sql = "UPDATE users SET username=?, password=?, full_name=?, email=?, phone_number=? " +
                "WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, email);
            pstmt.setString(5, phoneNumber);
            pstmt.setInt(6, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "User updated successfully!");

                // Clear input fields and reload users
                clearFields();
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to delete.");
            return;
        }

        int userId = (int) userTable.getValueAt(selectedRow, 0);

        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!");

                // Clear input fields and reload users
                clearFields();
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        fullNameField.setText("");
        emailField.setText("");
        phoneNumberField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserForm();
        });
    }
}
