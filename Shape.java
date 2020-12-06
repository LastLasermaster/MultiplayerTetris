package core;

import java.util.Random;

public class Shape {

    protected enum TetrisBlocks {
        NoShape, ZShape, SShape, LineShape,
        TShape, SquareShape, LShape, MirroredLShape
    }

    private TetrisBlocks pieceShape;
    private int[][] coords;

    public Shape() {

        coords = new int[4][2];
        setShape(TetrisBlocks.NoShape);
    }

    void setShape(TetrisBlocks shape) {

        int[][][] coordsTable = new int[][][]{
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}}
        };

        for (int i = 0; i < 4; i++) {

            System.arraycopy(coordsTable[shape.ordinal()], 0, coords, 0, 4);
        }

        pieceShape = shape;
    }

    private void setX(int index, int x) {

        coords[index][0] = x;
    }

    private void setY(int index, int y) {

        coords[index][1] = y;
    }

    int x(int index) {

        return coords[index][0];
    }

    int y(int index) {

        return coords[index][1];
    }

    TetrisBlocks getShape() {

        return pieceShape;
    }

    void setRandomShape() {

        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;

        TetrisBlocks[] values = TetrisBlocks.values();
        setShape(values[x]);
    }

    public int minX() {

        int m = coords[0][0];

        for (int i = 0; i < 4; i++) {

            m = Math.min(m, coords[i][0]);
        }

        return m;
    }


    int minY() {

        int m = coords[0][1];

        for (int i = 0; i < 4; i++) {

            m = Math.min(m, coords[i][1]);
        }

        return m;
    }

    Shape rotateLeft() {

        if (pieceShape == TetrisBlocks.SquareShape) {

            return this;
        }

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; i++) {

            result.setX(i, y(i));
            result.setY(i, -x(i));
        }

        return result;
    }

    Shape rotateRight() {

        if (pieceShape == TetrisBlocks.SquareShape) {

            return this;
        }

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; i++) {

            result.setX(i, -y(i));
            result.setY(i, x(i));
        }

        return result;
    }
}