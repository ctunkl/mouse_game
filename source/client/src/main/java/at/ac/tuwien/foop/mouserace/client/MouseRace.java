package at.ac.tuwien.foop.mouserace.client;

import at.ac.tuwien.foop.mouserace.client.event.NetworkGameEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MouseRace extends JFrame {

    private static final Dimension windowDimension = new Dimension(360, 420);

    public MouseRace() {
        NetworkGameEventListener listener = new NetworkGameEventListener();
        add(new MouseGame(listener));
        setTitle("Mouse Race v0.1");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                listener.gameStopped();
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(windowDimension);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MouseRace mr = new MouseRace();
            mr.setVisible(true);
        });
    }
}
