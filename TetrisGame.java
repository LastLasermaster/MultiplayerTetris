package core;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class TetrisGame extends JFrame {

    private JLabel statusbar;

    public TetrisGame() {

        initUI();
    }

    private void initUI() {

        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);

        Board board = new Board(this);
        add(board);
        board.start();
        /*
        Board board2 = new Board(this);
        add(board2);
        board2.start();
        */
        setTitle("Tetris");
        setSize(200, 400);
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
            TetrisGame game2 = new TetrisGame();
            game2.setVisible(true);
        });
    }
}