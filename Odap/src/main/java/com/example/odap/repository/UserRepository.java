package com.example.odap.repository;

import com.example.odap.entity.PictureData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.odap.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    Page<User> findAll(PageRequest pageRequest);
    User findByUserName(String userName);
    boolean existsByUserName(String userName);

}
