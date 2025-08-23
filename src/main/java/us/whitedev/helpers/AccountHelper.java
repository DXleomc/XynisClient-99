package us.whitedev.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AccountHelper {
    public static final Map<String, String> accountMap = new HashMap<>();
    private final Gson gson = new Gson();
    private final String filePath = System.getenv("APPDATA") + "/.minecraft/accXynisMap.ser";

    public List<String> getAccountsName() {
        return accountMap.keySet().stream().toList();
    }

    public String getToken(String userName) {
        return accountMap.get(userName);
    }

    public void saveAccountsMap() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(accountMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void readAccountsMap() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                Map<String, String> loadedMap = (Map<String, String>) ois.readObject();
                accountMap.clear();
                accountMap.putAll(loadedMap);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void addNewAccount(String username, String password) {
        accountMap.put(username, password);
        saveAccountsMap();
    }

    public void removeAccount(String username) {
        accountMap.remove(username);
        saveAccountsMap();
    }

    public File initFileSelector() {
        FileDialog fileSelector = new FileDialog((Frame) null, "Select Cookie File");
        fileSelector.setMode(FileDialog.LOAD);
        fileSelector.setVisible(true);

        return fileSelector.getFiles()[0];
    }

    public List<String> getCookiesList(File file) {
        try {
            List<String> content = new ArrayList<>();
            Scanner scanner = new Scanner(new FileReader(file));
            while (scanner.hasNextLine()) {
                content.add(scanner.nextLine());
            }
            scanner.close();

            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String extractAuthToken(List<String> cookieEntries) throws IOException {
        Set<String> uniqueCookies = new HashSet<>();
        String cookieString = cookieEntries.stream()
                .map(entry -> entry.split("\t"))
                .filter(elements -> elements[0].endsWith("login.live.com") && uniqueCookies.add(elements[5]))
                .map(elements -> elements[5] + "=" + elements[6])
                .collect(Collectors.joining("; "));

        return fetchAccessToken(cookieString);
    }

    private String parseAuthToken(String authToken) {
        String decodedToken = new String(Base64.getDecoder().decode(authToken), StandardCharsets.UTF_8);
        return decodedToken.split("\"rp://api.minecraftservices.com/\",", 2)[1];
    }

    private String buildXblAuth(String parsedToken) {
        String tokenValue = parsedToken.split("\"Token\":\"")[1].split("\"")[0];
        String userHash = parsedToken.split(Pattern.quote("{\"DisplayClaims\":{\"xui\":[{\"uhs\":\""))[1].split("\"")[0];
        return "XBL3.0 x=" + userHash + ";" + tokenValue;
    }

    private JsonObject authenticateWithXbl(String xblAuth) {
        String requestBody = "{\"identityToken\":\"" + xblAuth + "\",\"ensureLegacyEnabled\":true}";
        String response = sendPostRequest(requestBody);
        return gson.fromJson(response, JsonObject.class);
    }

    private JsonObject fetchProfileData(String mcToken) {
        String profileData = fetchBearerData(mcToken);
        return gson.fromJson(profileData, JsonObject.class);
    }

    private String fetchAccessToken(String cookies) throws IOException {
        String initialUrl = "https://sisu.xboxlive.com/connect/XboxLive/?state=login&cobrandId=8058f65d-ce06-4c30-9559-473c9275a65d&tid=896928775&ru=https%3A%2F%2Fwww.minecraft.net%2Fen-us%2Flogin&aid=1142970254";

        String followRedirect = followRedirect(initialUrl, null, cookies);
        String followRedirect1 = followRedirect(followRedirect, cookies, cookies);
        String location = followRedirect(followRedirect1, cookies, cookies);

        return extractAccessToken(location);
    }

    private String followRedirect(String url, String cookies, String initialCookies) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        setRequestHeaders(connection, cookies != null ? cookies : initialCookies);
        connection.setInstanceFollowRedirects(false);
        connection.connect();

        return connection.getHeaderField("Location").replaceAll(" ", "%20");
    }

    private void setRequestHeaders(HttpsURLConnection connection, String cookies) {
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        connection.setRequestProperty("Accept-Language", "en-GB,en;q=0.9");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        if (cookies != null) {
            connection.setRequestProperty("Cookie", cookies);
        }
    }

    private String extractAccessToken(String url) {
        return url.split("accessToken=")[1];
    }

    private String sendPostRequest(String payload) {
        try {
            HttpsURLConnection conn = createHttpsConnection(payload);
            int statusCode = conn.getResponseCode();
            InputStream responseStream = (statusCode >= 200 && statusCode < 400) ? conn.getInputStream() : conn.getErrorStream();

            if (responseStream == null) return null;

            StringBuilder result = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseStream))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> fetchUserDetails(List<String> cookieEntries) {
        List<String> userInfo = new ArrayList<>();

        if (cookieEntries.isEmpty()) {
            return userInfo;
        }

        try {
            String authToken = extractAuthToken(cookieEntries);
            String parsedToken = parseAuthToken(authToken);
            String xblAuth = buildXblAuth(parsedToken);

            JsonObject dataResp = authenticateWithXbl(xblAuth);

            if (dataResp != null && dataResp.has("access_token")) {
                String mcToken = dataResp.get("access_token").getAsString();
                JsonObject profileResp = fetchProfileData(mcToken);

                if (profileResp != null && profileResp.has("id") && profileResp.has("name")) {
                    userInfo.add(profileResp.get("name").getAsString());
                    userInfo.add(profileResp.get("id").getAsString());
                    userInfo.add(mcToken);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return userInfo;
    }

    private HttpsURLConnection createHttpsConnection(String requestBody) throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection) new URL("https://api.minecraftservices.com/authentication/login_with_xbox").openConnection();
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        byte[] requestData = requestBody.getBytes(StandardCharsets.UTF_8);
        conn.setFixedLengthStreamingMode(requestData.length);
        conn.addRequestProperty("Content-Type", "application/json");
        conn.addRequestProperty("Accept", "application/json");
        conn.connect();

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestData);
        }

        return conn;
    }

    private String fetchBearerData(String bearerToken) {
        try {
            InputStream responseStream = getInputStream(bearerToken);
            if (responseStream == null) return null;

            StringBuilder result = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }
            return result.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static InputStream getInputStream(String bearerToken) throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection) new URL("https://api.minecraftservices.com/minecraft/profile").openConnection();
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        conn.addRequestProperty("Accept", "application/json");
        conn.addRequestProperty("Authorization", "Bearer " + bearerToken);

        return (conn.getResponseCode() == 200) ? conn.getInputStream() : conn.getErrorStream();
    }
}

          
