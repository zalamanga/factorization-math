
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import matchalgo.Matchalgo;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MatchalgoTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testFindFactorsForPositiveNumber() {
        List<Integer> expectedFactors = Arrays.asList(2, 23);
        List<Integer> actualFactors = Matchalgo.findFactors(46);
        assertEquals(expectedFactors, actualFactors);
    }

    @Test
    public void testFindFactorsForOne() {
        List<Integer> expectedFactors = Arrays.asList();
        List<Integer> actualFactors = Matchalgo.findFactors(1);
        assertEquals(expectedFactors, actualFactors);
    }

    @Test
    public void testFindFactorsForPrimeNumber() {
        List<Integer> expectedFactors = Arrays.asList(5);
        List<Integer> actualFactors = Matchalgo.findFactors(5);
        assertEquals(expectedFactors, actualFactors);
    }


    @Test
    public void testFindFactorsForNegativeNumber() {
        List<Integer> expectedFactors = Arrays.asList();
        List<Integer> actualFactors = Matchalgo.findFactors(-5);
        assertEquals(expectedFactors, actualFactors);
    }

    @Test
    public void testFindFactorsForZero() {
        List<Integer> expectedFactors = Arrays.asList();
        List<Integer> actualFactors = Matchalgo.findFactors(0);
        assertEquals(expectedFactors, actualFactors);
    }

    @Test
    public void testMainWithExit() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Matchalgo.main(null);

        String expectedOutput = "Enter the number (0 to exit): Exiting the program." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testMainWith1() {
        String input = "1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Matchalgo.main(null);

        String expectedOutput = "Enter the number (0 to exit): Sorry, 1 doesn't have any factors." + System.lineSeparator()
                + "Enter the number (0 to exit): Exiting the program." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testMainWithNegativeNumber() {
        String input = "-5\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Matchalgo.main(null);

        String expectedOutput = "Enter the number (0 to exit): Please enter a non-negative number." + System.lineSeparator()
                + "Enter the number (0 to exit): Exiting the program." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }
}
