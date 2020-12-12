package wikipediaScanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


public class WikipediaScanner {
    String request;
    String wikipediaJson = "";
    String apiURL = "https://www.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=";
    String randomURL = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&generator=random&grnnamespace=0";

    public WikipediaScanner(String request) {
        this.request = request;
        this.GET(request);
    }

    public void GET(String request) {
        final String encoding = "UTF-8";
        Document search;

        assert request != null;
        request = request + " wikipedia";

        try {
            search = Jsoup.connect("https://www.google.com/search?q="
                    + URLEncoder.encode(request, encoding)).userAgent("Chrome/87.0.4280.88").get();

            String wikipediaURL = search.getElementsByTag("a").get(16).text();

            if (wikipediaURL.indexOf("-") != -1){
                wikipediaJson = apiURL + wikipediaURL.substring(0, wikipediaURL.indexOf("-")-1).replaceAll(" ", "_");
            } else {
                System.out.println("Nothing found but read this:");
                wikipediaJson = randomURL;
            }

            HttpURLConnection http = (HttpURLConnection) new URL(wikipediaJson).openConnection();
            http.addRequestProperty("User-Agent", "Chrome/87.0.4280.88");
            BufferedReader inquiry = new BufferedReader(new InputStreamReader(http.getInputStream()));

            String response = inquiry.lines().collect(Collectors.joining());
            inquiry.close();

            String result = response.split("extract\":\"")[1];
            result = result.replaceAll("}", "");
            System.out.println(result.replaceAll("(.{125})", "$1\n"));
            wikipediaURL = "https://en.wikipedia.org/wiki/"
                    +response.split("title\":\"")[1].split("\",")[0].replaceAll(" ", "_");
            System.out.println("Read more here:");
            System.out.println(wikipediaURL);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
