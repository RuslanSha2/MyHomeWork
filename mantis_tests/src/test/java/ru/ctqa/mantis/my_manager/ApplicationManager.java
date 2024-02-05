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
    private SessionHelper my_sessionHelper;
    private HttpSessionHelper my_httpSessionHelper;
    private JamesCliHelper my_jamesCliHelper;
    private MailHelper my_mailHelper;

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

    public SessionHelper session() {
        if (my_sessionHelper == null) {
            my_sessionHelper = new SessionHelper(this);
        }
        return my_sessionHelper;
    }

    public HttpSessionHelper http() {
        if (my_httpSessionHelper == null) {
            my_httpSessionHelper = new HttpSessionHelper(this);
        }
        return my_httpSessionHelper;
    }

    public JamesCliHelper jamesCli() {
        if (my_jamesCliHelper == null) {
            my_jamesCliHelper = new JamesCliHelper(this);
        }
        return my_jamesCliHelper;
    }

    public MailHelper mail() {
        if (my_mailHelper == null) {
            my_mailHelper = new MailHelper(this);
        }
        return my_mailHelper;
    }

    public String property(String name) {
        return my_properties.getProperty(name);
    }
}
