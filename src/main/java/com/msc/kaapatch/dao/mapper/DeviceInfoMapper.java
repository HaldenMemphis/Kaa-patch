package com.msc.kaapatch.dao.mapper;

import com.msc.kaapatch.dao.entity.DeviceInfoData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: Kaa-patch
 * @description:
 * @author: yfliu
 * @create: 2023-08-08 23:01
 **/
@Mapper

public interface DeviceInfoMapper {

    List<String> selectAllMac();

    int registerDevice(DeviceInfoData deviceInfoData);

}
