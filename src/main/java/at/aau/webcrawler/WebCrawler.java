package at.aau.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;

public class WebCrawler {

    private ArrayList<String> urlsOnWebsite;
    private String websiteUrl;
    private static HashSet<String> visitedWebsites = new HashSet<>();
    private Document document;
    private String connectionMessage;
    private ArrayList<String> websitesList;

    WebCrawler(String websiteUrl) {
        this.websiteUrl = websiteUrl;
        urlsOnWebsite = new ArrayList<>();
    }

    WebCrawler(ArrayList<String> websitesList) {

        this.websitesList = websitesList;
    }

    public boolean connectToWebsite() {
        try {
            document = Jsoup.connect(websiteUrl).get();
        } catch (Exception e) {
            connectionMessage = e.getMessage();
        }
        visitedWebsites.add(websiteUrl);
        return document != null;
    }

    public int countWordsOnWebsite() {
        String[] wordsOnWebsite = document.text().split(" ");
        return wordsOnWebsite.length;
    }

    public int countImagesOnWebsite() {
        Elements images = document.getElementsByTag("img");
        return images.size();
    }

    public int countVideosOnWebsite() {
        Elements videos = document.select("[type^=video], .video, video");
        return videos.size();
    }

    public void printInformationAboutWebsite() {
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Die Website [" + websiteUrl + "] enthält:");
        System.out.println(countWordsOnWebsite() + " Wörter, " + urlsOnWebsite.size() + " Links, " + countImagesOnWebsite() + " Bilder und " + countVideosOnWebsite() + " Videos.");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void scrapeURLSFromWebsite() {
        Elements links = document.select("a[href]");
        for (Element link : links) {
            urlsOnWebsite.add(link.absUrl("href"));
        }
    }

    public void crawlWebsite(int recursionDepth) {
        if (recursionDepth < 0) return;
        if (!connectToWebsite()) {
            System.out.print("Verbindung zu dieser URL nicht möglich: " + websiteUrl + " -> " + connectionMessage + "\n");
            return;
        }
        scrapeURLSFromWebsite();
        printInformationAboutWebsite();

        for (String link : urlsOnWebsite) {
            if (!visitedWebsites.contains(link) && recursionDepth > 0) {
                new WebCrawler(link).crawlWebsite(recursionDepth - 1);
            }
        }
    }

    public void crawlAllWebsitesFromList(int recursionDepth){

        for(String website : websitesList)
            new Thread(() -> new WebCrawler(website).crawlWebsite(recursionDepth)).start();
    }

    public ArrayList<String> getUrlsOnWebsite() {
        return urlsOnWebsite;
    }

    public static HashSet<String> getVisitedWebsites() {
        return visitedWebsites;
    }

    public static void resetVisitedWebsites() {
        visitedWebsites = new HashSet<>();
    }
}