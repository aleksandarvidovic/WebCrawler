import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class WebCrawler {

    private static Document connectToWebsite(String websiteUrl){

        try {

            Document document = Jsoup.connect(websiteUrl).get();
            return document;

        } catch (IOException e) {

            System.out.println(e.getMessage());
            return null;
        }
    }

    private static int countWordsInString(String string) {

        if(string == null || string.isEmpty())
            return 0;

        String[] words = string.split("\\s+");

        return words.length;
    }

    private static int countWordsOnWebsite(String websiteUrl) {

        Document document = connectToWebsite(websiteUrl);

        Element websiteBody = document.body();
        String websiteText = websiteBody.text();

        return countWordsInString(websiteText);
    }

    private static int countLinksOnWebsite(String websiteUrl) {

        Document document = connectToWebsite(websiteUrl);

        Elements links = document.select("a[href]");

        return links.size();
    }

    public static void main(String[] args) {

        System.out.print("Bitte geben Sie die URL der Website ein: ");

        Scanner urlInput = new Scanner(System.in);

        String websiteUrl = urlInput.nextLine();

        System.out.println("Die Website enthält " + countWordsOnWebsite(websiteUrl) + " Wörter, " + countLinksOnWebsite(websiteUrl) + " Links.");
    }
}
