package at.aau.webcrawler;

import java.io.*;
import java.util.logging.Logger;

public class CrawlReport {
    private static CrawlReport instance;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private File report;
    private Logger logger;

    private CrawlReport() {
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        report = new File("target/generated-sources/CrawlResults.txt");
        clearReport();

        try {
            fileWriter = new FileWriter(report.getPath(), true);
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            logger.info("Creating FileWriter failed. "+e.getMessage());
        }
    }

    public static CrawlReport getInstance() {
        if (CrawlReport.instance == null) {
            CrawlReport.instance = new CrawlReport();
        }
        return CrawlReport.instance;
    }

    public static void destroy(){
        instance = null;
    }


    public void clearReport() {
        try {
            new FileWriter(report.getPath());
        } catch (IOException e) {
            logger.info("Clearing the report failed. "+e.getMessage());
        }
    }

    public void appendPageStatistics(PageStatistic websiteStats) {
        try {
            bufferedWriter.write(websiteStats.getSummary());
            bufferedWriter.flush();
        } catch (IOException e) {
            logger.info("Appending statistic to file failed. "+e.getMessage());
        }

    }

    public void appendBrokenPageInformation(String url) {
        try {
            bufferedWriter.write(
                    "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                            + "\nAn error occurred. There are no information available for " + url
                            + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            bufferedWriter.flush();

        } catch (IOException e) {
            logger.info("Writing to file failed. "+e.getMessage());
        }
    }

    public void appendInvalidUrlInformation(String url) {
        try {
            bufferedWriter.write(
                    "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                            + "\nThis url has no valid format! " + url
                            + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            bufferedWriter.flush();

        } catch (IOException e) {
            logger.info("Writing to file failed. "+e.getMessage());
        }
    }

}
