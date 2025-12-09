package models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.awt.*;


/*
    Class representing the player's paddle
 */
public class Paddle {
    int width;
    int height;
    int positionX;
    int positionY;
    Boolean movingLeft;
    Boolean movingRight;
    int speed;

    public Paddle(int width, int height, int positionX, int positionY, Boolean movingLeft, Boolean movingRight,
                  int speed){
        this.width = width;
        this.height = height;
        this.positionX = positionX;
        this.positionY = positionY;
        this.movingLeft = movingLeft;
        this.movingRight = movingRight;
        this.speed = speed;
    }

    public Paddle(){}

    // Paddle methods:
    public void paintPaddle(GraphicsContext g){
        g.setFill(Paint.valueOf("white"));
        g.fillRect(positionX, positionY, width, height);
    }

    public void movePaddle(String direction, int screenWidth){
        if (direction.equals("left")){
            movingLeft = true;
            movingRight = false;
        } else if (direction.equals("right")){
            movingRight = true;
            movingLeft = false;
        }

        if (movingLeft){
            positionX -= speed;
            if (positionX < 0) {
                positionX = 0;
            }
        } else if (movingRight){
            positionX += speed;
            if (positionX + this.width > screenWidth){
                positionX = screenWidth - this.width;
            }
        }
    }
}
