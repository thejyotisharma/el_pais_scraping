package elpais.tests;

import elpais.analyzer.FrequentWordCounter;
import elpais.scraper.Article;
import elpais.scraper.OpinionScraper;
import elpais.translator.SpanishTranslator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ElPaisTest {

    private static final Logger logger = LoggerFactory.getLogger(ElPaisTest.class);
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);
        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            logger.warn("Unable to maximize browser");
        }
    }

    @Test
    public void testElPaisWorkflow() {
        logger.info("Step 1: Visit El País");
        OpinionScraper scraper = new OpinionScraper(driver);
        scraper.openElPaisWebsite();
        scraper.acceptCookies();
        scraper.clickHamburgerButton();
        Assert.assertEquals("ESPAÑA", scraper.getSelectedLanguage());
        scraper.clickHamburgerCloseButton();

        logger.info("Step 2: Scrape Opinion Articles");
        scraper.visitOpinionPage();
        List<String> links = scraper.getAllArticleLinksOnPage();

        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Article article = scraper.scrapeArticleData(links.get(i));
            articles.add(article);

            String articleNumber = "Article " + (i + 1);
            scraper.downloadImage(article, articleNumber);
            logger.info(articleNumber + " : " + article.getTextToPrint());
        }

        logger.info("Step 3: Translate Headers");
        SpanishTranslator translator = new SpanishTranslator();
        List<String> headers = new ArrayList<>();
        for (Article a : articles) {
            headers.add(a.getTitle());
        }
        List<String> translated = translator.toEnglish(headers);
        logger.info("Translated Headers are: ");
        for (String header : translated) {
            logger.info(header);
        }

        logger.info("Step 4: Analyze Translations");
        FrequentWordCounter counter = new FrequentWordCounter();
        Map<String, Integer> wordCount = counter.getWordsRepeatingMoreThanTwice(translated);
        logger.info("Frequent words: {}", wordCount);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
