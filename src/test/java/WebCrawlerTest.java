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

import static org.mockito.Mockito.*;


public class WebCrawlerTest {
    Document document;
    WebCrawler webCrawler;
    Connection connection = mock(Connection.class);;
    MockedStatic<Jsoup> jsoupMocked;

    public void initJsoupStaticMock(){
        jsoupMocked = Mockito.mockStatic(Jsoup.class);;
        jsoupMocked.when(() -> Jsoup.connect("https://www.htl-villach.at/schule/anmeldung")).thenReturn(connection);
    }

    public void initDocument() throws IOException {
        File input = new File("src/test/java/TestDoc.html");
        document = Jsoup.parse(input, "UTF-8", "");
    }

    public void initConnection() throws IOException {
        when(connection.get()).thenReturn(document);
    }

    @BeforeEach
    public void setUp() throws IOException {
        initDocument();
        initJsoupStaticMock();
        initConnection();
        webCrawler = new WebCrawler("https://www.htl-villach.at/schule/anmeldung");
        webCrawler.connectToWebsite();
    }

    @AfterEach
    public void tearDown(){
        if(!jsoupMocked.isClosed())jsoupMocked.close();
        jsoupMocked=null;
        document=null;
        Mockito.reset(connection);
    }

    @Test
    public void imageCount() {
        Assertions.assertEquals(4, webCrawler.countImagesOnWebsite());
    }

    @Test
    public void videoCount() {
        //countVideos funktioniert noch nicht richtig
        //Assertions.assertEquals(-, webCrawler.countVideosOnWebsite());
    }

    @Test
    public void wordCount(){
        jsoupMocked.close();
        Assertions.assertEquals(709, webCrawler.countWordsOnWebsite());
    }

    @Test
    public void urlsFoundOnWebsite(){
        webCrawler.scrapeURLSFromWebsite();
        Assertions.assertEquals(58, webCrawler.getUrlsOnWebsite().size());
    }

    @Test
    public void crawlWithRecursionDepthLessThan0(){
        jsoupMocked.close();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        webCrawler.crawlWebsite(-1);
        Assertions.assertEquals(0,outContent.size());
    }


    @Test
    public void cannotConnectToWebsite() throws IOException {
        webCrawler = new WebCrawler("https://www.htl-villach.at/schule/anmeldung");
        when(connection.get()).thenThrow(new IOException());
        Assertions.assertFalse(webCrawler.connectToWebsite());
        verify(connection, times(2)).get();
    }

    @Test
    public void visitedWebsitesWithRecursionDepth0(){
        jsoupMocked.close();
        WebCrawler.resetVisitedWebsites();
        webCrawler = new WebCrawler("https://www.htl-villach.at/schule/anmeldung");
        webCrawler.crawlWebsite(0);
        Assertions.assertEquals(1,WebCrawler.getVisitedWebsites().size());
    }


    @Test
    public void crawlBrokenLink() throws IOException {
        webCrawler = new WebCrawler("https://www.htl-villach.at/schule/anmeldung");
        when(connection.get()).thenThrow(new IOException("HTTP error fetching URL"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String expectedOutput = "Verbindung zu dieser URL nicht möglich: https://www.htl-villach.at/schule/anmeldung -> HTTP error fetching URL\n";
        webCrawler.crawlWebsite(2);

        Assertions.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void correctOutputAfterCrawling(){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String expectedOutput =
                "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\r\n"
                        + "Die Website [https://www.htl-villach.at/schule/anmeldung] enthält:\r\n"
                        + "709 Wörter, 58 Links, 4 Bilder und 0 Videos.\r\n"
                        + "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\r\n";

        jsoupMocked.close();
        webCrawler.crawlWebsite(0);

        Assertions.assertEquals(expectedOutput, outContent.toString());
    }
}
