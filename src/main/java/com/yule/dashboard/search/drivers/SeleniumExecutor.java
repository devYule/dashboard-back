package com.yule.dashboard.search.drivers;

import com.yule.dashboard.pbl.exception.ServerException;
import com.yule.dashboard.pbl.mythreadpool.ThreadPoolProvider;
import com.yule.dashboard.search.drivers.model.SiteCategories;
import com.yule.dashboard.search.drivers.model.SiteInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeleniumExecutor {

    public final DriverPool driverPool;
    private final ThreadPoolProvider threadPoolProvider;

    @Value("${crawling.scroll-wait-time-ms}")
    private long scrollWaitTimeMs;
    @Value("${crawling.wait-time-ms}")
    private long waitTimeMs;

    public Future<List<SiteInfo>> naver(String query) {
        log.debug("by selenium");
        return threadPoolProvider.getThreadPool().submit(() -> {
            ChromeDriver driver = null;
            try {
                driver = driverPool.getDriver();
                try {
                    driver.get("data:"); // null check
                } catch (NoSuchWindowException | NullPointerException e) {
                    driver = driverPool.getNewDriver(driver);

                }

                log.trace("do naver search");

                driver.get("https://search.naver.com/search.naver?query=" + query);

                List<SiteInfo> result = new ArrayList<>();

                log.trace("base info is loaded");

                sleepAndScrollToAndSleep(driver);

                // 지식, 인플루언서, 맛집, 카페 등
                List<WebElement> community = driver.findElements(By.xpath("//div[@class=\"detail_box\"]"));
                log.trace("base info loaded");
                for (WebElement comm : community) {
                    try {
                        SiteInfo site = new SiteInfo();
                        WebElement titleEl = comm.findElement(By.xpath(
                                ".//a[contains(@class, \"title_link\") or contains(@class, \"title\")]"));
                        WebElement contentEl = comm.findElement(
                                By.xpath(".//a[contains(@class, \"dsc_link\") or contains(@class, \"desc\")]"));
                        site.setTitle(titleEl.getText());
                        site.setLink(titleEl.getAttribute("href"));
                        site.getContent().add(contentEl.getText());
                        site.setCategory(SiteCategories.COMMUNITY);

                        WebElement iconEl = comm.findElement(By.xpath("./../div[contains(@class, user_box)]//img[@height=24]"));
                        site.setIconPath(iconEl.getAttribute("src"));
                        result.add(site);
                    } catch (NoSuchElementException ignore) {

                    }

                }
                // 네이버 지식백과
                log.trace("wiki loaded");
                List<WebElement> naverWiki = driver.findElements(By.xpath("//div[@class=\"nkindic_basic\"]"));
                for (WebElement wiki : naverWiki) {
                    try {
                        SiteInfo site = new SiteInfo();
                        WebElement titleEl = wiki.findElement(By.xpath(".//h3[contains(@class, \"tit_area\")]/a"));
                        WebElement contentEl = wiki.findElement(By.xpath(".//div[contains(@class, \"content_desc\")]/a"));
                        site.setTitle(titleEl.getText());
                        site.setLink(titleEl.getAttribute("href"));

                        site.getContent().add(contentEl.getText());
                        site.setCategory(SiteCategories.INFO);
                        result.add(site);

                    } catch (NoSuchElementException ignore) {

                    }
                }
                // 네이버 뉴스
                log.trace("news loaded");
                List<WebElement> news = driver.findElements(By.xpath("//ul[@class=\"list_news\"]/li"));
                for (WebElement n : news) {
                    try {
                        SiteInfo site = new SiteInfo();
                        WebElement titleEl = n.findElement(By.xpath(".//a[contains(@class, \"news_tit\")]"));
                        WebElement contentEl = n.findElement(By.xpath(".//div[contains(@class, \"dsc_wrap\")]/a"));
                        site.setTitle(titleEl.getText());
                        site.setLink(titleEl.getAttribute("href"));
                        site.getContent().add(contentEl.getText());
                        site.setCategory(SiteCategories.NEWS);


                        WebElement iconEl = n.findElement(By.xpath(".//span[contains(@class, \"thumb_box\")]/img"));
                        site.setIconPath(iconEl.getAttribute("src"));
                        result.add(site);
                    } catch (NoSuchElementException ignore) {
                    }
                }
                // 지식인
                log.trace("kin loaded");
                List<WebElement> kins = driver.findElements(By.xpath("//ul[@class=\"lst_nkin\"]/li"));
                for (WebElement kin : kins) {
                    try {
                        WebElement titleEl = kin.findElement(By.xpath(".//a[contains(@class, \"question_text\")]"));
                        WebElement contentEl = kin.findElement(By.xpath(".//a[contains(@class, \"answer_text\")]"));
                        SiteInfo site = new SiteInfo();
                        site.setTitle(titleEl.getText());
                        site.setLink(titleEl.getAttribute("href"));
                        site.getContent().add(contentEl.getText());

                        result.add(site);
                    } catch (NoSuchElementException ignore) {

                    }
                }
                // 기타
                log.trace("ect load");
                List<WebElement> ects = driver.findElements(By.xpath("//div[contains(@class, \"total_wrap\")]"));

                for (WebElement ect : ects) {
                    try {
                        //div[contains(@class, "source_box")]/a/img : icon
                        //div[@class="total_tit"]/a : title & link
                        //div[contains(@class, "total_dsc")]/a : content
                        WebElement iconEl;
                        WebElement titleElAndLink;
                        List<WebElement> contentEl;

                        iconEl = ect.findElement(By.xpath(".//div[contains(@class, \"source_box\")]/a/img"));
                        titleElAndLink = ect.findElement(By.xpath(".//div[@class=\"total_tit\"]/a"));
                        contentEl = ect.findElements(By.xpath(".//div[contains(@class, \"total_dsc\")]/a"));

                        SiteInfo siteInfo = new SiteInfo();
                        siteInfo.setTitle(titleElAndLink.getText());
                        siteInfo.setLink(titleElAndLink.getAttribute("href"));
                        siteInfo.setCategory(SiteCategories.ECT);
                        siteInfo.setIconPath(iconEl.getAttribute("src"));
                        for (WebElement content : contentEl) {
                            siteInfo.getContent().add(content.getText());
                        }
                        result.add(siteInfo);

                    } catch (NoSuchElementException ignored) {
                        log.trace("passed");

                    }
                }

                return result;

            } finally {
                driverPool.returnDriver(driver);
            }
        });
    }

    public Future<List<SiteInfo>> google(String query) {
        log.debug("by selenium");
        return threadPoolProvider.getThreadPool().submit(() -> {
            ChromeDriver driver = null;
            try {
                driver = driverPool.getDriver();
                try {
                    driver.get("data:"); // null check
                } catch (NoSuchWindowException | NullPointerException e) {
                    driver = driverPool.getNewDriver(driver);
                }

                log.trace("do google search");
                driver.get("https://www.google.com/search?q=" + query);
                driver.switchTo();

                List<SiteInfo> result = new ArrayList<>();

                sleepAndScrollToAndSleep(driver);

//            List<WebElement> divs = driver.findElements(By.xpath("//div[@id='rso']/div"));
                List<WebElement> divs = driver.findElements(By.xpath("//div[@class='MjjYud']/div"));
                log.trace("google get page search");

                log.trace("google: findEl's size = {}", divs.size());
                for (WebElement thisDiv : divs) {
                    try {

                        //div[@id='rso']/div : 전체
                        //h3 : h3 ( title ) xx //div[@id='rso']/div//div[contains(@class, 'notranslate')] xx //*[contains(@class, 'LC20lb')]
                        //h3/parent::a
                        //span[text()] , .hasAttribute('class') 가 없는것 : content
                        //div[@id='rso']/div//h3/parent::a//img : icon


                        WebElement titleEl = thisDiv.findElement(By.xpath(".//h3"));
                        WebElement linkEl = titleEl.findElement(By.xpath("./parent::a"));
                        WebElement contentEl = thisDiv.findElement(By.xpath(".//div[contains(@style, '-webkit-line-clamp:2')]"));
                        WebElement iconEl = linkEl.findElement(By.xpath(".//img"));
                        SiteInfo resultObj = new SiteInfo();
                        result.add(resultObj);

                        resultObj.setTitle(titleEl.getText());
                        resultObj.setLink(linkEl.getAttribute("href"));
                        resultObj.getContent().add(contentEl.getText());


                        resultObj.setIconPath(iconEl.getAttribute("src"));

                    } catch (NoSuchElementException ignored) {
                        System.out.println("passed");
                    }
                }
                return result;
            } finally {
                driverPool.returnDriver(driver);
            }
        });
    }

    /* --- extracted method */
    private void sleepAndScrollToAndSleep(ChromeDriver driver) {
        try {
            Thread.sleep(scrollWaitTimeMs);
            new Actions(driver).sendKeys(Keys.END).perform();
            Thread.sleep(waitTimeMs);
        } catch (InterruptedException e) {
            throw new ServerException(e);
        }

    }
}
