package elpais;

import elpais.scraper.Article;
import elpais.scraper.OpinionScraper;
import elpais.scraper.WebDriverConfig;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        WebDriver driver = WebDriverConfig.getDriver();

        logger.info("Starting Step: 1. Visit the website El País, a Spanish news outlet");
        OpinionScraper opinionScraper = new OpinionScraper(driver);
        opinionScraper.openElPaisWebsite();
        logger.info("Completed Step: 1");

        logger.info("Starting Step: 2. Scrape Articles from the Opinion Section");
        opinionScraper.acceptCookies();
        opinionScraper.visitOpinionPage();
        List<String> allArticleLinks = opinionScraper.getAllArticleLinksOnPage();
        List<Article> topFiveArticles = new ArrayList<>();
        logger.info("Printing top 5 articles");
        for (int i = 0; i < 5; i++) {
            Article article = opinionScraper.scrapeArticleData(allArticleLinks.get(i));
            topFiveArticles.add(article);

            String articleNumber = "Article " + (i + 1);
            logger.info(articleNumber + " : " + article.getTextToPrint());
            opinionScraper.downloadImage(article, articleNumber);
        }
        logger.info("Completed Step: 2");

        driver.quit();
    }

}
