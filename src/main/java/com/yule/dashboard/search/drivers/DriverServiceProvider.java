package com.yule.dashboard.search.drivers;

import com.yule.dashboard.search.drivers.model.GoogleSiteInfo;
import com.yule.dashboard.search.drivers.model.NaverSiteInfo;
import com.yule.dashboard.search.drivers.model.SiteCategories;
import com.yule.dashboard.search.drivers.model.SiteInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class DriverServiceProvider {

    public final DriverPool driverPool;

    public List<NaverSiteInfo> naver(String query) {
        return doLogic(driver -> {
            driver.get("https://www.naver.com");
            driver.findElement(By.xpath("//*[@id=\"query\"]")).sendKeys(query);
            driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();

            List<NaverSiteInfo> result = new ArrayList<>();

            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(100));
            // 지식, 인플루언서, 맛집, 카페 등
            List<WebElement> community = driver.findElements(By.xpath("//div[@class=\"detail_box\"]"));
            for (WebElement comm : community) {
                NaverSiteInfo site = new NaverSiteInfo();
                result.add(site);
                WebElement titleEl = comm.findElement(By.xpath(
                        ".//a[contains(@class, \"title_link\") or contains(@class, \"title\")]"));
                WebElement contentEl = comm.findElement(
                        By.xpath(".//a[contains(@class, \"dsc_link\") or contains(@class, \"desc\")]"));
                site.getTitle().add(titleEl.getText());
                site.setLink(titleEl.getAttribute("href"));
                site.getContent().add(contentEl.getText());
                site.setCategory(SiteCategories.COMMUNITY);

                try {
                    WebElement iconEl = comm.findElement(By.xpath("./../div[contains(@class, user_box)]//img[@height=24]"));
                    site.setIconPath(iconEl.getAttribute("src"));
                } catch (NoSuchElementException ignore) {

                }

            }
            // 네이버 지식백과
            List<WebElement> naverWiki = driver.findElements(By.xpath("//div[@class=\"nkindic_basic\"]"));
            for (WebElement wiki : naverWiki) {
                NaverSiteInfo site = new NaverSiteInfo();
                result.add(site);
                WebElement titleEl = wiki.findElement(By.xpath(".//h3[contains(@class, \"tit_area\")]/a"));
                WebElement subTitleEl = null;
                try {
                    subTitleEl = wiki.findElement(By.xpath(".//div[contains(@class, \"lnk_sub_tit\")]/a"));
                } catch (NoSuchElementException ignore) {
                }
                WebElement contentEl = wiki.findElement(By.xpath(".//div[contains(@class, \"content_desc\")]/a"));
                site.getTitle().add(titleEl.getText());
                site.setLink(titleEl.getAttribute("href"));
                if (subTitleEl != null) {
                    site.setSubTitle(subTitleEl.getText());
                }
                site.getContent().add(contentEl.getText());
                site.setCategory(SiteCategories.INFO);
            }
            // 네이버 뉴스
            List<WebElement> news = driver.findElements(By.xpath("//ul[@class=\"list_news\"]/li"));
            for (WebElement n : news) {
                NaverSiteInfo site = new NaverSiteInfo();
                result.add(site);
                WebElement titleEl = n.findElement(By.xpath(".//a[contains(@class, \"news_tit\")]"));
                WebElement contentEl = n.findElement(By.xpath(".//div[contains(@class, \"dsc_wrap\")]/a"));
                site.getTitle().add(titleEl.getText());
                site.setLink(titleEl.getAttribute("href"));
                site.getContent().add(contentEl.getText());
                site.setCategory(SiteCategories.NEWS);

                try {
                    WebElement iconEl = n.findElement(By.xpath(".//span[contains(@class, \"thumb_box\")]/img"));
                    site.setIconPath(iconEl.getAttribute("src"));
                } catch (NoSuchElementException ignore) {
                }
            }
            // 지식인
            List<WebElement> kins = driver.findElements(By.xpath("//ul[@class=\"lst_nkin\"]/li"));
            for (WebElement kin : kins) {
                WebElement titleEl = kin.findElement(By.xpath(".//a[contains(@class, \"question_text\")]"));
                WebElement contentEl = kin.findElement(By.xpath(".//a[contains(@class, \"answer_text\")]"));
                NaverSiteInfo site = new NaverSiteInfo();
                result.add(site);

                site.getTitle().add(titleEl.getText());
                site.setLink(titleEl.getAttribute("href"));
                site.getContent().add(contentEl.getText());

            }

            return result;
        });
    }

    public List<? extends SiteInfo> google(String query) {
        return doLogic(driver -> {
            driver.get("https://www.google.com");

            List<GoogleSiteInfo> result = new ArrayList<>();

            WebElement element = driver.findElement(By.cssSelector("[name='q'"));
            element.sendKeys(query);
            element.submit();
            long tryDurationStart = System.currentTimeMillis();
            while (!driver.findElements(By.xpath("//div[@id='search']")).isEmpty()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (System.currentTimeMillis() - tryDurationStart > 10000) throw new RuntimeException();
            }
            List<WebElement> divs = driver.findElements(By.xpath("//div[@id='rso']/div"));

            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5));

            int size = divs.size();
            int lastIdx = size - 1;
            for (int i = 0; i < size; i++) {
                WebElement thisDiv = divs.get(i);
                try {
                    if (i == lastIdx) {
                        thisDiv = thisDiv.findElement(By.xpath("./div"));
                    }
                    //*[@id="rso"]/div[2]  /div/div/div[1]/div/div/span/a
                    WebElement atag = thisDiv.findElement(By.xpath("./div/div/div[1]/div/div/span/a"));

                    GoogleSiteInfo site = new GoogleSiteInfo();
                    result.add(site);
                    site.getTitle().add(atag.findElement(By.xpath("./h3")).getText());
                    site.setLink(atag.getAttribute("href"));
                    List<WebElement> children = thisDiv.findElements(By.xpath("./div/div/div"));
                    int totalSize = children.size();
                    int contentIdx = totalSize - 2;
                    List<WebElement> spans = children.get(contentIdx).findElements(By.xpath("./div/span"));
                    for (WebElement span : spans) {
                        site.getContent().add(span.getText());
                    }
                    WebElement iconEl = atag.findElement(By.xpath("./div/div/span/div/img"));
                    site.setIconPath(iconEl.getAttribute("src"));

                } catch (NoSuchElementException ignore) {
                }
            }
            return result;
        });
    }

    /* --- inner --- */
    private synchronized <T extends SiteInfo> List<T> doLogic(Function<ChromeDriver, List<T>> func) {
        ChromeDriver driver = null;
        try {
            driver = driverPool.getDriver();
            try {
                driver.get("data:"); // null check
            } catch (NoSuchWindowException | NullPointerException e) {
                driver = driverPool.getNewDriver(driver);

            } finally {
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            }
            return func.apply(driver);
        } finally {
            driverPool.returnDriver(driver);
        }
    }
}
