package com.msc.kaapatch.service;

import com.msc.kaapatch.dao.entity.BloodSugarMonitor;
import com.msc.kaapatch.dao.entity.BloodSugarOriginalData;
import com.msc.kaapatch.dao.entity.DeviceInfoData;
import com.msc.kaapatch.dao.mapper.DeviceInfoMapper;
import com.msc.kaapatch.utils.CacheMap;
import com.msc.kaapatch.utils.KafkaConsumer;
import com.msc.kaapatch.utils.KafkaProducer;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
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

    public final CacheMap registerDeviceCacheMap = new CacheMap();
    private ExecutorService executorEventHandlerService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private DeviceInfoMapper deviceInfoMapper;



    public MessageHandlingService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        initDeviceInfo();
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

    public void initDeviceInfo(){
        log.info("DeviceStatusService runner beginning...");
        // Queries out all devices mac address
        try {
            List<String> init = deviceInfoMapper.selectAllMac();
            if (null == init) {
                log.info("Initialization query device status is null");
            } else {
                init.forEach(mac -> registerDeviceCacheMap.put(mac, mac));
            }
            log.info("init size {},DeviceStatusService runner completed...");
        } catch (Exception e) {
            log.info(e);
        }
    }


    private void bloodSugarKafkaSend(CacheMap bloodSugarCacheMap) {

        // The map write does not force locking, and new data may be inserted during processing to ensure that all data in the map is processed each time.
        for (int size = bloodSugarCacheMap.getCatchMap().size(); size > 0; size = bloodSugarCacheMap.getCatchMap().size()) {
            Iterator<Map.Entry<String, Object>> iterator = bloodSugarCacheMap.getCatchMap().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (!checkMacExist(entry.getKey())) {
                    registerDevices((BloodSugarOriginalData) entry.getValue());
                }
                kafkaProducer.BloodSugarKafkaSend(entry.getValue());
                // Use key and value to adapt to scenarios where data is updated during processing
                bloodSugarCacheMap.getCatchMap().remove(entry.getKey(), entry.getValue());
            }
        }
    }

    private void registerDevices(BloodSugarOriginalData bloodSugarOriginalData) {
        DeviceInfoData deviceInfoData = new DeviceInfoData(bloodSugarOriginalData);
        try {
            deviceInfoMapper.registerDevice(deviceInfoData);
            registerDeviceCacheMap.put(deviceInfoData.getMac(), deviceInfoData.getMac());
        } catch (Exception e) {
            log.info("registerDevices error:{}", e.getMessage());
        }
    }


    private Boolean checkMacExist(String mac) {
        if (null == registerDeviceCacheMap.get(mac)) {
            return false;
        } else {
            return true;
        }
    }
}
