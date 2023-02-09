package assignment;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Main extends AbstractHandler {
    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        if (request.getMethod().equals("POST")) {
            Webhook wh = new Webhook();
            wh.handle(target, baseRequest, request, response);
        } else if (request.getMethod().equals("GET")) {
            Webview wv = new Webview();
            wv.handle(target, baseRequest, request, response);
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        Server server = new Server(port);
        server.setHandler(new Main());

        server.start();
        server.join();
    }
}
