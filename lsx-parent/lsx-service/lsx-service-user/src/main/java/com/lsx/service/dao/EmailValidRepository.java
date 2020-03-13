package com.lsx.service.dao;

import com.lsx.service.entity.EmailValid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailValidRepository extends JpaRepository<EmailValid,String> {
}
