package at.aau.webcrawler;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner urlInput = new Scanner(System.in);
        String website;
        ArrayList<String> webistesList = new ArrayList<>();

        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Bitte geben Sie die URLs der Websites ein.");
        System.out.println("Um die Eingabe zu beenden, geben Sie q ein.");
        System.out.println();

        while (true){

            System.out.print("Eingabe: ");
            website = urlInput.nextLine();

            if(website.equals("q")){

                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                break;
            }

            webistesList.add(website);
        }

        if(webistesList.isEmpty()){

            urlInput.close();
            System.out.println("Sie haben keine URLs eingegeben.");
        }
        else{

            System.out.print("Geben Sie recursion depth ein: ");
            int recursionDepth = urlInput.nextInt();
            urlInput.close();
            new WebCrawler(webistesList).crawlAllWebsitesFromList(recursionDepth);
        }
    }
}
