package my_manager;

import org.openqa.selenium.By;

public class LoginHelper extends HelperBase {

    public LoginHelper(ApplicationManager my_manager) {
        super(my_manager);
    }

    public void login(String my_user, String my_password) {
        type(By.name("user"), my_user);
        type(By.name("pass"), my_password);
        click(By.xpath("//input[@value=\'Login\']"));

    }

    public void close() {
        click(By.linkText("Logout"));
        quit();
    }
}
