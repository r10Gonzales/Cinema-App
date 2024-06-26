package org.app.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class MovieForm extends JFrame {

    private JTable movieTable;
    private JTextField titleField, genreField, directorField, actorsField, durationField, ratingField, releaseDateField;

    public MovieForm() {
        setTitle("Manage Movies");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout and Components
        JPanel panel = new JPanel(new BorderLayout());

        // Form panel (Left)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField(30);
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(titleLabel);
        titlePanel.add(titleField);
        formPanel.add(titlePanel);

        JLabel genreLabel = new JLabel("Genre:");
        genreField = new JTextField(30);
        JPanel genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genrePanel.add(genreLabel);
        genrePanel.add(genreField);
        formPanel.add(genrePanel);

        JLabel directorLabel = new JLabel("Director:");
        directorField = new JTextField(30);
        JPanel directorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        directorPanel.add(directorLabel);
        directorPanel.add(directorField);
        formPanel.add(directorPanel);

        JLabel actorsLabel = new JLabel("Actors:");
        actorsField = new JTextField(30);
        JPanel actorsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actorsPanel.add(actorsLabel);
        actorsPanel.add(actorsField);
        formPanel.add(actorsPanel);

        JLabel durationLabel = new JLabel("Duration (min):");
        durationField = new JTextField(30);
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        durationPanel.add(durationLabel);
        durationPanel.add(durationField);
        formPanel.add(durationPanel);

        JLabel ratingLabel = new JLabel("Rating:");
        ratingField = new JTextField(30);
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratingPanel.add(ratingLabel);
        ratingPanel.add(ratingField);
        formPanel.add(ratingPanel);

        JLabel releaseDateLabel = new JLabel("Release Date (YYYY-MM-DD):");
        releaseDateField = new JTextField(30);
        JPanel releaseDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        releaseDatePanel.add(releaseDateLabel);
        releaseDatePanel.add(releaseDateField);
        formPanel.add(releaseDatePanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Movie");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMovie();
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Movie");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMovie();
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Movie");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMovie();
            }
        });
        buttonPanel.add(deleteButton);

        formPanel.add(buttonPanel);
        panel.add(formPanel, BorderLayout.WEST);

        // Table panel (Right)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        movieTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(movieTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);

        add(panel);

        loadMovies();

        setVisible(true);
    }

    private void loadMovies() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM movies")) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int j = 1; j <= columnCount; j++) {
                    row.add(rs.getObject(j));
                }
                data.add(row);
            }

            movieTable.setModel(new DefaultTableModel(data, columnNames));

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load movies.");
        }
    }

    private void addMovie() {
        String title = titleField.getText();
        String genre = genreField.getText();
        String director = directorField.getText();
        String actors = actorsField.getText();
        int duration;
        float rating;

        // Validasi input duration
        String durationText = durationField.getText();
        if (durationText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Duration cannot be empty.");
            return;
        }
        try {
            duration = Integer.parseInt(durationText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for duration.");
            return;
        }

        // Validasi input rating
        String ratingText = ratingField.getText();
        if (ratingText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rating cannot be empty.");
            return;
        }
        try {
            rating = Float.parseFloat(ratingText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for rating.");
            return;
        }

        String releaseDate = releaseDateField.getText();

        String sql = "INSERT INTO movies (title, genre, director, actors, duration, rating, release_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, genre);
            pstmt.setString(3, director);
            pstmt.setString(4, actors);
            pstmt.setInt(5, duration);
            pstmt.setFloat(6, rating);
            pstmt.setDate(7, java.sql.Date.valueOf(releaseDate));

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Movie added successfully!");

            // Clear input fields and reload movies
            clearFields();
            loadMovies();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add movie.");
        }
    }


    private void updateMovie() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a movie to update.");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) movieTable.getModel();

        // Example: Retrieving data from the first column of the selected row
//        Object rowData = model.getValueAt();

        int movieId = (int) model.getValueAt(selectedRow, 0);
        String title = (String) model.getValueAt(selectedRow, 1);
        String genre = (String) model.getValueAt(selectedRow, 2);
        String director = (String) model.getValueAt(selectedRow, 3);
        String actors = (String) model.getValueAt(selectedRow, 4);
        Integer durationText = (Integer) model.getValueAt(selectedRow, 5);
        Double rating = (Double) model.getValueAt(selectedRow, 6);
        Date releaseDate = (Date) model.getValueAt(selectedRow, 8);


        // Validasi input duration

//        if (durationText.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Duration cannot be empty.");
//            return;
////        }
//        try {
//            duration = durationText;
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(this, "Invalid input for duration.");
//            return;
//        }

        // Validasi input rating
//        String ratingText = ratingField.getText();
//        if (rating.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Rating cannot be empty.");
//            return;
//        }
//        try {
//            rating = Float.parseFloat(ratingText);
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(this, "Invalid input for rating.");
//            return;
//        }

//        String releaseDate = releaseDateField.getText();

        String sql = "UPDATE movies SET title=?, genre=?, director=?, actors=?, duration=?, rating=?, release_date=? " +
                "WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, genre);
            pstmt.setString(3, director);
            pstmt.setString(4, actors);
            pstmt.setInt(5, durationText);
            pstmt.setDouble(6, rating);
            pstmt.setDate(7, java.sql.Date.valueOf(releaseDate.toLocalDate()));
            pstmt.setInt(8, movieId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Movie updated successfully!");

                // Clear input fields and reload movies
                clearFields();
                loadMovies();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update movie.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update movie.");
        }
    }

    private void deleteMovie() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a movie to delete.");
            return;
        }

        int movieId = (int) movieTable.getValueAt(selectedRow, 0);

        String sql = "DELETE FROM movies WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Movie deleted successfully!");

                // Clear input fields and reload movies
                clearFields();
                loadMovies();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete movie.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete movie.");
        }
    }

    private void clearFields() {
        titleField.setText("");
        genreField.setText("");
        directorField.setText("");
        actorsField.setText("");
        durationField.setText("");
        ratingField.setText("");
        releaseDateField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MovieForm();
        });
    }
}
