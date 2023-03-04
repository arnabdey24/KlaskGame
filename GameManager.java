public class GameManager {

    private GameArena gameArena;
    private Rectangle outerRectangle, innerRectangle;
    private Text welcomeText, leftPlayerResult, rightPlayerResult;
    private Ball ball, leftHole, rightHole;
    private Line midLine;
    private Bat leftBat, rightBat;
    private Magnet magnet1, magnet2, magnet3;
    private ServeArea leftServeAreaTop, rightServeAreaTop, rightServeAreaDown, leftServeAreaDown;

    private int speed = 2;
    private boolean served = false;
    private int servePosition = 0;
    private String servePlayer = "left";
    private int leftScore = 0;
    private int rightScore = 0;


    public GameManager() {
        gameArena = new GameArena(800, 500, true);
        init();
        start();
    }

    /**
     * This method start the game loop
     */
    private void start() {

        //place the game part in serve position
        placeServe();

        //game loop
        while (true) {

            //game loop latency
            gameLoopLatency(5);

            if (!served) {
                //serve control only before serve
                serveControls();
            } else {

                //right player bat position update
                updateRightBat();

                //left player bat position update
                updateLeftBat();

                //update ball position
                ballMove();

                //magnet collide check
                magnetCollideCheck();

                //ball collide with hole check
                ballInHoleCheck();

                //game end check
                gameOverCheck();
            }
        }
    }

    /**
     * Helper method to add game latency
     *
     * @param millis latency time in milli sec
     */
    private void gameLoopLatency(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to check if it is a goal or not
     */
    private void ballInHoleCheck() {
        if (ball.collides(leftHole)) {
            servePlayer = "left";
            rightScore++;
            rightPlayerResult.setText(String.valueOf(rightScore));
            placeServe();
        } else if (ball.collides(rightHole)) {
            servePlayer = "right";
            leftScore++;
            leftPlayerResult.setText(String.valueOf(leftScore));
            placeServe();
        } else if (rightBat.collides(rightHole)) {
            servePlayer = "right";
            leftScore++;
            leftPlayerResult.setText(String.valueOf(leftScore));
            placeServe();
        } else if (leftBat.collides(leftHole)) {
            servePlayer = "left";
            rightScore++;
            rightPlayerResult.setText(String.valueOf(rightScore));
            placeServe();
        }
    }

    /**
     * Method to update left bat position
     */
    private void updateLeftBat() {
        double leftBatX = leftBat.getXPosition();
        double leftBatY = leftBat.getYPosition();

        if (gameArena.letterPressed('s')) {
            if (leftBat.getYPosition() + speed < 400) leftBat.move(0, speed);
        }
        if (gameArena.letterPressed('w')) {
            if (leftBat.getYPosition() - speed > 100) leftBat.move(0, -speed);
        }
        if (gameArena.letterPressed('a')) {
            if (leftBat.getXPosition() - speed > 100) leftBat.move(-speed, 0);
        }
        if (gameArena.letterPressed('d')) {
            if (leftBat.getXPosition() + speed < 400) leftBat.move(speed, 0);
        }

        leftBat.setDirection(leftBat.getXPosition() - leftBatX, leftBat.getYPosition() - leftBatY);
    }

    /**
     * Method to update right bat position
     */
    private void updateRightBat() {
        double rightBatX = rightBat.getXPosition();
        double rightBatY = rightBat.getYPosition();

        if (gameArena.downPressed()) {
            if (rightBat.getYPosition() + speed < 400) rightBat.move(0, speed);
        }
        if (gameArena.upPressed()) {
            if (rightBat.getYPosition() - speed > 100) rightBat.move(0, -speed);
        }
        if (gameArena.leftPressed()) {
            if (rightBat.getXPosition() - speed > 400) rightBat.move(-speed, 0);
        }
        if (gameArena.rightPressed()) {
            if (rightBat.getXPosition() + speed < 700) rightBat.move(speed, 0);
        }

        rightBat.setDirection(rightBat.getXPosition() - rightBatX, rightBat.getYPosition() - rightBatY);
    }

    /**
     * Helper method to handle serve time controls
     */
    private void serveControls() {
        if (servePlayer.equals("left") && gameArena.letterPressed('w')) {
            leftBat.setXPosition(120);
            leftBat.setYPosition(120);
            ball.setXPosition(130);
            ball.setYPosition(130);
        }

        if (servePlayer.equals("left") && gameArena.letterPressed('s')) {
            leftBat.setXPosition(120);
            leftBat.setYPosition(380);
            ball.setXPosition(130);
            ball.setYPosition(370);
        }

        if (servePlayer.equals("right") && gameArena.upPressed()) {
            rightBat.setXPosition(680);
            rightBat.setYPosition(120);
            ball.setXPosition(670);
            ball.setYPosition(130);
        }

        if (servePlayer.equals("right") && gameArena.downPressed()) {
            rightBat.setXPosition(680);
            rightBat.setYPosition(380);
            ball.setXPosition(670);
            ball.setYPosition(370);
        }
        if (gameArena.spacePressed()) {
            served = true;
        }
    }

    /**
     * Method to check if any player got 10 point, if so that player wins
     */
    private void gameOverCheck() {
        if (leftScore == 10 || rightScore == 10) {
            while (true) {
                gameLoopLatency(1);

                if (gameArena.enterPressed()) {

                    leftScore = 0;
                    rightScore = 0;
                    leftPlayerResult.setText(String.valueOf(leftScore));
                    rightPlayerResult.setText(String.valueOf(rightScore));
                    break;
                }
            }
        }
    }

    /**
     * Method to handle magnet collision with bat
     */
    private void magnetCollideCheck() {
        if (leftBat.collidesWithMagnet(magnet1)) {
            magnet1.setXPosition(leftBat.getXPosition());
            magnet1.setYPosition(leftBat.getYPosition());
            leftBat.addMagnet(1);
        }
        if (leftBat.collidesWithMagnet(magnet2)) {
            magnet2.setXPosition(leftBat.getXPosition());
            magnet2.setYPosition(leftBat.getYPosition());
            leftBat.addMagnet(2);
        }
        if (leftBat.collidesWithMagnet(magnet3)) {
            magnet3.setXPosition(leftBat.getXPosition());
            magnet3.setYPosition(leftBat.getYPosition());
            leftBat.addMagnet(3);
        }

        if (rightBat.collidesWithMagnet(magnet1)) {
            magnet1.setXPosition(rightBat.getXPosition());
            magnet1.setYPosition(rightBat.getYPosition());
            rightBat.addMagnet(1);
        }
        if (rightBat.collidesWithMagnet(magnet2)) {
            magnet2.setXPosition(rightBat.getXPosition());
            magnet2.setYPosition(rightBat.getYPosition());
            rightBat.addMagnet(2);
        }
        if (rightBat.collidesWithMagnet(magnet3)) {
            magnet3.setXPosition(rightBat.getXPosition());
            magnet3.setYPosition(rightBat.getYPosition());
            rightBat.addMagnet(3);
        }

        if (leftBat.getMagnets() == 2) {
            servePlayer = "left";
            rightScore++;
            rightPlayerResult.setText(String.valueOf(rightScore));
            placeServe();
        } else if (rightBat.getMagnets() == 2) {
            servePlayer = "right";
            leftScore++;
            leftPlayerResult.setText(String.valueOf(leftScore));
            placeServe();
        }
    }

    /**
     * Method to place game part in serving position
     */
    private void placeServe() {
        served = false;

        magnet1.setXPosition(400);
        magnet1.setYPosition(180);

        magnet2.setXPosition(400);
        magnet2.setYPosition(250);

        magnet3.setXPosition(400);
        magnet3.setYPosition(320);


        rightBat.clear();
        leftBat.clear();

        if (servePlayer.equals("left")) {
            ball.setDirection(2, 2);
            leftBat.setXPosition(120);
            leftBat.setYPosition(120);
            ball.setXPosition(130);
            ball.setYPosition(130);
            rightBat.setXPosition(500);
            rightBat.setYPosition(250);
        } else {
            ball.setDirection(-2, -2);
            rightBat.setXPosition(680);
            rightBat.setYPosition(380);
            ball.setXPosition(670);
            ball.setYPosition(370);
            leftBat.setXPosition(300);
            leftBat.setYPosition(250);
        }
    }

    /**
     * Method to update ball position
     */
    private void ballMove() {

        ballCollideCheck();

        ball.move(ball.getDx(), ball.getDy());
    }

    /**
     * Method to handle ball collision with bat,magnet,wall
     */
    private void ballCollideCheck() {

        Ball tmpBall = new Ball(ball.getXPosition(), ball.getYPosition(), ball.getSize(), ball.getColour());
        tmpBall.move(ball.getDx(), ball.getDy());

        if (tmpBall.getXPosition() <= 100 + tmpBall.getSize() || tmpBall.getXPosition() >= 700 - tmpBall.getSize() ||
                tmpBall.getYPosition() <= 100 + tmpBall.getSize() || tmpBall.getYPosition() >= 400 - tmpBall.getSize()) {
            if (tmpBall.getXPosition() <= 100 + tmpBall.getSize() || tmpBall.getXPosition() >= 700 - tmpBall.getSize()) {
                ball.setDirection(-ball.getDx(), ball.getDy());
            }
            if (tmpBall.getYPosition() <= 100 + tmpBall.getSize() || tmpBall.getYPosition() >= 400 - tmpBall.getSize()) {
                ball.setDirection(ball.getDx(), -ball.getDy());
            }

            ball.setDirection(ball.getDx() * 0.95, ball.getDy() * 0.95);
        } else if (tmpBall.collides(leftBat) || tmpBall.collides(rightBat)) {
            if (tmpBall.collides(leftBat)) {
                ball.setDirection(-ball.getDx() + leftBat.getDx(), -ball.getDy() + leftBat.getDy());
            } else {
                ball.setDirection(-ball.getDx() + rightBat.getDx(), -ball.getDy() + rightBat.getDy());
            }
        } else if (ball.collides(magnet1) || ball.collides(magnet2) || ball.collides(magnet3)) {
            if (ball.collides(magnet1)) {
                magnet1.move(ball.getDx() * 2, ball.getDy() * 2);
            } else if (ball.collides(magnet2)) {
                magnet2.move(ball.getDx() * 2, ball.getDy() * 2);
            } else {
                magnet3.move(ball.getDx() * 2, ball.getDy() * 2);
            }

            ball.setDirection(-ball.getDx(), -ball.getDy());
        }
    }

    /**
     * Method to initiate all game parts
     */
    private void init() {

        outerRectangle = new Rectangle(80, 80, 640, 340, "GREY");
        innerRectangle = new Rectangle(100, 100, 600, 300, "BLUE");

        gameArena.addRectangle(outerRectangle);
        gameArena.addRectangle(innerRectangle);

        welcomeText = new Text("Welcome to Klask v1!", 14, 100, 50, "WHITE");
        gameArena.addText(welcomeText);

        midLine = new Line(400, 100, 400, 400, 1, "GREY");
        gameArena.addLine(midLine);

        leftHole = new Ball(140, 250, 30, "GREY");
        gameArena.addBall(leftHole);

        rightHole = new Ball(660, 250, 30, "GREY");
        gameArena.addBall(rightHole);

        leftPlayerResult = new Text("0", 20, 50, 260, "WHITE");
        gameArena.addText(leftPlayerResult);

        rightPlayerResult = new Text("0", 20, 740, 260, "WHITE");
        gameArena.addText(rightPlayerResult);

        leftServeAreaTop = new ServeArea(50, 50, 100, 100, 250, 120, "GREY", 5);
        gameArena.addSemiCircle(leftServeAreaTop);

        rightServeAreaTop = new ServeArea(650, 50, 100, 100, 280, -120, "GREY", 5);
        gameArena.addSemiCircle(rightServeAreaTop);

        rightServeAreaDown = new ServeArea(650, 350, 100, 100, 200, -120, "GREY", 5);
        gameArena.addSemiCircle(rightServeAreaDown);

        leftServeAreaDown = new ServeArea(50, 350, 100, 100, 350, 120, "GREY", 5);
        gameArena.addSemiCircle(leftServeAreaDown);

        leftBat = new Bat(300, 250, 20, "BLACK");
        gameArena.addBat(leftBat);

        rightBat = new Bat(500, 250, 20, "BLACK");
        gameArena.addBat(rightBat);

        ball = new Ball(130, 130, 10, "YELLOW");
        gameArena.addBall(ball);

        magnet1 = new Magnet(400, 180, 5, "WHITE");
        gameArena.addBall(magnet1);

        magnet2 = new Magnet(400, 250, 5, "WHITE");
        gameArena.addBall(magnet2);

        magnet3 = new Magnet(400, 320, 5, "WHITE");
        gameArena.addBall(magnet3);
    }
}
