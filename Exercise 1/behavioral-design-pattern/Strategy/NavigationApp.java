import java.util.logging.Logger;

interface RoutingStrategy {
    void calculateRoute(String start, String end);
}

class FastestRouteStrategy implements RoutingStrategy {
    private static final Logger LOGGER = Logger.getLogger(FastestRouteStrategy.class.getName());

    @Override
    public void calculateRoute(String start, String end) {
        LOGGER.info("Calculating fastest route from " + start + " to " + end);
        // Implementation for fastest route calculation
    }
}

class ShortestRouteStrategy implements RoutingStrategy {
    private static final Logger LOGGER = Logger.getLogger(ShortestRouteStrategy.class.getName());

    @Override
    public void calculateRoute(String start, String end) {
        LOGGER.info("Calculating shortest route from " + start + " to " + end);
        // Implementation for shortest route calculation
    }
}

class EcoFriendlyRouteStrategy implements RoutingStrategy {
    private static final Logger LOGGER = Logger.getLogger(EcoFriendlyRouteStrategy.class.getName());

    @Override
    public void calculateRoute(String start, String end) {
        LOGGER.info("Calculating eco-friendly route from " + start + " to " + end);
        // Implementation for eco-friendly route calculation
    }
}

class NavigationSystem {
    private RoutingStrategy routingStrategy;
    private static final Logger LOGGER = Logger.getLogger(NavigationSystem.class.getName());

    public void setRoutingStrategy(RoutingStrategy strategy) {
        this.routingStrategy = strategy;
    }

    public void navigateTo(String destination) {
        if (routingStrategy == null) {
            LOGGER.warning("No routing strategy set. Please set a strategy before navigating.");
            return;
        }

        String start = getCurrentLocation();
        routingStrategy.calculateRoute(start, destination);
        LOGGER.info("Navigation to " + destination + " started.");
    }

    private String getCurrentLocation() {
        // Implementation to get current location
        return "Current Location";
    }
}

public class NavigationApp {
    private static final Logger LOGGER = Logger.getLogger(NavigationApp.class.getName());

    public static void main(String[] args) {
        NavigationSystem nav = new NavigationSystem();

        try {
            nav.navigateTo("Central Park"); // Should log a warning

            nav.setRoutingStrategy(new FastestRouteStrategy());
            nav.navigateTo("Central Park");

            nav.setRoutingStrategy(new ShortestRouteStrategy());
            nav.navigateTo("Times Square");

            nav.setRoutingStrategy(new EcoFriendlyRouteStrategy());
            nav.navigateTo("Brooklyn Bridge");
        } catch (Exception e) {
            LOGGER.severe("An error occurred during navigation: " + e.getMessage());
        }
    }
}