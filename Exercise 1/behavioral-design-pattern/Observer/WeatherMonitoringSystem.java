import java.util.*;
import java.util.logging.Logger;

interface WeatherObserver {
    void update(WeatherData data);
}

class WeatherData {
    private float temperature;
    private float humidity;
    private float pressure;

    // Getters and setters
    // ...

    @Override
    public String toString() {
        return String.format("Temperature: %.1f°C, Humidity: %.1f%%, Pressure: %.1f hPa", temperature, humidity, pressure);
    }
}

class WeatherStation implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(WeatherStation.class.getName());
    private final List<WeatherObserver> observers = new ArrayList<>();
    private WeatherData currentWeather = new WeatherData();
    private final Timer updateTimer = new Timer();

    public WeatherStation() {
        scheduleWeatherUpdates();
    }

    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(WeatherObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.update(currentWeather);
        }
    }

    private void scheduleWeatherUpdates() {
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateWeather();
            }
        }, 0, 60000); // Update every minute
    }

    private void updateWeather() {
        // Simulate weather changes
        currentWeather = new WeatherData();
        // ... set new values for temperature, humidity, and pressure
        notifyObservers();
    }

    @Override
    public void close() {
        updateTimer.cancel();
        LOGGER.info("Weather station shut down.");
    }
}

class DisplayDevice implements WeatherObserver {
    private final String name;
    private static final Logger LOGGER = Logger.getLogger(DisplayDevice.class.getName());

    public DisplayDevice(String name) {
        this.name = name;
    }

    @Override
    public void update(WeatherData data) {
        LOGGER.info(name + " received update: " + data);
    }
}

public class WeatherMonitoringSystem {
    public static void main(String[] args) {
        try (WeatherStation weatherStation = new WeatherStation()) {
            DisplayDevice mobileApp = new DisplayDevice("Mobile App");
            DisplayDevice webApp = new DisplayDevice("Web App");

            weatherStation.addObserver(mobileApp);
            weatherStation.addObserver(webApp);

            // Simulate running for a while
            Thread.sleep(5000);

            weatherStation.removeObserver(webApp);

            // Run for a bit longer
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Logger.getLogger(WeatherMonitoringSystem.class.getName()).severe("Weather monitoring interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}