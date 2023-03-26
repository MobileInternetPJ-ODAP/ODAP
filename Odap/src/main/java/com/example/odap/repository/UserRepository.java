package com.example.odap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.odap.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    boolean existsByUserName(String userName);

}
