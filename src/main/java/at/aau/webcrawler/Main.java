package at.aau.webcrawler;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> websites = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        String userInput;

        System.out.println("--------------------------------------------"
                + "\nPlease enter your URLS."
                + "\nIf you are done enter d."
                + "\n--------------------------------------------"
                + "\nInput: ");

        while (true) {

            userInput = scanner.nextLine();
            if(userInput.equals("d")) break;
            websites.add(userInput);

        }

        System.out.println("--------------------------------------------"
                + "\nPlease enter a recursion depth");

        int recursionDepth = scanner.nextInt();
        scanner.close();

        new WebCrawler(websites).crawlAllWebsitesFromList(recursionDepth);

    }
}
