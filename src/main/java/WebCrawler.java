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

    WebCrawler(String websiteUrl){
        this.websiteUrl = websiteUrl;
        urlsOnWebsite = new ArrayList<>();
        visitedWebsites.add(websiteUrl);
    }

    public boolean connectToWebsite(){
        try {
            document = Jsoup.connect(websiteUrl).get();
        } catch (Exception e) {
            connectionMessage = e.getMessage();
        }
        return document != null;
    }

    public int countWordsOnWebsite() {
        String[] wordsOnWebsite = Jsoup.parse(document.outerHtml()).text().split(" ");
        return wordsOnWebsite.length;
    }

    public int countImagesOnWebsite(){
        Elements images = document.getElementsByTag("img");
        return images.size();
    }

    public int countVideosOnWebsite(){
        Elements videos = document.getElementsByTag("video");
        return videos.size();
    }

    public void printInformationAboutWebsite(){
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Die Website [" + websiteUrl + "] enthält:");
        System.out.println(countWordsOnWebsite() + " Wörter, " + urlsOnWebsite.size() + " Links, " + countImagesOnWebsite() + " Bilder und " + countVideosOnWebsite() + " Videos.");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void scrapeURLSFromWebsite(){
        Elements links = document.select("a[href]");
        for (Element link : links) {
            urlsOnWebsite.add(link.absUrl("href"));
        }
    }

    public void crawlWebsite(int recursionDepth) {
        if (recursionDepth < 0) return;
        if (!connectToWebsite()) {
            System.out.println("Verbindung zu dieser URL nicht möglich: " + websiteUrl + " -> " + connectionMessage + "\n");
            return;
        }
        scrapeURLSFromWebsite();
        printInformationAboutWebsite();
        for (String link : urlsOnWebsite) {
            if (!visitedWebsites.contains(link)) {
                new WebCrawler(link).crawlWebsite(recursionDepth - 1);
            }
        }
    }

    public ArrayList<String> getUrlsOnWebsite(){
        return urlsOnWebsite;
    }

    public static HashSet<String> getVisitedWebsites() {
        return visitedWebsites;
    }
}