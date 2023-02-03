package assignment;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Main extends AbstractHandler {
    private String requests = "";
    private int requestsCount = 0;

    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        // Declare response encoding and types
        response.setContentType("text/html; charset=utf-8");

        // Declare response status code
        response.setStatus(HttpServletResponse.SC_OK);

        // Write back response
        PrintWriter res = response.getWriter();
        res.println(requests);
        BufferedReader body = request.getReader();
        String buf = "<h1>Request #" + requestsCount + "</h1><pre>";
        for (String line = body.readLine(); line != null; line = body.readLine()) {
            buf = buf + line + "\n";
        }
        buf = buf + "</pre>";
        requests = buf + requests;
        ++requestsCount;

        // Inform jetty that this request has now been handled
        baseRequest.setHandled(true);
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        Server server = new Server(port);
        server.setHandler(new Main());

        server.start();
        server.join();
    }
}
