import java.util.Scanner;

public class WebCrawler {

    public static void main(String[] args) {

        System.out.print("Bitte geben Sie die URL der Website ein: ");

        Scanner urlInput = new Scanner(System.in);

        String websiteUrl = urlInput.nextLine();

        urlInput.close();

        new Website(websiteUrl);
    }
}
