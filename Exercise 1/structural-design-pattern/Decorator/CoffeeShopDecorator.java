import java.util.logging.Logger;
import java.util.logging.Level;

// Base Coffee interface
interface Coffee {
    String getDescription();
    double getCost();
}

// Concrete implementation of basic coffee
class BasicCoffee implements Coffee {
    @Override
    public String getDescription() {
        return "Basic Coffee";
    }

    @Override
    public double getCost() {
        return 2.0;
    }
}

// Abstract Decorator class
abstract class CoffeeDecorator implements Coffee {
    protected Coffee decoratedCoffee;

    public CoffeeDecorator(Coffee coffee) {
        this.decoratedCoffee = coffee;
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription();
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost();
    }
}

// Concrete Decorators
class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Milk";
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.5;
    }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Sugar";
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.2;
    }
}

class WhippedCreamDecorator extends CoffeeDecorator {
    public WhippedCreamDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Whipped Cream";
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.7;
    }
}

public class CoffeeShopDecorator {
    private static final Logger LOGGER = Logger.getLogger(CoffeeShopDecorator.class.getName());

    public static void main(String[] args) {
        try {
            Coffee basicCoffee = new BasicCoffee();
            LOGGER.info("Ordered: " + basicCoffee.getDescription() + " - Cost: $" + basicCoffee.getCost());

            Coffee milkCoffee = new MilkDecorator(new BasicCoffee());
            LOGGER.info("Ordered: " + milkCoffee.getDescription() + " - Cost: $" + milkCoffee.getCost());

            Coffee fancyCoffee = new WhippedCreamDecorator(new SugarDecorator(new MilkDecorator(new BasicCoffee())));
            LOGGER.info("Ordered: " + fancyCoffee.getDescription() + " - Cost: $" + fancyCoffee.getCost());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in coffee shop system", e);
        }
    }
}