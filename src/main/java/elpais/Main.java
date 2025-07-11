package elpais;

import elpais.scraper.OpinionScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){
        logger.info("Starting: 1. Visit the website El País, a Spanish news outlet");
        OpinionScraper opinionScraper = new OpinionScraper();
        opinionScraper.openElPaisWebsite();
    }

}
