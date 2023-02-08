package assignment;

import java.io.IOException;
import java.io.BufferedReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONObject;
import org.json.JSONException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Webhook extends AbstractHandler {
    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        BufferedReader body = request.getReader();
        String content = "";
        for (String line = body.readLine(); line != null; line = body.readLine()) {
            content = content + line + "\n";
        }
        String expectedHash = request.getHeader("X-Hub-Signature-256");
        if (!hash(content).equals(expectedHash)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            baseRequest.setHandled(true);
            return;
        }
        String commitHash;
        try {
            JSONObject json = new JSONObject(content);
            commitHash = json.getJSONObject("head_commit").getString("id");
        } catch (JSONException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            baseRequest.setHandled(true);
            return;
        }
        try {
            Builder.build(commitHash);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            baseRequest.setHandled(true);
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
    }

    static String hash(String data) {
        try {
            final String key = "TODO: load key from file/env";
            final String alg = "HmacSHA256";
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), alg);
            Mac mac = Mac.getInstance(alg);
            mac.init(secretKeySpec);
            return bytesToHex(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            return "";
        }
    }

    static String bytesToHex(byte[] bytes) {
        String result = "";
        for (byte b : bytes) {
            result = result + String.format("%02X", b);
        }
        return result;
    }
}
