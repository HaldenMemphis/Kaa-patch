package com.msc.kaapatch.dao.entity;

import lombok.Data;

/**
 * @program: Kaa-patch
 * @description:
 * @author: yfliu
 * @create: 2023-08-08 23:04
 **/
@Data
public class DeviceInfoData {

    private String mac;

    private String deviceType;

    private String displayName;

    private Boolean bindStatus;


    public DeviceInfoData(BloodSugarOriginalData bloodSugarOriginalData) {
        this.mac = bloodSugarOriginalData.getMac();
        this.deviceType = bloodSugarOriginalData.getDeviceType();
        this.displayName = bloodSugarOriginalData.getDisplayName();
        this.bindStatus = false;
    }
}
