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
    // переменные для запроса пользователя и ответа API Википедии
    String request;
    String wikipediaJson = "";

    // Ссылки для получения статей с API
    String apiURL = "https://www.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=";
    String randomURL = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&generator=random&grnnamespace=0";

    public WikipediaScanner(String request) {
        this.request = request;
        this.GET(request);
    }

    public void GET(String request) {
        final String encoding = "UTF-8";
        Document search;

        /*
        Здесь проверка корректности запроса пользователя и добавление к нему слова "wikipedia"
        Мне показалось, что находить статью в поисковике и парсить её название по первой ссылке результата поиска
        будет более гибким решением, чем просить у пользователя точное название статьи. Таким образом, можно вбивать
        в программу более "очеловеченные" запросы. Например, "что такое солнце"? Или "кто такой Иван Грозный"?
         */
        assert request != null;
        request = request + " wikipedia";

        try {
            // запрос к гуглу
            search = Jsoup.connect("https://www.google.com/search?q="
                    + URLEncoder.encode(request, encoding)).userAgent("Chrome/87.0.4280.88").get();

            // на обычной странице с результатами поиска ссылки находятся в тэге <h3></h3>
            String wikipediaURL = search.getElementsByTag("h3").get(0).text();
            /*
            с помощью инструментов строк берём из тэга заголовок статьи, если же нашлось что-то неверное или ничего не
            нашлось, то программа предложит пользователю рандомную статью, полученную тоже с помощью API.
             */
            if (wikipediaURL.contains(" - Wikipedia")) {
                wikipediaJson = apiURL + wikipediaURL.split(" - Wikipedia")[0].replaceAll(" ", "_");
            } else if (wikipediaURL.contains(" — Википедия")) {
                wikipediaURL = search.getElementsByTag("h3").get(1).text();
                wikipediaJson = apiURL + wikipediaURL.split(" - Wikipedia")[0].replaceAll(" ", "_");
            } else {
                    System.out.println("Nothing found but read this:");
                    wikipediaJson = randomURL;}

            // GET-запрос по ссылке для API, добавляем устройство
            HttpURLConnection http = (HttpURLConnection) new URL(wikipediaJson).openConnection();
            http.addRequestProperty("User-Agent", "Chrome/87.0.4280.88");
            // получаем содержимое страницы и прочитываем его с помощью BufferedReader (всё читается как стрим)
            BufferedReader inquiry = new BufferedReader(new InputStreamReader(http.getInputStream()));

            // читаем все полученные строки и записываем в переменную
            String response = inquiry.lines().collect(Collectors.joining());
            inquiry.close();

            // так как нужна краткая версия статьи, из json-ответа нужен лишь extract
            String result = response.split("extract\":\"")[1];

            // обычное форматирование для лучшего восприятия пользователем ответа и его вывод
            result = result.replaceAll("}", "");
            System.out.println(result.replaceAll("(.{125})", "$1\n"));

            // добавление ссылки на полную статью
            String outputURL = "https://en.wikipedia.org/wiki/"
                    +response.split("title\":\"")[1].split("\",")[0].replaceAll(" ", "_");
            System.out.println("Read more here:");
            System.out.println(outputURL);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
