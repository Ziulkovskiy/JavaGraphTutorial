import com.azure.identity.AuthorizationCodeCredential;
import com.azure.identity.AuthorizationCodeCredentialBuilder;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.http.BaseRequest;
import com.microsoft.graph.httpcore.HttpClients;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.Drive;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.DriveCollectionPage;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;
import okhttp3.OkHttpClient;
import sun.net.www.http.HttpClient;

import javax.naming.ServiceUnavailableException;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {
    public static void main(String[] args) throws IOException {
        String resourceId = "https%3A%2F%2Fgraph.microsoft.com%2F.default";
        String clientId = "0f0f5373-14aa-4679-82d3-37609caa8470";
        String username = "sovieteagle2017@gmail.com";
        String password = "reddivision";
        String client_secret = "tj~tbPMSk82kkv86kxSe-e-57ABfCI_k-3";
        String tenantId ="8442d917-8472-4dc7-8aae-ab698fc91706";

        /////Second registration SomeChange
        //String resourceId = "https%3A%2F%2Fgraph.microsoft.com%2F.default";
        //String username = "sovieteagle2017@gmail.com";
        //String password = "reddivision";
        /*String clientId = "66e431ef-f50c-4efd-9aa3-efdafb0692eb";
        String client_secret = "2fYyD4_r.I-~4h.ZxkB6ht_E91Hj_f3S1P";
        String tenantId ="8442d917-8472-4dc7-8aae-ab698fc91706";*/

        ObjectMapper objectMapper = new ObjectMapper();
        //System.out.println("Before clientSecretCredential");

        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(client_secret)
                .tenantId(tenantId)
                .build();
        List<String> scopes = new ArrayList<>();
        scopes.add("https://graph.microsoft.com/.default");
        //System.out.println("After clientSecretCredential");

        final TokenCredentialAuthProvider tokenCredAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);
       /* final OkHttpClient httpClient = HttpClients.createDefault(tokenCredAuthProvider)
                .newBuilder()
                .followSslRedirects(false) // sample configuration to apply to client
                .build();*/
        //System.out.println("After tokenCredAuthProvider");
        final GraphServiceClient graphClient = GraphServiceClient
                .builder()
                .authenticationProvider(tokenCredAuthProvider)
                //.httpClient(httpClient) //Пока не понятна разница с элементов выше
                .buildClient();
        graphClient.getLogger().setLoggingLevel(LoggerLevel.DEBUG);
        //System.out.println("After graphClient");
        /*final User me = graphClient.me().buildRequest().get();
        System.out.println("ME " + me);*/
        UserCollectionPage users = graphClient.users()
                .buildRequest()
                .get();

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
        System.out.println("Drive " + graphClient.drive().buildRequest().toString());
        //System.out.println("Drives " + graphClient.drives().count().buildRequest().get());
        //System.out.println("Directory" + graphClient.directory().buildRequest().get());// Не работает
        System.out.println("SDK " + graphClient.getServiceSDKVersion());
        System.out.println("ServiceRoot " + graphClient.getServiceRoot());
        /*DriveItem driveItem = graphClient.me().drive().root()
                .buildRequest()
                .get();
        System.out.println(driveItem);*/
      DriveCollectionPage drive = graphClient.users("2873d171c-8145-4586-b351-5dbea7fcf3a0").drives()
                .buildRequest()
                .get();
        System.out.println(drive);

        //System.out.println(objectMapper.writeValueAsString(graphClient.drive().items().buildRequest().get()));
    }
}
