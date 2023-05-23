import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();

        // query failed
        if (!validateParams(params)) {
            results.put("query_success", false);
            // put arbitrary values to other fields
            results.put("depth", -1);
            results.put("raster_ul_lon", -1);
            results.put("raster_ul_lat", -1);
            results.put("raster_lr_lon", -1);
            results.put("raster_lr_lat", -1);
            results.put("render_grid", null);
            return results;
        }
        // query success
        results.put("query_success", true);

        int depth = calcDepth(params);
        results.put("depth", depth);

        // get the (x, y) for bounding box upper-left tile and lower-right tile.
        int ulX = calcUlX(depth, params);
        int ulY = calcUlY(depth, params);
        int lrX = calcLrX(depth, params);
        int lrY = calcLrY(depth, params);
        // Record the files
        String[][] files = new String[lrY - ulY + 1][lrX - ulX + 1];
        for (int row = ulY; row <= lrY; row++) {
            for (int col = ulX; col <= lrX; col++) {
                files[row - ulY][col - ulX] = "d" + depth + "_x" + col + "_y" + row + ".png";
            }
        }
        results.put("render_grid", files);

        // Record the ullon, ullat, lrlon, lrlat for the bounding box
        double lonPT = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        double latPT = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
        results.put("raster_ul_lon", MapServer.ROOT_ULLON + ulX * lonPT);
        results.put("raster_ul_lat", MapServer.ROOT_ULLAT - ulY * latPT);
        results.put("raster_lr_lon", MapServer.ROOT_ULLON + (lrX + 1) * lonPT);
        results.put("raster_lr_lat", MapServer.ROOT_ULLAT - (lrY + 1) * latPT);
        return results;
    }

    private int calcUlX(int d, Map<String, Double> params) {
        double ullon = params.get("ullon");
        if (ullon < MapServer.ROOT_ULLON) {
            return 0;
        }
        double lonPT = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, d);
        return (int) ((ullon - MapServer.ROOT_ULLON) / lonPT);
    }
    private int calcUlY(int d, Map<String, Double> params) {
        double ullat = params.get("ullat");
        if (ullat > MapServer.ROOT_ULLAT) {
            return 0;
        }
        double latPT = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, d);
        return (int) ((MapServer.ROOT_ULLAT - ullat) / latPT);
    }
    private int calcLrX(int d, Map<String, Double> params) {
        double lrlon = params.get("lrlon");
        if (lrlon > MapServer.ROOT_LRLON) {
            return (int) (Math.pow(2, d) - 1);
        }
        double lonPT = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, d);
        return (int) ((lrlon - MapServer.ROOT_ULLON) / lonPT);
    }
    private int calcLrY(int d, Map<String, Double> params) {
        double lrlat = params.get("lrlat");
        if (lrlat < MapServer.ROOT_LRLAT) {
            return (int) (Math.pow(2, d) - 1);
        }
        double latPT = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, d);
        return (int) ((MapServer.ROOT_ULLAT - lrlat) / latPT);
    }

    /**
     * Calculates the desired depth. If depth > 7, just return 7.
     */
    private int calcDepth(Map<String, Double> params) {
        double userLonDPP = (params.get("lrlon") - params.get("ullon")) / params.get("w");
        int depth = (int) Math.ceil(
                Math.log((MapServer.ROOT_LRLON - MapServer.ROOT_ULLON)
                        / (MapServer.TILE_SIZE * userLonDPP))
                / Math.log(2));
        return Math.min(depth, 7);
    }

    /**
     * Returns true if params is valid, false otherwise.
     */
    private boolean validateParams(Map<String, Double> params) {
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        // Check if QB doesn't make sense.
        if (ullon >= lrlon || ullat <= lrlat) {
            return false;
        }
        // check if QB is completely outside of ROOT
        if (lrlon <= MapServer.ROOT_ULLON || ullon >= MapServer.ROOT_LRLON
            || lrlat >= MapServer.ROOT_ULLAT || ullat <= MapServer.ROOT_LRLAT) {
            return false;
        }
        return true;
    }

}
