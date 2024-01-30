package my_manager;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Properties;

public class ApplicationManager {
    protected WebDriver my_driver;
    private LoginHelper my_session;
    private GroupHelper my_groups;
    private ContactHelper my_contacts;
    private JdbcHelper my_jdbc;
    private HibernateHelper my_hbm;
    private Properties my_properties;

    public void init(String my_browser, Properties my_properties) {
        if ("firefox".equals(my_browser)) {
            my_driver = new FirefoxDriver();
        } else if ("chrome".equals(my_browser)) {
            my_driver = new ChromeDriver();
        } else {
            throw new IllegalArgumentException(String.format("Unknown browser %s", my_browser));
        }
        this.my_properties = my_properties;
        my_driver.get(my_properties.getProperty("web.baseUrl"));
        my_driver.manage().window().setSize(new Dimension(1050, 790));
        my_session().login(my_properties.getProperty("web.username"), my_properties.getProperty("web.password"));
    }

    public LoginHelper my_session() {
        if (my_session == null) {
            my_session = new LoginHelper(this);
        }
        return my_session;
    }

    public GroupHelper my_groups() {
        if (my_groups == null) {
            my_groups = new GroupHelper(this);
        }
        return my_groups;
    }

    public ContactHelper my_contacts() {
        if (my_contacts == null) {
            my_contacts = new ContactHelper(this);
        }
        return my_contacts;
    }

    public JdbcHelper my_jdbc() {
        if (my_jdbc == null) {
            my_jdbc = new JdbcHelper(this);
        }
        return my_jdbc;
    }

    public HibernateHelper my_hbm() {
        if (my_hbm == null) {
            my_hbm = new HibernateHelper(this);
        }
        return my_hbm;
    }
    public Properties my_properties() {
        return my_properties;
    }

    protected boolean isElementPresent(By myLocator) {
        try {
            my_driver.findElement(myLocator);
            return true;
        } catch (NoSuchElementException exception) {
            return false;
        }
    }
}
