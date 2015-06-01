package at.ac.tuwien.foop.mouserace.client;

import at.ac.tuwien.foop.mouserace.client.event.EventDispatcher;
import at.ac.tuwien.foop.mouserace.client.event.NetworkEventType;
import at.ac.tuwien.foop.mouserace.client.event.UIEventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static at.ac.tuwien.foop.mouserace.client.MouseGameOptions.GAME_SIZE;
import static at.ac.tuwien.foop.mouserace.client.event.NetworkEventType.*;
import static at.ac.tuwien.foop.mouserace.client.event.UIEventType.*;

public class MouseGame extends JPanel {

    private boolean waitingForServerStarting = true;
    private boolean waitingForUserStarting = true;
    private boolean waitingForServer = true;

    public MouseGame(EventDispatcher<UIEventType> uiDispatcher, EventDispatcher<NetworkEventType> networkDispatcher) {
        addKeyListener(new KeyAdapter(uiDispatcher));

        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);


        networkDispatcher.subscribeTo(READY, e -> {
            waitingForServer = false;
            waitingForUserStarting = true;
            waitingForServerStarting = true;
            //TODO: e -> <map>, <figures>, <own mouse>
        });
        uiDispatcher.subscribeTo(GAME_STOPPED, e -> {
            waitingForServer = true;
            waitingForUserStarting = true;
            waitingForServerStarting = true;
            networkDispatcher.publishEvent(NetworkEventType.ABORT);
        });
        uiDispatcher.subscribeTo(START_PRESSED, e -> {
            waitingForUserStarting = false;
            waitingForServer = false;
            waitingForServerStarting = true;
            networkDispatcher.publishEvent(ACK_READY);
        });
        networkDispatcher.subscribeTo(START, e -> {
            waitingForServer = false;
            waitingForUserStarting = false;
            waitingForServerStarting = false;
        });
        networkDispatcher.subscribeTo(END, e -> {
            waitingForServer = false;
            waitingForUserStarting = false;
            waitingForServerStarting = false;
            //TODO: e -> display who's won or error conditions
        });

        networkDispatcher.publishEvent(REGISTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (waitingForServer) {
            showScreen("Waiting for the Server for initial response", g);
        } else if (waitingForUserStarting) {
            showScreen("To start the game press 's'", g);
        } else if (waitingForServerStarting) {
            showScreen("Waiting for the Server to start the game", g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private void showScreen(String text, Graphics g) {
        Font font = new Font("Helvetica", Font.BOLD, 12);
        FontMetrics m = this.getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(text, (GAME_SIZE - m.stringWidth(text)) / 2, GAME_SIZE / 2);
    }

    private class KeyAdapter extends java.awt.event.KeyAdapter {

        private EventDispatcher<UIEventType> uiDispatcher;

        private KeyAdapter(EventDispatcher<UIEventType> uiDispatcher) {
            this.uiDispatcher = uiDispatcher;
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int key = keyEvent.getKeyCode();

            switch (key) {
                case 's':
                    uiDispatcher.publishEvent(START_PRESSED);
                    break;
                case 'S':
                    uiDispatcher.publishEvent(START_PRESSED);
                    break;
                case KeyEvent.VK_LEFT:
                    uiDispatcher.publishEvent(LEFT_ARROW_PRESSED);
                    break;
                case KeyEvent.VK_RIGHT:
                    uiDispatcher.publishEvent(RIGHT_ARROW_PRESSED);
                    break;
                case KeyEvent.VK_UP:
                    uiDispatcher.publishEvent(UP_ARROW_PRESSED);
                    break;
                case KeyEvent.VK_DOWN:
                    uiDispatcher.publishEvent(DOWN_ARROW_PRESSED);
                    break;
                case KeyEvent.VK_ESCAPE:
                    uiDispatcher.publishEvent(GAME_STOPPED);
                    break;
            }
        }
    }
}
