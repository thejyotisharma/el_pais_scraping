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

    public static void main(String[] args){
        WebDriver driver = WebDriverConfig.getDriver();

        logger.info("Starting: 1. Visit the website El País, a Spanish news outlet");
        OpinionScraper opinionScraper = new OpinionScraper(driver);
        opinionScraper.openElPaisWebsite();

        logger.info("Starting: 2. Scrape Articles from the Opinion Section");
        opinionScraper.acceptCookies();
        opinionScraper.visitOpinionPage();
        List<String> allArticleLinks = opinionScraper.getAllArticleLinksOnPage();
        List<Article> topFiveArticles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Article article = opinionScraper.scrapeArticleData(allArticleLinks.get(i));
            topFiveArticles.add(article);

            logger.info(article.getTitle());
            logger.info(article.getContent());
        }

        driver.quit();
    }

}
