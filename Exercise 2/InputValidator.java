import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

class InputValidator {

    // Get a non-empty string input from the user
    public static String getStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim(); // Read and trim input
    }

    // Get an integer input within specified bounds
    public static int getIntInput(Scanner scanner, String prompt, int min, int max) {
        int input = -1; // Initialize input
        boolean isValid = false; // Input validity flag

        while (!isValid) {
            try {
                System.out.print(prompt); // Prompt for input
                input = Integer.parseInt(scanner.nextLine()); // Parse input as integer
                if (input >= min && input <= max) {
                    isValid = true; // Valid input, exit loop
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return input; // Return valid integer input
    }

    // Get a valid time input in HH:mm format
    public static String getTimeInput(Scanner scanner, String prompt) {
        String input;
        boolean isValid = false; // Input validity flag

        do {
            input = getStringInput(scanner, prompt); // Get string input
            if (input.isEmpty()) {
                return input;  // Allow empty input for optional fields
            }
            try {
                LocalTime.parse(input); // Validate the time format
                isValid = true; // Valid time, exit loop
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use HH:mm.");
            }
        } while (!isValid);

        return input; // Return valid time input
    }

    // Get a valid priority input (LOW, MEDIUM, HIGH)
    public static String getPriorityInput(Scanner scanner, String prompt) {
        String input;
        boolean isValid = false; // Input validity flag

        do {
            input = getStringInput(scanner, prompt).toUpperCase(); // Get string input and convert to uppercase
            if (input.isEmpty()) {
                return input;  // Allow empty input for optional fields
            }
            try {
                Priority.valueOf(input); // Validate priority
                isValid = true; // Valid priority, exit loop
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority. Please enter LOW, MEDIUM, or HIGH.");
            }
        } while (!isValid);

        return input; // Return valid priority input
    }
}
