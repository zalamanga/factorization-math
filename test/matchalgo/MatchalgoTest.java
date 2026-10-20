package matchalgo;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public final class MatchalgoTest {
    private static final String NEWLINE = System.lineSeparator();
    private static final String MENU = "Math tools" + NEWLINE
            + "1. Prime factorization" + NEWLINE
            + "2. GCD / LCM (FPB / KPK)" + NEWLINE
            + "0. Exit" + NEWLINE + "Choose an option: ";
    private static final String FACTOR_PROMPT = "Enter a positive integer (0 to return to menu): ";
    private static final String FIRST_PROMPT = "Enter the 1st number: ";
    private static final String SECOND_PROMPT = "Enter the 2nd number: ";
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
        String multiple = runConsole("1 46 2 12 18 1 12 0");
        check(multiple.equals(MENU + FACTOR_PROMPT + "Prime factors of 46: 2 23" + NEWLINE
                + MENU + FIRST_PROMPT + SECOND_PROMPT + "GCD (FPB): 6" + NEWLINE
                + "LCM (KPK): 36" + NEWLINE
                + MENU + FACTOR_PROMPT + "Prime factors of 12: 2 2 3" + NEWLINE
                + MENU + "Exiting the program." + NEWLINE), "both calculators repeat through the menu");

        String invalid = runConsole("abc 2.5 2147483648 -2147483649 9 1 oops -5 12 0");
        check(count(invalid, "Invalid input.") == 5, "invalid and out-of-range tokens are consumed");
        check(count(invalid, "Invalid option.") == 1, "unsupported menu option is rejected");
        check(invalid.contains("Please enter a positive integer or 0 to return to menu."),
                "factorization rejects negative input and retries");
        check(invalid.contains("Prime factors of 12: 2 2 3"), "valid factorization works after invalid tokens");
        check(invalid.endsWith(MENU + "Exiting the program." + NEWLINE), "menu exit after invalid inputs");

        String badSecond = runConsole("2 12 bad 2.5 2147483648 18 0");
        check(count(badSecond, FIRST_PROMPT) == 1, "retrying second operand retains first operand");
        check(count(badSecond, SECOND_PROMPT) == 4, "invalid second operand is re-prompted");
        check(badSecond.contains("GCD (FPB): 6" + NEWLINE + "LCM (KPK): 36"),
                "pair calculation recovers after invalid second operands");

        String zero = runConsole("1 0 2 0 18 0");
        check(!zero.contains("Prime factors of 0"), "factorization zero returns to menu");
        check(zero.contains("GCD (FPB): 18" + NEWLINE + "LCM (KPK): 0"),
                "zero is a valid common-factor operand");
        check(count(zero, "Exiting the program.") == 1, "only menu zero exits");

        String signed = runConsole("2 0 0 2 -12 18 2 -2147483648 2147483647 0");
        check(signed.contains("GCD (FPB): 0" + NEWLINE + "LCM (KPK): 0"), "zero pair convention");
        check(signed.contains("GCD (FPB): 6" + NEWLINE + "LCM (KPK): 36"), "signed pair magnitudes");
        check(signed.contains("GCD (FPB): 1" + NEWLINE + "LCM (KPK): 4611686016279904256"),
                "large least common multiple is printed without integer overflow");

        check(runConsole("1 1 0").contains("1 has no prime factors."), "one has no prime factors");
        check(runConsole("1 -2147483648 +0046 0").contains("Prime factors of 46: 2 23"),
                "minimum integer is parsed and rejected only by the factorization domain");
        check(runConsole("\t+0001\r\n+0046 0 ").contains("Prime factors of 46: 2 23"),
                "signed decimal tokens and mixed whitespace");
        check(runConsole("1 2147483647 0").contains("Prime factors of 2147483647: 2147483647"),
                "CLI accepts largest positive int for factorization");
        check(runConsole("").equals(MENU + NEWLINE), "empty input ends cleanly at EOF");
        check(runConsole(" \t\r\n").equals(MENU + NEWLINE), "whitespace-only EOF");
        check(runConsole("1 12").equals(MENU + FACTOR_PROMPT + "Prime factors of 12: 2 2 3" + NEWLINE
                + MENU + NEWLINE), "EOF ends after completed factorization");
        for (String partial : new String[]{"1", "1 nope", "1 -5", "2", "2 12", "2 12 nope"}) {
            String output = runConsole(partial);
            check(output.endsWith(NEWLINE), "EOF terminates partial input: " + partial);
            check(!output.contains("GCD (FPB):") && !output.contains("LCM (KPK):"),
                    "partial pair never produces a result: " + partial);
        }
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
