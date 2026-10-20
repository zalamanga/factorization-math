package matchalgo;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public final class MatchalgoTest {
    private static final String NEWLINE = System.lineSeparator();
    private static final String PROMPT = "Enter an integer (0 to exit): ";
    private static int checks;

    public static void main(String[] args) throws Exception {
        verifyFactors();
        verifyConsole();
        verifyBorrowedStreams();
        System.out.println("MatchalgoTest: " + checks + " checks passed.");
    }

    private static void verifyFactors() {
        for (int number : new int[]{Integer.MIN_VALUE, -5, -1, 0, 1}) {
            expectFactors(number);
        }
        expectFactors(2, 2);
        expectFactors(3, 3);
        expectFactors(4, 2, 2);
        expectFactors(12, 2, 2, 3);
        expectFactors(46, 2, 23);
        expectFactors(60, 2, 2, 3, 5);
        expectFactors(225, 3, 3, 5, 5);
        expectFactors(Integer.MAX_VALUE, Integer.MAX_VALUE);
        expectFactors(2147117569, 46337, 46337);
        expectFactors(2146654199, 46327, 46337);
        expectFactors(2145747229, 32749, 65521);
        expectFactors(2147483646, 2, 3, 3, 7, 11, 31, 151, 331);
        check(Matchalgo.findFactors(1073741824).equals(Collections.nCopies(30, 2)), "2^30 multiplicity");
        check(Matchalgo.findFactors(1162261467).equals(Collections.nCopies(19, 3)), "3^19 multiplicity");
        check(Matchalgo.findFactors(1220703125).equals(Collections.nCopies(13, 5)), "5^13 multiplicity");

        List<Integer> first = Matchalgo.findFactors(12);
        first.clear();
        expectFactors(12, 2, 2, 3);
    }

    private static void verifyConsole() throws Exception {
        String multiple = runConsole("46\n12\n0\n");
        check(multiple.equals(PROMPT + "Prime factors of 46: 2 23" + NEWLINE
                + PROMPT + "Prime factors of 12: 2 2 3" + NEWLINE
                + PROMPT + "Exiting the program." + NEWLINE), "multiple queries and zero exit");

        String invalid = runConsole("abc 2.5 2147483648 -2147483649 7 0");
        check(count(invalid, "Invalid input.") == 4, "invalid, noninteger and out-of-range tokens consumed");
        check(invalid.contains("Prime factors of 7: 7"), "valid input works after invalid tokens");
        check(invalid.endsWith(PROMPT + "Exiting the program." + NEWLINE), "zero exits after invalid input");

        String nonpositive = runConsole("1 -5 -2147483648 0");
        check(nonpositive.contains("1 has no prime factors."), "one has no prime factors");
        check(count(nonpositive, "Please enter a positive integer or 0 to exit.") == 2,
                "negative inputs including Integer.MIN_VALUE are rejected by CLI");
        check(!nonpositive.contains("Invalid input."), "Integer.MIN_VALUE is parsed before domain validation");

        check(runConsole("").equals(PROMPT + NEWLINE), "empty input ends cleanly at EOF");
        check(runConsole(" \t\r\n").equals(PROMPT + NEWLINE), "whitespace-only EOF");
        check(runConsole("12").equals(PROMPT + "Prime factors of 12: 2 2 3" + NEWLINE
                + PROMPT + NEWLINE), "EOF ends after a query without zero sentinel");
        check(runConsole("oops").endsWith(PROMPT + NEWLINE), "EOF ends after invalid input");
        check(runConsole("\t+0046\r\n0 ").contains("Prime factors of 46: 2 23"),
                "signed decimal token and mixed whitespace");
        check(runConsole("2147483647 0").contains("Prime factors of 2147483647: 2147483647"),
                "CLI accepts Integer.MAX_VALUE");
    }

    private static void verifyBorrowedStreams() throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (Scanner input = new Scanner("0 ignored");
                PrintStream output = new PrintStream(buffer, true, "UTF-8")) {
            Matchalgo.run(input, output);
            check(input.next().equals("ignored"), "zero exit leaves trailing input and scanner open");
            output.print("still open");
            check(buffer.toString("UTF-8").endsWith("still open"), "run leaves caller output open");
        }
    }

    private static String runConsole(String text) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (Scanner input = new Scanner(text);
                PrintStream output = new PrintStream(buffer, true, "UTF-8")) {
            Matchalgo.run(input, output);
        }
        return buffer.toString("UTF-8");
    }

    private static void expectFactors(int number, Integer... expected) {
        check(Matchalgo.findFactors(number).equals(Arrays.asList(expected)), "prime factors of " + number);
    }

    private static int count(String text, String fragment) {
        int matches = 0;
        for (int index = text.indexOf(fragment); index >= 0;
                index = text.indexOf(fragment, index + fragment.length())) {
            matches++;
        }
        return matches;
    }

    private static void check(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError(description);
        }
        checks++;
    }
}
