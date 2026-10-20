package matchalgo;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Matchalgo {
    public static void main(String[] args) {
        run(new Scanner(System.in), System.out);
    }

    /** Processes whitespace-separated integers without closing the caller's streams. */
    public static void run(Scanner input, PrintStream output) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(output, "output");
        while (true) {
            output.print("Enter an integer (0 to exit): ");
            output.flush();
            if (!input.hasNext()) {
                output.println();
                return;
            }

            int number;
            try {
                number = Integer.parseInt(input.next());
            } catch (NumberFormatException exception) {
                output.println("Invalid input. Enter an integer from -2147483648 to 2147483647.");
                continue;
            }
            if (number == 0) {
                output.println("Exiting the program.");
                return;
            }
            if (number < 0) {
                output.println("Please enter a positive integer or 0 to exit.");
            } else if (number == 1) {
                output.println("1 has no prime factors.");
            } else {
                output.print("Prime factors of " + number + ":");
                for (int factor : findFactors(number)) {
                    output.print(" " + factor);
                }
                output.println();
            }
        }
    }

    /** Returns ascending prime factors with repetition; inputs below 2 return an empty list. */
    public static List<Integer> findFactors(int number) {
        List<Integer> factors = new ArrayList<>();
        if (number < 2) {
            return factors;
        }
        int remaining = number;
        while (remaining % 2 == 0) {
            factors.add(2);
            remaining /= 2;
        }
        // Division avoids overflow from squaring the divisor near Integer.MAX_VALUE.
        for (int divisor = 3; divisor <= remaining / divisor; divisor += 2) {
            while (remaining % divisor == 0) {
                factors.add(divisor);
                remaining /= divisor;
            }
        }
        if (remaining > 1) {
            factors.add(remaining);
        }
        return factors;
    }
}
