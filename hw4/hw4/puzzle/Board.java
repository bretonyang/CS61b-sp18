package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;

public class Board implements WorldState {

    private int N;
    private int[][] tiles;

    /**
     * Constructs a board from an N-by-N array of tiles
     * where tiles[i][j] = tile at row i, column j
     */
    public Board(int[][] t) {
        N = t.length;
        tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = t[i][j];
            }
        }
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     */
    public int tileAt(int i, int j) {
        if (invalidIndex(i, j)) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        return tiles[i][j];
    }

    /**
     * Returns the board size N
     */
    public int size() {
        return N;
    }

    /**
     * Returns the neighbors of the current board
     */
    @Override
    public Iterable<WorldState> neighbors() {
        ArrayList<WorldState> neighbors = new ArrayList<>();
        int Bi = 0, Bj = 0;
        int[][] tmpTiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tmpTiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    Bi = i;
                    Bj = j;
                }
            }
        }
        // up, left, right, down
        int[] dx = new int[]{-1, 0, 0, 1};
        int[] dy = new int[]{0, -1, 1, 0};
        for (int i = 0; i < 4; i++) {
            int newBi = Bi + dx[i];
            int newBj = Bj + dy[i];
            if (!invalidIndex(newBi, newBj)) {
                tmpTiles[Bi][Bj] = tmpTiles[newBi][newBj];
                tmpTiles[newBi][newBj] = 0;
                neighbors.add(new Board(tmpTiles));
                tmpTiles[newBi][newBj] = tmpTiles[Bi][Bj];
                tmpTiles[Bi][Bj] = 0;
            }
        }
        return neighbors;
    }

    /**
     * Hamming estimate described below
     */
    public int hamming() {
        int ham = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                int v = tileAt(i, j);
                int expected = size() * i + j + 1;
                if (v != 0 && v != expected) {
                    ham++;
                }
            }
        }
        return ham;
    }

    /**
     * Manhattan estimate described below
     */
    public int manhattan() {
        int man = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                int v = tileAt(i, j);
                if (v != 0) {
                    int expectedI = (v - 1) / size();
                    int expectedJ = (v - 1) % size();
                    man += Math.abs(i - expectedI) + Math.abs(j - expectedJ);
                }
            }
        }
        return man;
    }

    /**
     * Estimated distance to goal. This method simply return the
     * results of manhattan() when submitted to Gradescope.
     */
    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * Returns true if this board's tile values are the same position as o's
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        Board other = (Board) o;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tileAt(i, j) != other.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the string representation of the board.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    private boolean invalidIndex(int i, int j) {
        return i < 0 || i >= N || j < 0 || j >= N;
    }

}
