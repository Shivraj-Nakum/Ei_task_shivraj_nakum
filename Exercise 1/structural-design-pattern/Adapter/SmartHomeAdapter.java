import java.util.logging.Logger;
import java.util.logging.Level;

// Legacy temperature sensor interface
interface LegacyTemperatureSensor {
    double getTemperatureFahrenheit();
}

// Modern smart home system interface
interface SmartHomeSystem {
    double getTemperatureCelsius();
    void setTemperature(double celsius);
}

// Legacy temperature sensor implementation
class OldTemperatureSensor implements LegacyTemperatureSensor {
    private static final Logger LOGGER = Logger.getLogger(OldTemperatureSensor.class.getName());
    
    @Override
    public double getTemperatureFahrenheit() {
        try {
            // Simulating sensor reading
            return 68.0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error reading temperature", e);
            throw new RuntimeException("Failed to read temperature", e);
        }
    }
}

// Adapter to make the legacy sensor work with the new system
class TemperatureSensorAdapter implements SmartHomeSystem {
    private final LegacyTemperatureSensor legacySensor;
    private static final Logger LOGGER = Logger.getLogger(TemperatureSensorAdapter.class.getName());

    public TemperatureSensorAdapter(LegacyTemperatureSensor legacySensor) {
        this.legacySensor = legacySensor;
    }

    @Override
    public double getTemperatureCelsius() {
        try {
            double fahrenheit = legacySensor.getTemperatureFahrenheit();
            return (fahrenheit - 32) * 5 / 9;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error converting temperature", e);
            throw new RuntimeException("Failed to get temperature in Celsius", e);
        }
    }

    @Override
    public void setTemperature(double celsius) {
        // Implementation for setting temperature (if supported by the legacy sensor)
        LOGGER.info("Setting temperature is not supported by the legacy sensor");
    }
}

public class SmartHomeAdapter {
    private static final Logger LOGGER = Logger.getLogger(SmartHomeAdapter.class.getName());

    public static void main(String[] args) {
        LegacyTemperatureSensor oldSensor = new OldTemperatureSensor();
        SmartHomeSystem smartSystem = new TemperatureSensorAdapter(oldSensor);

        try {
            double celsius = smartSystem.getTemperatureCelsius();
            LOGGER.info("Current temperature: " + celsius + "°C");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in smart home system", e);
        }
    }
}