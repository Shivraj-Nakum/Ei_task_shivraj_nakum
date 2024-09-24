import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class Meal {
    private List<String> items = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(Meal.class.getName());

    public void addItem(String item) {
        items.add(item);
    }

    public void showItems() {
        for (String item : items) {
            LOGGER.info("Item: " + item);
        }
    }
}

interface MealBuilder {
    void buildMainCourse();
    void buildSide();
    void buildDrink();
    void buildDessert();
    Meal getMeal();
}

class HealthyMealBuilder implements MealBuilder {
    private Meal meal = new Meal();

    @Override
    public void buildMainCourse() {
        meal.addItem("Grilled Chicken Breast");
    }

    @Override
    public void buildSide() {
        meal.addItem("Steamed Vegetables");
    }

    @Override
    public void buildDrink() {
        meal.addItem("Fresh Orange Juice");
    }

    @Override
    public void buildDessert() {
        meal.addItem("Fruit Salad");
    }

    @Override
    public Meal getMeal() {
        return meal;
    }
}

class FastFoodMealBuilder implements MealBuilder {
    private Meal meal = new Meal();

    @Override
    public void buildMainCourse() {
        meal.addItem("Cheeseburger");
    }

    @Override
    public void buildSide() {
        meal.addItem("French Fries");
    }

    @Override
    public void buildDrink() {
        meal.addItem("Cola");
    }

    @Override
    public void buildDessert() {
        meal.addItem("Ice Cream Sundae");
    }

    @Override
    public Meal getMeal() {
        return meal;
    }
}

class MealDirector {
    private MealBuilder mealBuilder;

    public void setMealBuilder(MealBuilder mealBuilder) {
        this.mealBuilder = mealBuilder;
    }

    public Meal constructMeal() {
        mealBuilder.buildMainCourse();
        mealBuilder.buildSide();
        mealBuilder.buildDrink();
        mealBuilder.buildDessert();
        return mealBuilder.getMeal();
    }
}

public class MealOrderingSystem {
    private static final Logger LOGGER = Logger.getLogger(MealOrderingSystem.class.getName());

    public static void main(String[] args) {
        try {
            MealDirector director = new MealDirector();

            MealBuilder healthyBuilder = new HealthyMealBuilder();
            director.setMealBuilder(healthyBuilder);
            Meal healthyMeal = director.constructMeal();

            LOGGER.info("Healthy Meal:");
            healthyMeal.showItems();

            MealBuilder fastFoodBuilder = new FastFoodMealBuilder();
            director.setMealBuilder(fastFoodBuilder);
            Meal fastFoodMeal = director.constructMeal();

            LOGGER.info("Fast Food Meal:");
            fastFoodMeal.showItems();
        } catch (Exception e) {
            LOGGER.severe("An error occurred during meal ordering: " + e.getMessage());
        }
    }
}