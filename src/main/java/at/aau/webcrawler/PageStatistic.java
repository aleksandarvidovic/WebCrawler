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

    public int getWordCount() {
        String[] wordsOnWebsite = document.text().split(" ");
        return wordsOnWebsite.length;
    }

    public int getImageCount() {
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

    public int getVideoCount() {
        Elements videos = document.select("[type^=video], .video, video");
        return videos.size();
    }

    public String getSummary() {
        String statistic =
                "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                        + "\nThe website [" + document.baseUri() + "] contains:"
                        + "\n" + getWordCount() + " words, " + getURLS().size() + " links, " + getImageCount() + " images and " + getVideoCount() + " videos."
                        + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";

        return statistic;
    }
}
