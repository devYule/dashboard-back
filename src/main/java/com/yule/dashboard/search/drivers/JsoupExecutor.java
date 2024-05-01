package com.yule.dashboard.search.drivers;

import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.mythreadpool.ThreadPoolProvider;
import com.yule.dashboard.search.drivers.model.SiteCategories;
import com.yule.dashboard.search.drivers.model.SiteInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsoupExecutor {

    private final ThreadPoolProvider threadPoolProvider;

    public Future<List<SiteInfo>> naver(String query) {
        log.debug("by Jsoup");
        return threadPoolProvider.getThreadPool().submit(() -> {
            Connection connect = Jsoup.connect("https://search.naver.com/search.naver?query=" + query);
            Document document;
            try {
                document = connect.get();
            } catch (IOException e) {
                throw new ClientException(ExceptionCause.QUERY_ERROR);
            }

            List<SiteInfo> result = new ArrayList<>();

//         지식, 인플루언서, 맛집, 카페 등
            Elements elements = document.selectXpath("//div[@class=\"detail_box\"]");
            for (Element section : elements) {
                try {
                    Elements titleEl = section.selectXpath(".//a[contains(@class, \"title_link\") or contains(@class, \"title\")]");
                    Elements contentEl = section.selectXpath(".//a[contains(@class, \"dsc_link\") or contains(@class, \"desc\")]");
                    Elements iconEl = section.selectXpath("./../div[contains(@class, user_box)]//img[@height=24]");

                    SiteInfo site = new SiteInfo();
                    site.setTitle(titleEl.text());
                    site.setLink(titleEl.attr("href"));
                    site.getContent().add(contentEl.text());
                    site.setIconPath(iconEl.attr("src"));
                    site.setCategory(SiteCategories.COMMUNITY);
                    result.add(site);

                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    System.out.println("NPE!!");
                }
            }

            // 네이버 지식백과
            Elements wikis = document.selectXpath("//div[@class=\"nkindic_basic\"]");
            for (Element wiki : wikis) {
                try {
                    Elements titleEl = wiki.selectXpath(".//h3[contains(@class, \"tit_area\")]/a");
                    Elements contentEl = wiki.selectXpath(".//div[contains(@class, \"content_desc\")]/a");

                    SiteInfo site = new SiteInfo();
                    site.setTitle(titleEl.text());
                    site.setLink(titleEl.attr("href"));
                    site.getContent().add(contentEl.text());
                    site.setCategory(SiteCategories.INFO);

                    result.add(site);

                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    System.out.println("NPE!!");
                }
            }

            // 네이버 뉴스
            Elements news = document.selectXpath("//ul[@class=\"list_news\"]/li");
            for (Element n : news) {
                try {
                    Elements titleEl = n.selectXpath(".//a[contains(@class, \"news_tit\")]");
                    Elements contentEl = n.selectXpath(".//div[contains(@class, \"dsc_wrap\")]/a");
                    Elements iconEl = n.selectXpath(".//span[contains(@class, \"thumb_box\")]/img");

                    SiteInfo site = new SiteInfo();
                    site.setTitle(titleEl.text());
                    site.setLink(titleEl.attr("href"));
                    site.getContent().add(contentEl.text());
                    site.setIconPath(iconEl.attr("src"));
                    site.setCategory(SiteCategories.NEWS);

                    result.add(site);

                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    System.out.println("NPE!!");
                }
            }

            // 지식인
            Elements kins = document.selectXpath("//ul[@class=\"lst_nkin\"]/li");
            for (Element kin : kins) {
                try {
                    Elements titleEl = kin.selectXpath(".//a[contains(@class, \"question_text\")]");
                    Elements contentEl = kin.selectXpath(".//a[contains(@class, \"answer_text\")]");
                    System.out.println("titleEl.text() = " + titleEl.text());
                    System.out.println("titleEl.attr(\"href\") = " + titleEl.attr("href"));
                    System.out.println("contentEl.text() = " + contentEl.text());

                    SiteInfo site = new SiteInfo();
                    site.setTitle(titleEl.text());
                    site.setLink(titleEl.attr("href"));
                    site.getContent().add(contentEl.text());

                    result.add(site);

                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    System.out.println("NPE!!");
                }
            }
            // 기타
            Elements ects = document.selectXpath("//div[contains(@class, \"total_wrap\")]");
            for (Element ect : ects) {
                try {
                    Elements titleEl = ect.selectXpath(".//div[@class=\"total_tit\"]/a");
                    Elements contentEl = ect.selectXpath(".//div[contains(@class, \"total_dsc\")]/a");
                    Elements iconEl = ect.selectXpath(".//div[contains(@class, \"source_box\")]/a/img");

                    SiteInfo site = new SiteInfo();
                    site.setTitle(titleEl.text());
                    site.setLink(titleEl.attr("href"));
                    site.getContent().add(contentEl.text());
                    site.setIconPath(iconEl.attr("src"));

                    result.add(site);

                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    System.out.println("NPE!!");
                }
            }
            return result;
        });
    }

    public Future<List<SiteInfo>> google(String query) {
        log.debug("by Jsoup");
        return threadPoolProvider.getThreadPool().submit(() -> {
            Connection connect = Jsoup.connect("https://www.google.com/search?q=" + query);
            Document document;
            try {
                document = connect.get();
            } catch (IOException e) {
                throw new ClientException(ExceptionCause.QUERY_ERROR);
            }
//            Elements elements = document.selectXpath("//div[@class='MjjYud']/div//div[@class='kb0PBd cvP2Ce ieodic jGGQ5e']");
            Elements elements = document.selectXpath("//div[@class='MjjYud']//div[@class='N54PNb BToiNc cvP2Ce']");

            List<SiteInfo> result = new ArrayList<>();

            for (Element section : elements) {
                try {
                    Elements titleEl = section.selectXpath(".//h3");
                    Elements linkEl = section.selectXpath(".//a");
                    Elements contentEl =
//                            section.selectXpath(".//parent::div//div[@class='VwiC3b yXK7lf lVm3ye r025kc hJNv6b']");
                            section.selectXpath(".//div[@data-snf=\"nke7rc\"]");
//                    Elements iconEl = section.selectXpath(".//parent::a//img");
                    Elements iconEl = section.selectXpath(".//a//img");

                    SiteInfo site = new SiteInfo();
                    site.setTitle(titleEl.text());
                    site.setLink(linkEl.attr("href"));
                    site.getContent().add(contentEl.text());
                    site.setIconPath(iconEl.attr("src"));

                    result.add(site);

                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    System.out.println("NPE!!");
                }
            }

            return result;
        });
    }
}
