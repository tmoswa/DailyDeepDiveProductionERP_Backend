package com.softpala.SalesDeepDive_ERP.persistence.dao;

import com.softpala.SalesDeepDive_ERP.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Override
    void delete(User user);

    @Override
    List<User> findAll();

    User findByUsername(String username);
}
