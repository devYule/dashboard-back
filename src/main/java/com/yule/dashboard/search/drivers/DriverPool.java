package com.yule.dashboard.search.drivers;

import com.yule.dashboard.pbl.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class DriverPool {
    public final List<ChromeDriver> drivers;
    public final List<ChromeDriver> using;
    public final int driverSize;
    private final int retryCount;
    private final int timeout;

    public DriverPool(@Value("${driver-pool.size}") int driverSize,
                      @Value("${driver-pool.retry-count}") int retryCount,
                      @Value("${driver-pool.timeout-second}") int timeout) {
        this.drivers = Collections.synchronizedList(new ArrayList<>());
        this.using = Collections.synchronizedList(new ArrayList<>());
        this.driverSize = driverSize;
        this.retryCount = retryCount;
        this.timeout = timeout;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void init() {
        for (int i = 0; i < driverSize; i++) {
            drivers.add(new ChromeDriver());
        }
    }

    public synchronized ChromeDriver getNewDriver(ChromeDriver oldDriver) {
        drivers.remove(oldDriver);
        using.remove(oldDriver);
        if (drivers.size() + using.size() == driverSize) throw new ServerException();
        ChromeDriver driver = new ChromeDriver();
        this.using.add(driver);
        return driver;
    }

    public synchronized ChromeDriver getDriver() {
        return getDriver(1);
    }

    public synchronized void returnDriver(ChromeDriver driver) {
        try {
            using.remove(driver);
            drivers.add(driver);
        } catch (RuntimeException e) {
            log.error("error", e);
            driver.quit();
            drivers.add(new ChromeDriver());
            using.remove(driver);
        }
    }

    /* --- inner --- */
    private synchronized ChromeDriver getDriver(int retryCnt) {
        if (retryCnt == this.retryCount) throw new ServerException();
        try {
            if (drivers.size() + using.size() < driverSize) {
                int lacking = driverSize - (drivers.size() + using.size());
                for (int i = 0; i < lacking; i++) {
                    drivers.add(new ChromeDriver());
                }
            }
            ChromeDriver driver = drivers.get(0);
            using.add(driver);
            drivers.remove(driver);
            return driver;
        } catch (ArrayIndexOutOfBoundsException e) {
            log.warn("driver pool is empty !!!\nwait " + timeout + "seconds...");
            try {
                Thread.sleep(timeout * 1000L);
            } catch (InterruptedException ex) {
                log.error("error", ex);
                throw new ServerException();
            }
            int arg = retryCnt;
            return getDriver(++arg);
        }
    }

}
