import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.azure.identity.*;
import okhttp3.Request;

import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.Attendee;
import com.microsoft.graph.models.DateTimeTimeZone;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.Event;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.User;
import com.microsoft.graph.models.AttendeeType;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.EventCollectionPage;
import com.microsoft.graph.requests.EventCollectionRequestBuilder;

public class Graph {

    public static void main(String[] args) {
        String resourceId = "https%3A%2F%2Fgraph.microsoft.com%2F.default";
        String clientId = "0f0f5373-14aa-4679-82d3-37609caa8470";
        String username = "andrey@graphjava.onmicrosoft.com";
        String password = "zAq1!xSw2@";
        String client_secret = "tj~tbPMSk82kkv86kxSe-e-57ABfCI_k-3";
        String tenantId ="8442d917-8472-4dc7-8aae-ab698fc91706";
        List<String> scopes = new ArrayList<>();
        scopes.add("https://graph.microsoft.com/.default");

        final UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredentialBuilder()
                .clientId(clientId)
                .username(username)
                .password(password)
                .tenantId(tenantId)
                .build();

        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, usernamePasswordCredential);

        final GraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(tokenCredentialAuthProvider)
                        .buildClient();
        System.out.println("Before build request");

        final User me = graphClient.me().buildRequest().get();
        System.out.println("Before build request");

    }
}