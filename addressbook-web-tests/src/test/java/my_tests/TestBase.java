package my_tests;

import my_manager.ApplicationManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TestBase {
    protected static ApplicationManager my_app;

    @BeforeEach
    public void setUp() throws IOException {
        if (my_app == null) {
            var my_properties = new Properties();
            my_properties.load(new FileReader(System.getProperty("target", "my_local.properties")));
            my_app = new ApplicationManager();
            my_app.init(System.getProperty("browser", "chrome"), my_properties);
        }
    }

    @AfterEach
    public void tearDown() {
        my_app.my_session().close();
        my_app = null;
    }
}
