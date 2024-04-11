package com.projectsvadim.vadimbot.repository;

import com.projectsvadim.vadimbot.enity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findUserByChatId(Long chatId);

    User findUserByToken(String token);
}
