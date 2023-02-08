
package assignment;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


public class Webview extends AbstractHandler {


    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {

        // Load JSON variables


        JSONArray jsonBuilds;
        String[][] builds;

        try {
            String content = new String(Files.readAllBytes(Paths.get("builds.json")));
            jsonBuilds = new JSONArray(content);
        } catch (IOException e) {
            System.out.println("Unable to read and parse JSON from build.json. IO");
            return;
        } catch (JSONException e) {
            System.out.println("Unable to read and parse JSON from build.json. JSON");
            return;
        }
        try {
            builds = new String[jsonBuilds.length()][4]; // assuming it
            for (int i = 0; i != jsonBuilds.length(); ++i) {
                JSONObject build = jsonBuilds.getJSONObject(i);
                builds[i][0] = build.getString("id");
                builds[i][1] = build.getString("origin");
                builds[i][2] = build.getString("status");
                builds[i][3] = build.getString("log");
            }
        } catch (JSONException e) {
            System.out.println("Unable to load POINTS from builds.json.");

            return;
        }
        // Declare response encoding and types
        response.setContentType("text/html; charset=utf-8");

        // Declare response status code
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter res = response.getWriter();
        String html = "<!DOCTYPE html>" + "\n<html>" + "\n<head>" + "\n<style>"
                        + "\n.center {text-align: center;}"
                        + "\n.pagination {display: inline-block;}"
                        + "\n.pagination a {color: black;float: left;padding: 8px 16px;text-decoration: none;}"
                        + "\n.pagination a.active {background-color: paleturquoise;color: plum;border: 1px solid paleturquoise;}"
                        + "\n.pagination a:hover:not(.active) { background-color: lightgreen;}"
                        + "\n</style>" + "\n<h1>Builds and Requests</h1>" + "\n</head>"
                        + "\n <body>";
        res.println(html);
        // Making a build

        // Making a box and filling it in with build information

        for (int i = 0; i < builds.length; i++) {
            res.println("\n<br>" + "\n<label for=\"freeform\">Build " + (i + 1) + "</label>"
                            + "\n<br>"
                            + "\n<textarea id=\"freeform\" name=\"freeform\" rows=\"5\" cols=\"110\">"
                            + "\nid: " + builds[i][0] + "\norigin: " + builds[i][1] + "\nstatus: "
                            + builds[i][2] + "\nlog: " + builds[i][3] + "\n</textarea>" + "\n<br>");
        }


        // pagination
        /*
         * int NumberofPaginations = Math.ceil(builds.length / 10);
         * res.println("\n<h2>More Pages</h2> " + "\n<div class =\"pagination\">" +
         * "\n<div class =\"pagination\"><a href = \"#\">&laquo;</a>"); for (int j = 0; j <
         * NumberofPaginations; j++) { res.println("\n<a href=\"#\">" + j + 1 + "</a>"); }
         * res.println("\n<a href=\"#\">&raquo;</a>"); res.println("\n<a href=\"#\">&raquo;</a>" +
         * "\n</div>" + "\n</body>" + "\n</html>");
         * 
         */
        baseRequest.setHandled(true);

        // Using requestcount we can decide number of paginations



    }



}

// res.println(style)
// res.println
