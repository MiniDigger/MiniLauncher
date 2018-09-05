package me.minidigger.minecraftlauncher.mojangapi;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Proxy;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import me.minidigger.minecraftlauncher.mojangapi.adapter.StatusAdapter;
import me.minidigger.minecraftlauncher.mojangapi.adapter.UUIDAdapter;
import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class AbstractEndpoint {

    protected static final MediaType APPLICATION_JSON = MediaType.parse("application/json");

    protected static OkHttpClient client;
    protected static Moshi moshi;

    static {
        client = new OkHttpClient.Builder().build();

        moshi = new Moshi.Builder().add(new StatusAdapter()).add(new UUIDAdapter()).build();
    }

    protected static Response get(String url) throws IOException {
        System.out.println("GET " + url);
        Request request = new Request.Builder().url(url).get().build();

        Call call = client.newCall(request);

        return call.execute();
    }

    protected static Response post(String url, RequestBody body) throws IOException {
        System.out.println("POST " + url + " with " + body);
        Request request = new Request.Builder().url(url).post(body).build();

        Call call = client.newCall(request);

        return call.execute();
    }

    protected static <T> Response post(String url, MediaType mediatype, Type typeOfBody, T body) throws IOException {
        JsonAdapter<T> jsonAdapter = moshi.adapter(typeOfBody);
        RequestBody requestBody = RequestBody.create(mediatype, jsonAdapter.toJson(body));
        System.out.println("POST " + url + " with " + jsonAdapter.toJson(body));

        Request request = new Request.Builder().url(url).post(requestBody).build();

        Call call = client.newCall(request);

        return call.execute();
    }

    protected static String read(Response resp) {
        if (resp == null) {
            System.out.println("null");
            return "null";
        } else if (resp.code() == 204) {
            System.out.println("empty");
            return "";
        } else if (resp.code() == 200) {
            try {
                String content = resp.body().string();
                System.out.println(content);
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                //TODO properly read error message if present
                System.out.println("error (" + resp.code() + "):" + resp.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
