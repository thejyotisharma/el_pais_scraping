package elpais.tests;

import elpais.analyzer.FrequentWordCounter;
import elpais.scraper.Article;
import elpais.scraper.OpinionScraper;
import elpais.scraper.WebDriverConfig;
import elpais.translator.SpanishTranslator;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Optional;

import java.util.*;

public class ElPaisTest {

    private static final Logger logger = LoggerFactory.getLogger(ElPaisTest.class);
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "browser_version", "os", "os_version", "device", "real_mobile"})
    public void setUp(@Optional("chrome") String browser,
                      @Optional("latest") String browserVersion,
                      @Optional("Windows") String os,
                      @Optional("11") String osVersion,
                      @Optional("") String device,
                      @Optional("false") String realMobile) throws Exception {

        Map<String, String> params = new HashMap<>();

        if (device != null && !device.isEmpty()) {
            params.put("device", device);
            params.put("os_version", osVersion);
            params.put("real_mobile", realMobile);
            params.put("browserName", browser);
        } else {
            params.put("browser", browser);
            params.put("browser_version", browserVersion);
            params.put("os", os);
            params.put("os_version", osVersion);
        }

        driver = WebDriverConfig.getDriver(params);
    }

    @Test
    public void testElPaisWorkflow() {
        logger.info("Step 1: Visit El País");
        OpinionScraper scraper = new OpinionScraper(driver);
        scraper.openElPaisWebsite();
        scraper.acceptCookies();
//        assert "ESPAÑA".equals(scraper.getSelectedLanguage()) : "Language is not Spanish";

        logger.info("Step 2: Scrape Opinion Articles");
        scraper.visitOpinionPage();
        List<String> links = scraper.getAllArticleLinksOnPage();

        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < Math.min(5, links.size()); i++) {
            Article article = scraper.scrapeArticleData(links.get(i));
            articles.add(article);
            scraper.downloadImage(article, "Article " + (i + 1));
        }

        logger.info("Step 3: Translate Headers");
        SpanishTranslator translator = new SpanishTranslator();
        List<String> headers = new ArrayList<>();
        for (Article a : articles) {
            headers.add(a.getTitle());
        }
        List<String> translated = translator.toEnglish(headers);

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
