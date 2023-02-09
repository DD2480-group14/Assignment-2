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

/** Provides webview for the CI. */
public class Webview extends AbstractHandler {
    private JSONArray json;
    private PrintWriter res;
    private String header = "<html><head><title>Awesome CI</title><style>"
                    + ".success{color:white;padding:8px;margin:8px;background-color:#3fb950;text-decoration:none;display:inline-block}"
                    + ".failure{color:white;padding:8px;margin:8px;background-color:#f85149;text-decoration:none;display:inline-block}"
                    + "</style></head><body>";
    private String footer = "</body></html>";

    /**
     * For empty path serves the list of all builds, for a path consisting of only a number (like
     * /5), it serves the page with details of build.
     *
     * <p>
     * The data is loaded from builds.json.
     */
    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        try {
            String content = new String(Files.readAllBytes(Paths.get("builds.json")));
            json = new JSONArray(content);
        } catch (IOException e) {
            return;
        } catch (JSONException e) {
            return;
        }

        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        res = response.getWriter();
        res.print(header);

        try {
            String[] url = target.split("/");
            if (target.equals("/")) {
                serveList(baseRequest, request, response);
            } else if (url.length == 2) {
                int buildId;
                try {
                    buildId = Integer.parseInt(url[1]);
                } catch (NumberFormatException e) {
                    return;
                }
                serveBuild(buildId, baseRequest, request, response);
            } else {
                return;
            }
        } catch (JSONException e) {
            return;
        }

        res.print(footer);
        baseRequest.setHandled(true);
    }

    void serveList(Request baseRequest,
                   HttpServletRequest request,
                   HttpServletResponse response) throws JSONException {
        res.print("<h1>Builds:</h1>");
        for (int i = json.length() - 1; i != -1; --i) {
            res.print("<a class=\"");
            JSONObject build = json.getJSONObject(i);
            if (build.getEnum(Builder.Result.class, "status") == Builder.Result.Success) {
                res.print("success");
            } else {
                res.print("failure");
            }
            res.print("\" href=\"/" + i + "/\">");
            res.print("Build #" + i + "</a><br/>");
        }
    }

    void serveBuild(int buildId,
                    Request baseRequest,
                    HttpServletRequest request,
                    HttpServletResponse response) {
        res.print("<a href=\"/\">go back</a>");
        res.print("<h1>Build #" + buildId + "</h1>");
        JSONObject build = json.getJSONObject(buildId);
        res.print("<a href=\"" + build.getString("origin") + "\">go to source</a>");
        res.print("<pre>" + build.getString("log") + "</pre>");
    }
}
