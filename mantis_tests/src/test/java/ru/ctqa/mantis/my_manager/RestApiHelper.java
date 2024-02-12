package ru.ctqa.mantis.my_manager;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.UserApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.User;
import ru.ctqa.mantis.my_model.UserData;

public class RestApiHelper extends HelperBase {
    public RestApiHelper(ApplicationManager manager) {
        super(manager);
        ApiClient my_defClient = Configuration.getDefaultApiClient();
        ApiKeyAuth my_auth = (ApiKeyAuth) my_defClient.getAuthentication("Authorization");
        my_auth.setApiKey(manager.property("apiKey"));
    }

    public void createUser(UserData userData) {
        UserApi my_apiInstance = new UserApi();

        User my_user = new User();
        my_user.setUsername(userData.username());
        my_user.setPassword(userData.password());
        my_user.setRealName(userData.realname());
        my_user.setEmail(userData.email());

        try {
            my_apiInstance.userAdd(my_user);
        } catch (ApiException e) {
            System.err.println("Exception when calling UserApi#userAdd");
            e.printStackTrace();
        }
    }


}