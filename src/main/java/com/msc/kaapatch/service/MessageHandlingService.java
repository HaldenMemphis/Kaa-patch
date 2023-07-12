package com.msc.kaapatch.service;

import com.msc.kaapatch.utils.CacheMap;
import com.msc.kaapatch.utils.KafkaConsumer;
import com.msc.kaapatch.utils.KafkaProducer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: Kaa-patch
 * @description: This model designed to handel the message
 * @author: yfliu
 * @create: 2023-07-09 15:14
 **/
@Log4j2
@Component
public class MessageHandlingService {

    public final CacheMap bloodSugarCacheMap = new CacheMap();
    private ExecutorService executorEventHandlerService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private  KafkaConsumer kafkaConsumer;


    public MessageHandlingService() {
        this.executorEventHandlerService = Executors.newSingleThreadExecutor();
        executorEventHandlerService.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(1L);
                    bloodSugarKafkaSend(kafkaConsumer.getBloodSugarCacheMap());
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }
        });
    }


    private void bloodSugarKafkaSend(CacheMap bloodSugarCacheMap) {

        // The map write does not force locking, and new data may be inserted during processing to ensure that all data in the map is processed each time.
        for (int size = bloodSugarCacheMap.getCatchMap().size(); size > 0; size = bloodSugarCacheMap.getCatchMap().size()) {
            Iterator<Map.Entry<String, Object>> iterator = bloodSugarCacheMap.getCatchMap().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                kafkaProducer.BloodSugarKafkaSend(entry.getValue());
                // Use key and value to adapt to scenarios where data is updated during processing
                bloodSugarCacheMap.getCatchMap().remove(entry.getKey(), entry.getValue());
            }
        }
    }
}
