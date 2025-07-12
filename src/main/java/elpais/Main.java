package elpais;

import elpais.analyzer.FrequentWordCounter;
import elpais.scraper.Article;
import elpais.scraper.OpinionScraper;
import elpais.scraper.WebDriverConfig;
import elpais.translator.SpanishTranslator;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        WebDriver driver = WebDriverConfig.getDriver();

        logger.info("Starting Step: 1. Visit the website El País, a Spanish news outlet");
        OpinionScraper opinionScraper = new OpinionScraper(driver);
        opinionScraper.openElPaisWebsite();
        if (!"ESPAÑA".equals(opinionScraper.getSelectedLanguage())){
            logger.error("Selected language is not Spanish");
            return;
        }
        logger.info("Completed Step: 1");

        logger.info("Starting Step: 2. Scrape Articles from the Opinion Section");
        opinionScraper.acceptCookies();
        opinionScraper.visitOpinionPage();
        List<String> allArticleLinks = opinionScraper.getAllArticleLinksOnPage();
        List<Article> topFiveArticles = new ArrayList<>();
        logger.info("Printing top 5 articles");
        for (int i = 0; i < 5; i++) {
            logger.info("Doing -> "+allArticleLinks.get(i));
            Article article = opinionScraper.scrapeArticleData(allArticleLinks.get(i));
            topFiveArticles.add(article);

            String articleNumber = "Article " + (i + 1);
            logger.info(articleNumber + " : " + article.getTextToPrint());
            opinionScraper.downloadImage(article, articleNumber);
        }
        logger.info("Completed Step: 2");

        logger.info("Starting Step: 3. Translate Article Headers");
        List<String> spanishHeaders = new ArrayList<>();
        for (Article article : topFiveArticles) {
            spanishHeaders.add(article.getTitle());
        }
        SpanishTranslator translator = new SpanishTranslator();
        List<String> englishHeaders = translator.toEnglish(spanishHeaders);
        logger.info("Translated headers are:");
        for (String header : englishHeaders) {
            logger.info(header);
        }
        logger.info("Completed Step: 3");

        logger.info("Starting Step: 4. Analyze Translated Headers");
        FrequentWordCounter wordsCounter = new FrequentWordCounter();
        Map<String, Integer> repeatingWords = wordsCounter.getWordsRepeatingMoreThanTwice(englishHeaders);
        logger.info("Repeating words are {}",repeatingWords);
        logger.info("Completed Step: 4");

        driver.quit();
    }

}
