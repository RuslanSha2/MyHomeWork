package ru.ctqa.mantis.my_manager;

import okhttp3.*;

import java.io.IOException;
import java.net.CookieManager;

public class HttpSessionHelper extends HelperBase {
    OkHttpClient my_client;

    public HttpSessionHelper(ApplicationManager manager) {
        super(manager);
        my_client = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(new CookieManager())).build();
    }

    public void login(String username, String password) {
        RequestBody my_formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request my_request = new Request.Builder()
                .url(String.format("%s/login.php", manager.property("web.baseUrl")))
                .post(my_formBody)
                .build();
        try (Response my_response = my_client.newCall(my_request).execute()) {
            if (!my_response.isSuccessful()) throw new IOException("Unexpected code " + my_response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isLoggedIn() {
        Request my_request = new Request.Builder()
                .url(manager.property("web.baseUrl"))
                .build();
        try (Response my_response = my_client.newCall(my_request).execute()) {
            if (!my_response.isSuccessful()) throw new IOException("Unexpected code " + my_response);
            String my_body = my_response.body().string();
            return my_body.contains("<span class=\"user-info\">");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
