package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = {MarketDataStorageService.class,
        RedisConfig.class })
class MarketDataStorageServiceTest {

    @Autowired
    private MarketDataStorageService storageService;


    @Test
    void testLastUpdatedTracking() {
        String symbol = "TEST";

        // Initial state
        assertThat(storageService.getLastUpdated(symbol).isEmpty(), is(true));

        // After storing data
        storageService.storeData(symbol, "yahoo", List.of(new MarketData(
                symbol,
                123.45,
                122.50,
                124.00,
                1000L,
                System.currentTimeMillis()
        )));
        assertThat(storageService.getLastUpdated(symbol).isPresent(), is(true));

        // Verify ISO-8601 format
        Instant.parse(storageService.getLastUpdated(symbol).get());
    }
}