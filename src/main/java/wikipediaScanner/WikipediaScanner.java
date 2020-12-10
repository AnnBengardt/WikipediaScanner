package wikipediaScanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


public class WikipediaScanner {
    String request;

    public WikipediaScanner(String request) throws IOException {
        this.request = request;
        this.GET(request);
    }

    public void GET(String request) {
        final String encoding = "UTF-8";
        Document search;

        assert request != null;
        request = request + " wikipedia";

        try {
            System.out.println(request);
            search = Jsoup.connect("https://www.google.com/search?q="
                    + URLEncoder.encode(request, encoding)).userAgent("Chrome/87.0.4280.88").get();

            String wikipediaURL = search.getElementsByTag("a").get(15).text();
            System.out.println(wikipediaURL);

            String wikipediaJson = "https://www.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles="
                    + wikipediaURL.substring(0, wikipediaURL.indexOf("-")-1).replaceAll(" ", "_");

            System.out.println(wikipediaJson);

            HttpURLConnection http = (HttpURLConnection) new URL(wikipediaJson).openConnection();
            http.addRequestProperty("User-Agent", "Chrome/87.0.4280.88");
            BufferedReader inquery = new BufferedReader(new InputStreamReader(http.getInputStream()));

            String response = inquery.lines().collect(Collectors.joining());
            inquery.close();

            String result = response.split("extract\":\"")[1];
            result = result.replaceAll("}", "");
            System.out.println(result);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
