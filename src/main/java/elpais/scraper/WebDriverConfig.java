package elpais.scraper;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WebDriverConfig {

    public static WebDriver getDriver(Map<String, String> params) {
        String username = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");

        MutableCapabilities capabilities = new MutableCapabilities();
        Map<String, Object> bstackOptions = new HashMap<>();

        if (params.containsKey("device")) {
            capabilities.setCapability("browserName", params.getOrDefault("browserName", "Chrome"));
            bstackOptions.put("deviceName", params.get("device"));
            bstackOptions.put("osVersion", params.getOrDefault("os_version", "latest"));
            bstackOptions.put("realMobile", params.getOrDefault("real_mobile", "true"));
        } else {
            capabilities.setCapability("browserName", params.getOrDefault("browser", "chrome"));
            capabilities.setCapability("browserVersion", params.getOrDefault("browser_version", "latest"));
            bstackOptions.put("os", params.getOrDefault("os", "Windows"));
            bstackOptions.put("osVersion", params.getOrDefault("os_version", "11"));
        }

        bstackOptions.put("projectName", "ElPais Scraper");
        bstackOptions.put("buildName", "BrowserStack Parallel");
        bstackOptions.put("sessionName", "ElPais Test");

        capabilities.setCapability("bstack:options", bstackOptions);

        try {
            WebDriver driver = new RemoteWebDriver(
                    new URL("https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"),
                    capabilities
            );
            if(!params.containsKey("device")){
                driver.manage().window().maximize();
            }
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid BrowserStack URL", e);
        }
    }
}
