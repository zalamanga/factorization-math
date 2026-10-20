package matchalgo;

import java.util.ArrayList;
import java.util.List;

public final class NumberTheory {
    private NumberTheory() {
    }

    /** Returns ascending prime factors with repetition; inputs below 2 return a mutable empty list. */
    public static List<Integer> primeFactors(int number) {
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

    /** Returns a nonnegative GCD, including gcd(0, 0) = 0. */
    public static long gcd(int first, int second) {
        long left = Math.abs((long) first);
        long right = Math.abs((long) second);
        while (right != 0) {
            long remainder = left % right;
            left = right;
            right = remainder;
        }
        return left;
    }

    /** Returns a nonnegative LCM, or zero if either input is zero. */
    public static long lcm(int first, int second) {
        if (first == 0 || second == 0) {
            return 0;
        }
        long left = Math.abs((long) first);
        long right = Math.abs((long) second);
        return (left / gcd(first, second)) * right;
    }
}
