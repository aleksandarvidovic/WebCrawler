package at.aau.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;



public class CrawlReportTest {
    CrawlReport crawlReport = CrawlReport.getInstance();
    File report;

    @BeforeEach
    public void setUp() throws IOException {
        crawlReport.clearReport();
        report = new File("target/generated-sources/CrawlResults.txt");
    }


    @Test
    public void singletonConstructor(){
        CrawlReport.destroy();
        Assertions.assertNotNull(CrawlReport.getInstance());
    }

    @Test
    public void clearReport() throws IOException {
        FileWriter fileWriter = new FileWriter(report.getPath(), true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        String test = "This is a test";
        bufferedWriter.write(test);
        bufferedWriter.flush();

        String reportContent;

        reportContent = Files.readString(Path.of(report.getPath()), StandardCharsets.UTF_8);
        Assertions.assertEquals(test, reportContent);

        crawlReport.clearReport();

        reportContent = Files.readString(Path.of(report.getPath()), StandardCharsets.UTF_8);
        Assertions.assertEquals("", reportContent);
    }

    @Test
    public void resetReportWhenNewInstance() throws IOException {
        String reportContent = Files.readString(Path.of(report.getPath()),StandardCharsets.UTF_8);
        Assertions.assertEquals("", reportContent);

        crawlReport.appendBrokenPageInformation("noUrl");
        reportContent =  Files.readString(Path.of(report.getPath()),StandardCharsets.UTF_8);
        Assertions.assertNotEquals("",reportContent);

        CrawlReport.destroy();
        crawlReport = CrawlReport.getInstance();
        reportContent = Files.readString(Path.of(report.getPath()),StandardCharsets.UTF_8);
        Assertions.assertEquals("", reportContent);

    }

    @Test
    public void appendValidStatistic() throws IOException {
        File input = new File("src/test/java/at/aau/webcrawler/TestDoc.html");
        Document document= Jsoup.parse(input, "UTF-8","https://www.htl-villach.at/");
        PageStatistic pageStatistic = new PageStatistic(document);

        crawlReport.appendPageStatistics(pageStatistic);

        String reportContent = Files.readString(Path.of(report.getPath()),StandardCharsets.UTF_8);
        String expectedOutput ="-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                + "\nThe website [" + document.baseUri() + "] contains:"
                + "\n" + pageStatistic.getWordCount() + " words, " + pageStatistic.getURLS().size()
                + " links, " + pageStatistic.getImageCount() + " images and " + pageStatistic.getVideoCount() + " videos."
                + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";

        Assertions.assertEquals(expectedOutput,reportContent);
    }

    @Test
    public void appendInvalidUrlInformation() throws IOException {
        String invalidLink = "thisIsNotALink";
        crawlReport.appendInvalidUrlInformation(invalidLink);
        String reportContent = Files.readString(Path.of(report.getPath()),StandardCharsets.UTF_8);
        String expectedOutput = "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                + "\nThis url has no valid format! " + invalidLink
                + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
        Assertions.assertEquals(expectedOutput, reportContent);
    }

    @Test
    public void appendBrokenPageInformation() throws IOException {
        String testLink = "error404.com";
        crawlReport.appendBrokenPageInformation(testLink);
        String reportContent = Files.readString(Path.of(report.getPath()),StandardCharsets.UTF_8);
        String expectedOutput ="-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                + "\nAn error occurred. There are no information available for " + testLink
                + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
        Assertions.assertEquals(expectedOutput, reportContent);
    }
}
