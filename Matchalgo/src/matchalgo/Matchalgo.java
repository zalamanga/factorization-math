package matchalgo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Matchalgo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("Enter the number (0 to exit): ");
            int number = scanner.nextInt();
            
            if (number == 0) {
                System.out.println("Exiting the program.");
                break;
            }
            
            if (number == 1) {
                System.out.println("Sorry, 1 doesn't have any factors.");
            } else if (number < 0) {
                System.out.println("Please enter a non-negative number.");
            } else {
                System.out.println("The factors of " + number + " are:");
                List<Integer> factors = findFactors(number);
                for (int factor : factors) {
                    System.out.println(factor);
                }
            }
        }
        
        scanner.close();
    }

    public static List<Integer> findFactors(int number) {
        List<Integer> factors = new ArrayList<>();
        
        for (int i = 2; i <= number; i++) {
            while (number % i == 0) {
                factors.add(i);
                number /= i;
            }
        }
        
        return factors;
    }

   
  
}
