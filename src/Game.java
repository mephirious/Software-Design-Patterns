interface IGame {
    void createGameSession();
    void startGameplay() throws InterruptedException;
}

class GameSession {
    private GameBoard gameBoard;
    private int tickNumber;
    private volatile boolean isRunning;
    private static final int TICK_RATE_MS = 50;

    public void initialize() {
        gameBoard = new GameBoard();
        tickNumber = 0;
        isRunning = true;
    }

    public void startGameplay() throws InterruptedException {
        long lastTime = System.currentTimeMillis();

        while (isRunning) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastTime;

            if (elapsedTime >= TICK_RATE_MS) {
                tickNumber += 1;
                boolean continueGame = gameBoard.Tick();

                if (tickNumber % 100 == 0) {
                    gameBoard.addEntity(Plant.Director.constructPeaShooter());
                }

                if (tickNumber % 150 == 0) {
                    gameBoard.addEntity(Plant.Director.constructWallNut());
                }

                if (tickNumber % 200 == 0) {
                    gameBoard.addEntity(Plant.Director.constructSunflower());
                }


                if (!continueGame) {
                    isRunning = false;
                    break;
                }

                lastTime = currentTime;
            }

            long sleepTime = TICK_RATE_MS - (System.currentTimeMillis() - lastTime);
            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
            }
        }
    }
}

public class Game implements IGame {
    private static Game instance;
    private GameSession gameSession;

    private Game() {}

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    @Override
    public void createGameSession() {
        this.gameSession = new GameSession();
        this.gameSession.initialize();
    }

    @Override
    public void startGameplay() throws InterruptedException {
        if (gameSession != null) {
            gameSession.startGameplay();
        }
    }
}

