package com.msc.kaapatch.utils;

import com.msc.kaapatch.dao.entity.BloodSugarOriginalData;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: Kaa-patch
 * @description: This Class receive the message devices send to Kafaka
 * @author: yfliu
 * @create: 2023-07-09 13:56
 **/
@Log4j2
@Data
@Component
public class KafkaConsumer {


    public CacheMap bloodSugarCacheMap;

    public KafkaConsumer(CacheMap bloodSugarCacheMap) {
        log.info("KafkaConsumer init");
        this.bloodSugarCacheMap = bloodSugarCacheMap;
    }

    //Receive the message from Kafka, and convert it to BloodSugarOriginalData. Put it in to map and wait for processes.
    @KafkaListener(topics = "${kafka.bds.receive.topic}")
    public void bloodSugarMonitorConsumer(String message) {
//        log.info("BloodSugarMonitorConsumer:{} ", message);
        BloodSugarOriginalData bloodSugarOriginalData = new BloodSugarOriginalData(message);
        if (null == bloodSugarOriginalData || StringUtils.isBlank(bloodSugarOriginalData.getMac())) {
            return;
        }
        bloodSugarCacheMap.put(bloodSugarOriginalData.getMac(), bloodSugarOriginalData);
    }
}
