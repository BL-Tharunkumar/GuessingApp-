/*
 * Use Case 3: Hint Generation
 *
 * This class is responsible for generating
 * controlled hints based on the number of
 * incorrect attempts made by the player.
 *
 * Hint logic is isolated to avoid cluttering
 * the main game flow.
 */
class HintService {

    /*
     * Generates a hint based on how many hints
     * have already been used.
     *
     * Hints provide partial information without
     * revealing the exact number.
     */
    public static String generateHint(int target, int hintCount) {

        if (hintCount == 1) {
            return (target % 2 == 0)
                    ? "Hint: Number is EVEN"
                    : "Hint: Number is ODD";
        } else if (hintCount == 2) {
            return (target > 50)
                    ? "Hint: Number is greater than 50"
                    : "Hint: Number is 50 or less";
        }

        return "No more hints available";
    }

}
