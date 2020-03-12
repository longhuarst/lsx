package com.lsx.service.chat.respository;

import com.lsx.service.chat.entity.FriendList;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FriendListRepository extends JpaRepository<FriendList, String> {



    //按用户名查找
    FriendList findByUsername(String username);



}
