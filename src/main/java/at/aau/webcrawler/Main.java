package at.aau.webcrawler;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner urlInput = new Scanner(System.in);

        System.out.print("Bitte geben Sie die URL der ersten Website ein: ");
        String firstWebsiteUrl = urlInput.nextLine();
        WebCrawler webCrawlerOne = new WebCrawler(firstWebsiteUrl);

        System.out.print("Bitte geben Sie die URL der zweiten Website ein: ");
        String secondWebsiteUrl = urlInput.nextLine();
        WebCrawler webCrawlerTwo = new WebCrawler(secondWebsiteUrl);

        System.out.print("Bitte geben Sie die URL der dritten Website ein: ");
        String thirdWebsiteUrl = urlInput.nextLine();
        WebCrawler webCrawlerThree = new WebCrawler(thirdWebsiteUrl);

        urlInput.close();

        new Thread(() -> {

            webCrawlerOne.crawlWebsite(1);

        }).start();

        new Thread(() -> {

            webCrawlerTwo.crawlWebsite(1);

        }).start();

        new Thread(() -> {

            webCrawlerThree.crawlWebsite(1);

        }).start();
    }
}
