package at.ac.tuwien.foop.mouserace.client;

import at.ac.tuwien.foop.mouserace.client.event.EventDispatcher;
import at.ac.tuwien.foop.mouserace.client.event.MouseGameEventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static at.ac.tuwien.foop.mouserace.client.event.MouseGameEventType.GAME_STOPPED;

public class MouseRace extends JFrame {

    private static final Dimension windowDimension = new Dimension(360, 420);

    public MouseRace() {
        EventDispatcher<MouseGameEventType> dispatcher = new EventDispatcher<>();
        add(new MouseGame(dispatcher));
        setTitle("Mouse Race v0.1");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                dispatcher.publishEvent(GAME_STOPPED);
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
