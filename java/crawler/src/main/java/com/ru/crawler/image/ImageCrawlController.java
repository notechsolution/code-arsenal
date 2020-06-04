package com.ru.crawler.image;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageCrawlController {

  public static List<String> crawlDomains = Arrays.asList("http://pic.114nz.com/guoshubinghai/");
  public static void main(String[] args) throws Exception {
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

    CrawlConfig config = new CrawlConfig();
    // Set the folder where intermediate crawl data is stored (e.g. list of urls that are extracted from previously
    // fetched pages and need to be crawled later).
    config.setCrawlStorageFolder("/tmp/crawler4j/guoshubinghai/");

    // Number of threads to use during crawling. Increasing this typically makes crawling faster. But crawling
    // speed depends on many other factors as well. You can experiment with this to figure out what number of
    // threads works best for you.
    int numberOfCrawlers = 8;

    // Where should the downloaded images be stored?
    File storageFolder = new File("/tmp/crawled-images/guoshubinghai/");

    // Since images are binary content, we need to set this parameter to
    // true to make sure they are included in the crawl.
    config.setIncludeBinaryContentInCrawling(true);

    PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
    for (String domain : crawlDomains) {
      controller.addSeed(domain);
    }

    if (!storageFolder.exists()) {
      storageFolder.mkdirs();
    }

    CrawlController.WebCrawlerFactory<ImageCrawler> factory = () -> new ImageCrawler(storageFolder, crawlDomains);
    controller.start(factory, numberOfCrawlers);
  }

}
