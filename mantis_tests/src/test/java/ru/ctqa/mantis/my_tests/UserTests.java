package ru.ctqa.mantis.my_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ctqa.mantis.my_common.MyCommonFunctions;
import ru.ctqa.mantis.my_model.UserData;

import java.time.Duration;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class UserTests extends TestBase {
    public static Stream<String> randomUsers() {
        Supplier<String> randomUser = () -> MyCommonFunctions.randomString(8);
        return Stream.generate(randomUser).limit(1);
    }

    @ParameterizedTest
    @MethodSource("randomUsers")
    void canRegisterUser(String my_username) {
        // создать пользователя (адрес) на почтовом сервисе (JamesApiHelper)
        var my_email = String.format("%s@localhost", my_username);
        var my_password = "password";
        my_app.jamesApi().addUser(my_email, my_password);

        // заполняем форму создания и отправляем (RestApiHelper)
        my_app.rest().createUser(new UserData()
                .withUsername(my_username)
                .withRealname(my_username)
                .withPassword(my_password)
                .withEmail(my_email));

        // ждём почту (MailHelper)
        var my_duration = Duration.ofSeconds(60);
        var my_messages = my_app.mail().receive(my_email, my_password, my_duration);
        Assertions.assertEquals(1, my_messages.size());

        // извлекаем ссылку из письма (MailHelper)
        var my_text = my_messages.get(0).content();
        var my_pattern = Pattern.compile("http://\\S*");
        var my_matcher = my_pattern.matcher(my_text);
        Assertions.assertTrue(my_matcher.find());
        var my_url = my_text.substring(my_matcher.start(), my_matcher.end());

        // проходим по ссылке и завершаем регистрацию пользователя (SessionHelper)
        my_app.session().confirmPassword(my_url, my_password);
        Assertions.assertTrue(my_app.session().isLoginForm());

        // проверяем, что пользователь может залогиниться (HttpSessionHelper)
        my_app.http().login(my_username, my_password);
        Assertions.assertTrue(my_app.http().isLoggedIn());
    }
}
