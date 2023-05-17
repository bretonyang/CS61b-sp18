package lab11.graphs;

/**
 * @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int[] privateEdgeTo;
    private boolean found = false;

    public MazeCycles(Maze m) {
        super(m);
        privateEdgeTo = new int[maze.V()];
        s = 0;
        privateEdgeTo[s] = s;
        distTo[s] = 0;
    }

    @Override
    public void solve() {
        cycleDetect(s, s);
    }

    // Helper methods go here

    // v: current vertex, p: parent of v
    // Run DFS on graph, record edges to privateEdgeTo,
    private void cycleDetect(int v, int p) {
        marked[v] = true;
        announce();
        if (found) {
            return;
        }

        for (int w : maze.adj(v)) {
            if (marked[w] && w != p) {
                // cycle found, and writes cycle to edgeTo[]
                edgeTo[w] = v;
                announce();
                for (int tmp = v; tmp != w; tmp = privateEdgeTo[tmp]) {
                    edgeTo[tmp] = privateEdgeTo[tmp];
                    announce();
                }
                found = true;
                return;
            } else if (!marked[w]) {
                privateEdgeTo[w] = v;
                distTo[w] = distTo[v] + 1;
                announce();
                cycleDetect(w, v);
                if (found) {
                    return;
                }
            }
        }
    }
}

