package TastyMeeting.utils;

import TastyMeeting.data.BearerToken;
import TastyMeeting.data.User;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rafal on 5/4/16.
 */
public class OauthTokenHandlerSingleton {
    private static OauthTokenHandlerSingleton oauthTokenHandlerSingleton = new OauthTokenHandlerSingleton();
    private final String serverLoginUrl = "http://localhost:8080/oauth/token";

    private OauthTokenHandlerSingleton() {

    }

    public static OauthTokenHandlerSingleton getInstance() {
        return oauthTokenHandlerSingleton;
    }

    public BearerToken getBearerTokenForUser(User user) throws IOException {
        Map<String, String> userData = prepareLoginData(user);
        HttpURLConnection connection = tryToOpenConnectionAndReturnIt(userData);
        String json = readStream(connection.getInputStream());
        BearerToken bearerToken = createBearerTokenFromJson(json);
        return bearerToken;
    }

    private BearerToken createBearerTokenFromJson(String json) {
        Gson gson = new Gson();
        BearerToken bearerToken = gson.fromJson(json, BearerToken.class);
        return bearerToken;
    }

    private Map<String, String> prepareLoginData(User user) {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", user.getEmail());
        loginData.put("password", user.getPassword());
        loginData.put("grant_type", "password");
        loginData.put("scope", "read write");
        loginData.put("client_id", "clientapp");
        loginData.put("client_secret", "123456");
        return loginData;
    }

    private HttpURLConnection tryToOpenConnectionAndReturnIt(Map<String, String> loginData)
            throws IOException {
        URL url = new URL(serverLoginUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Basic Y2xpZW50YXBwOjEyMzQ1Ng==");
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(loginData));
        writer.flush();
        writer.close();
        System.out.println(conn.getResponseCode());
        return conn;
    }

    private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private String readStream(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }
}
