package com.mrjaffesclass.apcs.mvc.template;

import com.mrjaffesclass.apcs.messenger.*;
import java.util.Random;
import java.awt.Dimension;
/**
 * The model represents the data use.
 * @author Roger Jaffe
 * @version 1.0
 */
public class Model implements MessageHandler {

  // Messaging system for the MVC
  private final Messenger mvcMessaging;
  private boolean [][] gameMap = new boolean[8][8];

  /**
   * Model constructor: Create the data representation of the program
   * @param messages Messaging class instantiated by the Controller for 
   *   local messages between Model, View, and controller
   */
  public Model(Messenger messages) {
      /**
       * Sets the variables.
       */
    mvcMessaging = messages;
  }
  
  /**
   * Initialize the model here and subscribe to any required messages
   */
  public void init() {
      mvcMessaging.subscribe("Square:position", this);
      mvcMessaging.subscribe("GameOver:Reset",this);
      generateGameMap();
  }
  public void generateGameMap(){
      /**
       * places mines in random places.
       * @return random places
       **/
      for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                    gameMap[x][y] = false;
            }
        }
        
        int count = 0;
        while (count < 10) {
            int rand1 = (int) (Math.random() * 8);
            int rand2 = (int) (Math.random() * 8);
            if (!gameMap[rand1][rand2]) {
                gameMap[rand1][rand2] = true;
                count++;
            }

        }
    
  }

    private void checkGameMap(Dimension dimension) {
        if (gameMap[(int) dimension.width][(int) dimension.height]) {
            mvcMessaging.notify("Model:LoseALife");

        } else {
            mvcMessaging.notify("Model:ScoreAPoint");

        }
    }

    private void resetBoard() {
        generateGameMap();
    }
    

    @Override
    public void messageHandler(String messageName, Object messagePayload) {
        switch (messageName) {
            case "Square:position":
                checkGameMap((Dimension) messagePayload);
                break;
                 case "GameOver:Reset": resetBoard();break;
        }
    }
}
    