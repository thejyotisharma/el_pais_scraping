package elpais.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class OpinionScraper {

    private static final Logger logger = LoggerFactory.getLogger(OpinionScraper.class);
    private final WebDriver driver;
    private final String EL_PAIS_URL = "https://elpais.com/";
    private Wait<WebDriver> wait;

    public OpinionScraper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void openElPaisWebsite(){
        logger.info("Opening https://elpais.com/");
        driver.get(EL_PAIS_URL);
    }

    public void acceptCookies(){
        By locator = By.id("didomi-notice-agree-button");
        WebElement cookieButton = wait.until(ExpectedConditions.elementToBeClickable(locator));
        cookieButton.click();
    }

    public void visitOpinionPage(){
        By locator = By.linkText("Opinión");
        WebElement OpinionButton = wait.until(ExpectedConditions.elementToBeClickable(locator));
        OpinionButton.click();
    }

    public List<String> getAllArticleLinksOnPage() {
        By locator = By.xpath("//article//h2//a");
        List<WebElement> linkElements = driver.findElements(locator);

        List<String> links = new ArrayList<>();
        for (WebElement element : linkElements) {
            String href = element.getAttribute("href");
            if (href != null && !href.isEmpty()) {
                links.add(href);
            }
        }

        return links;
    }

    public Article scrapeArticleData(String link) {
        Article article = new Article();
        // go to page

        // fetch ticlet and call article.setTitle

        // fetch content and call article.setContent


        // fetch imageLink and call article.setLink


        return article;
    }
}
