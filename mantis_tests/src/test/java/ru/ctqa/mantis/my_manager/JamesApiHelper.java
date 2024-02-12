package ru.ctqa.mantis.my_manager;

import okhttp3.*;

import java.io.IOException;
import java.net.CookieManager;

public class JamesApiHelper extends HelperBase {
    public static final MediaType JSON = MediaType.get("application/json");
    OkHttpClient my_client;

    public JamesApiHelper(ApplicationManager manager) {
        super(manager);
        my_client = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(new CookieManager())).build();
    }

    public void addUser(String email, String password) {
        RequestBody my_body = RequestBody.create(String.format("{\"password\":\"%s\"}", password), JSON);
        Request request = new Request.Builder()
                .url(String.format("%s/users/%s", manager.property("james.apiBaseUrl"), email))
                .put(my_body)
                .build();
        try (Response my_response = my_client.newCall(request).execute()) {
            if (!my_response.isSuccessful()) throw new IOException("Unexpected code " + my_response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}