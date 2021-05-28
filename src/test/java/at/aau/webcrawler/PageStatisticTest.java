package at.aau.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PageStatisticTest {
    Document document;
    PageStatistic pageStatistic;

    public void initDocument() throws IOException {
        File input = new File("src/test/java/at/aau/webcrawler/TestDoc.html");
        document = Jsoup.parse(input, "UTF-8","https://www.htl-villach.at/");
    }

    @BeforeEach
    public void setUp() throws IOException {
        initDocument();
        pageStatistic = new PageStatistic(document);
    }

    @AfterEach
    public void tearDown(){
        document=null;
        pageStatistic=null;
    }

    @Test
    public void imageCount() {
        Assertions.assertEquals(4, pageStatistic.getImageCount());
    }

    @Test
    public void videoCount() {
        Assertions.assertEquals(1, pageStatistic.getVideoCount());
    }

    @Test
    public void wordCount() {
        Assertions.assertEquals(709, pageStatistic.getWordCount());
    }

    @Test
    public void urlsFoundOnPage(){
        ArrayList<String> urlsOnWebSite = pageStatistic.getURLS();
        Assertions.assertEquals(58, urlsOnWebSite.size());
    }

    @Test
    public void isSummaryCorrect(){
        String summary = pageStatistic.getSummary();
        //String expectedOutput =
        //Assertions.assertEquals(summary, );
    }
}
