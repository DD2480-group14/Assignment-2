package assignment;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONObject;
import org.json.JSONException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.io.IOUtils;

public class Webhook extends AbstractHandler {
    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        byte[] content = IOUtils.toByteArray(request.getInputStream());
        String expectedHash = request.getHeader("X-Hub-Signature-256");
        if (!("sha256=" + hash(content)).equals(expectedHash)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            baseRequest.setHandled(true);
            return;
        }
        String commitHash;
        try {
            JSONObject json = new JSONObject(new String(content));
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

    static String hash(byte[] data) {
        try {
            final String key = "secret";
            final String alg = "HmacSHA256";
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), alg);
            Mac mac = Mac.getInstance(alg);
            mac.init(secretKeySpec);
            return bytesToHex(mac.doFinal(data));
        } catch (Exception e) {
            return "";
        }
    }

    static String bytesToHex(byte[] bytes) {
        String result = "";
        for (byte b : bytes) {
            result = result + String.format("%02x", b);
        }
        return result;
    }
}
