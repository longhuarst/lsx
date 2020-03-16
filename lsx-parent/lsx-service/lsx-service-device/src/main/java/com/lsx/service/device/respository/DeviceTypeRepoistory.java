package com.lsx.service.device.respository;

import com.lsx.service.device.bean.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTypeRepoistory extends JpaRepository<DeviceType,String > {
}
