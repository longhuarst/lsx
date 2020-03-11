package com.lsx.service.device.respository;

import com.lsx.service.device.bean.DeviceMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceMessageRespository extends JpaRepository<DeviceMessage, String> {
}
