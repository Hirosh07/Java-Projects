import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakeGame extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int score;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;

    public SnakeGame() {
        initBoard();
    }

    private void initBoard() {
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        addKeyListener(new TAdapter());

        initGame();
    }

    private void initGame() {
        dots = 3;
        score = 0;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * DOT_SIZE;
            y[z] = 50;
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            g.setColor(Color.green);
            g.fillRect(apple_x, apple_y, DOT_SIZE, DOT_SIZE);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.setColor(Color.red);
                } else {
                    g.setColor(Color.white);
                }
                g.fillRect(x[z], y[z], DOT_SIZE, DOT_SIZE);
            }

            g.setColor(Color.white);
            g.drawString("Score: " + score, 10, 20);

            Toolkit.getDefaultToolkit().sync();

        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        String scoreMsg = "Final Score: " + score;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2 - 10);
        g.drawString(scoreMsg, (B_WIDTH - metr.stringWidth(scoreMsg)) / 2, B_HEIGHT / 2 + 10);

        // Add code to reset the game and start a new game
        inGame = false;
        timer.stop();

        // Show a dialog to ask if the player wants to play again
        int choice = JOptionPane.showConfirmDialog(this, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
            timer.start();
        } else {
            System.exit(0); // Exit the game if the player chooses not to play again
        }
    }

    private void resetGame() {
        dots = 3;
        score = 0;
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * DOT_SIZE;
            y[z] = 50;
        }
        locateApple();
        inGame = true;
    }


    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score++;
            locateApple();
        }
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

