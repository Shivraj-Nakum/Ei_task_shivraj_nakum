import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

class ConfigurationManager {
    // Logger for logging configuration-related events and errors
    private static final Logger logger = Logger.getLogger(ConfigurationManager.class.getName());
    // Properties object to hold configuration settings
    private static Properties properties = new Properties();

    // Load configuration settings from the specified file
    public static void loadConfiguration(String filename) {
        try (FileInputStream input = new FileInputStream(filename)) {
            // Load properties from the configuration file
            properties.load(input);
            // Configure logging settings based on the properties loaded
            configureLogging();
        } catch (IOException e) {
            // Log an error if the configuration file cannot be loaded
            logger.log(Level.SEVERE, "Error loading configuration file", e);
        }
    }

    // Configure logging level based on properties
    private static void configureLogging() {
        // Get the log level from properties, default to "INFO" if not specified
        String logLevel = properties.getProperty("log.level", "INFO");
        // Set the console handler's log level
        System.setProperty("java.util.logging.ConsoleHandler.level", logLevel);
        // Set the logger's level to the specified log level
        Logger.getLogger("").setLevel(Level.parse(logLevel));
    }

    // Retrieve a property value by its key
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
