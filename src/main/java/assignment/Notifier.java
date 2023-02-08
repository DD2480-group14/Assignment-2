package assignment;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notifier {
    static String makeJson(Builder.Result status) {
        String state = "failure";
        String description = "";
        if (status == Builder.Result.Success) {
            state = "success";
            description = "Success!";
        } else if (status == Builder.Result.FailCompile) {
            description = "Failed compilation.";
        } else if (status == Builder.Result.FailTest) {
            description = "Failed testing.";
        } else if (status == Builder.Result.FailVerify) {
            description = "Failed additional checks.";
        }
        return "{\"state\":\"" + state
                        + "\",\"target_url\":\"https://ci.veresov.pro/\",\"description\":\""
                        + description + "\",\"context\":\"Awesome CI\"}";
    }

    public static void sendStatus(String token,
                                  String commitHash,
                                  Builder.Result status) throws Exception {
        String baseUrl = "https://api.github.com/repos/DD2480-group14/Assignment-2/statuses/";
        URL url = new URL(baseUrl + commitHash);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/vnd.github+json");
        connection.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");
        connection.setRequestProperty("Authorization", "Bearer" + token);
        connection.setDoOutput(true);
        OutputStream out = connection.getOutputStream();
        out.write(makeJson(status).getBytes());
        out.flush();
        out.close();
    }
}
