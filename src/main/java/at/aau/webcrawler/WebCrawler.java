package at.aau.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class WebCrawler {

    private static HashSet<String> visitedWebsites = new HashSet<>();
    private ArrayList<String> urlsToCrawl;
    private CrawlReport crawlReport;

    WebCrawler(ArrayList<String> urlsToCrawl) {
        this.urlsToCrawl = urlsToCrawl;
        this.crawlReport = CrawlReport.getInstance();
    }

    public void crawlWebsite(String url, int recursionDepth) {
        if (recursionDepth < 0) return;

        Document document = getDocument(url);

        if (document != null) {
            visitedWebsites.add(url);
            PageStatistic pageStatistic = new PageStatistic(document);
            crawlReport.appendPageStatistics(pageStatistic);
            crawlUrlsRecursively(pageStatistic, recursionDepth-1);
        }
    }

    public void crawlUrlsRecursively(PageStatistic pageStatistic, int recursionDepth){
        for (String link : pageStatistic.getURLS()) {
            if (!visitedWebsites.contains(link)) crawlWebsite(link, recursionDepth);
        }
    }

    public void startThreadedCrawl(int recursionDepth) {
        for (String url : urlsToCrawl)
                new Thread(() -> crawlWebsite(url, recursionDepth)).start();
    }

    public Document getDocument(String url) {
        Document document = null;

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            crawlReport.appendBrokenPageInformation(url);
        } catch(IllegalArgumentException e){
            crawlReport.appendInvalidUrlInformation(url);
        }
        return document;
    }

    public static HashSet<String> getVisitedWebsites() {
        return visitedWebsites;
    }

}