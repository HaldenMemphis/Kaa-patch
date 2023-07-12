package com.msc.kaapatch.utils;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: Kaa-patch
 * @description: This is Catch Map Tool, provide function to put or remove from Catch Map.
 * @author: yfliu
 * @create: 2023-07-09 14:35
 **/
@Log4j2
@Data
@Component
public class CacheMap {

    private ConcurrentHashMap<String, Object> catchMap = new ConcurrentHashMap<>();

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CacheMap() {
        log.info("CacheMap init");
    }


    /**
     * @Description: Put the message into Catch Map
     * @Param: [key, value]
     * @return: void
     **/
    public void put(String key, Object value) {
        executorService.execute(() -> {
            catchMap.put(key, value);
        });
    }

    /**
     * @Description: Remove the message from Catch Map
     * @Param: [key]
     * @return: void
     **/
    public void remove(String key) {
        executorService.execute(() -> {
            catchMap.remove(key);
        });
    }

    /**
     * @Description: Get the message from Catch Map
     * @Param: [key]
     * @return: java.lang.Object
     **/
    public Object get(String key) {
        return catchMap.get(key);
    }

}
