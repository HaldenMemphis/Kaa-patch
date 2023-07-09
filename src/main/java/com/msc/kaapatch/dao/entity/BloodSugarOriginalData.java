package com.msc.kaapatch.dao.entity;

import lombok.*;
import com.alibaba.fastjson2.*;

/**
 * @program: Kaa-patch
 * @description: This is the entity of the message receive from Kafka
 * @author: yfliu
 * @create: 2023-07-09 13:10
 **/

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BloodSugarOriginalData {

    private String mac;

    private String sourceIP;

    private long timestamp;

    private float bloodSugar;

    public BloodSugarOriginalData(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject data = jsonObject.getJSONObject("event");
        this.mac = data.getString("mac");
        this.sourceIP = data.getString("sourceIP");
        this.timestamp = data.getLong("timestamp");
        this.bloodSugar = data.getFloat("bloodSugar");
    }

}
