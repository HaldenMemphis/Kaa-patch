package com.msc.kaapatch.utils;

import com.alibaba.fastjson2.JSON;
import com.msc.kaapatch.dao.entity.BloodSugarMonitor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @program: Kaa-patch
 * @description: This class send the processed message to the Kafka
 * @author: yfliu
 * @create: 2023-07-09 13:55
 **/
@Log4j2
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.send.topic}")
    private String sendTopic;

    public void BloodSugarKafkaSend(Object bloodSugarMonitor) {
        kafkaSend(sendTopic, null, JSON.toJSONString(bloodSugarMonitor));
    }

    private void kafkaSend(String topic, String key, String data) {
        log.info("kafkaSend:{},{},{}", topic, key, data);
        kafkaTemplate.send(topic, key, data);
    }

}
