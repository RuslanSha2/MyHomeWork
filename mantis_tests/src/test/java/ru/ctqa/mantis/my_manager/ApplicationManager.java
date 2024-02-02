package ru.ctqa.mantis.my_manager;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Properties;

public class ApplicationManager {
    private WebDriver my_driver;
    private String my_browser;
    private Properties my_properties;

    public void init(String browser, Properties properties) {
        this.my_browser = browser;
        this.my_properties = properties;
    }

    public WebDriver driver() {
        if (my_driver == null) {
            if ("firefox".equals(my_browser)) {
                my_driver = new FirefoxDriver();
            } else if ("chrome".equals(my_browser)) {
                my_driver = new ChromeDriver();
            } else {
                throw new IllegalArgumentException(String.format("Unknown browser %s", my_browser));
            }
            Runtime.getRuntime().addShutdownHook(new Thread(my_driver::quit));
            my_driver.get(my_properties.getProperty("web.baseUrl"));
            my_driver.manage().window().setSize(new Dimension(1050, 790));
        }
        return my_driver;
    }
}
