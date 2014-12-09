package com.mrjaffesclass.apcs.mvc.template;

import com.mrjaffesclass.apcs.messenger.MessageHandler;
import com.mrjaffesclass.apcs.messenger.Messenger;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author barry
 */
public class GameOver extends JFrame implements MessageHandler {

    JLabel winorlose = new JLabel("test");
    JButton button = new JButton("Play Again?");
    int score;
    private Messenger m;

    public GameOver(Messenger m) {
        this.m = m;
        setResizable(false);
        this.setLocationRelativeTo(null);
        setPreferredSize(new Dimension(250, 100));
        add(winorlose);
        listener();
        add(button, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(false);
    }

    public void init(){
       m.subscribe("View:EndGame", this); 
       m.subscribe("View:SendScore", this);
    }
    private void listener() {

        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m.notify("GameOver:Reset");
                setVisible(false);
            }
        });
    }

    private void appear(boolean state){
        if(state)
            winorlose.setText("YOU WIN!!! With a score of : "+score);
        else
            winorlose.setText("You lose  with a score of : "+score);
        setVisible(true);
    }
    private void setScore(int s){
        score = s;
    }
    @Override
    public void messageHandler(String messageName, Object messagePayload) {
            switch(messageName){
                case "View:EndGame": appear((boolean)messagePayload);break;
                case "View:SendScore" : setScore((int)messagePayload);break;
            }
    }

}
