package core;
import core.Shape.TetrisBlocks;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel {

    final int BOARD_WIDTH = 10;
    final int BOARD_HEIGHT = 22;
    private Timer timer;
    private boolean isFallingFinished = false;
    int numLinesRemoved = 0;
    int curX = 0;
    int curY = 0;
    private JLabel statusbar;
    Shape curPiece;
    TetrisBlocks[] board;

    public Board(TetrisGame parent) {

        initBoard(parent);
    }

    private void initBoard(TetrisGame parent) {

        setFocusable(true);
        statusbar = parent.getStatusBar();
    }

    private int squareWidth() {
    	//System.out.println(this.getSize().getWidth());
        return (int) 200 / BOARD_WIDTH;
    }

    private int squareHeight() {

        return (int) 400 / BOARD_HEIGHT;
    }

    private TetrisBlocks shapeAt(int x, int y) {

        return board[(y * BOARD_WIDTH) + x];
    }
    
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for (int i = 0; i < BOARD_HEIGHT; i++) {

            for (int j = 0; j < BOARD_WIDTH; j++) {

            	TetrisBlocks shape = shapeAt(j, BOARD_HEIGHT - i - 1);

                if (shape != TetrisBlocks.NoShape) {

                    drawSquare(g, j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
                }
            }
        }

        if (curPiece.getShape() != TetrisBlocks.NoShape) {

            for (int i = 0; i < 4; i++) {

                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);

                drawSquare(g, x * squareWidth(),
                        boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                        curPiece.getShape());
            }
        }
    }

    void dropDown() {

        int newY = curY;

        while (newY > 0) {

            if (!tryMove(curPiece, curX, newY - 1)) {

                break;
            }

            newY--;
        }

        pieceDropped();
    }

    void oneLineDown() {

        if (!tryMove(curPiece, curX, curY - 1)) {

            pieceDropped();
        }
    }

    void clearBoard() {

        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {

            board[i] = TetrisBlocks.NoShape;
        }
    }

    private void pieceDropped() {

        for (int i = 0; i < 4; i++) {

            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }

        removeFullLines();

        if (!isFallingFinished) {

            newPiece();
        }
    }

    void newPiece() {

        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {

            curPiece.setShape(TetrisBlocks.NoShape);
            timer.stop();

            String msg = String.format("Game over. Score: %d", numLinesRemoved);
            statusbar.setText(msg);
        }
    }

    boolean tryMove(Shape newPiece, int newX, int newY) {

        for (int i = 0; i < 4; i++) {

            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {

                return false;
            }

            if (shapeAt(x, y) != TetrisBlocks.NoShape) {

                return false;
            }
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;

        repaint();

        return true;
    }

    private void removeFullLines() {

        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {

            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; j++) {

                if (shapeAt(j, i) == TetrisBlocks.NoShape) {

                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {

                numFullLines++;

                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0) {

            numLinesRemoved += numFullLines;

            statusbar.setText(String.valueOf(numLinesRemoved));
            isFallingFinished = true;
            curPiece.setShape(TetrisBlocks.NoShape);
        }
    }

    private void drawSquare(Graphics g, int x, int y, TetrisBlocks shape) {

        Color colors[] = {new Color(0, 0, 0), new Color(204, 102, 102),
                new Color(102, 204, 102), new Color(102, 102, 204),
                new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0)
        };

        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }



    public void update(boolean isPaused) {

        if (isPaused) {

            return;
        }

        if (isFallingFinished) {

            isFallingFinished = false;
            newPiece();
        } else {

            oneLineDown();
        }
    }
}
