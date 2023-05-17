package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Collections;
import java.util.LinkedList;

public class Solver {

    private class SearchNode implements Comparable<SearchNode> {
        private final WorldState state;
        private final int moves;
        private final int h; // heuristic value
        private final SearchNode parent;

        SearchNode(WorldState state, int moves, int h, SearchNode parent) {
            this.state = state;
            this.moves = moves;
            this.h = h;
            this.parent = parent;
        }

        @Override
        public int compareTo(SearchNode rhs) {
            return (moves + h) - (rhs.moves + rhs.h);
        }
    }

    private boolean solved = false;
    private SearchNode goalNode;
    private final LinkedList<WorldState> solutionStates;

    /**
     * Constructor which solves the puzzle, computing everything necessary
     * for moves() and solution() to not have to solve the problem again.
     * Solves the puzzle using the A* algorithm. Assumes a solution exists.
     *
     * @param initial The initial world state
     */
    public Solver(WorldState initial) {
        astar(initial);
        if (!solved) {
            throw new IllegalArgumentException("Puzzle not solvable!");
        }
        solutionStates = new LinkedList<>();
        for (SearchNode tmp = goalNode; tmp != null; tmp = tmp.parent) {
            solutionStates.add(tmp.state);
        }
        Collections.reverse(solutionStates); // from initial to goal
    }

    private void astar(WorldState state) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(state, 0, state.estimatedDistanceToGoal(), null));

        while (!pq.isEmpty()) {
            SearchNode v = pq.delMin();
            if (v.state.isGoal()) {
                solved = true;
                goalNode = v;
                return;
            }
            for (WorldState ws : v.state.neighbors()) {
                if (v.parent == null || !ws.equals(v.parent.state)) {
                    SearchNode w = new SearchNode(ws, v.moves + 1, ws.estimatedDistanceToGoal(), v);
                    pq.insert(w);
                }
            }
        }
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState.
     */
    public int moves() {
        if (!solved) {
            throw new IllegalArgumentException("Puzzle not solvable!");
        }
        return goalNode.moves;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     */
    public Iterable<WorldState> solution() {
        if (!solved) {
            throw new IllegalArgumentException("Puzzle not solvable!");
        }
        return solutionStates;
    }
}
