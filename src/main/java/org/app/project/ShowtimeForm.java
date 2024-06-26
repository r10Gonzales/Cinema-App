package org.app.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ShowtimeForm extends JFrame {

    private JTable showtimeTable;

    public ShowtimeForm() {
        setTitle("Manage Showtimes");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout and Components
        JPanel panel = new JPanel(new BorderLayout());

        // Table
        showtimeTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(showtimeTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        loadShowtimes();

        setVisible(true);
    }

    private void loadShowtimes() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT s.id, m.title, s.showtime FROM showtimes s JOIN movies m ON s.movie_id = m.id")) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            Vector<String> columnNames = new Vector<>();
            columnNames.add("ID");
            columnNames.add("Movie Title");
            columnNames.add("Showtime");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("title"));
                row.add(rs.getTimestamp("showtime"));
                data.add(row);
            }

            showtimeTable.setModel(new DefaultTableModel(data, columnNames));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
