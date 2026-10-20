package matchalgo;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class NumberTheoryTest {
    private static int checks;

    public static void main(String[] args) {
        verifyPrimeFactors();
        verifyGcdAndLcmFixtures();
        verifyBigIntegerOracle();
        System.out.println("NumberTheoryTest: " + checks + " checks passed.");
    }

    private static void verifyPrimeFactors() {
        for (int number : new int[]{Integer.MIN_VALUE, -12, -1, 0, 1}) {
            List<Integer> empty = NumberTheory.primeFactors(number);
            check(empty.isEmpty(), "no prime factors below 2");
            empty.add(7);
            check(NumberTheory.primeFactors(number).isEmpty(), "independent mutable empty list");
        }
        expectFactors(2, 2);
        expectFactors(3, 3);
        expectFactors(4, 2, 2);
        expectFactors(12, 2, 2, 3);
        expectFactors(360, 2, 2, 2, 3, 3, 5);
        expectFactors(225, 3, 3, 5, 5);
        expectFactors(Integer.MAX_VALUE, Integer.MAX_VALUE);
        expectFactors(2147117569, 46337, 46337);
        expectFactors(2146654199, 46327, 46337);
        expectFactors(2145747229, 32749, 65521);
        expectFactors(2147483646, 2, 3, 3, 7, 11, 31, 151, 331);
        check(NumberTheory.primeFactors(1073741824).equals(Collections.nCopies(30, 2)), "2^30 multiplicity");
        check(NumberTheory.primeFactors(1162261467).equals(Collections.nCopies(19, 3)), "3^19 multiplicity");
        check(NumberTheory.primeFactors(1220703125).equals(Collections.nCopies(13, 5)), "5^13 multiplicity");
        List<Integer> first = NumberTheory.primeFactors(12);
        first.clear();
        expectFactors(12, 2, 2, 3);
    }

    private static void verifyGcdAndLcmFixtures() {
        expectGcdLcm(0, 0, 0L, 0L);
        expectGcdLcm(0, 12, 12L, 0L);
        expectGcdLcm(-12, 0, 12L, 0L);
        expectGcdLcm(12, 18, 6L, 36L);
        expectGcdLcm(-12, 18, 6L, 36L);
        expectGcdLcm(-12, -18, 6L, 36L);
        expectGcdLcm(21, 6, 3L, 42L);
        expectGcdLcm(35, 64, 1L, 2240L);
        expectGcdLcm(-1, Integer.MIN_VALUE, 1L, 2147483648L);
        expectGcdLcm(Integer.MIN_VALUE, Integer.MIN_VALUE, 2147483648L, 2147483648L);
        expectGcdLcm(Integer.MIN_VALUE, 0, 2147483648L, 0L);
        expectGcdLcm(Integer.MIN_VALUE, Integer.MAX_VALUE, 1L, 4611686016279904256L);
        expectGcdLcm(Integer.MIN_VALUE, Integer.MAX_VALUE - 1, 2L, 2305843007066210304L);
        expectGcdLcm(Integer.MAX_VALUE, Integer.MAX_VALUE - 1, 1L, 4611686011984936962L);
        expectGcdLcm(Integer.MAX_VALUE, Integer.MAX_VALUE, 2147483647L, 2147483647L);
        expectGcdLcm(46337, 46327, 1L, 2146654199L);
    }

    private static void verifyBigIntegerOracle() {
        int[] edges = {Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -65536, -46337, -18, -1,
            0, 1, 2, 18, 46337, 65536, Integer.MAX_VALUE - 1, Integer.MAX_VALUE};
        for (int first : edges) {
            for (int second : edges) {
                compareWithBigInteger(first, second);
            }
        }
        Random random = new Random(20261021L);
        for (int i = 0; i < 256; i++) {
            compareWithBigInteger(random.nextInt(), random.nextInt());
        }
    }

    private static void compareWithBigInteger(int first, int second) {
        BigInteger left = BigInteger.valueOf(first).abs();
        BigInteger right = BigInteger.valueOf(second).abs();
        BigInteger gcd = left.gcd(right);
        BigInteger lcm = gcd.signum() == 0 ? BigInteger.ZERO : left.multiply(right).divide(gcd);
        expectGcdLcm(first, second, gcd.longValueExact(), lcm.longValueExact());
    }

    private static void expectFactors(int number, Integer... expected) {
        check(NumberTheory.primeFactors(number).equals(Arrays.asList(expected)), "prime factors of " + number);
    }

    private static void expectGcdLcm(int first, int second, long gcd, long lcm) {
        check(NumberTheory.gcd(first, second) == gcd, "gcd(" + first + ", " + second + ")");
        check(NumberTheory.lcm(first, second) == lcm, "lcm(" + first + ", " + second + ")");
    }

    private static void check(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError(description);
        }
        checks++;
    }
}
