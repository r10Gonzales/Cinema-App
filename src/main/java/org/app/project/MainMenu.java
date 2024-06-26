package org.app.project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Cinema Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // Menu Movie
        JMenu movieMenu = new JMenu("Movie");
        JMenuItem manageMovies = new JMenuItem("Manage Movies");
        movieMenu.add(manageMovies);

        manageMovies.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MovieForm();
            }
        });

        // Menu User
        JMenu userMenu = new JMenu("User");
        JMenuItem manageUsers = new JMenuItem("Manage Users");
        userMenu.add(manageUsers);

        manageUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserForm();
            }
        });

        // Menu Booking
        JMenu bookingMenu = new JMenu("Booking");
        JMenuItem manageBookings = new JMenuItem("Manage Bookings");
        bookingMenu.add(manageBookings);

        manageBookings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BookingForm();
            }
        });

        // Menu Showtime
        JMenu showtimeMenu = new JMenu("Showtime");
        JMenuItem manageShowtimes = new JMenuItem("Manage Showtimes");
        showtimeMenu.add(manageShowtimes);

        manageShowtimes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShowtimeForm();
            }
        });

        // Add menus to menu bar
        menuBar.add(movieMenu);
        menuBar.add(userMenu);
        menuBar.add(bookingMenu);
        menuBar.add(showtimeMenu);

        // Set menu bar
        setJMenuBar(menuBar);

        setVisible(true);
    }
}
