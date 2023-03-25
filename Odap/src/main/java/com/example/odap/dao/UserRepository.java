package com.example.odap.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.odap.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);

}
