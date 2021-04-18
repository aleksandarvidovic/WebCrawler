package at.aau.webcrawler;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.print("Bitte geben Sie die URL der Website ein: ");

        Scanner urlInput = new Scanner(System.in);

        String websiteUrl = urlInput.nextLine();

        urlInput.close();

        WebCrawler webCrawler = new WebCrawler(websiteUrl);
        webCrawler.crawlWebsite(1);

    }
}
