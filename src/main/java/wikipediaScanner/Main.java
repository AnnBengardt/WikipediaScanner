package wikipediaScanner;

import java.util.Scanner;

public class Main {

    public static void main(String args[]) {
        boolean stop = false;

        while (!stop) {
            final Scanner scanner = new Scanner(System.in);
            System.out.println("\nSearch for...");
            String nextLine = scanner.nextLine();
            if (nextLine != null) {
                System.out.println("Searching on the web....");
                WikipediaScanner wikipediaScanner = new WikipediaScanner(nextLine);
            }
        }
    }

}

