package com.lsx.service.device.respository;

import com.lsx.service.device.bean.DeviceBinding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceBindingRepository extends JpaRepository<DeviceBinding, String> {
}
