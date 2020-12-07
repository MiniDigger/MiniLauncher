package me.minidigger.minecraftlauncher.launcher.gui;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.to2mbn.jmccc.auth.AuthInfo;
import org.to2mbn.jmccc.auth.Authenticator;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import me.minidigger.minecraftlauncher.launcher.tasks.MinecraftStartTask;

public class MsaFragmentController extends FragmentController {

    private static final String loginUrl = "https://login.live.com/oauth20_authorize.srf" +
                                           "?client_id=00000000402b5328" +
                                           "&response_type=code" +
                                           "&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL" +
                                           "&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf";

    private static final String redirectUrlSuffix = "https://login.live.com/oauth20_desktop.srf?code=";

    private static final String authTokenUrl = "https://login.live.com/oauth20_token.srf";

    private static final String xblAuthUrl = "https://user.auth.xboxlive.com/user/authenticate";

    private static final String xstsAuthUrl = "https://xsts.auth.xboxlive.com/xsts/authorize";

    private static final String mcLoginUrl = "https://api.minecraftservices.com/authentication/login_with_xbox";

    private static final String mcStoreUrl = "https://api.minecraftservices.com/entitlements/mcstore";

    private static final String mcProfileUrl = "https://api.minecraftservices.com/minecraft/profile";

    @FXML
    private Pane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainPane.getChildren().clear();

        WebView webView = new WebView();
        webView.getEngine().load(loginUrl);
        webView.getEngine().setJavaScriptEnabled(true);
        webView.setPrefHeight(406);
        webView.setPrefWidth(406);

        // listen to end oauth flow
        webView.getEngine().getHistory().getEntries().addListener((ListChangeListener<WebHistory.Entry>) c -> {
            if (c.next() && c.wasAdded()) {
                for (WebHistory.Entry entry : c.getAddedSubList()) {
                    if (entry.getUrl().startsWith(redirectUrlSuffix)) {
                        String authCode = entry.getUrl().substring(entry.getUrl().indexOf("=") + 1, entry.getUrl().indexOf("&"));
                        // once we got the auth code, we can turn it into a oauth token
                        System.out.println("authCode: " + authCode); // TODO debug
                        acquireAccessToken(authCode);
                    }
                }
            }
        });

        mainPane.getChildren().add(webView);
    }

    private void acquireAccessToken(String authcode) {
        try {
            URI uri = new URI(authTokenUrl);

            Map<Object, Object> data = Map.of(
                    "client_id", "00000000402b5328",
                    "code", authcode,
                    "grant_type", "authorization_code",
                    "redirect_uri", "https://login.live.com/oauth20_desktop.srf",
                    "scope", "service::user.auth.xboxlive.com::MBI_SSL"
            );

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(ofFormData(data)).build();

            HttpClient.newBuilder().build().sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(resp -> {
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                    String body = resp.body();
                    try {
                        JSONObject jsonObject = (JSONObject) new JSONParser().parse(body);
                        String accessToken = (String) jsonObject.get("access_token");
                        System.out.println("accessToken: " + accessToken); // TODO debug
                        acquireXBLToken(accessToken);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void acquireXBLToken(String accessToken) {
        try {
            URI uri = new URI(xblAuthUrl);

            Map<Object, Object> data = Map.of(
                    "Properties", Map.of(
                            "AuthMethod", "RPS",
                            "SiteName", "user.auth.xboxlive.com",
                            "RpsTicket", accessToken
                    ),
                    "RelyingParty", "http://auth.xboxlive.com",
                    "TokenType", "JWT"
            );

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(ofJSONData(data)).build();

            HttpClient.newBuilder().build().sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(resp -> {
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                    String body = resp.body();
                    try {
                        JSONObject jsonObject = (JSONObject) new JSONParser().parse(body);
                        String xblToken = (String) jsonObject.get("Token");
                        System.out.println("xblToken: " + xblToken); // TODO debug
                        acquireXsts(xblToken);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void acquireXsts(String xblToken) {
        try {
            URI uri = new URI(xstsAuthUrl);

            Map<Object, Object> data = Map.of(
                    "Properties", Map.of(
                            "SandboxId", "RETAIL",
                            "UserTokens", List.of(xblToken)
                    ),
                    "RelyingParty", "rp://api.minecraftservices.com/",
                    "TokenType", "JWT"
            );

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(ofJSONData(data)).build();

            HttpClient.newBuilder().build().sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(resp -> {
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                    String body = resp.body();
                    try {
                        JSONObject jsonObject = (JSONObject) new JSONParser().parse(body);
                        String xblXsts = (String) jsonObject.get("Token");
                        JSONObject claims = (JSONObject) jsonObject.get("DisplayClaims");
                        JSONArray xui = (JSONArray) claims.get("xui");
                        String uhs = (String) ((JSONObject) xui.get(0)).get("uhs");
                        System.out.println("xblXsts: " + xblXsts + ", uhs: " + uhs); // TODO debug
                        acquireMinecraftToken(uhs, xblXsts);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void acquireMinecraftToken(String xblUhs, String xblXsts) {
        try {
            URI uri = new URI(mcLoginUrl);

            Map<Object, Object> data = Map.of(
                    "identityToken", "XBL3.0 x=" + xblUhs + ";" + xblXsts
            );

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(ofJSONData(data)).build();

            HttpClient.newBuilder().build().sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(resp -> {
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                    String body = resp.body();
                    try {
                        JSONObject jsonObject = (JSONObject) new JSONParser().parse(body);
                        String mcAccessToken = (String) jsonObject.get("access_token");
                        System.out.println("mcAccessToken: " + mcAccessToken); // TODO debug
                        checkMcStore(mcAccessToken);
                        checkMcProfile(mcAccessToken);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void checkMcStore(String mcAccessToken) {
        try {
            URI uri = new URI(mcStoreUrl);

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Authorization", "Bearer " + mcAccessToken)
                    .GET().build();

            HttpClient.newBuilder().build().sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(resp -> {
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                    String body = resp.body();
                    System.out.println("store: " + body);
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void checkMcProfile(String mcAccessToken) {
        try {
            URI uri = new URI(mcProfileUrl);

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Authorization", "Bearer " + mcAccessToken)
                    .GET().build();

            HttpClient.newBuilder().build().sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(resp -> {
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                    String body = resp.body();
                    try {
                        System.out.println("profile:" + body);
                        JSONObject jsonObject = (JSONObject) new JSONParser().parse(body);
                        String name = (String) jsonObject.get("name");
                        String uuid = (String) jsonObject.get("id");
                        String uuidDashes = uuid .replaceFirst(
                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                        );

                        // hack, not actually working, need to get the right values
                        Authenticator auth = () -> new AuthInfo(name, mcAccessToken, UUID.fromString(uuidDashes), Map.of(), "mojang");
                        new MinecraftStartTask(() -> System.out.println("corrupt!"), () -> System.out.println("started!"), auth, minecraftDirectory).start();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    String body = resp.body();
                    System.out.println("profile: " + resp.statusCode() + ": " + body);
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static HttpRequest.BodyPublisher ofJSONData(Map<Object, Object> data) {
        return HttpRequest.BodyPublishers.ofString(new JSONObject(data).toJSONString());
    }

    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    @Override
    public void onClose() {

    }
}
