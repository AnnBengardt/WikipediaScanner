package wikipediaScanner;

import java.util.Scanner;

public class Main {

    public static void main(String args[]) {
        boolean stop = false;

        while (!stop) {
            final Scanner scanner = new Scanner(System.in);
            System.out.println("\nSearch for...");

            // ожидается поисковой запрос на английском (!!)
            String nextLine = scanner.nextLine();
            if (nextLine != null) {
                System.out.println("Searching on the web...");

                // запускаем поиск полученного запроса по википедии
                WikipediaScanner wikipediaScanner = new WikipediaScanner(nextLine);
            }
        }
    }

}

