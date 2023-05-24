import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.NoSuchElementException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /**
     * Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc.
     */

    // Maps from an id to a node
    private Map<Long, Node> nodes = new HashMap<>();
    // Maps from an edge (v, w) to its way name. Note that Pair is asymmetric.
    private Map<Pair, String> edges = new HashMap<>();
    // Trie for auto complete
    private Trie trie = new Trie();
    // Maps from a "cleaned" name to a list of locations
    private Map<String, List<Location>> locationsByName = new HashMap<>();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    void addLocation(long id, double lon, double lat, String name) {
        String cleanedName = cleanString(name);
        if (!locationsByName.containsKey(cleanedName)) {
            locationsByName.put(cleanedName, new LinkedList<>());
        }
        locationsByName.get(cleanedName).add(new Location(id, lon, lat, name));
    }

    List<Map<String, Object>> getLocations(String name) {
        name = cleanString(name);
        List<Map<String, Object>> result = new LinkedList<>();
        if (locationsByName.containsKey(name)) {
            for (Location location : locationsByName.get(name)) {
                result.add(location.getMap());
            }
        }
        return result;
    }

    void addWordToTrie(String word) {
        String cleanWord = cleanString(word);
        trie.addWord(cleanWord, word);
    }

    List<String> getLocationsByPrefix(String key) {
        return trie.wordsWithPrefix(cleanString(key));
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * The original string is not modified.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Set<Long> nodesToRemove = new HashSet<>();
        for (long v : vertices()) {
            if (nodes.get(v).neighbors.isEmpty()) {
                nodesToRemove.add(v);
            }
        }
        for (long v : nodesToRemove) {
            nodes.remove(v);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return nodes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     *
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        if (!nodes.containsKey(v)) {
            throw new NoSuchElementException("Node v is not in graph!");
        }
        return nodes.get(v).neighbors;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point. In other words, the initial bearing is the angle between the
     * direction to North and the direction along path (v, w). And the angle is
     * increased in clockwise direction.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long closestNode = -1;
        double closestDist = Double.POSITIVE_INFINITY;
        for (long v : vertices()) {
            double dist = distance(lon(v), lat(v), lon, lat);
            if (dist < closestDist) {
                closestDist = dist;
                closestNode = v;
            }
        }
        return closestNode;
    }

    /**
     * Gets the longitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        if (!nodes.containsKey(v)) {
            throw new NoSuchElementException("Node v is not in graph!");
        }
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        if (!nodes.containsKey(v)) {
            throw new NoSuchElementException("Node v is not in graph!");
        }
        return nodes.get(v).lat;
    }

    /**
     * Gets the name of the edge between (v, w).
     *
     * @param v id of first vertex
     * @param w id of second vertex
     * @return edge name of (v, w)
     */
    String getEdgeName(long v, long w) {
        Pair key = new Pair(v, w);
        if (!edges.containsKey(key)) {
            throw new NoSuchElementException("Edge (v, w) does not exist");
        }
        return edges.get(key);
    }

    /* Helper Methods */
    void addNode(long id, double lon, double lat) {
        Node n = new Node(id, lon, lat);
        nodes.put(id, n);
    }

    void addEdge(long v, long w, String name) {
        nodes.get(v).neighbors.add(w);
        nodes.get(w).neighbors.add(v);
        edges.put(new Pair(v, w), name);
        edges.put(new Pair(w, v), name);
    }

    private static class Node {
        private final long id;
        private final double lon;
        private final double lat;
        private LinkedList<Long> neighbors;

        Node(long id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
            neighbors = new LinkedList<>();
        }
    }

    private static class Pair {
        private final long v;
        private final long w;

        Pair(long v, long w) {
            this.v = v;
            this.w = w;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Pair other = (Pair) obj;
            return v == other.v && w == other.w;
        }

        @Override
        public int hashCode() {
            /// This hashCode algo is from "Effective Java"
            int result = 1;
            result = 31 * result + (int) (v ^ (v >>> 32));
            result = 31 * result + (int) (w ^ (w >>> 32));
            return result;
        }
    }

    static class Location {
        private Map<String, Object> info;

        Location(long id, double lon, double lat, String name) {
            info = new HashMap<>();
            info.put("id", id);
            info.put("lon", lon);
            info.put("lat", lat);
            info.put("name", name);
        }

        public Map<String, Object> getMap() {
            return info;
        }
    }

}
