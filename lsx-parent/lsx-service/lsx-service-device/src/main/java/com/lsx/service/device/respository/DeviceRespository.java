package com.lsx.service.device.respository;

import com.lsx.service.device.bean.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface DeviceRespository extends JpaRepository<Device, String> {


    @Transactional //删除要加上
    void deleteByUuid(String uuid);

}
