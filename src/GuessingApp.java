import java.util.*;
import java.io.*;

/* =========================
   Custom Exception
   ========================= */
class InvalidInputException extends Exception {
    public InvalidInputException(String msg) {
        super(msg);
    }
}

/* =========================
   Validation Service
   ========================= */
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

/* =========================
   Guess Validator
   ========================= */
class GuessValidator {
    public static String validateGuess(int guess, int target) {
        if (guess == target) return "CORRECT";
        if (guess < target) return "LOW";
        return "HIGH";
    }
}

/* =========================
   Game Config
   ========================= */
class GameConfig {

    private final int MIN = 1;
    private final int MAX = 100;
    private final int MAX_ATTEMPTS = 7;
    private final int MAX_HINTS = 2;

    private int targetNumber;

    public GameConfig() {
        targetNumber = new Random().nextInt(MAX - MIN + 1) + MIN;
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

/* =========================
   Hint Service
   ========================= */
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

/* =========================
   Storage Service
   ========================= */
class StorageService {

    public static void saveResult(String player,
                                  int attempts,
                                  boolean win) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter("game_results.txt", true))) {

            writer.write("Player: " + player +
                    ", Attempts: " + attempts +
                    ", Result: " + (win ? "WIN" : "LOSE"));
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Unable to save game result.");
        }
    }
}

/* =========================
   MAIN — Use Case 5
   ========================= */
public class GuessingApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("==========================");
        System.out.println("Welcome to the Guessing App");
        System.out.println("==========================\n");

        System.out.print("Enter Player Name: ");
        String player = scanner.nextLine();

        GameConfig config = new GameConfig();
        config.showRules();

        int attempts = 0;
        int hintsUsed = 0;
        boolean win = false;

        while (attempts < config.getMaxAttempts()) {

            try {
                System.out.print("Enter your guess: ");

                int guess = ValidationService.validateInput(
                        scanner.nextLine());

                attempts++;

                String result = GuessValidator.validateGuess(
                        guess, config.getTargetNumber());

                if (!result.equals("CORRECT")) {
                    if (hintsUsed < config.getMaxHints()) {
                        hintsUsed++;
                        System.out.println(
                                HintService.generateHint(
                                        config.getTargetNumber(),
                                        hintsUsed));
                    } else {
                        System.out.println("No more hints available");
                    }
                }

                System.out.println(result);

                if (result.equals("CORRECT")) {
                    win = true;
                    break;
                }

            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }

        StorageService.saveResult(player, attempts, win);
        scanner.close();
    }
}
