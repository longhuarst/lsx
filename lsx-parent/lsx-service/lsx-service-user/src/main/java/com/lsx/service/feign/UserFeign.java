package com.lsx.service.feign;

import com.lsx.service.entity.User;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.websocket.server.PathParam;


//服务接口定义
@FeignClient(name = "lsx-service-user")//name 要和 eureka中服务名字一样 否则 会有 异常 loadbalance
public interface UserFeign {

    @GetMapping("/user/load/{username}")
    public User findUserInfo(@PathVariable("username") String username);


    @PostMapping("/findByUsername/{username}")  // fegin 调用 传输 json 要用 post
    public Result<User> findByUsername(@PathVariable("username") String username);

}
