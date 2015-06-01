package at.ac.tuwien.foop.mouserace.client;

import at.ac.tuwien.foop.mouserace.client.event.EventDispatcher;
import at.ac.tuwien.foop.mouserace.client.event.MouseGameEventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static at.ac.tuwien.foop.mouserace.client.MouseGameOptions.GAME_SIZE;
import static at.ac.tuwien.foop.mouserace.client.event.MouseGameEventType.*;

public class MouseGame extends JPanel implements ActionListener {

    private boolean gameStarted = false;

    public MouseGame(EventDispatcher<MouseGameEventType> dispatcher) {
        addKeyListener(new KeyAdapter(dispatcher));

        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameStarted) {
            //TODO
        } else {
            showWaitingScreen(g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private void showWaitingScreen(Graphics g) {
        Font font = new Font("Helvetica", Font.BOLD, 12);
        FontMetrics m = this.getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        String text = "Waiting for the Server to start the game !";
        g.drawString(text, (GAME_SIZE - m.stringWidth(text)) / 2, GAME_SIZE / 2);
    }

    private class KeyAdapter extends java.awt.event.KeyAdapter {

        private EventDispatcher<MouseGameEventType> dispatcher;

        private KeyAdapter(EventDispatcher<MouseGameEventType> dispatcher) {
            this.dispatcher = dispatcher;
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int key = keyEvent.getKeyCode();

            switch (key) {
                case KeyEvent.VK_LEFT:
                    dispatcher.publishEvent(LEFT_ARROW_PRESSED);
                    break;
                case KeyEvent.VK_RIGHT:
                    dispatcher.publishEvent(RIGHT_ARROW_PRESSED);
                    break;
                case KeyEvent.VK_UP:
                    dispatcher.publishEvent(UP_ARROW_PRESSED);
                    break;
                case KeyEvent.VK_DOWN:
                    dispatcher.publishEvent(DOWN_ARROW_PRESSED);
                    break;
                case KeyEvent.VK_ESCAPE:
                    dispatcher.publishEvent(GAME_STOPPED);
                    gameStarted = false;
                    break;
            }
        }
    }
}
