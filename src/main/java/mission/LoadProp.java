package mission;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadProp extends BasePage {

    private static final Properties prop = new Properties();

    static {
        try (InputStream input = LoadProp.class.getClassLoader()
                .getResourceAsStream("testdata/TestData.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find testdata/TestData.properties in classpath. " +
                        "Ensure file is at src/test/resources/testdata/TestData.properties");
            }
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file from testdata/TestData.properties", e);
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}