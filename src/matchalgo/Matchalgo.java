package matchalgo;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Matchalgo {
    public static void main(String[] args) {
        run(new Scanner(System.in), System.out);
    }

    /** Runs the menu without closing the caller's streams. */
    public static void run(Scanner input, PrintStream output) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(output, "output");
        while (true) {
            output.println("Math tools");
            output.println("1. Prime factorization");
            output.println("2. GCD / LCM (FPB / KPK)");
            output.println("0. Exit");
            Integer choice = readInteger(input, output, "Choose an option: ");
            if (choice == null) {
                return;
            }
            if (choice == 0) {
                output.println("Exiting the program.");
                return;
            }
            if (choice == 1) {
                if (!showPrimeFactors(input, output)) {
                    return;
                }
            } else if (choice == 2) {
                if (!showCommonFactors(input, output)) {
                    return;
                }
            } else {
                output.println("Invalid option. Choose 1, 2, or 0.");
            }
        }
    }

    private static boolean showPrimeFactors(Scanner input, PrintStream output) {
        while (true) {
            Integer number = readInteger(input, output,
                    "Enter a positive integer (0 to return to menu): ");
            if (number == null) {
                return false;
            }
            if (number == 0) {
                return true;
            }
            if (number < 0) {
                output.println("Please enter a positive integer or 0 to return to menu.");
                continue;
            }
            if (number == 1) {
                output.println("1 has no prime factors.");
            } else {
                output.print("Prime factors of " + number + ":");
                for (int factor : findFactors(number)) {
                    output.print(" " + factor);
                }
                output.println();
            }
            return true;
        }
    }

    private static boolean showCommonFactors(Scanner input, PrintStream output) {
        Integer first = readInteger(input, output, "Enter the 1st number: ");
        if (first == null) {
            return false;
        }
        Integer second = readInteger(input, output, "Enter the 2nd number: ");
        if (second == null) {
            return false;
        }
        output.println("GCD (FPB): " + NumberTheory.gcd(first, second));
        output.println("LCM (KPK): " + NumberTheory.lcm(first, second));
        return true;
    }

    private static Integer readInteger(Scanner input, PrintStream output, String prompt) {
        while (true) {
            output.print(prompt);
            output.flush();
            if (!input.hasNext()) {
                output.println();
                return null;
            }
            try {
                return Integer.parseInt(input.next());
            } catch (NumberFormatException exception) {
                output.println("Invalid input. Enter an integer from -2147483648 to 2147483647.");
            }
        }
    }

    /** Returns ascending prime factors with repetition; inputs below 2 return an empty list. */
    public static List<Integer> findFactors(int number) {
        return NumberTheory.primeFactors(number);
    }
}
