package my_manager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.nio.file.Paths;

public class HelperBase {
    protected final ApplicationManager my_manager;

    public HelperBase(ApplicationManager my_manager) {
        this.my_manager = my_manager;
    }

    protected void click(By my_locator) {
        my_manager.my_driver.findElement(my_locator).click();
    }

    protected void click(By my_locator, By my_list_locator) {
        WebElement my_list = my_manager.my_driver.findElement(my_locator);
        my_list.click();
        my_list.findElement(my_list_locator).click();
    }

    protected void type(By my_locator, String text) {
        click(my_locator);
        my_manager.my_driver.findElement(my_locator).clear();
        my_manager.my_driver.findElement(my_locator).sendKeys(text);
    }

    protected void attach(By locator, String file) {
        my_manager.my_driver.findElement(locator).sendKeys(Paths.get(file).toAbsolutePath().toString());
    }

    protected void quit() {
        my_manager.my_driver.quit();
    }
}
