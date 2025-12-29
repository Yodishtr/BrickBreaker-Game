import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){
        Canvas gameCanvas = new Canvas(700, 700);
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        primaryStage.setTitle("Brick Breaker Game");
        Group root = new Group();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        // set the stage by drawing the brick layer & paddle & ball
        drawSomething(gc);
        root.getChildren().add(gameCanvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String [] args){
        launch(args);
    }

    // just use to test out the drawing of the components
    private static void drawSomething(GraphicsContext gc){
        gc.setFill(Color.ORANGE);
        gc.setStroke(Color.BLACK);
        int rows = 3;
        int cols = 7;
        double currX = 0;
        double currY = 0;
        double w = 100;
        double h = 100;
        for (int j = 0; j < rows; j++){
            for (int i = 0; i < cols; i++){
                gc.fillRect(currX, currY, w, h);
                currX += (w + 1);
            }
            currY += (h + 1);
            currX = 0;
        }
        // keep w and h similar so that it looks like a circle
        gc.fillOval(100, 400, 30, 30);
    }

    private static class Brick {
        double positionX;
        double positionY;
        double width;
        double height;
        boolean alive;

        public Brick(double positionX, double positionY, double width, double height){
            this.positionX = positionX;
            this.positionY = positionY;
            this.width = width;
            this.height = height;
            this.alive = true;
        }
    }

    private static class Paddle {
        double positionX;
        double positionY;
        double width;
        double height;
        boolean leftPressed;
        boolean rightPressed;
        double speed;

        public Paddle(double positionX, double positionY, double width, double height){
            this.positionX = positionX;
            this.positionY = positionY;
            this.width = width;
            this.height = height;
            this.leftPressed = false;
            this.rightPressed = false;
            this.speed = 400;
        }

        public void clampToBounds(double sceneWidth){
            if (this.positionX < 0){
                this.positionX = 0;
            } else if (this.positionX > sceneWidth - this.width){
                this.positionX = sceneWidth - this.width;
            }
        }

        public void move(double dt){
            if (leftPressed){
                this.positionX -= speed * dt;
            } else if (rightPressed){
                this.positionX += speed * dt;
            }
        }
    }

    private static class Ball {
        double centerPositionX;
        double centerPositionY;
        double radius;
        double velocityX;
        double velocityY;
        boolean launched;

        public Ball(double centerPositionX, double centerPositionY, double radius, double velocityX, double velocityY){
            this.centerPositionX = centerPositionX;
            this.centerPositionY = centerPositionY;
            this.radius = radius;
            this.velocityX = velocityX; // 150 - 250 px
            this.velocityY = velocityY; // 250 - 350 px
            this.launched = false;
        }

        public void move(double dt, double sceneWidth, double sceneHeight, Paddle paddle){
            this.centerPositionX += velocityX * dt;
            this.centerPositionY += velocityY * dt;
            if (this.centerPositionX + this.radius < 0){
                this.centerPositionX = this.radius;
            } else if (this.centerPositionX + this.radius > sceneWidth){
                this.centerPositionX = sceneWidth - this.radius;
            }
            if (this.centerPositionY + this.radius < 0){
                this.centerPositionY = sceneHeight - this.radius;
            } else if (this.centerPositionY + this.radius > paddle.positionY + paddle.height){
                resetOnPaddle(paddle);
            }
        }

        public void bounceHorizontal(){
            this.velocityX *= -1;
        }

        public void bounceVertical(){
            this.velocityY *= -1;
        }

        public void resetOnPaddle(Paddle paddle){
            this.centerPositionX = paddle.positionX + (paddle.width / 2);
            this.centerPositionY = paddle.positionY - (2 * this.radius);
        }
    }

}
