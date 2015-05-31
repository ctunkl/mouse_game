package at.ac.tuwien.foop.mouserace.client;

import at.ac.tuwien.foop.mouserace.client.event.GameEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static at.ac.tuwien.foop.mouserace.client.MouseGameOptions.GAME_SIZE;
import static at.ac.tuwien.foop.mouserace.client.MouseGameOptions.TICK_INTERVAL_IN_SEC;

public class MouseGame extends JPanel implements ActionListener {

    private Timer timer;
    private boolean gameStarted = false;

    public MouseGame(GameEventListener listener) {
        initGame();
        addKeyListener(new KeyAdapter(listener));

        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);
    }

    private void initGame() {
        timer = new Timer(TICK_INTERVAL_IN_SEC, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameStarted) {

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

        private final GameEventListener listener;

        KeyAdapter(GameEventListener listener) {
            this.listener = listener;
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int key = keyEvent.getKeyCode();

            switch (key) {
                case 'S':
                    gameStarted = true;
                    break;
                case KeyEvent.VK_LEFT:
                    listener.leftArrowPressed();
                    break;
                case KeyEvent.VK_RIGHT:
                    listener.rightArrowPressed();
                    break;
                case KeyEvent.VK_UP:
                    listener.upArrowPressed();
                    break;
                case KeyEvent.VK_DOWN:
                    listener.downArrowPressed();
                    break;
                case KeyEvent.VK_ESCAPE:
                    listener.gameStopped();
                    gameStarted = false;
                    break;
            }
        }
    }
}
