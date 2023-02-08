package assignment;

import org.json.JSONObject;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notifier {
    public static JSONObject getJson(String testResult) {
        String state;
        String targetUrl = "";
        String description;
        String context = "DD2480 Group14 CI";

        if (testResult.equals("succeeded")) {
            state = "success";
            description = "Succeeded, passed the tests";
        } else if (testResult.equals("buildFailed")) {
            state = "failure";
            description = "Failed, compilation failed";
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

    public static boolean sentStatus(String commitHash, String testResult) {
        String urlString = "https://github.com/DD2480-group14/Assignment-2";
        String tokenString = "d9ffe565c130a12b63833547aede6d893796eba4";

        try {
            URL url = new URL(urlString + commitHash + tokenString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Accept", "application/vnd.github+json");
            connection.setRequestProperty("Authorization", "application/vnd.github+json");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(getJson(testResult).toString());
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
