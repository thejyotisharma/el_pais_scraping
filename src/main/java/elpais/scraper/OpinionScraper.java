package elpais.scraper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpinionScraper {

    private static final Logger logger = LoggerFactory.getLogger(OpinionScraper.class);

    public void openElPaisWebsite(){
        logger.info("Opening https://elpais.com/");
    }

}
