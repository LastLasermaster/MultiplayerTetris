package core;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import core.Shape.TetrisBlocks;


public class TetrisGame extends JFrame {

    private JLabel statusbar;
    
    private Board boardOne;
    
    private Board boardTwo;
    
    private boolean isPaused = false;
    
    final int PERIOD_INTERVAL = 300;

    public TetrisGame() {

        initUI();
    }

    private void initUI() {

        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);

        Board boardOne = new Board(this);
        this.getContentPane().add(boardOne, BorderLayout.CENTER);
        this.boardOne = boardOne;
        boardOne.setSize(200, 400);
        System.out.println(boardOne.getSize());
        boardOne.addKeyListener(new KeyPresses());
        
        Board boardTwo = new Board(this);
        this.getContentPane().add(boardTwo, BorderLayout.CENTER);
        this.boardTwo = boardTwo;
        boardTwo.setSize(200, 400);
        boardTwo.addKeyListener(new KeyPresses());
        
        start();


        setTitle("Tetris");
        setSize(220, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setLocationRelativeTo(null);
    }

    JLabel getStatusBar() {

        return statusbar;
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

        	TetrisGame game = new TetrisGame();
            game.setVisible(true);
        });
    }
    
    void start() {

        boardOne.curPiece = new Shape();
        boardOne.board = new TetrisBlocks[boardOne.BOARD_WIDTH * boardOne.BOARD_HEIGHT];

        boardOne.clearBoard();
        boardOne.newPiece();
        
        boardTwo.curPiece = new Shape();
        boardTwo.board = new TetrisBlocks[boardOne.BOARD_WIDTH * boardOne.BOARD_HEIGHT];

        boardTwo.clearBoard();
        boardTwo.newPiece();

        Timer timer = new Timer(PERIOD_INTERVAL, new GameCycle());
        timer.start();
    }

    private void pause() {

        isPaused = !isPaused;

        if (isPaused) {

            statusbar.setText("paused");
        }

        repaint();
    }
    
    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private void doGameCycle() {

        boardOne.update(isPaused);
        boardTwo.update(isPaused);
        boardOne.repaint();
        boardTwo.repaint();
    }
    
    class KeyPresses extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            if (boardOne.curPiece.getShape() == TetrisBlocks.NoShape) {

                return;
            }

            int keycode = e.getKeyCode();

            switch (keycode) {

                case KeyEvent.VK_P: pause();break;
                case KeyEvent.VK_LEFT: boardOne.tryMove(boardOne.curPiece, boardOne.curX - 1, boardOne.curY);break;
                case KeyEvent.VK_RIGHT: boardOne.tryMove(boardOne.curPiece, boardOne.curX + 1, boardOne.curY);break;
                case KeyEvent.VK_DOWN: boardOne.tryMove(boardOne.curPiece.rotateRight(), boardOne.curX, boardOne.curY);break;
                case KeyEvent.VK_UP: boardOne.tryMove(boardOne.curPiece.rotateLeft(), boardOne.curX, boardOne.curY);break;
                case KeyEvent.VK_0: boardOne.dropDown();break;
                case KeyEvent.VK_A: boardTwo.tryMove(boardTwo.curPiece, boardTwo.curX - 1, boardTwo.curY);break;
                case KeyEvent.VK_D: boardTwo.tryMove(boardTwo.curPiece, boardTwo.curX + 1, boardTwo.curY);break;
                case KeyEvent.VK_S: boardTwo.tryMove(boardTwo.curPiece.rotateRight(), boardTwo.curX, boardTwo.curY);break;
                case KeyEvent.VK_W: boardTwo.tryMove(boardTwo.curPiece.rotateLeft(), boardTwo.curX, boardTwo.curY);break;
                case KeyEvent.VK_SPACE: boardTwo.dropDown();break;
            }
        }
    }
}