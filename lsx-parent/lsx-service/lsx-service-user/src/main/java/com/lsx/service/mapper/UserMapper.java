package com.lsx.service.mapper;


import com.lsx.service.entity.User;
import org.apache.ibatis.annotations.Mapper;

//@Mapper 在配置类增加 MapperScan("com.lsx.service.mapper") 可以自动扫描
public interface UserMapper {

    User findByUsername(String username);

}
