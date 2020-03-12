package com.lsx.service.chat.respository;

import com.lsx.service.chat.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<Notify, String> {
}
