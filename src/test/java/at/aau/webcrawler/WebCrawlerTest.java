package at.aau.webcrawler;

import at.aau.webcrawler.WebCrawler;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.mockito.Mockito.*;


public class WebCrawlerTest {
    Document document;
    WebCrawler webCrawler;
    File report = new File("target/generated-sources/CrawlResults.txt");;
    Connection connection = mock(Connection.class);


    MockedStatic<Jsoup> jsoupMocked;

    public void initJsoupStaticMock() {
        jsoupMocked = Mockito.mockStatic(Jsoup.class);
        jsoupMocked.when(() -> Jsoup.connect(Matchers.anyString())).thenReturn(connection);
    }

    public void initDocument() throws IOException {
        File input = new File("src/test/java/at/aau/webcrawler/TestDoc.html");
        document = Jsoup.parse(input, "UTF-8", "https://www.htl-villach.at/");
    }

    public void initConnection() throws IOException {
        when(connection.get()).thenReturn(document);
    }

    @BeforeEach
    public void setUp() throws IOException {
        initDocument();
        initJsoupStaticMock();
        initConnection();
        WebCrawler.getVisitedWebsites().clear();
        webCrawler = new WebCrawler(null);
        CrawlReport.getInstance().clearReport();

    }

    @AfterEach
    public void tearDown() {
        jsoupMocked.close();
        jsoupMocked = null;
        document = null;
        Mockito.reset(connection);
    }


    @Test
    public void visitedWebsiteCount() throws IOException {
        WebCrawler.getVisitedWebsites().clear();
        webCrawler.crawlWebsite("https://www.htl-villach.at/", 0);
        Assertions.assertEquals(1, WebCrawler.getVisitedWebsites().size());
        verify(connection,times(1)).get();
    }

    @Test
    public void correctFileOutPut() throws IOException {
        webCrawler.crawlWebsite("https://www.htl-villach.at/", 0);
        String reportContent = Files.readString(Path.of(report.getPath()), StandardCharsets.UTF_8);
        String expectedOutput ="-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                + "\nThe website [https://www.htl-villach.at/] contains:"
                + "\n709 words, 58 links, 4 images and 1 videos."
                + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
        Assertions.assertEquals(expectedOutput, reportContent);
        verify(connection,times(1)).get();
    }

    @Test
    public void getDocument() throws IOException {
        Document doc = webCrawler.getDocument("https://www.htl-villach.at/");
        Assertions.assertEquals(document, doc);
        verify(connection,times(1)).get();
    }

    @Test
    public void appendBrokenPageInfo() throws IOException {
        String url = "error404.com";
        when(connection.get()).thenThrow(new IOException());
        webCrawler.crawlWebsite(url,0);
        String reportContent = Files.readString(Path.of(report.getPath()), StandardCharsets.UTF_8);
        String expectedOutput ="-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                + "\nAn error occurred. There are no information available for " + url
                + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
        Assertions.assertEquals(expectedOutput,reportContent);
        verify(connection, times(1)).get();

    }

    @Test
    public void appendInvalidUrlInformation() throws IOException {
        String urlInput = "noUrl";
        when(connection.get()).thenThrow(new IllegalArgumentException());
        webCrawler.crawlWebsite(urlInput,0);
        String reportContent = Files.readString(Path.of(report.getPath()), StandardCharsets.UTF_8);
        String expectedOutput = "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                + "\nThis url has no valid format! " + urlInput
                + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
        Assertions.assertEquals(expectedOutput,reportContent);
        verify(connection, times(1)).get();
    }

}


