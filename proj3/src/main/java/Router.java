
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {

    private static boolean found;

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     *
     * @param g       The graph to use.
     * @param stlon   The longitude of the start location.
     * @param stlat   The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long start = g.closest(stlon, stlat);
        long dest = g.closest(destlon, destlat);

        found = false;
        Map<Long, Long> edgeTo = astar(g, start, dest);

        List<Long> spt = new LinkedList<>();
        if (found) {
            for (long cur = dest; cur != start; cur = edgeTo.get(cur)) {
                spt.add(cur);
            }
            spt.add(start);
            Collections.reverse(spt);
        }
        return spt;
    }

    private static Map<Long, Long> astar(GraphDB graph, long src, long target) {
        Map<Long, Long> edgeTo = new HashMap<>();
        Map<Long, Double> distTo = new HashMap<>();
        PriorityQueue<PQNode> pq = new PriorityQueue<>();
        Set<Long> marked = new HashSet<>();

        edgeTo.put(src, src);
        distTo.put(src, 0.0);
        pq.add(new PQNode(src, graph.distance(src, target)));

        while (!pq.isEmpty()) {
            long v = pq.poll().id;
            if (marked.contains(v)) {
                continue;
            }
            if (v == target) {
                found = true;
                break;
            }

            marked.add(v);
            for (long w : graph.adjacent(v)) {
                double curDist = distTo.get(v) + graph.distance(v, w);
                if (distTo.get(w) == null || curDist < distTo.get(w)) {
                    distTo.put(w, curDist);
                    edgeTo.put(w, v);
                    pq.add(new PQNode(w, curDist + graph.distance(w, target)));
                }
            }
        }
        return edgeTo;
    }

    private static class PQNode implements Comparable<PQNode> {
        private final long id;
        private final double priority;

        PQNode(long id, double priority) {
            this.id = id;
            this.priority = priority;
        }

        @Override
        public int compareTo(PQNode o) {
            if (priority < o.priority) {
                return -1;
            } else if (priority > o.priority) {
                return 1;
            }
            return 0;
        }
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     *
     * @param g     The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigationDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        // Edge case: return empty list if no edges in route
        if (route.size() < 2) {
            return new LinkedList<>();
        }

        // Normal case: at least 2 vertices in route

        // convert List to ArrayList for constant time get() method
        ArrayList<Long> arrayRoute = new ArrayList<>(route);
        List<NavigationDirection> result = new LinkedList<>();

        // create the starting navigation (current navigation)
        NavigationDirection curNav = new NavigationDirection();
        curNav.direction = NavigationDirection.START;
        curNav.way = getWay(g, arrayRoute.get(0), arrayRoute.get(1));
        curNav.distance += g.distance(arrayRoute.get(0), arrayRoute.get(1));

        // Iterate through the route, keeping track of the prev, cur, and next nodes.
        for (int i = 1; i < arrayRoute.size() - 1; i++) {
            long prevNode = arrayRoute.get(i - 1);
            long curNode = arrayRoute.get(i);
            long nextNode = arrayRoute.get(i + 1);
            // Note: curWay is (prev, cur) and nextWay is (cur, next)
            String nextWay = getWay(g, curNode, nextNode);

            if (!curNav.way.equals(nextWay)) {
                result.add(curNav);
                curNav = new NavigationDirection();
                curNav.way = nextWay;
                curNav.distance = g.distance(curNode, nextNode);

                // Calculate bearings for (prev, cur) and (cur, next) ways.
                double curBearing = g.bearing(prevNode, curNode);
                double nextBearing = g.bearing(curNode, nextNode);
                curNav.direction = NavigationDirection.getDirection(curBearing, nextBearing);
            } else {
                curNav.distance += g.distance(curNode, nextNode);
            }
        }

        // Add last way to target
        result.add(curNav);
        return result;
    }

    private static String getWay(GraphDB g, long v, long w) {
        String way = g.getEdgeName(v, w);
//        return way == null ? NavigationDirection.UNKNOWN_ROAD : way;
        return way == null ? "" : way; // Somehow the AG requires "" instead of UNKNOWN_ROAD
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /**
         * Integer constants representing directions.
         */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /**
         * Number of directions supported.
         */
        public static final int NUM_DIRECTIONS = 8;

        /**
         * A mapping of integer values to directions.
         */
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /**
         * Default name for an unknown way.
         */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /**
         * The direction a given NavigationDirection represents.
         */
        int direction;
        /**
         * The name of the way I represent.
         */
        String way;
        /**
         * The distance along this way I represent.
         */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        private static int getDirection(double curBearing, double nextBearing) {
            // phi is the directed angle from current path to next path.
            // (clockwise direction is positive, which means a right turn)
            // The below calculation might not be correct for some edge cases,
            // so we need to fix it by the following if statements.
            double phi = nextBearing - curBearing;

            // Transform edge case angles into angle directed clockwise from direction
            // of current path to next path.
            // See Notability proj3 notes for more detail.
            if (phi < -180) {
                phi += 360;
            } else if (phi > 180) {
                phi -= 360;
            }

            if (phi < -100) {
                return SHARP_LEFT;
            } else if (phi < -30) {
                return LEFT;
            } else if (phi < -15) {
                return SLIGHT_LEFT;
            } else if (phi < 15) {
                return STRAIGHT;
            } else if (phi < 30) {
                return SLIGHT_RIGHT;
            } else if (phi < 100) {
                return RIGHT;
            } else {
                return SHARP_RIGHT;
            }
        }

        private static boolean isRight(double cur, double next) {
            return false;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         *
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                        && way.equals(((NavigationDirection) o).way)
                        && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
