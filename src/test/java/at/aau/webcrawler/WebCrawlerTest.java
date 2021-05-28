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
import java.util.ArrayList;

import static org.mockito.Mockito.*;


public class WebCrawlerTest {
    Document document;
    WebCrawler webCrawler;
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
        WebCrawler.resetVisitedWebsites();

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
  
    }


}


