import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Website {

    private final String websiteUrl;
    private String failConnectionMessage;
    private Document document;

    Website(String websiteUrl){

        this.websiteUrl = websiteUrl;

        failConnectionMessage = null;

        recursivelyAnalysis(2, websiteUrl, new ArrayList<>());
    }

    private Document connectToWebsite(String websiteUrl, ArrayList<String> allWebsitesUrl){

        try {

            document = Jsoup.connect(websiteUrl).get();
            analyzeWebsite();
            allWebsitesUrl.add(websiteUrl);
            return document;

        } catch (Exception e) {

            failConnectionMessage = e.getMessage();
            return null;
        }
    }

    private int countWordsInString(String string) {

        if(string == null || string.isEmpty())
            return 0;

        String[] words = string.split("\\s+");

        return words.length;
    }

    private int countWordsOnWebsite() {

        if(document == null)
            return 0;

        Element websiteBody = document.body();
        String websiteText = websiteBody.text();

        return countWordsInString(websiteText);
    }

    private int countLinksOnWebsite() {

        if(document == null)
            return 0;

        Elements links = document.select("a[href]");

        return links.size();
    }

    private int countImagesOnWebsite(){

        if(document == null)
            return 0;

        Elements images = document.getElementsByTag("img");

        return images.size();
    }

    private int countVideosOnWebsite(){

        if(document == null)
            return 0;

        Elements videos = document.getElementsByTag("video");

        return videos.size();
    }

    private void analyzeWebsite(){

        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        if(document == null){

            System.out.println("[" + websiteUrl + "]");
            System.out.println("Verbindung konnte nicht hergestellt werden -> " + failConnectionMessage);
        }
        else {

            System.out.println("Die Website [" + document.title() + "] enthält:");
            System.out.println(countWordsOnWebsite() + " Wörter, " + countLinksOnWebsite() + " Links, " + countImagesOnWebsite() + " Bilder und " + countVideosOnWebsite() + " Videos.");
        }

        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private void recursivelyAnalysis(int analyzeDepth, String websiteUrl, ArrayList<String> allWebsitesUrl){

        if(analyzeDepth >= 1){

            Document document = connectToWebsite(websiteUrl, allWebsitesUrl);

            if(document != null){

                for(Element link : document.select("a[href]")){

                    String nextLink = link.absUrl("href");

                    if(!allWebsitesUrl.contains(nextLink)){

                        recursivelyAnalysis(analyzeDepth--, nextLink, allWebsitesUrl);
                    }
                }
            }

            else
                analyzeWebsite();
        }
    }
}
