package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean found = false;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        if (found) {
            return;
        }

        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        distTo[s] = 0;
        edgeTo[s] = s;
        marked[s] = true;
        announce();
        if (s == t) {
            found = true;
            return;
        }

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    distTo[w] = distTo[v] + 1;
                    edgeTo[w] = v;
                    queue.add(w);
                    announce();
                    if (w == t) {
                        found = true;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        bfs();
    }
}

