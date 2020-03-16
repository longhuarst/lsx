package com.lsx.service.device.configure;



//用于检查数据库中de默认值

import com.lsx.service.device.bean.Device;
import com.lsx.service.device.bean.DeviceType;
import com.lsx.service.device.respository.DeviceTypeRepoistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class DefaultDeviceTypeConfig implements CommandLineRunner {


    @Autowired
    private DeviceTypeRepoistory deviceTypeRepoistory;


    Logger logger = LoggerFactory.getLogger(DefaultDeviceTypeConfig.class);



    @Override
    public void run(String... args) throws Exception {

        //检查数据库

        try {

            DeviceType example = new DeviceType();

            example.setType("温度传感器");

            Optional<DeviceType> dt = deviceTypeRepoistory.findOne(Example.of(example));
            if (!dt.isPresent()){
                DeviceType deviceType = new DeviceType();
                deviceType.setType("温度传感器");
                deviceType.setDataType("float");
                deviceType.setId(1);

                deviceTypeRepoistory.save(deviceType);
            }else{
                logger.info("传感器类型 温度 已存在");
            }




            //默认设备

            //1。 温度传感器      浮点
            //2。 位置传感器      json数据





        }catch (Exception e){
            e.printStackTrace();

            logger.info(e.getMessage());
        }


    }
}
