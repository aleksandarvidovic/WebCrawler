package at.aau.webcrawler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class PageStatistic {
    private Document document;

    public PageStatistic(Document document) {
        this.document = document;
    }

    public int countWords() {
        String[] wordsOnWebsite = document.text().split(" ");
        return wordsOnWebsite.length;
    }

    public int countImages() {
        Elements images = document.getElementsByTag("img");
        return images.size();
    }

    public ArrayList<String> getURLS() {
        ArrayList<String> urlsOnWebsite = new ArrayList<>();
        Elements links = document.select("a[href]");
        for (Element link : links) {
            urlsOnWebsite.add(link.absUrl("href"));
        }

        return urlsOnWebsite;
    }

    public int countVideos() {
        Elements videos = document.select("[type^=video], .video, video");
        return videos.size();
    }

    public String getSummary() {
        String statistic =
                "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                        + "\nThe website [" + document.baseUri() + "] contains:"
                        + "\n" + countWords() + " words, " + getURLS().size() + " links, " + countImages() + " words and " + countVideos() + " videos."
                        + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";

        return statistic;
    }
}
