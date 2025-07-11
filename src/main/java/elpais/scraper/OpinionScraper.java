package elpais.scraper;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpinionScraper {

    private static final Logger logger = LoggerFactory.getLogger(OpinionScraper.class);
    private final WebDriver driver;
    private final String EL_PAIS_URL = "https://elpais.com/";

    public OpinionScraper(WebDriver driver) {
        this.driver = driver;
    }

    public void openElPaisWebsite(){
        logger.info("Opening https://elpais.com/");
        driver.get(EL_PAIS_URL);
    }

}
