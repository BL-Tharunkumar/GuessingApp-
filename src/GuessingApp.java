import java.util.Random;
import java.util.Scanner;

/*
 * Custom exception for invalid input
 */
class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}


/*
 * Input validation service
 */
class ValidationService {

    public static int validateInput(String input)
            throws InvalidInputException {

        try {
            int value = Integer.parseInt(input);

            if (value < 1 || value > 100) {
                throw new InvalidInputException(
                        "Number must be between 1 and 100");
            }

            return value;

        } catch (NumberFormatException e) {
            throw new InvalidInputException(
                    "Invalid input. Please enter numbers only.");
        }
    }
}


/*
 * Guess comparison logic
 */
class GuessValidator {

    public static String validateGuess(int guess, int target) {
        if (guess == target) return "CORRECT";
        if (guess < target) return "LOW";
        return "HIGH";
    }
}


/*
 * Game configuration
 */
class GameConfig {

    private final int MIN = 1;
    private final int MAX = 100;
    private final int MAX_ATTEMPTS = 7;
    private final int MAX_HINTS = 2;

    private int targetNumber;

    public GameConfig() {
        Random random = new Random();
        targetNumber = random.nextInt(MAX - MIN + 1) + MIN;
    }

    public int getTargetNumber() { return targetNumber; }
    public int getMaxAttempts() { return MAX_ATTEMPTS; }
    public int getMaxHints() { return MAX_HINTS; }

    public void showRules() {
        System.out.println("🎯 Guess a number between " + MIN + " and " + MAX);
        System.out.println("You have " + MAX_ATTEMPTS + " attempts.");
        System.out.println("Hints will be provided after wrong guesses.\n");
    }
}


/*
 * Hint logic
 */
class HintService {

    public static String generateHint(int target, int hintCount) {

        if (hintCount == 1) {
            return (target % 2 == 0)
                    ? "Hint: Number is EVEN"
                    : "Hint: Number is ODD";
        }

        if (hintCount == 2) {
            return (target > 50)
                    ? "Hint: Number is greater than 50"
                    : "Hint: Number is 50 or less";
        }

        return "No more hints available";
    }
}


/*
 * MAIN — with validation & error handling
 */
public class GuessingApp {

    public static void main(String[] args) {

        System.out.println("Welcome to the Guessing App");

        GameConfig config = new GameConfig();
        config.showRules();

        Scanner scanner = new Scanner(System.in);

        int attempts = 0;
        int hintCount = 0;

        while (attempts < config.getMaxAttempts()) {

            try {
                System.out.print("Enter your guess: ");

                // ✅ validated input
                int guess = ValidationService.validateInput(
                        scanner.nextLine());

                attempts++;

                String result = GuessValidator.validateGuess(
                        guess, config.getTargetNumber());

                // hints only for wrong guesses
                if (!result.equals("CORRECT")) {
                    hintCount++;
                    if (hintCount <= config.getMaxHints()) {
                        System.out.println(
                                HintService.generateHint(
                                        config.getTargetNumber(),
                                        hintCount));
                    } else {
                        System.out.println("No more hints available");
                    }
                }

                System.out.println(result);

                if (result.equals("CORRECT")) {
                    break;
                }

            } catch (InvalidInputException e) {
                // ✅ graceful error message
                System.out.println(e.getMessage());
                // attempt NOT counted
            }
        }

        scanner.close();
    }
}
