package com.yule.dashboard.search.drivers;

import com.yule.dashboard.bookmark.BookmarkRepository;
import com.yule.dashboard.bookmark.BookmarkShotRepository;
import com.yule.dashboard.entities.Bookmark;
import com.yule.dashboard.entities.BookmarkScreenShot;
import com.yule.dashboard.pbl.utils.enums.FileCategory;
import com.yule.dashboard.pbl.utils.enums.FileType;
import com.yule.dashboard.search.drivers.model.SiteInfo;
import com.yule.dashboard.search.model.SearchType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.Future;

@Component
@Slf4j
@RequiredArgsConstructor
public class DriverServiceProvider {

    @Value("${crawling.screen-shot-wait-time-ms}")
    private long bookmarkShotWaitTime;
    @Value("${files.base-path}")
    private String basePath;
    private final BookmarkShotRepository bookmarkShotRepository;
    private final BookmarkRepository bookmarkRepository;
    public final DriverPool driverPool;

    private final SeleniumExecutor seleniumExecutor;
    private final JsoupExecutor jsoupExecutor;

    public Future<List<SiteInfo>> naver(String query, SearchType type) {
        return switch (type) {
            case JSOUP -> jsoupExecutor.naver(query);
            case SELENIUM -> seleniumExecutor.naver(query);
        };

    }

    public Future<List<SiteInfo>> google(String query, SearchType type) {
        return switch (type) {
            case JSOUP -> jsoupExecutor.google(query);
            case SELENIUM -> seleniumExecutor.google(query);
        };
    }

    @Async
    @Transactional
    public void saveShot(String url, Long bookmarkId, Long userId) {
        log.debug("DriverServiceProvider.saveShot()");
        ChromeDriver driver = driverPool.getDriver();
        driver.get(url);
        try {
            Thread.sleep(bookmarkShotWaitTime);
        } catch (InterruptedException e) {
            log.error("error", e);
            return;
        }

        Path suffix = Paths.get(FileCategory.BOOKMARK.getValue(),
                FileType.SHOT.getValue(), userId.toString(), bookmarkId.toString(), "shot.png");
        Path path = Paths.get(
                basePath, suffix.toString());
        try {
            if (Files.notExists(path)) {

                Files.createDirectories(path.getParent());

            }
            File shot = driver.getScreenshotAs(OutputType.FILE);
            FileCopyUtils.copy(shot, path.toFile());
        } catch (IOException e) {
            log.error("error", e);
            return;
        }
        log.debug("shot is saved {}", path);

        Bookmark findBookmark = bookmarkRepository.findByIdAndStateAndUserId(bookmarkId, userId);
        BookmarkScreenShot saveBookmarkShot = BookmarkScreenShot.builder()
                .shot(suffix.toString())
                .bookmark(findBookmark)
                .build();
        bookmarkShotRepository.save(saveBookmarkShot);
        driverPool.returnDriver(driver);

    }

    @Async
    public void removeShot(Long bookmarkId, Long userId) {
        log.debug("DriverServiceProvider.removeShot()");
        Path path = Paths.get(
                basePath, FileCategory.BOOKMARK.getValue(),
                FileType.SHOT.getValue(), userId.toString(), bookmarkId.toString(), "shot.png");
        if (Files.notExists(path)) {
            return;
        }
        try {
            removeAll(path);
        } catch (IOException e) {
            log.error("error", e);
            return;
        }
        BookmarkScreenShot findBookmarkShot = bookmarkShotRepository.findByBookmarkId(bookmarkId);
        bookmarkShotRepository.delete(findBookmarkShot);
    }
    /* --- inner --- */

    private void removeAll(Path totalPath) throws IOException {
        Files.walkFileTree(totalPath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }




}
