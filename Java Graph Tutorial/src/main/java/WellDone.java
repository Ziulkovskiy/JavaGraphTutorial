import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.microsoft.graph.authentication.BaseAuthenticationProvider;
import com.microsoft.graph.httpcore.HttpClients;
import com.microsoft.graph.info.Constants;
import com.microsoft.graph.models.Drive;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WellDone {
    /** The application ID to use for unit testing */
    public static final String clientId = "36ac5bbb-ce51-4129-8a7e-672254355f66";
    /** The user email to use for unit testing */
    public static final String username = "sovieteagle2017%40gmail.com";
    /** The user password to use for unit testing */
    public static final String password = "reddivision";
    /** The tenant ID to use for unit testing */
    public static final String TENANTID = "ffe5bce8-5a08-415f-b813-b9bc0d79d731";
    /** The client secret to use for unit testing */
    public static final String clientSecret = "AJg7bWlD6z7K.2_A-HvoQudq1Qxh.RKlw_";
    /** The SDK version */
    public static final String VERSION_NAME = "3.6.0";
    private static String grantType = "password";
    private static String tokenEndpoint = "https://login.microsoftonline.com/"+TENANTID +"/oauth2/v2.0/token";
    private static String resourceId = "https%3A%2F%2Fgraph.microsoft.com%2F.default";

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        final OkHttpClient httpClient = HttpClients.createDefault(getAuthenticationProvider());
        GraphServiceClient<Request> graphClient = GraphServiceClient.builder()
                .httpClient(httpClient)
                .buildClient();
        UserCollectionPage users = graphClient.users()
                .buildRequest()
                .get();
        //System.out.println(driveItem);
        //ObjectMapper objectMapper = new ObjectMapper();
        List<String> asJson = new ArrayList<>();
        users.getCurrentPage().stream().forEach(user -> {
            try {
                asJson.add(objectMapper.writeValueAsString(user));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        System.out.println(asJson);
        List<User> aUser = users.getCurrentPage();
        System.out.println(aUser.size());
        for(User oUser : aUser){
            Drive oDrive = oUser.drive;
            System.out.println(oUser.displayName);
            System.out.println(oDrive);
            //oDrive.
        }
    }

    public static BaseAuthenticationProvider getAuthenticationProvider() {
        final String accessToken = GetAccessToken().replace("\"", "");
        System.out.println(accessToken);
        return new BaseAuthenticationProvider() {
            @Override
            public CompletableFuture<String> getAuthorizationTokenAsync(final URL requestUrl) {
                if(this.shouldAuthenticateRequestWithUrl(requestUrl)) {
                    return CompletableFuture.completedFuture(accessToken);
                } else {
                    return CompletableFuture.completedFuture(null);
                }
            }
        };
    }

    private static String GetAccessToken()
    {

        try {
            final URL url = new URL(tokenEndpoint);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String line;
            final StringBuilder jsonString = new StringBuilder();

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            try (final OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
                final String payload = String.format("grant_type=%1$s&scope=%2$s&client_id=%3$s&username=%4$s&password=%5$s&client_secret=%6$s",
                        grantType,
                        resourceId,
                        clientId,
                        username,
                        password,
                        clientSecret);
                writer.write(payload);
                System.out.println(payload);
            }
            try {
                try (final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")))) {
                    while((line = br.readLine()) != null){
                        jsonString.append(line);
                    }
                }
            } catch (Exception e) {
                throw new Error("Error reading authorization response: " + e.getLocalizedMessage());
            }
            conn.disconnect();

            JsonObject res = new GsonBuilder().create().fromJson(jsonString.toString(), JsonObject.class);
            return res.get("access_token").toString().replaceAll("\"", "");

        } catch (Exception e) {
            throw new Error("Error retrieving access token: " + e.getLocalizedMessage());
        }
    }
}
