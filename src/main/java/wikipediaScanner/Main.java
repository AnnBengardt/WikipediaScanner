package wikipediaScanner;


import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String args[]) throws IOException {
        boolean exit = false;

        while (!exit) {
            final Scanner scanner = new Scanner(System.in);
            System.out.println("\nSearch for......");
            String nextLine = scanner.nextLine();
            if (nextLine != null) {
                String search = nextLine;
                System.out.println("Searching on the web....");
                WikipediaScanner wikipediaScanner = new WikipediaScanner(search);
            } else {
                continue;
            }
        }
    }

}

