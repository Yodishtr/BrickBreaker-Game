import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        // drawSomething(gc);
        Brick[][] brickWall = initBricks(gc);
        Paddle paddle = initPaddle(gc);
        Ball ball = initBall(gc);

        root.getChildren().add(gameCanvas);
        Scene scene = new Scene(root);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.LEFT){
                paddle.leftPressed = true;
            } else if (event.getCode() == KeyCode.RIGHT){
                paddle.rightPressed = true;
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.LEFT){
                paddle.leftPressed = false;
            } else if (event.getCode() == KeyCode.RIGHT){
                paddle.rightPressed = false;
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
        AnimationTimer timer = new AnimationTimer() {
            private long lastNow = -1;

            @Override
            public void handle(long now){
                if (lastNow < 0){
                    lastNow = now;
                    return;
                }
                double dt = (now - lastNow) / 1_000_000_000.0;
                lastNow = now;
                if (dt > 0.05){
                    dt = 0.05;
                }
                double w = gameCanvas.getWidth();
                double h = gameCanvas.getHeight();
                update(dt, paddle, ball, brickWall, w, h);
                render(gc, brickWall, paddle, ball, w, h);
            }
        };
        timer.start();
    }

    public static void main(String [] args){
        launch(args);
    }

    private static void update(double dt, Paddle paddle, Ball ball, Brick[][] bricks, double sceneWidth,
                               double sceneHeight){}

    private static void render(GraphicsContext gc, Brick[][] bricks, Paddle paddle, Ball ball,
                               double canvasWidth, double canvasHeight){

    }

    private static Brick[][] initBricks(GraphicsContext gc){
        int rows = 3;
        int cols = 7;
        double currX = 0;
        double currY = 0;
        double w = 100;
        double h = 100;
//        ArrayList<Color> colorsArray = new ArrayList<>(
//                Arrays.asList(Color.ORANGE, Color.BLUE, Color.GREEN));

        Brick[][] bricks = new Brick[rows][cols];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
               Brick currBrick = new Brick(currX, currY, w, h);
               bricks[i][j] = currBrick;
//               gc.setFill(colorsArray.get(i));
//               gc.setStroke(Color.BLACK);
               gc.fillRect(currX, currY, w, h);
               currX += (w + 1);
            }
            currY += (h + 1);
            currX = 0;
        }
        return bricks;
    }

    private static Paddle initPaddle(GraphicsContext gc){
        Paddle myPaddle = new Paddle(300, 600, 100, 20);
        return myPaddle;
    }

    private static Ball initBall(GraphicsContext gc){
        Ball currBall = new Ball(325, 500, 20, 150, 250);
        // x-coordinate for ball should be its centerX - r, centerY - r, height and width is 2 * radius
        return currBall;
    }

    // just use to test out the drawing of the components
//    private static void drawSomething(GraphicsContext gc){
//        gc.setFill(Color.ORANGE);
//        gc.setStroke(Color.BLACK);
//        int rows = 3;
//        int cols = 7;
//        double currX = 0;
//        double currY = 0;
//        double w = 100;
//        double h = 100;
//        for (int j = 0; j < rows; j++){
//            for (int i = 0; i < cols; i++){
//                gc.fillRect(currX, currY, w, h);
//                currX += (w + 1);
//            }
//            currY += (h + 1);
//            currX = 0;
//        }
//        // keep w and h similar so that it looks like a circle
//        gc.fillOval(100, 400, 30, 30);
//    }


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
            if (this.centerPositionX - this.radius < 0){
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
