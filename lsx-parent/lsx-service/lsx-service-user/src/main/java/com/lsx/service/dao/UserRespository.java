package com.lsx.service.dao;

import com.lsx.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<User, String> {

    User findByUsername(String username);


    User findByMail(String mail);



}
