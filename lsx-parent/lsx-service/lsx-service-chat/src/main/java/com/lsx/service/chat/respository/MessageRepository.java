package com.lsx.service.chat.respository;

import com.lsx.service.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message,String> {
}
