package com.softpala.SalesDeepDive_ERP.persistence.dao;


import com.softpala.SalesDeepDive_ERP.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);


}
