package com.msc.kaapatch.dao.entity;

import lombok.*;
import java.util.UUID;
import com.alibaba.fastjson2.*;


/**
 * @program: Kaa-patch
 * @description: This is the entity of Blood Sugar Monitor Body, which receive message from Kafka and will finally storage to the Clickhouse
 * @author: yfliu
 * @create: 2023-07-08 16:27
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BloodSugarMonitor {

    private String uuid;

    private String mac;

    private String sourceIP;

    private long timestamp;

    private float bloodSugar;

    public BloodSugarMonitor(BloodSugarOriginalData bloodSugarOriginalData) {
        this.uuid = UUID.randomUUID().toString();
        this.mac = bloodSugarOriginalData.getMac();
        this.sourceIP = bloodSugarOriginalData.getSourceIP();
        this.timestamp = bloodSugarOriginalData.getTimestamp();
        this.bloodSugar = bloodSugarOriginalData.getBloodSugar();
    }

    public String toJSONString(BloodSugarMonitor bl) {
        return JSON.toJSONString(bl);
    }



}
