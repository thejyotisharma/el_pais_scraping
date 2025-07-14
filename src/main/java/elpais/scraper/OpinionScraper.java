package elpais.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    public void openElPaisWebsite() {
        logger.info("Opening https://elpais.com/");
        driver.get(EL_PAIS_URL);
    }

    public String getSelectedLanguage() {
        By locator = By.xpath("//footer//div/ul/li[@class='ed_c']");
        WebElement selectedLaguage = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return selectedLaguage.getText();
    }

    public void acceptCookies() {
        clickButton("didomi-notice-agree-button");
    }

    public void clickHamburgerButton() {
        clickButton("btn_open_hamburger");
    }

    public void clickHamburgerCloseButton() {
        clickButton("btn_toggle_hamburger");
    }

    public void visitOpinionPage() {
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
        driver.get(link);

        By title = By.xpath("//article/header//h1");
        By subTitle = By.xpath("//article/header//h2");
        WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(title));
        WebElement pageSubTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(subTitle));
        article.setTitle(pageTitle.getText() + " " + pageSubTitle.getText());

        By content = By.xpath("//article/div[2]");
        WebElement pageContent = wait.until(ExpectedConditions.visibilityOfElementLocated(content));
        article.setContent(pageContent.getText());

        By image = By.xpath("//article/header//span//img");
        WebElement coverImage = wait.until(ExpectedConditions.presenceOfElementLocated(image));
        if (coverImage.isDisplayed()) {
            article.setImageUrl(coverImage.getAttribute("src"));
        }

        return article;
    }

    public void downloadImage(Article article, String fileName) {
        if (article.getImageUrl() == null) {
            return;
        }

        try (InputStream stream = new URL(article.getImageUrl()).openStream()) {
            Files.copy(stream, Paths.get(fileName+".jpg"), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void clickButton(String id) {
        By locator = By.id(id);
        WebElement cookieButton = wait.until(ExpectedConditions.elementToBeClickable(locator));
        cookieButton.click();
    }
}
