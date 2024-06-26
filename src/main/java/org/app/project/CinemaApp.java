package org.app.project;

import org.app.project.BookingForm;
import org.app.project.MovieForm;
import org.app.project.ShowtimeForm;
import org.app.project.UserForm;

import javax.swing.*;

import javax.swing.*;

public class CinemaApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu();
        });
    }
}

