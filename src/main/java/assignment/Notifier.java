package assignment;

import org.json.JSONObject;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notifier {
    public static JSONObject setJson(JSONObject commitStatus) {
        String testResult = "";
        String state;
        String targetUrl = "";
        String description;
        String context = "";

        if (testResult.equals("buildFailed")) {
            state = "success";
            description = "Succeeded, passed the tests";
        } else if (testResult.equals("testsFailed")) {
            state = "failure";
            description = "Failed, some tests failed";
        } else {
            state = "pending";
            description = "Testing...";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state", state);
        jsonObject.put("target_url", targetUrl);
        jsonObject.put("description", description);
        jsonObject.put("context", context);
        return jsonObject;
    }

    public static boolean setStatus(JSONObject commitStatus) {
        String urlString = "";
        String shaString = "";
        String userString = "";
        String tokenString = "";
        HttpURLConnection connection;
        DataOutputStream dataOutput;

        try {
            URL url = new URL(urlString + shaString + userString + tokenString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-type", "application/vnd.github+json");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(setJson(commitStatus).toString());
            out.flush();
            out.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
