package com.mrjaffesclass.apcs.mvc.template;

import com.mrjaffesclass.apcs.messenger.MessageHandler;
import com.mrjaffesclass.apcs.messenger.Messenger;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class View extends JFrame implements MessageHandler {

    private int numOfB = 8;
    private CreateSquares[][] squares = new CreateSquares[numOfB][numOfB];
    private final int BUTTON_SIZE = 30;
    private final int BOARD_SIZE = BUTTON_SIZE * numOfB;
    private Messenger messenger;
    private JPanel panel = new JPanel();
    private JPanel panel2 = new JPanel();
    private int lives = 3;
    private int score = 0;
    private JLabel lifeLab = new JLabel(lives + "");
    private JLabel scoreLab = new JLabel(score + "");
    GridLayout layout1 = new GridLayout(0, 2);
    GridLayout layout2 = new GridLayout(numOfB, numOfB);

    public View(Messenger messenger) {
        super("MineSweeper");
        this.messenger = messenger;
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        panel.setLayout(layout1);
        panel.setLayout(layout2);
        panel.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        createBoard();

        panel2.add(new Label("Score:"));
        panel2.add(scoreLab);
        panel2.add(new Label("Lives:"));
        panel2.add(lifeLab);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(panel, BorderLayout.NORTH);
        add(new JSeparator(), BorderLayout.CENTER);
        add(panel2, BorderLayout.SOUTH);
        pack();

        setVisible(true);

    }
  
    private void createBoard() {
        int counter = 0;
        for (int y = 0; y < numOfB; y++) {
            for (int x = 0; x < numOfB; x++) {
                squares[x][y] = new CreateSquares(x, y, messenger);
                panel.add(squares[x][y].getButton());

            }
        }
    }
    public void init() {
        messenger.subscribe("Model:ScoreAPoint", this);
        messenger.subscribe("Model:LoseALife", this);
        messenger.subscribe("GameOver:Reset", this);
    }

    private void loseALife() {
        lives--;
        lifeLab.setText(lives + "");
        if (lives <= 0) {
            messenger.notify("View:SendScore", score);
            messenger.notify("View:EndGame", false);

            setVisible(false);
        }
    }

    private void scoreAPoint() {
        score++;
        scoreLab.setText(score + "");
        if (score >= 54) {
            messenger.notify("View:SendScore", score);
            messenger.notify("View:EndGame", true);

        }
    }

    private void reset() {
        score = 0;
        lives = 3;
        scoreLab.setText(score + "");
        lifeLab.setText(lives + "");
        setVisible(true);
    }

    @Override
    public void messageHandler(String messageName, Object messagePayload) {
        switch (messageName) {
            case "Model:LoseALife":
                loseALife();
                break;
            case "Model:ScoreAPoint":
                scoreAPoint();
                break;
            case "GameOver:Reset":
                reset();
                break;
        }
    }
